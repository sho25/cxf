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
name|transport
operator|.
name|jms
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SimpleTimeZone
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|ConcurrentLinkedQueue
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
name|jms
operator|.
name|BytesMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|endpoint
operator|.
name|MessageEndpoint
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
name|BusFactory
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
name|continuations
operator|.
name|ContinuationProvider
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
name|continuations
operator|.
name|SuspendedInvocationException
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
name|OneWayProcessorInterceptor
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
name|AbstractConduit
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
name|AbstractMultiplexDestination
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
name|transport
operator|.
name|jms
operator|.
name|continuations
operator|.
name|JMSContinuation
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
name|jms
operator|.
name|continuations
operator|.
name|JMSContinuationProvider
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
name|wsdl
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|MessageCreator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|SessionCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|listener
operator|.
name|AbstractMessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|JmsUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|destination
operator|.
name|DestinationResolver
import|;
end_import

begin_class
specifier|public
class|class
name|JMSDestination
extends|extends
name|AbstractMultiplexDestination
implements|implements
name|MessageListener
implements|,
name|JMSExchangeSender
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
name|JMSDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JMSConfiguration
name|jmsConfig
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|EndpointInfo
name|ei
decl_stmt|;
specifier|private
name|AbstractMessageListenerContainer
name|jmsListener
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|JMSContinuation
argument_list|>
name|continuations
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|JMSContinuation
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|JMSDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|info
parameter_list|,
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|super
argument_list|(
name|b
argument_list|,
name|getTargetReference
argument_list|(
name|info
argument_list|,
name|b
argument_list|)
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|ei
operator|=
name|info
expr_stmt|;
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
name|info
operator|.
name|setProperty
argument_list|(
name|OneWayProcessorInterceptor
operator|.
name|USE_ORIGINAL_THREAD
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param inMessage the incoming message      * @return the inbuilt backchannel      */
specifier|protected
name|Conduit
name|getInbuiltBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
name|EndpointReferenceType
name|anon
init|=
name|EndpointReferenceUtils
operator|.
name|getAnonymousEndpointReference
argument_list|()
decl_stmt|;
return|return
operator|new
name|BackChannelConduit
argument_list|(
name|this
argument_list|,
name|anon
argument_list|,
name|inMessage
argument_list|)
return|;
block|}
comment|/**      * Initialize jmsTemplate and jmsListener from jms configuration data in jmsConfig {@inheritDoc}      */
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"JMSDestination activate().... "
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".jms-destination"
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|msg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"INSUFFICIENT_CONFIGURATION_DESTINATION"
argument_list|,
name|LOG
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|jmsConfig
operator|.
name|ensureProperlyConfigured
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|jmsListener
operator|=
name|JMSFactory
operator|.
name|createJmsListener
argument_list|(
name|ei
argument_list|,
name|jmsConfig
argument_list|,
name|this
argument_list|,
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
if|if
condition|(
name|jmsListener
operator|!=
literal|null
condition|)
block|{
name|jmsListener
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"JMSDestination shutdown()"
argument_list|)
expr_stmt|;
name|this
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Destination
name|resolveDestinationName
parameter_list|(
specifier|final
name|JmsTemplate
name|jmsTemplate
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|Destination
operator|)
name|jmsTemplate
operator|.
name|execute
argument_list|(
operator|new
name|SessionCallback
argument_list|()
block|{
specifier|public
name|Object
name|doInJms
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|DestinationResolver
name|resolv
init|=
name|jmsTemplate
operator|.
name|getDestinationResolver
argument_list|()
decl_stmt|;
return|return
name|resolv
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|name
argument_list|,
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|public
name|Destination
name|getReplyToDestination
parameter_list|(
name|JmsTemplate
name|jmsTemplate
parameter_list|,
name|Message
name|inMessage
parameter_list|)
throws|throws
name|JMSException
block|{
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
init|=
operator|(
name|javax
operator|.
name|jms
operator|.
name|Message
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|)
decl_stmt|;
comment|// If WS-Addressing had set the replyTo header.
specifier|final
name|String
name|replyToName
init|=
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_REBASED_REPLY_TO
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyToName
operator|!=
literal|null
condition|)
block|{
return|return
name|resolveDestinationName
argument_list|(
name|jmsTemplate
argument_list|,
name|replyToName
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|getJMSReplyTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|message
operator|.
name|getJMSReplyTo
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|resolveDestinationName
argument_list|(
name|jmsTemplate
argument_list|,
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No replyTo destination set on request message or cxf message"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Decides what correlationId to use for the reply by looking at the request headers. If the request has a      * correlationId set this is taken. Else the messageId from the request message is used as correlation Id      *       * @param request      * @return      * @throws JMSException      */
specifier|public
name|String
name|determineCorrelationID
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Message
name|request
parameter_list|)
throws|throws
name|JMSException
block|{
name|String
name|correlationID
init|=
name|request
operator|.
name|getJMSCorrelationID
argument_list|()
decl_stmt|;
if|if
condition|(
name|correlationID
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|correlationID
argument_list|)
condition|)
block|{
name|correlationID
operator|=
name|request
operator|.
name|getJMSMessageID
argument_list|()
expr_stmt|;
block|}
return|return
name|correlationID
return|;
block|}
comment|/**      * Convert JMS message received by ListenerThread to CXF message and inform incomingObserver that a      * message was received. The observer will call the service and then send the response CXF message by      * using the BackChannelConduit      *       * @param message      * @throws IOException      */
specifier|public
name|void
name|onMessage
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"server received request: "
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// Build CXF message from JMS message
name|MessageImpl
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|JMSUtils
operator|.
name|populateIncomingContext
argument_list|(
name|message
argument_list|,
name|inMessage
argument_list|,
name|JMSConstants
operator|.
name|JMS_SERVER_REQUEST_HEADERS
argument_list|)
expr_stmt|;
name|JMSUtils
operator|.
name|retrieveAndSetPayload
argument_list|(
name|inMessage
argument_list|,
name|message
argument_list|,
operator|(
name|String
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|,
operator|new
name|JMSMessageHeadersType
argument_list|()
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|inMessage
operator|.
name|put
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|JMSContinuationProvider
argument_list|(
name|bus
argument_list|,
name|inMessage
argument_list|,
name|incomingObserver
argument_list|,
name|continuations
argument_list|,
name|jmsListener
argument_list|,
name|jmsConfig
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|MessageEndpoint
name|ep
init|=
name|JCATransactionalMessageListenerContainer
operator|.
name|ENDPOINT_LOCAL
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|inMessage
operator|.
name|setContent
argument_list|(
name|MessageEndpoint
operator|.
name|class
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|JCATransactionalMessageListenerContainer
operator|.
name|ENDPOINT_LOCAL
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
comment|// handle the incoming message
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SuspendedInvocationException
name|ex
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Request message has been suspended"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"can't get the right encoding information. "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|sendExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|replyObj
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
comment|//Don't need to send anything
return|return;
block|}
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
condition|)
block|{
comment|// we will never receive a non-oneway invocation in pub-sub
comment|// domain from CXF client - however a mis-behaving pure JMS
comment|// client could conceivably make suce an invocation, in which
comment|// case we silently discard the reply
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"discarding reply for non-oneway invocation "
argument_list|,
literal|"with 'topic' destinationStyle"
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
specifier|final
name|JMSMessageHeadersType
name|messageProperties
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|JMSMessageHeadersType
name|inMessageProperties
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|JMSUtils
operator|.
name|initResponseMessageProperties
argument_list|(
name|messageProperties
argument_list|,
name|inMessageProperties
argument_list|)
expr_stmt|;
name|JmsTemplate
name|jmsTemplate
init|=
name|JMSFactory
operator|.
name|createJmsTemplate
argument_list|(
name|jmsConfig
argument_list|,
name|messageProperties
argument_list|)
decl_stmt|;
comment|// setup the reply message
specifier|final
name|javax
operator|.
name|jms
operator|.
name|Message
name|request
init|=
operator|(
name|javax
operator|.
name|jms
operator|.
name|Message
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|)
decl_stmt|;
specifier|final
name|String
name|msgType
decl_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|TextMessage
condition|)
block|{
name|msgType
operator|=
name|JMSConstants
operator|.
name|TEXT_MESSAGE_TYPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|request
operator|instanceof
name|BytesMessage
condition|)
block|{
name|msgType
operator|=
name|JMSConstants
operator|.
name|BYTE_MESSAGE_TYPE
expr_stmt|;
block|}
else|else
block|{
name|msgType
operator|=
name|JMSConstants
operator|.
name|BINARY_MESSAGE_TYPE
expr_stmt|;
block|}
name|Destination
name|replyTo
init|=
name|getReplyToDestination
argument_list|(
name|jmsTemplate
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|.
name|getJMSExpiration
argument_list|()
operator|>
literal|0
condition|)
block|{
name|TimeZone
name|tz
init|=
operator|new
name|SimpleTimeZone
argument_list|(
literal|0
argument_list|,
literal|"GMT"
argument_list|)
decl_stmt|;
name|Calendar
name|cal
init|=
operator|new
name|GregorianCalendar
argument_list|(
name|tz
argument_list|)
decl_stmt|;
name|long
name|timeToLive
init|=
name|request
operator|.
name|getJMSExpiration
argument_list|()
operator|-
name|cal
operator|.
name|getTimeInMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeToLive
operator|<
literal|0
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Message time to live is already expired skipping response."
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"send out the message!"
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|send
argument_list|(
name|replyTo
argument_list|,
operator|new
name|MessageCreator
argument_list|()
block|{
specifier|public
name|javax
operator|.
name|jms
operator|.
name|Message
name|createMessage
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
name|javax
operator|.
name|jms
operator|.
name|Message
name|reply
init|=
name|JMSUtils
operator|.
name|createAndSetPayload
argument_list|(
name|replyObj
argument_list|,
name|session
argument_list|,
name|msgType
argument_list|)
decl_stmt|;
name|reply
operator|.
name|setJMSCorrelationID
argument_list|(
name|determineCorrelationID
argument_list|(
name|request
argument_list|)
argument_list|)
expr_stmt|;
name|JMSUtils
operator|.
name|prepareJMSProperties
argument_list|(
name|messageProperties
argument_list|,
name|outMessage
argument_list|,
name|jmsConfig
argument_list|)
expr_stmt|;
name|JMSUtils
operator|.
name|setJMSProperties
argument_list|(
name|reply
argument_list|,
name|messageProperties
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"server sending reply: "
argument_list|,
name|reply
argument_list|)
expr_stmt|;
return|return
name|reply
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
name|JmsUtils
operator|.
name|convertJmsAccessException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
specifier|public
name|JMSConfiguration
name|getJmsConfig
parameter_list|()
block|{
return|return
name|jmsConfig
return|;
block|}
specifier|public
name|void
name|setJmsConfig
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
block|}
comment|/**      * Conduit for sending the reply back to the client      */
specifier|protected
class|class
name|BackChannelConduit
extends|extends
name|AbstractConduit
block|{
specifier|protected
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|JMSExchangeSender
name|sender
decl_stmt|;
name|BackChannelConduit
parameter_list|(
name|JMSExchangeSender
name|sender
parameter_list|,
name|EndpointReferenceType
name|ref
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|inMessage
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
comment|/**          * Register a message observer for incoming messages.          *           * @param observer the observer to notify on receipt of incoming          */
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
comment|// shouldn't be called for a back channel conduit
block|}
comment|/**          * Send an outbound message, assumed to contain all the name-value mappings of the corresponding input          * message (if any).          *           * @param message the message to be sent.          */
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
comment|// setup the message to be send back
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMessage
init|=
operator|(
name|javax
operator|.
name|jms
operator|.
name|Message
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|message
operator|.
name|containsKey
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|)
operator|&&
name|inMessage
operator|.
name|containsKey
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|,
name|inMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|JMSOutputStream
argument_list|(
name|sender
argument_list|,
name|exchange
argument_list|,
name|jmsMessage
operator|instanceof
name|TextMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
block|}
end_class

end_unit

