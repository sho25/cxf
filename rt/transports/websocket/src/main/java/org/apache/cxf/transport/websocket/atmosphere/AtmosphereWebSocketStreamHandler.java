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
name|atmosphere
package|;
end_package

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
name|Reader
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
name|WebSocketServletHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocketProtocolStream
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AtmosphereWebSocketStreamHandler
extends|extends
name|AtmosphereWebSocketHandler
implements|implements
name|WebSocketProtocolStream
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
name|AtmosphereWebSocketStreamHandler
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AtmosphereRequest
argument_list|>
name|onTextStream
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|Reader
name|r
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"onTextStream(WebSocket, Reader)"
argument_list|)
expr_stmt|;
comment|//TODO add support for Reader
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"not implemented"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AtmosphereRequest
argument_list|>
name|onBinaryStream
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|InputStream
name|stream
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"onBinaryStream(WebSocket, InputStream)"
argument_list|)
expr_stmt|;
try|try
block|{
name|WebSocketServletHolder
name|webSocketHolder
init|=
operator|new
name|AtmosphereWebSocketServletHolder
argument_list|(
name|webSocket
argument_list|)
decl_stmt|;
name|HttpServletRequest
name|request
init|=
name|createServletRequest
argument_list|(
name|webSocketHolder
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|response
init|=
name|createServletResponse
argument_list|(
name|webSocketHolder
argument_list|)
decl_stmt|;
if|if
condition|(
name|destination
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|WebSocketDestinationService
operator|)
name|destination
operator|)
operator|.
name|invokeInternal
argument_list|(
literal|null
argument_list|,
name|webSocket
operator|.
name|resource
argument_list|()
operator|.
name|getRequest
argument_list|()
operator|.
name|getServletContext
argument_list|()
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

