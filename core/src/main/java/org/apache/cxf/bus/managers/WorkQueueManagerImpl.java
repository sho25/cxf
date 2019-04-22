begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|managers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|buslifecycle
operator|.
name|BusLifeCycleListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|buslifecycle
operator|.
name|BusLifeCycleManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|ConfiguredBeanLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|InstrumentationManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|workqueue
operator|.
name|AutomaticWorkQueue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|workqueue
operator|.
name|AutomaticWorkQueueImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|WorkQueueManagerImpl
implements|implements
name|WorkQueueManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_QUEUE_NAME
init|=
literal|"default"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_WORKQUEUE_BEAN_NAME
init|=
literal|"cxf.default.workqueue"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WorkQueueManagerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|AutomaticWorkQueue
argument_list|>
name|namedQueues
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|4
argument_list|,
literal|0.75f
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|boolean
name|inShutdown
decl_stmt|;
name|InstrumentationManager
name|imanager
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WorkQueueManagerImpl
parameter_list|()
block|{      }
specifier|public
name|WorkQueueManagerImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|WorkQueueManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|imanager
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|imanager
condition|)
block|{
try|try
block|{
name|imanager
operator|.
name|register
argument_list|(
operator|new
name|WorkQueueManagerImplMBeanWrapper
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|jmex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|jmex
argument_list|)
expr_stmt|;
block|}
block|}
name|ConfiguredBeanLocator
name|locator
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|AutomaticWorkQueue
argument_list|>
name|q
init|=
name|locator
operator|.
name|getBeansOfType
argument_list|(
name|AutomaticWorkQueue
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|q
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AutomaticWorkQueue
name|awq
range|:
name|q
control|)
block|{
name|addNamedWorkQueue
argument_list|(
name|awq
operator|.
name|getName
argument_list|()
argument_list|,
name|awq
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|namedQueues
operator|.
name|containsKey
argument_list|(
name|DEFAULT_QUEUE_NAME
argument_list|)
condition|)
block|{
name|AutomaticWorkQueue
name|defaultQueue
init|=
name|locator
operator|.
name|getBeanOfType
argument_list|(
name|DEFAULT_WORKQUEUE_BEAN_NAME
argument_list|,
name|AutomaticWorkQueue
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultQueue
operator|!=
literal|null
condition|)
block|{
name|addNamedWorkQueue
argument_list|(
name|DEFAULT_QUEUE_NAME
argument_list|,
name|defaultQueue
argument_list|)
expr_stmt|;
block|}
block|}
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
operator|new
name|WQLifecycleListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|AutomaticWorkQueue
name|getAutomaticWorkQueue
parameter_list|()
block|{
name|AutomaticWorkQueue
name|defaultQueue
init|=
name|getNamedWorkQueue
argument_list|(
name|DEFAULT_QUEUE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultQueue
operator|==
literal|null
condition|)
block|{
name|defaultQueue
operator|=
name|createAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
return|return
name|defaultQueue
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|shutdown
parameter_list|(
name|boolean
name|processRemainingTasks
parameter_list|)
block|{
name|inShutdown
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|AutomaticWorkQueue
name|q
range|:
name|namedQueues
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|q
operator|instanceof
name|AutomaticWorkQueueImpl
condition|)
block|{
name|AutomaticWorkQueueImpl
name|impl
init|=
operator|(
name|AutomaticWorkQueueImpl
operator|)
name|q
decl_stmt|;
if|if
condition|(
name|impl
operator|.
name|isShared
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|impl
init|)
block|{
name|impl
operator|.
name|removeSharedUser
argument_list|()
expr_stmt|;
if|if
condition|(
name|impl
operator|.
name|getShareCount
argument_list|()
operator|==
literal|0
operator|&&
name|imanager
operator|!=
literal|null
operator|&&
name|imanager
operator|.
name|getMBeanServer
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|imanager
operator|.
name|unregister
argument_list|(
operator|new
name|WorkQueueImplMBeanWrapper
argument_list|(
name|impl
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|jmex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|jmex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|q
operator|.
name|shutdown
argument_list|(
name|processRemainingTasks
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|q
operator|.
name|shutdown
argument_list|(
name|processRemainingTasks
argument_list|)
expr_stmt|;
block|}
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
while|while
condition|(
operator|!
name|inShutdown
condition|)
block|{
try|try
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
for|for
control|(
name|AutomaticWorkQueue
name|q
range|:
name|namedQueues
operator|.
name|values
argument_list|()
control|)
block|{
while|while
condition|(
operator|!
name|q
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
try|try
block|{
name|wait
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
for|for
control|(
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
name|h
range|:
name|LOG
operator|.
name|getHandlers
argument_list|()
control|)
block|{
name|h
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|AutomaticWorkQueue
name|getNamedWorkQueue
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|namedQueues
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
specifier|final
name|void
name|addNamedWorkQueue
parameter_list|(
name|String
name|name
parameter_list|,
name|AutomaticWorkQueue
name|q
parameter_list|)
block|{
name|namedQueues
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|q
argument_list|)
expr_stmt|;
if|if
condition|(
name|q
operator|instanceof
name|AutomaticWorkQueueImpl
condition|)
block|{
name|AutomaticWorkQueueImpl
name|impl
init|=
operator|(
name|AutomaticWorkQueueImpl
operator|)
name|q
decl_stmt|;
if|if
condition|(
name|impl
operator|.
name|isShared
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|impl
init|)
block|{
if|if
condition|(
name|impl
operator|.
name|getShareCount
argument_list|()
operator|==
literal|0
operator|&&
name|imanager
operator|!=
literal|null
operator|&&
name|imanager
operator|.
name|getMBeanServer
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|imanager
operator|.
name|register
argument_list|(
operator|new
name|WorkQueueImplMBeanWrapper
argument_list|(
operator|(
name|AutomaticWorkQueueImpl
operator|)
name|q
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|jmex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|jmex
argument_list|)
expr_stmt|;
block|}
block|}
name|impl
operator|.
name|addSharedUser
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|imanager
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|imanager
operator|.
name|register
argument_list|(
operator|new
name|WorkQueueImplMBeanWrapper
argument_list|(
operator|(
name|AutomaticWorkQueueImpl
operator|)
name|q
argument_list|,
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|jmex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|jmex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|AutomaticWorkQueue
name|createAutomaticWorkQueue
parameter_list|()
block|{
name|AutomaticWorkQueue
name|q
init|=
operator|new
name|AutomaticWorkQueueImpl
argument_list|(
name|DEFAULT_QUEUE_NAME
argument_list|)
decl_stmt|;
name|addNamedWorkQueue
argument_list|(
name|DEFAULT_QUEUE_NAME
argument_list|,
name|q
argument_list|)
expr_stmt|;
return|return
name|q
return|;
block|}
class|class
name|WQLifecycleListener
implements|implements
name|BusLifeCycleListener
block|{
specifier|public
name|void
name|initComplete
parameter_list|()
block|{          }
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{         }
block|}
block|}
end_class

end_unit

