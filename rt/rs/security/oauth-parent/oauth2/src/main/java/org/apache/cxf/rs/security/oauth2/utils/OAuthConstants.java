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
name|utils
package|;
end_package

begin_comment
comment|/**  * Miscellaneous constants   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OAuthConstants
block|{
comment|// Common OAuth2 constants
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_ID
init|=
literal|"client_id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_SECRET
init|=
literal|"client_secret"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REDIRECT_URI
init|=
literal|"redirect_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SCOPE
init|=
literal|"scope"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STATE
init|=
literal|"state"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_TOKEN
init|=
literal|"access_token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_TOKEN_TYPE
init|=
literal|"token_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_TOKEN_EXPIRES_IN
init|=
literal|"expires_in"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GRANT_TYPE
init|=
literal|"grant_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESPONSE_TYPE
init|=
literal|"response_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_RESPONSE_TYPE
init|=
literal|"token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REFRESH_TOKEN
init|=
literal|"refresh_token"
decl_stmt|;
comment|// Well-known grant types
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION_CODE_GRANT
init|=
literal|"authorization_code"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLIENT_CREDENTIALS_GRANT
init|=
literal|"client_credentials"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMPLICIT_GRANT
init|=
literal|"implicit"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_OWNER_GRANT
init|=
literal|"password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REFRESH_TOKEN_GRANT
init|=
literal|"refresh_token"
decl_stmt|;
comment|// Well-known token types
specifier|public
specifier|static
specifier|final
name|String
name|BEARER_TOKEN_TYPE
init|=
literal|"bearer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_TYPE
init|=
literal|"mac"
decl_stmt|;
comment|// MAC token parameters
comment|// Set by Access Token Service
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_SECRET
init|=
literal|"secret"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_ALGORITHM
init|=
literal|"algorithm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_ALGO_HMAC_SHA_1
init|=
literal|"hmac-sha-1"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_ALGO_HMAC_SHA_256
init|=
literal|"hmac-sha-256"
decl_stmt|;
comment|// Set in Authorization header
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_ID
init|=
literal|"id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_EXTENSION
init|=
literal|"ext"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_NONCE
init|=
literal|"nonce"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_TOKEN_SIGNATURE
init|=
literal|"mac"
decl_stmt|;
comment|// Token Authorization schemes
specifier|public
specifier|static
specifier|final
name|String
name|BEARER_AUTHORIZATION_SCHEME
init|=
literal|"Bearer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAC_AUTHORIZATION_SCHEME
init|=
literal|"MAC"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALL_AUTH_SCHEMES
init|=
literal|"*"
decl_stmt|;
comment|// Default Client Authentication Scheme
specifier|public
specifier|static
specifier|final
name|String
name|BASIC_SCHEME
init|=
literal|"Basic"
decl_stmt|;
comment|// Authorization Code grant constants
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION_CODE_VALUE
init|=
literal|"code"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CODE_RESPONSE_TYPE
init|=
literal|"code"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SESSION_AUTHENTICITY_TOKEN
init|=
literal|"session_authenticity_token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION_DECISION_KEY
init|=
literal|"oauthDecision"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION_DECISION_ALLOW
init|=
literal|"allow"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|AUTHORIZATION_DECISION_DENY
init|=
literal|"deny"
decl_stmt|;
comment|// Resource Owner grant constants
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_OWNER_NAME
init|=
literal|"username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_OWNER_PASSWORD
init|=
literal|"password"
decl_stmt|;
comment|// Error constants
specifier|public
specifier|static
specifier|final
name|String
name|ERROR_KEY
init|=
literal|"error"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ERROR_DESCRIPTION_KEY
init|=
literal|"error_description"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ERROR_URI_KEY
init|=
literal|"error_uri"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_ERROR
init|=
literal|"server_error"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_REQUEST
init|=
literal|"invalid_request"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_GRANT
init|=
literal|"invalid_grant"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNSUPPORTED_GRANT_TYPE
init|=
literal|"unsupported_grant_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNSUPPORTED_RESPONSE_TYPE
init|=
literal|"unsupported_response_type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNAUTHORIZED_CLIENT
init|=
literal|"unauthorized_client"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_CLIENT
init|=
literal|"invalid_client"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_SCOPE
init|=
literal|"invalid_scope"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_DENIED
init|=
literal|"access_denied"
decl_stmt|;
comment|// CXF-Specific parameters
specifier|public
specifier|static
specifier|final
name|String
name|ACCESS_TOKEN_ISSUED_AT
init|=
literal|"issued_at"
decl_stmt|;
comment|// End Of CXF-Specific
specifier|private
name|OAuthConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

