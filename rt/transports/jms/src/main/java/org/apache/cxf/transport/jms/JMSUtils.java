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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

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
name|BytesMessage
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
name|ObjectMessage
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
name|helpers
operator|.
name|HttpHeaderHelper
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
name|security
operator|.
name|SecurityContext
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
name|JmsUtils
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
name|converter
operator|.
name|MessageConversionException
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
name|converter
operator|.
name|SimpleMessageConverter102
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JMSUtils
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|CORRELATTION_ID_PADDING
init|=
block|{
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|}
decl_stmt|;
specifier|private
name|JMSUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|long
name|getTimeToLive
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|)
block|{
name|long
name|ttl
init|=
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetTimeToLive
argument_list|()
condition|)
block|{
name|ttl
operator|=
name|headers
operator|.
name|getTimeToLive
argument_list|()
expr_stmt|;
block|}
return|return
name|ttl
return|;
block|}
specifier|public
specifier|static
name|void
name|setMessageProperties
parameter_list|(
name|JMSMessageHeadersType
name|headers
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|headers
operator|!=
literal|null
operator|&&
name|headers
operator|.
name|isSetProperty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|JMSPropertyType
argument_list|>
name|props
init|=
name|headers
operator|.
name|getProperty
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|props
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|message
operator|.
name|setStringProperty
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
name|props
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Create a JMS of the appropriate type populated with the given payload.      *       * @param payload the message payload, expected to be either of type String or byte[] depending on payload      *            type      * @param session the JMS session      * @param replyTo the ReplyTo destination if any      * @return a JMS of the appropriate type populated with the given payload      */
specifier|public
specifier|static
name|Message
name|createAndSetPayload
parameter_list|(
name|Object
name|payload
parameter_list|,
name|Session
name|session
parameter_list|,
name|String
name|messageType
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JMSConstants
operator|.
name|TEXT_MESSAGE_TYPE
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
condition|)
block|{
name|message
operator|=
name|session
operator|.
name|createTextMessage
argument_list|(
operator|(
name|String
operator|)
name|payload
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JMSConstants
operator|.
name|BYTE_MESSAGE_TYPE
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
condition|)
block|{
name|message
operator|=
name|session
operator|.
name|createBytesMessage
argument_list|()
expr_stmt|;
operator|(
operator|(
name|BytesMessage
operator|)
name|message
operator|)
operator|.
name|writeBytes
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|payload
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|session
operator|.
name|createObjectMessage
argument_list|()
expr_stmt|;
operator|(
operator|(
name|ObjectMessage
operator|)
name|message
operator|)
operator|.
name|setObject
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|payload
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
comment|/**      * Extract the payload of an incoming message.      *       * @param message the incoming message      * @param encoding the message encoding      * @return the message payload as byte[]      * @throws UnsupportedEncodingException      */
specifier|public
specifier|static
name|byte
index|[]
name|retrievePayload
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|Object
name|converted
decl_stmt|;
try|try
block|{
name|converted
operator|=
operator|new
name|SimpleMessageConverter102
argument_list|()
operator|.
name|fromMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessageConversionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Conversion failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
name|JmsUtils
operator|.
name|convertJmsAccessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|converted
operator|instanceof
name|String
condition|)
block|{
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|String
operator|)
name|converted
operator|)
operator|.
name|getBytes
argument_list|(
name|encoding
argument_list|)
return|;
block|}
else|else
block|{
comment|// Using the UTF-8 encoding as default
return|return
operator|(
operator|(
name|String
operator|)
name|converted
operator|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|converted
operator|instanceof
name|byte
index|[]
condition|)
block|{
return|return
operator|(
name|byte
index|[]
operator|)
name|converted
return|;
block|}
else|else
block|{
return|return
operator|(
name|byte
index|[]
operator|)
name|converted
return|;
comment|// TODO is this correct?
block|}
block|}
specifier|public
specifier|static
name|void
name|populateIncomingContext
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|inMessage
parameter_list|,
name|String
name|headerType
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
try|try
block|{
name|JMSMessageHeadersType
name|headers
init|=
literal|null
decl_stmt|;
name|headers
operator|=
operator|(
name|JMSMessageHeadersType
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|headerType
argument_list|)
expr_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|JMSMessageHeadersType
argument_list|()
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|headerType
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|setJMSCorrelationID
argument_list|(
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSDeliveryMode
argument_list|(
operator|new
name|Integer
argument_list|(
name|message
operator|.
name|getJMSDeliveryMode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSExpiration
argument_list|(
operator|new
name|Long
argument_list|(
name|message
operator|.
name|getJMSExpiration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSMessageID
argument_list|(
name|message
operator|.
name|getJMSMessageID
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSPriority
argument_list|(
operator|new
name|Integer
argument_list|(
name|message
operator|.
name|getJMSPriority
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
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
name|headers
operator|.
name|setJMSTimeStamp
argument_list|(
operator|new
name|Long
argument_list|(
name|message
operator|.
name|getJMSTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJMSType
argument_list|(
name|message
operator|.
name|getJMSType
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protHeaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|JMSPropertyType
argument_list|>
name|props
init|=
name|headers
operator|.
name|getProperty
argument_list|()
decl_stmt|;
name|Enumeration
name|enm
init|=
name|message
operator|.
name|getPropertyNames
argument_list|()
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
operator|(
name|String
operator|)
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
name|name
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|props
operator|.
name|add
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|protHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
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
operator|||
name|name
operator|.
name|equals
argument_list|(
name|JMSConstants
operator|.
name|JMS_CONTENT_TYPE
argument_list|)
operator|&&
name|val
operator|!=
literal|null
condition|)
block|{
name|inMessage
operator|.
name|put
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
argument_list|,
name|val
argument_list|)
expr_stmt|;
comment|// set the message encoding
name|inMessage
operator|.
name|put
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
name|ENCODING
argument_list|,
name|getEncoding
argument_list|(
name|val
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|inMessage
operator|.
name|put
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
name|PROTOCOL_HEADERS
argument_list|,
name|protHeaders
argument_list|)
expr_stmt|;
name|SecurityContext
name|securityContext
init|=
name|buildSecurityContext
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|securityContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
throw|throw
name|JmsUtils
operator|.
name|convertJmsAccessException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/**      * Extract the property JMSXUserID from the jms message and create a SecurityContext from it.       * For more info see Jira Issue CXF-2055      * {@link https://issues.apache.org/jira/browse/CXF-2055}      *       * @param message jms message to retrieve user information from      * @return SecurityContext that contains the user of the producer of the message as the Principal      * @throws JMSException if something goes wrong      */
specifier|private
specifier|static
name|SecurityContext
name|buildSecurityContext
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
parameter_list|)
throws|throws
name|JMSException
block|{
specifier|final
name|String
name|jmsUserName
init|=
name|message
operator|.
name|getStringProperty
argument_list|(
literal|"JMSXUserID"
argument_list|)
decl_stmt|;
if|if
condition|(
name|jmsUserName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Principal
name|principal
init|=
operator|new
name|Principal
argument_list|()
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|jmsUserName
return|;
block|}
block|}
decl_stmt|;
name|SecurityContext
name|securityContext
init|=
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|principal
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
return|return
name|securityContext
return|;
block|}
specifier|static
name|String
name|getEncoding
parameter_list|(
name|String
name|ct
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|contentType
init|=
name|ct
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|String
name|enc
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|tokens
init|=
name|contentType
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|token
range|:
name|tokens
control|)
block|{
name|int
name|index
init|=
name|token
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|enc
operator|=
name|token
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|8
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|String
name|normalizedEncoding
init|=
name|HttpHeaderHelper
operator|.
name|mapCharset
argument_list|(
name|enc
argument_list|)
decl_stmt|;
if|if
condition|(
name|normalizedEncoding
operator|==
literal|null
condition|)
block|{
name|String
name|m
init|=
operator|new
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
argument_list|(
literal|"INVALID_ENCODING_MSG"
argument_list|,
name|LOG
argument_list|,
operator|new
name|Object
index|[]
block|{
name|enc
block|}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|m
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnsupportedEncodingException
argument_list|(
name|m
argument_list|)
throw|;
block|}
return|return
name|normalizedEncoding
return|;
block|}
specifier|protected
specifier|static
name|void
name|addProtocolHeaders
parameter_list|(
name|Message
name|message
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
throws|throws
name|JMSException
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|StringBuilder
name|value
init|=
operator|new
name|StringBuilder
argument_list|(
literal|256
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|value
operator|.
name|setLength
argument_list|(
literal|0
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
name|s
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|value
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
name|value
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
comment|// If the Content-Type header key is Content-Type replace with JMS_Content_Type
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
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
condition|)
block|{
name|message
operator|.
name|setStringProperty
argument_list|(
name|JMSConstants
operator|.
name|JMS_CONTENT_TYPE
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setStringProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|void
name|addContentTypeToProtocolHeader
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|message
parameter_list|)
block|{
name|String
name|contentType
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
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
decl_stmt|;
name|String
name|enc
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
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
name|ENCODING
argument_list|)
decl_stmt|;
comment|// add the encoding information
if|if
condition|(
literal|null
operator|!=
name|contentType
condition|)
block|{
if|if
condition|(
name|enc
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|contentType
operator|=
name|contentType
operator|+
literal|"; charset="
operator|+
name|enc
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|enc
operator|!=
literal|null
condition|)
block|{
name|contentType
operator|=
literal|"text/xml; charset="
operator|+
name|enc
expr_stmt|;
block|}
else|else
block|{
name|contentType
operator|=
literal|"text/xml"
expr_stmt|;
block|}
comment|// Retrieve or create protocol headers
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
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
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|headers
condition|)
block|{
name|headers
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
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
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
comment|// Add content type to the protocol headers
name|List
argument_list|<
name|String
argument_list|>
name|ct
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CONTENT_TYPE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ct
operator|=
name|headers
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|headers
operator|.
name|get
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
operator|!=
literal|null
condition|)
block|{
name|ct
operator|=
name|headers
operator|.
name|get
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
else|else
block|{
name|ct
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
name|ct
operator|.
name|add
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Message
name|buildJMSMessageFromCXFMessage
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|outMessage
parameter_list|,
name|Object
name|payload
parameter_list|,
name|String
name|messageType
parameter_list|,
name|Session
name|session
parameter_list|,
name|Destination
name|replyTo
parameter_list|,
name|String
name|correlationId
parameter_list|)
throws|throws
name|JMSException
block|{
name|Message
name|jmsMessage
init|=
name|JMSUtils
operator|.
name|createAndSetPayload
argument_list|(
name|payload
argument_list|,
name|session
argument_list|,
name|messageType
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSReplyTo
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
block|}
name|JMSMessageHeadersType
name|headers
init|=
operator|(
name|JMSMessageHeadersType
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|JMSConstants
operator|.
name|JMS_CLIENT_REQUEST_HEADERS
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|JMSMessageHeadersType
argument_list|()
expr_stmt|;
block|}
name|JMSUtils
operator|.
name|setMessageProperties
argument_list|(
name|headers
argument_list|,
name|jmsMessage
argument_list|)
expr_stmt|;
comment|// ensure that the contentType is set to the out jms message header
name|JMSUtils
operator|.
name|addContentTypeToProtocolHeader
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|outMessage
operator|.
name|get
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
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|JMSUtils
operator|.
name|addProtocolHeaders
argument_list|(
name|jmsMessage
argument_list|,
name|protHeaders
argument_list|)
expr_stmt|;
name|jmsMessage
operator|.
name|setJMSCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
return|return
name|jmsMessage
return|;
block|}
specifier|public
specifier|static
name|String
name|createCorrelationId
parameter_list|(
specifier|final
name|String
name|prefix
parameter_list|,
name|long
name|i
parameter_list|)
block|{
name|String
name|index
init|=
name|Long
operator|.
name|toHexString
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|StringBuffer
name|id
init|=
operator|new
name|StringBuffer
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
name|id
operator|.
name|append
argument_list|(
name|CORRELATTION_ID_PADDING
argument_list|,
literal|0
argument_list|,
literal|16
operator|-
name|index
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|id
operator|.
name|append
argument_list|(
name|index
argument_list|)
expr_stmt|;
return|return
name|id
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

