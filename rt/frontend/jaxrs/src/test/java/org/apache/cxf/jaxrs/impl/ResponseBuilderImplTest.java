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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|NewCookie
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
name|Response
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ResponseBuilderImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testLanguage
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Content-Language"
argument_list|,
literal|"de"
argument_list|)
expr_stmt|;
name|checkBuild
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|language
argument_list|(
literal|"de"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLanguageReplace
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Content-Language"
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|checkBuild
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|language
argument_list|(
literal|"de"
argument_list|)
operator|.
name|language
argument_list|(
operator|(
name|Locale
operator|)
literal|null
argument_list|)
operator|.
name|language
argument_list|(
literal|"en"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddHeader
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Content-Language"
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|checkBuild
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
literal|"de"
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
literal|null
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_LANGUAGE
argument_list|,
literal|"en"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddCookie
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"Set-Cookie"
argument_list|,
literal|"a=b"
argument_list|)
expr_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"Set-Cookie"
argument_list|,
literal|"c=d"
argument_list|)
expr_stmt|;
name|checkBuild
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|cookie
argument_list|(
operator|new
name|NewCookie
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
operator|.
name|cookie
argument_list|(
operator|new
name|NewCookie
argument_list|(
literal|"c"
argument_list|,
literal|"d"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExpires
parameter_list|()
throws|throws
name|Exception
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Expires"
argument_list|,
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
expr_stmt|;
name|SimpleDateFormat
name|format
init|=
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
decl_stmt|;
name|Date
name|date
init|=
name|format
operator|.
name|parse
argument_list|(
literal|"Tue, 21 Oct 2008 17:00:00 GMT"
argument_list|)
decl_stmt|;
name|checkBuild
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|expires
argument_list|(
name|date
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOkBuild
parameter_list|()
block|{
name|checkBuild
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
argument_list|,
literal|200
argument_list|,
literal|null
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreatedNoEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Location"
argument_list|,
literal|"http://foo"
argument_list|)
expr_stmt|;
name|checkBuild
argument_list|(
name|Response
operator|.
name|created
argument_list|(
operator|new
name|URI
argument_list|(
literal|"http://foo"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
literal|201
argument_list|,
literal|null
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkBuild
parameter_list|(
name|Response
name|r
parameter_list|,
name|int
name|status
parameter_list|,
name|Object
name|entity
parameter_list|,
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|meta
parameter_list|)
block|{
name|ResponseImpl
name|ri
init|=
operator|(
name|ResponseImpl
operator|)
name|r
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong status"
argument_list|,
name|ri
operator|.
name|getStatus
argument_list|()
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Wrong entity"
argument_list|,
name|ri
operator|.
name|getEntity
argument_list|()
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong meta"
argument_list|,
name|ri
operator|.
name|getMetadata
argument_list|()
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

