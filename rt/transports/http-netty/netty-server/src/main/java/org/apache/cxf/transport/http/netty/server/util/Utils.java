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
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|net
operator|.
name|FileNameMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
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
name|Enumeration
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
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|cookie
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|cookie
operator|.
name|ServerCookieDecoder
import|;
end_import

begin_import
import|import static
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpHeaders
operator|.
name|Names
operator|.
name|COOKIE
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Utils
block|{
specifier|private
name|Utils
parameter_list|()
block|{
comment|// Utils class
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Enumeration
argument_list|<
name|T
argument_list|>
name|emptyEnumeration
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|Collections
operator|.
expr|<
name|T
operator|>
name|emptySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Enumeration
argument_list|<
name|T
argument_list|>
name|enumeration
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|==
literal|null
condition|)
block|{
return|return
name|emptyEnumeration
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|collection
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Enumeration
argument_list|<
name|T
argument_list|>
name|enumerationFromKeys
parameter_list|(
name|Map
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
name|emptyEnumeration
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Enumeration
argument_list|<
name|T
argument_list|>
name|enumerationFromValues
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|T
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
name|emptyEnumeration
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Parse the character encoding from the specified content type header. If      * the content type is null, or there is no explicit character encoding,      *<code>null</code> is returned.      *      * @param contentType a content type header      */
specifier|public
specifier|static
name|String
name|getCharsetFromContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|start
init|=
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
decl_stmt|;
if|if
condition|(
name|start
operator|<
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|encoding
init|=
name|contentType
operator|.
name|substring
argument_list|(
name|start
operator|+
literal|8
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|encoding
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
if|if
condition|(
name|end
operator|>=
literal|0
condition|)
block|{
name|encoding
operator|=
name|encoding
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|end
argument_list|)
expr_stmt|;
block|}
name|encoding
operator|=
name|encoding
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|encoding
operator|.
name|length
argument_list|()
operator|>
literal|2
operator|)
operator|&&
operator|(
name|encoding
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|)
operator|&&
operator|(
name|encoding
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
operator|)
condition|)
block|{
name|encoding
operator|=
name|encoding
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|encoding
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|encoding
operator|.
name|trim
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Collection
argument_list|<
name|Cookie
argument_list|>
name|getCookies
parameter_list|(
name|String
name|name
parameter_list|,
name|HttpRequest
name|request
parameter_list|)
block|{
name|String
name|cookieString
init|=
name|request
operator|.
name|headers
argument_list|()
operator|.
name|get
argument_list|(
name|COOKIE
argument_list|)
decl_stmt|;
if|if
condition|(
name|cookieString
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Cookie
argument_list|>
name|foundCookie
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Cookie
argument_list|>
name|cookies
init|=
name|ServerCookieDecoder
operator|.
name|STRICT
operator|.
name|decode
argument_list|(
name|cookieString
argument_list|)
decl_stmt|;
for|for
control|(
name|Cookie
name|cookie
range|:
name|cookies
control|)
block|{
if|if
condition|(
name|cookie
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|foundCookie
operator|.
name|add
argument_list|(
name|cookie
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|foundCookie
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|getMimeType
parameter_list|(
name|String
name|fileUrl
parameter_list|)
block|{
name|FileNameMap
name|fileNameMap
init|=
name|URLConnection
operator|.
name|getFileNameMap
argument_list|()
decl_stmt|;
return|return
name|fileNameMap
operator|.
name|getContentTypeFor
argument_list|(
name|fileUrl
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|sanitizeUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// Decode the path.
try|try
block|{
name|uri
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|uri
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
try|try
block|{
name|uri
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|uri
argument_list|,
name|StandardCharsets
operator|.
name|ISO_8859_1
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|()
throw|;
block|}
block|}
comment|// Convert file separators.
name|uri
operator|=
name|uri
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
expr_stmt|;
comment|// Simplistic dumb security check.
comment|// You will have to do something serious in the production environment.
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
name|File
operator|.
name|separator
operator|+
literal|"."
argument_list|)
operator|||
name|uri
operator|.
name|contains
argument_list|(
literal|"."
operator|+
name|File
operator|.
name|separator
argument_list|)
operator|||
name|uri
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
operator|||
name|uri
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|uri
return|;
block|}
specifier|public
specifier|static
name|Collection
argument_list|<
name|Locale
argument_list|>
name|parseAcceptLanguageHeader
parameter_list|(
name|String
name|acceptLanguageHeader
parameter_list|)
block|{
if|if
condition|(
name|acceptLanguageHeader
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
name|Locale
argument_list|>
name|locales
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|str
range|:
name|acceptLanguageHeader
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|String
index|[]
name|arr
init|=
name|str
operator|.
name|trim
argument_list|()
operator|.
name|replace
argument_list|(
literal|"-"
argument_list|,
literal|"_"
argument_list|)
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
comment|// Parse the locale
name|Locale
name|locale
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|l
init|=
name|arr
index|[
literal|0
index|]
operator|.
name|split
argument_list|(
literal|"_"
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|l
operator|.
name|length
condition|)
block|{
case|case
literal|2
case|:
name|locale
operator|=
operator|new
name|Locale
argument_list|(
name|l
index|[
literal|0
index|]
argument_list|,
name|l
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|locale
operator|=
operator|new
name|Locale
argument_list|(
name|l
index|[
literal|0
index|]
argument_list|,
name|l
index|[
literal|1
index|]
argument_list|,
name|l
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
break|break;
default|default:
name|locale
operator|=
operator|new
name|Locale
argument_list|(
name|l
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
break|break;
block|}
name|locales
operator|.
name|add
argument_list|(
name|locale
argument_list|)
expr_stmt|;
block|}
return|return
name|locales
return|;
block|}
block|}
end_class

end_unit

