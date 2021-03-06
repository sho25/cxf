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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleSearchConditionTest
block|{
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|SingleAttr
argument_list|>
name|cEq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|SingleAttr
argument_list|>
name|cGt
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|SingleAttr
argument_list|>
name|cGeq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|SingleAttr
argument_list|>
name|cLt
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|SingleAttr
argument_list|>
name|cLeq
decl_stmt|;
specifier|private
specifier|static
name|SingleAttr
name|attr
init|=
operator|new
name|SingleAttr
argument_list|(
literal|"bbb"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|SingleAttr
name|attrGreater
init|=
operator|new
name|SingleAttr
argument_list|(
literal|"ccc"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|SingleAttr
name|attrLesser
init|=
operator|new
name|SingleAttr
argument_list|(
literal|"aaa"
argument_list|)
decl_stmt|;
comment|// TODO 1. comparison with multiple values
comment|// TODO 2. comparison when getter returns null/throws exception
specifier|private
specifier|static
name|DoubleAttr
name|attr2Vals
init|=
operator|new
name|DoubleAttr
argument_list|(
literal|"bbb"
argument_list|,
literal|"ccc"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|DoubleAttr
name|attr2ValsGreater
init|=
operator|new
name|DoubleAttr
argument_list|(
literal|"ccc"
argument_list|,
literal|"ddd"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|DoubleAttr
name|attr2ValsLesser
init|=
operator|new
name|DoubleAttr
argument_list|(
literal|"aaa"
argument_list|,
literal|"bbb"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|DoubleAttr
name|attr1Val
init|=
operator|new
name|DoubleAttr
argument_list|(
literal|"bbb"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|DoubleAttr
name|attr1ValGreater
init|=
operator|new
name|DoubleAttr
argument_list|(
literal|"ccc"
argument_list|,
literal|"ingored"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|DoubleAttr
name|attr1ValLesser
init|=
operator|new
name|DoubleAttr
argument_list|(
literal|"aaa"
argument_list|,
literal|"ingored"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc1Eq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc1Gt
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc1Geq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc1Lt
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc1Leq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc2Eq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc2Gt
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc2Geq
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc2Lt
decl_stmt|;
specifier|private
specifier|static
name|SearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
name|dc2Leq
decl_stmt|;
specifier|private
specifier|static
name|List
argument_list|<
name|ConditionType
argument_list|>
name|supported
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
name|ConditionType
operator|.
name|NOT_EQUALS
argument_list|,
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
argument_list|,
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|,
name|ConditionType
operator|.
name|LESS_OR_EQUALS
argument_list|,
name|ConditionType
operator|.
name|LESS_THAN
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpBeforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|cEq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|cGt
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|cGeq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|cLt
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|cLeq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|LESS_OR_EQUALS
argument_list|,
name|attr
argument_list|)
expr_stmt|;
name|dc1Eq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
name|attr1Val
argument_list|)
expr_stmt|;
name|dc1Gt
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|,
name|attr1Val
argument_list|)
expr_stmt|;
name|dc1Geq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
argument_list|,
name|attr1Val
argument_list|)
expr_stmt|;
name|dc1Lt
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|,
name|attr1Val
argument_list|)
expr_stmt|;
name|dc1Leq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|LESS_OR_EQUALS
argument_list|,
name|attr1Val
argument_list|)
expr_stmt|;
name|dc2Eq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
name|attr2Vals
argument_list|)
expr_stmt|;
name|dc2Gt
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|,
name|attr2Vals
argument_list|)
expr_stmt|;
name|dc2Geq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|GREATER_OR_EQUALS
argument_list|,
name|attr2Vals
argument_list|)
expr_stmt|;
name|dc2Lt
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|,
name|attr2Vals
argument_list|)
expr_stmt|;
name|dc2Leq
operator|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|LESS_OR_EQUALS
argument_list|,
name|attr2Vals
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
name|testCtorNull1
parameter_list|()
block|{
operator|new
name|SimpleSearchCondition
argument_list|<
name|SingleAttr
argument_list|>
argument_list|(
operator|(
name|ConditionType
operator|)
literal|null
argument_list|,
name|attr
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
name|testCtorNull2
parameter_list|()
block|{
operator|new
name|SimpleSearchCondition
argument_list|<
name|SingleAttr
argument_list|>
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCtorCondSupported
parameter_list|()
block|{
for|for
control|(
name|ConditionType
name|ct
range|:
name|ConditionType
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
operator|new
name|SimpleSearchCondition
argument_list|<
name|SingleAttr
argument_list|>
argument_list|(
name|ct
argument_list|,
name|attr
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|supported
operator|.
name|contains
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Not supported type %s should throw exception"
argument_list|,
name|ct
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
if|if
condition|(
name|supported
operator|.
name|contains
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Supported type %s should not throw exception"
argument_list|,
name|ct
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|testCtorMapNull
parameter_list|()
block|{
operator|new
name|SimpleSearchCondition
argument_list|<
name|SingleAttr
argument_list|>
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|ConditionType
argument_list|>
operator|)
literal|null
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCtorMapCondSupported
parameter_list|()
block|{
for|for
control|(
name|ConditionType
name|ct
range|:
name|ConditionType
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ConditionType
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
operator|new
name|SimpleSearchCondition
argument_list|<
name|SingleAttr
argument_list|>
argument_list|(
name|map
argument_list|,
name|attr
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|supported
operator|.
name|contains
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Not supported type %s should throw exception"
argument_list|,
name|ct
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
if|if
condition|(
name|supported
operator|.
name|contains
argument_list|(
name|ct
argument_list|)
condition|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Supported type %s should not throw exception"
argument_list|,
name|ct
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCondition
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|cLeq
operator|.
name|getCondition
argument_list|()
argument_list|,
name|attr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConditionType
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|cEq
operator|.
name|getConditionType
argument_list|()
argument_list|,
name|ConditionType
operator|.
name|EQUALS
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|cLt
operator|.
name|getConditionType
argument_list|()
argument_list|,
name|ConditionType
operator|.
name|LESS_THAN
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConditions
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|cGt
operator|.
name|getSearchConditions
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetEq
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|cEq
operator|.
name|isMet
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cEq
operator|.
name|isMet
argument_list|(
name|attrGreater
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGt
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|cGt
operator|.
name|isMet
argument_list|(
name|attrGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cGt
operator|.
name|isMet
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cGt
operator|.
name|isMet
argument_list|(
name|attrLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGeq
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|cGeq
operator|.
name|isMet
argument_list|(
name|attrGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cGeq
operator|.
name|isMet
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cGeq
operator|.
name|isMet
argument_list|(
name|attrLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLt
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|cLt
operator|.
name|isMet
argument_list|(
name|attrGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cLt
operator|.
name|isMet
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cLt
operator|.
name|isMet
argument_list|(
name|attrLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLeq
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|cLeq
operator|.
name|isMet
argument_list|(
name|attrGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cLeq
operator|.
name|isMet
argument_list|(
name|attr
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cLeq
operator|.
name|isMet
argument_list|(
name|attrLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetEqPrimitive
parameter_list|()
block|{
name|assertTrue
argument_list|(
operator|new
name|SimpleSearchCondition
argument_list|<
name|String
argument_list|>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
literal|"foo"
argument_list|)
operator|.
name|isMet
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGtPrimitive
parameter_list|()
block|{
name|assertTrue
argument_list|(
operator|new
name|SimpleSearchCondition
argument_list|<
name|Float
argument_list|>
argument_list|(
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|,
literal|1.5f
argument_list|)
operator|.
name|isMet
argument_list|(
literal|2.5f
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLtPrimitive
parameter_list|()
block|{
name|assertTrue
argument_list|(
operator|new
name|SimpleSearchCondition
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|ConditionType
operator|.
name|LESS_THAN
argument_list|,
literal|10
argument_list|)
operator|.
name|isMet
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindAll
parameter_list|()
block|{
name|List
argument_list|<
name|SingleAttr
argument_list|>
name|inputs
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|attr
argument_list|,
name|attrGreater
argument_list|,
name|attrLesser
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SingleAttr
argument_list|>
name|found
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|attr
argument_list|,
name|attrGreater
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|found
argument_list|,
name|cGeq
operator|.
name|findAll
argument_list|(
name|inputs
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetEqDouble1Val
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|dc1Eq
operator|.
name|isMet
argument_list|(
name|attr1ValGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc1Eq
operator|.
name|isMet
argument_list|(
name|attr1Val
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc1Eq
operator|.
name|isMet
argument_list|(
name|attr1ValLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGtDouble1Val
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|dc1Gt
operator|.
name|isMet
argument_list|(
name|attr1ValGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc1Gt
operator|.
name|isMet
argument_list|(
name|attr1Val
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc1Gt
operator|.
name|isMet
argument_list|(
name|attr1ValLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGeqDouble1Val
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|dc1Geq
operator|.
name|isMet
argument_list|(
name|attr1ValGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc1Geq
operator|.
name|isMet
argument_list|(
name|attr1Val
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc1Geq
operator|.
name|isMet
argument_list|(
name|attr1ValLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLtDouble1Val
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|dc1Lt
operator|.
name|isMet
argument_list|(
name|attr1ValGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc1Lt
operator|.
name|isMet
argument_list|(
name|attr1Val
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc1Lt
operator|.
name|isMet
argument_list|(
name|attr1ValLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLeqDouble1Val
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|dc1Leq
operator|.
name|isMet
argument_list|(
name|attr1ValGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc1Leq
operator|.
name|isMet
argument_list|(
name|attr1Val
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc1Leq
operator|.
name|isMet
argument_list|(
name|attr1ValLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetEqDouble2Vals
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|dc2Eq
operator|.
name|isMet
argument_list|(
name|attr2ValsGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc2Eq
operator|.
name|isMet
argument_list|(
name|attr2Vals
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc2Eq
operator|.
name|isMet
argument_list|(
name|attr2ValsLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGtDouble2Vals
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|dc2Gt
operator|.
name|isMet
argument_list|(
name|attr2ValsGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc2Gt
operator|.
name|isMet
argument_list|(
name|attr2Vals
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc2Gt
operator|.
name|isMet
argument_list|(
name|attr2ValsLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetGeqDouble2Vals
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|dc2Geq
operator|.
name|isMet
argument_list|(
name|attr2ValsGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc2Geq
operator|.
name|isMet
argument_list|(
name|attr2Vals
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc2Geq
operator|.
name|isMet
argument_list|(
name|attr2ValsLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLtDouble2Vals
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|dc2Lt
operator|.
name|isMet
argument_list|(
name|attr2ValsGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dc2Lt
operator|.
name|isMet
argument_list|(
name|attr2Vals
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc2Lt
operator|.
name|isMet
argument_list|(
name|attr2ValsLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetLeqDouble2Vals
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|dc2Leq
operator|.
name|isMet
argument_list|(
name|attr2ValsGreater
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc2Leq
operator|.
name|isMet
argument_list|(
name|attr2Vals
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dc2Leq
operator|.
name|isMet
argument_list|(
name|attr2ValsLesser
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetMappedOperators
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ConditionType
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
name|ConditionType
operator|.
name|LESS_THAN
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
name|ConditionType
operator|.
name|GREATER_THAN
argument_list|)
expr_stmt|;
comment|// expression "template.getFoo()< pojo.getFoo()& template.getBar()> pojo.getBar()"
name|assertTrue
argument_list|(
operator|new
name|SimpleSearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
argument_list|(
name|map
argument_list|,
operator|new
name|DoubleAttr
argument_list|(
literal|"bbb"
argument_list|,
literal|"ccc"
argument_list|)
argument_list|)
operator|.
name|isMet
argument_list|(
operator|new
name|DoubleAttr
argument_list|(
literal|"aaa"
argument_list|,
literal|"ddd"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// expression "template.getBar()> pojo.getBar()"
name|assertTrue
argument_list|(
operator|new
name|SimpleSearchCondition
argument_list|<
name|DoubleAttr
argument_list|>
argument_list|(
name|map
argument_list|,
operator|new
name|DoubleAttr
argument_list|(
literal|null
argument_list|,
literal|"ccc"
argument_list|)
argument_list|)
operator|.
name|isMet
argument_list|(
operator|new
name|DoubleAttr
argument_list|(
literal|"!not-interpreted!"
argument_list|,
literal|"ddd"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetWildcardEnds
parameter_list|()
block|{
name|SimpleSearchCondition
argument_list|<
name|String
argument_list|>
name|ssc
init|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
literal|"bar*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"barbaz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"foobar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetWildcardStarts
parameter_list|()
block|{
name|SimpleSearchCondition
argument_list|<
name|String
argument_list|>
name|ssc
init|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
literal|"*bar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"barbaz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"foobar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetWildcardStartsEnds
parameter_list|()
block|{
name|SimpleSearchCondition
argument_list|<
name|String
argument_list|>
name|ssc
init|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
literal|"*bar*"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"barbaz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"foobar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsMetWildcardMultiAsterisk
parameter_list|()
block|{
name|SimpleSearchCondition
argument_list|<
name|String
argument_list|>
name|ssc
init|=
operator|new
name|SimpleSearchCondition
argument_list|<>
argument_list|(
name|ConditionType
operator|.
name|EQUALS
argument_list|,
literal|"*ba*r*"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"ba*r"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"fooba*r"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"fooba*rbaz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ssc
operator|.
name|isMet
argument_list|(
literal|"foobarbaz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|SingleAttr
block|{
specifier|private
name|String
name|foo
decl_stmt|;
name|SingleAttr
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
comment|// this should not be used by "isMet" (is not public)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|String
name|getBar
parameter_list|()
block|{
return|return
literal|"it's private!"
return|;
block|}
block|}
specifier|static
class|class
name|DoubleAttr
block|{
specifier|private
name|String
name|foo
decl_stmt|;
specifier|private
name|String
name|bar
decl_stmt|;
name|DoubleAttr
parameter_list|(
name|String
name|foo
parameter_list|,
name|String
name|bar
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
name|this
operator|.
name|bar
operator|=
name|bar
expr_stmt|;
block|}
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
specifier|public
name|String
name|getBar
parameter_list|()
block|{
return|return
name|bar
return|;
block|}
block|}
block|}
end_class

end_unit

