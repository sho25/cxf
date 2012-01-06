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
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|Bus
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
name|interceptor
operator|.
name|Interceptor
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|Destination
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|EffectivePolicyImplTest
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
operator|new
name|Integer
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|createMockInterceptorList
parameter_list|()
block|{
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Interceptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|m
init|=
name|i
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|a
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|a
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|a
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAccessors
parameter_list|()
block|{
name|EffectivePolicyImpl
name|effectivePolicy
init|=
operator|new
name|EffectivePolicyImpl
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|effectivePolicy
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|effectivePolicy
operator|.
name|getChosenAlternative
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|effectivePolicy
operator|.
name|getInterceptors
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
name|Assertion
name|a
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Assertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Assertion
argument_list|>
name|la
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|li
init|=
name|createMockInterceptorList
argument_list|()
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|setPolicy
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p
argument_list|,
name|effectivePolicy
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|effectivePolicy
operator|.
name|setChosenAlternative
argument_list|(
name|la
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|la
argument_list|,
name|effectivePolicy
operator|.
name|getChosenAlternative
argument_list|()
argument_list|)
expr_stmt|;
name|effectivePolicy
operator|.
name|setInterceptors
argument_list|(
name|li
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|li
argument_list|,
name|effectivePolicy
operator|.
name|getInterceptors
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
name|testInitialiseFromEndpointPolicy
parameter_list|()
throws|throws
name|NoSuchMethodException
block|{
name|Method
name|m
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"initialiseInterceptors"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|PolicyEngineImpl
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|EffectivePolicyImpl
name|effectivePolicy
init|=
name|EasyMock
operator|.
name|createMockBuilder
argument_list|(
name|EffectivePolicyImpl
operator|.
name|class
argument_list|)
operator|.
name|addMockedMethod
argument_list|(
name|m
argument_list|)
operator|.
name|createMock
argument_list|(
name|control
argument_list|)
decl_stmt|;
name|EndpointPolicyImpl
name|endpointPolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointPolicyImpl
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getPolicy
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|chosenAlternative
init|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getChosenAlternative
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|chosenAlternative
argument_list|)
expr_stmt|;
name|PolicyEngineImpl
name|pe
init|=
operator|new
name|PolicyEngineImpl
argument_list|()
decl_stmt|;
name|effectivePolicy
operator|.
name|initialiseInterceptors
argument_list|(
name|pe
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|initialise
argument_list|(
name|endpointPolicy
argument_list|,
name|pe
argument_list|,
literal|false
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
name|testInitialise
parameter_list|()
throws|throws
name|NoSuchMethodException
block|{
name|Method
name|m1
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"initialisePolicy"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|EndpointInfo
operator|.
name|class
block|,
name|BindingOperationInfo
operator|.
name|class
block|,
name|PolicyEngineImpl
operator|.
name|class
block|,
name|boolean
operator|.
name|class
block|,
name|boolean
operator|.
name|class
block|,
name|Assertor
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Method
name|m2
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"chooseAlternative"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|PolicyEngineImpl
operator|.
name|class
block|,
name|Assertor
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Method
name|m3
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"initialiseInterceptors"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|PolicyEngineImpl
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|EffectivePolicyImpl
name|effectivePolicy
init|=
name|EasyMock
operator|.
name|createMockBuilder
argument_list|(
name|EffectivePolicyImpl
operator|.
name|class
argument_list|)
operator|.
name|addMockedMethods
argument_list|(
name|m1
argument_list|,
name|m2
argument_list|,
name|m3
argument_list|)
operator|.
name|createMock
argument_list|(
name|control
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei
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
name|BindingOperationInfo
name|boi
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
name|PolicyEngineImpl
name|pe
init|=
operator|new
name|PolicyEngineImpl
argument_list|()
decl_stmt|;
name|Assertor
name|a
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
name|boolean
name|requestor
init|=
literal|true
decl_stmt|;
name|effectivePolicy
operator|.
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|pe
argument_list|,
name|requestor
argument_list|,
name|requestor
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|effectivePolicy
operator|.
name|chooseAlternative
argument_list|(
name|pe
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|initialiseInterceptors
argument_list|(
name|pe
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|initialise
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|pe
argument_list|,
name|a
argument_list|,
name|requestor
argument_list|,
name|requestor
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
name|testInitialiseFault
parameter_list|()
throws|throws
name|NoSuchMethodException
block|{
name|Method
name|m1
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"initialisePolicy"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|EndpointInfo
operator|.
name|class
block|,
name|BindingOperationInfo
operator|.
name|class
block|,
name|BindingFaultInfo
operator|.
name|class
block|,
name|PolicyEngineImpl
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Method
name|m2
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"chooseAlternative"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|PolicyEngineImpl
operator|.
name|class
block|,
name|Assertor
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|Method
name|m3
init|=
name|EffectivePolicyImpl
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"initialiseInterceptors"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|PolicyEngineImpl
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|EffectivePolicyImpl
name|effectivePolicy
init|=
name|EasyMock
operator|.
name|createMockBuilder
argument_list|(
name|EffectivePolicyImpl
operator|.
name|class
argument_list|)
operator|.
name|addMockedMethods
argument_list|(
name|m1
argument_list|,
name|m2
argument_list|,
name|m3
argument_list|)
operator|.
name|createMock
argument_list|(
name|control
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei
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
name|BindingFaultInfo
name|bfi
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
name|PolicyEngineImpl
name|pe
init|=
operator|new
name|PolicyEngineImpl
argument_list|()
decl_stmt|;
name|Assertor
name|a
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
name|effectivePolicy
operator|.
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
literal|null
argument_list|,
name|bfi
argument_list|,
name|pe
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|chooseAlternative
argument_list|(
name|pe
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|initialiseInterceptors
argument_list|(
name|pe
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|effectivePolicy
operator|.
name|initialise
argument_list|(
name|ei
argument_list|,
literal|null
argument_list|,
name|bfi
argument_list|,
name|pe
argument_list|,
name|a
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
name|testInitialiseClientPolicy
parameter_list|()
block|{
name|doTestInitialisePolicy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitialiseServerPolicy
parameter_list|()
block|{
name|doTestInitialisePolicy
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestInitialisePolicy
parameter_list|(
name|boolean
name|requestor
parameter_list|)
block|{
name|EndpointInfo
name|ei
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
name|BindingOperationInfo
name|boi
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
name|PolicyEngineImpl
name|engine
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngineImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|bmi
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
if|if
condition|(
name|requestor
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|boi
operator|.
name|getInput
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bmi
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|boi
operator|.
name|getOutput
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bmi
argument_list|)
expr_stmt|;
block|}
name|EndpointPolicy
name|effectivePolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestor
condition|)
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getClientEndpointPolicy
argument_list|(
name|ei
argument_list|,
operator|(
name|Conduit
operator|)
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
operator|(
name|Destination
operator|)
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
block|}
name|Policy
name|ep
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getPolicy
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|Policy
name|op
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getAggregatedOperationPolicy
argument_list|(
name|boi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|Policy
name|merged
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|merge
argument_list|(
name|op
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|merged
argument_list|)
expr_stmt|;
name|Policy
name|mp
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getAggregatedMessagePolicy
argument_list|(
name|bmi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|mp
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|merged
operator|.
name|merge
argument_list|(
name|mp
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|merged
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|merged
operator|.
name|normalize
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|merged
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|EffectivePolicyImpl
name|epi
init|=
operator|new
name|EffectivePolicyImpl
argument_list|()
decl_stmt|;
name|epi
operator|.
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|engine
argument_list|,
name|requestor
argument_list|,
name|requestor
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|merged
argument_list|,
name|epi
operator|.
name|getPolicy
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
name|testInitialiseServerFaultPolicy
parameter_list|()
block|{
name|EndpointInfo
name|ei
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
name|BindingFaultInfo
name|bfi
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
name|PolicyEngineImpl
name|engine
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngineImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|boi
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|bfi
operator|.
name|getBindingOperation
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EndpointPolicy
name|endpointPolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
operator|(
name|Destination
operator|)
literal|null
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpointPolicy
argument_list|)
expr_stmt|;
name|Policy
name|ep
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getPolicy
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|Policy
name|op
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getAggregatedOperationPolicy
argument_list|(
name|boi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|Policy
name|merged
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|merge
argument_list|(
name|op
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|merged
argument_list|)
expr_stmt|;
name|Policy
name|fp
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getAggregatedFaultPolicy
argument_list|(
name|bfi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fp
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|merged
operator|.
name|merge
argument_list|(
name|fp
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|merged
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|merged
operator|.
name|normalize
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|merged
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|EffectivePolicyImpl
name|epi
init|=
operator|new
name|EffectivePolicyImpl
argument_list|()
decl_stmt|;
name|epi
operator|.
name|initialisePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|bfi
argument_list|,
name|engine
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|merged
argument_list|,
name|epi
operator|.
name|getPolicy
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
name|testChooseAlternative
parameter_list|()
block|{
name|EffectivePolicyImpl
name|epi
init|=
operator|new
name|EffectivePolicyImpl
argument_list|()
decl_stmt|;
name|Policy
name|policy
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|epi
operator|.
name|setPolicy
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|PolicyEngineImpl
name|engine
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngineImpl
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
name|AlternativeSelector
name|selector
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AlternativeSelector
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getAlternativeSelector
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|selector
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
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
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|epi
operator|.
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
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
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getAlternativeSelector
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|selector
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
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
argument_list|)
operator|.
name|andReturn
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|epi
operator|.
name|chooseAlternative
argument_list|(
name|engine
argument_list|,
name|assertor
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|choice
init|=
name|epi
operator|.
name|getChosenAlternative
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|choice
argument_list|,
name|alternative
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testInitialiseOutInterceptors
parameter_list|()
block|{
name|EffectivePolicyImpl
name|epi
init|=
operator|new
name|EffectivePolicyImpl
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Assertion
argument_list|>
name|alternative
init|=
operator|new
name|ArrayList
argument_list|<
name|Assertion
argument_list|>
argument_list|()
decl_stmt|;
name|epi
operator|.
name|setChosenAlternative
argument_list|(
name|alternative
argument_list|)
expr_stmt|;
name|PolicyEngineImpl
name|engine
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngineImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|PolicyInterceptorProviderRegistry
name|reg
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
name|setupPolicyInterceptorProviderRegistry
argument_list|(
name|engine
argument_list|,
name|reg
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|epi
operator|.
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|epi
operator|.
name|getInterceptors
argument_list|()
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
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupPolicyInterceptorProviderRegistry
argument_list|(
name|engine
argument_list|,
name|reg
argument_list|)
expr_stmt|;
name|PolicyAssertion
name|a
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
name|alternative
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|epi
operator|.
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|epi
operator|.
name|getInterceptors
argument_list|()
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
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupPolicyInterceptorProviderRegistry
argument_list|(
name|engine
argument_list|,
name|reg
argument_list|)
expr_stmt|;
name|QName
name|qn
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
name|a
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|qn
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|reg
operator|.
name|get
argument_list|(
name|qn
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|epi
operator|.
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|epi
operator|.
name|getInterceptors
argument_list|()
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
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupPolicyInterceptorProviderRegistry
argument_list|(
name|engine
argument_list|,
name|reg
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|a
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|qn
argument_list|)
expr_stmt|;
name|PolicyInterceptorProvider
name|pp
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyInterceptorProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|reg
operator|.
name|get
argument_list|(
name|qn
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|pp
argument_list|)
expr_stmt|;
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|pi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Interceptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|m
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
name|pi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|pp
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|epi
operator|.
name|initialiseInterceptors
argument_list|(
name|engine
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|epi
operator|.
name|getInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|pi
argument_list|,
name|epi
operator|.
name|getInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|setupPolicyInterceptorProviderRegistry
parameter_list|(
name|PolicyEngineImpl
name|engine
parameter_list|,
name|PolicyInterceptorProviderRegistry
name|reg
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|engine
operator|.
name|getBus
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bus
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|reg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

