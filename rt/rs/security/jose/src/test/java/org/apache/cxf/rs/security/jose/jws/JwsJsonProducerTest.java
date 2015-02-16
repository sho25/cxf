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
name|JoseHeaders
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
name|JwsJsonProducerTest
extends|extends
name|Assert
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ENCODED_MAC_KEY_1
init|=
literal|"AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75"
operator|+
literal|"aKtMN3Yj0iPS4hcgUuTwjAzZr1Z9CAow"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCODED_MAC_KEY_2
init|=
literal|"09Y_RK7l5rAY9QY7EblYQNuYbu9cy1j7ovCbkeIyAKN8LIeRL-3H8g"
operator|+
literal|"c8kZSYzAQ1uTRC_egZ_8cgZSZa9T5nmQ"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNSIGNED_PLAIN_JSON_DOCUMENT
init|=
literal|"{"
operator|+
literal|" \"from\": \"user\","
operator|+
literal|" \"to\": \"developer\","
operator|+
literal|" \"msg\": \"good job!\" "
operator|+
literal|"}"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNSIGNED_PLAIN_JSON_DOCUMENT_AS_B64URL
init|=
literal|"eyAiZnJvbSI6ICJ1c2VyIiwgInRvIjogI"
operator|+
literal|"mRldmVsb3BlciIsICJtc2ciOiAiZ29vZCBqb2IhIiB9"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNED_JWS_JSON_DOCUMENT
init|=
literal|"{"
operator|+
literal|"\"payload\":\""
operator|+
name|UNSIGNED_PLAIN_JSON_DOCUMENT_AS_B64URL
operator|+
literal|"\",\"signatures\":[{\"protected\":\"eyJhbGciOiJIUzI1NiJ9\",\"signature\":"
operator|+
literal|"\"NNksREOsFCI1nUQEqzCe6XZFa-bRAge2XXMMAU2Jj2I\"}]}"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNED_JWS_JSON_FLAT_DOCUMENT
init|=
literal|"{"
operator|+
literal|"\"payload\":\""
operator|+
name|UNSIGNED_PLAIN_JSON_DOCUMENT_AS_B64URL
operator|+
literal|"\",\"protected\":\"eyJhbGciOiJIUzI1NiJ9\",\"signature\":"
operator|+
literal|"\"NNksREOsFCI1nUQEqzCe6XZFa-bRAge2XXMMAU2Jj2I\"}"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DUAL_SIGNED_JWS_JSON_DOCUMENT
init|=
literal|"{"
operator|+
literal|"\"payload\":\""
operator|+
name|UNSIGNED_PLAIN_JSON_DOCUMENT_AS_B64URL
operator|+
literal|"\",\"signatures\":[{\"protected\":\"eyJhbGciOiJIUzI1NiJ9\","
operator|+
literal|"\"signature\":\"NNksREOsFCI1nUQEqzCe6XZFa-bRAge2XXMMAU2Jj2I\"},"
operator|+
literal|"{\"protected\":\"eyJhbGciOiJIUzI1NiJ9\","
operator|+
literal|"\"signature\":\"KY2r_Gubar7G86fVyrA7I2-69KA7faKDmebfCCmibdI\"}]}"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testSignPlainJsonDocumentPayloadConstruction
parameter_list|()
block|{
name|JwsJsonProducer
name|producer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|UNSIGNED_PLAIN_JSON_DOCUMENT
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|UNSIGNED_PLAIN_JSON_DOCUMENT_AS_B64URL
argument_list|,
name|producer
operator|.
name|getUnsignedEncodedPayload
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignWithProtectedHeaderOnly
parameter_list|()
block|{
name|JwsJsonProducer
name|producer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|UNSIGNED_PLAIN_JSON_DOCUMENT
argument_list|)
decl_stmt|;
name|JoseHeaders
name|headerEntries
init|=
operator|new
name|JoseHeaders
argument_list|()
decl_stmt|;
name|headerEntries
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
expr_stmt|;
name|producer
operator|.
name|signWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY_1
argument_list|,
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
argument_list|,
name|headerEntries
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SIGNED_JWS_JSON_DOCUMENT
argument_list|,
name|producer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignWithProtectedHeaderOnlyFlat
parameter_list|()
block|{
name|JwsJsonProducer
name|producer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|UNSIGNED_PLAIN_JSON_DOCUMENT
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|JoseHeaders
name|headerEntries
init|=
operator|new
name|JoseHeaders
argument_list|()
decl_stmt|;
name|headerEntries
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
expr_stmt|;
name|producer
operator|.
name|signWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY_1
argument_list|,
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
argument_list|,
name|headerEntries
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SIGNED_JWS_JSON_FLAT_DOCUMENT
argument_list|,
name|producer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDualSignWithProtectedHeaderOnly
parameter_list|()
block|{
name|JwsJsonProducer
name|producer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|UNSIGNED_PLAIN_JSON_DOCUMENT
argument_list|)
decl_stmt|;
name|JoseHeaders
name|headerEntries
init|=
operator|new
name|JoseHeaders
argument_list|()
decl_stmt|;
name|headerEntries
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
expr_stmt|;
name|producer
operator|.
name|signWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY_1
argument_list|,
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
argument_list|,
name|headerEntries
argument_list|)
expr_stmt|;
name|producer
operator|.
name|signWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY_2
argument_list|,
name|JoseConstants
operator|.
name|HMAC_SHA_256_ALGO
argument_list|)
argument_list|,
name|headerEntries
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DUAL_SIGNED_JWS_JSON_DOCUMENT
argument_list|,
name|producer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

