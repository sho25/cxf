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
name|ArrayList
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
name|Iterator
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
name|ListIterator
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
name|Node
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
name|SOAPBody
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
name|SOAPElement
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
name|SOAPException
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
name|SOAPHeader
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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|dom
operator|.
name|DOMSource
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
name|AbstractSoapInterceptor
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
name|MustUnderstandInterceptor
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|SoapPreProtocolOutInterceptor
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
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJOutInterceptor
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
name|interceptor
operator|.
name|OutgoingChainInterceptor
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
name|message
operator|.
name|MessageImpl
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
name|staxutils
operator|.
name|StaxUtils
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|Names
import|;
end_import

begin_class
specifier|public
class|class
name|SOAPHandlerInterceptor
extends|extends
name|AbstractProtocolHandlerInterceptor
argument_list|<
name|SoapMessage
argument_list|>
implements|implements
name|SoapInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|SAAJOutInterceptor
name|SAAJ_OUT
init|=
operator|new
name|SAAJOutInterceptor
argument_list|()
decl_stmt|;
name|AbstractSoapInterceptor
name|ending
init|=
operator|new
name|AbstractSoapInterceptor
argument_list|(
name|SOAPHandlerInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ENDING"
argument_list|,
name|Phase
operator|.
name|USER_PROTOCOL
argument_list|)
block|{
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|handleMessageInternal
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|public
name|SOAPHandlerInterceptor
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
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|MustUnderstandInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|SAAJOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
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
name|Set
argument_list|<
name|URI
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
comment|//TODO
return|return
name|roles
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
name|binding
operator|.
name|getHandlerChain
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
operator|!
name|chainAlreadyContainsSAAJ
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|SAAJ_OUT
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|isFault
init|=
name|handleMessageInternal
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|SOAPMessage
name|msg
init|=
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
name|XMLStreamReader
name|xmlReader
init|=
name|createXMLStreamReaderFromSOAPMessage
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|xmlReader
argument_list|)
expr_stmt|;
comment|// replace headers
try|try
block|{
name|SAAJInInterceptor
operator|.
name|replaceHeaders
argument_list|(
name|msg
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isFault
condition|)
block|{
name|Endpoint
name|ep
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
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|abort
argument_list|()
expr_stmt|;
if|if
condition|(
name|ep
operator|.
name|getInFaultObserver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|getInFaultObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|handleMessageInternal
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|MessageContext
name|context
init|=
name|createProtocolMessageContext
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
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
return|return
literal|false
return|;
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
operator|!
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
operator|&&
name|observer
operator|!=
literal|null
condition|)
block|{
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
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|responseMsg
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|responseMsg
operator|=
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|responseMsg
argument_list|)
expr_stmt|;
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
name|XMLStreamReader
name|xmlReader
init|=
name|createXMLStreamReaderFromSOAPMessage
argument_list|(
name|soapMessage
argument_list|)
decl_stmt|;
name|responseMsg
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|xmlReader
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
else|else
block|{
if|if
condition|(
operator|!
name|getInvoker
argument_list|(
name|message
argument_list|)
operator|.
name|isOutbound
argument_list|()
condition|)
block|{
comment|// server side inbound
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
if|if
condition|(
operator|!
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|Message
name|responseMsg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|responseMsg
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|responseMsg
operator|=
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|responseMsg
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
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
name|InterceptorChain
name|chain
init|=
name|OutgoingChainInterceptor
operator|.
name|getOutInterceptorChain
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|responseMsg
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
comment|// so the idea of starting interceptor chain from any
comment|// specified point does not work
comment|// well for outbound case, as many outbound interceptors
comment|// have their ending interceptors.
comment|// For example, we can not skip MessageSenderInterceptor.
name|chain
operator|.
name|doInterceptStartingAfter
argument_list|(
name|responseMsg
argument_list|,
name|SoapPreProtocolOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// server side outbound - Normal handler message processing
comment|// stops, but still continue the outbound interceptor chain, dispatch the message
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
name|SOAPMessageContextImpl
name|sm
init|=
operator|new
name|SOAPMessageContextImpl
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Exchange
name|exch
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|setupBindingOperationInfo
argument_list|(
name|exch
argument_list|,
name|sm
argument_list|)
expr_stmt|;
name|SOAPMessage
name|msg
init|=
name|sm
operator|.
name|getMessage
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|SOAPElement
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|SOAPElement
argument_list|>
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|REFERENCE_PARAMETERS
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|SOAPHeader
name|head
init|=
name|msg
operator|.
name|getSOAPHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|head
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|Node
argument_list|>
name|it
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|head
operator|.
name|getChildElements
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|!=
literal|null
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Node
name|nd
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|nd
operator|instanceof
name|SOAPElement
condition|)
block|{
name|SOAPElement
name|el
init|=
operator|(
name|SOAPElement
operator|)
name|nd
decl_stmt|;
if|if
condition|(
name|el
operator|.
name|hasAttributeNS
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
literal|"IsReferenceParameter"
argument_list|)
operator|&&
operator|(
literal|"1"
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getAttributeNS
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
literal|"IsReferenceParameter"
argument_list|)
argument_list|)
operator|||
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|el
operator|.
name|getAttributeNS
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
literal|"IsReferenceParameter"
argument_list|)
argument_list|)
operator|)
condition|)
block|{
name|params
operator|.
name|add
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|msg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
operator|&&
name|msg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|hasFault
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|sm
return|;
block|}
specifier|private
name|XMLStreamReader
name|createXMLStreamReaderFromSOAPMessage
parameter_list|(
name|SOAPMessage
name|soapMessage
parameter_list|)
block|{
comment|// responseMsg.setContent(SOAPMessage.class, soapMessage);
name|XMLStreamReader
name|xmlReader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|DOMSource
name|bodySource
init|=
operator|new
name|DOMSource
argument_list|(
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|xmlReader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bodySource
argument_list|)
expr_stmt|;
name|xmlReader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|xmlReader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|// move past body tag
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|xmlReader
return|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|binding
operator|.
name|getHandlerChain
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
name|getInvoker
argument_list|(
name|message
argument_list|)
operator|.
name|isOutbound
argument_list|()
operator|&&
operator|!
name|chainAlreadyContainsSAAJ
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|SAAJ_OUT
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|QName
name|getOpQName
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
name|SOAPMessageContextImpl
name|sm
init|=
operator|(
name|SOAPMessageContextImpl
operator|)
name|data
decl_stmt|;
try|try
block|{
name|SOAPMessage
name|msg
init|=
name|sm
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SOAPBody
name|body
init|=
name|msg
operator|.
name|getSOAPBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
name|nd
init|=
name|body
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|nd
operator|instanceof
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
operator|)
condition|)
block|{
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|nd
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|nd
operator|.
name|getLocalName
argument_list|()
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|//ignore, nothing we can do
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|boolean
name|chainAlreadyContainsSAAJ
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|ListIterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|listIterator
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|getIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|listIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|listIterator
operator|.
name|next
argument_list|()
operator|instanceof
name|SAAJOutInterceptor
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
block|}
end_class

end_unit

