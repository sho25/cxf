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
name|client
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
name|io
operator|.
name|InputStream
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
name|Map
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
name|ProcessingException
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
name|client
operator|.
name|ResponseProcessingException
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
name|Form
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
name|Base64Utility
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
name|client
operator|.
name|WebClient
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
name|AccessTokenGrant
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
name|provider
operator|.
name|OAuthJSONProvider
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
name|tokens
operator|.
name|mac
operator|.
name|MacAuthorizationScheme
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
comment|/**  * The utility class for simplifying working with OAuth servers  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OAuthClientUtils
block|{
specifier|private
name|OAuthClientUtils
parameter_list|()
block|{              }
comment|/**      * Builds a complete URI for redirecting to OAuth Authorization Service      * @param authorizationServiceURI the service endpoint address      * @param clientId client registration id      * @param redirectUri the uri the authorization code will be posted to      * @param state the client state, example the key or the encrypted token       *              representing the info about the current end user's request      * @scope scope the optional scope; if not specified then the authorization      *              service will allocate the default scope                     * @return authorization service URI      */
specifier|public
specifier|static
name|URI
name|getAuthorizationURI
parameter_list|(
name|String
name|authorizationServiceURI
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|redirectUri
parameter_list|,
name|String
name|state
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
name|UriBuilder
name|ub
init|=
name|getAuthorizationURIBuilder
argument_list|(
name|authorizationServiceURI
argument_list|,
name|clientId
argument_list|,
name|scope
argument_list|)
decl_stmt|;
if|if
condition|(
name|redirectUri
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|REDIRECT_URI
argument_list|,
name|redirectUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
return|return
name|ub
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Creates the builder for building OAuth AuthorizationService URIs      * @param authorizationServiceURI the service endpoint address       * @param clientId client registration id      * @param scope the optional scope; if not specified then the authorization      *              service will allocate the default scope      * @return the builder      */
specifier|public
specifier|static
name|UriBuilder
name|getAuthorizationURIBuilder
parameter_list|(
name|String
name|authorizationServiceURI
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
name|UriBuilder
name|ub
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|authorizationServiceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|clientId
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|,
name|clientId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|RESPONSE_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|CODE_RESPONSE_TYPE
argument_list|)
expr_stmt|;
return|return
name|ub
return|;
block|}
comment|/**      * Obtains the access token from OAuth AccessToken Service       * using the initialized web client       * @param accessTokenService the AccessToken client      * @param consumer {@link Consumer} representing the registered client       * @param grant {@link AccessTokenGrant} grant      * @return {@link ClientAccessToken} access token      * @throws OAuthServiceException      */
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|AccessTokenGrant
name|grant
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getAccessToken
argument_list|(
name|accessTokenService
argument_list|,
name|consumer
argument_list|,
name|grant
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Obtains the access token from OAuth AccessToken Service       * @param accessTokenServiceUri the AccessToken endpoint address      * @param consumer {@link Consumer} representing the registered client       * @param grant {@link AccessTokenGrant} grant      * @param setAuthorizationHeader if set to true then HTTP Basic scheme      *           will be used to pass client id and secret, otherwise they will      *           be passed in the form payload      * @return {@link ClientAccessToken} access token      * @throws OAuthServiceException      */
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessTokenServiceUri
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|AccessTokenGrant
name|grant
parameter_list|,
name|boolean
name|setAuthorizationHeader
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|WebClient
name|accessTokenService
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|accessTokenServiceUri
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|provider
argument_list|)
argument_list|)
decl_stmt|;
name|accessTokenService
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
return|return
name|getAccessToken
argument_list|(
name|accessTokenService
argument_list|,
name|consumer
argument_list|,
name|grant
argument_list|,
name|setAuthorizationHeader
argument_list|)
return|;
block|}
comment|/**      * Obtains the access token from OAuth AccessToken Service       * using the initialized web client       * @param accessTokenService the AccessToken client      * @param consumer {@link Consumer} representing the registered client.      * @param grant {@link AccessTokenGrant} grant      * @param setAuthorizationHeader if set to true then HTTP Basic scheme      *           will be used to pass client id and secret, otherwise they will      *           be passed in the form payload        * @return {@link ClientAccessToken} access token      * @throws OAuthServiceException      */
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|AccessTokenGrant
name|grant
parameter_list|,
name|boolean
name|setAuthorizationHeader
parameter_list|)
block|{
return|return
name|getAccessToken
argument_list|(
name|accessTokenService
argument_list|,
name|consumer
argument_list|,
name|grant
argument_list|,
literal|null
argument_list|,
name|setAuthorizationHeader
argument_list|)
return|;
block|}
comment|/**      * Obtains the access token from OAuth AccessToken Service       * using the initialized web client       * @param accessTokenService the AccessToken client      * @param grant {@link AccessTokenGrant} grant      * @param extraParams extra parameters      * @return {@link ClientAccessToken} access token      * @throws OAuthServiceException      */
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|AccessTokenGrant
name|grant
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getAccessToken
argument_list|(
name|accessTokenService
argument_list|,
literal|null
argument_list|,
name|grant
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Obtains the access token from OAuth AccessToken Service       * using the initialized web client       * @param accessTokenService the AccessToken client      * @param grant {@link AccessTokenGrant} grant      * @param extraParams extra parameters      * @return {@link ClientAccessToken} access token      * @throws OAuthServiceException      */
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|AccessTokenGrant
name|grant
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParams
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getAccessToken
argument_list|(
name|accessTokenService
argument_list|,
literal|null
argument_list|,
name|grant
argument_list|,
name|extraParams
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Obtains the access token from OAuth AccessToken Service       * using the initialized web client       * @param accessTokenService the AccessToken client      * @param consumer {@link Consumer} representing the registered client.      * @param grant {@link AccessTokenGrant} grant      * @param extraParams extra parameters      * @param setAuthorizationHeader if set to true then HTTP Basic scheme      *           will be used to pass client id and secret, otherwise they will      *           be passed in the form payload        * @return {@link ClientAccessToken} access token      * @throws OAuthServiceException      */
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessToken
parameter_list|(
name|WebClient
name|accessTokenService
parameter_list|,
name|Consumer
name|consumer
parameter_list|,
name|AccessTokenGrant
name|grant
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParams
parameter_list|,
name|boolean
name|setAuthorizationHeader
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|Form
name|form
init|=
operator|new
name|Form
argument_list|(
name|grant
operator|.
name|toMap
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|extraParams
operator|!=
literal|null
condition|)
block|{
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
name|extraParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|form
operator|.
name|param
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
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|setAuthorizationHeader
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Basic "
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|data
init|=
name|consumer
operator|.
name|getKey
argument_list|()
operator|+
literal|":"
operator|+
name|consumer
operator|.
name|getSecret
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|Base64Utility
operator|.
name|encode
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ProcessingException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|accessTokenService
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|form
operator|.
name|param
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_ID
argument_list|,
name|consumer
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getSecret
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|form
operator|.
name|param
argument_list|(
name|OAuthConstants
operator|.
name|CLIENT_SECRET
argument_list|,
name|consumer
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// in this case the AccessToken service is expected to find a mapping between
comment|// the authenticated credentials and the client registration id
block|}
name|Response
name|response
init|=
name|accessTokenService
operator|.
name|form
argument_list|(
name|form
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
literal|null
decl_stmt|;
try|try
block|{
name|map
operator|=
operator|new
name|OAuthJSONProvider
argument_list|()
operator|.
name|readJSONResponse
argument_list|(
operator|(
name|InputStream
operator|)
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ResponseProcessingException
argument_list|(
name|response
argument_list|,
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
literal|200
operator|==
name|response
operator|.
name|getStatus
argument_list|()
condition|)
block|{
name|ClientAccessToken
name|token
init|=
name|fromMapToClientToken
argument_list|(
name|map
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
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|SERVER_ERROR
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|token
return|;
block|}
block|}
elseif|else
if|if
condition|(
literal|400
operator|==
name|response
operator|.
name|getStatus
argument_list|()
operator|&&
name|map
operator|.
name|containsKey
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|)
condition|)
block|{
name|OAuthError
name|error
init|=
operator|new
name|OAuthError
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_DESCRIPTION_KEY
argument_list|)
argument_list|)
decl_stmt|;
name|error
operator|.
name|setErrorUri
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_URI_KEY
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|error
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|SERVER_ERROR
argument_list|)
throw|;
block|}
specifier|public
specifier|static
name|ClientAccessToken
name|fromMapToClientToken
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
operator|&&
name|map
operator|.
name|containsKey
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|)
condition|)
block|{
name|ClientAccessToken
name|token
init|=
operator|new
name|ClientAccessToken
argument_list|(
name|map
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|)
argument_list|,
name|map
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|refreshToken
init|=
name|map
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|REFRESH_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|refreshToken
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setRefreshToken
argument_list|(
name|refreshToken
argument_list|)
expr_stmt|;
block|}
name|String
name|expiresInStr
init|=
name|map
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_EXPIRES_IN
argument_list|)
decl_stmt|;
if|if
condition|(
name|expiresInStr
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setExpiresIn
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
name|expiresInStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|issuedAtStr
init|=
name|map
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_ISSUED_AT
argument_list|)
decl_stmt|;
name|token
operator|.
name|setIssuedAt
argument_list|(
name|issuedAtStr
operator|!=
literal|null
condition|?
name|Long
operator|.
name|valueOf
argument_list|(
name|issuedAtStr
argument_list|)
else|:
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
expr_stmt|;
name|String
name|scope
init|=
name|map
operator|.
name|remove
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setApprovedScope
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
name|token
operator|.
name|setParameters
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Creates OAuth Authorization header with Bearer scheme      * @param accessToken the access token        * @return the header value      */
specifier|public
specifier|static
name|String
name|createAuthorizationHeader
parameter_list|(
name|ClientAccessToken
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|createAuthorizationHeader
argument_list|(
name|accessToken
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates OAuth Authorization header with the scheme that      * may require an access to the current HTTP request properties      * @param accessToken the access token        * @param httpProps http request properties, can be null for Bearer tokens      * @return the header value      */
specifier|public
specifier|static
name|String
name|createAuthorizationHeader
parameter_list|(
name|ClientAccessToken
name|accessToken
parameter_list|,
name|HttpRequestProperties
name|httpProps
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|appendTokenData
argument_list|(
name|sb
argument_list|,
name|accessToken
argument_list|,
name|httpProps
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|void
name|appendTokenData
parameter_list|(
name|StringBuilder
name|sb
parameter_list|,
name|ClientAccessToken
name|token
parameter_list|,
name|HttpRequestProperties
name|httpProps
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// this should all be handled by token specific serializers
if|if
condition|(
name|OAuthConstants
operator|.
name|BEARER_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|BEARER_AUTHORIZATION_SCHEME
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|httpProps
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"MAC scheme requires HTTP Request properties"
argument_list|)
throw|;
block|}
name|MacAuthorizationScheme
name|macAuthData
init|=
operator|new
name|MacAuthorizationScheme
argument_list|(
name|httpProps
argument_list|,
name|token
argument_list|)
decl_stmt|;
name|String
name|macAlgo
init|=
name|token
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_ALGORITHM
argument_list|)
decl_stmt|;
name|String
name|macKey
init|=
name|token
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|MAC_TOKEN_KEY
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|macAuthData
operator|.
name|toAuthorizationHeader
argument_list|(
name|macAlgo
argument_list|,
name|macKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ProcessingException
argument_list|(
operator|new
name|OAuthServiceException
argument_list|(
literal|"Unsupported token type"
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * Simple consumer representation      */
specifier|public
specifier|static
class|class
name|Consumer
block|{
specifier|private
name|String
name|key
decl_stmt|;
specifier|private
name|String
name|secret
decl_stmt|;
specifier|public
name|Consumer
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|secret
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|secret
operator|=
name|secret
expr_stmt|;
block|}
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
specifier|public
name|String
name|getSecret
parameter_list|()
block|{
return|return
name|secret
return|;
block|}
block|}
block|}
end_class

end_unit

