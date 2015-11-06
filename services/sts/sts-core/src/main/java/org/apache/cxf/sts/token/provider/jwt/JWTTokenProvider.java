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
name|SAMLRealm
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
name|SAMLRealm
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
comment|/*         if (signToken) {             STSPropertiesMBean stsProperties = tokenParameters.getStsProperties();             signToken(assertion, samlRealm, stsProperties, tokenParameters.getKeyRequirements());         }         */
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
name|String
name|tokenData
init|=
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
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
comment|/*response.setEntropy(entropyBytes);             if (keySize> 0) {                 response.setKeySize(keySize);             }             response.setComputedKey(computedKey);             */
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
comment|/**      * Set the map of realm->SAMLRealm for this token provider      * @param realms the map of realm->SAMLRealm for this token provider      */
specifier|public
name|void
name|setRealmMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|SAMLRealm
argument_list|>
name|realms
parameter_list|)
block|{
name|this
operator|.
name|realmMap
operator|=
name|realms
expr_stmt|;
block|}
comment|/**      * Get the map of realm->SAMLRealm for this token provider      * @return the map of realm->SAMLRealm for this token provider      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|SAMLRealm
argument_list|>
name|getRealmMap
parameter_list|()
block|{
return|return
name|realmMap
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
block|}
end_class

end_unit
