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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
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
name|service
operator|.
name|ServiceImpl
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
name|invoker
operator|.
name|Invoker
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
name|assertEquals
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
name|assertNotNull
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
name|ServiceInvokerInterceptorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceInvokerInterceptor
name|intc
init|=
operator|new
name|ServiceInvokerInterceptor
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
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
expr_stmt|;
name|TestInvoker
name|i
init|=
operator|new
name|TestInvoker
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|i
argument_list|)
decl_stmt|;
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
name|Object
name|input
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|lst
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|lst
operator|.
name|add
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|lst
argument_list|)
expr_stmt|;
name|intc
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|i
operator|.
name|invoked
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|input
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Endpoint
name|createEndpoint
parameter_list|(
name|Invoker
name|i
parameter_list|)
throws|throws
name|Exception
block|{
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
operator|(
name|ServiceInfo
operator|)
literal|null
argument_list|)
decl_stmt|;
name|service
operator|.
name|setInvoker
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|service
operator|.
name|setExecutor
argument_list|(
operator|new
name|SimpleExecutor
argument_list|()
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
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
return|return
name|endpoint
return|;
block|}
specifier|static
class|class
name|TestInvoker
implements|implements
name|Invoker
block|{
name|boolean
name|invoked
decl_stmt|;
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
return|return
name|o
return|;
block|}
block|}
specifier|static
class|class
name|SimpleExecutor
implements|implements
name|Executor
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|command
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

