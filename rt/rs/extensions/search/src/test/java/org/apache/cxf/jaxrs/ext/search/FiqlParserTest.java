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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
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
name|xml
operator|.
name|datatype
operator|.
name|DatatypeFactory
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
name|FiqlParserTest
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
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testCompareWrongComparator
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name>booba"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testCompareMissingName
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"==30"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testCompareMissingValue
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name=gt="
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompareValueTextSpaces
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name=gt=some text"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testCompareNameTextSpaces
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"some name=gt=text"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testDanglingOperator
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==a;(level==10;),"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultilevelExpression
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==a;(level==10,(name!=b;name!=c;(level=gt=10)))"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultilevelExpression2
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"((name==a;level==10),name!=b;name!=c);level=gt=10"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRedundantBrackets
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==a;((((level==10))))"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndOfOrs
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"(name==a,name==b);(level=gt=0,level=lt=10)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrOfAnds
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"(name==a;name==b),(level=gt=0;level=lt=10)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnmatchedBracket
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==a;(name!=b;(level==10,(name!=b))"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testUnmatchedBracket2
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==bbb;))()level==111"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testMissingComparison
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==bbb;,level==111"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testSetterMissing
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"noSuchSetter==xxx"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FiqlParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testSetterWrongType
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"exception==text"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetterNumericText
parameter_list|()
throws|throws
name|FiqlParseException
block|{
name|parser
operator|.
name|parse
argument_list|(
literal|"name==10"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseName
parameter_list|()
throws|throws
name|FiqlParseException
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
literal|"name==king"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"king"
argument_list|,
literal|10
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"king"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"diamond"
argument_list|,
literal|10
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"diamond"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseLevel
parameter_list|()
throws|throws
name|FiqlParseException
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
literal|"level=gt=10"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"whatever"
argument_list|,
literal|15
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|15
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"blah"
argument_list|,
literal|5
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"foobar"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseDateWithDefaultFormat
parameter_list|()
throws|throws
name|FiqlParseException
throws|,
name|ParseException
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
literal|"time=le=2010-03-11T18:00:00.000+00:00"
argument_list|)
decl_stmt|;
name|DateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|FiqlParser
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"whatever"
argument_list|,
literal|15
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-11T18:00:00.000+0000"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-10T22:22:00.000+0000"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"blah"
argument_list|,
literal|null
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-12T00:00:00.000+0000"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|123
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-12T00:00:00.000+0000"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseDateWithCustomFormat
parameter_list|()
throws|throws
name|FiqlParseException
throws|,
name|ParseException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
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
name|props
operator|.
name|put
argument_list|(
name|SearchUtils
operator|.
name|DATE_FORMAT_PROPERTY
argument_list|,
literal|"yyyy-MM-dd'T'HH:mm:ss"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SearchUtils
operator|.
name|TIMEZONE_SUPPORT_PROPERTY
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|parser
operator|=
operator|new
name|FiqlParser
argument_list|<
name|Condition
argument_list|>
argument_list|(
name|Condition
operator|.
name|class
argument_list|,
name|props
argument_list|)
expr_stmt|;
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
literal|"time=le=2010-03-11T18:00:00"
argument_list|)
decl_stmt|;
name|DateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd'T'HH:mm:ss"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"whatever"
argument_list|,
literal|15
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-11T18:00:00"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-10T22:22:00"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"blah"
argument_list|,
literal|null
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-12T00:00:00"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|123
argument_list|,
name|df
operator|.
name|parse
argument_list|(
literal|"2010-03-12T00:00:00"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseDateDuration
parameter_list|()
throws|throws
name|Exception
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
literal|"time=gt=-PT1M"
argument_list|)
decl_stmt|;
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|tenMinutesAgo
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|"-PT10M"
argument_list|)
operator|.
name|addTo
argument_list|(
name|tenMinutesAgo
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|now
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|tenMinutesAgo
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseComplex1
parameter_list|()
throws|throws
name|FiqlParseException
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
name|assertEquals
argument_list|(
name|ConditionType
operator|.
name|AND
argument_list|,
name|filter
operator|.
name|getConditionType
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
argument_list|>
name|conditions
init|=
name|filter
operator|.
name|getSearchConditions
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|conditions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|PrimitiveStatement
name|st1
init|=
name|conditions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStatement
argument_list|()
decl_stmt|;
name|PrimitiveStatement
name|st2
init|=
name|conditions
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getStatement
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
name|ConditionType
operator|.
name|EQUALS
operator|.
name|equals
argument_list|(
name|st1
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|&&
name|ConditionType
operator|.
name|GREATER_THAN
operator|.
name|equals
argument_list|(
name|st2
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|ConditionType
operator|.
name|EQUALS
operator|.
name|equals
argument_list|(
name|st2
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|&&
name|ConditionType
operator|.
name|GREATER_THAN
operator|.
name|equals
argument_list|(
name|st1
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"amichalec"
argument_list|,
literal|12
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"ami"
argument_list|,
literal|12
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"ami"
argument_list|,
literal|8
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"am"
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQL1
parameter_list|()
throws|throws
name|FiqlParseException
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
name|String
name|sql
init|=
name|SearchUtils
operator|.
name|toSQL
argument_list|(
name|filter
argument_list|,
literal|"table"
argument_list|)
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
name|testParseComplex2
parameter_list|()
throws|throws
name|FiqlParseException
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
name|assertEquals
argument_list|(
name|ConditionType
operator|.
name|OR
argument_list|,
name|filter
operator|.
name|getConditionType
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SearchCondition
argument_list|<
name|Condition
argument_list|>
argument_list|>
name|conditions
init|=
name|filter
operator|.
name|getSearchConditions
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|conditions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|PrimitiveStatement
name|st1
init|=
name|conditions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStatement
argument_list|()
decl_stmt|;
name|PrimitiveStatement
name|st2
init|=
name|conditions
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getStatement
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|(
name|ConditionType
operator|.
name|EQUALS
operator|.
name|equals
argument_list|(
name|st1
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|&&
name|ConditionType
operator|.
name|GREATER_THAN
operator|.
name|equals
argument_list|(
name|st2
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|)
operator|||
operator|(
name|ConditionType
operator|.
name|EQUALS
operator|.
name|equals
argument_list|(
name|st2
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|&&
name|ConditionType
operator|.
name|GREATER_THAN
operator|.
name|equals
argument_list|(
name|st1
operator|.
name|getCondition
argument_list|()
argument_list|)
operator|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"ami"
argument_list|,
literal|0
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"foo"
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"foo"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
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
name|FiqlParseException
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
name|String
name|sql
init|=
name|SearchUtils
operator|.
name|toSQL
argument_list|(
name|filter
argument_list|,
literal|"table"
argument_list|)
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
name|testParseComplex3
parameter_list|()
throws|throws
name|FiqlParseException
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
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"fooooo"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"fooooobar"
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"fooobar"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"bar"
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|)
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
name|FiqlParseException
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
name|String
name|sql
init|=
name|SearchUtils
operator|.
name|toSQL
argument_list|(
name|filter
argument_list|,
literal|"table"
argument_list|)
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
name|FiqlParseException
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
name|String
name|sql
init|=
name|SearchUtils
operator|.
name|toSQL
argument_list|(
name|filter
argument_list|,
literal|"table"
argument_list|)
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
name|FiqlParseException
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
name|String
name|sql
init|=
name|SearchUtils
operator|.
name|toSQL
argument_list|(
name|filter
argument_list|,
literal|"table"
argument_list|)
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
name|testParseComplex4
parameter_list|()
throws|throws
name|FiqlParseException
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
literal|"name==foo*;name!=*bar,level=gt=10"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"zonk"
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"foobaz"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"foobar"
argument_list|,
literal|20
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Condition
argument_list|(
literal|"fooxxxbar"
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
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
name|int
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
name|int
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

