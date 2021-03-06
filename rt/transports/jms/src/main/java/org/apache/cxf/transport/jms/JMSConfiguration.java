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
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|Message
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
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|TransactionManager
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|DestinationResolver
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
name|JMSDestinationResolver
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
name|JndiHelper
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|JMSConfiguration
block|{
comment|/**      * Default value to mark as unset      */
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_VALUE
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
specifier|volatile
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
specifier|private
name|Properties
name|jndiEnvironment
decl_stmt|;
specifier|private
name|String
name|connectionFactoryName
decl_stmt|;
specifier|private
name|String
name|userName
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|DestinationResolver
name|destinationResolver
init|=
operator|new
name|JMSDestinationResolver
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|pubSubNoLocal
decl_stmt|;
specifier|private
name|Long
name|clientReceiveTimeout
init|=
literal|60000L
decl_stmt|;
specifier|private
name|Long
name|serverReceiveTimeout
decl_stmt|;
specifier|private
name|boolean
name|ignoreTimeoutException
decl_stmt|;
specifier|private
name|boolean
name|explicitQosEnabled
decl_stmt|;
specifier|private
name|int
name|deliveryMode
init|=
name|Message
operator|.
name|DEFAULT_DELIVERY_MODE
decl_stmt|;
specifier|private
name|int
name|priority
init|=
name|Message
operator|.
name|DEFAULT_PRIORITY
decl_stmt|;
specifier|private
name|long
name|timeToLive
init|=
name|Message
operator|.
name|DEFAULT_TIME_TO_LIVE
decl_stmt|;
specifier|private
name|boolean
name|sessionTransacted
decl_stmt|;
specifier|private
name|boolean
name|createSecurityContext
init|=
literal|true
decl_stmt|;
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|maxSuspendedContinuations
init|=
name|DEFAULT_VALUE
decl_stmt|;
specifier|private
name|int
name|reconnectPercentOfMax
init|=
literal|70
decl_stmt|;
specifier|private
specifier|volatile
name|String
name|messageSelector
decl_stmt|;
specifier|private
name|boolean
name|subscriptionDurable
decl_stmt|;
specifier|private
name|String
name|durableSubscriptionClientId
decl_stmt|;
specifier|private
name|String
name|durableSubscriptionName
decl_stmt|;
specifier|private
name|String
name|targetDestination
decl_stmt|;
comment|/**      * Destination name to listen on for reply messages      */
specifier|private
name|String
name|replyDestination
decl_stmt|;
specifier|private
specifier|volatile
name|Destination
name|replyDestinationDest
decl_stmt|;
comment|/**      * Destination name to send out as replyTo address in the message      */
specifier|private
name|String
name|replyToDestination
decl_stmt|;
specifier|private
specifier|volatile
name|Destination
name|replyToDestinationDest
decl_stmt|;
specifier|private
name|String
name|messageType
init|=
name|JMSConstants
operator|.
name|TEXT_MESSAGE_TYPE
decl_stmt|;
specifier|private
name|boolean
name|pubSubDomain
decl_stmt|;
specifier|private
name|boolean
name|replyPubSubDomain
decl_stmt|;
comment|/**      *  Default to use conduitIdSelector as it allows to receive using a listener      *  which improves performance.      *  Set to false to use message id as correlation id for compatibility with IBM MQ.      */
specifier|private
name|boolean
name|useConduitIdSelector
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|conduitSelectorPrefix
decl_stmt|;
specifier|private
name|boolean
name|jmsProviderTibcoEms
decl_stmt|;
specifier|private
name|boolean
name|oneSessionPerConnection
decl_stmt|;
specifier|private
name|TransactionManager
name|transactionManager
decl_stmt|;
comment|// For jms spec. Do not configure manually
specifier|private
name|String
name|targetService
decl_stmt|;
specifier|private
name|String
name|requestURI
decl_stmt|;
specifier|private
name|int
name|retryInterval
init|=
literal|5000
decl_stmt|;
specifier|public
name|void
name|ensureProperlyConfigured
parameter_list|()
block|{
name|ConnectionFactory
name|cf
init|=
name|getConnectionFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|cf
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"connectionFactory may not be null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|targetDestination
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"targetDestination may not be null"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Properties
name|getJndiEnvironment
parameter_list|()
block|{
return|return
name|jndiEnvironment
return|;
block|}
specifier|public
name|void
name|setJndiEnvironment
parameter_list|(
name|Properties
name|jndiEnvironment
parameter_list|)
block|{
name|this
operator|.
name|jndiEnvironment
operator|=
name|jndiEnvironment
expr_stmt|;
block|}
specifier|public
name|String
name|getConnectionFactoryName
parameter_list|()
block|{
return|return
name|connectionFactoryName
return|;
block|}
specifier|public
name|void
name|setConnectionFactoryName
parameter_list|(
name|String
name|connectionFactoryName
parameter_list|)
block|{
name|this
operator|.
name|connectionFactoryName
operator|=
name|connectionFactoryName
expr_stmt|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPubSubNoLocal
parameter_list|()
block|{
return|return
name|pubSubNoLocal
return|;
block|}
specifier|public
name|void
name|setPubSubNoLocal
parameter_list|(
name|boolean
name|pubSubNoLocal
parameter_list|)
block|{
name|this
operator|.
name|pubSubNoLocal
operator|=
name|pubSubNoLocal
expr_stmt|;
block|}
specifier|public
name|Long
name|getReceiveTimeout
parameter_list|()
block|{
return|return
name|clientReceiveTimeout
return|;
block|}
specifier|public
name|void
name|setReceiveTimeout
parameter_list|(
name|Long
name|receiveTimeout
parameter_list|)
block|{
name|this
operator|.
name|clientReceiveTimeout
operator|=
name|receiveTimeout
expr_stmt|;
block|}
specifier|public
name|Long
name|getServerReceiveTimeout
parameter_list|()
block|{
return|return
name|serverReceiveTimeout
return|;
block|}
specifier|public
name|void
name|setServerReceiveTimeout
parameter_list|(
name|Long
name|receiveTimeout
parameter_list|)
block|{
name|this
operator|.
name|serverReceiveTimeout
operator|=
name|receiveTimeout
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExplicitQosEnabled
parameter_list|()
block|{
return|return
name|explicitQosEnabled
return|;
block|}
specifier|public
name|void
name|setExplicitQosEnabled
parameter_list|(
name|boolean
name|explicitQosEnabled
parameter_list|)
block|{
name|this
operator|.
name|explicitQosEnabled
operator|=
name|explicitQosEnabled
expr_stmt|;
block|}
specifier|public
name|int
name|getDeliveryMode
parameter_list|()
block|{
return|return
name|deliveryMode
return|;
block|}
specifier|public
name|void
name|setDeliveryMode
parameter_list|(
name|int
name|deliveryMode
parameter_list|)
block|{
name|this
operator|.
name|deliveryMode
operator|=
name|deliveryMode
expr_stmt|;
block|}
specifier|public
name|int
name|getPriority
parameter_list|()
block|{
return|return
name|priority
return|;
block|}
specifier|public
name|void
name|setPriority
parameter_list|(
name|int
name|priority
parameter_list|)
block|{
name|this
operator|.
name|priority
operator|=
name|priority
expr_stmt|;
block|}
specifier|public
name|long
name|getTimeToLive
parameter_list|()
block|{
return|return
name|timeToLive
return|;
block|}
specifier|public
name|void
name|setTimeToLive
parameter_list|(
name|long
name|timeToLive
parameter_list|)
block|{
name|this
operator|.
name|timeToLive
operator|=
name|timeToLive
expr_stmt|;
block|}
specifier|public
name|String
name|getMessageSelector
parameter_list|()
block|{
return|return
name|messageSelector
return|;
block|}
specifier|public
name|void
name|setMessageSelector
parameter_list|(
name|String
name|messageSelector
parameter_list|)
block|{
name|this
operator|.
name|messageSelector
operator|=
name|messageSelector
expr_stmt|;
block|}
specifier|public
name|void
name|setConduitSelectorPrefix
parameter_list|(
name|String
name|conduitSelectorPrefix
parameter_list|)
block|{
name|this
operator|.
name|conduitSelectorPrefix
operator|=
name|conduitSelectorPrefix
expr_stmt|;
block|}
specifier|public
name|String
name|getConduitSelectorPrefix
parameter_list|()
block|{
if|if
condition|(
name|conduitSelectorPrefix
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|conduitSelectorPrefix
return|;
block|}
specifier|public
name|boolean
name|isSetConduitSelectorPrefix
parameter_list|()
block|{
return|return
name|conduitSelectorPrefix
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSubscriptionDurable
parameter_list|()
block|{
return|return
name|subscriptionDurable
return|;
block|}
specifier|public
name|void
name|setSubscriptionDurable
parameter_list|(
name|boolean
name|subscriptionDurable
parameter_list|)
block|{
name|this
operator|.
name|subscriptionDurable
operator|=
name|subscriptionDurable
expr_stmt|;
block|}
specifier|public
name|String
name|getDurableSubscriptionName
parameter_list|()
block|{
return|return
name|durableSubscriptionName
return|;
block|}
specifier|public
name|void
name|setDurableSubscriptionName
parameter_list|(
name|String
name|durableSubscriptionName
parameter_list|)
block|{
name|this
operator|.
name|durableSubscriptionName
operator|=
name|durableSubscriptionName
expr_stmt|;
block|}
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|connectionFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Required property connectionfactory was not set"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetDestination
parameter_list|()
block|{
return|return
name|targetDestination
return|;
block|}
specifier|public
name|void
name|setTargetDestination
parameter_list|(
name|String
name|targetDestination
parameter_list|)
block|{
name|this
operator|.
name|targetDestination
operator|=
name|targetDestination
expr_stmt|;
block|}
specifier|public
name|String
name|getReplyDestination
parameter_list|()
block|{
return|return
name|replyDestination
return|;
block|}
specifier|public
name|void
name|setReplyDestination
parameter_list|(
name|String
name|replyDestination
parameter_list|)
block|{
name|this
operator|.
name|replyDestination
operator|=
name|replyDestination
expr_stmt|;
block|}
specifier|public
name|String
name|getReplyToDestination
parameter_list|()
block|{
return|return
name|replyToDestination
operator|!=
literal|null
condition|?
name|replyToDestination
else|:
name|replyDestination
return|;
block|}
specifier|public
name|void
name|setReplyToDestination
parameter_list|(
name|String
name|replyToDestination
parameter_list|)
block|{
name|this
operator|.
name|replyToDestination
operator|=
name|replyToDestination
expr_stmt|;
block|}
specifier|public
name|String
name|getMessageType
parameter_list|()
block|{
return|return
name|messageType
return|;
block|}
specifier|public
name|void
name|setMessageType
parameter_list|(
name|String
name|messageType
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPubSubDomain
parameter_list|()
block|{
return|return
name|pubSubDomain
return|;
block|}
specifier|public
name|void
name|setPubSubDomain
parameter_list|(
name|boolean
name|pubSubDomain
parameter_list|)
block|{
name|this
operator|.
name|pubSubDomain
operator|=
name|pubSubDomain
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReplyPubSubDomain
parameter_list|()
block|{
return|return
name|replyPubSubDomain
return|;
block|}
specifier|public
name|void
name|setReplyPubSubDomain
parameter_list|(
name|boolean
name|replyPubSubDomain
parameter_list|)
block|{
name|this
operator|.
name|replyPubSubDomain
operator|=
name|replyPubSubDomain
expr_stmt|;
block|}
specifier|public
name|DestinationResolver
name|getDestinationResolver
parameter_list|()
block|{
return|return
name|destinationResolver
return|;
block|}
specifier|public
name|void
name|setDestinationResolver
parameter_list|(
name|DestinationResolver
name|destinationResolver
parameter_list|)
block|{
name|this
operator|.
name|destinationResolver
operator|=
name|destinationResolver
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSessionTransacted
parameter_list|()
block|{
return|return
name|sessionTransacted
return|;
block|}
specifier|public
name|void
name|setSessionTransacted
parameter_list|(
name|boolean
name|sessionTransacted
parameter_list|)
block|{
name|this
operator|.
name|sessionTransacted
operator|=
name|sessionTransacted
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCreateSecurityContext
parameter_list|()
block|{
return|return
name|createSecurityContext
return|;
block|}
specifier|public
name|void
name|setCreateSecurityContext
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|this
operator|.
name|createSecurityContext
operator|=
name|b
expr_stmt|;
block|}
comment|/**      * For compatibility with old spring based code      * @param transactionManager      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|Object
name|transactionManager
parameter_list|)
block|{     }
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
specifier|public
name|int
name|getMaxSuspendedContinuations
parameter_list|()
block|{
return|return
name|maxSuspendedContinuations
return|;
block|}
specifier|public
name|void
name|setMaxSuspendedContinuations
parameter_list|(
name|int
name|maxSuspendedContinuations
parameter_list|)
block|{
name|this
operator|.
name|maxSuspendedContinuations
operator|=
name|maxSuspendedContinuations
expr_stmt|;
block|}
specifier|public
name|int
name|getReconnectPercentOfMax
parameter_list|()
block|{
return|return
name|reconnectPercentOfMax
return|;
block|}
specifier|public
name|void
name|setReconnectPercentOfMax
parameter_list|(
name|int
name|reconnectPercentOfMax
parameter_list|)
block|{
name|this
operator|.
name|reconnectPercentOfMax
operator|=
name|reconnectPercentOfMax
expr_stmt|;
block|}
specifier|public
name|void
name|setUseConduitIdSelector
parameter_list|(
name|boolean
name|useConduitIdSelector
parameter_list|)
block|{
name|this
operator|.
name|useConduitIdSelector
operator|=
name|useConduitIdSelector
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseConduitIdSelector
parameter_list|()
block|{
return|return
name|useConduitIdSelector
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|void
name|setReconnectOnException
parameter_list|(
name|boolean
name|reconnectOnException
parameter_list|)
block|{
comment|// Ignore. We always reconnect on exceptions
block|}
specifier|public
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
name|ConnectionFactory
name|factory
init|=
name|connectionFactory
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|factory
operator|=
name|connectionFactory
expr_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
name|getConnectionFactoryFromJndi
argument_list|()
expr_stmt|;
name|connectionFactory
operator|=
name|factory
expr_stmt|;
block|}
block|}
block|}
return|return
name|factory
return|;
block|}
comment|/**      * Retrieve connection factory from JNDI      *      * @return the connection factory from JNDI      */
specifier|private
name|ConnectionFactory
name|getConnectionFactoryFromJndi
parameter_list|()
block|{
if|if
condition|(
name|getJndiEnvironment
argument_list|()
operator|==
literal|null
operator|||
name|getConnectionFactoryName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
operator|new
name|JndiHelper
argument_list|(
name|getJndiEnvironment
argument_list|()
argument_list|)
operator|.
name|lookup
argument_list|(
name|getConnectionFactoryName
argument_list|()
argument_list|,
name|ConnectionFactory
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getDurableSubscriptionClientId
parameter_list|()
block|{
return|return
name|durableSubscriptionClientId
return|;
block|}
specifier|public
name|void
name|setDurableSubscriptionClientId
parameter_list|(
name|String
name|durableSubscriptionClientId
parameter_list|)
block|{
name|this
operator|.
name|durableSubscriptionClientId
operator|=
name|durableSubscriptionClientId
expr_stmt|;
block|}
specifier|public
name|void
name|setTargetService
parameter_list|(
name|String
name|targetService
parameter_list|)
block|{
name|this
operator|.
name|targetService
operator|=
name|targetService
expr_stmt|;
block|}
specifier|public
name|String
name|getTargetService
parameter_list|()
block|{
return|return
name|targetService
return|;
block|}
specifier|public
name|void
name|setRequestURI
parameter_list|(
name|String
name|requestURI
parameter_list|)
block|{
name|this
operator|.
name|requestURI
operator|=
name|requestURI
expr_stmt|;
block|}
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
return|return
name|requestURI
return|;
block|}
comment|/** * @return Returns the jmsProviderTibcoEms.      */
specifier|public
name|boolean
name|isJmsProviderTibcoEms
parameter_list|()
block|{
return|return
name|jmsProviderTibcoEms
return|;
block|}
comment|/**      * @param jmsProviderTibcoEms The jmsProviderTibcoEms to set.      */
specifier|public
name|void
name|setJmsProviderTibcoEms
parameter_list|(
name|boolean
name|jmsProviderTibcoEms
parameter_list|)
block|{
name|this
operator|.
name|jmsProviderTibcoEms
operator|=
name|jmsProviderTibcoEms
expr_stmt|;
block|}
specifier|public
name|boolean
name|isOneSessionPerConnection
parameter_list|()
block|{
return|return
name|oneSessionPerConnection
return|;
block|}
specifier|public
name|void
name|setOneSessionPerConnection
parameter_list|(
name|boolean
name|oneSessionPerConnection
parameter_list|)
block|{
name|this
operator|.
name|oneSessionPerConnection
operator|=
name|oneSessionPerConnection
expr_stmt|;
block|}
specifier|public
specifier|static
name|Destination
name|resolveOrCreateDestination
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|,
specifier|final
name|DestinationResolver
name|resolver
parameter_list|,
specifier|final
name|String
name|replyToDestinationName
parameter_list|,
specifier|final
name|boolean
name|pubSubDomain
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|replyToDestinationName
operator|==
literal|null
condition|)
block|{
return|return
name|session
operator|.
name|createTemporaryQueue
argument_list|()
return|;
block|}
return|return
name|resolver
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|replyToDestinationName
argument_list|,
name|pubSubDomain
argument_list|)
return|;
block|}
specifier|public
name|Destination
name|getReplyToDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|userDestination
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|userDestination
operator|!=
literal|null
condition|)
block|{
return|return
name|destinationResolver
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|userDestination
argument_list|,
name|replyPubSubDomain
argument_list|)
return|;
block|}
if|if
condition|(
name|replyToDestination
operator|==
literal|null
condition|)
block|{
return|return
name|getReplyDestination
argument_list|(
name|session
argument_list|)
return|;
block|}
name|Destination
name|result
init|=
name|replyToDestinationDest
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|result
operator|=
name|replyToDestinationDest
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|destinationResolver
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|replyToDestination
argument_list|,
name|replyPubSubDomain
argument_list|)
expr_stmt|;
name|replyToDestinationDest
operator|=
name|result
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
name|Destination
name|getReplyDestination
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|this
operator|.
name|replyDestinationDest
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|this
operator|.
name|replyDestinationDest
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|replyDestinationDest
operator|=
name|getReplyDestinationInternal
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
operator|.
name|replyDestinationDest
return|;
block|}
specifier|private
name|Destination
name|getReplyDestinationInternal
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|replyDestination
operator|==
literal|null
condition|?
name|session
operator|.
name|createTemporaryQueue
argument_list|()
else|:
name|destinationResolver
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|replyDestination
argument_list|,
name|replyPubSubDomain
argument_list|)
return|;
block|}
specifier|public
name|void
name|resetCachedReplyDestination
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|this
operator|.
name|replyDestinationDest
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|Destination
name|getTargetDestination
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|JMSException
block|{
return|return
name|destinationResolver
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|targetDestination
argument_list|,
name|pubSubDomain
argument_list|)
return|;
block|}
specifier|public
name|Destination
name|getReplyDestination
parameter_list|(
name|Session
name|session
parameter_list|,
name|String
name|replyToName
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|replyToName
operator|!=
literal|null
condition|)
block|{
return|return
name|destinationResolver
operator|.
name|resolveDestinationName
argument_list|(
name|session
argument_list|,
name|replyToName
argument_list|,
name|replyPubSubDomain
argument_list|)
return|;
block|}
return|return
name|getReplyDestination
argument_list|(
name|session
argument_list|)
return|;
block|}
specifier|public
name|TransactionManager
name|getTransactionManager
parameter_list|()
block|{
return|return
name|this
operator|.
name|transactionManager
return|;
block|}
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|TransactionManager
name|transactionManager
parameter_list|)
block|{
name|this
operator|.
name|transactionManager
operator|=
name|transactionManager
expr_stmt|;
block|}
specifier|public
name|int
name|getRetryInterval
parameter_list|()
block|{
return|return
name|this
operator|.
name|retryInterval
return|;
block|}
specifier|public
name|void
name|setRetryInterval
parameter_list|(
name|int
name|retryInterval
parameter_list|)
block|{
name|this
operator|.
name|retryInterval
operator|=
name|retryInterval
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIgnoreTimeoutException
parameter_list|()
block|{
return|return
name|ignoreTimeoutException
return|;
block|}
specifier|public
name|void
name|setIgnoreTimeoutException
parameter_list|(
name|boolean
name|ignoreTimeoutException
parameter_list|)
block|{
name|this
operator|.
name|ignoreTimeoutException
operator|=
name|ignoreTimeoutException
expr_stmt|;
block|}
block|}
end_class

end_unit

