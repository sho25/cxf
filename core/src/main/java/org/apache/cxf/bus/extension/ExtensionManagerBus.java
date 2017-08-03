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
name|extension
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

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
name|Set
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
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
name|binding
operator|.
name|BindingFactoryManager
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
name|bus
operator|.
name|CXFBusFactory
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
name|bus
operator|.
name|managers
operator|.
name|BindingFactoryManagerImpl
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
name|bus
operator|.
name|managers
operator|.
name|ConduitInitiatorManagerImpl
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
name|bus
operator|.
name|managers
operator|.
name|DestinationFactoryManagerImpl
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
name|util
operator|.
name|SystemPropertyAction
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
name|configuration
operator|.
name|Configurer
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
name|NullConfigurer
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
name|Feature
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|resource
operator|.
name|DefaultResourceManager
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
name|resource
operator|.
name|ObjectTypeResolver
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
name|resource
operator|.
name|PropertiesResolver
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
name|resource
operator|.
name|ResourceManager
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
name|resource
operator|.
name|ResourceResolver
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
name|resource
operator|.
name|SinglePropertyResolver
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
name|transport
operator|.
name|ConduitInitiatorManager
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
name|transport
operator|.
name|DestinationFactoryManager
import|;
end_import

begin_comment
comment|/**  * This bus uses CXF's built in extension manager to load components  * (as opposed to using the Spring bus implementation). While this is faster  * to load it doesn't allow extensive configuration and customization like  * the Spring bus does.  */
end_comment

begin_class
specifier|public
class|class
name|ExtensionManagerBus
extends|extends
name|AbstractBasicInterceptorProvider
implements|implements
name|Bus
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BUS_PROPERTY_NAME
init|=
literal|"bus"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BUS_ID_PROPERTY_NAME
init|=
literal|"org.apache.cxf.bus.id"
decl_stmt|;
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
specifier|final
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|missingExtensions
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
name|Feature
argument_list|>
name|features
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|Feature
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
argument_list|(
literal|16
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ExtensionManagerImpl
name|extensionManager
decl_stmt|;
specifier|public
name|ExtensionManagerBus
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
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|,
name|ClassLoader
name|extensionClassLoader
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
argument_list|(
literal|16
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
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
name|this
operator|.
name|missingExtensions
operator|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
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
literal|null
operator|!=
name|props
condition|)
block|{
name|properties
operator|.
name|putAll
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
name|Configurer
name|configurer
init|=
operator|(
name|Configurer
operator|)
name|extensions
operator|.
name|get
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|configurer
condition|)
block|{
name|configurer
operator|=
operator|new
name|NullConfigurer
argument_list|()
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|Configurer
operator|.
name|class
argument_list|,
name|configurer
argument_list|)
expr_stmt|;
block|}
name|id
operator|=
name|getBusId
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|ResourceManager
name|resourceManager
init|=
operator|new
name|DefaultResourceManager
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|BUS_ID_PROPERTY_NAME
argument_list|,
name|BUS_PROPERTY_NAME
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|BUS_PROPERTY_NAME
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|DEFAULT_BUS_ID
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ResourceResolver
name|propertiesResolver
init|=
operator|new
name|PropertiesResolver
argument_list|(
name|properties
argument_list|)
decl_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
name|propertiesResolver
argument_list|)
expr_stmt|;
name|ResourceResolver
name|busResolver
init|=
operator|new
name|SinglePropertyResolver
argument_list|(
name|BUS_PROPERTY_NAME
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
name|busResolver
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|ObjectTypeResolver
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|busResolver
operator|=
operator|new
name|SinglePropertyResolver
argument_list|(
name|DEFAULT_BUS_ID
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
name|busResolver
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|ObjectTypeResolver
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|ResourceResolver
argument_list|()
block|{
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceType
parameter_list|)
block|{
if|if
condition|(
name|extensionManager
operator|!=
literal|null
condition|)
block|{
return|return
name|extensionManager
operator|.
name|getExtension
argument_list|(
name|resourceName
argument_list|,
name|resourceType
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|InputStream
name|getAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|,
name|resourceManager
argument_list|)
expr_stmt|;
name|extensionManager
operator|=
operator|new
name|ExtensionManagerImpl
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|extensionClassLoader
argument_list|,
name|extensions
argument_list|,
name|resourceManager
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|setState
argument_list|(
name|BusState
operator|.
name|INITIAL
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|this
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
condition|)
block|{
operator|new
name|DestinationFactoryManagerImpl
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|this
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
condition|)
block|{
operator|new
name|ConduitInitiatorManagerImpl
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|this
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
condition|)
block|{
operator|new
name|BindingFactoryManagerImpl
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|extensionManager
operator|.
name|load
argument_list|(
operator|new
name|String
index|[]
block|{
name|ExtensionManagerImpl
operator|.
name|BUS_EXTENSION_RESOURCE
block|}
argument_list|)
expr_stmt|;
name|extensionManager
operator|.
name|activateAllByType
argument_list|(
name|ResourceResolver
operator|.
name|class
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|ExtensionManager
operator|.
name|class
argument_list|,
name|extensionManager
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ExtensionManagerBus
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
name|e
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
argument_list|(
name|e
argument_list|,
name|properties
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ExtensionManagerBus
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
name|e
parameter_list|)
block|{
name|this
argument_list|(
name|e
argument_list|,
literal|null
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ExtensionManagerBus
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|final
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
if|if
condition|(
name|missingExtensions
operator|.
name|contains
argument_list|(
name|extensionType
argument_list|)
condition|)
block|{
comment|//already know we cannot find it
return|return
literal|null
return|;
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
name|obj
operator|=
name|loc
operator|.
name|getBeanOfType
argument_list|(
name|extensionType
operator|.
name|getName
argument_list|()
argument_list|,
name|extensionType
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|extensions
operator|.
name|put
argument_list|(
name|extensionType
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
else|else
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
operator|&&
name|objs
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|extensions
operator|.
name|put
argument_list|(
name|extensionType
argument_list|,
name|objs
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
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
comment|//record that it couldn't be found to avoid expensive searches again in the future
name|missingExtensions
operator|.
name|add
argument_list|(
name|extensionType
argument_list|)
expr_stmt|;
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
specifier|final
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
name|extensionManager
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
specifier|final
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
if|if
condition|(
name|extension
operator|==
literal|null
condition|)
block|{
name|extensions
operator|.
name|remove
argument_list|(
name|extensionType
argument_list|)
expr_stmt|;
name|missingExtensions
operator|.
name|add
argument_list|(
name|extensionType
argument_list|)
expr_stmt|;
block|}
else|else
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
name|missingExtensions
operator|.
name|remove
argument_list|(
name|extensionType
argument_list|)
expr_stmt|;
block|}
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
name|Math
operator|.
name|abs
argument_list|(
name|this
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
else|:
name|id
return|;
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
name|extensionManager
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|initializeFeatures
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|loadAdditionalFeatures
parameter_list|()
block|{      }
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
name|Feature
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
block|{
name|extensionManager
operator|.
name|destroyBeans
argument_list|()
expr_stmt|;
block|}
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
name|Feature
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
name|?
extends|extends
name|Feature
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
specifier|private
specifier|static
name|String
name|getBusId
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|String
name|busId
init|=
literal|null
decl_stmt|;
comment|// first check properties
if|if
condition|(
literal|null
operator|!=
name|properties
condition|)
block|{
name|busId
operator|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|BUS_ID_PROPERTY_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|busId
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|busId
argument_list|)
condition|)
block|{
return|return
name|busId
return|;
block|}
block|}
comment|// next check system properties
name|busId
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|BUS_ID_PROPERTY_NAME
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|busId
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|busId
argument_list|)
condition|)
block|{
return|return
name|busId
return|;
block|}
comment|// otherwise use null so the default will be used
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

