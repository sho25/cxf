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
name|helpers
operator|.
name|CastUtils
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
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertion
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
name|builders
operator|.
name|PolicyContainingPrimitiveAssertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|AssertionInfoMapTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAlternativeSupported
parameter_list|()
block|{
name|PolicyAssertion
name|a1
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|aqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|a1
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aqn
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|PolicyAssertion
name|a2
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|a2
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aqn
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|PolicyAssertion
name|b
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|bqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|b
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bqn
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|PolicyAssertion
name|c
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|cqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|cqn
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ai1
init|=
operator|new
name|AssertionInfo
argument_list|(
name|a1
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ai2
init|=
operator|new
name|AssertionInfo
argument_list|(
name|a2
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|AssertionInfo
name|bi
init|=
operator|new
name|AssertionInfo
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ci
init|=
operator|new
name|AssertionInfo
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|ai1
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|ai2
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|aqn
argument_list|,
name|ais
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|bqn
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|bi
argument_list|)
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|cqn
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|ci
argument_list|)
argument_list|)
expr_stmt|;
name|ai2
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bi
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ci
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|a1
operator|.
name|equal
argument_list|(
name|a1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|a2
operator|.
name|equal
argument_list|(
name|a2
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|b
operator|.
name|equal
argument_list|(
name|b
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|c
operator|.
name|equal
argument_list|(
name|c
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|a2
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|b
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|c
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Assertion
argument_list|>
name|alt1
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|alt1
operator|.
name|add
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|alt1
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Assertion
argument_list|>
name|alt2
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|alt2
operator|.
name|add
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|alt2
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|aim
operator|.
name|supportsAlternative
argument_list|(
name|alt1
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aim
operator|.
name|supportsAlternative
argument_list|(
name|alt2
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCheckEffectivePolicy
parameter_list|()
block|{
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|QName
name|aqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|Assertion
name|a
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
name|aqn
argument_list|)
decl_stmt|;
name|QName
name|bqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|Assertion
name|b
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
name|bqn
argument_list|)
decl_stmt|;
name|QName
name|cqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|Assertion
name|c
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
name|cqn
argument_list|)
decl_stmt|;
name|All
name|alt1
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|alt1
operator|.
name|addAssertion
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|alt1
operator|.
name|addAssertion
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|All
name|alt2
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|alt2
operator|.
name|addAssertion
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|alt1
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|alt2
argument_list|)
expr_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ai
init|=
operator|new
name|AssertionInfo
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|AssertionInfo
name|bi
init|=
operator|new
name|AssertionInfo
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ci
init|=
operator|new
name|AssertionInfo
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|aqn
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|ai
argument_list|)
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|bqn
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|bi
argument_list|)
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|cqn
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|ci
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|aim
operator|.
name|checkEffectivePolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ci
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|aim
operator|.
name|checkEffectivePolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCheck
parameter_list|()
throws|throws
name|PolicyException
block|{
name|QName
name|aqn
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|Assertion
name|a
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
name|aqn
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|assertions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|assertions
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|assertions
argument_list|)
decl_stmt|;
try|try
block|{
name|aim
operator|.
name|check
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PolicyException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"NOT_ASSERTED_EXC"
argument_list|,
name|ex
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|aim
operator|.
name|get
argument_list|(
name|aqn
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|aim
operator|.
name|check
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAllAssertionsIn
parameter_list|()
block|{
name|Policy
name|nested
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|Assertion
name|nb
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|nested
operator|.
name|addAssertion
argument_list|(
name|nb
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|Assertion
name|a1
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Assertion
name|a2
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|Assertion
name|b
init|=
operator|new
name|PrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|Assertion
name|c
init|=
operator|new
name|PolicyContainingPrimitiveAssertion
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"c"
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|nested
argument_list|)
decl_stmt|;
name|All
name|alt1
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|alt1
operator|.
name|addAssertion
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|alt1
operator|.
name|addAssertion
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|All
name|alt2
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|alt1
operator|.
name|addAssertion
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|alt2
operator|.
name|addAssertion
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|alt1
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|alt2
argument_list|)
expr_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|listA
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2 A assertions should've been added"
argument_list|,
literal|2
argument_list|,
name|listA
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AssertionInfo
index|[]
name|ais
init|=
name|listA
operator|.
name|toArray
argument_list|(
operator|new
name|AssertionInfo
index|[]
block|{}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Two different A instances should be added"
argument_list|,
name|ais
index|[
literal|0
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|a1
operator|&&
name|ais
index|[
literal|1
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|a2
operator|||
name|ais
index|[
literal|0
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|a2
operator|&&
name|ais
index|[
literal|1
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|a1
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|listB
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2 B assertions should've been added"
argument_list|,
literal|2
argument_list|,
name|listB
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ais
operator|=
name|listB
operator|.
name|toArray
argument_list|(
operator|new
name|AssertionInfo
index|[]
block|{}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Two different B instances should be added"
argument_list|,
name|ais
index|[
literal|0
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|nb
operator|&&
name|ais
index|[
literal|1
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|b
operator|||
name|ais
index|[
literal|0
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|b
operator|&&
name|ais
index|[
literal|1
index|]
operator|.
name|getAssertion
argument_list|()
operator|==
name|nb
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|listC
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1 C assertion should've been added"
argument_list|,
literal|1
argument_list|,
name|listC
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ais
operator|=
name|listC
operator|.
name|toArray
argument_list|(
operator|new
name|AssertionInfo
index|[]
block|{}
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"One C instances should be added"
argument_list|,
name|ais
index|[
literal|0
index|]
operator|.
name|getAssertion
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

