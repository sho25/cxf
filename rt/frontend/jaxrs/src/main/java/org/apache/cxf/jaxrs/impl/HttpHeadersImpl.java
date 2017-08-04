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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Cookie
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
name|HttpHeadersImpl
implements|implements
name|HttpHeaders
block|{
specifier|private
specifier|static
specifier|final
name|String
name|HEADER_SPLIT_PROPERTY
init|=
literal|"org.apache.cxf.http.header.split"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COOKIE_SEPARATOR_PROPERTY
init|=
literal|"org.apache.cxf.http.cookie.separator"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COOKIE_SEPARATOR_CRLF
init|=
literal|"crlf"
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
name|DEFAULT_COOKIE_SEPARATOR
init|=
literal|";"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DOLLAR_CHAR
init|=
literal|"$"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COOKIE_VERSION_PARAM
init|=
name|DOLLAR_CHAR
operator|+
literal|"Version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COOKIE_PATH_PARAM
init|=
name|DOLLAR_CHAR
operator|+
literal|"Path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COOKIE_DOMAIN_PARAM
init|=
name|DOLLAR_CHAR
operator|+
literal|"Domain"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPLEX_HEADER_EXPRESSION
init|=
literal|"(([\\w]+=\"[^\"]*\")|([\\w]+=[\\w]+)|([\\w]+))(;(([\\w]+=\"[^\"]*\")|([\\w]+=[\\w]+)|([\\w]+)))?"
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
name|QUOTE
init|=
literal|"\""
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|HEADERS_WITH_POSSIBLE_QUOTES
decl_stmt|;
static|static
block|{
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|)
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|ETAG
argument_list|)
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|IF_MATCH
argument_list|)
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|IF_NONE_MATCH
argument_list|)
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|)
expr_stmt|;
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|add
argument_list|(
name|HttpHeaders
operator|.
name|SET_COOKIE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
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
decl_stmt|;
specifier|public
name|HttpHeadersImpl
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|headers
operator|=
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
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|MediaType
argument_list|>
name|getAcceptableMediaTypes
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|lValues
init|=
name|headers
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|lValues
operator|==
literal|null
operator|||
name|lValues
operator|.
name|isEmpty
argument_list|()
operator|||
name|lValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|WILDCARD_TYPE
argument_list|)
return|;
block|}
name|List
argument_list|<
name|MediaType
argument_list|>
name|mediaTypes
init|=
operator|new
name|LinkedList
argument_list|<
name|MediaType
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|lValues
control|)
block|{
name|mediaTypes
operator|.
name|addAll
argument_list|(
name|JAXRSUtils
operator|.
name|parseMediaTypes
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sortMediaTypesUsingQualityFactor
argument_list|(
name|mediaTypes
argument_list|)
expr_stmt|;
return|return
name|mediaTypes
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|getCookies
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|isEmpty
argument_list|()
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
name|Cookie
argument_list|>
name|cl
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|cs
init|=
name|getHeaderValues
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|,
name|value
argument_list|,
name|getCookieSeparator
argument_list|(
name|value
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|c
range|:
name|cs
control|)
block|{
name|Cookie
name|cookie
init|=
name|Cookie
operator|.
name|valueOf
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|cl
operator|.
name|put
argument_list|(
name|cookie
operator|.
name|getName
argument_list|()
argument_list|,
name|cookie
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cl
return|;
block|}
specifier|private
name|String
name|getCookieSeparator
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|String
name|separator
init|=
name|getCookieSeparatorFromProperty
argument_list|()
decl_stmt|;
if|if
condition|(
name|separator
operator|!=
literal|null
condition|)
block|{
return|return
name|separator
return|;
block|}
if|if
condition|(
name|value
operator|.
name|contains
argument_list|(
name|DOLLAR_CHAR
argument_list|)
operator|&&
operator|(
name|value
operator|.
name|contains
argument_list|(
name|COOKIE_VERSION_PARAM
argument_list|)
operator|||
name|value
operator|.
name|contains
argument_list|(
name|COOKIE_PATH_PARAM
argument_list|)
operator|||
name|value
operator|.
name|contains
argument_list|(
name|COOKIE_DOMAIN_PARAM
argument_list|)
operator|)
condition|)
block|{
return|return
name|DEFAULT_SEPARATOR
return|;
block|}
return|return
name|DEFAULT_COOKIE_SEPARATOR
return|;
block|}
specifier|private
name|String
name|getCookieSeparatorFromProperty
parameter_list|()
block|{
name|Object
name|cookiePropValue
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|COOKIE_SEPARATOR_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|cookiePropValue
operator|!=
literal|null
condition|)
block|{
name|String
name|separator
init|=
name|cookiePropValue
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|COOKIE_SEPARATOR_CRLF
operator|.
name|equals
argument_list|(
name|separator
argument_list|)
condition|)
block|{
return|return
literal|"\r\n"
return|;
block|}
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
return|return
name|separator
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Locale
name|getLanguage
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|)
decl_stmt|;
return|return
name|values
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|HttpUtils
operator|.
name|getLocale
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
return|return
name|values
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|?
literal|null
else|:
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getRequestHeaders
parameter_list|()
block|{
name|boolean
name|splitIndividualValue
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|HEADER_SPLIT_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitIndividualValue
condition|)
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
name|newHeaders
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
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
name|newHeaders
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|getRequestHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|newHeaders
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|headers
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|Locale
argument_list|>
name|getAcceptableLanguages
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ls
init|=
name|getListValues
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"*"
argument_list|)
argument_list|)
return|;
block|}
name|List
argument_list|<
name|Locale
argument_list|>
name|newLs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
name|prefs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|l
range|:
name|ls
control|)
block|{
name|String
index|[]
name|pair
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|l
argument_list|,
literal|";"
argument_list|)
decl_stmt|;
name|Locale
name|locale
init|=
name|HttpUtils
operator|.
name|getLocale
argument_list|(
name|pair
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|newLs
operator|.
name|add
argument_list|(
name|locale
argument_list|)
expr_stmt|;
if|if
condition|(
name|pair
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|String
index|[]
name|pair2
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|pair
index|[
literal|1
index|]
argument_list|,
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|pair2
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|prefs
operator|.
name|put
argument_list|(
name|locale
argument_list|,
name|JAXRSUtils
operator|.
name|getMediaTypeQualityFactor
argument_list|(
name|pair2
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prefs
operator|.
name|put
argument_list|(
name|locale
argument_list|,
literal|1F
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|prefs
operator|.
name|put
argument_list|(
name|locale
argument_list|,
literal|1F
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newLs
operator|.
name|size
argument_list|()
operator|<=
literal|1
condition|)
block|{
return|return
name|newLs
return|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|newLs
argument_list|,
operator|new
name|AcceptLanguageComparator
argument_list|(
name|prefs
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|newLs
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRequestHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|boolean
name|splitIndividualValue
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|HEADER_SPLIT_PROPERTY
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|splitIndividualValue
operator|||
name|values
operator|==
literal|null
operator|||
name|HttpUtils
operator|.
name|isDateRelatedHeader
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|values
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|ls
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|String
name|sep
init|=
name|HttpHeaders
operator|.
name|COOKIE
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|?
name|getCookieSeparator
argument_list|(
name|value
argument_list|)
else|:
name|DEFAULT_SEPARATOR
decl_stmt|;
name|ls
operator|.
name|addAll
argument_list|(
name|getHeaderValues
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|sep
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ls
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getListValues
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|isEmpty
argument_list|()
operator|||
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
if|if
condition|(
name|HttpUtils
operator|.
name|isDateRelatedHeader
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
return|return
name|values
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|actualValues
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|v
range|:
name|values
control|)
block|{
name|actualValues
operator|.
name|addAll
argument_list|(
name|getHeaderValues
argument_list|(
name|headerName
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|actualValues
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getHeaderValues
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|originalValue
parameter_list|)
block|{
return|return
name|getHeaderValues
argument_list|(
name|headerName
argument_list|,
name|originalValue
argument_list|,
name|DEFAULT_SEPARATOR
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getHeaderValues
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|originalValue
parameter_list|,
name|String
name|sep
parameter_list|)
block|{
if|if
condition|(
operator|!
name|originalValue
operator|.
name|contains
argument_list|(
name|QUOTE
argument_list|)
operator|||
name|HEADERS_WITH_POSSIBLE_QUOTES
operator|.
name|contains
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
name|String
index|[]
name|ls
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|originalValue
argument_list|,
name|sep
argument_list|)
decl_stmt|;
if|if
condition|(
name|ls
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|ls
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|newValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|v
range|:
name|ls
control|)
block|{
name|newValues
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
return|return
name|newValues
return|;
block|}
if|if
condition|(
name|originalValue
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|originalValue
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
name|String
name|actualValue
init|=
name|originalValue
operator|.
name|length
argument_list|()
operator|==
literal|2
condition|?
literal|""
else|:
name|originalValue
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|originalValue
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|actualValue
argument_list|)
return|;
block|}
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
name|originalValue
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
return|;
block|}
specifier|private
specifier|static
class|class
name|AcceptLanguageComparator
implements|implements
name|Comparator
argument_list|<
name|Locale
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
name|prefs
decl_stmt|;
name|AcceptLanguageComparator
parameter_list|(
name|Map
argument_list|<
name|Locale
argument_list|,
name|Float
argument_list|>
name|prefs
parameter_list|)
block|{
name|this
operator|.
name|prefs
operator|=
name|prefs
expr_stmt|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|Locale
name|lang1
parameter_list|,
name|Locale
name|lang2
parameter_list|)
block|{
name|float
name|p1
init|=
name|prefs
operator|.
name|get
argument_list|(
name|lang1
argument_list|)
decl_stmt|;
name|float
name|p2
init|=
name|prefs
operator|.
name|get
argument_list|(
name|lang2
argument_list|)
decl_stmt|;
return|return
name|Float
operator|.
name|compare
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
operator|*
operator|-
literal|1
return|;
block|}
block|}
specifier|private
name|void
name|sortMediaTypesUsingQualityFactor
parameter_list|(
name|List
argument_list|<
name|MediaType
argument_list|>
name|types
parameter_list|)
block|{
if|if
condition|(
name|types
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|types
argument_list|,
operator|new
name|Comparator
argument_list|<
name|MediaType
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|MediaType
name|mt1
parameter_list|,
name|MediaType
name|mt2
parameter_list|)
block|{
return|return
name|JAXRSUtils
operator|.
name|compareMediaTypesQualityFactors
argument_list|(
name|mt1
argument_list|,
name|mt2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Date
name|getDate
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|DATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|isEmpty
argument_list|()
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|HttpUtils
operator|.
name|getHttpDate
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getHeaderString
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
return|return
name|HttpUtils
operator|.
name|getHeaderString
argument_list|(
name|headers
operator|.
name|get
argument_list|(
name|headerName
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|int
name|getLength
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LENGTH
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|HttpUtils
operator|.
name|getContentLength
argument_list|(
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

