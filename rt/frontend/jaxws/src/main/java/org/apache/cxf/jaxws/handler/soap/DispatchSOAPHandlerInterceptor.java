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
operator|.
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|soap
operator|.
name|SOAPMessage
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
name|soap
operator|.
name|SOAPHandler
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
name|soap
operator|.
name|SOAPMessageContext
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
name|SoapMessage
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
name|interceptor
operator|.
name|SoapInterceptor
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
name|jaxws
operator|.
name|handler
operator|.
name|AbstractProtocolHandlerInterceptor
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
name|HandlerChainInvoker
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
name|phase
operator|.
name|Phase
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|MessageObserver
import|;
end_import

begin_class
specifier|public
class|class
name|DispatchSOAPHandlerInterceptor
extends|extends
name|AbstractProtocolHandlerInterceptor
argument_list|<
name|SoapMessage
argument_list|>
implements|implements
name|SoapInterceptor
block|{
specifier|public
name|DispatchSOAPHandlerInterceptor
parameter_list|(
name|Binding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|binding
argument_list|,
name|Phase
operator|.
name|USER_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|URI
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|URI
argument_list|>
argument_list|()
return|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|understood
init|=
operator|new
name|HashSet
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Handler
name|h
range|:
name|getBinding
argument_list|()
operator|.
name|getHandlerChain
argument_list|()
control|)
block|{
if|if
condition|(
name|h
operator|instanceof
name|SOAPHandler
condition|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
operator|(
name|SOAPHandler
operator|)
name|h
operator|)
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|understood
operator|.
name|addAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|understood
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|getInvoker
argument_list|(
name|message
argument_list|)
operator|.
name|getProtocolHandlers
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|MessageContext
name|context
init|=
name|createProtocolMessageContext
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|HandlerChainInvoker
name|invoker
init|=
name|getInvoker
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|invoker
operator|.
name|setProtocolMessageContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|invoker
operator|.
name|invokeProtocolHandlers
argument_list|(
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|,
name|context
argument_list|)
condition|)
block|{
name|handleAbort
argument_list|(
name|message
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|// If this is the outbound and end of MEP, call MEP completion
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|invoker
operator|.
name|getLogicalHandlers
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|isOutbound
argument_list|(
name|message
argument_list|)
operator|&&
name|isMEPComlete
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|onCompletion
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isOutbound
argument_list|(
name|message
argument_list|)
operator|&&
name|isMEPComlete
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|onCompletion
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleAbort
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|MessageContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|// client side outbound
if|if
condition|(
name|getInvoker
argument_list|(
name|message
argument_list|)
operator|.
name|isOutbound
argument_list|()
condition|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
name|Endpoint
name|e
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|responseMsg
init|=
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|MessageObserver
name|observer
init|=
operator|(
name|MessageObserver
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|observer
operator|!=
literal|null
condition|)
block|{
comment|// the request message becomes the response message
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|responseMsg
argument_list|)
expr_stmt|;
name|SOAPMessage
name|soapMessage
init|=
operator|(
operator|(
name|SOAPMessageContext
operator|)
name|context
operator|)
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|soapMessage
operator|!=
literal|null
condition|)
block|{
name|responseMsg
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|soapMessage
argument_list|)
expr_stmt|;
block|}
name|responseMsg
operator|.
name|put
argument_list|(
name|PhaseInterceptorChain
operator|.
name|STARTING_AT_INTERCEPTOR_ID
argument_list|,
name|SOAPHandlerInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|responseMsg
argument_list|)
expr_stmt|;
block|}
comment|//We dont call onCompletion here, as onCompletion will be called by inbound
comment|//LogicalHandlerInterceptor
block|}
else|else
block|{
comment|// client side inbound - Normal handler message processing
comment|// stops, but the inbound interceptor chain still continues, dispatch the message
comment|//By onCompletion here, we can skip following Logical handlers
name|onCompletion
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|MessageContext
name|createProtocolMessageContext
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
return|return
operator|new
name|SOAPMessageContextImpl
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{     }
block|}
end_class

end_unit

