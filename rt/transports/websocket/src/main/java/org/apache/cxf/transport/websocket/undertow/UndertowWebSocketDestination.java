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
name|IOException
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|StringUtils
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
name|http_undertow
operator|.
name|UndertowHTTPDestination
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
name|http_undertow
operator|.
name|UndertowHTTPHandler
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
name|http_undertow
operator|.
name|UndertowHTTPServerEngineFactory
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
name|WebSocketUtils
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
name|xnio
operator|.
name|StreamConnection
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpUpgradeListener
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|handlers
operator|.
name|ServletRequestContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|spec
operator|.
name|HttpServletRequestImpl
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|spec
operator|.
name|HttpServletResponseImpl
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|spec
operator|.
name|ServletContextImpl
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Methods
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
name|AbstractReceiveListener
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
name|protocol
operator|.
name|Handshake
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
name|protocol
operator|.
name|version07
operator|.
name|Hybi07Handshake
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
name|protocol
operator|.
name|version08
operator|.
name|Hybi08Handshake
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
name|protocol
operator|.
name|version13
operator|.
name|Hybi13Handshake
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
name|spi
operator|.
name|AsyncWebSocketHttpServerExchange
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|UndertowWebSocketDestination
extends|extends
name|UndertowHTTPDestination
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
name|UndertowWebSocketDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
specifier|public
name|UndertowWebSocketDestination
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
name|UndertowHTTPServerEngineFactory
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
specifier|private
specifier|static
name|String
name|getNonWSAddress
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
name|String
name|getAddress
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
return|return
name|getNonWSAddress
argument_list|(
name|endpointInfo
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getBasePath
parameter_list|(
name|String
name|contextPath
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
operator|new
name|URL
argument_list|(
name|getAddress
argument_list|(
name|endpointInfo
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|UndertowHTTPHandler
name|createUndertowHTTPHandler
parameter_list|(
name|UndertowHTTPDestination
name|jhd
parameter_list|,
name|boolean
name|cmExact
parameter_list|)
block|{
return|return
operator|new
name|AtmosphereUndertowWebSocketHandler
argument_list|(
name|jhd
argument_list|,
name|cmExact
argument_list|)
return|;
block|}
specifier|private
class|class
name|AtmosphereUndertowWebSocketHandler
extends|extends
name|UndertowHTTPHandler
block|{
specifier|private
specifier|final
name|Set
argument_list|<
name|Handshake
argument_list|>
name|handshakes
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|WebSocketChannel
argument_list|>
name|peerConnections
init|=
name|Collections
operator|.
name|newSetFromMap
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<
name|WebSocketChannel
argument_list|,
name|Boolean
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|AtmosphereUndertowWebSocketHandler
parameter_list|(
name|UndertowHTTPDestination
name|jhd
parameter_list|,
name|boolean
name|cmExact
parameter_list|)
block|{
name|super
argument_list|(
name|jhd
argument_list|,
name|cmExact
argument_list|)
expr_stmt|;
name|handshakes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|handshakes
operator|.
name|add
argument_list|(
operator|new
name|Hybi13Handshake
argument_list|()
argument_list|)
expr_stmt|;
name|handshakes
operator|.
name|add
argument_list|(
operator|new
name|Hybi08Handshake
argument_list|()
argument_list|)
expr_stmt|;
name|handshakes
operator|.
name|add
argument_list|(
operator|new
name|Hybi07Handshake
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleRequest
parameter_list|(
name|HttpServerExchange
name|undertowExchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|undertowExchange
operator|.
name|isInIoThread
argument_list|()
condition|)
block|{
name|undertowExchange
operator|.
name|dispatch
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|undertowExchange
operator|.
name|getRequestMethod
argument_list|()
operator|.
name|equals
argument_list|(
name|Methods
operator|.
name|GET
argument_list|)
condition|)
block|{
comment|// Only GET is supported to start the handshake
name|handleNormalRequest
argument_list|(
name|undertowExchange
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|AsyncWebSocketHttpServerExchange
name|facade
init|=
operator|new
name|AsyncWebSocketHttpServerExchange
argument_list|(
name|undertowExchange
argument_list|,
name|peerConnections
argument_list|)
decl_stmt|;
name|Handshake
name|handshaker
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Handshake
name|method
range|:
name|handshakes
control|)
block|{
if|if
condition|(
name|method
operator|.
name|matches
argument_list|(
name|facade
argument_list|)
condition|)
block|{
name|handshaker
operator|=
name|method
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|handshaker
operator|==
literal|null
condition|)
block|{
name|handleNormalRequest
argument_list|(
name|undertowExchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Handshake
name|selected
init|=
name|handshaker
decl_stmt|;
name|undertowExchange
operator|.
name|upgradeChannel
argument_list|(
operator|new
name|HttpUpgradeListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handleUpgrade
parameter_list|(
name|StreamConnection
name|streamConnection
parameter_list|,
name|HttpServerExchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|WebSocketChannel
name|channel
init|=
name|selected
operator|.
name|createChannel
argument_list|(
name|facade
argument_list|,
name|streamConnection
argument_list|,
name|facade
operator|.
name|getBufferPool
argument_list|()
argument_list|)
decl_stmt|;
name|peerConnections
operator|.
name|add
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|channel
operator|.
name|getReceiveSetter
argument_list|()
operator|.
name|set
argument_list|(
operator|new
name|AbstractReceiveListener
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|onFullTextMessage
parameter_list|(
name|WebSocketChannel
name|channel
parameter_list|,
name|BufferedTextMessage
name|message
parameter_list|)
block|{
name|handleReceivedMessage
argument_list|(
name|channel
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|onFullBinaryMessage
parameter_list|(
name|WebSocketChannel
name|channel
parameter_list|,
name|BufferedBinaryMessage
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|handleReceivedMessage
argument_list|(
name|channel
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|channel
operator|.
name|resumeReceives
argument_list|()
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
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|handshaker
operator|.
name|handshake
argument_list|(
name|facade
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleNormalRequest
parameter_list|(
name|HttpServerExchange
name|undertowExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpServletResponseImpl
name|response
init|=
operator|new
name|HttpServletResponseImpl
argument_list|(
name|undertowExchange
argument_list|,
operator|(
name|ServletContextImpl
operator|)
name|servletContext
argument_list|)
decl_stmt|;
name|HttpServletRequestImpl
name|request
init|=
operator|new
name|HttpServletRequestImpl
argument_list|(
name|undertowExchange
argument_list|,
operator|(
name|ServletContextImpl
operator|)
name|servletContext
argument_list|)
decl_stmt|;
name|ServletRequestContext
name|servletRequestContext
init|=
operator|new
name|ServletRequestContext
argument_list|(
operator|(
operator|(
name|ServletContextImpl
operator|)
name|servletContext
operator|)
operator|.
name|getDeployment
argument_list|()
argument_list|,
name|request
argument_list|,
name|response
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|undertowExchange
operator|.
name|putAttachment
argument_list|(
name|ServletRequestContext
operator|.
name|ATTACHMENT_KEY
argument_list|,
name|servletRequestContext
argument_list|)
expr_stmt|;
name|doService
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleNormalRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|doService
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|handleReceivedMessage
parameter_list|(
name|WebSocketChannel
name|channel
parameter_list|,
name|Object
name|message
parameter_list|,
name|HttpServerExchange
name|exchange
parameter_list|)
block|{
name|executor
operator|.
name|execute
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
try|try
block|{
name|HttpServletRequest
name|request
init|=
operator|new
name|WebSocketUndertowServletRequest
argument_list|(
name|channel
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|response
init|=
operator|new
name|WebSocketUndertowServletResponse
argument_list|(
name|channel
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|.
name|getHeader
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|headerValue
init|=
name|request
operator|.
name|getHeader
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|WebSocketUtils
operator|.
name|isContainingCRLF
argument_list|(
name|headerValue
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid characters (CR/LF) in header "
operator|+
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|.
name|setHeader
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
name|handleNormalRequest
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

