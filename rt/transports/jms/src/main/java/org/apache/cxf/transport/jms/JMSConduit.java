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
name|security
operator|.
name|SecurityContext
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
name|jms
operator|.
name|util
operator|.
name|JMSListenerContainer
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
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  * JMSConduit is instantiated by the JMSTransportFactory which is selected by a client if the transport  * protocol starts with jms://. JMSConduit converts CXF Messages to JMS Messages and sends the request by   * using a JMS destination. If the Exchange is not oneway it then recevies the response and converts it to   * a CXF Message. This is then provided in the Exchange and also sent to the incomingObserver.  */
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
name|JMSListenerContainer
name|jmsListener
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
name|JMSBusLifeCycleListener
name|listener
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|private
name|Destination
name|staticReplyDestination
decl_stmt|;
specifier|public
name|JMSConduit
parameter_list|(
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
comment|/**      * Prepare the message to be sent. The message will be sent after the caller has written the payload to      * the OutputStream of the message and called the stream's close method. In the JMS case the      * JMSOutputStream will then call back the sendExchange method of this class. {@inheritDoc}      */
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
name|MessageStreamUtil
operator|.
name|prepareStream
argument_list|(
name|message
argument_list|,
name|isTextPayload
argument_list|,
name|this
argument_list|)
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
specifier|private
specifier|synchronized
name|void
name|getJMSListener
parameter_list|(
name|Destination
name|replyTo
parameter_list|)
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
name|createSimpleJmsListener
argument_list|(
name|jmsConfig
argument_list|,
name|connection
argument_list|,
name|this
argument_list|,
name|replyTo
argument_list|,
name|conduitId
argument_list|)
expr_stmt|;
name|addBusListener
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Send the JMS message and if the MEP is not oneway receive the response.      *       * @param exchange the Exchange containing the outgoing message      * @param request  the payload of the outgoing JMS message      */
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
name|jmsConfig
operator|.
name|ensureProperlyConfigured
argument_list|()
expr_stmt|;
name|assertIsNotTextMessageAndMtom
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
comment|//assertIsNotSyncAndTopicReply(exchange);
name|JMSMessageHeadersType
name|headers
init|=
name|getOrCreateJmsHeaders
argument_list|(
name|outMessage
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
name|assertIsNotAsyncSyncAndUserCID
argument_list|(
name|exchange
argument_list|,
name|userCID
argument_list|)
expr_stmt|;
name|ResourceCloser
name|closer
init|=
operator|new
name|ResourceCloser
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
block|{
name|connection
operator|=
name|JMSFactory
operator|.
name|createConnection
argument_list|(
name|jmsConfig
argument_list|)
expr_stmt|;
block|}
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
name|Destination
name|targetDest
init|=
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|(
name|session
argument_list|)
decl_stmt|;
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
condition|)
block|{
if|if
condition|(
operator|!
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|&&
name|staticReplyDestination
operator|==
literal|null
condition|)
block|{
name|staticReplyDestination
operator|=
name|jmsConfig
operator|.
name|getReplyDestination
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|getJMSListener
argument_list|(
name|staticReplyDestination
argument_list|)
expr_stmt|;
block|}
name|replyToDestination
operator|=
name|jmsConfig
operator|.
name|getReplyToDestination
argument_list|(
name|session
argument_list|,
name|headers
operator|.
name|getJMSReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
name|String
name|messageType
init|=
name|jmsConfig
operator|.
name|getMessageType
argument_list|()
decl_stmt|;
name|String
name|correlationId
init|=
name|createCorrelationId
argument_list|(
name|exchange
argument_list|,
name|userCID
argument_list|)
decl_stmt|;
if|if
condition|(
name|correlationId
operator|!=
literal|null
condition|)
block|{
name|correlationMap
operator|.
name|put
argument_list|(
name|correlationId
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
init|=
name|JMSMessageUtils
operator|.
name|asJMSMessage
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
name|correlationId
argument_list|,
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyToDestination
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setJMSReplyTo
argument_list|(
name|replyToDestination
argument_list|)
expr_stmt|;
block|}
name|JMSSender
name|sender
init|=
name|JMSFactory
operator|.
name|createJmsSender
argument_list|(
name|jmsConfig
argument_list|,
name|headers
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|exchange
init|)
block|{
name|sender
operator|.
name|sendMessage
argument_list|(
name|closer
argument_list|,
name|session
argument_list|,
name|targetDest
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"client sending request message "
operator|+
name|message
operator|.
name|getJMSMessageID
argument_list|()
operator|+
literal|" to "
operator|+
name|targetDest
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSMessageID
argument_list|(
name|message
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|correlationId
operator|==
literal|null
condition|)
block|{
comment|// Warning: We might loose the reply if it already arrived at this point
name|correlationId
operator|=
name|message
operator|.
name|getJMSMessageID
argument_list|()
expr_stmt|;
name|correlationMap
operator|.
name|put
argument_list|(
name|correlationId
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**              * If the message is not oneWay we will expect to receive a reply on the listener.              */
if|if
condition|(
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
operator|&&
operator|(
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|)
condition|)
block|{
name|Destination
name|replyDestination
init|=
name|staticReplyDestination
operator|!=
literal|null
condition|?
name|staticReplyDestination
else|:
name|replyToDestination
decl_stmt|;
name|javax
operator|.
name|jms
operator|.
name|Message
name|replyMessage
init|=
name|JMSUtil
operator|.
name|receive
argument_list|(
name|session
argument_list|,
name|replyDestination
argument_list|,
name|correlationId
argument_list|,
name|jmsConfig
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|,
name|jmsConfig
operator|.
name|isPubSubNoLocal
argument_list|()
argument_list|)
decl_stmt|;
name|correlationMap
operator|.
name|remove
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
name|doReplyMessage
argument_list|(
name|exchange
argument_list|,
name|replyMessage
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
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
specifier|private
name|void
name|assertIsNotAsyncSyncAndUserCID
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|userCID
parameter_list|)
block|{
if|if
condition|(
operator|!
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|&&
name|userCID
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"User CID can not be used for asynchronous exchanges"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|assertIsNotTextMessageAndMtom
parameter_list|(
specifier|final
name|Message
name|outMessage
parameter_list|)
block|{
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|void
name|assertIsNotSyncAndTopicReply
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|isSynchronous
argument_list|()
operator|&&
name|jmsConfig
operator|.
name|isReplyPubSubDomain
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Synchronous calls can not be combined with a response on a Topic"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|createCorrelationId
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|String
name|userCID
parameter_list|)
block|{
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
operator|!
name|jmsConfig
operator|.
name|isReplyPubSubDomain
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
comment|// in this case the correlation id will be set to
comment|// the message id later
name|correlationId
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|String
name|prefix
init|=
operator|(
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
operator|)
condition|?
name|jmsConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
operator|+
name|conduitId
else|:
name|jmsConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
decl_stmt|;
name|correlationId
operator|=
name|JMSUtil
operator|.
name|createCorrelationId
argument_list|(
name|prefix
argument_list|,
name|messageCount
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|correlationId
return|;
block|}
specifier|private
name|JMSMessageHeadersType
name|getOrCreateJmsHeaders
parameter_list|(
specifier|final
name|Message
name|outMessage
parameter_list|)
block|{
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
return|return
name|headers
return|;
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
try|try
block|{
name|String
name|correlationId
init|=
name|jmsMessage
operator|.
name|getJMSCorrelationID
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Received reply message with correlation id "
operator|+
name|correlationId
argument_list|)
expr_stmt|;
comment|// Try to correlate the incoming message with some timeout as it may have been
comment|// added to the map after the message was sent
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|exchange
operator|==
literal|null
operator|&&
name|count
operator|<
literal|100
condition|)
block|{
name|exchange
operator|=
name|correlationMap
operator|.
name|remove
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
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
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
name|JMSUtil
operator|.
name|convertJmsException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Interrupted while correlating"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Process the reply message      * @throws JMSException       */
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
throws|throws
name|JMSException
block|{
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
name|Message
name|inMessage
init|=
name|JMSMessageUtils
operator|.
name|asCXFMessage
argument_list|(
name|jmsMessage
argument_list|,
name|JMSConstants
operator|.
name|JMS_CLIENT_RESPONSE_HEADERS
argument_list|)
decl_stmt|;
name|SecurityContext
name|securityContext
init|=
name|JMSMessageUtils
operator|.
name|buildSecurityContext
argument_list|(
name|jmsMessage
argument_list|,
name|jmsConfig
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|securityContext
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|Object
name|responseCode
init|=
name|inMessage
operator|.
name|get
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
name|RESPONSE_CODE
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|put
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
name|RESPONSE_CODE
argument_list|,
name|responseCode
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
name|stop
argument_list|()
expr_stmt|;
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
name|ResourceCloser
operator|.
name|close
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|connection
operator|=
literal|null
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
name|ret
operator|.
name|booleanValue
argument_list|()
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

