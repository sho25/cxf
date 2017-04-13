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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|SystemPropertyAction
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
name|DestinationFactory
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
name|DestinationFactoryManager
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
name|http
operator|.
name|HTTPTransportFactory
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
name|HttpDestinationFactory
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
name|atmosphere
operator|.
name|AtmosphereWebSocketServletDestination
import|;
end_import

begin_comment
comment|//import org.apache.cxf.transport.websocket.jetty.JettyWebSocketServletDestination;
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|()
specifier|public
class|class
name|WebSocketDestinationFactory
implements|implements
name|HttpDestinationFactory
block|{
specifier|private
specifier|static
specifier|final
name|boolean
name|ATMOSPHERE_AVAILABLE
init|=
name|probeClass
argument_list|(
literal|"org.atmosphere.cpr.ApplicationConfig"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|JETTY_AVAILABLE
init|=
name|probeClass
argument_list|(
literal|"org.eclipse.jetty.server.Server"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|UNDERTOW_AVAILABLE
init|=
name|probeClass
argument_list|(
literal|"io.undertow.websockets.core.WebSockets"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Constructor
argument_list|<
name|?
argument_list|>
name|JETTY9_WEBSOCKET_DESTINATION_CTR
init|=
name|probeConstructor
argument_list|(
literal|"org.apache.cxf.transport.websocket.jetty9.Jetty9WebSocketDestination"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Constructor
argument_list|<
name|?
argument_list|>
name|ATMOSPHERE_WEBSOCKET_JETTY_DESTINATION_CTR
init|=
name|probeConstructor
argument_list|(
literal|"org.apache.cxf.transport.websocket.atmosphere.AtmosphereWebSocketJettyDestination"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Constructor
argument_list|<
name|?
argument_list|>
name|ATMOSPHERE_WEBSOCKET_UNDERTOW_DESTINATION_CTR
init|=
name|probeUndertowConstructor
argument_list|(
literal|"org.apache.cxf.transport.websocket.atmosphere.AtmosphereWebSocketUndertowDestination"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|atmosphereDisabled
init|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"org.apache.cxf.transport.websocket.atmosphere.disabled"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|probeClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
argument_list|,
name|WebSocketDestinationFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
specifier|static
name|Constructor
argument_list|<
name|?
argument_list|>
name|probeConstructor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
argument_list|,
name|WebSocketDestinationFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|clz
operator|.
name|getConstructor
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|DestinationRegistry
operator|.
name|class
argument_list|,
name|EndpointInfo
operator|.
name|class
argument_list|,
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
name|Constructor
argument_list|<
name|?
argument_list|>
name|probeUndertowConstructor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
argument_list|,
name|WebSocketDestinationFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|clz
operator|.
name|getConstructor
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|DestinationRegistry
operator|.
name|class
argument_list|,
name|EndpointInfo
operator|.
name|class
argument_list|,
name|UndertowHTTPServerEngineFactory
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|AbstractHTTPDestination
name|createDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"ws"
argument_list|)
condition|)
block|{
if|if
condition|(
name|ATMOSPHERE_AVAILABLE
operator|&&
operator|!
name|atmosphereDisabled
condition|)
block|{
comment|// use atmosphere if available
if|if
condition|(
name|JETTY_AVAILABLE
condition|)
block|{
comment|// for the embedded mode, we stick to jetty
name|JettyHTTPServerEngineFactory
name|serverEngineFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|createJettyHTTPDestination
argument_list|(
name|ATMOSPHERE_WEBSOCKET_JETTY_DESTINATION_CTR
argument_list|,
name|bus
argument_list|,
name|registry
argument_list|,
name|endpointInfo
argument_list|,
name|serverEngineFactory
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|UNDERTOW_AVAILABLE
condition|)
block|{
comment|// use AtmosphereWebSocketUndertowDestination
name|UndertowHTTPServerEngineFactory
name|undertowServerEngineFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|UndertowHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|createUndertowHTTPDestination
argument_list|(
name|ATMOSPHERE_WEBSOCKET_UNDERTOW_DESTINATION_CTR
argument_list|,
name|bus
argument_list|,
name|registry
argument_list|,
name|endpointInfo
argument_list|,
name|undertowServerEngineFactory
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
comment|// for the embedded mode, we stick to jetty
name|JettyHTTPServerEngineFactory
name|serverEngineFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|createJettyHTTPDestination
argument_list|(
name|JETTY9_WEBSOCKET_DESTINATION_CTR
argument_list|,
name|bus
argument_list|,
name|registry
argument_list|,
name|endpointInfo
argument_list|,
name|serverEngineFactory
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// REVISIT other way of getting the registry of http so that the plain cxf servlet finds the
comment|// destination?
name|registry
operator|=
name|getDestinationRegistry
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// choose atmosphere if available, otherwise assume jetty is available
if|if
condition|(
name|ATMOSPHERE_AVAILABLE
operator|&&
operator|!
name|atmosphereDisabled
condition|)
block|{
comment|// use atmosphere if available
return|return
operator|new
name|AtmosphereWebSocketServletDestination
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|endpointInfo
argument_list|,
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// use jetty-websocket
return|return
name|createJettyHTTPDestination
argument_list|(
name|JETTY9_WEBSOCKET_DESTINATION_CTR
argument_list|,
name|bus
argument_list|,
name|registry
argument_list|,
name|endpointInfo
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
block|}
specifier|private
specifier|static
name|DestinationRegistry
name|getDestinationRegistry
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|DestinationFactory
name|df
init|=
name|dfm
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|)
decl_stmt|;
if|if
condition|(
name|df
operator|instanceof
name|HTTPTransportFactory
condition|)
block|{
name|HTTPTransportFactory
name|transportFactory
init|=
operator|(
name|HTTPTransportFactory
operator|)
name|df
decl_stmt|;
return|return
name|transportFactory
operator|.
name|getRegistry
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// why are we throwing a busexception if the DF isn't found?
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|AbstractHTTPDestination
name|createJettyHTTPDestination
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctr
parameter_list|,
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
name|jhsef
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ctr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|(
name|AbstractHTTPDestination
operator|)
name|ctr
operator|.
name|newInstance
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|jhsef
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// log
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|AbstractHTTPDestination
name|createUndertowHTTPDestination
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctr
parameter_list|,
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
name|jhsef
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ctr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
operator|(
name|AbstractHTTPDestination
operator|)
name|ctr
operator|.
name|newInstance
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|jhsef
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// log
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

