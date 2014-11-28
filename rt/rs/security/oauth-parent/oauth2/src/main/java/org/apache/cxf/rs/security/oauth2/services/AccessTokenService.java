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
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|AbstractTokenService
block|{
specifier|private
name|List
argument_list|<
name|AccessTokenGrantHandler
argument_list|>
name|grantHandlers
init|=
operator|new
name|LinkedList
argument_list|<
name|AccessTokenGrantHandler
argument_list|>
argument_list|()
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
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
comment|/**      * Sets a grant handler      * @param handler the grant handler      */
specifier|public
name|void
name|setGrantHandler
parameter_list|(
name|AccessTokenGrantHandler
name|handler
parameter_list|)
block|{
name|grantHandlers
operator|.
name|add
argument_list|(
name|handler
argument_list|)
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
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|isGrantSupportedForClient
argument_list|(
name|client
argument_list|,
name|isCanSupportPublicClients
argument_list|()
argument_list|,
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|OAuthConstants
operator|.
name|UNAUTHORIZED_CLIENT
argument_list|)
return|;
block|}
try|try
block|{
name|checkAudience
argument_list|(
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
return|return
name|super
operator|.
name|createErrorResponseFromBean
argument_list|(
name|ex
operator|.
name|getError
argument_list|()
argument_list|)
return|;
block|}
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
return|return
name|handleException
argument_list|(
name|ex
argument_list|,
name|OAuthConstants
operator|.
name|INVALID_GRANT
argument_list|)
return|;
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
name|OAuthUtils
operator|.
name|toClientAccessToken
argument_list|(
name|serverToken
argument_list|,
name|isWriteOptionalParameters
argument_list|()
argument_list|)
decl_stmt|;
name|processClientAccessToken
argument_list|(
name|clientToken
argument_list|,
name|serverToken
argument_list|)
expr_stmt|;
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
name|void
name|checkAudience
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
if|if
condition|(
name|audiences
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|audienceParam
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_AUDIENCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|audienceParam
operator|==
literal|null
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
comment|// must be URL
try|try
block|{
operator|new
name|URL
argument_list|(
name|audienceParam
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
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
if|if
condition|(
operator|!
name|audiences
operator|.
name|contains
argument_list|(
name|audienceParam
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
name|ACCESS_DENIED
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Find the matching grant handler      */
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
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAudiences
parameter_list|()
block|{
return|return
name|audiences
return|;
block|}
specifier|public
name|void
name|setAudiences
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|audiences
parameter_list|)
block|{
name|this
operator|.
name|audiences
operator|=
name|audiences
expr_stmt|;
block|}
block|}
end_class

end_unit

