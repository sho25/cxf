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
name|jose
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
comment|// A1 example
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|CONTENT_ENCRYPTION_KEY_A1
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
name|RSA_MODULUS_ENCODED_A1
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
name|RSA_PUBLIC_EXPONENT_ENCODED_A1
init|=
literal|"AQAB"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PRIVATE_EXPONENT_ENCODED_A1
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
name|INIT_VECTOR_A1
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
comment|// A3 example
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|CONTENT_ENCRYPTION_KEY_A3
init|=
block|{
literal|4
block|,
operator|(
name|byte
operator|)
literal|211
block|,
literal|31
block|,
operator|(
name|byte
operator|)
literal|197
block|,
literal|84
block|,
operator|(
name|byte
operator|)
literal|157
block|,
operator|(
name|byte
operator|)
literal|252
block|,
operator|(
name|byte
operator|)
literal|254
block|,
literal|11
block|,
literal|100
block|,
operator|(
name|byte
operator|)
literal|157
block|,
operator|(
name|byte
operator|)
literal|250
block|,
literal|63
block|,
operator|(
name|byte
operator|)
literal|170
block|,
literal|106
block|,
operator|(
name|byte
operator|)
literal|206
block|,
literal|107
block|,
literal|124
block|,
operator|(
name|byte
operator|)
literal|212
block|,
literal|45
block|,
literal|111
block|,
literal|107
block|,
literal|9
block|,
operator|(
name|byte
operator|)
literal|219
block|,
operator|(
name|byte
operator|)
literal|200
block|,
operator|(
name|byte
operator|)
literal|177
block|,
literal|0
block|,
operator|(
name|byte
operator|)
literal|240
block|,
operator|(
name|byte
operator|)
literal|143
block|,
operator|(
name|byte
operator|)
literal|156
block|,
literal|44
block|,
operator|(
name|byte
operator|)
literal|207
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|INIT_VECTOR_A3
init|=
block|{
literal|3
block|,
literal|22
block|,
literal|60
block|,
literal|12
block|,
literal|43
block|,
literal|67
block|,
literal|104
block|,
literal|105
block|,
literal|108
block|,
literal|108
block|,
literal|105
block|,
literal|99
block|,
literal|111
block|,
literal|116
block|,
literal|104
block|,
literal|101
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY_ENCRYPTION_KEY_A3
init|=
literal|"GawgguFyGrWKav7AX4VKUg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JWE_OUTPUT_A3
init|=
literal|"eyJhbGciOiJBMTI4S1ciLCJlbmMiOiJBMTI4Q0JDLUhTMjU2In0"
operator|+
literal|".6KB707dM9YTIgHtLvtgWQ8mKwboJW3of9locizkDTHzBC2IlrT1oOQ"
operator|+
literal|".AxY8DCtDaGlsbGljb3RoZQ"
operator|+
literal|".KDlTtXchhZTGufMYmOYGS4HffxPSUrfmqCHXaI9wOGY"
operator|+
literal|".U0m_YmjN04DJvceFICbCVQ"
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
name|testEncryptDecryptAesWrapA128CBCHS256
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|specPlainText
init|=
literal|"Live long and prosper."
decl_stmt|;
name|JweHeaders
name|headers
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|Algorithm
operator|.
name|A128KW
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|Algorithm
operator|.
name|A128CBC_HS256
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|cekEncryptionKey
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|KEY_ENCRYPTION_KEY_A3
argument_list|)
decl_stmt|;
name|AesWrapKeyEncryptionAlgorithm
name|keyEncryption
init|=
operator|new
name|AesWrapKeyEncryptionAlgorithm
argument_list|(
name|cekEncryptionKey
argument_list|,
name|Algorithm
operator|.
name|A128KW
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|encryption
init|=
operator|new
name|AesCbcHmacJweEncryption
argument_list|(
name|headers
argument_list|,
name|CONTENT_ENCRYPTION_KEY_A3
argument_list|,
name|INIT_VECTOR_A3
argument_list|,
name|keyEncryption
argument_list|)
decl_stmt|;
name|String
name|jweContent
init|=
name|encryption
operator|.
name|encrypt
argument_list|(
name|specPlainText
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|JWE_OUTPUT_A3
argument_list|,
name|jweContent
argument_list|)
expr_stmt|;
name|AesWrapKeyDecryptionAlgorithm
name|keyDecryption
init|=
operator|new
name|AesWrapKeyDecryptionAlgorithm
argument_list|(
name|cekEncryptionKey
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|decryption
init|=
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
name|keyDecryption
argument_list|)
decl_stmt|;
name|String
name|decryptedText
init|=
name|decryption
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
name|specPlainText
argument_list|,
name|decryptedText
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptDecryptAesGcmWrapA128CBCHS256
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|specPlainText
init|=
literal|"Live long and prosper."
decl_stmt|;
name|JweHeaders
name|headers
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|A128GCMKW_ALGO
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|Algorithm
operator|.
name|A128CBC_HS256
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|cekEncryptionKey
init|=
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|KEY_ENCRYPTION_KEY_A3
argument_list|)
decl_stmt|;
name|AesGcmWrapKeyEncryptionAlgorithm
name|keyEncryption
init|=
operator|new
name|AesGcmWrapKeyEncryptionAlgorithm
argument_list|(
name|cekEncryptionKey
argument_list|,
name|JoseConstants
operator|.
name|A128GCMKW_ALGO
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|encryption
init|=
operator|new
name|AesCbcHmacJweEncryption
argument_list|(
name|headers
argument_list|,
name|CONTENT_ENCRYPTION_KEY_A3
argument_list|,
name|INIT_VECTOR_A3
argument_list|,
name|keyEncryption
argument_list|)
decl_stmt|;
name|String
name|jweContent
init|=
name|encryption
operator|.
name|encrypt
argument_list|(
name|specPlainText
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|AesGcmWrapKeyDecryptionAlgorithm
name|keyDecryption
init|=
operator|new
name|AesGcmWrapKeyDecryptionAlgorithm
argument_list|(
name|cekEncryptionKey
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|decryption
init|=
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
name|keyDecryption
argument_list|)
decl_stmt|;
name|String
name|decryptedText
init|=
name|decryption
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
name|specPlainText
argument_list|,
name|decryptedText
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
argument_list|,
literal|true
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
argument_list|,
literal|false
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
name|RSA_MODULUS_ENCODED_A1
argument_list|,
name|RSA_PUBLIC_EXPONENT_ENCODED_A1
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
name|KeyEncryptionAlgorithm
name|keyEncryptionAlgo
init|=
operator|new
name|RSAOaepKeyEncryptionAlgorithm
argument_list|(
name|publicKey
argument_list|,
name|Algorithm
operator|.
name|RSA_OAEP
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
name|ContentEncryptionAlgorithm
name|contentEncryptionAlgo
init|=
operator|new
name|AesGcmContentEncryptionAlgorithm
argument_list|(
name|key
operator|==
literal|null
condition|?
literal|null
else|:
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|,
name|INIT_VECTOR_A1
argument_list|,
name|jwtKeyName
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|encryptor
init|=
operator|new
name|WrappedKeyJweEncryption
argument_list|(
name|keyEncryptionAlgo
argument_list|,
name|contentEncryptionAlgo
argument_list|)
decl_stmt|;
return|return
name|encryptor
operator|.
name|encrypt
argument_list|(
name|content
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
literal|null
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
name|DirectKeyJweEncryption
name|encryptor
init|=
operator|new
name|DirectKeyJweEncryption
argument_list|(
operator|new
name|AesGcmContentEncryptionAlgorithm
argument_list|(
name|key
argument_list|,
name|INIT_VECTOR_A1
argument_list|,
name|JoseConstants
operator|.
name|A128GCM_ALGO
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|encryptor
operator|.
name|encrypt
argument_list|(
name|content
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
literal|null
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
parameter_list|,
name|boolean
name|unwrap
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
name|RSA_MODULUS_ENCODED_A1
argument_list|,
name|RSA_PRIVATE_EXPONENT_ENCODED_A1
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|decryptor
init|=
operator|new
name|WrappedKeyJweDecryption
argument_list|(
operator|new
name|RSAOaepKeyDecryptionAlgorithm
argument_list|(
name|privateKey
argument_list|)
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|()
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
name|DirectKeyJweDecryption
name|decryptor
init|=
operator|new
name|DirectKeyJweDecryption
argument_list|(
name|key
argument_list|,
operator|new
name|AesGcmContentDecryptionAlgorithm
argument_list|()
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
name|CONTENT_ENCRYPTION_KEY_A1
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

