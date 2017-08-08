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
name|rm
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
name|EndpointReferenceUtils
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

begin_comment
comment|/**  * Holder for utility methods relating to contexts. Somewhat stripped-down version of class of same name in  * org.apache.cxf.ws.addressing.impl.  */
end_comment

begin_class
specifier|final
class|class
name|InternalContextUtils
block|{
specifier|private
specifier|static
specifier|final
class|class
name|DecoupledDestination
implements|implements
name|Destination
block|{
specifier|private
specifier|final
name|EndpointInfo
name|ei
decl_stmt|;
specifier|private
specifier|final
name|EndpointReferenceType
name|reference
decl_stmt|;
specifier|private
name|DecoupledDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|reference
parameter_list|)
block|{
name|this
operator|.
name|ei
operator|=
name|ei
expr_stmt|;
name|this
operator|.
name|reference
operator|=
name|reference
expr_stmt|;
block|}
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
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ContextUtils
operator|.
name|isNoneAddress
argument_list|(
name|reference
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Bus
name|bus
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
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
argument_list|,
name|bus
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
block|{         }
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{         }
block|}
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
comment|/**      * Rebase response on replyTo      *      * @param reference the replyTo reference      * @param inMAPs the inbound MAPs      * @param inMessage the current message      */
comment|//CHECKSTYLE:OFF Max executable statement count limitation
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
name|Conduit
name|backChannel
init|=
name|target
operator|.
name|getBackChannel
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|backChannel
operator|!=
literal|null
condition|)
block|{
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
name|boolean
name|robust
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|inMessage
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|robust
condition|)
block|{
name|BindingOperationInfo
name|boi
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
comment|// insert the executor in the exchange to fool the OneWayProcessorInterceptor
name|exchange
operator|.
name|put
argument_list|(
name|Executor
operator|.
name|class
argument_list|,
name|getExecutor
argument_list|(
name|inMessage
argument_list|)
argument_list|)
expr_stmt|;
comment|// pause dispatch on current thread and resume...
name|inMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|pause
argument_list|()
expr_stmt|;
name|inMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|resume
argument_list|()
expr_stmt|;
comment|// restore the BOI for the partial response handling
name|exchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
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
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|partialResponse
argument_list|)
expr_stmt|;
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
name|getEndpoint
argument_list|()
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
comment|//CHECKSTYLE:ON
specifier|private
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
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
return|return
operator|new
name|DecoupledDestination
argument_list|(
name|ei
argument_list|,
name|reference
argument_list|)
return|;
block|}
comment|/**      * Construct and store MAPs for partial response.      *      * @param partialResponse the partial response message      * @param namespaceURI the current namespace URI      */
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
name|AddressingProperties
name|maps
init|=
operator|new
name|AddressingProperties
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
name|getEndpoint
argument_list|()
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
name|getBus
argument_list|()
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

