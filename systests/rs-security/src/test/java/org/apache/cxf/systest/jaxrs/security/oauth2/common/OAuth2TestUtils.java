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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|jaxrs
operator|.
name|provider
operator|.
name|json
operator|.
name|JSONProvider
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
name|provider
operator|.
name|json
operator|.
name|JsonMapObjectProvider
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
name|jose
operator|.
name|jaxrs
operator|.
name|JsonWebKeysProvider
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
name|jose
operator|.
name|jwa
operator|.
name|SignatureAlgorithm
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
name|jose
operator|.
name|jws
operator|.
name|JwsHeaders
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
name|jose
operator|.
name|jws
operator|.
name|JwsJwtCompactProducer
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
name|jose
operator|.
name|jws
operator|.
name|JwsSignatureProvider
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
name|jose
operator|.
name|jws
operator|.
name|JwsUtils
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
name|jose
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|OAuthAuthorizationData
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
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|builder
operator|.
name|SAML1Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * Some test utils for the OAuth 2.0 tests  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OAuth2TestUtils
block|{
specifier|private
name|OAuth2TestUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|String
name|getAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|)
block|{
return|return
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
return|return
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
name|scope
argument_list|,
literal|"consumer-id"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|scope
parameter_list|,
name|String
name|consumerId
parameter_list|)
block|{
return|return
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
name|scope
argument_list|,
name|consumerId
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|scope
parameter_list|,
name|String
name|consumerId
parameter_list|,
name|String
name|nonce
parameter_list|,
name|String
name|state
parameter_list|)
block|{
name|AuthorizationCodeParameters
name|parameters
init|=
operator|new
name|AuthorizationCodeParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setConsumerId
argument_list|(
name|consumerId
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setScope
argument_list|(
name|scope
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setNonce
argument_list|(
name|nonce
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setState
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setResponseType
argument_list|(
literal|"code"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPath
argument_list|(
literal|"authorize/"
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|getLocation
argument_list|(
name|client
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
return|return
name|getSubstring
argument_list|(
name|location
argument_list|,
literal|"code"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getLocation
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|AuthorizationCodeParameters
name|parameters
parameter_list|)
block|{
comment|// Make initial authorization request
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"client_id"
argument_list|,
name|parameters
operator|.
name|getConsumerId
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"redirect_uri"
argument_list|,
literal|"http://www.blah.apache.org"
argument_list|)
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"response_type"
argument_list|,
name|parameters
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameters
operator|.
name|getScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|query
argument_list|(
literal|"scope"
argument_list|,
name|parameters
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|query
argument_list|(
literal|"nonce"
argument_list|,
name|parameters
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|getState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|query
argument_list|(
literal|"state"
argument_list|,
name|parameters
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|.
name|getRequest
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|query
argument_list|(
literal|"request"
argument_list|,
name|parameters
operator|.
name|getRequest
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|path
argument_list|(
name|parameters
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|get
argument_list|()
decl_stmt|;
name|OAuthAuthorizationData
name|authzData
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|OAuthAuthorizationData
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|getLocation
argument_list|(
name|client
argument_list|,
name|authzData
argument_list|,
name|parameters
operator|.
name|getState
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getLocation
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|OAuthAuthorizationData
name|authzData
parameter_list|,
name|String
name|state
parameter_list|)
block|{
comment|// Now call "decision" to get the authorization code grant
name|client
operator|.
name|path
argument_list|(
literal|"decision"
argument_list|)
expr_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
expr_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"session_authenticity_token"
argument_list|,
name|authzData
operator|.
name|getAuthenticityToken
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"client_id"
argument_list|,
name|authzData
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"redirect_uri"
argument_list|,
name|authzData
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|authzData
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|form
operator|.
name|param
argument_list|(
literal|"nonce"
argument_list|,
name|authzData
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authzData
operator|.
name|getProposedScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|form
operator|.
name|param
argument_list|(
literal|"scope"
argument_list|,
name|authzData
operator|.
name|getProposedScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authzData
operator|.
name|getState
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|form
operator|.
name|param
argument_list|(
literal|"state"
argument_list|,
name|authzData
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|form
operator|.
name|param
argument_list|(
literal|"response_type"
argument_list|,
name|authzData
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"oauthDecision"
argument_list|,
literal|"allow"
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
name|form
argument_list|)
decl_stmt|;
name|String
name|location
init|=
name|response
operator|.
name|getHeaderString
argument_list|(
literal|"Location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|location
operator|.
name|contains
argument_list|(
literal|"state="
operator|+
name|state
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|location
return|;
block|}
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessTokenWithAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|code
parameter_list|)
block|{
return|return
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|client
argument_list|,
name|code
argument_list|,
literal|"consumer-id"
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ClientAccessToken
name|getAccessTokenWithAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|code
parameter_list|,
name|String
name|consumerId
parameter_list|,
name|String
name|audience
parameter_list|)
block|{
name|client
operator|.
name|type
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"token"
argument_list|)
expr_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"grant_type"
argument_list|,
literal|"authorization_code"
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"code"
argument_list|,
name|code
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"client_id"
argument_list|,
name|consumerId
argument_list|)
expr_stmt|;
if|if
condition|(
name|audience
operator|!=
literal|null
condition|)
block|{
name|form
operator|.
name|param
argument_list|(
literal|"audience"
argument_list|,
name|audience
argument_list|)
expr_stmt|;
block|}
name|form
operator|.
name|param
argument_list|(
literal|"redirect_uri"
argument_list|,
literal|"http://www.blah.apache.org"
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
name|form
argument_list|)
decl_stmt|;
return|return
name|response
operator|.
name|readEntity
argument_list|(
name|ClientAccessToken
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Object
argument_list|>
name|setupProviders
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|JSONProvider
argument_list|<
name|OAuthAuthorizationData
argument_list|>
name|jsonP
init|=
operator|new
name|JSONProvider
argument_list|<
name|OAuthAuthorizationData
argument_list|>
argument_list|()
decl_stmt|;
name|jsonP
operator|.
name|setNamespaceMap
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"http://org.apache.cxf.rs.security.oauth"
argument_list|,
literal|"ns2"
argument_list|)
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jsonP
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|OAuthJSONProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JsonWebKeysProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JsonMapObjectProvider
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|providers
return|;
block|}
specifier|public
specifier|static
name|String
name|createToken
parameter_list|(
name|String
name|audRestr
parameter_list|)
throws|throws
name|WSSecurityException
block|{
return|return
name|createToken
argument_list|(
name|audRestr
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|createToken
parameter_list|(
name|String
name|audRestr
parameter_list|,
name|boolean
name|saml2
parameter_list|,
name|boolean
name|sign
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SamlCallbackHandler
name|samlCallbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|(
name|sign
argument_list|)
decl_stmt|;
name|samlCallbackHandler
operator|.
name|setAudience
argument_list|(
name|audRestr
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|saml2
condition|)
block|{
name|samlCallbackHandler
operator|.
name|setSaml2
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|samlCallbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML1Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
block|}
name|SAMLCallback
name|samlCallback
init|=
operator|new
name|SAMLCallback
argument_list|()
decl_stmt|;
name|SAMLUtil
operator|.
name|doSAMLCallback
argument_list|(
name|samlCallbackHandler
argument_list|,
name|samlCallback
argument_list|)
expr_stmt|;
name|SamlAssertionWrapper
name|samlAssertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|samlCallback
argument_list|)
decl_stmt|;
if|if
condition|(
name|samlCallback
operator|.
name|isSignAssertion
argument_list|()
condition|)
block|{
name|samlAssertion
operator|.
name|signAssertion
argument_list|(
name|samlCallback
operator|.
name|getIssuerKeyName
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|getIssuerKeyPassword
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|getIssuerCrypto
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|isSendKeyValue
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|getCanonicalizationAlgorithm
argument_list|()
argument_list|,
name|samlCallback
operator|.
name|getSignatureAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|samlAssertion
operator|.
name|assertionToString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|createToken
parameter_list|(
name|String
name|issuer
parameter_list|,
name|String
name|subject
parameter_list|,
name|String
name|audience
parameter_list|,
name|boolean
name|expiry
parameter_list|,
name|boolean
name|sign
parameter_list|)
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
if|if
condition|(
name|issuer
operator|!=
literal|null
condition|)
block|{
name|claims
operator|.
name|setIssuer
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
block|}
name|Instant
name|now
init|=
name|Instant
operator|.
name|now
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|now
operator|.
name|getEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|expiry
condition|)
block|{
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|now
operator|.
name|plusSeconds
argument_list|(
literal|60L
argument_list|)
operator|.
name|getEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|audience
operator|!=
literal|null
condition|)
block|{
name|claims
operator|.
name|setAudiences
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|audience
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sign
condition|)
block|{
comment|// Sign the JWT Token
name|Properties
name|signingProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
literal|"rs.security.keystore.type"
argument_list|,
literal|"jks"
argument_list|)
expr_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
literal|"rs.security.keystore.password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
literal|"rs.security.keystore.alias"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
literal|"rs.security.keystore.file"
argument_list|,
literal|"keys/alice.jks"
argument_list|)
expr_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
literal|"rs.security.key.password"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"RS256"
argument_list|)
expr_stmt|;
name|JwsHeaders
name|jwsHeaders
init|=
operator|new
name|JwsHeaders
argument_list|(
name|signingProperties
argument_list|)
decl_stmt|;
name|JwsJwtCompactProducer
name|jws
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|jwsHeaders
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|JwsSignatureProvider
name|sigProvider
init|=
name|JwsUtils
operator|.
name|loadSignatureProvider
argument_list|(
name|signingProperties
argument_list|,
name|jwsHeaders
argument_list|)
decl_stmt|;
return|return
name|jws
operator|.
name|signWith
argument_list|(
name|sigProvider
argument_list|)
return|;
block|}
name|JwsHeaders
name|jwsHeaders
init|=
operator|new
name|JwsHeaders
argument_list|(
name|SignatureAlgorithm
operator|.
name|NONE
argument_list|)
decl_stmt|;
name|JwsJwtCompactProducer
name|jws
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|jwsHeaders
argument_list|,
name|claims
argument_list|)
decl_stmt|;
return|return
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|getSubstring
parameter_list|(
name|String
name|parentString
parameter_list|,
name|String
name|substringName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|parentString
operator|.
name|contains
argument_list|(
name|substringName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|foundString
init|=
name|parentString
operator|.
name|substring
argument_list|(
name|parentString
operator|.
name|indexOf
argument_list|(
name|substringName
operator|+
literal|"="
argument_list|)
operator|+
operator|(
name|substringName
operator|+
literal|"="
operator|)
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|ampersandIndex
init|=
name|foundString
operator|.
name|indexOf
argument_list|(
literal|'&'
argument_list|)
decl_stmt|;
if|if
condition|(
name|ampersandIndex
operator|<
literal|1
condition|)
block|{
name|ampersandIndex
operator|=
name|foundString
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|foundString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ampersandIndex
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|AuthorizationCodeParameters
block|{
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|String
name|consumerId
decl_stmt|;
specifier|private
name|String
name|nonce
decl_stmt|;
specifier|private
name|String
name|state
decl_stmt|;
specifier|private
name|String
name|responseType
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|private
name|String
name|request
decl_stmt|;
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
specifier|public
name|String
name|getConsumerId
parameter_list|()
block|{
return|return
name|consumerId
return|;
block|}
specifier|public
name|void
name|setConsumerId
parameter_list|(
name|String
name|consumerId
parameter_list|)
block|{
name|this
operator|.
name|consumerId
operator|=
name|consumerId
expr_stmt|;
block|}
specifier|public
name|String
name|getNonce
parameter_list|()
block|{
return|return
name|nonce
return|;
block|}
specifier|public
name|void
name|setNonce
parameter_list|(
name|String
name|nonce
parameter_list|)
block|{
name|this
operator|.
name|nonce
operator|=
name|nonce
expr_stmt|;
block|}
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|state
return|;
block|}
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
specifier|public
name|String
name|getResponseType
parameter_list|()
block|{
return|return
name|responseType
return|;
block|}
specifier|public
name|void
name|setResponseType
parameter_list|(
name|String
name|responseType
parameter_list|)
block|{
name|this
operator|.
name|responseType
operator|=
name|responseType
expr_stmt|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|String
name|getRequest
parameter_list|()
block|{
return|return
name|request
return|;
block|}
specifier|public
name|void
name|setRequest
parameter_list|(
name|String
name|request
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

