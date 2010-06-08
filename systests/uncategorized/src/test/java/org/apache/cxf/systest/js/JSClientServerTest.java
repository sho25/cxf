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
name|js
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|UndeclaredThrowableException
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
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|SOAPServiceTest1
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
name|JSClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JS_PORT
init|=
name|Server
operator|.
name|JS_PORT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JSX_PORT
init|=
name|Server
operator|.
name|JSX_PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NS
init|=
literal|"http://apache.org/hello_world_soap_http"
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
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJSMessageMode
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
name|NS
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
name|NS
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|response1
init|=
operator|new
name|String
argument_list|(
literal|"TestGreetMeResponse"
argument_list|)
decl_stmt|;
name|String
name|response2
init|=
operator|new
name|String
argument_list|(
literal|"TestSayHiResponse"
argument_list|)
decl_stmt|;
try|try
block|{
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|JS_PORT
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"TestGreetMeRequest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response1
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response2
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJSPayloadMode
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
name|NS
argument_list|,
literal|"SOAPService_Test1"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
name|NS
argument_list|,
literal|"SoapPort_Test1"
argument_list|)
decl_stmt|;
name|SOAPServiceTest1
name|service
init|=
operator|new
name|SOAPServiceTest1
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|String
name|response1
init|=
operator|new
name|String
argument_list|(
literal|"TestGreetMeResponse"
argument_list|)
decl_stmt|;
name|String
name|response2
init|=
operator|new
name|String
argument_list|(
literal|"TestSayHiResponse"
argument_list|)
decl_stmt|;
try|try
block|{
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|JSX_PORT
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"TestGreetMeRequest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response1
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response2
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|(
name|Exception
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

