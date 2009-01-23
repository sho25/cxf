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
name|InputStreamRequestEntity
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
name|JAXRSMultipartTest
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
name|MultipartServer
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
name|testBookAsRootAttachmentStreamSource
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/stream"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBookAsRootAttachmentInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/istream"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBookAsMessageContextDataHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/mchandlers"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddBookAsRootAttachmentJAXB
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/jaxb"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddBookAsDataSource
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/dsource"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddBookAsDataSource2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/dsource2"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddBookAsJAXB2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/jaxb2"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddBookAsJAXBJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/jaxbjson"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData2"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumesMismatch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/mismatch1"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData2"
argument_list|,
literal|415
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConsumesMismatch2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/mismatch2"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData2"
argument_list|,
literal|415
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddBookAsDataHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstore/books/dhandler"
decl_stmt|;
name|doAddBook
argument_list|(
name|address
argument_list|,
literal|"attachmentData"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doAddBook
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|resourceName
parameter_list|,
name|int
name|status
parameter_list|)
throws|throws
name|Exception
block|{
name|PostMethod
name|post
init|=
operator|new
name|PostMethod
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|String
name|ct
init|=
literal|"multipart/related; type=\"text/xml\"; "
operator|+
literal|"start=\"rootPart\"; "
operator|+
literal|"boundary=\"----=_Part_4_701508.1145579811786\""
decl_stmt|;
name|post
operator|.
name|setRequestHeader
argument_list|(
literal|"Content-Type"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/cxf/systest/jaxrs/"
operator|+
name|resourceName
argument_list|)
decl_stmt|;
name|RequestEntity
name|entity
init|=
operator|new
name|InputStreamRequestEntity
argument_list|(
name|is
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
name|status
argument_list|,
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|status
operator|==
literal|200
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

