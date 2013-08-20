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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|JAXRSClientFactory
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

begin_class
specifier|public
class|class
name|JAXRSClientServerResourceJacksonSpringProviderTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerResourceJacksonSpringProviders
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
name|BookServerResourceJacksonSpringProviders
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
name|testGetBook123
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store1/bookstore/books/123"
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|URLConnection
name|connect
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|connect
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Jackson output not correct"
argument_list|,
literal|"{\"class\":\"org.apache.cxf.systest.jaxrs.Book\",\"name\":\"CXF in Action\",\"id\":123}"
argument_list|,
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSuperBookProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store2"
decl_stmt|;
name|BookStoreSpring
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|BookStoreSpring
operator|.
name|class
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
name|SuperBook
name|book
init|=
name|proxy
operator|.
name|getSuperBookJson
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|999L
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
name|testGetSuperBookCollectionProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store2"
decl_stmt|;
name|BookStoreSpring
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|BookStoreSpring
operator|.
name|class
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
name|List
argument_list|<
name|SuperBook
argument_list|>
name|books
init|=
name|proxy
operator|.
name|getSuperBookCollectionJson
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|999L
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|testEchoSuperBookCollectionProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store2"
decl_stmt|;
name|BookStoreSpring
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|BookStoreSpring
operator|.
name|class
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
name|List
argument_list|<
name|SuperBook
argument_list|>
name|books
init|=
name|proxy
operator|.
name|echoSuperBookCollectionJson
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoSuperBookProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store2"
decl_stmt|;
name|BookStoreSpring
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|BookStoreSpring
operator|.
name|class
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
name|SuperBook
name|book
init|=
name|proxy
operator|.
name|echoSuperBookJson
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookCollectionProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore"
decl_stmt|;
name|GenericBookStoreSpring
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|GenericBookStoreSpring
operator|.
name|class
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
name|List
argument_list|<
name|SuperBook
argument_list|>
name|books
init|=
name|proxy
operator|.
name|echoSuperBookCollectionJson
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore"
decl_stmt|;
name|GenericBookStoreSpring
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|GenericBookStoreSpring
operator|.
name|class
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
name|proxy
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
literal|1000000000L
argument_list|)
expr_stmt|;
name|SuperBook
name|book
init|=
name|proxy
operator|.
name|echoSuperBookJson
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookProxy2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore2"
decl_stmt|;
name|GenericBookStoreSpring2
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|GenericBookStoreSpring2
operator|.
name|class
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
name|proxy
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
literal|1000000000L
argument_list|)
expr_stmt|;
name|SuperBook
name|book
init|=
name|proxy
operator|.
name|echoSuperBookJson
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookCollectionProxy2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore2"
decl_stmt|;
name|GenericBookStoreSpring2
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|GenericBookStoreSpring2
operator|.
name|class
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
name|List
argument_list|<
name|SuperBook
argument_list|>
name|books
init|=
name|proxy
operator|.
name|echoSuperBookCollectionJson
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookWebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore/books/superbook"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
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
name|wc
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|SuperBook
name|book
init|=
name|wc
operator|.
name|post
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookWebClientXml
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore/books/superbook"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
argument_list|)
expr_stmt|;
name|SuperBook
name|book
init|=
name|wc
operator|.
name|post
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookCollectionWebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore/books/superbooks"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
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
literal|100000000L
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|SuperBook
argument_list|>
name|books
init|=
name|wc
operator|.
name|postAndGetCollection
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|SuperBook
name|book
init|=
name|books
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetGenericSuperBookCollectionWebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore/books/superbooks2"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
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
literal|100000000L
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SuperBook
argument_list|>
name|books
init|=
name|wc
operator|.
name|get
argument_list|(
operator|new
name|GenericType
argument_list|<
name|List
argument_list|<
name|SuperBook
argument_list|>
argument_list|>
argument_list|()
block|{         }
argument_list|)
decl_stmt|;
name|SuperBook
name|book
init|=
name|books
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoGenericSuperBookCollectionWebClientXml
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/genericstore/books/superbooks"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|SuperBook
argument_list|>
name|books
init|=
name|wc
operator|.
name|postAndGetCollection
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|SuperBook
argument_list|(
literal|"Super"
argument_list|,
literal|124L
argument_list|,
literal|true
argument_list|)
argument_list|)
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|,
name|SuperBook
operator|.
name|class
argument_list|)
decl_stmt|;
name|SuperBook
name|book
init|=
name|books
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|book
operator|.
name|isSuperBook
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCollectionOfBooks
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store1/bookstore/collections"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
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
name|wc
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|Book
argument_list|>
name|collection
init|=
name|wc
operator|.
name|getCollection
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|collection
operator|.
name|iterator
argument_list|()
operator|.
name|next
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
name|testGetCollectionOfSuperBooks
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/webapp/store2/books/superbooks"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
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
name|wc
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
extends|extends
name|Book
argument_list|>
name|collection
init|=
name|wc
operator|.
name|getCollection
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|collection
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|999L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getStringFromInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|IOUtils
operator|.
name|toString
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
end_class

end_unit

