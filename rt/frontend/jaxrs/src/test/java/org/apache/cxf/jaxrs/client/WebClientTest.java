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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|ext
operator|.
name|ParamConverter
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
name|ext
operator|.
name|ParamConverterProvider
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
name|BookInterface
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
name|WebClientTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReplaceHeader
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|header
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|wc
operator|.
name|replaceHeader
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveHeader
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|header
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|wc
operator|.
name|replaceHeader
argument_list|(
literal|"a"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
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
name|testEncoding
parameter_list|()
block|{
name|URI
name|u
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|path
argument_list|(
literal|"bar+ %2B"
argument_list|)
operator|.
name|matrix
argument_list|(
literal|"a"
argument_list|,
literal|"value+ "
argument_list|)
operator|.
name|query
argument_list|(
literal|"b"
argument_list|,
literal|"bv+ %2B"
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo/bar+%20%2B;a=value+%20?b=bv%2B+%2B"
argument_list|,
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExistingAsteriscs
parameter_list|()
block|{
name|URI
name|u
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo/*"
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo/*"
argument_list|,
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAsteriscs
parameter_list|()
block|{
name|URI
name|u
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|path
argument_list|(
literal|"*"
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo/*"
argument_list|,
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoubleAsteriscs
parameter_list|()
block|{
name|URI
name|u
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|path
argument_list|(
literal|"**"
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo/**"
argument_list|,
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBaseCurrentPath
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNewBaseCurrentPath
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|to
argument_list|(
literal|"http://bar"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForward
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|to
argument_list|(
literal|"http://foo/bar"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompositePath
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"/bar/baz/"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz/"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
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
name|testWrongForward
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|to
argument_list|(
literal|"http://bar"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBaseCurrentPathAfterChange
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|matrix
argument_list|(
literal|"m1"
argument_list|,
literal|"m1value"
argument_list|)
operator|.
name|query
argument_list|(
literal|"q1"
argument_list|,
literal|"q1value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz;m1=m1value?q1=q1value"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBaseCurrentPathAfterCopy
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|matrix
argument_list|(
literal|"m1"
argument_list|,
literal|"m1value"
argument_list|)
operator|.
name|query
argument_list|(
literal|"q1"
argument_list|,
literal|"q1value"
argument_list|)
expr_stmt|;
name|WebClient
name|wc1
init|=
name|WebClient
operator|.
name|fromClient
argument_list|(
name|wc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz;m1=m1value?q1=q1value"
argument_list|)
argument_list|,
name|wc1
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz;m1=m1value?q1=q1value"
argument_list|)
argument_list|,
name|wc1
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHeaders
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|header
argument_list|(
literal|"h1"
argument_list|,
literal|"h1value"
argument_list|)
operator|.
name|header
argument_list|(
literal|"h2"
argument_list|,
literal|"h2value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"h1"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"h1value"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"h1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"h2"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"h2value"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"h2"
argument_list|)
argument_list|)
expr_stmt|;
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"h1"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"h1value"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"h1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"h2"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"h2value"
argument_list|,
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
literal|"h2"
argument_list|)
argument_list|)
expr_stmt|;
name|wc
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|wc
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBackFast
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|matrix
argument_list|(
literal|"m1"
argument_list|,
literal|"m1value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz;m1=m1value"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|back
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBack
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|back
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|back
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|back
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResetQueryAndBack
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz?foo=bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|resetQuery
argument_list|()
operator|.
name|back
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReplaceQuery
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz?foo=bar"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|replaceQuery
argument_list|(
literal|"foo1=bar1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz?foo1=bar1"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReplaceQueryParam
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|query
argument_list|(
literal|"foo1"
argument_list|,
literal|"bar1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz?foo=bar&foo1=bar1"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|replaceQueryParam
argument_list|(
literal|"foo1"
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz?foo=bar&foo1=baz"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReplacePathAll
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|replacePath
argument_list|(
literal|"/new"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/new"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReplacePathLastSegment
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|replacePath
argument_list|(
literal|"new"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/new"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFragment
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|path
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
operator|.
name|fragment
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/bar/baz?foo=bar#1"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPathWithTemplates
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|path
argument_list|(
literal|"{bar}/{foo}"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|,
name|wc
operator|.
name|getBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo/1/2"
argument_list|)
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebClientConfiguration
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebClientPathParamConverter
parameter_list|()
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|ParamConverterProviderImpl
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|wc
operator|.
name|path
argument_list|(
operator|new
name|ComplexObject
argument_list|()
argument_list|)
expr_stmt|;
name|wc
operator|.
name|query
argument_list|(
literal|"param"
argument_list|,
operator|new
name|ComplexObject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo/complex?param=complex"
argument_list|,
name|wc
operator|.
name|getCurrentURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProxyConfiguration
parameter_list|()
block|{
comment|// interface
name|BookInterface
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|,
name|BookInterface
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|WebClient
operator|.
name|getConfig
argument_list|(
name|proxy
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
comment|// cglib
name|BookStore
name|proxy2
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"http://foo"
argument_list|,
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|WebClient
operator|.
name|getConfig
argument_list|(
name|proxy2
argument_list|)
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|ParamConverterProviderImpl
implements|implements
name|ParamConverterProvider
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ParamConverter
argument_list|<
name|T
argument_list|>
name|getConverter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|rawType
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|)
block|{
return|return
operator|(
name|ParamConverter
argument_list|<
name|T
argument_list|>
operator|)
operator|new
name|ParamConverterImpl
argument_list|()
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ParamConverterImpl
implements|implements
name|ParamConverter
argument_list|<
name|ComplexObject
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|ComplexObject
name|fromString
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|ComplexObject
name|value
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
literal|"complex"
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ComplexObject
block|{              }
block|}
end_class

end_unit

