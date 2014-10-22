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
name|jose
operator|.
name|jwe
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jaxrs
operator|.
name|KeyManagementUtils
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
name|JwkUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JweUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
init|=
literal|"rs.security.jwe.content.encryption.algorithm"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_ENCRYPTION_KEY_ALGO_PROP
init|=
literal|"rs.security.jwe.key.encryption.algorithm"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JSON_WEB_ENCRYPTION_ZIP_ALGO_PROP
init|=
literal|"rs.security.jwe.zip.algorithm"
decl_stmt|;
specifier|private
name|JweUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|KeyEncryptionAlgorithm
name|getKeyEncryptionAlgorithm
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|getKeyEncryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|KeyEncryptionAlgorithm
name|getKeyEncryptionAlgorithm
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|String
name|defaultAlgorithm
parameter_list|)
block|{
name|String
name|keyEncryptionAlgo
init|=
name|jwk
operator|.
name|getAlgorithm
argument_list|()
operator|==
literal|null
condition|?
name|defaultAlgorithm
else|:
name|jwk
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|KeyEncryptionAlgorithm
name|keyEncryptionProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|keyEncryptionProvider
operator|=
operator|new
name|RSAOaepKeyEncryptionAlgorithm
argument_list|(
name|JwkUtils
operator|.
name|toRSAPublicKey
argument_list|(
name|jwk
argument_list|)
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|SecretKey
name|key
init|=
name|JwkUtils
operator|.
name|toSecretKey
argument_list|(
name|jwk
argument_list|)
decl_stmt|;
if|if
condition|(
name|Algorithm
operator|.
name|isAesKeyWrap
argument_list|(
name|keyEncryptionAlgo
argument_list|)
condition|)
block|{
name|keyEncryptionProvider
operator|=
operator|new
name|AesWrapKeyEncryptionAlgorithm
argument_list|(
name|key
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Algorithm
operator|.
name|isAesGcmKeyWrap
argument_list|(
name|keyEncryptionAlgo
argument_list|)
condition|)
block|{
name|keyEncryptionProvider
operator|=
operator|new
name|AesGcmWrapKeyEncryptionAlgorithm
argument_list|(
name|key
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// TODO: support elliptic curve keys
block|}
return|return
name|keyEncryptionProvider
return|;
block|}
specifier|public
specifier|static
name|KeyDecryptionAlgorithm
name|getKeyDecryptionAlgorithm
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|getKeyDecryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|KeyDecryptionAlgorithm
name|getKeyDecryptionAlgorithm
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|String
name|defaultAlgorithm
parameter_list|)
block|{
name|String
name|keyEncryptionAlgo
init|=
name|jwk
operator|.
name|getAlgorithm
argument_list|()
operator|==
literal|null
condition|?
name|defaultAlgorithm
else|:
name|jwk
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|KeyDecryptionAlgorithm
name|keyDecryptionProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|RSAOaepKeyDecryptionAlgorithm
argument_list|(
name|JwkUtils
operator|.
name|toRSAPrivateKey
argument_list|(
name|jwk
argument_list|)
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|SecretKey
name|key
init|=
name|JwkUtils
operator|.
name|toSecretKey
argument_list|(
name|jwk
argument_list|)
decl_stmt|;
if|if
condition|(
name|Algorithm
operator|.
name|isAesKeyWrap
argument_list|(
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
condition|)
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|AesWrapKeyDecryptionAlgorithm
argument_list|(
name|key
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Algorithm
operator|.
name|isAesGcmKeyWrap
argument_list|(
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
condition|)
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|AesGcmWrapKeyDecryptionAlgorithm
argument_list|(
name|key
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// TODO: support elliptic curve keys
block|}
return|return
name|keyDecryptionProvider
return|;
block|}
specifier|public
specifier|static
name|ContentEncryptionAlgorithm
name|getContentEncryptionAlgorithm
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|getContentEncryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ContentEncryptionAlgorithm
name|getContentEncryptionAlgorithm
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|String
name|defaultAlgorithm
parameter_list|)
block|{
name|String
name|ctEncryptionAlgo
init|=
name|jwk
operator|.
name|getAlgorithm
argument_list|()
operator|==
literal|null
condition|?
name|defaultAlgorithm
else|:
name|jwk
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|ContentEncryptionAlgorithm
name|contentEncryptionProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
condition|)
block|{
name|SecretKey
name|key
init|=
name|JwkUtils
operator|.
name|toSecretKey
argument_list|(
name|jwk
argument_list|)
decl_stmt|;
if|if
condition|(
name|Algorithm
operator|.
name|isAesGcm
argument_list|(
name|ctEncryptionAlgo
argument_list|)
condition|)
block|{
name|contentEncryptionProvider
operator|=
operator|new
name|AesGcmContentEncryptionAlgorithm
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
name|ctEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|contentEncryptionProvider
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|getContentDecryptionSecretKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|)
block|{
return|return
name|getContentDecryptionSecretKey
argument_list|(
name|jwk
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|getContentDecryptionSecretKey
parameter_list|(
name|JsonWebKey
name|jwk
parameter_list|,
name|String
name|defaultAlgorithm
parameter_list|)
block|{
name|String
name|ctEncryptionAlgo
init|=
name|jwk
operator|.
name|getAlgorithm
argument_list|()
operator|==
literal|null
condition|?
name|defaultAlgorithm
else|:
name|jwk
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
operator|.
name|equals
argument_list|(
name|jwk
operator|.
name|getKeyType
argument_list|()
argument_list|)
operator|&&
name|Algorithm
operator|.
name|isAesGcm
argument_list|(
name|ctEncryptionAlgo
argument_list|)
condition|)
block|{
return|return
name|JwkUtils
operator|.
name|toSecretKey
argument_list|(
name|jwk
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|JweEncryptionProvider
name|loadEncryptionProvider
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|KeyEncryptionAlgorithm
name|keyEncryptionProvider
init|=
literal|null
decl_stmt|;
name|String
name|keyEncryptionAlgo
init|=
literal|null
decl_stmt|;
name|Properties
name|props
init|=
literal|null
decl_stmt|;
try|try
block|{
name|props
operator|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|propLoc
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
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
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|String
name|contentEncryptionAlgo
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
argument_list|)
decl_stmt|;
name|ContentEncryptionAlgorithm
name|ctEncryptionProvider
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|JsonWebKey
name|jwk
init|=
name|JwkUtils
operator|.
name|loadJsonWebKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_ENCRYPT
argument_list|)
decl_stmt|;
name|keyEncryptionAlgo
operator|=
name|getKeyEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"direct"
operator|.
name|equals
argument_list|(
name|keyEncryptionAlgo
argument_list|)
condition|)
block|{
name|contentEncryptionAlgo
operator|=
name|getContentEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|ctEncryptionProvider
operator|=
name|JweUtils
operator|.
name|getContentEncryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyEncryptionProvider
operator|=
name|JweUtils
operator|.
name|getKeyEncryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|keyEncryptionProvider
operator|=
operator|new
name|RSAOaepKeyEncryptionAlgorithm
argument_list|(
operator|(
name|RSAPublicKey
operator|)
name|KeyManagementUtils
operator|.
name|loadPublicKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
argument_list|,
name|getKeyEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|keyEncryptionAlgo
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyEncryptionProvider
operator|==
literal|null
operator|&&
name|ctEncryptionProvider
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
name|JweHeaders
name|headers
init|=
operator|new
name|JweHeaders
argument_list|(
name|getKeyEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|keyEncryptionAlgo
argument_list|)
argument_list|,
name|contentEncryptionAlgo
argument_list|)
decl_stmt|;
name|String
name|compression
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_ZIP_ALGO_PROP
argument_list|)
decl_stmt|;
if|if
condition|(
name|compression
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|setZipAlgorithm
argument_list|(
name|compression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyEncryptionProvider
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Algorithm
operator|.
name|isAesCbcHmac
argument_list|(
name|contentEncryptionAlgo
argument_list|)
condition|)
block|{
return|return
operator|new
name|AesCbcHmacJweEncryption
argument_list|(
name|contentEncryptionAlgo
argument_list|,
name|keyEncryptionProvider
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|WrappedKeyJweEncryption
argument_list|(
name|headers
argument_list|,
name|keyEncryptionProvider
argument_list|,
operator|new
name|AesGcmContentEncryptionAlgorithm
argument_list|(
name|contentEncryptionAlgo
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|DirectKeyJweEncryption
argument_list|(
name|ctEncryptionProvider
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|JweDecryptionProvider
name|loadDecryptionProvider
parameter_list|(
name|String
name|propLoc
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|KeyDecryptionAlgorithm
name|keyDecryptionProvider
init|=
literal|null
decl_stmt|;
name|Properties
name|props
init|=
literal|null
decl_stmt|;
try|try
block|{
name|props
operator|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|propLoc
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
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
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|String
name|contentEncryptionAlgo
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
argument_list|)
decl_stmt|;
name|SecretKey
name|ctDecryptionKey
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JwkUtils
operator|.
name|JWK_KEY_STORE_TYPE
operator|.
name|equals
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|JsonWebKey
name|jwk
init|=
name|JwkUtils
operator|.
name|loadJsonWebKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|JsonWebKey
operator|.
name|KEY_OPER_ENCRYPT
argument_list|)
decl_stmt|;
name|String
name|keyEncryptionAlgo
init|=
name|getKeyEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"direct"
operator|.
name|equals
argument_list|(
name|keyEncryptionAlgo
argument_list|)
condition|)
block|{
name|contentEncryptionAlgo
operator|=
name|getContentEncryptionAlgo
argument_list|(
name|props
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
name|ctDecryptionKey
operator|=
name|JweUtils
operator|.
name|getContentDecryptionSecretKey
argument_list|(
name|jwk
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyDecryptionProvider
operator|=
name|JweUtils
operator|.
name|getKeyDecryptionAlgorithm
argument_list|(
name|jwk
argument_list|,
name|keyEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|keyDecryptionProvider
operator|=
operator|new
name|RSAOaepKeyDecryptionAlgorithm
argument_list|(
operator|(
name|RSAPrivateKey
operator|)
name|KeyManagementUtils
operator|.
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|KeyManagementUtils
operator|.
name|RSSEC_DECRYPT_KEY_PSWD_PROVIDER
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyDecryptionProvider
operator|==
literal|null
operator|&&
name|ctDecryptionKey
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
if|if
condition|(
name|keyDecryptionProvider
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|Algorithm
operator|.
name|isAesCbcHmac
argument_list|(
name|contentEncryptionAlgo
argument_list|)
condition|)
block|{
return|return
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
name|keyDecryptionProvider
argument_list|,
name|contentEncryptionAlgo
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|WrappedKeyJweDecryption
argument_list|(
name|keyDecryptionProvider
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|(
name|contentEncryptionAlgo
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|DirectKeyJweDecryption
argument_list|(
name|ctDecryptionKey
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|(
name|contentEncryptionAlgo
argument_list|)
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getKeyEncryptionAlgo
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
name|algo
operator|==
literal|null
condition|?
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_KEY_ALGO_PROP
argument_list|)
else|:
name|algo
return|;
block|}
specifier|private
specifier|static
name|String
name|getContentEncryptionAlgo
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
name|algo
operator|==
literal|null
condition|?
name|props
operator|.
name|getProperty
argument_list|(
name|JSON_WEB_ENCRYPTION_CEK_ALGO_PROP
argument_list|)
else|:
name|algo
return|;
block|}
block|}
end_class

end_unit

