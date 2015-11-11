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
name|jetty9
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
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
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
name|Locale
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
name|ExecutionException
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
name|Executor
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
name|RejectedExecutionException
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
name|ServletConfig
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
name|Bus
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|http
operator|.
name|DestinationRegistry
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
name|http_jetty
operator|.
name|JettyHTTPDestination
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
name|http_jetty
operator|.
name|JettyHTTPHandler
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
name|http_jetty
operator|.
name|JettyHTTPServerEngineFactory
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
name|WebSocketDestinationService
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
name|jetty
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
name|jetty
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
name|jetty
operator|.
name|WebSocketVirtualServletResponse
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|server
operator|.
name|Request
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
name|api
operator|.
name|Session
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
name|api
operator|.
name|WebSocketAdapter
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
name|servlet
operator|.
name|ServletUpgradeRequest
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
name|servlet
operator|.
name|ServletUpgradeResponse
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
name|servlet
operator|.
name|WebSocketCreator
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
name|servlet
operator|.
name|WebSocketServletFactory
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|Jetty9WebSocketDestination
extends|extends
name|JettyHTTPDestination
implements|implements
name|WebSocketDestinationService
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
name|Jetty9WebSocketDestination
operator|.
name|class
argument_list|)
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
specifier|private
name|WebSocketServletFactory
name|webSocketFactory
decl_stmt|;
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
specifier|public
name|Jetty9WebSocketDestination
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|JettyHTTPServerEngineFactory
name|serverEngineFactory
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|serverEngineFactory
argument_list|)
expr_stmt|;
try|try
block|{
name|webSocketFactory
operator|=
operator|(
name|WebSocketServletFactory
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"org.eclipse.jetty.websocket.server.WebSocketServerFactory"
argument_list|,
name|WebSocketServletFactory
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
decl||
name|IllegalAccessException
decl||
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|webSocketFactory
operator|.
name|setCreator
argument_list|(
operator|new
name|Creator
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Field
name|f
init|=
name|webSocketFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"objectFactory"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
name|webSocketFactory
argument_list|,
name|f
operator|.
name|getType
argument_list|()
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore, on Jetty<=9.2 this field doesn't exist
block|}
name|executor
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invokeInternal
parameter_list|(
name|ServletConfig
name|config
parameter_list|,
name|ServletContext
name|context
parameter_list|,
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|invoke
argument_list|(
name|config
argument_list|,
name|context
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|invoke
parameter_list|(
specifier|final
name|ServletConfig
name|config
parameter_list|,
specifier|final
name|ServletContext
name|context
parameter_list|,
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
specifier|final
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|webSocketFactory
operator|.
name|isUpgradeRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
operator|&&
name|webSocketFactory
operator|.
name|acceptWebSocket
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
condition|)
block|{
operator|(
operator|(
name|Request
operator|)
name|request
operator|)
operator|.
name|setHandled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|invoke
argument_list|(
name|config
argument_list|,
name|context
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getAddress
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|String
name|address
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
literal|"ws"
argument_list|)
condition|)
block|{
name|address
operator|=
literal|"http"
operator|+
name|address
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|address
return|;
block|}
annotation|@
name|Override
specifier|protected
name|JettyHTTPHandler
name|createJettyHTTPHandler
parameter_list|(
name|JettyHTTPDestination
name|jhd
parameter_list|,
name|boolean
name|cmExact
parameter_list|)
block|{
return|return
operator|new
name|JettyWebSocketHandler
argument_list|(
name|jhd
argument_list|,
name|cmExact
argument_list|,
name|webSocketFactory
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
try|try
block|{
name|webSocketFactory
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
name|super
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|invoke
parameter_list|(
specifier|final
name|byte
index|[]
name|data
parameter_list|,
specifier|final
name|int
name|offset
parameter_list|,
specifier|final
name|int
name|length
parameter_list|,
specifier|final
name|Session
name|session
parameter_list|)
block|{
comment|// invoke the service asynchronously as the jetty websocket's onMessage is synchronously blocked
comment|// make sure the byte array passed to this method is immutable, as the websocket framework
comment|// may corrupt the byte array after this method is returned (i.e., before the data is returned in
comment|// the executor's thread.
name|executeServiceTask
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
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
name|WebSocketServletHolder
name|holder
init|=
operator|new
name|Jetty9WebSocketHolder
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|response
operator|=
name|createServletResponse
argument_list|(
name|holder
argument_list|)
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
argument_list|,
name|holder
argument_list|)
expr_stmt|;
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
name|invoke
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPathException
name|ex
parameter_list|)
block|{
name|reportErrorStatus
argument_list|(
name|session
argument_list|,
literal|400
argument_list|,
name|response
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
name|session
argument_list|,
literal|500
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|executeServiceTask
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Executor queue is full, run the service invocation task in caller thread."
operator|+
literal|"  Users can specify a larger executor queue to avoid this."
argument_list|)
expr_stmt|;
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|reportErrorStatus
parameter_list|(
name|Session
name|session
parameter_list|,
name|int
name|i
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
block|{
try|try
block|{
name|resp
operator|.
name|sendError
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
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
parameter_list|,
name|WebSocketServletHolder
name|holder
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|WebSocketVirtualServletRequest
argument_list|(
name|holder
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
parameter_list|(
name|WebSocketServletHolder
name|holder
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|WebSocketVirtualServletResponse
argument_list|(
name|holder
argument_list|)
return|;
block|}
comment|// hide this jetty9 interface here to avoid CNFE on WebSocketCreator
specifier|private
class|class
name|Creator
implements|implements
name|WebSocketCreator
block|{
annotation|@
name|Override
specifier|public
name|Object
name|createWebSocket
parameter_list|(
name|ServletUpgradeRequest
name|req
parameter_list|,
name|ServletUpgradeResponse
name|resp
parameter_list|)
block|{
return|return
operator|new
name|WebSocketAdapter
argument_list|()
block|{
name|Session
name|session
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|onWebSocketConnect
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onWebSocketBinary
parameter_list|(
name|byte
index|[]
name|payload
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|invoke
argument_list|(
name|payload
argument_list|,
name|offset
argument_list|,
name|len
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onWebSocketText
parameter_list|(
name|String
name|message
parameter_list|)
block|{
comment|//TODO may want use string directly instead of converting it to byte[]
try|try
block|{
name|byte
index|[]
name|bdata
init|=
name|message
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|onWebSocketBinary
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
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
class|class
name|Jetty9WebSocketHolder
implements|implements
name|WebSocketServletHolder
block|{
specifier|final
name|Session
name|session
decl_stmt|;
name|Jetty9WebSocketHolder
parameter_list|(
name|Session
name|s
parameter_list|)
block|{
name|session
operator|=
name|s
expr_stmt|;
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
operator|(
operator|(
name|ServletUpgradeRequest
operator|)
name|session
operator|.
name|getUpgradeRequest
argument_list|()
operator|)
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getContextPath
argument_list|()
return|;
block|}
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
return|return
literal|0
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
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
return|return
name|session
operator|.
name|getUpgradeRequest
argument_list|()
operator|.
name|getRequestURI
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
specifier|public
name|StringBuffer
name|getRequestURL
parameter_list|()
block|{
return|return
operator|new
name|StringBuffer
argument_list|(
name|session
operator|.
name|getUpgradeRequest
argument_list|()
operator|.
name|getRequestURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|DispatcherType
name|getDispatcherType
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
return|return
name|session
operator|.
name|getUpgradeRequest
argument_list|()
operator|.
name|getRequestURI
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPathTranslated
parameter_list|()
block|{
return|return
name|session
operator|.
name|getUpgradeRequest
argument_list|()
operator|.
name|getRequestURI
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|"ws"
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
name|String
name|getServletPath
parameter_list|()
block|{
return|return
literal|""
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
name|int
name|getServerPort
parameter_list|()
block|{
return|return
name|session
operator|.
name|getLocalAddress
argument_list|()
operator|.
name|getPort
argument_list|()
return|;
block|}
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
literal|null
return|;
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
operator|(
operator|(
name|ServletUpgradeRequest
operator|)
name|session
operator|.
name|getUpgradeRequest
argument_list|()
operator|)
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|name
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
try|try
block|{
name|session
operator|.
name|getRemote
argument_list|()
operator|.
name|sendBytesByFuture
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

