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
name|ws
operator|.
name|security
operator|.
name|tokenstore
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_class
specifier|public
class|class
name|EHCacheTokenStoreTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|private
specifier|static
name|TokenStore
name|store
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
name|TokenStoreFactory
name|tokenStoreFactory
init|=
operator|new
name|EHCacheTokenStoreFactory
argument_list|()
decl_stmt|;
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
name|SecurityConstants
operator|.
name|CACHE_CONFIG_FILE
argument_list|,
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
literal|"cxf-ehcache.xml"
argument_list|,
name|EHCacheTokenStoreTest
operator|.
name|class
argument_list|)
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
name|store
operator|=
name|tokenStoreFactory
operator|.
name|newTokenStore
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
comment|// tests TokenStore apis for storing in the cache.
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testTokenAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
argument_list|,
name|store
operator|.
name|getToken
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|getToken
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|newKey
init|=
literal|"xyz"
decl_stmt|;
name|store
operator|.
name|add
argument_list|(
name|newKey
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|getToken
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
argument_list|,
name|store
operator|.
name|getToken
argument_list|(
name|newKey
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|newKey
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|getToken
argument_list|(
name|newKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// tests TokenStore apis for storing in the cache with various expiration times
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testTokenAddExpiration
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityToken
name|expiredToken
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"expiredToken"
argument_list|)
decl_stmt|;
name|Date
name|currentDate
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|long
name|currentTime
init|=
name|currentDate
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|Date
name|expiry
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|expiry
operator|.
name|setTime
argument_list|(
name|currentTime
operator|-
literal|5000L
argument_list|)
expr_stmt|;
name|expiredToken
operator|.
name|setExpires
argument_list|(
name|expiry
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|expiredToken
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|SecurityToken
name|farFutureToken
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"farFuture"
argument_list|)
decl_stmt|;
name|expiry
operator|=
operator|new
name|Date
argument_list|()
expr_stmt|;
name|expiry
operator|.
name|setTime
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|farFutureToken
operator|.
name|setExpires
argument_list|(
name|expiry
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|farFutureToken
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|farFutureToken
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// tests TokenStore apis for removing from the cache.
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testTokenRemove
parameter_list|()
block|{
name|SecurityToken
name|token1
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"token1"
argument_list|)
decl_stmt|;
name|SecurityToken
name|token2
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"token2"
argument_list|)
decl_stmt|;
name|SecurityToken
name|token3
init|=
operator|new
name|SecurityToken
argument_list|(
literal|"token3"
argument_list|)
decl_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token1
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token2
argument_list|)
expr_stmt|;
name|store
operator|.
name|add
argument_list|(
name|token3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|3
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token3
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|store
operator|.
name|getToken
argument_list|(
literal|"test3"
argument_list|)
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|token2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|getTokenIdentifiers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

