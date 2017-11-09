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
name|reactor
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
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|reactor
operator|.
name|client
operator|.
name|ReactorInvoker
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
name|reactor
operator|.
name|client
operator|.
name|ReactorInvokerProvider
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

begin_class
specifier|public
class|class
name|MonoReactorTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|ReactorServer
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
name|ReactorServer
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
literal|"/reactor/mono/textJson"
decl_stmt|;
name|List
argument_list|<
name|HelloWorldBean
argument_list|>
name|holder
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
operator|.
name|register
argument_list|(
operator|new
name|ReactorInvokerProvider
argument_list|()
argument_list|)
operator|.
name|target
argument_list|(
name|address
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|rx
argument_list|(
name|ReactorInvoker
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|HelloWorldBean
operator|.
name|class
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|holder
operator|::
name|add
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|holder
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|HelloWorldBean
name|bean
init|=
name|holder
operator|.
name|get
argument_list|(
literal|0
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
name|testTextJsonImplicitListAsyncStream
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
literal|"/reactor/mono/textJsonImplicitListAsyncStream"
decl_stmt|;
name|Holder
argument_list|<
name|HelloWorldBean
argument_list|>
name|holder
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
operator|.
name|register
argument_list|(
operator|new
name|ReactorInvokerProvider
argument_list|()
argument_list|)
operator|.
name|target
argument_list|(
name|address
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|rx
argument_list|(
name|ReactorInvoker
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|(
name|HelloWorldBean
operator|.
name|class
argument_list|)
operator|.
name|doOnNext
argument_list|(
name|helloWorldBean
lambda|->
name|holder
operator|.
name|value
operator|=
name|helloWorldBean
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|holder
operator|.
name|value
operator|.
name|getGreeting
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|holder
operator|.
name|value
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
name|testGetString
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
literal|"/reactor/mono/textAsync"
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|holder
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|register
argument_list|(
operator|new
name|ReactorInvokerProvider
argument_list|()
argument_list|)
operator|.
name|target
argument_list|(
name|address
argument_list|)
operator|.
name|request
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|rx
argument_list|(
name|ReactorInvoker
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
name|doOnNext
argument_list|(
name|msg
lambda|->
name|holder
operator|.
name|value
operator|=
name|msg
argument_list|)
operator|.
name|subscribe
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello, world!"
argument_list|,
name|holder
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

