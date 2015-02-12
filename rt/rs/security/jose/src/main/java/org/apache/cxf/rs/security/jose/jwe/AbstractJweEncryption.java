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
name|spec
operator|.
name|AlgorithmParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
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
name|crypto
operator|.
name|CryptoUtils
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
name|crypto
operator|.
name|KeyProperties
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
name|JoseHeadersReaderWriter
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJweEncryption
implements|implements
name|JweEncryptionProvider
block|{
specifier|protected
specifier|static
specifier|final
name|int
name|DEFAULT_AUTH_TAG_LENGTH
init|=
literal|128
decl_stmt|;
specifier|private
name|ContentEncryptionAlgorithm
name|contentEncryptionAlgo
decl_stmt|;
specifier|private
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgo
decl_stmt|;
specifier|private
name|JoseHeadersReaderWriter
name|writer
init|=
operator|new
name|JoseHeadersReaderWriter
argument_list|()
decl_stmt|;
specifier|protected
name|AbstractJweEncryption
parameter_list|(
name|ContentEncryptionAlgorithm
name|contentEncryptionAlgo
parameter_list|,
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgo
parameter_list|)
block|{
name|this
operator|.
name|keyEncryptionAlgo
operator|=
name|keyEncryptionAlgo
expr_stmt|;
name|this
operator|.
name|contentEncryptionAlgo
operator|=
name|contentEncryptionAlgo
expr_stmt|;
block|}
specifier|protected
name|ContentEncryptionAlgorithm
name|getContentEncryptionAlgorithm
parameter_list|()
block|{
return|return
name|contentEncryptionAlgo
return|;
block|}
specifier|protected
name|AlgorithmParameterSpec
name|getAlgorithmParameterSpec
parameter_list|(
name|byte
index|[]
name|theIv
parameter_list|)
block|{
return|return
name|getContentEncryptionAlgorithm
argument_list|()
operator|.
name|getAlgorithmParameterSpec
argument_list|(
name|theIv
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
name|byte
index|[]
name|cek
init|=
name|getProvidedContentEncryptionKey
argument_list|(
name|headers
argument_list|)
decl_stmt|;
if|if
condition|(
name|cek
operator|==
literal|null
condition|)
block|{
name|String
name|algoJava
init|=
name|getContentEncryptionAlgoJava
argument_list|()
decl_stmt|;
name|String
name|algoJwt
init|=
name|getContentEncryptionAlgoJwt
argument_list|()
decl_stmt|;
name|cek
operator|=
name|CryptoUtils
operator|.
name|getSecretKey
argument_list|(
name|Algorithm
operator|.
name|stripAlgoProperties
argument_list|(
name|algoJava
argument_list|)
argument_list|,
name|getCekSize
argument_list|(
name|algoJwt
argument_list|)
argument_list|)
operator|.
name|getEncoded
argument_list|()
expr_stmt|;
block|}
return|return
name|cek
return|;
block|}
specifier|protected
name|int
name|getCekSize
parameter_list|(
name|String
name|algoJwt
parameter_list|)
block|{
return|return
name|Algorithm
operator|.
name|valueOf
argument_list|(
name|algoJwt
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'_'
argument_list|)
argument_list|)
operator|.
name|getKeySizeBits
argument_list|()
return|;
block|}
specifier|protected
name|byte
index|[]
name|getProvidedContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
return|return
name|getContentEncryptionAlgorithm
argument_list|()
operator|.
name|getContentEncryptionKey
argument_list|(
name|headers
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|theCek
parameter_list|)
block|{
return|return
name|getKeyEncryptionAlgo
argument_list|()
operator|.
name|getEncryptedContentEncryptionKey
argument_list|(
name|headers
argument_list|,
name|theCek
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getContentEncryptionAlgoJwt
parameter_list|()
block|{
return|return
name|getContentEncryptionAlgorithm
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getContentEncryptionAlgoJava
parameter_list|()
block|{
return|return
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|getContentEncryptionAlgoJwt
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getAAD
parameter_list|(
name|String
name|protectedHeaders
parameter_list|,
name|byte
index|[]
name|aad
parameter_list|)
block|{
return|return
name|getContentEncryptionAlgorithm
argument_list|()
operator|.
name|getAdditionalAuthenticationData
argument_list|(
name|protectedHeaders
argument_list|,
name|aad
argument_list|)
return|;
block|}
specifier|public
name|String
name|encrypt
parameter_list|(
name|byte
index|[]
name|content
parameter_list|,
name|JweHeaders
name|jweHeaders
parameter_list|)
block|{
name|JweEncryptionInternal
name|state
init|=
name|getInternalState
argument_list|(
name|jweHeaders
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|byte
index|[]
name|cipher
init|=
name|CryptoUtils
operator|.
name|encryptBytes
argument_list|(
name|content
argument_list|,
name|createCekSecretKey
argument_list|(
name|state
argument_list|)
argument_list|,
name|state
operator|.
name|keyProps
argument_list|)
decl_stmt|;
name|JweCompactProducer
name|producer
init|=
name|getJweCompactProducer
argument_list|(
name|state
argument_list|,
name|cipher
argument_list|)
decl_stmt|;
return|return
name|producer
operator|.
name|getJweContent
argument_list|()
return|;
block|}
specifier|protected
name|JweCompactProducer
name|getJweCompactProducer
parameter_list|(
name|JweEncryptionInternal
name|state
parameter_list|,
name|byte
index|[]
name|cipher
parameter_list|)
block|{
return|return
operator|new
name|JweCompactProducer
argument_list|(
name|state
operator|.
name|theHeaders
argument_list|,
name|state
operator|.
name|jweContentEncryptionKey
argument_list|,
name|state
operator|.
name|theIv
argument_list|,
name|cipher
argument_list|,
name|DEFAULT_AUTH_TAG_LENGTH
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getKeyAlgorithm
parameter_list|()
block|{
return|return
name|getKeyEncryptionAlgo
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getContentAlgorithm
parameter_list|()
block|{
return|return
name|getContentEncryptionAlgorithm
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
return|;
block|}
specifier|protected
name|JoseHeadersReaderWriter
name|getJwtHeadersWriter
parameter_list|()
block|{
return|return
name|writer
return|;
block|}
annotation|@
name|Override
specifier|public
name|JweEncryptionState
name|createJweEncryptionState
parameter_list|(
name|JweEncryptionInput
name|jweInput
parameter_list|)
block|{
name|JweEncryptionInternal
name|state
init|=
name|getInternalState
argument_list|(
name|jweInput
operator|.
name|getJweHeaders
argument_list|()
argument_list|,
name|jweInput
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|CryptoUtils
operator|.
name|initCipher
argument_list|(
name|createCekSecretKey
argument_list|(
name|state
argument_list|)
argument_list|,
name|state
operator|.
name|keyProps
argument_list|,
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|)
decl_stmt|;
return|return
operator|new
name|JweEncryptionState
argument_list|(
name|c
argument_list|,
name|state
operator|.
name|theHeaders
argument_list|,
name|state
operator|.
name|jweContentEncryptionKey
argument_list|,
name|state
operator|.
name|theIv
argument_list|,
name|getAuthenticationTagProducer
argument_list|(
name|state
argument_list|)
argument_list|,
name|state
operator|.
name|keyProps
operator|.
name|isCompressionSupported
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|AuthenticationTagProducer
name|getAuthenticationTagProducer
parameter_list|(
name|JweEncryptionInternal
name|state
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|protected
name|SecretKey
name|createCekSecretKey
parameter_list|(
name|JweEncryptionInternal
name|state
parameter_list|)
block|{
return|return
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|getActualCek
argument_list|(
name|state
operator|.
name|secretKey
argument_list|,
name|this
operator|.
name|getContentEncryptionAlgoJwt
argument_list|()
argument_list|)
argument_list|,
name|state
operator|.
name|keyProps
operator|.
name|getKeyAlgo
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getActualCek
parameter_list|(
name|byte
index|[]
name|theCek
parameter_list|,
name|String
name|algoJwt
parameter_list|)
block|{
return|return
name|theCek
return|;
block|}
specifier|private
name|JweEncryptionInternal
name|getInternalState
parameter_list|(
name|JweHeaders
name|jweInHeaders
parameter_list|,
name|JweEncryptionInput
name|jweInput
parameter_list|)
block|{
name|JweHeaders
name|theHeaders
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|getKeyAlgorithm
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|theHeaders
operator|.
name|setKeyEncryptionAlgorithm
argument_list|(
name|getKeyAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|theHeaders
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|getContentAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|JweHeaders
name|protectedHeaders
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|jweInHeaders
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|jweInHeaders
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|getKeyAlgorithm
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|getKeyAlgorithm
argument_list|()
operator|.
name|equals
argument_list|(
name|jweInHeaders
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|)
operator|)
operator|||
name|jweInHeaders
operator|.
name|getAlgorithm
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getContentAlgorithm
argument_list|()
operator|.
name|equals
argument_list|(
name|jweInHeaders
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
name|theHeaders
operator|.
name|asMap
argument_list|()
operator|.
name|putAll
argument_list|(
name|jweInHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
name|protectedHeaders
operator|=
name|jweInHeaders
operator|.
name|getProtectedHeaders
argument_list|()
operator|!=
literal|null
condition|?
name|jweInHeaders
operator|.
name|getProtectedHeaders
argument_list|()
else|:
name|theHeaders
expr_stmt|;
block|}
else|else
block|{
name|protectedHeaders
operator|=
name|theHeaders
expr_stmt|;
block|}
name|byte
index|[]
name|theCek
init|=
name|jweInput
operator|!=
literal|null
operator|&&
name|jweInput
operator|.
name|getCek
argument_list|()
operator|!=
literal|null
condition|?
name|jweInput
operator|.
name|getCek
argument_list|()
else|:
name|getContentEncryptionKey
argument_list|(
name|theHeaders
argument_list|)
decl_stmt|;
name|String
name|contentEncryptionAlgoJavaName
init|=
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|getContentEncryptionAlgoJwt
argument_list|()
argument_list|)
decl_stmt|;
name|KeyProperties
name|keyProps
init|=
operator|new
name|KeyProperties
argument_list|(
name|contentEncryptionAlgoJavaName
argument_list|)
decl_stmt|;
name|keyProps
operator|.
name|setCompressionSupported
argument_list|(
name|compressionRequired
argument_list|(
name|theHeaders
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|theIv
init|=
name|jweInput
operator|!=
literal|null
operator|&&
name|jweInput
operator|.
name|getIv
argument_list|()
operator|!=
literal|null
condition|?
name|jweInput
operator|.
name|getIv
argument_list|()
else|:
name|getContentEncryptionAlgorithm
argument_list|()
operator|.
name|getInitVector
argument_list|()
decl_stmt|;
name|AlgorithmParameterSpec
name|specParams
init|=
name|getAlgorithmParameterSpec
argument_list|(
name|theIv
argument_list|)
decl_stmt|;
name|keyProps
operator|.
name|setAlgoSpec
argument_list|(
name|specParams
argument_list|)
expr_stmt|;
name|byte
index|[]
name|jweContentEncryptionKey
init|=
name|getEncryptedContentEncryptionKey
argument_list|(
name|theHeaders
argument_list|,
name|theCek
argument_list|)
decl_stmt|;
name|String
name|protectedHeadersJson
init|=
name|writer
operator|.
name|headersToJson
argument_list|(
name|protectedHeaders
argument_list|)
decl_stmt|;
name|byte
index|[]
name|additionalEncryptionParam
init|=
name|getAAD
argument_list|(
name|protectedHeadersJson
argument_list|,
name|jweInput
operator|==
literal|null
condition|?
literal|null
else|:
name|jweInput
operator|.
name|getAad
argument_list|()
argument_list|)
decl_stmt|;
name|keyProps
operator|.
name|setAdditionalData
argument_list|(
name|additionalEncryptionParam
argument_list|)
expr_stmt|;
name|JweEncryptionInternal
name|state
init|=
operator|new
name|JweEncryptionInternal
argument_list|()
decl_stmt|;
name|state
operator|.
name|theHeaders
operator|=
name|theHeaders
expr_stmt|;
name|state
operator|.
name|jweContentEncryptionKey
operator|=
name|jweContentEncryptionKey
expr_stmt|;
name|state
operator|.
name|keyProps
operator|=
name|keyProps
expr_stmt|;
name|state
operator|.
name|secretKey
operator|=
name|theCek
expr_stmt|;
name|state
operator|.
name|theIv
operator|=
name|theIv
expr_stmt|;
name|state
operator|.
name|protectedHeadersJson
operator|=
name|protectedHeadersJson
expr_stmt|;
name|state
operator|.
name|aad
operator|=
name|jweInput
operator|!=
literal|null
condition|?
name|jweInput
operator|.
name|getAad
argument_list|()
else|:
literal|null
expr_stmt|;
return|return
name|state
return|;
block|}
specifier|private
name|boolean
name|compressionRequired
parameter_list|(
name|JweHeaders
name|theHeaders
parameter_list|)
block|{
return|return
name|JoseConstants
operator|.
name|DEFLATE_ZIP_ALGORITHM
operator|.
name|equals
argument_list|(
name|theHeaders
operator|.
name|getZipAlgorithm
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|KeyEncryptionAlgorithm
name|getKeyEncryptionAlgo
parameter_list|()
block|{
return|return
name|keyEncryptionAlgo
return|;
block|}
specifier|protected
specifier|static
class|class
name|JweEncryptionInternal
block|{
name|JweHeaders
name|theHeaders
decl_stmt|;
name|byte
index|[]
name|jweContentEncryptionKey
decl_stmt|;
name|byte
index|[]
name|theIv
decl_stmt|;
name|KeyProperties
name|keyProps
decl_stmt|;
name|byte
index|[]
name|secretKey
decl_stmt|;
name|String
name|protectedHeadersJson
decl_stmt|;
name|byte
index|[]
name|aad
decl_stmt|;
block|}
block|}
end_class

end_unit

