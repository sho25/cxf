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
name|ext
operator|.
name|search
operator|.
name|jpa
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Date
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
name|persistence
operator|.
name|Tuple
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
name|search
operator|.
name|SearchConditionParser
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
name|search
operator|.
name|odata
operator|.
name|ODataParser
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
name|JPATypedQueryVisitorODataTest
extends|extends
name|AbstractJPATypedQueryVisitorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testOrQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id lt 10 or id gt 10"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|9
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
operator|||
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|9
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
name|testOrQueryNoMatch
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id eq 7 or id eq 5"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|books
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
name|testAndQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id eq 10 and bookTitle eq 'num10'"
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|"num10"
operator|.
name|equals
argument_list|(
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBookTitle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fixing, in progress"
argument_list|)
specifier|public
name|void
name|testQueryCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"reviews.authors eq 'Ted'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fixing, in progress"
argument_list|)
specifier|public
name|void
name|testQueryCollection2
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"reviews.book.id eq 10"
argument_list|)
decl_stmt|;
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
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fixing, in progress"
argument_list|)
specifier|public
name|void
name|testQueryCollection3
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"reviews.book.ownerInfo.name eq 'Barry'"
argument_list|)
decl_stmt|;
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
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fixing, in progress"
argument_list|)
specifier|public
name|void
name|testQueryElementCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"authors eq 'John'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|books
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
name|testNumberOfReviews
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"reviews gt 0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|books
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
name|testNumberOfReviews2
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"reviews gt 3"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fixing, in progress"
argument_list|)
specifier|public
name|void
name|testQueryCollectionSize2
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"reviews.authors gt 0"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Fixing, in progress"
argument_list|)
specifier|public
name|void
name|testAndQueryCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id eq 10 and authors eq 'John' and reviews.review eq 'good' and reviews.authors eq 'Ted'"
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|"num10"
operator|.
name|equals
argument_list|(
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getBookTitle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndQueryNoMatch
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id eq 10 and bookTitle eq 'num9'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|books
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
name|testEqualsQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id eq 10"
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|testEqualsCriteriaQueryTuple
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Tuple
argument_list|>
name|books
init|=
name|criteriaQueryBooksTuple
argument_list|(
literal|"id eq 10"
argument_list|)
decl_stmt|;
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
name|Tuple
name|tuple
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|tupleId
init|=
name|tuple
operator|.
name|get
argument_list|(
literal|"id"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|tupleId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsCriteriaQueryCount
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|criteriaQueryBooksCount
argument_list|(
literal|"id eq 10"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsCriteriaQueryConstruct
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|BookInfo
argument_list|>
name|books
init|=
name|criteriaQueryBooksConstruct
argument_list|(
literal|"id eq 10"
argument_list|)
decl_stmt|;
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
name|BookInfo
name|info
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|info
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"num10"
argument_list|,
name|info
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrderByAsc
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|criteriaQueryBooksOrderBy
argument_list|(
literal|"reviews gt 0"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|2
argument_list|)
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
name|testOrderByDesc
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|criteriaQueryBooksOrderBy
argument_list|(
literal|"reviews gt 0"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|books
operator|.
name|get
argument_list|(
literal|2
argument_list|)
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
name|testEqualsCriteriaQueryArray
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|books
init|=
name|criteriaQueryBooksArray
argument_list|(
literal|"id eq 10"
argument_list|)
decl_stmt|;
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
name|Object
index|[]
name|info
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
operator|(
operator|(
name|Integer
operator|)
name|info
index|[
literal|0
index|]
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"num10"
argument_list|,
operator|(
name|String
operator|)
name|info
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsAddressQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"address eq 'Street1'"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"address"
argument_list|,
literal|"address.street"
argument_list|)
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|9
operator|==
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Street1"
argument_list|,
name|book
operator|.
name|getAddress
argument_list|()
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsAddressQuery2
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"street eq 'Street1'"
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"street"
argument_list|,
literal|"address.street"
argument_list|)
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|9
operator|==
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Street1"
argument_list|,
name|book
operator|.
name|getAddress
argument_list|()
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsAddressQuery3
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|beanPropertiesMap
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
name|beanPropertiesMap
operator|.
name|put
argument_list|(
literal|"street"
argument_list|,
literal|"address.street"
argument_list|)
expr_stmt|;
name|beanPropertiesMap
operator|.
name|put
argument_list|(
literal|"housenum"
argument_list|,
literal|"address.houseNumber"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"street eq 'Street2' and housenum lt 5"
argument_list|,
literal|null
argument_list|,
name|beanPropertiesMap
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|10
operator|==
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Street2"
argument_list|,
name|book
operator|.
name|getAddress
argument_list|()
operator|.
name|getStreet
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsOwnerNameQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"ownerInfo.name.name eq 'Fred'"
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|// "ownerInfo.name" maps to Name class and this
comment|// does not work in OpenJPA, as opposed to Hibernate
comment|// "ownerInfo.name.name" will map to primitive type, see
comment|// testEqualsOwnerNameQuery3(), which also works in OpenJPA
specifier|public
name|void
name|testEqualsOwnerNameQuery2
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"ownerInfo.name eq 'Fred'"
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getName
argument_list|()
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
name|testEqualsOwnerNameQuery3
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"ownerName eq 'Fred'"
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ownerName"
argument_list|,
literal|"ownerInfo.name.name"
argument_list|)
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getName
argument_list|()
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
name|testFindBookInTownLibrary
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"libAddress eq 'town' and bookTitle eq 'num10'"
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"libAddress"
argument_list|,
literal|"library.address"
argument_list|)
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Barry"
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getName
argument_list|()
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
name|testEqualsOwnerBirthDate
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"ownerbdate eq '2000-01-01'"
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ownerbdate"
argument_list|,
literal|"ownerInfo.dateOfBirth"
argument_list|)
argument_list|)
decl_stmt|;
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
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Date
name|d
init|=
name|parseDate
argument_list|(
literal|"2000-01-01"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|d
argument_list|,
name|book
operator|.
name|getOwnerInfo
argument_list|()
operator|.
name|getDateOfBirth
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualsWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"bookTitle eq 'num1*'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
operator|||
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
name|testGreaterQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id gt 10"
argument_list|)
decl_stmt|;
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
name|assertTrue
argument_list|(
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|testGreaterEqualQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id ge 10"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
operator|||
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
name|testLessEqualQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id le 10"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|9
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
operator|||
literal|9
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|10
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
name|testNotEqualsQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
name|queryBooks
argument_list|(
literal|"id ne 10"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|books
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|9
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
operator|||
literal|11
operator|==
name|books
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
operator|&&
literal|9
operator|==
name|books
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|SearchConditionParser
argument_list|<
name|Book
argument_list|>
name|getParser
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|visitorProps
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parserBinProps
parameter_list|)
block|{
return|return
operator|new
name|ODataParser
argument_list|<
name|Book
argument_list|>
argument_list|(
name|Book
operator|.
name|class
argument_list|,
name|visitorProps
argument_list|,
name|parserBinProps
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|SearchConditionParser
argument_list|<
name|Book
argument_list|>
name|getParser
parameter_list|()
block|{
return|return
operator|new
name|ODataParser
argument_list|<
name|Book
argument_list|>
argument_list|(
name|Book
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

