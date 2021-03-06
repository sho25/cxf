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
name|StringWriter
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
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|Abdera
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|factory
operator|.
name|Factory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Content
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
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
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|CloseableHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|json
operator|.
name|JSONObject
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSAtomBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|AtomBookServer
operator|.
name|PORT
decl_stmt|;
specifier|private
name|Abdera
name|abdera
init|=
operator|new
name|Abdera
argument_list|()
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
name|AtomBookServer
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
name|testGetBooks
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
literal|"/bookstore/bookstore/books/feed"
decl_stmt|;
name|Feed
name|feed
init|=
name|getFeed
argument_list|(
name|endpointAddress
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/feed"
argument_list|,
name|feed
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Collection of Books"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/feed"
argument_list|,
literal|"resources/expected_atom_books_json.txt"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/jsonfeed"
argument_list|,
literal|"resources/expected_atom_books_jsonfeed.txt"
argument_list|,
literal|"application/json, text/html, application/xml;q=0.9,"
operator|+
literal|" application/xhtml+xml, image/png, image/jpeg, image/gif,"
operator|+
literal|" image/x-xbitmap, */*;q=0.1"
argument_list|)
expr_stmt|;
name|Entry
name|entry
init|=
name|addEntry
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|entry
operator|=
name|addEntry
argument_list|(
name|endpointAddress
operator|+
literal|"/relative"
argument_list|)
expr_stmt|;
name|endpointAddress
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/subresources/123"
expr_stmt|;
name|entry
operator|=
name|getEntry
argument_list|(
name|endpointAddress
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|entry
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/entries/123"
argument_list|,
literal|"resources/expected_atom_book_json.txt"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/entries/123?_type="
operator|+
literal|"application/json"
argument_list|,
literal|"resources/expected_atom_book_json.txt"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/entries/123?_type="
operator|+
literal|"json"
argument_list|,
literal|"resources/expected_atom_book_json.txt"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
comment|// do the same using extension mappings
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/entries/123.json"
argument_list|,
literal|"resources/expected_atom_book_json.txt"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
comment|// do the same using extension mappings& matrix parameters
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore/books/entries/123.json;a=b"
argument_list|,
literal|"resources/expected_atom_book_json_matrix.txt"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Entry
name|addEntry
parameter_list|(
name|String
name|endpointAddress
parameter_list|)
throws|throws
name|Exception
block|{
name|Entry
name|e
init|=
name|createBookEntry
argument_list|(
literal|256
argument_list|,
literal|"AtomBook"
argument_list|)
decl_stmt|;
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|e
operator|.
name|writeTo
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|post
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
name|w
operator|.
name|toString
argument_list|()
argument_list|,
name|ContentType
operator|.
name|APPLICATION_ATOM_XML
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|location
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|post
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|201
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|location
operator|=
name|response
operator|.
name|getFirstHeader
argument_list|(
literal|"Location"
argument_list|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|InputStream
name|ins
init|=
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|Document
argument_list|<
name|Entry
argument_list|>
name|entryDoc
init|=
name|abdera
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|copyIn
argument_list|(
name|ins
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|entryDoc
operator|.
name|getRoot
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|post
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
name|Entry
name|entry
init|=
name|getEntry
argument_list|(
name|location
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|location
argument_list|,
name|entry
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AtomBook"
argument_list|,
name|entry
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|entry
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBooks2
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
literal|"/bookstore/sub/"
decl_stmt|;
name|Feed
name|feed
init|=
name|getFeed
argument_list|(
name|endpointAddress
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/sub/"
argument_list|,
name|feed
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Collection of Books"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/sub/books/entries/123.json"
argument_list|,
literal|"resources/expected_atom_book_json2.txt"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBooks3
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareJson
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/atom/atomservice3/atom/books/entries/123.json"
argument_list|,
literal|"resources/expected_atom_book_json3.txt"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBooksWithCustomProvider
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
literal|"/bookstore/bookstore4/books/feed"
decl_stmt|;
name|Feed
name|feed
init|=
name|getFeed
argument_list|(
name|endpointAddress
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore/bookstore4/books/feed"
argument_list|,
name|feed
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Collection of Books"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getAndCompareJson
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|get
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
name|get
operator|.
name|setHeader
argument_list|(
literal|"Accept"
argument_list|,
name|type
argument_list|)
expr_stmt|;
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|String
name|jsonContent
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|expected
init|=
name|getStringFromInputStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resourcePath
argument_list|)
argument_list|)
decl_stmt|;
name|expected
operator|=
name|expected
operator|.
name|replaceAll
argument_list|(
literal|"9080"
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|JSONObject
name|obj1
init|=
operator|new
name|JSONObject
argument_list|(
name|jsonContent
argument_list|)
decl_stmt|;
name|JSONObject
name|obj2
init|=
operator|new
name|JSONObject
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Atom entry should've been formatted as json"
argument_list|,
name|obj1
operator|.
name|toString
argument_list|()
argument_list|,
name|obj2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|Entry
name|createBookEntry
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
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
name|id
argument_list|)
expr_stmt|;
name|b
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|Factory
name|factory
init|=
name|Abdera
operator|.
name|getNewFactory
argument_list|()
decl_stmt|;
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|Entry
name|e
init|=
name|factory
operator|.
name|getAbdera
argument_list|()
operator|.
name|newEntry
argument_list|()
decl_stmt|;
name|e
operator|.
name|setTitle
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|jc
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|b
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|Content
name|ct
init|=
name|factory
operator|.
name|newContent
argument_list|(
name|Content
operator|.
name|Type
operator|.
name|XML
argument_list|)
decl_stmt|;
name|ct
operator|.
name|setValue
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|setContentElement
argument_list|(
name|ct
argument_list|)
expr_stmt|;
return|return
name|e
return|;
block|}
specifier|private
name|Feed
name|getFeed
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|String
name|acceptType
parameter_list|)
throws|throws
name|Exception
block|{
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|get
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
if|if
condition|(
name|acceptType
operator|!=
literal|null
condition|)
block|{
name|get
operator|.
name|setHeader
argument_list|(
literal|"Accept"
argument_list|,
name|acceptType
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|Document
argument_list|<
name|Feed
argument_list|>
name|doc
init|=
name|abdera
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|copyIn
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|doc
operator|.
name|getRoot
argument_list|()
return|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|Entry
name|getEntry
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|String
name|acceptType
parameter_list|)
throws|throws
name|Exception
block|{
name|CloseableHttpClient
name|client
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|get
operator|.
name|setHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
if|if
condition|(
name|acceptType
operator|!=
literal|null
condition|)
block|{
name|get
operator|.
name|setHeader
argument_list|(
literal|"Accept"
argument_list|,
name|acceptType
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|CloseableHttpResponse
name|response
init|=
name|client
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|Document
argument_list|<
name|Entry
argument_list|>
name|doc
init|=
name|abdera
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|copyIn
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getContent
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|doc
operator|.
name|getRoot
argument_list|()
return|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|InputStream
name|copyIn
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|CachedOutputStream
name|bos
init|=
operator|new
name|CachedOutputStream
argument_list|()
init|)
block|{
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|in
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|in
operator|=
name|bos
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|in
return|;
block|}
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

