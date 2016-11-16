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
name|jaxrs
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|AsyncContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|AsyncResponse
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
name|continuations
operator|.
name|ContinuationProvider
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
name|http
operator|.
name|Servlet3ContinuationProvider
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

begin_class
specifier|public
class|class
name|AsyncResponseImplTest
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
block|}
comment|/**      * According to the spec, subsequent calls to cancel the same AsyncResponse should      * have the same behavior as the first call.      */
annotation|@
name|Test
specifier|public
name|void
name|testCancelBehavesTheSameWhenInvokedMultipleTimes
parameter_list|()
block|{
name|HttpServletRequest
name|req
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|resp
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|AsyncContext
name|asyncCtx
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AsyncContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|Servlet3ContinuationProvider
argument_list|(
name|req
argument_list|,
name|resp
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|req
operator|.
name|startAsync
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|asyncCtx
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|AsyncResponse
name|impl
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// cancel the AsyncResponse for the first time
name|assertTrue
argument_list|(
literal|"Unexpectedly returned false when canceling the first time"
argument_list|,
name|impl
operator|.
name|cancel
argument_list|()
argument_list|)
expr_stmt|;
comment|// check the state of the AsyncResponse
name|assertTrue
argument_list|(
literal|"AsyncResponse was canceled but is reporting that it was not canceled"
argument_list|,
name|impl
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|isDone
init|=
name|impl
operator|.
name|isDone
argument_list|()
decl_stmt|;
name|boolean
name|isSuspended
init|=
name|impl
operator|.
name|isSuspended
argument_list|()
decl_stmt|;
comment|// cancel the AsyncResponse a second time
name|assertTrue
argument_list|(
literal|"Unexpectedly returned false when canceling the second time"
argument_list|,
name|impl
operator|.
name|cancel
argument_list|()
argument_list|)
expr_stmt|;
comment|// verify that the state is the same as before the second cancel
name|assertTrue
argument_list|(
literal|"AsyncResponse was canceled (twice) but is reporting that it was not canceled"
argument_list|,
name|impl
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsynchResponse.isDone() returned a different response after canceling a second time"
argument_list|,
name|isDone
argument_list|,
name|impl
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsynchResponse.isSuspended() returned a different response after canceling a second time"
argument_list|,
name|isSuspended
argument_list|,
name|impl
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Similar to testCancelBehavesTheSameWhenInvokedMultipleTimes, but using the cancel(int) signature.      */
annotation|@
name|Test
specifier|public
name|void
name|testCancelIntBehavesTheSameWhenInvokedMultipleTimes
parameter_list|()
block|{
name|HttpServletRequest
name|req
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|resp
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|AsyncContext
name|asyncCtx
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AsyncContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|Servlet3ContinuationProvider
argument_list|(
name|req
argument_list|,
name|resp
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|req
operator|.
name|startAsync
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|asyncCtx
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|AsyncResponse
name|impl
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// cancel the AsyncResponse for the first time
name|assertTrue
argument_list|(
literal|"Unexpectedly returned false when canceling the first time"
argument_list|,
name|impl
operator|.
name|cancel
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the state of the AsyncResponse
name|assertTrue
argument_list|(
literal|"AsyncResponse was canceled but is reporting that it was not canceled"
argument_list|,
name|impl
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|isDone
init|=
name|impl
operator|.
name|isDone
argument_list|()
decl_stmt|;
name|boolean
name|isSuspended
init|=
name|impl
operator|.
name|isSuspended
argument_list|()
decl_stmt|;
comment|// cancel the AsyncResponse a second time
name|assertTrue
argument_list|(
literal|"Unexpectedly returned false when canceling the second time"
argument_list|,
name|impl
operator|.
name|cancel
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
comment|// verify that the state is the same as before the second cancel
name|assertTrue
argument_list|(
literal|"AsyncResponse was canceled (twice) but is reporting that it was not canceled"
argument_list|,
name|impl
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsynchResponse.isDone() returned a different response after canceling a second time"
argument_list|,
name|isDone
argument_list|,
name|impl
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsynchResponse.isSuspended() returned a different response after canceling a second time"
argument_list|,
name|isSuspended
argument_list|,
name|impl
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Similar to testCancelBehavesTheSameWhenInvokedMultipleTimes, but using the cancel(Date) signature.      */
annotation|@
name|Test
specifier|public
name|void
name|testCancelDateBehavesTheSameWhenInvokedMultipleTimes
parameter_list|()
block|{
name|HttpServletRequest
name|req
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|resp
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|AsyncContext
name|asyncCtx
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AsyncContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|Servlet3ContinuationProvider
argument_list|(
name|req
argument_list|,
name|resp
argument_list|,
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|req
operator|.
name|startAsync
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|asyncCtx
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|AsyncResponse
name|impl
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// cancel the AsyncResponse for the first time
name|Date
name|d
init|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|60000
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpectedly returned false when canceling the first time"
argument_list|,
name|impl
operator|.
name|cancel
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
comment|// check the state of the AsyncResponse
name|assertTrue
argument_list|(
literal|"AsyncResponse was canceled but is reporting that it was not canceled"
argument_list|,
name|impl
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|isDone
init|=
name|impl
operator|.
name|isDone
argument_list|()
decl_stmt|;
name|boolean
name|isSuspended
init|=
name|impl
operator|.
name|isSuspended
argument_list|()
decl_stmt|;
comment|// cancel the AsyncResponse a second time
name|d
operator|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
literal|120000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpectedly returned false when canceling the second time"
argument_list|,
name|impl
operator|.
name|cancel
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
comment|// verify that the state is the same as before the second cancel
name|assertTrue
argument_list|(
literal|"AsyncResponse was canceled (twice) but is reporting that it was not canceled"
argument_list|,
name|impl
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsynchResponse.isDone() returned a different response after canceling a second time"
argument_list|,
name|isDone
argument_list|,
name|impl
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AsynchResponse.isSuspended() returned a different response after canceling a second time"
argument_list|,
name|isSuspended
argument_list|,
name|impl
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

