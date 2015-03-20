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
name|IOException
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
name|ServletException
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
name|servlet
operator|.
name|ServletDestination
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|ApplicationConfig
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
name|AtmosphereFramework
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
name|cpr
operator|.
name|AtmosphereResource
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
name|AtmosphereResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|handler
operator|.
name|AbstractReflectorAtmosphereHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|util
operator|.
name|Utils
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AtmosphereWebSocketServletDestination
extends|extends
name|ServletDestination
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
name|AtmosphereWebSocketServletDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AtmosphereFramework
name|framework
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|public
name|AtmosphereWebSocketServletDestination
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
name|String
name|path
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
name|path
argument_list|)
expr_stmt|;
name|framework
operator|=
operator|new
name|AtmosphereFramework
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|framework
operator|.
name|setUseNativeImplementation
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|framework
operator|.
name|addInitParameter
argument_list|(
name|ApplicationConfig
operator|.
name|PROPERTY_NATIVE_COMETSUPPORT
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|framework
operator|.
name|addInitParameter
argument_list|(
name|ApplicationConfig
operator|.
name|PROPERTY_SESSION_SUPPORT
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|framework
operator|.
name|addInitParameter
argument_list|(
name|ApplicationConfig
operator|.
name|WEBSOCKET_SUPPORT
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|framework
operator|.
name|interceptor
argument_list|(
name|AtmosphereUtils
operator|.
name|getInterceptor
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
name|framework
operator|.
name|addAtmosphereHandler
argument_list|(
literal|"/"
argument_list|,
operator|new
name|DestinationHandler
argument_list|()
argument_list|)
expr_stmt|;
name|framework
operator|.
name|init
argument_list|()
expr_stmt|;
comment|// the executor for decoupling the service invocation from websocket's onMessage call which is
comment|// synchronously blocked
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
name|invoke
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
if|if
condition|(
name|Utils
operator|.
name|webSocketEnabled
argument_list|(
name|req
argument_list|)
condition|)
block|{
try|try
block|{
name|framework
operator|.
name|doCometSupport
argument_list|(
name|AtmosphereRequest
operator|.
name|wrap
argument_list|(
name|req
argument_list|)
argument_list|,
name|AtmosphereResponse
operator|.
name|wrap
argument_list|(
name|resp
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServletException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
name|req
argument_list|,
name|resp
argument_list|)
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
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
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
name|framework
operator|.
name|destroy
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
class|class
name|DestinationHandler
extends|extends
name|AbstractReflectorAtmosphereHandler
block|{
annotation|@
name|Override
specifier|public
name|void
name|onRequest
parameter_list|(
specifier|final
name|AtmosphereResource
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"onRequest"
argument_list|)
expr_stmt|;
name|executeHandlerTask
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
name|invokeInternal
argument_list|(
literal|null
argument_list|,
name|resource
operator|.
name|getRequest
argument_list|()
operator|.
name|getServletContext
argument_list|()
argument_list|,
name|resource
operator|.
name|getRequest
argument_list|()
argument_list|,
name|resource
operator|.
name|getResponse
argument_list|()
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
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|executeHandlerTask
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
block|}
end_class

end_unit

