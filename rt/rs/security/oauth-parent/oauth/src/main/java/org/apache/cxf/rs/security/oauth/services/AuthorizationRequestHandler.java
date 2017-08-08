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
name|services
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
name|java
operator|.
name|util
operator|.
name|UUID
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
name|HttpServletResponse
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
name|HttpSession
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
name|oauth
operator|.
name|data
operator|.
name|AuthorizationInput
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
name|RequestToken
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
name|LoginSecurityContext
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

begin_class
specifier|public
class|class
name|AuthorizationRequestHandler
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
name|AuthorizationRequestHandler
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
name|OAUTH_TOKEN
block|}
decl_stmt|;
specifier|public
name|Response
name|handle
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|HttpServletRequest
name|request
init|=
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
decl_stmt|;
try|try
block|{
name|OAuthMessage
name|oAuthMessage
init|=
name|OAuthUtils
operator|.
name|getOAuthMessage
argument_list|(
name|mc
argument_list|,
name|request
argument_list|,
name|REQUIRED_PARAMETERS
argument_list|)
decl_stmt|;
operator|new
name|DefaultOAuthValidator
argument_list|()
operator|.
name|checkSingleParameter
argument_list|(
name|oAuthMessage
argument_list|)
expr_stmt|;
name|RequestToken
name|token
init|=
name|dataProvider
operator|.
name|getRequestToken
argument_list|(
name|oAuthMessage
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
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
name|String
name|decision
init|=
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_DECISION_KEY
argument_list|)
decl_stmt|;
name|OAuthAuthorizationData
name|secData
init|=
operator|new
name|OAuthAuthorizationData
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareRequestSessionTokens
argument_list|(
name|request
argument_list|,
name|oAuthMessage
argument_list|)
condition|)
block|{
if|if
condition|(
name|decision
operator|!=
literal|null
condition|)
block|{
comment|// this is a user decision request, the session has expired or been possibly hijacked
name|LOG
operator|.
name|warning
argument_list|(
literal|"Session authenticity token is missing or invalid"
argument_list|)
expr_stmt|;
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
comment|// assume it is an initial authorization request
name|addAuthenticityTokenToSession
argument_list|(
name|secData
argument_list|,
name|request
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|addAdditionalParams
argument_list|(
name|secData
argument_list|,
name|dataProvider
argument_list|,
name|token
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|allow
condition|)
block|{
name|SecurityContext
name|sc
init|=
operator|(
name|SecurityContext
operator|)
name|mc
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
name|List
argument_list|<
name|String
argument_list|>
name|roleNames
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
if|if
condition|(
name|sc
operator|instanceof
name|LoginSecurityContext
condition|)
block|{
name|roleNames
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
operator|(
operator|(
name|LoginSecurityContext
operator|)
name|sc
operator|)
operator|.
name|getUserRoles
argument_list|()
decl_stmt|;
for|for
control|(
name|Principal
name|p
range|:
name|roles
control|)
block|{
name|roleNames
operator|.
name|add
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|token
operator|.
name|setSubject
argument_list|(
operator|new
name|UserSubject
argument_list|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|roleNames
argument_list|)
argument_list|)
expr_stmt|;
name|AuthorizationInput
name|input
init|=
operator|new
name|AuthorizationInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|OAuthPermission
argument_list|>
name|approvedScopesSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|originalScopes
init|=
name|token
operator|.
name|getScopes
argument_list|()
decl_stmt|;
for|for
control|(
name|OAuthPermission
name|perm
range|:
name|originalScopes
control|)
block|{
name|String
name|param
init|=
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|perm
operator|.
name|getPermission
argument_list|()
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
name|approvedScopesSet
operator|.
name|add
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|approvedScopes
init|=
operator|new
name|LinkedList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|(
name|approvedScopesSet
argument_list|)
decl_stmt|;
if|if
condition|(
name|approvedScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|approvedScopes
operator|=
name|originalScopes
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|approvedScopes
operator|.
name|size
argument_list|()
operator|<
name|originalScopes
operator|.
name|size
argument_list|()
condition|)
block|{
for|for
control|(
name|OAuthPermission
name|perm
range|:
name|originalScopes
control|)
block|{
if|if
condition|(
name|perm
operator|.
name|isDefault
argument_list|()
operator|&&
operator|!
name|approvedScopes
operator|.
name|contains
argument_list|(
name|perm
argument_list|)
condition|)
block|{
name|approvedScopes
operator|.
name|add
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|input
operator|.
name|setApprovedScopes
argument_list|(
name|approvedScopes
argument_list|)
expr_stmt|;
name|String
name|verifier
init|=
name|dataProvider
operator|.
name|finalizeAuthorization
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|queryParams
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_VERIFIER
argument_list|,
name|verifier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataProvider
operator|.
name|removeToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|queryParams
operator|.
name|put
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|,
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|getState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|queryParams
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|X_OAUTH_STATE
argument_list|,
name|token
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|callbackValue
init|=
name|getCallbackValue
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|OAuthConstants
operator|.
name|OAUTH_CALLBACK_OOB
operator|.
name|equals
argument_list|(
name|callbackValue
argument_list|)
condition|)
block|{
name|OOBAuthorizationResponse
name|bean
init|=
name|convertQueryParamsToOOB
argument_list|(
name|queryParams
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|entity
argument_list|(
name|bean
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
name|URI
name|callbackURI
init|=
name|buildCallbackURI
argument_list|(
name|callbackValue
argument_list|,
name|queryParams
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|callbackURI
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OAuthProblemException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"An OAuth related problem: {0}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
operator|.
name|fillInStackTrace
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|int
name|code
init|=
name|e
operator|.
name|getHttpStatusCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|code
operator|==
name|HttpServletResponse
operator|.
name|SC_OK
condition|)
block|{
name|code
operator|=
name|e
operator|.
name|getProblem
argument_list|()
operator|==
name|OAuth
operator|.
name|Problems
operator|.
name|CONSUMER_KEY_UNKNOWN
condition|?
literal|401
else|:
literal|400
expr_stmt|;
block|}
return|return
name|OAuthUtils
operator|.
name|handleException
argument_list|(
name|mc
argument_list|,
name|e
argument_list|,
name|code
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|e
parameter_list|)
block|{
return|return
name|OAuthUtils
operator|.
name|handleException
argument_list|(
name|mc
argument_list|,
name|e
argument_list|,
name|HttpServletResponse
operator|.
name|SC_BAD_REQUEST
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Unexpected internal server exception: {0}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
operator|.
name|fillInStackTrace
argument_list|()
block|}
argument_list|)
expr_stmt|;
return|return
name|OAuthUtils
operator|.
name|handleException
argument_list|(
name|mc
argument_list|,
name|e
argument_list|,
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
return|;
block|}
block|}
specifier|protected
name|String
name|getCallbackValue
parameter_list|(
name|RequestToken
name|token
parameter_list|)
throws|throws
name|OAuthProblemException
block|{
name|String
name|callback
init|=
name|token
operator|.
name|getCallback
argument_list|()
decl_stmt|;
if|if
condition|(
name|callback
operator|==
literal|null
condition|)
block|{
name|callback
operator|=
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getApplicationURI
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|callback
operator|==
literal|null
condition|)
block|{
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
return|return
name|callback
return|;
block|}
specifier|private
name|URI
name|buildCallbackURI
parameter_list|(
name|String
name|callback
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
parameter_list|)
block|{
name|UriBuilder
name|builder
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|callback
argument_list|)
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
argument_list|>
name|entry
range|:
name|queryParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builder
operator|.
name|queryParam
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
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|OOBAuthorizationResponse
name|convertQueryParamsToOOB
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParams
parameter_list|)
block|{
name|OOBAuthorizationResponse
name|oob
init|=
operator|new
name|OOBAuthorizationResponse
argument_list|()
decl_stmt|;
name|oob
operator|.
name|setRequestToken
argument_list|(
name|queryParams
operator|.
name|get
argument_list|(
name|OAuth
operator|.
name|OAUTH_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
name|oob
operator|.
name|setVerifier
argument_list|(
name|queryParams
operator|.
name|get
argument_list|(
name|OAuth
operator|.
name|OAUTH_VERIFIER
argument_list|)
argument_list|)
expr_stmt|;
name|oob
operator|.
name|setState
argument_list|(
name|queryParams
operator|.
name|get
argument_list|(
literal|"state"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|oob
return|;
block|}
specifier|protected
name|OAuthAuthorizationData
name|addAdditionalParams
parameter_list|(
name|OAuthAuthorizationData
name|secData
parameter_list|,
name|OAuthDataProvider
name|dataProvider
parameter_list|,
name|RequestToken
name|token
parameter_list|)
throws|throws
name|OAuthProblemException
block|{
name|secData
operator|.
name|setOauthToken
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationName
argument_list|(
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getApplicationName
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationURI
argument_list|(
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getApplicationURI
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setCallbackURI
argument_list|(
name|getCallbackValue
argument_list|(
name|token
argument_list|)
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setApplicationDescription
argument_list|(
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getApplicationDescription
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setLogoUri
argument_list|(
name|token
operator|.
name|getClient
argument_list|()
operator|.
name|getLogoUri
argument_list|()
argument_list|)
expr_stmt|;
name|secData
operator|.
name|setPermissions
argument_list|(
name|token
operator|.
name|getScopes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|secData
return|;
block|}
specifier|private
name|void
name|addAuthenticityTokenToSession
parameter_list|(
name|OAuthAuthorizationData
name|secData
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|)
block|{
name|HttpSession
name|session
init|=
name|request
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|secData
operator|.
name|setAuthenticityToken
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|OAuthConstants
operator|.
name|AUTHENTICITY_TOKEN
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|compareRequestSessionTokens
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|OAuthMessage
name|oAuthMessage
parameter_list|)
block|{
name|HttpSession
name|session
init|=
name|request
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|String
name|requestToken
init|=
literal|null
decl_stmt|;
try|try
block|{
name|requestToken
operator|=
name|oAuthMessage
operator|.
name|getParameter
argument_list|(
name|OAuthConstants
operator|.
name|AUTHENTICITY_TOKEN
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|sessionToken
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|OAuthConstants
operator|.
name|AUTHENTICITY_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|requestToken
argument_list|)
operator|||
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
name|boolean
name|b
init|=
name|requestToken
operator|.
name|equals
argument_list|(
name|sessionToken
argument_list|)
decl_stmt|;
name|session
operator|.
name|removeAttribute
argument_list|(
name|OAuthConstants
operator|.
name|AUTHENTICITY_TOKEN
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

