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
name|jaxrs
operator|.
name|ext
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|Collection
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
name|LinkedList
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
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|SecurityContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|ContextResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Providers
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
name|attachment
operator|.
name|AttachmentDeserializer
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
name|attachment
operator|.
name|AttachmentImpl
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
name|attachment
operator|.
name|AttachmentUtil
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
name|attachment
operator|.
name|HeaderSizeExceededException
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
name|interceptor
operator|.
name|AttachmentOutInterceptor
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
name|io
operator|.
name|CacheSizeExceededException
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Attachment
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|MultipartBody
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
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
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
name|jaxrs
operator|.
name|impl
operator|.
name|ProvidersImpl
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
name|jaxrs
operator|.
name|interceptor
operator|.
name|AttachmentInputInterceptor
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
name|jaxrs
operator|.
name|interceptor
operator|.
name|AttachmentOutputInterceptor
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|Exchange
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
name|ExchangeImpl
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
name|MessageImpl
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

begin_class
specifier|public
class|class
name|MessageContextImpl
implements|implements
name|MessageContext
block|{
specifier|private
name|Message
name|m
decl_stmt|;
specifier|public
name|MessageContextImpl
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|this
operator|.
name|m
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|String
name|keyValue
init|=
name|key
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|MultipartBody
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
operator|.
name|equals
argument_list|(
name|keyValue
argument_list|)
operator|||
operator|(
name|MultipartBody
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
operator|+
literal|".embedded"
operator|)
operator|.
name|equals
argument_list|(
name|keyValue
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|createAttachments
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|CacheSizeExceededException
name|e
parameter_list|)
block|{
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
literal|"cxf.io.cacheinput"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|,
literal|413
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|HeaderSizeExceededException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|,
literal|413
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|keyValue
operator|.
name|equals
argument_list|(
literal|"WRITE-"
operator|+
name|Message
operator|.
name|ATTACHMENTS
argument_list|)
condition|)
block|{
return|return
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ATTACHMENTS
argument_list|)
return|;
block|}
name|Message
name|currentMessage
init|=
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|currentMessage
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
condition|)
block|{
if|if
condition|(
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|currentMessage
return|;
block|}
name|Exchange
name|exchange
init|=
name|currentMessage
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|Message
name|otherMessage
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|==
name|currentMessage
condition|?
name|exchange
operator|.
name|getOutMessage
argument_list|()
else|:
name|exchange
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|otherMessage
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|otherMessage
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|value
return|;
block|}
specifier|private
name|Message
name|getCurrentMessage
parameter_list|()
block|{
name|Message
name|currentMessage
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentMessage
operator|==
literal|null
condition|)
block|{
name|currentMessage
operator|=
name|m
expr_stmt|;
block|}
return|return
name|currentMessage
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|format
parameter_list|)
block|{
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|m
argument_list|)
operator|&&
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Message
name|inMessage
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
return|return
name|inMessage
operator|.
name|getContent
argument_list|(
name|format
argument_list|)
return|;
block|}
return|return
name|m
operator|.
name|getContent
argument_list|(
name|format
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getContextualProperty
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Object
name|value
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|&&
name|key
operator|.
name|getClass
argument_list|()
operator|==
name|Class
operator|.
name|class
condition|)
block|{
return|return
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|key
argument_list|)
return|;
block|}
return|return
name|value
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|contextClass
parameter_list|)
block|{
return|return
name|getContext
argument_list|(
name|contextClass
argument_list|,
name|contextClass
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getContext
parameter_list|(
name|Type
name|genericType
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|JAXRSUtils
operator|.
name|createContextValue
argument_list|(
name|m
argument_list|,
name|genericType
argument_list|,
name|clazz
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
name|T
name|getResolver
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|resolverClazz
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|resolveClazz
parameter_list|)
block|{
if|if
condition|(
name|ContextResolver
operator|.
name|class
operator|==
name|resolverClazz
condition|)
block|{
return|return
name|resolverClazz
operator|.
name|cast
argument_list|(
name|getContext
argument_list|(
name|resolveClazz
argument_list|,
name|ContextResolver
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Request
name|getRequest
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|Request
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|HttpHeaders
name|getHttpHeaders
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|HttpHeaders
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|Providers
name|getProviders
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|Providers
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|SecurityContext
name|getSecurityContext
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|UriInfo
name|getUriInfo
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|UriInfo
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|HttpServletRequest
name|getHttpServletRequest
parameter_list|()
block|{
try|try
block|{
return|return
name|getContext
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|HttpServletResponse
name|getHttpServletResponse
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|ServletConfig
name|getServletConfig
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|getContext
argument_list|(
name|ServletContext
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|MultipartBody
operator|.
name|OUTBOUND_MESSAGE_ATTACHMENTS
operator|.
name|equals
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|convertToAttachments
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|Message
name|currentMessage
init|=
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|currentMessage
operator|.
name|put
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|currentMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|key
operator|.
name|toString
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|convertToAttachments
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|handlers
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|atts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|handlers
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Attachment
name|handler
init|=
operator|(
name|Attachment
operator|)
name|handlers
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|AttachmentImpl
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|handler
operator|.
name|getContentId
argument_list|()
argument_list|,
name|handler
operator|.
name|getDataHandler
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|handler
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|att
operator|.
name|setHeader
argument_list|(
name|key
argument_list|,
name|handler
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|att
operator|.
name|setXOP
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
name|Message
name|outMessage
init|=
name|getOutMessage
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|setAttachments
argument_list|(
name|atts
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|WRITE_ATTACHMENTS
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|Attachment
name|root
init|=
operator|(
name|Attachment
operator|)
name|handlers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|rootContentType
init|=
name|root
operator|.
name|getContentType
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rootHeaders
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|(
name|root
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|AttachmentUtil
operator|.
name|isMtomEnabled
argument_list|(
name|outMessage
argument_list|)
condition|)
block|{
name|rootHeaders
operator|.
name|putSingle
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|rootContentType
argument_list|)
expr_stmt|;
block|}
name|String
name|messageContentType
init|=
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|int
name|index
init|=
name|messageContentType
operator|.
name|indexOf
argument_list|(
literal|";type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|messageContentType
operator|=
name|messageContentType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|AttachmentOutputInterceptor
name|attInterceptor
init|=
operator|new
name|AttachmentOutputInterceptor
argument_list|(
name|messageContentType
argument_list|,
name|rootHeaders
argument_list|)
decl_stmt|;
name|outMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|rootContentType
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
name|allHeaders
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
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|allHeaders
operator|!=
literal|null
condition|)
block|{
name|allHeaders
operator|.
name|remove
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
name|attInterceptor
operator|.
name|handleMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|getOutMessage
parameter_list|()
block|{
name|Message
name|message
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
name|Endpoint
name|ep
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|message
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
argument_list|()
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|m
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|message
return|;
block|}
specifier|private
name|MultipartBody
name|createAttachments
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|Message
name|inMessage
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|boolean
name|embeddedAttachment
init|=
name|inMessage
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.multipart.embedded"
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|Object
name|o
init|=
name|inMessage
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|MultipartBody
operator|)
name|o
return|;
block|}
if|if
condition|(
name|embeddedAttachment
condition|)
block|{
name|inMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_DIRECTORY
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_DIRECTORY
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MAX_SIZE
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MAX_SIZE
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MAX_HEADER_SIZE
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MAX_HEADER_SIZE
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.multipart.embedded.input"
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.multipart.embedded.ctype"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|new
name|AttachmentInputInterceptor
argument_list|()
operator|.
name|handleMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Attachment
argument_list|>
name|newAttachments
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
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
name|inMessage
operator|.
name|get
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_PART_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|Attachment
name|first
init|=
operator|new
name|Attachment
argument_list|(
name|AttachmentUtil
operator|.
name|createAttachment
argument_list|(
name|inMessage
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|headers
argument_list|)
argument_list|,
operator|new
name|ProvidersImpl
argument_list|(
name|inMessage
argument_list|)
argument_list|)
decl_stmt|;
name|newAttachments
operator|.
name|add
argument_list|(
name|first
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|childAttachments
init|=
name|inMessage
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|childAttachments
operator|==
literal|null
condition|)
block|{
name|childAttachments
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
name|a
range|:
name|childAttachments
control|)
block|{
name|newAttachments
operator|.
name|add
argument_list|(
operator|new
name|Attachment
argument_list|(
name|a
argument_list|,
operator|new
name|ProvidersImpl
argument_list|(
name|inMessage
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|MediaType
name|mt
init|=
name|embeddedAttachment
condition|?
operator|(
name|MediaType
operator|)
name|inMessage
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.multipart.embedded.ctype"
argument_list|)
else|:
name|getHttpHeaders
argument_list|()
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|new
name|MultipartBody
argument_list|(
name|newAttachments
argument_list|,
name|mt
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|propertyName
argument_list|,
name|body
argument_list|)
expr_stmt|;
return|return
name|body
return|;
block|}
block|}
end_class

end_unit

