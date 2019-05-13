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
name|oauth
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
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
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
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequestWrapper
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuth
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthMessage
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthProblemException
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|OAuthValidator
import|;
end_import

begin_import
import|import
name|net
operator|.
name|oauth
operator|.
name|server
operator|.
name|OAuthServlet
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|oauth
operator|.
name|data
operator|.
name|AccessToken
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|data
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
name|oauth
operator|.
name|provider
operator|.
name|DefaultOAuthValidator
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
name|oauth
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
name|oauth
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
name|oauth
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
comment|/**  * Base OAuth filter which can be used to protect end-user endpoints  */
end_comment

begin_class
specifier|public
class|class
name|AbstractAuthFilter
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
name|AbstractAuthFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|REQUIRED_PARAMETERS
init|=
operator|new
name|String
index|[]
block|{
name|OAuth
operator|.
name|OAUTH_CONSUMER_KEY
block|,
name|OAuth
operator|.
name|OAUTH_TOKEN
block|,
name|OAuth
operator|.
name|OAUTH_SIGNATURE_METHOD
block|,
name|OAuth
operator|.
name|OAUTH_SIGNATURE
block|,
name|OAuth
operator|.
name|OAUTH_TIMESTAMP
block|,
name|OAuth
operator|.
name|OAUTH_NONCE
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ALLOWED_OAUTH_PARAMETERS
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|REQUIRED_PARAMETERS
argument_list|)
argument_list|)
decl_stmt|;
static|static
block|{
name|ALLOWED_OAUTH_PARAMETERS
operator|.
name|add
argument_list|(
name|OAuth
operator|.
name|OAUTH_VERSION
argument_list|)
expr_stmt|;
name|ALLOWED_OAUTH_PARAMETERS
operator|.
name|add
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_CONSUMER_SECRET
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|supportUnknownParameters
decl_stmt|;
specifier|private
name|boolean
name|useUserSubject
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|private
name|OAuthValidator
name|validator
init|=
operator|new
name|DefaultOAuthValidator
argument_list|()
decl_stmt|;
specifier|protected
name|AbstractAuthFilter
parameter_list|()
block|{      }
comment|/**      * Sets {@link OAuthDataProvider} provider.      * @param provider the provider      */
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|provider
parameter_list|)
block|{
name|dataProvider
operator|=
name|provider
expr_stmt|;
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
specifier|public
name|boolean
name|isUseUserSubject
parameter_list|()
block|{
return|return
name|useUserSubject
return|;
block|}
comment|/**      * Authenticates the third-party consumer and returns      * {@link OAuthInfo} bean capturing the information about the request.      * @param req http request      * @return OAuth info      * @see OAuthInfo      * @throws Exception      * @throws OAuthProblemException      */
specifier|protected
name|OAuthInfo
name|handleOAuthRequest
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
throws|throws
name|Exception
throws|,
name|OAuthProblemException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"OAuth security filter for url: {0}"
argument_list|,
name|req
operator|.
name|getRequestURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|AccessToken
name|accessToken
init|=
literal|null
decl_stmt|;
name|Client
name|client
init|=
literal|null
decl_stmt|;
name|OAuthMessage
name|oAuthMessage
init|=
name|OAuthServlet
operator|.
name|getMessage
argument_list|(
operator|new
name|CustomHttpServletWrapper
argument_list|(
name|req
argument_list|)
argument_list|,
name|OAuthServlet
operator|.
name|getRequestURL
argument_list|(
name|req
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|oAuthMessage
operator|.
name|requireParameters
argument_list|(
name|REQUIRED_PARAMETERS
argument_list|)
expr_stmt|;
name|accessToken
operator|=
name|dataProvider
operator|.
name|getAccessToken
argument_list|(
name|oAuthMessage
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
comment|//check if access token is not null
if|if
condition|(
name|accessToken
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Access token is unavailable"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|TOKEN_REJECTED
argument_list|)
throw|;
block|}
name|client
operator|=
name|accessToken
operator|.
name|getClient
argument_list|()
expr_stmt|;
name|OAuthUtils
operator|.
name|validateMessage
argument_list|(
name|oAuthMessage
argument_list|,
name|client
argument_list|,
name|accessToken
argument_list|,
name|dataProvider
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|consumerKey
init|=
literal|null
decl_stmt|;
name|String
name|consumerSecret
init|=
literal|null
decl_stmt|;
name|String
name|authHeader
init|=
name|oAuthMessage
operator|.
name|getHeader
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
if|if
condition|(
name|authHeader
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|authHeader
operator|.
name|startsWith
argument_list|(
literal|"OAuth"
argument_list|)
condition|)
block|{
name|consumerKey
operator|=
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|OAuth
operator|.
name|OAUTH_CONSUMER_KEY
argument_list|)
expr_stmt|;
name|consumerSecret
operator|=
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|OAuthConstants
operator|.
name|OAUTH_CONSUMER_SECRET
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|authHeader
operator|.
name|startsWith
argument_list|(
literal|"Basic"
argument_list|)
condition|)
block|{
name|AuthorizationPolicy
name|policy
init|=
name|getAuthorizationPolicy
argument_list|(
name|authHeader
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
name|consumerKey
operator|=
name|policy
operator|.
name|getUserName
argument_list|()
expr_stmt|;
name|consumerSecret
operator|=
name|policy
operator|.
name|getPassword
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|consumerKey
operator|!=
literal|null
condition|)
block|{
name|client
operator|=
name|dataProvider
operator|.
name|getClient
argument_list|(
name|consumerKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Client is invalid"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|CONSUMER_KEY_UNKNOWN
argument_list|)
throw|;
block|}
if|if
condition|(
name|consumerSecret
operator|!=
literal|null
operator|&&
operator|!
name|consumerSecret
operator|.
name|equals
argument_list|(
name|client
operator|.
name|getSecretKey
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Client secret is invalid"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|CONSUMER_KEY_UNKNOWN
argument_list|)
throw|;
block|}
name|OAuthUtils
operator|.
name|validateMessage
argument_list|(
name|oAuthMessage
argument_list|,
name|client
argument_list|,
literal|null
argument_list|,
name|dataProvider
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|accessToken
operator|=
name|client
operator|.
name|getPreAuthorizedToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|accessToken
operator|==
literal|null
operator|||
operator|!
name|accessToken
operator|.
name|isPreAuthorized
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Preauthorized access token is unavailable"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OAuthProblemException
argument_list|(
name|OAuth
operator|.
name|Problems
operator|.
name|TOKEN_REJECTED
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
init|=
name|accessToken
operator|.
name|getScopes
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
argument_list|<>
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
if|if
condition|(
name|uriOK
operator|&&
name|verbOK
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
operator|new
name|OAuthProblemException
argument_list|(
name|message
argument_list|)
throw|;
block|}
return|return
operator|new
name|OAuthInfo
argument_list|(
name|accessToken
argument_list|,
name|matchingPermissions
argument_list|)
return|;
block|}
specifier|protected
name|AuthorizationPolicy
name|getAuthorizationPolicy
parameter_list|(
name|String
name|authorizationHeader
parameter_list|)
block|{
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
return|return
name|m
operator|!=
literal|null
condition|?
name|m
operator|.
name|get
argument_list|(
name|AuthorizationPolicy
operator|.
name|class
argument_list|)
else|:
literal|null
return|;
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
name|warning
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
name|SecurityContext
name|createSecurityContext
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
specifier|final
name|OAuthInfo
name|info
parameter_list|)
block|{
comment|// TODO:
comment|// This custom parameter is only needed by the "oauth"
comment|// demo shipped in the distribution; needs to be removed.
name|request
operator|.
name|setAttribute
argument_list|(
literal|"oauth_authorities"
argument_list|,
name|info
operator|.
name|getRoles
argument_list|()
argument_list|)
expr_stmt|;
name|UserSubject
name|subject
init|=
name|info
operator|.
name|getToken
argument_list|()
operator|.
name|getSubject
argument_list|()
decl_stmt|;
specifier|final
name|UserSubject
name|theSubject
init|=
name|subject
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
name|String
name|login
init|=
name|AbstractAuthFilter
operator|.
name|this
operator|.
name|useUserSubject
condition|?
operator|(
name|theSubject
operator|!=
literal|null
condition|?
name|theSubject
operator|.
name|getLogin
argument_list|()
else|:
literal|null
operator|)
else|:
name|info
operator|.
name|getToken
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|getLoginName
argument_list|()
decl_stmt|;
return|return
operator|new
name|SimplePrincipal
argument_list|(
name|login
argument_list|)
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
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|AbstractAuthFilter
operator|.
name|this
operator|.
name|useUserSubject
operator|&&
name|theSubject
operator|!=
literal|null
condition|)
block|{
name|roles
operator|=
name|theSubject
operator|.
name|getRoles
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|roles
operator|=
name|info
operator|.
name|getRoles
argument_list|()
expr_stmt|;
block|}
return|return
name|roles
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
name|OAuthContext
name|createOAuthContext
parameter_list|(
name|OAuthInfo
name|info
parameter_list|)
block|{
name|UserSubject
name|subject
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|subject
operator|=
name|info
operator|.
name|getToken
argument_list|()
operator|.
name|getSubject
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|OAuthContext
argument_list|(
name|subject
argument_list|,
name|info
operator|.
name|getMatchedPermissions
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setValidator
parameter_list|(
name|OAuthValidator
name|validator
parameter_list|)
block|{
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
specifier|public
name|void
name|setSupportUnknownParameters
parameter_list|(
name|boolean
name|supportUnknownParameters
parameter_list|)
block|{
name|this
operator|.
name|supportUnknownParameters
operator|=
name|supportUnknownParameters
expr_stmt|;
block|}
specifier|private
class|class
name|CustomHttpServletWrapper
extends|extends
name|HttpServletRequestWrapper
block|{
name|CustomHttpServletWrapper
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
name|super
argument_list|(
name|req
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|getParameterMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|params
init|=
name|super
operator|.
name|getParameterMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|supportUnknownParameters
operator|||
name|ALLOWED_OAUTH_PARAMETERS
operator|.
name|containsAll
argument_list|(
name|params
operator|.
name|keySet
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|params
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|newParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
index|[]
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|ALLOWED_OAUTH_PARAMETERS
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|newParams
operator|.
name|put
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
expr_stmt|;
block|}
block|}
return|return
name|newParams
return|;
block|}
block|}
block|}
end_class

end_unit

