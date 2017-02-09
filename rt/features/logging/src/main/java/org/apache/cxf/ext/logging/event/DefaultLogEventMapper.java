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
name|ext
operator|.
name|logging
operator|.
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|binding
operator|.
name|Binding
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
name|security
operator|.
name|AuthorizationPolicy
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
name|endpoint
operator|.
name|Endpoint
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
name|message
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
name|message
operator|.
name|MessageUtils
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
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|InterfaceInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceModelUtil
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
name|AddressingProperties
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
name|ContextUtils
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultLogEventMapper
implements|implements
name|LogEventMapper
block|{
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|BINARY_CONTENT_MEDIA_TYPES
decl_stmt|;
static|static
block|{
name|BINARY_CONTENT_MEDIA_TYPES
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|BINARY_CONTENT_MEDIA_TYPES
operator|.
name|add
argument_list|(
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|BINARY_CONTENT_MEDIA_TYPES
operator|.
name|add
argument_list|(
literal|"image/png"
argument_list|)
expr_stmt|;
name|BINARY_CONTENT_MEDIA_TYPES
operator|.
name|add
argument_list|(
literal|"image/jpeg"
argument_list|)
expr_stmt|;
name|BINARY_CONTENT_MEDIA_TYPES
operator|.
name|add
argument_list|(
literal|"image/gif"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|String
name|MULTIPART_CONTENT_MEDIA_TYPE
init|=
literal|"multipart"
decl_stmt|;
specifier|public
name|LogEvent
name|map
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
specifier|final
name|LogEvent
name|event
init|=
operator|new
name|LogEvent
argument_list|()
decl_stmt|;
name|event
operator|.
name|setMessageId
argument_list|(
name|getMessageId
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setExchangeId
argument_list|(
operator|(
name|String
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|LogEvent
operator|.
name|KEY_EXCHANGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setType
argument_list|(
name|getEventType
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|DECOUPLED_CHANNEL_MESSAGE
argument_list|)
argument_list|)
condition|)
block|{
comment|// avoid logging the default responseCode 200 for the decoupled responses
name|Integer
name|responseCode
init|=
operator|(
name|Integer
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseCode
operator|!=
literal|null
condition|)
block|{
name|event
operator|.
name|setResponseCode
argument_list|(
name|responseCode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|event
operator|.
name|setEncoding
argument_list|(
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setHttpMethod
argument_list|(
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setContentType
argument_list|(
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headerMap
init|=
name|getHeaders
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|event
operator|.
name|setHeaders
argument_list|(
name|headerMap
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
name|getUri
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|event
operator|.
name|setAddress
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|event
operator|.
name|setPrincipal
argument_list|(
name|getPrincipal
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setBinaryContent
argument_list|(
name|isBinaryContent
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|event
operator|.
name|setMultipartContent
argument_list|(
name|isMultipartContent
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|setEpInfo
argument_list|(
name|message
argument_list|,
name|event
argument_list|)
expr_stmt|;
return|return
name|event
return|;
block|}
specifier|private
name|String
name|getPrincipal
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|principal
init|=
name|getJAASPrincipal
argument_list|()
decl_stmt|;
if|if
condition|(
name|principal
operator|!=
literal|null
condition|)
block|{
return|return
name|principal
return|;
block|}
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|!=
literal|null
operator|&&
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
name|AuthorizationPolicy
name|authPolicy
init|=
name|message
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|authPolicy
operator|!=
literal|null
condition|)
block|{
return|return
name|authPolicy
operator|.
name|getUserName
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|getJAASPrincipal
parameter_list|()
block|{
name|StringBuilder
name|principals
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|principalIt
init|=
name|getJAASPrincipals
argument_list|()
decl_stmt|;
while|while
condition|(
name|principalIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|principals
operator|.
name|append
argument_list|(
name|principalIt
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|principalIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|principals
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|principals
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|principals
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|Iterator
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|getJAASPrincipals
parameter_list|()
block|{
name|Subject
name|subject
init|=
name|Subject
operator|.
name|getSubject
argument_list|(
name|AccessController
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|subject
operator|!=
literal|null
operator|&&
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|!=
literal|null
condition|?
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|iterator
argument_list|()
else|:
name|Collections
operator|.
name|emptyIterator
argument_list|()
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
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
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
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
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
index|[]
name|valueAr
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|valueAr
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|String
name|getUri
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|uri
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|String
name|address
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
name|uri
operator|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|REQUEST_URI
argument_list|)
expr_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
if|if
condition|(
name|address
operator|!=
literal|null
operator|&&
operator|!
name|address
operator|.
name|startsWith
argument_list|(
name|uri
argument_list|)
condition|)
block|{
if|if
condition|(
name|address
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
name|address
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|address
operator|=
name|address
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|address
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|uri
operator|=
name|address
operator|+
name|uri
expr_stmt|;
block|}
block|}
else|else
block|{
name|uri
operator|=
name|address
expr_stmt|;
block|}
block|}
name|String
name|query
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|QUERY_STRING
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
return|return
name|uri
operator|+
literal|"?"
operator|+
name|query
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
specifier|private
name|boolean
name|isBinaryContent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|contentType
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|contentType
operator|!=
literal|null
operator|&&
name|BINARY_CONTENT_MEDIA_TYPES
operator|.
name|contains
argument_list|(
name|contentType
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isMultipartContent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|contentType
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|startsWith
argument_list|(
name|MULTIPART_CONTENT_MEDIA_TYPE
argument_list|)
return|;
block|}
comment|/**      * check if a Message is a Rest Message      *      * @param message      * @return      */
specifier|private
name|boolean
name|isSOAPMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Binding
name|binding
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
return|return
name|binding
operator|!=
literal|null
operator|&&
name|binding
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"SoapBinding"
argument_list|)
return|;
block|}
comment|/**      * Get MessageId from WS Addressing properties      *       * @param message      * @return message id      */
specifier|private
name|String
name|getMessageId
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|AddressingProperties
name|addrProp
init|=
name|ContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|addrProp
operator|!=
literal|null
operator|&&
name|addrProp
operator|.
name|getMessageID
argument_list|()
operator|!=
literal|null
condition|?
name|addrProp
operator|.
name|getMessageID
argument_list|()
operator|.
name|getValue
argument_list|()
else|:
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|getOperationName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|operationName
init|=
literal|null
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
literal|null
decl_stmt|;
name|boi
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|boi
condition|)
block|{
name|boi
operator|=
name|getOperationFromContent
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|boi
condition|)
block|{
name|Message
name|inMsg
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|inMsg
condition|)
block|{
name|Message
name|reqMsg
init|=
name|inMsg
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|reqMsg
condition|)
block|{
name|boi
operator|=
name|getOperationFromContent
argument_list|(
name|reqMsg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|boi
condition|)
block|{
name|operationName
operator|=
name|boi
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|operationName
return|;
block|}
specifier|private
name|BindingOperationInfo
name|getOperationFromContent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|XMLStreamReader
name|xmlReader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlReader
operator|!=
literal|null
condition|)
block|{
return|return
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|xmlReader
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|Message
name|getEffectiveMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|boolean
name|isRequestor
init|=
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|boolean
name|isOutbound
init|=
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRequestor
condition|)
block|{
return|return
name|isOutbound
condition|?
name|message
else|:
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|isOutbound
condition|?
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
else|:
name|message
return|;
block|}
block|}
specifier|private
name|String
name|getRestOperationName
parameter_list|(
name|Message
name|curMessage
parameter_list|)
block|{
name|Message
name|message
init|=
name|getEffectiveMessage
argument_list|(
name|curMessage
argument_list|)
decl_stmt|;
name|String
name|httpMethod
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpMethod
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|String
name|path
init|=
literal|""
decl_stmt|;
name|String
name|requestUri
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|REQUEST_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestUri
operator|!=
literal|null
condition|)
block|{
name|String
name|basePath
init|=
name|safeGet
argument_list|(
name|message
argument_list|,
name|Message
operator|.
name|BASE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
operator|&&
name|requestUri
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|path
operator|=
name|requestUri
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|path
operator|=
literal|"/"
expr_stmt|;
block|}
block|}
return|return
operator|new
name|StringBuffer
argument_list|()
operator|.
name|append
argument_list|(
name|httpMethod
argument_list|)
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|path
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|String
name|safeGet
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
operator|||
operator|!
name|message
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|value
init|=
name|message
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|value
operator|instanceof
name|String
operator|)
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Gets the event type from message.      *      * @param message the message      * @return the event type      */
specifier|public
name|EventType
name|getEventType
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|boolean
name|isRequestor
init|=
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|boolean
name|isFault
init|=
name|MessageUtils
operator|.
name|isFault
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isFault
condition|)
block|{
name|isFault
operator|=
operator|!
name|isSOAPMessage
argument_list|(
name|message
argument_list|)
operator|&&
name|isRESTFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isOutbound
init|=
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|isOutbound
condition|)
block|{
if|if
condition|(
name|isFault
condition|)
block|{
return|return
name|EventType
operator|.
name|FAULT_OUT
return|;
block|}
else|else
block|{
return|return
name|isRequestor
condition|?
name|EventType
operator|.
name|REQ_OUT
else|:
name|EventType
operator|.
name|RESP_OUT
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isFault
condition|)
block|{
return|return
name|EventType
operator|.
name|FAULT_IN
return|;
block|}
else|else
block|{
return|return
name|isRequestor
condition|?
name|EventType
operator|.
name|RESP_IN
else|:
name|EventType
operator|.
name|REQ_IN
return|;
block|}
block|}
block|}
comment|/**      * For REST we also consider a response to be a fault if the operation is not found or the response code      * is an error      *       * @param message      * @return      */
specifier|private
name|boolean
name|isRESTFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Object
name|opName
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.resource.operation.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|Integer
name|responseCode
init|=
operator|(
name|Integer
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
decl_stmt|;
return|return
operator|(
name|responseCode
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|responseCode
operator|>=
literal|400
operator|)
return|;
block|}
block|}
specifier|public
name|void
name|setEpInfo
parameter_list|(
name|Message
name|message
parameter_list|,
specifier|final
name|LogEvent
name|event
parameter_list|)
block|{
name|EndpointInfo
name|endpoint
init|=
name|getEPInfo
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|event
operator|.
name|setPortName
argument_list|(
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|setPortTypeName
argument_list|(
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|opName
init|=
name|isSOAPMessage
argument_list|(
name|message
argument_list|)
condition|?
name|getOperationName
argument_list|(
name|message
argument_list|)
else|:
name|getRestOperationName
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|event
operator|.
name|setOperationName
argument_list|(
name|opName
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setServiceInfo
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|setServiceInfo
parameter_list|(
name|ServiceInfo
name|service
parameter_list|,
name|LogEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|setServiceName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|InterfaceInfo
name|iface
init|=
name|service
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|event
operator|.
name|setPortTypeName
argument_list|(
name|iface
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|EndpointInfo
name|getEPInfo
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
return|return
operator|(
name|ep
operator|==
literal|null
operator|)
condition|?
operator|new
name|EndpointInfo
argument_list|()
else|:
name|ep
operator|.
name|getEndpointInfo
argument_list|()
return|;
block|}
block|}
end_class

end_unit

