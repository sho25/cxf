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
name|ECPrivateKey
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
name|KeyAlgorithm
import|;
end_import

begin_class
specifier|public
class|class
name|EcdhAesWrapKeyDecryptionAlgorithm
implements|implements
name|KeyDecryptionProvider
block|{
specifier|private
name|ECPrivateKey
name|key
decl_stmt|;
specifier|private
name|KeyAlgorithm
name|algo
decl_stmt|;
specifier|public
name|EcdhAesWrapKeyDecryptionAlgorithm
parameter_list|(
name|ECPrivateKey
name|key
parameter_list|)
block|{
name|this
argument_list|(
name|key
argument_list|,
name|KeyAlgorithm
operator|.
name|ECDH_ES_A128KW
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EcdhAesWrapKeyDecryptionAlgorithm
parameter_list|(
name|ECPrivateKey
name|key
parameter_list|,
name|KeyAlgorithm
name|algo
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|algo
operator|=
name|algo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getDecryptedContentEncryptionKey
parameter_list|(
name|JweDecryptionInput
name|jweDecryptionInput
parameter_list|)
block|{
name|byte
index|[]
name|derivedKey
init|=
name|EcdhDirectKeyJweDecryption
operator|.
name|getDecryptedContentEncryptionKeyFromHeaders
argument_list|(
name|jweDecryptionInput
operator|.
name|getJweHeaders
argument_list|()
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|KeyDecryptionProvider
name|aesWrap
init|=
operator|new
name|AesWrapKeyDecryptionAlgorithm
argument_list|(
name|derivedKey
argument_list|)
block|{
specifier|protected
name|boolean
name|isValidAlgorithmFamily
parameter_list|(
name|String
name|wrapAlgo
parameter_list|)
block|{
return|return
name|AlgorithmUtils
operator|.
name|isEcdhEsWrap
argument_list|(
name|wrapAlgo
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|aesWrap
operator|.
name|getDecryptedContentEncryptionKey
argument_list|(
name|jweDecryptionInput
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|KeyAlgorithm
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algo
return|;
block|}
block|}
end_class

end_unit

