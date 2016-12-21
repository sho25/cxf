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
name|oidc
operator|.
name|rp
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|jose
operator|.
name|jwk
operator|.
name|JsonWebKey
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
name|jwk
operator|.
name|JsonWebKeys
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
name|jwk
operator|.
name|JwkUtils
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
name|JwsSignatureVerifier
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
name|JwtException
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JwtUtils
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
name|OAuthJoseJwtConsumer
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
import|;
end_import

begin_class
specifier|public
class|class
name|OidcClaimsValidator
extends|extends
name|OAuthJoseJwtConsumer
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SELF_ISSUED_ISSUER
init|=
literal|"https://self-issued.me"
decl_stmt|;
specifier|private
name|String
name|issuerId
decl_stmt|;
specifier|private
name|WebClient
name|jwkSetClient
decl_stmt|;
specifier|private
name|boolean
name|supportSelfIssuedProvider
decl_stmt|;
specifier|private
name|boolean
name|strictTimeValidation
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|JsonWebKey
argument_list|>
name|keyMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|JsonWebKey
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Validate core JWT claims      * @param claims the claims      * @param clientId OAuth2 client id      * @param validateClaimsAlways if set to true then enforce that the claims       *                             to be validated must be set      */
specifier|public
name|void
name|validateJwtClaims
parameter_list|(
name|JwtClaims
name|claims
parameter_list|,
name|String
name|clientId
parameter_list|,
name|boolean
name|validateClaimsAlways
parameter_list|)
block|{
comment|// validate the issuer
name|String
name|issuer
init|=
name|claims
operator|.
name|getIssuer
argument_list|()
decl_stmt|;
if|if
condition|(
name|issuer
operator|==
literal|null
operator|&&
name|validateClaimsAlways
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid issuer"
argument_list|)
throw|;
block|}
if|if
condition|(
name|supportSelfIssuedProvider
operator|&&
name|issuerId
operator|==
literal|null
operator|&&
name|issuer
operator|!=
literal|null
operator|&&
name|SELF_ISSUED_ISSUER
operator|.
name|equals
argument_list|(
name|issuer
argument_list|)
condition|)
block|{
name|validateSelfIssuedProvider
argument_list|(
name|claims
argument_list|,
name|clientId
argument_list|,
name|validateClaimsAlways
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|issuer
operator|!=
literal|null
operator|&&
operator|!
name|issuer
operator|.
name|equals
argument_list|(
name|issuerId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid issuer"
argument_list|)
throw|;
block|}
comment|// validate subject
if|if
condition|(
name|claims
operator|.
name|getSubject
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid subject"
argument_list|)
throw|;
block|}
comment|// validate authorized party
name|String
name|authorizedParty
init|=
operator|(
name|String
operator|)
name|claims
operator|.
name|getClaim
argument_list|(
name|IdToken
operator|.
name|AZP_CLAIM
argument_list|)
decl_stmt|;
if|if
condition|(
name|authorizedParty
operator|!=
literal|null
operator|&&
operator|!
name|authorizedParty
operator|.
name|equals
argument_list|(
name|clientId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid authorized party"
argument_list|)
throw|;
block|}
comment|// validate audience
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
name|claims
operator|.
name|getAudiences
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|audiences
argument_list|)
operator|&&
name|validateClaimsAlways
operator|||
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|audiences
argument_list|)
operator|&&
operator|!
name|audiences
operator|.
name|contains
argument_list|(
name|clientId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid audience"
argument_list|)
throw|;
block|}
comment|// If strict time validation: if no issuedTime claim is set then an expiresAt claim must be set
comment|// Otherwise: validate only if expiresAt claim is set
name|boolean
name|expiredRequired
init|=
name|validateClaimsAlways
operator|||
name|strictTimeValidation
operator|&&
name|claims
operator|.
name|getIssuedAt
argument_list|()
operator|==
literal|null
decl_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtExpiry
argument_list|(
name|claims
argument_list|,
name|getClockOffset
argument_list|()
argument_list|,
name|expiredRequired
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"ID Token has expired"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
comment|// If strict time validation: If no expiresAt claim is set then an issuedAt claim must be set
comment|// Otherwise: validate only if issuedAt claim is set
name|boolean
name|issuedAtRequired
init|=
name|validateClaimsAlways
operator|||
name|strictTimeValidation
operator|&&
name|claims
operator|.
name|getExpiryTime
argument_list|()
operator|==
literal|null
decl_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|claims
argument_list|,
name|getTtl
argument_list|()
argument_list|,
name|getClockOffset
argument_list|()
argument_list|,
name|issuedAtRequired
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Invalid issuedAt claim"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
name|strictTimeValidation
condition|)
block|{
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtNotBefore
argument_list|(
name|claims
argument_list|,
name|getClockOffset
argument_list|()
argument_list|,
name|strictTimeValidation
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"ID Token can not be used yet"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|validateSelfIssuedProvider
parameter_list|(
name|JwtClaims
name|claims
parameter_list|,
name|String
name|clientId
parameter_list|,
name|boolean
name|validateClaimsAlways
parameter_list|)
block|{     }
specifier|public
name|void
name|setIssuerId
parameter_list|(
name|String
name|issuerId
parameter_list|)
block|{
name|this
operator|.
name|issuerId
operator|=
name|issuerId
expr_stmt|;
block|}
specifier|public
name|void
name|setJwkSetClient
parameter_list|(
name|WebClient
name|jwkSetClient
parameter_list|)
block|{
name|this
operator|.
name|jwkSetClient
operator|=
name|jwkSetClient
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSignatureVerifier
parameter_list|(
name|JwtToken
name|jwt
parameter_list|)
block|{
name|JsonWebKey
name|key
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|supportSelfIssuedProvider
operator|&&
name|SELF_ISSUED_ISSUER
operator|.
name|equals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
literal|"issuer"
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|publicKeyJson
init|=
operator|(
name|String
operator|)
name|jwt
operator|.
name|getClaim
argument_list|(
literal|"sub_jwk"
argument_list|)
decl_stmt|;
if|if
condition|(
name|publicKeyJson
operator|!=
literal|null
condition|)
block|{
name|JsonWebKey
name|publicKey
init|=
name|JwkUtils
operator|.
name|readJwkKey
argument_list|(
name|publicKeyJson
argument_list|)
decl_stmt|;
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|publicKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|thumbprint
operator|.
name|equals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
literal|"sub"
argument_list|)
argument_list|)
condition|)
block|{
name|key
operator|=
name|publicKey
expr_stmt|;
block|}
block|}
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Self-issued JWK key is invalid or not available"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|String
name|keyId
init|=
name|jwt
operator|.
name|getJwsHeaders
argument_list|()
operator|.
name|getKeyId
argument_list|()
decl_stmt|;
name|key
operator|=
name|keyId
operator|!=
literal|null
condition|?
name|keyMap
operator|.
name|get
argument_list|(
name|keyId
argument_list|)
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
operator|&&
name|jwkSetClient
operator|!=
literal|null
condition|)
block|{
name|JsonWebKeys
name|keys
init|=
name|jwkSetClient
operator|.
name|get
argument_list|(
name|JsonWebKeys
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyId
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|keys
operator|.
name|getKey
argument_list|(
name|keyId
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keys
operator|.
name|getKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|key
operator|=
name|keys
operator|.
name|getKeys
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|//jwkSetClient returns the most up-to-date keys
name|keyMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|keyMap
operator|.
name|putAll
argument_list|(
name|keys
operator|.
name|getKeyIdMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|JwsSignatureVerifier
name|theJwsVerifier
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|theJwsVerifier
operator|=
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|theJwsVerifier
operator|=
name|super
operator|.
name|getInitializedSignatureVerifier
argument_list|(
name|jwt
operator|.
name|getJwsHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theJwsVerifier
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"JWS Verifier is not available"
argument_list|)
throw|;
block|}
return|return
name|theJwsVerifier
return|;
block|}
specifier|public
name|void
name|setSupportSelfIssuedProvider
parameter_list|(
name|boolean
name|supportSelfIssuedProvider
parameter_list|)
block|{
name|this
operator|.
name|supportSelfIssuedProvider
operator|=
name|supportSelfIssuedProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setStrictTimeValidation
parameter_list|(
name|boolean
name|strictTimeValidation
parameter_list|)
block|{
name|this
operator|.
name|strictTimeValidation
operator|=
name|strictTimeValidation
expr_stmt|;
block|}
block|}
end_class

end_unit

