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
name|FaultMode
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
name|W3CDOMStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|LogicalHandlerFaultOutInterceptor
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
literal|"original_writer"
decl_stmt|;
name|LogicalHandlerFaultOutEndingInterceptor
name|ending
decl_stmt|;
specifier|public
name|LogicalHandlerFaultOutInterceptor
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
name|LogicalHandlerFaultOutEndingInterceptor
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
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|doc
argument_list|)
decl_stmt|;
comment|// set up the namespace context
try|try
block|{
name|writer
operator|.
name|setNamespaceContext
argument_list|(
name|origWriter
operator|.
name|getNamespaceContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
comment|// don't set the namespaceContext
block|}
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
specifier|private
class|class
name|LogicalHandlerFaultOutEndingInterceptor
extends|extends
name|AbstractJAXWSHandlerInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|LogicalHandlerFaultOutEndingInterceptor
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
name|LogicalHandlerFaultOutInterceptor
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
name|Node
operator|.
name|class
argument_list|,
name|domWriter
operator|.
name|getDocument
argument_list|()
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
try|try
block|{
if|if
condition|(
operator|!
name|invoker
operator|.
name|invokeLogicalHandlersHandleFault
argument_list|(
name|requestor
argument_list|,
name|lctx
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
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Fault
argument_list|(
name|exception
argument_list|)
decl_stmt|;
name|FaultMode
name|mode
init|=
name|message
operator|.
name|get
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|faultMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|faultMessage
condition|)
block|{
name|faultMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|faultMessage
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|faultMessage
operator|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
block|}
name|faultMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mode
condition|)
block|{
name|faultMessage
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setOutMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutFaultMessage
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
name|ic
operator|.
name|reset
argument_list|()
expr_stmt|;
name|onCompletion
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|faultMessage
operator|.
name|setInterceptorChain
argument_list|(
name|ic
argument_list|)
expr_stmt|;
name|ic
operator|.
name|doIntercept
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
return|return;
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

