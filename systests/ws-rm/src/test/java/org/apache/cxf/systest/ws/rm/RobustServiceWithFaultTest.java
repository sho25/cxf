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
name|interceptor
operator|.
name|Fault
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|assertFalse
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

begin_comment
comment|/**  * Tests the addition of WS-RM properties to application messages and the  * exchange of WS-RM protocol messages.  */
end_comment

begin_class
specifier|public
class|class
name|RobustServiceWithFaultTest
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
name|GREETMEONEWAY_ACTION
init|=
literal|"http://cxf.apache.org/greeter_control/Greeter/greetMeOneWayRequest"
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
name|RobustServiceWithFaultTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|RobustOneWayPropertySetter
name|robustSetter
decl_stmt|;
specifier|private
specifier|static
name|GreeterCounterImpl
name|serverGreeter
decl_stmt|;
specifier|private
name|Greeter
name|greeter
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
comment|// use a at-most-once server with sync ack processing
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"/org/apache/cxf/systest/ws/rm/atmostonce.xml"
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
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setAcknowledgementInterval
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|serverGreeter
operator|=
operator|new
name|GreeterCounterImpl
argument_list|()
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
name|robustSetter
operator|=
operator|new
name|RobustOneWayPropertySetter
argument_list|()
expr_stmt|;
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|robustSetter
argument_list|)
expr_stmt|;
comment|// publish this robust oneway endpoint
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|serverGreeter
argument_list|)
expr_stmt|;
comment|// leave the robust prop untouched, as it will be set per call later
name|ep
operator|.
name|publish
argument_list|(
name|address
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
name|testRobustWithSomeFaults
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
literal|"/org/apache/cxf/systest/ws/rm/seqlength1.xml"
argument_list|)
expr_stmt|;
comment|// set the client retry interval much shorter than the slow processing delay
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
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|5000
argument_list|)
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
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
name|PORT
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
comment|// invoked once
name|assertEquals
argument_list|(
literal|"not invoked once"
argument_list|,
literal|1
argument_list|,
name|serverGreeter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"still in retransmission"
argument_list|,
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Invoking greeter and raising a fault"
argument_list|)
expr_stmt|;
name|serverGreeter
operator|.
name|setThrowAlways
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// still invoked once
name|assertEquals
argument_list|(
literal|"not invoked once"
argument_list|,
literal|1
argument_list|,
name|serverGreeter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"still in retransmission"
argument_list|,
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Invoking robust greeter and raising a fault"
argument_list|)
expr_stmt|;
name|robustSetter
operator|.
name|setRobust
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"three"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// still invoked once
name|assertEquals
argument_list|(
literal|"not invoked once"
argument_list|,
literal|1
argument_list|,
name|serverGreeter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"no message in retransmission"
argument_list|,
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Stop raising a fault and let the retransmission succeeds"
argument_list|)
expr_stmt|;
name|serverGreeter
operator|.
name|setThrowAlways
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|8000
argument_list|)
expr_stmt|;
comment|// invoked twice
name|assertEquals
argument_list|(
literal|"not invoked twice"
argument_list|,
literal|2
argument_list|,
name|serverGreeter
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"still in retransmission"
argument_list|,
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|GreeterCounterImpl
extends|extends
name|GreeterImpl
block|{
specifier|private
name|int
name|count
decl_stmt|;
specifier|private
name|boolean
name|throwAlways
decl_stmt|;
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
if|if
condition|(
name|throwAlways
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"invocation exception"
argument_list|)
throw|;
block|}
name|super
operator|.
name|greetMeOneWay
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setThrowAlways
parameter_list|(
name|boolean
name|t
parameter_list|)
block|{
name|throwAlways
operator|=
name|t
expr_stmt|;
name|super
operator|.
name|setThrowAlways
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|RobustOneWayPropertySetter
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
name|boolean
name|robust
decl_stmt|;
name|RobustOneWayPropertySetter
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|RECEIVE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRobust
parameter_list|(
name|boolean
name|robust
parameter_list|)
block|{
name|this
operator|.
name|robust
operator|=
name|robust
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|,
name|robust
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

