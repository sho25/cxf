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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|ConfigurationFactory
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|DiskStoreConfiguration
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
name|Bus
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
name|BusFactory
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
name|CastUtils
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
name|ResourceUtils
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|Client
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|ServerAccessToken
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|UserSubject
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|tokens
operator|.
name|refresh
operator|.
name|RefreshToken
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|utils
operator|.
name|EHCacheUtil
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultEHCacheOAuthDataProvider
extends|extends
name|AbstractOAuthDataProvider
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_CACHE_KEY
init|=
literal|"cxf.oauth2.client.cache"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_TOKEN_CACHE_KEY
init|=
literal|"cxf.oauth2.accesstoken.cache"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REFRESH_TOKEN_CACHE_KEY
init|=
literal|"cxf.oauth2.refreshtoken.cache"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CONFIG_URL
init|=
literal|"cxf-oauth2-ehcache.xml"
decl_stmt|;
specifier|protected
name|CacheManager
name|cacheManager
decl_stmt|;
specifier|private
name|Ehcache
name|clientCache
decl_stmt|;
specifier|private
name|Ehcache
name|accessTokenCache
decl_stmt|;
specifier|private
name|Ehcache
name|refreshTokenCache
decl_stmt|;
specifier|public
name|DefaultEHCacheOAuthDataProvider
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_CONFIG_URL
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultEHCacheOAuthDataProvider
parameter_list|(
name|String
name|configFileURL
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|this
argument_list|(
name|configFileURL
argument_list|,
name|bus
argument_list|,
name|CLIENT_CACHE_KEY
argument_list|,
name|ACCESS_TOKEN_CACHE_KEY
argument_list|,
name|REFRESH_TOKEN_CACHE_KEY
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultEHCacheOAuthDataProvider
parameter_list|(
name|String
name|configFileURL
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|String
name|clientCacheKey
parameter_list|,
name|String
name|accessTokenKey
parameter_list|,
name|String
name|refreshTokenKey
parameter_list|)
block|{
name|createCaches
argument_list|(
name|configFileURL
argument_list|,
name|bus
argument_list|,
name|clientCacheKey
argument_list|,
name|accessTokenKey
argument_list|,
name|refreshTokenKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getCacheValue
argument_list|(
name|clientCache
argument_list|,
name|clientId
argument_list|,
name|Client
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|putCacheValue
argument_list|(
name|clientCache
argument_list|,
name|client
operator|.
name|getClientId
argument_list|()
argument_list|,
name|client
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRemoveClient
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
name|clientCache
operator|.
name|remove
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Client
argument_list|>
name|getClients
parameter_list|(
name|UserSubject
name|resourceOwner
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|clientCache
operator|.
name|getKeys
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Client
argument_list|>
name|clients
init|=
operator|new
name|ArrayList
argument_list|<
name|Client
argument_list|>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|Client
name|c
init|=
name|getClient
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|isClientMatched
argument_list|(
name|c
argument_list|,
name|resourceOwner
argument_list|)
condition|)
block|{
name|clients
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|clients
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
name|getAccessTokens
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|sub
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|accessTokenCache
operator|.
name|getKeys
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
name|tokens
init|=
operator|new
name|ArrayList
argument_list|<
name|ServerAccessToken
argument_list|>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|ServerAccessToken
name|token
init|=
name|getAccessToken
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTokenMatched
argument_list|(
name|token
argument_list|,
name|c
argument_list|,
name|sub
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tokens
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RefreshToken
argument_list|>
name|getRefreshTokens
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|sub
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|refreshTokenCache
operator|.
name|getKeys
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RefreshToken
argument_list|>
name|tokens
init|=
operator|new
name|ArrayList
argument_list|<
name|RefreshToken
argument_list|>
argument_list|(
name|keys
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|RefreshToken
name|token
init|=
name|getRefreshToken
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTokenMatched
argument_list|(
name|token
argument_list|,
name|c
argument_list|,
name|sub
argument_list|)
condition|)
block|{
name|tokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tokens
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getCacheValue
argument_list|(
name|accessTokenCache
argument_list|,
name|accessToken
argument_list|,
name|ServerAccessToken
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeAccessToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|accessTokenCache
operator|.
name|remove
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|RefreshToken
name|getRefreshToken
parameter_list|(
name|String
name|refreshTokenKey
parameter_list|)
block|{
return|return
name|getCacheValue
argument_list|(
name|refreshTokenCache
argument_list|,
name|refreshTokenKey
argument_list|,
name|RefreshToken
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeRefreshToken
parameter_list|(
name|RefreshToken
name|rt
parameter_list|)
block|{
name|refreshTokenCache
operator|.
name|remove
argument_list|(
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveAccessToken
parameter_list|(
name|ServerAccessToken
name|serverToken
parameter_list|)
block|{
name|putCacheValue
argument_list|(
name|accessTokenCache
argument_list|,
name|serverToken
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|serverToken
argument_list|,
name|serverToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveRefreshToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|,
name|RefreshToken
name|refreshToken
parameter_list|)
block|{
name|putCacheValue
argument_list|(
name|refreshTokenCache
argument_list|,
name|refreshToken
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|refreshToken
argument_list|,
name|refreshToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getCacheValue
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|String
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|Element
name|e
init|=
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|e
operator|.
name|getObjectValue
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
specifier|static
name|void
name|putCacheValue
parameter_list|(
name|Ehcache
name|cache
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|,
name|long
name|ttl
parameter_list|)
block|{
name|Element
name|element
init|=
operator|new
name|Element
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|int
name|parsedTTL
init|=
operator|(
name|int
operator|)
name|ttl
decl_stmt|;
if|if
condition|(
name|ttl
operator|!=
operator|(
name|long
operator|)
name|parsedTTL
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Requested time to live can not be supported"
argument_list|)
throw|;
block|}
name|element
operator|.
name|setTimeToLive
argument_list|(
name|parsedTTL
argument_list|)
expr_stmt|;
name|element
operator|.
name|setTimeToIdle
argument_list|(
name|parsedTTL
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|CacheManager
name|createCacheManager
parameter_list|(
name|String
name|configFile
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|URL
name|configFileURL
init|=
literal|null
decl_stmt|;
try|try
block|{
name|configFileURL
operator|=
name|ResourceUtils
operator|.
name|getClasspathResourceURL
argument_list|(
name|configFile
argument_list|,
name|DefaultEHCacheOAuthDataProvider
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|CacheManager
name|cacheManager
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|configFileURL
operator|==
literal|null
condition|)
block|{
name|cacheManager
operator|=
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Configuration
name|conf
init|=
name|ConfigurationFactory
operator|.
name|parseConfiguration
argument_list|(
name|configFileURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|conf
operator|.
name|setName
argument_list|(
name|bus
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|DiskStoreConfiguration
name|dsc
init|=
name|conf
operator|.
name|getDiskStoreConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|dsc
operator|!=
literal|null
operator|&&
literal|"java.io.tmpdir"
operator|.
name|equals
argument_list|(
name|dsc
operator|.
name|getOriginalPath
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|conf
operator|.
name|getDiskStoreConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
operator|+
name|File
operator|.
name|separator
operator|+
name|bus
operator|.
name|getId
argument_list|()
decl_stmt|;
name|conf
operator|.
name|getDiskStoreConfiguration
argument_list|()
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
name|cacheManager
operator|=
name|EHCacheUtil
operator|.
name|createCacheManager
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
return|return
name|cacheManager
return|;
block|}
specifier|protected
specifier|static
name|Ehcache
name|createCache
parameter_list|(
name|CacheManager
name|cacheManager
parameter_list|,
name|String
name|cacheKey
parameter_list|)
block|{
name|CacheConfiguration
name|clientCC
init|=
name|EHCacheUtil
operator|.
name|getCacheConfiguration
argument_list|(
name|cacheKey
argument_list|,
name|cacheManager
argument_list|)
decl_stmt|;
return|return
name|cacheManager
operator|.
name|addCacheIfAbsent
argument_list|(
operator|new
name|Cache
argument_list|(
name|clientCC
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|void
name|createCaches
parameter_list|(
name|String
name|configFile
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|String
name|clientCacheKey
parameter_list|,
name|String
name|accessTokenKey
parameter_list|,
name|String
name|refreshTokenKey
parameter_list|)
block|{
name|cacheManager
operator|=
name|createCacheManager
argument_list|(
name|configFile
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|clientCache
operator|=
name|createCache
argument_list|(
name|cacheManager
argument_list|,
name|clientCacheKey
argument_list|)
expr_stmt|;
name|accessTokenCache
operator|=
name|createCache
argument_list|(
name|cacheManager
argument_list|,
name|accessTokenKey
argument_list|)
expr_stmt|;
name|refreshTokenCache
operator|=
name|createCache
argument_list|(
name|cacheManager
argument_list|,
name|refreshTokenKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|cacheManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

