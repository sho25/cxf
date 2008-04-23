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
name|jaxws
operator|.
name|handler
operator|.
name|soap
operator|.
name|SOAPHandlerInterceptor
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
name|ContextPropertiesMapping
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
name|LogicalHandlerInInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
extends|extends
name|AbstractJAXWSHandlerInterceptor
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|LogicalHandlerInInterceptor
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
name|addAfter
argument_list|(
name|SOAPHandlerInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|T
name|message
parameter_list|)
block|{
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
if|if
condition|(
operator|!
name|requestor
condition|)
block|{
name|setupBindingOperationInfo
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|lctx
argument_list|)
expr_stmt|;
block|}
name|ContextPropertiesMapping
operator|.
name|mapCxf2Jaxws
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|lctx
argument_list|,
name|requestor
argument_list|)
expr_stmt|;
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
operator|!
name|requestor
condition|)
block|{
comment|//server side
name|handleAbort
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//Client side inbound, thus no response expected, do nothing, the close will
comment|//be handled by MEPComplete later
block|}
block|}
comment|//If this is the inbound and end of MEP, call MEP completion
if|if
condition|(
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
block|}
specifier|private
name|void
name|handleAbort
parameter_list|(
name|T
name|message
parameter_list|,
name|W3CDOMStreamWriter
name|writer
parameter_list|)
block|{
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
comment|//server side inbound
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
name|XMLStreamReader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|==
literal|null
operator|&&
name|writer
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|responseMsg
operator|.
name|put
argument_list|(
literal|"LogicalHandlerInterceptor.INREADER"
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doIntercept
argument_list|(
name|responseMsg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|T
name|message
parameter_list|)
block|{
comment|// TODO
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
name|LogicalMessageContextImpl
name|sm
init|=
operator|(
name|LogicalMessageContextImpl
operator|)
name|data
decl_stmt|;
name|Source
name|src
init|=
name|sm
operator|.
name|getMessage
argument_list|()
operator|.
name|getPayload
argument_list|()
decl_stmt|;
if|if
condition|(
name|src
operator|instanceof
name|DOMSource
condition|)
block|{
name|DOMSource
name|dsrc
init|=
operator|(
name|DOMSource
operator|)
name|src
decl_stmt|;
name|String
name|ln
init|=
name|dsrc
operator|.
name|getNode
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|dsrc
operator|.
name|getNode
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|ln
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

