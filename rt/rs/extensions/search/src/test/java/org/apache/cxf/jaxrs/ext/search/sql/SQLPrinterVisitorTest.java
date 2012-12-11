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
name|sql
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
name|SearchBean
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
name|SearchParseException
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
name|fiql
operator|.
name|FiqlParser
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
name|visitor
operator|.
name|SBThreadLocalVisitorState
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
name|SQLPrinterVisitorTest
extends|extends
name|Assert
block|{
specifier|private
name|FiqlParser
argument_list|<
name|Condition
argument_list|>
name|parser
init|=
operator|new
name|FiqlParser
argument_list|<
name|Condition
argument_list|>
argument_list|(
name|Condition
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSQL1
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==ami*;level=gt=10"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
operator|.
name|visitor
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"SELECT * FROM table WHERE (name LIKE 'ami%') AND (level> '10')"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
literal|"SELECT * FROM table WHERE (level> '10') AND (name LIKE 'ami%')"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL1WithSearchBean
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
name|beanParser
init|=
operator|new
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|beanParser
operator|.
name|parse
argument_list|(
literal|"name==ami*;level=gt=10"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"SELECT * FROM table WHERE (name LIKE 'ami%') AND (level> '10')"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
literal|"SELECT * FROM table WHERE (level> '10') AND (name LIKE 'ami%')"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL2
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==ami*,level=gt=10"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"SELECT * FROM table WHERE (name LIKE 'ami%') OR (level> '10')"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
literal|"SELECT * FROM table WHERE (level> '10') OR (name LIKE 'ami%')"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL3
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==foo*;(name!=*bar,level=gt=10)"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
literal|"SELECT * FROM table WHERE (name LIKE 'foo%') AND ((name NOT LIKE '%bar') "
operator|+
literal|"OR (level> '10'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
operator|(
literal|"SELECT * FROM table WHERE (name LIKE 'foo%') AND "
operator|+
literal|"((level> '10') OR (name NOT LIKE '%bar'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL3WithSearchBean
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
name|beanParser
init|=
operator|new
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|beanParser
operator|.
name|parse
argument_list|(
literal|"name==foo*;(name!=*bar,level=gt=10)"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
literal|"SELECT * FROM table WHERE (name LIKE 'foo%') AND ((name NOT LIKE '%bar') "
operator|+
literal|"OR (level> '10'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
operator|(
literal|"SELECT * FROM table WHERE (name LIKE 'foo%') AND "
operator|+
literal|"((level> '10') OR (name NOT LIKE '%bar'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL4
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"(name==test,level==18);(name==test1,level!=19)"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
literal|"SELECT * FROM table WHERE ((name = 'test') OR (level = '18'))"
operator|+
literal|" AND ((name = 'test1') OR (level<> '19'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
operator|(
literal|"SELECT * FROM table WHERE ((name = 'test1') OR (level<> '19'))"
operator|+
literal|" AND ((name = 'test') OR (level = '18'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL4WithTLStateAndSingleThread
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"(name==test,level==18);(name==test1,level!=19)"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|visitor
operator|.
name|setVisitorState
argument_list|(
operator|new
name|SBThreadLocalVisitorState
argument_list|()
argument_list|)
expr_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
literal|"SELECT * FROM table WHERE ((name = 'test') OR (level = '18'))"
operator|+
literal|" AND ((name = 'test1') OR (level<> '19'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
operator|||
operator|(
literal|"SELECT * FROM table WHERE ((name = 'test1') OR (level<> '19'))"
operator|+
literal|" AND ((name = 'test') OR (level = '18'))"
operator|)
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL5
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==test"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"SELECT * FROM table WHERE name = 'test'"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL5WithColumns
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==test"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
literal|"table"
argument_list|,
literal|"NAMES"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"SELECT NAMES FROM table WHERE name = 'test'"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL5WithFieldMap
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==test"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|Condition
argument_list|>
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"name"
argument_list|,
literal|"NAMES"
argument_list|)
argument_list|,
literal|"table"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"NAMES"
argument_list|)
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"SELECT NAMES FROM table WHERE NAMES = 'test'"
operator|.
name|equals
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|Condition
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Integer
name|level
decl_stmt|;
specifier|private
name|Date
name|time
decl_stmt|;
specifier|public
name|Condition
parameter_list|()
block|{         }
specifier|public
name|Condition
parameter_list|(
name|String
name|name
parameter_list|,
name|Integer
name|level
parameter_list|,
name|Date
name|time
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
name|this
operator|.
name|time
operator|=
name|time
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Integer
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
specifier|public
name|void
name|setLevel
parameter_list|(
name|Integer
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
specifier|public
name|Date
name|getTime
parameter_list|()
block|{
return|return
name|time
return|;
block|}
specifier|public
name|void
name|setTime
parameter_list|(
name|Date
name|time
parameter_list|)
block|{
name|this
operator|.
name|time
operator|=
name|time
expr_stmt|;
block|}
specifier|public
name|void
name|setException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
block|}
end_class

end_unit

