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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
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
name|GetMethod
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
name|JAXRSClientServerNonSpringBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookNonSpringServer
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
literal|10000000
argument_list|)
expr_stmt|;
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
argument_list|<
name|Book
argument_list|>
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
name|testGetBook123ApplicationSingleton
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
literal|"/application/bookstore/books/123"
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
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|get
operator|.
name|setRequestHeader
argument_list|(
literal|"Accept"
argument_list|,
name|acceptType
argument_list|)
expr_stmt|;
name|get
operator|.
name|setRequestHeader
argument_list|(
literal|"Accept-Language"
argument_list|,
literal|"da;q=0.8,en"
argument_list|)
expr_stmt|;
name|get
operator|.
name|setRequestHeader
argument_list|(
literal|"Book"
argument_list|,
literal|"1,2,3"
argument_list|)
expr_stmt|;
name|HttpClient
name|httpClient
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
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedStatus
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|String
name|content
init|=
name|getStringFromInputStream
argument_list|(
name|get
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Expected value is wrong"
argument_list|,
name|expectedValue
argument_list|,
name|content
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
name|get
operator|.
name|getResponseHeader
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

