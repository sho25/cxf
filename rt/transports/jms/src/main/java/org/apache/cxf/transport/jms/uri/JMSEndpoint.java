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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_comment
comment|/**  * Parses and holds configuration retrieved from a SOAP/JMS spec URI  */
end_comment

begin_class
specifier|public
class|class
name|JMSEndpoint
block|{
comment|// JMS Variants
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
comment|// default values
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
comment|/**      * All parameters with this prefix will go to jndiParameters and be used      * as the jndi inital context properties      */
specifier|public
specifier|static
specifier|final
name|String
name|JNDI_PARAMETER_NAME_PREFIX
init|=
literal|"jndi-"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAXWS_PROPERTY_PREFIX
init|=
literal|"jms."
decl_stmt|;
specifier|private
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
specifier|private
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
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
specifier|private
name|String
name|jmsVariant
decl_stmt|;
specifier|private
name|String
name|destinationName
decl_stmt|;
comment|/**      * URI parameters      * Will be filled from URI query parameters with matching names      */
specifier|private
name|String
name|conduitIdSelectorPrefix
decl_stmt|;
specifier|private
name|DeliveryModeType
name|deliveryMode
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
name|jndiConnectionFactoryName
init|=
literal|"ConnectionFactory"
decl_stmt|;
specifier|private
name|String
name|jndiInitialContextFactory
decl_stmt|;
specifier|private
name|String
name|jndiTransactionManagerName
decl_stmt|;
specifier|private
name|String
name|jndiURL
decl_stmt|;
specifier|private
name|MessageType
name|messageType
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|Integer
name|priority
decl_stmt|;
specifier|private
name|long
name|receiveTimeout
init|=
literal|60000L
decl_stmt|;
specifier|private
name|String
name|replyToName
decl_stmt|;
specifier|private
name|boolean
name|sessionTransacted
decl_stmt|;
specifier|private
name|String
name|targetService
decl_stmt|;
specifier|private
name|long
name|timeToLive
decl_stmt|;
specifier|private
name|String
name|topicReplyToName
decl_stmt|;
specifier|private
name|boolean
name|useConduitIdSelector
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
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
argument_list|(
literal|null
argument_list|,
name|endpointUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the extensors from the wsdl and/or configuration that will      * then be used to configure the JMSConfiguration object       * @param target       */
specifier|public
name|JMSEndpoint
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
block|{
name|this
argument_list|(
name|endpointInfo
argument_list|,
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
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param uri      * @param subject      */
specifier|public
name|JMSEndpoint
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|jmsVariant
operator|=
name|JMSEndpoint
operator|.
name|QUEUE
expr_stmt|;
if|if
condition|(
name|ei
operator|!=
literal|null
condition|)
block|{
name|JMSEndpointWSDLUtil
operator|.
name|retrieveWSDLInformation
argument_list|(
name|this
argument_list|,
name|ei
argument_list|)
expr_stmt|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|query
init|=
name|parsed
operator|.
name|parseQuery
argument_list|()
decl_stmt|;
name|configureProperties
argument_list|(
name|query
argument_list|)
expr_stmt|;
comment|// Use the properties like e.g. from JAXWS properties with "jms." prefix
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jmsProps
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ei
operator|!=
literal|null
condition|)
block|{
name|getJaxWsJmsProps
argument_list|(
name|ei
operator|.
name|getProperties
argument_list|()
argument_list|,
name|jmsProps
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ei
operator|!=
literal|null
operator|&&
name|ei
operator|.
name|getBinding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getJaxWsJmsProps
argument_list|(
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getProperties
argument_list|()
argument_list|,
name|jmsProps
argument_list|)
expr_stmt|;
block|}
name|configureProperties
argument_list|(
name|jmsProps
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|getJaxWsJmsProps
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jaxwsProps
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|jmsProps
parameter_list|)
block|{
if|if
condition|(
name|jaxwsProps
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|jaxwsProps
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
name|JAXWS_PROPERTY_PREFIX
argument_list|)
condition|)
block|{
name|jmsProps
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|substring
argument_list|(
name|JAXWS_PROPERTY_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|trySetProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
name|getPropSetterName
argument_list|(
name|name
argument_list|)
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|method
operator|.
name|invoke
argument_list|(
name|this
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error setting property "
operator|+
name|name
operator|+
literal|":"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|getPropSetterName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|first
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|String
name|rest
init|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
literal|"set"
operator|+
name|first
operator|.
name|toUpperCase
argument_list|()
operator|+
name|rest
return|;
block|}
comment|/**      * Configure properties form map.      * For each key of the map first a property with the same name in the endpoint is tried.      * If that does not match then the value is either stored in the jndiParameters or the parameters      * depending on the prefix of the key. If it matches JNDI_PARAMETER_NAME_PREFIX it is stored in the       * jndiParameters else in the parameters      *       * @param endpoint      * @param params      */
specifier|private
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
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
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|trySetProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|String
operator|)
condition|)
block|{
continue|continue;
block|}
name|String
name|valueSt
init|=
operator|(
name|String
operator|)
name|value
decl_stmt|;
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
name|putJndiParameter
argument_list|(
name|key
argument_list|,
name|valueSt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|putParameter
argument_list|(
name|key
argument_list|,
name|valueSt
argument_list|)
expr_stmt|;
block|}
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
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
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
name|entry
operator|.
name|getKey
argument_list|()
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
name|entry
operator|.
name|getKey
argument_list|()
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
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
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
name|void
name|setDeliveryMode
parameter_list|(
name|String
name|deliveryMode
parameter_list|)
block|{
name|this
operator|.
name|deliveryMode
operator|=
name|DeliveryModeType
operator|.
name|valueOf
argument_list|(
name|deliveryMode
argument_list|)
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
name|MessageType
operator|.
name|fromValue
argument_list|(
name|messageType
argument_list|)
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
name|void
name|setTimeToLive
parameter_list|(
name|String
name|timeToLive
parameter_list|)
block|{
name|this
operator|.
name|timeToLive
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|timeToLive
argument_list|)
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
name|void
name|setPriority
parameter_list|(
name|String
name|priority
parameter_list|)
block|{
name|this
operator|.
name|priority
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|priority
argument_list|)
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
name|void
name|setConcurrentConsumers
parameter_list|(
name|String
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|concurrentConsumers
argument_list|)
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
name|long
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
name|long
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
name|void
name|setReceiveTimeout
parameter_list|(
name|String
name|receiveTimeout
parameter_list|)
block|{
name|this
operator|.
name|receiveTimeout
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|receiveTimeout
argument_list|)
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
name|void
name|setSessionTransacted
parameter_list|(
name|String
name|sessionTransacted
parameter_list|)
block|{
name|this
operator|.
name|sessionTransacted
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|sessionTransacted
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getConduitIdSelectorPrefix
parameter_list|()
block|{
return|return
name|conduitIdSelectorPrefix
return|;
block|}
specifier|public
name|void
name|setConduitIdSelectorPrefix
parameter_list|(
name|String
name|conduitIdSelectorPrefix
parameter_list|)
block|{
name|this
operator|.
name|conduitIdSelectorPrefix
operator|=
name|conduitIdSelectorPrefix
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
specifier|public
name|void
name|setUseConduitIdSelector
parameter_list|(
name|String
name|useConduitIdSelectorSt
parameter_list|)
block|{
name|this
operator|.
name|useConduitIdSelector
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|useConduitIdSelectorSt
argument_list|)
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
name|String
name|getJndiTransactionManagerName
parameter_list|()
block|{
return|return
name|jndiTransactionManagerName
return|;
block|}
specifier|public
name|void
name|setJndiTransactionManagerName
parameter_list|(
name|String
name|jndiTransactionManagerName
parameter_list|)
block|{
name|this
operator|.
name|jndiTransactionManagerName
operator|=
name|jndiTransactionManagerName
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

