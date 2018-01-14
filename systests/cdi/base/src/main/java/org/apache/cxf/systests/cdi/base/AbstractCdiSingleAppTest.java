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
name|systests
operator|.
name|cdi
operator|.
name|base
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|Form
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
name|Test
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCdiSingleAppTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInjectedVersionIsProperlyReturned
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/version"
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|pathInfo
init|=
name|r
operator|.
name|getHeaderString
argument_list|(
name|Message
operator|.
name|PATH_INFO
argument_list|)
decl_stmt|;
name|String
name|httpMethod
init|=
name|r
operator|.
name|getHeaderString
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
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
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|r
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pathInfo
operator|.
name|endsWith
argument_list|(
literal|"/bookstore/version"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET"
argument_list|,
name|httpMethod
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseHasBeenReceivedWhenAddingNewBook
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"1234"
argument_list|)
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"Book 1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|CREATED
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseHasBeenReceivedWhenQueringAllBooks
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResponseHasBeenReceivedWhenQueringBooksById
parameter_list|()
block|{
specifier|final
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"Book "
operator|+
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
name|r
operator|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/byIds"
argument_list|)
operator|.
name|query
argument_list|(
literal|"ids"
argument_list|,
literal|"1234"
argument_list|)
operator|.
name|query
argument_list|(
literal|"ids"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|query
argument_list|(
literal|"ids"
argument_list|,
name|id
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|r
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddAndQueryOneBook
parameter_list|()
block|{
specifier|final
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"Book 1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|CREATED
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
name|r
operator|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books"
argument_list|)
operator|.
name|path
argument_list|(
name|id
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
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
name|Book
name|book
init|=
name|r
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|book
operator|.
name|getId
argument_list|()
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
name|createWebClient
argument_list|(
name|url
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
return|;
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
name|String
name|mediaType
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|?
argument_list|>
name|providers
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
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
name|url
argument_list|,
name|providers
argument_list|)
operator|.
name|accept
argument_list|(
name|mediaType
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
literal|10000000L
argument_list|)
expr_stmt|;
return|return
name|wc
return|;
block|}
specifier|protected
name|String
name|getBasePath
parameter_list|()
block|{
return|return
literal|"/rest/bookstore"
return|;
block|}
specifier|protected
specifier|abstract
name|int
name|getPort
parameter_list|()
function_decl|;
block|}
end_class

end_unit

