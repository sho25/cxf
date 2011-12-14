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
name|provider
operator|.
name|jsonp
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
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
name|core
operator|.
name|MediaType
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
name|easymock
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
name|JsonpInterceptorTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JSON
init|=
literal|"{}"
decl_stmt|;
name|JsonpInInterceptor
name|in
decl_stmt|;
name|JsonpPreStreamInterceptor
name|preStream
decl_stmt|;
name|JsonpPostStreamInterceptor
name|postStream
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
comment|// Create the interceptors
name|in
operator|=
operator|new
name|JsonpInInterceptor
argument_list|()
expr_stmt|;
name|preStream
operator|=
operator|new
name|JsonpPreStreamInterceptor
argument_list|()
expr_stmt|;
name|postStream
operator|=
operator|new
name|JsonpPostStreamInterceptor
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonWithPadding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// The callback value included in the request
name|String
name|callback
init|=
literal|"myCallback"
decl_stmt|;
comment|// Mock up an output stream as a strict mock. We want to verify that its
comment|// being written to correctly.
name|ServletOutputStream
name|out
init|=
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServletOutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|EasyMock
operator|.
name|anyObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// the interceptors write both "myCallback(" and ")"
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// Mock up an HTTP request
name|HttpServletRequest
name|request
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|request
operator|.
name|getParameter
argument_list|(
name|JsonpInInterceptor
operator|.
name|CALLBACK_PARAM
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|// Mock up an HTTP response
name|HttpServletResponse
name|response
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|out
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// Mock up an exchange
name|Exchange
name|exchange
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Exchange
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
name|JsonpInInterceptor
operator|.
name|CALLBACK_KEY
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|callback
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Mock up a message
name|Message
name|message
init|=
name|EasyMock
operator|.
name|createNiceMock
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
literal|"HTTP.REQUEST"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|request
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
name|get
argument_list|(
literal|"HTTP.RESPONSE"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|response
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
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
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
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Process the message
name|in
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|preStream
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|postStream
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Verify that the mock response stream was written to as expected
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonWithoutPadding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Mock up an output stream as a strict mock. We want to verify that its
comment|// being written to correctly.
name|ServletOutputStream
name|out
init|=
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
argument_list|(
name|ServletOutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// the interceptors write nothing, so we expect no behaviors from the
comment|// mock
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// Mock up an HTTP request
name|HttpServletRequest
name|request
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|request
operator|.
name|getParameter
argument_list|(
name|JsonpInInterceptor
operator|.
name|CALLBACK_PARAM
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
name|replay
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|// Mock up an HTTP response
name|HttpServletResponse
name|response
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|out
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// Mock up an exchange
name|Exchange
name|exchange
init|=
name|EasyMock
operator|.
name|createNiceMock
argument_list|(
name|Exchange
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
name|JsonpInInterceptor
operator|.
name|CALLBACK_KEY
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// Mock up a message
name|Message
name|message
init|=
name|EasyMock
operator|.
name|createNiceMock
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
literal|"HTTP.REQUEST"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|request
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
name|get
argument_list|(
literal|"HTTP.RESPONSE"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|response
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
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
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
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Process the message
name|in
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|preStream
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|postStream
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Verify that the mock response stream was written to as expected
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

