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
name|ByteArrayInputStream
import|;
end_import

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
name|io
operator|.
name|Reader
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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|LinkedHashSet
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|RuntimeDelegate
operator|.
name|HeaderDelegate
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
name|io
operator|.
name|ReaderInputStream
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
name|InjectionUtils
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
specifier|final
class|class
name|ResponseImpl
extends|extends
name|Response
block|{
specifier|private
name|StatusType
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
name|outMessage
decl_stmt|;
specifier|private
name|boolean
name|entityClosed
decl_stmt|;
specifier|private
name|boolean
name|entityBufferred
decl_stmt|;
specifier|private
name|Object
name|lastEntity
decl_stmt|;
name|ResponseImpl
parameter_list|(
name|int
name|statusCode
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|createStatusType
argument_list|(
name|statusCode
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|ResponseImpl
parameter_list|(
name|int
name|statusCode
parameter_list|,
name|Object
name|entity
parameter_list|)
block|{
name|this
argument_list|(
name|statusCode
argument_list|)
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
name|ResponseImpl
parameter_list|(
name|int
name|statusCode
parameter_list|,
name|Object
name|entity
parameter_list|,
name|String
name|reasonPhrase
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|createStatusType
argument_list|(
name|statusCode
argument_list|,
name|reasonPhrase
argument_list|)
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
specifier|public
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
name|statusCode
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|createStatusType
argument_list|(
name|statusCode
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|statusCode
parameter_list|,
name|String
name|reasonPhrase
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|createStatusType
argument_list|(
name|statusCode
argument_list|,
name|reasonPhrase
argument_list|)
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
specifier|public
name|void
name|setOutMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|outMessage
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|Message
name|getOutMessage
parameter_list|()
block|{
return|return
name|this
operator|.
name|outMessage
return|;
block|}
specifier|public
name|int
name|getStatus
parameter_list|()
block|{
return|return
name|status
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
specifier|public
name|StatusType
name|getStatusInfo
parameter_list|()
block|{
return|return
name|status
return|;
block|}
specifier|public
name|Object
name|getActualEntity
parameter_list|()
block|{
name|checkEntityIsClosed
argument_list|()
expr_stmt|;
return|return
name|lastEntity
operator|!=
literal|null
condition|?
name|lastEntity
else|:
name|entity
return|;
block|}
specifier|public
name|Object
name|getEntity
parameter_list|()
block|{
return|return
name|InjectionUtils
operator|.
name|getEntity
argument_list|(
name|getActualEntity
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasEntity
parameter_list|()
block|{
return|return
name|getActualEntity
argument_list|()
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
argument_list|<>
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
name|String
name|headerName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|headerName
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
name|List
argument_list|<
name|String
argument_list|>
name|stringValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|HeaderDelegate
argument_list|<
name|Object
argument_list|>
name|hd
init|=
name|HttpUtils
operator|.
name|getHeaderDelegate
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|String
name|actualValue
init|=
name|hd
operator|==
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
name|hd
operator|.
name|toString
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|stringValues
operator|.
name|add
argument_list|(
name|actualValue
argument_list|)
expr_stmt|;
block|}
return|return
name|stringValues
return|;
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
name|Set
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|HashSet
argument_list|<>
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
argument_list|<>
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
name|Object
name|value
init|=
name|metadata
operator|.
name|getFirst
argument_list|(
name|dateHeader
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
operator|||
name|value
operator|instanceof
name|Date
condition|?
operator|(
name|Date
operator|)
name|value
else|:
name|HttpUtils
operator|.
name|getHttpDate
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|EntityTag
name|getEntityTag
parameter_list|()
block|{
name|Object
name|header
init|=
name|metadata
operator|.
name|getFirst
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
operator|||
name|header
operator|instanceof
name|EntityTag
condition|?
operator|(
name|EntityTag
operator|)
name|header
else|:
name|EntityTag
operator|.
name|valueOf
argument_list|(
name|header
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Locale
name|getLanguage
parameter_list|()
block|{
name|Object
name|header
init|=
name|metadata
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|)
decl_stmt|;
return|return
name|header
operator|==
literal|null
operator|||
name|header
operator|instanceof
name|Locale
condition|?
operator|(
name|Locale
operator|)
name|header
else|:
name|HttpUtils
operator|.
name|getLocale
argument_list|(
name|header
operator|.
name|toString
argument_list|()
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
name|Object
name|header
init|=
name|metadata
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LENGTH
argument_list|)
decl_stmt|;
return|return
name|HttpUtils
operator|.
name|getContentLength
argument_list|(
name|header
operator|==
literal|null
condition|?
literal|null
else|:
name|header
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|URI
name|getLocation
parameter_list|()
block|{
name|Object
name|header
init|=
name|metadata
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|LOCATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
name|header
operator|=
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
expr_stmt|;
block|}
return|return
name|header
operator|==
literal|null
operator|||
name|header
operator|instanceof
name|URI
condition|?
operator|(
name|URI
operator|)
name|header
else|:
name|URI
operator|.
name|create
argument_list|(
name|header
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
name|Object
name|header
init|=
name|metadata
operator|.
name|getFirst
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
operator|||
name|header
operator|instanceof
name|MediaType
condition|?
operator|(
name|MediaType
operator|)
name|header
else|:
operator|(
name|MediaType
operator|)
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|header
operator|.
name|toString
argument_list|()
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
operator|!=
literal|null
condition|)
block|{
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
name|o
operator|instanceof
name|Link
condition|?
operator|(
name|Link
operator|)
name|o
else|:
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
if|if
condition|(
name|relation
operator|.
name|equals
argument_list|(
name|link
operator|.
name|getRel
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
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
name|Link
argument_list|>
name|links
init|=
name|getAllLinks
argument_list|()
decl_stmt|;
for|for
control|(
name|Link
name|link
range|:
name|links
control|)
block|{
if|if
condition|(
name|link
operator|.
name|getRel
argument_list|()
operator|!=
literal|null
operator|&&
name|link
operator|.
name|getRel
argument_list|()
operator|.
name|equals
argument_list|(
name|relation
argument_list|)
condition|)
block|{
return|return
name|link
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
name|Link
name|link
init|=
name|getLink
argument_list|(
name|relation
argument_list|)
decl_stmt|;
return|return
name|link
operator|==
literal|null
condition|?
literal|null
else|:
name|Link
operator|.
name|fromLink
argument_list|(
name|link
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
argument_list|<>
argument_list|(
name|getAllLinks
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Set
argument_list|<
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
name|emptySet
argument_list|()
return|;
block|}
name|Set
argument_list|<
name|Link
argument_list|>
name|links
init|=
operator|new
name|LinkedHashSet
argument_list|<>
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
name|o
operator|instanceof
name|Link
condition|?
operator|(
name|Link
operator|)
name|o
else|:
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
if|if
condition|(
operator|!
name|link
operator|.
name|getUri
argument_list|()
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|URI
name|requestURI
init|=
name|URI
operator|.
name|create
argument_list|(
operator|(
name|String
operator|)
name|outMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
argument_list|)
decl_stmt|;
name|link
operator|=
name|Link
operator|.
name|fromLink
argument_list|(
name|link
argument_list|)
operator|.
name|baseUri
argument_list|(
name|requestURI
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|links
operator|.
name|add
argument_list|(
name|link
argument_list|)
expr_stmt|;
block|}
return|return
name|links
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
name|lastEntity
operator|!=
literal|null
operator|&&
name|cls
operator|.
name|isAssignableFrom
argument_list|(
name|lastEntity
operator|.
name|getClass
argument_list|()
argument_list|)
operator|&&
operator|!
operator|(
name|lastEntity
operator|instanceof
name|InputStream
operator|)
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|lastEntity
argument_list|)
return|;
block|}
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
comment|// the stream is available if entity is IS or
comment|// message contains XMLStreamReader or Reader
name|boolean
name|entityStreamAvailable
init|=
name|entityStreamAvailable
argument_list|()
decl_stmt|;
name|InputStream
name|entityStream
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|entityStreamAvailable
condition|)
block|{
comment|// try create a stream if the entity is String or Number
name|entityStream
operator|=
name|convertEntityToStreamIfPossible
argument_list|()
expr_stmt|;
name|entityStreamAvailable
operator|=
name|entityStream
operator|!=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|entity
operator|instanceof
name|InputStream
condition|)
block|{
name|entityStream
operator|=
name|InputStream
operator|.
name|class
operator|.
name|cast
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
name|inMessage
init|=
name|getResponseMessage
argument_list|()
decl_stmt|;
name|Reader
name|reader
init|=
name|inMessage
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|entityStream
operator|=
name|InputStream
operator|.
name|class
operator|.
name|cast
argument_list|(
operator|new
name|ReaderInputStream
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we need to check for readers even if no IS is set - the readers may still do it
name|List
argument_list|<
name|ReaderInterceptor
argument_list|>
name|readers
init|=
name|outMessage
operator|==
literal|null
condition|?
literal|null
else|:
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|outMessage
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
name|outMessage
argument_list|,
name|entityStreamAvailable
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
if|if
condition|(
name|entityBufferred
condition|)
block|{
name|InputStream
operator|.
name|class
operator|.
name|cast
argument_list|(
name|entity
argument_list|)
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
name|Message
name|responseMessage
init|=
name|getResponseMessage
argument_list|()
decl_stmt|;
name|responseMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|lastEntity
operator|=
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
name|entityStream
argument_list|,
name|mediaType
argument_list|,
name|responseMessage
argument_list|)
expr_stmt|;
name|autoClose
argument_list|(
name|cls
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|castLastEntity
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|autoClose
argument_list|(
name|cls
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|reportMessageHandlerProblem
argument_list|(
literal|"MSG_READER_PROBLEM"
argument_list|,
name|cls
argument_list|,
name|mediaType
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ProviderFactory
name|pf
init|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|outMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|pf
operator|!=
literal|null
condition|)
block|{
name|pf
operator|.
name|clearThreadLocalProxies
argument_list|()
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|entity
operator|!=
literal|null
operator|&&
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
name|lastEntity
operator|=
name|entity
expr_stmt|;
return|return
name|castLastEntity
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|entityStreamAvailable
condition|)
block|{
name|reportMessageHandlerProblem
argument_list|(
literal|"NO_MSG_READER"
argument_list|,
name|cls
argument_list|,
name|mediaType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The entity is not backed by an input stream, entity class is : "
operator|+
operator|(
name|entity
operator|!=
literal|null
condition|?
name|entity
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
name|cls
operator|.
name|getName
argument_list|()
operator|)
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|castLastEntity
parameter_list|()
block|{
return|return
operator|(
name|T
operator|)
name|lastEntity
return|;
block|}
specifier|public
name|InputStream
name|convertEntityToStreamIfPossible
parameter_list|()
block|{
name|String
name|stringEntity
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|entity
operator|instanceof
name|String
operator|||
name|entity
operator|instanceof
name|Number
condition|)
block|{
name|stringEntity
operator|=
name|entity
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|stringEntity
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|stringEntity
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
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
name|ProcessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|boolean
name|entityStreamAvailable
parameter_list|()
block|{
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
name|Message
name|inMessage
init|=
name|getResponseMessage
argument_list|()
decl_stmt|;
return|return
name|inMessage
operator|!=
literal|null
operator|&&
operator|(
name|inMessage
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|||
name|inMessage
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|)
return|;
block|}
return|return
name|entity
operator|instanceof
name|InputStream
return|;
block|}
specifier|private
name|Message
name|getResponseMessage
parameter_list|()
block|{
name|Message
name|responseMessage
init|=
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseMessage
operator|==
literal|null
condition|)
block|{
name|responseMessage
operator|=
name|outMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInFaultMessage
argument_list|()
expr_stmt|;
block|}
return|return
name|responseMessage
return|;
block|}
specifier|private
name|void
name|reportMessageHandlerProblem
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|MediaType
name|ct
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|String
name|errorMessage
init|=
name|JAXRSUtils
operator|.
name|logMessageHandlerProblem
argument_list|(
name|name
argument_list|,
name|cls
argument_list|,
name|ct
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ResponseProcessingException
argument_list|(
name|this
argument_list|,
name|errorMessage
argument_list|,
name|cause
argument_list|)
throw|;
block|}
specifier|protected
name|void
name|autoClose
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|boolean
name|exception
parameter_list|)
block|{
if|if
condition|(
operator|!
name|entityBufferred
operator|&&
operator|!
name|JAXRSUtils
operator|.
name|isStreamingOutType
argument_list|(
name|cls
argument_list|)
operator|&&
operator|(
name|exception
operator|||
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|outMessage
argument_list|,
literal|"response.stream.auto.close"
argument_list|)
operator|)
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
name|checkEntityIsClosed
argument_list|()
expr_stmt|;
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
name|oldEntity
operator|.
name|close
argument_list|()
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
name|entity
operator|=
literal|null
expr_stmt|;
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
specifier|private
name|Response
operator|.
name|StatusType
name|createStatusType
parameter_list|(
name|int
name|statusCode
parameter_list|,
name|String
name|reasonPhrase
parameter_list|)
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
name|statusCode
argument_list|)
return|;
block|}
specifier|public
name|String
name|getReasonPhrase
parameter_list|()
block|{
if|if
condition|(
name|reasonPhrase
operator|!=
literal|null
condition|)
block|{
return|return
name|reasonPhrase
return|;
block|}
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
name|statusCode
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
name|statusCode
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

