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
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|List
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|impl
operator|.
name|MetadataMap
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
name|Before
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
name|URITemplateTest
extends|extends
name|Assert
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{      }
annotation|@
name|Test
specifier|public
name|void
name|testMatchBasic
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers/{id}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers/123/"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchWithMatrixAndTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers/{id}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers/123;123456/"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123;123456"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchWithMatrixOnClearPath1
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers/{id}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers;123456/123/"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchWithMatrixOnClearPath2
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers/{id}/orders/{order}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers;123456/123/orders;456/3"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"3"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"order"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchWithMatrixOnClearPath3
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/{id}/customers/"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/123/customers;123456/"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchWithMatrixOnClearPath4
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers;123456/123/orders;456/3"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchBasicTwoParametersVariation1
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers/{name}/{department}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers/john/CS"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|department
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"department"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"john"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CS"
argument_list|,
name|department
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMatchBasicTwoParametersVariation2
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers/name/{name}/dep/{department}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers/name/john/dep/CS"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|department
init|=
name|values
operator|.
name|getFirst
argument_list|(
literal|"department"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"john"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CS"
argument_list|,
name|department
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testURITemplateWithSubResource
parameter_list|()
throws|throws
name|Exception
block|{
comment|// So "/customers" is the URITemplate for the root resource class
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers/123"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/123"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testURITemplateWithSubResourceVariation2
parameter_list|()
throws|throws
name|Exception
block|{
comment|// So "/customers" is the URITemplate for the root resource class
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/customers"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/customers/name/john/dep/CS"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/name/john/dep/CS"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|/*      * Test a sub-resource locator method like this @HttpMethod("GET") @UriTemplate("/books/{bookId}/") public      * Book getBook(@UriParam("bookId") String id)      */
specifier|public
name|void
name|testURITemplateWithSubResourceVariation3
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId}/"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123/chapter/1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/chapter/1"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicCustomExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId:[^/]+?}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123/chapter/1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"bookId"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/chapter/1"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicCustomExpression2
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId:123}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123/chapter/1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"bookId"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/chapter/1"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicCustomExpression3
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId:\\d\\d\\d}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123/chapter/1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"bookId"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/chapter/1"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEscaping
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/a.db"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/a.db"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/adbc"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/acdb"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBasicCustomExpression4
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId:...\\.}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123."
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123."
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"bookId"
argument_list|)
argument_list|)
expr_stmt|;
name|values
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/abc."
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc."
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"bookId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/abcd"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/abc"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleExpression2
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId:123}/chapter/{id}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123/chapter/1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|match
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"bookId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|subResourcePath
init|=
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|subResourcePath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailCustomExpression
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/books/{bookId:124}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|match
init|=
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/books/123/chapter/1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|match
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBaseTail1
parameter_list|()
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/{base:base.+}/{tail}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base1/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"base1"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tails"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"tail"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBaseTail2
parameter_list|()
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/{base:.+base}/{tail}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base1/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/1base/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1base"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tails"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"tail"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBaseTail3
parameter_list|()
block|{
name|URITemplate
name|uriTemplate
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/{base:base.+suffix}/{tail}"
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base1/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uriTemplate
operator|.
name|match
argument_list|(
literal|"/base1suffix/tails"
argument_list|,
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"base1suffix"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"base"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tails"
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
literal|"tail"
argument_list|)
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
name|testSubstituteListNull
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|URITemplate
argument_list|(
literal|"anything"
argument_list|)
operator|.
name|substitute
argument_list|(
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteList
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{b:\\d\\d}/{c}"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"99"
argument_list|,
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/foo/99/baz"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|list
argument_list|)
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
name|testSubstituteListWrongPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{b:\\d\\d}"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"not-two-digits"
argument_list|)
decl_stmt|;
name|ut
operator|.
name|substitute
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteListSameVars
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{a}/{a}"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|,
literal|"blah"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/bar/baz/blah"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteListIncomplete
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{c}/{b}"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/bar/baz/{b}"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteListExceeding
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{b}"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"bar"
argument_list|,
literal|"baz"
argument_list|,
literal|"blah"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/bar/baz"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|list
argument_list|)
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
name|testSubstituteMapNull
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|URITemplate
argument_list|(
literal|"anything"
argument_list|)
operator|.
name|substitute
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteMap
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{b:\\d\\d}/{c}"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"c"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"11"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/bar/11/foo"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteMapSameVars
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{a}/{a}"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/bar/bar/bar"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteMapIncomplete
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{b}/{a:\\d}"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/{a}/bar/{a:\\d}"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteMapSameVarWithPattern
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{a:\\d}"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/0/0"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|map
argument_list|)
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
name|testSubstituteMapSameVarWithPatternFail
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}/{a:\\d}"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"not-a-digit"
argument_list|)
expr_stmt|;
name|ut
operator|.
name|substitute
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSubstituteMapExceeding
parameter_list|()
throws|throws
name|Exception
block|{
name|URITemplate
name|ut
init|=
operator|new
name|URITemplate
argument_list|(
literal|"/foo/{a}"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
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
name|map
operator|.
name|put
argument_list|(
literal|"b"
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong substitution"
argument_list|,
literal|"/foo/blah"
argument_list|,
name|ut
operator|.
name|substitute
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

