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
name|transport
operator|.
name|common
operator|.
name|gzip
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|InputStream
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
name|util
operator|.
name|Collections
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|helpers
operator|.
name|HttpHeaderHelper
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
name|Fault
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|common
operator|.
name|gzip
operator|.
name|GZIPOutInterceptor
operator|.
name|UseGzip
operator|.
name|FORCE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|common
operator|.
name|gzip
operator|.
name|GZIPOutInterceptor
operator|.
name|UseGzip
operator|.
name|YES
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  * Test for the parsing of Accept-Encoding by the GZIPOutInterceptor. For  * Accept-Encoding values that enable gzip we expect an extra interceptor to be  * added to the out message, and the {@link GZIPOutInterceptor#USE_GZIP_KEY} to  * be set correctly. For Accept-Encoding values that do not enable gzip the  * interceptor should not be added.  */
end_comment

begin_class
specifier|public
class|class
name|GZIPAcceptEncodingTest
block|{
specifier|private
name|GZIPOutInterceptor
name|interceptor
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|Message
name|outMessage
decl_stmt|;
specifier|private
name|InterceptorChain
name|outInterceptors
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
name|interceptor
operator|=
operator|new
name|GZIPOutInterceptor
argument_list|()
expr_stmt|;
name|inMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|outMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|outMessage
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
name|outInterceptors
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|InterceptorChain
operator|.
name|class
argument_list|)
expr_stmt|;
name|outMessage
operator|.
name|setInterceptorChain
argument_list|(
name|outInterceptors
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoAcceptEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|outInterceptors
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcceptGzip
parameter_list|()
throws|throws
name|Exception
block|{
name|singleTest
argument_list|(
literal|"gzip"
argument_list|,
name|YES
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcceptXGzip
parameter_list|()
throws|throws
name|Exception
block|{
name|singleTest
argument_list|(
literal|"x-gzip, x-compress"
argument_list|,
name|YES
argument_list|,
literal|"x-gzip"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcceptStar
parameter_list|()
throws|throws
name|Exception
block|{
name|singleTest
argument_list|(
literal|"*"
argument_list|,
name|YES
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcceptOnlyGzip
parameter_list|()
throws|throws
name|Exception
block|{
name|singleTest
argument_list|(
literal|"gzip, identity; q=0"
argument_list|,
name|FORCE
argument_list|,
literal|"gzip"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnlyIdentitySupported
parameter_list|()
throws|throws
name|Exception
block|{
name|singleTest
argument_list|(
literal|"deflate"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGzipExplicitlyDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|singleTest
argument_list|(
literal|"gzip; q=0.00"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|Fault
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNoValidEncodings
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|replay
argument_list|()
expr_stmt|;
name|setAcceptEncoding
argument_list|(
literal|"*;q=0, deflate;q=0.5"
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|singleTest
parameter_list|(
name|String
name|encoding
parameter_list|,
name|GZIPOutInterceptor
operator|.
name|UseGzip
name|expectedUseGzip
parameter_list|,
name|String
name|expectedGzipEncoding
parameter_list|)
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|replay
argument_list|(
name|outInterceptors
argument_list|)
expr_stmt|;
name|setAcceptEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Wrong value of "
operator|+
name|GZIPOutInterceptor
operator|.
name|USE_GZIP_KEY
argument_list|,
name|expectedUseGzip
argument_list|,
name|outMessage
operator|.
name|get
argument_list|(
name|GZIPOutInterceptor
operator|.
name|USE_GZIP_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong value of "
operator|+
name|GZIPOutInterceptor
operator|.
name|GZIP_ENCODING_KEY
argument_list|,
name|expectedGzipEncoding
argument_list|,
name|outMessage
operator|.
name|get
argument_list|(
name|GZIPOutInterceptor
operator|.
name|GZIP_ENCODING_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setAcceptEncoding
parameter_list|(
name|String
name|enc
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|protocolHeaders
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
name|protocolHeaders
operator|.
name|put
argument_list|(
name|HttpHeaderHelper
operator|.
name|getHeaderKey
argument_list|(
name|HttpHeaderHelper
operator|.
name|ACCEPT_ENCODING
argument_list|)
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|enc
argument_list|)
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|protocolHeaders
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

