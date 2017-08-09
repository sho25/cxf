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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|io
operator|.
name|CachedOutputStream
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
name|IAnswer
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
class|class
name|LoggingInInterceptorTest
extends|extends
name|Assert
block|{
specifier|static
name|String
name|encoding
init|=
literal|"UTF-8"
decl_stmt|;
specifier|static
name|String
name|contentType
init|=
literal|"text/xml"
decl_stmt|;
specifier|static
name|String
name|bufferContent
init|=
literal|"<today><is><the><eighteenth><of><july><two><thousand><seventeen>"
operator|+
literal|"</seventeen></thousand></two></july></of></eighteenth></the></is></today>"
decl_stmt|;
specifier|static
name|int
name|bufferLength
init|=
name|bufferContent
operator|.
name|getBytes
argument_list|()
operator|.
name|length
decl_stmt|;
specifier|protected
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|InputStream
name|inputStream
decl_stmt|;
specifier|private
name|LoggingMessage
name|loggingMessage
decl_stmt|;
specifier|private
name|LoggingInInterceptorAncestorTester
name|classUnderTest
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
name|loggingMessage
operator|=
operator|new
name|LoggingMessage
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|sw
operator|.
name|append
argument_list|(
literal|"<today/>"
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|MessageImpl
argument_list|()
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
name|CONTENT_TYPE
argument_list|,
literal|"application/xml"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|,
name|sw
argument_list|)
expr_stmt|;
name|inputStream
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|inputStream
operator|.
name|read
argument_list|(
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|anyInt
argument_list|()
argument_list|,
name|EasyMock
operator|.
name|anyInt
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andAnswer
argument_list|(
operator|new
name|IAnswer
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
specifier|public
name|Integer
name|answer
parameter_list|()
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|bufferContent
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|EasyMock
operator|.
name|getCurrentArguments
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|0
argument_list|,
name|bufferLength
argument_list|)
expr_stmt|;
return|return
name|bufferLength
return|;
block|}
block|}
argument_list|)
operator|.
name|andStubReturn
argument_list|(
operator|-
literal|1
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
throws|throws
name|Exception
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
name|testLogInputStreamInLimit
parameter_list|()
throws|throws
name|Exception
block|{
comment|//arrange
name|classUnderTest
operator|=
operator|new
name|LoggingInInterceptorAncestorTester
argument_list|(
literal|4098
argument_list|)
expr_stmt|;
comment|//act
name|classUnderTest
operator|.
name|testLogInputStream
argument_list|(
name|message
argument_list|,
name|inputStream
argument_list|,
name|loggingMessage
argument_list|,
name|encoding
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
comment|//assert
name|assertEquals
argument_list|(
literal|"The truncated status should be set to false"
argument_list|,
literal|false
argument_list|,
name|classUnderTest
operator|.
name|isTruncated
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLogInputStreamOffLimit
parameter_list|()
throws|throws
name|Exception
block|{
comment|//arrange
name|classUnderTest
operator|=
operator|new
name|LoggingInInterceptorAncestorTester
argument_list|(
literal|16
argument_list|)
expr_stmt|;
comment|//act
name|classUnderTest
operator|.
name|testLogInputStream
argument_list|(
name|message
argument_list|,
name|inputStream
argument_list|,
name|loggingMessage
argument_list|,
name|encoding
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
comment|//assert
name|assertEquals
argument_list|(
literal|"The truncated status should be set to true"
argument_list|,
literal|true
argument_list|,
name|classUnderTest
operator|.
name|isTruncated
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|LoggingInInterceptorAncestorTester
extends|extends
name|LoggingInInterceptor
block|{
specifier|private
name|boolean
name|truncated
decl_stmt|;
name|LoggingInInterceptorAncestorTester
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|super
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTruncated
parameter_list|()
block|{
return|return
name|truncated
return|;
block|}
specifier|public
name|void
name|testLogInputStream
parameter_list|(
name|Message
name|logMessage
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|LoggingMessage
name|buffer
parameter_list|,
name|String
name|contentEncoding
parameter_list|,
name|String
name|logContentType
parameter_list|)
block|{
name|this
operator|.
name|logInputStream
argument_list|(
name|logMessage
argument_list|,
name|is
argument_list|,
name|buffer
argument_list|,
name|contentEncoding
argument_list|,
name|logContentType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|writePayload
parameter_list|(
name|StringBuilder
name|builder
parameter_list|,
name|CachedOutputStream
name|cos
parameter_list|,
name|String
name|contentEncoding
parameter_list|,
name|String
name|logContentType
parameter_list|,
name|boolean
name|truncatedStatus
parameter_list|)
block|{
name|this
operator|.
name|truncated
operator|=
name|truncatedStatus
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

