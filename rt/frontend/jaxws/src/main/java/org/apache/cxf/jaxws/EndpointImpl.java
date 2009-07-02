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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Executor
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|EndpointReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServicePermission
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|http
operator|.
name|HTTPBinding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReferenceBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|ClassHelper
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
name|ModCountCopyOnWriteArrayList
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
name|Configurable
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
name|interceptor
operator|.
name|Interceptor
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
name|InterceptorProvider
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsEndpointImpl
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsImplementorInfo
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|EndpointInfo
import|;
end_import

begin_class
specifier|public
class|class
name|EndpointImpl
extends|extends
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
implements|implements
name|InterceptorProvider
implements|,
name|Configurable
block|{
comment|/**      * This property controls whether the 'publishEndpoint' permission is checked       * using only the AccessController (i.e. when SecurityManager is not installed).      * By default this check is not done as the system property is not set.      */
specifier|public
specifier|static
specifier|final
name|String
name|CHECK_PUBLISH_ENDPOINT_PERMISSON_PROPERTY
init|=
literal|"org.apache.cxf.jaxws.checkPublishEndpointPermission"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|WebServicePermission
name|PUBLISH_PERMISSION
init|=
operator|new
name|WebServicePermission
argument_list|(
literal|"publishEndpoint"
argument_list|)
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
name|EndpointImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Object
name|implementor
decl_stmt|;
specifier|private
name|Server
name|server
decl_stmt|;
specifier|private
name|JaxWsServerFactoryBean
name|serverFactory
decl_stmt|;
specifier|private
name|JaxWsServiceFactoryBean
name|serviceFactory
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Source
argument_list|>
name|metadata
decl_stmt|;
specifier|private
name|Invoker
name|invoker
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|String
name|bindingUri
decl_stmt|;
specifier|private
name|String
name|wsdlLocation
decl_stmt|;
specifier|private
name|String
name|address
decl_stmt|;
specifier|private
name|String
name|publishedEndpointUrl
decl_stmt|;
specifier|private
name|QName
name|endpointName
decl_stmt|;
specifier|private
name|QName
name|serviceName
decl_stmt|;
specifier|private
name|Class
name|implementorClass
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|features
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|>
name|in
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|>
name|out
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|>
name|outFault
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|>
name|inFault
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Interceptor
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
init|=
operator|new
name|ModCountCopyOnWriteArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|EndpointImpl
parameter_list|(
name|Object
name|implementor
parameter_list|)
block|{
name|this
argument_list|(
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EndpointImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Object
name|implementor
parameter_list|,
name|JaxWsServerFactoryBean
name|sf
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|serverFactory
operator|=
name|sf
expr_stmt|;
name|this
operator|.
name|implementor
operator|=
name|implementor
expr_stmt|;
block|}
comment|/**      *       * @param b      * @param i The implementor object.      * @param bindingUri The URI of the Binding being used. Optional.      * @param wsdl The URL of the WSDL for the service, if different than the URL specified on the      * WebService annotation. Optional.      */
specifier|public
name|EndpointImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Object
name|i
parameter_list|,
name|String
name|bindingUri
parameter_list|,
name|String
name|wsdl
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|implementor
operator|=
name|i
expr_stmt|;
name|this
operator|.
name|bindingUri
operator|=
name|bindingUri
expr_stmt|;
name|wsdlLocation
operator|=
name|wsdl
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|String
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|serverFactory
operator|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EndpointImpl
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Object
name|i
parameter_list|,
name|String
name|bindingUri
parameter_list|)
block|{
name|this
argument_list|(
name|b
argument_list|,
name|i
argument_list|,
name|bindingUri
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Object
name|implementor
parameter_list|)
block|{
name|this
argument_list|(
name|bus
argument_list|,
name|implementor
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
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
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
return|;
block|}
specifier|public
name|Service
name|getService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
specifier|public
name|JaxWsServiceFactoryBean
name|getServiceFactory
parameter_list|()
block|{
return|return
name|serviceFactory
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getImplementor
parameter_list|()
block|{
return|return
name|implementor
return|;
block|}
comment|/**      * Gets the class of the implementor.      * @return the class of the implementor object      */
specifier|public
name|Class
name|getImplementorClass
parameter_list|()
block|{
return|return
name|implementorClass
operator|!=
literal|null
condition|?
name|implementorClass
else|:
name|ClassHelper
operator|.
name|getRealClass
argument_list|(
name|implementor
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Source
argument_list|>
name|getMetadata
parameter_list|()
block|{
return|return
name|metadata
return|;
block|}
annotation|@
name|Override
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
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
return|return
name|server
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
if|if
condition|(
name|properties
operator|==
literal|null
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
return|return
name|properties
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isPublished
parameter_list|()
block|{
return|return
name|server
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|publish
parameter_list|(
name|Object
name|arg0
parameter_list|)
block|{
comment|// Since this does not do anything now, just check the permission
name|checkPublishPermission
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|publish
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
name|doPublish
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceFactory
parameter_list|(
name|JaxWsServiceFactoryBean
name|sf
parameter_list|)
block|{
name|serviceFactory
operator|=
name|sf
expr_stmt|;
block|}
specifier|public
name|void
name|setMetadata
parameter_list|(
name|List
argument_list|<
name|Source
argument_list|>
name|metadata
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
block|}
annotation|@
name|Override
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
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|server
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|endpointName
operator|.
name|toString
argument_list|()
operator|+
literal|".jaxws-endpoint"
return|;
block|}
specifier|protected
name|void
name|checkProperties
parameter_list|()
block|{
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
literal|"javax.xml.ws.wsdl.description"
argument_list|)
condition|)
block|{
name|wsdlLocation
operator|=
name|properties
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.wsdl.description"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
operator|.
name|WSDL_PORT
argument_list|)
condition|)
block|{
name|endpointName
operator|=
operator|(
name|QName
operator|)
name|properties
operator|.
name|get
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
operator|.
name|WSDL_PORT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|containsKey
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
operator|.
name|WSDL_SERVICE
argument_list|)
condition|)
block|{
name|serviceName
operator|=
operator|(
name|QName
operator|)
name|properties
operator|.
name|get
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
operator|.
name|WSDL_SERVICE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|doPublish
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
name|checkPublishPermission
argument_list|()
expr_stmt|;
try|try
block|{
name|ServerImpl
name|serv
init|=
name|getServer
argument_list|(
name|addr
argument_list|)
decl_stmt|;
if|if
condition|(
name|addr
operator|!=
literal|null
condition|)
block|{
name|EndpointInfo
name|endpointInfo
init|=
name|serv
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|endpointInfo
operator|.
name|getAddress
argument_list|()
operator|.
name|contains
argument_list|(
name|addr
argument_list|)
condition|)
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|addr
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|publishedEndpointUrl
operator|!=
literal|null
condition|)
block|{
comment|// TODO is there a good place to put this key-string as a constant?
name|endpointInfo
operator|.
name|setProperty
argument_list|(
literal|"publishedEndpointUrl"
argument_list|,
name|publishedEndpointUrl
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|address
operator|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
name|serv
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ServerImpl
name|getServer
parameter_list|()
block|{
return|return
name|getServer
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|ServerImpl
name|getServer
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|checkProperties
argument_list|()
expr_stmt|;
comment|// Initialize the endpointName so we can do configureObject
name|QName
name|origEpn
init|=
name|endpointName
decl_stmt|;
if|if
condition|(
name|endpointName
operator|==
literal|null
condition|)
block|{
name|JaxWsImplementorInfo
name|implInfo
init|=
operator|new
name|JaxWsImplementorInfo
argument_list|(
name|getImplementorClass
argument_list|()
argument_list|)
decl_stmt|;
name|endpointName
operator|=
name|implInfo
operator|.
name|getEndpointName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|serviceFactory
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|setServiceFactory
argument_list|(
name|serviceFactory
argument_list|)
expr_stmt|;
block|}
comment|/*if (serviceName != null) {                 serverFactory.getServiceFactory().setServiceName(serviceName);             }*/
name|configureObject
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|endpointName
operator|=
name|origEpn
expr_stmt|;
comment|// Set up the server factory
name|serverFactory
operator|.
name|setAddress
argument_list|(
name|addr
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setEndpointName
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setServiceBean
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setSchemaLocations
argument_list|(
name|schemaLocations
argument_list|)
expr_stmt|;
if|if
condition|(
name|serverFactory
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serverFactory
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
comment|// Be careful not to override any serverfactory settings as a user might
comment|// have supplied their own.
if|if
condition|(
name|getWsdlLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|setWsdlURL
argument_list|(
name|getWsdlLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bindingUri
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|setBindingId
argument_list|(
name|bindingUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceName
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|implementorClass
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|setServiceClass
argument_list|(
name|implementorClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|serverFactory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handlers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|serverFactory
operator|.
name|addHandlers
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
block|}
name|configureObject
argument_list|(
name|serverFactory
argument_list|)
expr_stmt|;
name|server
operator|=
name|serverFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Endpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|getInInterceptors
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|endpoint
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
name|endpoint
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
name|endpoint
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
name|endpoint
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
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|configureObject
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|configureObject
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|service
operator|=
name|endpoint
operator|.
name|getService
argument_list|()
expr_stmt|;
if|if
condition|(
name|getWsdlLocation
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|//hold onto the wsdl location so cache won't clear till we go away
name|setWsdlLocation
argument_list|(
name|serverFactory
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serviceName
operator|==
literal|null
condition|)
block|{
name|setServiceName
argument_list|(
name|serverFactory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|getServiceQName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|(
name|ServerImpl
operator|)
name|server
return|;
block|}
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
operator|(
name|ServerImpl
operator|)
name|getServer
argument_list|(
literal|null
argument_list|)
operator|)
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
specifier|private
name|void
name|configureObject
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|Configurer
name|configurer
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|configurer
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|checkPublishPermission
parameter_list|()
block|{
name|SecurityManager
name|sm
init|=
name|System
operator|.
name|getSecurityManager
argument_list|()
decl_stmt|;
if|if
condition|(
name|sm
operator|!=
literal|null
condition|)
block|{
name|sm
operator|.
name|checkPermission
argument_list|(
name|PUBLISH_PERMISSION
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|CHECK_PUBLISH_ENDPOINT_PERMISSON_PROPERTY
argument_list|)
condition|)
block|{
name|AccessController
operator|.
name|checkPermission
argument_list|(
name|PUBLISH_PERMISSION
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|publish
parameter_list|()
block|{
name|publish
argument_list|(
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
block|}
comment|/**     * The published endpoint url is used for excplicitely specifying the url of the     * endpoint that would show up the generated wsdl definition, when the service is     * brought on line.     * @return     */
specifier|public
name|String
name|getPublishedEndpointUrl
parameter_list|()
block|{
return|return
name|publishedEndpointUrl
return|;
block|}
specifier|public
name|void
name|setPublishedEndpointUrl
parameter_list|(
name|String
name|publishedEndpointUrl
parameter_list|)
block|{
name|this
operator|.
name|publishedEndpointUrl
operator|=
name|publishedEndpointUrl
expr_stmt|;
block|}
specifier|public
name|QName
name|getEndpointName
parameter_list|()
block|{
return|return
name|endpointName
return|;
block|}
specifier|public
name|void
name|setEndpointName
parameter_list|(
name|QName
name|endpointName
parameter_list|)
block|{
name|this
operator|.
name|endpointName
operator|=
name|endpointName
expr_stmt|;
block|}
specifier|public
name|QName
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|QName
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
specifier|public
name|String
name|getWsdlLocation
parameter_list|()
block|{
return|return
name|wsdlLocation
return|;
block|}
specifier|public
name|void
name|setWsdlLocation
parameter_list|(
name|String
name|wsdlLocation
parameter_list|)
block|{
if|if
condition|(
name|wsdlLocation
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|wsdlLocation
operator|=
operator|new
name|String
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|wsdlLocation
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBindingUri
parameter_list|(
name|String
name|binding
parameter_list|)
block|{
name|this
operator|.
name|bindingUri
operator|=
name|binding
expr_stmt|;
block|}
specifier|public
name|String
name|getBindingUri
parameter_list|()
block|{
return|return
name|this
operator|.
name|bindingUri
return|;
block|}
specifier|public
name|void
name|setDataBinding
parameter_list|(
name|DataBinding
name|dataBinding
parameter_list|)
block|{
name|serverFactory
operator|.
name|setDataBinding
argument_list|(
name|dataBinding
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|()
block|{
return|return
name|serverFactory
operator|.
name|getDataBinding
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|>
name|getOutFaultInterceptors
parameter_list|()
block|{
return|return
name|outFault
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|>
name|getInFaultInterceptors
parameter_list|()
block|{
return|return
name|inFault
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|>
name|getInInterceptors
parameter_list|()
block|{
return|return
name|in
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|>
name|getOutInterceptors
parameter_list|()
block|{
return|return
name|out
return|;
block|}
specifier|public
name|void
name|setInInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|interceptors
parameter_list|)
block|{
name|in
operator|=
name|interceptors
expr_stmt|;
block|}
specifier|public
name|void
name|setInFaultInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|interceptors
parameter_list|)
block|{
name|inFault
operator|=
name|interceptors
expr_stmt|;
block|}
specifier|public
name|void
name|setOutInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|interceptors
parameter_list|)
block|{
name|out
operator|=
name|interceptors
expr_stmt|;
block|}
specifier|public
name|void
name|setOutFaultInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|interceptors
parameter_list|)
block|{
name|outFault
operator|=
name|interceptors
expr_stmt|;
block|}
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
argument_list|<
name|Handler
argument_list|>
name|h
parameter_list|)
block|{
name|handlers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|handlers
operator|.
name|addAll
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
return|;
block|}
specifier|public
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|getFeatures
parameter_list|()
block|{
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
name|features
operator|=
operator|new
name|ArrayList
argument_list|<
name|AbstractFeature
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|features
return|;
block|}
specifier|public
name|void
name|setFeatures
parameter_list|(
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|features
parameter_list|)
block|{
name|this
operator|.
name|features
operator|=
name|features
expr_stmt|;
block|}
specifier|public
name|Invoker
name|getInvoker
parameter_list|()
block|{
return|return
name|invoker
return|;
block|}
specifier|public
name|void
name|setInvoker
parameter_list|(
name|Invoker
name|invoker
parameter_list|)
block|{
name|this
operator|.
name|invoker
operator|=
name|invoker
expr_stmt|;
block|}
specifier|public
name|void
name|setImplementorClass
parameter_list|(
name|Class
name|implementorClass
parameter_list|)
block|{
name|this
operator|.
name|implementorClass
operator|=
name|implementorClass
expr_stmt|;
block|}
specifier|public
name|void
name|setTransportId
parameter_list|(
name|String
name|transportId
parameter_list|)
block|{
name|serverFactory
operator|.
name|setTransportId
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTransportId
parameter_list|()
block|{
return|return
name|serverFactory
operator|.
name|getTransportId
argument_list|()
return|;
block|}
specifier|public
name|void
name|setBindingConfig
parameter_list|(
name|BindingConfiguration
name|config
parameter_list|)
block|{
name|serverFactory
operator|.
name|setBindingConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BindingConfiguration
name|getBindingConfig
parameter_list|()
block|{
return|return
name|serverFactory
operator|.
name|getBindingConfig
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSchemaLocations
parameter_list|()
block|{
return|return
name|schemaLocations
return|;
block|}
specifier|public
name|void
name|setSchemaLocations
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|schemaLocations
parameter_list|)
block|{
name|this
operator|.
name|schemaLocations
operator|=
name|schemaLocations
expr_stmt|;
block|}
specifier|public
name|EndpointReference
name|getEndpointReference
parameter_list|(
name|Element
modifier|...
name|referenceParameters
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isPublished
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ENDPOINT_NOT_PUBLISHED"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|getBinding
argument_list|()
operator|instanceof
name|HTTPBinding
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"GET_ENDPOINTREFERENCE_UNSUPPORTED_BINDING"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|W3CEndpointReferenceBuilder
name|builder
init|=
operator|new
name|W3CEndpointReferenceBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|address
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|builder
operator|.
name|serviceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
name|builder
operator|.
name|endpointName
argument_list|(
name|endpointName
argument_list|)
expr_stmt|;
if|if
condition|(
name|referenceParameters
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Element
name|referenceParameter
range|:
name|referenceParameters
control|)
block|{
name|builder
operator|.
name|referenceParameter
argument_list|(
name|referenceParameter
argument_list|)
expr_stmt|;
block|}
block|}
name|builder
operator|.
name|wsdlDocumentLocation
argument_list|(
name|wsdlLocation
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|EndpointReference
parameter_list|>
name|T
name|getEndpointReference
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Element
modifier|...
name|referenceParameters
parameter_list|)
block|{
if|if
condition|(
name|W3CEndpointReference
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|getEndpointReference
argument_list|(
name|referenceParameters
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ENDPOINTREFERENCE_TYPE_NOT_SUPPORTED"
argument_list|,
name|LOG
argument_list|,
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

