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
name|reactive
package|;
end_package

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
name|ExecutionException
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
name|NotFoundException
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
name|client
operator|.
name|ClientBuilder
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
name|client
operator|.
name|Invocation
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
name|GenericType
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
name|rx
operator|.
name|client
operator|.
name|ObservableRxInvoker
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
name|rx
operator|.
name|client
operator|.
name|ObservableRxInvokerProvider
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
name|rx
operator|.
name|provider
operator|.
name|ObservableReader
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

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSReactiveTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|ReactiveServer
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|ReactiveServer
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
name|Test
specifier|public
name|void
name|testGetHelloWorldText
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
literal|"/reactive/text"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
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
literal|"Hello, world!"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldAsyncText
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
literal|"/reactive/textAsync"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
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
literal|"Hello, world!"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldTextObservableSync
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
literal|"/reactive/text"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|ObservableReader
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|GenericType
argument_list|<
name|Observable
argument_list|<
name|String
argument_list|>
argument_list|>
name|genericResponseType
init|=
operator|new
name|GenericType
argument_list|<
name|Observable
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{                     }
decl_stmt|;
name|Observable
argument_list|<
name|String
argument_list|>
name|obs
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|get
argument_list|(
name|genericResponseType
argument_list|)
decl_stmt|;
name|obs
operator|.
name|subscribe
argument_list|(
name|s
lambda|->
name|assertResponse
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertResponse
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello, world!"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldJson
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
literal|"/reactive/textJson"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|HelloWorldBean
name|bean
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|get
argument_list|(
name|HelloWorldBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|bean
operator|.
name|getGreeting
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|bean
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldJsonList
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
literal|"/reactive/textJsonList"
decl_stmt|;
name|doTestGetHelloWorldJsonList
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldJsonImplicitList
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
literal|"/reactive/textJsonImplicitList"
decl_stmt|;
name|doTestGetHelloWorldJsonList
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldJsonImplicitListAsync
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
literal|"/reactive/textJsonImplicitListAsync"
decl_stmt|;
name|doTestGetHelloWorldJsonList
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldJsonImplicitListAsyncStream
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
literal|"/reactive/textJsonImplicitListAsyncStream"
decl_stmt|;
name|doTestGetHelloWorldJsonList
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestGetHelloWorldJsonList
parameter_list|(
name|String
name|address
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
name|address
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|10000000
argument_list|)
expr_stmt|;
name|GenericType
argument_list|<
name|List
argument_list|<
name|HelloWorldBean
argument_list|>
argument_list|>
name|genericResponseType
init|=
operator|new
name|GenericType
argument_list|<
name|List
argument_list|<
name|HelloWorldBean
argument_list|>
argument_list|>
argument_list|()
block|{                 }
decl_stmt|;
name|List
argument_list|<
name|HelloWorldBean
argument_list|>
name|beans
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|get
argument_list|(
name|genericResponseType
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|beans
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|beans
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getGreeting
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|beans
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Ciao"
argument_list|,
name|beans
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getGreeting
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|beans
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldAsyncObservable
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
literal|"/reactive/textAsync"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|ObservableRxInvokerProvider
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Observable
argument_list|<
name|String
argument_list|>
name|obs
init|=
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|rx
argument_list|(
name|ObservableRxInvoker
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|obs
operator|.
name|map
argument_list|(
name|s
lambda|->
block|{
return|return
name|s
operator|+
name|s
return|;
block|}
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|obs
operator|.
name|subscribe
argument_list|(
name|s
lambda|->
name|assertDuplicateResponse
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHelloWorldAsyncObservable404
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
literal|"/reactive/textAsync404"
decl_stmt|;
name|Invocation
operator|.
name|Builder
name|b
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
operator|new
name|ObservableRxInvokerProvider
argument_list|()
argument_list|)
operator|.
name|target
argument_list|(
name|address
argument_list|)
operator|.
name|request
argument_list|()
decl_stmt|;
name|b
operator|.
name|rx
argument_list|(
name|ObservableRxInvoker
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|subscribe
argument_list|(
name|s
lambda|->
block|{
name|fail
argument_list|(
literal|"Exception expected"
argument_list|)
expr_stmt|;
block|}
argument_list|,
name|t
lambda|->
name|validateT
argument_list|(
operator|(
name|ExecutionException
operator|)
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateT
parameter_list|(
name|ExecutionException
name|t
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|t
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NotFoundException
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertDuplicateResponse
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello, world!Hello, world!"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

