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
name|fault
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
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|Dispatch
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
name|endpoint
operator|.
name|Client
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
name|DispatchImpl
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
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|example
operator|.
name|contract
operator|.
name|doubleit
operator|.
name|DoubleItPortType
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

begin_comment
comment|/**  * A set of tests for (signing and encrypting) SOAP Faults.  */
end_comment

begin_class
specifier|public
class|class
name|FaultTest
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
name|NAMESPACE
init|=
literal|"http://www.example.org/contract/DoubleIt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_QNAME
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItService"
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
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSoap11
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|FaultTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|FaultTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItSoap11Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|utPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|utPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|utPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|utPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// Now make an invocation using another username
operator|(
operator|(
name|BindingProvider
operator|)
name|utPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|utPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
try|try
block|{
name|utPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on bob"
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
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"This is a fault"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|utPort
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSoap12
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|FaultTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client/client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|SpringBusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|SpringBusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|FaultTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItSoap12Port"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|utPort
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portQName
argument_list|,
name|DoubleItPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|utPort
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// Make a successful invocation
operator|(
operator|(
name|BindingProvider
operator|)
name|utPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|utPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
comment|// Now make an invocation using another username
operator|(
operator|(
name|BindingProvider
operator|)
name|utPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|utPort
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
try|try
block|{
name|utPort
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on bob"
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
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"This is a fault"
argument_list|)
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|utPort
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSoap12Dispatch
parameter_list|()
throws|throws
name|Exception
block|{
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|FaultTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleItFault.wsdl"
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_QNAME
argument_list|)
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|NAMESPACE
argument_list|,
literal|"DoubleItSoap12DispatchPort"
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispatch
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portQName
argument_list|,
name|DOMSource
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
comment|// Creating a DOMSource Object for the request
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Document
name|requestDoc
init|=
name|db
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|requestDoc
operator|.
name|createElementNS
argument_list|(
literal|"http://www.example.org/schema/DoubleIt"
argument_list|,
literal|"ns2:DoubleIt"
argument_list|)
decl_stmt|;
name|root
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:ns2"
argument_list|,
literal|"http://www.example.org/schema/DoubleIt"
argument_list|)
expr_stmt|;
name|Element
name|number
init|=
name|requestDoc
operator|.
name|createElementNS
argument_list|(
literal|null
argument_list|,
literal|"numberToDouble"
argument_list|)
decl_stmt|;
name|number
operator|.
name|setTextContent
argument_list|(
literal|"25"
argument_list|)
expr_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|number
argument_list|)
expr_stmt|;
name|requestDoc
operator|.
name|appendChild
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|DOMSource
name|request
init|=
operator|new
name|DOMSource
argument_list|(
name|requestDoc
argument_list|)
decl_stmt|;
comment|// Add WS-Security configuration
name|Client
name|client
init|=
operator|(
operator|(
name|DispatchImpl
argument_list|<
name|DOMSource
argument_list|>
operator|)
name|dispatch
operator|)
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.ws.common.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"bob.properties"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|dispatch
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|// Make a successful request
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|DOMSource
name|response
init|=
name|dispatch
operator|.
name|invoke
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// Now make an invocation using another username
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"ws-security.password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
try|try
block|{
name|dispatch
operator|.
name|invoke
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on bob"
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
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"This is a fault"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

