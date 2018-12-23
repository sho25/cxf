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
name|policy
package|;
end_package

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
name|SocketTimeoutException
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
name|logging
operator|.
name|Logger
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
name|WebServiceException
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|greeter_control
operator|.
name|BasicGreeterService
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
name|greeter_control
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
name|cxf
operator|.
name|greeter_control
operator|.
name|PingMeFault
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
name|policy
operator|.
name|PolicyException
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
name|assertEquals
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

begin_comment
comment|/**  * Tests the use of the WS-Policy Framework to determine the behaviour of the HTTP client  * by policies including the HTTPClientPolicy assertion attached to different subjects  * of the contract (endpoint, operation, binding, messager).  * The server in this test is not policy aware.  * Neither client nor server do have addressing interceptors installed: there are no addressing  * assertions that would trigger the installation of the interceptors on the client side. The use  * of the DecoupledEndpoint attribute in the HTTPClientPolicy assertions is merely for illustrating  * the use of multiple compatible or incompatible assertions.  */
end_comment

begin_class
specifier|public
class|class
name|HTTPClientPolicyTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
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
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|HTTPClientPolicyTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POLICY_ENGINE_ENABLED_CFG
init|=
literal|"org/apache/cxf/systest/ws/policy/http.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|POLICY_VIA_FEATURE_CFG
init|=
literal|"org/apache/cxf/systest/ws/policy/http_client_policy_feature.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|GREETER_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/greeter_control"
argument_list|,
literal|"BasicGreeterService"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
name|Endpoint
name|ep
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|LoggingInInterceptor
name|in
init|=
operator|new
name|LoggingInInterceptor
argument_list|()
decl_stmt|;
name|LoggingOutInterceptor
name|out
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|HttpGreeterImpl
name|implementor
init|=
operator|new
name|HttpGreeterImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|setThrowAlways
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published greeter endpoint."
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ep
operator|=
literal|null
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
name|Test
specifier|public
name|void
name|testUsingHTTPClientPolicies
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
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|POLICY_ENGINE_ENABLED_CFG
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|LoggingInInterceptor
name|in
init|=
operator|new
name|LoggingInInterceptor
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|LoggingOutInterceptor
name|out
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// use a client wsdl with policies attached to endpoint, operation and message subjects
name|URL
name|url
init|=
name|HTTPClientPolicyTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"http_client_greeter.wsdl"
argument_list|)
decl_stmt|;
name|BasicGreeterService
name|gs
init|=
operator|new
name|BasicGreeterService
argument_list|(
name|url
argument_list|,
name|GREETER_QNAME
argument_list|)
decl_stmt|;
specifier|final
name|Greeter
name|greeter
init|=
name|gs
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created greeter client."
argument_list|)
expr_stmt|;
comment|// sayHi - this operation has message policies that are incompatible with
comment|// the endpoint policies
try|try
block|{
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Did not receive expected PolicyException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|wex
parameter_list|)
block|{
name|PolicyException
name|ex
init|=
operator|(
name|PolicyException
operator|)
name|wex
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"INCOMPATIBLE_HTTPCLIENTPOLICY_ASSERTIONS"
argument_list|,
name|ex
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// greetMeOneWay - no message or operation policies
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
comment|// greetMe - operation policy specifies receive timeout and should cause every
comment|// other invocation to fail
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"cxf"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Didn't get the exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ex.printStackTrace();
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|SocketTimeoutException
argument_list|)
expr_stmt|;
block|}
comment|// pingMe - policy attached to binding operation fault should have no effect
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PingMeFault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHTTPClientPolicyViaFeature
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
name|bus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|POLICY_VIA_FEATURE_CFG
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// use a WSDL sanitized of any policy assertions, instead
comment|// the HTTPClientPolicy is applied via a feature set on the
comment|//<jaxws:client> bean
comment|//
name|URL
name|url
init|=
name|HTTPClientPolicyTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"bare_greeter.wsdl"
argument_list|)
decl_stmt|;
name|BasicGreeterService
name|gs
init|=
operator|new
name|BasicGreeterService
argument_list|(
name|url
argument_list|,
name|GREETER_QNAME
argument_list|)
decl_stmt|;
specifier|final
name|Greeter
name|greeter
init|=
name|gs
operator|.
name|getGreeterPort
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created greeter client."
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
name|HTTPConduit
name|c
init|=
call|(
name|HTTPConduit
call|)
argument_list|(
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getConduit
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected HTTPConduit"
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected DecoupledEndpoint"
argument_list|,
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected DecoupledEndpoint"
argument_list|,
literal|"http://localhost:9909/decoupled_endpoint"
argument_list|,
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

