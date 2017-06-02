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
name|grants
operator|.
name|code
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
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
name|provider
operator|.
name|JCacheOAuthDataProvider
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
name|provider
operator|.
name|OAuthServiceException
import|;
end_import

begin_class
specifier|public
class|class
name|JCacheCodeDataProvider
extends|extends
name|JCacheOAuthDataProvider
implements|implements
name|AuthorizationCodeDataProvider
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CODE_GRANT_CACHE_KEY
init|=
literal|"cxf.oauth2.codegrant.cache"
decl_stmt|;
specifier|private
name|long
name|codeLifetime
init|=
literal|10
operator|*
literal|60
decl_stmt|;
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|ServerAuthorizationCodeGrant
argument_list|>
name|grantCache
decl_stmt|;
specifier|protected
name|JCacheCodeDataProvider
parameter_list|()
throws|throws
name|Exception
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
specifier|protected
name|JCacheCodeDataProvider
parameter_list|(
name|String
name|configFileURL
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|this
argument_list|(
name|configFileURL
argument_list|,
name|bus
argument_list|,
name|CLIENT_CACHE_KEY
argument_list|,
name|CODE_GRANT_CACHE_KEY
argument_list|,
name|ACCESS_TOKEN_CACHE_KEY
argument_list|,
name|REFRESH_TOKEN_CACHE_KEY
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JCacheCodeDataProvider
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
name|codeCacheKey
parameter_list|,
name|String
name|accessTokenKey
parameter_list|,
name|String
name|refreshTokenKey
parameter_list|)
throws|throws
name|Exception
block|{
name|super
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
name|grantCache
operator|=
name|createCache
argument_list|(
name|cacheManager
argument_list|,
name|codeCacheKey
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|ServerAuthorizationCodeGrant
operator|.
name|class
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
for|for
control|(
name|ServerAuthorizationCodeGrant
name|grant
range|:
name|getCodeGrants
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
control|)
block|{
name|removeCodeGrant
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doRemoveClient
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|createCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|AbstractCodeDataProvider
operator|.
name|initCodeGrant
argument_list|(
name|reg
argument_list|,
name|codeLifetime
argument_list|)
decl_stmt|;
name|grantCache
operator|.
name|put
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|,
name|grant
argument_list|)
expr_stmt|;
return|return
name|grant
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|getCodeGrants
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|sub
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|toRemove
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|grants
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Cache
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ServerAuthorizationCodeGrant
argument_list|>
argument_list|>
name|it
init|=
name|grantCache
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Cache
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ServerAuthorizationCodeGrant
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|isExpired
argument_list|(
name|grant
argument_list|)
condition|)
block|{
name|toRemove
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|AbstractCodeDataProvider
operator|.
name|isCodeMatched
argument_list|(
name|grant
argument_list|,
name|c
argument_list|,
name|sub
argument_list|)
condition|)
block|{
name|grants
operator|.
name|add
argument_list|(
name|grant
argument_list|)
expr_stmt|;
block|}
block|}
name|grantCache
operator|.
name|removeAll
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
return|return
name|grants
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|removeCodeGrant
parameter_list|(
name|String
name|code
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|getCodeGrant
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|grant
operator|!=
literal|null
condition|)
block|{
name|grantCache
operator|.
name|remove
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
return|return
name|grant
return|;
block|}
specifier|public
name|void
name|setCodeLifetime
parameter_list|(
name|long
name|codeLifetime
parameter_list|)
block|{
name|this
operator|.
name|codeLifetime
operator|=
name|codeLifetime
expr_stmt|;
block|}
specifier|protected
name|ServerAuthorizationCodeGrant
name|getCodeGrant
parameter_list|(
name|String
name|code
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|grantCache
operator|.
name|get
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|grant
operator|!=
literal|null
operator|&&
name|isExpired
argument_list|(
name|grant
argument_list|)
condition|)
block|{
name|grantCache
operator|.
name|remove
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|grant
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|grant
return|;
block|}
specifier|protected
specifier|static
name|boolean
name|isExpired
parameter_list|(
name|ServerAuthorizationCodeGrant
name|grant
parameter_list|)
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
operator|(
name|grant
operator|.
name|getIssuedAt
argument_list|()
operator|+
name|grant
operator|.
name|getExpiresIn
argument_list|()
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|grantCache
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

