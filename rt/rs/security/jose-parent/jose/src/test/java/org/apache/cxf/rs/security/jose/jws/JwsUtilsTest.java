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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|Bus
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
name|BusFactory
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|KeyType
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
name|assertFalse
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
name|assertNotNull
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
name|assertNull
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
name|assertTrue
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
name|JwsUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSignatureAlgorithm
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|AlgorithmUtils
operator|.
name|isRsaSign
argument_list|(
name|SignatureAlgorithm
operator|.
name|RS256
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|AlgorithmUtils
operator|.
name|isRsaSign
argument_list|(
name|SignatureAlgorithm
operator|.
name|NONE
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|AlgorithmUtils
operator|.
name|RSA_SHA_SIGN_SET
operator|.
name|add
argument_list|(
name|SignatureAlgorithm
operator|.
name|NONE
operator|.
name|getJwaName
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on trying to modify the algorithm lists"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSignatureProviderFromJKS
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|,
literal|"org/apache/cxf/rs/security/jose/jws/alice.jks"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_PSWD
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|JwsHeaders
name|headers
init|=
operator|new
name|JwsHeaders
argument_list|()
decl_stmt|;
name|JwsSignatureProvider
name|jws
init|=
name|JwsUtils
operator|.
name|loadSignatureProvider
argument_list|(
name|createMessage
argument_list|()
argument_list|,
name|p
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jws
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|headers
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSignatureVerifierFromJKS
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|,
literal|"org/apache/cxf/rs/security/jose/jws/alice.jks"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|JwsSignatureVerifier
name|jws
init|=
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
name|createMessage
argument_list|()
argument_list|,
name|p
argument_list|,
operator|new
name|JwsHeaders
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jws
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadVerificationKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|,
literal|"org/apache/cxf/rs/security/jose/jws/alice.jks"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|JsonWebKeys
name|keySet
init|=
name|JwsUtils
operator|.
name|loadPublicVerificationKeys
argument_list|(
name|createMessage
argument_list|()
argument_list|,
name|p
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keySet
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|keySet
operator|.
name|getRsaKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|KeyType
operator|.
name|RSA
argument_list|,
name|key
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|key
operator|.
name|getKeyProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PUBLIC_EXP
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|key
operator|.
name|getKeyProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|key
operator|.
name|getKeyProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PRIVATE_EXP
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|key
operator|.
name|getX509Chain
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadVerificationKeyWithCert
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|,
literal|"org/apache/cxf/rs/security/jose/jws/alice.jks"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|p
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_SIGNATURE_INCLUDE_CERT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|JsonWebKeys
name|keySet
init|=
name|JwsUtils
operator|.
name|loadPublicVerificationKeys
argument_list|(
name|createMessage
argument_list|()
argument_list|,
name|p
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keySet
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|keySet
operator|.
name|getRsaKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|KeyType
operator|.
name|RSA
argument_list|,
name|key
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|key
operator|.
name|getKeyProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PUBLIC_EXP
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|key
operator|.
name|getKeyProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|key
operator|.
name|getKeyProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PRIVATE_EXP
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|chain
init|=
name|key
operator|.
name|getX509Chain
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|chain
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|createMessage
parameter_list|()
block|{
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|Exchange
name|e
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|e
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|JoseConstants
operator|.
name|RSSEC_SIGNATURE_INCLUDE_KEY_ID
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|e
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
block|}
end_class

end_unit

