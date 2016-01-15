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

begin_comment
comment|/**  * OAuth2 Token Revocation Service implementation  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/revoke"
argument_list|)
specifier|public
class|class
name|TokenRevocationService
extends|extends
name|AbstractTokenService
block|{
comment|/**      * Processes a token revocation request      * @param params the form parameters representing the access token grant       * @return Access Token or the error       */
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
name|handleTokenRevocation
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
name|String
name|token
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|TOKEN_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
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
name|UNSUPPORTED_TOKEN_TYPE
argument_list|)
return|;
block|}
name|String
name|tokenTypeHint
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|TOKEN_TYPE_HINT
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenTypeHint
operator|!=
literal|null
operator|&&
operator|!
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
operator|.
name|equals
argument_list|(
name|tokenTypeHint
argument_list|)
operator|&&
operator|!
name|OAuthConstants
operator|.
name|REFRESH_TOKEN
operator|.
name|equals
argument_list|(
name|tokenTypeHint
argument_list|)
condition|)
block|{
return|return
name|createErrorResponseFromErrorCode
argument_list|(
name|OAuthConstants
operator|.
name|UNSUPPORTED_TOKEN_TYPE
argument_list|)
return|;
block|}
try|try
block|{
name|getDataProvider
argument_list|()
operator|.
name|revokeToken
argument_list|(
name|client
argument_list|,
name|token
argument_list|,
name|tokenTypeHint
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
comment|// Spec: The authorization server responds with HTTP status code 200 if the
comment|// token has been revoked successfully or if the client submitted an
comment|// invalid token
block|}
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

