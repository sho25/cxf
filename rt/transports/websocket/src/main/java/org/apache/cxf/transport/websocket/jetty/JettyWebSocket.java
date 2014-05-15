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
name|jetty
package|;
end_package

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
name|UnsupportedEncodingException
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
name|DispatcherType
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
name|transport
operator|.
name|websocket
operator|.
name|InvalidPathException
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
name|WebSocketConstants
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
name|WebSocketServletHolder
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
name|WebSocketVirtualServletRequest
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
name|WebSocketVirtualServletResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_class
class|class
name|JettyWebSocket
implements|implements
name|WebSocket
operator|.
name|OnBinaryMessage
implements|,
name|WebSocket
operator|.
name|OnTextMessage
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
name|JettyWebSocket
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JettyWebSocketManager
name|manager
decl_stmt|;
specifier|private
name|Connection
name|webSocketConnection
decl_stmt|;
specifier|private
name|WebSocketServletHolder
name|webSocketHolder
decl_stmt|;
specifier|private
name|String
name|protocol
decl_stmt|;
comment|//REVISIT make these keys configurable
specifier|private
name|String
name|requestIdKey
init|=
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
decl_stmt|;
specifier|private
name|String
name|responseIdKey
init|=
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
decl_stmt|;
specifier|public
name|JettyWebSocket
parameter_list|(
name|JettyWebSocketManager
name|manager
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
name|this
operator|.
name|webSocketHolder
operator|=
operator|new
name|JettyWebSocketServletHolder
argument_list|(
name|this
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|int
name|closeCode
parameter_list|,
name|String
name|message
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
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"onClose({0}, {1})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|closeCode
block|,
name|message
block|}
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|webSocketConnection
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onOpen
parameter_list|(
name|Connection
name|connection
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
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"onOpen({0}))"
argument_list|,
name|connection
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|webSocketConnection
operator|=
name|connection
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|data
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
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"onMessage({0})"
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|//TODO may want use string directly instead of converting it to byte[]
name|byte
index|[]
name|bdata
init|=
name|data
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|invokeService
argument_list|(
name|bdata
argument_list|,
literal|0
argument_list|,
name|bdata
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// will not happen
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
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
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"onMessage({0}, {1}, {2})"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|data
block|,
name|offset
block|,
name|length
block|}
argument_list|)
expr_stmt|;
block|}
name|invokeService
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|invokeService
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|HttpServletRequest
name|request
init|=
literal|null
decl_stmt|;
name|HttpServletResponse
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|response
operator|=
name|createServletResponse
argument_list|()
expr_stmt|;
name|request
operator|=
name|createServletRequest
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|String
name|reqid
init|=
name|request
operator|.
name|getHeader
argument_list|(
name|requestIdKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|reqid
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setHeader
argument_list|(
name|responseIdKey
argument_list|,
name|reqid
argument_list|)
expr_stmt|;
block|}
name|manager
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidPathException
name|ex
parameter_list|)
block|{
name|reportErrorStatus
argument_list|(
name|response
argument_list|,
literal|400
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Failed to invoke service"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|reportErrorStatus
argument_list|(
name|response
argument_list|,
literal|500
argument_list|)
expr_stmt|;
block|}
block|}
comment|// may want to move this error reporting code to WebSocketServletHolder
specifier|private
name|void
name|reportErrorStatus
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|,
name|int
name|status
parameter_list|)
block|{
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
try|try
block|{
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|write
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|response
operator|.
name|getWriter
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|response
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|WebSocketVirtualServletRequest
name|createServletRequest
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|WebSocketVirtualServletRequest
argument_list|(
name|webSocketHolder
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|WebSocketVirtualServletResponse
name|createServletResponse
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|WebSocketVirtualServletResponse
argument_list|(
name|webSocketHolder
argument_list|)
return|;
block|}
comment|/**      * Writes to the underlining socket.      *       * @param data      * @param offset      * @param length      */
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"write(byte[], offset, length)"
argument_list|)
expr_stmt|;
name|webSocketConnection
operator|.
name|sendMessage
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
specifier|private
specifier|static
class|class
name|JettyWebSocketServletHolder
implements|implements
name|WebSocketServletHolder
block|{
specifier|private
name|JettyWebSocket
name|webSocket
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestProperties
decl_stmt|;
specifier|public
name|JettyWebSocketServletHolder
parameter_list|(
name|JettyWebSocket
name|webSocket
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|this
operator|.
name|webSocket
operator|=
name|webSocket
expr_stmt|;
name|this
operator|.
name|requestProperties
operator|=
name|readProperties
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getRequestProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|requestProperties
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|readProperties
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"servletPath"
argument_list|,
name|request
operator|.
name|getServletPath
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"requestURI"
argument_list|,
name|request
operator|.
name|getRequestURI
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"requestURL"
argument_list|,
name|request
operator|.
name|getRequestURL
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"contextPath"
argument_list|,
name|request
operator|.
name|getContextPath
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"servletPath"
argument_list|,
name|request
operator|.
name|getServletPath
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"servletContext"
argument_list|,
name|request
operator|.
name|getServletContext
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"pathInfo"
argument_list|,
name|request
operator|.
name|getPathInfo
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"pathTranslated"
argument_list|,
name|request
operator|.
name|getPathTranslated
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"protocol"
argument_list|,
name|request
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"scheme"
argument_list|,
name|request
operator|.
name|getScheme
argument_list|()
argument_list|)
expr_stmt|;
comment|// some additional ones
name|properties
operator|.
name|put
argument_list|(
literal|"localAddr"
argument_list|,
name|request
operator|.
name|getLocalAddr
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"localName"
argument_list|,
name|request
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"localPort"
argument_list|,
name|request
operator|.
name|getLocalPort
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"locale"
argument_list|,
name|request
operator|.
name|getLocale
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"locales"
argument_list|,
name|request
operator|.
name|getLocales
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"remoteHost"
argument_list|,
name|request
operator|.
name|getRemoteHost
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"remoteAddr"
argument_list|,
name|request
operator|.
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"serverName"
argument_list|,
name|request
operator|.
name|getServerName
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"serverPort"
argument_list|,
name|request
operator|.
name|getServerPort
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"secure"
argument_list|,
name|request
operator|.
name|isSecure
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"authType"
argument_list|,
name|request
operator|.
name|getAuthType
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"dispatcherType"
argument_list|,
name|request
operator|.
name|getDispatcherType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"authType"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"contextPath"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"LocalAddr"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"localName"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"localPort"
argument_list|,
name|int
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"locale"
argument_list|,
name|Locale
operator|.
name|class
argument_list|)
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
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|getRequestProperty
argument_list|(
literal|"locales"
argument_list|,
name|Enumeration
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"protocol"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"remoteAddr"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"remoteHost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"remotePort"
argument_list|,
name|int
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"requestURI"
argument_list|,
name|String
operator|.
name|class
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
name|getRequestProperty
argument_list|(
literal|"requestURL"
argument_list|,
name|StringBuffer
operator|.
name|class
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
return|return
name|getRequestProperty
argument_list|(
literal|"dispatcherType"
argument_list|,
name|DispatcherType
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"secure"
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
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
name|getRequestProperty
argument_list|(
literal|"pathInfo"
argument_list|,
name|String
operator|.
name|class
argument_list|)
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
name|getRequestProperty
argument_list|(
literal|"pathTranslated"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"scheme"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getServerName
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"serverName"
argument_list|,
name|String
operator|.
name|class
argument_list|)
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
name|getRequestProperty
argument_list|(
literal|"servletPath"
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"serverPort"
argument_list|,
name|int
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"serverContext"
argument_list|,
name|ServletContext
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|getRequestProperty
argument_list|(
literal|"userPrincipal"
argument_list|,
name|Principal
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|webSocket
operator|.
name|write
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

