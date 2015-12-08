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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|OAuthConstants
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
name|OAuthUtils
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOAuthDataProvider
implements|implements
name|OAuthDataProvider
implements|,
name|ClientRegistrationProvider
block|{
specifier|private
name|long
name|accessTokenLifetime
init|=
literal|3600L
decl_stmt|;
specifier|private
name|long
name|refreshTokenLifetime
decl_stmt|;
comment|// refresh tokens are eternal by default
specifier|private
name|boolean
name|recycleRefreshTokens
init|=
literal|true
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|OAuthPermission
argument_list|>
name|permissionMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|MessageContext
name|messageContext
decl_stmt|;
specifier|protected
name|AbstractOAuthDataProvider
parameter_list|()
block|{     }
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAccessToken
name|at
init|=
name|doCreateAccessToken
argument_list|(
name|reg
argument_list|)
decl_stmt|;
name|saveAccessToken
argument_list|(
name|at
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRefreshTokenSupported
argument_list|(
name|reg
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
condition|)
block|{
name|createNewRefreshToken
argument_list|(
name|at
argument_list|)
expr_stmt|;
block|}
return|return
name|at
return|;
block|}
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|AccessTokenRegistration
name|accessToken
parameter_list|)
block|{
name|ServerAccessToken
name|at
init|=
name|createNewAccessToken
argument_list|(
name|accessToken
operator|.
name|getClient
argument_list|()
argument_list|)
decl_stmt|;
name|at
operator|.
name|setAudience
argument_list|(
name|accessToken
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|setGrantType
argument_list|(
name|accessToken
operator|.
name|getGrantType
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|theScopes
init|=
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|thePermissions
init|=
name|convertScopeToPermissions
argument_list|(
name|accessToken
operator|.
name|getClient
argument_list|()
argument_list|,
name|theScopes
argument_list|)
decl_stmt|;
name|at
operator|.
name|setScopes
argument_list|(
name|thePermissions
argument_list|)
expr_stmt|;
name|at
operator|.
name|setSubject
argument_list|(
name|accessToken
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|setClientCodeVerifier
argument_list|(
name|accessToken
operator|.
name|getClientCodeVerifier
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|at
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAccessToken
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|revokeAccessToken
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
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
name|refreshTokenKey
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|restrictedScopes
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|RefreshToken
name|currentRefreshToken
init|=
name|revokeRefreshAndAccessTokens
argument_list|(
name|client
argument_list|,
name|refreshTokenKey
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|at
init|=
name|doRefreshAccessToken
argument_list|(
name|client
argument_list|,
name|currentRefreshToken
argument_list|,
name|restrictedScopes
argument_list|)
decl_stmt|;
name|saveAccessToken
argument_list|(
name|at
argument_list|)
expr_stmt|;
if|if
condition|(
name|recycleRefreshTokens
condition|)
block|{
name|createNewRefreshToken
argument_list|(
name|at
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateRefreshToken
argument_list|(
name|currentRefreshToken
argument_list|,
name|at
argument_list|)
expr_stmt|;
block|}
return|return
name|at
return|;
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
name|tokenKey
parameter_list|,
name|String
name|tokenTypeHint
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAccessToken
name|accessToken
init|=
name|revokeAccessToken
argument_list|(
name|tokenKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessToken
operator|==
literal|null
condition|)
block|{
comment|// Revoke refresh token
name|doRevokeRefreshAndAccessTokens
argument_list|(
name|client
argument_list|,
name|tokenKey
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Revoke access token
if|if
condition|(
name|accessToken
operator|.
name|getRefreshToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|RefreshToken
name|rt
init|=
name|getRefreshToken
argument_list|(
name|client
argument_list|,
name|accessToken
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rt
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|unlinkRefreshAccessToken
argument_list|(
name|rt
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|rt
operator|.
name|getAccessTokens
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|revokeRefreshToken
argument_list|(
name|client
argument_list|,
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|saveRefreshToken
argument_list|(
literal|null
argument_list|,
name|rt
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|unlinkRefreshAccessToken
parameter_list|(
name|RefreshToken
name|rt
parameter_list|,
name|String
name|tokenKey
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|accessTokenKeys
init|=
name|rt
operator|.
name|getAccessTokens
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|accessTokenKeys
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|accessTokenKeys
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|tokenKey
argument_list|)
condition|)
block|{
name|accessTokenKeys
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|protected
name|RefreshToken
name|revokeRefreshAndAccessTokens
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenKey
parameter_list|)
block|{
return|return
name|doRevokeRefreshAndAccessTokens
argument_list|(
name|client
argument_list|,
name|tokenKey
argument_list|,
name|recycleRefreshTokens
argument_list|)
return|;
block|}
specifier|protected
name|RefreshToken
name|doRevokeRefreshAndAccessTokens
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|boolean
name|recycle
parameter_list|)
block|{
name|RefreshToken
name|currentRefreshToken
init|=
name|recycle
condition|?
name|revokeRefreshToken
argument_list|(
name|client
argument_list|,
name|tokenKey
argument_list|)
else|:
name|getRefreshToken
argument_list|(
name|client
argument_list|,
name|tokenKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentRefreshToken
operator|==
literal|null
operator|||
name|OAuthUtils
operator|.
name|isExpired
argument_list|(
name|currentRefreshToken
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|currentRefreshToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_DENIED
argument_list|)
throw|;
block|}
if|if
condition|(
name|recycle
condition|)
block|{
for|for
control|(
name|String
name|accessTokenKey
range|:
name|currentRefreshToken
operator|.
name|getAccessTokens
argument_list|()
control|)
block|{
name|revokeAccessToken
argument_list|(
name|accessTokenKey
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|currentRefreshToken
return|;
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
throws|throws
name|OAuthServiceException
block|{
if|if
condition|(
name|requestedScope
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|permissionMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|scope
range|:
name|requestedScope
control|)
block|{
name|OAuthPermission
name|permission
init|=
name|permissionMap
operator|.
name|get
argument_list|(
name|scope
argument_list|)
decl_stmt|;
if|if
condition|(
name|permission
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Unexpected scope: "
operator|+
name|scope
argument_list|)
throw|;
block|}
name|list
operator|.
name|add
argument_list|(
name|permission
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Requested scopes can not be mapped"
argument_list|)
throw|;
block|}
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
return|return
literal|null
return|;
block|}
specifier|protected
name|boolean
name|isRefreshTokenSupported
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|theScopes
parameter_list|)
block|{
return|return
name|theScopes
operator|.
name|contains
argument_list|(
name|OAuthConstants
operator|.
name|REFRESH_TOKEN_SCOPE
argument_list|)
return|;
block|}
specifier|protected
name|ServerAccessToken
name|createNewAccessToken
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
return|return
operator|new
name|BearerAccessToken
argument_list|(
name|client
argument_list|,
name|accessTokenLifetime
argument_list|)
return|;
block|}
specifier|protected
name|RefreshToken
name|updateRefreshToken
parameter_list|(
name|RefreshToken
name|rt
parameter_list|,
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|linkRefreshAccessTokens
argument_list|(
name|rt
argument_list|,
name|at
argument_list|)
expr_stmt|;
name|saveRefreshToken
argument_list|(
name|at
argument_list|,
name|rt
argument_list|)
expr_stmt|;
return|return
name|rt
return|;
block|}
specifier|protected
name|RefreshToken
name|createNewRefreshToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|RefreshToken
name|rt
init|=
name|doCreateNewRefreshToken
argument_list|(
name|at
argument_list|)
decl_stmt|;
name|saveRefreshToken
argument_list|(
name|at
argument_list|,
name|rt
argument_list|)
expr_stmt|;
return|return
name|rt
return|;
block|}
specifier|protected
name|RefreshToken
name|doCreateNewRefreshToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|RefreshToken
name|rt
init|=
operator|new
name|RefreshToken
argument_list|(
name|at
operator|.
name|getClient
argument_list|()
argument_list|,
name|refreshTokenLifetime
argument_list|)
decl_stmt|;
name|rt
operator|.
name|setAudience
argument_list|(
name|at
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|rt
operator|.
name|setGrantType
argument_list|(
name|at
operator|.
name|getGrantType
argument_list|()
argument_list|)
expr_stmt|;
name|rt
operator|.
name|setScopes
argument_list|(
name|at
operator|.
name|getScopes
argument_list|()
argument_list|)
expr_stmt|;
name|rt
operator|.
name|setSubject
argument_list|(
name|at
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|rt
operator|.
name|setClientCodeVerifier
argument_list|(
name|at
operator|.
name|getClientCodeVerifier
argument_list|()
argument_list|)
expr_stmt|;
name|linkRefreshAccessTokens
argument_list|(
name|rt
argument_list|,
name|at
argument_list|)
expr_stmt|;
return|return
name|rt
return|;
block|}
specifier|private
name|void
name|linkRefreshAccessTokens
parameter_list|(
name|RefreshToken
name|rt
parameter_list|,
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|rt
operator|.
name|getAccessTokens
argument_list|()
operator|.
name|add
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|setRefreshToken
argument_list|(
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ServerAccessToken
name|doRefreshAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|RefreshToken
name|oldRefreshToken
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|restrictedScopes
parameter_list|)
block|{
name|ServerAccessToken
name|at
init|=
name|createNewAccessToken
argument_list|(
name|client
argument_list|)
decl_stmt|;
name|at
operator|.
name|setAudience
argument_list|(
name|oldRefreshToken
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|setGrantType
argument_list|(
name|oldRefreshToken
operator|.
name|getGrantType
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|setSubject
argument_list|(
name|oldRefreshToken
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|restrictedScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|at
operator|.
name|setScopes
argument_list|(
name|oldRefreshToken
operator|.
name|getScopes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|theNewScopes
init|=
name|convertScopeToPermissions
argument_list|(
name|client
argument_list|,
name|restrictedScopes
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldRefreshToken
operator|.
name|getScopes
argument_list|()
operator|.
name|containsAll
argument_list|(
name|theNewScopes
argument_list|)
condition|)
block|{
name|at
operator|.
name|setScopes
argument_list|(
name|theNewScopes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid scopes"
argument_list|)
throw|;
block|}
block|}
return|return
name|at
return|;
block|}
specifier|public
name|void
name|setAccessTokenLifetime
parameter_list|(
name|long
name|accessTokenLifetime
parameter_list|)
block|{
name|this
operator|.
name|accessTokenLifetime
operator|=
name|accessTokenLifetime
expr_stmt|;
block|}
specifier|public
name|void
name|setRefreshTokenLifetime
parameter_list|(
name|long
name|refreshTokenLifetime
parameter_list|)
block|{
name|this
operator|.
name|refreshTokenLifetime
operator|=
name|refreshTokenLifetime
expr_stmt|;
block|}
specifier|public
name|void
name|setRecycleRefreshTokens
parameter_list|(
name|boolean
name|recycleRefreshTokens
parameter_list|)
block|{
name|this
operator|.
name|recycleRefreshTokens
operator|=
name|recycleRefreshTokens
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|()
block|{     }
specifier|public
name|void
name|close
parameter_list|()
block|{     }
specifier|protected
specifier|abstract
name|void
name|saveAccessToken
parameter_list|(
name|ServerAccessToken
name|serverToken
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|saveRefreshToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|,
name|RefreshToken
name|refreshToken
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|ServerAccessToken
name|revokeAccessToken
parameter_list|(
name|String
name|accessTokenKey
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|RefreshToken
name|revokeRefreshToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshTokenKey
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|RefreshToken
name|getRefreshToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshTokenKey
parameter_list|)
function_decl|;
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|OAuthPermission
argument_list|>
name|getPermissionMap
parameter_list|()
block|{
return|return
name|permissionMap
return|;
block|}
specifier|public
name|void
name|setPermissionMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|OAuthPermission
argument_list|>
name|permissionMap
parameter_list|)
block|{
name|this
operator|.
name|permissionMap
operator|=
name|permissionMap
expr_stmt|;
block|}
specifier|public
name|void
name|setScopes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|scopes
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|scopes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|OAuthPermission
name|permission
init|=
operator|new
name|OAuthPermission
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|permissionMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|permission
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|MessageContext
name|getMessageContext
parameter_list|()
block|{
return|return
name|messageContext
return|;
block|}
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|messageContext
parameter_list|)
block|{
name|this
operator|.
name|messageContext
operator|=
name|messageContext
expr_stmt|;
block|}
block|}
end_class

end_unit

