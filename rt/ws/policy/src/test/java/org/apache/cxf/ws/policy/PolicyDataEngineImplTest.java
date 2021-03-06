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
name|message
operator|.
name|Message
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
name|policy
operator|.
name|PolicyCalculator
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
name|jaxb
operator|.
name|JaxbAssertion
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|PolicyDataEngineImplTest
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|TEST_POLICY_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://test"
argument_list|,
literal|"TestPolicy"
argument_list|)
decl_stmt|;
class|class
name|TestPolicy
block|{     }
class|class
name|TestPolicyCalculator
implements|implements
name|PolicyCalculator
argument_list|<
name|TestPolicy
argument_list|>
block|{
specifier|public
name|Class
argument_list|<
name|TestPolicy
argument_list|>
name|getDataClass
parameter_list|()
block|{
return|return
name|TestPolicy
operator|.
name|class
return|;
block|}
specifier|public
name|QName
name|getDataClassName
parameter_list|()
block|{
return|return
name|TEST_POLICY_NAME
return|;
block|}
specifier|public
name|TestPolicy
name|intersect
parameter_list|(
name|TestPolicy
name|policy1
parameter_list|,
name|TestPolicy
name|policy2
parameter_list|)
block|{
return|return
name|policy1
return|;
block|}
specifier|public
name|boolean
name|isAsserted
parameter_list|(
name|Message
name|message
parameter_list|,
name|TestPolicy
name|policy
parameter_list|,
name|TestPolicy
name|refPolicy
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
specifier|public
name|AssertionInfo
name|getTestPolicyAssertionInfo
parameter_list|(
name|TestPolicy
name|policy
parameter_list|)
block|{
name|JaxbAssertion
argument_list|<
name|TestPolicy
argument_list|>
name|assertion
init|=
operator|new
name|JaxbAssertion
argument_list|<>
argument_list|(
name|TEST_POLICY_NAME
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertion
operator|.
name|setData
argument_list|(
name|policy
argument_list|)
expr_stmt|;
return|return
operator|new
name|AssertionInfo
argument_list|(
name|assertion
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertMessageNullAim
parameter_list|()
block|{
name|checkAssertWithMap
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertMessageEmptyAim
parameter_list|()
block|{
name|checkAssertWithMap
argument_list|(
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertMessage
parameter_list|()
block|{
name|TestPolicy
name|policy
init|=
operator|new
name|TestPolicy
argument_list|()
decl_stmt|;
name|AssertionInfo
name|ai
init|=
name|getTestPolicyAssertionInfo
argument_list|(
name|policy
argument_list|)
decl_stmt|;
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
name|ais
operator|.
name|add
argument_list|(
name|ai
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|TEST_POLICY_NAME
argument_list|,
name|ais
argument_list|)
expr_stmt|;
name|checkAssertWithMap
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Simply check that it runs without any exceptions      *      * @param assertionInfoMap      */
specifier|private
name|void
name|checkAssertWithMap
parameter_list|(
name|AssertionInfoMap
name|assertionInfoMap
parameter_list|)
block|{
name|PolicyDataEngineImpl
name|pde
init|=
operator|new
name|PolicyDataEngineImpl
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|pde
operator|.
name|setPolicyEngine
argument_list|(
operator|new
name|PolicyEngineImpl
argument_list|()
argument_list|)
expr_stmt|;
name|TestPolicy
name|confPol
init|=
operator|new
name|TestPolicy
argument_list|()
decl_stmt|;
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createControl
argument_list|()
decl_stmt|;
name|PolicyCalculator
argument_list|<
name|TestPolicy
argument_list|>
name|policyCalculator
init|=
operator|new
name|TestPolicyCalculator
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|TestPolicy
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|confPol
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|assertionInfoMap
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|pde
operator|.
name|assertMessage
argument_list|(
name|message
argument_list|,
name|confPol
argument_list|,
name|policyCalculator
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

