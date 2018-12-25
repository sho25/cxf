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
name|ArrayList
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
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJAXRSContinuationsTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDefaultTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/defaulttimeout"
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|503
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImmediateResume
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/resume"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"immediateResume"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResumeFromFastAppThread
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress2
argument_list|()
operator|+
literal|"/books/resumeFromFastThread"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"resumeFromFastThread"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoContent
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/nocontent"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomStatusFromInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/async/nocontentInterface"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|206
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnmappedAfterTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/suspend/unmapped"
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImmediateResumeSubresource
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/subresources/books/resume"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"immediateResume"
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookNotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/notfound"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|404
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookNotFoundUnmapped
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/notfound/unmapped"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookNotFoundUnmappedImmediate
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/notfound/unmappedImmediate"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookMappedImmediate
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/mappedImmediate"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTimeoutAndCancel
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestTimeoutAndCancel
argument_list|(
name|getBaseAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestTimeoutAndCancel
parameter_list|(
name|String
name|baseAddress
parameter_list|)
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|baseAddress
operator|+
literal|"/books/cancel"
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|503
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|retryAfter
init|=
name|r
operator|.
name|getHeaderString
argument_list|(
name|HttpHeaders
operator|.
name|RETRY_AFTER
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|retryAfter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"10"
argument_list|,
name|retryAfter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContinuationWithTimeHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestContinuation
argument_list|(
literal|"/books/timeouthandler"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContinuationWithTimeHandlerResumeOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestContinuation
argument_list|(
literal|"/books/timeouthandlerresume"
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
literal|"/books"
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
literal|"/books/subresources"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doTestContinuation
parameter_list|(
name|String
name|pathSegment
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|String
name|port
init|=
name|getPort
argument_list|()
decl_stmt|;
name|ThreadPoolExecutor
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|5
argument_list|,
literal|5
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
name|List
argument_list|<
name|BookWorker
argument_list|>
name|workers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|1
init|;
name|x
operator|<
literal|6
condition|;
name|x
operator|++
control|)
block|{
name|workers
operator|.
name|add
argument_list|(
operator|new
name|BookWorker
argument_list|(
literal|"http://localhost:"
operator|+
name|port
operator|+
name|getBaseAddress
argument_list|()
operator|+
name|pathSegment
operator|+
literal|"/"
operator|+
name|x
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|x
argument_list|)
argument_list|,
literal|"CXF in Action"
operator|+
name|x
argument_list|,
name|startSignal
argument_list|,
name|doneSignal
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|BookWorker
name|w
range|:
name|workers
control|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
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
for|for
control|(
name|BookWorker
name|w
range|:
name|workers
control|)
block|{
name|w
operator|.
name|checkError
argument_list|()
expr_stmt|;
block|}
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
specifier|private
name|Exception
name|error
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
name|checkError
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
throw|throw
name|error
throw|;
block|}
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
name|fillInStackTrace
argument_list|()
expr_stmt|;
name|error
operator|=
name|ex
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|String
name|getBaseAddress
parameter_list|()
block|{
return|return
literal|"/bookstore"
return|;
block|}
specifier|protected
name|String
name|getBaseAddress2
parameter_list|()
block|{
return|return
literal|"/bookstore"
return|;
block|}
specifier|protected
specifier|abstract
name|String
name|getPort
parameter_list|()
function_decl|;
block|}
end_class

end_unit

