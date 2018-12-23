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
comment|/**  * Tests the addition of WS-Addressing Message Addressing Properties.  */
end_comment

begin_class
specifier|public
class|class
name|MAPTest
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
name|MAPTest
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
decl_stmt|;
static|static
block|{
name|CONFIG
operator|=
literal|"org/apache/cxf/systest/ws/addressing/cxf"
operator|+
operator|(
operator|(
literal|"HP-UX"
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
operator|||
literal|"Windows XP"
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
operator|)
condition|?
literal|"-hpux"
else|:
literal|""
operator|)
operator|+
literal|".xml"
expr_stmt|;
block|}
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
name|getAddress
parameter_list|()
block|{
return|return
name|ADDRESS
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
literal|true
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
block|{
specifier|public
name|GreeterImpl
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

