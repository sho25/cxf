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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|annotation
operator|.
name|Priority
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
name|Priorities
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
name|container
operator|.
name|ContainerRequestFilter
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
name|PreMatching
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
name|Context
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
name|common
operator|.
name|util
operator|.
name|Base64UrlUtility
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
name|client
operator|.
name|WebClient
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
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
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
name|jaxrs
operator|.
name|utils
operator|.
name|FormUtils
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
name|JAXRSUtils
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
name|AccessTokenGrant
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
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeGrant
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
name|CodeVerifierTransformer
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
annotation|@
name|PreMatching
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
operator|+
literal|1
argument_list|)
specifier|public
class|class
name|ClientCodeRequestFilter
implements|implements
name|ContainerRequestFilter
block|{
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|String
name|scopes
decl_stmt|;
specifier|private
name|String
name|completeUri
decl_stmt|;
specifier|private
name|String
name|startUri
decl_stmt|;
specifier|private
name|String
name|authorizationServiceUri
decl_stmt|;
specifier|private
name|Consumer
name|consumer
decl_stmt|;
specifier|private
name|ClientCodeStateManager
name|clientStateManager
decl_stmt|;
specifier|private
name|ClientTokenContextManager
name|clientTokenContextManager
decl_stmt|;
specifier|private
name|WebClient
name|accessTokenServiceClient
decl_stmt|;
specifier|private
name|boolean
name|decodeRequestParameters
decl_stmt|;
specifier|private
name|long
name|expiryThreshold
decl_stmt|;
specifier|private
name|String
name|redirectUri
decl_stmt|;
specifier|private
name|boolean
name|setFormPostResponseMode
decl_stmt|;
specifier|private
name|boolean
name|faultAccessDeniedResponses
decl_stmt|;
specifier|private
name|boolean
name|applicationCanHandleAccessDenied
decl_stmt|;
specifier|private
name|CodeVerifierTransformer
name|codeVerifierTransformer
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
throws|throws
name|IOException
block|{
name|checkSecurityContextStart
argument_list|(
name|rc
argument_list|)
expr_stmt|;
name|UriInfo
name|ui
init|=
name|rc
operator|.
name|getUriInfo
argument_list|()
decl_stmt|;
name|String
name|absoluteRequestUri
init|=
name|ui
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|boolean
name|sameUriRedirect
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|completeUri
operator|==
literal|null
condition|)
block|{
name|String
name|referer
init|=
name|rc
operator|.
name|getHeaderString
argument_list|(
literal|"Referer"
argument_list|)
decl_stmt|;
if|if
condition|(
name|referer
operator|!=
literal|null
operator|&&
name|referer
operator|.
name|startsWith
argument_list|(
name|authorizationServiceUri
argument_list|)
condition|)
block|{
name|completeUri
operator|=
name|absoluteRequestUri
expr_stmt|;
name|sameUriRedirect
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|sameUriRedirect
operator|&&
name|absoluteRequestUri
operator|.
name|endsWith
argument_list|(
name|startUri
argument_list|)
condition|)
block|{
name|ClientTokenContext
name|request
init|=
name|getClientTokenContext
argument_list|(
name|rc
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|!=
literal|null
condition|)
block|{
name|setClientCodeRequest
argument_list|(
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|completeUri
operator|!=
literal|null
condition|)
block|{
name|rc
operator|.
name|setRequestUri
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|completeUri
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|Response
name|codeResponse
init|=
name|createCodeResponse
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|)
decl_stmt|;
name|rc
operator|.
name|abortWith
argument_list|(
name|codeResponse
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|absoluteRequestUri
operator|.
name|endsWith
argument_list|(
name|completeUri
argument_list|)
condition|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestParams
init|=
name|toRequestState
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|)
decl_stmt|;
name|processCodeResponse
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|,
name|requestParams
argument_list|)
expr_stmt|;
name|checkSecurityContextEnd
argument_list|(
name|rc
argument_list|,
name|requestParams
argument_list|)
expr_stmt|;
block|}
block|}
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
name|sc
operator|==
literal|null
operator|||
name|sc
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
block|}
specifier|private
name|void
name|checkSecurityContextEnd
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestParams
parameter_list|)
block|{
name|String
name|codeParam
init|=
name|requestParams
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|)
decl_stmt|;
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
name|sc
operator|==
literal|null
operator|||
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|codeParam
operator|==
literal|null
operator|&&
name|requestParams
operator|.
name|containsKey
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|)
operator|&&
operator|!
name|faultAccessDeniedResponses
condition|)
block|{
if|if
condition|(
operator|!
name|applicationCanHandleAccessDenied
condition|)
block|{
name|String
name|error
init|=
name|requestParams
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|)
decl_stmt|;
name|rc
operator|.
name|abortWith
argument_list|(
name|Response
operator|.
name|ok
argument_list|(
operator|new
name|AccessDeniedResponse
argument_list|(
name|error
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
block|}
specifier|private
name|Response
name|createCodeResponse
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
name|redirectState
init|=
name|createRedirectState
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|)
decl_stmt|;
name|String
name|theState
init|=
name|redirectState
operator|!=
literal|null
condition|?
name|redirectState
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
else|:
literal|null
decl_stmt|;
name|String
name|redirectScope
init|=
name|redirectState
operator|!=
literal|null
condition|?
name|redirectState
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
else|:
literal|null
decl_stmt|;
name|String
name|theScope
init|=
name|redirectScope
operator|!=
literal|null
condition|?
name|redirectScope
else|:
name|scopes
decl_stmt|;
name|UriBuilder
name|ub
init|=
name|OAuthClientUtils
operator|.
name|getAuthorizationURIBuilder
argument_list|(
name|authorizationServiceUri
argument_list|,
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|,
name|getAbsoluteRedirectUri
argument_list|(
name|ui
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|theState
argument_list|,
name|theScope
argument_list|)
decl_stmt|;
name|setFormPostResponseMode
argument_list|(
name|ub
argument_list|,
name|redirectState
argument_list|)
expr_stmt|;
name|setCodeVerifier
argument_list|(
name|ub
argument_list|,
name|redirectState
argument_list|)
expr_stmt|;
name|setAdditionalCodeRequestParams
argument_list|(
name|ub
argument_list|,
name|redirectState
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
name|ub
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|uri
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setFormPostResponseMode
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
parameter_list|)
block|{
if|if
condition|(
name|setFormPostResponseMode
condition|)
block|{
comment|// This property is described in OIDC OAuth 2.0 Form Post Response Mode which is technically
comment|// can be used without OIDC hence this is set in this filter as opposed to the OIDC specific one.
name|ub
operator|.
name|queryParam
argument_list|(
literal|"response_mode"
argument_list|,
literal|"form_post"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setCodeVerifier
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
parameter_list|)
block|{
if|if
condition|(
name|codeVerifierTransformer
operator|!=
literal|null
condition|)
block|{
name|String
name|codeVerifier
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|32
argument_list|)
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_CHALLENGE
argument_list|,
name|codeVerifierTransformer
operator|.
name|transformCodeVerifier
argument_list|(
name|codeVerifier
argument_list|)
argument_list|)
expr_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_CHALLENGE_METHOD
argument_list|,
name|codeVerifierTransformer
operator|.
name|getChallengeMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
parameter_list|)
block|{     }
specifier|private
name|URI
name|getAbsoluteRedirectUri
parameter_list|(
name|UriInfo
name|ui
parameter_list|)
block|{
if|if
condition|(
name|redirectUri
operator|!=
literal|null
condition|)
block|{
return|return
name|URI
operator|.
name|create
argument_list|(
name|redirectUri
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|completeUri
operator|!=
literal|null
condition|)
block|{
return|return
name|completeUri
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|?
name|URI
operator|.
name|create
argument_list|(
name|completeUri
argument_list|)
else|:
name|ui
operator|.
name|getBaseUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|completeUri
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|ui
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
block|}
specifier|protected
name|void
name|processCodeResponse
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|,
name|UriInfo
name|ui
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestParams
parameter_list|)
block|{
name|String
name|codeParam
init|=
name|requestParams
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|)
decl_stmt|;
name|ClientAccessToken
name|at
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|codeParam
operator|!=
literal|null
condition|)
block|{
name|AccessTokenGrant
name|grant
init|=
operator|new
name|AuthorizationCodeGrant
argument_list|(
name|codeParam
argument_list|,
name|getAbsoluteRedirectUri
argument_list|(
name|ui
argument_list|)
argument_list|)
decl_stmt|;
name|at
operator|=
name|OAuthClientUtils
operator|.
name|getAccessToken
argument_list|(
name|accessTokenServiceClient
argument_list|,
name|consumer
argument_list|,
name|grant
argument_list|)
expr_stmt|;
block|}
name|ClientTokenContext
name|tokenContext
init|=
name|initializeClientTokenContext
argument_list|(
name|rc
argument_list|,
name|at
argument_list|,
name|requestParams
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|!=
literal|null
operator|&&
name|clientTokenContextManager
operator|!=
literal|null
condition|)
block|{
name|clientTokenContextManager
operator|.
name|setClientTokenContext
argument_list|(
name|mc
argument_list|,
name|tokenContext
argument_list|)
expr_stmt|;
block|}
name|setClientCodeRequest
argument_list|(
name|tokenContext
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ClientTokenContext
name|initializeClientTokenContext
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
name|params
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
literal|null
decl_stmt|;
if|if
condition|(
name|clientStateManager
operator|!=
literal|null
condition|)
block|{
name|state
operator|=
name|clientStateManager
operator|.
name|fromRedirectState
argument_list|(
name|mc
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
name|ClientTokenContext
name|tokenContext
init|=
name|createTokenContext
argument_list|(
name|rc
argument_list|,
name|at
argument_list|,
name|state
argument_list|)
decl_stmt|;
operator|(
operator|(
name|ClientTokenContextImpl
operator|)
name|tokenContext
operator|)
operator|.
name|setToken
argument_list|(
name|at
argument_list|)
expr_stmt|;
operator|(
operator|(
name|ClientTokenContextImpl
operator|)
name|tokenContext
operator|)
operator|.
name|setState
argument_list|(
name|state
argument_list|)
expr_stmt|;
return|return
name|tokenContext
return|;
block|}
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
name|state
parameter_list|)
block|{
return|return
operator|new
name|ClientTokenContextImpl
argument_list|()
return|;
block|}
specifier|private
name|void
name|setClientCodeRequest
parameter_list|(
name|ClientTokenContext
name|request
parameter_list|)
block|{
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|ClientTokenContext
operator|.
name|class
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createRedirectState
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|,
name|UriInfo
name|ui
parameter_list|)
block|{
if|if
condition|(
name|clientStateManager
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|clientStateManager
operator|.
name|toRedirectState
argument_list|(
name|mc
argument_list|,
name|toCodeRequestState
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|)
argument_list|)
return|;
block|}
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
return|return
name|toRequestState
argument_list|(
name|rc
argument_list|,
name|ui
argument_list|)
return|;
block|}
specifier|protected
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toRequestState
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
name|requestState
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|requestState
operator|.
name|putAll
argument_list|(
name|ui
operator|.
name|getQueryParameters
argument_list|(
name|decodeRequestParameters
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED_TYPE
operator|.
name|isCompatible
argument_list|(
name|rc
operator|.
name|getMediaType
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|body
init|=
name|FormUtils
operator|.
name|readBody
argument_list|(
name|rc
operator|.
name|getEntityStream
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromString
argument_list|(
name|requestState
argument_list|,
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|body
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|,
name|decodeRequestParameters
argument_list|)
expr_stmt|;
block|}
return|return
name|requestState
return|;
block|}
specifier|public
name|void
name|setScopeList
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|list
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|setScopes
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setScopes
parameter_list|(
name|String
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setStartUri
parameter_list|(
name|String
name|relStartUri
parameter_list|)
block|{
name|this
operator|.
name|startUri
operator|=
name|relStartUri
expr_stmt|;
block|}
specifier|public
name|void
name|setAuthorizationServiceUri
parameter_list|(
name|String
name|authorizationServiceUri
parameter_list|)
block|{
name|this
operator|.
name|authorizationServiceUri
operator|=
name|authorizationServiceUri
expr_stmt|;
block|}
specifier|public
name|void
name|setCompleteUri
parameter_list|(
name|String
name|completeUri
parameter_list|)
block|{
name|this
operator|.
name|completeUri
operator|=
name|completeUri
expr_stmt|;
block|}
specifier|public
name|void
name|setAccessTokenServiceClient
parameter_list|(
name|WebClient
name|accessTokenServiceClient
parameter_list|)
block|{
name|this
operator|.
name|accessTokenServiceClient
operator|=
name|accessTokenServiceClient
expr_stmt|;
block|}
specifier|public
name|void
name|setClientCodeStateManager
parameter_list|(
name|ClientCodeStateManager
name|manager
parameter_list|)
block|{
name|this
operator|.
name|clientStateManager
operator|=
name|manager
expr_stmt|;
block|}
specifier|public
name|void
name|setClientTokenContextManager
parameter_list|(
name|ClientTokenContextManager
name|clientTokenContextManager
parameter_list|)
block|{
name|this
operator|.
name|clientTokenContextManager
operator|=
name|clientTokenContextManager
expr_stmt|;
block|}
specifier|public
name|void
name|setConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
specifier|public
name|void
name|setDecodeRequestParameters
parameter_list|(
name|boolean
name|decodeRequestParameters
parameter_list|)
block|{
name|this
operator|.
name|decodeRequestParameters
operator|=
name|decodeRequestParameters
expr_stmt|;
block|}
specifier|protected
name|ClientTokenContext
name|getClientTokenContext
parameter_list|(
name|ContainerRequestContext
name|rc
parameter_list|)
block|{
name|ClientTokenContext
name|ctx
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|clientTokenContextManager
operator|!=
literal|null
condition|)
block|{
name|ctx
operator|=
name|clientTokenContextManager
operator|.
name|getClientTokenContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
name|ClientAccessToken
name|newAt
init|=
name|refreshAccessTokenIfExpired
argument_list|(
name|ctx
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|newAt
operator|!=
literal|null
condition|)
block|{
name|clientTokenContextManager
operator|.
name|removeClientTokenContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|ClientTokenContext
name|newCtx
init|=
name|initializeClientTokenContext
argument_list|(
name|rc
argument_list|,
name|newAt
argument_list|,
name|ctx
operator|.
name|getState
argument_list|()
argument_list|)
decl_stmt|;
name|clientTokenContextManager
operator|.
name|setClientTokenContext
argument_list|(
name|mc
argument_list|,
name|newCtx
argument_list|)
expr_stmt|;
name|ctx
operator|=
name|newCtx
expr_stmt|;
block|}
block|}
block|}
return|return
name|ctx
return|;
block|}
specifier|private
name|ClientAccessToken
name|refreshAccessTokenIfExpired
parameter_list|(
name|ClientAccessToken
name|at
parameter_list|)
block|{
if|if
condition|(
name|at
operator|.
name|getRefreshToken
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
operator|(
name|expiryThreshold
operator|>
literal|0
operator|&&
name|OAuthUtils
operator|.
name|isExpired
argument_list|(
name|at
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|at
operator|.
name|getExpiresIn
argument_list|()
operator|-
name|expiryThreshold
argument_list|)
operator|)
operator|||
name|OAuthUtils
operator|.
name|isExpired
argument_list|(
name|at
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|at
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return
name|OAuthClientUtils
operator|.
name|refreshAccessToken
argument_list|(
name|accessTokenServiceClient
argument_list|,
name|consumer
argument_list|,
name|at
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setExpiryThreshold
parameter_list|(
name|long
name|expiryThreshold
parameter_list|)
block|{
name|this
operator|.
name|expiryThreshold
operator|=
name|expiryThreshold
expr_stmt|;
block|}
specifier|public
name|void
name|setRedirectUri
parameter_list|(
name|String
name|redirectUri
parameter_list|)
block|{
comment|// Can be set to something like "postmessage" in some flows
name|this
operator|.
name|redirectUri
operator|=
name|redirectUri
expr_stmt|;
block|}
specifier|public
name|void
name|setSetFormPostResponseMode
parameter_list|(
name|boolean
name|setFormPostResponseMode
parameter_list|)
block|{
name|this
operator|.
name|setFormPostResponseMode
operator|=
name|setFormPostResponseMode
expr_stmt|;
block|}
specifier|public
name|void
name|setBlockAccessDeniedResponses
parameter_list|(
name|boolean
name|blockAccessDeniedResponses
parameter_list|)
block|{
name|this
operator|.
name|faultAccessDeniedResponses
operator|=
name|blockAccessDeniedResponses
expr_stmt|;
block|}
specifier|public
name|void
name|setApplicationCanHandleAccessDenied
parameter_list|(
name|boolean
name|applicationCanHandleAccessDenied
parameter_list|)
block|{
name|this
operator|.
name|applicationCanHandleAccessDenied
operator|=
name|applicationCanHandleAccessDenied
expr_stmt|;
block|}
specifier|public
name|void
name|setCodeVerifierTransformer
parameter_list|(
name|CodeVerifierTransformer
name|codeVerifierTransformer
parameter_list|)
block|{
name|this
operator|.
name|codeVerifierTransformer
operator|=
name|codeVerifierTransformer
expr_stmt|;
block|}
block|}
end_class

end_unit

