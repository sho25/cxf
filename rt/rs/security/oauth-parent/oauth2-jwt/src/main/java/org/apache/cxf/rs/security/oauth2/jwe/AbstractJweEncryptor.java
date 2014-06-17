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
name|io
operator|.
name|UnsupportedEncodingException
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
name|AbstractJweEncryptor
implements|implements
name|JweEncryptor
block|{
specifier|protected
specifier|static
specifier|final
name|int
name|DEFAULT_IV_SIZE
init|=
literal|96
decl_stmt|;
specifier|protected
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
init|=
operator|new
name|JwtTokenReaderWriter
argument_list|()
decl_stmt|;
specifier|private
name|byte
index|[]
name|cek
decl_stmt|;
specifier|private
name|byte
index|[]
name|iv
decl_stmt|;
specifier|private
name|int
name|authTagLen
init|=
name|DEFAULT_AUTH_TAG_LENGTH
decl_stmt|;
specifier|protected
name|AbstractJweEncryptor
parameter_list|(
name|SecretKey
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JweHeaders
argument_list|(
name|Algorithm
operator|.
name|toJwtName
argument_list|(
name|cek
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|cek
operator|.
name|getEncoded
argument_list|()
operator|.
name|length
operator|*
literal|8
argument_list|)
argument_list|)
argument_list|,
name|cek
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|iv
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractJweEncryptor
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
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
name|cek
operator|=
name|cek
expr_stmt|;
name|this
operator|.
name|iv
operator|=
name|iv
expr_stmt|;
block|}
specifier|protected
name|AbstractJweEncryptor
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|int
name|authTagLen
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|)
expr_stmt|;
name|this
operator|.
name|authTagLen
operator|=
name|authTagLen
expr_stmt|;
block|}
specifier|protected
name|AbstractJweEncryptor
parameter_list|(
name|JweHeaders
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
block|}
specifier|protected
name|AbstractJweEncryptor
parameter_list|(
name|JweHeaders
name|headers
parameter_list|,
name|byte
index|[]
name|cek
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|,
name|int
name|authTagLen
parameter_list|,
name|JwtHeadersWriter
name|writer
parameter_list|)
block|{
name|this
argument_list|(
name|headers
argument_list|,
name|cek
argument_list|,
name|iv
argument_list|,
name|authTagLen
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
block|}
block|}
specifier|protected
name|AlgorithmParameterSpec
name|getContentEncryptionCipherSpec
parameter_list|(
name|byte
index|[]
name|theIv
parameter_list|)
block|{
return|return
name|CryptoUtils
operator|.
name|getContentEncryptionCipherSpec
argument_list|(
name|getAuthTagLen
argument_list|()
argument_list|,
name|theIv
argument_list|)
return|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionCipherInitVector
parameter_list|()
block|{
return|return
name|iv
operator|==
literal|null
condition|?
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
name|DEFAULT_IV_SIZE
argument_list|)
else|:
name|iv
return|;
block|}
specifier|protected
name|byte
index|[]
name|getContentEncryptionKey
parameter_list|()
block|{
return|return
name|cek
return|;
block|}
specifier|protected
specifier|abstract
name|byte
index|[]
name|getEncryptedContentEncryptionKey
parameter_list|(
name|byte
index|[]
name|theCek
parameter_list|)
function_decl|;
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
name|authTagLen
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
name|getContentEncryptionCipherInitVector
argument_list|()
decl_stmt|;
name|AlgorithmParameterSpec
name|specParams
init|=
name|getContentEncryptionCipherSpec
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
name|cipherText
init|=
name|CryptoUtils
operator|.
name|encryptBytes
argument_list|(
name|content
argument_list|,
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|theCek
argument_list|,
name|contentEncryptionAlgoJavaName
argument_list|)
argument_list|,
name|keyProps
argument_list|)
decl_stmt|;
name|byte
index|[]
name|jweContentEncryptionKey
init|=
name|getEncryptedContentEncryptionKey
argument_list|(
name|theCek
argument_list|)
decl_stmt|;
name|JweCompactProducer
name|producer
init|=
operator|new
name|JweCompactProducer
argument_list|(
name|theHeaders
argument_list|,
name|jweContentEncryptionKey
argument_list|,
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
specifier|public
name|String
name|encryptText
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|contentType
parameter_list|)
block|{
try|try
block|{
return|return
name|encrypt
argument_list|(
name|text
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
name|contentType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
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
block|}
block|}
end_class

end_unit

