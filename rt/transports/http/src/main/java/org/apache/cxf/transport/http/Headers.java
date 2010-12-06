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
package|;
end_package

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
name|net
operator|.
name|HttpURLConnection
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
name|Arrays
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
name|logging
operator|.
name|Level
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|helpers
operator|.
name|HttpHeaderHelper
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPServerPolicy
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
name|version
operator|.
name|Version
import|;
end_import

begin_class
specifier|public
class|class
name|Headers
block|{
comment|/**      *  This constant is the Message(Map) key for the HttpURLConnection that      *  is used to get the response.      */
specifier|public
specifier|static
specifier|final
name|String
name|KEY_HTTP_CONNECTION
init|=
literal|"http.connection"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_HEADERS_CONTENT_TYPE
init|=
name|Message
operator|.
name|CONTENT_TYPE
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HTTP_HEADERS_SETCOOKIE
init|=
literal|"Set-Cookie"
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
name|Headers
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
specifier|private
specifier|final
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
name|Headers
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
name|getSetProtocolHeaders
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Write cookie header from given session cookies      *       * @param sessionCookies      */
specifier|public
name|void
name|writeSessionCookies
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|sessionCookies
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|cookies
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|HttpHeaderHelper
operator|.
name|COOKIE
operator|.
name|equalsIgnoreCase
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|cookies
operator|=
name|headers
operator|.
name|remove
argument_list|(
name|s
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|cookies
operator|==
literal|null
condition|)
block|{
name|cookies
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cookies
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|cookies
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|HttpHeaderHelper
operator|.
name|COOKIE
argument_list|,
name|cookies
argument_list|)
expr_stmt|;
for|for
control|(
name|Cookie
name|c
range|:
name|sessionCookies
operator|.
name|values
argument_list|()
control|)
block|{
name|cookies
operator|.
name|add
argument_list|(
name|c
operator|.
name|requestCookieHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This call places HTTP Header strings into the headers that are relevant      * to the ClientPolicy that is set on this conduit by configuration.      *       * REVISIT: A cookie is set statically from configuration?       */
name|void
name|setFromClientPolicy
parameter_list|(
name|HTTPClientPolicy
name|policy
parameter_list|)
block|{
if|if
condition|(
name|policy
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetCacheControl
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Cache-Control"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getCacheControl
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetHost
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Host"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getHost
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetConnection
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Connection"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getConnection
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetAccept
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Accept"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getAccept
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|headers
operator|.
name|containsKey
argument_list|(
literal|"Accept"
argument_list|)
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Accept"
argument_list|,
name|createMutableList
argument_list|(
literal|"*/*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetAcceptEncoding
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Accept-Encoding"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getAcceptEncoding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetAcceptLanguage
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Accept-Language"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getAcceptLanguage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetContentType
argument_list|()
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|policy
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetCookie
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Cookie"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getCookie
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetBrowserType
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"BrowserType"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getBrowserType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetReferer
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Referer"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getReferer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|setFromServerPolicy
parameter_list|(
name|HTTPServerPolicy
name|policy
parameter_list|)
block|{
if|if
condition|(
name|policy
operator|.
name|isSetCacheControl
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Cache-Control"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getCacheControl
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetContentLocation
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Location"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getContentLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetContentEncoding
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Encoding"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetContentType
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|HttpHeaderHelper
operator|.
name|CONTENT_TYPE
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getContentType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetServerType
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Server"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getServerType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|.
name|isSetHonorKeepAlive
argument_list|()
operator|&&
operator|!
name|policy
operator|.
name|isHonorKeepAlive
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Connection"
argument_list|,
name|createMutableList
argument_list|(
literal|"close"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|policy
operator|.
name|isSetKeepAliveParameters
argument_list|()
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Keep-Alive"
argument_list|,
name|createMutableList
argument_list|(
name|policy
operator|.
name|getKeepAliveParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * TODO - hook up these policies<xs:attribute name="SuppressClientSendErrors" type="xs:boolean" use="optional" default="false"><xs:attribute name="SuppressClientReceiveErrors" type="xs:boolean" use="optional" default="false">     */
block|}
specifier|public
name|void
name|removeAuthorizationHeaders
parameter_list|()
block|{
name|headers
operator|.
name|remove
argument_list|(
literal|"Authorization"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|remove
argument_list|(
literal|"Proxy-Authorization"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthorization
parameter_list|(
name|String
name|authorization
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Authorization"
argument_list|,
name|createMutableList
argument_list|(
name|authorization
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProxyAuthorization
parameter_list|(
name|String
name|authorization
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"Proxy-Authorization"
argument_list|,
name|createMutableList
argument_list|(
name|authorization
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * While extracting the Message.PROTOCOL_HEADERS property from the Message,      * this call ensures that the Message.PROTOCOL_HEADERS property is      * set on the Message. If it is not set, an empty map is placed there, and      * then returned.      *       * @param message The outbound message      * @return The PROTOCOL_HEADERS map      */
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getSetProtocolHeaders
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
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
name|headers
init|=
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
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|headers
condition|)
block|{
name|headers
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|headers
operator|instanceof
name|HashMap
condition|)
block|{
name|headers
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
return|return
name|headers
return|;
block|}
specifier|public
name|void
name|readFromConnection
parameter_list|(
name|HttpURLConnection
name|connection
parameter_list|)
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
name|origHeaders
init|=
name|connection
operator|.
name|getHeaderFields
argument_list|()
decl_stmt|;
name|headers
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|connection
operator|.
name|getHeaderFields
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|HttpHeaderHelper
operator|.
name|getHeaderKey
argument_list|(
name|key
argument_list|)
argument_list|,
name|origHeaders
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|createMutableList
parameter_list|(
name|String
name|val
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|val
block|}
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This procedure logs the PROTOCOL_HEADERS from the       * Message at the specified logging level.      *       * @param level   The Logging Level.      * @param headers The Message protocol headers.      */
name|void
name|logProtocolHeaders
parameter_list|(
name|Level
name|level
parameter_list|)
block|{
for|for
control|(
name|String
name|header
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|headerList
init|=
name|headers
operator|.
name|get
argument_list|(
name|header
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|headerList
control|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|level
argument_list|,
name|header
operator|+
literal|": "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Put the headers from Message.PROTOCOL_HEADERS headers into the URL      * connection.      * Note, this does not mean they immediately get written to the output      * stream or the wire. They just just get set on the HTTP request.      *       * @param message The outbound message.      * @throws IOException      */
specifier|public
name|void
name|setURLRequestHeaders
parameter_list|(
name|String
name|conduitName
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpURLConnection
name|connection
init|=
operator|(
name|HttpURLConnection
operator|)
name|message
operator|.
name|get
argument_list|(
name|KEY_HTTP_CONNECTION
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|String
name|enc
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ct
condition|)
block|{
if|if
condition|(
name|enc
operator|!=
literal|null
operator|&&
name|ct
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
operator|==
operator|-
literal|1
operator|&&
operator|!
name|ct
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"multipart/related"
argument_list|)
condition|)
block|{
name|ct
operator|=
name|ct
operator|+
literal|"; charset="
operator|+
name|enc
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|enc
operator|!=
literal|null
condition|)
block|{
name|ct
operator|=
literal|"text/xml; charset="
operator|+
name|enc
expr_stmt|;
block|}
else|else
block|{
name|ct
operator|=
literal|"text/xml"
expr_stmt|;
block|}
name|connection
operator|.
name|setRequestProperty
argument_list|(
name|HttpHeaderHelper
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Sending "
operator|+
name|connection
operator|.
name|getRequestMethod
argument_list|()
operator|+
literal|" Message with Headers to "
operator|+
name|connection
operator|.
name|getURL
argument_list|()
operator|+
literal|" Conduit :"
operator|+
name|conduitName
operator|+
literal|"\nContent-Type: "
operator|+
name|ct
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
operator|new
name|Headers
argument_list|(
name|message
argument_list|)
operator|.
name|logProtocolHeaders
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
expr_stmt|;
block|}
name|transferProtocolHeadersToURLConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
comment|/**      * This procedure sets the URLConnection request properties      * from the PROTOCOL_HEADERS in the message.      */
specifier|private
name|void
name|transferProtocolHeadersToURLConnection
parameter_list|(
name|URLConnection
name|connection
parameter_list|)
block|{
for|for
control|(
name|String
name|header
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|headerList
init|=
name|headers
operator|.
name|get
argument_list|(
name|header
argument_list|)
decl_stmt|;
if|if
condition|(
name|HttpHeaderHelper
operator|.
name|CONTENT_TYPE
operator|.
name|equalsIgnoreCase
argument_list|(
name|header
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|HttpHeaderHelper
operator|.
name|COOKIE
operator|.
name|equalsIgnoreCase
argument_list|(
name|header
argument_list|)
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|headerList
control|)
block|{
name|connection
operator|.
name|addRequestProperty
argument_list|(
name|HttpHeaderHelper
operator|.
name|COOKIE
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|StringBuilder
name|b
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
name|headerList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|headerList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|headerList
operator|.
name|size
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
name|connection
operator|.
name|setRequestProperty
argument_list|(
name|header
argument_list|,
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|connection
operator|.
name|getRequestProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"User-Agent"
argument_list|)
condition|)
block|{
name|connection
operator|.
name|addRequestProperty
argument_list|(
literal|"User-Agent"
argument_list|,
name|Version
operator|.
name|getCompleteVersionString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Copy the request headers into the message.      *       * @param message the current message      * @param headers the current set of headers      */
specifier|protected
name|void
name|copyFromRequest
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
comment|//TODO how to deal with the fields
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|req
operator|.
name|getHeaderNames
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|fname
init|=
operator|(
name|String
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|mappedName
init|=
name|HttpHeaderHelper
operator|.
name|getHeaderKey
argument_list|(
name|fname
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
decl_stmt|;
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|mappedName
argument_list|)
condition|)
block|{
name|values
operator|=
name|headers
operator|.
name|get
argument_list|(
name|mappedName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|values
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|mappedName
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e2
init|=
name|req
operator|.
name|getHeaders
argument_list|(
name|fname
argument_list|)
init|;
name|e2
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|val
init|=
operator|(
name|String
operator|)
name|e2
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
name|headers
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|req
operator|.
name|getContentType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Request Headers: "
operator|+
name|headers
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getContentTypeFromMessage
parameter_list|()
block|{
specifier|final
name|String
name|ct
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
specifier|final
name|String
name|enc
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ct
operator|&&
literal|null
operator|!=
name|enc
operator|&&
name|ct
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
operator|==
operator|-
literal|1
operator|&&
operator|!
name|ct
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"multipart/related"
argument_list|)
condition|)
block|{
return|return
name|ct
operator|+
literal|"; charset="
operator|+
name|enc
return|;
block|}
else|else
block|{
return|return
name|ct
return|;
block|}
block|}
comment|/**      * Copy the response headers into the response.      *       * @param message the current message      * @param headers the current set of headers      */
specifier|protected
name|void
name|copyToResponse
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|String
name|contentType
init|=
name|getContentTypeFromMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|headers
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
condition|)
block|{
name|response
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|headers
operator|.
name|keySet
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
name|String
name|header
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|headerList
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|headers
operator|.
name|get
argument_list|(
name|header
argument_list|)
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
name|HTTP_HEADERS_SETCOOKIE
operator|.
name|equals
argument_list|(
name|header
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headerList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|response
operator|.
name|addHeader
argument_list|(
name|header
argument_list|,
name|headerList
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headerList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|headerList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|headerList
operator|.
name|size
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
block|}
name|response
operator|.
name|addHeader
argument_list|(
name|header
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|removeContentType
parameter_list|()
block|{
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|PROTOCOL_HEADERS_CONTENT_TYPE
argument_list|)
condition|)
block|{
name|headers
operator|.
name|remove
argument_list|(
name|PROTOCOL_HEADERS_CONTENT_TYPE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getAuthorization
parameter_list|()
block|{
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
literal|"Authorization"
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|authorizationLines
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
return|return
name|authorizationLines
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

