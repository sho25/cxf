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
package|;
end_package

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
name|CacheControlHeaderProviderTest
extends|extends
name|Assert
block|{
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
name|parse
argument_list|(
literal|"public;must-revalidate"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isPublic
argument_list|()
operator|&&
operator|!
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
name|assertTrue
argument_list|(
operator|!
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
name|size
argument_list|()
operator|==
literal|0
operator|&&
name|c
operator|.
name|getPrivateFields
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
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
name|parse
argument_list|(
literal|"private=\"foo\";no-cache=\"bar\";no-store;no-transform;"
operator|+
literal|"must-revalidate;proxy-revalidate;max-age=2;s-maxage=3"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|c
operator|.
name|isPublic
argument_list|()
operator|&&
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
name|testToString
parameter_list|()
block|{
name|String
name|s
init|=
literal|"private=\"foo\";no-cache=\"bar\";no-store;no-transform;"
operator|+
literal|"must-revalidate;proxy-revalidate;max-age=2;s-maxage=3"
decl_stmt|;
name|String
name|parsed
init|=
name|CacheControl
operator|.
name|parse
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
block|}
end_class

end_unit

