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
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

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
name|KeyFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
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
name|RSAPrivateKey
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
name|RSAPublicKey
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
name|security
operator|.
name|spec
operator|.
name|RSAPrivateKeySpec
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
name|RSAPublicKeySpec
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
name|KeyGenerator
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
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|SecretKeySpec
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
name|Base64Exception
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
name|CompressionUtils
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
name|helpers
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * Encryption helpers  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|EncryptionUtils
block|{
specifier|private
name|EncryptionUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|encodeSecretKey
parameter_list|(
name|SecretKey
name|key
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|encodeBytes
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptSecretKey
parameter_list|(
name|SecretKey
name|secretKey
parameter_list|,
name|PublicKey
name|publicKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|SecretKeyProperties
name|props
init|=
operator|new
name|SecretKeyProperties
argument_list|(
name|publicKey
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|encryptSecretKey
argument_list|(
name|secretKey
argument_list|,
name|publicKey
argument_list|,
name|props
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptSecretKey
parameter_list|(
name|SecretKey
name|secretKey
parameter_list|,
name|PublicKey
name|publicKey
parameter_list|,
name|SecretKeyProperties
name|props
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|byte
index|[]
name|encryptedBytes
init|=
name|encryptBytes
argument_list|(
name|secretKey
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|publicKey
argument_list|,
name|props
argument_list|)
decl_stmt|;
return|return
name|encodeBytes
argument_list|(
name|encryptedBytes
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|RSAPublicKey
name|getRsaPublicKey
parameter_list|(
name|KeyFactory
name|factory
parameter_list|,
name|String
name|encodedModulus
parameter_list|,
name|String
name|encodedPublicExponent
parameter_list|)
block|{
try|try
block|{
return|return
name|getRSAPublicKey
argument_list|(
name|factory
argument_list|,
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encodedModulus
argument_list|)
argument_list|,
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encodedPublicExponent
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|RSAPublicKey
name|getRSAPublicKey
parameter_list|(
name|KeyFactory
name|factory
parameter_list|,
name|byte
index|[]
name|modulusBytes
parameter_list|,
name|byte
index|[]
name|publicExponentBytes
parameter_list|)
block|{
name|BigInteger
name|modulus
init|=
operator|new
name|BigInteger
argument_list|(
literal|1
argument_list|,
name|modulusBytes
argument_list|)
decl_stmt|;
name|BigInteger
name|publicExponent
init|=
operator|new
name|BigInteger
argument_list|(
literal|1
argument_list|,
name|publicExponentBytes
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|RSAPublicKey
operator|)
name|factory
operator|.
name|generatePublic
argument_list|(
operator|new
name|RSAPublicKeySpec
argument_list|(
name|modulus
argument_list|,
name|publicExponent
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|RSAPrivateKey
name|getRSAPrivateKey
parameter_list|(
name|KeyFactory
name|factory
parameter_list|,
name|String
name|encodedModulus
parameter_list|,
name|String
name|encodedPrivateExponent
parameter_list|)
block|{
try|try
block|{
return|return
name|getRSAPrivateKey
argument_list|(
name|factory
argument_list|,
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encodedModulus
argument_list|)
argument_list|,
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encodedPrivateExponent
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|RSAPrivateKey
name|getRSAPrivateKey
parameter_list|(
name|KeyFactory
name|factory
parameter_list|,
name|byte
index|[]
name|modulusBytes
parameter_list|,
name|byte
index|[]
name|privateExponentBytes
parameter_list|)
block|{
name|BigInteger
name|modulus
init|=
operator|new
name|BigInteger
argument_list|(
literal|1
argument_list|,
name|modulusBytes
argument_list|)
decl_stmt|;
name|BigInteger
name|privateExponent
init|=
operator|new
name|BigInteger
argument_list|(
literal|1
argument_list|,
name|privateExponentBytes
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|RSAPrivateKey
operator|)
name|factory
operator|.
name|generatePrivate
argument_list|(
operator|new
name|RSAPrivateKeySpec
argument_list|(
name|modulus
argument_list|,
name|privateExponent
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|SecretKey
name|getSecretKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getSecretKey
argument_list|(
literal|"AES"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|getSecretKey
parameter_list|(
name|String
name|symEncAlgo
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|getSecretKey
argument_list|(
operator|new
name|SecretKeyProperties
argument_list|(
name|symEncAlgo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|getSecretKey
parameter_list|(
name|SecretKeyProperties
name|props
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
name|KeyGenerator
name|keyGen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
name|props
operator|.
name|getKeyAlgo
argument_list|()
argument_list|)
decl_stmt|;
name|AlgorithmParameterSpec
name|algoSpec
init|=
name|props
operator|.
name|getAlgoSpec
argument_list|()
decl_stmt|;
name|SecureRandom
name|random
init|=
name|props
operator|.
name|getSecureRandom
argument_list|()
decl_stmt|;
if|if
condition|(
name|algoSpec
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|random
operator|!=
literal|null
condition|)
block|{
name|keyGen
operator|.
name|init
argument_list|(
name|algoSpec
argument_list|,
name|random
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyGen
operator|.
name|init
argument_list|(
name|algoSpec
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|int
name|keySize
init|=
name|props
operator|.
name|getKeySize
argument_list|()
decl_stmt|;
if|if
condition|(
name|keySize
operator|==
operator|-
literal|1
condition|)
block|{
name|keySize
operator|=
literal|128
expr_stmt|;
block|}
if|if
condition|(
name|random
operator|!=
literal|null
condition|)
block|{
name|keyGen
operator|.
name|init
argument_list|(
name|keySize
argument_list|,
name|random
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyGen
operator|.
name|init
argument_list|(
name|keySize
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|keyGen
operator|.
name|generateKey
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|decryptSequence
parameter_list|(
name|String
name|encodedToken
parameter_list|,
name|String
name|encodedSecretKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|decryptSequence
argument_list|(
name|encodedToken
argument_list|,
name|encodedSecretKey
argument_list|,
operator|new
name|SecretKeyProperties
argument_list|(
literal|"AES"
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|decryptSequence
parameter_list|(
name|String
name|encodedData
parameter_list|,
name|String
name|encodedSecretKey
parameter_list|,
name|SecretKeyProperties
name|props
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|SecretKey
name|key
init|=
name|decodeSecretKey
argument_list|(
name|encodedSecretKey
argument_list|,
name|props
operator|.
name|getKeyAlgo
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|decryptSequence
argument_list|(
name|encodedData
argument_list|,
name|key
argument_list|,
name|props
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|decryptSequence
parameter_list|(
name|String
name|encodedData
parameter_list|,
name|Key
name|secretKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|decryptSequence
argument_list|(
name|encodedData
argument_list|,
name|secretKey
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|decryptSequence
parameter_list|(
name|String
name|encodedData
parameter_list|,
name|Key
name|secretKey
parameter_list|,
name|SecretKeyProperties
name|props
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|byte
index|[]
name|encryptedBytes
init|=
name|decodeSequence
argument_list|(
name|encodedData
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|decryptBytes
argument_list|(
name|encryptedBytes
argument_list|,
name|secretKey
argument_list|,
name|props
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|encryptSequence
parameter_list|(
name|String
name|sequence
parameter_list|,
name|Key
name|secretKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|encryptSequence
argument_list|(
name|sequence
argument_list|,
name|secretKey
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|encryptSequence
parameter_list|(
name|String
name|sequence
parameter_list|,
name|Key
name|secretKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
name|byte
index|[]
name|bytes
init|=
name|encryptBytes
argument_list|(
name|sequence
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
name|secretKey
argument_list|,
name|keyProps
argument_list|)
decl_stmt|;
return|return
name|encodeBytes
argument_list|(
name|bytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|encodeBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
return|return
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|bytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|byte
index|[]
name|encryptBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|Key
name|secretKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|encryptBytes
argument_list|(
name|bytes
argument_list|,
name|secretKey
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|encryptBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|Key
name|secretKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|processBytes
argument_list|(
name|bytes
argument_list|,
name|secretKey
argument_list|,
name|keyProps
argument_list|,
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|decryptBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|Key
name|secretKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|decryptBytes
argument_list|(
name|bytes
argument_list|,
name|secretKey
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|decryptBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|Key
name|secretKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|processBytes
argument_list|(
name|bytes
argument_list|,
name|secretKey
argument_list|,
name|keyProps
argument_list|,
name|Cipher
operator|.
name|DECRYPT_MODE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|wrapSecretKey
parameter_list|(
name|byte
index|[]
name|keyBytes
parameter_list|,
name|String
name|keyAlgo
parameter_list|,
name|Key
name|wrapperKey
parameter_list|,
name|String
name|wrapperKeyAlgo
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|wrapSecretKey
argument_list|(
operator|new
name|SecretKeySpec
argument_list|(
name|keyBytes
argument_list|,
name|keyAlgo
argument_list|)
argument_list|,
name|wrapperKey
argument_list|,
operator|new
name|SecretKeyProperties
argument_list|(
name|wrapperKeyAlgo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|wrapSecretKey
parameter_list|(
name|SecretKey
name|secretKey
parameter_list|,
name|Key
name|wrapperKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
name|Cipher
name|c
init|=
name|initCipher
argument_list|(
name|wrapperKey
argument_list|,
name|keyProps
argument_list|,
name|Cipher
operator|.
name|WRAP_MODE
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|wrap
argument_list|(
name|secretKey
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|SecretKey
name|unwrapSecretKey
parameter_list|(
name|byte
index|[]
name|wrappedBytes
parameter_list|,
name|String
name|wrappedKeyAlgo
parameter_list|,
name|Key
name|unwrapperKey
parameter_list|,
name|String
name|unwrapperKeyAlgo
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|unwrapSecretKey
argument_list|(
name|wrappedBytes
argument_list|,
name|wrappedKeyAlgo
argument_list|,
name|unwrapperKey
argument_list|,
operator|new
name|SecretKeyProperties
argument_list|(
name|unwrapperKeyAlgo
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|unwrapSecretKey
parameter_list|(
name|byte
index|[]
name|wrappedBytes
parameter_list|,
name|String
name|wrappedKeyAlgo
parameter_list|,
name|Key
name|unwrapperKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
name|Cipher
name|c
init|=
name|initCipher
argument_list|(
name|unwrapperKey
argument_list|,
name|keyProps
argument_list|,
name|Cipher
operator|.
name|UNWRAP_MODE
argument_list|)
decl_stmt|;
return|return
operator|(
name|SecretKey
operator|)
name|c
operator|.
name|unwrap
argument_list|(
name|wrappedBytes
argument_list|,
name|wrappedKeyAlgo
argument_list|,
name|Cipher
operator|.
name|SECRET_KEY
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|byte
index|[]
name|processBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|Key
name|secretKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|,
name|int
name|mode
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|boolean
name|compressionSupported
init|=
name|keyProps
operator|!=
literal|null
operator|&&
name|keyProps
operator|.
name|isCompressionSupported
argument_list|()
decl_stmt|;
if|if
condition|(
name|compressionSupported
operator|&&
name|mode
operator|==
name|Cipher
operator|.
name|ENCRYPT_MODE
condition|)
block|{
name|bytes
operator|=
name|CompressionUtils
operator|.
name|deflate
argument_list|(
name|bytes
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Cipher
name|c
init|=
name|initCipher
argument_list|(
name|secretKey
argument_list|,
name|keyProps
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
name|int
name|blockSize
init|=
name|keyProps
operator|!=
literal|null
condition|?
name|keyProps
operator|.
name|getBlockSize
argument_list|()
else|:
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|secretKey
operator|instanceof
name|SecretKey
operator|&&
name|blockSize
operator|==
operator|-
literal|1
condition|)
block|{
name|result
operator|=
name|c
operator|.
name|doFinal
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|blockSize
operator|==
operator|-
literal|1
condition|)
block|{
name|blockSize
operator|=
name|secretKey
operator|instanceof
name|PublicKey
condition|?
literal|117
else|:
literal|128
expr_stmt|;
block|}
name|int
name|offset
init|=
literal|0
decl_stmt|;
for|for
control|(
init|;
name|offset
operator|+
name|blockSize
operator|<
name|bytes
operator|.
name|length
condition|;
name|offset
operator|+=
name|blockSize
control|)
block|{
name|result
operator|=
name|addToResult
argument_list|(
name|result
argument_list|,
name|c
operator|.
name|doFinal
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|blockSize
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|offset
operator|<
name|bytes
operator|.
name|length
condition|)
block|{
name|result
operator|=
name|addToResult
argument_list|(
name|result
argument_list|,
name|c
operator|.
name|doFinal
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|bytes
operator|.
name|length
operator|-
name|offset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|compressionSupported
operator|&&
name|mode
operator|==
name|Cipher
operator|.
name|DECRYPT_MODE
condition|)
block|{
name|result
operator|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|CompressionUtils
operator|.
name|inflate
argument_list|(
name|result
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|Cipher
name|initCipher
parameter_list|(
name|Key
name|secretKey
parameter_list|,
name|SecretKeyProperties
name|keyProps
parameter_list|,
name|int
name|mode
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
name|String
name|algorithm
init|=
name|keyProps
operator|!=
literal|null
operator|&&
name|keyProps
operator|.
name|getKeyAlgo
argument_list|()
operator|!=
literal|null
condition|?
name|keyProps
operator|.
name|getKeyAlgo
argument_list|()
else|:
name|secretKey
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyProps
operator|==
literal|null
operator|||
name|keyProps
operator|.
name|getAlgoSpec
argument_list|()
operator|==
literal|null
operator|&&
name|keyProps
operator|.
name|getSecureRandom
argument_list|()
operator|==
literal|null
condition|)
block|{
name|c
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|secretKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|AlgorithmParameterSpec
name|algoSpec
init|=
name|keyProps
operator|.
name|getAlgoSpec
argument_list|()
decl_stmt|;
name|SecureRandom
name|random
init|=
name|keyProps
operator|.
name|getSecureRandom
argument_list|()
decl_stmt|;
if|if
condition|(
name|algoSpec
operator|==
literal|null
condition|)
block|{
name|c
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|secretKey
argument_list|,
name|random
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|random
operator|==
literal|null
condition|)
block|{
name|c
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|secretKey
argument_list|,
name|algoSpec
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|c
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|secretKey
argument_list|,
name|algoSpec
argument_list|,
name|random
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|keyProps
operator|!=
literal|null
operator|&&
name|keyProps
operator|.
name|getAdditionalData
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// TODO: call updateAAD directly after switching to Java7
name|Method
name|m
init|=
name|Cipher
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"updateAAD"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|byte
index|[]
operator|.
expr|class
block|}
argument_list|)
decl_stmt|;
name|m
operator|.
name|invoke
argument_list|(
name|c
argument_list|,
operator|new
name|Object
index|[]
block|{
name|keyProps
operator|.
name|getAdditionalData
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|byte
index|[]
name|addToResult
parameter_list|(
name|byte
index|[]
name|prefix
parameter_list|,
name|byte
index|[]
name|suffix
parameter_list|)
block|{
name|byte
index|[]
name|result
init|=
operator|new
name|byte
index|[
name|prefix
operator|.
name|length
operator|+
name|suffix
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|prefix
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|prefix
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|suffix
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|prefix
operator|.
name|length
argument_list|,
name|suffix
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|decodeSecretKey
parameter_list|(
name|String
name|encodedSecretKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
return|return
name|decodeSecretKey
argument_list|(
name|encodedSecretKey
argument_list|,
literal|"AES"
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|decodeSecretKey
parameter_list|(
name|String
name|encodedSecretKey
parameter_list|,
name|String
name|secretKeyAlgo
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|byte
index|[]
name|secretKeyBytes
init|=
name|decodeSequence
argument_list|(
name|encodedSecretKey
argument_list|)
decl_stmt|;
return|return
name|recreateSecretKey
argument_list|(
name|secretKeyBytes
argument_list|,
name|secretKeyAlgo
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|decryptSecretKey
parameter_list|(
name|String
name|encodedEncryptedSecretKey
parameter_list|,
name|PrivateKey
name|privateKey
parameter_list|)
block|{
return|return
name|decryptSecretKey
argument_list|(
name|encodedEncryptedSecretKey
argument_list|,
literal|"AES"
argument_list|,
name|privateKey
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|decryptSecretKey
parameter_list|(
name|String
name|encodedEncryptedSecretKey
parameter_list|,
name|String
name|secretKeyAlgo
parameter_list|,
name|PrivateKey
name|privateKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|SecretKeyProperties
name|props
init|=
operator|new
name|SecretKeyProperties
argument_list|(
name|privateKey
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|decryptSecretKey
argument_list|(
name|encodedEncryptedSecretKey
argument_list|,
name|secretKeyAlgo
argument_list|,
name|props
argument_list|,
name|privateKey
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|decryptSecretKey
parameter_list|(
name|String
name|encodedEncryptedSecretKey
parameter_list|,
name|String
name|secretKeyAlgo
parameter_list|,
name|SecretKeyProperties
name|props
parameter_list|,
name|PrivateKey
name|privateKey
parameter_list|)
throws|throws
name|EncryptionException
block|{
name|byte
index|[]
name|encryptedBytes
init|=
name|decodeSequence
argument_list|(
name|encodedEncryptedSecretKey
argument_list|)
decl_stmt|;
name|byte
index|[]
name|descryptedBytes
init|=
name|decryptBytes
argument_list|(
name|encryptedBytes
argument_list|,
name|privateKey
argument_list|,
name|props
argument_list|)
decl_stmt|;
return|return
name|recreateSecretKey
argument_list|(
name|descryptedBytes
argument_list|,
name|secretKeyAlgo
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|SecretKey
name|recreateSecretKey
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|algo
parameter_list|)
block|{
return|return
operator|new
name|SecretKeySpec
argument_list|(
name|bytes
argument_list|,
name|algo
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|decodeSequence
parameter_list|(
name|String
name|encodedSequence
parameter_list|)
throws|throws
name|EncryptionException
block|{
try|try
block|{
return|return
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|encodedSequence
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|EncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

