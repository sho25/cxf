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
name|DELETE
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
name|GET
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
name|PUT
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
name|PathParam
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
name|QueryParam
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
name|Context
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
name|Base64UrlUtility
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
name|ClientRegistrationProvider
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"register"
argument_list|)
specifier|public
class|class
name|DynamicRegistrationService
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_APPLICATION_TYPE
init|=
literal|"web"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Integer
name|DEFAULT_CLIENT_ID_SIZE
init|=
literal|10
decl_stmt|;
specifier|private
name|ClientRegistrationProvider
name|clientProvider
decl_stmt|;
specifier|private
name|String
name|initialAccessToken
decl_stmt|;
specifier|private
name|int
name|clientIdSizeInBytes
init|=
name|DEFAULT_CLIENT_ID_SIZE
decl_stmt|;
specifier|private
name|MessageContext
name|mc
decl_stmt|;
specifier|private
name|boolean
name|supportRegistrationAccessTokens
init|=
literal|true
decl_stmt|;
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|Response
name|register
parameter_list|(
name|ClientRegistration
name|request
parameter_list|)
block|{
name|checkInitialAccessToken
argument_list|()
expr_stmt|;
name|Client
name|client
init|=
name|createNewClient
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|createRegAccessToken
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|clientProvider
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|status
argument_list|(
literal|201
argument_list|)
operator|.
name|entity
argument_list|(
name|fromClientToRegistrationResponse
argument_list|(
name|client
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|void
name|checkInitialAccessToken
parameter_list|()
block|{
if|if
condition|(
name|initialAccessToken
operator|!=
literal|null
condition|)
block|{
name|String
name|accessToken
init|=
name|getRequestAccessToken
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|initialAccessToken
operator|.
name|equals
argument_list|(
name|accessToken
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|String
name|createRegAccessToken
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|String
name|regAccessToken
init|=
name|OAuthUtils
operator|.
name|generateRandomTokenKey
argument_list|()
decl_stmt|;
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|ClientRegistrationResponse
operator|.
name|REG_ACCESS_TOKEN
argument_list|,
name|regAccessToken
argument_list|)
expr_stmt|;
return|return
name|regAccessToken
return|;
block|}
specifier|protected
name|void
name|checkRegistrationAccessToken
parameter_list|(
name|Client
name|c
parameter_list|,
name|String
name|accessToken
parameter_list|)
block|{
name|String
name|regAccessToken
init|=
name|c
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|ClientRegistrationResponse
operator|.
name|REG_ACCESS_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|regAccessToken
operator|.
name|equals
argument_list|(
name|accessToken
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|ClientRegistration
name|readClientRegistrationWithQuery
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"client_id"
argument_list|)
name|String
name|clientId
parameter_list|)
block|{
return|return
name|doReadClientRegistration
argument_list|(
name|clientId
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{clientId}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|ClientRegistration
name|readClientRegistrationWithPath
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"clientId"
argument_list|)
name|String
name|clientId
parameter_list|)
block|{
return|return
name|doReadClientRegistration
argument_list|(
name|clientId
argument_list|)
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"{clientId}"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|Response
name|updateClientRegistration
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"clientId"
argument_list|)
name|String
name|clientId
parameter_list|)
block|{
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
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"{clientId}"
argument_list|)
specifier|public
name|Response
name|deleteClientRegistration
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"clientId"
argument_list|)
name|String
name|clientId
parameter_list|)
block|{
if|if
condition|(
name|readClient
argument_list|(
name|clientId
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|clientProvider
operator|.
name|removeClient
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
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
specifier|protected
name|ClientRegistrationResponse
name|fromClientToRegistrationResponse
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|ClientRegistrationResponse
name|response
init|=
operator|new
name|ClientRegistrationResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setClientId
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setClientSecret
argument_list|(
name|client
operator|.
name|getClientSecret
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setClientIdIssuedAt
argument_list|(
name|client
operator|.
name|getRegisteredAt
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: consider making Client secret time limited
name|response
operator|.
name|setClientSecretExpiresAt
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|UriBuilder
name|ub
init|=
name|getMessageContext
argument_list|()
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getAbsolutePathBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|supportRegistrationAccessTokens
condition|)
block|{
comment|// both registration access token and uri are either included or excluded
name|response
operator|.
name|setRegistrationClientUri
argument_list|(
name|ub
operator|.
name|path
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setRegistrationAccessToken
argument_list|(
name|client
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|ClientRegistrationResponse
operator|.
name|REG_ACCESS_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|response
return|;
block|}
specifier|protected
name|ClientRegistration
name|doReadClientRegistration
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|Client
name|client
init|=
name|readClient
argument_list|(
name|clientId
argument_list|)
decl_stmt|;
return|return
name|fromClientToClientRegistration
argument_list|(
name|client
argument_list|)
return|;
block|}
specifier|protected
name|ClientRegistration
name|fromClientToClientRegistration
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
name|ClientRegistration
name|reg
init|=
operator|new
name|ClientRegistration
argument_list|()
decl_stmt|;
name|reg
operator|.
name|setClientName
argument_list|(
name|c
operator|.
name|getApplicationName
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setGrantTypes
argument_list|(
name|c
operator|.
name|getAllowedGrantTypes
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setApplicationType
argument_list|(
name|c
operator|.
name|isConfidential
argument_list|()
condition|?
literal|"web"
else|:
literal|"native"
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setRedirectUris
argument_list|(
name|c
operator|.
name|getRedirectUris
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setScope
argument_list|(
name|OAuthUtils
operator|.
name|convertListOfScopesToString
argument_list|(
name|c
operator|.
name|getRegisteredScopes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|.
name|getApplicationWebUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reg
operator|.
name|setClientUri
argument_list|(
name|c
operator|.
name|getApplicationWebUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|getApplicationLogoUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reg
operator|.
name|setLogoUri
argument_list|(
name|c
operator|.
name|getApplicationLogoUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//etc
return|return
name|reg
return|;
block|}
specifier|protected
name|Client
name|readClient
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|String
name|accessToken
init|=
name|getRequestAccessToken
argument_list|()
decl_stmt|;
name|Client
name|c
init|=
name|clientProvider
operator|.
name|getClient
argument_list|(
name|clientId
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|checkRegistrationAccessToken
argument_list|(
name|c
argument_list|,
name|accessToken
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
specifier|public
name|String
name|getInitialAccessToken
parameter_list|()
block|{
return|return
name|initialAccessToken
return|;
block|}
specifier|public
name|void
name|setInitialAccessToken
parameter_list|(
name|String
name|initialAccessToken
parameter_list|)
block|{
name|this
operator|.
name|initialAccessToken
operator|=
name|initialAccessToken
expr_stmt|;
block|}
specifier|protected
name|Client
name|createNewClient
parameter_list|(
name|ClientRegistration
name|request
parameter_list|)
block|{
comment|// Client ID
name|String
name|clientId
init|=
name|generateClientId
argument_list|()
decl_stmt|;
comment|// Client Name
name|String
name|clientName
init|=
name|request
operator|.
name|getClientName
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|clientName
argument_list|)
condition|)
block|{
name|clientName
operator|=
name|clientId
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|grantTypes
init|=
name|request
operator|.
name|getGrantTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|grantTypes
operator|==
literal|null
condition|)
block|{
name|grantTypes
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"authorization_code"
argument_list|)
expr_stmt|;
block|}
comment|// Client Type
comment|// https://tools.ietf.org/html/rfc7591 has no this property but
comment|// but http://openid.net/specs/openid-connect-registration-1_0.html#ClientMetadata does
name|String
name|appType
init|=
name|request
operator|.
name|getApplicationType
argument_list|()
decl_stmt|;
if|if
condition|(
name|appType
operator|==
literal|null
condition|)
block|{
name|appType
operator|=
name|DEFAULT_APPLICATION_TYPE
expr_stmt|;
block|}
name|boolean
name|isConfidential
init|=
name|DEFAULT_APPLICATION_TYPE
operator|.
name|equals
argument_list|(
name|appType
argument_list|)
operator|&&
name|grantTypes
operator|.
name|contains
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_GRANT
argument_list|)
decl_stmt|;
comment|// Client Secret
name|String
name|clientSecret
init|=
name|isConfidential
condition|?
name|generateClientSecret
argument_list|(
name|request
argument_list|)
else|:
literal|null
decl_stmt|;
name|Client
name|newClient
init|=
operator|new
name|Client
argument_list|(
name|clientId
argument_list|,
name|clientSecret
argument_list|,
name|isConfidential
argument_list|,
name|clientName
argument_list|)
decl_stmt|;
name|newClient
operator|.
name|setAllowedGrantTypes
argument_list|(
name|grantTypes
argument_list|)
expr_stmt|;
comment|// Client Registration Time
name|newClient
operator|.
name|setRegisteredAt
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
argument_list|)
expr_stmt|;
comment|// Client Redirect URIs
name|List
argument_list|<
name|String
argument_list|>
name|redirectUris
init|=
name|request
operator|.
name|getRedirectUris
argument_list|()
decl_stmt|;
if|if
condition|(
name|redirectUris
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|uri
range|:
name|redirectUris
control|)
block|{
name|validateRequestUri
argument_list|(
name|uri
argument_list|,
name|appType
argument_list|,
name|grantTypes
argument_list|)
expr_stmt|;
block|}
name|newClient
operator|.
name|setRedirectUris
argument_list|(
name|redirectUris
argument_list|)
expr_stmt|;
block|}
comment|// Client Scopes
name|String
name|scope
init|=
name|request
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|newClient
operator|.
name|setRegisteredScopes
argument_list|(
name|OAuthUtils
operator|.
name|parseScope
argument_list|(
name|scope
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Client Application URI
name|String
name|clientUri
init|=
name|request
operator|.
name|getClientUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientUri
operator|!=
literal|null
condition|)
block|{
name|newClient
operator|.
name|setApplicationWebUri
argument_list|(
name|clientUri
argument_list|)
expr_stmt|;
block|}
comment|// Client Logo URI
name|String
name|clientLogoUri
init|=
name|request
operator|.
name|getLogoUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|clientLogoUri
operator|!=
literal|null
condition|)
block|{
name|newClient
operator|.
name|setApplicationLogoUri
argument_list|(
name|clientLogoUri
argument_list|)
expr_stmt|;
block|}
comment|//TODO: check other properties
comment|// Add more typed properties like tosUri, policyUri, etc to Client
comment|// or set them as Client extra properties
name|newClient
operator|.
name|setRegisteredDynamically
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|newClient
return|;
block|}
specifier|protected
name|void
name|validateRequestUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|appType
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|grantTypes
parameter_list|)
block|{
comment|// Web Clients using the OAuth Implicit Grant Type MUST only register URLs using the https scheme
comment|// as redirect_uris; they MUST NOT use localhost as the hostname. Native Clients MUST only register
comment|// redirect_uris using custom URI schemes or URLs using the http: scheme with localhost as the hostname.
comment|// Authorization Servers MAY place additional constraints on Native Clients. Authorization Servers MAY
comment|// reject Redirection URI values using the http scheme, other than the localhost case for Native Clients
block|}
specifier|public
name|void
name|setClientProvider
parameter_list|(
name|ClientRegistrationProvider
name|clientProvider
parameter_list|)
block|{
name|this
operator|.
name|clientProvider
operator|=
name|clientProvider
expr_stmt|;
block|}
specifier|protected
name|String
name|generateClientId
parameter_list|()
block|{
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
name|getClientIdSizeInBytes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|int
name|getClientIdSizeInBytes
parameter_list|()
block|{
return|return
name|clientIdSizeInBytes
return|;
block|}
specifier|public
name|void
name|setClientIdSizeInBytes
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|clientIdSizeInBytes
operator|=
name|size
expr_stmt|;
block|}
specifier|protected
name|String
name|generateClientSecret
parameter_list|(
name|ClientRegistration
name|request
parameter_list|)
block|{
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
name|getClientSecretSizeInBytes
argument_list|(
name|request
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getRequestAccessToken
parameter_list|()
block|{
return|return
name|AuthorizationUtils
operator|.
name|getAuthorizationParts
argument_list|(
name|getMessageContext
argument_list|()
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|OAuthConstants
operator|.
name|BEARER_AUTHORIZATION_SCHEME
argument_list|)
argument_list|)
index|[
literal|1
index|]
return|;
block|}
specifier|protected
name|int
name|getClientSecretSizeInBytes
parameter_list|(
name|ClientRegistration
name|request
parameter_list|)
block|{
return|return
literal|16
return|;
block|}
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|mc
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|MessageContext
name|getMessageContext
parameter_list|()
block|{
return|return
name|mc
return|;
block|}
specifier|public
name|void
name|setSupportRegistrationAccessTokens
parameter_list|(
name|boolean
name|supportRegistrationAccessTokens
parameter_list|)
block|{
name|this
operator|.
name|supportRegistrationAccessTokens
operator|=
name|supportRegistrationAccessTokens
expr_stmt|;
block|}
block|}
end_class

end_unit

