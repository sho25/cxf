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
name|cxf2006
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
name|RespectBindingFeature
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
name|testutil
operator|.
name|common
operator|.
name|ServerLauncher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|GreeterRPCLit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_rpclit
operator|.
name|SOAPServiceRPCLit
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|RespectBindingFeatureClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortRPCLit"
argument_list|)
decl_stmt|;
specifier|private
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
argument_list|()
decl_stmt|;
specifier|private
name|ServerLauncher
name|serverLauncher
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
literal|null
operator|!=
name|serverLauncher
condition|)
block|{
name|serverLauncher
operator|.
name|signalStop
argument_list|()
expr_stmt|;
name|serverLauncher
operator|.
name|stopServer
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|startServers
parameter_list|(
name|String
name|wsdlLocation
parameter_list|)
throws|throws
name|Exception
block|{
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
name|wsdlLocation
block|}
decl_stmt|;
name|serverLauncher
operator|=
operator|new
name|ServerLauncher
argument_list|(
name|Server
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|,
name|args
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|boolean
name|isServerReady
init|=
name|serverLauncher
operator|.
name|launchServer
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|isServerReady
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRespectBindingFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|startServers
argument_list|(
literal|"/wsdl_systest/cxf2006.wsdl"
argument_list|)
expr_stmt|;
try|try
block|{
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|,
operator|new
name|RespectBindingFeature
argument_list|(
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebServiceException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"WebServiceException is expected"
argument_list|,
name|ex
operator|instanceof
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"RespectBindingFeature message is expected: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"extension with required=true attribute"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOperationRespectBindingFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|startServers
argument_list|(
literal|"/wsdl_systest/cxf_operation_respectbing.wsdl"
argument_list|)
expr_stmt|;
try|try
block|{
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|,
operator|new
name|RespectBindingFeature
argument_list|(
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebServiceException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"WebServiceException is expected"
argument_list|,
name|ex
operator|instanceof
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"RespectBindingFeature message is expected: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"extension with required=true attribute"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOperationInputRespectBindingFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|startServers
argument_list|(
literal|"/wsdl_systest/cxf_operation_input_respectbing.wsdl"
argument_list|)
expr_stmt|;
try|try
block|{
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|,
operator|new
name|RespectBindingFeature
argument_list|(
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebServiceException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"WebServiceException is expected"
argument_list|,
name|ex
operator|instanceof
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"RespectBindingFeature message is expected: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"extension with required=true attribute"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOperationOutputRespectBindingFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|startServers
argument_list|(
literal|"/wsdl_systest/cxf_operation_output_respectbing.wsdl"
argument_list|)
expr_stmt|;
try|try
block|{
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|,
operator|new
name|RespectBindingFeature
argument_list|(
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebServiceException is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"WebServiceException is expected"
argument_list|,
name|ex
operator|instanceof
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"RespectBindingFeature message is expected: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"extension with required=true attribute"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRespectBindingFeatureFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|startServers
argument_list|(
literal|"/wsdl_systest/cxf2006.wsdl"
argument_list|)
expr_stmt|;
name|GreeterRPCLit
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|GreeterRPCLit
operator|.
name|class
argument_list|,
operator|new
name|RespectBindingFeature
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour"
argument_list|,
name|greeter
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

