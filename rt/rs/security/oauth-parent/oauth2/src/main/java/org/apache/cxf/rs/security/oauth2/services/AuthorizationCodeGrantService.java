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
name|util
operator|.
name|List
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
name|Path
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
name|MediaType
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriBuilder
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
name|OAuthAuthorizationData
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
name|OOBAuthorizationResponse
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
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeDataProvider
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
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeRegistration
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
name|grants
operator|.
name|code
operator|.
name|ServerAuthorizationCodeGrant
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
name|AuthorizationCodeRequestFilter
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
name|AuthorizationCodeResponseFilter
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
name|provider
operator|.
name|OOBResponseDeliverer
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

begin_comment
comment|/**  * This resource handles the End User authorising  * or denying the Client to access its resources.  * If End User approves the access this resource will  * redirect End User back to the Client, supplying   * the authorization code.  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/authorize"
argument_list|)
specifier|public
class|class
name|AuthorizationCodeGrantService
extends|extends
name|RedirectionBasedGrantService
block|{
specifier|private
specifier|static
specifier|final
name|long
name|RECOMMENDED_CODE_EXPIRY_TIME_SECS
init|=
literal|10L
operator|*
literal|60L
decl_stmt|;
specifier|private
name|boolean
name|canSupportPublicClients
decl_stmt|;
specifier|private
name|boolean
name|canSupportEmptyRedirectForPrivateClients
decl_stmt|;
specifier|private
name|OOBResponseDeliverer
name|oobDeliverer
decl_stmt|;
specifier|private
name|AuthorizationCodeRequestFilter
name|codeRequestFilter
decl_stmt|;
specifier|private
name|AuthorizationCodeResponseFilter
name|codeResponseFilter
decl_stmt|;
specifier|public
name|AuthorizationCodeGrantService
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|CODE_RESPONSE_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_GRANT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|OAuthAuthorizationData
name|createAuthorizationData
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|redirectUri
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
parameter_list|,
name|boolean
name|authorizationCanBeSkipped
parameter_list|)
block|{
name|OAuthAuthorizationData
name|data
init|=
name|super
operator|.
name|createAuthorizationData
argument_list|(
name|client
argument_list|,
name|params
argument_list|,
name|redirectUri
argument_list|,
name|subject
argument_list|,
name|perms
argument_list|,
name|authorizationCanBeSkipped
argument_list|)
decl_stmt|;
name|setCodeQualifier
argument_list|(
name|data
argument_list|,
name|params
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
specifier|protected
name|OAuthRedirectionState
name|recreateRedirectionStateFromSession
parameter_list|(
name|UserSubject
name|subject
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|sessionToken
parameter_list|)
block|{
name|OAuthRedirectionState
name|state
init|=
name|super
operator|.
name|recreateRedirectionStateFromSession
argument_list|(
name|subject
argument_list|,
name|params
argument_list|,
name|sessionToken
argument_list|)
decl_stmt|;
name|setCodeQualifier
argument_list|(
name|state
argument_list|,
name|params
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
specifier|private
specifier|static
name|void
name|setCodeQualifier
parameter_list|(
name|OAuthRedirectionState
name|data
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|data
operator|.
name|setClientCodeChallenge
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_CHALLENGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Response
name|startAuthorization
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|Client
name|client
parameter_list|)
block|{
if|if
condition|(
name|codeRequestFilter
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
name|codeRequestFilter
operator|.
name|process
argument_list|(
name|params
argument_list|,
name|userSubject
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|startAuthorization
argument_list|(
name|params
argument_list|,
name|userSubject
argument_list|,
name|client
argument_list|)
return|;
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
name|preauthorizedToken
parameter_list|)
block|{
comment|// in this flow the code is still created, the preauthorized token
comment|// will be retrieved by the authorization code grant handler
name|AuthorizationCodeRegistration
name|codeReg
init|=
operator|new
name|AuthorizationCodeRegistration
argument_list|()
decl_stmt|;
name|codeReg
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setRedirectUri
argument_list|(
name|state
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
expr_stmt|;
name|codeReg
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
name|codeReg
operator|.
name|setApprovedScope
argument_list|(
name|requestedScope
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|codeReg
operator|.
name|setApprovedScope
argument_list|(
name|approvedScope
argument_list|)
expr_stmt|;
block|}
name|codeReg
operator|.
name|setSubject
argument_list|(
name|userSubject
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setAudience
argument_list|(
name|state
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setNonce
argument_list|(
name|state
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setClientCodeChallenge
argument_list|(
name|state
operator|.
name|getClientCodeChallenge
argument_list|()
argument_list|)
expr_stmt|;
name|ServerAuthorizationCodeGrant
name|grant
init|=
literal|null
decl_stmt|;
try|try
block|{
name|grant
operator|=
operator|(
operator|(
name|AuthorizationCodeDataProvider
operator|)
name|getDataProvider
argument_list|()
operator|)
operator|.
name|createCodeGrant
argument_list|(
name|codeReg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|state
operator|.
name|getState
argument_list|()
argument_list|,
name|state
operator|.
name|getRedirectUri
argument_list|()
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_DENIED
argument_list|)
return|;
block|}
if|if
condition|(
name|grant
operator|.
name|getExpiresIn
argument_list|()
operator|>
name|RECOMMENDED_CODE_EXPIRY_TIME_SECS
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Code expiry time exceeds 10 minutes"
argument_list|)
expr_stmt|;
block|}
name|String
name|grantCode
init|=
name|processCodeGrant
argument_list|(
name|client
argument_list|,
name|grant
operator|.
name|getCode
argument_list|()
argument_list|,
name|grant
operator|.
name|getSubject
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|.
name|getRedirectUri
argument_list|()
operator|==
literal|null
condition|)
block|{
name|OOBAuthorizationResponse
name|oobResponse
init|=
operator|new
name|OOBAuthorizationResponse
argument_list|()
decl_stmt|;
name|oobResponse
operator|.
name|setClientId
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|oobResponse
operator|.
name|setClientDescription
argument_list|(
name|client
operator|.
name|getApplicationDescription
argument_list|()
argument_list|)
expr_stmt|;
name|oobResponse
operator|.
name|setAuthorizationCode
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|oobResponse
operator|.
name|setUserId
argument_list|(
name|userSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
expr_stmt|;
name|oobResponse
operator|.
name|setExpiresIn
argument_list|(
name|grant
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|deliverOOBResponse
argument_list|(
name|oobResponse
argument_list|)
return|;
block|}
else|else
block|{
comment|// return the code by appending it as a query parameter to the redirect URI
name|UriBuilder
name|ub
init|=
name|getRedirectUriBuilder
argument_list|(
name|state
operator|.
name|getState
argument_list|()
argument_list|,
name|state
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|,
name|grantCode
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|ub
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
specifier|protected
name|String
name|processCodeGrant
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|code
parameter_list|,
name|UserSubject
name|endUser
parameter_list|)
block|{
if|if
condition|(
name|codeResponseFilter
operator|!=
literal|null
condition|)
block|{
return|return
name|codeResponseFilter
operator|.
name|process
argument_list|(
name|client
argument_list|,
name|code
argument_list|,
name|endUser
argument_list|)
return|;
block|}
return|return
name|code
return|;
block|}
specifier|protected
name|Response
name|deliverOOBResponse
parameter_list|(
name|OOBAuthorizationResponse
name|response
parameter_list|)
block|{
if|if
condition|(
name|oobDeliverer
operator|!=
literal|null
condition|)
block|{
return|return
name|oobDeliverer
operator|.
name|deliver
argument_list|(
name|response
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|response
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_HTML
argument_list|)
operator|.
name|build
argument_list|()
return|;
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
if|if
condition|(
name|redirectUri
operator|==
literal|null
condition|)
block|{
return|return
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|entity
argument_list|(
name|error
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
name|UriBuilder
name|ub
init|=
name|getRedirectUriBuilder
argument_list|(
name|state
argument_list|,
name|redirectUri
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|,
name|error
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|ub
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
specifier|protected
name|UriBuilder
name|getRedirectUriBuilder
parameter_list|(
name|String
name|state
parameter_list|,
name|String
name|redirectUri
parameter_list|)
block|{
name|UriBuilder
name|ub
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|redirectUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
return|return
name|ub
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
name|canSupportPublicClients
operator|&&
operator|!
name|c
operator|.
name|isConfidential
argument_list|()
operator|&&
name|c
operator|.
name|getClientSecret
argument_list|()
operator|==
literal|null
return|;
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
comment|// If a redirect URI is empty then the code will be returned out of band,
comment|// typically will be returned directly to a human user
return|return
operator|(
name|c
operator|.
name|isConfidential
argument_list|()
operator|&&
name|canSupportEmptyRedirectForPrivateClients
operator|||
name|canSupportPublicClient
argument_list|(
name|c
argument_list|)
operator|)
operator|&&
name|c
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|void
name|setCanSupportPublicClients
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|this
operator|.
name|canSupportPublicClients
operator|=
name|support
expr_stmt|;
block|}
specifier|public
name|void
name|setCodeResponseFilter
parameter_list|(
name|AuthorizationCodeResponseFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|codeResponseFilter
operator|=
name|filter
expr_stmt|;
block|}
specifier|public
name|void
name|setCodeRequestFilter
parameter_list|(
name|AuthorizationCodeRequestFilter
name|codeRequestFilter
parameter_list|)
block|{
name|this
operator|.
name|codeRequestFilter
operator|=
name|codeRequestFilter
expr_stmt|;
block|}
specifier|public
name|void
name|setCanSupportEmptyRedirectForPrivateClients
parameter_list|(
name|boolean
name|canSupportEmptyRedirectForPrivateClients
parameter_list|)
block|{
name|this
operator|.
name|canSupportEmptyRedirectForPrivateClients
operator|=
name|canSupportEmptyRedirectForPrivateClients
expr_stmt|;
block|}
block|}
end_class

end_unit

