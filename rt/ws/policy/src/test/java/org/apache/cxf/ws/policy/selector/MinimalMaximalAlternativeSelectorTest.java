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
name|selector
package|;
end_package

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
name|AlternativeSelector
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
name|Assertor
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
name|cxf
operator|.
name|ws
operator|.
name|policy
operator|.
name|PolicyEngine
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
name|TestAssertion
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
name|easymock
operator|.
name|classextension
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
name|classextension
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|MinimalMaximalAlternativeSelectorTest
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
name|testChooseMinAlternative
parameter_list|()
block|{
name|AlternativeSelector
name|selector
init|=
operator|new
name|MinimalAlternativeSelector
argument_list|()
decl_stmt|;
name|PolicyEngine
name|engine
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assertor
name|assertor
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Assertor
operator|.
name|class
argument_list|)
decl_stmt|;
name|Policy
name|policy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|PolicyAssertion
name|a1
init|=
operator|new
name|TestAssertion
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAssertion
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|maxAlternative
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|all
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|all
operator|=
operator|new
name|All
argument_list|()
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|minAlternative
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|all
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|policy
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|maxAlternative
argument_list|,
name|assertor
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|minAlternative
argument_list|,
name|assertor
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|choice
init|=
name|selector
operator|.
name|selectAlternative
argument_list|(
name|policy
argument_list|,
name|engine
argument_list|,
name|assertor
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|choice
operator|.
name|size
argument_list|()
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
name|testChooseMaxAlternative
parameter_list|()
block|{
name|AlternativeSelector
name|selector
init|=
operator|new
name|MaximalAlternativeSelector
argument_list|()
decl_stmt|;
name|PolicyEngine
name|engine
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assertor
name|assertor
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Assertor
operator|.
name|class
argument_list|)
decl_stmt|;
name|Policy
name|policy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|PolicyAssertion
name|a1
init|=
operator|new
name|TestAssertion
argument_list|()
decl_stmt|;
name|all
operator|.
name|addAssertion
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|maxAlternative
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|all
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|all
operator|=
operator|new
name|All
argument_list|()
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|minAlternative
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|all
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|policy
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|maxAlternative
argument_list|,
name|assertor
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|minAlternative
argument_list|,
name|assertor
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|choice
init|=
name|selector
operator|.
name|selectAlternative
argument_list|(
name|policy
argument_list|,
name|engine
argument_list|,
name|assertor
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|choice
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|choice
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

