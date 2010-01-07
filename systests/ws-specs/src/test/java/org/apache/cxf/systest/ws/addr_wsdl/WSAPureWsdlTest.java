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
name|addr_wsdl
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
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
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
name|Response
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
operator|.
name|Mode
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
name|handler
operator|.
name|MessageContext
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
name|interceptor
operator|.
name|Interceptor
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
name|message
operator|.
name|Message
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
name|AddNumbersPortType
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
name|AddNumbersResponse
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
name|AddNumbersService
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
name|soap
operator|.
name|MAPCodec
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
name|WSAPureWsdlTest
extends|extends
name|AbstractWSATestBase
block|{
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
name|testBasicInvocation
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
name|Response
argument_list|<
name|AddNumbersResponse
argument_list|>
name|resp
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
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:9094/jaxws/add"
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
name|base
init|=
literal|"http://apache.org/cxf/systest/ws/addr_feature/AddNumbersPortType/"
decl_stmt|;
name|String
name|expectedOut
init|=
name|base
operator|+
literal|"addNumbersRequest</Action>"
decl_stmt|;
name|String
name|expectedIn
init|=
name|base
operator|+
literal|"addNumbersResponse</Action>"
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
name|resp
operator|=
name|port
operator|.
name|addNumbers3Async
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|resp
operator|.
name|get
argument_list|()
operator|.
name|getReturn
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"http://localhost:9094/doesntexist"
argument_list|)
expr_stmt|;
name|resp
operator|=
name|port
operator|.
name|addNumbers3Async
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|resp
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IOException
argument_list|)
expr_stmt|;
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|port
argument_list|)
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|m
range|:
name|c
operator|.
name|getOutInterceptors
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|instanceof
name|MAPCodec
condition|)
block|{
name|assertTrue
argument_list|(
operator|(
operator|(
name|MAPCodec
operator|)
name|m
operator|)
operator|.
name|getUncorrelatedExchanges
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProviderEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|base
init|=
literal|"http://apache.org/cxf/systest/ws/addr_feature/AddNumbersPortType/"
decl_stmt|;
name|String
name|expectedOut
init|=
name|base
operator|+
literal|"addNumbersRequest</Action>"
decl_stmt|;
name|String
name|expectedIn
init|=
name|base
operator|+
literal|"addNumbersResponse</Action>"
decl_stmt|;
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
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:9094/jaxws/add-provider"
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
name|output
operator|.
name|reset
argument_list|()
expr_stmt|;
name|input
operator|.
name|reset
argument_list|()
expr_stmt|;
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
literal|"http://localhost:9094/jaxws/add-providernows"
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
name|testBasicDispatchInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|req
init|=
literal|"<addNumbers xmlns=\"http://apache.org/cxf/systest/ws/addr_feature/\">"
operator|+
literal|"<number1>1</number1><number2>2</number2></addNumbers>"
decl_stmt|;
name|String
name|base
init|=
literal|"http://apache.org/cxf/systest/ws/addr_feature/AddNumbersPortType/"
decl_stmt|;
name|String
name|expectedOut
init|=
name|base
operator|+
literal|"addNumbersRequest"
decl_stmt|;
name|String
name|expectedIn
init|=
name|base
operator|+
literal|"addNumbersResponse</Action>"
decl_stmt|;
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
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|disp
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|AddNumbersService
operator|.
name|AddNumbersPort
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|Mode
operator|.
name|PAYLOAD
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
literal|"http://localhost:9094/jaxws/add"
argument_list|)
expr_stmt|;
comment|//manually set the action
name|disp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|SOAPACTION_URI_PROPERTY
argument_list|,
name|expectedOut
argument_list|)
expr_stmt|;
name|disp
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|req
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|output
operator|.
name|reset
argument_list|()
expr_stmt|;
name|input
operator|.
name|reset
argument_list|()
expr_stmt|;
name|disp
operator|=
name|service
operator|.
name|createDispatch
argument_list|(
name|AddNumbersService
operator|.
name|AddNumbersPort
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|Mode
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
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
literal|"http://localhost:9094/jaxws/add"
argument_list|)
expr_stmt|;
comment|//set the operation name so action can be pulled from the wsdl
name|disp
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|MessageContext
operator|.
name|WSDL_OPERATION
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/systest/ws/addr_feature/"
argument_list|,
literal|"addNumbers"
argument_list|)
argument_list|)
expr_stmt|;
name|disp
operator|.
name|invoke
argument_list|(
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|req
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
specifier|private
name|AddNumbersPortType
name|getPort
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
return|return
name|service
operator|.
name|getAddNumbersPort
argument_list|()
return|;
block|}
block|}
end_class

end_unit

