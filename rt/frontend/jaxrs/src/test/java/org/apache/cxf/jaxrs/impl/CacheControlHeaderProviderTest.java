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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|InternalServerErrorException
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
name|CacheControl
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|CacheControlHeaderProviderTest
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testValueOfNull
parameter_list|()
block|{
name|CacheControl
operator|.
name|valueOf
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromSimpleString
parameter_list|()
block|{
name|CacheControl
name|c
init|=
name|CacheControl
operator|.
name|valueOf
argument_list|(
literal|"public,must-revalidate"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|isPrivate
argument_list|()
operator|&&
operator|!
name|c
operator|.
name|isNoStore
argument_list|()
operator|&&
name|c
operator|.
name|isMustRevalidate
argument_list|()
operator|&&
operator|!
name|c
operator|.
name|isProxyRevalidate
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|isNoCache
argument_list|()
operator|&&
operator|!
name|c
operator|.
name|isNoTransform
argument_list|()
operator|&&
name|c
operator|.
name|getNoCacheFields
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|c
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFromComplexString
parameter_list|()
block|{
name|CacheControl
name|c
init|=
name|CacheControl
operator|.
name|valueOf
argument_list|(
literal|"private=\"foo\",no-cache=\"bar\",no-store,no-transform,"
operator|+
literal|"must-revalidate,proxy-revalidate,max-age=2,s-maxage=3"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isPrivate
argument_list|()
operator|&&
name|c
operator|.
name|isNoStore
argument_list|()
operator|&&
name|c
operator|.
name|isMustRevalidate
argument_list|()
operator|&&
name|c
operator|.
name|isProxyRevalidate
argument_list|()
operator|&&
name|c
operator|.
name|isNoCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isNoTransform
argument_list|()
operator|&&
name|c
operator|.
name|getNoCacheFields
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|c
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|c
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|c
operator|.
name|getNoCacheFields
argument_list|()
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
name|testFromComplexStringWithSemicolon
parameter_list|()
block|{
name|CacheControlHeaderProvider
name|cp
init|=
operator|new
name|CacheControlHeaderProvider
argument_list|()
block|{
specifier|protected
name|Message
name|getCurrentMessage
parameter_list|()
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
name|CacheControlHeaderProvider
operator|.
name|CACHE_CONTROL_SEPARATOR_PROPERTY
argument_list|,
literal|";"
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
decl_stmt|;
name|CacheControl
name|c
init|=
name|cp
operator|.
name|fromString
argument_list|(
literal|"private=\"foo\";no-cache=\"bar\";no-store;no-transform;"
operator|+
literal|"must-revalidate;proxy-revalidate;max-age=2;s-maxage=3"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isPrivate
argument_list|()
operator|&&
name|c
operator|.
name|isNoStore
argument_list|()
operator|&&
name|c
operator|.
name|isMustRevalidate
argument_list|()
operator|&&
name|c
operator|.
name|isProxyRevalidate
argument_list|()
operator|&&
name|c
operator|.
name|isNoCache
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isNoTransform
argument_list|()
operator|&&
name|c
operator|.
name|getNoCacheFields
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|c
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|c
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|c
operator|.
name|getNoCacheFields
argument_list|()
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
argument_list|(
name|expected
operator|=
name|InternalServerErrorException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInvalidSeparator
parameter_list|()
block|{
name|CacheControlHeaderProvider
name|cp
init|=
operator|new
name|CacheControlHeaderProvider
argument_list|()
block|{
specifier|protected
name|Message
name|getCurrentMessage
parameter_list|()
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
name|CacheControlHeaderProvider
operator|.
name|CACHE_CONTROL_SEPARATOR_PROPERTY
argument_list|,
literal|"(e+)+"
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
decl_stmt|;
name|cp
operator|.
name|fromString
argument_list|(
literal|"no-store"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|String
name|s
init|=
literal|"private=\"foo\",no-cache=\"bar\",no-store,no-transform,"
operator|+
literal|"must-revalidate,proxy-revalidate,max-age=2,s-maxage=3"
decl_stmt|;
name|String
name|parsed
init|=
name|CacheControl
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|s
argument_list|,
name|parsed
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoCacheEnabled
parameter_list|()
block|{
name|CacheControl
name|cc
init|=
operator|new
name|CacheControl
argument_list|()
decl_stmt|;
name|cc
operator|.
name|setNoCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"no-cache,no-transform"
argument_list|,
name|cc
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
name|testNoCacheDisabled
parameter_list|()
block|{
name|CacheControl
name|cc
init|=
operator|new
name|CacheControl
argument_list|()
decl_stmt|;
name|cc
operator|.
name|setNoCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"no-transform"
argument_list|,
name|cc
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
name|testMultiplePrivateFields
parameter_list|()
block|{
name|CacheControl
name|cc
init|=
operator|new
name|CacheControl
argument_list|()
decl_stmt|;
name|cc
operator|.
name|setPrivate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"private=\"a,b\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleNoCacheFields
parameter_list|()
block|{
name|CacheControl
name|cc
init|=
operator|new
name|CacheControl
argument_list|()
decl_stmt|;
name|cc
operator|.
name|setNoCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getNoCacheFields
argument_list|()
operator|.
name|add
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getNoCacheFields
argument_list|()
operator|.
name|add
argument_list|(
literal|"d"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"no-cache=\"c,d\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadMultiplePrivateAndNoCacheFields
parameter_list|()
block|{
name|String
name|s
init|=
literal|"private=\"foo1,foo2\",no-store,no-transform,"
operator|+
literal|"must-revalidate,proxy-revalidate,max-age=2,s-maxage=3,no-cache=\"bar1,bar2\","
operator|+
literal|"ext=1"
decl_stmt|;
name|CacheControl
name|cc
init|=
name|CacheControl
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|privateFields
init|=
name|cc
operator|.
name|getPrivateFields
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|privateFields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo1"
argument_list|,
name|privateFields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo2"
argument_list|,
name|privateFields
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|isNoCache
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|noCacheFields
init|=
name|cc
operator|.
name|getNoCacheFields
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|noCacheFields
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar1"
argument_list|,
name|noCacheFields
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar2"
argument_list|,
name|noCacheFields
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|isNoStore
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|isNoTransform
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|isMustRevalidate
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cc
operator|.
name|isProxyRevalidate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cc
operator|.
name|getMaxAge
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|cc
operator|.
name|getSMaxAge
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|exts
init|=
name|cc
operator|.
name|getCacheExtension
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|exts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exts
operator|.
name|get
argument_list|(
literal|"ext"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCacheExtensionToString
parameter_list|()
block|{
name|CacheControl
name|cc
init|=
operator|new
name|CacheControl
argument_list|()
decl_stmt|;
name|cc
operator|.
name|getCacheExtension
argument_list|()
operator|.
name|put
argument_list|(
literal|"ext1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getCacheExtension
argument_list|()
operator|.
name|put
argument_list|(
literal|"ext2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|cc
operator|.
name|getCacheExtension
argument_list|()
operator|.
name|put
argument_list|(
literal|"ext3"
argument_list|,
literal|"value 3"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|cc
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|value
operator|.
name|indexOf
argument_list|(
literal|"ext1"
argument_list|)
operator|!=
operator|-
literal|1
operator|&&
name|value
operator|.
name|indexOf
argument_list|(
literal|"ext1="
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|value
operator|.
name|indexOf
argument_list|(
literal|"ext2=value2"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|value
operator|.
name|indexOf
argument_list|(
literal|"ext3=\"value 3\""
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

