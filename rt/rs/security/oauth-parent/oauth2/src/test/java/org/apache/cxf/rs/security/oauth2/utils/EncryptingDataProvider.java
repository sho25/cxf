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
name|utils
package|;
end_package

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
name|HashMap
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
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
name|AccessTokenRegistration
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
name|OAuthPermission
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
name|provider
operator|.
name|OAuthDataProvider
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
name|bearer
operator|.
name|BearerAccessToken
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

begin_class
specifier|public
class|class
name|EncryptingDataProvider
implements|implements
name|OAuthDataProvider
block|{
name|SecretKey
name|key
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|clients
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|tokens
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|refreshTokens
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|EncryptingDataProvider
parameter_list|()
throws|throws
name|Exception
block|{
name|key
operator|=
name|EncryptionUtils
operator|.
name|getSecretKey
argument_list|()
expr_stmt|;
name|String
name|encryptedClient
init|=
name|ModelEncryptionSupport
operator|.
name|encryptClient
argument_list|(
operator|new
name|Client
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|true
argument_list|)
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|clients
operator|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"1"
argument_list|,
name|encryptedClient
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
name|ModelEncryptionSupport
operator|.
name|decryptClient
argument_list|(
name|clients
operator|.
name|get
argument_list|(
name|clientId
argument_list|)
argument_list|,
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|accessTokenReg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAccessToken
name|token
init|=
name|createAccessTokenInternal
argument_list|(
name|accessTokenReg
argument_list|)
decl_stmt|;
name|encryptAccessToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessTokenKey
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|ModelEncryptionSupport
operator|.
name|decryptAccessToken
argument_list|(
name|this
argument_list|,
name|accessTokenKey
argument_list|,
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|refreshAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|String
name|encrypted
init|=
name|refreshTokens
operator|.
name|remove
argument_list|(
name|refreshToken
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|token
init|=
name|ModelEncryptionSupport
operator|.
name|decryptAccessToken
argument_list|(
name|this
argument_list|,
name|encrypted
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|remove
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
comment|// create a new refresh token
name|createRefreshToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
comment|// possibly update other token properties
name|encryptAccessToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAccessToken
parameter_list|(
name|ServerAccessToken
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|tokens
operator|.
name|remove
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|revokeToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|tokenTypeHint
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// the fast way: if it is the refresh token then there will be a matching value for it
name|String
name|accessToken
init|=
name|refreshTokens
operator|.
name|remove
argument_list|(
name|token
argument_list|)
decl_stmt|;
comment|// if no matching value then the token parameter is access token key
name|tokens
operator|.
name|remove
argument_list|(
name|accessToken
operator|==
literal|null
condition|?
name|token
else|:
name|accessToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|convertScopeToPermissions
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
comment|// assuming that no specific scopes is documented/supported
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getPreauthorizedToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|String
name|grantType
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// This is an optimization useful in cases where a client requests an authorization code:
comment|// if a user has already provided a given client with a pre-authorized token then challenging
comment|// a user with yet another form asking for the authorization is redundant
return|return
literal|null
return|;
block|}
name|BearerAccessToken
name|createAccessTokenInternal
parameter_list|(
name|AccessTokenRegistration
name|accessTokenReg
parameter_list|)
block|{
name|BearerAccessToken
name|token
init|=
operator|new
name|BearerAccessToken
argument_list|(
name|accessTokenReg
operator|.
name|getClient
argument_list|()
argument_list|,
literal|3600L
argument_list|)
decl_stmt|;
name|token
operator|.
name|setSubject
argument_list|(
name|accessTokenReg
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|createRefreshToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|token
operator|.
name|setGrantType
argument_list|(
name|accessTokenReg
operator|.
name|getGrantType
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setAudience
argument_list|(
name|accessTokenReg
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"param"
argument_list|,
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
name|token
operator|.
name|setScopes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|OAuthPermission
argument_list|(
literal|"read"
argument_list|,
literal|"read permission"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
specifier|private
name|void
name|encryptAccessToken
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|)
block|{
name|String
name|encryptedToken
init|=
name|ModelEncryptionSupport
operator|.
name|encryptAccessToken
argument_list|(
name|token
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|add
argument_list|(
name|encryptedToken
argument_list|)
expr_stmt|;
name|refreshTokens
operator|.
name|put
argument_list|(
name|token
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|encryptedToken
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenKey
argument_list|(
name|encryptedToken
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createRefreshToken
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|)
block|{
name|RefreshToken
name|refreshToken
init|=
operator|new
name|RefreshToken
argument_list|(
name|token
operator|.
name|getClient
argument_list|()
argument_list|,
literal|"refresh"
argument_list|,
literal|1200L
argument_list|,
name|OAuthUtils
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|encryptedRefreshToken
init|=
name|ModelEncryptionSupport
operator|.
name|encryptRefreshToken
argument_list|(
name|refreshToken
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|token
operator|.
name|setRefreshToken
argument_list|(
name|encryptedRefreshToken
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

