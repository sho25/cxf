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
name|HeaderUtil
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
class|class
name|SOAPHandlerFaultInInterceptor
extends|extends
name|AbstractProtocolHandlerInterceptor
argument_list|<
name|SoapMessage
argument_list|>
implements|implements
name|SoapInterceptor
block|{
specifier|public
name|SOAPHandlerFaultInInterceptor
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
name|PRE_PROTOCOL_FRONTEND
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
comment|// TODO
return|return
operator|new
name|HashSet
argument_list|<>
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
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Handler
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
name|checkUnderstoodHeaders
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|invokeProtocolHandlersHandleFault
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
block|}
block|}
specifier|private
name|void
name|checkUnderstoodHeaders
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|paramHeaders
init|=
name|HeaderUtil
operator|.
name|getHeaderQNameInOperationParam
argument_list|(
name|soapMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|paramHeaders
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|//the TCK expects the getHeaders method to always be
comment|//called.   If there aren't any headers in the message,
comment|//THe MustUnderstandInterceptor quickly returns without
comment|//trying to calculate the understood headers.   Thus,
comment|//we need to call it here.
name|getUnderstoodHeaders
argument_list|()
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
comment|// client side outbound
comment|// wont get here
block|}
else|else
block|{
comment|// client side inbound - Normal handler message processing
comment|// stops, but the inbound interceptor chain still continues, dispatch the message
comment|//By onCompletion here, we can skip rest Logical handlers
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
comment|// wont get here
block|}
else|else
block|{
comment|// server side outbound
comment|// wont get here
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
block|{     }
block|}
end_class

end_unit

