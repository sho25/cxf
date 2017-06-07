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
name|jaxrs
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|model
operator|.
name|AbstractResourceInfo
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
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|CloseableHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
specifier|public
class|class
name|JAXRSCxfContinuationsTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookCxfContinuationServer
operator|.
name|PORT
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookCxfContinuationServer
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
name|testContinuation
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestContinuation
argument_list|(
literal|"books"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContinuationSubresource
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestContinuation
argument_list|(
literal|"books/subresources"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestContinuation
parameter_list|(
name|String
name|pathSegment
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
literal|1
argument_list|,
literal|1
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
literal|10
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
name|doneSignal
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|BookWorker
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/"
operator|+
name|pathSegment
operator|+
literal|"/1"
argument_list|,
literal|"1"
argument_list|,
literal|"CXF in Action1"
argument_list|,
name|startSignal
argument_list|,
name|doneSignal
argument_list|)
argument_list|)
expr_stmt|;
name|startSignal
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|doneSignal
operator|.
name|await
argument_list|(
literal|60
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
literal|"Not all invocations have completed"
argument_list|,
literal|0
argument_list|,
name|doneSignal
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkBook
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|address
argument_list|)
decl_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Book description for id "
operator|+
name|id
operator|+
literal|" is wrong"
argument_list|,
name|expected
argument_list|,
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Release current connection to the connection pool once you are done
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Ignore
specifier|private
class|class
name|BookWorker
implements|implements
name|Runnable
block|{
specifier|private
name|String
name|address
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|String
name|expected
decl_stmt|;
specifier|private
name|CountDownLatch
name|startSignal
decl_stmt|;
specifier|private
name|CountDownLatch
name|doneSignal
decl_stmt|;
name|BookWorker
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|expected
parameter_list|,
name|CountDownLatch
name|startSignal
parameter_list|,
name|CountDownLatch
name|doneSignal
parameter_list|)
block|{
name|this
operator|.
name|address
operator|=
name|address
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|expected
operator|=
name|expected
expr_stmt|;
name|this
operator|.
name|startSignal
operator|=
name|startSignal
expr_stmt|;
name|this
operator|.
name|doneSignal
operator|=
name|doneSignal
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|startSignal
operator|.
name|await
argument_list|()
expr_stmt|;
name|checkBook
argument_list|(
name|address
argument_list|,
name|id
argument_list|,
name|expected
argument_list|)
expr_stmt|;
name|doneSignal
operator|.
name|countDown
argument_list|()
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
name|Assert
operator|.
name|fail
argument_list|(
literal|"Book thread failed for : "
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncodedURL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
literal|"A%20B%20C"
decl_stmt|;
comment|// "A B C"
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/books/"
operator|+
name|id
argument_list|)
decl_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Encoded path '/"
operator|+
name|id
operator|+
literal|"' is not handled successfully"
argument_list|,
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Book description for id "
operator|+
name|id
operator|+
literal|" is wrong"
argument_list|,
literal|"CXF in Action A B C"
argument_list|,
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Release current connection to the connection pool once you are done
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

