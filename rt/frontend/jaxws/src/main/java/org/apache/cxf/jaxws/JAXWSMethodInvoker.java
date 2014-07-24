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
name|Method
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
name|ws
operator|.
name|Provider
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
name|MessageContext
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
name|MessageContext
operator|.
name|Scope
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
name|helpers
operator|.
name|CastUtils
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
name|Fault
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
name|InterceptorChain
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
name|WebServiceContextImpl
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
name|WrappedMessageContext
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|MessageUtils
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
name|Factory
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

begin_class
specifier|public
class|class
name|JAXWSMethodInvoker
extends|extends
name|AbstractJAXWSMethodInvoker
block|{
specifier|public
specifier|static
specifier|final
name|String
name|COPY_SOAP_HEADERS_BY_FAULT
init|=
literal|"org.apache.cxf.fault.copySoapHeaders"
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|Invoker
name|invoker
decl_stmt|;
specifier|public
name|JAXWSMethodInvoker
parameter_list|(
specifier|final
name|Object
name|bean
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|SingletonFactory
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXWSMethodInvoker
parameter_list|(
name|Factory
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JAXWSMethodInvoker
parameter_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|Invoker
name|i
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|invoker
operator|=
name|i
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|performInvocation
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|Object
index|[]
name|paramArray
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|invoker
operator|!=
literal|null
condition|)
block|{
return|return
name|invoker
operator|.
name|invoke
argument_list|(
name|m
argument_list|,
name|paramArray
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|performInvocation
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|paramArray
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|serviceObject
parameter_list|,
name|Method
name|m
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
comment|// set up the webservice request context
name|WrappedMessageContext
name|ctx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|handlerScopedStuff
init|=
name|removeHandlerProperties
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
specifier|final
name|MessageContext
name|oldCtx
init|=
name|WebServiceContextImpl
operator|.
name|setMessageContext
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|res
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|(
name|params
operator|==
literal|null
operator|||
name|params
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
name|m
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|equals
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
condition|)
block|{
name|params
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|res
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|super
operator|.
name|invoke
argument_list|(
name|exchange
argument_list|,
name|serviceObject
argument_list|,
name|m
argument_list|,
name|params
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|serviceObject
operator|instanceof
name|Provider
operator|)
operator|&&
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
literal|"jaxws.provider.interpretNullAsOneway"
argument_list|,
literal|true
argument_list|)
operator|&&
operator|(
name|res
operator|!=
literal|null
operator|&&
operator|!
name|res
operator|.
name|isEmpty
argument_list|()
operator|&&
name|res
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
literal|null
operator|)
operator|&&
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|InterceptorChain
operator|.
name|State
operator|.
name|EXECUTING
condition|)
block|{
comment|// treat the non-oneway call as oneway when a provider returns null
comment|// and the chain is not suspended due to a continuation suspend
name|res
operator|=
literal|null
expr_stmt|;
name|changeToOneway
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|//update the webservice response context
name|updateWebServiceContext
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
comment|//get chance to copy over customer's header
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|COPY_SOAP_HEADERS_BY_FAULT
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|updateHeader
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
throw|throw
name|f
throw|;
block|}
finally|finally
block|{
comment|//restore the WebServiceContextImpl's ThreadLocal variable to the previous value
if|if
condition|(
name|oldCtx
operator|==
literal|null
condition|)
block|{
name|WebServiceContextImpl
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|WebServiceContextImpl
operator|.
name|setMessageContext
argument_list|(
name|oldCtx
argument_list|)
expr_stmt|;
block|}
name|addHandlerProperties
argument_list|(
name|ctx
argument_list|,
name|handlerScopedStuff
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
specifier|private
name|void
name|changeToOneway
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
name|httpresp
init|=
operator|(
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|)
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
literal|"HTTP.RESPONSE"
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpresp
operator|!=
literal|null
condition|)
block|{
name|httpresp
operator|.
name|setStatus
argument_list|(
literal|202
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

