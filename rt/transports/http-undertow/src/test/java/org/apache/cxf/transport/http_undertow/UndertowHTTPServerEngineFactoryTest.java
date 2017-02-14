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
name|http_undertow
package|;
end_package

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
name|BusFactory
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
name|spring
operator|.
name|SpringBusFactory
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
name|HTTPTransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|BeforeClass
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

begin_class
specifier|public
class|class
name|UndertowHTTPServerEngineFactoryTest
extends|extends
name|Assert
block|{
name|Bus
name|bus
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|classUp
parameter_list|()
block|{
comment|// Get rid of any notion of a default bus set by other
comment|// rogue tests.
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|classDown
parameter_list|()
block|{
comment|// Clean up.
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * This test makes sure that a default Spring initialized bus will      * have the UndertowHTTPServerEngineFactory (absent of<httpu:engine-factory>      * configuration.      */
annotation|@
name|Test
specifier|public
name|void
name|testMakeSureTransportFactoryHasEngineFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Cannot get bus"
argument_list|,
name|bus
argument_list|)
expr_stmt|;
comment|// Make sure we got the Transport Factory.
name|DestinationFactoryManager
name|destFM
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
name|assertNotNull
argument_list|(
literal|"Cannot get DestinationFactoryManager"
argument_list|,
name|destFM
argument_list|)
expr_stmt|;
name|DestinationFactory
name|destF
init|=
name|destFM
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://cxf.apache.org/transports/http"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No DestinationFactory"
argument_list|,
name|destF
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|HTTPTransportFactory
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|destF
argument_list|)
argument_list|)
expr_stmt|;
comment|// And the UndertowHTTPServerEngineFactory should be there.
name|UndertowHTTPServerEngineFactory
name|factory
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
name|assertNotNull
argument_list|(
literal|"EngineFactory is not configured."
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test makes sure that with a<httpu:engine-factory bus="cxf">      * that the bus is configured with the rightly configured Undertow      * HTTP Server Engine Factory.  Port 1234 should have be configured      * for TLS.      */
annotation|@
name|Test
specifier|public
name|void
name|testMakeSureTransportFactoryHasEngineFactoryConfigured
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This file configures the factory to configure
comment|// port 1234 with default TLS.
name|URL
name|config
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"server-engine-factory.xml"
argument_list|)
decl_stmt|;
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|config
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
name|factory
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
name|assertNotNull
argument_list|(
literal|"EngineFactory is not configured."
argument_list|,
name|factory
argument_list|)
expr_stmt|;
comment|// The Engine for port 1234 should be configured for TLS.
comment|// This will throw an error if it is not.
name|UndertowHTTPServerEngine
name|engine
init|=
literal|null
decl_stmt|;
name|engine
operator|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
literal|1234
argument_list|,
literal|"https"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Engine is not available."
argument_list|,
name|engine
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1234
argument_list|,
name|engine
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Not https"
argument_list|,
literal|"https"
argument_list|,
name|engine
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|engine
operator|=
name|factory
operator|.
name|createUndertowHTTPServerEngine
argument_list|(
literal|1234
argument_list|,
literal|"http"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"The engine's protocol should be https"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expect the exception
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAnInvalidConfiguresfile
parameter_list|()
block|{
comment|// This file configures the factory to configure
comment|// port 1234 with default TLS.
name|URL
name|config
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"invalid-engines.xml"
argument_list|)
decl_stmt|;
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|UndertowHTTPServerEngineFactory
name|factory
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
name|assertNotNull
argument_list|(
literal|"EngineFactory is not configured."
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

