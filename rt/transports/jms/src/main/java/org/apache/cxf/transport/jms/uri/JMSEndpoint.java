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
operator|.
name|uri
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|jms
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
name|transport
operator|.
name|jms
operator|.
name|spec
operator|.
name|JMSSpecConstants
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JMSEndpoint
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JNDI
init|=
literal|"jndi"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"topic"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUEUE
init|=
literal|"queue"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDI_TOPIC
init|=
literal|"jndi-topic"
decl_stmt|;
comment|// shared parameters
specifier|public
specifier|static
specifier|final
name|String
name|DELIVERYMODE_PARAMETER_NAME
init|=
literal|"deliveryMode"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TIMETOLIVE_PARAMETER_NAME
init|=
literal|"timeToLive"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PRIORITY_PARAMETER_NAME
init|=
literal|"priority"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPLYTONAME_PARAMETER_NAME
init|=
literal|"replyToName"
decl_stmt|;
comment|// The new configuration to set the message type of jms message body
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_TYPE_PARAMETER_NAME
init|=
literal|"messageType"
decl_stmt|;
comment|// default parameters
specifier|public
specifier|static
specifier|final
name|DeliveryModeType
name|DELIVERYMODE_DEFAULT
init|=
name|DeliveryModeType
operator|.
name|PERSISTENT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|long
name|TIMETOLIVE_DEFAULT
init|=
name|Message
operator|.
name|DEFAULT_TIME_TO_LIVE
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PRIORITY_DEFAULT
init|=
name|Message
operator|.
name|DEFAULT_PRIORITY
decl_stmt|;
comment|// jndi parameters ? need to be sure.
specifier|public
specifier|static
specifier|final
name|String
name|JNDICONNECTIONFACTORYNAME_PARAMETER_NAME
init|=
literal|"jndiConnectionFactoryName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDIINITIALCONTEXTFACTORY_PARAMETER_NAME
init|=
literal|"jndiInitialContextFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDIURL_PARAMETER_NAME
init|=
literal|"jndiURL"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JNDI_PARAMETER_NAME_PREFIX
init|=
literal|"jndi-"
decl_stmt|;
comment|// queue and topic parameters
specifier|public
specifier|static
specifier|final
name|String
name|TOPICREPLYTONAME_PARAMETER_NAME
init|=
literal|"topicReplyToName"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|jndiParameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|endpointUri
decl_stmt|;
specifier|private
name|String
name|jmsVariant
decl_stmt|;
specifier|private
name|String
name|destinationName
decl_stmt|;
specifier|private
name|DeliveryModeType
name|deliveryMode
decl_stmt|;
specifier|private
name|MessageType
name|messageType
decl_stmt|;
specifier|private
name|long
name|timeToLive
decl_stmt|;
specifier|private
name|Integer
name|priority
decl_stmt|;
specifier|private
name|String
name|replyToName
decl_stmt|;
specifier|private
name|String
name|topicReplyToName
decl_stmt|;
specifier|private
name|String
name|jndiConnectionFactoryName
decl_stmt|;
specifier|private
name|String
name|jndiInitialContextFactory
decl_stmt|;
specifier|private
name|String
name|jndiURL
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|boolean
name|reconnectOnException
init|=
literal|true
decl_stmt|;
comment|/**      * @param uri      * @param subject      */
specifier|public
name|JMSEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|endpointUri
argument_list|)
operator|||
literal|"jms://"
operator|.
name|equals
argument_list|(
name|endpointUri
argument_list|)
operator|||
operator|!
name|endpointUri
operator|.
name|startsWith
argument_list|(
literal|"jms"
argument_list|)
operator|)
condition|)
block|{
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
name|JMSURIParser
name|parsed
init|=
operator|new
name|JMSURIParser
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|setJmsVariant
argument_list|(
name|parsed
operator|.
name|getVariant
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|destinationName
operator|=
name|parsed
operator|.
name|getDestination
argument_list|()
expr_stmt|;
name|configureProperties
argument_list|(
name|this
argument_list|,
name|parsed
operator|.
name|parseQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|JMSEndpoint
parameter_list|()
block|{
name|this
operator|.
name|jmsVariant
operator|=
name|JMSEndpoint
operator|.
name|QUEUE
expr_stmt|;
block|}
comment|/**      * @param endpoint      * @param params      */
specifier|private
specifier|static
name|void
name|configureProperties
parameter_list|(
name|JMSEndpoint
name|endpoint
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|String
name|deliveryMode
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|DELIVERYMODE_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|timeToLive
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|TIMETOLIVE_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|priority
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|PRIORITY_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|replyToName
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|REPLYTONAME_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|topicReplyToName
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|TOPICREPLYTONAME_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|jndiConnectionFactoryName
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|JNDICONNECTIONFACTORYNAME_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|jndiInitialContextFactory
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|JNDIINITIALCONTEXTFACTORY_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|jndiUrl
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|JNDIURL_PARAMETER_NAME
argument_list|)
decl_stmt|;
name|String
name|messageType
init|=
name|getAndRemoveParameter
argument_list|(
name|params
argument_list|,
name|JMSEndpoint
operator|.
name|MESSAGE_TYPE_PARAMETER_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|deliveryMode
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setDeliveryMode
argument_list|(
name|DeliveryModeType
operator|.
name|valueOf
argument_list|(
name|deliveryMode
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timeToLive
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setTimeToLive
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|timeToLive
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|priority
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setPriority
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|priority
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|replyToName
operator|!=
literal|null
operator|&&
name|topicReplyToName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The replyToName and topicReplyToName should not be defined at the same time."
argument_list|)
throw|;
block|}
if|if
condition|(
name|replyToName
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setReplyToName
argument_list|(
name|replyToName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|topicReplyToName
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setTopicReplyToName
argument_list|(
name|topicReplyToName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jndiConnectionFactoryName
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJndiConnectionFactoryName
argument_list|(
name|jndiConnectionFactoryName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jndiInitialContextFactory
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJndiInitialContextFactory
argument_list|(
name|jndiInitialContextFactory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jndiUrl
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJndiURL
argument_list|(
name|jndiUrl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageType
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setMessageType
argument_list|(
name|MessageType
operator|.
name|fromValue
argument_list|(
name|messageType
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|key
range|:
name|params
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|params
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|JMSEndpoint
operator|.
name|JNDI_PARAMETER_NAME_PREFIX
argument_list|)
condition|)
block|{
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|putJndiParameter
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|.
name|putParameter
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @param parameters      * @param deliverymodeParameterName      * @return      */
specifier|private
specifier|static
name|String
name|getAndRemoveParameter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
parameter_list|,
name|String
name|parameterName
parameter_list|)
block|{
name|String
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|parameterName
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
name|parameterName
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
name|StringBuilder
name|requestUri
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"jms:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|jmsVariant
operator|==
name|JNDI_TOPIC
condition|)
block|{
name|requestUri
operator|.
name|append
argument_list|(
literal|"jndi"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestUri
operator|.
name|append
argument_list|(
name|jmsVariant
argument_list|)
expr_stmt|;
block|}
name|requestUri
operator|.
name|append
argument_list|(
literal|":"
operator|+
name|destinationName
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|parameters
operator|.
name|keySet
argument_list|()
control|)
block|{
comment|// now we just skip the MESSAGE_TYPE_PARAMETER_NAME
comment|// and TARGETSERVICE_PARAMETER_NAME
if|if
condition|(
name|JMSSpecConstants
operator|.
name|TARGETSERVICE_PARAMETER_NAME
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|MESSAGE_TYPE_PARAMETER_NAME
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
condition|)
block|{
name|requestUri
operator|.
name|append
argument_list|(
literal|"?"
operator|+
name|key
operator|+
literal|"="
operator|+
name|value
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|requestUri
operator|.
name|append
argument_list|(
literal|"&"
operator|+
name|key
operator|+
literal|"="
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|requestUri
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @param key      * @param value      */
specifier|public
name|void
name|putJndiParameter
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|jndiParameters
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putParameter
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param targetserviceParameterName      * @return      */
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getJndiParameters
parameter_list|()
block|{
return|return
name|jndiParameters
return|;
block|}
comment|/**      * @return      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|endpointUri
return|;
block|}
specifier|public
name|void
name|setEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
block|}
specifier|public
name|String
name|getJmsVariant
parameter_list|()
block|{
return|return
name|jmsVariant
return|;
block|}
specifier|public
name|void
name|setJmsVariant
parameter_list|(
name|String
name|jmsVariant
parameter_list|)
block|{
if|if
condition|(
name|jmsVariant
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|jmsVariant
operator|=
name|QUEUE
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|QUEUE
operator|.
name|equals
argument_list|(
name|jmsVariant
argument_list|)
operator|||
name|TOPIC
operator|.
name|equals
argument_list|(
name|jmsVariant
argument_list|)
operator|||
name|JNDI
operator|.
name|equals
argument_list|(
name|jmsVariant
argument_list|)
operator|||
name|JNDI_TOPIC
operator|.
name|equals
argument_list|(
name|jmsVariant
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknow JMS Variant "
operator|+
name|jmsVariant
argument_list|)
throw|;
block|}
name|this
operator|.
name|jmsVariant
operator|=
name|jmsVariant
expr_stmt|;
block|}
specifier|public
name|String
name|getDestinationName
parameter_list|()
block|{
return|return
name|destinationName
return|;
block|}
specifier|public
name|void
name|setDestinationName
parameter_list|(
name|String
name|destinationName
parameter_list|)
block|{
name|this
operator|.
name|destinationName
operator|=
name|destinationName
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetDeliveryMode
parameter_list|()
block|{
return|return
name|deliveryMode
operator|!=
literal|null
return|;
block|}
specifier|public
name|DeliveryModeType
name|getDeliveryMode
parameter_list|()
block|{
return|return
name|deliveryMode
operator|==
literal|null
condition|?
name|DeliveryModeType
operator|.
name|PERSISTENT
else|:
name|deliveryMode
return|;
block|}
specifier|public
name|void
name|setDeliveryMode
parameter_list|(
name|DeliveryModeType
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
name|MessageType
name|getMessageType
parameter_list|()
block|{
return|return
name|messageType
operator|==
literal|null
condition|?
name|MessageType
operator|.
name|BYTE
else|:
name|messageType
return|;
block|}
specifier|public
name|void
name|setMessageType
parameter_list|(
name|MessageType
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
name|boolean
name|isSetPriority
parameter_list|()
block|{
return|return
name|priority
operator|!=
literal|null
return|;
block|}
specifier|public
name|int
name|getPriority
parameter_list|()
block|{
return|return
name|priority
operator|==
literal|null
condition|?
name|Message
operator|.
name|DEFAULT_PRIORITY
else|:
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
name|String
name|getReplyToName
parameter_list|()
block|{
return|return
name|replyToName
return|;
block|}
specifier|public
name|void
name|setReplyToName
parameter_list|(
name|String
name|replyToName
parameter_list|)
block|{
name|this
operator|.
name|replyToName
operator|=
name|replyToName
expr_stmt|;
block|}
specifier|public
name|String
name|getTopicReplyToName
parameter_list|()
block|{
return|return
name|topicReplyToName
return|;
block|}
specifier|public
name|void
name|setTopicReplyToName
parameter_list|(
name|String
name|topicReplyToName
parameter_list|)
block|{
name|this
operator|.
name|topicReplyToName
operator|=
name|topicReplyToName
expr_stmt|;
block|}
specifier|public
name|String
name|getJndiConnectionFactoryName
parameter_list|()
block|{
return|return
name|jndiConnectionFactoryName
return|;
block|}
specifier|public
name|void
name|setJndiConnectionFactoryName
parameter_list|(
name|String
name|jndiConnectionFactoryName
parameter_list|)
block|{
name|this
operator|.
name|jndiConnectionFactoryName
operator|=
name|jndiConnectionFactoryName
expr_stmt|;
block|}
specifier|public
name|String
name|getJndiInitialContextFactory
parameter_list|()
block|{
return|return
name|jndiInitialContextFactory
return|;
block|}
specifier|public
name|void
name|setJndiInitialContextFactory
parameter_list|(
name|String
name|jndiInitialContextFactory
parameter_list|)
block|{
name|this
operator|.
name|jndiInitialContextFactory
operator|=
name|jndiInitialContextFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getJndiURL
parameter_list|()
block|{
return|return
name|jndiURL
return|;
block|}
specifier|public
name|void
name|setJndiURL
parameter_list|(
name|String
name|jndiURL
parameter_list|)
block|{
name|this
operator|.
name|jndiURL
operator|=
name|jndiURL
expr_stmt|;
block|}
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
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
enum|enum
name|DeliveryModeType
block|{
name|PERSISTENT
block|,
name|NON_PERSISTENT
block|}
empty_stmt|;
specifier|public
enum|enum
name|MessageType
block|{
name|BYTE
argument_list|(
literal|"byte"
argument_list|)
block|,
name|BINARY
argument_list|(
literal|"binary"
argument_list|)
block|,
name|TEXT
argument_list|(
literal|"text"
argument_list|)
block|;
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
name|MessageType
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
specifier|static
name|MessageType
name|fromValue
parameter_list|(
name|String
name|v
parameter_list|)
block|{
for|for
control|(
name|MessageType
name|c
range|:
name|MessageType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|value
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|v
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

