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
name|SOAPFault
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
name|soap
operator|.
name|SOAPFaultException
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
name|Node
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
name|SoapFault
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

begin_class
specifier|public
class|class
name|SOAPHandlerFaultOutInterceptor
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
specifier|private
specifier|static
specifier|final
name|String
name|ENDING_ID
init|=
name|SOAPHandlerFaultOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ENDING"
decl_stmt|;
name|AbstractSoapInterceptor
name|ending
init|=
operator|new
name|AbstractSoapInterceptor
argument_list|(
name|ENDING_ID
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
name|SOAPHandlerFaultOutInterceptor
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
comment|// TODO
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
name|checkUnderstoodHeaders
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
comment|//The SOAPMessage might be set from the outchain, in this case,
comment|//we need to clean it up and create a new SOAPMessage dedicated to fault.
name|message
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|SAAJ_OUT
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
try|try
block|{
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
comment|// handleAbort(message, context);
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|exception
parameter_list|)
block|{
comment|/*              * handleFault throws exception, in this case we need to replace              * SOAPFault with the exception thrown from HandleFault so that the              * exception can be dispatched.              */
try|try
block|{
name|SOAPMessage
name|originalMsg
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
name|SOAPBody
name|body
init|=
name|originalMsg
operator|.
name|getSOAPBody
argument_list|()
decl_stmt|;
name|body
operator|.
name|removeContents
argument_list|()
expr_stmt|;
name|SOAPFault
name|soapFault
init|=
name|body
operator|.
name|addFault
argument_list|()
decl_stmt|;
if|if
condition|(
name|exception
operator|instanceof
name|SOAPFaultException
condition|)
block|{
name|SOAPFaultException
name|sf
init|=
operator|(
name|SOAPFaultException
operator|)
name|exception
decl_stmt|;
name|soapFault
operator|.
name|setFaultString
argument_list|(
name|sf
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultString
argument_list|()
argument_list|)
expr_stmt|;
name|soapFault
operator|.
name|setFaultCode
argument_list|(
name|sf
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultCodeAsQName
argument_list|()
argument_list|)
expr_stmt|;
name|soapFault
operator|.
name|setFaultActor
argument_list|(
name|sf
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultActor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sf
operator|.
name|getFault
argument_list|()
operator|.
name|hasDetail
argument_list|()
condition|)
block|{
name|Node
name|nd
init|=
name|originalMsg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|importNode
argument_list|(
name|sf
operator|.
name|getFault
argument_list|()
operator|.
name|getDetail
argument_list|()
operator|.
name|getFirstChild
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|soapFault
operator|.
name|addDetail
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|exception
operator|instanceof
name|Fault
condition|)
block|{
name|SoapFault
name|sf
init|=
name|SoapFault
operator|.
name|createFault
argument_list|(
operator|(
name|Fault
operator|)
name|exception
argument_list|,
operator|(
operator|(
name|SoapMessage
operator|)
name|message
operator|)
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|soapFault
operator|.
name|setFaultString
argument_list|(
name|sf
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|soapFault
operator|.
name|setFaultCode
argument_list|(
name|sf
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sf
operator|.
name|hasDetails
argument_list|()
condition|)
block|{
name|soapFault
operator|.
name|addDetail
argument_list|()
expr_stmt|;
name|Node
name|nd
init|=
name|originalMsg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|importNode
argument_list|(
name|sf
operator|.
name|getDetail
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|nd
operator|=
name|nd
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
while|while
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
name|soapFault
operator|.
name|getDetail
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|soapFault
operator|.
name|setFaultString
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|soapFault
operator|.
name|setFaultCode
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/faultcode"
argument_list|,
literal|"HandleFault"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
comment|// do nothing
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
name|onCompletion
argument_list|(
name|message
argument_list|)
expr_stmt|;
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

