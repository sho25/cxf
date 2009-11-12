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
name|helpers
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|IllegalCharsetNameException
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
name|UnsupportedCharsetException
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|HttpHeaderHelper
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ACCEPT_ENCODING
init|=
literal|"Accept-Encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_ID
init|=
literal|"Content-ID"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_ENCODING
init|=
literal|"Content-Encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_LENGTH
init|=
literal|"Content-Length"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_TRANSFER_ENCODING
init|=
literal|"Content-Transfer-Encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COOKIE
init|=
literal|"Cookie"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRANSFER_ENCODING
init|=
literal|"Transfer-Encoding"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHUNKED
init|=
literal|"chunked"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION
init|=
literal|"Connection"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLOSE
init|=
literal|"close"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION
init|=
literal|"Authorization"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ISO88591
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-1"
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|internalHeaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|encodings
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Accept-Encoding"
argument_list|,
literal|"accept-encoding"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Content-Encoding"
argument_list|,
literal|"content-encoding"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"content-type"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Content-ID"
argument_list|,
literal|"content-id"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Content-Transfer-Encoding"
argument_list|,
literal|"content-transfer-encoding"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Transfer-Encoding"
argument_list|,
literal|"transfer-encoding"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"Connection"
argument_list|,
literal|"connection"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"authorization"
argument_list|,
literal|"Authorization"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"soapaction"
argument_list|,
literal|"SOAPAction"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"accept"
argument_list|,
literal|"Accept"
argument_list|)
expr_stmt|;
name|internalHeaders
operator|.
name|put
argument_list|(
literal|"content-length"
argument_list|,
literal|"Content-Length"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|HttpHeaderHelper
parameter_list|()
block|{              }
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headerMap
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|headerMap
operator|.
name|get
argument_list|(
name|getHeaderKey
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getHeaderKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|internalHeaders
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|internalHeaders
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|key
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|findCharset
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
name|idx
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
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|charset
init|=
name|contentType
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|8
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|charset
operator|=
name|charset
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|charset
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|charset
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'\"'
condition|)
block|{
name|charset
operator|=
name|charset
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|charset
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|charset
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|mapCharset
parameter_list|(
name|String
name|enc
parameter_list|)
block|{
return|return
name|mapCharset
argument_list|(
name|enc
argument_list|,
name|ISO88591
argument_list|)
return|;
block|}
comment|//helper to map the charsets that various things send in the http Content-Type header
comment|//into something that is actually supported by Java and the Stax parsers and such.
specifier|public
specifier|static
name|String
name|mapCharset
parameter_list|(
name|String
name|enc
parameter_list|,
name|String
name|deflt
parameter_list|)
block|{
if|if
condition|(
name|enc
operator|==
literal|null
condition|)
block|{
return|return
name|deflt
return|;
block|}
comment|//older versions of tomcat don't properly parse ContentType headers with stuff
comment|//after charset="UTF-8"
name|int
name|idx
init|=
name|enc
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|enc
operator|=
name|enc
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
comment|// Charsets can be quoted. But it's quite certain that they can't have escaped quoted or
comment|// anything like that.
name|enc
operator|=
name|enc
operator|.
name|replace
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
name|enc
operator|=
name|enc
operator|.
name|replace
argument_list|(
literal|"'"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|enc
argument_list|)
condition|)
block|{
return|return
name|deflt
return|;
block|}
name|String
name|newenc
init|=
name|encodings
operator|.
name|get
argument_list|(
name|enc
argument_list|)
decl_stmt|;
if|if
condition|(
name|newenc
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|newenc
operator|=
name|Charset
operator|.
name|forName
argument_list|(
name|enc
argument_list|)
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalCharsetNameException
name|icne
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedCharsetException
name|uce
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
name|encodings
operator|.
name|put
argument_list|(
name|enc
argument_list|,
name|newenc
argument_list|)
expr_stmt|;
block|}
return|return
name|newenc
return|;
block|}
block|}
end_class

end_unit

