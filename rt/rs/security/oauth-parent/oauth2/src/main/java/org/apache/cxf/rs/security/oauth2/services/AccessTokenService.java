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
name|HttpHeaders
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
name|AuthorizationCodeGrantHandler
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
name|AccessTokenGrantHandler
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

begin_comment
comment|/**  * OAuth2 Access Token Service implementation  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/token"
argument_list|)
specifier|public
class|class
name|AccessTokenService
extends|extends
name|AbstractOAuthService
block|{
specifier|private
name|List
argument_list|<
name|AccessTokenGrantHandler
argument_list|>
name|grantHandlers
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|writeOptionalParameters
init|=
literal|true
decl_stmt|;
specifier|public
name|void
name|setWriteOptionalParameters
parameter_list|(
name|boolean
name|write
parameter_list|)
block|{
name|writeOptionalParameters
operator|=
name|write
expr_stmt|;
block|}
comment|/**      * Sets the list of optional grant handlers      * @param handlers the grant handlers      */
specifier|public
name|void
name|setGrantHandlers
parameter_list|(
name|List
argument_list|<
name|AccessTokenGrantHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|grantHandlers
operator|=
name|handlers
expr_stmt|;
block|}
comment|/**      * Processes an access token request      * @param params the form parameters representing the access token grant       * @return Access Token or the error       */
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|Response
name|handleTokenRequest
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
comment|// Make sure the client is authenticated
name|Client
name|client
init|=
name|authenticateClientIfNeeded
argument_list|(
name|params
argument_list|)
decl_stmt|;
comment|// Find the grant handler
name|AccessTokenGrantHandler
name|handler
init|=
name|findGrantHandler
argument_list|(
name|params
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|OAuthConstants
operator|.
name|UNSUPPORTED_GRANT_TYPE
argument_list|)
return|;
block|}
comment|// Create the access token
name|ServerAccessToken
name|serverToken
init|=
literal|null
decl_stmt|;
try|try
block|{
name|serverToken
operator|=
name|handler
operator|.
name|createAccessToken
argument_list|(
name|client
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
comment|// the error response is to be returned next
block|}
if|if
condition|(
name|serverToken
operator|==
literal|null
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
return|;
block|}
comment|// Extract the information to be of use for the client
name|ClientAccessToken
name|clientToken
init|=
operator|new
name|ClientAccessToken
argument_list|(
name|serverToken
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|serverToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|writeOptionalParameters
condition|)
block|{
name|clientToken
operator|.
name|setExpiresIn
argument_list|(
name|serverToken
operator|.
name|getLifetime
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
init|=
name|serverToken
operator|.
name|getScopes
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|perms
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|clientToken
operator|.
name|setApprovedScope
argument_list|(
name|OAuthUtils
operator|.
name|convertPermissionsToScope
argument_list|(
name|perms
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|clientToken
operator|.
name|setParameters
argument_list|(
name|serverToken
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//TODO: also set a refresh token if any
comment|// Return it to the client
return|return
name|Response
operator|.
name|ok
argument_list|(
name|clientToken
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|CACHE_CONTROL
argument_list|,
literal|"no-store"
argument_list|)
operator|.
name|header
argument_list|(
literal|"Pragma"
argument_list|,
literal|"no-cache"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Make sure the client is authenticated      */
specifier|private
name|Client
name|authenticateClientIfNeeded
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
name|Client
name|client
init|=
literal|null
decl_stmt|;
name|SecurityContext
name|sc
init|=
name|getMessageContext
argument_list|()
operator|.
name|getSecurityContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|containsKey
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
condition|)
block|{
comment|// both client_id and client_secret are expected in the form payload
name|client
operator|=
name|getAndValidateClient
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// client has already authenticated
name|Principal
name|p
init|=
name|sc
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
name|String
name|scheme
init|=
name|sc
operator|.
name|getAuthenticationScheme
argument_list|()
decl_stmt|;
if|if
condition|(
name|OAuthConstants
operator|.
name|BASIC_SCHEME
operator|.
name|equalsIgnoreCase
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
comment|// section 2.3.1
name|client
operator|=
name|getClient
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// section 2.3.2
comment|// the client has authenticated itself using some other scheme
comment|// in which case the mapping between the scheme and the client_id
comment|// should've been done and the client_id is expected
comment|// on the current message
name|Object
name|clientIdProp
init|=
name|getMessageContext
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientIdProp
operator|!=
literal|null
condition|)
block|{
name|client
operator|=
name|getClient
argument_list|(
name|clientIdProp
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: consider matching client.getUserSubject().getLoginName()
comment|// against principal.getName() ?
block|}
block|}
block|}
else|else
block|{
comment|// the client id and secret are expected to be in the Basic scheme data
name|String
index|[]
name|parts
init|=
name|AuthorizationUtils
operator|.
name|getAuthorizationParts
argument_list|(
name|getMessageContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|OAuthConstants
operator|.
name|BASIC_SCHEME
operator|.
name|equalsIgnoreCase
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|String
index|[]
name|authInfo
init|=
name|AuthorizationUtils
operator|.
name|getBasicAuthParts
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|client
operator|=
name|getAndValidateClient
argument_list|(
name|authInfo
index|[
literal|0
index|]
argument_list|,
name|authInfo
index|[
literal|1
index|]
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
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|401
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
comment|// Get the Client and check the id and secret
specifier|private
name|Client
name|getAndValidateClient
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|clientSecret
parameter_list|)
block|{
name|Client
name|client
init|=
name|getClient
argument_list|(
name|clientId
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientSecret
operator|==
literal|null
operator|||
operator|!
name|client
operator|.
name|getClientId
argument_list|()
operator|.
name|equals
argument_list|(
name|clientId
argument_list|)
operator|||
operator|!
name|client
operator|.
name|getClientSecret
argument_list|()
operator|.
name|equals
argument_list|(
name|clientSecret
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|401
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
comment|/**      * Find the mathcing grant handler      */
specifier|protected
name|AccessTokenGrantHandler
name|findGrantHandler
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
name|String
name|grantType
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|grantType
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AccessTokenGrantHandler
name|handler
range|:
name|grantHandlers
control|)
block|{
if|if
condition|(
name|handler
operator|.
name|getSupportedGrantTypes
argument_list|()
operator|.
name|contains
argument_list|(
name|grantType
argument_list|)
condition|)
block|{
return|return
name|handler
return|;
block|}
block|}
comment|// Lets try the default grant handler
if|if
condition|(
name|grantHandlers
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|AuthorizationCodeGrantHandler
name|handler
init|=
operator|new
name|AuthorizationCodeGrantHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|.
name|getSupportedGrantTypes
argument_list|()
operator|.
name|contains
argument_list|(
name|grantType
argument_list|)
condition|)
block|{
name|handler
operator|.
name|setDataProvider
argument_list|(
operator|(
name|AuthorizationCodeDataProvider
operator|)
name|super
operator|.
name|getDataProvider
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|handler
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
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
name|error
parameter_list|)
block|{
name|OAuthError
name|oauthError
init|=
operator|new
name|OAuthError
argument_list|(
name|error
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|status
argument_list|(
literal|400
argument_list|)
operator|.
name|entity
argument_list|(
name|oauthError
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

