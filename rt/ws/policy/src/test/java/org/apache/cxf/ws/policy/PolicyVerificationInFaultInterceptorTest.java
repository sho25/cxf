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
name|PolicyVerificationInFaultInterceptorTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Bus
name|bus
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
name|BindingFaultInfo
name|bfi
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
name|PolicyEngine
name|engine
decl_stmt|;
specifier|private
name|AssertionInfoMap
name|aim
decl_stmt|;
specifier|private
name|Exception
name|ex
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
name|Test
specifier|public
name|void
name|testHandleMessage
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
literal|"getTransportAssertions"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Message
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|PolicyVerificationInFaultInterceptor
name|interceptor
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyVerificationInFaultInterceptor
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
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
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
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|false
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
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
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
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
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
name|interceptor
operator|.
name|getTransportAssertions
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EffectivePolicyImpl
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
name|engine
operator|.
name|getEffectiveClientFaultPolicy
argument_list|(
name|ei
argument_list|,
name|bfi
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|effectivePolicy
argument_list|)
expr_stmt|;
name|Policy
name|policy
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
name|policy
argument_list|)
expr_stmt|;
name|aim
operator|.
name|checkEffectivePolicy
argument_list|(
name|policy
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
name|requestor
parameter_list|,
name|boolean
name|setupOperationInfo
parameter_list|,
name|boolean
name|setupEndpoint
parameter_list|,
name|boolean
name|setupPolicyEngine
parameter_list|,
name|boolean
name|setupAssertionInfoMap
parameter_list|,
name|boolean
name|setupBindingFaultInfo
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|message
condition|)
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
block|}
if|if
condition|(
name|setupAssertionInfoMap
operator|&&
literal|null
operator|==
name|aim
condition|)
block|{
name|aim
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|aim
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setupAssertionInfoMap
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|null
operator|==
name|exchange
condition|)
block|{
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
block|}
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
name|requestor
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|requestor
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
name|setupOperationInfo
operator|&&
literal|null
operator|==
name|boi
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
name|boi
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setupOperationInfo
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|setupEndpoint
operator|&&
literal|null
operator|==
name|endpoint
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
name|endpoint
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
expr_stmt|;
if|if
condition|(
name|setupPolicyEngine
operator|&&
literal|null
operator|==
name|engine
condition|)
block|{
name|engine
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
name|engine
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|setupPolicyEngine
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|null
operator|==
name|ex
condition|)
block|{
name|ex
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
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
if|if
condition|(
name|setupBindingFaultInfo
operator|&&
literal|null
operator|==
name|bfi
condition|)
block|{
name|bfi
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|bfi
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

