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
name|WebSocketDestinationService
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Request
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AtmosphereWebSocketJettyDestination
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
name|AtmosphereWebSocketJettyDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AtmosphereFramework
name|framework
decl_stmt|;
specifier|public
name|AtmosphereWebSocketJettyDestination
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
name|addInitParameter
argument_list|(
name|ApplicationConfig
operator|.
name|WEBSOCKET_PROTOCOL_EXECUTION
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|AtmosphereUtils
operator|.
name|addInterceptors
argument_list|(
name|framework
argument_list|,
name|bus
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
name|AtmosphereJettyWebSocketHandler
argument_list|(
name|jhd
argument_list|,
name|cmExact
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
name|AtmosphereJettyWebSocketHandler
extends|extends
name|JettyHTTPHandler
block|{
name|AtmosphereJettyWebSocketHandler
parameter_list|(
name|JettyHTTPDestination
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|String
name|target
parameter_list|,
name|Request
name|baseRequest
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
if|if
condition|(
name|AtmosphereUtils
operator|.
name|useAtmosphere
argument_list|(
name|request
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
name|request
argument_list|)
argument_list|,
name|AtmosphereResponse
operator|.
name|wrap
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
name|baseRequest
operator|.
name|setHandled
argument_list|(
literal|true
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
else|else
block|{
name|super
operator|.
name|handle
argument_list|(
name|target
argument_list|,
name|baseRequest
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
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
comment|// used for internal tests
name|AtmosphereFramework
name|getAtmosphereFramework
parameter_list|()
block|{
return|return
name|framework
return|;
block|}
block|}
end_class

end_unit

