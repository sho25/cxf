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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
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
name|Control
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
name|ControlService
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
name|GreeterService
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
name|recorders
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
name|testutil
operator|.
name|recorders
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
name|testutil
operator|.
name|recorders
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|Names
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
name|VersionTransformer
operator|.
name|Names200408
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
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|RM11Constants
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
name|RMConstants
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
name|RMException
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
name|After
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
comment|/**  * Tests control of WS-RM protocol variations on the client, and of the server responses matching whichever  * variation is used by the client.  */
end_comment

begin_class
specifier|public
class|class
name|ProtocolVariationsTest
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
name|ProtocolVariationsTest
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
name|ProtocolVariationsTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GREETME_ACTION
init|=
literal|"http://cxf.apache.org/greeter_control/Greeter/greetMeRequest"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GREETME_RESPONSE_ACTION
init|=
literal|"http://cxf.apache.org/greeter_control/Greeter/greetMeResponse"
decl_stmt|;
specifier|private
specifier|static
name|String
name|decoupledEndpoint
decl_stmt|;
specifier|private
specifier|static
name|int
name|decoupledCount
init|=
literal|1
decl_stmt|;
specifier|private
name|Bus
name|controlBus
decl_stmt|;
specifier|private
name|Control
name|control
decl_stmt|;
specifier|private
name|Bus
name|greeterBus
decl_stmt|;
specifier|private
name|Greeter
name|greeter
decl_stmt|;
specifier|private
name|OutMessageRecorder
name|outRecorder
decl_stmt|;
specifier|private
name|InMessageRecorder
name|inRecorder
decl_stmt|;
specifier|private
name|Dispatch
argument_list|<
name|DOMSource
argument_list|>
name|dispatch
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
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|()
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
comment|//System.out.println("Created control bus " + bus);
name|ControlImpl
name|implementor
init|=
operator|new
name|ControlImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|setDbName
argument_list|(
literal|"pvt-server"
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
argument_list|)
expr_stmt|;
name|GreeterImpl
name|greeterImplementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|setImplementor
argument_list|(
name|greeterImplementor
argument_list|)
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/ControlPort"
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
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
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|stopClient
argument_list|()
expr_stmt|;
name|stopControl
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRM10WSA200408
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// same as default, but explicitly setting the WS-Addressing namespace
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRM10WSA15
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// WS-RM 1.0, but using the WS-A 1.0 namespace
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRM11
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// WS-RM 1.1 and WS-A 1.0
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_VERSION_PROPERTY
argument_list|,
name|RM11Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM11Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidRM11WSA200408
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// WS-RM 1.1, but using the WS-A 1.0 namespace
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_VERSION_PROPERTY
argument_list|,
name|RM11Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
try|try
block|{
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"invalid namespace combination"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RMException
argument_list|)
expr_stmt|;
comment|// verify a partial error text match to exclude an unexpected exception
comment|// (see UNSUPPORTED_NAMESPACE in Messages.properties)
specifier|final
name|String
name|text
init|=
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
operator|+
literal|" is not supported"
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
operator|&&
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
name|text
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultDecoupled
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRM10WSA200408Decoupled
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// same as default, but explicitly setting the WS-Addressing namespace
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRM10WSA15Decoupled
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// WS-RM 1.0, but using the WS-A 1.0 namespace
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRM11Decoupled
parameter_list|()
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/rminterceptors.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// WS-RM 1.1 and WS-A 1.0
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
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_VERSION_PROPERTY
argument_list|,
name|RM11Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ONE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TWO"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"THREE"
argument_list|,
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyTwowayNonAnonymous
argument_list|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM11Constants
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyTwowayNonAnonymous
parameter_list|(
name|String
name|wsaUri
parameter_list|,
name|RMConstants
name|consts
parameter_list|)
throws|throws
name|Exception
block|{
comment|// CreateSequence and three greetMe messages
name|awaitMessages
argument_list|(
literal|4
argument_list|,
literal|4
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
argument_list|,
name|wsaUri
argument_list|,
name|consts
operator|.
name|getWSRMNamespace
argument_list|()
argument_list|)
decl_stmt|;
name|mf
operator|.
name|verifyMessages
argument_list|(
literal|4
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
name|consts
operator|.
name|getCreateSequenceAction
argument_list|()
block|,
name|GREETME_ACTION
block|,
name|GREETME_ACTION
block|,
name|GREETME_ACTION
block|}
decl_stmt|;
name|mf
operator|.
name|verifyActions
argument_list|(
name|expectedActions
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyMessageNumbers
argument_list|(
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|"1"
block|,
literal|"2"
block|,
literal|"3"
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyLastMessage
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|false
block|,
literal|false
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyAcknowledgements
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|,
literal|true
block|,
literal|true
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// createSequenceResponse plus 3 greetMeResponse messages
comment|// the first response should not include an acknowledgement, the other three should
name|mf
operator|.
name|verifyMessages
argument_list|(
literal|4
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|expectedActions
operator|=
operator|new
name|String
index|[]
block|{
name|consts
operator|.
name|getCreateSequenceResponseAction
argument_list|()
block|,
name|GREETME_RESPONSE_ACTION
block|,
name|GREETME_RESPONSE_ACTION
block|,
name|GREETME_RESPONSE_ACTION
block|}
expr_stmt|;
name|mf
operator|.
name|verifyActions
argument_list|(
name|expectedActions
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyMessageNumbers
argument_list|(
operator|new
name|String
index|[]
block|{
literal|null
block|,
literal|"1"
block|,
literal|"2"
block|,
literal|"3"
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyLastMessage
argument_list|(
operator|new
name|boolean
index|[
literal|4
index|]
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyAcknowledgements
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|true
block|,
literal|true
block|,
literal|true
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// --- test utilities ---
specifier|private
name|void
name|init
parameter_list|(
name|String
name|cfgResource
parameter_list|,
name|boolean
name|useDecoupledEndpoint
parameter_list|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|initControl
argument_list|(
name|bf
argument_list|,
name|cfgResource
argument_list|)
expr_stmt|;
name|initGreeterBus
argument_list|(
name|bf
argument_list|,
name|cfgResource
argument_list|)
expr_stmt|;
name|initProxy
argument_list|(
name|useDecoupledEndpoint
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initControl
parameter_list|(
name|SpringBusFactory
name|bf
parameter_list|,
name|String
name|cfgResource
parameter_list|)
block|{
name|controlBus
operator|=
name|bf
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|controlBus
argument_list|)
expr_stmt|;
name|ControlService
name|cs
init|=
operator|new
name|ControlService
argument_list|()
decl_stmt|;
name|control
operator|=
name|cs
operator|.
name|getControlPort
argument_list|()
expr_stmt|;
try|try
block|{
name|updateAddressPort
argument_list|(
name|control
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
name|assertTrue
argument_list|(
literal|"Failed to start greeter"
argument_list|,
name|control
operator|.
name|startGreeter
argument_list|(
name|cfgResource
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initGreeterBus
parameter_list|(
name|SpringBusFactory
name|bf
parameter_list|,
name|String
name|cfgResource
parameter_list|)
block|{
name|greeterBus
operator|=
name|bf
operator|.
name|createBus
argument_list|(
name|cfgResource
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|greeterBus
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Initialised greeter bus with configuration: "
operator|+
name|cfgResource
argument_list|)
expr_stmt|;
name|outRecorder
operator|=
operator|new
name|OutMessageRecorder
argument_list|()
expr_stmt|;
name|greeterBus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outRecorder
argument_list|)
expr_stmt|;
name|inRecorder
operator|=
operator|new
name|InMessageRecorder
argument_list|()
expr_stmt|;
name|greeterBus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|inRecorder
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initProxy
parameter_list|(
name|boolean
name|useDecoupledEndpoint
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|GreeterService
name|gs
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|executor
condition|)
block|{
name|gs
operator|.
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
name|greeter
operator|=
name|gs
operator|.
name|getGreeterPort
argument_list|()
expr_stmt|;
try|try
block|{
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
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
if|if
condition|(
name|useDecoupledEndpoint
condition|)
block|{
name|initDecoupledEndpoint
argument_list|(
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initDecoupledEndpoint
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
comment|// programatically configure decoupled endpoint that is guaranteed to
comment|// be unique across all test cases
name|decoupledEndpoint
operator|=
literal|"http://localhost:"
operator|+
name|allocatePort
argument_list|(
literal|"decoupled-"
operator|+
name|decoupledCount
operator|++
argument_list|)
operator|+
literal|"/decoupled_endpoint"
expr_stmt|;
name|HTTPConduit
name|hc
init|=
call|(
name|HTTPConduit
call|)
argument_list|(
name|c
operator|.
name|getConduit
argument_list|()
argument_list|)
decl_stmt|;
name|HTTPClientPolicy
name|cp
init|=
name|hc
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|cp
operator|.
name|setDecoupledEndpoint
argument_list|(
name|decoupledEndpoint
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Using decoupled endpoint: "
operator|+
name|cp
operator|.
name|getDecoupledEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|stopClient
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|greeterBus
condition|)
block|{
comment|//ensure we close the decoupled destination of the conduit,
comment|//so that release the port if the destination reference count hit zero
if|if
condition|(
name|greeter
operator|!=
literal|null
condition|)
block|{
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
operator|.
name|getConduit
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|dispatch
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|DispatchImpl
argument_list|<
name|?
argument_list|>
operator|)
name|dispatch
operator|)
operator|.
name|getClient
argument_list|()
operator|.
name|getConduit
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|greeterBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|greeter
operator|=
literal|null
expr_stmt|;
name|dispatch
operator|=
literal|null
expr_stmt|;
name|greeterBus
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|stopControl
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|control
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Failed to stop greeter"
argument_list|,
name|control
operator|.
name|stopGreeter
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|controlBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|awaitMessages
parameter_list|(
name|int
name|nExpectedOut
parameter_list|,
name|int
name|nExpectedIn
parameter_list|)
block|{
name|awaitMessages
argument_list|(
name|nExpectedOut
argument_list|,
name|nExpectedIn
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|awaitMessages
parameter_list|(
name|int
name|nExpectedOut
parameter_list|,
name|int
name|nExpectedIn
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
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
name|nExpectedOut
argument_list|,
name|nExpectedIn
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

