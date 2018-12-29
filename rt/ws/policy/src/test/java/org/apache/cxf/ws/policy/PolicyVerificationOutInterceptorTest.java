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
name|message
operator|.
name|Message
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
name|PolicyVerificationOutInterceptorTest
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
name|PolicyVerificationOutInterceptor
name|interceptor
init|=
name|EasyMock
operator|.
name|createMockBuilder
argument_list|(
name|PolicyVerificationOutInterceptor
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
name|Message
operator|.
name|PARTIAL_RESPONSE_MESSAGE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
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
name|PARTIAL_RESPONSE_MESSAGE
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
name|PARTIAL_RESPONSE_MESSAGE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AssertionInfoMap
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
name|EffectivePolicy
name|ep
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
name|message
operator|.
name|get
argument_list|(
name|EffectivePolicy
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|getPolicy
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|aim
operator|.
name|checkEffectivePolicy
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
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
block|}
block|}
end_class

end_unit

