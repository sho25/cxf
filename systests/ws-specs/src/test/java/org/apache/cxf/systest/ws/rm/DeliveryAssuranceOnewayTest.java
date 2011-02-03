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
name|ArrayList
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
name|List
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
name|XMLUtils
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
name|test
operator|.
name|TestUtilities
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
comment|/**  * Tests the operation of InOrder delivery assurance for one-way messages to the server.  */
end_comment

begin_class
specifier|public
class|class
name|DeliveryAssuranceOnewayTest
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
name|DeliveryAssuranceOnewayTest
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
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|DeliveryAssuranceOnewayTest
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
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setProps
parameter_list|()
throws|throws
name|Exception
block|{
name|TestUtilities
operator|.
name|setKeepAliveSystemProperty
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
block|{
name|TestUtilities
operator|.
name|recoverKeepAliveSystemProperty
argument_list|()
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
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
comment|/*    @Test         public void testAtLeastOnce() throws Exception {         testOnewayAtLeastOnce(null);     }          @Test         public void testAtLeastOnceAsyncExecutor() throws Exception {         testOnewayAtLeastOnce(Executors.newSingleThreadExecutor());     }       private void testOnewayAtLeastOnce(Executor executor) throws Exception {         init("org/apache/cxf/systest/ws/rm/atleastonce.xml", executor);                  greeterBus.getOutInterceptors().add(new MessageLossSimulator());         RMManager manager = greeterBus.getExtension(RMManager.class);         manager.getRMAssertion().getBaseRetransmissionInterval().setMilliseconds(new BigInteger("2000"));         String[] callArgs = new String[] {"one", "two", "three", "four"};         for (int i = 0; i< callArgs.length; i++) {             greeter.greetMeOneWay(callArgs[i]);         }                  awaitMessages(callArgs.length + 2, 3000, 60000);         List<String> actualArgs = GreeterProvider.CALL_ARGS;         assertTrue("Too few messages", callArgs.length<= actualArgs.size());         for (int i = 0; i< callArgs.length; i++) {             boolean match = false;             for (int j = 0; j< actualArgs.size(); j++) {                 if (actualArgs.get(j).equals(callArgs[i])) {                     match = true;                     break;                 }             }             if (!match) {                 fail("No match for request " + callArgs[i]);             }         }              }      @Test         public void testAtMostOnce() throws Exception {         testOnewayAtMostOnce(null);     }          @Test         public void testAtMostOnceAsyncExecutor() throws Exception {         testOnewayAtMostOnce(Executors.newSingleThreadExecutor());     }       private void testOnewayAtMostOnce(Executor executor) throws Exception {         init("org/apache/cxf/systest/ws/rm/atmostonce.xml", executor);                  greeterBus.getOutInterceptors().add(new MessageLossSimulator());         RMManager manager = greeterBus.getExtension(RMManager.class);         manager.getRMAssertion().getBaseRetransmissionInterval().setMilliseconds(new BigInteger("2000"));         String[] callArgs = new String[] {"one", "two", "three", "four"};         for (int i = 0; i< callArgs.length; i++) {             greeter.greetMeOneWay(callArgs[i]);         }                  awaitMessages(callArgs.length, 3000, 60000);         List<String> actualArgs = GreeterProvider.CALL_ARGS;         assertTrue("Too many messages", callArgs.length>= actualArgs.size());         for (int i = 0; i< actualArgs.size() - 1; i++) {             for (int j = i + 1; j< actualArgs.size(); j++) {                 if (actualArgs.get(j).equals(actualArgs.get(i))) {                     fail("Message received more than once " + callArgs[i]);                 }             }         }              }      @Test         public void testExactlyOnce() throws Exception {         testOnewayExactlyOnce(null);     }          @Test         public void testExactlyOnceAsyncExecutor() throws Exception {         testOnewayExactlyOnce(Executors.newSingleThreadExecutor());     }       private void testOnewayExactlyOnce(Executor executor) throws Exception {         init("org/apache/cxf/systest/ws/rm/exactlyonce.xml", executor);                  greeterBus.getOutInterceptors().add(new MessageLossSimulator());         RMManager manager = greeterBus.getExtension(RMManager.class);         manager.getRMAssertion().getBaseRetransmissionInterval().setMilliseconds(new BigInteger("2000"));         String[] callArgs = new String[] {"one", "two", "three", "four"};         for (int i = 0; i< callArgs.length; i++) {             greeter.greetMeOneWay(callArgs[i]);         }                  awaitMessages(callArgs.length, 3000, 60000);         List<String> actualArgs = GreeterProvider.CALL_ARGS;         assertEquals("Wrong message count", callArgs.length, actualArgs.size());         for (int i = 0; i< callArgs.length; i++) {             boolean match = false;             for (int j = 0; j< actualArgs.size(); j++) {                 if (actualArgs.get(j).equals(callArgs[i])) {                     match = true;                     break;                 }             }             if (!match) {                 fail("No match for request " + callArgs[i]);             }         }              }*/
annotation|@
name|Test
specifier|public
name|void
name|testInOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayInOrder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInOrderAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayInOrder
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
name|testOnewayInOrder
parameter_list|(
name|Executor
name|executor
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/inorder.xml"
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
literal|"2000"
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|callArgs
init|=
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
name|callArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|callArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|awaitMessages
argument_list|(
name|callArgs
operator|.
name|length
operator|-
literal|2
argument_list|,
literal|3000
argument_list|,
literal|60000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|actualArgs
init|=
name|GreeterProvider
operator|.
name|CALL_ARGS
decl_stmt|;
name|int
name|argNum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|actual
range|:
name|actualArgs
control|)
block|{
while|while
condition|(
name|argNum
operator|<
name|callArgs
operator|.
name|length
operator|&&
operator|!
name|actual
operator|.
name|equals
argument_list|(
name|callArgs
index|[
name|argNum
index|]
argument_list|)
condition|)
block|{
name|argNum
operator|++
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Message out of order"
argument_list|,
name|argNum
operator|<
name|callArgs
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtMostOnceInOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayAtMostOnceInOrder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAtMostOnceInOrderAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|testOnewayAtMostOnceInOrder
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
name|testOnewayAtMostOnceInOrder
parameter_list|(
name|Executor
name|executor
parameter_list|)
throws|throws
name|Exception
block|{
name|init
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/atmostonce-inorder.xml"
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
literal|"2000"
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|callArgs
init|=
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
name|callArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|callArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|awaitMessages
argument_list|(
name|callArgs
operator|.
name|length
operator|-
literal|2
argument_list|,
literal|3000
argument_list|,
literal|60000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|actualArgs
init|=
name|GreeterProvider
operator|.
name|CALL_ARGS
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Too many messages"
argument_list|,
name|callArgs
operator|.
name|length
operator|>=
name|actualArgs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|argNum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|actual
range|:
name|actualArgs
control|)
block|{
while|while
condition|(
name|argNum
operator|<
name|callArgs
operator|.
name|length
operator|&&
operator|!
name|actual
operator|.
name|equals
argument_list|(
name|callArgs
index|[
name|argNum
index|]
argument_list|)
condition|)
block|{
name|argNum
operator|++
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Message out of order"
argument_list|,
name|argNum
operator|<
name|callArgs
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
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
literal|"2000"
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|callArgs
init|=
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
name|callArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|callArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|awaitMessages
argument_list|(
name|callArgs
operator|.
name|length
argument_list|,
literal|3000
argument_list|,
literal|60000
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|actualArgs
init|=
name|GreeterProvider
operator|.
name|CALL_ARGS
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong number of messages"
argument_list|,
name|callArgs
operator|.
name|length
argument_list|,
name|actualArgs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|argNum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|actual
range|:
name|actualArgs
control|)
block|{
while|while
condition|(
name|argNum
operator|<
name|callArgs
operator|.
name|length
operator|&&
operator|!
name|actual
operator|.
name|equals
argument_list|(
name|callArgs
index|[
name|argNum
index|]
argument_list|)
condition|)
block|{
name|argNum
operator|++
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Message out of order"
argument_list|,
name|argNum
operator|<
name|callArgs
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
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
synchronized|synchronized
init|(
name|GreeterProvider
operator|.
name|CALL_ARGS
init|)
block|{
name|GreeterProvider
operator|.
name|CALL_ARGS
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
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
literal|true
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
comment|/**      * @param nExpectedIn number of messages to wait for      * @param delay added delay before return (in case more are coming)      * @param timeout maximum time to wait for expected messages      */
specifier|private
name|void
name|awaitMessages
parameter_list|(
name|int
name|nExpectedIn
parameter_list|,
name|int
name|delay
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
name|int
name|waited
init|=
literal|0
decl_stmt|;
name|int
name|nIn
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|waited
operator|<=
name|timeout
condition|)
block|{
synchronized|synchronized
init|(
name|GreeterProvider
operator|.
name|CALL_ARGS
init|)
block|{
name|nIn
operator|=
name|GreeterProvider
operator|.
name|CALL_ARGS
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|nIn
operator|>=
name|nExpectedIn
condition|)
block|{
break|break;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|waited
operator|+=
literal|100
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
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
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|CALL_ARGS
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|XMLUtils
operator|.
name|fromSource
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"ns"
argument_list|,
literal|"http://cxf.apache.org/greeter_control/types"
argument_list|)
expr_stmt|;
name|XPathUtils
name|xp
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
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
synchronized|synchronized
init|(
name|CALL_ARGS
init|)
block|{
name|CALL_ARGS
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
synchronized|synchronized
init|(
name|CALL_ARGS
init|)
block|{
name|CALL_ARGS
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
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
block|}
block|}
end_class

end_unit

