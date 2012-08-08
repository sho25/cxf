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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|Locale
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Cookie
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
name|HttpHeaders
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|jaxrs
operator|.
name|utils
operator|.
name|HttpUtils
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
name|HttpHeadersImplTest
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
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderNameValue
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeader
argument_list|(
literal|"COMPLEX_HEADER"
argument_list|,
literal|"b=c; param=c, a=b;param=b"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"COMPLEX_HEADER"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b=c; param=c"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a=b;param=b"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderWithQuotes1
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeader
argument_list|(
literal|"COMPLEX_HEADER"
argument_list|,
literal|"a1=\"a\", a2=\"a\";param, b, b;param, c1=\"c, d, e\", "
operator|+
literal|"c2=\"c, d, e\";param, a=b, a=b;p=p1, a2=\"a\";param=p,"
operator|+
literal|"a3=\"a\";param=\"p,b\""
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"COMPLEX_HEADER"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a1=\"a\""
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a2=\"a\";param"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b;param"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c1=\"c, d, e\""
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c2=\"c, d, e\";param"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a=b"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a=b;p=p1"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a2=\"a\";param=p"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a3=\"a\";param=\"p,b\""
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderWithQuotes2
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeader
argument_list|(
literal|"X-WSSE"
argument_list|,
literal|"UsernameToken Username=\"Foo\", Nonce=\"bar\""
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"X-WSSE"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UsernameToken"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Username=\"Foo\""
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Nonce=\"bar\""
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderWithQuotes3
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeader
argument_list|(
literal|"COMPLEX_HEADER"
argument_list|,
literal|"\"value with space\""
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"COMPLEX_HEADER"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value with space"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hs
init|=
name|h
operator|.
name|getRequestHeaders
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acceptValues
init|=
name|hs
operator|.
name|get
argument_list|(
literal|"Accept"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|acceptValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/bar;q=0.6"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/*;q=1"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|hs
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMediaType
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
literal|"*/*"
argument_list|)
argument_list|,
name|h
operator|.
name|getMediaType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMissingContentLegth
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|h
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetContentLegth
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|h
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDate
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|dateValues
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"Date"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dateValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|,
name|dateValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Date
name|d
init|=
name|h
operator|.
name|getDate
argument_list|()
decl_stmt|;
name|String
name|theDateValue
init|=
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
operator|.
name|format
argument_list|(
name|d
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|theDateValue
argument_list|,
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderString
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|String
name|date
init|=
name|h
operator|.
name|getHeaderString
argument_list|(
literal|"Date"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHeaderString2
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|String
name|date
init|=
name|h
operator|.
name|getHeaderString
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1,2"
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetMediaTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MediaType
argument_list|>
name|acceptValues
init|=
name|h
operator|.
name|getAcceptableMediaTypes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|acceptValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/*;q=1"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/bar;q=0.6"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|2
argument_list|)
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
name|testGetHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acceptValues
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"Accept"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|acceptValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/bar;q=0.6"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/*;q=1"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|acceptValues
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|contentValues
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|contentValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"*/*"
argument_list|,
name|contentValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|dateValues
init|=
name|h
operator|.
name|getRequestHeader
argument_list|(
literal|"Date"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dateValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|,
name|dateValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNullLanguage
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|createHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|h
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLanguage
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
literal|"en-US"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"en_US"
argument_list|,
name|h
operator|.
name|getLanguage
argument_list|()
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
name|testSingleAcceptableLanguages
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Locale
argument_list|>
name|languages
init|=
name|h
operator|.
name|getAcceptableLanguages
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|languages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
name|languages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCookies
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|,
literal|"a=b;c=d"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|cookies
init|=
name|h
operator|.
name|getCookies
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cookies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|cookies
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|cookies
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCookieWithAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|,
literal|"$Version=1;a=b"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|cookies
init|=
name|h
operator|.
name|getCookies
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cookies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Cookie
name|cookie
init|=
name|cookies
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|cookie
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cookie
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCookiesWithComma
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.http.cookie.separator"
argument_list|,
literal|","
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|COOKIE
argument_list|,
literal|"a=b,c=d"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Cookie
argument_list|>
name|cookies
init|=
name|h
operator|.
name|getCookies
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cookies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b"
argument_list|,
name|cookies
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"d"
argument_list|,
name|cookies
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleAcceptableLanguages
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeader
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en;q=0.7, en-gb;q=0.8, da"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Locale
argument_list|>
name|languages
init|=
name|h
operator|.
name|getAcceptableLanguages
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|languages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"da"
argument_list|)
argument_list|,
name|languages
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|,
literal|"GB"
argument_list|)
argument_list|,
name|languages
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
name|languages
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createHeaders
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hs
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|hs
operator|.
name|putSingle
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/bar;q=0.6,text/*;q=1,application/xml"
argument_list|)
expr_stmt|;
name|hs
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"*/*"
argument_list|)
expr_stmt|;
name|hs
operator|.
name|putSingle
argument_list|(
literal|"Date"
argument_list|,
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
expr_stmt|;
name|hs
operator|.
name|putSingle
argument_list|(
literal|"Content-Length"
argument_list|,
literal|"10"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"2"
argument_list|)
expr_stmt|;
name|hs
operator|.
name|addAll
argument_list|(
literal|"a"
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|hs
return|;
block|}
specifier|private
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
modifier|...
name|values
parameter_list|)
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hs
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
name|hs
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|list
argument_list|)
expr_stmt|;
return|return
name|hs
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnmodifiableRequestHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|m
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
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|createHeader
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|,
literal|"en;q=0.7, en-gb;q=0.8, da"
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|HttpHeaders
name|h
init|=
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Locale
argument_list|>
name|languages
init|=
name|h
operator|.
name|getAcceptableLanguages
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|languages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|languages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|languages
operator|=
name|h
operator|.
name|getAcceptableLanguages
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|languages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rHeaders
init|=
name|h
operator|.
name|getRequestHeaders
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|acceptL
init|=
name|rHeaders
operator|.
name|get
argument_list|(
name|HttpHeaders
operator|.
name|ACCEPT_LANGUAGE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|acceptL
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|rHeaders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

