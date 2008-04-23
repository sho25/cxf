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
name|ws
operator|.
name|policy
operator|.
name|builder
operator|.
name|primitive
package|;
end_package

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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfo
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfoMap
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
name|ws
operator|.
name|policy
operator|.
name|PolicyAssertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|All
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyComponent
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|NestedPrimitiveAssertionTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAMESPACE
init|=
literal|"http://www.w3.org/2007/01/addressing/metadata"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|TEST_NAME1
init|=
operator|new
name|QName
argument_list|(
name|TEST_NAMESPACE
argument_list|,
literal|"Addressing"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|TEST_NAME2
init|=
operator|new
name|QName
argument_list|(
name|TEST_NAMESPACE
argument_list|,
literal|"AnonymousResponses"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|TEST_NAME3
init|=
operator|new
name|QName
argument_list|(
name|TEST_NAMESPACE
argument_list|,
literal|"NonAnonymousResponses"
argument_list|)
decl_stmt|;
specifier|public
class|class
name|CustomPrimitiveAssertion
extends|extends
name|PrimitiveAssertion
block|{
specifier|private
name|int
name|x
decl_stmt|;
specifier|public
name|CustomPrimitiveAssertion
parameter_list|(
name|QName
name|type
parameter_list|,
name|int
name|x
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|x
operator|=
name|x
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equal
parameter_list|(
name|PolicyComponent
name|pc
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|pc
operator|instanceof
name|CustomPrimitiveAssertion
operator|)
operator|||
operator|!
name|super
operator|.
name|equal
argument_list|(
name|pc
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|x
operator|==
operator|(
operator|(
name|CustomPrimitiveAssertion
operator|)
name|pc
operator|)
operator|.
name|x
return|;
block|}
block|}
specifier|private
name|Policy
index|[]
name|policies
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|policies
operator|=
name|buildTestPolicies
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoNeedToAssertWithEmptyPolicy
parameter_list|()
block|{
name|PolicyAssertion
name|a
init|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|a
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No need to assert"
argument_list|,
name|a
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No need to assert"
argument_list|,
name|a
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoNeedToAssertWithNonEmptyPolicy
parameter_list|()
block|{
name|PolicyAssertion
name|a
init|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|a
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"No need to assert"
argument_list|,
name|a
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|p
operator|.
name|addAssertion
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|,
name|p
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Primitive Assertions need to be asserted"
argument_list|,
name|a
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|p
operator|.
name|addAssertion
argument_list|(
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|false
argument_list|,
name|p
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No need to assert"
argument_list|,
name|a
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAsserted
parameter_list|()
block|{
name|PolicyAssertion
name|a1
init|=
operator|new
name|CustomPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|PolicyAssertion
name|a2
init|=
operator|new
name|CustomPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Policy
name|nested
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAssertion
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|nested
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|NestedPrimitiveAssertion
name|na
init|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"nested"
argument_list|)
argument_list|,
literal|false
argument_list|,
name|nested
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PolicyAssertion
argument_list|>
name|ais
init|=
operator|new
name|ArrayList
argument_list|<
name|PolicyAssertion
argument_list|>
argument_list|()
decl_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|na
argument_list|)
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|ais
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Assertion has been asserted even though nether na nor a2 have been"
argument_list|,
name|na
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|assertAssertion
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
literal|"nested"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Assertion has been asserted even though a2 has not been"
argument_list|,
name|na
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert a1 only
name|assertAssertion
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Assertion has been asserted even though a2 has not been"
argument_list|,
name|na
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert a2 tpp
name|assertAssertion
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Assertion has not been asserted even though both na nad a2 have been"
argument_list|,
name|na
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|PolicyAssertion
name|a3
init|=
operator|new
name|CustomPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|all
operator|.
name|addAssertion
argument_list|(
name|a3
argument_list|)
expr_stmt|;
name|aim
operator|.
name|getAssertionInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|AssertionInfo
argument_list|(
name|a3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Assertion has been asserted even though a3 has not been"
argument_list|,
name|na
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|assertAssertion
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Assertion has not been asserted even though na,a2,a3 have been"
argument_list|,
name|na
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertAssertion
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|type
parameter_list|,
name|boolean
name|value
parameter_list|,
name|boolean
name|all
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|aic
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|all
condition|)
block|{
name|AssertionInfo
name|ai
init|=
name|aic
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|aic
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEqual
parameter_list|()
block|{
name|PolicyAssertion
name|other
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|policies
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|PolicyAssertion
name|a
init|=
operator|(
name|PolicyAssertion
operator|)
name|policies
index|[
name|i
index|]
operator|.
name|getFirstPolicyComponent
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Assertion "
operator|+
name|i
operator|+
literal|" should equal itself."
argument_list|,
name|a
operator|.
name|equal
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Assertion "
operator|+
name|i
operator|+
literal|" should not equal other."
argument_list|,
operator|!
name|a
operator|.
name|equal
argument_list|(
name|other
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
name|i
operator|+
literal|1
init|;
name|j
operator|<
name|policies
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|Assertion
name|b
init|=
operator|(
name|Assertion
operator|)
name|policies
index|[
name|j
index|]
operator|.
name|getFirstPolicyComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|j
operator|==
literal|1
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Assertion "
operator|+
name|i
operator|+
literal|" should equal "
operator|+
name|j
operator|+
literal|"."
argument_list|,
name|a
operator|.
name|equal
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
literal|"Assertion "
operator|+
name|i
operator|+
literal|" unexpectedly equals assertion "
operator|+
name|j
operator|+
literal|"."
argument_list|,
operator|!
name|a
operator|.
name|equal
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
specifier|static
name|Policy
index|[]
name|buildTestPolicies
parameter_list|()
block|{
name|Policy
index|[]
name|p
init|=
operator|new
name|Policy
index|[
literal|5
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|p
index|[
name|i
index|]
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|NestedPrimitiveAssertion
name|a
init|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
name|TEST_NAME1
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Policy
name|nested
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|a
operator|.
name|setPolicy
argument_list|(
name|nested
argument_list|)
expr_stmt|;
name|p
index|[
name|i
operator|++
index|]
operator|.
name|addPolicyComponent
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|p
index|[
name|i
index|]
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
name|TEST_NAME1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|nested
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|.
name|setPolicy
argument_list|(
name|nested
argument_list|)
expr_stmt|;
name|p
index|[
name|i
operator|++
index|]
operator|.
name|addPolicyComponent
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|p
index|[
name|i
index|]
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
name|TEST_NAME1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|nested
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|.
name|setPolicy
argument_list|(
name|nested
argument_list|)
expr_stmt|;
name|nested
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|TEST_NAME2
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|nested
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|TEST_NAME3
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|p
index|[
name|i
operator|++
index|]
operator|.
name|addPolicyComponent
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|p
index|[
name|i
index|]
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
name|TEST_NAME1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|nested
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|.
name|setPolicy
argument_list|(
name|nested
argument_list|)
expr_stmt|;
name|ExactlyOne
name|eo
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|nested
operator|.
name|addPolicyComponent
argument_list|(
name|eo
argument_list|)
expr_stmt|;
name|eo
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|TEST_NAME2
argument_list|)
argument_list|)
expr_stmt|;
name|eo
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|TEST_NAME3
argument_list|)
argument_list|)
expr_stmt|;
name|p
index|[
name|i
operator|++
index|]
operator|.
name|addPolicyComponent
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|p
index|[
name|i
index|]
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|=
operator|new
name|NestedPrimitiveAssertion
argument_list|(
name|TEST_NAME1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|nested
operator|=
operator|new
name|Policy
argument_list|()
expr_stmt|;
name|a
operator|.
name|setPolicy
argument_list|(
name|nested
argument_list|)
expr_stmt|;
name|nested
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|TEST_NAME3
argument_list|)
argument_list|)
expr_stmt|;
name|p
index|[
name|i
operator|++
index|]
operator|.
name|addPolicyComponent
argument_list|(
name|a
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
block|}
end_class

end_unit

