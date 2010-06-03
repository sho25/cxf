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
name|jaxws
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
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
name|jaxws
operator|.
name|ServerMixedStyle
operator|.
name|MixedTest
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
name|AbstractClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_mixedstyle
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
name|hello_world_mixedstyle
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
name|hello_world_mixedstyle
operator|.
name|types
operator|.
name|GreetMe1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_mixedstyle
operator|.
name|types
operator|.
name|GreetMeResponse
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
name|ClientServerMixedStyleTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|ServerMixedStyle
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_mixedstyle"
argument_list|,
literal|"SoapPort"
argument_list|)
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
name|ServerMixedStyle
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
name|testMixedStyle
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
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
name|PORT
argument_list|)
expr_stmt|;
name|GreetMe1
name|request
init|=
operator|new
name|GreetMe1
argument_list|()
decl_stmt|;
name|request
operator|.
name|setRequestType
argument_list|(
literal|"Bonjour"
argument_list|)
expr_stmt|;
name|GreetMeResponse
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
name|request
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
literal|"Hello Bonjour"
argument_list|,
name|greeting
operator|.
name|getResponseType
argument_list|()
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
literal|"Bonjour"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"expected exception not caught"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|hello_world_mixedstyle
operator|.
name|PingMeFault
name|f
parameter_list|)
block|{
comment|//ignore, expected
block|}
block|}
catch|catch
parameter_list|(
name|UndeclaredThrowableException
name|ex
parameter_list|)
block|{
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
name|testCXF885
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|serv
init|=
name|Service
operator|.
name|create
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://example.com"
argument_list|,
literal|"MixedTest"
argument_list|)
argument_list|)
decl_stmt|;
name|MixedTest
name|test
init|=
name|serv
operator|.
name|getPort
argument_list|(
name|MixedTest
operator|.
name|class
argument_list|)
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|test
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
literal|"/cxf885"
argument_list|)
expr_stmt|;
name|String
name|ret
init|=
name|test
operator|.
name|hello
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello A and B"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
name|String
name|ret2
init|=
name|test
operator|.
name|simple
argument_list|(
literal|"Dan"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Dan"
argument_list|,
name|ret2
argument_list|)
expr_stmt|;
name|String
name|ret3
init|=
name|test
operator|.
name|tripple
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Tripple: A B C"
argument_list|,
name|ret3
argument_list|)
expr_stmt|;
name|String
name|ret4
init|=
name|test
operator|.
name|simple2
argument_list|(
literal|24
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Int: 24"
argument_list|,
name|ret4
argument_list|)
expr_stmt|;
name|serv
operator|=
name|Service
operator|.
name|create
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/cxf885?wsdl"
argument_list|)
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://example.com"
argument_list|,
literal|"MixedTestImplService"
argument_list|)
argument_list|)
expr_stmt|;
name|test
operator|=
name|serv
operator|.
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://example.com"
argument_list|,
literal|"MixedTestImplPort"
argument_list|)
argument_list|,
name|MixedTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|ret
operator|=
name|test
operator|.
name|hello
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello A and B"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
name|ret2
operator|=
name|test
operator|.
name|simple
argument_list|(
literal|"Dan"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Dan"
argument_list|,
name|ret2
argument_list|)
expr_stmt|;
name|ret3
operator|=
name|test
operator|.
name|tripple
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Tripple: A B C"
argument_list|,
name|ret3
argument_list|)
expr_stmt|;
name|ret4
operator|=
name|test
operator|.
name|simple2
argument_list|(
literal|24
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Int: 24"
argument_list|,
name|ret4
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

