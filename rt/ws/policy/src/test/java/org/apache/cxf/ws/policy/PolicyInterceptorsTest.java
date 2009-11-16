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
name|endpoint
operator|.
name|Endpoint
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
name|interceptor
operator|.
name|InterceptorChain
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
name|Exchange
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
name|FaultInfo
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
name|PolicyInterceptorsTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|Exchange
name|exchange
decl_stmt|;
specifier|private
name|BindingOperationInfo
name|boi
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
name|EndpointInfo
name|ei
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|PolicyEngine
name|pe
decl_stmt|;
specifier|private
name|Conduit
name|conduit
decl_stmt|;
specifier|private
name|Destination
name|destination
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
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
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
name|testClientPolicyOutInterceptor
parameter_list|()
block|{
name|PolicyOutInterceptor
name|interceptor
init|=
operator|new
name|PolicyOutInterceptor
argument_list|()
decl_stmt|;
name|doTestBasics
argument_list|(
name|interceptor
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EffectivePolicy
name|effectivePolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EffectivePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|pe
operator|.
name|getEffectiveClientRequestPolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|conduit
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|li
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterceptorChain
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
name|getInterceptorChain
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ic
argument_list|)
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|li
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
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
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getChosenAlternative
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|assertions
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|testClientPolicyInInterceptor
parameter_list|()
block|{
name|PolicyInInterceptor
name|interceptor
init|=
operator|new
name|PolicyInInterceptor
argument_list|()
decl_stmt|;
name|doTestBasics
argument_list|(
name|interceptor
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EffectivePolicy
name|effectivePolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EffectivePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|pe
operator|.
name|getEffectiveClientResponsePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
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
operator|new
name|Policy
argument_list|()
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
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
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|lst
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
name|lst
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|lst
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterceptorChain
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
name|getInterceptorChain
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ic
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isA
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|PolicyVerificationInInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|testClientPolicyInFaultInterceptor
parameter_list|()
block|{
name|ClientPolicyInFaultInterceptor
name|interceptor
init|=
operator|new
name|ClientPolicyInFaultInterceptor
argument_list|()
decl_stmt|;
name|doTestBasics
argument_list|(
name|interceptor
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
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
name|pe
operator|.
name|getClientEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|conduit
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpointPolicy
argument_list|)
expr_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getFaultInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|li
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterceptorChain
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
name|getInterceptorChain
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ic
argument_list|)
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|li
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
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
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getFaultVocabulary
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|assertions
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|testServerPolicyInInterceptor
parameter_list|()
block|{
name|PolicyInInterceptor
name|interceptor
init|=
operator|new
name|PolicyInInterceptor
argument_list|()
decl_stmt|;
name|doTestBasics
argument_list|(
name|interceptor
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EndpointPolicy
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|pe
operator|.
name|getServerEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|destination
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|endpointPolicy
argument_list|)
expr_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|li
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterceptorChain
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
name|getInterceptorChain
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ic
argument_list|)
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|li
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
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
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointPolicy
operator|.
name|getVocabulary
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|assertions
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|testServerPolicyOutInterceptor
parameter_list|()
block|{
name|PolicyOutInterceptor
name|interceptor
init|=
operator|new
name|PolicyOutInterceptor
argument_list|()
decl_stmt|;
name|doTestBasics
argument_list|(
name|interceptor
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EffectivePolicy
name|effectivePolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EffectivePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|pe
operator|.
name|getEffectiveServerResponsePolicy
argument_list|(
name|ei
argument_list|,
name|boi
argument_list|,
name|destination
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|li
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterceptorChain
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
name|getInterceptorChain
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ic
argument_list|)
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|li
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
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
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getChosenAlternative
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|assertions
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|testServerPolicyOutFaultInterceptor
parameter_list|()
throws|throws
name|NoSuchMethodException
block|{
name|Method
name|m
init|=
name|AbstractPolicyInterceptor
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getBindingFaultInfo"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Message
operator|.
name|class
block|,
name|Exception
operator|.
name|class
block|,
name|BindingOperationInfo
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|ServerPolicyOutFaultInterceptor
name|interceptor
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServerPolicyOutFaultInterceptor
operator|.
name|class
argument_list|,
operator|new
name|Method
index|[]
block|{
name|m
block|}
argument_list|)
decl_stmt|;
name|doTestBasics
argument_list|(
name|interceptor
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|reset
argument_list|()
expr_stmt|;
name|setupMessage
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|interceptor
operator|.
name|getBindingFaultInfo
argument_list|(
name|message
argument_list|,
name|ex
argument_list|,
name|boi
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
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|setupMessage
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Exception ex = control.createMock(Exception.class);
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
expr_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|interceptor
operator|.
name|getBindingFaultInfo
argument_list|(
name|message
argument_list|,
name|ex
argument_list|,
name|boi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bfi
argument_list|)
expr_stmt|;
name|EffectivePolicy
name|effectivePolicy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EffectivePolicyImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|pe
operator|.
name|getEffectiveServerFaultPolicy
argument_list|(
name|ei
argument_list|,
name|bfi
argument_list|,
name|destination
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|li
argument_list|)
expr_stmt|;
name|InterceptorChain
name|ic
init|=
name|control
operator|.
name|createMock
argument_list|(
name|InterceptorChain
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
name|getInterceptorChain
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ic
argument_list|)
expr_stmt|;
name|ic
operator|.
name|add
argument_list|(
name|li
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
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
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|effectivePolicy
operator|.
name|getChosenAlternative
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|assertions
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|testServerPolicyOutFaultInterceptorGetBindingFaultInfo
parameter_list|()
block|{
name|ServerPolicyOutFaultInterceptor
name|interceptor
init|=
operator|new
name|ServerPolicyOutFaultInterceptor
argument_list|()
decl_stmt|;
name|message
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
operator|new
name|UnsupportedOperationException
argument_list|(
operator|new
name|RuntimeException
argument_list|()
argument_list|)
decl_stmt|;
name|boi
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingOperationInfo
operator|.
name|class
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
name|BindingFaultInfo
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
name|Collection
argument_list|<
name|BindingFaultInfo
argument_list|>
name|bfis
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|boi
operator|.
name|getFaults
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bfis
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|wrappedBoi
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
name|boi
operator|.
name|getWrappedOperation
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|wrappedBoi
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|BindingFaultInfo
argument_list|>
name|wrappedBfis
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|bfi
argument_list|)
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|wrappedBoi
operator|.
name|getFaults
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|wrappedBfis
argument_list|)
expr_stmt|;
name|FaultInfo
name|fi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|FaultInfo
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
name|getFaultInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|fi
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|RuntimeException
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|,
name|bfi
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
name|assertSame
argument_list|(
name|bfi
argument_list|,
name|interceptor
operator|.
name|getBindingFaultInfo
argument_list|(
name|message
argument_list|,
name|ex
argument_list|,
name|boi
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
name|doTestBasics
parameter_list|(
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|interceptor
parameter_list|,
name|boolean
name|isClient
parameter_list|,
name|boolean
name|usesOperationInfo
parameter_list|)
block|{
name|setupMessage
argument_list|(
operator|!
name|isClient
argument_list|,
name|isClient
argument_list|,
name|usesOperationInfo
argument_list|,
operator|!
name|usesOperationInfo
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|setupMessage
argument_list|(
name|isClient
argument_list|,
name|isClient
argument_list|,
name|usesOperationInfo
argument_list|,
operator|!
name|usesOperationInfo
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|setupMessage
argument_list|(
name|isClient
argument_list|,
name|isClient
argument_list|,
name|usesOperationInfo
argument_list|,
name|usesOperationInfo
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
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
name|setupMessage
argument_list|(
name|isClient
argument_list|,
name|isClient
argument_list|,
name|usesOperationInfo
argument_list|,
name|usesOperationInfo
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
name|void
name|setupMessage
parameter_list|(
name|boolean
name|setupRequestor
parameter_list|,
name|boolean
name|isClient
parameter_list|,
name|boolean
name|usesOperationInfo
parameter_list|,
name|boolean
name|setupOperation
parameter_list|,
name|Boolean
name|setupEndpoint
parameter_list|,
name|Boolean
name|setupEngine
parameter_list|)
block|{
name|message
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
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
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|isClient
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
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
if|if
condition|(
name|usesOperationInfo
condition|)
block|{
if|if
condition|(
literal|null
operator|==
name|boi
operator|&&
name|setupOperation
condition|)
block|{
name|boi
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|setupOperation
condition|?
name|boi
else|:
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setupOperation
condition|)
block|{
return|return;
block|}
block|}
if|if
condition|(
literal|null
operator|==
name|endpoint
operator|&&
name|setupEndpoint
condition|)
block|{
name|endpoint
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|setupEndpoint
condition|?
name|endpoint
else|:
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setupEndpoint
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|null
operator|==
name|ei
condition|)
block|{
name|ei
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ei
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|pe
operator|&&
name|setupEngine
condition|)
block|{
name|pe
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|setupEngine
condition|?
name|pe
else|:
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setupEngine
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isClient
condition|)
block|{
name|conduit
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Conduit
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getConduit
argument_list|(
name|message
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|conduit
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|destination
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Destination
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
operator|.
name|getDestination
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

