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
name|client
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|DatatypeConfigurationException
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
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|Duration
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
name|ConditionType
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
name|junit
operator|.
name|AfterClass
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
name|FiqlSearchConditionBuilderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
name|FiqlSearchConditionBuilder
name|b
init|=
operator|new
name|FiqlSearchConditionBuilder
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|TimeZone
name|tz
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
name|tz
operator|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|afterClass
parameter_list|()
block|{
comment|// restoring defaults
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|tz
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyBuild
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|b
operator|.
name|query
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualToString
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"literalOrPattern*"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==literalOrPattern*"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualToNumber
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|123.5
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==123.5"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualToNumberCondition
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|comparesTo
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|,
literal|123.5
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=lt=123.5"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Date
name|parseDate
parameter_list|(
name|String
name|format
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|ParseException
block|{
return|return
operator|new
name|SimpleDateFormat
argument_list|(
name|format
argument_list|)
operator|.
name|parse
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualToDateDefault
parameter_list|()
throws|throws
name|ParseException
block|{
name|Date
name|d
init|=
name|parseDate
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|,
literal|"2011-03-01"
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==2011-03-01"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualToDateWithCustomFormat
parameter_list|()
throws|throws
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
argument_list|<>
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
name|Date
name|d
init|=
name|parseDate
argument_list|(
literal|"yyyy-MM-dd HH:mm Z"
argument_list|,
literal|"2011-03-01 12:34 +0000"
argument_list|)
decl_stmt|;
name|FiqlSearchConditionBuilder
name|bCustom
init|=
operator|new
name|FiqlSearchConditionBuilder
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|bCustom
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==2011-03-01T12:34:00"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqualToDuration
parameter_list|()
throws|throws
name|ParseException
throws|,
name|DatatypeConfigurationException
block|{
name|Duration
name|d
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==-P0Y0M1DT12H0M0S"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqualToString
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notEqualTo
argument_list|(
literal|"literalOrPattern*"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo!=literalOrPattern*"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqualToNumber
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notEqualTo
argument_list|(
literal|123.5
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo!=123.5"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqualToDateDefault
parameter_list|()
throws|throws
name|ParseException
block|{
name|Date
name|d
init|=
name|parseDate
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|,
literal|"2011-03-01"
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notEqualTo
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo!=2011-03-01"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNotEqualToDuration
parameter_list|()
throws|throws
name|ParseException
throws|,
name|DatatypeConfigurationException
block|{
name|Duration
name|d
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notEqualTo
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo!=-P0Y0M1DT12H0M0S"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterThanString
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lexicalAfter
argument_list|(
literal|"abc"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=abc"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessThanString
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lexicalBefore
argument_list|(
literal|"abc"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=lt=abc"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessOrEqualToString
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lexicalNotAfter
argument_list|(
literal|"abc"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=le=abc"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterOrEqualToString
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lexicalNotBefore
argument_list|(
literal|"abc"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=ge=abc"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterThanNumberDouble
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterThan
argument_list|(
literal|25.0
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=25.0"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterThanLong
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterThan
argument_list|(
literal|25
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=25"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessThanNumber
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lessThan
argument_list|(
literal|25.333
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=lt=25.333"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessOrEqualToNumberDouble
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lessOrEqualTo
argument_list|(
literal|0.0
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=le=0.0"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessOrEqualToNumberLong
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lessOrEqualTo
argument_list|(
literal|0
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=le=0"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterOrEqualToNumberDouble
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterOrEqualTo
argument_list|(
operator|-
literal|5.0
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=ge=-5.0"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterOrEqualToNumberLong
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterOrEqualTo
argument_list|(
operator|-
literal|5
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=ge=-5"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterThanDate
parameter_list|()
throws|throws
name|ParseException
block|{
name|Date
name|d
init|=
name|parseDate
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|,
literal|"2011-03-02"
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|after
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=2011-03-02"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessThanDate
parameter_list|()
throws|throws
name|ParseException
block|{
name|Date
name|d
init|=
name|parseDate
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|,
literal|"2011-03-02"
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|before
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=lt=2011-03-02"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessOrEqualToDate
parameter_list|()
throws|throws
name|ParseException
block|{
name|Date
name|d
init|=
name|parseDate
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|,
literal|"2011-03-02"
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notAfter
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=le=2011-03-02"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterOrEqualToDate
parameter_list|()
throws|throws
name|ParseException
block|{
name|Date
name|d
init|=
name|parseDate
argument_list|(
name|SearchUtils
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|,
literal|"2011-03-02"
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notBefore
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=ge=2011-03-02"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterThanDuration
parameter_list|()
throws|throws
name|DatatypeConfigurationException
block|{
name|Duration
name|d
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|after
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=-P0Y0M1DT12H0M0S"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessThanDuration
parameter_list|()
throws|throws
name|DatatypeConfigurationException
block|{
name|Duration
name|d
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|before
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=lt=-P0Y0M1DT12H0M0S"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessOrEqualToDuration
parameter_list|()
throws|throws
name|DatatypeConfigurationException
block|{
name|Duration
name|d
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notAfter
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=le=-P0Y0M1DT12H0M0S"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreaterOrEqualToDuration
parameter_list|()
throws|throws
name|DatatypeConfigurationException
block|{
name|Duration
name|d
init|=
name|DatatypeFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDuration
argument_list|(
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|notBefore
argument_list|(
name|d
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=ge=-P0Y0M1DT12H0M0S"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrSimple
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterThan
argument_list|(
literal|20
argument_list|)
operator|.
name|or
argument_list|()
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lessThan
argument_list|(
literal|10
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=20,foo=lt=10"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrSimpleShortcut
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterThan
argument_list|(
literal|20
argument_list|)
operator|.
name|or
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|lessThan
argument_list|(
literal|10
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=20,foo=lt=10"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndSimple
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterThan
argument_list|(
literal|20
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|is
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"plonk"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=20;bar==plonk"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndSimpleShortcut
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|greaterThan
argument_list|(
literal|20
argument_list|)
operator|.
name|and
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"plonk"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo=gt=20;bar==plonk"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrComplex
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|or
argument_list|(
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"aaa"
argument_list|)
argument_list|,
name|b
operator|.
name|is
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"bbb"
argument_list|)
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(foo==aaa,bar==bbb)"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndComplex
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|and
argument_list|(
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"aaa"
argument_list|)
argument_list|,
name|b
operator|.
name|is
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"bbb"
argument_list|)
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(foo==aaa;bar==bbb)"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplex1
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|123.4
argument_list|)
operator|.
name|or
argument_list|()
operator|.
name|and
argument_list|(
name|b
operator|.
name|is
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"asadf*"
argument_list|)
argument_list|,
name|b
operator|.
name|is
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|lessThan
argument_list|(
literal|20
argument_list|)
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==123.4,(bar==asadf*;baz=lt=20)"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplex2
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|123L
argument_list|)
operator|.
name|or
argument_list|()
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"null"
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|or
argument_list|(
name|b
operator|.
name|is
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"asadf*"
argument_list|)
argument_list|,
name|b
operator|.
name|is
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|lessThan
argument_list|(
literal|20
argument_list|)
operator|.
name|and
argument_list|()
operator|.
name|or
argument_list|(
name|b
operator|.
name|is
argument_list|(
literal|"sub1"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|0L
argument_list|)
argument_list|,
name|b
operator|.
name|is
argument_list|(
literal|"sub2"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|0L
argument_list|)
argument_list|)
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==123,foo==null;(bar==asadf*,baz=lt=20;(sub1==0,sub2==0))"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrAndImplicitWrap
parameter_list|()
block|{
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|and
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(foo==1,foo==2);bar==baz"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleOrShortcut
parameter_list|()
block|{
comment|// alternative to
comment|// b.is("foo").equalTo(123.4).or().is("foo").equalTo("137.8")
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|123.4
argument_list|,
literal|137.8
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo==123.4,foo==137.8"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleOrShortcutWithAnd
parameter_list|()
block|{
comment|// alternative to
comment|// b.is("foo").equalTo(123.4).or().is("foo").equalTo("137.8")
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|123.4
argument_list|,
literal|137.8
argument_list|)
operator|.
name|and
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(foo==123.4,foo==137.8);bar==baz"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleOrShortcutWithAnd2
parameter_list|()
block|{
comment|// alternative to
comment|// b.is("foo").equalTo(123.4).or().is("foo").equalTo("137.8")
name|String
name|ret
init|=
name|b
operator|.
name|is
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|123.4
argument_list|,
literal|137.8
argument_list|)
operator|.
name|or
argument_list|(
literal|"n"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"n1"
argument_list|)
operator|.
name|and
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|equalTo
argument_list|(
literal|"baz"
argument_list|)
operator|.
name|query
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"(foo==123.4,foo==137.8,n==n1);bar==baz"
argument_list|,
name|ret
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

