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
name|workqueue
package|;
end_package

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
name|PostConstruct
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
name|management
operator|.
name|InstrumentationManager
import|;
end_import

begin_class
specifier|public
class|class
name|WorkQueueManagerImpl
implements|implements
name|WorkQueueManager
block|{
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
name|ThreadingModel
name|threadingModel
init|=
name|ThreadingModel
operator|.
name|MULTI_THREADED
decl_stmt|;
name|AutomaticWorkQueue
name|autoQueue
decl_stmt|;
name|boolean
name|inShutdown
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
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
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|register
parameter_list|()
block|{
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
block|}
block|}
specifier|public
specifier|synchronized
name|AutomaticWorkQueue
name|getAutomaticWorkQueue
parameter_list|()
block|{
if|if
condition|(
name|autoQueue
operator|==
literal|null
condition|)
block|{
name|autoQueue
operator|=
name|createAutomaticWorkQueue
argument_list|()
expr_stmt|;
name|InstrumentationManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|manager
condition|)
block|{
try|try
block|{
name|manager
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
block|}
return|return
name|autoQueue
return|;
block|}
specifier|public
name|ThreadingModel
name|getThreadingModel
parameter_list|()
block|{
return|return
name|threadingModel
return|;
block|}
specifier|public
name|void
name|setThreadingModel
parameter_list|(
name|ThreadingModel
name|model
parameter_list|)
block|{
name|threadingModel
operator|=
name|model
expr_stmt|;
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
if|if
condition|(
name|autoQueue
operator|!=
literal|null
condition|)
block|{
name|autoQueue
operator|.
name|shutdown
argument_list|(
name|processRemainingTasks
argument_list|)
expr_stmt|;
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
while|while
condition|(
name|autoQueue
operator|!=
literal|null
operator|&&
operator|!
name|autoQueue
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
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
specifier|private
name|AutomaticWorkQueue
name|createAutomaticWorkQueue
parameter_list|()
block|{
comment|// Configuration configuration = bus.getConfiguration();
comment|// configuration.getInteger("threadpool:initial_threads");
name|int
name|initialThreads
init|=
literal|1
decl_stmt|;
comment|// int lwm = configuration.getInteger("threadpool:low_water_mark");
name|int
name|lwm
init|=
literal|5
decl_stmt|;
comment|// int hwm = configuration.getInteger("threadpool:high_water_mark");
name|int
name|hwm
init|=
literal|25
decl_stmt|;
comment|// configuration.getInteger("threadpool:max_queue_size");
name|int
name|maxQueueSize
init|=
literal|10
operator|*
name|hwm
decl_stmt|;
comment|// configuration.getInteger("threadpool:dequeue_timeout");
name|long
name|dequeueTimeout
init|=
literal|2
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
return|return
operator|new
name|AutomaticWorkQueueImpl
argument_list|(
name|maxQueueSize
argument_list|,
name|initialThreads
argument_list|,
name|hwm
argument_list|,
name|lwm
argument_list|,
name|dequeueTimeout
argument_list|)
return|;
block|}
block|}
end_class

end_unit

