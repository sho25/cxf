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
name|systest
operator|.
name|ws
operator|.
name|wssc
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|systest
operator|.
name|ws
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|systest
operator|.
name|ws
operator|.
name|wssc
operator|.
name|server
operator|.
name|Server
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|WSSCTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUT
init|=
literal|"CXF : ping"
decl_stmt|;
specifier|private
specifier|static
name|wssec
operator|.
name|wssc
operator|.
name|PingService
name|svc
decl_stmt|;
specifier|private
specifier|static
name|Bus
name|bus
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/wssc/client/client.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svc
operator|=
operator|new
name|wssec
operator|.
name|wssc
operator|.
name|PingService
argument_list|()
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSecureConversationMutualCertificate10SignEncryptIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"SecureConversation_MutualCertificate10SignEncrypt_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testACIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"AC_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testADCIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"ADC_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testADCESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"ADC-ES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_A_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testADIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_AD_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testADESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_AD-ES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUXCIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"UXC_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUXDCIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"UXDC_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUXDCSEESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"UXDC-SEES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUXIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_UX_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUXDIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_UXD_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUXDSEESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_UXD-SEES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXCIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"XC_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDCIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"XDC_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDCIPingService1
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"XDC_IPingService1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDCESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"XDC-ES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDCSEESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"XDC-SEES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_X_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testX10IPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_X10_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_XD_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDSEESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_XD-SEES_IPingService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXDESIPingService
parameter_list|()
throws|throws
name|Exception
block|{
name|runTest
argument_list|(
literal|"_XD-ES_IPingService"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runTest
parameter_list|(
name|String
modifier|...
name|argv
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|String
name|portPrefix
range|:
name|argv
control|)
block|{
specifier|final
name|wssec
operator|.
name|wssc
operator|.
name|IPingService
name|port
init|=
name|svc
operator|.
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://WSSec/wssc"
argument_list|,
name|portPrefix
argument_list|)
argument_list|,
name|wssec
operator|.
name|wssc
operator|.
name|IPingService
operator|.
name|class
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
operator|+
name|portPrefix
argument_list|)
expr_stmt|;
if|if
condition|(
name|portPrefix
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'_'
condition|)
block|{
comment|//MS would like the _ versions to send a cancel
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_DO_CANCEL
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|wssec
operator|.
name|wssc
operator|.
name|PingRequest
name|params
init|=
operator|new
name|wssec
operator|.
name|wssc
operator|.
name|PingRequest
argument_list|()
decl_stmt|;
name|org
operator|.
name|xmlsoap
operator|.
name|ping
operator|.
name|Ping
name|ping
init|=
operator|new
name|org
operator|.
name|xmlsoap
operator|.
name|ping
operator|.
name|Ping
argument_list|()
decl_stmt|;
name|ping
operator|.
name|setOrigin
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|ping
operator|.
name|setScenario
argument_list|(
literal|"Scenario5"
argument_list|)
expr_stmt|;
name|ping
operator|.
name|setText
argument_list|(
literal|"ping"
argument_list|)
expr_stmt|;
name|params
operator|.
name|setPing
argument_list|(
name|ping
argument_list|)
expr_stmt|;
try|try
block|{
name|wssec
operator|.
name|wssc
operator|.
name|PingResponse
name|output
init|=
name|port
operator|.
name|ping
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|OUT
argument_list|,
name|output
operator|.
name|getPingResponse
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error doing "
operator|+
name|portPrefix
argument_list|,
name|ex
argument_list|)
throw|;
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

