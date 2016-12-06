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
name|Arrays
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
name|core
operator|.
name|CacheControl
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
name|NioErrorHandler
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
name|NioWriterHandler
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
name|ResponseBuilder
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
name|core
operator|.
name|Variant
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
name|nio
operator|.
name|NioWriteEntity
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
specifier|public
class|class
name|ResponseBuilderImpl
extends|extends
name|ResponseBuilder
implements|implements
name|Cloneable
block|{
specifier|private
name|int
name|status
init|=
literal|200
decl_stmt|;
specifier|private
name|boolean
name|statusSet
decl_stmt|;
specifier|private
name|Object
name|entity
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|metadata
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Annotation
index|[]
name|annotations
decl_stmt|;
specifier|public
name|ResponseBuilderImpl
parameter_list|()
block|{     }
specifier|private
name|ResponseBuilderImpl
parameter_list|(
name|ResponseBuilderImpl
name|copy
parameter_list|)
block|{
name|status
operator|=
name|copy
operator|.
name|status
expr_stmt|;
name|statusSet
operator|=
name|copy
operator|.
name|statusSet
expr_stmt|;
name|metadata
operator|.
name|putAll
argument_list|(
name|copy
operator|.
name|metadata
argument_list|)
expr_stmt|;
name|entity
operator|=
name|copy
operator|.
name|entity
expr_stmt|;
block|}
specifier|public
name|Response
name|build
parameter_list|()
block|{
if|if
condition|(
name|entity
operator|==
literal|null
operator|&&
operator|!
name|statusSet
condition|)
block|{
name|status
operator|=
literal|204
expr_stmt|;
block|}
name|ResponseImpl
name|r
init|=
operator|new
name|ResponseImpl
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|metadata
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|r
operator|.
name|addMetadata
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|r
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|,
name|annotations
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|public
name|ResponseBuilder
name|status
parameter_list|(
name|int
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
argument_list|<
literal|100
operator|||
name|s
argument_list|>
literal|599
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal status value : "
operator|+
name|s
argument_list|)
throw|;
block|}
name|status
operator|=
name|s
expr_stmt|;
name|statusSet
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ResponseBuilder
name|entity
parameter_list|(
name|Object
name|e
parameter_list|)
block|{
name|entity
operator|=
name|e
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ResponseBuilder
name|type
parameter_list|(
name|MediaType
name|type
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|type
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|language
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
name|locale
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|language
parameter_list|(
name|String
name|language
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
name|language
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|location
parameter_list|(
name|URI
name|loc
parameter_list|)
block|{
if|if
condition|(
operator|!
name|loc
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|Message
name|currentMessage
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentMessage
operator|!=
literal|null
condition|)
block|{
name|UriInfo
name|ui
init|=
operator|new
name|UriInfoImpl
argument_list|(
name|currentMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|loc
operator|=
name|ui
operator|.
name|getBaseUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|loc
operator|.
name|getRawPath
argument_list|()
argument_list|)
operator|.
name|replaceQuery
argument_list|(
name|loc
operator|.
name|getRawQuery
argument_list|()
argument_list|)
operator|.
name|fragment
argument_list|(
name|loc
operator|.
name|getRawFragment
argument_list|()
argument_list|)
operator|.
name|buildFromEncoded
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|LOCATION
argument_list|,
name|loc
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|contentLocation
parameter_list|(
name|URI
name|location
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LOCATION
argument_list|,
name|location
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|tag
parameter_list|(
name|EntityTag
name|tag
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|ETAG
argument_list|,
name|tag
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|tag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
specifier|final
name|String
name|doubleQuote
init|=
literal|"\""
decl_stmt|;
if|if
condition|(
name|tag
operator|!=
literal|null
operator|&&
operator|!
name|tag
operator|.
name|startsWith
argument_list|(
name|doubleQuote
argument_list|)
condition|)
block|{
name|tag
operator|=
name|doubleQuote
operator|+
name|tag
operator|+
name|doubleQuote
expr_stmt|;
block|}
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|ETAG
argument_list|,
name|tag
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|lastModified
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|LAST_MODIFIED
argument_list|,
name|date
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|cacheControl
parameter_list|(
name|CacheControl
name|cacheControl
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
name|cacheControl
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|expires
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|EXPIRES
argument_list|,
name|date
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|cookie
parameter_list|(
name|NewCookie
modifier|...
name|cookies
parameter_list|)
block|{
return|return
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|SET_COOKIE
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|cookies
argument_list|)
return|;
block|}
specifier|public
name|ResponseBuilder
name|header
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|addHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|variant
parameter_list|(
name|Variant
name|variant
parameter_list|)
block|{
name|type
argument_list|(
name|variant
operator|==
literal|null
condition|?
literal|null
else|:
name|variant
operator|.
name|getMediaType
argument_list|()
argument_list|)
expr_stmt|;
name|language
argument_list|(
name|variant
operator|==
literal|null
condition|?
literal|null
else|:
name|variant
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|,
name|variant
operator|==
literal|null
condition|?
literal|null
else|:
name|variant
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|variants
parameter_list|(
name|List
argument_list|<
name|Variant
argument_list|>
name|variants
parameter_list|)
block|{
if|if
condition|(
name|variants
operator|==
literal|null
condition|)
block|{
name|metadata
operator|.
name|remove
argument_list|(
name|HttpHeaders
operator|.
name|VARY
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|String
name|acceptVary
init|=
literal|null
decl_stmt|;
name|String
name|acceptLangVary
init|=
literal|null
decl_stmt|;
name|String
name|acceptEncVary
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Variant
name|v
range|:
name|variants
control|)
block|{
name|MediaType
name|mt
init|=
name|v
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
if|if
condition|(
name|mt
operator|!=
literal|null
condition|)
block|{
name|acceptVary
operator|=
name|HttpHeaders
operator|.
name|ACCEPT
expr_stmt|;
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|mt
argument_list|)
expr_stmt|;
block|}
name|Locale
name|l
init|=
name|v
operator|.
name|getLanguage
argument_list|()
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
condition|)
block|{
name|acceptLangVary
operator|=
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
expr_stmt|;
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|String
name|enc
init|=
name|v
operator|.
name|getEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|enc
operator|!=
literal|null
condition|)
block|{
name|acceptEncVary
operator|=
name|HttpHeaders
operator|.
name|ACCEPT_ENCODING
expr_stmt|;
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|,
name|enc
argument_list|)
expr_stmt|;
block|}
block|}
name|handleVaryValue
argument_list|(
name|acceptVary
argument_list|,
name|acceptLangVary
argument_list|,
name|acceptEncVary
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|void
name|handleVaryValue
parameter_list|(
name|String
modifier|...
name|values
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|varyValues
init|=
name|metadata
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|VARY
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|v
range|:
name|values
control|)
block|{
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|metadata
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|varyValues
operator|!=
literal|null
condition|)
block|{
name|varyValues
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|VARY
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|//  CHECKSTYLE:OFF
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|clone
parameter_list|()
block|{
return|return
operator|new
name|ResponseBuilderImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|//  CHECKSTYLE:ON
specifier|private
name|void
name|reset
parameter_list|()
block|{
name|metadata
operator|.
name|clear
argument_list|()
expr_stmt|;
name|entity
operator|=
literal|null
expr_stmt|;
name|annotations
operator|=
literal|null
expr_stmt|;
name|status
operator|=
literal|200
expr_stmt|;
block|}
specifier|private
name|ResponseBuilder
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|metadata
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|metadata
operator|.
name|putSingle
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|private
name|ResponseBuilder
name|addHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|length
operator|>=
literal|1
operator|&&
name|values
index|[
literal|0
index|]
operator|!=
literal|null
condition|)
block|{
name|boolean
name|isAllowHeader
init|=
name|HttpHeaders
operator|.
name|ALLOW
operator|.
name|equals
argument_list|(
name|name
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
name|Object
name|thevalue
init|=
name|isAllowHeader
condition|?
name|value
operator|.
name|toString
argument_list|()
operator|.
name|toUpperCase
argument_list|()
else|:
name|value
decl_stmt|;
if|if
condition|(
operator|!
name|valueExists
argument_list|(
name|name
argument_list|,
name|thevalue
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|thevalue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|metadata
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|private
name|boolean
name|valueExists
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|metadata
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|values
operator|==
literal|null
condition|?
literal|false
else|:
name|values
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|allow
parameter_list|(
name|String
modifier|...
name|methods
parameter_list|)
block|{
return|return
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|ALLOW
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|methods
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|allow
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|methods
parameter_list|)
block|{
if|if
condition|(
name|methods
operator|==
literal|null
condition|)
block|{
return|return
name|allow
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|allow
argument_list|(
name|methods
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|methods
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|encoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
return|return
name|setHeader
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_ENCODING
argument_list|,
name|encoding
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|entity
parameter_list|(
name|Object
name|ent
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
block|{
name|this
operator|.
name|annotations
operator|=
name|anns
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|ent
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|link
parameter_list|(
name|URI
name|href
parameter_list|,
name|String
name|rel
parameter_list|)
block|{
name|Link
operator|.
name|Builder
name|linkBuilder
init|=
operator|new
name|LinkBuilderImpl
argument_list|()
decl_stmt|;
return|return
name|links
argument_list|(
name|linkBuilder
operator|.
name|uri
argument_list|(
name|href
argument_list|)
operator|.
name|rel
argument_list|(
name|rel
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|link
parameter_list|(
name|String
name|href
parameter_list|,
name|String
name|rel
parameter_list|)
block|{
name|Link
operator|.
name|Builder
name|linkBuilder
init|=
operator|new
name|LinkBuilderImpl
argument_list|()
decl_stmt|;
return|return
name|links
argument_list|(
name|linkBuilder
operator|.
name|uri
argument_list|(
name|href
argument_list|)
operator|.
name|rel
argument_list|(
name|rel
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|links
parameter_list|(
name|Link
modifier|...
name|links
parameter_list|)
block|{
return|return
name|addHeader
argument_list|(
name|HttpHeaders
operator|.
name|LINK
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|links
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|replaceAll
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|metadata
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
name|metadata
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|variants
parameter_list|(
name|Variant
modifier|...
name|variants
parameter_list|)
block|{
if|if
condition|(
name|variants
operator|==
literal|null
condition|)
block|{
return|return
name|variants
argument_list|(
operator|(
name|List
argument_list|<
name|Variant
argument_list|>
operator|)
literal|null
argument_list|)
return|;
block|}
return|return
name|variants
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|variants
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|entity
parameter_list|(
name|NioWriterHandler
name|writerHandler
parameter_list|)
block|{
return|return
name|entity
argument_list|(
name|writerHandler
argument_list|,
operator|(
name|NioErrorHandler
operator|)
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ResponseBuilder
name|entity
parameter_list|(
name|NioWriterHandler
name|writerHandler
parameter_list|,
name|NioErrorHandler
name|errorHandler
parameter_list|)
block|{
name|entity
operator|=
operator|new
name|NioWriteEntity
argument_list|(
name|writerHandler
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

