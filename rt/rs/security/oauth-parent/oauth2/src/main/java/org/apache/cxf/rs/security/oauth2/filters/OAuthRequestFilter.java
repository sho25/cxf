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
name|filters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|Collections
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
name|logging
operator|.
name|Logger
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|HttpMethod
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
name|Form
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
name|ext
operator|.
name|Provider
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
name|logging
operator|.
name|LogUtils
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
name|security
operator|.
name|SimplePrincipal
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
name|provider
operator|.
name|FormEncodingProvider
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
name|MessageUtils
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|AccessTokenValidation
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
name|AuthenticationMethod
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
name|OAuthContext
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
name|services
operator|.
name|AbstractAccessTokenValidator
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
name|AuthorizationUtils
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
comment|/**  * JAX-RS OAuth2 filter which can be used to protect the end-user endpoints  */
end_comment

begin_class
annotation|@
name|Provider
annotation|@
name|PreMatching
comment|// Priorities.AUTHORIZATION also works
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
argument_list|)
specifier|public
class|class
name|OAuthRequestFilter
extends|extends
name|AbstractAccessTokenValidator
implements|implements
name|ContainerRequestFilter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|OAuthRequestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|useUserSubject
decl_stmt|;
specifier|private
name|String
name|audience
decl_stmt|;
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|boolean
name|completeAudienceMatch
decl_stmt|;
specifier|private
name|boolean
name|audienceIsEndpointAddress
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|checkFormData
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|requiredScopes
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|allPermissionsMatch
decl_stmt|;
specifier|private
name|boolean
name|blockPublicClients
decl_stmt|;
specifier|private
name|AuthenticationMethod
name|am
decl_stmt|;
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
block|{
name|validateRequest
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|validateRequest
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|isCorsRequest
argument_list|(
name|m
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// Get the scheme and its data, Bearer only is supported by default
comment|// WWW-Authenticate with the list of supported schemes will be sent back
comment|// if the scheme is not accepted
name|String
index|[]
name|authParts
init|=
name|getAuthorizationParts
argument_list|(
name|m
argument_list|)
decl_stmt|;
if|if
condition|(
name|authParts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toForbiddenException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|String
name|authScheme
init|=
name|authParts
index|[
literal|0
index|]
decl_stmt|;
name|String
name|authSchemeData
init|=
name|authParts
index|[
literal|1
index|]
decl_stmt|;
comment|// Get the access token
name|AccessTokenValidation
name|accessTokenV
init|=
name|getAccessTokenValidation
argument_list|(
name|authScheme
argument_list|,
name|authSchemeData
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|accessTokenV
operator|.
name|isInitialValidationSuccessful
argument_list|()
condition|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
comment|// Check audiences
name|String
name|validAudience
init|=
name|validateAudiences
argument_list|(
name|accessTokenV
operator|.
name|getAudiences
argument_list|()
argument_list|)
decl_stmt|;
comment|// Check if token was issued by the supported issuer
if|if
condition|(
name|issuer
operator|!=
literal|null
operator|&&
name|issuer
operator|.
name|equals
argument_list|(
name|accessTokenV
operator|.
name|getTokenIssuer
argument_list|()
argument_list|)
condition|)
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
comment|// Find the scopes which match the current request
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
init|=
name|accessTokenV
operator|.
name|getTokenScopes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|matchingPermissions
init|=
operator|new
name|ArrayList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
name|HttpServletRequest
name|req
init|=
name|getMessageContext
argument_list|()
operator|.
name|getHttpServletRequest
argument_list|()
decl_stmt|;
for|for
control|(
name|OAuthPermission
name|perm
range|:
name|permissions
control|)
block|{
name|boolean
name|uriOK
init|=
name|checkRequestURI
argument_list|(
name|req
argument_list|,
name|perm
operator|.
name|getUris
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|verbOK
init|=
name|checkHttpVerb
argument_list|(
name|req
argument_list|,
name|perm
operator|.
name|getHttpVerbs
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|scopeOk
init|=
name|checkScopeProperty
argument_list|(
name|perm
operator|.
name|getPermission
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriOK
operator|&&
name|verbOK
operator|&&
name|scopeOk
condition|)
block|{
name|matchingPermissions
operator|.
name|add
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|permissions
operator|.
name|isEmpty
argument_list|()
operator|&&
name|matchingPermissions
operator|.
name|isEmpty
argument_list|()
operator|||
name|allPermissionsMatch
operator|&&
operator|(
name|matchingPermissions
operator|.
name|size
argument_list|()
operator|!=
name|permissions
operator|.
name|size
argument_list|()
operator|)
operator|||
operator|!
name|requiredScopes
operator|.
name|isEmpty
argument_list|()
operator|&&
name|requiredScopes
operator|.
name|size
argument_list|()
operator|!=
name|matchingPermissions
operator|.
name|size
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"Client has no valid permissions"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toForbiddenException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
if|if
condition|(
name|accessTokenV
operator|.
name|getClientIpAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|remoteAddress
init|=
name|getMessageContext
argument_list|()
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getRemoteAddr
argument_list|()
decl_stmt|;
if|if
condition|(
name|remoteAddress
operator|==
literal|null
operator|||
name|accessTokenV
operator|.
name|getClientIpAddress
argument_list|()
operator|.
name|matches
argument_list|(
name|remoteAddress
argument_list|)
condition|)
block|{
name|String
name|message
init|=
literal|"Client IP Address is invalid"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toForbiddenException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|blockPublicClients
operator|&&
operator|!
name|accessTokenV
operator|.
name|isClientConfidential
argument_list|()
condition|)
block|{
name|String
name|message
init|=
literal|"Only Confidential Clients are supported"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toForbiddenException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
if|if
condition|(
name|am
operator|!=
literal|null
operator|&&
operator|!
name|am
operator|.
name|equals
argument_list|(
name|accessTokenV
operator|.
name|getTokenSubject
argument_list|()
operator|.
name|getAuthenticationMethod
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|message
init|=
literal|"The token has been authorized by the resource owner "
operator|+
literal|"using an unsupported authentication method"
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toForbiddenException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
comment|// Create the security context and make it available on the message
name|SecurityContext
name|sc
init|=
name|createSecurityContext
argument_list|(
name|req
argument_list|,
name|accessTokenV
argument_list|)
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
comment|// Also set the OAuthContext
name|OAuthContext
name|oauthContext
init|=
operator|new
name|OAuthContext
argument_list|(
name|accessTokenV
operator|.
name|getTokenSubject
argument_list|()
argument_list|,
name|accessTokenV
operator|.
name|getClientSubject
argument_list|()
argument_list|,
name|matchingPermissions
argument_list|,
name|accessTokenV
operator|.
name|getTokenGrantType
argument_list|()
argument_list|)
decl_stmt|;
name|oauthContext
operator|.
name|setClientId
argument_list|(
name|accessTokenV
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|oauthContext
operator|.
name|setClientConfidential
argument_list|(
name|accessTokenV
operator|.
name|isClientConfidential
argument_list|()
argument_list|)
expr_stmt|;
name|oauthContext
operator|.
name|setTokenKey
argument_list|(
name|accessTokenV
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|oauthContext
operator|.
name|setTokenAudience
argument_list|(
name|validAudience
argument_list|)
expr_stmt|;
name|oauthContext
operator|.
name|setTokenIssuer
argument_list|(
name|accessTokenV
operator|.
name|getTokenIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|oauthContext
operator|.
name|setTokenRequestParts
argument_list|(
name|authParts
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|OAuthContext
operator|.
name|class
argument_list|,
name|oauthContext
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|checkHttpVerb
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|verbs
parameter_list|)
block|{
if|if
condition|(
operator|!
name|verbs
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|verbs
operator|.
name|contains
argument_list|(
name|req
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|message
init|=
literal|"Invalid http verb"
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|boolean
name|checkRequestURI
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|uris
parameter_list|)
block|{
if|if
condition|(
name|uris
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|String
name|servletPath
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|boolean
name|foundValidScope
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|uri
range|:
name|uris
control|)
block|{
if|if
condition|(
name|OAuthUtils
operator|.
name|checkRequestURI
argument_list|(
name|servletPath
argument_list|,
name|uri
argument_list|)
condition|)
block|{
name|foundValidScope
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|foundValidScope
condition|)
block|{
name|String
name|message
init|=
literal|"Invalid request URI: "
operator|+
name|request
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return
name|foundValidScope
return|;
block|}
specifier|protected
name|boolean
name|checkScopeProperty
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
if|if
condition|(
operator|!
name|requiredScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|requiredScopes
operator|.
name|contains
argument_list|(
name|scope
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
specifier|public
name|void
name|setUseUserSubject
parameter_list|(
name|boolean
name|useUserSubject
parameter_list|)
block|{
name|this
operator|.
name|useUserSubject
operator|=
name|useUserSubject
expr_stmt|;
block|}
specifier|protected
name|SecurityContext
name|createSecurityContext
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|AccessTokenValidation
name|accessTokenV
parameter_list|)
block|{
name|UserSubject
name|resourceOwnerSubject
init|=
name|accessTokenV
operator|.
name|getTokenSubject
argument_list|()
decl_stmt|;
name|UserSubject
name|clientSubject
init|=
name|accessTokenV
operator|.
name|getClientSubject
argument_list|()
decl_stmt|;
specifier|final
name|UserSubject
name|theSubject
init|=
name|OAuthRequestFilter
operator|.
name|this
operator|.
name|useUserSubject
condition|?
name|resourceOwnerSubject
else|:
name|clientSubject
decl_stmt|;
return|return
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|theSubject
operator|!=
literal|null
condition|?
operator|new
name|SimplePrincipal
argument_list|(
name|theSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
if|if
condition|(
name|theSubject
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|theSubject
operator|.
name|getRoles
argument_list|()
operator|.
name|contains
argument_list|(
name|role
argument_list|)
return|;
block|}
block|}
return|;
block|}
specifier|protected
name|boolean
name|isCorsRequest
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
comment|//Redirection-based flows (Implicit Grant Flow specifically) may have
comment|//the browser issuing CORS preflight OPTIONS request.
comment|//org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter can be
comment|//used to handle preflights but local preflights (to be handled by the service code)
comment|// will be blocked by this filter unless CORS filter has done the initial validation
comment|// and set a message "local_preflight" property to true
return|return
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|m
operator|.
name|get
argument_list|(
literal|"local_preflight"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|String
name|validateAudiences
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|audiences
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|audiences
argument_list|)
operator|&&
name|audience
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|audience
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|audiences
operator|.
name|contains
argument_list|(
name|audience
argument_list|)
condition|)
block|{
return|return
name|audience
return|;
block|}
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|audienceIsEndpointAddress
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|requestPath
init|=
operator|(
name|String
operator|)
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUEST_URL
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|audiences
control|)
block|{
name|boolean
name|matched
init|=
name|completeAudienceMatch
condition|?
name|requestPath
operator|.
name|equals
argument_list|(
name|s
argument_list|)
else|:
name|requestPath
operator|.
name|startsWith
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|matched
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setCheckFormData
parameter_list|(
name|boolean
name|checkFormData
parameter_list|)
block|{
name|this
operator|.
name|checkFormData
operator|=
name|checkFormData
expr_stmt|;
block|}
specifier|protected
name|String
index|[]
name|getAuthorizationParts
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
operator|!
name|checkFormData
condition|)
block|{
return|return
name|AuthorizationUtils
operator|.
name|getAuthorizationParts
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|supportedSchemes
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|String
index|[]
block|{
name|OAuthConstants
operator|.
name|BEARER_AUTHORIZATION_SCHEME
block|,
name|getTokenFromFormData
argument_list|(
name|m
argument_list|)
block|}
return|;
block|}
block|}
specifier|protected
name|String
name|getTokenFromFormData
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
name|String
name|type
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
operator|.
name|startsWith
argument_list|(
name|type
argument_list|)
operator|&&
name|method
operator|!=
literal|null
operator|&&
operator|(
name|method
operator|.
name|equals
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|)
operator|||
name|method
operator|.
name|equals
argument_list|(
name|HttpMethod
operator|.
name|PUT
argument_list|)
operator|)
condition|)
block|{
try|try
block|{
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|provider
init|=
operator|new
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|Form
name|form
init|=
name|FormUtils
operator|.
name|readForm
argument_list|(
name|provider
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|formData
init|=
name|form
operator|.
name|asMap
argument_list|()
decl_stmt|;
name|String
name|token
init|=
name|formData
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|FormUtils
operator|.
name|restoreForm
argument_list|(
name|provider
argument_list|,
name|form
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// the exception will be thrown below
block|}
block|}
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|supportedSchemes
argument_list|,
name|realm
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setRequiredScopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requiredScopes
parameter_list|)
block|{
name|this
operator|.
name|requiredScopes
operator|=
name|requiredScopes
expr_stmt|;
block|}
specifier|public
name|void
name|setAllPermissionsMatch
parameter_list|(
name|boolean
name|allPermissionsMatch
parameter_list|)
block|{
name|this
operator|.
name|allPermissionsMatch
operator|=
name|allPermissionsMatch
expr_stmt|;
block|}
specifier|public
name|void
name|setBlockPublicClients
parameter_list|(
name|boolean
name|blockPublicClients
parameter_list|)
block|{
name|this
operator|.
name|blockPublicClients
operator|=
name|blockPublicClients
expr_stmt|;
block|}
specifier|public
name|void
name|setTokenSubjectAuthenticationMethod
parameter_list|(
name|AuthenticationMethod
name|method
parameter_list|)
block|{
name|this
operator|.
name|am
operator|=
name|method
expr_stmt|;
block|}
specifier|public
name|String
name|getAudience
parameter_list|()
block|{
return|return
name|audience
return|;
block|}
specifier|public
name|void
name|setAudience
parameter_list|(
name|String
name|audience
parameter_list|)
block|{
name|this
operator|.
name|audience
operator|=
name|audience
expr_stmt|;
block|}
specifier|public
name|boolean
name|isCompleteAudienceMatch
parameter_list|()
block|{
return|return
name|completeAudienceMatch
return|;
block|}
specifier|public
name|void
name|setCompleteAudienceMatch
parameter_list|(
name|boolean
name|completeAudienceMatch
parameter_list|)
block|{
name|this
operator|.
name|completeAudienceMatch
operator|=
name|completeAudienceMatch
expr_stmt|;
block|}
specifier|public
name|void
name|setAudienceIsEndpointAddress
parameter_list|(
name|boolean
name|audienceIsEndpointAddress
parameter_list|)
block|{
name|this
operator|.
name|audienceIsEndpointAddress
operator|=
name|audienceIsEndpointAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
block|}
end_class

end_unit

