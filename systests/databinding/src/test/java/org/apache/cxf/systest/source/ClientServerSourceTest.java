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
name|source
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
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|ext
operator|.
name|logging
operator|.
name|LoggingOutInterceptor
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
name|frontend
operator|.
name|ClientProxy
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
name|helpers
operator|.
name|DOMUtils
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
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http_source
operator|.
name|source
operator|.
name|GreetMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http_source
operator|.
name|source
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
name|hello_world_soap_http_source
operator|.
name|source
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ClientServerSourceTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|WSDL_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http_source/source"
argument_list|,
literal|"SOAPService"
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
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Element
name|getElement
parameter_list|(
name|Node
name|nd
parameter_list|)
block|{
if|if
condition|(
name|nd
operator|instanceof
name|Document
condition|)
block|{
return|return
operator|(
operator|(
name|Document
operator|)
name|nd
operator|)
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
return|return
operator|(
name|Element
operator|)
name|nd
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCallFromClient
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/source/cxf.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest_databinding/source/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should have found the WSDL here. "
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|ss
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|ss
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|WSDL_PORT
argument_list|)
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://apache.org/hello_world_soap_http_source/source/types"
argument_list|,
literal|"ns1:sayHi"
argument_list|)
argument_list|)
expr_stmt|;
name|DOMSource
name|ds
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|DOMSource
name|resp
init|=
name|port
operator|.
name|sayHi
argument_list|(
name|ds
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right response"
argument_list|,
literal|"Bonjour"
argument_list|,
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|getElement
argument_list|(
name|resp
operator|.
name|getNode
argument_list|()
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getFirstChild
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
expr_stmt|;
name|Element
name|el
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://apache.org/hello_world_soap_http_source/source/types"
argument_list|,
literal|"ns1:greetMe"
argument_list|)
decl_stmt|;
name|Element
name|el2
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://apache.org/hello_world_soap_http_source/source/types"
argument_list|,
literal|"ns1:requestType"
argument_list|)
decl_stmt|;
name|el2
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
literal|"Willem"
argument_list|)
argument_list|)
expr_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|el2
argument_list|)
expr_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|ds
operator|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|resp
operator|=
name|port
operator|.
name|greetMe
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right response"
argument_list|,
literal|"Hello Willem"
argument_list|,
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|getElement
argument_list|(
name|resp
operator|.
name|getNode
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doc
operator|=
name|DOMUtils
operator|.
name|newDocument
argument_list|()
expr_stmt|;
name|el
operator|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://apache.org/hello_world_soap_http_source/source/types"
argument_list|,
literal|"ns1:greetMe"
argument_list|)
expr_stmt|;
name|el2
operator|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|"http://apache.org/hello_world_soap_http_source/source/types"
argument_list|,
literal|"ns1:requestType"
argument_list|)
expr_stmt|;
name|el2
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
literal|"fault"
argument_list|)
argument_list|)
expr_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|el2
argument_list|)
expr_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|ds
operator|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|port
operator|.
name|greetMe
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have been a fault"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GreetMeFault
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Some fault detail"
argument_list|,
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|getElement
argument_list|(
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getNode
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

