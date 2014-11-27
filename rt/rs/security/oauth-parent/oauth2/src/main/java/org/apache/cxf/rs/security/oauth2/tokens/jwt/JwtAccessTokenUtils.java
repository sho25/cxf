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
name|tokens
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
name|interfaces
operator|.
name|RSAPrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
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
name|rs
operator|.
name|security
operator|.
name|jose
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
name|JoseHeaders
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
name|Algorithm
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
name|DirectKeyJweDecryption
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
name|JweEncryptionProvider
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
name|JwsSignature
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
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JwtAccessTokenUtils
block|{
specifier|private
name|JwtAccessTokenUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|ServerAccessToken
name|encryptToAccessToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|Client
name|client
parameter_list|,
name|SecretKey
name|key
parameter_list|)
block|{
name|JweEncryptionProvider
name|jweEncryption
init|=
name|JweUtils
operator|.
name|getDirectKeyJweEncryption
argument_list|(
name|key
argument_list|,
name|Algorithm
operator|.
name|A128GCM
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|encryptToAccessToken
argument_list|(
name|jwt
argument_list|,
name|client
argument_list|,
name|jweEncryption
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ServerAccessToken
name|encryptToAccessToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|Client
name|client
parameter_list|,
name|JweEncryptionProvider
name|jweEncryption
parameter_list|)
block|{
name|String
name|jwtString
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|jwt
argument_list|)
operator|.
name|signWith
argument_list|(
operator|new
name|NoneSignatureProvider
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|tokenId
init|=
name|jweEncryption
operator|.
name|encrypt
argument_list|(
name|getBytes
argument_list|(
name|jwtString
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|toAccessToken
argument_list|(
name|jwt
argument_list|,
name|client
argument_list|,
name|tokenId
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|ServerAccessToken
name|toAccessToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|Client
name|client
parameter_list|,
name|String
name|tokenId
parameter_list|)
block|{
name|JwtClaims
name|claims
init|=
name|jwt
operator|.
name|getClaims
argument_list|()
decl_stmt|;
name|validateJwtSubjectAndAudience
argument_list|(
name|claims
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|Long
name|issuedAt
init|=
name|claims
operator|.
name|getIssuedAt
argument_list|()
decl_stmt|;
name|Long
name|notBefore
init|=
name|claims
operator|.
name|getNotBefore
argument_list|()
decl_stmt|;
name|Long
name|expiresIn
init|=
name|notBefore
operator|-
name|issuedAt
decl_stmt|;
return|return
operator|new
name|BearerAccessToken
argument_list|(
name|client
argument_list|,
name|tokenId
argument_list|,
name|issuedAt
argument_list|,
name|expiresIn
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwtToken
name|decryptFromfromAccessToken
parameter_list|(
name|String
name|tokenId
parameter_list|,
name|SecretKey
name|key
parameter_list|)
block|{
name|DirectKeyJweDecryption
name|jweDecryption
init|=
name|JweUtils
operator|.
name|getDirectKeyJweDecryption
argument_list|(
name|key
argument_list|,
name|Algorithm
operator|.
name|A128GCM
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|decryptFromAccessToken
argument_list|(
name|tokenId
argument_list|,
name|jweDecryption
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwtToken
name|decryptFromAccessToken
parameter_list|(
name|String
name|tokenId
parameter_list|,
name|JweDecryptionProvider
name|jweDecryption
parameter_list|)
block|{
name|String
name|decrypted
init|=
name|jweDecryption
operator|.
name|decrypt
argument_list|(
name|tokenId
argument_list|)
operator|.
name|getContentText
argument_list|()
decl_stmt|;
name|JwsJwtCompactConsumer
name|consumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|decrypted
argument_list|)
decl_stmt|;
return|return
name|consumer
operator|.
name|getJwtToken
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|ServerAccessToken
name|signToAccessToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|Client
name|client
parameter_list|,
name|RSAPrivateKey
name|key
parameter_list|)
block|{
name|JwsSignatureProvider
name|jws
init|=
name|JwsUtils
operator|.
name|getRSAKeySignatureProvider
argument_list|(
name|key
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
decl_stmt|;
return|return
name|signToAccessToken
argument_list|(
name|jwt
argument_list|,
name|client
argument_list|,
name|jws
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ServerAccessToken
name|signToAccessToken
parameter_list|(
name|JwtToken
name|jwt
parameter_list|,
name|Client
name|client
parameter_list|,
name|JwsSignatureProvider
name|jws
parameter_list|)
block|{
name|String
name|jwtString
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|jwt
argument_list|)
operator|.
name|signWith
argument_list|(
name|jws
argument_list|)
decl_stmt|;
return|return
name|toAccessToken
argument_list|(
name|jwt
argument_list|,
name|client
argument_list|,
name|jwtString
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwtToken
name|verifyAccessToken
parameter_list|(
name|String
name|tokenId
parameter_list|,
name|RSAPublicKey
name|key
parameter_list|)
block|{
name|JwsSignatureVerifier
name|jws
init|=
name|JwsUtils
operator|.
name|getRSAKeySignatureVerifier
argument_list|(
name|key
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
decl_stmt|;
return|return
name|verifyAccessToken
argument_list|(
name|tokenId
argument_list|,
name|jws
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|JwtToken
name|verifyAccessToken
parameter_list|(
name|String
name|tokenId
parameter_list|,
name|JwsSignatureVerifier
name|jws
parameter_list|)
block|{
name|JwsJwtCompactConsumer
name|consumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|tokenId
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|.
name|verifySignatureWith
argument_list|(
name|jws
argument_list|)
condition|)
block|{
return|return
name|consumer
operator|.
name|getJwtToken
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
specifier|public
specifier|static
name|void
name|validateJwtClaims
parameter_list|(
name|JwtClaims
name|claims
parameter_list|,
name|Client
name|c
parameter_list|)
block|{
name|validateJwtSubjectAndAudience
argument_list|(
name|claims
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|JwtUtils
operator|.
name|validateJwtTimeClaims
argument_list|(
name|claims
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|validateJwtSubjectAndAudience
parameter_list|(
name|JwtClaims
name|claims
parameter_list|,
name|Client
name|c
parameter_list|)
block|{
if|if
condition|(
name|claims
operator|.
name|getSubject
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|claims
operator|.
name|getSubject
argument_list|()
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
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
operator|||
operator|!
name|c
operator|.
name|getRegisteredAudiences
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|c
operator|.
name|getRegisteredAudiences
argument_list|()
operator|.
name|contains
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
comment|// TODO: the issuer is indirectly validated by validating the signature
comment|// but an extra check can be done
block|}
specifier|private
specifier|static
class|class
name|NoneSignatureProvider
implements|implements
name|JwsSignatureProvider
block|{
annotation|@
name|Override
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
literal|"none"
return|;
block|}
annotation|@
name|Override
specifier|public
name|JwsSignature
name|createJwsSignature
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|)
block|{
return|return
operator|new
name|NoneJwsSignature
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|sign
parameter_list|(
name|JoseHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|content
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|NoneJwsSignature
implements|implements
name|JwsSignature
block|{
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|sign
parameter_list|()
block|{
return|return
operator|new
name|byte
index|[]
block|{}
return|;
block|}
block|}
specifier|private
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
end_class

end_unit

