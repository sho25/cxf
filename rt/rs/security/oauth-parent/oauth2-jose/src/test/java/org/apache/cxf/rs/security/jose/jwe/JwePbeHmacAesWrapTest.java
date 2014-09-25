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
name|After
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
name|Before
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
name|JwePbeHmacAesWrapTest
extends|extends
name|Assert
block|{
annotation|@
name|Before
specifier|public
name|void
name|registerBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
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
annotation|@
name|After
specifier|public
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
name|testEncryptDecryptPbesHmacAesWrapA128CBCHS256
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
name|PBES2_HS256_A128KW_ALGO
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
specifier|final
name|String
name|password
init|=
literal|"Thus from my lips, by yours, my sin is purged."
decl_stmt|;
name|KeyEncryptionAlgorithm
name|keyEncryption
init|=
operator|new
name|PbesHmacAesWrapKeyEncryptionAlgorithm
argument_list|(
name|password
argument_list|,
name|JoseConstants
operator|.
name|PBES2_HS256_A128KW_ALGO
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
name|PbesHmacAesWrapKeyDecryptionAlgorithm
name|keyDecryption
init|=
operator|new
name|PbesHmacAesWrapKeyDecryptionAlgorithm
argument_list|(
name|password
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
name|testEncryptDecryptPbesHmacAesWrapAesGcm
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
name|PBES2_HS256_A128KW_ALGO
argument_list|)
expr_stmt|;
name|headers
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
name|Algorithm
operator|.
name|A128GCM
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|password
init|=
literal|"Thus from my lips, by yours, my sin is purged."
decl_stmt|;
name|KeyEncryptionAlgorithm
name|keyEncryption
init|=
operator|new
name|PbesHmacAesWrapKeyEncryptionAlgorithm
argument_list|(
name|password
argument_list|,
name|JoseConstants
operator|.
name|PBES2_HS256_A128KW_ALGO
argument_list|)
decl_stmt|;
name|JweEncryptionProvider
name|encryption
init|=
operator|new
name|WrappedKeyJweEncryption
argument_list|(
name|headers
argument_list|,
name|keyEncryption
argument_list|,
operator|new
name|AesGcmContentEncryptionAlgorithm
argument_list|(
name|Algorithm
operator|.
name|A128GCM
operator|.
name|getJwtName
argument_list|()
argument_list|)
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
name|PbesHmacAesWrapKeyDecryptionAlgorithm
name|keyDecryption
init|=
operator|new
name|PbesHmacAesWrapKeyDecryptionAlgorithm
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|JweDecryptionProvider
name|decryption
init|=
operator|new
name|WrappedKeyJweDecryption
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
block|}
end_class

end_unit

