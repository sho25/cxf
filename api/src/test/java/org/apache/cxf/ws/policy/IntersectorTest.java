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
name|PrimitiveAssertionBuilder
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
name|IntersectorTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|NAME1
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|NAME2
init|=
operator|new
name|QName
argument_list|(
literal|"http://x.y.z"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
specifier|private
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
specifier|private
name|Intersector
name|intersector
decl_stmt|;
specifier|private
name|AssertionBuilderRegistry
name|reg
decl_stmt|;
specifier|private
name|PrimitiveAssertionBuilder
name|pab1
decl_stmt|;
specifier|private
name|PrimitiveAssertionBuilder
name|pab2
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|reg
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|intersector
operator|=
operator|new
name|Intersector
argument_list|(
name|reg
argument_list|)
expr_stmt|;
name|pab1
operator|=
operator|new
name|PrimitiveAssertionBuilder
argument_list|()
expr_stmt|;
name|pab1
operator|.
name|setKnownElements
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|NAME1
argument_list|)
argument_list|)
expr_stmt|;
name|pab2
operator|=
operator|new
name|PrimitiveAssertionBuilder
argument_list|()
expr_stmt|;
name|pab2
operator|.
name|setKnownElements
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|NAME2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompatiblePoliciesBothEmpty
parameter_list|()
block|{
name|Policy
name|p1
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|Policy
name|p2
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|intersector
operator|.
name|compatiblePolicies
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompatiblePoliciesOneEmpty
parameter_list|()
block|{
name|Policy
name|p1
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|Policy
name|p2
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|p2
operator|.
name|addPolicyComponent
argument_list|(
operator|new
name|PrimitiveAssertion
argument_list|(
name|NAME1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|intersector
operator|.
name|compatiblePolicies
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIntersectPoliciesBothEmpty
parameter_list|()
block|{
name|Policy
name|p1
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|Policy
name|p2
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|Policy
name|p
init|=
name|intersector
operator|.
name|intersect
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
comment|// control.replay();
block|}
block|}
end_class

end_unit

