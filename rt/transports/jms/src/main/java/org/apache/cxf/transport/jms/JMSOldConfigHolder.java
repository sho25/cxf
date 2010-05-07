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
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|spec
operator|.
name|JMSSpecConstants
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
name|uri
operator|.
name|JMSEndpointParser
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
name|JMSURIConstants
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
name|wsdl
operator|.
name|DeliveryModeType
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
name|wsdl
operator|.
name|JndiConnectionFactoryNameType
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
name|wsdl
operator|.
name|JndiContextParameterType
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
name|wsdl
operator|.
name|JndiInitialContextFactoryType
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
name|wsdl
operator|.
name|JndiURLType
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
name|wsdl
operator|.
name|PriorityType
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
name|wsdl
operator|.
name|ReplyToNameType
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
name|wsdl
operator|.
name|TimeToLiveType
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
name|AddressType
name|address
decl_stmt|;
specifier|public
name|void
name|setAddress
parameter_list|(
name|AddressType
name|ad
parameter_list|)
block|{
name|address
operator|=
name|ad
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
comment|/**      * Get the extensors from the wsdl and/or configuration that will      * then be used to configure the JMSConfiguration object       * @param target       */
specifier|private
name|JMSEndpoint
name|getExtensorsAndConfig
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|,
name|boolean
name|isConduit
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSEndpoint
name|endpoint
init|=
literal|null
decl_stmt|;
name|String
name|adr
init|=
name|target
operator|==
literal|null
condition|?
name|endpointInfo
operator|.
name|getAddress
argument_list|()
else|:
name|target
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|adr
argument_list|)
operator|||
literal|"jms://"
operator|.
name|equals
argument_list|(
name|adr
argument_list|)
operator|||
operator|!
name|adr
operator|.
name|startsWith
argument_list|(
literal|"jms"
argument_list|)
condition|?
operator|new
name|JMSEndpoint
argument_list|()
else|:
name|JMSEndpointParser
operator|.
name|createEndpoint
argument_list|(
name|adr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|e2
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|e2
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e2
throw|;
block|}
name|retrieveWSDLInformation
argument_list|(
name|endpoint
argument_list|,
name|endpointInfo
argument_list|)
expr_stmt|;
name|address
operator|=
name|endpointInfo
operator|.
name|getTraversedExtensor
argument_list|(
name|address
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
operator|==
literal|null
condition|?
literal|null
else|:
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
if|if
condition|(
name|name
operator|!=
literal|null
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
if|if
condition|(
name|adr
operator|!=
literal|null
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|adr
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endpoint
return|;
block|}
comment|/**      * @param bus      * @param endpointInfo      * @param isConduit      * @return      */
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
name|EndpointReferenceType
name|target
parameter_list|,
name|boolean
name|isConduit
parameter_list|)
throws|throws
name|IOException
block|{
name|JMSEndpoint
name|endpoint
init|=
name|getExtensorsAndConfig
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
name|target
argument_list|,
name|isConduit
argument_list|)
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|mapAddressToEndpoint
argument_list|(
name|address
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jmsConfig
operator|==
literal|null
condition|)
block|{
name|jmsConfig
operator|=
operator|new
name|JMSConfiguration
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isSetDeliveryMode
argument_list|()
condition|)
block|{
name|int
name|deliveryMode
init|=
name|endpoint
operator|.
name|getDeliveryMode
argument_list|()
operator|.
name|equals
argument_list|(
name|JMSURIConstants
operator|.
name|DELIVERYMODE_PERSISTENT
argument_list|)
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
block|}
if|if
condition|(
name|endpoint
operator|.
name|isSetPriority
argument_list|()
condition|)
block|{
name|int
name|priority
init|=
name|endpoint
operator|.
name|getPriority
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jmsConfig
operator|.
name|isUsingEndpointInfo
argument_list|()
condition|)
block|{
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
name|endpoint
argument_list|)
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
name|JMSURIConstants
operator|.
name|TOPIC
argument_list|)
decl_stmt|;
name|JNDIConfiguration
name|jndiConfig
init|=
operator|new
name|JNDIConfiguration
argument_list|()
decl_stmt|;
name|jndiConfig
operator|.
name|setJndiConnectionFactoryName
argument_list|(
name|endpoint
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setJndiTemplate
argument_list|(
name|jt
argument_list|)
expr_stmt|;
name|jndiConfig
operator|.
name|setConnectionUserName
argument_list|(
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|jndiConfig
operator|.
name|setConnectionPassword
argument_list|(
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|setJndiConfig
argument_list|(
name|jndiConfig
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSetReconnectOnException
argument_list|()
condition|)
block|{
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
block|}
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
if|if
condition|(
name|jmsConfig
operator|.
name|getMessageSelector
argument_list|()
operator|==
literal|null
condition|)
block|{
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
block|}
if|if
condition|(
name|isConduit
condition|)
block|{
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
elseif|else
if|if
condition|(
name|address
operator|==
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setMessageType
argument_list|(
name|JMSConstants
operator|.
name|BYTE_MESSAGE_TYPE
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|clientConfig
operator|.
name|isSetClientReceiveTimeout
argument_list|()
condition|)
block|{
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
block|}
if|if
condition|(
name|clientConfig
operator|.
name|isSetUseConduitIdSelector
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setUseConduitIdSelector
argument_list|(
name|clientConfig
operator|.
name|isUseConduitIdSelector
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|clientConfig
operator|.
name|isSetConduitSelectorPrefix
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setConduitSelectorPrefix
argument_list|(
name|clientConfig
operator|.
name|getConduitSelectorPrefix
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|jmsConfig
operator|.
name|setEnforceSpec
argument_list|(
name|clientConfig
operator|.
name|isEnforceSpec
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
name|setDurableSubscriptionClientId
argument_list|(
name|serverConfig
operator|.
name|getDurableSubscriptionClientId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sessionPool
operator|.
name|isSetHighWaterMark
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setMaxConcurrentConsumers
argument_list|(
name|sessionPool
operator|.
name|getHighWaterMark
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sessionPool
operator|.
name|isSetLowWaterMark
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setConcurrentConsumers
argument_list|(
name|sessionPool
operator|.
name|getLowWaterMark
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isSetTimeToLive
argument_list|()
condition|)
block|{
name|long
name|timeToLive
init|=
name|endpoint
operator|.
name|getTimeToLive
argument_list|()
decl_stmt|;
name|jmsConfig
operator|.
name|setTimeToLive
argument_list|(
name|timeToLive
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isSetUseJMS11
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setUseJms11
argument_list|(
name|endpoint
operator|.
name|isUseJMS11
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serverBehavior
operator|.
name|isSetTransactional
argument_list|()
condition|)
block|{
name|jmsConfig
operator|.
name|setSessionTransacted
argument_list|(
name|serverBehavior
operator|.
name|isTransactional
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|useJndi
init|=
name|endpoint
operator|.
name|getJmsVariant
argument_list|()
operator|.
name|contains
argument_list|(
name|JMSURIConstants
operator|.
name|JNDI
argument_list|)
decl_stmt|;
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
name|endpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setReplyToDestination
argument_list|(
name|address
operator|.
name|getJndiReplyToDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|jmsConfig
operator|.
name|setReplyToDestination
argument_list|(
name|address
operator|.
name|getJmsReplyToDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|getParameter
argument_list|(
name|JMSSpecConstants
operator|.
name|TARGETSERVICE_PARAMETER_NAME
argument_list|)
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
name|mapAddressToEndpoint
parameter_list|(
name|AddressType
name|address
parameter_list|,
name|JMSEndpoint
name|endpoint
parameter_list|)
block|{
name|boolean
name|pubSubDomain
init|=
name|DestinationStyleType
operator|.
name|TOPIC
operator|==
name|address
operator|.
name|getDestinationStyle
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|isSetDestinationStyle
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setJmsVariant
argument_list|(
name|pubSubDomain
condition|?
name|JMSURIConstants
operator|.
name|TOPIC
else|:
name|JMSURIConstants
operator|.
name|QUEUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|setJmsVariant
argument_list|(
name|JMSURIConstants
operator|.
name|QUEUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|.
name|isSetJndiConnectionFactoryName
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setJndiConnectionFactoryName
argument_list|(
name|address
operator|.
name|getJndiConnectionFactoryName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|.
name|isSetConnectionUserName
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setUsername
argument_list|(
name|address
operator|.
name|getConnectionUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|.
name|isSetConnectionPassword
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setPassword
argument_list|(
name|address
operator|.
name|getConnectionUserName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|.
name|isSetReconnectOnException
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setReconnectOnException
argument_list|(
name|address
operator|.
name|isReconnectOnException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|.
name|isSetUseJms11
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|setUseJMS11
argument_list|(
name|address
operator|.
name|isUseJms11
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|useJndi
init|=
name|address
operator|.
name|isSetJndiDestinationName
argument_list|()
decl_stmt|;
if|if
condition|(
name|useJndi
condition|)
block|{
name|endpoint
operator|.
name|setJmsVariant
argument_list|(
name|pubSubDomain
condition|?
name|JMSURIConstants
operator|.
name|JNDI_TOPIC
else|:
name|JMSURIConstants
operator|.
name|JNDI
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setDestinationName
argument_list|(
name|address
operator|.
name|getJndiDestinationName
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setReplyToName
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
name|endpoint
operator|.
name|setDestinationName
argument_list|(
name|address
operator|.
name|getJmsDestinationName
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setReplyToName
argument_list|(
name|address
operator|.
name|getJmsReplyDestinationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|java
operator|.
name|util
operator|.
name|ListIterator
argument_list|<
name|JMSNamingPropertyType
argument_list|>
name|listIter
init|=
name|address
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
name|endpoint
operator|.
name|putJndiParameter
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
block|}
comment|/**      * @param endpoint      * @param ei      */
specifier|private
name|void
name|retrieveWSDLInformation
parameter_list|(
name|JMSEndpoint
name|endpoint
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|JndiContextParameterType
name|jndiContextParameterType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|JndiContextParameterType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jndiContextParameterType
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getJndiParameters
argument_list|()
operator|.
name|get
argument_list|(
name|jndiContextParameterType
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|.
name|putJndiParameter
argument_list|(
name|jndiContextParameterType
operator|.
name|getName
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|jndiContextParameterType
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetJndiConnectionFactoryName
argument_list|()
condition|)
block|{
name|JndiConnectionFactoryNameType
name|jndiConnectionFactoryNameType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|JndiConnectionFactoryNameType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jndiConnectionFactoryNameType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJndiConnectionFactoryName
argument_list|(
name|jndiConnectionFactoryNameType
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetJndiInitialContextFactory
argument_list|()
condition|)
block|{
name|JndiInitialContextFactoryType
name|jndiInitialContextFactoryType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|JndiInitialContextFactoryType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jndiInitialContextFactoryType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJndiInitialContextFactory
argument_list|(
name|jndiInitialContextFactoryType
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetJndiURL
argument_list|()
condition|)
block|{
name|JndiURLType
name|jndiURLType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|JndiURLType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jndiURLType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJndiURL
argument_list|(
name|jndiURLType
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetDeliveryMode
argument_list|()
condition|)
block|{
name|DeliveryModeType
name|deliveryModeType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|DeliveryModeType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|deliveryModeType
operator|!=
literal|null
condition|)
block|{
name|String
name|deliveryMode
init|=
name|deliveryModeType
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setDeliveryMode
argument_list|(
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
name|DeliveryModeType
operator|.
name|valueOf
argument_list|(
name|deliveryMode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetPriority
argument_list|()
condition|)
block|{
name|PriorityType
name|priorityType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|PriorityType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|priorityType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setPriority
argument_list|(
name|priorityType
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetTimeToLive
argument_list|()
condition|)
block|{
name|TimeToLiveType
name|timeToLiveType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|TimeToLiveType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeToLiveType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setTimeToLive
argument_list|(
name|timeToLiveType
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isSetReplyToName
argument_list|()
condition|)
block|{
name|ReplyToNameType
name|replyToNameType
init|=
name|getWSDLExtensor
argument_list|(
name|ei
argument_list|,
name|ReplyToNameType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyToNameType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setReplyToName
argument_list|(
name|replyToNameType
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getWSDLExtensor
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|ServiceInfo
name|si
init|=
name|ei
operator|.
name|getService
argument_list|()
decl_stmt|;
name|BindingInfo
name|bi
init|=
name|ei
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
name|ei
operator|.
name|getExtensor
argument_list|(
name|cls
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
operator|&&
name|si
operator|!=
literal|null
condition|)
block|{
name|o
operator|=
name|si
operator|.
name|getExtensor
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|&&
name|bi
operator|!=
literal|null
condition|)
block|{
name|o
operator|=
name|bi
operator|.
name|getExtensor
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|cls
operator|.
name|isInstance
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|o
argument_list|)
return|;
block|}
return|return
literal|null
return|;
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
name|isSetJndiInitialContextFactory
argument_list|()
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
name|isSetJndiURL
argument_list|()
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

