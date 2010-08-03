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
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|XAConnectionFactory
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
name|configuration
operator|.
name|ConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Required
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|task
operator|.
name|TaskExecutor
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
name|connection
operator|.
name|SingleConnectionFactory
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
name|connection
operator|.
name|SingleConnectionFactory102
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
name|destination
operator|.
name|DestinationResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jndi
operator|.
name|JndiTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|PlatformTransactionManager
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|JMSConfiguration
block|{
comment|/**      * The use of -1 is to make easier to determine       * if the setCacheLevel has been called.      */
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_VALUE
init|=
operator|-
literal|1
decl_stmt|;
specifier|static
specifier|final
name|boolean
name|DEFAULT_USEJMS11
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|usingEndpointInfo
init|=
literal|true
decl_stmt|;
specifier|private
name|JmsTemplate
name|jmsTemplate
decl_stmt|;
specifier|private
name|AbstractMessageListenerContainer
name|messageListenerContainer
decl_stmt|;
specifier|private
name|JndiTemplate
name|jndiTemplate
decl_stmt|;
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
specifier|private
name|DestinationResolver
name|destinationResolver
decl_stmt|;
specifier|private
name|PlatformTransactionManager
name|transactionManager
decl_stmt|;
specifier|private
name|boolean
name|wrapInSingleConnectionFactory
init|=
literal|true
decl_stmt|;
specifier|private
name|TaskExecutor
name|taskExecutor
decl_stmt|;
specifier|private
name|boolean
name|useJms11
init|=
name|DEFAULT_USEJMS11
decl_stmt|;
specifier|private
name|boolean
name|reconnectOnException
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|messageIdEnabled
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|messageTimestampEnabled
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|pubSubNoLocal
decl_stmt|;
specifier|private
name|Long
name|receiveTimeout
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
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|maxConcurrentConsumers
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
specifier|private
name|String
name|replyDestination
decl_stmt|;
specifier|private
name|String
name|replyToDestination
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
name|Boolean
name|useConduitIdSelector
decl_stmt|;
specifier|private
name|String
name|conduitSelectorPrefix
decl_stmt|;
specifier|private
name|boolean
name|autoResolveDestination
decl_stmt|;
specifier|private
name|long
name|recoveryInterval
init|=
name|DEFAULT_VALUE
decl_stmt|;
specifier|private
name|int
name|cacheLevel
init|=
name|DEFAULT_VALUE
decl_stmt|;
specifier|private
name|String
name|cacheLevelName
decl_stmt|;
specifier|private
name|Boolean
name|enforceSpec
decl_stmt|;
specifier|private
name|boolean
name|acceptMessagesWhileStopping
decl_stmt|;
specifier|private
name|boolean
name|jmsProviderTibcoEms
decl_stmt|;
comment|//For jms spec.
specifier|private
name|String
name|targetService
decl_stmt|;
specifier|private
name|String
name|requestURI
decl_stmt|;
specifier|private
name|ConnectionFactory
name|wrappedConnectionFactory
decl_stmt|;
specifier|private
name|JNDIConfiguration
name|jndiConfig
decl_stmt|;
specifier|public
name|void
name|ensureProperlyConfigured
parameter_list|(
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
parameter_list|)
block|{
if|if
condition|(
name|targetDestination
operator|==
literal|null
operator|||
name|getOrCreateWrappedConnectionFactory
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getCacheLevelName
parameter_list|()
block|{
return|return
name|cacheLevelName
return|;
block|}
specifier|public
name|void
name|setCacheLevelName
parameter_list|(
name|String
name|cacheLevelName
parameter_list|)
block|{
name|this
operator|.
name|cacheLevelName
operator|=
name|cacheLevelName
expr_stmt|;
block|}
specifier|public
name|int
name|getCacheLevel
parameter_list|()
block|{
return|return
name|cacheLevel
return|;
block|}
specifier|public
name|void
name|setCacheLevel
parameter_list|(
name|int
name|cacheLevel
parameter_list|)
block|{
name|this
operator|.
name|cacheLevel
operator|=
name|cacheLevel
expr_stmt|;
block|}
specifier|public
name|long
name|getRecoveryInterval
parameter_list|()
block|{
return|return
name|recoveryInterval
return|;
block|}
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|recoveryInterval
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|recoveryInterval
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAutoResolveDestination
parameter_list|()
block|{
return|return
name|autoResolveDestination
return|;
block|}
specifier|public
name|void
name|setAutoResolveDestination
parameter_list|(
name|boolean
name|autoResolveDestination
parameter_list|)
block|{
name|this
operator|.
name|autoResolveDestination
operator|=
name|autoResolveDestination
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsingEndpointInfo
parameter_list|()
block|{
return|return
name|this
operator|.
name|usingEndpointInfo
return|;
block|}
specifier|public
name|void
name|setUsingEndpointInfo
parameter_list|(
name|boolean
name|usingEndpointInfo
parameter_list|)
block|{
name|this
operator|.
name|usingEndpointInfo
operator|=
name|usingEndpointInfo
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMessageIdEnabled
parameter_list|()
block|{
return|return
name|messageIdEnabled
return|;
block|}
specifier|public
name|void
name|setMessageIdEnabled
parameter_list|(
name|boolean
name|messageIdEnabled
parameter_list|)
block|{
name|this
operator|.
name|messageIdEnabled
operator|=
name|messageIdEnabled
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMessageTimestampEnabled
parameter_list|()
block|{
return|return
name|messageTimestampEnabled
return|;
block|}
specifier|public
name|void
name|setMessageTimestampEnabled
parameter_list|(
name|boolean
name|messageTimestampEnabled
parameter_list|)
block|{
name|this
operator|.
name|messageTimestampEnabled
operator|=
name|messageTimestampEnabled
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
name|receiveTimeout
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
name|receiveTimeout
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
annotation|@
name|Required
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
name|isUseJms11
parameter_list|()
block|{
return|return
name|useJms11
return|;
block|}
specifier|public
name|void
name|setUseJms11
parameter_list|(
name|boolean
name|useJms11
parameter_list|)
block|{
name|this
operator|.
name|useJms11
operator|=
name|useJms11
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
name|PlatformTransactionManager
name|getTransactionManager
parameter_list|()
block|{
return|return
name|transactionManager
return|;
block|}
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|PlatformTransactionManager
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
name|getMaxConcurrentConsumers
parameter_list|()
block|{
return|return
name|maxConcurrentConsumers
return|;
block|}
specifier|public
name|void
name|setMaxConcurrentConsumers
parameter_list|(
name|int
name|maxConcurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|maxConcurrentConsumers
operator|=
name|maxConcurrentConsumers
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
name|TaskExecutor
name|getTaskExecutor
parameter_list|()
block|{
return|return
name|taskExecutor
return|;
block|}
specifier|public
name|void
name|setTaskExecutor
parameter_list|(
name|TaskExecutor
name|taskExecutor
parameter_list|)
block|{
name|this
operator|.
name|taskExecutor
operator|=
name|taskExecutor
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
if|if
condition|(
name|useConduitIdSelector
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|useConduitIdSelector
return|;
block|}
specifier|public
name|boolean
name|isSetUseConduitIdSelector
parameter_list|()
block|{
return|return
name|useConduitIdSelector
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|setJndiTemplate
parameter_list|(
name|JndiTemplate
name|jndiTemplate
parameter_list|)
block|{
name|this
operator|.
name|jndiTemplate
operator|=
name|jndiTemplate
expr_stmt|;
block|}
specifier|public
name|JndiTemplate
name|getJndiTemplate
parameter_list|()
block|{
return|return
name|jndiTemplate
return|;
block|}
specifier|public
name|JNDIConfiguration
name|getJndiConfig
parameter_list|()
block|{
return|return
name|jndiConfig
return|;
block|}
specifier|public
name|void
name|setJndiConfig
parameter_list|(
name|JNDIConfiguration
name|jndiConfig
parameter_list|)
block|{
name|this
operator|.
name|jndiConfig
operator|=
name|jndiConfig
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReconnectOnException
parameter_list|()
block|{
return|return
name|reconnectOnException
return|;
block|}
specifier|public
name|void
name|setReconnectOnException
parameter_list|(
name|boolean
name|reconnectOnException
parameter_list|)
block|{
name|this
operator|.
name|reconnectOnException
operator|=
name|reconnectOnException
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAcceptMessagesWhileStopping
parameter_list|()
block|{
return|return
name|acceptMessagesWhileStopping
return|;
block|}
specifier|public
name|void
name|setAcceptMessagesWhileStopping
parameter_list|(
name|boolean
name|acceptMessagesWhileStopping
parameter_list|)
block|{
name|this
operator|.
name|acceptMessagesWhileStopping
operator|=
name|acceptMessagesWhileStopping
expr_stmt|;
block|}
comment|/**      * Tries to creates a ConnectionFactory from jndi if none was set as a property      * by using the jndConfig. Then it determiens if the connectionFactory should be wrapped      * into a SingleConnectionFactory and wraps it if necessary. After the first call the      * same connectionFactory will be returned for all subsequent calls      *       * @return usable connectionFactory      */
specifier|public
specifier|synchronized
name|ConnectionFactory
name|getOrCreateWrappedConnectionFactory
parameter_list|()
block|{
if|if
condition|(
name|wrappedConnectionFactory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|connectionFactory
operator|==
literal|null
condition|)
block|{
name|connectionFactory
operator|=
name|JMSFactory
operator|.
name|getConnectionFactoryFromJndi
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
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
literal|"connectionFactory has not been initialized"
argument_list|)
throw|;
block|}
if|if
condition|(
name|wrapInSingleConnectionFactory
operator|&&
operator|!
operator|(
name|connectionFactory
operator|instanceof
name|SingleConnectionFactory
operator|)
condition|)
block|{
name|SingleConnectionFactory
name|scf
decl_stmt|;
if|if
condition|(
name|useJms11
condition|)
block|{
if|if
condition|(
name|connectionFactory
operator|instanceof
name|XAConnectionFactory
condition|)
block|{
name|scf
operator|=
operator|new
name|XASingleConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scf
operator|=
operator|new
name|SingleConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|scf
operator|=
operator|new
name|SingleConnectionFactory102
argument_list|(
name|connectionFactory
argument_list|,
name|pubSubDomain
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getDurableSubscriptionClientId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scf
operator|.
name|setClientId
argument_list|(
name|getDurableSubscriptionClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|scf
operator|.
name|setReconnectOnException
argument_list|(
name|isReconnectOnException
argument_list|()
argument_list|)
expr_stmt|;
name|wrappedConnectionFactory
operator|=
name|scf
expr_stmt|;
block|}
else|else
block|{
name|wrappedConnectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
block|}
return|return
name|wrappedConnectionFactory
return|;
block|}
specifier|public
name|ConnectionFactory
name|getWrappedConnectionFactory
parameter_list|()
block|{
return|return
name|wrappedConnectionFactory
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|destroyWrappedConnectionFactory
parameter_list|()
block|{
if|if
condition|(
name|wrappedConnectionFactory
operator|instanceof
name|SingleConnectionFactory
condition|)
block|{
operator|(
operator|(
name|SingleConnectionFactory
operator|)
name|wrappedConnectionFactory
operator|)
operator|.
name|destroy
argument_list|()
expr_stmt|;
if|if
condition|(
name|connectionFactory
operator|==
name|wrappedConnectionFactory
condition|)
block|{
name|connectionFactory
operator|=
literal|null
expr_stmt|;
block|}
name|wrappedConnectionFactory
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Only for tests      * @return      */
specifier|protected
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
specifier|public
name|boolean
name|isWrapInSingleConnectionFactory
parameter_list|()
block|{
return|return
name|wrapInSingleConnectionFactory
return|;
block|}
specifier|public
name|void
name|setWrapInSingleConnectionFactory
parameter_list|(
name|boolean
name|wrapInSingleConnectionFactory
parameter_list|)
block|{
name|this
operator|.
name|wrapInSingleConnectionFactory
operator|=
name|wrapInSingleConnectionFactory
expr_stmt|;
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
specifier|public
name|boolean
name|isEnforceSpec
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSetEnforceSpec
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|enforceSpec
return|;
block|}
specifier|public
name|void
name|setEnforceSpec
parameter_list|(
name|boolean
name|enforceSpec
parameter_list|)
block|{
name|this
operator|.
name|enforceSpec
operator|=
name|enforceSpec
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetEnforceSpec
parameter_list|()
block|{
return|return
name|this
operator|.
name|enforceSpec
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|setJmsTemplate
parameter_list|(
name|JmsTemplate
name|jmsTemplate
parameter_list|)
block|{
name|this
operator|.
name|jmsTemplate
operator|=
name|jmsTemplate
expr_stmt|;
block|}
specifier|public
name|JmsTemplate
name|getJmsTemplate
parameter_list|()
block|{
return|return
name|jmsTemplate
return|;
block|}
specifier|public
name|AbstractMessageListenerContainer
name|getMessageListenerContainer
parameter_list|()
block|{
return|return
name|messageListenerContainer
return|;
block|}
specifier|public
name|void
name|setMessageListenerContainer
parameter_list|(
name|AbstractMessageListenerContainer
name|messageListenerContainer
parameter_list|)
block|{
name|this
operator|.
name|messageListenerContainer
operator|=
name|messageListenerContainer
expr_stmt|;
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
block|}
end_class

end_unit

