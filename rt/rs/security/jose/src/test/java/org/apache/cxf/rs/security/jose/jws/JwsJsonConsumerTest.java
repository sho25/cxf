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
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|SignatureAlgorithm
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
name|jwk
operator|.
name|JsonWebKey
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
name|jwk
operator|.
name|JsonWebKeys
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
name|jwk
operator|.
name|JwkUtils
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
name|JwsJsonConsumerTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DUAL_SIGNED_DOCUMENT
init|=
literal|"{\"payload\":\n"
operator|+
literal|"\t\"eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ\",\n"
operator|+
literal|"\t\"signatures\":[\n"
operator|+
literal|"\t\t\t{\"protected\":\"eyJhbGciOiJSUzI1NiJ9\",\n"
operator|+
literal|"\t\t\t \"header\":\n"
operator|+
literal|"\t\t\t\t{\"kid\":\"2010-12-29\"},\n"
operator|+
literal|"\t\t\t \"signature\":\n"
operator|+
literal|"\t\t\t\t\"cC4hiUPoj9Eetdgtv3hF80EGrhuB__dzERat0XF9g2VtQgr9PJbu3XOiZj5RZmh7AAuHIm4Bh-0Qc_lF5YKt_O8W2Fp5"
operator|+
literal|"jujGbds9uJdbF9CUAr7t1dnZcAcQjbKBYNX4BAynRFdiuB--f_nZLgrnbyTyWzO75vRK5h6xBArLIARNPvkSjtQBMHlb"
operator|+
literal|"1L07Qe7K0GarZRmB_eSN9383LcOLn6_dO--xi12jzDwusC-eOkHWEsqtFZESc6BfI7noOPqvhJ1phCnvWh6IeYI2w9QOY"
operator|+
literal|"EUipUTI8np6LbgGY9Fs98rqVt5AXLIhWkWywlVmtVrBp0igcN_IoypGlUPQGe77Rw\"},\n"
operator|+
literal|"\t\t\t{\"protected\":\"eyJhbGciOiJFUzI1NiJ9\",\n"
operator|+
literal|"\t\t\t \"header\":\n"
operator|+
literal|"\t\t\t\t{\"kid\":\"e9bc097a-ce51-4036-9562-d2ade882db0d\"},\n"
operator|+
literal|"\t\t\t \"signature\":\n"
operator|+
literal|"\t\t\t\t\"DtEhU3ljbEg8L38VWAfUAqOyKAM6-Xx-F4GawxaepmXFCgfTjDxw5djxLa8ISlSApmWQxfKTUJqPP3-Kg6NU1Q\"}]\n"
operator|+
literal|"}"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KID_OF_THE_FIRST_SIGNER
init|=
literal|"2010-12-29"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KID_OF_THE_SECOND_SIGNER
init|=
literal|"e9bc097a-ce51-4036-9562-d2ade882db0d"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testVerifySignedWithProtectedHeaderOnlyUnencodedPayload
parameter_list|()
block|{
name|JwsJsonConsumer
name|consumer
init|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|JwsJsonProducerTest
operator|.
name|SIGNED_JWS_JSON_FLAT_UNENCODED_DOCUMENT
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|JwsJsonProducerTest
operator|.
name|UNSIGNED_PLAIN_DOCUMENT
argument_list|,
name|consumer
operator|.
name|getJwsPayload
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JwsJsonProducerTest
operator|.
name|UNSIGNED_PLAIN_DOCUMENT
argument_list|,
name|consumer
operator|.
name|getDecodedJwsPayload
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|consumer
operator|.
name|verifySignatureWith
argument_list|(
operator|new
name|HmacJwsSignatureVerifier
argument_list|(
name|JwsJsonProducerTest
operator|.
name|ENCODED_MAC_KEY_1
argument_list|,
name|SignatureAlgorithm
operator|.
name|HS256
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testVerifyDualSignedDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|JwsJsonConsumer
name|consumer
init|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|DUAL_SIGNED_DOCUMENT
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"jwkPublicJsonConsumerSet.txt"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|JwsJsonSignatureEntry
argument_list|>
name|sigEntries
init|=
name|consumer
operator|.
name|getSignatureEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|sigEntries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// 1st signature
name|String
name|firstKid
init|=
operator|(
name|String
operator|)
name|sigEntries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getKeyId
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|KID_OF_THE_FIRST_SIGNER
argument_list|,
name|firstKid
argument_list|)
expr_stmt|;
name|JsonWebKey
name|rsaKey
init|=
name|jwks
operator|.
name|getKey
argument_list|(
name|firstKid
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rsaKey
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sigEntries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|verifySignatureWith
argument_list|(
name|rsaKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// 2nd signature
name|String
name|secondKid
init|=
operator|(
name|String
operator|)
name|sigEntries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getKeyId
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|KID_OF_THE_SECOND_SIGNER
argument_list|,
name|secondKid
argument_list|)
expr_stmt|;
name|JsonWebKey
name|ecKey
init|=
name|jwks
operator|.
name|getKey
argument_list|(
name|secondKid
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ecKey
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sigEntries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|verifySignatureWith
argument_list|(
name|ecKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JsonWebKeys
name|readKeySet
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|JwsJsonConsumerTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
return|return
name|JwkUtils
operator|.
name|readJwkSet
argument_list|(
name|is
argument_list|)
return|;
block|}
block|}
end_class

end_unit

