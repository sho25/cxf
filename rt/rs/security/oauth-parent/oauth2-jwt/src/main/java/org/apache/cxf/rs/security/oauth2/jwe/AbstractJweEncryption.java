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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|jwt
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtConstants
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
name|jwt
operator|.
name|JwtHeadersWriter
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
name|jwt
operator|.
name|JwtTokenReaderWriter
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|utils
operator|.
name|crypto
operator|.
name|KeyProperties
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
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_AUTH_TAG_LENGTH
init|=
literal|128
decl_stmt|;
specifier|private
name|JweHeaders
name|headers
decl_stmt|;
specifier|private
name|JwtHeadersWriter
name|writer
decl_stmt|;
specifier|private
name|ContentEncryptionAlgorithm
name|contentEncryptionAlgo
decl_stmt|;
specifier|private
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgo
decl_stmt|;
specifier|protected
name|AbstractJweEncryption
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|ContentEncryptionAlgorithm
name|contentEncryptionAlgo
parameter_list|,
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
name|contentEncryptionAlgo
argument_list|,
name|keyEncryptionAlgo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJweEncryption
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|ContentEncryptionAlgorithm
name|contentEncryptionAlgo
parameter_list|,
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgo
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|writer
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|writer
operator|=
operator|new
name|JwtTokenReaderWriter
argument_list|()
expr_stmt|;
block|}
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
name|AlgorithmParameterSpec
name|getAlgorithmParameterSpec
parameter_list|(
name|byte
index|[]
name|theIv
parameter_list|)
block|{
return|return
name|contentEncryptionAlgo
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
parameter_list|()
block|{
name|byte
index|[]
name|cek
init|=
name|contentEncryptionAlgo
operator|.
name|getContentEncryptionKey
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
name|Algorithm
operator|.
name|valueOf
argument_list|(
name|algoJwt
argument_list|)
operator|.
name|getKeySizeBits
argument_list|()
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
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|(
name|byte
index|[]
name|theCek
parameter_list|)
block|{
return|return
name|keyEncryptionAlgo
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
name|headers
operator|.
name|getContentEncryptionAlgorithm
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
name|int
name|getAuthTagLen
parameter_list|()
block|{
return|return
name|DEFAULT_AUTH_TAG_LENGTH
return|;
block|}
specifier|protected
name|JweHeaders
name|getJweHeaders
parameter_list|()
block|{
return|return
name|headers
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
name|String
name|contentType
parameter_list|)
block|{
name|JweEncryptionInternal
name|state
init|=
name|getInternalState
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
name|byte
index|[]
name|cipherText
init|=
name|CryptoUtils
operator|.
name|encryptBytes
argument_list|(
name|content
argument_list|,
name|state
operator|.
name|secretKey
argument_list|,
name|state
operator|.
name|keyProps
argument_list|)
decl_stmt|;
name|JweCompactProducer
name|producer
init|=
operator|new
name|JweCompactProducer
argument_list|(
name|state
operator|.
name|theHeaders
argument_list|,
name|writer
argument_list|,
name|state
operator|.
name|jweContentEncryptionKey
argument_list|,
name|state
operator|.
name|theIv
argument_list|,
name|cipherText
argument_list|,
name|getAuthTagLen
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|producer
operator|.
name|getJweContent
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|JweEncryptionState
name|createJweEncryptionState
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|JweEncryptionInternal
name|state
init|=
name|getInternalState
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|CryptoUtils
operator|.
name|initCipher
argument_list|(
name|state
operator|.
name|secretKey
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
name|getAuthTagLen
argument_list|()
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
name|state
operator|.
name|keyProps
operator|.
name|isCompressionSupported
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|JweEncryptionInternal
name|getInternalState
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|JweHeaders
name|theHeaders
init|=
name|headers
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|theHeaders
operator|=
operator|new
name|JweHeaders
argument_list|(
name|theHeaders
operator|.
name|asMap
argument_list|()
argument_list|)
expr_stmt|;
name|theHeaders
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|theCek
init|=
name|getContentEncryptionKey
argument_list|()
decl_stmt|;
name|String
name|contentEncryptionAlgoJavaName
init|=
name|Algorithm
operator|.
name|toJavaName
argument_list|(
name|theHeaders
operator|.
name|getContentEncryptionAlgorithm
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
name|additionalEncryptionParam
init|=
name|theHeaders
operator|.
name|toCipherAdditionalAuthData
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|keyProps
operator|.
name|setAdditionalData
argument_list|(
name|additionalEncryptionParam
argument_list|)
expr_stmt|;
name|byte
index|[]
name|theIv
init|=
name|contentEncryptionAlgo
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
name|theCek
argument_list|)
decl_stmt|;
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
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|theCek
argument_list|,
name|contentEncryptionAlgoJavaName
argument_list|)
expr_stmt|;
name|state
operator|.
name|theIv
operator|=
name|theIv
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
name|JwtConstants
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
specifier|private
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
name|SecretKey
name|secretKey
decl_stmt|;
block|}
block|}
end_class

end_unit

