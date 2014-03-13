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
name|Connection
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
name|util
operator|.
name|JMSSender
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
name|util
operator|.
name|JMSUtil
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
name|util
operator|.
name|ResourceCloser
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

begin_comment
comment|/**  * Conduit for sending the reply back to the client  */
end_comment

begin_class
class|class
name|BackChannelConduit
extends|extends
name|AbstractConduit
implements|implements
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
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
name|BackChannelConduit
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|Connection
name|connection
parameter_list|)
block|{
name|super
argument_list|(
name|EndpointReferenceUtils
operator|.
name|getAnonymousEndpointReference
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|inMessage
operator|=
name|inMessage
expr_stmt|;
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|MessageStreamUtil
operator|.
name|closeStreams
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|super
operator|.
name|close
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
comment|/**      * Register a message observer for incoming messages.      *       * @param observer the observer to notify on receipt of incoming      */
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
comment|/**      * Send an outbound message, assumed to contain all the name-value mappings of the corresponding input      * message (if any).      *       * @param message the message to be sent.      */
specifier|public
name|void
name|prepare
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
comment|// setup the message to be sent back
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
name|boolean
name|isTextMessage
init|=
operator|(
name|jmsMessage
operator|instanceof
name|TextMessage
operator|)
operator|&&
operator|!
name|JMSMessageUtils
operator|.
name|isMtomEnabled
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|MessageStreamUtil
operator|.
name|prepareStream
argument_list|(
name|message
argument_list|,
name|isTextMessage
argument_list|,
name|this
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
specifier|final
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|ResourceCloser
name|closer
init|=
operator|new
name|ResourceCloser
argument_list|()
decl_stmt|;
try|try
block|{
name|Session
name|session
init|=
name|closer
operator|.
name|register
argument_list|(
name|connection
operator|.
name|createSession
argument_list|(
name|jmsConfig
operator|.
name|isSessionTransacted
argument_list|()
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
argument_list|)
decl_stmt|;
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
name|initResponseMessageProperties
argument_list|(
name|messageProperties
argument_list|,
name|inMessageProperties
argument_list|)
expr_stmt|;
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
init|=
name|JMSMessageUtils
operator|.
name|isMtomEnabled
argument_list|(
name|outMessage
argument_list|)
condition|?
name|JMSConstants
operator|.
name|BINARY_MESSAGE_TYPE
else|:
name|JMSMessageUtils
operator|.
name|getMessageType
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTimedOut
argument_list|(
name|request
argument_list|)
condition|)
block|{
return|return;
block|}
name|Destination
name|replyTo
init|=
name|getReplyToDestination
argument_list|(
name|session
argument_list|,
name|inMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyTo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No replyTo destination set"
argument_list|)
throw|;
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
name|String
name|correlationId
init|=
name|determineCorrelationID
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|javax
operator|.
name|jms
operator|.
name|Message
name|reply
init|=
name|JMSMessageUtils
operator|.
name|asJMSMessage
argument_list|(
name|jmsConfig
argument_list|,
name|outMessage
argument_list|,
name|replyObj
argument_list|,
name|msgType
argument_list|,
name|session
argument_list|,
name|correlationId
argument_list|,
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|JMSSender
name|sender
init|=
name|JMSFactory
operator|.
name|createJmsSender
argument_list|(
name|jmsConfig
argument_list|,
name|messageProperties
argument_list|)
decl_stmt|;
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
name|sender
operator|.
name|sendMessage
argument_list|(
name|closer
argument_list|,
name|session
argument_list|,
name|replyTo
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
throw|throw
name|JMSUtil
operator|.
name|convertJmsException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @param messageProperties      * @param inMessageProperties      */
specifier|public
specifier|static
name|void
name|initResponseMessageProperties
parameter_list|(
name|JMSMessageHeadersType
name|messageProperties
parameter_list|,
name|JMSMessageHeadersType
name|inMessageProperties
parameter_list|)
block|{
name|messageProperties
operator|.
name|setJMSDeliveryMode
argument_list|(
name|inMessageProperties
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|)
expr_stmt|;
name|messageProperties
operator|.
name|setJMSPriority
argument_list|(
name|inMessageProperties
operator|.
name|getJMSPriority
argument_list|()
argument_list|)
expr_stmt|;
name|messageProperties
operator|.
name|setSOAPJMSRequestURI
argument_list|(
name|inMessageProperties
operator|.
name|getSOAPJMSRequestURI
argument_list|()
argument_list|)
expr_stmt|;
name|messageProperties
operator|.
name|setSOAPJMSBindingVersion
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|isTimedOut
parameter_list|(
specifier|final
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
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|Destination
name|getReplyToDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|Message
name|inMessage2
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
name|inMessage2
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
name|inMessage2
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
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|(
name|session
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
else|else
block|{
return|return
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|(
name|session
argument_list|)
return|;
block|}
block|}
comment|/**      * Decides what correlationId to use for the reply by looking at the request headers      *       * @param request jms request message      * @return correlation id of request if set else message id from request      * @throws JMSException      */
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
return|return
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|request
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
condition|?
name|request
operator|.
name|getJMSMessageID
argument_list|()
else|:
name|request
operator|.
name|getJMSCorrelationID
argument_list|()
return|;
block|}
block|}
end_class

end_unit

