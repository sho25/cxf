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
name|jwa
package|;
end_package

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
name|support
operator|.
name|Serialization
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
specifier|abstract
class|class
name|JwaEncryptRfcConformanceTest
extends|extends
name|AbstractEncryptTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testOctA128GcmJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"OCTET"
argument_list|,
literal|"A128KW"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|,
name|PLAIN_TEXT
argument_list|,
name|JWKS_PRIVATE_KEYS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctA128GcmJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"OCTET"
argument_list|,
literal|"A128KW"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|,
name|PLAIN_TEXT
argument_list|,
name|JWKS_PRIVATE_KEYS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctA128GcmJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"OCTET"
argument_list|,
literal|"A128KW"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|,
name|PLAIN_TEXT
argument_list|,
name|JWKS_PRIVATE_KEYS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsaOaepA128GcmJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA-OAEP"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsaOaepA128GcmJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA-OAEP"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsaOaepA128GcmJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA-OAEP"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhDirectA128GcmJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhDirectA128GcmJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhDirectA128GcmJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhA128KwA128GcmJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES+A128KW"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhA128KwA128GcmJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES+A128KW"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhA128KwA128GcmJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES+A128KW"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhA128KwA128CbcJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES+A128KW"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhA128KwA128CbcJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES+A128KW"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEcdhA128KwA128CbcJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"EC"
argument_list|,
literal|"ECDH-ES+A128KW"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsa15A128GcmCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA1_5"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsa15A128GcmJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA1_5"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsa15A128GcmJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA1_5"
argument_list|,
literal|"A128GCM"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsa15A128CbcJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA1_5"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsa15A128CbcJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA1_5"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsa15A128CbcJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"RSA"
argument_list|,
literal|"RSA1_5"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctA128CbcJweCompact
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"OCTET"
argument_list|,
literal|"A128KW"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|COMPACT
argument_list|,
name|PLAIN_TEXT
argument_list|,
name|JWKS_PRIVATE_KEYS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctA128CbcJweJsonFlattened
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"OCTET"
argument_list|,
literal|"A128KW"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|FLATTENED
argument_list|,
name|PLAIN_TEXT
argument_list|,
name|JWKS_PRIVATE_KEYS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctA128CbcJweJson
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"OCTET"
argument_list|,
literal|"A128KW"
argument_list|,
literal|"A128CBC-HS256"
argument_list|,
name|Serialization
operator|.
name|JSON
argument_list|,
name|PLAIN_TEXT
argument_list|,
name|JWKS_PRIVATE_KEYS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

