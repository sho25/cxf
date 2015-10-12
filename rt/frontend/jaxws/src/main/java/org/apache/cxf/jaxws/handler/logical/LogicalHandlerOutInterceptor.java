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
name|logical
package|;
end_package

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
name|stream
operator|.
name|XMLStreamWriter
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|DocumentFragment
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
name|DOMUtils
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
name|AbstractJAXWSHandlerInterceptor
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
name|staxutils
operator|.
name|W3CDOMStreamReader
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
name|W3CDOMStreamWriter
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
name|LogicalHandlerOutInterceptor
extends|extends
name|AbstractJAXWSHandlerInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_WRITER
init|=
name|LogicalHandlerOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".original_writer"
decl_stmt|;
specifier|private
name|LogicalHandlerOutEndingInterceptor
name|ending
decl_stmt|;
specifier|public
name|LogicalHandlerOutInterceptor
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
name|PRE_MARSHAL
argument_list|)
expr_stmt|;
name|ending
operator|=
operator|new
name|LogicalHandlerOutEndingInterceptor
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
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
name|HandlerChainInvoker
name|invoker
init|=
name|getInvoker
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|invoker
operator|.
name|getLogicalHandlers
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|XMLStreamWriter
name|origWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Node
name|nd
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|SOAPMessage
name|m
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
name|Document
name|document
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|document
operator|=
name|m
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nd
operator|!=
literal|null
condition|)
block|{
name|document
operator|=
name|nd
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|document
argument_list|)
expr_stmt|;
block|}
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|document
operator|.
name|createDocumentFragment
argument_list|()
argument_list|)
decl_stmt|;
comment|// Replace stax writer with DomStreamWriter
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|ORIGINAL_WRITER
argument_list|,
name|origWriter
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
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|XMLStreamWriter
name|os
init|=
operator|(
name|XMLStreamWriter
operator|)
name|message
operator|.
name|get
argument_list|(
name|ORIGINAL_WRITER
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|LogicalHandlerOutEndingInterceptor
extends|extends
name|AbstractJAXWSHandlerInterceptor
argument_list|<
name|Message
argument_list|>
block|{
name|LogicalHandlerOutEndingInterceptor
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
name|POST_MARSHAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|W3CDOMStreamWriter
name|domWriter
init|=
operator|(
name|W3CDOMStreamWriter
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|origWriter
init|=
operator|(
name|XMLStreamWriter
operator|)
name|message
operator|.
name|get
argument_list|(
name|LogicalHandlerOutInterceptor
operator|.
name|ORIGINAL_WRITER
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
name|LogicalMessageContextImpl
name|lctx
init|=
operator|new
name|LogicalMessageContextImpl
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|invoker
operator|.
name|setLogicalMessageContext
argument_list|(
name|lctx
argument_list|)
expr_stmt|;
name|boolean
name|requestor
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
operator|(
name|XMLStreamReader
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"LogicalHandlerInterceptor.INREADER"
argument_list|)
decl_stmt|;
name|SOAPMessage
name|origMessage
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|origMessage
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|message
operator|.
name|removeContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|domWriter
operator|.
name|getCurrentFragment
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DocumentFragment
name|frag
init|=
name|domWriter
operator|.
name|getCurrentFragment
argument_list|()
decl_stmt|;
name|Node
name|nd
init|=
name|frag
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
name|Source
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|nd
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
operator|new
name|W3CDOMStreamReader
argument_list|(
name|domWriter
operator|.
name|getCurrentFragment
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|domWriter
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Source
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|domWriter
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|domWriter
operator|.
name|getDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|invoker
operator|.
name|invokeLogicalHandlers
argument_list|(
name|requestor
argument_list|,
name|lctx
argument_list|)
condition|)
block|{
if|if
condition|(
name|requestor
condition|)
block|{
comment|// client side - abort
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
name|Endpoint
name|e
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
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
name|MessageObserver
name|observer
init|=
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
comment|//client side outbound, the request message becomes the response message
name|responseMsg
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|responseMsg
operator|.
name|put
argument_list|(
name|PhaseInterceptorChain
operator|.
name|STARTING_AT_INTERCEPTOR_ID
argument_list|,
name|LogicalHandlerInInterceptor
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
return|return;
block|}
block|}
else|else
block|{
comment|// server side - abort
comment|// Even return false, also should try to set the XMLStreamWriter using
comment|// reader or domWriter, or the SOAPMessage's body maybe empty.
block|}
block|}
if|if
condition|(
name|origMessage
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|origMessage
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|reader
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|removeContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|origWriter
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|domWriter
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|domWriter
operator|.
name|getDocument
argument_list|()
argument_list|,
name|origWriter
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|origWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
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
block|}
block|}
block|}
end_class

end_unit

