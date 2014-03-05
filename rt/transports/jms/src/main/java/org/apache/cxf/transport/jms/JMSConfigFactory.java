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
name|Map
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
name|jms
operator|.
name|DeliveryMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|ConfiguredBeanLocator
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
name|uri
operator|.
name|JMSEndpoint
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
specifier|final
class|class
name|JMSConfigFactory
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
name|JMSConfigFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JMSConfigFactory
parameter_list|()
block|{     }
specifier|public
specifier|static
name|JMSConfiguration
name|createFromEndpointInfo
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
block|{
name|JMSEndpoint
name|jmsEndpoint
init|=
operator|new
name|JMSEndpoint
argument_list|(
name|endpointInfo
argument_list|,
name|target
argument_list|)
decl_stmt|;
return|return
name|createFromEndpoint
argument_list|(
name|bus
argument_list|,
name|jmsEndpoint
argument_list|)
return|;
block|}
comment|/**      * @param bus      * @param endpointInfo      * @return      */
specifier|public
specifier|static
name|JMSConfiguration
name|createFromEndpoint
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|JMSEndpoint
name|endpoint
parameter_list|)
block|{
name|JMSConfiguration
name|jmsConfig
init|=
operator|new
name|JMSConfiguration
argument_list|()
decl_stmt|;
name|int
name|deliveryMode
init|=
name|endpoint
operator|.
name|getDeliveryMode
argument_list|()
operator|==
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
name|uri
operator|.
name|JMSEndpoint
operator|.
name|DeliveryModeType
operator|.
name|PERSISTENT
condition|?
name|DeliveryMode
operator|.
name|PERSISTENT
else|:
name|DeliveryMode
operator|.
name|NON_PERSISTENT
decl_stmt|;
name|jmsConfig
operator|.
name|setDeliveryMode
argument_list|(
name|deliveryMode
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setPriority
argument_list|(
name|endpoint
operator|.
name|getPriority
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReconnectOnException
argument_list|(
name|endpoint
operator|.
name|isReconnectOnException
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
name|jmsConfig
operator|.
name|setMessageType
argument_list|(
name|endpoint
operator|.
name|getMessageType
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|pubSubDomain
init|=
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
operator|.
name|contains
argument_list|(
name|JMSEndpoint
operator|.
name|TOPIC
argument_list|)
decl_stmt|;
name|jmsConfig
operator|.
name|setPubSubDomain
argument_list|(
name|pubSubDomain
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setDurableSubscriptionName
argument_list|(
name|endpoint
operator|.
name|getDurableSubscriptionName
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO We might need a separate config here
name|jmsConfig
operator|.
name|setDurableSubscriptionClientId
argument_list|(
name|endpoint
operator|.
name|getDurableSubscriptionName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReceiveTimeout
argument_list|(
name|endpoint
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setTimeToLive
argument_list|(
name|endpoint
operator|.
name|getTimeToLive
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setSessionTransacted
argument_list|(
name|endpoint
operator|.
name|isSessionTransacted
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isUseConduitIdSelector
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setUseConduitIdSelector
argument_list|(
name|endpoint
operator|.
name|isUseConduitIdSelector
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|jmsConfig
operator|.
name|setConduitSelectorPrefix
argument_list|(
name|endpoint
operator|.
name|getConduitIdSelectorPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setUserName
argument_list|(
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setPassword
argument_list|(
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getJndiURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Configure Connection Factory using jndi
name|jmsConfig
operator|.
name|setJndiEnvironment
argument_list|(
name|JMSConfigFactory
operator|.
name|getInitialContextEnv
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setConnectionFactoryName
argument_list|(
name|endpoint
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ConfiguredBeanLocator
name|locator
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConnectionFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|endpoint
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|locator
operator|!=
literal|null
condition|)
block|{
comment|// Configure ConnectionFactory using locator
comment|// Lookup connectionFactory in context like blueprint
name|ConnectionFactory
name|cf
init|=
name|locator
operator|.
name|getBeanOfType
argument_list|(
name|endpoint
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|,
name|ConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cf
operator|!=
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|boolean
name|resolveUsingJndi
init|=
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
operator|.
name|contains
argument_list|(
name|JMSEndpoint
operator|.
name|JNDI
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolveUsingJndi
condition|)
block|{
comment|// Setup Destination jndi destination resolver
name|JndiHelper
name|jt
init|=
operator|new
name|JndiHelper
argument_list|(
name|JMSConfigFactory
operator|.
name|getInitialContextEnv
argument_list|(
name|endpoint
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|JMSDestinationResolver
name|jndiDestinationResolver
init|=
operator|new
name|JMSDestinationResolver
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
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
name|setReplyDestination
argument_list|(
name|jmsConfig
argument_list|,
name|endpoint
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
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
name|setReplyDestination
argument_list|(
name|jmsConfig
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
name|String
name|requestURI
init|=
name|endpoint
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setRequestURI
argument_list|(
name|requestURI
argument_list|)
expr_stmt|;
name|String
name|targetService
init|=
name|endpoint
operator|.
name|getTargetService
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setTargetService
argument_list|(
name|targetService
argument_list|)
expr_stmt|;
return|return
name|jmsConfig
return|;
block|}
specifier|private
specifier|static
name|void
name|setReplyDestination
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|,
name|JMSEndpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getReplyToName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setReplyDestination
argument_list|(
name|endpoint
operator|.
name|getReplyToName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReplyPubSubDomain
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getTopicReplyToName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setReplyDestination
argument_list|(
name|endpoint
operator|.
name|getTopicReplyToName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setReplyPubSubDomain
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|Properties
name|getInitialContextEnv
parameter_list|(
name|JMSEndpoint
name|endpoint
parameter_list|)
block|{
name|Properties
name|env
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getJndiInitialContextFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|INITIAL_CONTEXT_FACTORY
argument_list|,
name|endpoint
operator|.
name|getJndiInitialContextFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getJndiURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|env
operator|.
name|put
argument_list|(
name|Context
operator|.
name|PROVIDER_URL
argument_list|,
name|endpoint
operator|.
name|getJndiURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ent
range|:
name|endpoint
operator|.
name|getJndiParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|env
operator|.
name|put
argument_list|(
name|ent
operator|.
name|getKey
argument_list|()
argument_list|,
name|ent
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
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
argument_list|<
name|?
argument_list|>
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

