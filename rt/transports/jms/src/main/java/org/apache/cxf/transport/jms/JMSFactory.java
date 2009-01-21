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
name|QueueSession
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
name|JmsTemplate102
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
name|listener
operator|.
name|DefaultMessageListenerContainer102
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

begin_comment
comment|/**  * Factory to create JmsTemplates and JmsListeners from configuration and context information  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSFactory
block|{
specifier|private
name|JMSFactory
parameter_list|()
block|{     }
comment|/**      * Create JmsTemplate from configuration information. Most settings are taken from jmsConfig. The QoS      * settings in headers override the settings from jmsConfig      *       * @param jmsConfig configuration information      * @param headers context headers      * @return      */
specifier|public
specifier|static
name|JmsTemplate
name|createJmsTemplate
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|JMSMessageHeadersType
name|headers
parameter_list|)
block|{
name|JmsTemplate
name|jmsTemplate
init|=
name|jmsConfig
operator|.
name|isUseJms11
argument_list|()
condition|?
operator|new
name|JmsTemplate
argument_list|()
else|:
operator|new
name|JmsTemplate102
argument_list|()
decl_stmt|;
name|jmsTemplate
operator|.
name|setConnectionFactory
argument_list|(
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|setPubSubDomain
argument_list|(
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|setReceiveTimeout
argument_list|(
name|jmsConfig
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|setTimeToLive
argument_list|(
name|jmsConfig
operator|.
name|getTimeToLive
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|priority
init|=
operator|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetJMSPriority
argument_list|()
operator|)
condition|?
name|headers
operator|.
name|getJMSPriority
argument_list|()
else|:
name|jmsConfig
operator|.
name|getPriority
argument_list|()
decl_stmt|;
name|jmsTemplate
operator|.
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
name|int
name|deliveryMode
init|=
operator|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetJMSDeliveryMode
argument_list|()
operator|)
condition|?
name|headers
operator|.
name|getJMSDeliveryMode
argument_list|()
else|:
name|jmsConfig
operator|.
name|getDeliveryMode
argument_list|()
decl_stmt|;
name|jmsTemplate
operator|.
name|setDeliveryMode
argument_list|(
name|deliveryMode
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|setExplicitQosEnabled
argument_list|(
name|jmsConfig
operator|.
name|isExplicitQosEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|jmsTemplate
operator|.
name|setSessionTransacted
argument_list|(
name|jmsConfig
operator|.
name|isSessionTransacted
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmsConfig
operator|.
name|getDestinationResolver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsTemplate
operator|.
name|setDestinationResolver
argument_list|(
name|jmsConfig
operator|.
name|getDestinationResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jmsTemplate
return|;
block|}
comment|/**      * Create and start listener using configuration information from jmsConfig. Uses      * resolveOrCreateDestination to determine the destination for the listener.      *       * @param jmsConfig configuration information      * @param listenerHandler object to be called when a message arrives      * @param destinationName null for temp dest or a destination name      * @param messageSelectorPrefix prefix for the messageselector      * @return      */
specifier|public
specifier|static
name|DefaultMessageListenerContainer
name|createJmsListener
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|MessageListener
name|listenerHandler
parameter_list|,
name|String
name|destinationName
parameter_list|,
name|String
name|messageSelectorPrefix
parameter_list|)
block|{
name|DefaultMessageListenerContainer
name|jmsListener
init|=
name|jmsConfig
operator|.
name|isUseJms11
argument_list|()
condition|?
operator|new
name|DefaultMessageListenerContainer
argument_list|()
else|:
operator|new
name|DefaultMessageListenerContainer102
argument_list|()
decl_stmt|;
name|jmsListener
operator|.
name|setConcurrentConsumers
argument_list|(
name|jmsConfig
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setMaxConcurrentConsumers
argument_list|(
name|jmsConfig
operator|.
name|getMaxConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setPubSubDomain
argument_list|(
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setAutoStartup
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setConnectionFactory
argument_list|(
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setMessageSelector
argument_list|(
name|jmsConfig
operator|.
name|getMessageSelector
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setDurableSubscriptionName
argument_list|(
name|jmsConfig
operator|.
name|getDurableSubscriptionName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setSessionTransacted
argument_list|(
name|jmsConfig
operator|.
name|isSessionTransacted
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setTransactionManager
argument_list|(
name|jmsConfig
operator|.
name|getTransactionManager
argument_list|()
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|setMessageListener
argument_list|(
name|listenerHandler
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageSelectorPrefix
operator|!=
literal|null
operator|&&
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
condition|)
block|{
name|jmsListener
operator|.
name|setMessageSelector
argument_list|(
literal|"JMSCorrelationID LIKE '"
operator|+
name|messageSelectorPrefix
operator|+
literal|"%'"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jmsConfig
operator|.
name|getDestinationResolver
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsListener
operator|.
name|setDestinationResolver
argument_list|(
name|jmsConfig
operator|.
name|getDestinationResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jmsConfig
operator|.
name|getTaskExecutor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsListener
operator|.
name|setTaskExecutor
argument_list|(
name|jmsConfig
operator|.
name|getTaskExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JmsTemplate
name|jmsTemplate
init|=
name|createJmsTemplate
argument_list|(
name|jmsConfig
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Destination
name|dest
init|=
name|JMSFactory
operator|.
name|resolveOrCreateDestination
argument_list|(
name|jmsTemplate
argument_list|,
name|destinationName
argument_list|,
name|jmsConfig
operator|.
name|isPubSubDomain
argument_list|()
argument_list|)
decl_stmt|;
name|jmsListener
operator|.
name|setDestination
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|jmsListener
operator|.
name|initialize
argument_list|()
expr_stmt|;
return|return
name|jmsListener
return|;
block|}
comment|/**      * If the destinationName given is null then a temporary destination is created else the destination name      * is resolved using the resolver from the jmsConfig      *       * @param jmsTemplate template to use for session and resolver      * @param replyToDestinationName null for temporary destination or a destination name      * @param pubSubDomain true=pubSub, false=Queues      * @return resolved destination      */
specifier|private
specifier|static
name|Destination
name|resolveOrCreateDestination
parameter_list|(
specifier|final
name|JmsTemplate
name|jmsTemplate
parameter_list|,
specifier|final
name|String
name|replyToDestinationName
parameter_list|,
specifier|final
name|boolean
name|pubSubDomain
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
if|if
condition|(
name|replyToDestinationName
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|session
operator|instanceof
name|QueueSession
condition|)
block|{
comment|// For JMS 1.0.2
return|return
operator|(
operator|(
name|QueueSession
operator|)
name|session
operator|)
operator|.
name|createTemporaryQueue
argument_list|()
return|;
block|}
else|else
block|{
comment|// For JMS 1.1
return|return
name|session
operator|.
name|createTemporaryQueue
argument_list|()
return|;
block|}
block|}
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
name|replyToDestinationName
argument_list|,
name|pubSubDomain
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

