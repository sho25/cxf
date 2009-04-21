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
name|JAXRSClientServerSpringBookTest
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
name|BookServerSpring
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookByUriInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/thebooks/bookstore/bookinfo?"
operator|+
literal|"param1=12&param2=3"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookByUriInfo2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/thebooks3/bookstore/bookinfo?"
operator|+
literal|"param1=12&param2=3"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
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
literal|"http://localhost:9080/the/bookstore/books/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|,
literal|"application/jettison"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookAsArray
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9080/the/bookstore/books/list/123"
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
name|assertEquals
argument_list|(
literal|"{\"Books\":{\"books\":[{\"id\":123,\"name\":\"CXF in Action\"}]}}"
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
name|testGetBookWithEncodedQueryValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/bookstore/booksquery?id=12%2B3"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWithEncodedPathValue
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/bookstore/id=12%2B3"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWithEncodedPathValue2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/bookstore/id=12+3"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultBook
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/bookstore"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_get_book123json.txt"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getBook
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|String
name|resource
parameter_list|)
throws|throws
name|Exception
block|{
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
name|resource
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getBook
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|type
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
name|type
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
name|resource
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
name|testAddInvalidXmlBook
parameter_list|()
throws|throws
name|Exception
block|{
name|doPost
argument_list|(
literal|"http://localhost:9080/the/bookstore/books/convert"
argument_list|,
literal|500
argument_list|,
literal|"application/xml"
argument_list|,
literal|"resources/add_book.txt"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|doPost
argument_list|(
literal|"http://localhost:9080/the/thebooks/bookstore/books/convert"
argument_list|,
literal|500
argument_list|,
literal|"application/xml"
argument_list|,
literal|"resources/add_book.txt"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddInvalidJsonBook
parameter_list|()
throws|throws
name|Exception
block|{
name|doPost
argument_list|(
literal|"http://localhost:9080/the/bookstore/books/convert"
argument_list|,
literal|500
argument_list|,
literal|"application/json"
argument_list|,
literal|"resources/add_book2json_invalid.txt"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|doPost
argument_list|(
literal|"http://localhost:9080/the/thebooks/bookstore/books/convert"
argument_list|,
literal|500
argument_list|,
literal|"application/json"
argument_list|,
literal|"resources/add_book2json_invalid.txt"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddValidXmlBook
parameter_list|()
throws|throws
name|Exception
block|{
name|doPost
argument_list|(
literal|"http://localhost:9080/the/bookstore/books/convert"
argument_list|,
literal|200
argument_list|,
literal|"application/xml"
argument_list|,
literal|"resources/add_book2.txt"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|)
expr_stmt|;
name|doPost
argument_list|(
literal|"http://localhost:9080/the/thebooks/bookstore/books/convert"
argument_list|,
literal|200
argument_list|,
literal|"application/xml"
argument_list|,
literal|"resources/add_book2.txt"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetBookAegis
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/the/thebooks4/bookstore/books/aegis"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"resources/expected_add_book_aegis.txt"
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddValidBookJson
parameter_list|()
throws|throws
name|Exception
block|{
name|doPost
argument_list|(
literal|"http://localhost:9080/the/bookstore/books/convert"
argument_list|,
literal|200
argument_list|,
literal|"application/json"
argument_list|,
literal|"resources/add_book2json.txt"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|)
expr_stmt|;
name|doPost
argument_list|(
literal|"http://localhost:9080/the/thebooks/bookstore/books/convert"
argument_list|,
literal|200
argument_list|,
literal|"application/json"
argument_list|,
literal|"resources/add_book2json.txt"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|)
expr_stmt|;
name|doPost
argument_list|(
literal|"http://localhost:9080/the/thebooks/bookstore/books/convert"
argument_list|,
literal|200
argument_list|,
literal|"application/jettison"
argument_list|,
literal|"resources/add_book2json.txt"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doPost
parameter_list|(
name|String
name|endpointAddress
parameter_list|,
name|int
name|expectedStatus
parameter_list|,
name|String
name|contentType
parameter_list|,
name|String
name|inResource
parameter_list|,
name|String
name|expectedResource
parameter_list|)
throws|throws
name|Exception
block|{
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
name|inResource
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
name|contentType
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
literal|"text/xml"
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
name|expectedStatus
argument_list|,
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedStatus
operator|!=
literal|500
condition|)
block|{
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|expectedResource
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
else|else
block|{
name|assertTrue
argument_list|(
name|post
operator|.
name|getResponseBodyAsString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Cannot find the declaration of element"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

