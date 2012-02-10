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
name|provider
package|;
end_package

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
name|RequestTokenRegistration
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
name|Token
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
comment|/**      * Creates a temporarily request token which will capture the      * information about the {@link Client} attempting to access or      * modify the resource owner's resource       * @param reg RequestTokenRegistration      * @return new request token      * @see RequestTokenRegistration      * @throws OAuthServiceException      */
name|RequestToken
name|createRequestToken
parameter_list|(
name|RequestTokenRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Returns the previously registered {@link RequestToken}      * @param requestToken the token key      * @return RequestToken      * @throws OAuthServiceException      */
name|RequestToken
name|getRequestToken
parameter_list|(
name|String
name|requestToken
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Sets the verifier confirming the resource owner's agreement for      * the {@link Client} to perform the action as represented by      * the provided {@link RequestToken}. The runtime will report      * this verifier to the client who will exchange it for       * a new {@link AccessToken}      *          * @param data AuthorizationInput      * @return the generated verifier      * @throws OAuthServiceException      */
name|String
name|finalizeAuthorization
parameter_list|(
name|AuthorizationInput
name|data
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Creates a new {@link AccessToken}      * @param reg {@link AccessTokenRegistration} instance which captures       *        a request token approved by the resource owner      * @return new AccessToken      * @throws OAuthServiceException      */
name|AccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Returns the {@link AccessToken}      * @param accessToken the token key       * @return AccessToken      * @throws OAuthServiceException      */
name|AccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
comment|/**      * Removes the token      * @param token the token      * @throws OAuthServiceException      */
name|void
name|removeToken
parameter_list|(
name|Token
name|token
parameter_list|)
throws|throws
name|OAuthServiceException
function_decl|;
block|}
end_interface

end_unit

