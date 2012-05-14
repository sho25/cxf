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
name|getStringFromInputStream
argument_list|(
name|expected
argument_list|)
operator|.
name|trim
argument_list|()
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
comment|//System.out.println("---" + getStringFromInputStream(in));
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
comment|//System.out.println(bos.getOut().toString());
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

