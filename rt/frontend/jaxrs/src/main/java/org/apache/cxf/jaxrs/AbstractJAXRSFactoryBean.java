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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|ResourceBundle
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|databinding
operator|.
name|DataBinding
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
name|jaxrs
operator|.
name|model
operator|.
name|ClassResourceInfo
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
name|model
operator|.
name|UserResource
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
name|DataBindingProvider
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
name|jaxrs
operator|.
name|utils
operator|.
name|InjectionUtils
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
name|utils
operator|.
name|ResourceUtils
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
name|FactoryBeanListener
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
name|BindingOperationInfo
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
comment|/**  * Abstract bean holding functionality common for creating   * JAX-RS Server and Client objects.  */
end_comment

begin_class
specifier|public
class|class
name|AbstractJAXRSFactoryBean
extends|extends
name|AbstractEndpointFactory
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
name|AbstractJAXRSFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|AbstractJAXRSFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
specifier|protected
name|JAXRSServiceFactoryBean
name|serviceFactory
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|entityProviders
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|AbstractJAXRSFactoryBean
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
name|setBindingId
argument_list|(
name|JAXRSBindingFactory
operator|.
name|JAXRS_BINDING_ID
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
name|Bus
name|b
init|=
name|super
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|checkBindingFactory
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setServiceName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|super
operator|.
name|setServiceName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkBindingFactory
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|BindingFactoryManager
name|bfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|bfm
operator|.
name|getBindingFactory
argument_list|(
name|JAXRSBindingFactory
operator|.
name|JAXRS_BINDING_ID
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|b
parameter_list|)
block|{
comment|//not registered, let's register one
name|bfm
operator|.
name|registerBindingFactory
argument_list|(
name|JAXRSBindingFactory
operator|.
name|JAXRS_BINDING_ID
argument_list|,
operator|new
name|JAXRSBindingFactory
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|super
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|checkBindingFactory
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
comment|/*      * EndpointInfo contains information form WSDL's physical part such as      * endpoint address, binding, transport etc. For JAX-RS based EndpointInfo,      * as there is no WSDL, these information are set manually, eg, default      * transport is http, binding is JAX-RS binding, endpoint address is from      * server mainline.      */
specifier|protected
name|EndpointInfo
name|createEndpointInfo
parameter_list|(
name|Service
name|service
parameter_list|)
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
name|super
operator|.
name|setDestinationFactory
argument_list|(
name|df
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
literal|"http://cxf.apache.org/transports/http"
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
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|publishedEndpointUrl
argument_list|)
condition|)
block|{
name|ei
operator|.
name|setProperty
argument_list|(
literal|"publishedEndpointUrl"
argument_list|,
name|publishedEndpointUrl
argument_list|)
expr_stmt|;
block|}
name|ei
operator|.
name|setName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|ENDPOINTINFO_CREATED
argument_list|,
name|ei
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
name|BindingInfo
name|bi
init|=
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
decl_stmt|;
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|bi
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|BINDING_OPERATION_CREATED
argument_list|,
name|bi
argument_list|,
name|boi
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|BINDING_CREATED
argument_list|,
name|bi
argument_list|)
expr_stmt|;
return|return
name|bi
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
comment|/**      * Returns the service factory      * @return the factory      */
specifier|public
name|JAXRSServiceFactoryBean
name|getServiceFactory
parameter_list|()
block|{
return|return
name|serviceFactory
return|;
block|}
comment|/**      * Sets the custom service factory which processes       * the registered classes and providers       * @param serviceFactory the factory      */
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
argument_list|(
name|service
argument_list|)
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
name|service
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
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|list
init|=
name|serviceFactory
operator|.
name|getRealClassResourceInfo
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassResourceInfo
name|cri
range|:
name|list
control|)
block|{
name|initializeAnnotationInterceptors
argument_list|(
name|ep
argument_list|,
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|sendEvent
argument_list|(
name|FactoryBeanListener
operator|.
name|Event
operator|.
name|ENDPOINT_SELECTED
argument_list|,
name|ei
argument_list|,
name|ep
argument_list|,
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|ep
return|;
block|}
comment|/**      * Sets the location of the schema which can be used to validate      * the incoming XML or JAXB-driven JSON. JAX-RS MessageBodyReader implementations      * which have the setSchemaLocations method accepting a list of schema locations       * will be injected with this value.      *       * @param schema the schema location      */
specifier|public
name|void
name|setSchemaLocation
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|setSchemaLocations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|schema
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the locations of the schemas which can be used to validate      * the incoming XML or JAXB-driven JSON. JAX-RS MessageBodyReader implementations      * which have the setSchemaLocations method accepting a list of schema locations       * will be injected with this value.      *       * For example, if A.xsd imports B.xsd then both A.xsd and B.xsd need to be referenced.      *       * @param schema the schema locations      */
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
comment|/**      * @return the list of custom JAX-RS providers      */
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
comment|/**      * Add custom JAX-RS providers to the list of providers      *       * @param entityProviders the entityProviders      */
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
operator|.
name|addAll
argument_list|(
name|providers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add custom JAX-RS provider to the list of providers      *       * @param provider the custom provider.      */
specifier|public
name|void
name|setProvider
parameter_list|(
name|Object
name|provider
parameter_list|)
block|{
name|entityProviders
operator|.
name|add
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|checkResources
parameter_list|(
name|boolean
name|server
parameter_list|)
block|{
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|list
init|=
name|serviceFactory
operator|.
name|getRealClassResourceInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ClassResourceInfo
argument_list|>
name|it
init|=
name|list
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ClassResourceInfo
name|cri
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|cri
operator|.
name|isCreatedFromModel
argument_list|()
operator|&&
name|cri
operator|.
name|getServiceClass
argument_list|()
operator|==
name|cri
operator|.
name|getResourceClass
argument_list|()
operator|&&
operator|!
name|InjectionUtils
operator|.
name|isConcreteClass
argument_list|(
name|cri
operator|.
name|getServiceClass
argument_list|()
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|msg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_RESOURCES_AVAILABLE"
argument_list|,
name|BUNDLE
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|setupFactory
parameter_list|(
name|ProviderFactory
name|factory
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
block|{
if|if
condition|(
name|entityProviders
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setUserProviders
argument_list|(
name|entityProviders
argument_list|)
expr_stmt|;
block|}
name|setDataBindingProvider
argument_list|(
name|factory
argument_list|,
name|ep
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|initProviders
argument_list|(
name|serviceFactory
operator|.
name|getRealClassResourceInfo
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|schemaLocations
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setSchemaLocations
argument_list|(
name|schemaLocations
argument_list|)
expr_stmt|;
block|}
name|ep
operator|.
name|put
argument_list|(
name|factory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setDataBindingProvider
parameter_list|(
name|ProviderFactory
name|factory
parameter_list|,
name|Service
name|s
parameter_list|)
block|{
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|cris
init|=
name|serviceFactory
operator|.
name|getRealClassResourceInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|getDataBinding
argument_list|()
operator|==
literal|null
operator|&&
name|cris
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|DataBinding
name|ann
init|=
name|cris
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|DataBinding
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ann
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|setDataBinding
argument_list|(
name|ann
operator|.
name|value
argument_list|()
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"DataBinding "
operator|+
name|ann
operator|.
name|value
argument_list|()
operator|+
literal|" can not be loaded"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|DataBinding
name|db
init|=
name|getDataBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|s
operator|instanceof
name|JAXRSServiceImpl
condition|)
block|{
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|s
operator|)
operator|.
name|setCreateServiceModel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|initialize
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setUserProviders
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|DataBindingProvider
argument_list|<
name|Object
argument_list|>
argument_list|(
name|db
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the description of root resources.      * Can be used to 'attach' the JAX-RS like description to the application      * classes without adding JAX-RS annotations.      *         * @param resources root resource descriptions       */
specifier|public
name|void
name|setModelBeans
parameter_list|(
name|UserResource
modifier|...
name|resources
parameter_list|)
block|{
name|setModelBeans
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|resources
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the description of root resources.      * Can be used to 'attach' the JAX-RS like description to the application      * classes without adding JAX-RS annotations.      *         * @param resources root resource descriptions       */
specifier|public
name|void
name|setModelBeans
parameter_list|(
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setUserResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the description of root resources with the list of concrete classes.      * Can be used to 'attach' the JAX-RS like description to the application      * classes without adding JAX-RS annotations. Some models may only reference      * interfaces, thus providing a list of concrete classes that will be      * instantiated is required in such cases.      *         * @param resources root resource descriptions.      * @param sClasses concrete root resource classes      */
specifier|public
name|void
name|setModelBeansWithServiceClass
parameter_list|(
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|sClasses
parameter_list|)
block|{
name|serviceFactory
operator|.
name|setUserResourcesWithServiceClass
argument_list|(
name|resources
argument_list|,
name|sClasses
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets a reference to the external user model,       * Example: "classpath:/model/resources.xml"      *       * @param modelRef the reference to the external model resource.      */
specifier|public
name|void
name|setModelRef
parameter_list|(
name|String
name|modelRef
parameter_list|)
block|{
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
init|=
name|ResourceUtils
operator|.
name|getUserResources
argument_list|(
name|modelRef
argument_list|,
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|serviceFactory
operator|.
name|setUserResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets a reference to the external user model,       * Example: "classpath:/model/resources.xml".      * Some models may only reference interfaces, thus providing a list of       * concrete classes that will be instantiated is required in such cases.      *       * @param modelRef the reference to the external model resource.      * @param sClasses concrete root resource classes      */
specifier|public
name|void
name|setModelRefWithServiceClass
parameter_list|(
name|String
name|modelRef
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|sClasses
parameter_list|)
block|{
name|List
argument_list|<
name|UserResource
argument_list|>
name|resources
init|=
name|ResourceUtils
operator|.
name|getUserResources
argument_list|(
name|modelRef
argument_list|,
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|serviceFactory
operator|.
name|setUserResourcesWithServiceClass
argument_list|(
name|resources
argument_list|,
name|sClasses
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

