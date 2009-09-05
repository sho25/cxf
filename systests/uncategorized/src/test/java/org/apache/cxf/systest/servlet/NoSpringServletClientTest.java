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
name|servlet
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
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebConversation
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebLink
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|NoSpringServletClientTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|serviceURL
init|=
literal|"http://localhost:9000/soap/"
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
name|NoSpringServletServer
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
name|testBasicConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
operator|new
name|URL
argument_list|(
name|serviceURL
operator|+
literal|"Greeter?wsdl"
argument_list|)
argument_list|)
decl_stmt|;
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
try|try
block|{
name|String
name|reply
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"test"
argument_list|)
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
literal|"Hello test"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|reply
operator|=
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
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
name|testHelloService
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|cpfb
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|serviceURL
operator|+
literal|"Hello"
decl_stmt|;
name|cpfb
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|cpfb
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Hello
name|hello
init|=
operator|(
name|Hello
operator|)
name|cpfb
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|reply
init|=
name|hello
operator|.
name|sayHi
argument_list|(
literal|" Willem"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrongreply "
argument_list|,
name|reply
argument_list|,
literal|"get Willem"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetServiceList
parameter_list|()
throws|throws
name|Exception
block|{
name|WebConversation
name|client
init|=
operator|new
name|WebConversation
argument_list|()
decl_stmt|;
name|WebResponse
name|res
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|serviceURL
argument_list|)
decl_stmt|;
name|WebLink
index|[]
name|links
init|=
name|res
operator|.
name|getLinks
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There should get two links for the service"
argument_list|,
literal|2
argument_list|,
name|links
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|serviceURL
operator|+
literal|"Greeter?wsdl"
argument_list|,
name|links
index|[
literal|0
index|]
operator|.
name|getURLString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/html"
argument_list|,
name|res
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

