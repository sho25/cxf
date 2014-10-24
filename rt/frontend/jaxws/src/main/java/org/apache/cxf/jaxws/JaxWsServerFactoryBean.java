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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|List
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
name|soap
operator|.
name|SOAPBinding
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
name|soap
operator|.
name|Soap12
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|ResourceInjector
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|AnnotationInterceptors
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
name|binding
operator|.
name|soap
operator|.
name|JaxWsSoapBindingConfiguration
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
name|context
operator|.
name|WebServiceContextResourceResolver
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
name|handler
operator|.
name|AnnotationHandlerChainBuilder
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
name|invoker
operator|.
name|SingletonFactory
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

begin_comment
comment|/**  * Bean to help easily create Server endpoints for JAX-WS.  *<pre>  * JaxWsServerFactoryBean sf = new JaxWsServerFactoryBean();  * sf.setServiceClass(MyService.class);  * sf.setAddress("http://acme.com/myService");  * sf.create();  *</pre>  * This will start a server and register it with the ServerManager.   */
end_comment

begin_class
specifier|public
class|class
name|JaxWsServerFactoryBean
extends|extends
name|ServerFactoryBean
block|{
specifier|protected
name|boolean
name|doInit
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|protected
name|List
argument_list|<
name|Handler
argument_list|>
name|handlers
init|=
operator|new
name|ArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|blockPostConstruct
decl_stmt|;
specifier|private
name|boolean
name|blockInjection
decl_stmt|;
specifier|public
name|JaxWsServerFactoryBean
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JaxWsServerFactoryBean
parameter_list|(
name|JaxWsServiceFactoryBean
name|serviceFactory
parameter_list|)
block|{
name|super
argument_list|(
name|serviceFactory
argument_list|)
expr_stmt|;
name|JaxWsSoapBindingConfiguration
name|defConfig
init|=
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
name|serviceFactory
argument_list|)
decl_stmt|;
name|setBindingConfig
argument_list|(
name|defConfig
argument_list|)
expr_stmt|;
name|doInit
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|JaxWsServiceFactoryBean
name|getJaxWsServiceFactory
parameter_list|()
block|{
return|return
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
return|;
block|}
specifier|public
name|void
name|setHandlers
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
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
name|void
name|addHandlers
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|List
argument_list|<
name|Handler
argument_list|>
name|h
parameter_list|)
block|{
name|handlers
operator|.
name|addAll
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
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
comment|/**      * Add annotated Interceptors and Features to the Endpoint      * @param ep      */
specifier|protected
name|void
name|initializeAnnotationInterceptors
parameter_list|(
name|Endpoint
name|ep
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|cls
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|seiClass
init|=
operator|(
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
operator|)
operator|.
name|getJaxWsImplementorInfo
argument_list|()
operator|.
name|getSEIClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|seiClass
operator|!=
literal|null
condition|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|cls
control|)
block|{
if|if
condition|(
name|c
operator|.
name|equals
argument_list|(
name|seiClass
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls2
index|[]
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|cls
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|cls
argument_list|,
literal|0
argument_list|,
name|cls2
argument_list|,
literal|0
argument_list|,
name|cls
operator|.
name|length
argument_list|)
expr_stmt|;
name|cls2
index|[
name|cls
operator|.
name|length
index|]
operator|=
name|seiClass
expr_stmt|;
name|cls
operator|=
name|cls2
expr_stmt|;
block|}
block|}
name|AnnotationInterceptors
name|provider
init|=
operator|new
name|AnnotationInterceptors
argument_list|(
name|cls
argument_list|)
decl_stmt|;
name|initializeAnnotationInterceptors
argument_list|(
name|provider
argument_list|,
name|ep
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Invoker
name|createInvoker
parameter_list|()
block|{
if|if
condition|(
name|getServiceBean
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|JAXWSMethodInvoker
argument_list|(
operator|new
name|SingletonFactory
argument_list|(
name|getServiceClass
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|new
name|JAXWSMethodInvoker
argument_list|(
name|getServiceBean
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|BindingInfo
name|createBindingInfo
parameter_list|()
block|{
name|JaxWsServiceFactoryBean
name|sf
init|=
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
decl_stmt|;
name|JaxWsImplementorInfo
name|implInfo
init|=
name|sf
operator|.
name|getJaxWsImplementorInfo
argument_list|()
decl_stmt|;
name|String
name|jaxBid
init|=
name|implInfo
operator|.
name|getBindingType
argument_list|()
decl_stmt|;
name|String
name|binding
init|=
name|getBindingId
argument_list|()
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
name|jaxBid
expr_stmt|;
name|setBindingId
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|)
operator|||
name|binding
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_MTOM_BINDING
argument_list|)
condition|)
block|{
name|binding
operator|=
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
expr_stmt|;
name|setBindingId
argument_list|(
name|binding
argument_list|)
expr_stmt|;
if|if
condition|(
name|getBindingConfig
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setBindingConfig
argument_list|(
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
name|sf
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|binding
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP12HTTP_MTOM_BINDING
argument_list|)
condition|)
block|{
name|binding
operator|=
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
expr_stmt|;
name|setBindingId
argument_list|(
name|binding
argument_list|)
expr_stmt|;
if|if
condition|(
name|getBindingConfig
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setBindingConfig
argument_list|(
operator|new
name|JaxWsSoapBindingConfiguration
argument_list|(
name|sf
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getBindingConfig
argument_list|()
operator|instanceof
name|JaxWsSoapBindingConfiguration
condition|)
block|{
name|JaxWsSoapBindingConfiguration
name|conf
init|=
operator|(
name|JaxWsSoapBindingConfiguration
operator|)
name|getBindingConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|jaxBid
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|)
condition|)
block|{
name|conf
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jaxBid
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP12HTTP_MTOM_BINDING
argument_list|)
condition|)
block|{
name|conf
operator|.
name|setVersion
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setMtomEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jaxBid
operator|.
name|equals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP11HTTP_MTOM_BINDING
argument_list|)
condition|)
block|{
name|conf
operator|.
name|setMtomEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|transportId
operator|!=
literal|null
condition|)
block|{
name|conf
operator|.
name|setTransportURI
argument_list|(
name|transportId
argument_list|)
expr_stmt|;
block|}
name|conf
operator|.
name|setJaxWsServiceFactoryBean
argument_list|(
name|sf
argument_list|)
expr_stmt|;
block|}
name|BindingInfo
name|bindingInfo
init|=
name|super
operator|.
name|createBindingInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|implInfo
operator|.
name|isWebServiceProvider
argument_list|()
condition|)
block|{
name|bindingInfo
operator|.
name|getService
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"soap.force.doclit.bare"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|isPopulateFromClass
argument_list|()
condition|)
block|{
for|for
control|(
name|EndpointInfo
name|ei
range|:
name|bindingInfo
operator|.
name|getService
argument_list|()
operator|.
name|getEndpoints
argument_list|()
control|)
block|{
name|ei
operator|.
name|setProperty
argument_list|(
literal|"soap.no.validate.parts"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|//Provider, but no wsdl.  Synthetic ops
for|for
control|(
name|BindingOperationInfo
name|op
range|:
name|bindingInfo
operator|.
name|getOperations
argument_list|()
control|)
block|{
name|op
operator|.
name|setProperty
argument_list|(
literal|"operation.is.synthetic"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|op
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"operation.is.synthetic"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|bindingInfo
return|;
block|}
specifier|public
name|Server
name|create
parameter_list|()
block|{
name|ClassLoaderHolder
name|orig
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|ClassLoader
name|loader
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|orig
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
name|Server
name|server
init|=
name|super
operator|.
name|create
argument_list|()
decl_stmt|;
name|initializeResourcesAndHandlerChain
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|checkPrivateEndpoint
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|server
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|orig
operator|!=
literal|null
condition|)
block|{
name|orig
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|initializeResourcesAndHandlerChain
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
if|if
condition|(
name|doInit
condition|)
block|{
try|try
block|{
name|injectResources
argument_list|(
name|getServiceBean
argument_list|()
argument_list|)
expr_stmt|;
name|buildHandlerChain
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|instanceof
name|WebServiceException
condition|)
block|{
throw|throw
operator|(
name|WebServiceException
operator|)
name|ex
throw|;
block|}
throw|throw
operator|new
name|WebServiceException
argument_list|(
literal|"Creation of Endpoint failed"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
name|doInit
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * Obtain handler chain from annotations.      * @param server       *      */
specifier|private
name|void
name|buildHandlerChain
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|AnnotationHandlerChainBuilder
name|builder
init|=
operator|new
name|AnnotationHandlerChainBuilder
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
init|=
operator|new
name|ArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|(
name|handlers
argument_list|)
decl_stmt|;
name|chain
operator|.
name|addAll
argument_list|(
name|builder
operator|.
name|buildHandlerChainFromClass
argument_list|(
name|getServiceBeanClass
argument_list|()
argument_list|,
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|this
operator|.
name|getBindingId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Handler
argument_list|<
name|?
argument_list|>
name|h
range|:
name|chain
control|)
block|{
name|injectResources
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|server
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
operator|.
name|setHandlerChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
block|}
comment|/**      * inject resources into servant.  The resources are injected      * according to @Resource annotations.  See JSR 250 for more      * information.      */
comment|/**      * @param instance      */
specifier|protected
name|void
name|injectResources
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|!=
literal|null
operator|&&
operator|!
name|blockInjection
condition|)
block|{
name|ResourceManager
name|resourceManager
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|resolvers
init|=
name|resourceManager
operator|.
name|getResourceResolvers
argument_list|()
decl_stmt|;
name|resourceManager
operator|=
operator|new
name|DefaultResourceManager
argument_list|(
name|resolvers
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|WebServiceContextResourceResolver
argument_list|()
argument_list|)
expr_stmt|;
name|ResourceInjector
name|injector
init|=
operator|new
name|ResourceInjector
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
if|if
condition|(
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|instance
operator|.
name|getClass
argument_list|()
argument_list|)
operator|&&
name|getServiceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|injector
operator|.
name|inject
argument_list|(
name|instance
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|blockPostConstruct
condition|)
block|{
name|injector
operator|.
name|construct
argument_list|(
name|instance
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|injector
operator|.
name|inject
argument_list|(
name|instance
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|blockPostConstruct
condition|)
block|{
name|injector
operator|.
name|construct
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * @param blockPostConstruct @PostConstruct method will not be called       *  if this property is set to true - this may be necessary in cases      *  when the @PostConstruct method needs to be called at a later stage,      *  for example, when a higher level container does its own injection.        */
specifier|public
name|void
name|setBlockPostConstruct
parameter_list|(
name|boolean
name|blockPostConstruct
parameter_list|)
block|{
name|this
operator|.
name|blockPostConstruct
operator|=
name|blockPostConstruct
expr_stmt|;
block|}
comment|/**      * No injection or PostConstruct will be called if this is set to true.      * If the container has already handled the injection, this should       * be set to true.      * @param b      */
specifier|public
name|void
name|setBlockInjection
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|this
operator|.
name|blockInjection
operator|=
name|b
expr_stmt|;
block|}
block|}
end_class

end_unit

