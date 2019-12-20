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
name|nio
operator|.
name|CharBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|rt
operator|.
name|security
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
name|rt
operator|.
name|security
operator|.
name|crypto
operator|.
name|MessageDigestUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|Digest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|digests
operator|.
name|SHA256Digest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|digests
operator|.
name|SHA384Digest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|digests
operator|.
name|SHA512Digest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|generators
operator|.
name|PKCS5S2ParametersGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|crypto
operator|.
name|params
operator|.
name|KeyParameter
import|;
end_import

begin_class
specifier|public
class|class
name|PbesHmacAesWrapKeyEncryptionAlgorithm
implements|implements
name|KeyEncryptionProvider
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|PbesHmacAesWrapKeyEncryptionAlgorithm
operator|.
name|class
argument_list|)
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
name|PBES_HMAC_MAP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|PBES_AES_MAP
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
name|DERIVED_KEY_SIZE_MAP
decl_stmt|;
static|static
block|{
name|PBES_HMAC_MAP
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|PBES_HMAC_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS256_A128KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|256
argument_list|)
expr_stmt|;
name|PBES_HMAC_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS384_A192KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|384
argument_list|)
expr_stmt|;
name|PBES_HMAC_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS512_A256KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|512
argument_list|)
expr_stmt|;
name|PBES_AES_MAP
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|PBES_AES_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS256_A128KW
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
name|PBES_AES_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS384_A192KW
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
name|PBES_AES_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS512_A256KW
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
name|DERIVED_KEY_SIZE_MAP
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|DERIVED_KEY_SIZE_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS256_A128KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|16
argument_list|)
expr_stmt|;
name|DERIVED_KEY_SIZE_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS384_A192KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|24
argument_list|)
expr_stmt|;
name|DERIVED_KEY_SIZE_MAP
operator|.
name|put
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS512_A256KW
operator|.
name|getJwaName
argument_list|()
argument_list|,
literal|32
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|byte
index|[]
name|password
decl_stmt|;
specifier|private
specifier|final
name|int
name|pbesCount
decl_stmt|;
specifier|private
specifier|final
name|KeyAlgorithm
name|keyAlgoJwt
decl_stmt|;
specifier|public
name|PbesHmacAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|String
name|password
parameter_list|,
name|KeyAlgorithm
name|keyAlgoJwt
parameter_list|)
block|{
name|this
argument_list|(
name|stringToBytes
argument_list|(
name|password
argument_list|)
argument_list|,
name|keyAlgoJwt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PbesHmacAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|String
name|password
parameter_list|,
name|int
name|pbesCount
parameter_list|,
name|KeyAlgorithm
name|keyAlgoJwt
parameter_list|,
name|boolean
name|hashLargePasswords
parameter_list|)
block|{
name|this
argument_list|(
name|stringToBytes
argument_list|(
name|password
argument_list|)
argument_list|,
name|pbesCount
argument_list|,
name|keyAlgoJwt
argument_list|,
name|hashLargePasswords
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PbesHmacAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|char
index|[]
name|password
parameter_list|,
name|KeyAlgorithm
name|keyAlgoJwt
parameter_list|)
block|{
name|this
argument_list|(
name|password
argument_list|,
literal|4096
argument_list|,
name|keyAlgoJwt
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PbesHmacAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|char
index|[]
name|password
parameter_list|,
name|int
name|pbesCount
parameter_list|,
name|KeyAlgorithm
name|keyAlgoJwt
parameter_list|,
name|boolean
name|hashLargePasswords
parameter_list|)
block|{
name|this
argument_list|(
name|charsToBytes
argument_list|(
name|password
argument_list|)
argument_list|,
name|pbesCount
argument_list|,
name|keyAlgoJwt
argument_list|,
name|hashLargePasswords
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PbesHmacAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|password
parameter_list|,
name|KeyAlgorithm
name|keyAlgoJwt
parameter_list|)
block|{
name|this
argument_list|(
name|password
argument_list|,
literal|4096
argument_list|,
name|keyAlgoJwt
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PbesHmacAesWrapKeyEncryptionAlgorithm
parameter_list|(
name|byte
index|[]
name|password
parameter_list|,
name|int
name|pbesCount
parameter_list|,
name|KeyAlgorithm
name|keyAlgoJwt
parameter_list|,
name|boolean
name|hashLargePasswords
parameter_list|)
block|{
name|this
operator|.
name|keyAlgoJwt
operator|=
name|validateKeyAlgorithm
argument_list|(
name|keyAlgoJwt
argument_list|)
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|validatePassword
argument_list|(
name|password
argument_list|,
name|keyAlgoJwt
operator|.
name|getJwaName
argument_list|()
argument_list|,
name|hashLargePasswords
argument_list|)
expr_stmt|;
name|this
operator|.
name|pbesCount
operator|=
name|validatePbesCount
argument_list|(
name|pbesCount
argument_list|)
expr_stmt|;
block|}
specifier|static
name|byte
index|[]
name|validatePassword
parameter_list|(
name|byte
index|[]
name|p
parameter_list|,
name|String
name|keyAlgoJwt
parameter_list|,
name|boolean
name|hashLargePasswords
parameter_list|)
block|{
name|int
name|minLen
init|=
name|DERIVED_KEY_SIZE_MAP
operator|.
name|get
argument_list|(
name|keyAlgoJwt
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|length
argument_list|<
name|minLen
operator|||
name|p
operator|.
name|length
argument_list|>
literal|128
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid password length: "
operator|+
name|p
operator|.
name|length
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JweException
argument_list|(
name|JweException
operator|.
name|Error
operator|.
name|KEY_ENCRYPTION_FAILURE
argument_list|)
throw|;
block|}
if|if
condition|(
name|p
operator|.
name|length
operator|>
name|minLen
operator|&&
name|hashLargePasswords
condition|)
block|{
try|try
block|{
return|return
name|MessageDigestUtils
operator|.
name|createDigest
argument_list|(
name|p
argument_list|,
name|MessageDigestUtils
operator|.
name|ALGO_SHA_256
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Password hash calculation error"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JweException
argument_list|(
name|JweException
operator|.
name|Error
operator|.
name|KEY_ENCRYPTION_FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|p
return|;
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
name|int
name|keySize
init|=
name|getKeySize
argument_list|(
name|keyAlgoJwt
operator|.
name|getJwaName
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|saltInput
init|=
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
name|keySize
argument_list|)
decl_stmt|;
name|byte
index|[]
name|derivedKey
init|=
name|createDerivedKey
argument_list|(
name|keyAlgoJwt
operator|.
name|getJwaName
argument_list|()
argument_list|,
name|keySize
argument_list|,
name|password
argument_list|,
name|saltInput
argument_list|,
name|pbesCount
argument_list|)
decl_stmt|;
name|headers
operator|.
name|setHeader
argument_list|(
literal|"p2s"
argument_list|,
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|saltInput
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setIntegerHeader
argument_list|(
literal|"p2c"
argument_list|,
name|pbesCount
argument_list|)
expr_stmt|;
name|KeyEncryptionProvider
name|aesWrap
init|=
operator|new
name|AesWrapKeyEncryptionAlgorithm
argument_list|(
name|derivedKey
argument_list|,
name|keyAlgoJwt
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
specifier|static
name|int
name|getKeySize
parameter_list|(
name|String
name|keyAlgoJwt
parameter_list|)
block|{
return|return
name|DERIVED_KEY_SIZE_MAP
operator|.
name|get
argument_list|(
name|keyAlgoJwt
argument_list|)
return|;
block|}
specifier|static
name|byte
index|[]
name|createDerivedKey
parameter_list|(
name|String
name|keyAlgoJwt
parameter_list|,
name|int
name|keySize
parameter_list|,
name|byte
index|[]
name|password
parameter_list|,
name|byte
index|[]
name|saltInput
parameter_list|,
name|int
name|pbesCount
parameter_list|)
block|{
name|byte
index|[]
name|saltValue
init|=
name|createSaltValue
argument_list|(
name|keyAlgoJwt
argument_list|,
name|saltInput
argument_list|)
decl_stmt|;
name|Digest
name|digest
init|=
literal|null
decl_stmt|;
name|int
name|macSigSize
init|=
name|PBES_HMAC_MAP
operator|.
name|get
argument_list|(
name|keyAlgoJwt
argument_list|)
decl_stmt|;
if|if
condition|(
name|macSigSize
operator|==
literal|256
condition|)
block|{
name|digest
operator|=
operator|new
name|SHA256Digest
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|macSigSize
operator|==
literal|384
condition|)
block|{
name|digest
operator|=
operator|new
name|SHA384Digest
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|digest
operator|=
operator|new
name|SHA512Digest
argument_list|()
expr_stmt|;
block|}
name|PKCS5S2ParametersGenerator
name|gen
init|=
operator|new
name|PKCS5S2ParametersGenerator
argument_list|(
name|digest
argument_list|)
decl_stmt|;
name|gen
operator|.
name|init
argument_list|(
name|password
argument_list|,
name|saltValue
argument_list|,
name|pbesCount
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|KeyParameter
operator|)
name|gen
operator|.
name|generateDerivedParameters
argument_list|(
name|keySize
operator|*
literal|8
argument_list|)
operator|)
operator|.
name|getKey
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|byte
index|[]
name|createSaltValue
parameter_list|(
name|String
name|keyAlgoJwt
parameter_list|,
name|byte
index|[]
name|saltInput
parameter_list|)
block|{
name|byte
index|[]
name|algoBytes
init|=
name|stringToBytes
argument_list|(
name|keyAlgoJwt
argument_list|)
decl_stmt|;
name|byte
index|[]
name|saltValue
init|=
operator|new
name|byte
index|[
name|algoBytes
operator|.
name|length
operator|+
literal|1
operator|+
name|saltInput
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|algoBytes
argument_list|,
literal|0
argument_list|,
name|saltValue
argument_list|,
literal|0
argument_list|,
name|algoBytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|saltValue
index|[
name|algoBytes
operator|.
name|length
index|]
operator|=
literal|0
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|saltInput
argument_list|,
literal|0
argument_list|,
name|saltValue
argument_list|,
name|algoBytes
operator|.
name|length
operator|+
literal|1
argument_list|,
name|saltInput
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|saltValue
return|;
block|}
specifier|static
name|KeyAlgorithm
name|validateKeyAlgorithm
parameter_list|(
name|KeyAlgorithm
name|algo
parameter_list|)
block|{
if|if
condition|(
operator|!
name|AlgorithmUtils
operator|.
name|isPbesHsWrap
argument_list|(
name|algo
operator|.
name|getJwaName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Invalid key encryption algorithm"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JweException
argument_list|(
name|JweException
operator|.
name|Error
operator|.
name|INVALID_KEY_ALGORITHM
argument_list|)
throw|;
block|}
return|return
name|algo
return|;
block|}
specifier|static
name|int
name|validatePbesCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
if|if
condition|(
name|count
operator|<
literal|1000
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Iteration count is too low"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JweException
argument_list|(
name|JweException
operator|.
name|Error
operator|.
name|KEY_ENCRYPTION_FAILURE
argument_list|)
throw|;
block|}
return|return
name|count
return|;
block|}
specifier|static
name|byte
index|[]
name|stringToBytes
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|str
argument_list|)
return|;
block|}
specifier|static
name|byte
index|[]
name|charsToBytes
parameter_list|(
name|char
index|[]
name|chars
parameter_list|)
block|{
name|ByteBuffer
name|bb
init|=
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|encode
argument_list|(
name|CharBuffer
operator|.
name|wrap
argument_list|(
name|chars
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|b
init|=
operator|new
name|byte
index|[
name|bb
operator|.
name|remaining
argument_list|()
index|]
decl_stmt|;
name|bb
operator|.
name|get
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|b
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
name|keyAlgoJwt
return|;
block|}
block|}
end_class

end_unit

