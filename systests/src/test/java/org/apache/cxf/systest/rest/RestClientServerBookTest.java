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
name|rest
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
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
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
name|binding
operator|.
name|http
operator|.
name|HttpBindingFactory
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|customer
operator|.
name|book
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
name|customer
operator|.
name|book
operator|.
name|BookService
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
name|customer
operator|.
name|book
operator|.
name|BookServiceWrapped
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
name|customer
operator|.
name|book
operator|.
name|GetAnotherBook
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
name|customer
operator|.
name|book
operator|.
name|GetBook
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
name|helpers
operator|.
name|XMLUtils
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
name|XPathUtils
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|RestClientServerBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|RestClientServerBookTest
operator|.
name|class
argument_list|)
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServer
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
name|testGetBookWithXmlRootElement
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|sf
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|BookService
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Turn off wrapped mode to make our xml prettier
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Use the HTTP Binding which understands the Java Rest Annotations
name|sf
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/xml/"
argument_list|)
expr_stmt|;
name|BookService
name|bs
init|=
operator|(
name|BookService
operator|)
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|GetBook
name|getBook
init|=
operator|new
name|GetBook
argument_list|()
decl_stmt|;
name|getBook
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|getBook
argument_list|(
name|getBook
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|123
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getName
argument_list|()
argument_list|,
literal|"CXF in Action"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWithOutXmlRootElement
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|sf
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|BookService
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Turn off wrapped mode to make our xml prettier
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Use the HTTP Binding which understands the Java Rest Annotations
name|sf
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/xml/"
argument_list|)
expr_stmt|;
name|BookService
name|bs
init|=
operator|(
name|BookService
operator|)
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|GetAnotherBook
name|getAnotherBook
init|=
operator|new
name|GetAnotherBook
argument_list|()
decl_stmt|;
name|getAnotherBook
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|getAnotherBook
argument_list|(
name|getAnotherBook
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|123
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getName
argument_list|()
argument_list|,
literal|"CXF in Action"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWrapped
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|sf
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|BookServiceWrapped
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Use the HTTP Binding which understands the Java Rest Annotations
name|sf
operator|.
name|getClientFactoryBean
argument_list|()
operator|.
name|setBindingId
argument_list|(
name|HttpBindingFactory
operator|.
name|HTTP_BINDING_ID
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9080/xmlwrapped/"
argument_list|)
expr_stmt|;
name|BookServiceWrapped
name|bs
init|=
operator|(
name|BookServiceWrapped
operator|)
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|getBook
argument_list|(
literal|123
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|123
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|book
operator|.
name|getName
argument_list|()
argument_list|,
literal|"CXF in Action"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookWrappedUsingURL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/xmlwrapped/books/123"
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
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"a1"
argument_list|,
literal|"http://book.acme.com"
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"a2"
argument_list|,
literal|"http://book.customer.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|XPathUtils
name|xp
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|xp
operator|.
name|isExist
argument_list|(
literal|"/a2:getBookResponse"
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xp
operator|.
name|isExist
argument_list|(
literal|"/a2:getBookResponse/a2:Book"
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xp
operator|.
name|isExist
argument_list|(
literal|"/a2:getBookResponse/a2:Book/a1:id"
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|xp
operator|.
name|getValue
argument_list|(
literal|"/a2:getBookResponse/a2:Book/a1:id"
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|xp
operator|.
name|getValue
argument_list|(
literal|"/a2:getBookResponse/a2:Book/a1:name"
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|STRING
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBooksJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/json/books"
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
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
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
literal|"resources/expected_json_books.txt"
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
name|testGetBookJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/json/books/123"
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
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
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
literal|"resources/expected_json_book123.txt"
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
name|testAddBookJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/json/books"
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
literal|"resources/add_book_json.txt"
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
name|RequestEntity
name|entity
init|=
operator|new
name|FileRequestEntity
argument_list|(
name|input
argument_list|,
literal|"text/plain; charset=ISO-8859-1"
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
comment|//System.out.println("Response status code: " + result);
comment|//System.out.println("Response body: ");
comment|//System.out.println(post.getResponseBodyAsString());
name|InputStream
name|expected
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"resources/expected_add_book_json.txt"
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

