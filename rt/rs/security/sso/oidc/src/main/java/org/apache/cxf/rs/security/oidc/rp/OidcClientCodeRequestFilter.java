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
name|oidc
operator|.
name|rp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestContext
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
name|SecurityContext
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
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
name|json
operator|.
name|basic
operator|.
name|JsonMapObjectReaderWriter
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
name|client
operator|.
name|ClientCodeRequestFilter
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
name|client
operator|.
name|ClientTokenContext
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
name|oidc
operator|.
name|common
operator|.
name|ClaimsRequest
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
import|;
end_import

begin_class
specifier|public
class|class
name|OidcClientCodeRequestFilter
extends|extends
name|ClientCodeRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ACR_PARAMETER
init|=
literal|"acr_values"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LOGIN_HINT_PARAMETER
init|=
literal|"login_hint"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MAX_AGE_PARAMETER
init|=
literal|"max_age"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROMPT_PARAMETER
init|=
literal|"prompt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|PROMPTS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"none"
argument_list|,
literal|"consent"
argument_list|,
literal|"login"
argument_list|,
literal|"select_account"
argument_list|)
decl_stmt|;
specifier|private
name|IdTokenReader
name|idTokenReader
decl_stmt|;
specifier|private
name|UserInfoClient
name|userInfoClient
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|authenticationContextRef
decl_stmt|;
specifier|private
name|String
name|promptLogin
decl_stmt|;
specifier|private
name|Long
name|maxAgeOffset
decl_stmt|;
specifier|private
name|String
name|claims
decl_stmt|;
specifier|private
name|String
name|claimsLocales
decl_stmt|;
specifier|private
name|String
name|roleClaim
decl_stmt|;
specifier|public
name|OidcClientCodeRequestFilter
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setScopes
argument_list|(
literal|"openid"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthenticationContextRef
parameter_list|(
name|String
name|acr
parameter_list|)
block|{
name|this
operator|.
name|authenticationContextRef
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|acr
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ClientTokenContext
name|createTokenContext
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|,
name|ClientAccessToken
name|at
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestParams
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|state
parameter_list|)
block|{
if|if
condition|(
name|rc
operator|.
name|getSecurityContext
argument_list|()
operator|instanceof
name|OidcSecurityContext
condition|)
block|{
return|return
operator|(
operator|(
name|OidcSecurityContext
operator|)
name|rc
operator|.
name|getSecurityContext
argument_list|()
operator|)
operator|.
name|getOidcContext
argument_list|()
return|;
block|}
name|OidcClientTokenContextImpl
name|ctx
init|=
operator|new
name|OidcClientTokenContextImpl
argument_list|()
decl_stmt|;
if|if
condition|(
name|at
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|idTokenReader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|SERVER_ERROR
argument_list|)
throw|;
block|}
name|IdToken
name|idToken
init|=
name|idTokenReader
operator|.
name|getIdToken
argument_list|(
name|at
argument_list|,
name|requestParams
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|)
argument_list|,
name|getConsumer
argument_list|()
argument_list|)
decl_stmt|;
comment|// Validate the properties set up at the redirection time.
name|validateIdToken
argument_list|(
name|idToken
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|setIdToken
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
if|if
condition|(
name|userInfoClient
operator|!=
literal|null
condition|)
block|{
name|ctx
operator|.
name|setUserInfo
argument_list|(
name|userInfoClient
operator|.
name|getUserInfo
argument_list|(
name|at
argument_list|,
name|ctx
operator|.
name|getIdToken
argument_list|()
argument_list|,
name|getConsumer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|OidcSecurityContext
name|oidcSecCtx
init|=
operator|new
name|OidcSecurityContext
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|oidcSecCtx
operator|.
name|setRoleClaim
argument_list|(
name|roleClaim
argument_list|)
expr_stmt|;
name|rc
operator|.
name|setSecurityContext
argument_list|(
name|oidcSecCtx
argument_list|)
expr_stmt|;
block|}
return|return
name|ctx
return|;
block|}
annotation|@
name|Override
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toCodeRequestState
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|,
name|UriInfo
name|ui
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|state
init|=
name|super
operator|.
name|toCodeRequestState
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxAgeOffset
operator|!=
literal|null
condition|)
block|{
name|state
operator|.
name|putSingle
argument_list|(
name|MAX_AGE_PARAMETER
argument_list|,
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|maxAgeOffset
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|state
return|;
block|}
specifier|private
name|void
name|validateIdToken
parameter_list|(
name|IdToken
name|idToken
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|state
parameter_list|)
block|{
name|String
name|nonce
init|=
name|state
operator|.
name|getFirst
argument_list|(
name|IdToken
operator|.
name|NONCE_CLAIM
argument_list|)
decl_stmt|;
name|String
name|tokenNonce
init|=
name|idToken
operator|.
name|getNonce
argument_list|()
decl_stmt|;
if|if
condition|(
name|nonce
operator|!=
literal|null
operator|&&
operator|(
name|tokenNonce
operator|==
literal|null
operator|||
operator|!
name|nonce
operator|.
name|equals
argument_list|(
name|tokenNonce
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
if|if
condition|(
name|maxAgeOffset
operator|!=
literal|null
condition|)
block|{
name|long
name|authTime
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|state
operator|.
name|getFirst
argument_list|(
name|MAX_AGE_PARAMETER
argument_list|)
argument_list|)
decl_stmt|;
name|Long
name|tokenAuthTime
init|=
name|idToken
operator|.
name|getAuthenticationTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokenAuthTime
operator|>
name|authTime
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
name|String
name|acr
init|=
name|idToken
operator|.
name|getAuthenticationContextRef
argument_list|()
decl_stmt|;
comment|// Skip the check if the acr is not set given it is a voluntary claim
if|if
condition|(
name|acr
operator|!=
literal|null
operator|&&
name|authenticationContextRef
operator|!=
literal|null
operator|&&
operator|!
name|authenticationContextRef
operator|.
name|contains
argument_list|(
name|acr
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_REQUEST
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setIdTokenReader
parameter_list|(
name|IdTokenReader
name|idTokenReader
parameter_list|)
block|{
name|this
operator|.
name|idTokenReader
operator|=
name|idTokenReader
expr_stmt|;
block|}
specifier|public
name|void
name|setUserInfoClient
parameter_list|(
name|UserInfoClient
name|userInfoClient
parameter_list|)
block|{
name|this
operator|.
name|userInfoClient
operator|=
name|userInfoClient
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|checkSecurityContextStart
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
block|{
name|SecurityContext
name|sc
init|=
name|rc
operator|.
name|getSecurityContext
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|sc
operator|instanceof
name|OidcSecurityContext
operator|)
operator|&&
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
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
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setAdditionalCodeRequestParams
parameter_list|(
name|UriBuilder
name|ub
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|redirectState
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|codeRequestState
parameter_list|)
block|{
if|if
condition|(
name|redirectState
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|redirectState
operator|.
name|getFirst
argument_list|(
name|IdToken
operator|.
name|NONCE_CLAIM
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|IdToken
operator|.
name|NONCE_CLAIM
argument_list|,
name|redirectState
operator|.
name|getFirst
argument_list|(
name|IdToken
operator|.
name|NONCE_CLAIM
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|redirectState
operator|.
name|getFirst
argument_list|(
name|MAX_AGE_PARAMETER
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|MAX_AGE_PARAMETER
argument_list|,
name|redirectState
operator|.
name|getFirst
argument_list|(
name|MAX_AGE_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|codeRequestState
operator|!=
literal|null
operator|&&
name|codeRequestState
operator|.
name|getFirst
argument_list|(
name|LOGIN_HINT_PARAMETER
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|LOGIN_HINT_PARAMETER
argument_list|,
name|codeRequestState
operator|.
name|getFirst
argument_list|(
name|LOGIN_HINT_PARAMETER
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claims
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
literal|"claims"
argument_list|,
name|claims
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claimsLocales
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
literal|"claims_locales"
argument_list|,
name|claimsLocales
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authenticationContextRef
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|ACR_PARAMETER
argument_list|,
name|authenticationContextRef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|promptLogin
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|PROMPT_PARAMETER
argument_list|,
name|promptLogin
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setPromptLogin
parameter_list|(
name|String
name|promptLogin
parameter_list|)
block|{
if|if
condition|(
name|PROMPTS
operator|.
name|contains
argument_list|(
name|promptLogin
argument_list|)
condition|)
block|{
name|this
operator|.
name|promptLogin
operator|=
name|promptLogin
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Illegal prompt value"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setMaxAgeOffset
parameter_list|(
name|Long
name|maxAgeOffset
parameter_list|)
block|{
name|this
operator|.
name|maxAgeOffset
operator|=
name|maxAgeOffset
expr_stmt|;
block|}
specifier|public
name|void
name|setClaimsRequest
parameter_list|(
name|ClaimsRequest
name|claimsRequest
parameter_list|)
block|{
name|setClaims
argument_list|(
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
operator|.
name|toJson
argument_list|(
name|claimsRequest
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setClaims
parameter_list|(
name|String
name|claims
parameter_list|)
block|{
name|this
operator|.
name|claims
operator|=
name|claims
expr_stmt|;
block|}
specifier|public
name|void
name|setClaimsLocales
parameter_list|(
name|String
name|claimsLocales
parameter_list|)
block|{
name|this
operator|.
name|claimsLocales
operator|=
name|claimsLocales
expr_stmt|;
block|}
specifier|public
name|void
name|setRoleClaim
parameter_list|(
name|String
name|roleClaim
parameter_list|)
block|{
name|this
operator|.
name|roleClaim
operator|=
name|roleClaim
expr_stmt|;
block|}
block|}
end_class

end_unit

