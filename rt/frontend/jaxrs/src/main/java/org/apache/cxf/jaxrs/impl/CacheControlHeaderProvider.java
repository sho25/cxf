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
name|HashMap
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|ext
operator|.
name|RuntimeDelegate
operator|.
name|HeaderDelegate
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
name|CacheControlHeaderProvider
implements|implements
name|HeaderDelegate
argument_list|<
name|CacheControl
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CACHE_CONTROL_SEPARATOR_PROPERTY
init|=
literal|"org.apache.cxf.http.cache-control.separator"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_SEPARATOR
init|=
literal|","
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPLEX_HEADER_EXPRESSION
init|=
literal|"(([\\w-]+=\"[^\"]*\")|([\\w-]+=[\\w]+)|([\\w-]+))"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|COMPLEX_HEADER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|COMPLEX_HEADER_EXPRESSION
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PUBLIC
init|=
literal|"public"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PRIVATE
init|=
literal|"private"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_CACHE
init|=
literal|"no-cache"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_STORE
init|=
literal|"no-store"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_TRANSFORM
init|=
literal|"no-transform"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MUST_REVALIDATE
init|=
literal|"must-revalidate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROXY_REVALIDATE
init|=
literal|"proxy-revalidate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MAX_AGE
init|=
literal|"max-age"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SMAX_AGE
init|=
literal|"s-maxage"
decl_stmt|;
specifier|public
name|CacheControl
name|fromString
parameter_list|(
name|String
name|c
parameter_list|)
block|{
name|boolean
name|isPrivate
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|privateFields
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|noCache
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|noCacheFields
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|noStore
init|=
literal|false
decl_stmt|;
name|boolean
name|noTransform
init|=
literal|false
decl_stmt|;
name|boolean
name|mustRevalidate
init|=
literal|false
decl_stmt|;
name|boolean
name|proxyRevalidate
init|=
literal|false
decl_stmt|;
name|int
name|maxAge
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|sMaxAge
init|=
operator|-
literal|1
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extensions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
index|[]
name|tokens
init|=
name|getTokens
argument_list|(
name|c
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|rawToken
range|:
name|tokens
control|)
block|{
name|String
name|token
init|=
name|rawToken
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|MAX_AGE
argument_list|)
condition|)
block|{
name|maxAge
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|token
operator|.
name|substring
argument_list|(
name|MAX_AGE
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|SMAX_AGE
argument_list|)
condition|)
block|{
name|sMaxAge
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|token
operator|.
name|substring
argument_list|(
name|SMAX_AGE
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|PUBLIC
argument_list|)
condition|)
block|{
comment|// ignore
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|NO_STORE
argument_list|)
condition|)
block|{
name|noStore
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|NO_TRANSFORM
argument_list|)
condition|)
block|{
name|noTransform
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|MUST_REVALIDATE
argument_list|)
condition|)
block|{
name|mustRevalidate
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|PROXY_REVALIDATE
argument_list|)
condition|)
block|{
name|proxyRevalidate
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|PRIVATE
argument_list|)
condition|)
block|{
name|isPrivate
operator|=
literal|true
expr_stmt|;
name|addFields
argument_list|(
name|privateFields
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|.
name|startsWith
argument_list|(
name|NO_CACHE
argument_list|)
condition|)
block|{
name|noCache
operator|=
literal|true
expr_stmt|;
name|addFields
argument_list|(
name|noCacheFields
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
index|[]
name|extPair
init|=
name|token
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|extPair
operator|.
name|length
operator|==
literal|2
condition|?
name|extPair
index|[
literal|1
index|]
else|:
literal|""
decl_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|extPair
index|[
literal|0
index|]
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|CacheControl
name|cc
init|=
operator|new
name|CacheControl
argument_list|()
decl_stmt|;
name|cc
operator|.
name|setMaxAge
argument_list|(
name|maxAge
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setSMaxAge
argument_list|(
name|sMaxAge
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setPrivate
argument_list|(
name|isPrivate
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|addAll
argument_list|(
name|privateFields
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setMustRevalidate
argument_list|(
name|mustRevalidate
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setProxyRevalidate
argument_list|(
name|proxyRevalidate
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setNoCache
argument_list|(
name|noCache
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getNoCacheFields
argument_list|()
operator|.
name|addAll
argument_list|(
name|noCacheFields
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setNoStore
argument_list|(
name|noStore
argument_list|)
expr_stmt|;
name|cc
operator|.
name|setNoTransform
argument_list|(
name|noTransform
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getCacheExtension
argument_list|()
operator|.
name|putAll
argument_list|(
name|extensions
argument_list|)
expr_stmt|;
return|return
name|cc
return|;
block|}
specifier|private
name|String
index|[]
name|getTokens
parameter_list|(
name|String
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
if|if
condition|(
name|c
operator|.
name|contains
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|COMPLEX_HEADER_PATTERN
operator|.
name|matcher
argument_list|(
name|c
argument_list|)
decl_stmt|;
while|while
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|val
init|=
name|m
operator|.
name|group
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
return|;
block|}
name|String
name|separator
init|=
name|getSeparator
argument_list|()
decl_stmt|;
return|return
name|c
operator|.
name|split
argument_list|(
name|separator
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|CacheControl
name|c
parameter_list|)
block|{
name|String
name|separator
init|=
name|getSeparator
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|isPrivate
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|PRIVATE
argument_list|)
expr_stmt|;
name|handleFields
argument_list|(
name|c
operator|.
name|getPrivateFields
argument_list|()
argument_list|,
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|isNoCache
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|NO_CACHE
argument_list|)
expr_stmt|;
name|handleFields
argument_list|(
name|c
operator|.
name|getNoCacheFields
argument_list|()
argument_list|,
name|sb
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|isNoStore
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|NO_STORE
argument_list|)
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|isNoTransform
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|NO_TRANSFORM
argument_list|)
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|isMustRevalidate
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|MUST_REVALIDATE
argument_list|)
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|isProxyRevalidate
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|PROXY_REVALIDATE
argument_list|)
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|getMaxAge
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|MAX_AGE
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|c
operator|.
name|getMaxAge
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|getSMaxAge
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SMAX_AGE
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|c
operator|.
name|getSMaxAge
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|exts
init|=
name|c
operator|.
name|getCacheExtension
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
name|String
argument_list|>
name|entry
range|:
name|exts
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
operator|.
name|append
argument_list|(
name|v
argument_list|)
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
name|String
name|s
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|s
operator|.
name|endsWith
argument_list|(
name|separator
argument_list|)
condition|?
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|s
return|;
block|}
specifier|private
specifier|static
name|void
name|addFields
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|int
name|i
init|=
name|token
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|f
init|=
name|i
operator|==
name|token
operator|.
name|length
argument_list|()
operator|+
literal|1
condition|?
literal|""
else|:
name|token
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|length
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|f
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|||
operator|!
name|f
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
return|return;
block|}
name|f
operator|=
name|f
operator|.
name|length
argument_list|()
operator|==
literal|2
condition|?
literal|""
else|:
name|f
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|f
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|String
index|[]
name|values
init|=
name|f
operator|.
name|split
argument_list|(
literal|","
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
name|fields
operator|.
name|add
argument_list|(
name|v
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|handleFields
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|,
name|StringBuilder
name|sb
parameter_list|)
block|{
if|if
condition|(
name|fields
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|fields
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getSeparator
parameter_list|()
block|{
name|String
name|separator
init|=
name|DEFAULT_SEPARATOR
decl_stmt|;
name|Message
name|message
init|=
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|Object
name|sepProperty
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|CACHE_CONTROL_SEPARATOR_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|sepProperty
operator|!=
literal|null
condition|)
block|{
name|separator
operator|=
name|sepProperty
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|separator
operator|.
name|length
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|separator
return|;
block|}
specifier|protected
name|Message
name|getCurrentMessage
parameter_list|()
block|{
return|return
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

