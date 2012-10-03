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
operator|.
name|handler
package|;
end_package

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
name|handler
operator|.
name|HandlerResolver
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
name|PortInfo
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
class|class
name|HandlerResolverImpl
implements|implements
name|HandlerResolver
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|PortInfo
argument_list|,
name|List
argument_list|<
name|Handler
argument_list|>
argument_list|>
name|handlerMap
init|=
operator|new
name|HashMap
argument_list|<
name|PortInfo
argument_list|,
name|List
argument_list|<
name|Handler
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|//private QName service;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|annotationClass
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|HandlerResolverImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|QName
name|serviceName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
comment|//this.service = pService;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|this
operator|.
name|annotationClass
operator|=
name|clazz
expr_stmt|;
block|}
specifier|public
name|HandlerResolverImpl
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlerChain
parameter_list|(
name|PortInfo
name|portInfo
parameter_list|)
block|{
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerChain
init|=
name|handlerMap
operator|.
name|get
argument_list|(
name|portInfo
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlerChain
operator|==
literal|null
condition|)
block|{
name|handlerChain
operator|=
name|createHandlerChain
argument_list|(
name|portInfo
argument_list|)
expr_stmt|;
name|handlerMap
operator|.
name|put
argument_list|(
name|portInfo
argument_list|,
name|handlerChain
argument_list|)
expr_stmt|;
block|}
return|return
name|handlerChain
return|;
block|}
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|createHandlerChain
parameter_list|(
name|PortInfo
name|portInfo
parameter_list|)
block|{
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
argument_list|()
decl_stmt|;
if|if
condition|(
name|annotationClass
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|addAll
argument_list|(
name|getHandlersFromAnnotation
argument_list|(
name|annotationClass
argument_list|,
name|portInfo
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|configHandler
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
return|return
name|chain
return|;
block|}
comment|/**      * Obtain handler chain from annotations.      *       * @param obj A endpoint implementation class or a SEI, or a generated      *            service class.      */
specifier|private
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlersFromAnnotation
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|PortInfo
name|portInfo
parameter_list|)
block|{
name|AnnotationHandlerChainBuilder
name|builder
init|=
operator|new
name|AnnotationHandlerChainBuilder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|chain
init|=
name|builder
operator|.
name|buildHandlerChainFromClass
argument_list|(
name|clazz
argument_list|,
name|portInfo
operator|!=
literal|null
condition|?
name|portInfo
operator|.
name|getPortName
argument_list|()
else|:
literal|null
argument_list|,
name|portInfo
operator|!=
literal|null
condition|?
name|portInfo
operator|.
name|getServiceName
argument_list|()
else|:
literal|null
argument_list|,
name|portInfo
operator|!=
literal|null
condition|?
name|portInfo
operator|.
name|getBindingID
argument_list|()
else|:
literal|null
argument_list|)
decl_stmt|;
return|return
name|chain
return|;
block|}
comment|/**      * JAX-WS section 9.3.1: The runtime MUST then carry out any injections      * requested by the handler, typically via the javax .annotation.Resource      * annotation. After all the injections have been carried out, including in      * the case where no injections were requested, the runtime MUST invoke the      * method carrying a javax.annotation .PostConstruct annotation, if present.      */
specifier|private
name|void
name|configHandler
parameter_list|(
name|Handler
argument_list|<
name|?
argument_list|>
name|handler
parameter_list|)
block|{
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|ResourceManager
name|resourceManager
init|=
name|bus
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
comment|//            resourceManager.addResourceResolver(new WebContextEntriesResourceResolver());
name|ResourceInjector
name|injector
init|=
operator|new
name|ResourceInjector
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
name|injector
operator|.
name|inject
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|injector
operator|.
name|construct
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

