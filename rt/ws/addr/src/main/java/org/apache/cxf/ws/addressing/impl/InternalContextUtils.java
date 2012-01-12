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
name|ws
operator|.
name|addressing
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConstants
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
name|model
operator|.
name|SoapOperationInfo
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
name|logging
operator|.
name|LogUtils
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
name|util
operator|.
name|StringUtils
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
name|ConduitSelector
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
name|endpoint
operator|.
name|NullConduitSelector
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
name|PreexistingConduitSelector
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
name|io
operator|.
name|DelegatingInputStream
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
name|service
operator|.
name|model
operator|.
name|BindingFaultInfo
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
name|BindingOperationInfo
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
name|EndpointInfo
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
name|Extensible
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
name|FaultInfo
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
name|MessageInfo
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
name|Conduit
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
name|ConduitInitiator
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
name|ConduitInitiatorManager
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
name|Destination
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
name|workqueue
operator|.
name|OneShotAsyncExecutor
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
name|workqueue
operator|.
name|SynchronousExecutor
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|AddressingProperties
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
name|AttributedURIType
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
name|ContextUtils
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
name|EndpointReferenceType
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
name|JAXWSAConstants
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsdl
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_comment
comment|/**  * Holder for utility methods relating to contexts.  */
end_comment

begin_class
specifier|final
class|class
name|InternalContextUtils
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|InternalContextUtils
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**     * Prevents instantiation.     */
specifier|private
name|InternalContextUtils
parameter_list|()
block|{     }
comment|/**      * Rebase response on replyTo      *       * @param reference the replyTo reference      * @param inMAPs the inbound MAPs      * @param inMessage the current message      */
specifier|public
specifier|static
name|void
name|rebaseResponse
parameter_list|(
name|EndpointReferenceType
name|reference
parameter_list|,
name|AddressingProperties
name|inMAPs
parameter_list|,
specifier|final
name|Message
name|inMessage
parameter_list|)
block|{
name|String
name|namespaceURI
init|=
name|inMAPs
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ContextUtils
operator|.
name|retrievePartialResponseSent
argument_list|(
name|inMessage
argument_list|)
condition|)
block|{
name|ContextUtils
operator|.
name|storePartialResponseSent
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|fullResponse
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|Message
name|partialResponse
init|=
name|ContextUtils
operator|.
name|createMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|ensurePartialResponseMAPs
argument_list|(
name|partialResponse
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
comment|// ensure the inbound MAPs are available in the partial response
comment|// message (used to determine relatesTo etc.)
name|ContextUtils
operator|.
name|propogateReceivedMAPs
argument_list|(
name|inMAPs
argument_list|,
name|partialResponse
argument_list|)
expr_stmt|;
name|partialResponse
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PARTIAL_RESPONSE_MESSAGE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|partialResponse
operator|.
name|put
argument_list|(
name|Message
operator|.
name|EMPTY_PARTIAL_RESPONSE_MESSAGE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Destination
name|target
init|=
name|inMessage
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
name|reference
operator|=
name|ContextUtils
operator|.
name|getNoneEndpointReference
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|partialResponse
argument_list|)
expr_stmt|;
name|Conduit
name|backChannel
init|=
name|target
operator|.
name|getBackChannel
argument_list|(
name|inMessage
argument_list|,
name|partialResponse
argument_list|,
name|reference
argument_list|)
decl_stmt|;
if|if
condition|(
name|backChannel
operator|!=
literal|null
condition|)
block|{
comment|// set up interceptor chains and send message
name|InterceptorChain
name|chain
init|=
name|fullResponse
operator|!=
literal|null
condition|?
name|fullResponse
operator|.
name|getInterceptorChain
argument_list|()
else|:
name|OutgoingChainInterceptor
operator|.
name|getOutInterceptorChain
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|partialResponse
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|,
operator|new
name|PreexistingConduitSelector
argument_list|(
name|backChannel
argument_list|,
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|chain
operator|!=
literal|null
operator|&&
operator|!
name|chain
operator|.
name|doIntercept
argument_list|(
name|partialResponse
argument_list|)
operator|&&
name|partialResponse
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|partialResponse
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|instanceof
name|Fault
condition|)
block|{
throw|throw
operator|(
name|Fault
operator|)
name|partialResponse
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|partialResponse
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|chain
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|put
argument_list|(
name|ConduitSelector
operator|.
name|class
argument_list|,
operator|new
name|NullConduitSelector
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|fullResponse
operator|==
literal|null
condition|)
block|{
name|fullResponse
operator|=
name|ContextUtils
operator|.
name|createMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|fullResponse
argument_list|)
expr_stmt|;
name|Destination
name|destination
init|=
name|createDecoupledDestination
argument_list|(
name|exchange
argument_list|,
name|reference
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
if|if
condition|(
name|ContextUtils
operator|.
name|retrieveAsyncPostResponseDispatch
argument_list|(
name|inMessage
argument_list|)
condition|)
block|{
comment|//need to suck in all the data from the input stream as
comment|//the transport might discard any data on the stream when this
comment|//thread unwinds or when the empty response is sent back
name|DelegatingInputStream
name|in
init|=
name|inMessage
operator|.
name|getContent
argument_list|(
name|DelegatingInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|cacheInput
argument_list|()
expr_stmt|;
block|}
comment|// async service invocation required *after* a response
comment|// has been sent (i.e. to a oneway, or a partial response
comment|// to a decoupled twoway)
comment|// pause dispatch on current thread ...
name|inMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|pause
argument_list|()
expr_stmt|;
comment|// ... and resume on executor thread
name|getExecutor
argument_list|(
name|inMessage
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|inMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SERVER_TRANSPORT_REBASE_FAILURE_MSG"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|Destination
name|createDecoupledDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|EndpointReferenceType
name|reference
parameter_list|)
block|{
specifier|final
name|EndpointInfo
name|ei
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
return|return
operator|new
name|Destination
argument_list|()
block|{
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
return|return
name|reference
return|;
block|}
specifier|public
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|Message
name|partialResponse
parameter_list|,
name|EndpointReferenceType
name|address
parameter_list|)
throws|throws
name|IOException
block|{
name|Bus
name|bus
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//this is a response targeting a decoupled endpoint.   Treat it as a oneway so
comment|//we don't wait for a response.
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ConduitInitiator
name|conduitInitiator
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
operator|.
name|getConduitInitiatorForUri
argument_list|(
name|reference
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduitInitiator
operator|!=
literal|null
condition|)
block|{
name|Conduit
name|c
init|=
name|conduitInitiator
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|reference
argument_list|)
decl_stmt|;
comment|// ensure decoupled back channel input stream is closed
name|c
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|m
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{             }
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{             }
block|}
return|;
block|}
comment|/**      * Construct and store MAPs for partial response.      *       * @param partialResponse the partial response message      * @param namespaceURI the current namespace URI      */
specifier|private
specifier|static
name|void
name|ensurePartialResponseMAPs
parameter_list|(
name|Message
name|partialResponse
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{
comment|// ensure there is a MAPs instance available for the outbound
comment|// partial response that contains appropriate To and ReplyTo
comment|// properties (i.e. anonymous& none respectively)
name|AddressingPropertiesImpl
name|maps
init|=
operator|new
name|AddressingPropertiesImpl
argument_list|()
decl_stmt|;
name|maps
operator|.
name|setTo
argument_list|(
name|EndpointReferenceUtils
operator|.
name|getAnonymousEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|maps
operator|.
name|setReplyTo
argument_list|(
name|ContextUtils
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
argument_list|)
expr_stmt|;
name|maps
operator|.
name|getReplyTo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|ContextUtils
operator|.
name|getAttributedURI
argument_list|(
name|Names
operator|.
name|WSA_NONE_ADDRESS
argument_list|)
argument_list|)
expr_stmt|;
name|maps
operator|.
name|setAction
argument_list|(
name|ContextUtils
operator|.
name|getAttributedURI
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|maps
operator|.
name|exposeAs
argument_list|(
name|namespaceURI
argument_list|)
expr_stmt|;
name|ContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|partialResponse
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct the Action URI.      *       * @param message the current message      * @return the Action URI      */
specifier|public
specifier|static
name|AttributedURIType
name|getAction
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|action
init|=
literal|null
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Determining action"
argument_list|)
expr_stmt|;
name|Exception
name|fault
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// REVISIT: add support for @{Fault}Action annotation (generated
comment|// from the wsaw:Action WSDL element). For the moment we just
comment|// pick up the wsaw:Action attribute by walking the WSDL model
comment|// directly
name|action
operator|=
name|getActionFromServiceModel
argument_list|(
name|message
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"action: "
operator|+
name|action
argument_list|)
expr_stmt|;
return|return
name|action
operator|!=
literal|null
condition|?
name|ContextUtils
operator|.
name|getAttributedURI
argument_list|(
name|action
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Get action from service model.      *      * @param message the current message      * @param fault the fault if one is set      */
specifier|private
specifier|static
name|String
name|getActionFromServiceModel
parameter_list|(
name|Message
name|message
parameter_list|,
name|Exception
name|fault
parameter_list|)
block|{
name|String
name|action
init|=
literal|null
decl_stmt|;
name|BindingOperationInfo
name|bindingOpInfo
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindingOpInfo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|bindingOpInfo
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|bindingOpInfo
operator|=
name|bindingOpInfo
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fault
operator|==
literal|null
condition|)
block|{
name|action
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|ContextUtils
operator|.
name|ACTION
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|action
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|MessageInfo
name|msgInfo
init|=
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|?
name|bindingOpInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getInput
argument_list|()
else|:
name|bindingOpInfo
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|String
name|cachedAction
init|=
operator|(
name|String
operator|)
name|msgInfo
operator|.
name|getProperty
argument_list|(
name|ContextUtils
operator|.
name|ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedAction
operator|==
literal|null
condition|)
block|{
name|action
operator|=
name|getActionFromMessageAttributes
argument_list|(
name|msgInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|action
operator|=
name|cachedAction
expr_stmt|;
block|}
if|if
condition|(
name|action
operator|==
literal|null
operator|&&
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|SoapOperationInfo
name|soi
init|=
name|getSoapOperationInfo
argument_list|(
name|bindingOpInfo
argument_list|)
decl_stmt|;
name|action
operator|=
name|soi
operator|==
literal|null
condition|?
literal|null
else|:
name|soi
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|action
operator|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|action
argument_list|)
condition|?
literal|null
else|:
name|action
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Throwable
name|t
init|=
name|fault
operator|.
name|getCause
argument_list|()
decl_stmt|;
comment|// FaultAction attribute is not defined in
comment|// http://www.w3.org/2005/02/addressing/wsdl schema
for|for
control|(
name|BindingFaultInfo
name|bfi
range|:
name|bindingOpInfo
operator|.
name|getFaults
argument_list|()
control|)
block|{
name|FaultInfo
name|fi
init|=
name|bfi
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fiTypeClass
init|=
name|fi
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
name|fiTypeClass
operator|!=
literal|null
operator|&&
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|fiTypeClass
argument_list|)
condition|)
block|{
if|if
condition|(
name|fi
operator|.
name|getExtensionAttributes
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|attr
init|=
operator|(
name|String
operator|)
name|fi
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|Names
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
name|attr
operator|=
operator|(
name|String
operator|)
name|fi
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_WSDL_NAME_OLD
argument_list|,
name|Names
operator|.
name|WSAW_ACTION_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
name|action
operator|=
name|attr
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"action determined from service model: "
operator|+
name|action
argument_list|)
expr_stmt|;
return|return
name|action
return|;
block|}
specifier|public
specifier|static
name|SoapOperationInfo
name|getSoapOperationInfo
parameter_list|(
name|BindingOperationInfo
name|bindingOpInfo
parameter_list|)
block|{
name|SoapOperationInfo
name|soi
init|=
name|bindingOpInfo
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|soi
operator|==
literal|null
operator|&&
name|bindingOpInfo
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|soi
operator|=
name|bindingOpInfo
operator|.
name|getWrappedOperation
argument_list|()
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|soi
return|;
block|}
comment|/**      * Get action from attributes on MessageInfo      *      * @param bindingOpInfo the current BindingOperationInfo      * @param msgInfo the current MessageInfo      * @return the action if set      */
specifier|private
specifier|static
name|String
name|getActionFromMessageAttributes
parameter_list|(
name|MessageInfo
name|msgInfo
parameter_list|)
block|{
name|String
name|action
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msgInfo
operator|!=
literal|null
operator|&&
name|msgInfo
operator|.
name|getExtensionAttributes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|attr
init|=
name|getAction
argument_list|(
name|msgInfo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|attr
argument_list|)
condition|)
block|{
name|action
operator|=
name|attr
expr_stmt|;
name|msgInfo
operator|.
name|setProperty
argument_list|(
name|ContextUtils
operator|.
name|ACTION
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|action
return|;
block|}
specifier|public
specifier|static
name|String
name|getAction
parameter_list|(
name|Extensible
name|ext
parameter_list|)
block|{
name|Object
name|o
init|=
name|ext
operator|.
name|getExtensionAttribute
argument_list|(
name|JAXWSAConstants
operator|.
name|WSAW_ACTION_QNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_WSDL_METADATA
argument_list|,
name|Names
operator|.
name|WSAW_ACTION_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|JAXWSAConstants
operator|.
name|NS_WSA
argument_list|,
name|Names
operator|.
name|WSAW_ACTION_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|ext
operator|.
name|getExtensionAttributes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_WSDL_NAME_OLD
argument_list|,
name|Names
operator|.
name|WSAW_ACTION_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|QName
condition|)
block|{
return|return
operator|(
operator|(
name|QName
operator|)
name|o
operator|)
operator|.
name|getLocalPart
argument_list|()
return|;
block|}
return|return
name|o
operator|==
literal|null
condition|?
literal|null
else|:
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Get the Executor for this invocation.      * @param endpoint      * @return      */
specifier|private
specifier|static
name|Executor
name|getExecutor
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
name|Endpoint
name|endpoint
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
name|Executor
name|executor
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getExecutor
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|==
literal|null
operator|||
name|SynchronousExecutor
operator|.
name|isA
argument_list|(
name|executor
argument_list|)
condition|)
block|{
comment|// need true asynchrony
name|Bus
name|bus
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|WorkQueueManager
name|workQueueManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|Executor
name|autoWorkQueue
init|=
name|workQueueManager
operator|.
name|getNamedWorkQueue
argument_list|(
literal|"ws-addressing"
argument_list|)
decl_stmt|;
name|executor
operator|=
name|autoWorkQueue
operator|!=
literal|null
condition|?
name|autoWorkQueue
else|:
name|workQueueManager
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|executor
operator|=
name|OneShotAsyncExecutor
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Executor
operator|.
name|class
argument_list|,
name|executor
argument_list|)
expr_stmt|;
return|return
name|executor
return|;
block|}
block|}
end_class

end_unit

