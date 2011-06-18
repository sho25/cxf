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
name|util
operator|.
name|List
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
name|endpoint
operator|.
name|ServerRegistry
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
name|greeter_control
operator|.
name|ReliableGreeterService
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
name|CastUtils
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
name|util
operator|.
name|ConnectionHelper
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
name|util
operator|.
name|InMessageRecorder
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
name|util
operator|.
name|MessageFlow
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
name|util
operator|.
name|MessageRecorder
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
name|util
operator|.
name|OutMessageRecorder
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
name|ws
operator|.
name|policy
operator|.
name|PolicyAssertion
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
name|PolicyEngine
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
name|rm
operator|.
name|RM10Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|All
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
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
comment|/**  * Tests the use of the WS-Policy Framework to automatically engage WS-RM  * in response to Policies defined for the endpoint via an direct attachment to the wsdl.  */
end_comment

begin_class
specifier|public
class|class
name|RMPolicyWsdlTest
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
specifier|public
specifier|static
specifier|final
name|String
name|DECOUPLED
init|=
name|allocatePort
argument_list|(
literal|"decoupled"
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
name|RMPolicyWsdlTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GREETMEONEWAY_ACTION
init|=
literal|"http://cxf.apache.org/greeter_control/Greeter/greetMeOneWayRequest"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GREETME_ACTION
init|=
literal|"http://cxf.apache.org/greeter_control/Greeter/greetMeRequest"
decl_stmt|;
comment|/*    private static final String GREETME_RESPONSE_ACTION          = "http://cxf.apache.org/greeter_control/Greeter/greetMeResponse";      */
specifier|private
specifier|static
specifier|final
name|String
name|PINGME_ACTION
init|=
literal|"http://cxf.apache.org/greeter_control/Greeter/pingMeRequest"
decl_stmt|;
comment|/*    private static final String PINGME_RESPONSE_ACTION          = "http://cxf.apache.org/greeter_control/Greeter/pingMeResponse";     private static final String GREETER_FAULT_ACTION         = "http://cxf.apache.org/greeter_control/Greeter/pingMe/Fault/faultDetail";     */
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
argument_list|(
literal|"org/apache/cxf/systest/ws/policy/rmwsdl_server.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|ServerRegistry
name|sr
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions1
init|=
name|getAssertions
argument_list|(
name|pe
argument_list|,
name|sr
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2 assertions should be available"
argument_list|,
literal|2
argument_list|,
name|assertions1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions2
init|=
name|getAssertions
argument_list|(
name|pe
argument_list|,
name|sr
operator|.
name|getServers
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1 assertion should be available"
argument_list|,
literal|1
argument_list|,
name|assertions2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Published greeter endpoints."
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|PolicyAssertion
argument_list|>
name|getAssertions
parameter_list|(
name|PolicyEngine
name|pe
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
name|s
parameter_list|)
block|{
name|Policy
name|p1
init|=
name|pe
operator|.
name|getServerEndpointPolicy
argument_list|(
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
literal|null
argument_list|)
operator|.
name|getPolicy
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ExactlyOne
argument_list|>
name|pops
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|p1
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|ExactlyOne
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"New policy must have 1 top level policy operator"
argument_list|,
literal|1
argument_list|,
name|pops
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|All
argument_list|>
name|alts
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pops
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|All
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1 alternatives should be available"
argument_list|,
literal|1
argument_list|,
name|alts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|alts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAssertions
argument_list|()
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
return|;
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
name|testUsingRM
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
literal|"org/apache/cxf/systest/ws/policy/rmwsdl.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|OutMessageRecorder
name|outRecorder
init|=
operator|new
name|OutMessageRecorder
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outRecorder
argument_list|)
expr_stmt|;
name|InMessageRecorder
name|inRecorder
init|=
operator|new
name|InMessageRecorder
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inRecorder
argument_list|)
expr_stmt|;
name|ReliableGreeterService
name|gs
init|=
operator|new
name|ReliableGreeterService
argument_list|()
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
name|ConnectionHelper
operator|.
name|setKeepAliveConnection
argument_list|(
name|greeter
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// oneway
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"CXF"
argument_list|)
expr_stmt|;
comment|// two-way
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
comment|// exception
try|try
block|{
name|greeter
operator|.
name|pingMe
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"First invocation should have succeeded."
argument_list|)
expr_stmt|;
block|}
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
operator|(
name|int
operator|)
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
operator|(
name|int
operator|)
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
name|MessageRecorder
name|mr
init|=
operator|new
name|MessageRecorder
argument_list|(
name|outRecorder
argument_list|,
name|inRecorder
argument_list|)
decl_stmt|;
name|mr
operator|.
name|awaitMessages
argument_list|(
literal|5
argument_list|,
literal|9
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
name|MessageFlow
name|mf
init|=
operator|new
name|MessageFlow
argument_list|(
name|outRecorder
operator|.
name|getOutboundMessages
argument_list|()
argument_list|,
name|inRecorder
operator|.
name|getInboundMessages
argument_list|()
argument_list|)
decl_stmt|;
name|mf
operator|.
name|verifyMessages
argument_list|(
literal|5
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
index|[]
name|expectedActions
init|=
operator|new
name|String
index|[]
block|{
name|RM10Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceAction
argument_list|()
block|,
name|GREETMEONEWAY_ACTION
block|,
name|GREETME_ACTION
block|,
name|PINGME_ACTION
block|,
name|PINGME_ACTION
block|}
decl_stmt|;
comment|/*        mf.verifyActions(expectedActions, true);         mf.verifyMessageNumbers(new String[] {null, "1", "2", "3", "4"}, true);         mf.verifyLastMessage(new boolean[] {false, false, false, false, false}, true);         mf.verifyAcknowledgements(new boolean[] {false, false, false, true, true}, true);          mf.verifyMessages(9, false);         mf.verifyPartialResponses(5);                 mf.purgePartialResponses();          expectedActions = new String[] {             RM10Constants.INSTANCE.getCreateSequenceResponseAction(),             GREETME_RESPONSE_ACTION,             PINGME_RESPONSE_ACTION,             GREETER_FAULT_ACTION         };         mf.verifyActions(expectedActions, false);         mf.verifyMessageNumbers(new String[] {null, "1", "2", "3"}, false);         mf.verifyLastMessage(new boolean[] {false, false, false, false}, false);         mf.verifyAcknowledgements(new boolean[] {false, true, true, true}, false);  */
block|}
block|}
end_class

end_unit

