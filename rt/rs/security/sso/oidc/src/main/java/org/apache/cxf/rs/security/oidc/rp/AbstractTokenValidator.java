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
name|jwe
operator|.
name|JweDecryptionProvider
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
name|jwe
operator|.
name|JweJwtCompactConsumer
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
name|jwe
operator|.
name|JweUtils
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
name|jws
operator|.
name|JwsJwtCompactConsumer
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTokenValidator
block|{
specifier|private
name|JweDecryptionProvider
name|jweDecryptor
decl_stmt|;
specifier|private
name|JwsSignatureVerifier
name|jwsVerifier
decl_stmt|;
specifier|private
name|String
name|issuerId
decl_stmt|;
specifier|private
name|int
name|issuedAtRange
decl_stmt|;
specifier|private
name|WebClient
name|jwkSetClient
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
specifier|protected
name|JwtToken
name|getJwtToken
parameter_list|(
name|String
name|wrappedJwtToken
parameter_list|,
name|String
name|clientId
parameter_list|,
name|String
name|idTokenKid
parameter_list|,
name|boolean
name|jweOnly
parameter_list|)
block|{
if|if
condition|(
name|wrappedJwtToken
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"ID Token is missing"
argument_list|)
throw|;
block|}
name|JweDecryptionProvider
name|theJweDecryptor
init|=
name|getInitializedDecryptionProvider
argument_list|(
name|jweOnly
argument_list|)
decl_stmt|;
if|if
condition|(
name|theJweDecryptor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|jweOnly
condition|)
block|{
return|return
operator|new
name|JweJwtCompactConsumer
argument_list|(
name|wrappedJwtToken
argument_list|)
operator|.
name|decryptWith
argument_list|(
name|jweDecryptor
argument_list|)
return|;
block|}
name|wrappedJwtToken
operator|=
name|jweDecryptor
operator|.
name|decrypt
argument_list|(
name|wrappedJwtToken
argument_list|)
operator|.
name|getContentText
argument_list|()
expr_stmt|;
block|}
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|wrappedJwtToken
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|JwsSignatureVerifier
name|theSigVerifier
init|=
name|getInitializedSigVerifier
argument_list|(
name|jwt
argument_list|,
name|idTokenKid
argument_list|)
decl_stmt|;
return|return
name|validateToken
argument_list|(
name|jwtConsumer
argument_list|,
name|jwt
argument_list|,
name|theSigVerifier
argument_list|)
return|;
block|}
specifier|protected
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
name|SecurityException
argument_list|(
literal|"Invalid subject"
argument_list|)
throw|;
block|}
comment|// validate audience
name|String
name|aud
init|=
name|claims
operator|.
name|getAudience
argument_list|()
decl_stmt|;
if|if
condition|(
name|aud
operator|==
literal|null
operator|&&
name|validateClaimsAlways
operator|||
name|aud
operator|!=
literal|null
operator|&&
operator|!
name|clientId
operator|.
name|equals
argument_list|(
name|aud
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid audience"
argument_list|)
throw|;
block|}
comment|// validate the provider
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
operator|||
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
name|SecurityException
argument_list|(
literal|"Invalid provider"
argument_list|)
throw|;
block|}
name|JwtUtils
operator|.
name|validateJwtTimeClaims
argument_list|(
name|claims
argument_list|,
name|issuedAtRange
argument_list|,
name|validateClaimsAlways
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JwtToken
name|validateToken
parameter_list|(
name|JwsJwtCompactConsumer
name|consumer
parameter_list|,
name|JwtToken
name|jwt
parameter_list|,
name|JwsSignatureVerifier
name|jws
parameter_list|)
block|{
if|if
condition|(
operator|!
name|consumer
operator|.
name|verifySignatureWith
argument_list|(
name|jws
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Invalid Signature"
argument_list|)
throw|;
block|}
return|return
name|jwt
return|;
block|}
specifier|public
name|void
name|setJweDecryptor
parameter_list|(
name|JweDecryptionProvider
name|jweDecryptor
parameter_list|)
block|{
name|this
operator|.
name|jweDecryptor
operator|=
name|jweDecryptor
expr_stmt|;
block|}
specifier|public
name|void
name|setJweVerifier
parameter_list|(
name|JwsSignatureVerifier
name|theJwsVerifier
parameter_list|)
block|{
name|this
operator|.
name|jwsVerifier
operator|=
name|theJwsVerifier
expr_stmt|;
block|}
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
specifier|public
name|void
name|setIssuedAtRange
parameter_list|(
name|int
name|issuedAtRange
parameter_list|)
block|{
name|this
operator|.
name|issuedAtRange
operator|=
name|issuedAtRange
expr_stmt|;
block|}
specifier|protected
name|JweDecryptionProvider
name|getInitializedDecryptionProvider
parameter_list|(
name|boolean
name|jweOnly
parameter_list|)
block|{
if|if
condition|(
name|jweDecryptor
operator|!=
literal|null
condition|)
block|{
return|return
name|jweDecryptor
return|;
block|}
return|return
name|JweUtils
operator|.
name|loadDecryptionProvider
argument_list|(
name|jweOnly
argument_list|)
return|;
block|}
specifier|protected
name|JwsSignatureVerifier
name|getInitializedSigVerifier
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|String
name|idTokenKid
parameter_list|)
block|{
if|if
condition|(
name|jwsVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|jwsVerifier
return|;
block|}
name|JwsSignatureVerifier
name|theJwsVerifier
init|=
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|theJwsVerifier
operator|!=
literal|null
condition|)
block|{
return|return
name|theJwsVerifier
return|;
block|}
if|if
condition|(
name|jwkSetClient
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
literal|"Provider Jwk Set Client is not available"
argument_list|)
throw|;
block|}
name|String
name|keyId
init|=
name|idTokenKid
operator|!=
literal|null
condition|?
name|idTokenKid
else|:
name|jwt
operator|.
name|getHeaders
argument_list|()
operator|.
name|getKeyId
argument_list|()
decl_stmt|;
name|JsonWebKey
name|key
init|=
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
decl_stmt|;
if|if
condition|(
name|key
operator|==
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
literal|"JWK key with the key id: \""
operator|+
name|keyId
operator|+
literal|"\" is not available"
argument_list|)
throw|;
block|}
name|theJwsVerifier
operator|=
name|JwsUtils
operator|.
name|getSignatureVerifier
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|jwkSetClient
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|theJwsVerifier
return|;
block|}
block|}
end_class

end_unit

