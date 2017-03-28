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
name|rm
operator|.
name|sec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|types
operator|.
name|GreetMe
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
name|rt
operator|.
name|security
operator|.
name|SecurityConstants
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|rm
operator|.
name|RMManager
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Tests the correct interaction of ws-rm calls with security.when policy validator verifies the calls.  */
end_comment

begin_class
specifier|public
class|class
name|WSRMWithWSSecurityPolicyTest
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
name|WSRMWithWSSecurityPolicyTest
operator|.
name|class
argument_list|)
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
literal|"/org/apache/cxf/systest/ws/rm/sec/server-policy.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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
name|testWithSecurityInPolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating greeter client"
argument_list|)
expr_stmt|;
try|try
init|(
name|ClassPathXmlApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/sec/client-policy.xml"
argument_list|)
init|)
block|{
name|Bus
name|bus
init|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"bus"
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
name|Greeter
name|greeter
init|=
operator|(
name|Greeter
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"GreeterCombinedClient"
argument_list|)
decl_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|empty
init|=
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"RetransmissionQueue is not empty"
argument_list|,
name|empty
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Invoking greeter"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|empty
operator|=
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"RetransmissionQueue not empty"
argument_list|,
name|empty
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContextProperty
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|ClassPathXmlApplicationContext
name|context
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/sec/client-policy.xml"
argument_list|)
init|)
block|{
name|Bus
name|bus
init|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"bus"
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
name|Greeter
name|greeter
init|=
operator|(
name|Greeter
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"GreeterCombinedClientNoProperty"
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|QName
name|operationQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/greeter_control"
argument_list|,
literal|"greetMe"
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getOperation
argument_list|(
name|operationQName
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|invocationContext
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|invocationContext
operator|.
name|put
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
name|invocationContext
operator|.
name|put
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME
argument_list|,
literal|"Alice"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
literal|"org.apache.cxf.systest.ws.rm.sec.UTPasswordCallback"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|,
literal|"bob.properties"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
literal|"alice.properties"
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|empty
init|=
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"RetransmissionQueue is not empty"
argument_list|,
name|empty
argument_list|)
expr_stmt|;
name|GreetMe
name|param
init|=
operator|new
name|GreetMe
argument_list|()
decl_stmt|;
name|param
operator|.
name|setRequestType
argument_list|(
literal|"testContextProperty"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|answer
init|=
name|client
operator|.
name|invoke
argument_list|(
name|boi
argument_list|,
operator|new
name|Object
index|[]
block|{
name|param
block|}
argument_list|,
name|invocationContext
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"TESTCONTEXTPROPERTY"
argument_list|,
name|answer
index|[
literal|0
index|]
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|empty
operator|=
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"RetransmissionQueue not empty"
argument_list|,
name|empty
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

