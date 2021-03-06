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
name|transport
operator|.
name|http
operator|.
name|asyncclient
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
name|Collections
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
name|ExecutionException
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|AsyncHandler
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduitFactory
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
name|workqueue
operator|.
name|AutomaticWorkQueueImpl
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
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
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
name|hello_world_soap_http
operator|.
name|SOAPService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeLaterResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
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
name|Ignore
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|AsyncHTTPConduitTest
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
name|AsyncHTTPConduitTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT_INV
init|=
name|allocatePort
argument_list|(
name|AsyncHTTPConduitTest
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILL_BUFFER
init|=
literal|"FillBuffer"
decl_stmt|;
specifier|static
name|Endpoint
name|ep
decl_stmt|;
specifier|static
name|String
name|request
decl_stmt|;
specifier|static
name|Greeter
name|g
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|b
init|=
name|createStaticBus
argument_list|()
decl_stmt|;
name|b
operator|.
name|setProperty
argument_list|(
name|AsyncHTTPConduit
operator|.
name|USE_ASYNC
argument_list|,
name|AsyncHTTPConduitFactory
operator|.
name|UseAsyncPolicy
operator|.
name|ALWAYS
argument_list|)
expr_stmt|;
name|b
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.transport.http.async.MAX_CONNECTIONS"
argument_list|,
literal|501
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|AsyncHTTPConduitFactory
name|hcf
init|=
operator|(
name|AsyncHTTPConduitFactory
operator|)
name|b
operator|.
name|getExtension
argument_list|(
name|HTTPConduitFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|501
argument_list|,
name|hcf
operator|.
name|maxConnections
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
literal|"/SoapContext/SoapPort"
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
argument_list|()
block|{
specifier|public
name|String
name|greetMeLater
parameter_list|(
name|long
name|cnt
parameter_list|)
block|{
comment|//use the continuations so the async client can
comment|//have a ton of connections, use less threads
comment|//
comment|//mimic a slow server by delaying somewhere between
comment|//1 and 2 seconds, with a preference of delaying the earlier
comment|//requests longer to create a sort of backlog/contention
comment|//with the later requests
name|ContinuationProvider
name|p
init|=
operator|(
name|ContinuationProvider
operator|)
name|getContext
argument_list|()
operator|.
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Continuation
name|c
init|=
name|p
operator|.
name|getContinuation
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|isNew
argument_list|()
condition|)
block|{
if|if
condition|(
name|cnt
operator|<
literal|0
condition|)
block|{
name|c
operator|.
name|suspend
argument_list|(
operator|-
name|cnt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|c
operator|.
name|suspend
argument_list|(
literal|2000
operator|-
operator|(
name|cnt
operator|%
literal|1000
operator|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
return|return
literal|"Hello, finally! "
operator|+
name|cnt
return|;
block|}
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
block|{
if|if
condition|(
name|me
operator|.
name|equals
argument_list|(
name|FILL_BUFFER
argument_list|)
condition|)
block|{
return|return
name|String
operator|.
name|join
argument_list|(
literal|""
argument_list|,
name|Collections
operator|.
name|nCopies
argument_list|(
literal|16093
argument_list|,
literal|" "
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|"Hello "
operator|+
name|me
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"NaNaNa"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
literal|50
condition|;
name|x
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|" NaNaNa "
argument_list|)
expr_stmt|;
block|}
name|request
operator|=
name|builder
operator|.
name|toString
argument_list|()
expr_stmt|;
name|URL
name|wsdl
init|=
name|AsyncHTTPConduitTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world_services.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|g
operator|=
name|service
operator|.
name|getSoapPort
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|g
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
operator|(
operator|(
name|java
operator|.
name|io
operator|.
name|Closeable
operator|)
name|g
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testResponseSameBufferSize
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|HTTPConduit
name|c
init|=
operator|(
name|HTTPConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|g
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|12000
argument_list|)
expr_stmt|;
try|try
block|{
name|g
operator|.
name|greetMe
argument_list|(
name|FILL_BUFFER
argument_list|)
expr_stmt|;
name|g
operator|.
name|greetMe
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|HTTPConduit
name|c
init|=
operator|(
name|HTTPConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|g
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|request
argument_list|,
name|g
operator|.
name|greetMeLater
argument_list|(
operator|-
literal|5000
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected!!!
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimeoutWithPropertySetting
parameter_list|()
throws|throws
name|Exception
block|{
operator|(
operator|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
operator|)
name|g
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"javax.xml.ws.client.receiveTimeout"
argument_list|,
literal|"3000"
argument_list|)
expr_stmt|;
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|request
argument_list|,
name|g
operator|.
name|greetMeLater
argument_list|(
operator|-
literal|5000
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected!!!
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimeoutAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|HTTPConduit
name|c
init|=
operator|(
name|HTTPConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|g
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|c
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
try|try
block|{
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|future
init|=
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
operator|-
literal|5000L
argument_list|)
decl_stmt|;
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected!!!
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimeoutAsyncWithPropertySetting
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
operator|(
operator|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
operator|)
name|g
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
literal|"javax.xml.ws.client.receiveTimeout"
argument_list|,
literal|"3000"
argument_list|)
expr_stmt|;
try|try
block|{
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|future
init|=
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
operator|-
literal|5000L
argument_list|)
decl_stmt|;
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected!!!
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConnectIssue
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT_INV
argument_list|)
expr_stmt|;
try|try
block|{
name|g
operator|.
name|greetMe
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should have connect exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInovationWithHCAddress
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"hc://http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|factory
operator|.
name|create
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Hello test"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocationWithTransportId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setTransportId
argument_list|(
literal|"http://cxf.apache.org/transports/http/http-client"
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|factory
operator|.
name|create
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Hello test"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCall
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|request
argument_list|,
name|g
operator|.
name|greetMe
argument_list|(
name|request
argument_list|)
argument_list|)
expr_stmt|;
name|HTTPConduit
name|c
init|=
operator|(
name|HTTPConduit
operator|)
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|g
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|cp
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|cp
operator|.
name|setAllowChunking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|c
operator|.
name|setClient
argument_list|(
name|cp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|request
argument_list|,
name|g
operator|.
name|greetMe
argument_list|(
name|request
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCallAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|GreetMeResponse
name|resp
init|=
operator|(
name|GreetMeResponse
operator|)
name|g
operator|.
name|greetMeAsync
argument_list|(
name|request
argument_list|,
operator|new
name|AsyncHandler
argument_list|<
name|GreetMeResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|res
parameter_list|)
block|{
try|try
block|{
name|res
operator|.
name|get
argument_list|()
operator|.
name|getResponseType
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|request
argument_list|,
name|resp
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
literal|1000
argument_list|,
operator|new
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|res
parameter_list|)
block|{             }
block|}
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCallAsyncCallbackInvokedOnlyOnce
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This test is especially targeted for RHEL 6.8
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT_INV
argument_list|)
expr_stmt|;
name|int
name|repeat
init|=
literal|100
decl_stmt|;
specifier|final
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
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
name|repeat
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|g
operator|.
name|greetMeAsync
argument_list|(
name|request
argument_list|,
operator|new
name|AsyncHandler
argument_list|<
name|GreetMeResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|res
parameter_list|)
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{             }
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Callback should be invoked only once per request"
argument_list|,
name|repeat
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCallAsyncWithFullWorkQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|WorkQueueManager
name|workQueueManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|AutomaticWorkQueueImpl
name|automaticWorkQueue1
init|=
operator|(
name|AutomaticWorkQueueImpl
operator|)
name|workQueueManager
operator|.
name|getAutomaticWorkQueue
argument_list|()
decl_stmt|;
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|g
argument_list|)
decl_stmt|;
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|httpClientPolicy
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|int
name|asyncExecuteTimeout
init|=
literal|500
decl_stmt|;
name|httpClientPolicy
operator|.
name|setAsyncExecuteTimeout
argument_list|(
name|asyncExecuteTimeout
argument_list|)
expr_stmt|;
name|http
operator|.
name|setClient
argument_list|(
name|httpClientPolicy
argument_list|)
expr_stmt|;
name|long
name|repeat
init|=
name|automaticWorkQueue1
operator|.
name|getHighWaterMark
argument_list|()
operator|+
name|automaticWorkQueue1
operator|.
name|getMaxSize
argument_list|()
operator|+
literal|1
decl_stmt|;
name|CountDownLatch
name|initialThreadsLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|automaticWorkQueue1
operator|.
name|getHighWaterMark
argument_list|()
argument_list|)
decl_stmt|;
name|CountDownLatch
name|doneLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
operator|(
name|int
operator|)
name|repeat
argument_list|)
decl_stmt|;
name|AtomicInteger
name|threadCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|repeat
condition|;
name|i
operator|++
control|)
block|{
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
operator|-
literal|50
argument_list|,
name|res
lambda|->
block|{
try|try
block|{
name|int
name|myCount
init|=
name|threadCount
operator|.
name|getAndIncrement
argument_list|()
decl_stmt|;
if|if
condition|(
name|myCount
operator|<
name|automaticWorkQueue1
operator|.
name|getHighWaterMark
argument_list|()
condition|)
block|{
comment|// Sleep long enough so that the workqueue will fill up and then
comment|// handleResponseOnWorkqueue will fail for the calls from both
comment|// responseReceived and consumeContent
name|Thread
operator|.
name|sleep
argument_list|(
literal|3
operator|*
name|asyncExecuteTimeout
argument_list|)
expr_stmt|;
name|initialThreadsLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
name|initialThreadsLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|doneLatch
operator|.
name|countDown
argument_list|()
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
block|}
argument_list|)
expr_stmt|;
block|}
name|doneLatch
operator|.
name|await
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"All responses should be handled eventually"
argument_list|,
literal|0
argument_list|,
name|doneLatch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"peformance test"
argument_list|)
specifier|public
name|void
name|testCalls
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
comment|//warmup
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
literal|10000
condition|;
name|x
operator|++
control|)
block|{
comment|//builder.append('a');
comment|//long s1 = System.nanoTime();
comment|//System.out.println("aa1: " + s1);
name|String
name|value
init|=
name|g
operator|.
name|greetMe
argument_list|(
name|request
argument_list|)
decl_stmt|;
comment|//long s2 = System.nanoTime();
comment|//System.out.println("aa2: " + s2 + " " + (s2 - s1));
name|assertEquals
argument_list|(
literal|"Hello "
operator|+
name|request
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|//System.out.println();
block|}
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
literal|10000
condition|;
name|x
operator|++
control|)
block|{
comment|//builder.append('a');
comment|//long s1 = System.nanoTime();
comment|//System.out.println("aa1: " + s1);
name|g
operator|.
name|greetMe
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|//long s2 = System.nanoTime();
comment|//System.out.println("aa2: " + s2 + " " + (s2 - s1));
comment|//System.out.println();
block|}
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Total: "
operator|+
operator|(
name|end
operator|-
name|start
operator|)
argument_list|)
expr_stmt|;
comment|/*         updateAddressPort(g, PORT2);         String value = g.greetMe(builder.toString());         assertEquals("Hello " + builder.toString(), value);         */
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"peformance test"
argument_list|)
specifier|public
name|void
name|testCallsAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|updateAddressPort
argument_list|(
name|g
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
specifier|final
name|int
name|warmupIter
init|=
literal|5000
decl_stmt|;
specifier|final
name|int
name|runIter
init|=
literal|5000
decl_stmt|;
specifier|final
name|CountDownLatch
name|wlatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|warmupIter
argument_list|)
decl_stmt|;
specifier|final
name|boolean
index|[]
name|wdone
init|=
operator|new
name|boolean
index|[
name|warmupIter
index|]
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
index|[]
name|whandler
init|=
operator|new
name|AsyncHandler
index|[
name|warmupIter
index|]
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|warmupIter
condition|;
name|x
operator|++
control|)
block|{
specifier|final
name|int
name|c
init|=
name|x
decl_stmt|;
name|whandler
index|[
name|x
index|]
operator|=
operator|new
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|res
parameter_list|)
block|{
try|try
block|{
name|String
name|s
init|=
name|res
operator|.
name|get
argument_list|()
operator|.
name|getResponseType
argument_list|()
decl_stmt|;
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|lastIndexOf
argument_list|(
literal|' '
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Problem "
operator|+
name|c
operator|+
literal|" != "
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|wdone
index|[
name|c
index|]
operator|=
literal|true
expr_stmt|;
name|wlatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
comment|//warmup
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|warmupIter
condition|;
name|x
operator|++
control|)
block|{
comment|//builder.append('a');
comment|//long s1 = System.nanoTime();
comment|//System.out.println("aa1: " + s1);
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
name|x
argument_list|,
name|whandler
index|[
name|x
index|]
argument_list|)
expr_stmt|;
comment|//long s2 = System.nanoTime();
comment|//System.out.println("aa2: " + s2 + " " + (s2 - s1));
comment|//System.out.println();
block|}
name|wlatch
operator|.
name|await
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Warmup Total: "
operator|+
operator|(
name|end
operator|-
name|start
operator|)
operator|+
literal|" "
operator|+
name|wlatch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|warmupIter
condition|;
name|x
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|wdone
index|[
name|x
index|]
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  "
operator|+
name|x
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|wlatch
operator|.
name|getCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CountDownLatch
name|rlatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|runIter
argument_list|)
decl_stmt|;
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|rhandler
init|=
operator|new
name|AsyncHandler
argument_list|<
name|GreetMeLaterResponse
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Response
argument_list|<
name|GreetMeLaterResponse
argument_list|>
name|res
parameter_list|)
block|{
name|rlatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|runIter
condition|;
name|x
operator|++
control|)
block|{
comment|//builder.append('a');
comment|//long s1 = System.nanoTime();
comment|//System.out.println("aa1: " + s1);
name|g
operator|.
name|greetMeLaterAsync
argument_list|(
name|x
argument_list|,
name|rhandler
argument_list|)
expr_stmt|;
comment|//long s2 = System.nanoTime();
comment|//System.out.println("aa2: " + s2 + " " + (s2 - s1));
comment|//System.out.println();
block|}
name|rlatch
operator|.
name|await
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|end
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Total: "
operator|+
operator|(
name|end
operator|-
name|start
operator|)
operator|+
literal|" "
operator|+
name|rlatch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

