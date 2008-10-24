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
name|classextension
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
name|assertEquals
argument_list|(
name|hs
operator|.
name|getFirst
argument_list|(
literal|"Accept"
argument_list|)
argument_list|,
literal|"text/*"
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
name|assertEquals
argument_list|(
operator|new
name|Locale
argument_list|(
literal|"en"
argument_list|)
argument_list|,
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
literal|"en-gb"
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
literal|"text/*"
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
block|}
end_class

end_unit

