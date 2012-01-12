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
name|List
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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|BusFactory
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
name|BusCreationListener
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
name|feature
operator|.
name|AbstractFeature
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
name|feature
operator|.
name|LoggingFeature
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
name|interceptor
operator|.
name|AbstractBasicInterceptorProvider
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|CXFBusImpl
extends|extends
name|AbstractBasicInterceptorProvider
implements|implements
name|Bus
block|{
specifier|static
specifier|final
name|boolean
name|FORCE_LOGGING
decl_stmt|;
static|static
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
try|try
block|{
name|b
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"org.apache.cxf.logging.enabled"
argument_list|)
expr_stmt|;
comment|//treat these all the same
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.transport.local.LocalTransportPipe.dump"
argument_list|)
expr_stmt|;
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.util.pipe.StandaloneTubeAssembler.dump"
argument_list|)
expr_stmt|;
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump"
argument_list|)
expr_stmt|;
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.transport.http.HttpAdapter.dump"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
name|FORCE_LOGGING
operator|=
name|b
expr_stmt|;
block|}
specifier|protected
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|extensions
decl_stmt|;
specifier|protected
name|String
name|id
decl_stmt|;
specifier|private
name|BusState
name|state
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|features
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|AbstractFeature
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CXFBusImpl
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CXFBusImpl
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|extensions
parameter_list|)
block|{
if|if
condition|(
name|extensions
operator|==
literal|null
condition|)
block|{
name|extensions
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|extensions
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|(
name|extensions
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|extensions
operator|=
name|extensions
expr_stmt|;
name|state
operator|=
name|BusState
operator|.
name|INITIAL
expr_stmt|;
name|CXFBusFactory
operator|.
name|possiblySetDefaultBus
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|FORCE_LOGGING
condition|)
block|{
name|features
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setState
parameter_list|(
name|BusState
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|i
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
block|}
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|getExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
block|{
name|Object
name|obj
init|=
name|extensions
operator|.
name|get
argument_list|(
name|extensionType
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
name|ConfiguredBeanLocator
name|loc
init|=
operator|(
name|ConfiguredBeanLocator
operator|)
name|extensions
operator|.
name|get
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loc
operator|==
literal|null
condition|)
block|{
name|loc
operator|=
name|createConfiguredBeanLocator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loc
operator|!=
literal|null
condition|)
block|{
comment|//force loading
name|Collection
argument_list|<
name|?
argument_list|>
name|objs
init|=
name|loc
operator|.
name|getBeansOfType
argument_list|(
name|extensionType
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|objs
control|)
block|{
name|extensions
operator|.
name|put
argument_list|(
name|extensionType
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
name|obj
operator|=
name|extensions
operator|.
name|get
argument_list|(
name|extensionType
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|obj
condition|)
block|{
return|return
name|extensionType
operator|.
name|cast
argument_list|(
name|obj
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|hasExtensionByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|extensions
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|ConfiguredBeanLocator
name|loc
init|=
operator|(
name|ConfiguredBeanLocator
operator|)
name|extensions
operator|.
name|get
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loc
operator|==
literal|null
condition|)
block|{
name|loc
operator|=
name|createConfiguredBeanLocator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loc
operator|!=
literal|null
condition|)
block|{
return|return
name|loc
operator|.
name|hasBeanOfName
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
specifier|synchronized
name|ConfiguredBeanLocator
name|createConfiguredBeanLocator
parameter_list|()
block|{
name|ConfiguredBeanLocator
name|loc
init|=
operator|(
name|ConfiguredBeanLocator
operator|)
name|extensions
operator|.
name|get
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loc
operator|==
literal|null
condition|)
block|{
name|loc
operator|=
operator|new
name|ConfiguredBeanLocator
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBeanNamesOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|getBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|boolean
name|loadBeansOfType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|BeanLoaderListener
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|hasConfiguredPropertyValue
parameter_list|(
name|String
name|beanName
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBeanOfType
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|hasBeanOfName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
expr_stmt|;
name|this
operator|.
name|setExtension
argument_list|(
name|loc
argument_list|,
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|loc
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setExtension
parameter_list|(
name|T
name|extension
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|extensionType
parameter_list|)
block|{
name|extensions
operator|.
name|put
argument_list|(
name|extensionType
argument_list|,
name|extension
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|null
operator|==
name|id
condition|?
name|DEFAULT_BUS_ID
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|this
operator|.
name|hashCode
argument_list|()
argument_list|)
else|:
name|id
return|;
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
name|state
operator|=
name|BusState
operator|.
name|RUNNING
expr_stmt|;
while|while
condition|(
name|state
operator|==
name|BusState
operator|.
name|RUNNING
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
comment|// ignore;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|setState
argument_list|(
name|BusState
operator|.
name|INITIALIZING
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|BusCreationListener
argument_list|>
name|ls
init|=
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
operator|.
name|getBeansOfType
argument_list|(
name|BusCreationListener
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|BusCreationListener
name|l
range|:
name|ls
control|)
block|{
name|l
operator|.
name|busCreated
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|doInitializeInternal
argument_list|()
expr_stmt|;
name|BusLifeCycleManager
name|lifeCycleManager
init|=
name|this
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lifeCycleManager
condition|)
block|{
name|lifeCycleManager
operator|.
name|initComplete
argument_list|()
expr_stmt|;
block|}
name|setState
argument_list|(
name|BusState
operator|.
name|RUNNING
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doInitializeInternal
parameter_list|()
block|{
name|initializeFeatures
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|loadAdditionalFeatures
parameter_list|()
block|{              }
specifier|protected
name|void
name|initializeFeatures
parameter_list|()
block|{
name|loadAdditionalFeatures
argument_list|()
expr_stmt|;
if|if
condition|(
name|features
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractFeature
name|f
range|:
name|features
control|)
block|{
name|f
operator|.
name|initialize
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|destroyBeans
parameter_list|()
block|{              }
specifier|public
name|void
name|shutdown
parameter_list|(
name|boolean
name|wait
parameter_list|)
block|{
if|if
condition|(
name|state
operator|==
name|BusState
operator|.
name|SHUTTING_DOWN
condition|)
block|{
return|return;
block|}
name|BusLifeCycleManager
name|lifeCycleManager
init|=
name|this
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lifeCycleManager
condition|)
block|{
name|lifeCycleManager
operator|.
name|preShutdown
argument_list|()
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|state
operator|=
name|BusState
operator|.
name|SHUTTING_DOWN
expr_stmt|;
block|}
name|destroyBeans
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|state
operator|=
name|BusState
operator|.
name|SHUTDOWN
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|lifeCycleManager
condition|)
block|{
name|lifeCycleManager
operator|.
name|postShutdown
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
operator|==
name|this
condition|)
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|BusFactory
operator|.
name|clearDefaultBusForAnyThread
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BusState
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|features
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|setFeatures
parameter_list|(
name|Collection
argument_list|<
name|AbstractFeature
argument_list|>
name|features
parameter_list|)
block|{
name|this
operator|.
name|features
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|features
operator|.
name|addAll
argument_list|(
name|features
argument_list|)
expr_stmt|;
if|if
condition|(
name|FORCE_LOGGING
condition|)
block|{
name|this
operator|.
name|features
operator|.
name|add
argument_list|(
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|==
name|BusState
operator|.
name|RUNNING
condition|)
block|{
name|initializeFeatures
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
interface|interface
name|ExtensionFinder
block|{
parameter_list|<
name|T
parameter_list|>
name|T
name|findExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
function_decl|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
name|properties
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|properties
operator|.
name|remove
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

