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
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|ConcurrentHashMap
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
name|atomic
operator|.
name|AtomicLong
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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|configuration
operator|.
name|ConfigurationException
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
name|EndpointReferenceType
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
name|listener
operator|.
name|DefaultMessageListenerContainer
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

begin_comment
comment|/**  * JMSConduit is instantiated by the JMSTransportfactory which is selected by a client if the transport  * protocol starts with jms:// JMSConduit converts CXF Messages to JMS Messages and sends the request by using  * a JMS destination. If the Exchange is not oneway it then recevies the response and converts it to a CXF  * Message. This is then provided in the Exchange and also sent to the incomingObserver  */
end_comment

begin_class
specifier|public
class|class
name|JMSConduit
extends|extends
name|AbstractConduit
implements|implements
name|JMSExchangeSender
implements|,
name|MessageListener
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSConduit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CORRELATED
init|=
name|JMSConduit
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".correlated"
decl_stmt|;
specifier|private
name|EndpointInfo
name|endpointInfo
decl_stmt|;
specifier|private
name|JMSConfiguration
name|jmsConfig
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Exchange
argument_list|>
name|correlationMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|DefaultMessageListenerContainer
name|jmsListener
decl_stmt|;
specifier|private
name|DefaultMessageListenerContainer
name|allListener
decl_stmt|;
specifier|private
name|String
name|conduitId
decl_stmt|;
specifier|private
name|AtomicLong
name|messageCount
decl_stmt|;
specifier|private
name|int
name|outstandingAsync
decl_stmt|;
specifier|private
name|JMSBusLifeCycleListener
name|listener
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|JMSConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|,
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
name|this
operator|.
name|endpointInfo
operator|=
name|endpointInfo
expr_stmt|;
name|conduitId
operator|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"-"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|messageCount
operator|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prepare the message for send out. The message will be sent after the caller has written the payload to      * the OutputStream of the message and calls the close method of the stream. In the JMS case the      * JMSOutputStream will then call back the sendExchange method of this class. {@inheritDoc}      */
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
literal|".jms-conduit"
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
literal|"INSUFFICIENT_CONFIGURATION_CONDUIT"
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
name|boolean
name|isTextPayload
init|=
name|JMSConstants
operator|.
name|TEXT_MESSAGE_TYPE
operator|.
name|equals
argument_list|(
name|jmsConfig
operator|.
name|getMessageType
argument_list|()
argument_list|)
decl_stmt|;
name|JMSOutputStream
name|out
init|=
operator|new
name|JMSOutputStream
argument_list|(
name|this
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|isTextPayload
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|AbstractMessageListenerContainer
name|getJMSListener
parameter_list|()
block|{
if|if
condition|(
name|jmsListener
operator|==
literal|null
condition|)
block|{
name|jmsListener
operator|=
name|JMSFactory
operator|.
name|createJmsListener
argument_list|(
name|jmsConfig
argument_list|,
name|this
argument_list|,
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|()
argument_list|,
name|conduitId
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|addBusListener
argument_list|()
expr_stmt|;
block|}
operator|++
name|outstandingAsync
expr_stmt|;
return|return
name|jmsListener
return|;
block|}
specifier|private
specifier|synchronized
name|AbstractMessageListenerContainer
name|getAllListener
parameter_list|()
block|{
if|if
condition|(
name|allListener
operator|==
literal|null
condition|)
block|{
name|allListener
operator|=
name|JMSFactory
operator|.
name|createJmsListener
argument_list|(
name|jmsConfig
argument_list|,
name|this
argument_list|,
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|addBusListener
argument_list|()
expr_stmt|;
block|}
operator|++
name|outstandingAsync
expr_stmt|;
return|return
name|allListener
return|;
block|}
comment|/**      * Send the JMS Request out and if not oneWay receive the response      *       * @param outMessage      * @param request      * @return inMessage      */
specifier|public
name|void
name|sendExchange
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|request
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"JMSConduit send message"
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|==
literal|null
condition|?
name|exchange
operator|.
name|getOutFaultMessage
argument_list|()
else|:
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|outMessage
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Exchange to be sent has no outMessage"
argument_list|)
throw|;
block|}
name|boolean
name|isTextPayload
init|=
name|JMSConstants
operator|.
name|TEXT_MESSAGE_TYPE
operator|.
name|equals
argument_list|(
name|jmsConfig
operator|.
name|getMessageType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTextPayload
operator|&&
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|outMessage
operator|.
name|getContextualProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MTOM_ENABLED
argument_list|)
argument_list|)
operator|&&
name|outMessage
operator|.
name|getAttachments
argument_list|()
operator|!=
literal|null
operator|&&
name|outMessage
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
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
literal|"INVALID_MESSAGE_TYPE"
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|JMSMessageHeadersType
name|headers
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
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|JMSMessageHeadersType
argument_list|()
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
name|String
name|replyTo
init|=
name|headers
operator|.
name|getJMSReplyTo
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyTo
operator|==
literal|null
condition|)
block|{
name|replyTo
operator|=
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|()
expr_stmt|;
block|}
specifier|final
name|JmsTemplate
name|jmsTemplate
init|=
name|JMSFactory
operator|.
name|createJmsTemplate
argument_list|(
name|jmsConfig
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|String
name|userCID
init|=
name|headers
operator|.
name|getJMSCorrelationID
argument_list|()
decl_stmt|;
name|boolean
name|messageIdPattern
init|=
literal|false
decl_stmt|;
name|String
name|correlationId
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
if|if
condition|(
name|userCID
operator|!=
literal|null
condition|)
block|{
name|correlationId
operator|=
name|userCID
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|jmsConfig
operator|.
name|isSetConduitSelectorPrefix
argument_list|()
operator|&&
operator|(
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|||
name|exchange
operator|.
name|isOneWay
argument_list|()
operator|)
operator|&&
operator|(
operator|!
name|jmsConfig
operator|.
name|isSetUseConduitIdSelector
argument_list|()
operator|||
operator|!
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
operator|)
condition|)
block|{
name|messageIdPattern
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
condition|)
block|{
name|correlationId
operator|=
name|JMSUtils
operator|.
name|createCorrelationId
argument_list|(
name|jmsConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
operator|+
name|conduitId
argument_list|,
name|messageCount
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|correlationId
operator|=
name|JMSUtils
operator|.
name|createCorrelationId
argument_list|(
name|jmsConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
argument_list|,
name|messageCount
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Destination
name|replyToDestination
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
operator|||
operator|!
name|jmsConfig
operator|.
name|isEnforceSpec
argument_list|()
operator|&&
name|isSetReplyTo
argument_list|(
name|outMessage
argument_list|)
operator|&&
name|replyTo
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|||
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|replyToDestination
operator|=
name|JMSFactory
operator|.
name|resolveOrCreateDestination
argument_list|(
name|jmsTemplate
argument_list|,
name|replyTo
argument_list|,
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|userCID
operator|==
literal|null
operator|||
operator|!
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
condition|)
block|{
name|replyToDestination
operator|=
name|getJMSListener
argument_list|()
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|replyToDestination
operator|=
name|getAllListener
argument_list|()
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|final
name|String
name|cid
init|=
name|correlationId
decl_stmt|;
specifier|final
name|Destination
name|rtd
init|=
name|replyToDestination
decl_stmt|;
class|class
name|JMSConduitMessageCreator
implements|implements
name|MessageCreator
block|{
specifier|private
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMessage
decl_stmt|;
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
name|String
name|messageType
init|=
name|jmsConfig
operator|.
name|getMessageType
argument_list|()
decl_stmt|;
name|Destination
name|destination
init|=
name|rtd
decl_stmt|;
name|String
name|replyToAddress
init|=
name|jmsConfig
operator|.
name|getReplyToDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyToAddress
operator|!=
literal|null
condition|)
block|{
name|destination
operator|=
name|JMSFactory
operator|.
name|resolveOrCreateDestination
argument_list|(
name|jmsTemplate
argument_list|,
name|replyToAddress
argument_list|,
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|jmsMessage
operator|=
name|JMSUtils
operator|.
name|buildJMSMessageFromCXFMessage
argument_list|(
name|jmsConfig
argument_list|,
name|outMessage
argument_list|,
name|request
argument_list|,
name|messageType
argument_list|,
name|session
argument_list|,
name|destination
argument_list|,
name|cid
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|&&
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|correlationMap
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"client sending request: "
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
return|return
name|jmsMessage
return|;
block|}
specifier|public
name|String
name|getMessageID
parameter_list|()
block|{
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|jmsMessage
operator|.
name|getJMSMessageID
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
name|JMSConduitMessageCreator
name|messageCreator
init|=
operator|new
name|JMSConduitMessageCreator
argument_list|()
decl_stmt|;
comment|/**          * If the message is not oneWay we will expect to receive a reply on the listener.           *           */
if|if
condition|(
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|exchange
init|)
block|{
name|jmsTemplate
operator|.
name|send
argument_list|(
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|()
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageIdPattern
condition|)
block|{
name|correlationId
operator|=
name|messageCreator
operator|.
name|getMessageID
argument_list|()
expr_stmt|;
block|}
name|headers
operator|.
name|setJMSMessageID
argument_list|(
name|messageCreator
operator|.
name|getMessageID
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|messageSelector
init|=
literal|"JMSCorrelationID = '"
operator|+
name|correlationId
operator|+
literal|"'"
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|javax
operator|.
name|jms
operator|.
name|Message
name|replyMessage
init|=
name|jmsTemplate
operator|.
name|receiveSelected
argument_list|(
name|replyToDestination
argument_list|,
name|messageSelector
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyMessage
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Timeout receiving message with correlationId "
operator|+
name|correlationId
argument_list|)
throw|;
block|}
else|else
block|{
name|doReplyMessage
argument_list|(
name|exchange
argument_list|,
name|replyMessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|jmsTemplate
operator|.
name|send
argument_list|(
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|()
argument_list|,
name|messageCreator
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSMessageID
argument_list|(
name|messageCreator
operator|.
name|getMessageID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|JMSBusLifeCycleListener
implements|implements
name|BusLifeCycleListener
block|{
specifier|final
name|WeakReference
argument_list|<
name|JMSConduit
argument_list|>
name|ref
decl_stmt|;
name|BusLifeCycleManager
name|blcm
decl_stmt|;
name|JMSBusLifeCycleListener
parameter_list|(
name|JMSConduit
name|c
parameter_list|,
name|BusLifeCycleManager
name|b
parameter_list|)
block|{
name|ref
operator|=
operator|new
name|WeakReference
argument_list|<
name|JMSConduit
argument_list|>
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|blcm
operator|=
name|b
expr_stmt|;
name|blcm
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{         }
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{         }
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
name|unreg
argument_list|()
expr_stmt|;
name|blcm
operator|=
literal|null
expr_stmt|;
name|JMSConduit
name|c
init|=
name|ref
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|listener
operator|=
literal|null
expr_stmt|;
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|unreg
parameter_list|()
block|{
if|if
condition|(
name|blcm
operator|!=
literal|null
condition|)
block|{
name|blcm
operator|.
name|unregisterLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|addBusListener
parameter_list|()
block|{
if|if
condition|(
name|listener
operator|==
literal|null
operator|&&
name|bus
operator|!=
literal|null
condition|)
block|{
name|BusLifeCycleManager
name|blcm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|blcm
operator|!=
literal|null
condition|)
block|{
name|listener
operator|=
operator|new
name|JMSBusLifeCycleListener
argument_list|(
name|this
argument_list|,
name|blcm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * When a message is received on the reply destination the correlation map is searched for the      * correlationId. If it is found the message is converted to a CXF message and the thread sending the      * request is notified {@inheritDoc}      */
specifier|public
name|void
name|onMessage
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMessage
parameter_list|)
block|{
name|String
name|correlationId
decl_stmt|;
try|try
block|{
name|correlationId
operator|=
name|jmsMessage
operator|.
name|getJMSCorrelationID
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
name|JmsUtils
operator|.
name|convertJmsAccessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|Exchange
name|exchange
init|=
name|correlationMap
operator|.
name|remove
argument_list|(
name|correlationId
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Could not correlate message with correlationId "
operator|+
name|correlationId
argument_list|)
expr_stmt|;
return|return;
block|}
name|doReplyMessage
argument_list|(
name|exchange
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
name|maybeShutdownListeners
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|maybeShutdownListenersInternal
parameter_list|()
block|{
if|if
condition|(
name|outstandingAsync
operator|==
literal|0
condition|)
block|{
name|shutdownListeners
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|maybeShutdownListeners
parameter_list|()
block|{
if|if
condition|(
name|outstandingAsync
operator|>
literal|0
condition|)
block|{
operator|--
name|outstandingAsync
expr_stmt|;
block|}
if|if
condition|(
name|outstandingAsync
operator|==
literal|0
condition|)
block|{
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
operator|.
name|getAutomaticWorkQueue
argument_list|()
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
name|maybeShutdownListenersInternal
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Here we just deal with the reply message      */
specifier|public
name|void
name|doReplyMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMessage
parameter_list|)
block|{
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
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
literal|"client received reply: "
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
try|try
block|{
name|JMSUtils
operator|.
name|populateIncomingContext
argument_list|(
name|jmsMessage
argument_list|,
name|inMessage
argument_list|,
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|,
name|jmsConfig
argument_list|)
expr_stmt|;
name|JMSUtils
operator|.
name|retrieveAndSetPayload
argument_list|(
name|inMessage
argument_list|,
name|jmsMessage
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
if|if
condition|(
name|exchange
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|exchange
init|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|CORRELATED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|incomingObserver
operator|!=
literal|null
condition|)
block|{
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"can't get the right encoding information "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|shutdownListeners
parameter_list|()
block|{
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|listener
operator|.
name|unreg
argument_list|()
expr_stmt|;
name|listener
operator|=
literal|null
expr_stmt|;
block|}
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
name|jmsListener
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|allListener
operator|!=
literal|null
condition|)
block|{
name|allListener
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|allListener
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
block|{
name|shutdownListeners
argument_list|()
expr_stmt|;
name|jmsConfig
operator|.
name|destroyWrappedConnectionFactory
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"JMSConduit closed "
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
specifier|protected
specifier|static
name|boolean
name|isSetReplyTo
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|ret
init|=
operator|(
name|Boolean
operator|)
name|message
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_SET_REPLY_TO
argument_list|)
decl_stmt|;
return|return
name|ret
operator|==
literal|null
operator|||
operator|(
name|ret
operator|!=
literal|null
operator|&&
name|ret
operator|.
name|booleanValue
argument_list|()
operator|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|finalize
parameter_list|()
throws|throws
name|Throwable
block|{
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|finalize
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

