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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|HashMap
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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Queue
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
name|transport
operator|.
name|jms
operator|.
name|spec
operator|.
name|JMSSpecConstants
import|;
end_import

begin_class
specifier|public
class|class
name|JMSMessageHeadersType
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
specifier|private
name|String
name|jmsCorrelationID
decl_stmt|;
specifier|private
name|Integer
name|jmsDeliveryMode
decl_stmt|;
specifier|private
name|Long
name|jmsExpiration
decl_stmt|;
specifier|private
name|String
name|jmsMessageID
decl_stmt|;
specifier|private
name|Integer
name|jmsPriority
decl_stmt|;
specifier|private
name|Boolean
name|jmsRedelivered
decl_stmt|;
specifier|private
name|String
name|jmsReplyTo
decl_stmt|;
specifier|private
name|Long
name|jmsTimeStamp
decl_stmt|;
specifier|private
name|String
name|jmsType
decl_stmt|;
specifier|private
name|Long
name|timeToLive
decl_stmt|;
specifier|private
name|String
name|soapjmsTargetService
decl_stmt|;
specifier|private
name|String
name|soapjmsBindingVersion
decl_stmt|;
specifier|private
name|String
name|soapjmsContentType
decl_stmt|;
specifier|private
name|String
name|soapjmsContentEncoding
decl_stmt|;
specifier|private
name|String
name|soapjmssoapAction
decl_stmt|;
specifier|private
name|Boolean
name|soapjmsIsFault
decl_stmt|;
specifier|private
name|String
name|soapjmsRequestURI
decl_stmt|;
specifier|public
name|JMSMessageHeadersType
parameter_list|()
block|{
name|this
operator|.
name|properties
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|List
argument_list|<
name|JMSPropertyType
argument_list|>
name|getProperty
parameter_list|()
block|{
name|List
argument_list|<
name|JMSPropertyType
argument_list|>
name|props
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
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
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|JMSPropertyType
name|prop
init|=
operator|new
name|JMSPropertyType
argument_list|()
decl_stmt|;
name|prop
operator|.
name|setName
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|props
argument_list|)
return|;
block|}
specifier|public
name|void
name|putProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|properties
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
name|Object
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getPropertyKeys
parameter_list|()
block|{
return|return
name|properties
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|String
name|getJMSCorrelationID
parameter_list|()
block|{
return|return
name|jmsCorrelationID
return|;
block|}
specifier|public
name|void
name|setJMSCorrelationID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|jmsCorrelationID
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSCorrelationID
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsCorrelationID
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getJMSMessageID
parameter_list|()
block|{
return|return
name|jmsMessageID
return|;
block|}
specifier|public
name|void
name|setJMSMessageID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|jmsMessageID
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSMessageID
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsMessageID
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getJMSReplyTo
parameter_list|()
block|{
return|return
name|jmsReplyTo
return|;
block|}
specifier|public
name|void
name|setJMSReplyTo
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|jmsReplyTo
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSReplyTo
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsReplyTo
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getJMSType
parameter_list|()
block|{
return|return
name|jmsType
return|;
block|}
specifier|public
name|void
name|setJMSType
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|jmsType
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSType
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsType
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getSOAPJMSTargetService
parameter_list|()
block|{
return|return
name|soapjmsTargetService
return|;
block|}
specifier|public
name|void
name|setSOAPJMSTargetService
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|soapjmsTargetService
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSTargetService
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmsTargetService
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getSOAPJMSBindingVersion
parameter_list|()
block|{
return|return
name|soapjmsBindingVersion
return|;
block|}
specifier|public
name|void
name|setSOAPJMSBindingVersion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|soapjmsBindingVersion
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSBindingVersion
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmsBindingVersion
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getSOAPJMSContentType
parameter_list|()
block|{
return|return
name|soapjmsContentType
return|;
block|}
specifier|public
name|void
name|setSOAPJMSContentType
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|soapjmsContentType
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSContentType
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmsContentType
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getSOAPJMSContentEncoding
parameter_list|()
block|{
return|return
name|soapjmsContentEncoding
return|;
block|}
specifier|public
name|void
name|setSOAPJMSContentEncoding
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|soapjmsContentEncoding
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSContentEncoding
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmsContentEncoding
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getSOAPJMSSOAPAction
parameter_list|()
block|{
return|return
name|soapjmssoapAction
return|;
block|}
specifier|public
name|void
name|setSOAPJMSSOAPAction
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|soapjmssoapAction
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSSOAPAction
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmssoapAction
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|getSOAPJMSRequestURI
parameter_list|()
block|{
return|return
name|soapjmsRequestURI
return|;
block|}
specifier|public
name|void
name|setSOAPJMSRequestURI
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|soapjmsRequestURI
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSRequestURI
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmsRequestURI
operator|!=
literal|null
return|;
block|}
specifier|public
name|void
name|setJMSDeliveryMode
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|jmsDeliveryMode
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetJMSDeliveryMode
parameter_list|()
block|{
name|jmsDeliveryMode
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSDeliveryMode
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsDeliveryMode
operator|!=
literal|null
return|;
block|}
specifier|public
name|int
name|getJMSDeliveryMode
parameter_list|()
block|{
return|return
name|jmsDeliveryMode
return|;
block|}
specifier|public
name|void
name|setJMSExpiration
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|jmsExpiration
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetJMSExpiration
parameter_list|()
block|{
name|jmsExpiration
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSExpiration
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsExpiration
operator|!=
literal|null
return|;
block|}
specifier|public
name|long
name|getJMSExpiration
parameter_list|()
block|{
return|return
name|jmsExpiration
return|;
block|}
specifier|public
name|void
name|setJMSPriority
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|jmsPriority
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetJMSPriority
parameter_list|()
block|{
name|jmsPriority
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSPriority
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsPriority
operator|!=
literal|null
return|;
block|}
specifier|public
name|int
name|getJMSPriority
parameter_list|()
block|{
return|return
name|jmsPriority
return|;
block|}
specifier|public
name|void
name|setJMSRedelivered
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|jmsRedelivered
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetJMSRedelivered
parameter_list|()
block|{
name|jmsRedelivered
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSRedelivered
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsRedelivered
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isJMSRedelivered
parameter_list|()
block|{
return|return
name|jmsRedelivered
return|;
block|}
specifier|public
name|void
name|setJMSTimeStamp
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|jmsTimeStamp
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetJMSTimeStamp
parameter_list|()
block|{
name|jmsTimeStamp
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetJMSTimeStamp
parameter_list|()
block|{
return|return
name|this
operator|.
name|jmsTimeStamp
operator|!=
literal|null
return|;
block|}
specifier|public
name|long
name|getJMSTimeStamp
parameter_list|()
block|{
return|return
name|jmsTimeStamp
return|;
block|}
specifier|public
name|void
name|setTimeToLive
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|timeToLive
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetTimeToLive
parameter_list|()
block|{
name|timeToLive
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetTimeToLive
parameter_list|()
block|{
return|return
name|this
operator|.
name|timeToLive
operator|!=
literal|null
return|;
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
name|setSOAPJMSIsFault
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|soapjmsIsFault
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|unsetSOAPJMSIsFault
parameter_list|()
block|{
name|soapjmsIsFault
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSetSOAPJMSIsFault
parameter_list|()
block|{
return|return
name|this
operator|.
name|soapjmsIsFault
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSOAPJMSIsFault
parameter_list|()
block|{
return|return
name|soapjmsIsFault
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
name|String
name|contentType
init|=
name|getSOAPJMSContentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
name|contentType
operator|=
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|CONTENTTYPE_FIELD
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
name|contentType
operator|=
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|JMSConstants
operator|.
name|RS_CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
name|contentType
operator|=
operator|(
name|String
operator|)
name|getProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
return|return
name|contentType
return|;
block|}
specifier|public
specifier|static
name|JMSMessageHeadersType
name|from
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|JMSMessageHeadersType
name|messageHeaders
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|messageHeaders
operator|.
name|read
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|messageHeaders
return|;
block|}
specifier|private
name|void
name|read
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|setJMSCorrelationID
argument_list|(
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
name|setJMSDeliveryMode
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setJMSExpiration
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getJMSExpiration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setJMSMessageID
argument_list|(
name|message
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
name|setJMSPriority
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getJMSPriority
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setJMSRedelivered
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getJMSRedelivered
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setJMSTimeStamp
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getJMSTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setJMSType
argument_list|(
name|message
operator|.
name|getJMSType
argument_list|()
argument_list|)
expr_stmt|;
name|setSOAPJMSTargetService
argument_list|(
name|message
operator|.
name|getStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|TARGETSERVICE_FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|setSOAPJMSBindingVersion
argument_list|(
name|message
operator|.
name|getStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|BINDINGVERSION_FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|setSOAPJMSContentType
argument_list|(
name|message
operator|.
name|getStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|CONTENTTYPE_FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|setSOAPJMSContentEncoding
argument_list|(
name|message
operator|.
name|getStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|CONTENTENCODING_FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|setSOAPJMSSOAPAction
argument_list|(
name|message
operator|.
name|getStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|SOAPACTION_FIELD
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|propertyExists
argument_list|(
name|JMSSpecConstants
operator|.
name|ISFAULT_FIELD
argument_list|)
condition|)
block|{
name|setSOAPJMSIsFault
argument_list|(
name|message
operator|.
name|getBooleanProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|ISFAULT_FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setSOAPJMSRequestURI
argument_list|(
name|message
operator|.
name|getStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|REQUESTURI_FIELD
argument_list|)
argument_list|)
expr_stmt|;
name|setJMSReplyTo
argument_list|(
name|getDestName
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|readProperties
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getDestName
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|Destination
name|replyTo
init|=
name|message
operator|.
name|getJMSReplyTo
argument_list|()
decl_stmt|;
if|if
condition|(
name|replyTo
operator|instanceof
name|Queue
condition|)
block|{
return|return
operator|(
operator|(
name|Queue
operator|)
name|replyTo
operator|)
operator|.
name|getQueueName
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|replyTo
operator|instanceof
name|Topic
condition|)
block|{
return|return
operator|(
operator|(
name|Topic
operator|)
name|replyTo
operator|)
operator|.
name|getTopicName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|readProperties
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
name|Enumeration
argument_list|<
name|String
argument_list|>
name|enm
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|message
operator|.
name|getPropertyNames
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|enm
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|enm
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|val
init|=
name|message
operator|.
name|getStringProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|unescapedName
init|=
name|name
operator|.
name|replace
argument_list|(
literal|"__"
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|putProperty
argument_list|(
name|unescapedName
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeProp
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|origName
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|JMSException
block|{
name|String
name|name
init|=
name|origName
operator|.
name|replace
argument_list|(
literal|"."
argument_list|,
literal|"__"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|cls
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|name
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Integer
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Integer
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setIntProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Integer
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Double
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Double
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setDoubleProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Double
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Float
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Float
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setFloatProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Float
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Long
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Long
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setLongProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Long
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Boolean
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setBooleanProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Boolean
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Short
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Short
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setShortProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Short
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Byte
operator|.
name|TYPE
operator|||
name|cls
operator|==
name|Byte
operator|.
name|class
condition|)
block|{
name|jmsMessage
operator|.
name|setShortProperty
argument_list|(
name|name
argument_list|,
operator|(
name|Byte
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jmsMessage
operator|.
name|setObjectProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Message
name|jmsMessage
parameter_list|)
throws|throws
name|JMSException
block|{
name|setProp
argument_list|(
name|jmsMessage
argument_list|,
name|JMSSpecConstants
operator|.
name|TARGETSERVICE_FIELD
argument_list|,
name|soapjmsTargetService
argument_list|)
expr_stmt|;
name|setProp
argument_list|(
name|jmsMessage
argument_list|,
name|JMSSpecConstants
operator|.
name|BINDINGVERSION_FIELD
argument_list|,
name|soapjmsBindingVersion
argument_list|)
expr_stmt|;
name|setProp
argument_list|(
name|jmsMessage
argument_list|,
name|JMSSpecConstants
operator|.
name|CONTENTTYPE_FIELD
argument_list|,
name|soapjmsContentType
argument_list|)
expr_stmt|;
name|setProp
argument_list|(
name|jmsMessage
argument_list|,
name|JMSSpecConstants
operator|.
name|CONTENTENCODING_FIELD
argument_list|,
name|soapjmsContentEncoding
argument_list|)
expr_stmt|;
name|setProp
argument_list|(
name|jmsMessage
argument_list|,
name|JMSSpecConstants
operator|.
name|SOAPACTION_FIELD
argument_list|,
name|soapjmssoapAction
argument_list|)
expr_stmt|;
name|setProp
argument_list|(
name|jmsMessage
argument_list|,
name|JMSSpecConstants
operator|.
name|REQUESTURI_FIELD
argument_list|,
name|soapjmsRequestURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSetSOAPJMSIsFault
argument_list|()
condition|)
block|{
name|jmsMessage
operator|.
name|setBooleanProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|ISFAULT_FIELD
argument_list|,
name|isSOAPJMSIsFault
argument_list|()
argument_list|)
expr_stmt|;
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
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|writeProp
argument_list|(
name|jmsMessage
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setProp
parameter_list|(
name|Message
name|jmsMessage
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

