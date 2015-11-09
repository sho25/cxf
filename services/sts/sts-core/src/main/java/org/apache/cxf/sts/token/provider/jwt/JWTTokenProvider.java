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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|jwt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|logging
operator|.
name|LogUtils
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|common
operator|.
name|JoseConstants
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
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|sts
operator|.
name|STSPropertiesMBean
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
name|sts
operator|.
name|SignatureProperties
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProvider
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderResponse
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
name|sts
operator|.
name|token
operator|.
name|realm
operator|.
name|RealmProperties
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
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|crypto
operator|.
name|Crypto
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
name|crypto
operator|.
name|Merlin
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
name|WSPasswordCallback
import|;
end_import

begin_comment
comment|/**  * A TokenProvider implementation that provides a JWT Token.  */
end_comment

begin_class
specifier|public
class|class
name|JWTTokenProvider
implements|implements
name|TokenProvider
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JWT_TOKEN_TYPE
init|=
literal|"urn:ietf:params:oauth:token-type:jwt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JWTTokenProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|signToken
init|=
literal|true
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RealmProperties
argument_list|>
name|realmMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|JWTClaimsProvider
name|jwtClaimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
return|return
name|canHandleToken
argument_list|(
name|tokenType
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return true if this TokenProvider implementation is capable of providing a token      * that corresponds to the given TokenType in a given realm.      */
specifier|public
name|boolean
name|canHandleToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|realm
parameter_list|)
block|{
if|if
condition|(
name|realm
operator|!=
literal|null
operator|&&
operator|!
name|realmMap
operator|.
name|containsKey
argument_list|(
name|realm
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|JWT_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
return|;
block|}
comment|/**      * Create a token given a TokenProviderParameters      */
specifier|public
name|TokenProviderResponse
name|createToken
parameter_list|(
name|TokenProviderParameters
name|tokenParameters
parameter_list|)
block|{
comment|//KeyRequirements keyRequirements = tokenParameters.getKeyRequirements();
name|TokenRequirements
name|tokenRequirements
init|=
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Handling token of type: "
operator|+
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|realm
init|=
name|tokenParameters
operator|.
name|getRealm
argument_list|()
decl_stmt|;
name|RealmProperties
name|jwtRealm
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
operator|&&
name|realmMap
operator|.
name|containsKey
argument_list|(
name|realm
argument_list|)
condition|)
block|{
name|jwtRealm
operator|=
name|realmMap
operator|.
name|get
argument_list|(
name|realm
argument_list|)
expr_stmt|;
block|}
comment|// Get the claims
name|JWTClaimsProviderParameters
name|jwtClaimsProviderParameters
init|=
operator|new
name|JWTClaimsProviderParameters
argument_list|()
decl_stmt|;
name|jwtClaimsProviderParameters
operator|.
name|setProviderParameters
argument_list|(
name|tokenParameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|jwtRealm
operator|!=
literal|null
condition|)
block|{
name|jwtClaimsProviderParameters
operator|.
name|setIssuer
argument_list|(
name|jwtRealm
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JwtClaims
name|claims
init|=
name|jwtClaimsProvider
operator|.
name|getJwtClaims
argument_list|(
name|jwtClaimsProviderParameters
argument_list|)
decl_stmt|;
try|try
block|{
comment|/*             Document doc = DOMUtils.createDocument();             SamlAssertionWrapper assertion = createSamlToken(tokenParameters, secret, doc);             Element token = assertion.toDOM(doc);                          // set the token in cache (only if the token is signed)             byte[] signatureValue = assertion.getSignatureValue();             if (tokenParameters.getTokenStore() != null&& signatureValue != null&& signatureValue.length> 0) {                 DateTime validTill = null;                 if (assertion.getSamlVersion().equals(SAMLVersion.VERSION_20)) {                     validTill = assertion.getSaml2().getConditions().getNotOnOrAfter();                 } else {                     validTill = assertion.getSaml1().getConditions().getNotOnOrAfter();                 }                                  SecurityToken securityToken =                      CacheUtils.createSecurityTokenForStorage(token, assertion.getId(),                          validTill.toDate(), tokenParameters.getPrincipal(), tokenParameters.getRealm(),                         tokenParameters.getTokenRequirements().getRenewing());                 CacheUtils.storeTokenInCache(                     securityToken, tokenParameters.getTokenStore(), signatureValue);             }             */
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|String
name|tokenData
init|=
name|signToken
argument_list|(
name|token
argument_list|,
name|jwtRealm
argument_list|,
name|tokenParameters
operator|.
name|getStsProperties
argument_list|()
argument_list|,
name|tokenParameters
operator|.
name|getTokenRequirements
argument_list|()
argument_list|)
decl_stmt|;
name|TokenProviderResponse
name|response
init|=
operator|new
name|TokenProviderResponse
argument_list|()
decl_stmt|;
name|response
operator|.
name|setToken
argument_list|(
name|tokenData
argument_list|)
expr_stmt|;
name|response
operator|.
name|setTokenId
argument_list|(
name|claims
operator|.
name|getTokenId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|claims
operator|.
name|getIssuedAt
argument_list|()
operator|>
literal|0
condition|)
block|{
name|response
operator|.
name|setCreated
argument_list|(
operator|new
name|Date
argument_list|(
name|claims
operator|.
name|getIssuedAt
argument_list|()
operator|*
literal|1000L
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|claims
operator|.
name|getExpiryTime
argument_list|()
operator|>
literal|0
condition|)
block|{
name|response
operator|.
name|setExpires
argument_list|(
operator|new
name|Date
argument_list|(
name|claims
operator|.
name|getExpiryTime
argument_list|()
operator|*
literal|1000L
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"JWT Token successfully created"
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't serialize JWT token"
argument_list|,
name|e
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
block|}
comment|/**      * Return whether the provided token will be signed or not. Default is true.      */
specifier|public
name|boolean
name|isSignToken
parameter_list|()
block|{
return|return
name|signToken
return|;
block|}
comment|/**      * Set whether the provided token will be signed or not. Default is true.      */
specifier|public
name|void
name|setSignToken
parameter_list|(
name|boolean
name|signToken
parameter_list|)
block|{
name|this
operator|.
name|signToken
operator|=
name|signToken
expr_stmt|;
block|}
comment|/**      * Set the map of realm->RealmProperties for this token provider      * @param realms the map of realm->RealmProperties for this token provider      */
specifier|public
name|void
name|setRealmMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|RealmProperties
argument_list|>
name|realms
parameter_list|)
block|{
name|this
operator|.
name|realmMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|realmMap
operator|.
name|putAll
argument_list|(
name|realms
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the map of realm->RealmProperties for this token provider      * @return the map of realm->RealmProperties for this token provider      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RealmProperties
argument_list|>
name|getRealmMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|realmMap
argument_list|)
return|;
block|}
specifier|public
name|JWTClaimsProvider
name|getJwtClaimsProvider
parameter_list|()
block|{
return|return
name|jwtClaimsProvider
return|;
block|}
specifier|public
name|void
name|setJwtClaimsProvider
parameter_list|(
name|JWTClaimsProvider
name|jwtClaimsProvider
parameter_list|)
block|{
name|this
operator|.
name|jwtClaimsProvider
operator|=
name|jwtClaimsProvider
expr_stmt|;
block|}
specifier|private
name|String
name|signToken
parameter_list|(
name|JwtToken
name|token
parameter_list|,
name|RealmProperties
name|jwtRealm
parameter_list|,
name|STSPropertiesMBean
name|stsProperties
parameter_list|,
name|TokenRequirements
name|tokenRequirements
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|signingProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
if|if
condition|(
name|signToken
condition|)
block|{
comment|// Initialise signature objects with defaults of STSPropertiesMBean
name|Crypto
name|signatureCrypto
init|=
name|stsProperties
operator|.
name|getSignatureCrypto
argument_list|()
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
name|stsProperties
operator|.
name|getCallbackHandler
argument_list|()
decl_stmt|;
name|SignatureProperties
name|signatureProperties
init|=
name|stsProperties
operator|.
name|getSignatureProperties
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|stsProperties
operator|.
name|getSignatureUsername
argument_list|()
decl_stmt|;
if|if
condition|(
name|jwtRealm
operator|!=
literal|null
condition|)
block|{
comment|// If SignatureCrypto configured in realm then
comment|// callbackhandler and alias of STSPropertiesMBean is ignored
if|if
condition|(
name|jwtRealm
operator|.
name|getSignatureCrypto
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"SAMLRealm signature keystore used"
argument_list|)
expr_stmt|;
name|signatureCrypto
operator|=
name|jwtRealm
operator|.
name|getSignatureCrypto
argument_list|()
expr_stmt|;
name|callbackHandler
operator|=
name|jwtRealm
operator|.
name|getCallbackHandler
argument_list|()
expr_stmt|;
name|alias
operator|=
name|jwtRealm
operator|.
name|getSignatureAlias
argument_list|()
expr_stmt|;
block|}
comment|// SignatureProperties can be defined independently of SignatureCrypto
if|if
condition|(
name|jwtRealm
operator|.
name|getSignatureProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|signatureProperties
operator|=
name|jwtRealm
operator|.
name|getSignatureProperties
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Get the signature algorithm to use - for now we don't allow the client to ask
comment|// for a particular signature algorithm, as with SAML
name|String
name|signatureAlgorithm
init|=
name|signatureProperties
operator|.
name|getSignatureAlgorithm
argument_list|()
decl_stmt|;
try|try
block|{
name|SignatureAlgorithm
operator|.
name|getAlgorithm
argument_list|(
name|signatureAlgorithm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|signatureAlgorithm
operator|=
name|SignatureAlgorithm
operator|.
name|RS256
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
comment|// If alias not defined, get the default of the SignatureCrypto
if|if
condition|(
operator|(
name|alias
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|alias
argument_list|)
operator|)
operator|&&
operator|(
name|signatureCrypto
operator|!=
literal|null
operator|)
condition|)
block|{
name|alias
operator|=
name|signatureCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Signature alias is null so using default alias: "
operator|+
name|alias
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Get the password
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|alias
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|)
block|}
decl_stmt|;
name|callbackHandler
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
name|String
name|password
init|=
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
decl_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_SIGNATURE_ALGORITHM
argument_list|,
name|signatureAlgorithm
argument_list|)
expr_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
condition|)
block|{
name|signingProperties
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
name|alias
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|signingProperties
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_PSWD
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't get the password"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
operator|(
name|signatureCrypto
operator|instanceof
name|Merlin
operator|)
condition|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Can't get the keystore"
argument_list|,
name|STSException
operator|.
name|REQUEST_FAILED
argument_list|)
throw|;
block|}
name|KeyStore
name|keystore
init|=
operator|(
operator|(
name|Merlin
operator|)
name|signatureCrypto
operator|)
operator|.
name|getKeyStore
argument_list|()
decl_stmt|;
name|signingProperties
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE
argument_list|,
name|keystore
argument_list|)
expr_stmt|;
name|JwsJwtCompactProducer
name|jws
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|jws
operator|.
name|setSignatureProperties
argument_list|(
name|signingProperties
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|JwsSignatureProvider
name|sigProvider
init|=
name|JwsUtils
operator|.
name|loadSignatureProvider
argument_list|(
name|m
argument_list|,
name|signingProperties
argument_list|,
name|token
operator|.
name|getJwsHeaders
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|token
operator|.
name|getJwsHeaders
argument_list|()
operator|.
name|setSignatureAlgorithm
argument_list|(
name|sigProvider
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jws
operator|.
name|signWith
argument_list|(
name|sigProvider
argument_list|)
return|;
block|}
else|else
block|{
name|signingProperties
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_SIGNATURE_ALGORITHM
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|JwsJwtCompactProducer
name|jws
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|jws
operator|.
name|setSignatureProperties
argument_list|(
name|signingProperties
argument_list|)
expr_stmt|;
return|return
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

