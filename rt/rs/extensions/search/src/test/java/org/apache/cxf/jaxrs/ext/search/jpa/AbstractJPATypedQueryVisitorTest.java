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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DriverManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Date
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
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Persistence
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
name|javax
operator|.
name|persistence
operator|.
name|TypedQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|criteria
operator|.
name|CriteriaQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|metamodel
operator|.
name|SingularAttribute
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
name|SearchCondition
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
name|SearchConditionVisitor
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
name|SearchUtils
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
name|jpa
operator|.
name|BookReview
operator|.
name|Review
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJPATypedQueryVisitorTest
extends|extends
name|Assert
block|{
specifier|private
name|EntityManagerFactory
name|emFactory
decl_stmt|;
specifier|private
name|EntityManager
name|em
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|protected
specifier|abstract
name|SearchConditionParser
argument_list|<
name|Book
argument_list|>
name|getParser
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
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
function_decl|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"org.hsqldb.jdbcDriver"
argument_list|)
expr_stmt|;
name|connection
operator|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
literal|"jdbc:hsqldb:mem:books-jpa"
argument_list|,
literal|"sa"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Exception during HSQL database init."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|emFactory
operator|=
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
literal|"testUnitHibernate"
argument_list|)
expr_stmt|;
name|em
operator|=
name|emFactory
operator|.
name|createEntityManager
argument_list|()
expr_stmt|;
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
name|Library
name|lib
init|=
operator|new
name|Library
argument_list|()
decl_stmt|;
name|lib
operator|.
name|setId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|lib
operator|.
name|setAddress
argument_list|(
literal|"town"
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|lib
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|em
operator|.
name|contains
argument_list|(
name|lib
argument_list|)
argument_list|)
expr_stmt|;
name|BookReview
name|br1
init|=
operator|new
name|BookReview
argument_list|()
decl_stmt|;
name|br1
operator|.
name|setId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|br1
operator|.
name|setReview
argument_list|(
name|Review
operator|.
name|BAD
argument_list|)
expr_stmt|;
name|br1
operator|.
name|getAuthors
argument_list|()
operator|.
name|add
argument_list|(
literal|"Ted"
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|br1
argument_list|)
expr_stmt|;
name|Book
name|b1
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|br1
operator|.
name|setBook
argument_list|(
name|b1
argument_list|)
expr_stmt|;
name|b1
operator|.
name|getReviews
argument_list|()
operator|.
name|add
argument_list|(
name|br1
argument_list|)
expr_stmt|;
name|b1
operator|.
name|setId
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|b1
operator|.
name|setBookTitle
argument_list|(
literal|"num9"
argument_list|)
expr_stmt|;
name|b1
operator|.
name|setAddress
argument_list|(
operator|new
name|OwnerAddress
argument_list|(
literal|"Street1"
argument_list|)
argument_list|)
expr_stmt|;
name|OwnerInfo
name|info1
init|=
operator|new
name|OwnerInfo
argument_list|()
decl_stmt|;
name|info1
operator|.
name|setName
argument_list|(
operator|new
name|Name
argument_list|(
literal|"Fred"
argument_list|)
argument_list|)
expr_stmt|;
name|info1
operator|.
name|setDateOfBirth
argument_list|(
name|parseDate
argument_list|(
literal|"2000-01-01"
argument_list|)
argument_list|)
expr_stmt|;
name|b1
operator|.
name|setOwnerInfo
argument_list|(
name|info1
argument_list|)
expr_stmt|;
name|b1
operator|.
name|setLibrary
argument_list|(
name|lib
argument_list|)
expr_stmt|;
name|b1
operator|.
name|getAuthors
argument_list|()
operator|.
name|add
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|b1
argument_list|)
expr_stmt|;
name|BookReview
name|br2
init|=
operator|new
name|BookReview
argument_list|()
decl_stmt|;
name|br2
operator|.
name|setId
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|br2
operator|.
name|setReview
argument_list|(
name|Review
operator|.
name|GOOD
argument_list|)
expr_stmt|;
name|br2
operator|.
name|getAuthors
argument_list|()
operator|.
name|add
argument_list|(
literal|"Ted"
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|br2
argument_list|)
expr_stmt|;
name|Book
name|b2
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b2
operator|.
name|getReviews
argument_list|()
operator|.
name|add
argument_list|(
name|br2
argument_list|)
expr_stmt|;
name|br2
operator|.
name|setBook
argument_list|(
name|b2
argument_list|)
expr_stmt|;
name|b2
operator|.
name|setId
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|b2
operator|.
name|setBookTitle
argument_list|(
literal|"num10"
argument_list|)
expr_stmt|;
name|b2
operator|.
name|setAddress
argument_list|(
operator|new
name|OwnerAddress
argument_list|(
literal|"Street2"
argument_list|)
argument_list|)
expr_stmt|;
name|OwnerInfo
name|info2
init|=
operator|new
name|OwnerInfo
argument_list|()
decl_stmt|;
name|info2
operator|.
name|setName
argument_list|(
operator|new
name|Name
argument_list|(
literal|"Barry"
argument_list|)
argument_list|)
expr_stmt|;
name|info2
operator|.
name|setDateOfBirth
argument_list|(
name|parseDate
argument_list|(
literal|"2001-01-01"
argument_list|)
argument_list|)
expr_stmt|;
name|b2
operator|.
name|setOwnerInfo
argument_list|(
name|info2
argument_list|)
expr_stmt|;
name|b2
operator|.
name|setLibrary
argument_list|(
name|lib
argument_list|)
expr_stmt|;
name|b2
operator|.
name|getAuthors
argument_list|()
operator|.
name|add
argument_list|(
literal|"John"
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|b2
argument_list|)
expr_stmt|;
name|BookReview
name|br3
init|=
operator|new
name|BookReview
argument_list|()
decl_stmt|;
name|br3
operator|.
name|setId
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|br3
operator|.
name|setReview
argument_list|(
name|Review
operator|.
name|GOOD
argument_list|)
expr_stmt|;
name|br3
operator|.
name|getAuthors
argument_list|()
operator|.
name|add
argument_list|(
literal|"Ted"
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|br3
argument_list|)
expr_stmt|;
name|Book
name|b3
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|b3
operator|.
name|getReviews
argument_list|()
operator|.
name|add
argument_list|(
name|br3
argument_list|)
expr_stmt|;
name|br3
operator|.
name|setBook
argument_list|(
name|b3
argument_list|)
expr_stmt|;
name|b3
operator|.
name|setId
argument_list|(
literal|11
argument_list|)
expr_stmt|;
name|b3
operator|.
name|setBookTitle
argument_list|(
literal|"num11"
argument_list|)
expr_stmt|;
name|b3
operator|.
name|setAddress
argument_list|(
operator|new
name|OwnerAddress
argument_list|(
literal|"Street&'3"
argument_list|)
argument_list|)
expr_stmt|;
name|b3
operator|.
name|getAuthors
argument_list|()
operator|.
name|add
argument_list|(
literal|"Barry"
argument_list|)
expr_stmt|;
name|OwnerInfo
name|info3
init|=
operator|new
name|OwnerInfo
argument_list|()
decl_stmt|;
name|info3
operator|.
name|setName
argument_list|(
operator|new
name|Name
argument_list|(
literal|"Bill"
argument_list|)
argument_list|)
expr_stmt|;
name|info3
operator|.
name|setDateOfBirth
argument_list|(
name|parseDate
argument_list|(
literal|"2002-01-01"
argument_list|)
argument_list|)
expr_stmt|;
name|b3
operator|.
name|setOwnerInfo
argument_list|(
name|info3
argument_list|)
expr_stmt|;
name|b3
operator|.
name|setLibrary
argument_list|(
name|lib
argument_list|)
expr_stmt|;
name|em
operator|.
name|persist
argument_list|(
name|b3
argument_list|)
expr_stmt|;
name|lib
operator|.
name|getBooks
argument_list|()
operator|.
name|add
argument_list|(
name|b1
argument_list|)
expr_stmt|;
name|lib
operator|.
name|getBooks
argument_list|()
operator|.
name|add
argument_list|(
name|b2
argument_list|)
expr_stmt|;
name|lib
operator|.
name|getBooks
argument_list|()
operator|.
name|add
argument_list|(
name|b3
argument_list|)
expr_stmt|;
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Exception during JPA EntityManager creation."
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
if|if
condition|(
name|em
operator|!=
literal|null
condition|)
block|{
name|em
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|emFactory
operator|!=
literal|null
condition|)
block|{
name|emFactory
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|connection
operator|.
name|createStatement
argument_list|()
operator|.
name|execute
argument_list|(
literal|"SHUTDOWN"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|List
argument_list|<
name|Book
argument_list|>
name|queryBooks
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|queryBooks
argument_list|(
name|expression
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Book
argument_list|>
name|queryBooks
parameter_list|(
name|String
name|expression
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|visitorProps
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|queryBooks
argument_list|(
name|expression
argument_list|,
name|visitorProps
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Book
argument_list|>
name|queryBooks
parameter_list|(
name|String
name|expression
parameter_list|,
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
throws|throws
name|Exception
block|{
return|return
name|queryBooks
argument_list|(
name|expression
argument_list|,
name|visitorProps
argument_list|,
name|parserBinProps
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Book
argument_list|>
name|queryBooks
parameter_list|(
name|String
name|expression
parameter_list|,
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
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|joinProps
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|filter
init|=
name|getParser
argument_list|(
name|visitorProps
argument_list|,
name|parserBinProps
argument_list|)
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|SearchConditionVisitor
argument_list|<
name|Book
argument_list|,
name|TypedQuery
argument_list|<
name|Book
argument_list|>
argument_list|>
name|jpa
init|=
operator|new
name|JPATypedQueryVisitor
argument_list|<
name|Book
argument_list|>
argument_list|(
name|em
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|visitorProps
argument_list|,
name|joinProps
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
name|TypedQuery
argument_list|<
name|Book
argument_list|>
name|query
init|=
name|jpa
operator|.
name|getQuery
argument_list|()
decl_stmt|;
return|return
name|query
operator|.
name|getResultList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Tuple
argument_list|>
name|criteriaQueryBooksTuple
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Tuple
argument_list|>
name|jpa
init|=
operator|new
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Tuple
argument_list|>
argument_list|(
name|em
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Tuple
operator|.
name|class
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
name|selections
init|=
operator|new
name|ArrayList
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|Book_
operator|.
name|id
argument_list|)
expr_stmt|;
name|jpa
operator|.
name|selectTuple
argument_list|(
name|selections
argument_list|)
expr_stmt|;
name|CriteriaQuery
argument_list|<
name|Tuple
argument_list|>
name|cquery
init|=
name|jpa
operator|.
name|getQuery
argument_list|()
decl_stmt|;
return|return
name|em
operator|.
name|createQuery
argument_list|(
name|cquery
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
specifier|protected
name|long
name|criteriaQueryBooksCount
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Long
argument_list|>
name|jpa
init|=
operator|new
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Long
argument_list|>
argument_list|(
name|em
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
return|return
name|jpa
operator|.
name|count
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Book
argument_list|>
name|criteriaQueryBooksOrderBy
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|asc
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Book
argument_list|>
name|jpa
init|=
operator|new
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Book
argument_list|>
argument_list|(
name|em
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
name|selections
init|=
operator|new
name|ArrayList
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|Book_
operator|.
name|id
argument_list|)
expr_stmt|;
return|return
name|jpa
operator|.
name|getOrderedTypedQuery
argument_list|(
name|selections
argument_list|,
name|asc
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|BookInfo
argument_list|>
name|criteriaQueryBooksConstruct
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|BookInfo
argument_list|>
name|jpa
init|=
operator|new
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|BookInfo
argument_list|>
argument_list|(
name|em
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|BookInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
name|selections
init|=
operator|new
name|ArrayList
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|Book_
operator|.
name|id
argument_list|)
expr_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|Book_
operator|.
name|bookTitle
argument_list|)
expr_stmt|;
name|jpa
operator|.
name|selectConstruct
argument_list|(
name|selections
argument_list|)
expr_stmt|;
name|CriteriaQuery
argument_list|<
name|BookInfo
argument_list|>
name|cquery
init|=
name|jpa
operator|.
name|getQuery
argument_list|()
decl_stmt|;
return|return
name|em
operator|.
name|createQuery
argument_list|(
name|cquery
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
specifier|protected
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|criteriaQueryBooksArray
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|SearchCondition
argument_list|<
name|Book
argument_list|>
name|filter
init|=
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Object
index|[]
argument_list|>
name|jpa
init|=
operator|new
name|JPACriteriaQueryVisitor
argument_list|<
name|Book
argument_list|,
name|Object
index|[]
argument_list|>
argument_list|(
name|em
argument_list|,
name|Book
operator|.
name|class
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|jpa
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
name|selections
init|=
operator|new
name|ArrayList
argument_list|<
name|SingularAttribute
argument_list|<
name|Book
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|Book_
operator|.
name|id
argument_list|)
expr_stmt|;
name|selections
operator|.
name|add
argument_list|(
name|Book_
operator|.
name|bookTitle
argument_list|)
expr_stmt|;
return|return
name|jpa
operator|.
name|getArrayTypedQuery
argument_list|(
name|selections
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
specifier|protected
name|Date
name|parseDate
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|SimpleDateFormat
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|)
operator|.
name|parse
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|BookInfo
block|{
specifier|private
name|int
name|id
decl_stmt|;
specifier|private
name|String
name|title
decl_stmt|;
specifier|public
name|BookInfo
parameter_list|()
block|{                      }
specifier|public
name|BookInfo
parameter_list|(
name|Integer
name|id
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

