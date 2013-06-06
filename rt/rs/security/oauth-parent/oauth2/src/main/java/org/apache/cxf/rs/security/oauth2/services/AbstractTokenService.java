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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|NotAuthorizedException
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

begin_class
specifier|public
class|class
name|AbstractTokenService
extends|extends
name|AbstractOAuthService
block|{
specifier|private
name|boolean
name|canSupportPublicClients
decl_stmt|;
specifier|private
name|boolean
name|writeCustomErrors
decl_stmt|;
comment|/**      * Make sure the client is authenticated      */
specifier|protected
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
name|NotAuthorizedException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
comment|// Get the Client and check the id and secret
specifier|protected
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
name|canSupportPublicClients
operator|&&
operator|!
name|client
operator|.
name|isConfidential
argument_list|()
operator|&&
name|client
operator|.
name|getClientSecret
argument_list|()
operator|==
literal|null
operator|&&
name|client
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|clientSecret
operator|==
literal|null
condition|)
block|{
return|return
name|client
return|;
block|}
if|if
condition|(
name|clientSecret
operator|==
literal|null
operator|||
name|client
operator|.
name|getClientSecret
argument_list|()
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
name|NotAuthorizedException
argument_list|(
name|Response
operator|.
name|status
argument_list|(
literal|401
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|client
return|;
block|}
specifier|protected
name|Response
name|handleException
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|OAuthError
name|customError
init|=
name|ex
operator|.
name|getError
argument_list|()
decl_stmt|;
if|if
condition|(
name|writeCustomErrors
operator|&&
name|customError
operator|!=
literal|null
condition|)
block|{
return|return
name|createErrorResponseFromBean
argument_list|(
name|customError
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createErrorResponseFromBean
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|error
argument_list|)
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
name|error
parameter_list|)
block|{
return|return
name|createErrorResponseFromBean
argument_list|(
operator|new
name|OAuthError
argument_list|(
name|error
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|Response
name|createErrorResponseFromBean
parameter_list|(
name|OAuthError
name|errorBean
parameter_list|)
block|{
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
name|errorBean
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Get the {@link Client} reference      * @param clientId the provided client id      * @return Client the client reference       * @throws {@link javax.ws.rs.WebApplicationException} if no matching Client is found      */
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
comment|// log it
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
argument_list|)
expr_stmt|;
block|}
return|return
name|client
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
name|setWriteCustomErrors
parameter_list|(
name|boolean
name|writeCustomErrors
parameter_list|)
block|{
name|this
operator|.
name|writeCustomErrors
operator|=
name|writeCustomErrors
expr_stmt|;
block|}
block|}
end_class

end_unit

