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
name|jwk
package|;
end_package

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
name|common
operator|.
name|JoseUtils
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JwkUtilsTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSA_KEY
init|=
literal|"{"
operator|+
literal|"\"kty\": \"RSA\","
operator|+
literal|"\"n\": \"0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx4cbbfAAt"
operator|+
literal|"VT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMstn6"
operator|+
literal|"4tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf0h4QyQ5v-65YGjQR0_FD"
operator|+
literal|"W2QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n9"
operator|+
literal|"1CbOpbISD08qNLyrdkt-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINH"
operator|+
literal|"aQ-G_xBniIqbw0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw\","
operator|+
literal|"\"e\": \"AQAB\","
operator|+
literal|"\"alg\": \"RS256\","
operator|+
literal|"\"kid\": \"2011-04-29\""
operator|+
literal|"}"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_256_KEY
init|=
literal|"{"
operator|+
literal|"\"kty\": \"EC\","
operator|+
literal|"\"x\": \"CEuRLUISufhcjrj-32N0Bvl3KPMiHH9iSw4ohN9jxrA\","
operator|+
literal|"\"y\": \"EldWz_iXSK3l_S7n4w_t3baxos7o9yqX0IjzG959vHc\","
operator|+
literal|"\"crv\": \"P-256\""
operator|+
literal|"}"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_384_KEY
init|=
literal|"{"
operator|+
literal|"\"kty\": \"EC\","
operator|+
literal|"\"x\": \"2jCG5DmKUql9YPn7F2C-0ljWEbj8O8-vn5Ih1k7Wzb-y3NpBLiG1BiRa392b1kcQ\","
operator|+
literal|"\"y\": \"7Ragi9rT-5tSzaMbJlH_EIJl6rNFfj4V4RyFM5U2z4j1hesX5JXa8dWOsE-5wPIl\","
operator|+
literal|"\"crv\": \"P-384\""
operator|+
literal|"}"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_521_KEY
init|=
literal|"{"
operator|+
literal|"\"kty\": \"EC\","
operator|+
literal|"\"x\": \"Aeq3uMrb3iCQEt0PzSeZMmrmYhsKP5DM1oMP6LQzTFQY9-F3Ab45xiK4AJxltXEI-87g3gRwId88hTyHgq180JDt\","
operator|+
literal|"\"y\": \"ARA0lIlrZMEzaXyXE4hjEkc50y_JON3qL7HSae9VuWpOv_2kit8p3pyJBiRb468_U5ztLT7FvDvtimyS42trhDTu\","
operator|+
literal|"\"crv\": \"P-521\""
operator|+
literal|"}"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OCTET_KEY_1
init|=
literal|"{"
operator|+
literal|"\"kty\": \"oct\","
operator|+
literal|"\"k\": \"ZW8Eg8TiwoT2YamLJfC2leYpLgLmUAh_PcMHqRzBnMg\""
operator|+
literal|"}"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OCTET_KEY_2
init|=
literal|"{"
operator|+
literal|"\"kty\": \"oct\","
operator|+
literal|"\"k\": \"NGbwp1rC4n85A1SaNxoHow\""
operator|+
literal|"}"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testRsaKeyModulus
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonWebKey
name|jwk
init|=
name|JwkUtils
operator|.
name|readJwkKey
argument_list|(
name|RSA_KEY
argument_list|)
decl_stmt|;
name|String
name|modulus
init|=
name|jwk
operator|.
name|getStringProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|256
argument_list|,
name|JoseUtils
operator|.
name|decode
argument_list|(
name|modulus
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|RSAPublicKey
name|pk
init|=
name|JwkUtils
operator|.
name|toRSAPublicKey
argument_list|(
name|jwk
argument_list|)
decl_stmt|;
name|JsonWebKey
name|jwk2
init|=
name|JwkUtils
operator|.
name|fromRSAPublicKey
argument_list|(
name|pk
argument_list|,
name|jwk
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|modulus2
init|=
name|jwk2
operator|.
name|getStringProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|256
argument_list|,
name|JoseUtils
operator|.
name|decode
argument_list|(
name|modulus2
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|modulus2
argument_list|,
name|modulus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRsaKeyThumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|RSA_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"NzbLsXh8uDCcd-6MNwXF4W_7noWXFZAfHkxZsRGC9Xs"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctetKey1Thumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|OCTET_KEY_1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"7WWD36NF4WCpPaYtK47mM4o0a5CCeOt01JXSuMayv5g"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOctetKey2Thumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|OCTET_KEY_2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"5_qb56G0OJDw-lb5mkDaWS4MwuY0fatkn9LkNqUHqMk"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEc256KeyThumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|EC_256_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"j4UYwo9wrtllSHaoLDJNh7MhVCL8t0t8cGPPzChpYDs"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEc384KeyThumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|EC_384_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"vZtaWIw-zw95JNzzURg1YB7mWNLlm44YZDZzhrPNetM"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEc521KeyThumbprint
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|thumbprint
init|=
name|JwkUtils
operator|.
name|getThumbprint
argument_list|(
name|EC_521_KEY
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"rz4Ohmpxg-UOWIWqWKHlOe0bHSjNUFlHW5vwG_M7qYg"
argument_list|,
name|thumbprint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

