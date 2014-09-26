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
block|}
end_class

end_unit

