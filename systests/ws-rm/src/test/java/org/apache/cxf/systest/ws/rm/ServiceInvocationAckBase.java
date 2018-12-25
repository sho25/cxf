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
name|net
operator|.
name|MalformedURLException
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
name|greeter_control
operator|.
name|types
operator|.
name|FaultLocation
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
name|ServiceInvokerInterceptor
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
name|phase
operator|.
name|Phase
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
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|RetransmissionQueue
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
comment|/**  * Tests the acknowledgement delivery back to the non-decoupled port when there is some  * error at the provider side and how its behavior is affected by the robust in-only mode setting.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ServiceInvocationAckBase
extends|extends
name|AbstractBusClientServerTestBase
block|{
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
name|ServiceInvocationAckBase
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
name|String
name|port
decl_stmt|;
name|String
name|pfx
decl_stmt|;
name|Endpoint
name|ep
decl_stmt|;
specifier|public
name|Server
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|port
operator|=
name|args
index|[
literal|0
index|]
expr_stmt|;
name|pfx
operator|=
name|args
index|[
literal|1
index|]
expr_stmt|;
block|}
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
name|pfx
operator|+
literal|"-server"
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|port
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
name|port
operator|+
literal|"/SoapContext/ControlPort"
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Published control endpoint."
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
specifier|public
specifier|abstract
name|String
name|getPort
parameter_list|()
function_decl|;
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
literal|"rmdb"
return|;
block|}
specifier|public
specifier|static
name|void
name|startServer
parameter_list|(
name|String
name|port
parameter_list|,
name|String
name|pfx
parameter_list|)
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
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
name|port
block|,
name|pfx
block|}
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
block|{
if|if
condition|(
literal|null
operator|!=
name|greeter
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Failed to stop greeter."
argument_list|,
name|control
operator|.
name|stopGreeter
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|greeterBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|greeterBus
operator|=
literal|null
expr_stmt|;
block|}
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
specifier|protected
name|void
name|setupGreeter
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|testDefaultInvocationHandling
parameter_list|()
throws|throws
name|Exception
block|{
name|setupGreeter
argument_list|()
expr_stmt|;
name|control
operator|.
name|setRobustInOnlyMode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|FaultLocation
name|location
init|=
operator|new
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
name|ObjectFactory
argument_list|()
operator|.
name|createFaultLocation
argument_list|()
decl_stmt|;
name|location
operator|.
name|setPhase
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
name|location
operator|.
name|setBefore
argument_list|(
name|ServiceInvokerInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|RMManager
name|manager
init|=
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the message is acked and the invocation takes place
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|waitForEmpty
argument_list|(
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|setFaultLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
comment|// the invocation fails but the message is acked because the delivery succeeds
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|waitForEmpty
argument_list|(
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRobustInvocationHandling
parameter_list|()
throws|throws
name|Exception
block|{
name|setupGreeter
argument_list|()
expr_stmt|;
name|control
operator|.
name|setRobustInOnlyMode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|FaultLocation
name|location
init|=
operator|new
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
name|ObjectFactory
argument_list|()
operator|.
name|createFaultLocation
argument_list|()
decl_stmt|;
name|location
operator|.
name|setPhase
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
name|location
operator|.
name|setBefore
argument_list|(
name|ServiceInvokerInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|RMManager
name|manager
init|=
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the message is acked and the invocation takes place
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|waitForEmpty
argument_list|(
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|setFaultLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
comment|// the invocation fails but the message is acked because the delivery succeeds
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|waitForNotEmpty
argument_list|(
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
argument_list|)
expr_stmt|;
name|location
operator|.
name|setPhase
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|setFaultLocation
argument_list|(
name|location
argument_list|)
expr_stmt|;
comment|// the retransmission succeeds and the invocation succeeds, the message is acked
name|waitForEmpty
argument_list|(
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|waitForNotEmpty
parameter_list|(
name|RetransmissionQueue
name|retransmissionQueue
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|retransmissionQueue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|long
name|total
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
if|if
condition|(
name|total
operator|>
literal|10000L
condition|)
block|{
name|fail
argument_list|(
literal|"RetransmissionQueue must not be empty"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|waitForEmpty
parameter_list|(
name|RetransmissionQueue
name|retransmissionQueue
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
if|if
condition|(
name|retransmissionQueue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|long
name|total
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
if|if
condition|(
name|total
operator|>
literal|10000L
condition|)
block|{
name|fail
argument_list|(
literal|"RetransmissionQueue must be empty"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|setupGreeter
parameter_list|(
name|String
name|cfgResource
parameter_list|)
throws|throws
name|NumberFormatException
throws|,
name|MalformedURLException
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
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
name|updateAddressPort
argument_list|(
name|control
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"db.name"
argument_list|,
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
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
name|System
operator|.
name|clearProperty
argument_list|(
literal|"db.name"
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
name|GreeterService
name|gs
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
name|greeter
operator|=
name|gs
operator|.
name|getGreeterPort
argument_list|()
expr_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created greeter client."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

