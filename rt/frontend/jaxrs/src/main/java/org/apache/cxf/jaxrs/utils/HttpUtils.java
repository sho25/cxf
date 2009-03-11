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
name|MalformedURLException
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
name|URL
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
name|TimeZone
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
name|servlet
operator|.
name|ServletDestination
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
name|String
name|LOCAL_IP_ADDRESS
init|=
literal|"127.0.0.1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOCAL_HOST
init|=
literal|"localhost"
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
name|urlEncode
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// URLEncoder will encode '+' to %2B but will turn ' ' into '+'
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
return|return
name|result
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
if|if
condition|(
operator|!
name|u
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|HttpServletRequest
name|httpRequest
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
if|if
condition|(
name|httpRequest
operator|!=
literal|null
condition|)
block|{
name|String
name|scheme
init|=
name|httpRequest
operator|.
name|isSecure
argument_list|()
condition|?
literal|"https"
else|:
literal|"http"
decl_stmt|;
name|String
name|host
init|=
name|httpRequest
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOCAL_IP_ADDRESS
operator|.
name|equals
argument_list|(
name|host
argument_list|)
condition|)
block|{
name|host
operator|=
name|LOCAL_HOST
expr_stmt|;
block|}
name|int
name|port
init|=
name|httpRequest
operator|.
name|getLocalPort
argument_list|()
decl_stmt|;
return|return
name|URI
operator|.
name|create
argument_list|(
name|scheme
operator|+
literal|"://"
operator|+
name|host
operator|+
literal|':'
operator|+
name|port
operator|+
name|u
operator|.
name|toString
argument_list|()
argument_list|)
return|;
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
operator|(
name|String
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URI
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
name|getBaseAddress
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
try|try
block|{
name|String
name|endpointAddress
init|=
name|getEndpointAddress
argument_list|(
name|m
argument_list|)
decl_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
return|return
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
name|ServletDestination
condition|)
block|{
name|address
operator|=
operator|(
operator|(
name|ServletDestination
operator|)
name|d
operator|)
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|address
operator|=
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
block|}
end_class

end_unit

