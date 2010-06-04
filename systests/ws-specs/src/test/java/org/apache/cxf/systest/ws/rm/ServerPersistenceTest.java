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
name|math
operator|.
name|BigInteger
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
name|Response
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
name|greeter_control
operator|.
name|types
operator|.
name|GreetMeResponse
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
name|persistence
operator|.
name|jdbc
operator|.
name|RMTxStore
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
comment|/**  * Tests the addition of WS-RM properties to application messages and the  * exchange of WS-RM protocol messages.  */
end_comment

begin_class
specifier|public
class|class
name|ServerPersistenceTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PORT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DECOUPLE_PORT
init|=
name|allocatePort
argument_list|(
literal|"decoupled.port"
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
specifier|public
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
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ServerPersistenceTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CFG
init|=
literal|"/org/apache/cxf/systest/ws/rm/persistent.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVER_LOSS_CFG
init|=
literal|"/org/apache/cxf/systest/ws/rm/persistent-message-loss-server.xml"
decl_stmt|;
specifier|private
name|OutMessageRecorder
name|out
decl_stmt|;
specifier|private
name|InMessageRecorder
name|in
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
name|String
name|derbyHome
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"derby.system.home"
argument_list|)
decl_stmt|;
if|if
condition|(
name|derbyHome
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"derby.system.home"
argument_list|,
literal|"target/derby"
argument_list|)
expr_stmt|;
block|}
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|()
expr_stmt|;
name|derbyHome
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"derby.system.home"
argument_list|)
expr_stmt|;
try|try
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"derby.system.home"
argument_list|,
name|derbyHome
operator|+
literal|"-server"
argument_list|)
expr_stmt|;
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|derbyHome
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"derby.system.home"
argument_list|,
name|derbyHome
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"derby.system.home"
argument_list|)
expr_stmt|;
block|}
block|}
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
name|testRecovery
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
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created bus "
operator|+
name|bus
operator|+
literal|" with default cfg"
argument_list|)
expr_stmt|;
name|ControlService
name|cs
init|=
operator|new
name|ControlService
argument_list|()
decl_stmt|;
name|Control
name|control
init|=
name|cs
operator|.
name|getControlPort
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|control
argument_list|,
name|PORT
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
name|SERVER_LOSS_CFG
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Started greeter server."
argument_list|)
expr_stmt|;
name|Bus
name|greeterBus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|CFG
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Created bus "
operator|+
name|greeterBus
operator|+
literal|" with cfg : "
operator|+
name|CFG
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|greeterBus
argument_list|)
expr_stmt|;
comment|// avoid early client resends
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
operator|.
name|getRMAssertion
argument_list|()
operator|.
name|getBaseRetransmissionInterval
argument_list|()
operator|.
name|setMilliseconds
argument_list|(
operator|new
name|BigInteger
argument_list|(
literal|"60000"
argument_list|)
argument_list|)
expr_stmt|;
name|GreeterService
name|gs
init|=
operator|new
name|GreeterService
argument_list|()
decl_stmt|;
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
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
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
literal|"http://localhost:"
operator|+
name|DECOUPLE_PORT
operator|+
literal|"/decoupled_endpoint"
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|OutMessageRecorder
argument_list|()
expr_stmt|;
name|in
operator|=
operator|new
name|InMessageRecorder
argument_list|()
expr_stmt|;
name|greeterBus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|greeterBus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Configured greeter client."
argument_list|)
expr_stmt|;
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|responses
index|[]
init|=
name|cast
argument_list|(
operator|new
name|Response
index|[
literal|3
index|]
argument_list|)
decl_stmt|;
name|responses
index|[
literal|0
index|]
operator|=
name|greeter
operator|.
name|greetMeAsync
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|responses
index|[
literal|1
index|]
operator|=
name|greeter
operator|.
name|greetMeAsync
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|responses
index|[
literal|2
index|]
operator|=
name|greeter
operator|.
name|greetMeAsync
argument_list|(
literal|"three"
argument_list|)
expr_stmt|;
name|verifyMissingResponse
argument_list|(
name|responses
argument_list|)
expr_stmt|;
name|control
operator|.
name|stopGreeter
argument_list|(
name|SERVER_LOSS_CFG
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Stopped greeter server"
argument_list|)
expr_stmt|;
name|out
operator|.
name|getOutboundMessages
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|in
operator|.
name|getInboundMessages
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|control
operator|.
name|startGreeter
argument_list|(
name|CFG
argument_list|)
expr_stmt|;
name|String
name|nl
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Restarted greeter server"
operator|+
name|nl
operator|+
name|nl
argument_list|)
expr_stmt|;
name|verifyServerRecovery
argument_list|(
name|responses
argument_list|)
expr_stmt|;
name|greeterBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|stopGreeter
argument_list|(
name|CFG
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|void
name|verifyMissingResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|responses
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
name|awaitMessages
argument_list|(
literal|4
argument_list|,
literal|6
argument_list|,
literal|20000
argument_list|)
expr_stmt|;
comment|// wait another while to prove that response to second request is indeed lost
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|int
name|nDone
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|responses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|responses
index|[
name|i
index|]
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|nDone
operator|++
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Unexpected number of responses already received."
argument_list|,
literal|2
argument_list|,
name|nDone
argument_list|)
expr_stmt|;
name|MessageFlow
name|mf
init|=
operator|new
name|MessageFlow
argument_list|(
name|out
operator|.
name|getOutboundMessages
argument_list|()
argument_list|,
name|in
operator|.
name|getInboundMessages
argument_list|()
argument_list|)
decl_stmt|;
name|String
index|[]
name|expectedActions
init|=
operator|new
name|String
index|[]
block|{
name|RMConstants
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
comment|// mf.verifyMessageNumbers(new String[] {null, "1", "2", "3"}, true);
comment|// mf.verifyAcknowledgements(new boolean[] {false, false, true, false}, true);
name|mf
operator|.
name|verifyPartialResponses
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|mf
operator|.
name|purgePartialResponses
argument_list|()
expr_stmt|;
name|expectedActions
operator|=
operator|new
name|String
index|[]
block|{
name|RMConstants
operator|.
name|getCreateSequenceResponseAction
argument_list|()
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
comment|// mf.verifyMessageNumbers(new String[] {null, "1", "3"}, false);
comment|// mf.verifyAcknowledgements(new boolean[] {false, true, true}, false);
block|}
name|void
name|verifyServerRecovery
parameter_list|(
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|responses
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
comment|// wait until all messages have received their responses
name|int
name|nDone
init|=
literal|0
decl_stmt|;
name|long
name|waited
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|waited
operator|<
literal|20
condition|)
block|{
name|nDone
operator|=
literal|0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|responses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|responses
index|[
name|i
index|]
operator|.
name|isDone
argument_list|()
condition|)
block|{
name|nDone
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nDone
operator|==
literal|3
condition|)
block|{
break|break;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|waited
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Not all responses have been received."
argument_list|,
literal|3
argument_list|,
name|nDone
argument_list|)
expr_stmt|;
comment|// verify that all inbound messages are resent responses
synchronized|synchronized
init|(
name|this
init|)
block|{
name|MessageFlow
name|mf
init|=
operator|new
name|MessageFlow
argument_list|(
name|out
operator|.
name|getOutboundMessages
argument_list|()
argument_list|,
name|in
operator|.
name|getInboundMessages
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|nOut
init|=
name|out
operator|.
name|getOutboundMessages
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|nIn
init|=
name|in
operator|.
name|getInboundMessages
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected outbound message(s)"
argument_list|,
literal|0
argument_list|,
name|nOut
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|nIn
operator|>=
literal|1
argument_list|)
expr_stmt|;
name|String
index|[]
name|expectedActions
init|=
operator|new
name|String
index|[
name|nIn
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nIn
condition|;
name|i
operator|++
control|)
block|{
name|expectedActions
index|[
name|i
index|]
operator|=
name|GREETME_RESPONSE_ACTION
expr_stmt|;
block|}
name|mf
operator|.
name|verifyActions
argument_list|(
name|expectedActions
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
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
name|out
argument_list|,
name|in
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
parameter_list|<
name|T
parameter_list|>
name|Response
argument_list|<
name|T
argument_list|>
index|[]
name|cast
parameter_list|(
name|Response
index|[]
name|val
parameter_list|)
block|{
return|return
operator|(
name|Response
argument_list|<
name|T
argument_list|>
index|[]
operator|)
name|val
return|;
block|}
block|}
end_class

end_unit

