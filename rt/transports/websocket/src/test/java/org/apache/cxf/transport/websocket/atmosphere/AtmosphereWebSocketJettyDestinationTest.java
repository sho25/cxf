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
name|List
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
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AtmosphereWebSocketJettyDestinationTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ENDPOINT_ADDRESS
init|=
literal|"ws://localhost:8080/websocket/nada"
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
annotation|@
name|Test
specifier|public
name|void
name|testUseCXFDefaultAtmoosphereInterceptor
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
name|AtmosphereWebSocketServletDestination
name|dest
init|=
operator|new
name|AtmosphereWebSocketServletDestination
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|endpoint
argument_list|,
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AtmosphereInterceptor
argument_list|>
name|ais
init|=
name|dest
operator|.
name|getAtmosphereFramework
argument_list|()
operator|.
name|interceptors
argument_list|()
decl_stmt|;
name|int
name|added
init|=
literal|0
decl_stmt|;
for|for
control|(
name|AtmosphereInterceptor
name|a
range|:
name|ais
control|)
block|{
if|if
condition|(
name|DefaultProtocolInterceptor
operator|.
name|class
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|added
operator|++
expr_stmt|;
break|break;
block|}
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|added
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUseCustomAtmoosphereInterceptors
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
name|bus
operator|.
name|setProperty
argument_list|(
literal|"atmosphere.interceptors"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|CustomInterceptor1
argument_list|()
argument_list|,
operator|new
name|CustomInterceptor2
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|AtmosphereWebSocketServletDestination
name|dest
init|=
operator|new
name|AtmosphereWebSocketServletDestination
argument_list|(
name|bus
argument_list|,
name|registry
argument_list|,
name|endpoint
argument_list|,
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AtmosphereInterceptor
argument_list|>
name|ais
init|=
name|dest
operator|.
name|getAtmosphereFramework
argument_list|()
operator|.
name|interceptors
argument_list|()
decl_stmt|;
name|int
name|added
init|=
literal|0
decl_stmt|;
for|for
control|(
name|AtmosphereInterceptor
name|a
range|:
name|ais
control|)
block|{
if|if
condition|(
name|CustomInterceptor1
operator|.
name|class
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|added
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CustomInterceptor2
operator|.
name|class
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|added
operator|++
expr_stmt|;
break|break;
block|}
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|added
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|CustomInterceptor1
extends|extends
name|DefaultProtocolInterceptor
block|{     }
specifier|private
specifier|static
class|class
name|CustomInterceptor2
extends|extends
name|DefaultProtocolInterceptor
block|{     }
block|}
end_class

end_unit

