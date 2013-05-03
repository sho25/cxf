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
name|impl
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
name|annotation
operator|.
name|Annotation
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
name|net
operator|.
name|URI
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
name|Date
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ProcessingException
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
name|client
operator|.
name|ResponseProcessingException
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
name|EntityTag
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
name|GenericType
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
name|Link
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
name|NewCookie
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
name|Response
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
name|Response
operator|.
name|Status
operator|.
name|Family
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
name|ReaderInterceptor
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
name|IOUtils
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
name|provider
operator|.
name|ProviderFactory
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
name|HttpUtils
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
name|Message
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ResponseImpl
extends|extends
name|Response
block|{
specifier|private
name|int
name|status
decl_stmt|;
specifier|private
name|Object
name|entity
decl_stmt|;
specifier|private
name|Annotation
index|[]
name|entityAnnotations
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|metadata
decl_stmt|;
specifier|private
name|Message
name|responseMessage
decl_stmt|;
specifier|private
name|boolean
name|entityClosed
decl_stmt|;
specifier|private
name|boolean
name|entityBufferred
decl_stmt|;
name|ResponseImpl
parameter_list|(
name|int
name|s
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|s
expr_stmt|;
block|}
name|ResponseImpl
parameter_list|(
name|int
name|s
parameter_list|,
name|Object
name|e
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|s
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|e
expr_stmt|;
block|}
name|void
name|addMetadata
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|meta
expr_stmt|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|s
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setEntity
parameter_list|(
name|Object
name|e
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|e
expr_stmt|;
name|this
operator|.
name|entityAnnotations
operator|=
name|anns
expr_stmt|;
block|}
specifier|public
name|void
name|setEntityAnnotations
parameter_list|(
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|this
operator|.
name|entityAnnotations
operator|=
name|anns
expr_stmt|;
block|}
specifier|public
name|Annotation
index|[]
name|getEntityAnnotations
parameter_list|()
block|{
return|return
name|entityAnnotations
return|;
block|}
comment|//TODO: This method is needed because on the client side the
comment|// Response processing is done after the chain completes, thus
comment|// PhaseInterceptorChain.getCurrentMessage() returns null.
comment|// The refactoring will be required
specifier|public
name|void
name|setMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|responseMessage
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|int
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|StatusType
name|getStatusInfo
parameter_list|()
block|{
return|return
operator|new
name|Response
operator|.
name|StatusType
argument_list|()
block|{
specifier|public
name|Family
name|getFamily
parameter_list|()
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|Family
operator|.
name|familyOf
argument_list|(
name|ResponseImpl
operator|.
name|this
operator|.
name|status
argument_list|)
return|;
block|}
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
name|Response
operator|.
name|Status
name|statusEnum
init|=
name|Response
operator|.
name|Status
operator|.
name|fromStatusCode
argument_list|(
name|ResponseImpl
operator|.
name|this
operator|.
name|status
argument_list|)
decl_stmt|;
return|return
name|statusEnum
operator|!=
literal|null
condition|?
name|statusEnum
operator|.
name|getReasonPhrase
argument_list|()
else|:
literal|""
return|;
block|}
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|ResponseImpl
operator|.
name|this
operator|.
name|status
return|;
block|}
block|}
return|;
block|}
specifier|public
name|Object
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|boolean
name|hasEntity
parameter_list|()
block|{
return|return
name|entity
operator|!=
literal|null
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getMetadata
parameter_list|()
block|{
return|return
name|getHeaders
argument_list|()
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|metadata
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getStringHeaders
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|metadata
operator|.
name|size
argument_list|()
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
name|Object
argument_list|>
argument_list|>
name|entry
range|:
name|metadata
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|toListOfStrings
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|headers
return|;
block|}
specifier|private
name|String
name|getHeader
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|Object
name|value
init|=
name|metadata
operator|.
name|getFirst
argument_list|(
name|header
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
literal|null
else|:
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getHeaderString
parameter_list|(
name|String
name|header
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|methodValues
init|=
name|metadata
operator|.
name|get
argument_list|(
name|header
argument_list|)
decl_stmt|;
return|return
name|HttpUtils
operator|.
name|getHeaderString
argument_list|(
name|toListOfStrings
argument_list|(
name|methodValues
argument_list|)
argument_list|)
return|;
block|}
comment|// This conversion is needed as some values may not be Strings
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|toListOfStrings
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|stringValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
name|stringValues
operator|.
name|add
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|stringValues
return|;
block|}
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getAllowedMethods
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|methodValues
init|=
name|metadata
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|ALLOW
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodValues
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|methodValues
control|)
block|{
name|methods
operator|.
name|add
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|methods
return|;
block|}
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|NewCookie
argument_list|>
name|getCookies
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|cookieValues
init|=
name|metadata
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|SET_COOKIE
argument_list|)
decl_stmt|;
if|if
condition|(
name|cookieValues
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|NewCookie
argument_list|>
name|cookies
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|NewCookie
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|cookieValues
control|)
block|{
name|NewCookie
name|newCookie
init|=
name|NewCookie
operator|.
name|valueOf
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|cookies
operator|.
name|put
argument_list|(
name|newCookie
operator|.
name|getName
argument_list|()
argument_list|,
name|newCookie
argument_list|)
expr_stmt|;
block|}
return|return
name|cookies
return|;
block|}
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
return|return
name|doGetDate
argument_list|(
name|HttpHeaders
operator|.
name|DATE
argument_list|)
return|;
block|}
specifier|private
name|Date
name|doGetDate
parameter_list|(
name|String
name|dateHeader
parameter_list|)
block|{
return|return
name|HttpUtils
operator|.
name|getHttpDate
argument_list|(
name|getHeader
argument_list|(
name|dateHeader
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|EntityTag
name|getEntityTag
parameter_list|()
block|{
name|String
name|header
init|=
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|ETAG
argument_list|)
decl_stmt|;
return|return
name|header
operator|==
literal|null
condition|?
literal|null
else|:
name|EntityTag
operator|.
name|valueOf
argument_list|(
name|header
argument_list|)
return|;
block|}
specifier|public
name|Locale
name|getLanguage
parameter_list|()
block|{
return|return
name|HttpUtils
operator|.
name|getLocale
argument_list|(
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Date
name|getLastModified
parameter_list|()
block|{
return|return
name|doGetDate
argument_list|(
name|HttpHeaders
operator|.
name|LAST_MODIFIED
argument_list|)
return|;
block|}
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|HttpUtils
operator|.
name|getContentLength
argument_list|(
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LENGTH
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|URI
name|getLocation
parameter_list|()
block|{
name|String
name|header
init|=
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|LOCATION
argument_list|)
decl_stmt|;
return|return
name|header
operator|==
literal|null
condition|?
literal|null
else|:
name|URI
operator|.
name|create
argument_list|(
name|header
argument_list|)
return|;
block|}
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
name|String
name|header
init|=
name|getHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|header
operator|==
literal|null
condition|?
literal|null
else|:
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|header
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasLink
parameter_list|(
name|String
name|relation
parameter_list|)
block|{
return|return
name|getLink
argument_list|(
name|relation
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|Link
name|getLink
parameter_list|(
name|String
name|relation
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Link
argument_list|>
argument_list|>
name|entries
init|=
name|getAllLinks
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Link
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|contains
argument_list|(
name|relation
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Link
operator|.
name|Builder
name|getLinkBuilder
parameter_list|(
name|String
name|relation
parameter_list|)
block|{
return|return
name|Link
operator|.
name|fromLink
argument_list|(
name|getLink
argument_list|(
name|relation
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Set
argument_list|<
name|Link
argument_list|>
name|getLinks
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|Link
argument_list|>
argument_list|(
name|getAllLinks
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Link
argument_list|>
name|getAllLinks
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|linkValues
init|=
name|metadata
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|LINK
argument_list|)
decl_stmt|;
if|if
condition|(
name|linkValues
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Link
argument_list|>
name|links
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Link
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|linkValues
control|)
block|{
name|Link
name|link
init|=
name|Link
operator|.
name|valueOf
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|links
operator|.
name|put
argument_list|(
name|link
operator|.
name|getRel
argument_list|()
argument_list|,
name|link
argument_list|)
expr_stmt|;
block|}
return|return
name|links
return|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readEntity
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
throws|throws
name|ProcessingException
throws|,
name|IllegalStateException
block|{
return|return
name|readEntity
argument_list|(
name|cls
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readEntity
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|genType
parameter_list|)
throws|throws
name|ProcessingException
throws|,
name|IllegalStateException
block|{
return|return
name|readEntity
argument_list|(
name|genType
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readEntity
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
throws|throws
name|ProcessingException
throws|,
name|IllegalStateException
block|{
return|return
name|doReadEntity
argument_list|(
name|cls
argument_list|,
name|cls
argument_list|,
name|anns
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|readEntity
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|genType
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
throws|throws
name|ProcessingException
throws|,
name|IllegalStateException
block|{
return|return
name|doReadEntity
argument_list|(
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|genType
operator|.
name|getRawType
argument_list|()
argument_list|,
name|genType
operator|.
name|getType
argument_list|()
argument_list|,
name|anns
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|doReadEntity
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
throws|throws
name|ProcessingException
throws|,
name|IllegalStateException
block|{
name|checkEntityIsClosed
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|hasEntity
argument_list|()
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
name|isAssignableFrom
argument_list|(
name|entity
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|T
name|response
init|=
name|cls
operator|.
name|cast
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|closeIfNotBufferred
argument_list|(
name|cls
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
if|if
condition|(
name|responseMessage
operator|!=
literal|null
operator|&&
name|entity
operator|instanceof
name|InputStream
condition|)
block|{
name|MediaType
name|mediaType
init|=
name|getMediaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|mediaType
operator|==
literal|null
condition|)
block|{
name|mediaType
operator|=
name|MediaType
operator|.
name|WILDCARD_TYPE
expr_stmt|;
block|}
name|List
argument_list|<
name|ReaderInterceptor
argument_list|>
name|readers
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|responseMessage
argument_list|)
operator|.
name|createMessageBodyReaderInterceptor
argument_list|(
name|cls
argument_list|,
name|t
argument_list|,
name|anns
argument_list|,
name|mediaType
argument_list|,
name|responseMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|readers
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|responseMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|this
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cls
operator|.
name|cast
argument_list|(
name|JAXRSUtils
operator|.
name|readFromMessageBodyReader
argument_list|(
name|readers
argument_list|,
name|cls
argument_list|,
name|t
argument_list|,
name|anns
argument_list|,
name|InputStream
operator|.
name|class
operator|.
name|cast
argument_list|(
name|entity
argument_list|)
argument_list|,
name|mediaType
argument_list|,
name|responseMessage
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ResponseProcessingException
argument_list|(
name|this
argument_list|,
name|ex
argument_list|)
throw|;
block|}
finally|finally
block|{
name|closeIfNotBufferred
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
block|}
throw|throw
operator|new
name|ResponseProcessingException
argument_list|(
name|this
argument_list|,
literal|"No Message Body reader is available"
argument_list|)
throw|;
block|}
specifier|private
name|void
name|closeIfNotBufferred
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|responseCls
parameter_list|)
block|{
if|if
condition|(
operator|!
name|entityBufferred
operator|&&
operator|!
name|InputStream
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|responseCls
argument_list|)
condition|)
block|{
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|bufferEntity
parameter_list|()
throws|throws
name|ProcessingException
block|{
if|if
condition|(
name|entityClosed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|entityBufferred
operator|&&
name|entity
operator|instanceof
name|InputStream
condition|)
block|{
try|try
block|{
name|InputStream
name|oldEntity
init|=
operator|(
name|InputStream
operator|)
name|entity
decl_stmt|;
name|entity
operator|=
name|IOUtils
operator|.
name|loadIntoBAIS
argument_list|(
name|oldEntity
argument_list|)
expr_stmt|;
name|entityBufferred
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ResponseProcessingException
argument_list|(
name|this
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|entityBufferred
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|ProcessingException
block|{
if|if
condition|(
operator|!
name|entityClosed
condition|)
block|{
if|if
condition|(
name|entity
operator|instanceof
name|InputStream
condition|)
block|{
try|try
block|{
operator|(
operator|(
name|InputStream
operator|)
name|entity
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|entity
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ResponseProcessingException
argument_list|(
name|this
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
name|entityClosed
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkEntityIsClosed
parameter_list|()
block|{
if|if
condition|(
name|entityClosed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Entity is not available"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

