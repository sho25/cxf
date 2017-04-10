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
name|provider
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
name|soap
operator|.
name|AttachmentPart
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
name|SOAPConnection
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
name|SOAPConnectionFactory
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
name|SOAPFactory
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
name|ProviderRPCClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
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
annotation|@
name|Test
specifier|public
name|void
name|testSWA
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPFactory
name|soapFac
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|MessageFactory
name|msgFac
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|SOAPConnectionFactory
name|conFac
init|=
name|SOAPConnectionFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|SOAPMessage
name|msg
init|=
name|msgFac
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|QName
name|sayHi
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sayHiWAttach"
argument_list|)
decl_stmt|;
name|msg
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|addChildElement
argument_list|(
name|soapFac
operator|.
name|createElement
argument_list|(
name|sayHi
argument_list|)
argument_list|)
expr_stmt|;
name|AttachmentPart
name|ap1
init|=
name|msg
operator|.
name|createAttachmentPart
argument_list|()
decl_stmt|;
name|ap1
operator|.
name|setContent
argument_list|(
literal|"Attachment content"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|addAttachmentPart
argument_list|(
name|ap1
argument_list|)
expr_stmt|;
name|AttachmentPart
name|ap2
init|=
name|msg
operator|.
name|createAttachmentPart
argument_list|()
decl_stmt|;
name|ap2
operator|.
name|setContent
argument_list|(
literal|"Attachment content - Part 2"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|addAttachmentPart
argument_list|(
name|ap2
argument_list|)
expr_stmt|;
name|msg
operator|.
name|saveChanges
argument_list|()
expr_stmt|;
name|SOAPConnection
name|con
init|=
name|conFac
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|URL
name|endpoint
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit1"
argument_list|)
decl_stmt|;
name|SOAPMessage
name|response
init|=
name|con
operator|.
name|call
argument_list|(
name|msg
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|QName
name|sayHiResp
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"sayHiResponse"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|getChildElements
argument_list|(
name|sayHiResp
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|response
operator|.
name|countAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doGreeterRPCLit
parameter_list|(
name|SOAPServiceRPCLit
name|service
parameter_list|,
name|QName
name|portName
parameter_list|,
name|int
name|count
parameter_list|,
name|boolean
name|doFault
parameter_list|)
throws|throws
name|Exception
block|{
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
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|count
condition|;
name|idx
operator|++
control|)
block|{
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Milestone-"
operator|+
name|idx
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
if|if
condition|(
name|doFault
condition|)
block|{
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"throwFault"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPFaultException
name|ex
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|ex
operator|.
name|getFault
argument_list|()
operator|.
name|getDetail
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getFault
argument_list|()
operator|.
name|getDetail
argument_list|()
operator|.
name|getDetailEntries
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
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
name|testSOAPMessageModeRPC
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit1"
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
literal|"TestGreetMeResponseServerLogicalHandlerServerSOAPHandler"
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
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Milestone-0"
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
annotation|@
name|Test
specifier|public
name|void
name|testSOAPMessageModeWithDOMSourceData
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit2"
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
name|doGreeterRPCLit
argument_list|(
name|service
argument_list|,
name|portName
argument_list|,
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPayloadModeWithDOMSourceData
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit3"
argument_list|)
decl_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
name|doGreeterRPCLit
argument_list|(
name|service
argument_list|,
name|portName
argument_list|,
literal|1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPayloadModeWithSourceData
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit8"
argument_list|)
decl_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
name|addresses
index|[]
init|=
block|{
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8"
block|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-dom"
block|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-sax"
block|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-cxfstax"
block|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-stax"
block|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-stream"
block|}
decl_stmt|;
name|String
name|response1
init|=
operator|new
name|String
argument_list|(
literal|"TestGreetMeResponseServerLogicalHandlerServerSOAPHandler"
argument_list|)
decl_stmt|;
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
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|ad
range|:
name|addresses
control|)
block|{
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
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
name|ad
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"Milestone-0"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service "
operator|+
name|ad
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"wrong response received from service "
operator|+
name|ad
argument_list|,
name|response1
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageModeWithSAXSourceData
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit4"
argument_list|)
decl_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
name|doGreeterRPCLit
argument_list|(
name|service
argument_list|,
name|portName
argument_list|,
literal|1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMessageModeWithStreamSourceData
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit5"
argument_list|)
decl_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
name|doGreeterRPCLit
argument_list|(
name|service
argument_list|,
name|portName
argument_list|,
literal|1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPayloadModeWithSAXSourceData
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
literal|"/wsdl/hello_world_rpc_lit.wsdl"
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
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SOAPServiceProviderRPCLit"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_rpclit"
argument_list|,
literal|"SoapPortProviderRPCLit6"
argument_list|)
decl_stmt|;
name|SOAPServiceRPCLit
name|service
init|=
operator|new
name|SOAPServiceRPCLit
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
name|doGreeterRPCLit
argument_list|(
name|service
argument_list|,
name|portName
argument_list|,
literal|1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

