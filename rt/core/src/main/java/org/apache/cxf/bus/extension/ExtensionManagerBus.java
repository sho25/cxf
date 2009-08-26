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
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|BindingFactory
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
name|binding
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
name|BusState
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
name|CXFBusImpl
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
name|ConduitInitiator
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
name|transport
operator|.
name|DestinationFactory
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
name|DestinationFactoryManagerImpl
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
name|CXFBusImpl
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
specifier|public
name|ExtensionManagerBus
parameter_list|(
name|Map
argument_list|<
name|Class
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
name|super
argument_list|(
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|properties
condition|)
block|{
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
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
name|setId
argument_list|(
name|getBusId
argument_list|(
name|properties
argument_list|)
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
name|ExtensionManagerImpl
name|em
init|=
operator|new
name|ExtensionManagerImpl
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|,
name|extensions
argument_list|,
name|resourceManager
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|setState
argument_list|(
name|BusState
operator|.
name|INITIAL
argument_list|)
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
name|DestinationFactoryManager
name|dfm
init|=
name|this
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|dfm
condition|)
block|{
name|dfm
operator|=
operator|new
name|DestinationFactoryManagerImpl
argument_list|(
operator|new
name|DeferredMap
argument_list|<
name|DestinationFactory
argument_list|>
argument_list|(
name|em
argument_list|,
name|DestinationFactory
operator|.
name|class
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|ConduitInitiatorManager
name|cfm
init|=
name|this
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|cfm
condition|)
block|{
name|cfm
operator|=
operator|new
name|ConduitInitiatorManagerImpl
argument_list|(
operator|new
name|DeferredMap
argument_list|<
name|ConduitInitiator
argument_list|>
argument_list|(
name|em
argument_list|,
name|ConduitInitiator
operator|.
name|class
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|BindingFactoryManager
name|bfm
init|=
name|this
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|bfm
condition|)
block|{
name|bfm
operator|=
operator|new
name|BindingFactoryManagerImpl
argument_list|(
operator|new
name|DeferredMap
argument_list|<
name|BindingFactory
argument_list|>
argument_list|(
name|em
argument_list|,
name|BindingFactory
operator|.
name|class
argument_list|)
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|,
name|bfm
argument_list|)
expr_stmt|;
block|}
name|em
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
block|,
name|ExtensionManagerImpl
operator|.
name|BUS_EXTENSION_RESOURCE_COMPAT
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|setExtension
argument_list|(
name|em
argument_list|,
name|ExtensionManager
operator|.
name|class
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
argument_list|)
expr_stmt|;
block|}
specifier|private
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
name|System
operator|.
name|getProperty
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
comment|// otherwise use default
return|return
name|DEFAULT_BUS_ID
return|;
block|}
block|}
end_class

end_unit

