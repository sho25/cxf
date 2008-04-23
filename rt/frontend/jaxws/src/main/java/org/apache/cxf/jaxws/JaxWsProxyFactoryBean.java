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
name|BindingProvider
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
name|Client
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
name|ClientProxy
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
name|ClientProxyFactoryBean
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
name|interceptors
operator|.
name|HolderInInterceptor
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
name|interceptors
operator|.
name|HolderOutInterceptor
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
name|interceptors
operator|.
name|WrapperClassInInterceptor
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
name|interceptors
operator|.
name|WrapperClassOutInterceptor
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
name|ReflectionServiceFactoryBean
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
name|OperationInfo
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
name|ServiceInfo
import|;
end_import

begin_class
specifier|public
class|class
name|JaxWsProxyFactoryBean
extends|extends
name|ClientProxyFactoryBean
block|{
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
name|boolean
name|loadHandlers
init|=
literal|true
decl_stmt|;
specifier|public
name|JaxWsProxyFactoryBean
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|JaxWsClientFactoryBean
argument_list|()
argument_list|)
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
name|void
name|setLoadHandlers
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|loadHandlers
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isLoadHandlers
parameter_list|()
block|{
return|return
name|loadHandlers
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ClientProxy
name|clientClientProxy
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
name|JaxWsClientProxy
name|cp
init|=
operator|new
name|JaxWsClientProxy
argument_list|(
name|c
argument_list|,
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|c
operator|.
name|getEndpoint
argument_list|()
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
argument_list|)
decl_stmt|;
name|cp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|this
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|buildHandlerChain
argument_list|(
name|cp
argument_list|)
expr_stmt|;
return|return
name|cp
return|;
block|}
specifier|protected
name|Class
index|[]
name|getImplementingClasses
parameter_list|()
block|{
name|Class
name|cls
init|=
name|getClientFactoryBean
argument_list|()
operator|.
name|getServiceClass
argument_list|()
decl_stmt|;
return|return
operator|new
name|Class
index|[]
block|{
name|cls
block|,
name|BindingProvider
operator|.
name|class
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|create
parameter_list|()
block|{
name|Object
name|obj
init|=
name|super
operator|.
name|create
argument_list|()
decl_stmt|;
name|Service
name|service
init|=
name|getServiceFactory
argument_list|()
operator|.
name|getService
argument_list|()
decl_stmt|;
if|if
condition|(
name|needWrapperClassInterceptor
argument_list|(
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Interceptor
argument_list|>
name|in
init|=
name|super
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|out
init|=
name|super
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
name|in
operator|.
name|add
argument_list|(
operator|new
name|WrapperClassInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|add
argument_list|(
operator|new
name|HolderInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|add
argument_list|(
operator|new
name|WrapperClassOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|add
argument_list|(
operator|new
name|HolderOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
specifier|private
name|boolean
name|needWrapperClassInterceptor
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|)
block|{
if|if
condition|(
name|serviceInfo
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|OperationInfo
name|opInfo
range|:
name|serviceInfo
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|opInfo
operator|.
name|isUnwrappedCapable
argument_list|()
operator|&&
name|opInfo
operator|.
name|getProperty
argument_list|(
name|ReflectionServiceFactoryBean
operator|.
name|WRAPPERGEN_NEEDED
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|buildHandlerChain
parameter_list|(
name|JaxWsClientProxy
name|cp
parameter_list|)
block|{
name|AnnotationHandlerChainBuilder
name|builder
init|=
operator|new
name|AnnotationHandlerChainBuilder
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
name|sf
init|=
operator|(
name|JaxWsServiceFactoryBean
operator|)
name|getServiceFactory
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|loadHandlers
condition|)
block|{
name|chain
operator|.
name|addAll
argument_list|(
name|builder
operator|.
name|buildHandlerChainFromClass
argument_list|(
name|sf
operator|.
name|getServiceClass
argument_list|()
argument_list|,
name|sf
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|sf
operator|.
name|getServiceQName
argument_list|()
argument_list|,
name|this
operator|.
name|getBindingId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|chain
operator|.
name|isEmpty
argument_list|()
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
for|for
control|(
name|Handler
name|h
range|:
name|chain
control|)
block|{
if|if
condition|(
name|Proxy
operator|.
name|isProxyClass
argument_list|(
name|h
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
name|h
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|injector
operator|.
name|construct
argument_list|(
name|h
argument_list|,
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|injector
operator|.
name|inject
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|injector
operator|.
name|construct
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|cp
operator|.
name|getBinding
argument_list|()
operator|.
name|setHandlerChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

