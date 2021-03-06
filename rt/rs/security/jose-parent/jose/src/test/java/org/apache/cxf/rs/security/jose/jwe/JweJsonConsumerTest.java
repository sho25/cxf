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
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|JweJsonConsumerTest
block|{
specifier|static
specifier|final
name|String
name|SINGLE_RECIPIENT_ALL_HEADERS_AAD_MODIFIED_OUTPUT
init|=
literal|"{"
operator|+
literal|"\"protected\":\"eyJlbmMiOiJBMTI4R0NNIn0\","
operator|+
literal|"\"unprotected\":{\"jku\":\"https://server.example.com/keys.jwks\"},"
operator|+
literal|"\"recipients\":"
operator|+
literal|"["
operator|+
literal|"{"
operator|+
literal|"\"header\":{\"alg\":\"A128KW\"},"
operator|+
literal|"\"encrypted_key\":\"b3-M9_CRgT3wEBhhXlpb-BoY7vtA4W_N\""
operator|+
literal|"}"
operator|+
literal|"],"
operator|+
literal|"\"aad\":\""
operator|+
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|JweJsonProducerTest
operator|.
name|EXTRA_AAD_SOURCE
operator|+
literal|"."
argument_list|)
operator|+
literal|"\","
operator|+
literal|"\"iv\":\"48V1_ALb6US04U3b\","
operator|+
literal|"\"ciphertext\":\"KTuJBMk9QG59xPB-c_YLM5-J7VG40_eMPvyHDD7eB-WHj_34YiWgpBOydTBm4RW0zUCJZ09xqorhWJME-DcQ\","
operator|+
literal|"\"tag\":\"oVUQGS9608D-INq61-vOaA\""
operator|+
literal|"}"
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
name|Cipher
operator|.
name|getInstance
argument_list|(
name|AlgorithmUtils
operator|.
name|AES_GCM_ALGO_JAVA
argument_list|)
expr_stmt|;
name|Cipher
operator|.
name|getInstance
argument_list|(
name|AlgorithmUtils
operator|.
name|AES_CBC_ALGO_JAVA
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
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
name|PROVIDER_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientGcm
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|doTestSingleRecipient
argument_list|(
name|text
argument_list|,
name|JweJsonProducerTest
operator|.
name|SINGLE_RECIPIENT_OUTPUT
argument_list|,
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|,
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientFlatGcm
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|doTestSingleRecipient
argument_list|(
name|text
argument_list|,
name|JweJsonProducerTest
operator|.
name|SINGLE_RECIPIENT_FLAT_OUTPUT
argument_list|,
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|,
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientDirectGcm
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|doTestSingleRecipient
argument_list|(
name|text
argument_list|,
name|JweJsonProducerTest
operator|.
name|SINGLE_RECIPIENT_DIRECT_OUTPUT
argument_list|,
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|,
literal|null
argument_list|,
name|JweJsonProducerTest
operator|.
name|CEK_BYTES
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientDirectA128CBCHS256
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|text
init|=
literal|"Live long and prosper."
decl_stmt|;
name|doTestSingleRecipient
argument_list|(
name|text
argument_list|,
name|JweJsonProducerTest
operator|.
name|SINGLE_RECIPIENT_A128CBCHS256_DIRECT_OUTPUT
argument_list|,
name|ContentAlgorithm
operator|.
name|A128CBC_HS256
argument_list|,
literal|null
argument_list|,
name|JweCompactReaderWriterTest
operator|.
name|CONTENT_ENCRYPTION_KEY_A3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientA128CBCHS256
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|text
init|=
literal|"Live long and prosper."
decl_stmt|;
name|doTestSingleRecipient
argument_list|(
name|text
argument_list|,
name|JweJsonProducerTest
operator|.
name|SINGLE_RECIPIENT_A128CBCHS256_OUTPUT
argument_list|,
name|ContentAlgorithm
operator|.
name|A128CBC_HS256
argument_list|,
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|JweCompactReaderWriterTest
operator|.
name|KEY_ENCRYPTION_KEY_A3
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientAllTypeOfHeadersAndAad
parameter_list|()
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|SecretKey
name|wrapperKey
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES1
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|jwe
init|=
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|wrapperKey
argument_list|,
name|KeyAlgorithm
operator|.
name|A128KW
argument_list|,
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|)
decl_stmt|;
name|JweJsonConsumer
name|consumer
init|=
operator|new
name|JweJsonConsumer
argument_list|(
name|JweJsonProducerTest
operator|.
name|SINGLE_RECIPIENT_ALL_HEADERS_AAD_OUTPUT
argument_list|)
decl_stmt|;
name|JweDecryptionOutput
name|out
init|=
name|consumer
operator|.
name|decryptWith
argument_list|(
name|jwe
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|out
operator|.
name|getContentText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JweJsonProducerTest
operator|.
name|EXTRA_AAD_SOURCE
argument_list|,
name|consumer
operator|.
name|getAadText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleRecipients
parameter_list|()
block|{
name|doTestMultipleRecipients
argument_list|(
name|JweJsonProducerTest
operator|.
name|MULTIPLE_RECIPIENTS_OUTPUT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleRecipientsAutogeneratedCek
parameter_list|()
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|SecretKey
name|wrapperKey1
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES1
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|SecretKey
name|wrapperKey2
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES2
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|JweHeaders
name|protectedHeaders
init|=
operator|new
name|JweHeaders
argument_list|(
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|)
decl_stmt|;
name|JweHeaders
name|sharedUnprotectedHeaders
init|=
operator|new
name|JweHeaders
argument_list|()
decl_stmt|;
name|sharedUnprotectedHeaders
operator|.
name|setJsonWebKeysUrl
argument_list|(
literal|"https://server.example.com/keys.jwks"
argument_list|)
expr_stmt|;
name|sharedUnprotectedHeaders
operator|.
name|setKeyEncryptionAlgorithm
argument_list|(
name|KeyAlgorithm
operator|.
name|A128KW
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JweEncryptionProvider
argument_list|>
name|jweProviders
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|KeyEncryptionProvider
name|keyEncryption1
init|=
name|JweUtils
operator|.
name|getSecretKeyEncryptionAlgorithm
argument_list|(
name|wrapperKey1
argument_list|,
name|KeyAlgorithm
operator|.
name|A128KW
argument_list|)
decl_stmt|;
name|ContentEncryptionProvider
name|contentEncryption
init|=
operator|new
name|AesGcmContentEncryptionAlgorithm
argument_list|(
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|jwe1
init|=
operator|new
name|JweEncryption
argument_list|(
name|keyEncryption1
argument_list|,
name|contentEncryption
argument_list|)
decl_stmt|;
name|KeyEncryptionProvider
name|keyEncryption2
init|=
name|JweUtils
operator|.
name|getSecretKeyEncryptionAlgorithm
argument_list|(
name|wrapperKey2
argument_list|,
name|KeyAlgorithm
operator|.
name|A128KW
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|jwe2
init|=
operator|new
name|JweEncryption
argument_list|(
name|keyEncryption2
argument_list|,
name|contentEncryption
argument_list|)
decl_stmt|;
name|jweProviders
operator|.
name|add
argument_list|(
name|jwe1
argument_list|)
expr_stmt|;
name|jweProviders
operator|.
name|add
argument_list|(
name|jwe2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JweHeaders
argument_list|>
name|perRecipientHeaders
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|perRecipientHeaders
operator|.
name|add
argument_list|(
operator|new
name|JweHeaders
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|perRecipientHeaders
operator|.
name|add
argument_list|(
operator|new
name|JweHeaders
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
name|JweJsonProducer
name|p
init|=
operator|new
name|JweJsonProducer
argument_list|(
name|protectedHeaders
argument_list|,
name|sharedUnprotectedHeaders
argument_list|,
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|text
argument_list|)
argument_list|,
name|StringUtils
operator|.
name|toBytesUTF8
argument_list|(
name|JweJsonProducerTest
operator|.
name|EXTRA_AAD_SOURCE
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|jweJson
init|=
name|p
operator|.
name|encryptWith
argument_list|(
name|jweProviders
argument_list|,
name|perRecipientHeaders
argument_list|)
decl_stmt|;
name|doTestMultipleRecipients
argument_list|(
name|jweJson
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestMultipleRecipients
parameter_list|(
name|String
name|jweJson
parameter_list|)
block|{
specifier|final
name|String
name|text
init|=
literal|"The true sign of intelligence is not knowledge but imagination."
decl_stmt|;
name|SecretKey
name|wrapperKey1
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES1
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|SecretKey
name|wrapperKey2
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES2
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|JweJsonConsumer
name|consumer
init|=
operator|new
name|JweJsonConsumer
argument_list|(
name|jweJson
argument_list|)
decl_stmt|;
name|KeyAlgorithm
name|keyAlgo
init|=
name|consumer
operator|.
name|getSharedUnprotectedHeader
argument_list|()
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
decl_stmt|;
name|ContentAlgorithm
name|ctAlgo
init|=
name|consumer
operator|.
name|getProtectedHeader
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
decl_stmt|;
comment|// Recipient 1
name|JweDecryptionProvider
name|jwe1
init|=
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|wrapperKey1
argument_list|,
name|keyAlgo
argument_list|,
name|ctAlgo
argument_list|)
decl_stmt|;
name|JweDecryptionOutput
name|out1
init|=
name|consumer
operator|.
name|decryptWith
argument_list|(
name|jwe1
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"kid"
argument_list|,
literal|"key1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|out1
operator|.
name|getContentText
argument_list|()
argument_list|)
expr_stmt|;
comment|// Recipient 2
name|JweDecryptionProvider
name|jwe2
init|=
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|wrapperKey2
argument_list|,
name|keyAlgo
argument_list|,
name|ctAlgo
argument_list|)
decl_stmt|;
name|JweDecryptionOutput
name|out2
init|=
name|consumer
operator|.
name|decryptWith
argument_list|(
name|jwe2
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"kid"
argument_list|,
literal|"key2"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|out2
operator|.
name|getContentText
argument_list|()
argument_list|)
expr_stmt|;
comment|// Extra AAD
name|assertEquals
argument_list|(
name|JweJsonProducerTest
operator|.
name|EXTRA_AAD_SOURCE
argument_list|,
name|consumer
operator|.
name|getAadText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingleRecipientAllTypeOfHeadersAndAadModified
parameter_list|()
block|{
name|SecretKey
name|wrapperKey
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|JweJsonProducerTest
operator|.
name|WRAPPER_BYTES1
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|jwe
init|=
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|wrapperKey
argument_list|,
name|KeyAlgorithm
operator|.
name|A128KW
argument_list|,
name|ContentAlgorithm
operator|.
name|A128GCM
argument_list|)
decl_stmt|;
name|JweJsonConsumer
name|consumer
init|=
operator|new
name|JweJsonConsumer
argument_list|(
name|SINGLE_RECIPIENT_ALL_HEADERS_AAD_MODIFIED_OUTPUT
argument_list|)
decl_stmt|;
try|try
block|{
name|consumer
operator|.
name|decryptWith
argument_list|(
name|jwe
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AAD check has passed unexpectedly"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|void
name|doTestSingleRecipient
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|input
parameter_list|,
name|ContentAlgorithm
name|contentEncryptionAlgo
parameter_list|,
specifier|final
name|byte
index|[]
name|wrapperKeyBytes
parameter_list|,
specifier|final
name|byte
index|[]
name|cek
parameter_list|)
throws|throws
name|Exception
block|{
name|JweDecryptionProvider
name|jwe
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wrapperKeyBytes
operator|!=
literal|null
condition|)
block|{
name|SecretKey
name|wrapperKey
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|wrapperKeyBytes
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|jwe
operator|=
name|JweUtils
operator|.
name|createJweDecryptionProvider
argument_list|(
name|wrapperKey
argument_list|,
name|KeyAlgorithm
operator|.
name|A128KW
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SecretKey
name|cekKey
init|=
name|CryptoUtils
operator|.
name|createSecretKeySpec
argument_list|(
name|cek
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|jwe
operator|=
name|JweUtils
operator|.
name|getDirectKeyJweDecryption
argument_list|(
name|cekKey
argument_list|,
name|contentEncryptionAlgo
argument_list|)
expr_stmt|;
block|}
name|JweJsonConsumer
name|consumer
init|=
operator|new
name|JweJsonConsumer
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|JweDecryptionOutput
name|out
init|=
name|consumer
operator|.
name|decryptWith
argument_list|(
name|jwe
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|text
argument_list|,
name|out
operator|.
name|getContentText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

