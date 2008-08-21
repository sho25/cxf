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
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|QueueSender
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Topic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TopicPublisher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|configuration
operator|.
name|Configurable
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
name|Configurer
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
name|io
operator|.
name|CachedOutputStream
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_class
specifier|public
class|class
name|JMSConduit
extends|extends
name|AbstractConduit
implements|implements
name|Configurable
implements|,
name|JMSTransport
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|BASE_BEAN_NAME_SUFFIX
init|=
literal|".jms-conduit-base"
decl_stmt|;
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
name|JMSConduit
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|JMSTransportBase
name|base
decl_stmt|;
specifier|protected
name|ClientConfig
name|clientConfig
decl_stmt|;
specifier|protected
name|ClientBehaviorPolicyType
name|runtimePolicy
decl_stmt|;
specifier|protected
name|AddressType
name|address
decl_stmt|;
specifier|protected
name|SessionPoolType
name|sessionPool
decl_stmt|;
specifier|public
name|JMSConduit
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|this
argument_list|(
name|b
argument_list|,
name|endpointInfo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JMSConduit
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|base
operator|=
operator|new
name|JMSTransportBase
argument_list|(
name|b
argument_list|,
name|endpointInfo
argument_list|,
literal|false
argument_list|,
name|BASE_BEAN_NAME_SUFFIX
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|initConfig
argument_list|()
expr_stmt|;
block|}
comment|// prepare the message for send out , not actually send out the message
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
name|getLogger
argument_list|()
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
try|try
block|{
if|if
condition|(
literal|null
operator|==
name|base
operator|.
name|sessionFactory
condition|)
block|{
name|JMSProviderHub
operator|.
name|connect
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|jmsex
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
literal|"JMS connect failed with JMSException : "
argument_list|,
name|jmsex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|jmsex
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|ne
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
literal|"JMS connect failed with NamingException : "
argument_list|,
name|ne
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|ne
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|base
operator|.
name|sessionFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|java
operator|.
name|lang
operator|.
name|IllegalStateException
argument_list|(
literal|"JMSClientTransport not connected"
argument_list|)
throw|;
block|}
try|try
block|{
name|boolean
name|isOneWay
init|=
literal|false
decl_stmt|;
comment|//test if the message is oneway message
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ex
condition|)
block|{
name|isOneWay
operator|=
name|ex
operator|.
name|isOneWay
argument_list|()
expr_stmt|;
block|}
comment|//get the pooledSession with response expected
name|PooledSession
name|pooledSession
init|=
name|base
operator|.
name|sessionFactory
operator|.
name|get
argument_list|(
operator|!
name|isOneWay
argument_list|)
decl_stmt|;
comment|// put the PooledSession into the outMessage
name|message
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_POOLEDSESSION
argument_list|,
name|pooledSession
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|jmsex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|jmsex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
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
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
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
literal|"JMSConduit closed "
argument_list|)
expr_stmt|;
comment|// ensure resources held by session factory are released
comment|//
if|if
condition|(
name|base
operator|.
name|sessionFactory
operator|!=
literal|null
condition|)
block|{
name|base
operator|.
name|sessionFactory
operator|.
name|shutdown
argument_list|()
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
comment|/**      * Receive mechanics.      *      * @param pooledSession the shared JMS resources      * @param inMessage       * @retrun the response buffer      */
specifier|private
name|Object
name|receive
parameter_list|(
name|PooledSession
name|pooledSession
parameter_list|,
name|Message
name|outMessage
parameter_list|,
name|Message
name|inMessage
parameter_list|)
throws|throws
name|JMSException
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
name|long
name|timeout
init|=
name|getClientConfig
argument_list|()
operator|.
name|getClientReceiveTimeout
argument_list|()
decl_stmt|;
name|Long
name|receiveTimeout
init|=
operator|(
name|Long
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_RECEIVE_TIMEOUT
argument_list|)
decl_stmt|;
if|if
condition|(
name|receiveTimeout
operator|!=
literal|null
condition|)
block|{
name|timeout
operator|=
name|receiveTimeout
operator|.
name|longValue
argument_list|()
expr_stmt|;
block|}
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMessage
init|=
name|pooledSession
operator|.
name|consumer
argument_list|()
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|getLogger
argument_list|()
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
if|if
condition|(
name|jmsMessage
operator|!=
literal|null
condition|)
block|{
name|base
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
argument_list|)
expr_stmt|;
name|result
operator|=
name|base
operator|.
name|unmarshal
argument_list|(
name|jmsMessage
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
else|else
block|{
name|String
name|error
init|=
literal|"JMSClientTransport.receive() timed out. No message available."
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|error
argument_list|)
expr_stmt|;
comment|//TODO: Review what exception should we throw.
throw|throw
operator|new
name|JMSException
argument_list|(
name|error
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|connected
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Destination
name|target
parameter_list|,
name|javax
operator|.
name|jms
operator|.
name|Destination
name|reply
parameter_list|,
name|JMSSessionFactory
name|factory
parameter_list|)
block|{
name|base
operator|.
name|connected
argument_list|(
name|target
argument_list|,
name|reply
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|base
operator|.
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".jms-conduit"
return|;
block|}
specifier|private
name|void
name|initConfig
parameter_list|()
block|{
name|this
operator|.
name|address
operator|=
name|base
operator|.
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
operator|new
name|AddressType
argument_list|()
argument_list|,
name|AddressType
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|sessionPool
operator|=
name|base
operator|.
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
operator|new
name|SessionPoolType
argument_list|()
argument_list|,
name|SessionPoolType
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|clientConfig
operator|=
name|base
operator|.
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
operator|new
name|ClientConfig
argument_list|()
argument_list|,
name|ClientConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|runtimePolicy
operator|=
name|base
operator|.
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
operator|new
name|ClientBehaviorPolicyType
argument_list|()
argument_list|,
name|ClientBehaviorPolicyType
operator|.
name|class
argument_list|)
expr_stmt|;
name|Configurer
name|configurer
init|=
name|base
operator|.
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|configurer
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isTextPayload
parameter_list|()
block|{
return|return
name|JMSConstants
operator|.
name|TEXT_MESSAGE_TYPE
operator|.
name|equals
argument_list|(
name|getRuntimePolicy
argument_list|()
operator|.
name|getMessageType
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|AddressType
name|getJMSAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|void
name|setJMSAddress
parameter_list|(
name|AddressType
name|a
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|a
expr_stmt|;
block|}
specifier|public
name|ClientConfig
name|getClientConfig
parameter_list|()
block|{
return|return
name|clientConfig
return|;
block|}
specifier|public
name|void
name|setClientConfig
parameter_list|(
name|ClientConfig
name|clientConfig
parameter_list|)
block|{
name|this
operator|.
name|clientConfig
operator|=
name|clientConfig
expr_stmt|;
block|}
specifier|public
name|ClientBehaviorPolicyType
name|getRuntimePolicy
parameter_list|()
block|{
return|return
name|runtimePolicy
return|;
block|}
specifier|public
name|void
name|setRuntimePolicy
parameter_list|(
name|ClientBehaviorPolicyType
name|runtimePolicy
parameter_list|)
block|{
name|this
operator|.
name|runtimePolicy
operator|=
name|runtimePolicy
expr_stmt|;
block|}
specifier|public
name|SessionPoolType
name|getSessionPool
parameter_list|()
block|{
return|return
name|sessionPool
return|;
block|}
specifier|public
name|void
name|setSessionPool
parameter_list|(
name|SessionPoolType
name|sessionPool
parameter_list|)
block|{
name|this
operator|.
name|sessionPool
operator|=
name|sessionPool
expr_stmt|;
block|}
specifier|private
class|class
name|JMSOutputStream
extends|extends
name|CachedOutputStream
block|{
specifier|private
name|Message
name|outMessage
decl_stmt|;
specifier|private
name|javax
operator|.
name|jms
operator|.
name|Message
name|jmsMessage
decl_stmt|;
specifier|private
name|PooledSession
name|pooledSession
decl_stmt|;
specifier|private
name|boolean
name|isOneWay
decl_stmt|;
specifier|public
name|JMSOutputStream
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|outMessage
operator|=
name|m
expr_stmt|;
name|pooledSession
operator|=
operator|(
name|PooledSession
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_POOLEDSESSION
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{
comment|//do nothing here
block|}
specifier|protected
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|isOneWay
operator|=
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
expr_stmt|;
name|commitOutputMessage
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isOneWay
condition|)
block|{
name|handleResponse
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|jmsex
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
literal|"JMS connect failed with JMSException : "
argument_list|,
name|jmsex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|jmsex
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
name|base
operator|.
name|sessionFactory
operator|.
name|recycle
argument_list|(
name|pooledSession
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{                      }
specifier|private
name|void
name|commitOutputMessage
parameter_list|()
throws|throws
name|JMSException
block|{
name|javax
operator|.
name|jms
operator|.
name|Destination
name|replyTo
init|=
name|pooledSession
operator|.
name|destination
argument_list|()
decl_stmt|;
comment|//TODO setting up the responseExpected
comment|//We don't want to send temp queue in
comment|//replyTo header for oneway calls
if|if
condition|(
name|isOneWay
operator|&&
operator|(
name|getJMSAddress
argument_list|()
operator|.
name|getJndiReplyDestinationName
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
name|replyTo
operator|=
literal|null
expr_stmt|;
block|}
name|Object
name|request
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isTextPayload
argument_list|()
condition|)
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|2048
argument_list|)
decl_stmt|;
name|this
operator|.
name|writeCacheTo
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|request
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|request
operator|=
name|getBytes
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|JMSException
name|ex2
init|=
operator|new
name|JMSException
argument_list|(
literal|"Error creating request"
argument_list|)
decl_stmt|;
name|ex2
operator|.
name|setLinkedException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex2
throw|;
block|}
if|if
condition|(
name|getLogger
argument_list|()
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
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
literal|"Conduit Request is :["
operator|+
name|request
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|jmsMessage
operator|=
name|base
operator|.
name|marshal
argument_list|(
name|request
argument_list|,
name|pooledSession
operator|.
name|session
argument_list|()
argument_list|,
name|replyTo
argument_list|,
name|getRuntimePolicy
argument_list|()
operator|.
name|getMessageType
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
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
name|int
name|deliveryMode
init|=
name|base
operator|.
name|getJMSDeliveryMode
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|int
name|priority
init|=
name|base
operator|.
name|getJMSPriority
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|String
name|correlationID
init|=
name|base
operator|.
name|getCorrelationId
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|long
name|ttl
init|=
name|base
operator|.
name|getTimeToLive
argument_list|(
name|headers
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttl
operator|<=
literal|0
condition|)
block|{
name|ttl
operator|=
name|getClientConfig
argument_list|()
operator|.
name|getMessageTimeToLive
argument_list|()
expr_stmt|;
block|}
name|base
operator|.
name|setMessageProperties
argument_list|(
name|headers
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|base
operator|.
name|addProtocolHeaders
argument_list|(
name|jmsMessage
argument_list|,
name|protHeaders
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isOneWay
condition|)
block|{
name|String
name|id
init|=
name|pooledSession
operator|.
name|getCorrelationID
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|correlationID
operator|!=
literal|null
condition|)
block|{
name|String
name|error
init|=
literal|"User cannot set JMSCorrelationID when "
operator|+
literal|"making a request/reply invocation using "
operator|+
literal|"a static replyTo Queue."
decl_stmt|;
throw|throw
operator|new
name|JMSException
argument_list|(
name|error
argument_list|)
throw|;
block|}
name|correlationID
operator|=
name|id
expr_stmt|;
block|}
block|}
if|if
condition|(
name|correlationID
operator|!=
literal|null
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//No message correlation id is set. Whatever comeback will be accepted as responses.
comment|// We assume that it will only happen in case of the temp. reply queue.
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
literal|"client sending request: "
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
comment|//getting  Destination Style
if|if
condition|(
name|base
operator|.
name|isDestinationStyleQueue
argument_list|()
condition|)
block|{
name|QueueSender
name|sender
init|=
operator|(
name|QueueSender
operator|)
name|pooledSession
operator|.
name|producer
argument_list|()
decl_stmt|;
name|sender
operator|.
name|setTimeToLive
argument_list|(
name|ttl
argument_list|)
expr_stmt|;
name|sender
operator|.
name|send
argument_list|(
operator|(
name|Queue
operator|)
name|base
operator|.
name|targetDestination
argument_list|,
name|jmsMessage
argument_list|,
name|deliveryMode
argument_list|,
name|priority
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|TopicPublisher
name|publisher
init|=
operator|(
name|TopicPublisher
operator|)
name|pooledSession
operator|.
name|producer
argument_list|()
decl_stmt|;
name|publisher
operator|.
name|setTimeToLive
argument_list|(
name|ttl
argument_list|)
expr_stmt|;
name|publisher
operator|.
name|publish
argument_list|(
operator|(
name|Topic
operator|)
name|base
operator|.
name|targetDestination
argument_list|,
name|jmsMessage
argument_list|,
name|deliveryMode
argument_list|,
name|priority
argument_list|,
name|ttl
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|handleResponse
parameter_list|()
throws|throws
name|IOException
block|{
comment|// REVISIT distinguish decoupled case or oneway call
name|Object
name|response
init|=
literal|null
decl_stmt|;
comment|//TODO if outMessage need to get the response
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|//set the message header back to the incomeMessage
comment|//inMessage.put(JMSConstants.JMS_CLIENT_RESPONSE_HEADERS,
comment|//              outMessage.get(JMSConstants.JMS_CLIENT_RESPONSE_HEADERS));
try|try
block|{
name|response
operator|=
name|receive
argument_list|(
name|pooledSession
argument_list|,
name|outMessage
argument_list|,
name|inMessage
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|jmsex
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
literal|"JMS connect failed with JMSException : "
argument_list|,
name|jmsex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|jmsex
operator|.
name|toString
argument_list|()
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
literal|"The Response Message is : ["
operator|+
name|response
operator|+
literal|"]"
argument_list|)
expr_stmt|;
comment|// setup the inMessage response stream
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|String
condition|)
block|{
name|String
name|requestString
init|=
operator|(
name|String
operator|)
name|response
decl_stmt|;
name|bytes
operator|=
name|requestString
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|bytes
operator|=
operator|(
name|byte
index|[]
operator|)
name|response
expr_stmt|;
block|}
name|inMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"incoming observer is "
operator|+
name|incomingObserver
argument_list|)
expr_stmt|;
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Represented decoupled response endpoint.      */
specifier|protected
class|class
name|DecoupledDestination
implements|implements
name|Destination
block|{
specifier|protected
name|MessageObserver
name|decoupledMessageObserver
decl_stmt|;
specifier|private
name|EndpointReferenceType
name|address
decl_stmt|;
name|DecoupledDestination
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|,
name|MessageObserver
name|incomingObserver
parameter_list|)
block|{
name|address
operator|=
name|ref
expr_stmt|;
name|decoupledMessageObserver
operator|=
name|incomingObserver
expr_stmt|;
block|}
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
return|return
name|address
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
name|addr
parameter_list|)
throws|throws
name|IOException
block|{
comment|// shouldn't be called on decoupled endpoint
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
specifier|synchronized
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|decoupledMessageObserver
operator|=
name|observer
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|decoupledMessageObserver
return|;
block|}
block|}
block|}
end_class

end_unit

