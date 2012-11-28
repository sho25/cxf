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
name|WebApplicationException
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
name|jaxrs
operator|.
name|ext
operator|.
name|RequestHandler
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
name|model
operator|.
name|ClassResourceInfo
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
specifier|public
class|class
name|OAuthRequestFilter
extends|extends
name|AbstractAccessTokenValidator
implements|implements
name|RequestHandler
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
specifier|public
name|Response
name|handleRequest
parameter_list|(
name|Message
name|m
parameter_list|,
name|ClassResourceInfo
name|resourceClass
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
return|return
literal|null
return|;
block|}
comment|// Get the access token
name|AccessTokenValidation
name|accessTokenV
init|=
name|getAccessTokenValidation
argument_list|()
decl_stmt|;
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
name|permissions
operator|.
name|size
argument_list|()
operator|>
literal|0
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
name|WebApplicationException
argument_list|(
literal|403
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
name|setTokenKey
argument_list|(
name|accessTokenV
operator|.
name|getTokenKey
argument_list|()
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
return|return
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
literal|"Invalid request URI"
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
block|}
end_class

end_unit

