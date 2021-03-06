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
name|InternalServerErrorException
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
name|Response
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
name|JAXRSClientFactoryBean
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
name|apache
operator|.
name|http
operator|.
name|Header
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
name|assertNotSame
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSClientServerNonSpringBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|BookNonSpringServer
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
name|BookNonSpringServer
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
name|testGetBook123Singleton
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareAsStrings
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/singleton/bookstore/books/123"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStaticResource
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
literal|"/singleton/staticmodel.xml"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|response
operator|.
name|startsWith
argument_list|(
literal|"<model"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml+model"
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
literal|"Content-Type"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetPathFromUriInfo
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
literal|"/application/bookstore/uifromconstructor"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|address
operator|+
literal|"?prop=cxf"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123UserModel
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareAsStrings
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/usermodel/bookstore/books/123"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123UserModelAuthorize
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/usermodel/bookstore/books"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setUsername
argument_list|(
literal|"Barry"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setModelRef
argument_list|(
literal|"classpath:org/apache/cxf/systest/jaxrs/resources/resources.xml"
argument_list|)
expr_stmt|;
name|WebClient
name|proxy
init|=
name|bean
operator|.
name|createWebClient
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|path
argument_list|(
literal|"{id}/authorize"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|proxy
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
name|testGetChapterUserModel
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareAsStrings
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/usermodel/bookstore/books/123/chapter"
argument_list|,
literal|"resources/expected_get_chapter1_utf.txt"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123UserModelInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareAsStrings
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/usermodel2/bookstore2/books/123"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBooksUserModelInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|BookStoreNoAnnotationsInterface
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|createFromModel
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/usermodel2"
argument_list|,
name|BookStoreNoAnnotationsInterface
operator|.
name|class
argument_list|,
literal|"classpath:org/apache/cxf/systest/jaxrs/resources/resources2.xml"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
operator|new
name|Book
argument_list|(
literal|"From Model"
argument_list|,
literal|1L
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|books
operator|.
name|add
argument_list|(
name|book
argument_list|)
expr_stmt|;
name|books
operator|=
name|proxy
operator|.
name|getBooks
argument_list|(
name|books
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|book
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"From Model"
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|testUserModelInterfaceOneWay
parameter_list|()
throws|throws
name|Exception
block|{
name|BookStoreNoAnnotationsInterface
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|createFromModel
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/usermodel2"
argument_list|,
name|BookStoreNoAnnotationsInterface
operator|.
name|class
argument_list|,
literal|"classpath:org/apache/cxf/systest/jaxrs/resources/resources2.xml"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|proxy
operator|.
name|pingBookStore
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|202
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|proxy
argument_list|)
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
name|testGetBook123ApplicationSingleton
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"/application/bookstore/default"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|wc
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
literal|"default"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|543L
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
name|testGetBook123ApplicationPerRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareAsStrings
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/application/bookstore2/bookheaders"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123Application11Singleton
parameter_list|()
throws|throws
name|Exception
block|{
name|getAndCompareAsStrings
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/application11/thebooks/bookstore/books/123"
argument_list|,
literal|"resources/expected_get_book123.txt"
argument_list|,
literal|"application/xml"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123Application11PerRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|Response
name|r
init|=
name|doTestPerRequest
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/application11/thebooks/bookstore2/bookheaders"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TheBook"
argument_list|,
name|r
operator|.
name|getHeaderString
argument_list|(
literal|"BookWriter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123TwoApplications
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestPerRequest
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/application6/thebooks/bookstore2/bookheaders"
argument_list|)
expr_stmt|;
name|doTestPerRequest
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/application6/the%20books2/bookstore2/book%20headers"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Response
name|doTestPerRequest
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
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
literal|"application/xml"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
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
literal|"CXF in Action"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
return|return
name|r
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNonExistentBook
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"/application11/thebooks/bookstore/books/321"
argument_list|)
decl_stmt|;
try|try
block|{
name|wc
operator|.
name|accept
argument_list|(
literal|"*/*"
argument_list|)
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InternalServerErrorException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No book found at all : 321"
argument_list|,
name|ex
operator|.
name|getResponse
argument_list|()
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBookWithNonExistentMethod
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"/application11/thebooks/bookstore/nonexistent"
argument_list|)
decl_stmt|;
try|try
block|{
name|wc
operator|.
name|accept
argument_list|(
literal|"*/*"
argument_list|)
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Nonexistent method"
argument_list|,
name|ex
operator|.
name|getResponse
argument_list|()
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|getAndCompareAsStrings
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|resourcePath
parameter_list|,
name|String
name|acceptType
parameter_list|,
name|int
name|status
parameter_list|)
throws|throws
name|Exception
block|{
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
name|getAndCompare
argument_list|(
name|address
argument_list|,
name|expected
argument_list|,
name|acceptType
argument_list|,
name|acceptType
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|getAndCompare
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|expectedValue
parameter_list|,
name|String
name|acceptType
parameter_list|,
name|String
name|expectedContentType
parameter_list|,
name|int
name|expectedStatus
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
literal|"Accept"
argument_list|,
name|acceptType
argument_list|)
expr_stmt|;
name|get
operator|.
name|setHeader
argument_list|(
literal|"Accept-Language"
argument_list|,
literal|"da;q=0.8,en"
argument_list|)
expr_stmt|;
name|get
operator|.
name|setHeader
argument_list|(
literal|"Book"
argument_list|,
literal|"1,2,3"
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
name|assertEquals
argument_list|(
name|expectedStatus
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
name|String
name|content
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
name|assertEquals
argument_list|(
literal|"Expected value is wrong"
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|expectedValue
argument_list|)
argument_list|,
name|stripXmlInstructionIfNeeded
argument_list|(
name|content
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedContentType
operator|!=
literal|null
condition|)
block|{
name|Header
name|ct
init|=
name|response
operator|.
name|getFirstHeader
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong type of response"
argument_list|,
name|expectedContentType
argument_list|,
name|ct
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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

