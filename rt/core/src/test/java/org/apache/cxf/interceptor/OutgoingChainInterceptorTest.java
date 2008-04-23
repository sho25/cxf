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
name|interceptor
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
name|binding
operator|.
name|Binding
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
name|ExchangeImpl
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
name|MessageImpl
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
name|phase
operator|.
name|Phase
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
name|phase
operator|.
name|PhaseManager
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
name|phase
operator|.
name|PhaseManagerImpl
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
name|Service
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
name|MessageInfo
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
name|OperationInfo
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
name|junit
operator|.
name|After
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

begin_class
specifier|public
class|class
name|OutgoingChainInterceptorTest
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
name|Service
name|service
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
name|BindingOperationInfo
name|bopInfo
decl_stmt|;
specifier|private
name|OperationInfo
name|opInfo
decl_stmt|;
specifier|private
name|BindingMessageInfo
name|bmInfo
decl_stmt|;
specifier|private
name|MessageInfo
name|mInfo
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Phase
argument_list|>
name|phases
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|>
name|empty
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|phases
operator|=
operator|new
name|ArrayList
argument_list|<
name|Phase
argument_list|>
argument_list|()
expr_stmt|;
name|phases
operator|.
name|add
argument_list|(
operator|new
name|Phase
argument_list|(
name|Phase
operator|.
name|SEND
argument_list|,
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|empty
operator|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|>
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
name|PhaseManager
name|pm
init|=
operator|new
name|PhaseManagerImpl
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|pm
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|service
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|binding
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Binding
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|MessageImpl
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|binding
operator|.
name|createMessage
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|service
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpoint
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|service
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|bopInfo
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
name|opInfo
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|mInfo
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|bmInfo
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingMessageInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bopInfo
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|opInfo
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|opInfo
operator|.
name|getOutput
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|mInfo
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|opInfo
operator|.
name|isOneWay
argument_list|()
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
name|bopInfo
operator|.
name|getOutput
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bmInfo
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
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
name|testInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|OutgoingChainInterceptor
name|intc
init|=
operator|new
name|OutgoingChainInterceptor
argument_list|()
decl_stmt|;
name|MessageImpl
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bopInfo
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|intc
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

