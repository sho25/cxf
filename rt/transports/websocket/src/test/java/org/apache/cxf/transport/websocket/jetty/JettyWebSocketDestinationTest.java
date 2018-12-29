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
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|bus
operator|.
name|extension
operator|.
name|ExtensionManagerBus
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
name|http_jetty
operator|.
name|JettyHTTPServerEngine
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
name|jetty9
operator|.
name|Jetty9WebSocketDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|JettyWebSocketDestinationTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_ADDRESS
init|=
literal|"ws://localhost:9001/websocket/nada"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|ENDPOINT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"urn:websocket:probe"
argument_list|,
literal|"nada"
argument_list|)
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRegisteration
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|DestinationRegistry
name|registry
init|=
operator|new
name|HTTPTransportFactory
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|EndpointInfo
name|endpoint
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|setAddress
argument_list|(
name|ENDPOINT_ADDRESS
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setName
argument_list|(
name|ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|JettyHTTPServerEngine
name|engine
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|JettyHTTPServerEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|TestJettyWebSocketDestination
name|dest
init|=
operator|new
name|TestJettyWebSocketDestination
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|endpoint
argument_list|,
literal|null
argument_list|,
name|engine
argument_list|)
decl_stmt|;
name|dest
operator|.
name|activate
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|registry
operator|.
name|getDestinationForPath
argument_list|(
name|ENDPOINT_ADDRESS
argument_list|)
argument_list|)
expr_stmt|;
name|dest
operator|.
name|deactivate
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|registry
operator|.
name|getDestinationForPath
argument_list|(
name|ENDPOINT_ADDRESS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|TestJettyWebSocketDestination
extends|extends
name|Jetty9WebSocketDestination
block|{
name|TestJettyWebSocketDestination
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
parameter_list|,
name|JettyHTTPServerEngine
name|engine
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
name|this
operator|.
name|engine
operator|=
name|engine
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|super
operator|.
name|activate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
name|super
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

