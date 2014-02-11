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
name|Connection
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
name|MessageListenerContainer
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
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|UserCredentialsConnectionFactoryAdapter
import|;
end_import

begin_comment
comment|/**  * Factory to create jms helper objects from configuration and context information  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSFactory
block|{
specifier|static
specifier|final
name|String
name|MESSAGE_ENDPOINT_FACTORY
init|=
literal|"MessageEndpointFactory"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MDB_TRANSACTED_METHOD
init|=
literal|"MDBTransactedMethod"
decl_stmt|;
comment|//private static final Logger LOG = LogUtils.getL7dLogger(JMSFactory.class);
specifier|private
name|JMSFactory
parameter_list|()
block|{     }
comment|/**      * Retrieve connection factory from jndi, wrap it in a UserCredentialsConnectionFactoryAdapter,      * set username and password and return the ConnectionFactory      *       * @param jmsConfig      * @param jndiConfig      * @return      */
specifier|static
name|ConnectionFactory
name|getConnectionFactoryFromJndi
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|JNDIConfiguration
name|jndiConfig
init|=
name|jmsConfig
operator|.
name|getJndiConfig
argument_list|()
decl_stmt|;
if|if
condition|(
name|jndiConfig
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|connectionFactoryName
init|=
name|jndiConfig
operator|.
name|getJndiConnectionFactoryName
argument_list|()
decl_stmt|;
if|if
condition|(
name|connectionFactoryName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|userName
init|=
name|jndiConfig
operator|.
name|getConnectionUserName
argument_list|()
decl_stmt|;
name|String
name|password
init|=
name|jndiConfig
operator|.
name|getConnectionPassword
argument_list|()
decl_stmt|;
try|try
block|{
name|ConnectionFactory
name|cf
init|=
operator|(
name|ConnectionFactory
operator|)
name|jmsConfig
operator|.
name|getJndiTemplate
argument_list|()
operator|.
name|lookup
argument_list|(
name|connectionFactoryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|userName
operator|!=
literal|null
condition|)
block|{
name|UserCredentialsConnectionFactoryAdapter
name|uccf
init|=
operator|new
name|UserCredentialsConnectionFactoryAdapter
argument_list|()
decl_stmt|;
name|uccf
operator|.
name|setUsername
argument_list|(
name|userName
argument_list|)
expr_stmt|;
name|uccf
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|uccf
operator|.
name|setTargetConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
name|cf
operator|=
name|uccf
expr_stmt|;
block|}
return|return
name|cf
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
comment|/**      * Create JmsTemplate from configuration information. Most settings are taken from jmsConfig. The QoS      * settings in headers override the settings from jmsConfig      *       * @param jmsConfig configuration information      * @param messageProperties context headers      * @return      */
specifier|public
specifier|static
name|JMSSender
name|createJmsSender
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|JMSMessageHeadersType
name|messageProperties
parameter_list|)
block|{
name|JMSSender
name|sender
init|=
operator|new
name|JMSSender
argument_list|()
decl_stmt|;
name|long
name|timeToLive
init|=
operator|(
name|messageProperties
operator|!=
literal|null
operator|&&
name|messageProperties
operator|.
name|isSetTimeToLive
argument_list|()
operator|)
condition|?
name|messageProperties
operator|.
name|getTimeToLive
argument_list|()
else|:
name|jmsConfig
operator|.
name|getTimeToLive
argument_list|()
decl_stmt|;
name|sender
operator|.
name|setTimeToLive
argument_list|(
name|timeToLive
argument_list|)
expr_stmt|;
name|int
name|priority
init|=
operator|(
name|messageProperties
operator|!=
literal|null
operator|&&
name|messageProperties
operator|.
name|isSetJMSPriority
argument_list|()
operator|)
condition|?
name|messageProperties
operator|.
name|getJMSPriority
argument_list|()
else|:
name|jmsConfig
operator|.
name|getPriority
argument_list|()
decl_stmt|;
name|sender
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
name|messageProperties
operator|!=
literal|null
operator|&&
name|messageProperties
operator|.
name|isSetJMSDeliveryMode
argument_list|()
operator|)
condition|?
name|messageProperties
operator|.
name|getJMSDeliveryMode
argument_list|()
else|:
name|jmsConfig
operator|.
name|getDeliveryMode
argument_list|()
decl_stmt|;
name|sender
operator|.
name|setDeliveryMode
argument_list|(
name|deliveryMode
argument_list|)
expr_stmt|;
name|sender
operator|.
name|setExplicitQosEnabled
argument_list|(
name|jmsConfig
operator|.
name|isExplicitQosEnabled
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sender
return|;
block|}
comment|/**      * Create and start listener using configuration information from jmsConfig. Uses      * resolveOrCreateDestination to determine the destination for the listener.      *       * @param ei the EndpointInfo for the listener      * @param jmsConfig configuration information      * @param listenerHandler object to be called when a message arrives      * @param destination to listen on      * @return      */
comment|/*     protected static JMSListenerContainer createJmsListener(EndpointInfo ei,                                                                     JMSConfiguration jmsConfig,                                                                     MessageListener listenerHandler,                                                                     Destination destination) {                  DefaultMessageListenerContainer jmsListener = null;                  //Check to see if transport is being used in JCA RA with XA         Method method = ei.getProperty(MDB_TRANSACTED_METHOD,                                        java.lang.reflect.Method.class);         MessageEndpointFactory factory = ei.getProperty(MESSAGE_ENDPOINT_FACTORY,                                        MessageEndpointFactory.class);         if (method != null&& jmsConfig.getConnectionFactory() instanceof XAConnectionFactory) {             jmsListener = new JCATransactionalMessageListenerContainer(factory, method);          } else {             jmsListener = new DefaultMessageListenerContainer();         }                  jmsListener.setConcurrentConsumers(jmsConfig.getConcurrentConsumers());         jmsListener.setMaxConcurrentConsumers(jmsConfig.getMaxConcurrentConsumers());                  jmsListener.setPubSubNoLocal(jmsConfig.isPubSubNoLocal());                  jmsListener.setConnectionFactory(jmsConfig.getConnectionFactory());         jmsListener.setSubscriptionDurable(jmsConfig.isSubscriptionDurable());         jmsListener.setClientId(jmsConfig.getDurableSubscriptionClientId());         jmsListener.setDurableSubscriptionName(jmsConfig.getDurableSubscriptionName());         jmsListener.setSessionTransacted(jmsConfig.isSessionTransacted());         jmsListener.setTransactionManager(jmsConfig.getTransactionManager());         jmsListener.setMessageListener(listenerHandler);         if (listenerHandler instanceof JMSDestination) {             //timeout on server side?             if (jmsConfig.getServerReceiveTimeout() != null) {                 jmsListener.setReceiveTimeout(jmsConfig.getServerReceiveTimeout());             }             jmsListener.setPubSubDomain(jmsConfig.isPubSubDomain());         } else {             if (jmsConfig.getReceiveTimeout() != null) {                 jmsListener.setReceiveTimeout(jmsConfig.getReceiveTimeout());             }             jmsListener.setPubSubDomain(jmsConfig.isReplyPubSubDomain());         }         if (jmsConfig.getRecoveryInterval() != JMSConfiguration.DEFAULT_VALUE) {             jmsListener.setRecoveryInterval(jmsConfig.getRecoveryInterval());         }         if (jmsConfig.getCacheLevelName() != null&& (jmsConfig.getCacheLevelName().trim().length()> 0)) {             jmsListener.setCacheLevelName(jmsConfig.getCacheLevelName());         } else if (jmsConfig.getCacheLevel() != JMSConfiguration.DEFAULT_VALUE) {             jmsListener.setCacheLevel(jmsConfig.getCacheLevel());         }         if (jmsListener.getCacheLevel()>= DefaultMessageListenerContainer.CACHE_CONSUMER&& jmsConfig.getMaxSuspendedContinuations()> 0) {             LOG.info("maxSuspendedContinuations value will be ignored - "                      + ", please set cacheLevel to the value less than "                      + " org.springframework.jms.listener.DefaultMessageListenerContainer.CACHE_CONSUMER");         }         if (jmsConfig.isAcceptMessagesWhileStopping()) {             jmsListener.setAcceptMessagesWhileStopping(jmsConfig.isAcceptMessagesWhileStopping());         }         String messageSelector = getMessageSelector(jmsConfig, null);         jmsListener.setMessageSelector(messageSelector);                  jmsListener.setTaskExecutor(jmsConfig.getTaskExecutor());                  jmsListener.setDestination(destination);         jmsListener.initialize();         jmsListener.start();         return new SpringJMSListenerAdapter(jmsListener);     }     */
specifier|private
specifier|static
name|String
name|getMessageSelector
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|String
name|conduitId
parameter_list|)
block|{
name|String
name|staticSelectorPrefix
init|=
name|jmsConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
decl_stmt|;
name|String
name|conduitIdSt
init|=
name|jmsConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
operator|&&
name|conduitId
operator|!=
literal|null
condition|?
name|conduitId
else|:
literal|""
decl_stmt|;
name|String
name|correlationIdPrefix
init|=
name|staticSelectorPrefix
operator|+
name|conduitIdSt
decl_stmt|;
return|return
name|correlationIdPrefix
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
literal|"JMSCorrelationID LIKE '"
operator|+
name|correlationIdPrefix
operator|+
literal|"%'"
return|;
block|}
specifier|public
specifier|static
name|JMSListenerContainer
name|createTargetDestinationListener
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|MessageListener
name|listenerHandler
parameter_list|)
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Connection
name|connection
init|=
name|createConnection
argument_list|(
name|jmsConfig
argument_list|)
decl_stmt|;
name|session
operator|=
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
expr_stmt|;
name|Destination
name|destination
init|=
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|MessageListenerContainer
name|container
init|=
operator|new
name|MessageListenerContainer
argument_list|(
name|connection
argument_list|,
name|destination
argument_list|,
name|listenerHandler
argument_list|)
decl_stmt|;
name|container
operator|.
name|setMessageSelector
argument_list|(
name|jmsConfig
operator|.
name|getMessageSelector
argument_list|()
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|container
return|;
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
finally|finally
block|{
name|ResourceCloser
operator|.
name|close
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|JMSListenerContainer
name|createSimpleJmsListener
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|Connection
name|connection
parameter_list|,
name|MessageListener
name|listenerHandler
parameter_list|,
name|Destination
name|destination
parameter_list|,
name|String
name|conduitId
parameter_list|)
block|{
name|MessageListenerContainer
name|container
init|=
operator|new
name|MessageListenerContainer
argument_list|(
name|connection
argument_list|,
name|destination
argument_list|,
name|listenerHandler
argument_list|)
decl_stmt|;
name|String
name|messageSelector
init|=
name|getMessageSelector
argument_list|(
name|jmsConfig
argument_list|,
name|conduitId
argument_list|)
decl_stmt|;
name|container
operator|.
name|setMessageSelector
argument_list|(
name|messageSelector
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|container
return|;
block|}
specifier|public
specifier|static
name|Connection
name|createConnection
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|)
throws|throws
name|JMSException
block|{
name|Connection
name|connection
init|=
name|jmsConfig
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|createConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|jmsConfig
operator|.
name|getDurableSubscriptionClientId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|setClientID
argument_list|(
name|jmsConfig
operator|.
name|getDurableSubscriptionClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|connection
return|;
block|}
block|}
end_class

end_unit

