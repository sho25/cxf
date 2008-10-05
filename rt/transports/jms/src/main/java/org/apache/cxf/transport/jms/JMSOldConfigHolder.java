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
name|Enumeration
import|;
end_import

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
name|ConnectionFactory
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
name|UserCredentialsConnectionFactoryAdapter
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
name|JndiDestinationResolver
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

begin_class
specifier|public
class|class
name|JMSOldConfigHolder
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
name|JMSOldConfigHolder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ClientConfig
name|clientConfig
decl_stmt|;
specifier|private
name|ClientBehaviorPolicyType
name|runtimePolicy
decl_stmt|;
specifier|private
name|AddressType
name|address
decl_stmt|;
specifier|private
name|SessionPoolType
name|sessionPool
decl_stmt|;
specifier|private
name|JMSConfiguration
name|jmsConfig
decl_stmt|;
specifier|private
name|ServerConfig
name|serverConfig
decl_stmt|;
specifier|private
name|ServerBehaviorPolicyType
name|serverBehavior
decl_stmt|;
specifier|private
name|ConnectionFactory
name|getConnectionFactoryFromJndi
parameter_list|(
name|String
name|connectionFactoryName
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|,
name|JndiTemplate
name|jt
parameter_list|)
block|{
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
try|try
block|{
name|ConnectionFactory
name|connectionFactory
init|=
operator|(
name|ConnectionFactory
operator|)
name|jt
operator|.
name|lookup
argument_list|(
name|connectionFactoryName
argument_list|)
decl_stmt|;
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
name|connectionFactory
argument_list|)
expr_stmt|;
name|SingleConnectionFactory
name|scf
init|=
operator|new
name|SingleConnectionFactory
argument_list|()
decl_stmt|;
name|scf
operator|.
name|setTargetConnectionFactory
argument_list|(
name|uccf
argument_list|)
expr_stmt|;
return|return
name|scf
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
name|JMSConfiguration
name|createJMSConfigurationFromEndpointInfo
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|boolean
name|isConduit
parameter_list|)
block|{
name|jmsConfig
operator|=
operator|new
name|JMSConfiguration
argument_list|()
expr_stmt|;
comment|// Retrieve configuration information that was extracted from the WSDL
name|address
operator|=
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
name|clientConfig
operator|=
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
name|runtimePolicy
operator|=
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
name|serverConfig
operator|=
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
operator|new
name|ServerConfig
argument_list|()
argument_list|,
name|ServerConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|sessionPool
operator|=
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
name|serverBehavior
operator|=
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
operator|new
name|ServerBehaviorPolicyType
argument_list|()
argument_list|,
name|ServerBehaviorPolicyType
operator|.
name|class
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
operator|(
name|isConduit
condition|?
literal|".jms-conduit"
else|:
literal|".jms-destination"
operator|)
decl_stmt|;
comment|// Try to retrieve configuration information from the spring
comment|// config. Search for a conduit or destination with name=endpoint name + ".jms-conduit"
comment|// or ".jms-destination"
name|Configurer
name|configurer
init|=
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
name|name
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|JndiTemplate
name|jt
init|=
operator|new
name|JndiTemplate
argument_list|()
decl_stmt|;
name|jt
operator|.
name|setEnvironment
argument_list|(
name|JMSOldConfigHolder
operator|.
name|getInitialContextEnv
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|ConnectionFactory
name|cf
init|=
name|getConnectionFactoryFromJndi
argument_list|(
name|address
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|,
name|address
operator|.
name|getConnectionUserName
argument_list|()
argument_list|,
name|address
operator|.
name|getConnectionPassword
argument_list|()
argument_list|,
name|jt
argument_list|)
decl_stmt|;
name|boolean
name|pubSubDomain
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|isSetDestinationStyle
argument_list|()
condition|)
block|{
name|pubSubDomain
operator|=
name|DestinationStyleType
operator|.
name|TOPIC
operator|==
name|address
operator|.
name|getDestinationStyle
argument_list|()
expr_stmt|;
block|}
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setDurableSubscriptionName
argument_list|(
name|serverBehavior
operator|.
name|getDurableSubscriberName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setExplicitQosEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// jmsConfig.setMessageIdEnabled(messageIdEnabled);
name|jmsConfig
operator|.
name|setMessageSelector
argument_list|(
name|serverBehavior
operator|.
name|getMessageSelector
argument_list|()
argument_list|)
expr_stmt|;
comment|// jmsConfig.setMessageTimestampEnabled(messageTimestampEnabled);
if|if
condition|(
name|runtimePolicy
operator|.
name|isSetMessageType
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setMessageType
argument_list|(
name|runtimePolicy
operator|.
name|getMessageType
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// jmsConfig.setOneWay(oneWay);
comment|// jmsConfig.setPriority(priority);
name|jmsConfig
operator|.
name|setPubSubDomain
argument_list|(
name|pubSubDomain
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setPubSubNoLocal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReceiveTimeout
argument_list|(
name|clientConfig
operator|.
name|getClientReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setSubscriptionDurable
argument_list|(
name|serverBehavior
operator|.
name|isSetDurableSubscriberName
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|timeToLive
init|=
name|isConduit
condition|?
name|clientConfig
operator|.
name|getMessageTimeToLive
argument_list|()
else|:
name|serverConfig
operator|.
name|getMessageTimeToLive
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setTimeToLive
argument_list|(
name|timeToLive
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setUseJms11
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|boolean
name|useJndi
init|=
name|address
operator|.
name|isSetJndiDestinationName
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setUseJndi
argument_list|(
name|useJndi
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setSessionTransacted
argument_list|(
name|serverBehavior
operator|.
name|isSetTransactional
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|useJndi
condition|)
block|{
comment|// Setup Destination jndi destination resolver
specifier|final
name|JndiDestinationResolver
name|jndiDestinationResolver
init|=
operator|new
name|JndiDestinationResolver
argument_list|()
decl_stmt|;
name|jndiDestinationResolver
operator|.
name|setJndiTemplate
argument_list|(
name|jt
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setDestinationResolver
argument_list|(
name|jndiDestinationResolver
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setTargetDestination
argument_list|(
name|address
operator|.
name|getJndiDestinationName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReplyDestination
argument_list|(
name|address
operator|.
name|getJndiReplyDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Use the default dynamic destination resolver
name|jmsConfig
operator|.
name|setTargetDestination
argument_list|(
name|address
operator|.
name|getJmsDestinationName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReplyDestination
argument_list|(
name|address
operator|.
name|getJmsReplyDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
return|return
name|jmsConfig
return|;
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
name|AddressType
name|getAddress
parameter_list|()
block|{
return|return
name|address
return|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|AddressType
name|address
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
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
specifier|public
name|ServerConfig
name|getServerConfig
parameter_list|()
block|{
return|return
name|serverConfig
return|;
block|}
specifier|public
name|void
name|setServerConfig
parameter_list|(
name|ServerConfig
name|serverConfig
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|=
name|serverConfig
expr_stmt|;
block|}
specifier|public
name|ServerBehaviorPolicyType
name|getServerBehavior
parameter_list|()
block|{
return|return
name|serverBehavior
return|;
block|}
specifier|public
name|void
name|setServerBehavior
parameter_list|(
name|ServerBehaviorPolicyType
name|serverBehavior
parameter_list|)
block|{
name|this
operator|.
name|serverBehavior
operator|=
name|serverBehavior
expr_stmt|;
block|}
specifier|public
specifier|static
name|Properties
name|getInitialContextEnv
parameter_list|(
name|AddressType
name|addrType
parameter_list|)
block|{
name|Properties
name|env
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|java
operator|.
name|util
operator|.
name|ListIterator
name|listIter
init|=
name|addrType
operator|.
name|getJMSNamingProperty
argument_list|()
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|listIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|JMSNamingPropertyType
name|propertyPair
init|=
operator|(
name|JMSNamingPropertyType
operator|)
name|listIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|propertyPair
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|env
operator|.
name|setProperty
argument_list|(
name|propertyPair
operator|.
name|getName
argument_list|()
argument_list|,
name|propertyPair
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|Enumeration
name|props
init|=
name|env
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|props
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|props
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|env
operator|.
name|getProperty
argument_list|(
name|name
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
literal|"Context property: "
operator|+
name|name
operator|+
literal|" | "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|env
return|;
block|}
block|}
end_class

end_unit

