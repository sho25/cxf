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
name|odata
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ODataParserTest
block|{
specifier|private
name|ODataParser
argument_list|<
name|Person
argument_list|>
name|parser
decl_stmt|;
specifier|public
specifier|static
class|class
name|Person
block|{
specifier|private
name|String
name|firstName
decl_stmt|;
specifier|private
name|String
name|lastName
decl_stmt|;
specifier|private
name|int
name|age
decl_stmt|;
specifier|private
name|float
name|height
decl_stmt|;
specifier|private
name|double
name|hourlyRate
decl_stmt|;
specifier|private
name|Long
name|ssn
decl_stmt|;
specifier|public
name|Person
parameter_list|()
block|{         }
specifier|public
name|Person
parameter_list|(
specifier|final
name|String
name|firstName
parameter_list|,
specifier|final
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
specifier|public
name|int
name|getAge
parameter_list|()
block|{
return|return
name|age
return|;
block|}
specifier|public
name|void
name|setAge
parameter_list|(
name|int
name|age
parameter_list|)
block|{
name|this
operator|.
name|age
operator|=
name|age
expr_stmt|;
block|}
specifier|public
name|float
name|getHeight
parameter_list|()
block|{
return|return
name|height
return|;
block|}
specifier|public
name|void
name|setHeight
parameter_list|(
name|float
name|height
parameter_list|)
block|{
name|this
operator|.
name|height
operator|=
name|height
expr_stmt|;
block|}
specifier|public
name|double
name|getHourlyRate
parameter_list|()
block|{
return|return
name|hourlyRate
return|;
block|}
specifier|public
name|void
name|setHourlyRate
parameter_list|(
name|double
name|hourlyRate
parameter_list|)
block|{
name|this
operator|.
name|hourlyRate
operator|=
name|hourlyRate
expr_stmt|;
block|}
specifier|public
name|Long
name|getSsn
parameter_list|()
block|{
return|return
name|ssn
return|;
block|}
specifier|public
name|void
name|setSsn
parameter_list|(
name|Long
name|ssn
parameter_list|)
block|{
name|this
operator|.
name|ssn
operator|=
name|ssn
expr_stmt|;
block|}
name|Person
name|withAge
parameter_list|(
name|int
name|newAge
parameter_list|)
block|{
name|setAge
argument_list|(
name|newAge
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|Person
name|withHeight
parameter_list|(
name|float
name|newHeight
parameter_list|)
block|{
name|setHeight
argument_list|(
name|newHeight
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|Person
name|withHourlyRate
parameter_list|(
name|double
name|newHourlyRate
parameter_list|)
block|{
name|setHourlyRate
argument_list|(
name|newHourlyRate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|Person
name|withSsn
parameter_list|(
name|Long
name|newSsn
parameter_list|)
block|{
name|setSsn
argument_list|(
name|newSsn
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|parser
operator|=
operator|new
name|ODataParser
argument_list|<>
argument_list|(
name|Person
operator|.
name|class
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"thename"
argument_list|,
literal|"FirstName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByFirstNameEqualsValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"FirstName eq 'Tom'"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
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
name|Person
argument_list|(
literal|"Peter"
argument_list|,
literal|"Bombadil"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByFirstNameEqualsValueNonMatchingProperty
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"thename eq 'Tom'"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
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
name|Person
argument_list|(
literal|"Peter"
argument_list|,
literal|"Bombadil"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByFirstAndLastNameEqualValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"FirstName eq 'Tom' and LastName eq 'Bombadil'"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
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
name|Person
argument_list|(
literal|"Peter"
argument_list|,
literal|"Bombadil"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByFirstOrLastNameEqualValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"FirstName eq 'Tom' or FirstName eq 'Peter' and LastName eq 'Bombadil'"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
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
name|Person
argument_list|(
literal|"Peter"
argument_list|,
literal|"Bombadil"
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
name|Person
argument_list|(
literal|"Barry"
argument_list|,
literal|"Bombadil"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByFirstAndLastNameEqualValueWithAlternative
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"(FirstName eq 'Tom' and LastName eq 'Tommyknocker')"
operator|+
literal|" or (FirstName eq 'Peter' and LastName eq 'Bombadil')"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Tommyknocker"
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
name|Person
argument_list|(
literal|"Peter"
argument_list|,
literal|"Bombadil"
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
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByValueEqualsFirstName
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"'Tom' eq FirstName"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByAgeGreatThanValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"Age gt 17"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withAge
argument_list|(
literal|18
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
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withAge
argument_list|(
literal|16
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByHeightGreatOrEqualValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"Height ge 179.5f or Height le 159.5d"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withHeight
argument_list|(
literal|185.6f
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
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withHeight
argument_list|(
literal|166.7f
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterByHourlyRateGreatThanValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"HourlyRate ge 30.50d or HourlyRate lt 20.50f"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withHourlyRate
argument_list|(
literal|45.6
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
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withHourlyRate
argument_list|(
literal|26.7
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFilterBySsnNotEqualsToValue
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|SearchCondition
argument_list|<
name|Person
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"Ssn ne 748232221"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|filter
operator|.
name|isMet
argument_list|(
operator|new
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withSsn
argument_list|(
literal|553232222L
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
name|Person
argument_list|(
literal|"Tom"
argument_list|,
literal|"Bombadil"
argument_list|)
operator|.
name|withHourlyRate
argument_list|(
literal|748232221L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

