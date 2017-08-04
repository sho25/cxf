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
name|Collections
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
name|LinkedHashMap
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
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
specifier|public
class|class
name|MediaTypeHeaderProvider
implements|implements
name|HeaderDelegate
argument_list|<
name|MediaType
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|MediaTypeHeaderProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STRICT_MEDIA_TYPE_CHECK
init|=
literal|"org.apache.cxf.jaxrs.mediaTypeCheck.strict"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|COMPLEX_PARAMETERS
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"(([\\w-]+=\"[^\"]*\")|([\\w-]+=[\\w-/\\+]+))"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
name|map
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|MediaType
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MAX_MT_CACHE_SIZE
init|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"org.apache.cxf.jaxrs.max_mediatype_cache_size"
argument_list|,
literal|200
argument_list|)
decl_stmt|;
specifier|public
name|MediaType
name|fromString
parameter_list|(
name|String
name|mType
parameter_list|)
block|{
return|return
name|valueOf
argument_list|(
name|mType
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|MediaType
name|valueOf
parameter_list|(
name|String
name|mType
parameter_list|)
block|{
if|if
condition|(
name|mType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Media type value can not be null"
argument_list|)
throw|;
block|}
name|MediaType
name|result
init|=
name|map
operator|.
name|get
argument_list|(
name|mType
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
name|internalValueOf
argument_list|(
name|mType
argument_list|)
expr_stmt|;
specifier|final
name|int
name|size
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>=
name|MAX_MT_CACHE_SIZE
condition|)
block|{
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|mType
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|MediaType
name|internalValueOf
parameter_list|(
name|String
name|mType
parameter_list|)
block|{
name|int
name|i
init|=
name|mType
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|handleMediaTypeWithoutSubtype
argument_list|(
name|mType
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid media type string: "
operator|+
name|mType
argument_list|)
throw|;
block|}
name|int
name|paramsStart
init|=
name|mType
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|,
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|paramsStart
operator|==
operator|-
literal|1
condition|?
name|mType
operator|.
name|length
argument_list|()
else|:
name|paramsStart
decl_stmt|;
name|String
name|type
init|=
name|mType
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|String
name|subtype
init|=
name|mType
operator|.
name|substring
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|end
argument_list|)
decl_stmt|;
if|if
condition|(
name|subtype
operator|.
name|indexOf
argument_list|(
literal|"/"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid media type string: "
operator|+
name|mType
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|paramsStart
operator|!=
operator|-
literal|1
condition|)
block|{
name|parameters
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|String
name|paramString
init|=
name|mType
operator|.
name|substring
argument_list|(
name|paramsStart
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|paramString
operator|.
name|contains
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|Matcher
name|m
init|=
name|COMPLEX_PARAMETERS
operator|.
name|matcher
argument_list|(
name|paramString
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
name|addParameter
argument_list|(
name|parameters
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|paramString
argument_list|,
literal|";"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|addParameter
argument_list|(
name|parameters
argument_list|,
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|new
name|MediaType
argument_list|(
name|type
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|subtype
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|parameters
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|addParameter
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
name|token
parameter_list|)
block|{
name|int
name|equalSign
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
name|equalSign
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Wrong media type parameter, separator is missing"
argument_list|)
throw|;
block|}
name|parameters
operator|.
name|put
argument_list|(
name|token
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|equalSign
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|token
operator|.
name|substring
argument_list|(
name|equalSign
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|(
name|MediaType
name|type
parameter_list|)
block|{
return|return
name|typeToString
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|typeToString
parameter_list|(
name|MediaType
name|type
parameter_list|)
block|{
return|return
name|typeToString
argument_list|(
name|type
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|// Max number of parameters that may be ignored is 3, at least as known
comment|// to the implementation
specifier|public
specifier|static
name|String
name|typeToString
parameter_list|(
name|MediaType
name|type
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|ignoreParams
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"MediaType parameter is null"
argument_list|)
throw|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|type
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|type
operator|.
name|getSubtype
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|type
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|iter
init|=
name|params
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|ignoreParams
operator|!=
literal|null
operator|&&
name|ignoreParams
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|MediaType
name|handleMediaTypeWithoutSubtype
parameter_list|(
name|String
name|mType
parameter_list|)
block|{
if|if
condition|(
name|mType
operator|.
name|startsWith
argument_list|(
name|MediaType
operator|.
name|MEDIA_TYPE_WILDCARD
argument_list|)
condition|)
block|{
name|String
name|mTypeNext
init|=
name|mType
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|?
literal|""
else|:
name|mType
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|boolean
name|mTypeNextEmpty
init|=
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|mTypeNext
argument_list|)
decl_stmt|;
if|if
condition|(
name|mTypeNextEmpty
operator|||
name|mTypeNext
operator|.
name|startsWith
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|mTypeNextEmpty
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameters
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|mType
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|,
literal|";"
argument_list|)
decl_stmt|;
while|while
condition|(
name|st
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|addParameter
argument_list|(
name|parameters
argument_list|,
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MediaType
argument_list|(
name|MediaType
operator|.
name|MEDIA_TYPE_WILDCARD
argument_list|,
name|MediaType
operator|.
name|MEDIA_TYPE_WILDCARD
argument_list|,
name|parameters
argument_list|)
return|;
block|}
return|return
name|MediaType
operator|.
name|WILDCARD_TYPE
return|;
block|}
block|}
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|STRICT_MEDIA_TYPE_CHECK
argument_list|)
argument_list|)
condition|)
block|{
name|MediaType
name|mt
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mType
operator|.
name|equals
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|mt
operator|=
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mType
operator|.
name|equals
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
operator|.
name|getSubtype
argument_list|()
argument_list|)
condition|)
block|{
name|mt
operator|=
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
expr_stmt|;
block|}
else|else
block|{
name|mt
operator|=
name|MediaType
operator|.
name|WILDCARD_TYPE
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Converting a malformed media type '"
operator|+
name|mType
operator|+
literal|"' to '"
operator|+
name|typeToString
argument_list|(
name|mt
argument_list|)
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return
name|mt
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Media type separator is missing"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

