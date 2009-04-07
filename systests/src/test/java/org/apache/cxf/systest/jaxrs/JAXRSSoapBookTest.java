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
name|File
import|;
end_import

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
name|WebApplicationException
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
name|MultivaluedMap
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
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|FileRequestEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|PostMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|RequestEntity
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
name|io
operator|.
name|CachedOutputStream
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
name|ResponseExceptionMapper
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
name|ext
operator|.
name|form
operator|.
name|Form
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
name|ext
operator|.
name|xml
operator|.
name|XMLSource
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
name|impl
operator|.
name|MetadataMap
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

begin_class
specifier|public
class|class
name|JAXRSSoapBookTest
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerRestSoap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAll
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|in
init|=
name|getRestInputStream
argument_list|(
literal|"http://localhost:9092/test/services/rest2/myRestService"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"0"
argument_list|,
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
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
name|InputStream
name|in
init|=
name|getRestInputStream
argument_list|(
literal|"http://localhost:9092/test/services/rest/bookstore/123"
argument_list|)
decl_stmt|;
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/expected_get_book123.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
argument_list|,
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123Client
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|proxy
operator|.
name|getBook
argument_list|(
operator|new
name|Long
argument_list|(
literal|"123"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123WebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|client
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123XMLSource
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|XMLSource
name|source
init|=
name|client
operator|.
name|get
argument_list|(
name|XMLSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|source
operator|.
name|setBuffering
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|source
operator|.
name|getNode
argument_list|(
literal|"/Book"
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|=
name|source
operator|.
name|getNode
argument_list|(
literal|"/Book"
argument_list|,
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoBookWebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/books/0/subresource"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|client
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|client
operator|.
name|getResponse
argument_list|()
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
name|testGetBook123WebClientResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|readBook
argument_list|(
operator|(
name|InputStream
operator|)
name|client
operator|.
name|get
argument_list|()
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook356ClientException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|TestResponseExceptionMapper
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|proxy
operator|.
name|getBook
argument_list|(
literal|356L
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BookNotFoundFault
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No Book with id 356 is available"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSubresourceClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
decl_stmt|;
name|BookSubresource
name|bs
init|=
name|proxy
operator|.
name|getBookSubresource
argument_list|(
literal|"125"
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getTheBook
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|125
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSubresourceClient2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
decl_stmt|;
name|doTestSubresource
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|BookStoreJaxrsJaxws
name|proxy2
init|=
name|proxy
operator|.
name|getBookStore
argument_list|(
literal|"number1"
argument_list|)
decl_stmt|;
name|doTestSubresource
argument_list|(
name|proxy2
argument_list|)
expr_stmt|;
name|BookStoreJaxrsJaxws
name|proxy3
init|=
name|proxy2
operator|.
name|getBookStore
argument_list|(
literal|"number1"
argument_list|)
decl_stmt|;
name|doTestSubresource
argument_list|(
name|proxy3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSubresourceWebClientProxyBean
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:9092/test/services/rest"
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|,
name|MediaType
operator|.
name|TEXT_XML_TYPE
argument_list|)
expr_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|fromClient
argument_list|(
name|client
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|doTestSubresource
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|BookStoreJaxrsJaxws
name|proxy2
init|=
name|JAXRSClientFactory
operator|.
name|fromClient
argument_list|(
name|WebClient
operator|.
name|client
argument_list|(
name|proxy
argument_list|)
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
decl_stmt|;
name|doTestSubresource
argument_list|(
name|proxy2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSubresourceWebClientProxy2
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:9092/test/services/rest/bookstore"
argument_list|)
operator|.
name|path
argument_list|(
literal|"/books/378"
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|BookSubresource
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|fromClient
argument_list|(
name|client
argument_list|,
name|BookSubresource
operator|.
name|class
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|proxy
operator|.
name|getTheBook2
argument_list|(
literal|"CXF "
argument_list|,
literal|"in "
argument_list|,
literal|"Action "
argument_list|,
literal|"- 3"
argument_list|,
literal|"7"
argument_list|,
literal|"8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|378
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 378"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestSubresource
parameter_list|(
name|BookStoreJaxrsJaxws
name|proxy
parameter_list|)
throws|throws
name|Exception
block|{
name|BookSubresource
name|bs
init|=
name|proxy
operator|.
name|getBookSubresource
argument_list|(
literal|"378"
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getTheBook2
argument_list|(
literal|"CXF "
argument_list|,
literal|"in "
argument_list|,
literal|"Action "
argument_list|,
literal|"- 3"
argument_list|,
literal|"7"
argument_list|,
literal|"8"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|378
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 378"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|reset
argument_list|()
operator|.
name|header
argument_list|(
literal|"N4"
argument_list|,
literal|"- 4"
argument_list|)
expr_stmt|;
name|b
operator|=
name|bs
operator|.
name|getTheBook2
argument_list|(
literal|"CXF "
argument_list|,
literal|"in "
argument_list|,
literal|"Action "
argument_list|,
literal|null
argument_list|,
literal|"7"
argument_list|,
literal|"8"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|378
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 478"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWebClientForm
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest/bookstore/books/679/subresource3"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|"id"
argument_list|,
literal|"679"
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|"name"
argument_list|,
literal|"CXF in Action - 679"
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|readBook
argument_list|(
operator|(
name|InputStream
operator|)
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|form
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
operator|)
name|map
argument_list|)
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|679
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 679"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWebClientForm2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest/bookstore/books/679/subresource3"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|Form
name|f
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
name|f
operator|.
name|set
argument_list|(
literal|"id"
argument_list|,
literal|"679"
argument_list|)
operator|.
name|set
argument_list|(
literal|"name"
argument_list|,
literal|"CXF in Action - 679"
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|readBook
argument_list|(
operator|(
name|InputStream
operator|)
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|form
argument_list|(
name|f
argument_list|)
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|679
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 679"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSubresourceClientFormParam
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
decl_stmt|;
name|BookSubresource
name|bs
init|=
name|proxy
operator|.
name|getBookSubresource
argument_list|(
literal|"679"
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getTheBook3
argument_list|(
literal|"679"
argument_list|,
literal|"CXF in Action - 679"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|679
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 679"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetBook123WebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"CXF in Action - 2"
argument_list|)
expr_stmt|;
name|Book
name|b2
init|=
name|client
operator|.
name|post
argument_list|(
name|b
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|b
argument_list|,
name|b2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|124
argument_list|,
name|b2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 2"
argument_list|,
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetBook123Client
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|baseAddress
init|=
literal|"http://localhost:9092/test/services/rest"
decl_stmt|;
name|BookStoreJaxrsJaxws
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|,
name|BookStoreJaxrsJaxws
operator|.
name|class
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b
operator|.
name|setId
argument_list|(
literal|124
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
literal|"CXF in Action - 2"
argument_list|)
expr_stmt|;
name|Book
name|b2
init|=
name|proxy
operator|.
name|addBook
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|b
argument_list|,
name|b2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|124
argument_list|,
name|b2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action - 2"
argument_list|,
name|b2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetBookRest
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9092/test/services/rest/bookstore/books"
decl_stmt|;
name|File
name|input
init|=
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/add_book.txt"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|PostMethod
name|post
init|=
operator|new
name|PostMethod
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|post
operator|.
name|setRequestHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|RequestEntity
name|entity
init|=
operator|new
name|FileRequestEntity
argument_list|(
name|input
argument_list|,
literal|"text/xml; charset=ISO-8859-1"
argument_list|)
decl_stmt|;
name|post
operator|.
name|setRequestEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|int
name|result
init|=
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/expected_add_book.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
argument_list|,
name|post
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// Release current connection to the connection pool once you are done
name|post
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSoap
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|wsdlAddress
init|=
literal|"http://localhost:9092/test/services/soap/bookservice?wsdl"
decl_stmt|;
name|URL
name|wsdlUrl
init|=
operator|new
name|URL
argument_list|(
name|wsdlAddress
argument_list|)
decl_stmt|;
name|BookSoapService
name|service
init|=
operator|new
name|BookSoapService
argument_list|(
name|wsdlUrl
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://books.com"
argument_list|,
literal|"BookService"
argument_list|)
argument_list|)
decl_stmt|;
name|BookStoreJaxrsJaxws
name|store
init|=
name|service
operator|.
name|getBookPort
argument_list|()
decl_stmt|;
name|Book
name|book
init|=
name|store
operator|.
name|getBook
argument_list|(
operator|new
name|Long
argument_list|(
literal|123
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"id is wrong"
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
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
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|getOut
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|InputStream
name|getRestInputStream
parameter_list|(
name|String
name|endpointAddress
parameter_list|)
throws|throws
name|Exception
block|{
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
literal|"application/xml,text/plain"
argument_list|)
expr_stmt|;
return|return
name|connect
operator|.
name|getInputStream
argument_list|()
return|;
block|}
specifier|private
name|Book
name|readBook
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|c
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Book
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Unmarshaller
name|u
init|=
name|c
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
return|return
operator|(
name|Book
operator|)
name|u
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
return|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|TestResponseExceptionMapper
implements|implements
name|ResponseExceptionMapper
argument_list|<
name|BookNotFoundFault
argument_list|>
block|{
specifier|public
name|TestResponseExceptionMapper
parameter_list|()
block|{         }
specifier|public
name|BookNotFoundFault
name|fromResponse
parameter_list|(
name|Response
name|r
parameter_list|)
block|{
name|Object
name|value
init|=
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"BOOK-HEADER"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|BookNotFoundFault
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|WebApplicationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

