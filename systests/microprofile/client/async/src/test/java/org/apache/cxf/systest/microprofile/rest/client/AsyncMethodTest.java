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
name|microprofile
operator|.
name|rest
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|CompletionStage
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
name|ThreadFactory
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
name|Response
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|core
operator|.
name|WireMockConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|junit
operator|.
name|WireMockRule
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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|mock
operator|.
name|AsyncClientWithCompletionStage
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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|mock
operator|.
name|AsyncInvocationInterceptorFactoryTestImpl
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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|mock
operator|.
name|AsyncInvocationInterceptorFactoryTestImpl2
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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|mock
operator|.
name|ThreadLocalClientFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|RestClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestClientRequestFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestClientResponseFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestMessageBodyReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestMessageBodyWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestParamConverterProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestReaderInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|tck
operator|.
name|providers
operator|.
name|TestWriterInterceptor
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
name|Rule
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
comment|//CHECKSTYLE:OFF
end_comment

begin_import
import|import static
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|client
operator|.
name|WireMock
operator|.
name|*
import|;
end_import

begin_comment
comment|//CHECKSTYLE:ON
end_comment

begin_class
specifier|public
class|class
name|AsyncMethodTest
extends|extends
name|Assert
block|{
annotation|@
name|Rule
specifier|public
name|WireMockRule
name|wireMockRule
init|=
operator|new
name|WireMockRule
argument_list|(
name|WireMockConfiguration
operator|.
name|options
argument_list|()
operator|.
name|dynamicPort
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testInvokesPostOperationWithRegisteredProvidersAsyncCompletionStage
parameter_list|()
throws|throws
name|Exception
block|{
name|wireMockRule
operator|.
name|stubFor
argument_list|(
name|put
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/echo/test"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
literal|"this is the replaced writer input body will be removed"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|inputBody
init|=
literal|"input body will be removed"
decl_stmt|;
name|String
name|expectedResponseBody
init|=
name|TestMessageBodyReader
operator|.
name|REPLACED_BODY
decl_stmt|;
name|AsyncClientWithCompletionStage
name|api
init|=
name|RestClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|TestClientRequestFilter
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|TestClientResponseFilter
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|TestMessageBodyReader
operator|.
name|class
argument_list|,
literal|3
argument_list|)
operator|.
name|register
argument_list|(
name|TestMessageBodyWriter
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|TestParamConverterProvider
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|TestReaderInterceptor
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|TestWriterInterceptor
operator|.
name|class
argument_list|)
operator|.
name|baseUri
argument_list|(
name|getBaseUri
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|AsyncClientWithCompletionStage
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompletionStage
argument_list|<
name|Response
argument_list|>
name|cs
init|=
name|api
operator|.
name|put
argument_list|(
name|inputBody
argument_list|)
decl_stmt|;
comment|// should need<1 second, but 20s timeout in case something goes wrong
name|Response
name|response
init|=
name|cs
operator|.
name|toCompletableFuture
argument_list|()
operator|.
name|get
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|String
name|actualResponseBody
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedResponseBody
argument_list|,
name|actualResponseBody
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestClientResponseFilter
operator|.
name|getAndResetValue
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestClientRequestFilter
operator|.
name|getAndResetValue
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TestReaderInterceptor
operator|.
name|getAndResetValue
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokesPostOperationWithRegisteredProvidersAsyncCompletionStageWithExecutor
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|inputBody
init|=
literal|"input body will be ignored"
decl_stmt|;
name|wireMockRule
operator|.
name|stubFor
argument_list|(
name|put
argument_list|(
name|urlEqualTo
argument_list|(
literal|"/echo/test"
argument_list|)
argument_list|)
operator|.
name|willReturn
argument_list|(
name|aResponse
argument_list|()
operator|.
name|withBody
argument_list|(
name|inputBody
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|INBOUND
operator|.
name|remove
argument_list|()
expr_stmt|;
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|OUTBOUND
operator|.
name|remove
argument_list|()
expr_stmt|;
try|try
block|{
specifier|final
name|String
name|asyncThreadName
init|=
literal|"CXF-MPRestClientThread-2"
decl_stmt|;
name|AsyncClientWithCompletionStage
name|api
init|=
name|RestClientBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|register
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl2
operator|.
name|class
argument_list|)
operator|.
name|register
argument_list|(
name|ThreadLocalClientFilter
operator|.
name|class
argument_list|)
operator|.
name|baseUri
argument_list|(
name|getBaseUri
argument_list|()
argument_list|)
operator|.
name|executorService
argument_list|(
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|(
operator|new
name|ThreadFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
return|return
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|asyncThreadName
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
name|AsyncClientWithCompletionStage
operator|.
name|class
argument_list|)
decl_stmt|;
name|CompletionStage
argument_list|<
name|Response
argument_list|>
name|cs
init|=
name|api
operator|.
name|put
argument_list|(
name|inputBody
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|outboundList
init|=
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|OUTBOUND
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|outboundList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// ensure filters and asyncInvocationInterceptors are executed in the correct order and the correct thread
comment|// outbound:
name|assertEquals
argument_list|(
name|ThreadLocalClientFilter
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|outboundList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|outboundList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl2
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|outboundList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|outboundList
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
comment|// inbound:
comment|// should need<1 second, but 20s timeout in case something goes wrong
name|Response
name|response
init|=
name|cs
operator|.
name|toCompletableFuture
argument_list|()
operator|.
name|get
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|responseList
init|=
name|response
operator|.
name|getStringHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"CXFTestResponse"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|responseList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|asyncThreadName
argument_list|,
name|responseList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl2
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|responseList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|responseList
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ThreadLocalClientFilter
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|responseList
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|INBOUND
operator|.
name|remove
argument_list|()
expr_stmt|;
name|AsyncInvocationInterceptorFactoryTestImpl
operator|.
name|OUTBOUND
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|URI
name|getBaseUri
parameter_list|()
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|wireMockRule
operator|.
name|port
argument_list|()
operator|+
literal|"/echo"
argument_list|)
return|;
block|}
specifier|private
name|void
name|fail
parameter_list|(
name|Response
name|r
parameter_list|,
name|String
name|failureMessage
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|failureMessage
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

