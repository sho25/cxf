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
name|http_jaxws_spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|javax
operator|.
name|servlet
operator|.
name|AsyncContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|DispatcherType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ReadListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|RequestDispatcher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|Cookie
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
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
name|HttpUpgradeHandler
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
name|Part
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|http
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|spi
operator|.
name|http
operator|.
name|HttpExchange
import|;
end_import

begin_comment
comment|/**  * This class provides a HttpServletRequest instance using information  * coming from the HttpExchange and HttpContext instances provided  * by the underlying container.  * Note: many methods' implementation still TODO.  *  */
end_comment

begin_class
class|class
name|HttpServletRequestAdapter
implements|implements
name|HttpServletRequest
block|{
specifier|private
name|HttpExchange
name|exchange
decl_stmt|;
specifier|private
name|HttpContext
name|context
decl_stmt|;
specifier|private
name|String
name|characterEncoding
decl_stmt|;
specifier|private
name|ServletInputStreamAdapter
name|servletInputStreamAdapter
decl_stmt|;
specifier|private
name|BufferedReader
name|reader
decl_stmt|;
name|HttpServletRequestAdapter
parameter_list|(
name|HttpExchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|exchange
operator|.
name|getHttpContext
argument_list|()
expr_stmt|;
block|}
specifier|public
name|AsyncContext
name|getAsyncContext
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getAttributeNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|exchange
operator|.
name|getAttributeNames
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
return|return
name|characterEncoding
return|;
block|}
specifier|public
name|int
name|getContentLength
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|this
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|)
return|;
block|}
specifier|public
name|DispatcherType
name|getDispatcherType
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|ServletInputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|servletInputStreamAdapter
operator|==
literal|null
condition|)
block|{
name|servletInputStreamAdapter
operator|=
operator|new
name|ServletInputStreamAdapter
argument_list|(
name|exchange
operator|.
name|getRequestBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|servletInputStreamAdapter
return|;
block|}
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
name|InetSocketAddress
name|isa
init|=
name|exchange
operator|.
name|getLocalAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|isa
operator|!=
literal|null
condition|)
block|{
name|InetAddress
name|ia
init|=
name|isa
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|ia
operator|!=
literal|null
condition|)
block|{
return|return
name|ia
operator|.
name|getHostAddress
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|Locale
argument_list|>
name|getLocales
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
name|InetSocketAddress
name|isa
init|=
name|exchange
operator|.
name|getLocalAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|isa
operator|!=
literal|null
condition|)
block|{
name|InetAddress
name|ia
init|=
name|isa
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|ia
operator|!=
literal|null
condition|)
block|{
return|return
name|ia
operator|.
name|getHostName
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
name|InetSocketAddress
name|isa
init|=
name|exchange
operator|.
name|getLocalAddress
argument_list|()
decl_stmt|;
return|return
name|isa
operator|!=
literal|null
condition|?
name|isa
operator|.
name|getPort
argument_list|()
else|:
literal|0
return|;
block|}
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getParameterMap
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getParameterNames
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
index|[]
name|getParameterValues
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getProtocol
argument_list|()
return|;
block|}
specifier|public
name|BufferedReader
name|getReader
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
name|Reader
name|isr
init|=
name|characterEncoding
operator|==
literal|null
condition|?
operator|new
name|InputStreamReader
argument_list|(
name|exchange
operator|.
name|getRequestBody
argument_list|()
argument_list|)
else|:
operator|new
name|InputStreamReader
argument_list|(
name|exchange
operator|.
name|getRequestBody
argument_list|()
argument_list|,
name|characterEncoding
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
name|isr
argument_list|)
expr_stmt|;
block|}
return|return
name|reader
return|;
block|}
annotation|@
name|Deprecated
specifier|public
name|String
name|getRealPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
name|InetSocketAddress
name|isa
init|=
name|exchange
operator|.
name|getRemoteAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|isa
operator|!=
literal|null
condition|)
block|{
name|InetAddress
name|ia
init|=
name|isa
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|ia
operator|!=
literal|null
condition|)
block|{
return|return
name|ia
operator|.
name|getHostAddress
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
name|InetSocketAddress
name|isa
init|=
name|exchange
operator|.
name|getRemoteAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|isa
operator|!=
literal|null
condition|)
block|{
name|InetAddress
name|ia
init|=
name|isa
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|ia
operator|!=
literal|null
condition|)
block|{
return|return
name|ia
operator|.
name|getHostName
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
name|InetSocketAddress
name|isa
init|=
name|exchange
operator|.
name|getRemoteAddress
argument_list|()
decl_stmt|;
return|return
name|isa
operator|!=
literal|null
condition|?
name|isa
operator|.
name|getPort
argument_list|()
else|:
literal|0
return|;
block|}
specifier|public
name|RequestDispatcher
name|getRequestDispatcher
parameter_list|(
name|String
name|path
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getScheme
argument_list|()
return|;
block|}
specifier|public
name|String
name|getServerName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isAsyncStarted
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|isAsyncSupported
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|setAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|setCharacterEncoding
parameter_list|(
name|String
name|env
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|this
operator|.
name|characterEncoding
operator|=
name|env
expr_stmt|;
block|}
specifier|public
name|AsyncContext
name|startAsync
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|AsyncContext
name|startAsync
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getContextPath
argument_list|()
return|;
block|}
specifier|public
name|Cookie
index|[]
name|getCookies
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|long
name|getDateHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|s
init|=
name|this
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|Long
operator|.
name|parseLong
argument_list|(
name|s
argument_list|)
else|:
literal|0
return|;
block|}
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getRequestHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|exchange
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getHeaders
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getRequestHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|list
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|enumeration
argument_list|(
name|list
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|int
name|getIntHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|s
init|=
name|this
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
else|:
literal|0
return|;
block|}
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getRequestMethod
argument_list|()
return|;
block|}
specifier|public
name|Part
name|getPart
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|Part
argument_list|>
name|getParts
parameter_list|()
throws|throws
name|IOException
throws|,
name|ServletException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getPathInfo
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPathTranslated
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getQueryString
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getQueryString
argument_list|()
return|;
block|}
specifier|public
name|String
name|getRemoteUser
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getRequestedSessionId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getRequestURI
argument_list|()
return|;
block|}
specifier|public
name|StringBuffer
name|getRequestURL
parameter_list|()
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
expr_stmt|;
name|String
name|host
init|=
name|this
operator|.
name|getHeader
argument_list|(
literal|"Host"
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InetSocketAddress
name|la
init|=
name|exchange
operator|.
name|getLocalAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|la
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|la
operator|.
name|getHostName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|la
operator|.
name|getPort
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|la
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|context
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
return|;
block|}
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|HttpSession
name|getSession
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|HttpSession
name|getSession
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getUserPrincipal
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdFromCookie
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Deprecated
specifier|public
name|boolean
name|isRequestedSessionIdFromUrl
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdFromURL
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdValid
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|isUserInRole
argument_list|(
name|role
argument_list|)
return|;
block|}
specifier|public
name|void
name|login
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|ServletException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|logout
parameter_list|()
throws|throws
name|ServletException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|private
specifier|static
class|class
name|ServletInputStreamAdapter
extends|extends
name|ServletInputStream
block|{
specifier|private
name|InputStream
name|delegate
decl_stmt|;
name|ServletInputStreamAdapter
parameter_list|(
name|InputStream
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|delegate
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFinished
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReady
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setReadListener
parameter_list|(
name|ReadListener
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|long
name|getContentLengthLong
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|changeSessionId
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|HttpUpgradeHandler
parameter_list|>
name|T
name|upgrade
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

