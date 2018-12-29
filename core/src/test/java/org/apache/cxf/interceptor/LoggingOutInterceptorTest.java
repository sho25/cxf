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
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNull
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|public
class|class
name|LoggingOutInterceptorTest
block|{
specifier|protected
name|IMocksControl
name|control
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
name|testFormatting
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
name|p
init|=
operator|new
name|LoggingOutInterceptor
argument_list|(
name|pw
argument_list|)
decl_stmt|;
comment|//p.setPrettyLogging(true);
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"<today><is><the><twenty><second><of><january><two><thousand><and><nine></nine> "
operator|+
literal|"</and></thousand></two></january></of></second></twenty></the></is></today>"
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
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
name|Logger
name|logger
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
operator|.
name|LoggingCallback
name|l
init|=
name|p
operator|.
expr|new
name|LoggingCallback
argument_list|(
name|logger
argument_list|,
name|message
argument_list|,
name|cos
argument_list|)
decl_stmt|;
name|l
operator|.
name|onClose
argument_list|(
name|cos
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//format has changed
name|assertFalse
argument_list|(
name|str
operator|.
name|matches
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|contains
argument_list|(
literal|"<today>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrettyLoggingWithoutEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
name|p
init|=
operator|new
name|LoggingOutInterceptor
argument_list|(
name|pw
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPrettyLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"<today><is><the><twenty><second><of><january><two><thousand><and><nine></nine> "
operator|+
literal|"</and></thousand></two></january></of></second></twenty></the></is></today>"
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
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
name|Logger
name|logger
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
operator|.
name|LoggingCallback
name|l
init|=
name|p
operator|.
expr|new
name|LoggingCallback
argument_list|(
name|logger
argument_list|,
name|message
argument_list|,
name|cos
argument_list|)
decl_stmt|;
name|l
operator|.
name|onClose
argument_list|(
name|cos
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//format has changed
name|assertFalse
argument_list|(
name|str
operator|.
name|matches
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|contains
argument_list|(
literal|"<today>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrettyLoggingWithEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
name|p
init|=
operator|new
name|LoggingOutInterceptor
argument_list|(
name|pw
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPrettyLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"<today><is><the><twenty><second><of><january><two><thousand><and><nine></nine> "
operator|+
literal|"</and></thousand></two></january></of></second></twenty></the></is></today>"
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
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
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|Logger
name|logger
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
operator|.
name|LoggingCallback
name|l
init|=
name|p
operator|.
expr|new
name|LoggingCallback
argument_list|(
name|logger
argument_list|,
name|message
argument_list|,
name|cos
argument_list|)
decl_stmt|;
name|l
operator|.
name|onClose
argument_list|(
name|cos
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|//format has changed
name|assertFalse
argument_list|(
name|str
operator|.
name|matches
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|contains
argument_list|(
literal|"<today>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFormattingOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|// create a custom logging interceptor that overrides how formatting is done
name|LoggingOutInterceptor
name|p
init|=
operator|new
name|CustomFormatLoggingOutInterceptor
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|baos
argument_list|)
argument_list|)
decl_stmt|;
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|String
name|s
init|=
literal|"<today><is><the><twenty><second><of><january><two><thousand><and><nine></nine> "
operator|+
literal|"</and></thousand></two></january></of></second></twenty></the></is></today>"
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|s
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
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
name|Logger
name|logger
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|LoggingOutInterceptor
operator|.
name|LoggingCallback
name|l
init|=
name|p
operator|.
expr|new
name|LoggingCallback
argument_list|(
name|logger
argument_list|,
name|message
argument_list|,
name|cos
argument_list|)
decl_stmt|;
name|l
operator|.
name|onClose
argument_list|(
name|cos
argument_list|)
expr_stmt|;
name|String
name|str
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|contains
argument_list|(
literal|"<tomorrow/>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFormattingOverrideLogWriter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create a custom logging interceptor that overrides how formatting is done
name|LoggingOutInterceptor
name|p
init|=
operator|new
name|CustomFormatLoggingOutInterceptor
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|p
operator|.
name|setPrintWriter
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|baos
argument_list|)
argument_list|)
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
name|EndpointInfo
name|endpointInfo
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|endpointInfo
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
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
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
name|p
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Writer
name|w
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|)
decl_stmt|;
name|w
operator|.
name|close
argument_list|()
expr_stmt|;
name|String
name|str
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|str
operator|.
name|contains
argument_list|(
literal|"<tomorrow/>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCachedOutputStreamThreshold
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|mex
init|=
literal|"<test><threshold/></test>"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|LoggingOutInterceptor
name|p
init|=
operator|new
name|LoggingOutInterceptor
argument_list|()
decl_stmt|;
name|p
operator|.
name|setInMemThreshold
argument_list|(
name|mex
operator|.
name|length
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|cos
init|=
name|handleAndGetCachedOutputStream
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|cos
operator|.
name|write
argument_list|(
name|mex
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cos
operator|.
name|getTempFile
argument_list|()
argument_list|)
expr_stmt|;
name|cos
operator|.
name|write
argument_list|(
literal|"a"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cos
operator|.
name|getTempFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CachedOutputStream
name|handleAndGetCachedOutputStream
parameter_list|(
name|LoggingOutInterceptor
name|interceptor
parameter_list|)
block|{
name|interceptor
operator|.
name|setPrintWriter
argument_list|(
operator|new
name|PrintWriter
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|EndpointInfo
name|endpointInfo
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|endpointInfo
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|BindingInfo
name|bindingInfo
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bindingInfo
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|endpointInfo
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bindingInfo
operator|.
name|getProperties
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
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
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|ExchangeImpl
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|message
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
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
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
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|os
operator|instanceof
name|CachedOutputStream
argument_list|)
expr_stmt|;
return|return
operator|(
name|CachedOutputStream
operator|)
name|os
return|;
block|}
specifier|private
class|class
name|CustomFormatLoggingOutInterceptor
extends|extends
name|LoggingOutInterceptor
block|{
name|CustomFormatLoggingOutInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
name|CustomFormatLoggingOutInterceptor
parameter_list|(
name|PrintWriter
name|w
parameter_list|)
block|{
name|super
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|formatLoggingMessage
parameter_list|(
name|LoggingMessage
name|loggingMessage
parameter_list|)
block|{
name|loggingMessage
operator|.
name|getPayload
argument_list|()
operator|.
name|append
argument_list|(
literal|"<tomorrow/>"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|formatLoggingMessage
argument_list|(
name|loggingMessage
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

