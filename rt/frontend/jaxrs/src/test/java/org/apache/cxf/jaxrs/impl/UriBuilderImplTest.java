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
name|jaxrs
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|resources
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
name|jaxrs
operator|.
name|resources
operator|.
name|BookStore
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
name|resources
operator|.
name|UriBuilderWrongAnnotations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|UriBuilderImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testUri
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"http://foo/bar/baz?query=1#fragment"
argument_list|)
decl_stmt|;
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
name|newUri
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPath
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"http://foo/bar"
argument_list|)
decl_stmt|;
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
operator|new
name|URI
argument_list|(
literal|"http://foo/bar/baz"
argument_list|)
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
name|newUri
operator|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|path
argument_list|(
literal|"1"
argument_list|)
operator|.
name|path
argument_list|(
literal|"2"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
operator|new
name|URI
argument_list|(
literal|"http://foo/bar/baz/1/2"
argument_list|)
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPathSlashes
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"http://foo/"
argument_list|)
decl_stmt|;
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|path
argument_list|(
literal|"/bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz/"
argument_list|)
operator|.
name|path
argument_list|(
literal|"/blah/"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
operator|new
name|URI
argument_list|(
literal|"http://foo/bar/baz/blah/"
argument_list|)
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPathClass
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"http://foo/"
argument_list|)
decl_stmt|;
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|path
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
operator|new
name|URI
argument_list|(
literal|"http://foo/bookstore/bar"
argument_list|)
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathClassNull
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
operator|(
name|Class
operator|)
literal|null
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathClassNoAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPathClassMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"http://foo/"
argument_list|)
decl_stmt|;
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|path
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|"updateBook"
argument_list|)
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
operator|new
name|URI
argument_list|(
literal|"http://foo/books/bar"
argument_list|)
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathClassMethodNull1
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
literal|null
argument_list|,
literal|"methName"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathClassMethodNull2
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|null
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathClassMethodTooMany
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
name|UriBuilderWrongAnnotations
operator|.
name|class
argument_list|,
literal|"overloaded"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathClassMethodTooLess
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
name|BookStore
operator|.
name|class
argument_list|,
literal|"nonexistingMethod"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddPathMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|meth
init|=
name|BookStore
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"updateBook"
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
literal|"http://foo/"
argument_list|)
decl_stmt|;
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|uri
argument_list|(
name|uri
argument_list|)
operator|.
name|path
argument_list|(
name|meth
argument_list|)
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
operator|new
name|URI
argument_list|(
literal|"http://foo/books/bar"
argument_list|)
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathMethodNull
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
operator|(
name|Method
operator|)
literal|null
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testAddPathMethodNoAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|noAnnot
init|=
name|BookStore
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getBook"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|path
argument_list|(
name|noAnnot
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSchemeHostPortQueryFragment
parameter_list|()
throws|throws
name|Exception
block|{
name|URI
name|uri
decl_stmt|;
if|if
condition|(
literal|"IBM Corporation"
operator|.
name|equals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.vendor"
argument_list|)
argument_list|)
condition|)
block|{
name|uri
operator|=
operator|new
name|URI
argument_list|(
literal|"http://foo:1234/bar?n2=v2&n1=v1#fragment"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|uri
operator|=
operator|new
name|URI
argument_list|(
literal|"http://foo:1234/bar?n1=v1&n2=v2#fragment"
argument_list|)
expr_stmt|;
block|}
name|URI
name|newUri
init|=
operator|new
name|UriBuilderImpl
argument_list|()
operator|.
name|scheme
argument_list|(
literal|"http"
argument_list|)
operator|.
name|host
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|port
argument_list|(
literal|1234
argument_list|)
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|queryParam
argument_list|(
literal|"n1"
argument_list|,
literal|"v1"
argument_list|)
operator|.
name|queryParam
argument_list|(
literal|"n2"
argument_list|,
literal|"v2"
argument_list|)
operator|.
name|fragment
argument_list|(
literal|"fragment"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"URI is not built correctly"
argument_list|,
name|uri
argument_list|,
name|newUri
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

