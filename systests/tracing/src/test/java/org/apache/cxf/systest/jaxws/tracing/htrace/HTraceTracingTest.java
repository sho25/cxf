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
name|jaxws
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
name|helpers
operator|.
name|CastUtils
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|Phase
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
name|TestSpanReceiver
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
name|jaxws
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
name|systest
operator|.
name|jaxws
operator|.
name|tracing
operator|.
name|BookStoreService
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
name|cxf
operator|.
name|tracing
operator|.
name|htrace
operator|.
name|HTraceClientStartInterceptor
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
name|htrace
operator|.
name|HTraceClientStopInterceptor
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
name|htrace
operator|.
name|HTraceStartInterceptor
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
name|htrace
operator|.
name|HTraceStopInterceptor
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
name|core
operator|.
name|AlwaysSampler
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
name|core
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
name|core
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
name|htrace
operator|.
name|core
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
name|htrace
operator|.
name|core
operator|.
name|Tracer
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|hasItems
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
name|Tracer
name|tracer
init|=
name|createTracer
argument_list|()
decl_stmt|;
specifier|final
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|BookStore
operator|.
name|class
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
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HTraceStartInterceptor
argument_list|(
name|Phase
operator|.
name|PRE_INVOKE
argument_list|,
name|tracer
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HTraceStopInterceptor
argument_list|(
name|Phase
operator|.
name|PRE_MARSHAL
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
specifier|private
interface|interface
name|Configurator
block|{
name|void
name|configure
parameter_list|(
specifier|final
name|JaxWsProxyFactoryBean
name|factory
parameter_list|)
function_decl|;
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
throws|throws
name|MalformedURLException
block|{
specifier|final
name|BookStoreService
name|service
init|=
name|createJaxWsService
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|service
operator|.
name|getBooks
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
literal|"POST /BookStore"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|response
init|=
name|getResponseHeaders
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|get
argument_list|(
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
argument_list|)
argument_list|,
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewInnerSpanIsCreated
parameter_list|()
throws|throws
name|MalformedURLException
block|{
specifier|final
name|SpanId
name|spanId
init|=
name|SpanId
operator|.
name|fromRandom
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|spanId
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|BookStoreService
name|service
init|=
name|createJaxWsService
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|service
operator|.
name|getBooks
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
literal|"POST /BookStore"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|response
init|=
name|getResponseHeaders
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|get
argument_list|(
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
argument_list|)
argument_list|,
name|hasItems
argument_list|(
name|spanId
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNewChildSpanIsCreatedWhenParentIsProvided
parameter_list|()
throws|throws
name|MalformedURLException
block|{
specifier|final
name|Tracer
name|tracer
init|=
name|createTracer
argument_list|()
decl_stmt|;
specifier|final
name|BookStoreService
name|service
init|=
name|createJaxWsService
argument_list|(
operator|new
name|Configurator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
specifier|final
name|JaxWsProxyFactoryBean
name|factory
parameter_list|)
block|{
name|factory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HTraceClientStartInterceptor
argument_list|(
name|tracer
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HTraceClientStopInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|service
operator|.
name|getBooks
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
literal|0
argument_list|)
operator|.
name|getParents
argument_list|()
operator|.
name|length
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
literal|1
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"POST /BookStore"
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
literal|2
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"POST http://localhost:"
operator|+
name|PORT
operator|+
literal|"/BookStore"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|response
init|=
name|getResponseHeaders
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|get
argument_list|(
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
argument_list|)
argument_list|,
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
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
name|Tracer
name|tracer
init|=
name|createTracer
argument_list|()
decl_stmt|;
specifier|final
name|BookStoreService
name|service
init|=
name|createJaxWsService
argument_list|(
operator|new
name|Configurator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
specifier|final
name|JaxWsProxyFactoryBean
name|factory
parameter_list|)
block|{
name|factory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HTraceClientStartInterceptor
argument_list|(
name|tracer
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|HTraceClientStopInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
try|try
init|(
specifier|final
name|TraceScope
name|scope
init|=
name|tracer
operator|.
name|newScope
argument_list|(
literal|"test span"
argument_list|)
init|)
block|{
name|assertThat
argument_list|(
name|service
operator|.
name|getBooks
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
name|Tracer
operator|.
name|getCurrentSpan
argument_list|()
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
literal|0
argument_list|)
operator|.
name|getParents
argument_list|()
operator|.
name|length
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
literal|1
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"POST /BookStore"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|3
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
literal|2
argument_list|)
operator|.
name|getDescription
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|"test span"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|response
init|=
name|getResponseHeaders
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|get
argument_list|(
name|TracerHeaders
operator|.
name|DEFAULT_HEADER_SPAN_ID
argument_list|)
argument_list|,
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|BookStoreService
name|createJaxWsService
parameter_list|()
throws|throws
name|MalformedURLException
block|{
return|return
name|createJaxWsService
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|BookStoreService
name|createJaxWsService
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
name|createJaxWsService
argument_list|(
name|headers
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|BookStoreService
name|createJaxWsService
parameter_list|(
specifier|final
name|Configurator
name|configurator
parameter_list|)
throws|throws
name|MalformedURLException
block|{
return|return
name|createJaxWsService
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|,
name|configurator
argument_list|)
return|;
block|}
specifier|private
name|BookStoreService
name|createJaxWsService
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
specifier|final
name|Configurator
name|configurator
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|BookStoreService
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/BookStore"
argument_list|)
expr_stmt|;
if|if
condition|(
name|configurator
operator|!=
literal|null
condition|)
block|{
name|configurator
operator|.
name|configure
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|final
name|BookStoreService
name|service
init|=
operator|(
name|BookStoreService
operator|)
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
specifier|final
name|Client
name|proxy
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|service
argument_list|)
decl_stmt|;
name|proxy
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getResponseHeaders
parameter_list|(
specifier|final
name|BookStoreService
name|service
parameter_list|)
block|{
specifier|final
name|Client
name|proxy
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|service
argument_list|)
decl_stmt|;
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|proxy
operator|.
name|getResponseContext
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Tracer
name|createTracer
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
name|Tracer
operator|.
name|SPAN_RECEIVER_CLASSES_KEY
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
name|Tracer
operator|.
name|SAMPLER_CLASSES_KEY
argument_list|,
name|AlwaysSampler
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|Tracer
operator|.
name|Builder
argument_list|()
operator|.
name|name
argument_list|(
literal|"tracer"
argument_list|)
operator|.
name|conf
argument_list|(
name|HTraceConfiguration
operator|.
name|fromMap
argument_list|(
name|properties
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

