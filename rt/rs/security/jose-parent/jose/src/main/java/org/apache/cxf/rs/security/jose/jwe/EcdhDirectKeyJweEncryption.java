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
name|KeyPair
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
name|ECPrivateKey
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
name|ECPublicKey
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
name|Base64UrlUtility
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
class|class
name|EcdhDirectKeyJweEncryption
extends|extends
name|JweEncryption
block|{
specifier|public
name|EcdhDirectKeyJweEncryption
parameter_list|(
name|ECPublicKey
name|peerPublicKey
parameter_list|,
name|String
name|curve
parameter_list|,
name|ContentAlgorithm
name|ctAlgo
parameter_list|)
block|{
name|this
argument_list|(
name|peerPublicKey
argument_list|,
name|curve
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|ctAlgo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EcdhDirectKeyJweEncryption
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
name|ContentAlgorithm
name|ctAlgo
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|EcdhDirectKeyEncryptionAlgorithm
argument_list|()
argument_list|,
operator|new
name|EcdhAesGcmContentEncryptionAlgorithm
argument_list|(
name|peerPublicKey
argument_list|,
name|curve
argument_list|,
name|apuString
argument_list|,
name|apvString
argument_list|,
name|ctAlgo
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
class|class
name|EcdhDirectKeyEncryptionAlgorithm
extends|extends
name|DirectKeyEncryptionAlgorithm
block|{
specifier|protected
name|void
name|checkKeyEncryptionAlgorithm
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
name|headers
operator|.
name|setKeyEncryptionAlgorithm
argument_list|(
name|KeyAlgorithm
operator|.
name|ECDH_ES_DIRECT
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|EcdhAesGcmContentEncryptionAlgorithm
extends|extends
name|AesGcmContentEncryptionAlgorithm
block|{
specifier|private
name|EcdhHelper
name|helper
decl_stmt|;
specifier|public
name|EcdhAesGcmContentEncryptionAlgorithm
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
name|ContentAlgorithm
name|ctAlgo
parameter_list|)
block|{
name|super
argument_list|(
name|ctAlgo
argument_list|)
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
name|ctAlgo
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
return|return
name|helper
operator|.
name|getDerivedKey
argument_list|(
name|headers
argument_list|)
return|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|EcdhHelper
block|{
specifier|private
name|ECPublicKey
name|peerPublicKey
decl_stmt|;
specifier|private
name|String
name|ecurve
decl_stmt|;
specifier|private
name|byte
index|[]
name|apuBytes
decl_stmt|;
specifier|private
name|byte
index|[]
name|apvBytes
decl_stmt|;
specifier|private
name|String
name|ctAlgo
decl_stmt|;
specifier|public
name|EcdhHelper
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
name|String
name|ctAlgo
parameter_list|)
block|{
name|this
operator|.
name|ctAlgo
operator|=
name|ctAlgo
expr_stmt|;
name|this
operator|.
name|peerPublicKey
operator|=
name|peerPublicKey
expr_stmt|;
name|this
operator|.
name|ecurve
operator|=
name|curve
expr_stmt|;
comment|// JWA spec suggests the "apu" field MAY either be omitted or
comment|// represent a random 512-bit value (...) and the "apv" field SHOULD NOT be present."
name|this
operator|.
name|apuBytes
operator|=
name|toApuBytes
argument_list|(
name|apuString
argument_list|)
expr_stmt|;
name|this
operator|.
name|apvBytes
operator|=
name|toBytes
argument_list|(
name|apvString
argument_list|)
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getDerivedKey
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
name|KeyPair
name|pair
init|=
name|CryptoUtils
operator|.
name|generateECKeyPair
argument_list|(
name|ecurve
argument_list|)
decl_stmt|;
name|ECPublicKey
name|publicKey
init|=
operator|(
name|ECPublicKey
operator|)
name|pair
operator|.
name|getPublic
argument_list|()
decl_stmt|;
name|ECPrivateKey
name|privateKey
init|=
operator|(
name|ECPrivateKey
operator|)
name|pair
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|ContentAlgorithm
name|jwtAlgo
init|=
name|ContentAlgorithm
operator|.
name|valueOf
argument_list|(
name|ctAlgo
argument_list|)
decl_stmt|;
name|headers
operator|.
name|setHeader
argument_list|(
literal|"apu"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|apuBytes
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setHeader
argument_list|(
literal|"apv"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|apvBytes
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setJsonWebKey
argument_list|(
literal|"epk"
argument_list|,
name|JwkUtils
operator|.
name|fromECPublicKey
argument_list|(
name|publicKey
argument_list|,
name|ecurve
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|JweUtils
operator|.
name|getECDHKey
argument_list|(
name|privateKey
argument_list|,
name|peerPublicKey
argument_list|,
name|apuBytes
argument_list|,
name|apvBytes
argument_list|,
name|jwtAlgo
operator|.
name|getJwaName
argument_list|()
argument_list|,
name|jwtAlgo
operator|.
name|getKeySizeBits
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|toApuBytes
parameter_list|(
name|String
name|apuString
parameter_list|)
block|{
if|if
condition|(
name|apuString
operator|!=
literal|null
condition|)
block|{
return|return
name|toBytes
argument_list|(
name|apuString
argument_list|)
return|;
block|}
return|return
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|512
operator|/
literal|8
argument_list|)
return|;
block|}
specifier|private
name|byte
index|[]
name|toBytes
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|==
literal|null
condition|?
literal|null
else|:
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

