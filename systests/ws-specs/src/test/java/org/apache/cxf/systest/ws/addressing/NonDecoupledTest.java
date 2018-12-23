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
name|addressing
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests the addition of WS-Addressing Message Addressing Properties  * in the non-decoupled case.  */
end_comment

begin_class
specifier|public
class|class
name|NonDecoupledTest
extends|extends
name|MAPTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|NonDecoupledTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONFIG
init|=
literal|"org/apache/cxf/systest/ws/addressing/wsa_interceptors.xml"
decl_stmt|;
specifier|public
name|String
name|getConfigFileName
parameter_list|()
block|{
return|return
name|CONFIG
return|;
block|}
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|PORT
return|;
block|}
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
comment|// special case handling for WS-Addressing system test to avoid
comment|// UUID related issue when server is run as separate process
comment|// via maven on Win2k
name|boolean
name|inProcess
init|=
literal|"Windows 2000"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
name|ADDRESS
block|,
name|GreeterImpl
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
argument_list|,
name|inProcess
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"SOAPServiceAddressing"
argument_list|,
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_soap_http.Greeter"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/hello_world.wsdl"
argument_list|)
specifier|public
specifier|static
class|class
name|GreeterImpl
extends|extends
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
name|addressing
operator|.
name|AbstractGreeterImpl
block|{      }
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|ADDRESS
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|foo
parameter_list|()
block|{      }
block|}
end_class

end_unit

