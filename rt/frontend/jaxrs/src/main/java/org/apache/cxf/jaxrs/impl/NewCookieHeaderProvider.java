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
name|Date
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
name|HttpUtils
import|;
end_import

begin_class
specifier|public
class|class
name|NewCookieHeaderProvider
implements|implements
name|HeaderDelegate
argument_list|<
name|NewCookie
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"Version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PATH
init|=
literal|"Path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DOMAIN
init|=
literal|"Domain"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MAX_AGE
init|=
literal|"Max-Age"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMMENT
init|=
literal|"Comment"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SECURE
init|=
literal|"Secure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXPIRES
init|=
literal|"Expires"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_ONLY
init|=
literal|"HttpOnly"
decl_stmt|;
comment|/** from RFC 2068, token special case characters */
specifier|private
specifier|static
specifier|final
name|String
name|TSPECIALS_PATH
init|=
literal|"\"()<>@,;:\\[]?={} \t"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TSPECIALS_ALL
init|=
name|TSPECIALS_PATH
operator|+
literal|"/"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DOUBLE_QUOTE
init|=
literal|"\""
decl_stmt|;
specifier|public
name|NewCookie
name|fromString
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
argument_list|(
literal|"SetCookie value can not be null"
argument_list|)
throw|;
block|}
name|String
name|name
init|=
literal|null
decl_stmt|;
name|String
name|value
init|=
literal|null
decl_stmt|;
name|String
name|path
init|=
literal|null
decl_stmt|;
name|String
name|domain
init|=
literal|null
decl_stmt|;
name|String
name|comment
init|=
literal|null
decl_stmt|;
name|int
name|maxAge
init|=
name|NewCookie
operator|.
name|DEFAULT_MAX_AGE
decl_stmt|;
name|boolean
name|isSecure
init|=
literal|false
decl_stmt|;
name|Date
name|expires
init|=
literal|null
decl_stmt|;
name|boolean
name|httpOnly
init|=
literal|false
decl_stmt|;
name|int
name|version
init|=
name|Cookie
operator|.
name|DEFAULT_VERSION
decl_stmt|;
name|String
index|[]
name|tokens
init|=
name|c
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
name|String
name|theToken
init|=
name|token
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
name|sepIndex
init|=
name|theToken
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|)
decl_stmt|;
name|String
name|paramName
init|=
name|sepIndex
operator|!=
operator|-
literal|1
condition|?
name|theToken
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sepIndex
argument_list|)
else|:
name|theToken
decl_stmt|;
name|String
name|paramValue
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sepIndex
operator|==
name|theToken
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
name|paramValue
operator|=
literal|""
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sepIndex
operator|!=
operator|-
literal|1
condition|)
block|{
name|paramValue
operator|=
name|theToken
operator|.
name|substring
argument_list|(
name|sepIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|paramValue
operator|!=
literal|null
condition|)
block|{
name|paramValue
operator|=
name|stripQuotes
argument_list|(
name|paramValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
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
name|paramValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|PATH
argument_list|)
condition|)
block|{
name|path
operator|=
name|paramValue
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|DOMAIN
argument_list|)
condition|)
block|{
name|domain
operator|=
name|paramValue
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|COMMENT
argument_list|)
condition|)
block|{
name|comment
operator|=
name|paramValue
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|SECURE
argument_list|)
condition|)
block|{
name|isSecure
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|EXPIRES
argument_list|)
condition|)
block|{
name|expires
operator|=
name|HttpUtils
operator|.
name|getHttpDate
argument_list|(
name|paramValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|HTTP_ONLY
argument_list|)
condition|)
block|{
name|httpOnly
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramName
operator|.
name|equalsIgnoreCase
argument_list|(
name|VERSION
argument_list|)
condition|)
block|{
name|version
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|paramValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramValue
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|paramName
expr_stmt|;
name|value
operator|=
name|paramValue
expr_stmt|;
block|}
block|}
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Set-Cookie is malformed : "
operator|+
name|c
argument_list|)
throw|;
block|}
return|return
operator|new
name|NewCookie
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|path
argument_list|,
name|domain
argument_list|,
name|version
argument_list|,
name|comment
argument_list|,
name|maxAge
argument_list|,
name|expires
argument_list|,
name|isSecure
argument_list|,
name|httpOnly
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|NewCookie
name|value
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|value
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null cookie input"
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
name|value
operator|.
name|getName
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
name|maybeQuoteAll
argument_list|(
name|value
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|.
name|getComment
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|COMMENT
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|maybeQuoteAll
argument_list|(
name|value
operator|.
name|getComment
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|.
name|getDomain
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|DOMAIN
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|maybeQuoteAll
argument_list|(
name|value
operator|.
name|getDomain
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
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
literal|';'
argument_list|)
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
name|value
operator|.
name|getMaxAge
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|PATH
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|maybeQuotePath
argument_list|(
name|value
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|.
name|getExpiry
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|EXPIRES
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|HttpUtils
operator|.
name|toHttpDate
argument_list|(
name|value
operator|.
name|getExpiry
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|.
name|isSecure
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|SECURE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|.
name|isHttpOnly
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
operator|.
name|append
argument_list|(
name|HTTP_ONLY
argument_list|)
expr_stmt|;
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
name|VERSION
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|value
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Append the input value string to the given buffer, wrapping it with      * quotes if need be.      *      * @param value      * @return String      */
specifier|static
name|String
name|maybeQuote
parameter_list|(
name|String
name|tSpecials
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|needsQuote
argument_list|(
name|tSpecials
argument_list|,
name|value
argument_list|)
condition|)
block|{
name|StringBuilder
name|buff
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buff
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|buff
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|buff
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
return|return
name|buff
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|value
operator|==
literal|null
condition|?
literal|""
else|:
name|value
return|;
block|}
specifier|static
name|String
name|maybeQuoteAll
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|maybeQuote
argument_list|(
name|TSPECIALS_ALL
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|static
name|String
name|maybeQuotePath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|maybeQuote
argument_list|(
name|TSPECIALS_PATH
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Return true if the string contains special characters that need to be      * quoted.      *      * @param value      * @return boolean      */
specifier|static
name|boolean
name|needsQuote
parameter_list|(
name|String
name|tSpecials
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|value
condition|)
block|{
return|return
literal|true
return|;
block|}
name|int
name|len
init|=
name|value
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|==
name|len
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
literal|'"'
operator|==
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|&&
literal|'"'
operator|==
name|value
operator|.
name|charAt
argument_list|(
name|len
operator|-
literal|1
argument_list|)
condition|)
block|{
comment|// already wrapped with quotes
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0x20
operator|||
name|c
operator|>=
literal|0x7f
operator|||
name|tSpecials
operator|.
name|indexOf
argument_list|(
name|c
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|static
name|String
name|stripQuotes
parameter_list|(
name|String
name|paramValue
parameter_list|)
block|{
if|if
condition|(
name|paramValue
operator|.
name|startsWith
argument_list|(
name|DOUBLE_QUOTE
argument_list|)
operator|&&
name|paramValue
operator|.
name|endsWith
argument_list|(
name|DOUBLE_QUOTE
argument_list|)
operator|&&
name|paramValue
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|paramValue
operator|=
name|paramValue
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|paramValue
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|paramValue
return|;
block|}
block|}
end_class

end_unit

