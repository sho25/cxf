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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusException
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
name|BindingConfiguration
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
name|endpoint
operator|.
name|AbstractEndpointFactory
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
name|endpoint
operator|.
name|Endpoint
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
name|endpoint
operator|.
name|EndpointException
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
name|endpoint
operator|.
name|EndpointImpl
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
name|endpoint
operator|.
name|Server
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
name|endpoint
operator|.
name|ServerImpl
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
name|jaxrs
operator|.
name|impl
operator|.
name|RequestPreprocessor
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
name|jaxrs
operator|.
name|lifecycle
operator|.
name|ResourceProvider
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
name|jaxrs
operator|.
name|provider
operator|.
name|ProviderFactory
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|factory
operator|.
name|ServiceConstructionException
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
name|service
operator|.
name|invoker
operator|.
name|Invoker
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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

begin_comment
comment|/**  * Bean to help easily create Server endpoints for JAX-RS. Example:  *<pre>  * JAXRSServerFactoryBean sf = JAXRSServerFactoryBean();  * sf.setResourceClasses(Book.class);  * sf.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);  * sf.setAddress("http://localhost:9080/");  * sf.create();  *</pre>  * This will start a server for you and register it with the ServerManager.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSServerFactoryBean
extends|extends
name|AbstractEndpointFactory
block|{
specifier|protected
name|boolean
name|doInit
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
name|Invoker
name|invoker
decl_stmt|;
specifier|private
name|boolean
name|start
init|=
literal|true
decl_stmt|;
specifier|private
name|JAXRSServiceFactoryBean
name|serviceFactory
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|serviceBeans
decl_stmt|;
specifier|private
name|List
argument_list|<
name|?
argument_list|>
name|entityProviders
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|languageMappings
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extensionMappings
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
specifier|public
name|JAXRSServerFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXRSServerFactoryBean
parameter_list|(
name|JAXRSServiceFactoryBean
name|sf
parameter_list|)
block|{
name|this
operator|.
name|serviceFactory
operator|=
name|sf
expr_stmt|;
name|doInit
operator|=
literal|true
expr_stmt|;
name|setBindingId
argument_list|(
name|JAXRSBindingFactory
operator|.
name|JAXRS_BINDING_ID
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schemas
parameter_list|)
block|{
name|this
operator|.
name|schemaLocations
operator|=
name|schemas
expr_stmt|;
block|}
specifier|public
name|void
name|setStaticSubresourceResolution
parameter_list|(
name|boolean
name|enableStatic
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setEnableStaticResolution
argument_list|(
name|enableStatic
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Server
name|create
parameter_list|()
block|{
try|try
block|{
name|Endpoint
name|ep
init|=
name|createEndpoint
argument_list|()
decl_stmt|;
name|server
operator|=
operator|new
name|ServerImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|ep
argument_list|,
name|getDestinationFactory
argument_list|()
argument_list|,
name|getBindingFactory
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|invoker
operator|==
literal|null
condition|)
block|{
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|createInvoker
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entityProviders
operator|!=
literal|null
condition|)
block|{
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|getAddress
argument_list|()
argument_list|)
operator|.
name|setUserProviders
argument_list|(
name|entityProviders
argument_list|)
expr_stmt|;
block|}
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|getAddress
argument_list|()
argument_list|)
operator|.
name|setRequestPreprocessor
argument_list|(
operator|new
name|RequestPreprocessor
argument_list|(
name|languageMappings
argument_list|,
name|extensionMappings
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|schemaLocations
operator|!=
literal|null
condition|)
block|{
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|getAddress
argument_list|()
argument_list|)
operator|.
name|setSchemaLocations
argument_list|(
name|schemaLocations
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|start
condition|)
block|{
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|EndpointException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|BusException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|applyFeatures
argument_list|()
expr_stmt|;
return|return
name|server
return|;
block|}
specifier|protected
name|void
name|applyFeatures
parameter_list|()
block|{
if|if
condition|(
name|getFeatures
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AbstractFeature
name|feature
range|:
name|getFeatures
argument_list|()
control|)
block|{
name|feature
operator|.
name|initialize
argument_list|(
name|server
argument_list|,
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Invoker
name|createInvoker
parameter_list|()
block|{
if|if
condition|(
name|serviceBeans
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|JAXRSInvoker
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|JAXRSInvoker
argument_list|(
name|serviceBeans
argument_list|)
return|;
block|}
block|}
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|()
throws|throws
name|BusException
throws|,
name|EndpointException
block|{
name|Service
name|service
init|=
name|serviceFactory
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
name|serviceFactory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
name|EndpointInfo
name|ei
init|=
name|createEndpointInfo
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|getServiceFactory
argument_list|()
operator|.
name|getService
argument_list|()
argument_list|,
name|ei
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getInInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getOutInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getInFaultInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getInFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getOutFaultInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ep
return|;
block|}
comment|/*      * EndpointInfo contains information form WSDL's physical part such as      * endpoint address, binding, transport etc. For JAX-RS based EndpointInfo,      * as there is no WSDL, these information are set manually, eg, default      * transport is http, binding is JAX-RS binding, endpoint address is from      * server mainline.      */
specifier|protected
name|EndpointInfo
name|createEndpointInfo
parameter_list|()
throws|throws
name|BusException
block|{
name|String
name|transportId
init|=
name|getTransportId
argument_list|()
decl_stmt|;
if|if
condition|(
name|transportId
operator|==
literal|null
operator|&&
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DestinationFactory
name|df
init|=
name|getDestinationFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|df
operator|==
literal|null
condition|)
block|{
name|DestinationFactoryManager
name|dfm
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|df
operator|=
name|dfm
operator|.
name|getDestinationFactoryForUri
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|df
operator|!=
literal|null
condition|)
block|{
name|transportId
operator|=
name|df
operator|.
name|getTransportIds
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|//default to http transport
if|if
condition|(
name|transportId
operator|==
literal|null
condition|)
block|{
name|transportId
operator|=
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
expr_stmt|;
block|}
name|setTransportId
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setTransportId
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setName
argument_list|(
name|serviceFactory
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ei
operator|.
name|setAddress
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|BindingInfo
name|bindingInfo
init|=
name|createBindingInfo
argument_list|()
decl_stmt|;
name|ei
operator|.
name|setBinding
argument_list|(
name|bindingInfo
argument_list|)
expr_stmt|;
return|return
name|ei
return|;
block|}
specifier|protected
name|BindingInfo
name|createBindingInfo
parameter_list|()
block|{
name|BindingFactoryManager
name|mgr
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|binding
init|=
name|getBindingId
argument_list|()
decl_stmt|;
name|BindingConfiguration
name|bindingConfig
init|=
name|getBindingConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
operator|&&
name|bindingConfig
operator|!=
literal|null
condition|)
block|{
name|binding
operator|=
name|bindingConfig
operator|.
name|getBindingId
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
name|JAXRSBindingFactory
operator|.
name|JAXRS_BINDING_ID
expr_stmt|;
block|}
try|try
block|{
name|BindingFactory
name|bindingFactory
init|=
name|mgr
operator|.
name|getBindingFactory
argument_list|(
name|binding
argument_list|)
decl_stmt|;
name|setBindingFactory
argument_list|(
name|bindingFactory
argument_list|)
expr_stmt|;
return|return
name|bindingFactory
operator|.
name|createBindingInfo
argument_list|(
name|serviceFactory
operator|.
name|getService
argument_list|()
argument_list|,
name|binding
argument_list|,
name|bindingConfig
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
comment|//do nothing
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setLanguageMappings
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|lMaps
parameter_list|)
block|{
name|languageMappings
operator|=
name|lMaps
expr_stmt|;
block|}
specifier|public
name|void
name|setExtensionMappings
parameter_list|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|extMaps
parameter_list|)
block|{
name|extensionMappings
operator|=
name|extMaps
expr_stmt|;
block|}
specifier|public
name|JAXRSServiceFactoryBean
name|getServiceFactory
parameter_list|()
block|{
return|return
name|serviceFactory
return|;
block|}
specifier|public
name|void
name|setServiceFactory
parameter_list|(
name|JAXRSServiceFactoryBean
name|serviceFactory
parameter_list|)
block|{
name|this
operator|.
name|serviceFactory
operator|=
name|serviceFactory
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Class
argument_list|>
name|getResourceClasses
parameter_list|()
block|{
return|return
name|serviceFactory
operator|.
name|getResourceClasses
argument_list|()
return|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|>
name|classes
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClasses
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceClasses
parameter_list|(
name|Class
modifier|...
name|classes
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceClasses
argument_list|(
name|classes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the backing service bean. If this is set, JAX-RS runtime will not be      * responsible for the lifecycle of resource classes.      *       * @return      */
specifier|public
name|void
name|setServiceBeans
parameter_list|(
name|Object
modifier|...
name|beans
parameter_list|)
block|{
name|setServiceBeans
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|beans
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceBeans
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|beans
parameter_list|)
block|{
name|this
operator|.
name|serviceBeans
operator|=
name|beans
expr_stmt|;
name|serviceFactory
operator|.
name|setResourceClassesFromBeans
argument_list|(
name|beans
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setResourceProvider
parameter_list|(
name|Class
name|c
parameter_list|,
name|ResourceProvider
name|rp
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setResourceProvider
argument_list|(
name|c
argument_list|,
name|rp
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the entityProviders      */
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|getProviders
parameter_list|()
block|{
return|return
name|entityProviders
return|;
block|}
comment|/**      * @param entityProviders the entityProviders to set      */
specifier|public
name|void
name|setProviders
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|providers
parameter_list|)
block|{
name|this
operator|.
name|entityProviders
operator|=
name|providers
expr_stmt|;
block|}
block|}
end_class

end_unit

