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
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|systest
operator|.
name|jaxrs
operator|.
name|Book
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
name|BookServerAsyncClient
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSCompletionStageTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerAsyncClient
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
name|BookServerAsyncClient
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
throws|throws
name|Exception
block|{
name|String
name|property
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"test.delay"
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|property
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookAsyncStage
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
literal|"/bookstore/books"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|CompletionStage
argument_list|<
name|Book
argument_list|>
name|stage
init|=
name|wc
operator|.
name|path
argument_list|(
literal|"123"
argument_list|)
operator|.
name|rx
argument_list|()
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|stage
operator|.
name|toCompletableFuture
argument_list|()
operator|.
name|join
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookAsyncStageThenAcceptAsync
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
literal|"/bookstore/books"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|CompletionStage
argument_list|<
name|Book
argument_list|>
name|stage
init|=
name|wc
operator|.
name|path
argument_list|(
literal|"123"
argument_list|)
operator|.
name|rx
argument_list|()
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|Holder
argument_list|<
name|Book
argument_list|>
name|holder
init|=
operator|new
name|Holder
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|stage
operator|.
name|thenApply
argument_list|(
name|v
lambda|->
block|{
name|v
operator|.
name|setId
argument_list|(
name|v
operator|.
name|getId
argument_list|()
operator|*
literal|2
argument_list|)
expr_stmt|;
return|return
name|v
return|;
block|}
argument_list|)
operator|.
name|thenAcceptAsync
argument_list|(
name|v
lambda|->
block|{
name|holder
operator|.
name|value
operator|=
name|v
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|246L
argument_list|,
name|holder
operator|.
name|value
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookAsyncStage404
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
literal|"/bookstore/bookheaders/404"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|CompletionStage
argument_list|<
name|Book
argument_list|>
name|stage
init|=
name|wc
operator|.
name|path
argument_list|(
literal|"123"
argument_list|)
operator|.
name|rx
argument_list|()
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|stage
operator|.
name|toCompletableFuture
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Exception expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NotFoundException
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|WebClient
name|createWebClient
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
return|;
block|}
block|}
end_class

end_unit
