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
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|transform
operator|.
name|Source
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
name|stream
operator|.
name|StreamSource
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
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
name|ClientImpl
operator|.
name|IllegalEmptyResponseException
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
name|LoggingFeature
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
name|hello_world_xml_http
operator|.
name|bare
operator|.
name|XMLService
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

begin_comment
comment|/**  * Test what happens when we make an invocation and get back an empty SOAP Body (see CXF-7653)  */
end_comment

begin_class
specifier|public
class|class
name|EmptySOAPBodyTest
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
name|EmptySOAPBodyServer
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
name|EmptySOAPBodyServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
name|launchServer
argument_list|(
name|EmptySoapProviderServer
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
name|testPlaintext
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
name|EmptySOAPBodyTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
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
name|URL
name|wsdl
init|=
name|EmptySOAPBodyTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"DoubleIt.wsdl"
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
literal|"DoubleItPlaintextPort"
argument_list|)
decl_stmt|;
name|DoubleItPortType
name|port
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
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|doubleIt
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|t
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Wrong exception cause "
operator|+
name|t
operator|.
name|getCause
argument_list|()
argument_list|,
name|t
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalEmptyResponseException
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
name|port
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
name|testProviderSource
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|providerServiceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/bare"
argument_list|,
literal|"HelloProviderService"
argument_list|)
decl_stmt|;
name|QName
name|providerPortName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_xml_http/bare"
argument_list|,
literal|"HelloProviderPort"
argument_list|)
decl_stmt|;
name|URL
name|wsdl
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|EmptySoapProviderServer
operator|.
name|REG_PORT
operator|+
literal|"/helloProvider/helloPort?wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|XMLService
name|service
init|=
operator|new
name|XMLService
argument_list|(
name|wsdl
argument_list|,
name|providerServiceName
argument_list|,
operator|new
name|LoggingFeature
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|dispatch
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|providerPortName
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|String
name|str
init|=
operator|new
name|String
argument_list|(
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>"
operator|+
literal|"<ns2:in xmlns=\"http://apache.org/hello_world_xml_http/bare/types\""
operator|+
literal|" xmlns:ns2=\"http://apache.org/hello_world_xml_http/bare\">"
operator|+
literal|"<elem1>empty</elem1><elem2>this is element 2</elem2><elem3>42</elem3></ns2:in>"
operator|+
literal|"</soap:Body></soap:Envelope>"
argument_list|)
decl_stmt|;
name|StreamSource
name|req
init|=
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|str
argument_list|)
argument_list|)
decl_stmt|;
name|Source
name|resSource
init|=
name|dispatch
operator|.
name|invoke
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
literal|"null result is expected"
argument_list|,
name|resSource
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

