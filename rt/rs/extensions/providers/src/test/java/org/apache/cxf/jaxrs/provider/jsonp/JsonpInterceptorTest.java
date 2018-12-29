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
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|assertEquals
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
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
name|JsonpInInterceptor
operator|.
name|CALLBACK_PARAM
operator|+
literal|"="
operator|+
literal|"myCallback"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|bos
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
name|assertEquals
argument_list|(
literal|"myCallback();"
argument_list|,
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonWithPaddingCustomCallbackParam
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|QUERY_STRING
argument_list|,
literal|"_customjsonp=myCallback"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|bos
argument_list|)
expr_stmt|;
comment|// Process the message
try|try
block|{
name|in
operator|.
name|setCallbackParam
argument_list|(
literal|"_customjsonp"
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"myCallback();"
argument_list|,
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|setCallbackParam
argument_list|(
literal|"_jsonp"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsonWithDefaultPadding
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|,
name|JsonpInInterceptor
operator|.
name|JSONP_TYPE
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|bos
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
name|assertEquals
argument_list|(
literal|"callback();"
argument_list|,
name|bos
operator|.
name|toString
argument_list|()
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
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|bos
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
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|bos
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

