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
name|websocket
operator|.
name|undertow
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
name|ByteArrayInputStream
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
name|URL
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
name|Arrays
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
name|TreeMap
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketUtils
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|websockets
operator|.
name|core
operator|.
name|BufferedBinaryMessage
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|websockets
operator|.
name|core
operator|.
name|BufferedTextMessage
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|websockets
operator|.
name|core
operator|.
name|WebSocketChannel
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WebSocketUndertowServletRequest
implements|implements
name|HttpServletRequest
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
name|WebSocketUndertowServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|WebSocketChannel
name|channel
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestHeaders
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
decl_stmt|;
specifier|private
name|InputStream
name|in
decl_stmt|;
specifier|public
name|WebSocketUndertowServletRequest
parameter_list|(
name|WebSocketChannel
name|channel
parameter_list|,
name|Object
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|BufferedBinaryMessage
condition|)
block|{
name|in
operator|=
operator|new
name|ByteBufferInputStream
argument_list|(
operator|(
operator|(
name|BufferedBinaryMessage
operator|)
name|message
operator|)
operator|.
name|getData
argument_list|()
operator|.
name|getResource
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|BufferedTextMessage
condition|)
block|{
name|in
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|BufferedTextMessage
operator|)
name|message
operator|)
operator|.
name|getData
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|requestHeaders
operator|=
name|WebSocketUtils
operator|.
name|readHeaders
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|/*String path = requestHeaders.get(WebSocketUtils.URI_KEY);         String origin = channel.getUrl();         path = path.substring(0,  path.length() - 10);         if (!path.startsWith(origin)) {             throw new InvalidPathException();         }*/
name|this
operator|.
name|attributes
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|Object
name|v
init|=
name|channel
operator|.
name|getAttribute
argument_list|(
literal|"org.apache.cxf.transport.endpoint.address"
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|attributes
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.transport.endpoint.address"
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|AsyncContext
name|getAsyncContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
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
literal|"getAttribute({0}) -> {1}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getAttributeNames
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getAttributeNames()"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|attributes
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getCharacterEncoding()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getContentLength
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getContentLength()"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getContentType()"
argument_list|)
expr_stmt|;
return|return
name|requestHeaders
operator|.
name|get
argument_list|(
literal|"Content-Type"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|DispatcherType
name|getDispatcherType
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getDispatcherType()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServletInputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ServletInputStream
argument_list|()
block|{
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
name|in
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|in
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
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
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getLocalAddr()"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getLocalName()"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getLocalPort()"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getPort
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getLocale()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|Locale
argument_list|>
name|getLocales
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getLocales()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
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
literal|"getParameter({0})"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
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
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getParameterMap()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getParameterNames
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getParameterNames()"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getParameterValues
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
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
literal|"getParameterValues({0})"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getProtocol"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getProtocol
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|BufferedReader
name|getReader
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getReader"
argument_list|)
expr_stmt|;
return|return
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
literal|"utf-8"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRealPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRealPath"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRemoteAddr"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getPeerAddress
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRemoteHost"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getPeerAddress
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRemotePort"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getPeerAddress
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|getPort
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RequestDispatcher
name|getRequestDispatcher
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRequestDispatcher"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getScheme"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|channel
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|getProtocol
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServerName
parameter_list|()
block|{
return|return
name|getLocalName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getServerPort"
argument_list|)
expr_stmt|;
return|return
name|getLocalPort
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getServletContext"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAsyncStarted
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isAsyncStarted"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAsyncSupported
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isAsyncSupported"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isSecure"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"removeAttribute"
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"setAttribute"
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"setCharacterEncoding"
argument_list|)
expr_stmt|;
comment|// ignore as we stick to utf-8.
block|}
annotation|@
name|Override
specifier|public
name|AsyncContext
name|startAsync
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"startAsync"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|AsyncContext
name|startAsync
parameter_list|(
name|ServletRequest
name|servletRequest
parameter_list|,
name|ServletResponse
name|servletResponse
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"startAsync"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|HttpServletResponse
name|servletResponse
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
comment|// TODO Auto-generated method stub
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"authenticate"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getAuthType"
argument_list|)
expr_stmt|;
return|return
literal|"null"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
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
literal|"getContextPath -> "
operator|+
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Cookie
index|[]
name|getCookies
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getCookies"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getDateHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getDateHeader"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getHeader"
argument_list|)
expr_stmt|;
return|return
name|requestHeaders
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getHeaderNames
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getHeaderNames"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|requestHeaders
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getHeaders"
argument_list|)
expr_stmt|;
comment|// our protocol assumes no multiple headers
if|if
condition|(
name|requestHeaders
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|requestHeaders
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|()
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getIntHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getIntHeader"
argument_list|)
expr_stmt|;
name|String
name|v
init|=
name|requestHeaders
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|v
operator|==
literal|null
condition|?
operator|-
literal|1
else|:
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getMethod"
argument_list|)
expr_stmt|;
return|return
name|requestHeaders
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|METHOD_KEY
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getPart"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getParts"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPathTranslated
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getQueryString
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getQueryString"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteUser
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRemoteUser"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
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
literal|"getRequestURI "
operator|+
name|requestHeaders
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|URI_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|requestHeaders
operator|.
name|get
argument_list|(
name|WebSocketUtils
operator|.
name|URI_KEY
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|StringBuffer
name|getRequestURL
parameter_list|()
block|{
return|return
operator|new
name|StringBuffer
argument_list|(
name|getRequestURI
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestedSessionId
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getRequestedSessionId"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|HttpSession
name|getSession
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getSession"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|HttpSession
name|getSession
parameter_list|(
name|boolean
name|create
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getSession"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"getUserPrincipal"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRequestedSessionIdFromCookie
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isRequestedSessionIdFromCookie"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRequestedSessionIdFromURL
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isRequestedSessionIdFromURL"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRequestedSessionIdFromUrl
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isRequestedSessionIdFromUrl"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isRequestedSessionIdValid
parameter_list|()
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isRequestedSessionIdValid"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"isUserInRole"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"login"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|logout
parameter_list|()
throws|throws
name|ServletException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"logout"
argument_list|)
expr_stmt|;
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
name|arg0
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

