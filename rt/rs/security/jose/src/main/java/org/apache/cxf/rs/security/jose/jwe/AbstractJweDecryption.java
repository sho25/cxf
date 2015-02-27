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
name|Key
import|;
end_import

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
name|jwa
operator|.
name|AlgorithmUtils
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
name|ContentAlgorithm
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
name|KeyAlgorithm
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJweDecryption
implements|implements
name|JweDecryptionProvider
block|{
specifier|private
name|KeyDecryptionAlgorithm
name|keyDecryptionAlgo
decl_stmt|;
specifier|private
name|ContentDecryptionAlgorithm
name|contentDecryptionAlgo
decl_stmt|;
specifier|protected
name|AbstractJweDecryption
parameter_list|(
name|KeyDecryptionAlgorithm
name|keyDecryptionAlgo
parameter_list|,
name|ContentDecryptionAlgorithm
name|contentDecryptionAlgo
parameter_list|)
block|{
name|this
operator|.
name|keyDecryptionAlgo
operator|=
name|keyDecryptionAlgo
expr_stmt|;
name|this
operator|.
name|contentDecryptionAlgo
operator|=
name|contentDecryptionAlgo
expr_stmt|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|keyDecryptionAlgo
operator|.
name|getDecryptedContentEncryptionKey
argument_list|(
name|jweDecryptionInput
argument_list|)
return|;
block|}
specifier|public
name|JweDecryptionOutput
name|decrypt
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|JweCompactConsumer
name|consumer
init|=
operator|new
name|JweCompactConsumer
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|byte
index|[]
name|cek
init|=
name|getContentEncryptionKey
argument_list|(
name|consumer
operator|.
name|getJweDecryptionInput
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|doDecrypt
argument_list|(
name|consumer
operator|.
name|getJweDecryptionInput
argument_list|()
argument_list|,
name|cek
argument_list|)
return|;
block|}
specifier|public
name|byte
index|[]
name|decrypt
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
name|byte
index|[]
name|cek
init|=
name|getContentEncryptionKey
argument_list|(
name|jweDecryptionInput
argument_list|)
decl_stmt|;
return|return
name|doDecrypt
argument_list|(
name|jweDecryptionInput
argument_list|,
name|cek
argument_list|)
operator|.
name|getContent
argument_list|()
return|;
block|}
specifier|protected
name|JweDecryptionOutput
name|doDecrypt
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|)
block|{
name|KeyProperties
name|keyProperties
init|=
operator|new
name|KeyProperties
argument_list|(
name|getContentEncryptionAlgorithm
argument_list|(
name|jweDecryptionInput
argument_list|)
argument_list|)
decl_stmt|;
name|keyProperties
operator|.
name|setAdditionalData
argument_list|(
name|getContentEncryptionCipherAAD
argument_list|(
name|jweDecryptionInput
argument_list|)
argument_list|)
expr_stmt|;
name|AlgorithmParameterSpec
name|spec
init|=
name|getContentEncryptionCipherSpec
argument_list|(
name|jweDecryptionInput
argument_list|)
decl_stmt|;
name|keyProperties
operator|.
name|setAlgoSpec
argument_list|(
name|spec
argument_list|)
expr_stmt|;
name|boolean
name|compressionSupported
init|=
name|JoseConstants
operator|.
name|DEFLATE_ZIP_ALGORITHM
operator|.
name|equals
argument_list|(
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getZipAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|keyProperties
operator|.
name|setCompressionSupported
argument_list|(
name|compressionSupported
argument_list|)
expr_stmt|;
name|byte
index|[]
name|actualCek
init|=
name|getActualCek
argument_list|(
name|cek
argument_list|,
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|Key
name|secretKey
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|actualCek
argument_list|,
name|keyProperties
operator|.
name|getKeyAlgo
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|CryptoUtils
operator|.
name|decryptBytes
argument_list|(
name|getEncryptedContentWithAuthTag
argument_list|(
name|jweDecryptionInput
argument_list|)
argument_list|,
name|secretKey
argument_list|,
name|keyProperties
argument_list|)
decl_stmt|;
return|return
operator|new
name|JweDecryptionOutput
argument_list|(
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
argument_list|,
name|bytes
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|(
name|JweCompactConsumer
name|consumer
parameter_list|)
block|{
return|return
name|consumer
operator|.
name|getEncryptedContentEncryptionKey
argument_list|()
return|;
block|}
specifier|protected
name|AlgorithmParameterSpec
name|getContentEncryptionCipherSpec
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|contentDecryptionAlgo
operator|.
name|getAlgorithmParameterSpec
argument_list|(
name|getContentEncryptionCipherInitVector
argument_list|(
name|jweDecryptionInput
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getContentEncryptionAlgorithm
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|AlgorithmUtils
operator|.
name|toJavaName
argument_list|(
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionCipherAAD
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|contentDecryptionAlgo
operator|.
name|getAdditionalAuthenticationData
argument_list|(
name|jweDecryptionInput
operator|.
name|getDecodedJsonHeaders
argument_list|()
argument_list|,
name|jweDecryptionInput
operator|.
name|getAad
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getEncryptedContentWithAuthTag
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|contentDecryptionAlgo
operator|.
name|getEncryptedSequence
argument_list|(
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
argument_list|,
name|jweDecryptionInput
operator|.
name|getEncryptedContent
argument_list|()
argument_list|,
name|getEncryptionAuthenticationTag
argument_list|(
name|jweDecryptionInput
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionCipherInitVector
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|jweDecryptionInput
operator|.
name|getInitVector
argument_list|()
return|;
block|}
specifier|protected
name|byte
index|[]
name|getEncryptionAuthenticationTag
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|jweDecryptionInput
operator|.
name|getAuthTag
argument_list|()
return|;
block|}
specifier|protected
name|int
name|getEncryptionAuthenticationTagLenBits
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
return|return
name|getEncryptionAuthenticationTag
argument_list|(
name|jweDecryptionInput
argument_list|)
operator|.
name|length
operator|*
literal|8
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
annotation|@
name|Override
specifier|public
name|KeyAlgorithm
name|getKeyAlgorithm
parameter_list|()
block|{
return|return
name|keyDecryptionAlgo
operator|.
name|getAlgorithm
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ContentAlgorithm
name|getContentAlgorithm
parameter_list|()
block|{
return|return
name|contentDecryptionAlgo
operator|.
name|getAlgorithm
argument_list|()
return|;
block|}
block|}
end_class

end_unit

