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
name|nio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|helpers
operator|.
name|IOUtils
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
name|NioBookStoreTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
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
name|NioBookStoreServer
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
name|testGetAllBooks
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Response
name|response
init|=
name|createWebClient
argument_list|(
literal|"/bookstore"
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/books.txt"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAllBooksIs
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Response
name|response
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/is"
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/books.txt"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostBookStore
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Response
name|response
init|=
name|createWebClient
argument_list|(
literal|"/bookstore"
argument_list|,
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
argument_list|)
operator|.
name|post
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/books.txt"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|"Book Store uploaded: 10355 bytes"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|NioBookStoreServer
operator|.
name|PORT
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
block|}
end_class

end_unit

