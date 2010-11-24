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
name|dispatch
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|AsyncHandler
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
name|Endpoint
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
name|interceptor
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
name|interceptor
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
name|AbstractBusTestServerBase
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
name|hello_world_soap_http
operator|.
name|GreeterImpl
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
name|DispatchClientServerWithMalformedResponseTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPDispatchService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapDispatchPort"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|String
name|greeterPort
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|DispatchClientServerWithMalformedResponseTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|int
name|asyncHandlerInvokedCount
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|DispatchClientServerWithMalformedResponseTest
operator|.
name|class
argument_list|)
operator|+
literal|"/SOAPDispatchService/SoapDispatchPort"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
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
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
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
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MalformedResponseInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|waitForFuture
parameter_list|(
name|Future
name|fd
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|fd
operator|.
name|isDone
argument_list|()
condition|)
block|{
operator|++
name|count
expr_stmt|;
if|if
condition|(
name|count
operator|>
literal|500
condition|)
block|{
name|fail
argument_list|(
literal|"Did not finish in 10 seconds"
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|20
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSOAPMessageWithMalformedResponse
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
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
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
name|PORT_NAME
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
name|greeterPort
operator|+
literal|"/SOAPDispatchService/SoapDispatchPort"
argument_list|)
expr_stmt|;
comment|// Test async callback
name|InputStream
name|is3
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/GreetMeDocLiteralReq3.xml"
argument_list|)
decl_stmt|;
name|SOAPMessage
name|soapReqMsg3
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
name|is3
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|soapReqMsg3
argument_list|)
expr_stmt|;
name|TestSOAPMessageHandler
name|tsmh
init|=
operator|new
name|TestSOAPMessageHandler
argument_list|()
decl_stmt|;
name|Future
name|f
init|=
name|disp
operator|.
name|invokeAsync
argument_list|(
name|soapReqMsg3
argument_list|,
name|tsmh
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|waitForFuture
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsyncHandler shouldn't get invoked more than once"
argument_list|,
name|asyncHandlerInvokedCount
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
class|class
name|TestSOAPMessageHandler
implements|implements
name|AsyncHandler
argument_list|<
name|SOAPMessage
argument_list|>
block|{
name|String
name|replyBuffer
decl_stmt|;
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|SOAPMessage
argument_list|>
name|response
parameter_list|)
block|{
try|try
block|{
name|asyncHandlerInvokedCount
operator|++
expr_stmt|;
name|SOAPMessage
name|reply
init|=
name|response
operator|.
name|get
argument_list|()
decl_stmt|;
name|replyBuffer
operator|=
name|reply
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|getTextContent
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
specifier|public
name|String
name|getReplyBuffer
parameter_list|()
block|{
return|return
name|replyBuffer
return|;
block|}
block|}
block|}
end_class

end_unit

