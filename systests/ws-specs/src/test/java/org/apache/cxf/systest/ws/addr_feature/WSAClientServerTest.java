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
name|addr_feature
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|AddressingFeature
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
name|helpers
operator|.
name|IOUtils
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
name|systest
operator|.
name|ws
operator|.
name|AbstractWSATestBase
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
name|addressing
operator|.
name|WSAddressingFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|WSAClientServerTest
extends|extends
name|AbstractWSATestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|Server
operator|.
name|PORT2
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|,
literal|"AddNumbersService"
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|createBus
argument_list|()
expr_stmt|;
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
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoWsaFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|AddNumbersPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxws/add"
argument_list|)
expr_stmt|;
name|AddNumbersPortType
name|port
init|=
operator|(
name|AddNumbersPortType
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expectedOut
init|=
literal|"<Address>http://www.w3.org/2005/08/addressing/anonymous</Address>"
decl_stmt|;
name|String
name|expectedIn
init|=
literal|"<RelatesTo xmlns=\"http://www.w3.org/2005/08/addressing\">"
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedOut
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedIn
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCxfWsaFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|AddNumbersPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/jaxws/add"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|WSAddressingFeature
argument_list|()
argument_list|)
expr_stmt|;
name|AddNumbersPortType
name|port
init|=
operator|(
name|AddNumbersPortType
operator|)
name|factory
operator|.
name|create
argument_list|()
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
literal|"ws-addressing.write.optional.replyto"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expectedOut
init|=
literal|"<Address>http://www.w3.org/2005/08/addressing/anonymous</Address>"
decl_stmt|;
name|String
name|expectedIn
init|=
literal|"<RelatesTo xmlns=\"http://www.w3.org/2005/08/addressing\">"
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedOut
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedIn
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxwsWsaFeature
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|input
init|=
name|setupInLogging
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
name|setupOutLogging
argument_list|()
decl_stmt|;
name|AddNumbersPortType
name|port
init|=
name|getPort
argument_list|()
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
literal|"ws-addressing.write.optional.replyto"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expectedOut
init|=
literal|"<Address>http://www.w3.org/2005/08/addressing/anonymous</Address>"
decl_stmt|;
name|String
name|expectedIn
init|=
literal|"<RelatesTo xmlns=\"http://www.w3.org/2005/08/addressing\">"
decl_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedOut
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
name|expectedIn
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|//CXF-3456
annotation|@
name|Test
specifier|public
name|void
name|testDuplicateHeaders
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
literal|"/wsdl_systest_wsspec/add_numbers.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|AddNumbersService
name|service
init|=
operator|new
name|AddNumbersService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|,
literal|"AddNumbersPort"
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|SOAPMessage
argument_list|>
name|disp
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|SOAPMessage
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
argument_list|,
operator|new
name|AddressingFeature
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
decl_stmt|;
name|disp
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
literal|"/jaxws/add"
argument_list|)
expr_stmt|;
name|InputStream
name|msgIns
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./duplicate-wsa-header-msg.xml"
argument_list|)
decl_stmt|;
name|String
name|msg
init|=
operator|new
name|String
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|msgIns
argument_list|)
argument_list|)
decl_stmt|;
name|msg
operator|=
name|msg
operator|.
name|replaceAll
argument_list|(
literal|"$PORT"
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bout
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|msg
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|SOAPMessage
name|soapReqMsg
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|bout
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|soapReqMsg
argument_list|)
expr_stmt|;
try|try
block|{
name|disp
operator|.
name|invoke
argument_list|(
name|soapReqMsg
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"SOAPFaultFxception is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"WSA header exception is expected"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"A header representing a Message Addressing"
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
name|testNonAnonSoap12Fault
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|AddNumbersPortType
name|port
init|=
name|getNonAnonPort
argument_list|()
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
name|PORT2
operator|+
literal|"/jaxws/soap12/add"
argument_list|)
expr_stmt|;
name|port
operator|.
name|addNumbers
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"expected non-anonymous required message"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Found anonymous address but non-anonymous required"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected sender faultCode"
argument_list|,
name|e
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultCode
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Sender"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected OnlyNonAnonymousAddressSupported fault subcode"
argument_list|,
name|e
operator|.
name|getFault
argument_list|()
operator|.
name|getFaultSubcodes
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"{http://www.w3.org/2005/08/addressing}OnlyNonAnonymousAddressSupported"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|AddNumbersPortType
name|getNonAnonPort
parameter_list|()
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest_soap12/add_numbers_soap12.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|AddNumbersService
name|service
init|=
operator|new
name|AddNumbersService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
return|return
name|service
operator|.
name|getAddNumbersNonAnonPort
argument_list|(
operator|new
name|AddressingFeature
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|AddNumbersPortType
name|getPort
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
literal|"/wsdl_systest_wsspec/add_numbers.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|AddNumbersService
name|service
init|=
operator|new
name|AddNumbersService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null "
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|AddNumbersPortType
name|port
init|=
name|service
operator|.
name|getAddNumbersPort
argument_list|(
operator|new
name|AddressingFeature
argument_list|()
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|port
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
return|return
name|port
return|;
block|}
block|}
end_class

end_unit

