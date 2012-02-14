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
name|utils
package|;
end_package

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
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLEncoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|PathSegment
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|UrlUtils
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
name|HttpHeadersImpl
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
name|PathSegmentImpl
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
name|model
operator|.
name|ParameterType
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|transport
operator|.
name|Destination
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|HttpUtils
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|HttpUtils
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|HttpUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ANY_IP_ADDRESS
init|=
literal|"0.0.0.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ANY_IP_ADDRESS_START
init|=
literal|"://0.0.0.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_HTTP_PORT
init|=
literal|80
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|ENCODE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"%[0-9a-fA-F][0-9a-fA-F]"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CHARSET_PARAMETER
init|=
literal|"charset"
decl_stmt|;
comment|// there are more of such characters, ex, '*' but '*' is not affected by UrlEncode
specifier|private
specifier|static
specifier|final
name|String
name|PATH_RESERVED_CHARACTERS
init|=
literal|"=@/:!$&\'(),;~"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|QUERY_RESERVED_CHARACTERS
init|=
literal|"?/"
decl_stmt|;
specifier|private
name|HttpUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|urlDecode
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|enc
parameter_list|)
block|{
return|return
name|UrlUtils
operator|.
name|urlDecode
argument_list|(
name|value
argument_list|,
name|enc
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|urlDecode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|UrlUtils
operator|.
name|urlDecode
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|pathDecode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|UrlUtils
operator|.
name|pathDecode
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|String
name|componentEncode
parameter_list|(
name|String
name|reservedChars
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|bufferToEncode
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|value
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|currentChar
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
name|reservedChars
operator|.
name|indexOf
argument_list|(
name|currentChar
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|bufferToEncode
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|urlEncode
argument_list|(
name|bufferToEncode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|bufferToEncode
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|currentChar
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bufferToEncode
operator|.
name|append
argument_list|(
name|currentChar
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bufferToEncode
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|urlEncode
argument_list|(
name|bufferToEncode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|queryEncode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|componentEncode
argument_list|(
name|QUERY_RESERVED_CHARACTERS
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|urlEncode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
name|value
operator|=
name|URLEncoder
operator|.
name|encode
argument_list|(
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
comment|// unlikely to happen
block|}
return|return
name|value
return|;
block|}
specifier|public
specifier|static
name|String
name|pathEncode
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|String
name|result
init|=
name|componentEncode
argument_list|(
name|PATH_RESERVED_CHARACTERS
argument_list|,
name|value
argument_list|)
decl_stmt|;
comment|// URLEncoder will encode '+' to %2B but will turn ' ' into '+'
comment|// We need to retain '+' and encode ' ' as %20
if|if
condition|(
name|result
operator|.
name|indexOf
argument_list|(
literal|'+'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|"+"
argument_list|,
literal|"%20"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|indexOf
argument_list|(
literal|"%2B"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|"%2B"
argument_list|,
literal|"+"
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isPartiallyEncoded
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|ENCODE_PATTERN
operator|.
name|matcher
argument_list|(
name|value
argument_list|)
operator|.
name|find
argument_list|()
return|;
block|}
comment|/**      * Encodes partially encoded string. Encode all values but those matching pattern       * "percent char followed by two hexadecimal digits".      *       * @param encoded fully or partially encoded string.      * @return fully encoded string      */
specifier|public
specifier|static
name|String
name|encodePartiallyEncoded
parameter_list|(
name|String
name|encoded
parameter_list|,
name|boolean
name|query
parameter_list|)
block|{
if|if
condition|(
name|encoded
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|encoded
return|;
block|}
name|Matcher
name|m
init|=
name|ENCODE_PATTERN
operator|.
name|matcher
argument_list|(
name|encoded
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
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
name|before
init|=
name|encoded
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|m
operator|.
name|start
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|query
condition|?
name|HttpUtils
operator|.
name|queryEncode
argument_list|(
name|before
argument_list|)
else|:
name|HttpUtils
operator|.
name|pathEncode
argument_list|(
name|before
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|m
operator|.
name|group
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|=
name|m
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
name|String
name|tail
init|=
name|encoded
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|encoded
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|query
condition|?
name|HttpUtils
operator|.
name|queryEncode
argument_list|(
name|tail
argument_list|)
else|:
name|HttpUtils
operator|.
name|pathEncode
argument_list|(
name|tail
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|SimpleDateFormat
name|getHttpDateFormat
parameter_list|()
block|{
name|SimpleDateFormat
name|dateFormat
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"EEE, dd MMM yyyy HH:mm:ss zzz"
argument_list|,
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|TimeZone
name|tZone
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
decl_stmt|;
name|dateFormat
operator|.
name|setTimeZone
argument_list|(
name|tZone
argument_list|)
expr_stmt|;
return|return
name|dateFormat
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isDateRelatedHeader
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
return|return
name|HttpHeaders
operator|.
name|DATE
operator|.
name|equalsIgnoreCase
argument_list|(
name|headerName
argument_list|)
operator|||
name|HttpHeaders
operator|.
name|IF_MODIFIED_SINCE
operator|.
name|equalsIgnoreCase
argument_list|(
name|headerName
argument_list|)
operator|||
name|HttpHeaders
operator|.
name|IF_UNMODIFIED_SINCE
operator|.
name|equalsIgnoreCase
argument_list|(
name|headerName
argument_list|)
operator|||
name|HttpHeaders
operator|.
name|EXPIRES
operator|.
name|equalsIgnoreCase
argument_list|(
name|headerName
argument_list|)
operator|||
name|HttpHeaders
operator|.
name|LAST_MODIFIED
operator|.
name|equalsIgnoreCase
argument_list|(
name|headerName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|URI
name|toAbsoluteUri
parameter_list|(
name|URI
name|u
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|message
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
name|boolean
name|absolute
init|=
name|u
operator|.
name|isAbsolute
argument_list|()
decl_stmt|;
if|if
condition|(
name|request
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|absolute
operator|||
name|u
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
name|ANY_IP_ADDRESS_START
argument_list|)
operator|)
condition|)
block|{
name|String
name|serverAndPort
init|=
name|request
operator|.
name|getServerName
argument_list|()
decl_stmt|;
name|int
name|port
init|=
name|request
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|port
operator|!=
name|DEFAULT_HTTP_PORT
condition|)
block|{
name|serverAndPort
operator|+=
literal|":"
operator|+
name|port
expr_stmt|;
block|}
name|String
name|base
init|=
name|request
operator|.
name|getScheme
argument_list|()
operator|+
literal|"://"
operator|+
name|serverAndPort
decl_stmt|;
if|if
condition|(
operator|!
name|absolute
condition|)
block|{
name|u
operator|=
name|URI
operator|.
name|create
argument_list|(
name|base
operator|+
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|u
operator|=
name|URI
operator|.
name|create
argument_list|(
name|u
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
name|ANY_IP_ADDRESS
argument_list|,
name|serverAndPort
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|u
return|;
block|}
specifier|public
specifier|static
name|String
name|getPathToMatch
parameter_list|(
name|Message
name|m
parameter_list|,
name|boolean
name|addSlash
parameter_list|)
block|{
name|String
name|requestAddress
init|=
name|getProtocolHeader
argument_list|(
name|m
argument_list|,
name|Message
operator|.
name|REQUEST_URI
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|String
name|baseAddress
init|=
name|getBaseAddress
argument_list|(
name|m
argument_list|)
decl_stmt|;
return|return
name|getPathToMatch
argument_list|(
name|requestAddress
argument_list|,
name|baseAddress
argument_list|,
name|addSlash
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getProtocolHeader
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|value
operator|==
literal|null
condition|?
name|defaultValue
else|:
name|value
return|;
block|}
specifier|public
specifier|static
name|String
name|getBaseAddress
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|String
name|endpointAddress
init|=
name|getEndpointAddress
argument_list|(
name|m
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|path
init|=
operator|new
name|URI
argument_list|(
name|endpointAddress
argument_list|)
operator|.
name|getRawPath
argument_list|()
decl_stmt|;
return|return
name|path
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|?
literal|"/"
else|:
name|path
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|ex
parameter_list|)
block|{
return|return
name|endpointAddress
operator|==
literal|null
condition|?
literal|"/"
else|:
name|endpointAddress
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getEndpointAddress
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|String
name|address
init|=
literal|null
decl_stmt|;
name|Destination
name|d
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|d
operator|instanceof
name|AbstractHTTPDestination
condition|)
block|{
name|EndpointInfo
name|ei
init|=
operator|(
operator|(
name|AbstractHTTPDestination
operator|)
name|d
operator|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|m
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
name|Object
name|property
init|=
name|request
operator|!=
literal|null
condition|?
name|request
operator|.
name|getAttribute
argument_list|(
literal|"org.apache.cxf.transport.endpoint.address"
argument_list|)
else|:
literal|null
decl_stmt|;
name|address
operator|=
name|property
operator|!=
literal|null
condition|?
name|property
operator|.
name|toString
argument_list|()
else|:
name|ei
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|address
operator|=
name|m
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|)
condition|?
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|)
else|:
name|d
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|address
operator|=
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
operator|&&
name|address
operator|.
name|endsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|address
operator|=
name|address
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|address
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|address
return|;
block|}
specifier|public
specifier|static
name|void
name|updatePath
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|String
name|baseAddress
init|=
name|getBaseAddress
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|boolean
name|pathSlash
init|=
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|boolean
name|baseSlash
init|=
name|baseAddress
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pathSlash
operator|&&
name|baseSlash
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|pathSlash
operator|&&
operator|!
name|baseSlash
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|,
name|baseAddress
operator|+
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getPathToMatch
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|address
parameter_list|,
name|boolean
name|addSlash
parameter_list|)
block|{
name|int
name|ind
init|=
name|path
operator|.
name|indexOf
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|==
operator|-
literal|1
operator|&&
name|address
operator|.
name|equals
argument_list|(
name|path
operator|+
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|+=
literal|"/"
expr_stmt|;
name|ind
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|ind
operator|==
literal|0
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|ind
operator|+
name|address
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|addSlash
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
specifier|public
specifier|static
name|String
name|getOriginalAddress
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Destination
name|d
init|=
name|m
operator|.
name|getDestination
argument_list|()
decl_stmt|;
return|return
name|d
operator|==
literal|null
condition|?
literal|"/"
else|:
name|d
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|fromPathSegment
parameter_list|(
name|PathSegment
name|ps
parameter_list|)
block|{
if|if
condition|(
name|PathSegmentImpl
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|ps
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
operator|(
name|PathSegmentImpl
operator|)
name|ps
operator|)
operator|.
name|getOriginalPath
argument_list|()
return|;
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
name|ps
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
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
name|ps
operator|.
name|getMatrixParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
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
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
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
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Response
operator|.
name|Status
name|getParameterFailureStatus
parameter_list|(
name|ParameterType
name|pType
parameter_list|)
block|{
if|if
condition|(
name|pType
operator|==
name|ParameterType
operator|.
name|MATRIX
operator|||
name|pType
operator|==
name|ParameterType
operator|.
name|PATH
operator|||
name|pType
operator|==
name|ParameterType
operator|.
name|QUERY
condition|)
block|{
return|return
name|Response
operator|.
name|Status
operator|.
name|NOT_FOUND
return|;
block|}
return|return
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
return|;
block|}
specifier|public
specifier|static
name|String
name|getSetEncoding
parameter_list|(
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|defaultEncoding
parameter_list|)
block|{
name|String
name|enc
init|=
name|mt
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|CHARSET_PARAMETER
argument_list|)
decl_stmt|;
if|if
condition|(
name|enc
operator|==
literal|null
condition|)
block|{
return|return
name|defaultEncoding
return|;
block|}
try|try
block|{
literal|"0"
operator|.
name|getBytes
argument_list|(
name|enc
argument_list|)
expr_stmt|;
return|return
name|enc
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
name|String
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"UNSUPPORTED_ENCODING"
argument_list|,
name|BUNDLE
argument_list|,
name|enc
argument_list|,
name|defaultEncoding
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|JAXRSUtils
operator|.
name|removeMediaTypeParameter
argument_list|(
name|mt
argument_list|,
name|CHARSET_PARAMETER
argument_list|)
operator|+
literal|';'
operator|+
name|CHARSET_PARAMETER
operator|+
literal|"="
operator|+
operator|(
name|defaultEncoding
operator|==
literal|null
condition|?
literal|"UTF-8"
else|:
name|defaultEncoding
operator|)
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultEncoding
return|;
block|}
specifier|public
specifier|static
name|String
name|getEncoding
parameter_list|(
name|MediaType
name|mt
parameter_list|,
name|String
name|defaultEncoding
parameter_list|)
block|{
name|String
name|charset
init|=
name|mt
operator|==
literal|null
condition|?
literal|"UTF-8"
else|:
name|mt
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"charset"
argument_list|)
decl_stmt|;
return|return
name|charset
operator|==
literal|null
condition|?
literal|"UTF-8"
else|:
name|charset
return|;
block|}
block|}
end_class

end_unit

