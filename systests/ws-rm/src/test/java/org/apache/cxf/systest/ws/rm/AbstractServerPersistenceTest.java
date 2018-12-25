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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests the addition of WS-RM properties to application messages and the  * exchange of WS-RM protocol messages.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractServerPersistenceTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
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
specifier|private
name|Bus
name|greeterBus
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
operator|new
name|Server
argument_list|(
name|args
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
specifier|abstract
name|String
name|getPort
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getDecoupledPort
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|String
name|getPrefix
parameter_list|()
function_decl|;
specifier|public
specifier|static
name|void
name|startServers
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
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
name|pfx
operator|+
literal|"-recovery"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
name|pfx
operator|+
literal|"-greeter"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
name|ConnectionHelper
operator|.
name|setKeepAliveConnection
argument_list|(
name|control
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"db.name"
argument_list|,
name|getPrefix
argument_list|()
operator|+
literal|"-recovery"
argument_list|)
expr_stmt|;
name|greeterBus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|CFG
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
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|60000
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
name|ConnectionHelper
operator|.
name|setKeepAliveConnection
argument_list|(
name|greeter
argument_list|,
literal|false
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
name|getDecoupledPort
argument_list|()
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
index|[]
name|responses
init|=
name|cast
argument_list|(
operator|new
name|Response
index|[
literal|4
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
name|responses
index|[
literal|3
index|]
operator|=
name|greeter
operator|.
name|greetMeAsync
argument_list|(
literal|"four"
argument_list|)
expr_stmt|;
name|verifyRetransmissionQueue
argument_list|()
expr_stmt|;
name|verifyAcknowledgementRange
argument_list|(
literal|1
argument_list|,
literal|4
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
index|[]
name|responses
parameter_list|)
throws|throws
name|Exception
block|{
name|awaitMessages
argument_list|(
literal|5
argument_list|,
literal|3
argument_list|,
literal|25000
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
literal|3
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
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|NAMESPACE_URI
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
name|RM10Constants
operator|.
name|CREATE_SEQUENCE_ACTION
block|,
name|GREETME_ACTION
block|,
name|GREETME_ACTION
block|,
name|GREETME_ACTION
block|,
name|RM10Constants
operator|.
name|SEQUENCE_ACKNOWLEDGMENT_ACTION
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
comment|//        mf.verifyPartialResponses(5);
comment|//        mf.purgePartialResponses();
name|expectedActions
operator|=
operator|new
name|String
index|[]
block|{
name|RM10Constants
operator|.
name|CREATE_SEQUENCE_RESPONSE_ACTION
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
index|[]
name|responses
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
literal|30
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
operator|-
literal|1
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
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|NAMESPACE_URI
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
name|void
name|verifyRetransmissionQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|awaitMessages
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|60000
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|boolean
name|empty
init|=
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
while|while
condition|(
name|count
operator|<
literal|50
operator|&&
operator|!
name|empty
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|empty
operator|=
name|greeterBus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Retransmission Queue is not empty"
argument_list|,
name|empty
argument_list|)
expr_stmt|;
block|}
name|void
name|verifyAcknowledgementRange
parameter_list|(
name|long
name|lower
parameter_list|,
name|long
name|higher
parameter_list|)
throws|throws
name|Exception
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
argument_list|,
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|)
decl_stmt|;
name|mf
operator|.
name|verifyAcknowledgementRange
argument_list|(
name|lower
argument_list|,
name|higher
argument_list|)
expr_stmt|;
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
literal|20000
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
argument_list|<
name|?
argument_list|>
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

