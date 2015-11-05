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
name|services
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
name|util
operator|.
name|LinkedList
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|ClientAccessToken
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
name|OAuthRedirectionState
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
name|AccessTokenResponseFilter
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
name|AbstractImplicitGrantService
extends|extends
name|RedirectionBasedGrantService
block|{
comment|// For a client to validate that this client is a targeted recipient.
specifier|private
name|boolean
name|reportClientId
decl_stmt|;
specifier|private
name|List
argument_list|<
name|AccessTokenResponseFilter
argument_list|>
name|responseHandlers
init|=
operator|new
name|LinkedList
argument_list|<
name|AccessTokenResponseFilter
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|AbstractImplicitGrantService
parameter_list|(
name|String
name|supportedResponseType
parameter_list|,
name|String
name|supportedGrantType
parameter_list|)
block|{
name|super
argument_list|(
name|supportedResponseType
argument_list|,
name|supportedGrantType
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractImplicitGrantService
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|supportedResponseTypes
parameter_list|,
name|String
name|supportedGrantType
parameter_list|)
block|{
name|super
argument_list|(
name|supportedResponseTypes
argument_list|,
name|supportedGrantType
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Response
name|createGrant
parameter_list|(
name|OAuthRedirectionState
name|state
parameter_list|,
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|ServerAccessToken
name|preAuthorizedToken
parameter_list|)
block|{
name|boolean
name|tokenCanBeReturned
init|=
name|preAuthorizedToken
operator|!=
literal|null
decl_stmt|;
name|ServerAccessToken
name|token
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|preAuthorizedToken
operator|==
literal|null
condition|)
block|{
name|tokenCanBeReturned
operator|=
name|canAccessTokenBeReturned
argument_list|(
name|requestedScope
argument_list|,
name|approvedScope
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokenCanBeReturned
condition|)
block|{
name|AccessTokenRegistration
name|reg
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|reg
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setGrantType
argument_list|(
name|super
operator|.
name|getSupportedGrantType
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setSubject
argument_list|(
name|userSubject
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setRequestedScope
argument_list|(
name|requestedScope
argument_list|)
expr_stmt|;
if|if
condition|(
name|approvedScope
operator|!=
literal|null
operator|&&
name|approvedScope
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// no down-scoping done by a user, all of the requested scopes have been authorized
name|reg
operator|.
name|setApprovedScope
argument_list|(
name|requestedScope
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reg
operator|.
name|setApprovedScope
argument_list|(
name|approvedScope
argument_list|)
expr_stmt|;
block|}
name|reg
operator|.
name|setAudience
argument_list|(
name|state
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|=
name|getDataProvider
argument_list|()
operator|.
name|createAccessToken
argument_list|(
name|reg
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|token
operator|=
name|preAuthorizedToken
expr_stmt|;
block|}
name|ClientAccessToken
name|clientToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|clientToken
operator|=
name|OAuthUtils
operator|.
name|toClientAccessToken
argument_list|(
name|token
argument_list|,
name|isWriteOptionalParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// this is not ideal - it is only done to have OIDC Implicit to have an id_token added
comment|// via AccessTokenResponseFilter. Note if id_token is needed (with or without access token)
comment|// then the service needs to be injected with SubjectCreator, example, DefaultSubjectCreator
comment|// extension which will have a chance to attach id_token to Subject properties which are checked
comment|// by id_token AccessTokenResponseFilter. If at is also needed then OAuthDataProvider may deal
comment|// with attaching id_token itself in which case no SubjectCreator injection is necessary
name|clientToken
operator|=
operator|new
name|ClientAccessToken
argument_list|()
expr_stmt|;
block|}
name|processClientAccessToken
argument_list|(
name|clientToken
argument_list|,
name|token
argument_list|)
expr_stmt|;
comment|// return the token by appending it as a fragment parameter to the redirect URI
name|StringBuilder
name|sb
init|=
name|getUriWithFragment
argument_list|(
name|state
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenCanBeReturned
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|clientToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|clientToken
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|.
name|getState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|state
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isWriteOptionalParameters
argument_list|()
condition|)
block|{
if|if
condition|(
name|tokenCanBeReturned
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_EXPIRES_IN
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|clientToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientToken
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|HttpUtils
operator|.
name|queryEncode
argument_list|(
name|clientToken
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|clientToken
operator|.
name|getParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|HttpUtils
operator|.
name|queryEncode
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tokenCanBeReturned
operator|&&
name|token
operator|.
name|getRefreshToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|processRefreshToken
argument_list|(
name|sb
argument_list|,
name|token
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reportClientId
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|boolean
name|canAccessTokenBeReturned
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|processRefreshToken
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|String
name|refreshToken
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Implicit grant tokens MUST not have refresh tokens, refresh token will not be reported"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processClientAccessToken
parameter_list|(
name|ClientAccessToken
name|clientToken
parameter_list|,
name|ServerAccessToken
name|serverToken
parameter_list|)
block|{
for|for
control|(
name|AccessTokenResponseFilter
name|filter
range|:
name|responseHandlers
control|)
block|{
name|filter
operator|.
name|process
argument_list|(
name|clientToken
argument_list|,
name|serverToken
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Response
name|createErrorResponse
parameter_list|(
name|String
name|state
parameter_list|,
name|String
name|redirectUri
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
name|getUriWithFragment
argument_list|(
name|redirectUri
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|error
argument_list|)
expr_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|StringBuilder
name|getUriWithFragment
parameter_list|(
name|String
name|redirectUri
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|redirectUri
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
return|return
name|sb
return|;
block|}
specifier|public
name|void
name|setReportClientId
parameter_list|(
name|boolean
name|reportClientId
parameter_list|)
block|{
name|this
operator|.
name|reportClientId
operator|=
name|reportClientId
expr_stmt|;
block|}
specifier|public
name|void
name|setResponseFilters
parameter_list|(
name|List
argument_list|<
name|AccessTokenResponseFilter
argument_list|>
name|handlers
parameter_list|)
block|{
name|this
operator|.
name|responseHandlers
operator|=
name|handlers
expr_stmt|;
block|}
specifier|public
name|void
name|setResponseFilter
parameter_list|(
name|AccessTokenResponseFilter
name|responseHandler
parameter_list|)
block|{
name|responseHandlers
operator|.
name|add
argument_list|(
name|responseHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|canRedirectUriBeEmpty
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|canSupportPublicClient
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

