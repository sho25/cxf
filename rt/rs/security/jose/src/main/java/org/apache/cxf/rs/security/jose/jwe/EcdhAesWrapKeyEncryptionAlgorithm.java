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
name|ECPublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|EcdhDirectKeyJweEncryption
operator|.
name|EcdhHelper
import|;
end_import

begin_class
specifier|public
class|class
name|EcdhAesWrapKeyEncryptionAlgorithm
implements|implements
name|KeyEncryptionProvider
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ECDH_AES_MAP
decl_stmt|;
static|static
block|{
name|ECDH_AES_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|ECDH_AES_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|ECDH_ES_A128KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
name|KeyAlgorithm
operator|.
name|A128KW
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
name|ECDH_AES_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|ECDH_ES_A192KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
name|KeyAlgorithm
operator|.
name|A192KW
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
name|ECDH_AES_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|ECDH_ES_A256KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
name|KeyAlgorithm
operator|.
name|A256KW
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|KeyAlgorithm
name|keyAlgo
decl_stmt|;
specifier|private
name|EcdhHelper
name|helper
decl_stmt|;
specifier|public
name|EcdhAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|ECPublicKey
name|peerPublicKey
parameter_list|,
name|String
name|curve
parameter_list|,
name|String
name|apuString
parameter_list|,
name|String
name|apvString
parameter_list|,
name|KeyAlgorithm
name|keyAlgo
parameter_list|)
block|{
name|this
operator|.
name|keyAlgo
operator|=
name|keyAlgo
expr_stmt|;
name|helper
operator|=
operator|new
name|EcdhHelper
argument_list|(
name|peerPublicKey
argument_list|,
name|curve
argument_list|,
name|apuString
argument_list|,
name|apvString
argument_list|,
name|keyAlgo
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|)
block|{
specifier|final
name|byte
index|[]
name|derivedKey
init|=
name|helper
operator|.
name|getDerivedKey
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|KeyEncryptionProvider
name|aesWrap
init|=
operator|new
name|AesWrapKeyEncryptionAlgorithm
argument_list|(
name|derivedKey
argument_list|,
name|keyAlgo
argument_list|)
block|{
specifier|protected
name|void
name|checkAlgorithms
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
comment|// complete
block|}
specifier|protected
name|String
name|getKeyEncryptionAlgoJava
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
return|return
name|AlgorithmUtils
operator|.
name|AES_WRAP_ALGO_JAVA
return|;
block|}
block|}
decl_stmt|;
return|return
name|aesWrap
operator|.
name|getEncryptedContentEncryptionKey
argument_list|(
name|headers
argument_list|,
name|cek
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
name|keyAlgo
return|;
block|}
block|}
end_class

end_unit

