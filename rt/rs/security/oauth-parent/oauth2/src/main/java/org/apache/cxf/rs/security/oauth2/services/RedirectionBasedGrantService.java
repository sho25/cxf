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
name|Collections
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|GET
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
name|POST
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
name|Produces
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
name|ExceptionUtils
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
name|OAuthError
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
name|ResourceOwnerNameProvider
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
name|SessionAuthenticityTokenProvider
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
name|SubjectCreator
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_comment
comment|/**  * The Base Redirection-Based Grant Service  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|RedirectionBasedGrantService
extends|extends
name|AbstractOAuthService
block|{
specifier|private
name|String
name|supportedResponseType
decl_stmt|;
specifier|private
name|String
name|supportedGrantType
decl_stmt|;
specifier|private
name|boolean
name|partialMatchScopeValidation
decl_stmt|;
specifier|private
name|boolean
name|useRegisteredRedirectUriIfPossible
init|=
literal|true
decl_stmt|;
specifier|private
name|SessionAuthenticityTokenProvider
name|sessionAuthenticityTokenProvider
decl_stmt|;
specifier|private
name|SubjectCreator
name|subjectCreator
decl_stmt|;
specifier|private
name|ResourceOwnerNameProvider
name|resourceOwnerNameProvider
decl_stmt|;
specifier|private
name|int
name|maxDefaultSessionInterval
decl_stmt|;
specifier|protected
name|RedirectionBasedGrantService
parameter_list|(
name|String
name|supportedResponseType
parameter_list|,
name|String
name|supportedGrantType
parameter_list|)
block|{
name|this
operator|.
name|supportedResponseType
operator|=
name|supportedResponseType
expr_stmt|;
name|this
operator|.
name|supportedGrantType
operator|=
name|supportedGrantType
expr_stmt|;
block|}
comment|/**      * Handles the initial authorization request by preparing       * the authorization challenge data and returning it to the user.      * Typically the data are expected to be presented in the HTML form       * @return the authorization data      */
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xhtml+xml"
block|,
literal|"text/html"
block|,
literal|"application/xml"
block|,
literal|"application/json"
block|}
argument_list|)
specifier|public
name|Response
name|authorize
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|getQueryParameters
argument_list|()
decl_stmt|;
return|return
name|startAuthorization
argument_list|(
name|params
argument_list|)
return|;
block|}
comment|/**      * Processes the end user decision      * @return The grant value, authorization code or the token      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/decision"
argument_list|)
specifier|public
name|Response
name|authorizeDecision
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|getQueryParameters
argument_list|()
decl_stmt|;
return|return
name|completeAuthorization
argument_list|(
name|params
argument_list|)
return|;
block|}
comment|/**      * Processes the end user decision      * @return The grant value, authorization code or the token      */
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/decision"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
specifier|public
name|Response
name|authorizeDecisionForm
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
return|return
name|completeAuthorization
argument_list|(
name|params
argument_list|)
return|;
block|}
comment|/**      * Starts the authorization process      */
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
parameter_list|)
block|{
comment|// Make sure the end user has authenticated, check if HTTPS is used
name|SecurityContext
name|sc
init|=
name|getAndValidateSecurityContext
argument_list|(
name|params
argument_list|)
decl_stmt|;
comment|// Create a UserSubject representing the end user
name|UserSubject
name|userSubject
init|=
name|createUserSubject
argument_list|(
name|sc
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|getClient
argument_list|(
name|params
argument_list|)
decl_stmt|;
return|return
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
comment|// Validate the provided request URI, if any, against the ones Client provided
comment|// during the registration
name|String
name|redirectUri
init|=
name|validateRedirectUri
argument_list|(
name|client
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|REDIRECT_URI
argument_list|)
argument_list|)
decl_stmt|;
comment|// Enforce the client confidentiality requirements
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|isGrantSupportedForClient
argument_list|(
name|client
argument_list|,
name|canSupportPublicClient
argument_list|(
name|client
argument_list|)
argument_list|,
name|supportedGrantType
argument_list|)
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|UNAUTHORIZED_CLIENT
argument_list|)
return|;
block|}
comment|// Check response_type
name|String
name|responseType
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseType
operator|==
literal|null
operator|||
operator|!
name|responseType
operator|.
name|equals
argument_list|(
name|supportedResponseType
argument_list|)
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|UNSUPPORTED_RESPONSE_TYPE
argument_list|)
return|;
block|}
comment|// Get the requested scopes
name|String
name|providedScope
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
init|=
literal|null
decl_stmt|;
try|try
block|{
name|requestedScope
operator|=
name|OAuthUtils
operator|.
name|getRequestedScopes
argument_list|(
name|client
argument_list|,
name|providedScope
argument_list|,
name|partialMatchScopeValidation
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
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|INVALID_SCOPE
argument_list|)
return|;
block|}
comment|// Convert the requested scopes to OAuthPermission instances
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
init|=
literal|null
decl_stmt|;
try|try
block|{
name|permissions
operator|=
name|getDataProvider
argument_list|()
operator|.
name|convertScopeToPermissions
argument_list|(
name|client
argument_list|,
name|requestedScope
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
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|INVALID_SCOPE
argument_list|)
return|;
block|}
comment|// Validate the audience
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|validateAudience
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|)
argument_list|,
name|client
operator|.
name|getRegisteredAudiences
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
argument_list|)
throw|;
block|}
comment|// Request a new grant only if no pre-authorized token is available
name|ServerAccessToken
name|preAuthorizedToken
init|=
name|getDataProvider
argument_list|()
operator|.
name|getPreauthorizedToken
argument_list|(
name|client
argument_list|,
name|requestedScope
argument_list|,
name|userSubject
argument_list|,
name|supportedGrantType
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|preAuthorizedTokenAvailable
init|=
name|preAuthorizedToken
operator|!=
literal|null
decl_stmt|;
comment|// Populate the authorization challenge data
name|OAuthAuthorizationData
name|data
init|=
name|createAuthorizationData
argument_list|(
name|client
argument_list|,
name|params
argument_list|,
name|userSubject
argument_list|,
name|permissions
argument_list|,
name|preAuthorizedTokenAvailable
argument_list|)
decl_stmt|;
if|if
condition|(
name|preAuthorizedTokenAvailable
condition|)
block|{
return|return
name|createGrant
argument_list|(
name|data
argument_list|,
name|client
argument_list|,
name|requestedScope
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|,
name|userSubject
argument_list|,
name|preAuthorizedToken
argument_list|)
return|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|data
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Create the authorization challenge data       */
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
name|preAuthorizedTokenAvailable
parameter_list|)
block|{
name|OAuthAuthorizationData
name|secData
init|=
operator|new
name|OAuthAuthorizationData
argument_list|()
decl_stmt|;
name|secData
operator|.
name|setState
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setRedirectUri
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|REDIRECT_URI
argument_list|)
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setAudience
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|)
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setClientId
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setProposedScope
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|preAuthorizedTokenAvailable
condition|)
block|{
name|secData
operator|.
name|setPermissions
argument_list|(
name|perms
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationName
argument_list|(
name|client
operator|.
name|getApplicationName
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationWebUri
argument_list|(
name|client
operator|.
name|getApplicationWebUri
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationDescription
argument_list|(
name|client
operator|.
name|getApplicationDescription
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationLogoUri
argument_list|(
name|client
operator|.
name|getApplicationLogoUri
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationCertificates
argument_list|(
name|client
operator|.
name|getApplicationCertificates
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraProperties
init|=
name|client
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|secData
operator|.
name|setExtraApplicationProperties
argument_list|(
name|extraProperties
argument_list|)
expr_stmt|;
name|String
name|replyTo
init|=
name|getMessageContext
argument_list|()
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getAbsolutePathBuilder
argument_list|()
operator|.
name|path
argument_list|(
literal|"decision"
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|secData
operator|.
name|setReplyTo
argument_list|(
name|replyTo
argument_list|)
expr_stmt|;
name|personalizeData
argument_list|(
name|secData
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|addAuthenticityTokenToSession
argument_list|(
name|secData
argument_list|,
name|params
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
return|return
name|secData
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
literal|null
decl_stmt|;
if|if
condition|(
name|sessionAuthenticityTokenProvider
operator|!=
literal|null
condition|)
block|{
name|state
operator|=
name|sessionAuthenticityTokenProvider
operator|.
name|getSessionState
argument_list|(
name|super
operator|.
name|getMessageContext
argument_list|()
argument_list|,
name|sessionToken
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
name|state
operator|=
operator|new
name|OAuthRedirectionState
argument_list|()
expr_stmt|;
name|state
operator|.
name|setClientId
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|setRedirectUri
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|REDIRECT_URI
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|setAudience
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|setProposedScope
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
argument_list|)
expr_stmt|;
name|state
operator|.
name|setState
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|state
return|;
block|}
specifier|protected
name|void
name|personalizeData
parameter_list|(
name|OAuthAuthorizationData
name|data
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|)
block|{
if|if
condition|(
name|resourceOwnerNameProvider
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|setEndUserName
argument_list|(
name|resourceOwnerNameProvider
operator|.
name|getName
argument_list|(
name|userSubject
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Completes the authorization process      */
specifier|protected
name|Response
name|completeAuthorization
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
comment|// Make sure the end user has authenticated, check if HTTPS is used
name|SecurityContext
name|securityContext
init|=
name|getAndValidateSecurityContext
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|UserSubject
name|userSubject
init|=
name|createUserSubject
argument_list|(
name|securityContext
argument_list|)
decl_stmt|;
comment|// Make sure the session is valid
name|String
name|sessionTokenParamName
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SESSION_AUTHENTICITY_TOKEN_PARAM_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|sessionTokenParamName
operator|==
literal|null
condition|)
block|{
name|sessionTokenParamName
operator|=
name|OAuthConstants
operator|.
name|SESSION_AUTHENTICITY_TOKEN
expr_stmt|;
block|}
name|String
name|sessionToken
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|sessionTokenParamName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|compareRequestAndSessionTokens
argument_list|(
name|sessionToken
argument_list|,
name|params
argument_list|,
name|userSubject
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|OAuthRedirectionState
name|state
init|=
name|recreateRedirectionStateFromSession
argument_list|(
name|userSubject
argument_list|,
name|params
argument_list|,
name|sessionToken
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|getClient
argument_list|(
name|state
operator|.
name|getClientId
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|redirectUri
init|=
name|validateRedirectUri
argument_list|(
name|client
argument_list|,
name|state
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
decl_stmt|;
comment|// Get the end user decision value
name|String
name|decision
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_DECISION_KEY
argument_list|)
decl_stmt|;
name|boolean
name|allow
init|=
name|OAuthConstants
operator|.
name|AUTHORIZATION_DECISION_ALLOW
operator|.
name|equals
argument_list|(
name|decision
argument_list|)
decl_stmt|;
comment|// Return the error if denied
if|if
condition|(
operator|!
name|allow
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_DENIED
argument_list|)
return|;
block|}
comment|// Check if the end user may have had a chance to down-scope the requested scopes
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
init|=
name|OAuthUtils
operator|.
name|parseScope
argument_list|(
name|state
operator|.
name|getProposedScope
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|rScope
range|:
name|requestedScope
control|)
block|{
name|String
name|param
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|rScope
operator|+
literal|"_status"
argument_list|)
decl_stmt|;
if|if
condition|(
name|param
operator|!=
literal|null
operator|&&
name|OAuthConstants
operator|.
name|AUTHORIZATION_DECISION_ALLOW
operator|.
name|equals
argument_list|(
name|param
argument_list|)
condition|)
block|{
name|approvedScope
operator|.
name|add
argument_list|(
name|rScope
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|requestedScope
operator|.
name|containsAll
argument_list|(
name|approvedScope
argument_list|)
operator|||
operator|!
name|OAuthUtils
operator|.
name|validateScopes
argument_list|(
name|requestedScope
argument_list|,
name|client
operator|.
name|getRegisteredScopes
argument_list|()
argument_list|,
name|partialMatchScopeValidation
argument_list|)
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|INVALID_SCOPE
argument_list|)
return|;
block|}
comment|// Request a new grant
return|return
name|createGrant
argument_list|(
name|state
argument_list|,
name|client
argument_list|,
name|requestedScope
argument_list|,
name|approvedScope
argument_list|,
name|userSubject
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSessionAuthenticityTokenProvider
parameter_list|(
name|SessionAuthenticityTokenProvider
name|sessionAuthenticityTokenProvider
parameter_list|)
block|{
name|this
operator|.
name|sessionAuthenticityTokenProvider
operator|=
name|sessionAuthenticityTokenProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setSubjectCreator
parameter_list|(
name|SubjectCreator
name|creator
parameter_list|)
block|{
name|this
operator|.
name|subjectCreator
operator|=
name|creator
expr_stmt|;
block|}
specifier|protected
name|UserSubject
name|createUserSubject
parameter_list|(
name|SecurityContext
name|securityContext
parameter_list|)
block|{
name|UserSubject
name|subject
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|subjectCreator
operator|!=
literal|null
condition|)
block|{
name|subject
operator|=
name|subjectCreator
operator|.
name|createUserSubject
argument_list|(
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
return|return
name|subject
return|;
block|}
block|}
name|subject
operator|=
name|getMessageContext
argument_list|()
operator|.
name|getContent
argument_list|(
name|UserSubject
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
return|return
name|subject
return|;
block|}
else|else
block|{
return|return
name|OAuthUtils
operator|.
name|createSubject
argument_list|(
name|securityContext
argument_list|)
return|;
block|}
block|}
specifier|protected
name|Response
name|createErrorResponse
parameter_list|(
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
name|String
name|error
parameter_list|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
argument_list|,
name|redirectUri
argument_list|,
name|error
argument_list|)
return|;
block|}
specifier|protected
specifier|abstract
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
function_decl|;
specifier|protected
specifier|abstract
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
function_decl|;
specifier|protected
name|SecurityContext
name|getAndValidateSecurityContext
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|SecurityContext
name|securityContext
init|=
operator|(
name|SecurityContext
operator|)
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityContext
operator|==
literal|null
operator|||
name|securityContext
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|checkTransportSecurity
argument_list|()
expr_stmt|;
return|return
name|securityContext
return|;
block|}
specifier|protected
name|String
name|validateRedirectUri
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|redirectUri
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|uris
init|=
name|client
operator|.
name|getRedirectUris
argument_list|()
decl_stmt|;
if|if
condition|(
name|redirectUri
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|uris
operator|.
name|contains
argument_list|(
name|redirectUri
argument_list|)
condition|)
block|{
name|redirectUri
operator|=
literal|null
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|uris
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|useRegisteredRedirectUriIfPossible
condition|)
block|{
name|redirectUri
operator|=
name|uris
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|redirectUri
operator|==
literal|null
operator|&&
name|uris
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|&&
operator|!
name|canRedirectUriBeEmpty
argument_list|(
name|client
argument_list|)
condition|)
block|{
name|reportInvalidRequestError
argument_list|(
literal|"Client Redirect Uri is invalid"
argument_list|)
expr_stmt|;
block|}
return|return
name|redirectUri
return|;
block|}
specifier|private
name|void
name|addAuthenticityTokenToSession
parameter_list|(
name|OAuthAuthorizationData
name|secData
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|UserSubject
name|subject
parameter_list|)
block|{
specifier|final
name|String
name|sessionToken
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|sessionAuthenticityTokenProvider
operator|!=
literal|null
condition|)
block|{
name|sessionToken
operator|=
name|sessionAuthenticityTokenProvider
operator|.
name|createSessionToken
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|params
argument_list|,
name|subject
argument_list|,
name|secData
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sessionToken
operator|=
name|OAuthUtils
operator|.
name|setDefaultSessionToken
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|maxDefaultSessionInterval
argument_list|)
expr_stmt|;
block|}
name|secData
operator|.
name|setAuthenticityToken
argument_list|(
name|sessionToken
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|compareRequestAndSessionTokens
parameter_list|(
name|String
name|requestToken
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|UserSubject
name|subject
parameter_list|)
block|{
specifier|final
name|String
name|sessionToken
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|sessionAuthenticityTokenProvider
operator|!=
literal|null
condition|)
block|{
name|sessionToken
operator|=
name|sessionAuthenticityTokenProvider
operator|.
name|removeSessionToken
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|params
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sessionToken
operator|=
name|OAuthUtils
operator|.
name|getDefaultSessionToken
argument_list|(
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|sessionToken
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
name|requestToken
operator|.
name|equals
argument_list|(
name|sessionToken
argument_list|)
return|;
block|}
block|}
comment|/**      * Get the {@link Client} reference      * @param params request parameters      * @return Client the client reference       * @throws {@link javax.ws.rs.WebApplicationException} if no matching Client is found,       *         the error is returned directly to the end user without       *         following the redirect URI if any      */
specifier|protected
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|Client
name|client
init|=
literal|null
decl_stmt|;
try|try
block|{
name|client
operator|=
name|getValidClient
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getError
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reportInvalidRequestError
argument_list|(
name|ex
operator|.
name|getError
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|reportInvalidRequestError
argument_list|(
literal|"Client ID is invalid"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|Client
name|getClient
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
return|return
name|this
operator|.
name|getClient
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setResourceOwnerNameProvider
parameter_list|(
name|ResourceOwnerNameProvider
name|resourceOwnerNameProvider
parameter_list|)
block|{
name|this
operator|.
name|resourceOwnerNameProvider
operator|=
name|resourceOwnerNameProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setPartialMatchScopeValidation
parameter_list|(
name|boolean
name|partialMatchScopeValidation
parameter_list|)
block|{
name|this
operator|.
name|partialMatchScopeValidation
operator|=
name|partialMatchScopeValidation
expr_stmt|;
block|}
comment|/**      * If a client does not include a redirect_uri parameter but has an exactly one      * pre-registered redirect_uri then use that redirect_uri      * @param use allows to use a single registered redirect_uri if set to true (default)      */
specifier|public
name|void
name|setUseRegisteredRedirectUriIfPossible
parameter_list|(
name|boolean
name|use
parameter_list|)
block|{
name|this
operator|.
name|useRegisteredRedirectUriIfPossible
operator|=
name|use
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|boolean
name|canSupportPublicClient
parameter_list|(
name|Client
name|c
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|boolean
name|canRedirectUriBeEmpty
parameter_list|(
name|Client
name|c
parameter_list|)
function_decl|;
specifier|public
name|void
name|setMaxDefaultSessionInterval
parameter_list|(
name|int
name|maxDefaultSessionInterval
parameter_list|)
block|{
name|this
operator|.
name|maxDefaultSessionInterval
operator|=
name|maxDefaultSessionInterval
expr_stmt|;
block|}
block|}
end_class

end_unit

