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
name|JAXRSRequestDispatcherTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerRequestDispatch
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
name|BookServerRequestDispatch
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
name|testGetBookHTML
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
literal|"/the/bookstore1/books/html/123"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
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
literal|100000000
argument_list|)
expr_stmt|;
name|XMLSource
name|source
init|=
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
operator|.
name|get
argument_list|(
name|XMLSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
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
name|namespaces
operator|.
name|put
argument_list|(
literal|"xhtml"
argument_list|,
literal|"http://www.w3.org/1999/xhtml"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"books"
argument_list|,
literal|"http://www.w3.org/books"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|source
operator|.
name|getValue
argument_list|(
literal|"xhtml:html/xhtml:body/xhtml:ul/books:bookTag"
argument_list|,
name|namespaces
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF Rocks"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|String
name|ct
init|=
name|client
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
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"text/html"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"JSP pages need to be precompiled by Maven build"
argument_list|)
specifier|public
name|void
name|testGetBookJSPRequestScope
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
literal|"/the/bookstore2/books/html/123"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
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
literal|100000000
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
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
name|data
operator|.
name|contains
argument_list|(
literal|"<h1>Request Book 123</h1>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|contains
argument_list|(
literal|"<books:bookName>CXF in Action</books:bookName>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"JSP pages need to be precompiled by Maven build"
argument_list|)
specifier|public
name|void
name|testGetBookJSPSessionScope
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
literal|"/the/bookstore3/books/html/456"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
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
literal|100000000
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
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
name|data
operator|.
name|contains
argument_list|(
literal|"<h1>Session Book 456</h1>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|data
operator|.
name|contains
argument_list|(
literal|"<books:bookName>CXF in Action</books:bookName>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookHTMLFromDefaultServlet
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
literal|"/the/bookstore4/books/html/123"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
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
literal|100000000
argument_list|)
expr_stmt|;
name|XMLSource
name|source
init|=
name|client
operator|.
name|accept
argument_list|(
literal|"text/html"
argument_list|)
operator|.
name|get
argument_list|(
name|XMLSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
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
name|namespaces
operator|.
name|put
argument_list|(
literal|"xhtml"
argument_list|,
literal|"http://www.w3.org/1999/xhtml"
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|put
argument_list|(
literal|"books"
argument_list|,
literal|"http://www.w3.org/books"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|source
operator|.
name|getValue
argument_list|(
literal|"xhtml:html/xhtml:body/xhtml:ul/books:bookTag"
argument_list|,
name|namespaces
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF Rocks"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

