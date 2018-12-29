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
name|attachment
operator|.
name|external
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
name|service
operator|.
name|model
operator|.
name|BindingFaultInfo
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
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|assertSame
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|PolicyAttachmentTest
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
name|testBasic
parameter_list|()
block|{
name|PolicyAttachment
name|pa
init|=
operator|new
name|PolicyAttachment
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|pa
operator|.
name|getDomainExpressions
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|pa
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|Policy
name|p
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Policy
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DomainExpression
argument_list|>
name|des
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|,
name|DomainExpression
operator|.
name|class
argument_list|)
decl_stmt|;
name|pa
operator|.
name|setPolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|pa
operator|.
name|setDomainExpressions
argument_list|(
name|des
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|pa
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|des
argument_list|,
name|pa
operator|.
name|getDomainExpressions
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAppliesToService
parameter_list|()
block|{
name|ServiceInfo
name|si1
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServiceInfo
name|si2
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|DomainExpression
name|de
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpression
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DomainExpression
argument_list|>
name|des
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|de
argument_list|)
decl_stmt|;
name|PolicyAttachment
name|pa
init|=
operator|new
name|PolicyAttachment
argument_list|()
decl_stmt|;
name|pa
operator|.
name|setDomainExpressions
argument_list|(
name|des
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|si1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|si2
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
name|assertTrue
argument_list|(
operator|!
name|pa
operator|.
name|appliesTo
argument_list|(
name|si1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pa
operator|.
name|appliesTo
argument_list|(
name|si2
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
name|testAppliesToEndpoint
parameter_list|()
block|{
name|EndpointInfo
name|ei1
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei2
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|DomainExpression
name|de
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpression
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DomainExpression
argument_list|>
name|des
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|de
argument_list|)
decl_stmt|;
name|PolicyAttachment
name|pa
init|=
operator|new
name|PolicyAttachment
argument_list|()
decl_stmt|;
name|pa
operator|.
name|setDomainExpressions
argument_list|(
name|des
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|ei1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|ei2
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
name|assertTrue
argument_list|(
operator|!
name|pa
operator|.
name|appliesTo
argument_list|(
name|ei1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pa
operator|.
name|appliesTo
argument_list|(
name|ei2
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
name|testAppliesToOperation
parameter_list|()
block|{
name|BindingOperationInfo
name|boi1
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|boi2
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|DomainExpression
name|de
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpression
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DomainExpression
argument_list|>
name|des
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|de
argument_list|)
decl_stmt|;
name|PolicyAttachment
name|pa
init|=
operator|new
name|PolicyAttachment
argument_list|()
decl_stmt|;
name|pa
operator|.
name|setDomainExpressions
argument_list|(
name|des
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|boi1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|boi2
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
name|assertTrue
argument_list|(
operator|!
name|pa
operator|.
name|appliesTo
argument_list|(
name|boi1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pa
operator|.
name|appliesTo
argument_list|(
name|boi2
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
name|testAppliesToMessage
parameter_list|()
block|{
name|BindingMessageInfo
name|bmi1
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|bmi2
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|DomainExpression
name|de
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpression
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DomainExpression
argument_list|>
name|des
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|de
argument_list|)
decl_stmt|;
name|PolicyAttachment
name|pa
init|=
operator|new
name|PolicyAttachment
argument_list|()
decl_stmt|;
name|pa
operator|.
name|setDomainExpressions
argument_list|(
name|des
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|bmi1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|bmi2
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
name|assertTrue
argument_list|(
operator|!
name|pa
operator|.
name|appliesTo
argument_list|(
name|bmi1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pa
operator|.
name|appliesTo
argument_list|(
name|bmi2
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
name|testAppliesToFault
parameter_list|()
block|{
name|BindingFaultInfo
name|bfi1
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingFaultInfo
name|bfi2
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|DomainExpression
name|de
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DomainExpression
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DomainExpression
argument_list|>
name|des
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|de
argument_list|)
decl_stmt|;
name|PolicyAttachment
name|pa
init|=
operator|new
name|PolicyAttachment
argument_list|()
decl_stmt|;
name|pa
operator|.
name|setDomainExpressions
argument_list|(
name|des
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|bfi1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|de
operator|.
name|appliesTo
argument_list|(
name|bfi2
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
name|assertTrue
argument_list|(
operator|!
name|pa
operator|.
name|appliesTo
argument_list|(
name|bfi1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pa
operator|.
name|appliesTo
argument_list|(
name|bfi2
argument_list|)
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

