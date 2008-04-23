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
name|jaxws
operator|.
name|handler
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
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|LogicalMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|LogicalHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|LogicalMessageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|jaxws
operator|.
name|handler
operator|.
name|logical
operator|.
name|LogicalHandlerInInterceptor
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
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|types
operator|.
name|AddNumbersResponse
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

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|isA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
operator|.
name|createNiceControl
import|;
end_import

begin_class
specifier|public
class|class
name|LogicalHandlerInterceptorTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|private
name|HandlerChainInvoker
name|invoker
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|Exchange
name|exchange
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
name|createNiceControl
argument_list|()
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
name|invoker
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|HandlerChainInvoker
operator|.
name|class
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|testInterceptSuccess
parameter_list|()
block|{
name|List
argument_list|<
name|LogicalHandler
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|LogicalHandler
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|LogicalHandler
argument_list|()
block|{
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{             }
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|invoker
operator|.
name|getLogicalHandlers
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|list
argument_list|)
expr_stmt|;
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
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|message
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|message
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
name|HandlerChainInvoker
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|exchange
operator|.
name|getOutMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|invoker
operator|.
name|invokeLogicalHandlers
argument_list|(
name|eq
argument_list|(
literal|true
argument_list|)
argument_list|,
name|isA
argument_list|(
name|LogicalMessageContext
operator|.
name|class
argument_list|)
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
name|LogicalHandlerInInterceptor
argument_list|<
name|Message
argument_list|>
name|li
init|=
operator|new
name|LogicalHandlerInInterceptor
argument_list|<
name|Message
argument_list|>
argument_list|(
name|binding
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected phase"
argument_list|,
literal|"pre-protocol"
argument_list|,
name|li
operator|.
name|getPhase
argument_list|()
argument_list|)
expr_stmt|;
name|li
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
comment|//JAX-WS spec: If handler returns false, for a request-response MEP, if the message
comment|//direction is reversed during processing of a request message then the message
comment|//becomes a response message.
comment|//NOTE: commented out as this has been covered by other tests.
specifier|public
name|void
name|xtestReturnFalseClientSide
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Handler
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Handler
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|LogicalHandler
argument_list|<
name|LogicalMessageContext
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|arg0
parameter_list|)
block|{             }
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|LogicalMessageContext
name|messageContext
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|LogicalMessageContext
name|messageContext
parameter_list|)
block|{
name|LogicalMessage
name|msg
init|=
name|messageContext
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|AddNumbersResponse
name|resp
init|=
operator|new
name|AddNumbersResponse
argument_list|()
decl_stmt|;
name|resp
operator|.
name|setReturn
argument_list|(
literal|11
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setPayload
argument_list|(
name|resp
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|HandlerChainInvoker
name|invoker1
init|=
operator|new
name|HandlerChainInvoker
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|IMocksControl
name|control1
init|=
name|createNiceControl
argument_list|()
decl_stmt|;
name|Binding
name|binding1
init|=
name|control1
operator|.
name|createMock
argument_list|(
name|Binding
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange1
init|=
name|control1
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|exchange1
operator|.
name|get
argument_list|(
name|HandlerChainInvoker
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|invoker1
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|Message
name|outMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|setExchange
argument_list|(
name|exchange1
argument_list|)
expr_stmt|;
name|InterceptorChain
name|chain
init|=
name|control1
operator|.
name|createMock
argument_list|(
name|InterceptorChain
operator|.
name|class
argument_list|)
decl_stmt|;
name|outMessage
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|chain
operator|.
name|abort
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|MessageObserver
name|observer
init|=
name|control1
operator|.
name|createMock
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
decl_stmt|;
name|expect
argument_list|(
name|exchange1
operator|.
name|get
argument_list|(
name|MessageObserver
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|observer
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|isA
argument_list|(
name|Message
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
name|control1
operator|.
name|replay
argument_list|()
expr_stmt|;
name|LogicalHandlerInInterceptor
argument_list|<
name|Message
argument_list|>
name|li
init|=
operator|new
name|LogicalHandlerInInterceptor
argument_list|<
name|Message
argument_list|>
argument_list|(
name|binding1
argument_list|)
decl_stmt|;
name|li
operator|.
name|handleMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|control1
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

