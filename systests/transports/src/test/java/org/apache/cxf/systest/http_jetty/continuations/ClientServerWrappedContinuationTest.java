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
name|http_jetty
operator|.
name|continuations
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|ArrayBlockingQueue
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
name|CountDownLatch
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
name|ThreadPoolExecutor
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
name|common
operator|.
name|AbstractClientServerTestBase
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

begin_class
specifier|public
class|class
name|ClientServerWrappedContinuationTest
extends|extends
name|AbstractClientServerTestBase
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
name|HTTPS_PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE
init|=
literal|"org/apache/cxf/systest/http_jetty/continuations/cxf.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_HTTPS_CONFIG_FILE
init|=
literal|"org/apache/cxf/systest/http_jetty/continuations/cxf_https.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVER_CONFIG_FILE
init|=
literal|"org/apache/cxf/systest/http_jetty/continuations/jaxws-server.xml"
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
name|SERVER_CONFIG_FILE
argument_list|)
decl_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|HelloImplWithWrapppedContinuation
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/hellocontinuation"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"https://localhost:"
operator|+
name|HTTPS_PORT
operator|+
literal|"/securecontinuation"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpWrappedContinuations
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
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|CLIENT_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|,
literal|"HelloContinuationService"
argument_list|)
decl_stmt|;
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/hellocontinuation?wsdl"
argument_list|)
decl_stmt|;
name|HelloContinuationService
name|service
init|=
operator|new
name|HelloContinuationService
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
specifier|final
name|HelloContinuation
name|helloPort
init|=
name|service
operator|.
name|getHelloContinuationPort
argument_list|()
decl_stmt|;
name|doTest
argument_list|(
name|helloPort
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
annotation|@
name|Test
specifier|public
name|void
name|testHttpsWrappedContinuations
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
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|CLIENT_HTTPS_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/jaxws"
argument_list|,
literal|"HelloContinuationService"
argument_list|)
decl_stmt|;
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
literal|"https://localhost:"
operator|+
name|HTTPS_PORT
operator|+
literal|"/securecontinuation?wsdl"
argument_list|)
decl_stmt|;
name|HelloContinuationService
name|service
init|=
operator|new
name|HelloContinuationService
argument_list|(
name|wsdlURL
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
specifier|final
name|HelloContinuation
name|helloPort
init|=
name|service
operator|.
name|getHelloContinuationPort
argument_list|()
decl_stmt|;
name|doTest
argument_list|(
name|helloPort
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
specifier|private
name|void
name|doTest
parameter_list|(
specifier|final
name|HelloContinuation
name|helloPort
parameter_list|)
throws|throws
name|Exception
block|{
name|ThreadPoolExecutor
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|10
argument_list|,
literal|10
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
literal|6
argument_list|)
argument_list|)
decl_stmt|;
name|CountDownLatch
name|startSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|CountDownLatch
name|controlDoneSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|CountDownLatch
name|helloDoneSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|ControlWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Fred"
argument_list|,
name|startSignal
argument_list|,
name|controlDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|HelloWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Fred"
argument_list|,
literal|""
argument_list|,
name|startSignal
argument_list|,
name|helloDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|ControlWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Barry"
argument_list|,
name|startSignal
argument_list|,
name|controlDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|HelloWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Barry"
argument_list|,
literal|"Jameson"
argument_list|,
name|startSignal
argument_list|,
name|helloDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|ControlWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Harry"
argument_list|,
name|startSignal
argument_list|,
name|controlDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|HelloWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Harry"
argument_list|,
literal|""
argument_list|,
name|startSignal
argument_list|,
name|helloDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|ControlWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Rob"
argument_list|,
name|startSignal
argument_list|,
name|controlDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|HelloWorker
argument_list|(
name|helloPort
argument_list|,
literal|"Rob"
argument_list|,
literal|"Davidson"
argument_list|,
name|startSignal
argument_list|,
name|helloDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|ControlWorker
argument_list|(
name|helloPort
argument_list|,
literal|"James"
argument_list|,
name|startSignal
argument_list|,
name|controlDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|HelloWorker
argument_list|(
name|helloPort
argument_list|,
literal|"James"
argument_list|,
literal|"ServiceMix"
argument_list|,
name|startSignal
argument_list|,
name|helloDoneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|startSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|controlDoneSignal
operator|.
name|await
argument_list|(
literal|100
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|helloDoneSignal
operator|.
name|await
argument_list|(
literal|100
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Not all invocations have been resumed"
argument_list|,
literal|0
argument_list|,
name|controlDoneSignal
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Not all invocations have completed"
argument_list|,
literal|0
argument_list|,
name|helloDoneSignal
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
name|helloPort
operator|.
name|sayHi
argument_list|(
literal|"Dan1"
argument_list|,
literal|"to:100"
argument_list|)
expr_stmt|;
name|helloPort
operator|.
name|sayHi
argument_list|(
literal|"Dan2"
argument_list|,
literal|"to:100"
argument_list|)
expr_stmt|;
name|helloPort
operator|.
name|sayHi
argument_list|(
literal|"Dan3"
argument_list|,
literal|"to:100"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

