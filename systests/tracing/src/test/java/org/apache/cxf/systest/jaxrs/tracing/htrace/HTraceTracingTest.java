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
name|htrace
package|;
end_package

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
name|jaxrs
operator|.
name|tracing
operator|.
name|htrace
operator|.
name|HTraceFeature
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
name|TracerHeaders
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|HTraceConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|impl
operator|.
name|AlwaysSampler
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_class
specifier|public
class|class
name|HTraceTracingTest
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
name|HTraceTracingTest
operator|.
name|class
argument_list|)
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
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
name|properties
operator|.
name|put
argument_list|(
literal|"span.receiver"
argument_list|,
name|TestSpanReceiver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"sampler"
argument_list|,
name|AlwaysSampler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
name|setFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|HTraceFeature
argument_list|(
name|HTraceConfiguration
operator|.
name|fromMap
argument_list|(
name|properties
argument_list|)
argument_list|)
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
name|TestSpanReceiver
operator|.
name|clear
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
name|TestSpanReceiver
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
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"Get Books"
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
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
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
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
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
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|header
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
argument_list|,
literal|10L
argument_list|)
operator|.
name|header
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|,
literal|20L
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
name|TestSpanReceiver
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
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"Get Books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"bookstore/books"
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
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"10"
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
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"20"
argument_list|)
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
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/book/1"
argument_list|)
operator|.
name|header
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
argument_list|,
literal|10L
argument_list|)
operator|.
name|header
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|,
literal|20L
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
name|TestSpanReceiver
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
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"bookstore/book/1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKVAnnotations
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
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"10"
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
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"20"
argument_list|)
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
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/process"
argument_list|)
operator|.
name|header
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
argument_list|,
literal|10L
argument_list|)
operator|.
name|header
argument_list|(
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|,
literal|20L
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
name|TestSpanReceiver
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
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"bookstore/process"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTimelineAnnotations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"Processing books"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|TestSpanReceiver
operator|.
name|getAllSpans
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getTimelineAnnotations
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
name|TracerHeaders
operator|.
name|HEADER_TRACE_ID
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"10"
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
name|TracerHeaders
operator|.
name|HEADER_SPAN_ID
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"20"
argument_list|)
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
block|}
end_class

end_unit

