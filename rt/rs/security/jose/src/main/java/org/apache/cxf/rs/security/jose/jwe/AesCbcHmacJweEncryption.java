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
name|nio
operator|.
name|ByteBuffer
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
name|javax
operator|.
name|crypto
operator|.
name|Mac
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
name|common
operator|.
name|util
operator|.
name|crypto
operator|.
name|HmacUtils
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
class|class
name|AesCbcHmacJweEncryption
extends|extends
name|AbstractJweEncryption
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
name|AES_HMAC_MAP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|AES_CEK_SIZE_MAP
decl_stmt|;
static|static
block|{
name|AES_HMAC_MAP
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
name|AES_HMAC_MAP
operator|.
name|put
argument_list|(
name|Algorithm
operator|.
name|A128CBC_HS256
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|HMAC_SHA_256_JAVA
argument_list|)
expr_stmt|;
name|AES_HMAC_MAP
operator|.
name|put
argument_list|(
name|Algorithm
operator|.
name|A192CBC_HS384
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|HMAC_SHA_384_JAVA
argument_list|)
expr_stmt|;
name|AES_HMAC_MAP
operator|.
name|put
argument_list|(
name|Algorithm
operator|.
name|A256CBC_HS512
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|HMAC_SHA_512_JAVA
argument_list|)
expr_stmt|;
name|AES_CEK_SIZE_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|AES_CEK_SIZE_MAP
operator|.
name|put
argument_list|(
name|Algorithm
operator|.
name|A128CBC_HS256
operator|.
name|getJwtName
argument_list|()
argument_list|,
literal|32
argument_list|)
expr_stmt|;
name|AES_CEK_SIZE_MAP
operator|.
name|put
argument_list|(
name|Algorithm
operator|.
name|A192CBC_HS384
operator|.
name|getJwtName
argument_list|()
argument_list|,
literal|48
argument_list|)
expr_stmt|;
name|AES_CEK_SIZE_MAP
operator|.
name|put
argument_list|(
name|Algorithm
operator|.
name|A256CBC_HS512
operator|.
name|getJwtName
argument_list|()
argument_list|,
literal|64
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AesCbcHmacJweEncryption
parameter_list|(
name|String
name|cekAlgoJwt
parameter_list|,
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgorithm
parameter_list|)
block|{
name|this
argument_list|(
name|cekAlgoJwt
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|keyEncryptionAlgorithm
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AesCbcHmacJweEncryption
parameter_list|(
name|String
name|cekAlgoJwt
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgorithm
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|AesCbcContentEncryptionAlgorithm
argument_list|(
name|cek
argument_list|,
name|iv
argument_list|,
name|validateCekAlgorithm
argument_list|(
name|cekAlgoJwt
argument_list|)
argument_list|)
argument_list|,
name|keyEncryptionAlgorithm
argument_list|)
expr_stmt|;
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
name|doGetActualCek
argument_list|(
name|theCek
argument_list|,
name|algoJwt
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getCekSize
parameter_list|(
name|String
name|algoJwt
parameter_list|)
block|{
return|return
name|getFullCekKeySize
argument_list|(
name|algoJwt
argument_list|)
operator|*
literal|8
return|;
block|}
specifier|protected
specifier|static
name|byte
index|[]
name|doGetActualCek
parameter_list|(
name|byte
index|[]
name|theCek
parameter_list|,
name|String
name|algoJwt
parameter_list|)
block|{
name|int
name|size
init|=
name|getFullCekKeySize
argument_list|(
name|algoJwt
argument_list|)
operator|/
literal|2
decl_stmt|;
name|byte
index|[]
name|actualCek
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|theCek
argument_list|,
name|size
argument_list|,
name|actualCek
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|actualCek
return|;
block|}
specifier|protected
specifier|static
name|int
name|getFullCekKeySize
parameter_list|(
name|String
name|algoJwt
parameter_list|)
block|{
return|return
name|AES_CEK_SIZE_MAP
operator|.
name|get
argument_list|(
name|algoJwt
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getActualCipher
parameter_list|(
name|byte
index|[]
name|cipher
parameter_list|)
block|{
return|return
name|cipher
return|;
block|}
specifier|protected
name|byte
index|[]
name|getAuthenticationTag
parameter_list|(
name|JweEncryptionInternal
name|state
parameter_list|,
name|byte
index|[]
name|cipher
parameter_list|)
block|{
specifier|final
name|MacState
name|macState
init|=
name|getInitializedMacState
argument_list|(
name|state
argument_list|)
decl_stmt|;
name|macState
operator|.
name|mac
operator|.
name|update
argument_list|(
name|cipher
argument_list|)
expr_stmt|;
return|return
name|signAndGetTag
argument_list|(
name|macState
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|byte
index|[]
name|signAndGetTag
parameter_list|(
name|MacState
name|macState
parameter_list|)
block|{
name|macState
operator|.
name|mac
operator|.
name|update
argument_list|(
name|macState
operator|.
name|al
argument_list|)
expr_stmt|;
name|byte
index|[]
name|sig
init|=
name|macState
operator|.
name|mac
operator|.
name|doFinal
argument_list|()
decl_stmt|;
name|int
name|authTagLen
init|=
name|DEFAULT_AUTH_TAG_LENGTH
operator|/
literal|8
decl_stmt|;
name|byte
index|[]
name|authTag
init|=
operator|new
name|byte
index|[
name|authTagLen
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|sig
argument_list|,
literal|0
argument_list|,
name|authTag
argument_list|,
literal|0
argument_list|,
name|authTagLen
argument_list|)
expr_stmt|;
return|return
name|authTag
return|;
block|}
specifier|private
name|MacState
name|getInitializedMacState
parameter_list|(
specifier|final
name|JweEncryptionInternal
name|state
parameter_list|)
block|{
return|return
name|getInitializedMacState
argument_list|(
name|state
operator|.
name|secretKey
argument_list|,
name|state
operator|.
name|theIv
argument_list|,
name|state
operator|.
name|aad
argument_list|,
name|state
operator|.
name|theHeaders
argument_list|,
name|state
operator|.
name|protectedHeadersJson
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|MacState
name|getInitializedMacState
parameter_list|(
name|byte
index|[]
name|secretKey
parameter_list|,
name|byte
index|[]
name|theIv
parameter_list|,
name|byte
index|[]
name|extraAad
parameter_list|,
name|JweHeaders
name|theHeaders
parameter_list|,
name|String
name|protectedHeadersJson
parameter_list|)
block|{
name|String
name|algoJwt
init|=
name|theHeaders
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|getFullCekKeySize
argument_list|(
name|algoJwt
argument_list|)
operator|/
literal|2
decl_stmt|;
name|byte
index|[]
name|macKey
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|secretKey
argument_list|,
literal|0
argument_list|,
name|macKey
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|String
name|hmacAlgoJava
init|=
name|AES_HMAC_MAP
operator|.
name|get
argument_list|(
name|algoJwt
argument_list|)
decl_stmt|;
name|Mac
name|mac
init|=
name|HmacUtils
operator|.
name|getInitializedMac
argument_list|(
name|macKey
argument_list|,
name|hmacAlgoJava
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|byte
index|[]
name|aad
init|=
name|JweUtils
operator|.
name|getAdditionalAuthenticationData
argument_list|(
name|protectedHeadersJson
argument_list|,
name|extraAad
argument_list|)
decl_stmt|;
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|8
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|al
init|=
name|buf
operator|.
name|putInt
argument_list|(
literal|0
argument_list|)
operator|.
name|putInt
argument_list|(
name|aad
operator|.
name|length
operator|*
literal|8
argument_list|)
operator|.
name|array
argument_list|()
decl_stmt|;
name|mac
operator|.
name|update
argument_list|(
name|aad
argument_list|)
expr_stmt|;
name|mac
operator|.
name|update
argument_list|(
name|theIv
argument_list|)
expr_stmt|;
name|MacState
name|macState
init|=
operator|new
name|MacState
argument_list|()
decl_stmt|;
name|macState
operator|.
name|mac
operator|=
name|mac
expr_stmt|;
name|macState
operator|.
name|al
operator|=
name|al
expr_stmt|;
return|return
name|macState
return|;
block|}
specifier|protected
name|AuthenticationTagProducer
name|getAuthenticationTagProducer
parameter_list|(
specifier|final
name|JweEncryptionInternal
name|state
parameter_list|)
block|{
specifier|final
name|MacState
name|macState
init|=
name|getInitializedMacState
argument_list|(
name|state
argument_list|)
decl_stmt|;
return|return
operator|new
name|AuthenticationTagProducer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|byte
index|[]
name|cipher
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|macState
operator|.
name|mac
operator|.
name|update
argument_list|(
name|cipher
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getTag
parameter_list|()
block|{
return|return
name|signAndGetTag
argument_list|(
name|macState
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
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
specifier|private
specifier|static
class|class
name|AesCbcContentEncryptionAlgorithm
extends|extends
name|AbstractContentEncryptionAlgorithm
block|{
specifier|public
name|AesCbcContentEncryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
name|super
argument_list|(
name|cek
argument_list|,
name|iv
argument_list|,
name|algo
argument_list|)
expr_stmt|;
block|}
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
parameter_list|,
name|byte
index|[]
name|aad
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|MacState
block|{
specifier|protected
name|Mac
name|mac
decl_stmt|;
specifier|private
name|byte
index|[]
name|al
decl_stmt|;
block|}
specifier|private
specifier|static
name|String
name|validateCekAlgorithm
parameter_list|(
name|String
name|cekAlgo
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Algorithm
operator|.
name|isAesCbcHmac
argument_list|(
name|cekAlgo
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
return|return
name|cekAlgo
return|;
block|}
block|}
end_class

end_unit

