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
name|Security
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
name|jws
operator|.
name|JwsCompactReaderWriterTest
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
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JweCompactReaderWriterTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|CONTENT_ENCRYPTION_KEY
init|=
block|{
operator|(
name|byte
operator|)
literal|177
block|,
operator|(
name|byte
operator|)
literal|161
block|,
operator|(
name|byte
operator|)
literal|244
block|,
operator|(
name|byte
operator|)
literal|128
block|,
literal|84
block|,
operator|(
name|byte
operator|)
literal|143
block|,
operator|(
name|byte
operator|)
literal|225
block|,
literal|115
block|,
literal|63
block|,
operator|(
name|byte
operator|)
literal|180
block|,
literal|3
block|,
operator|(
name|byte
operator|)
literal|255
block|,
literal|107
block|,
operator|(
name|byte
operator|)
literal|154
block|,
operator|(
name|byte
operator|)
literal|212
block|,
operator|(
name|byte
operator|)
literal|246
block|,
operator|(
name|byte
operator|)
literal|138
block|,
literal|7
block|,
literal|110
block|,
literal|91
block|,
literal|112
block|,
literal|46
block|,
literal|34
block|,
literal|105
block|,
literal|47
block|,
operator|(
name|byte
operator|)
literal|130
block|,
operator|(
name|byte
operator|)
literal|203
block|,
literal|46
block|,
literal|122
block|,
operator|(
name|byte
operator|)
literal|234
block|,
literal|64
block|,
operator|(
name|byte
operator|)
literal|252
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_MODULUS_ENCODED
init|=
literal|"oahUIoWw0K0usKNuOR6H4wkf4oBUXHTxRvgb48E-BVvxkeDNjbC4he8rUW"
operator|+
literal|"cJoZmds2h7M70imEVhRU5djINXtqllXI4DFqcI1DgjT9LewND8MW2Krf3S"
operator|+
literal|"psk_ZkoFnilakGygTwpZ3uesH-PFABNIUYpOiN15dsQRkgr0vEhxN92i2a"
operator|+
literal|"sbOenSZeyaxziK72UwxrrKoExv6kc5twXTq4h-QChLOln0_mtUZwfsRaMS"
operator|+
literal|"tPs6mS6XrgxnxbWhojf663tuEQueGC-FCMfra36C9knDFGzKsNa7LZK2dj"
operator|+
literal|"YgyD3JR_MB_4NUJW_TqOQtwHYbxevoJArm-L5StowjzGy-_bq6Gw"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PUBLIC_EXPONENT_ENCODED
init|=
literal|"AQAB"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PRIVATE_EXPONENT_ENCODED
init|=
literal|"kLdtIj6GbDks_ApCSTYQtelcNttlKiOyPzMrXHeI-yk1F7-kpDxY4-WY5N"
operator|+
literal|"WV5KntaEeXS1j82E375xxhWMHXyvjYecPT9fpwR_M9gV8n9Hrh2anTpTD9"
operator|+
literal|"3Dt62ypW3yDsJzBnTnrYu1iwWRgBKrEYY46qAZIrA2xAwnm2X7uGR1hghk"
operator|+
literal|"qDp0Vqj3kbSCz1XyfCs6_LehBwtxHIyh8Ripy40p24moOAbgxVw3rxT_vl"
operator|+
literal|"t3UVe4WO3JkJOzlpUf-KTVI2Ptgm-dARxTEtE-id-4OJr0h-K-VFs3VSnd"
operator|+
literal|"VTIznSxfyrj8ILL6MG_Uv8YAu7VILSB3lOW085-4qE3DzgrTjgyQ"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|INIT_VECTOR
init|=
block|{
operator|(
name|byte
operator|)
literal|227
block|,
operator|(
name|byte
operator|)
literal|197
block|,
literal|117
block|,
operator|(
name|byte
operator|)
literal|252
block|,
literal|2
block|,
operator|(
name|byte
operator|)
literal|219
block|,
operator|(
name|byte
operator|)
literal|233
block|,
literal|68
block|,
operator|(
name|byte
operator|)
literal|180
block|,
operator|(
name|byte
operator|)
literal|225
block|,
literal|77
block|,
operator|(
name|byte
operator|)
literal|219
block|}
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|registerBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// Java 8 apparently has it
name|Cipher
operator|.
name|getInstance
argument_list|(
name|Algorithm
operator|.
name|AES_GCM_ALGO_JAVA
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Oracle Java 7
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|unregisterBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
name|Security
operator|.
name|removeProvider
argument_list|(
name|BouncyCastleProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptDecryptSpecExample
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|specPlainText
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|String
name|jweContent
init|=
name|encryptContent
argument_list|(
name|specPlainText
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|decrypt
argument_list|(
name|jweContent
argument_list|,
name|specPlainText
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDirectKeyEncryptDecrypt
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|specPlainText
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|SecretKey
name|key
init|=
name|createSecretKey
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|String
name|jweContent
init|=
name|encryptContentDirect
argument_list|(
name|key
argument_list|,
name|specPlainText
argument_list|)
decl_stmt|;
name|decryptDirect
argument_list|(
name|key
argument_list|,
name|jweContent
argument_list|,
name|specPlainText
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptDecryptJwsToken
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|jweContent
init|=
name|encryptContent
argument_list|(
name|JwsCompactReaderWriterTest
operator|.
name|ENCODED_TOKEN_SIGNED_BY_MAC
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|decrypt
argument_list|(
name|jweContent
argument_list|,
name|JwsCompactReaderWriterTest
operator|.
name|ENCODED_TOKEN_SIGNED_BY_MAC
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|encryptContent
parameter_list|(
name|String
name|content
parameter_list|,
name|boolean
name|createIfException
parameter_list|)
throws|throws
name|Exception
block|{
name|RSAPublicKey
name|publicKey
init|=
name|CryptoUtils
operator|.
name|getRSAPublicKey
argument_list|(
name|RSA_MODULUS_ENCODED
argument_list|,
name|RSA_PUBLIC_EXPONENT_ENCODED
argument_list|)
decl_stmt|;
name|SecretKey
name|key
init|=
name|createSecretKey
argument_list|(
name|createIfException
argument_list|)
decl_stmt|;
name|String
name|jwtKeyName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
comment|// the encryptor will generate it
name|jwtKeyName
operator|=
name|Algorithm
operator|.
name|A128GCM
operator|.
name|getJwtName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|jwtKeyName
operator|=
name|Algorithm
operator|.
name|toJwtName
argument_list|(
name|key
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|key
operator|.
name|getEncoded
argument_list|()
operator|.
name|length
operator|*
literal|8
argument_list|)
expr_stmt|;
block|}
name|RSAJweEncryptor
name|encryptor
init|=
operator|new
name|RSAJweEncryptor
argument_list|(
name|publicKey
argument_list|,
name|key
argument_list|,
name|jwtKeyName
argument_list|,
name|INIT_VECTOR
argument_list|)
decl_stmt|;
return|return
name|encryptor
operator|.
name|encryptText
argument_list|(
name|content
argument_list|)
return|;
block|}
specifier|private
name|String
name|encryptContentDirect
parameter_list|(
name|SecretKey
name|key
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|Exception
block|{
name|DirectKeyJweEncryptor
name|encryptor
init|=
operator|new
name|DirectKeyJweEncryptor
argument_list|(
name|key
argument_list|,
name|INIT_VECTOR
argument_list|)
decl_stmt|;
return|return
name|encryptor
operator|.
name|encryptText
argument_list|(
name|content
argument_list|)
return|;
block|}
specifier|private
name|void
name|decrypt
parameter_list|(
name|String
name|jweContent
parameter_list|,
name|String
name|plainContent
parameter_list|)
throws|throws
name|Exception
block|{
name|RSAPrivateKey
name|privateKey
init|=
name|CryptoUtils
operator|.
name|getRSAPrivateKey
argument_list|(
name|RSA_MODULUS_ENCODED
argument_list|,
name|RSA_PRIVATE_EXPONENT_ENCODED
argument_list|)
decl_stmt|;
name|RSAJweDecryptor
name|decryptor
init|=
operator|new
name|RSAJweDecryptor
argument_list|(
name|privateKey
argument_list|)
decl_stmt|;
name|String
name|decryptedText
init|=
name|decryptor
operator|.
name|decrypt
argument_list|(
name|jweContent
argument_list|)
operator|.
name|getContentText
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|decryptedText
argument_list|,
name|plainContent
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|decryptDirect
parameter_list|(
name|SecretKey
name|key
parameter_list|,
name|String
name|jweContent
parameter_list|,
name|String
name|plainContent
parameter_list|)
throws|throws
name|Exception
block|{
name|DirectKeyJweDecryptor
name|decryptor
init|=
operator|new
name|DirectKeyJweDecryptor
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|String
name|decryptedText
init|=
name|decryptor
operator|.
name|decrypt
argument_list|(
name|jweContent
argument_list|)
operator|.
name|getContentText
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|decryptedText
argument_list|,
name|plainContent
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SecretKey
name|createSecretKey
parameter_list|(
name|boolean
name|createIfException
parameter_list|)
throws|throws
name|Exception
block|{
name|SecretKey
name|key
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Cipher
operator|.
name|getMaxAllowedKeyLength
argument_list|(
literal|"AES"
argument_list|)
operator|>
literal|128
condition|)
block|{
name|key
operator|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|CONTENT_ENCRYPTION_KEY
argument_list|,
literal|"AES"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|createIfException
condition|)
block|{
name|key
operator|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|128
operator|/
literal|8
argument_list|)
argument_list|,
literal|"AES"
argument_list|)
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
block|}
end_class

end_unit

