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
name|provider
package|;
end_package

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
name|AccessTokenRegistration
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
name|common
operator|.
name|UserSubject
import|;
end_import

begin_comment
comment|/**  * OAuth provider responsible for persisting the information about   * OAuth consumers, request and access tokens.  */
end_comment

begin_interface
specifier|public
interface|interface
name|OAuthDataProvider
block|{
comment|/**      * Returns the previously registered third-party {@link Client}       * @param clientId the client id      * @return Client      * @throws OAuthServiceException      */
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Create access token       * @param accessToken the token registration info       * @return AccessToken      * @throws OAuthServiceException      */
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Get access token       * @param accessToken the token key       * @return AccessToken      * @throws OAuthServiceException      */
name|ServerAccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Get preauthorized access token       * @param client Client      * @param requestedScopes the scopes requested by the client      * @param subject End User subject       * @return AccessToken access token      * @throws OAuthServiceException      */
name|ServerAccessToken
name|getPreauthorizedToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|String
name|grantType
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Refresh access token       * @param client the client      * @param refreshToken refresh token key       * @param requestedScopes the scopes requested by the client        * @return AccessToken      * @throws OAuthServiceException      */
name|ServerAccessToken
name|refreshAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Removes the access token      * The runtime will call this method if it finds that a token has expired      * @param accessToken the token      * @throws OAuthServiceException      */
name|void
name|removeAccessToken
parameter_list|(
name|ServerAccessToken
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Revokes a refresh or access token      * @param token token identifier      * @param tokenTypeHint       * @throws OAuthServiceException      */
name|void
name|revokeToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|tokenTypeHint
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Converts the requested scope to the list of permissions        * @param requestedScopes      * @return list of permissions      */
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|convertScopeToPermissions
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

