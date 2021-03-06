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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|soap
operator|.
name|AddressingFeature
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
name|systest
operator|.
name|ws
operator|.
name|addr_feature
operator|.
name|FaultToEndpointServer
operator|.
name|HelloHandler
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|AddressingProperties
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
name|AttributedURIType
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
name|EndpointReferenceType
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
name|JAXWSAConstants
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|WSAFaultToClientServerTest
extends|extends
name|AbstractWSATestBase
block|{
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
literal|"FaultTo server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|FaultToEndpointServer
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
name|testOneWayFaultTo
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
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPServiceAddressing"
argument_list|)
decl_stmt|;
name|Greeter
name|greeter
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
operator|.
name|getPort
argument_list|(
name|Greeter
operator|.
name|class
argument_list|,
operator|new
name|AddressingFeature
argument_list|()
argument_list|)
decl_stmt|;
name|EndpointReferenceType
name|faultTo
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|AddressingProperties
name|addrProperties
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|AttributedURIType
name|epr
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|String
name|faultToAddress
init|=
literal|"http://localhost:"
operator|+
name|FaultToEndpointServer
operator|.
name|FAULT_PORT
operator|+
literal|"/faultTo"
decl_stmt|;
name|epr
operator|.
name|setValue
argument_list|(
name|faultToAddress
argument_list|)
expr_stmt|;
name|faultTo
operator|.
name|setAddress
argument_list|(
name|epr
argument_list|)
expr_stmt|;
name|addrProperties
operator|.
name|setFaultTo
argument_list|(
name|faultTo
argument_list|)
expr_stmt|;
name|BindingProvider
name|provider
init|=
operator|(
name|BindingProvider
operator|)
name|greeter
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|provider
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|FaultToEndpointServer
operator|.
name|PORT
operator|+
literal|"/jaxws/greeter"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|,
name|addrProperties
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
comment|//wait for the fault request
name|int
name|i
init|=
literal|2
decl_stmt|;
while|while
condition|(
name|HelloHandler
operator|.
name|getFaultRequestPath
argument_list|()
operator|==
literal|null
operator|&&
name|i
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|i
operator|--
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"FaultTo request fpath isn't expected"
argument_list|,
literal|"/faultTo"
operator|.
name|equals
argument_list|(
name|HelloHandler
operator|.
name|getFaultRequestPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayFaultTo
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
name|AddNumbersPortType
name|port
init|=
name|getTwoWayPort
argument_list|()
decl_stmt|;
comment|//setup a real decoupled endpoint that will process the fault correctly
name|HTTPConduit
name|c
init|=
operator|(
name|HTTPConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|setDecoupledEndpoint
argument_list|(
literal|"http://localhost:"
operator|+
name|FaultToEndpointServer
operator|.
name|FAULT_PORT2
operator|+
literal|"/sendFaultHere"
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|faultTo
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|AddressingProperties
name|addrProperties
init|=
operator|new
name|AddressingProperties
argument_list|()
decl_stmt|;
name|AttributedURIType
name|epr
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|epr
operator|.
name|setValue
argument_list|(
literal|"http://localhost:"
operator|+
name|FaultToEndpointServer
operator|.
name|FAULT_PORT2
operator|+
literal|"/sendFaultHere"
argument_list|)
expr_stmt|;
name|faultTo
operator|.
name|setAddress
argument_list|(
name|epr
argument_list|)
expr_stmt|;
name|addrProperties
operator|.
name|setFaultTo
argument_list|(
name|faultTo
argument_list|)
expr_stmt|;
name|BindingProvider
name|provider
init|=
operator|(
name|BindingProvider
operator|)
name|port
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|provider
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|FaultToEndpointServer
operator|.
name|PORT
operator|+
literal|"/jaxws/add"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
argument_list|,
name|addrProperties
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|addNumbers
argument_list|(
operator|-
literal|1
argument_list|,
operator|-
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//do nothing
block|}
name|String
name|in
init|=
operator|new
name|String
argument_list|(
name|input
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
comment|//System.out.println(in);
name|assertTrue
argument_list|(
literal|"The response from faultTo endpoint is expected and actual response is "
operator|+
name|in
argument_list|,
name|in
operator|.
name|indexOf
argument_list|(
literal|"Address: http://localhost:"
operator|+
name|FaultToEndpointServer
operator|.
name|FAULT_PORT2
operator|+
literal|"/sendFaultHere"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"WS addressing header is expected"
argument_list|,
name|in
operator|.
name|indexOf
argument_list|(
literal|"http://www.w3.org/2005/08/addressing"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Fault deatil is expected"
argument_list|,
name|in
operator|.
name|indexOf
argument_list|(
literal|"Negative numbers cant be added"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Closeable
operator|)
name|port
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|AddNumbersPortType
name|getTwoWayPort
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
name|getAddNumbersPort
argument_list|(
operator|new
name|AddressingFeature
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

