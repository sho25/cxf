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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|IvParameterSpec
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
name|JoseHeadersReader
import|;
end_import

begin_class
specifier|public
class|class
name|AesCbcHmacJweDecryption
extends|extends
name|AbstractJweDecryption
block|{
specifier|public
name|AesCbcHmacJweDecryption
parameter_list|(
name|KeyDecryptionAlgorithm
name|keyDecryptionAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|keyDecryptionAlgo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AesCbcHmacJweDecryption
parameter_list|(
name|KeyDecryptionAlgorithm
name|keyDecryptionAlgo
parameter_list|,
name|JoseHeadersReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|,
name|keyDecryptionAlgo
argument_list|,
operator|new
name|AesCbcContentDecryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JweDecryptionOutput
name|doDecrypt
parameter_list|(
name|JweCompactConsumer
name|consumer
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|)
block|{
name|validateAuthenticationTag
argument_list|(
name|consumer
argument_list|,
name|cek
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|doDecrypt
argument_list|(
name|consumer
argument_list|,
name|cek
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|AesCbcHmacJweEncryption
operator|.
name|doGetActualCek
argument_list|(
name|theCek
argument_list|,
name|algoJwt
argument_list|)
return|;
block|}
specifier|protected
name|void
name|validateAuthenticationTag
parameter_list|(
name|JweCompactConsumer
name|consumer
parameter_list|,
name|byte
index|[]
name|theCek
parameter_list|)
block|{
name|byte
index|[]
name|actualAuthTag
init|=
name|consumer
operator|.
name|getEncryptionAuthenticationTag
argument_list|()
decl_stmt|;
specifier|final
name|AesCbcHmacJweEncryption
operator|.
name|MacState
name|macState
init|=
name|AesCbcHmacJweEncryption
operator|.
name|getInitializedMacState
argument_list|(
name|theCek
argument_list|,
name|consumer
operator|.
name|getContentDecryptionCipherInitVector
argument_list|()
argument_list|,
name|consumer
operator|.
name|getJweHeaders
argument_list|()
argument_list|,
name|consumer
operator|.
name|getDecodedJsonHeaders
argument_list|()
argument_list|)
decl_stmt|;
name|macState
operator|.
name|mac
operator|.
name|update
argument_list|(
name|consumer
operator|.
name|getEncryptedContent
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|expectedAuthTag
init|=
name|AesCbcHmacJweEncryption
operator|.
name|signAndGetTag
argument_list|(
name|macState
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|actualAuthTag
argument_list|,
name|expectedAuthTag
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
block|}
specifier|private
specifier|static
class|class
name|AesCbcContentDecryptionAlgorithm
extends|extends
name|AbstractContentEncryptionCipherProperties
implements|implements
name|ContentDecryptionAlgorithm
block|{
annotation|@
name|Override
specifier|public
name|AlgorithmParameterSpec
name|getAlgorithmParameterSpec
parameter_list|(
name|byte
index|[]
name|theIv
parameter_list|)
block|{
return|return
operator|new
name|IvParameterSpec
argument_list|(
name|theIv
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getAdditionalAuthenticationData
parameter_list|(
name|String
name|headersJson
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getEncryptedSequence
parameter_list|(
name|byte
index|[]
name|cipher
parameter_list|,
name|byte
index|[]
name|authTag
parameter_list|)
block|{
return|return
name|cipher
return|;
block|}
block|}
block|}
end_class

end_unit

