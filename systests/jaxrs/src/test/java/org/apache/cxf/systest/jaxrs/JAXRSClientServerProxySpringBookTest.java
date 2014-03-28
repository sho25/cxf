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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|DOMUtils
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
name|model
operator|.
name|wadl
operator|.
name|WadlGenerator
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
name|staxutils
operator|.
name|StaxUtils
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
name|JAXRSClientServerProxySpringBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerProxySpring
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
name|BookServerProxySpring
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
name|testGetWadlResourcesInfo
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
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test"
operator|+
literal|"?_wadl&_type=xml"
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
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
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|client
operator|.
name|get
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
name|root
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application"
argument_list|,
name|root
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|resourcesEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|root
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"resources"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resourcesEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|resourcesEl
init|=
name|resourcesEls
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/"
argument_list|,
name|resourcesEl
operator|.
name|getAttribute
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|resourceEls
init|=
name|DOMUtils
operator|.
name|getChildrenWithName
argument_list|(
name|resourcesEl
argument_list|,
name|WadlGenerator
operator|.
name|WADL_NS
argument_list|,
literal|"resource"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resourceEls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookNotFound
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
literal|"/test/bookstore/books/12345"
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
name|HttpURLConnection
name|connect
init|=
operator|(
name|HttpURLConnection
operator|)
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
literal|"text/plain,application/xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|connect
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|connect
operator|.
name|getErrorStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
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
literal|"resources/expected_get_book_notfound_mapped.txt"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Exception is not mapped correctly"
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBook123
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/bookstorestorage/thosebooks/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookSingleton
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/4/bookstore/books/123"
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/4/bookstore/books/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookInterfaceSingleton
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/4/bookstorestorage/thosebooks/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookPrototype
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/5/bookstore/books/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookInterfacePrototype
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/5/bookstorestorage/thosebooks/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookInterface2Prototype
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/6/bookstorestorage/thosebooks/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBook123UserResource
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/2/bookstore/books/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBook123UserResourceInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/3/bookstore2/books/123"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getBook
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
literal|"Content-Type"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|connect
operator|.
name|addRequestProperty
argument_list|(
literal|"SpringProxy"
argument_list|,
literal|"true"
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
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
argument_list|)
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookOverloaded
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/bookstorestorage/thosebooks/123/123"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetThatBookOverloaded2
parameter_list|()
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/bookstorestorage/thosebooks"
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
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/bookstore/books/123"
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
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/expected_get_book123json.txt"
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
name|testGetBookWithRequestScope
parameter_list|()
block|{
comment|// the BookStore method which will handle this request depends on the injected HttpHeaders
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/test/request/bookstore/booksecho2"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|type
argument_list|(
literal|"text/plain"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|wc
operator|.
name|header
argument_list|(
literal|"CustomHeader"
argument_list|,
literal|"custom-header"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|wc
operator|.
name|post
argument_list|(
literal|"CXF"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"custom-header"
argument_list|,
name|wc
operator|.
name|getResponse
argument_list|()
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"CustomHeader"
argument_list|)
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
name|String
name|str
init|=
operator|new
name|String
argument_list|(
name|bos
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
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
name|str
return|;
block|}
specifier|private
name|String
name|stripXmlInstructionIfNeeded
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|!=
literal|null
operator|&&
name|str
operator|.
name|startsWith
argument_list|(
literal|"<?xml"
argument_list|)
condition|)
block|{
name|int
name|index
init|=
name|str
operator|.
name|indexOf
argument_list|(
literal|"?>"
argument_list|)
decl_stmt|;
name|str
operator|=
name|str
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|str
return|;
block|}
block|}
end_class

end_unit

