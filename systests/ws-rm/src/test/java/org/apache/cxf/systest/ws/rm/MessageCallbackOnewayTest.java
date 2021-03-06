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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|BlockingQueue
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
name|Executor
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
name|Executors
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
name|LinkedBlockingQueue
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
name|TimeUnit
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
name|jws
operator|.
name|WebService
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
name|Source
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
name|stream
operator|.
name|StreamSource
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Provider
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
operator|.
name|Mode
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
name|ServiceMode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|helpers
operator|.
name|XPathUtils
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
name|staxutils
operator|.
name|StaxUtils
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
name|ws
operator|.
name|rm
operator|.
name|MessageCallback
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
name|RMMessageConstants
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
name|assertNull
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
comment|/**  * Tests the operation of MessageCallback for one-way messages to the server.  */
end_comment

begin_class
specifier|public
class|class
name|MessageCallbackOnewayTest
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
name|MessageCallbackOnewayTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GREETER_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/GreeterPort"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|RETRANSMISSION_INTERVAL
init|=
literal|500L
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
name|MessageCallbackOnewayTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|serverBus
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
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
name|RecordingMessageCallback
name|callback
decl_stmt|;
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
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
try|try
block|{
name|stopServer
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtLeastOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayAtLeastOnce
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtLeastOnceAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayAtLeastOnce
argument_list|(
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testOnewayAtLeastOnce
parameter_list|(
name|Executor
name|executor
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/atleastonce.xml"
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|greeterBus
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
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|RETRANSMISSION_INTERVAL
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|arg
range|:
operator|new
name|String
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|,
literal|"three"
block|,
literal|"four"
block|}
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|waitAndVerify
argument_list|(
literal|8
argument_list|,
literal|1000L
argument_list|,
literal|10000L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtMostOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayAtMostOnce
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtMostOnceAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayAtMostOnce
argument_list|(
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testOnewayAtMostOnce
parameter_list|(
name|Executor
name|executor
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/atmostonce.xml"
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|greeterBus
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
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|RETRANSMISSION_INTERVAL
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|arg
range|:
operator|new
name|String
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|,
literal|"three"
block|,
literal|"four"
block|}
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|waitAndVerify
argument_list|(
literal|8
argument_list|,
literal|1000L
argument_list|,
literal|10000L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExactlyOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayExactlyOnce
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExactlyOnceAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayExactlyOnce
argument_list|(
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testOnewayExactlyOnce
parameter_list|(
name|Executor
name|executor
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/exactlyonce.xml"
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|greeterBus
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
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|RETRANSMISSION_INTERVAL
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|arg
range|:
operator|new
name|String
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|,
literal|"three"
block|,
literal|"four"
block|}
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|waitAndVerify
argument_list|(
literal|8
argument_list|,
literal|1000L
argument_list|,
literal|10000L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExactlyOnceInOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayExactlyOnceInOrder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExactlyOnceInOrderAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayExactlyOnceInOrder
argument_list|(
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testOnewayExactlyOnceInOrder
parameter_list|(
name|Executor
name|executor
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/exactlyonce-inorder.xml"
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|greeterBus
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
name|manager
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBaseRetransmissionInterval
argument_list|(
name|RETRANSMISSION_INTERVAL
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|arg
range|:
operator|new
name|String
index|[]
block|{
literal|"one"
block|,
literal|"two"
block|,
literal|"three"
block|,
literal|"four"
block|}
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|waitAndVerify
argument_list|(
literal|8
argument_list|,
literal|1000L
argument_list|,
literal|10000L
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
name|Executor
name|executor
parameter_list|)
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|initServer
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
name|executor
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initServer
parameter_list|(
name|SpringBusFactory
name|bf
parameter_list|,
name|String
name|cfgResource
parameter_list|)
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
name|serverBus
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
name|serverBus
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Initialised bus "
operator|+
name|serverBus
operator|+
literal|" with cfg file resource: "
operator|+
name|cfgResource
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"serverBus inInterceptors: "
operator|+
name|serverBus
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|GREETER_ADDRESS
argument_list|,
operator|new
name|GreeterProvider
argument_list|()
argument_list|)
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
block|}
specifier|private
name|void
name|initProxy
parameter_list|(
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
literal|false
argument_list|)
expr_stmt|;
name|callback
operator|=
operator|new
name|RecordingMessageCallback
argument_list|()
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
name|RMMessageConstants
operator|.
name|RM_CLIENT_CALLBACK
argument_list|,
name|callback
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
name|greeterBus
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|stopServer
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|endpoint
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping Greeter endpoint"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"No endpoint active."
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|=
literal|null
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|serverBus
condition|)
block|{
name|serverBus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|serverBus
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|WebService
argument_list|(
name|serviceName
operator|=
literal|"GreeterService"
argument_list|,
name|portName
operator|=
literal|"GreeterPort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/greeter_control"
argument_list|,
name|wsdlLocation
operator|=
literal|"/wsdl/greeter_control.wsdl"
argument_list|)
annotation|@
name|ServiceMode
argument_list|(
name|Mode
operator|.
name|PAYLOAD
argument_list|)
specifier|public
specifier|static
class|class
name|GreeterProvider
implements|implements
name|Provider
argument_list|<
name|Source
argument_list|>
block|{
specifier|public
name|Source
name|invoke
parameter_list|(
name|Source
name|obj
parameter_list|)
block|{
name|Node
name|el
decl_stmt|;
try|try
block|{
name|el
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|el
operator|instanceof
name|Document
condition|)
block|{
name|el
operator|=
operator|(
operator|(
name|Document
operator|)
name|el
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
name|XPathUtils
name|xp
init|=
operator|new
name|XPathUtils
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ns"
argument_list|,
literal|"http://cxf.apache.org/greeter_control/types"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|s
init|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:greetMe/ns:requestType"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|s
operator|=
operator|(
name|String
operator|)
name|xp
operator|.
name|getValue
argument_list|(
literal|"/ns:greetMeOneWay/ns:requestType"
argument_list|,
name|el
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|resp
init|=
literal|"<greetMeResponse "
operator|+
literal|"xmlns=\"http://cxf.apache.org/greeter_control/types\">"
operator|+
literal|"<responseType>"
operator|+
name|s
operator|.
name|toUpperCase
argument_list|()
operator|+
literal|"</responseType>"
operator|+
literal|"</greetMeResponse>"
decl_stmt|;
return|return
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|resp
argument_list|)
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|RecordingMessageCallback
implements|implements
name|MessageCallback
block|{
specifier|private
name|BlockingQueue
argument_list|<
name|Callback
argument_list|>
name|callbacks
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|messageAccepted
parameter_list|(
name|String
name|seqId
parameter_list|,
name|long
name|msgNum
parameter_list|)
block|{
name|callbacks
operator|.
name|offer
argument_list|(
operator|new
name|Callback
argument_list|(
literal|true
argument_list|,
name|msgNum
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|messageAcknowledged
parameter_list|(
name|String
name|seqId
parameter_list|,
name|long
name|msgNum
parameter_list|)
block|{
name|callbacks
operator|.
name|offer
argument_list|(
operator|new
name|Callback
argument_list|(
literal|false
argument_list|,
name|msgNum
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**          * Wait for expected number of callbacks. Checks that all callbacks are          * received, that messages are accepted in order, and that each message          * is accepted before it is acknowledged (order of acknowledgements          * doesn't really matter).          *          * @param count          *            expected number of callbacks          * @param delay          *            extra time to wait after expected number received (in case          *            more are coming)          * @param timeout          *            maximum time to wait, in milliseconds          * @throws InterruptedException           */
specifier|public
name|void
name|waitAndVerify
parameter_list|(
name|int
name|count
parameter_list|,
name|long
name|delay
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Set
argument_list|<
name|Long
argument_list|>
name|acks
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|long
name|nextNum
init|=
literal|1L
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
name|count
condition|;
operator|++
name|i
control|)
block|{
name|Callback
name|cb
init|=
name|callbacks
operator|.
name|poll
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Timeout"
argument_list|,
name|cb
argument_list|)
expr_stmt|;
if|if
condition|(
name|cb
operator|.
name|isAccept
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
name|nextNum
operator|++
argument_list|,
name|cb
operator|.
name|getMsgNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|cb
operator|.
name|getMsgNumber
argument_list|()
operator|<
name|nextNum
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|acks
operator|.
name|add
argument_list|(
name|cb
operator|.
name|getMsgNumber
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|assertNull
argument_list|(
literal|"Unexpected callback"
argument_list|,
name|callbacks
operator|.
name|poll
argument_list|(
name|delay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|Callback
block|{
specifier|private
specifier|final
name|boolean
name|accept
decl_stmt|;
specifier|private
specifier|final
name|long
name|msgNumber
decl_stmt|;
name|Callback
parameter_list|(
name|boolean
name|acc
parameter_list|,
name|long
name|msgNum
parameter_list|)
block|{
name|accept
operator|=
name|acc
expr_stmt|;
name|msgNumber
operator|=
name|msgNum
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAccept
parameter_list|()
block|{
return|return
name|accept
return|;
block|}
specifier|public
name|long
name|getMsgNumber
parameter_list|()
block|{
return|return
name|msgNumber
return|;
block|}
block|}
block|}
end_class

end_unit

