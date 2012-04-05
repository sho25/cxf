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
name|Collection
import|;
end_import

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
name|DestinationSequence
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
name|RMUtils
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
name|SourceSequence
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
name|RMMessage
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
name|RMStore
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
name|AfterClass
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
name|ClientPersistenceTest
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
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ClientPersistenceTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Greeter
name|greeter
decl_stmt|;
specifier|private
name|OutMessageRecorder
name|out
decl_stmt|;
specifier|private
name|InMessageRecorder
name|in
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
literal|"/org/apache/cxf/systest/ws/rm/persistent.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|LoggingInInterceptor
name|logIn
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
name|logIn
argument_list|)
expr_stmt|;
name|LoggingOutInterceptor
name|logOut
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|logOut
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|logOut
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
name|getRMAssertion
argument_list|()
operator|.
name|getBaseRetransmissionInterval
argument_list|()
operator|.
name|setMilliseconds
argument_list|(
operator|new
name|Long
argument_list|(
literal|60000
argument_list|)
argument_list|)
expr_stmt|;
name|GreeterImpl
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
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"schema-validation-enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
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
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|()
expr_stmt|;
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
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
name|RMTxStore
operator|.
name|DEFAULT_DATABASE_NAME
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
try|try
block|{
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
literal|"derby-server"
argument_list|)
expr_stmt|;
block|}
else|else
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
finally|finally
block|{
if|if
condition|(
name|derbyHome
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"derby.system.home"
argument_list|)
expr_stmt|;
block|}
else|else
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
block|}
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|tearDownOnce
parameter_list|()
block|{
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
name|RMTxStore
operator|.
name|DEFAULT_DATABASE_NAME
argument_list|,
literal|false
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
name|startClient
argument_list|()
expr_stmt|;
name|populateStore
argument_list|()
expr_stmt|;
name|verifyStorePopulation
argument_list|()
expr_stmt|;
name|stopClient
argument_list|()
expr_stmt|;
name|startClient
argument_list|()
expr_stmt|;
name|populateStoreAfterRestart
argument_list|()
expr_stmt|;
name|recover
argument_list|()
expr_stmt|;
name|verifyRecovery
argument_list|()
expr_stmt|;
block|}
name|void
name|startClient
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
literal|"/org/apache/cxf/systest/ws/rm/persistent.xml"
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
operator|(
operator|(
name|BindingProvider
operator|)
name|greeter
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"schema-validation-enabled"
argument_list|,
name|Boolean
operator|.
name|TRUE
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
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
name|void
name|populateStore
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
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
name|Long
argument_list|(
literal|60000
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|MessageLossSimulator
argument_list|()
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"three"
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"four"
argument_list|)
expr_stmt|;
name|awaitMessages
argument_list|(
literal|5
argument_list|,
literal|3
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
comment|// sent create seq + 4 app messages and losing 2 app messages
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
name|CREATE_SEQUENCE_ACTION
block|,
name|GREETMEONEWAY_ACTION
block|,
name|GREETMEONEWAY_ACTION
block|,
name|GREETMEONEWAY_ACTION
block|,
name|GREETMEONEWAY_ACTION
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
block|,
literal|"4"
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
index|[
literal|5
index|]
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// as 2 messages being lost, received seq ack and 2 ack messages
name|mf
operator|.
name|verifyMessages
argument_list|(
literal|3
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
name|RM10Constants
operator|.
name|CREATE_SEQUENCE_RESPONSE_ACTION
block|,
name|RM10Constants
operator|.
name|SEQUENCE_ACKNOWLEDGMENT_ACTION
block|,
name|RM10Constants
operator|.
name|SEQUENCE_ACKNOWLEDGMENT_ACTION
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
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|void
name|verifyStorePopulation
parameter_list|()
block|{
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
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMStore
name|store
init|=
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|store
argument_list|)
expr_stmt|;
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
name|String
name|id
init|=
name|RMUtils
operator|.
name|getEndpointIdentifier
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DestinationSequence
argument_list|>
name|dss
init|=
name|store
operator|.
name|getDestinationSequences
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dss
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SourceSequence
argument_list|>
name|sss
init|=
name|store
operator|.
name|getSourceSequences
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sss
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|RMMessage
argument_list|>
name|msgs
init|=
name|store
operator|.
name|getMessages
argument_list|(
name|sss
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|msgs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|msgs
operator|=
name|store
operator|.
name|getMessages
argument_list|(
name|sss
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|msgs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|stopClient
parameter_list|()
block|{
comment|// ClientProxy.getClient(greeter).destroy();
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|void
name|populateStoreAfterRestart
parameter_list|()
throws|throws
name|Exception
block|{
name|bus
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
name|Long
argument_list|(
literal|60000
argument_list|)
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"five"
argument_list|)
expr_stmt|;
comment|// force at least two outbound messages, since can't always count on three
name|awaitMessages
argument_list|(
literal|1
argument_list|,
literal|2
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
comment|// sent 1 app message and no create seq messag this time
name|mf
operator|.
name|verifyMessages
argument_list|(
literal|1
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
name|GREETMEONEWAY_ACTION
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
literal|"5"
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
index|[
literal|1
index|]
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|mf
operator|.
name|verifyMessages
argument_list|(
literal|2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// we can't reliably predict how the three remaining messages are acknowledged
comment|//        expectedActions = new String[] {RM10Constants.SEQUENCE_ACKNOWLEDGMENT_ACTION,
comment|//            RM10Constants.SEQUENCE_ACKNOWLEDGMENT_ACTION,
comment|//            null};
comment|//        mf.verifyActions(expectedActions, false);
comment|//        mf.verifyAcknowledgements(new boolean[]{true, true, false}, false);
comment|// verify the final ack range to be complete
name|mf
operator|.
name|verifyAcknowledgementRange
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
name|void
name|recover
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do nothing - resends should happen in the background
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Recovered messages should have been resent by now."
argument_list|)
expr_stmt|;
block|}
name|void
name|verifyRecovery
parameter_list|()
throws|throws
name|Exception
block|{
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
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMStore
name|store
init|=
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|store
argument_list|)
expr_stmt|;
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
name|String
name|id
init|=
name|RMUtils
operator|.
name|getEndpointIdentifier
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DestinationSequence
argument_list|>
name|dss
init|=
name|store
operator|.
name|getDestinationSequences
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dss
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SourceSequence
argument_list|>
name|sss
init|=
name|store
operator|.
name|getSourceSequences
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sss
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|store
operator|.
name|getMessages
argument_list|(
name|sss
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|i
operator|<
literal|10
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|store
operator|.
name|getMessages
argument_list|(
name|sss
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|store
operator|.
name|getMessages
argument_list|(
name|sss
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|size
argument_list|()
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
block|}
end_class

end_unit

