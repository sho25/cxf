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
operator|.
name|tracing
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|Future
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
name|MediaType
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|JAXRSServerFactoryBean
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
name|jaxrs
operator|.
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|systest
operator|.
name|TestSpanReporter
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
name|brave
operator|.
name|BraveTestSupport
operator|.
name|SpanId
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
name|jaxrs
operator|.
name|tracing
operator|.
name|BookStore
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
name|tracing
operator|.
name|brave
operator|.
name|TraceScope
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
name|tracing
operator|.
name|brave
operator|.
name|jaxrs
operator|.
name|BraveClientProvider
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
name|tracing
operator|.
name|brave
operator|.
name|jaxrs
operator|.
name|BraveFeature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|brave
operator|.
name|BraveTestSupport
operator|.
name|PARENT_SPAN_ID_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|brave
operator|.
name|BraveTestSupport
operator|.
name|SAMPLED_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|brave
operator|.
name|BraveTestSupport
operator|.
name|SPAN_ID_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|brave
operator|.
name|BraveTestSupport
operator|.
name|TRACE_ID_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|brave
operator|.
name|HasSpan
operator|.
name|hasSpan
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|brave
operator|.
name|IsAnnotationContaining
operator|.
name|hasItem
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|brave
operator|.
name|IsBinaryAnnotationContaining
operator|.
name|hasItem
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|Tracer
operator|.
name|SpanInScope
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|Tracing
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|sampler
operator|.
name|Sampler
import|;
end_import

begin_class
specifier|public
class|class
name|BraveTracingTest
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
name|BraveTracingTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Tracing
name|brave
decl_stmt|;
specifier|private
name|BraveClientProvider
name|braveClientProvider
decl_stmt|;
specifier|private
name|Random
name|random
decl_stmt|;
annotation|@
name|Ignore
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
specifier|final
name|Tracing
name|brave
init|=
name|Tracing
operator|.
name|newBuilder
argument_list|()
operator|.
name|reporter
argument_list|(
operator|new
name|TestSpanReporter
argument_list|()
argument_list|)
operator|.
name|sampler
argument_list|(
name|Sampler
operator|.
name|ALWAYS_SAMPLE
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookStore
argument_list|<
name|TraceScope
argument_list|>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProvider
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProvider
argument_list|(
operator|new
name|BraveFeature
argument_list|(
name|brave
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
comment|//keep out of process due to stack traces testing failures
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
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|TestSpanReporter
operator|.
name|clear
argument_list|()
expr_stmt|;
name|brave
operator|=
name|Tracing
operator|.
name|newBuilder
argument_list|()
operator|.
name|reporter
argument_list|(
operator|new
name|TestSpanReporter
argument_list|()
argument_list|)
operator|.
name|sampler
argument_list|(
name|Sampler
operator|.
name|ALWAYS_SAMPLE
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|braveClientProvider
operator|=
operator|new
name|BraveClientProvider
argument_list|(
name|brave
argument_list|)
expr_stmt|;
name|random
operator|=
operator|new
name|Random
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewSpanIsCreatedWhenNotProvided
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SPAN_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|TRACE_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SAMPLED_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|PARENT_SPAN_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewInnerSpanIsCreated
parameter_list|()
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|withTrace
argument_list|(
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
argument_list|,
name|spanId
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceIsPresent
argument_list|(
name|r
argument_list|,
name|spanId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatCurrentSpanIsAnnotatedWithKeyValue
parameter_list|()
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|withTrace
argument_list|(
name|createWebClient
argument_list|(
literal|"/bookstore/book/1"
argument_list|)
argument_list|,
name|spanId
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/book/1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|binaryAnnotations
argument_list|,
name|hasItem
argument_list|(
literal|"book-id"
argument_list|,
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceIsPresent
argument_list|(
name|r
argument_list|,
name|spanId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatParallelSpanIsAnnotatedWithTimeline
parameter_list|()
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|withTrace
argument_list|(
name|createWebClient
argument_list|(
literal|"/bookstore/process"
argument_list|)
argument_list|,
name|spanId
argument_list|)
operator|.
name|put
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
argument_list|,
name|hasSpan
argument_list|(
literal|"processing books"
argument_list|,
name|hasItem
argument_list|(
literal|"Processing started"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
argument_list|,
name|hasSpan
argument_list|(
literal|"put /bookstore/process"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceIsPresent
argument_list|(
name|r
argument_list|,
name|spanId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewChildSpanIsCreatedWhenParentIsProvided
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|,
name|braveClientProvider
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|parentId
argument_list|,
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceHeadersArePresent
argument_list|(
name|r
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewInnerSpanIsCreatedUsingAsyncInvocation
parameter_list|()
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|withTrace
argument_list|(
name|createWebClient
argument_list|(
literal|"/bookstore/books/async"
argument_list|)
argument_list|,
name|spanId
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books/async"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"processing books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceIsPresent
argument_list|(
name|r
argument_list|,
name|spanId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatOuterSpanIsCreatedUsingAsyncInvocation
parameter_list|()
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|withTrace
argument_list|(
name|createWebClient
argument_list|(
literal|"/bookstore/books/async/notrace"
argument_list|)
argument_list|,
name|spanId
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books/async/notrace"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceIsPresent
argument_list|(
name|r
argument_list|,
name|spanId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewSpanIsCreatedUsingAsyncInvocation
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books/async"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books/async"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"processing books"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewSpanIsCreatedWhenNotProvidedUsingAsyncClient
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WebClient
name|client
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|,
name|braveClientProvider
argument_list|)
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|Response
argument_list|>
name|f
init|=
name|client
operator|.
name|async
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|f
operator|.
name|get
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get "
operator|+
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceHeadersArePresent
argument_list|(
name|r
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatProvidedSpanIsNotClosedWhenActive
parameter_list|()
throws|throws
name|MalformedURLException
block|{
specifier|final
name|WebClient
name|client
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|,
name|braveClientProvider
argument_list|)
decl_stmt|;
specifier|final
name|Span
name|span
init|=
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|nextSpan
argument_list|()
operator|.
name|name
argument_list|(
literal|"test span"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
try|try
block|{
try|try
init|(
name|SpanInScope
name|scope
init|=
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|withSpanInScope
argument_list|(
name|span
argument_list|)
init|)
block|{
specifier|final
name|Response
name|r
init|=
name|client
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|parentId
argument_list|,
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get "
operator|+
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceHeadersArePresent
argument_list|(
name|r
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|span
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"test span"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatProvidedSpanIsNotDetachedWhenActiveUsingAsyncClient
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|WebClient
name|client
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|,
name|braveClientProvider
argument_list|)
decl_stmt|;
specifier|final
name|Span
name|span
init|=
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|nextSpan
argument_list|()
operator|.
name|name
argument_list|(
literal|"test span"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
try|try
block|{
try|try
init|(
name|SpanInScope
name|scope
init|=
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|withSpanInScope
argument_list|(
name|span
argument_list|)
init|)
block|{
specifier|final
name|Future
argument_list|<
name|Response
argument_list|>
name|f
init|=
name|client
operator|.
name|async
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|f
operator|.
name|get
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|brave
operator|.
name|tracer
argument_list|()
operator|.
name|currentSpan
argument_list|()
operator|.
name|context
argument_list|()
operator|.
name|spanId
argument_list|()
argument_list|,
name|equalTo
argument_list|(
name|span
operator|.
name|context
argument_list|()
operator|.
name|spanId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get "
operator|+
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceHeadersArePresent
argument_list|(
name|r
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|span
operator|.
name|finish
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"test span"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatInnerSpanIsCreatedUsingPseudoAsyncInvocation
parameter_list|()
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Response
name|r
init|=
name|withTrace
argument_list|(
name|createWebClient
argument_list|(
literal|"/bookstore/books/pseudo-async"
argument_list|)
argument_list|,
name|spanId
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"get /bookstore/books/pseudo-async"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReporter
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|,
name|equalTo
argument_list|(
literal|"processing books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThatTraceIsPresent
argument_list|(
name|r
argument_list|,
name|spanId
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|WebClient
name|createWebClient
parameter_list|(
specifier|final
name|String
name|url
parameter_list|,
specifier|final
name|Object
modifier|...
name|providers
parameter_list|)
block|{
return|return
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
name|url
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|providers
argument_list|)
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
return|;
block|}
specifier|protected
name|WebClient
name|withTrace
parameter_list|(
specifier|final
name|WebClient
name|client
parameter_list|,
specifier|final
name|SpanId
name|spanId
parameter_list|)
block|{
return|return
name|client
operator|.
name|header
argument_list|(
name|SPAN_ID_NAME
argument_list|,
name|spanId
operator|.
name|spanId
argument_list|()
argument_list|)
operator|.
name|header
argument_list|(
name|TRACE_ID_NAME
argument_list|,
name|spanId
operator|.
name|traceId
argument_list|()
argument_list|)
operator|.
name|header
argument_list|(
name|SAMPLED_NAME
argument_list|,
name|spanId
operator|.
name|sampled
argument_list|()
argument_list|)
operator|.
name|header
argument_list|(
name|PARENT_SPAN_ID_NAME
argument_list|,
name|spanId
operator|.
name|parentId
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|assertThatTraceIsPresent
parameter_list|(
specifier|final
name|Response
name|r
parameter_list|,
specifier|final
name|SpanId
name|spanId
parameter_list|)
block|{
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|SPAN_ID_NAME
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|spanId
operator|.
name|spanId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|TRACE_ID_NAME
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|spanId
operator|.
name|traceId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|SAMPLED_NAME
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|spanId
operator|.
name|sampled
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
operator|(
name|String
operator|)
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|PARENT_SPAN_ID_NAME
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|spanId
operator|.
name|parentId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertThatTraceHeadersArePresent
parameter_list|(
specifier|final
name|Response
name|r
parameter_list|,
specifier|final
name|boolean
name|expectParent
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SPAN_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|TRACE_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SAMPLED_NAME
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectParent
condition|)
block|{
name|assertTrue
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|PARENT_SPAN_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
argument_list|(
name|r
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|PARENT_SPAN_ID_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|SpanId
name|fromRandom
parameter_list|()
block|{
return|return
operator|new
name|SpanId
argument_list|()
operator|.
name|traceId
argument_list|(
name|random
operator|.
name|nextLong
argument_list|()
argument_list|)
operator|.
name|parentId
argument_list|(
name|random
operator|.
name|nextLong
argument_list|()
argument_list|)
operator|.
name|spanId
argument_list|(
name|random
operator|.
name|nextLong
argument_list|()
argument_list|)
operator|.
name|sampled
argument_list|(
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

