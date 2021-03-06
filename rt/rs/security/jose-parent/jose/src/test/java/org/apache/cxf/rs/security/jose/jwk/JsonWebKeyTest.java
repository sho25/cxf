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
name|io
operator|.
name|InputStream
import|;
end_import

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
name|cert
operator|.
name|X509Certificate
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwe
operator|.
name|JweCompactConsumer
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|JsonWebKeyTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RSA_MODULUS_VALUE
init|=
literal|"0vx7agoebGcQSuuPiLJXZptN9nndrQmbXEps2aiAFbWhM78LhWx4cbbfAAt"
operator|+
literal|"VT86zwu1RK7aPFFxuhDR1L6tSoc_BJECPebWKRXjBZCiFV4n3oknjhMstn64tZ_2W-5JsGY4Hc5n9yBXArwl93lqt7_RN5w6Cf"
operator|+
literal|"0h4QyQ5v-65YGjQR0_FDW2QvzqY368QQMicAtaSqzs8KJZgnYb9c7d0zgdAZHzu6qMQvRL5hajrn1n91CbOpbISD08qNLyrdkt"
operator|+
literal|"-bFTWhAI4vMQFh6WeZu0fM4lFd2NcRwr3XPksINHaQ-G_xBniIqbw0Ls1jF44-csFCur-kEgU8awapJzKnqDKgw"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PUBLIC_EXP_VALUE
init|=
literal|"AQAB"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PRIVATE_EXP_VALUE
init|=
literal|"X4cTteJY_gn4FYPsXB8rdXix5vwsg1FLN5E3EaG6RJoVH-HLLKD9M7d"
operator|+
literal|"x5oo7GURknchnrRweUkC7hT5fJLM0WbFAKNLWY2vv7B6NqXSzUvxT0_YSfqijwp3RTzlBaCxWp4doFk5N2o8Gy_nHNKroADIkJ4"
operator|+
literal|"6pRUohsXywbReAdYaMwFs9tv8d_cPVY3i07a3t8MN6TNwm0dSawm9v47UiCl3Sk5ZiG7xojPLu4sbg1U2jx4IBTNBznbJSzFHK66"
operator|+
literal|"jT8bgkuqsk0GjskDJk19Z4qwjwbsnn4j2WBii3RL-Us2lGVkY8fkFzme1z0HbIkfz0Y6mqnOYtqc0X4jfcKoAC8Q"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_FIRST_PRIME_FACTOR_VALUE
init|=
literal|"83i-7IvMGXoMXCskv73TKr8637FiO7Z27zv8oj6pbWUQyLPQ"
operator|+
literal|"BQxtPVnwD20R-60eTDmD2ujnMt5PoqMrm8RfmNhVWDtjjMmCMjOpSXicFHj7XOuVIYQyqVWlWEh6dN36GVZYk93N8Bc9vY41xy8B9"
operator|+
literal|"RzzOGVQzXvNEvn7O0nVbfs"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_SECOND_PRIME_FACTOR_VALUE
init|=
literal|"3dfOR9cuYq-0S-mkFLzgItgMEfFzB2q3hWehMuG0oCuqnb3"
operator|+
literal|"vobLyumqjVZQO1dIrdwgTnCdpYzBcOfW5r370AFXjiWft_NGEiovonizhKpo9VVS78TzFgxkIdrecRezsZ-1kYd_s1qDbxtkDEgfA"
operator|+
literal|"ITAG9LUnADun4vIcb6yelxk"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_FIRST_PRIME_CRT_VALUE
init|=
literal|"G4sPXkc6Ya9y8oJW9_ILj4xuppu0lzi_H7VTkS8xj5SdX3coE0o"
operator|+
literal|"imYwxIi2emTAue0UOa5dpgFGyBJ4c8tQ2VF402XRugKDTP8akYhFo5tAA77Qe_NmtuYZc3C3m3I24G2GvR5sSDxUyAN2zq8Lfn9EUm"
operator|+
literal|"s6rY3Ob8YeiKkTiBj0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_SECOND_PRIME_CRT_VALUE
init|=
literal|"s9lAH9fggBsoFR8Oac2R_E2gw282rT2kGOAhvIllETE1efrA6hu"
operator|+
literal|"UUvMfBcMpn8lqeW6vzznYY5SSQF7pMdC_agI3nG8Ibp1BUb0JUiraRNqUfLhcQb_d9GF4Dh7e74WbRsobRonujTYN1xCaP6TO61jvW"
operator|+
literal|"rX-L18txXw494Q_cgk"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_FIRST_CRT_COEFFICIENT_VALUE
init|=
literal|"GyM_p6JrXySiz1toFgKbWV-JdI3jQ4ypu9rbMWx3rQJBfm"
operator|+
literal|"t0FoYzgUIZEVFEcOqwemRN81zoDAaa-Bk0KWNGDjJHZDdDmFhW3AN7lI-puxk_mHZGJ11rxyR8O55XLSe3SPmRfKwZI6yU24ZxvQKF"
operator|+
literal|"YItdldUKGzO6Ia6zTKhAVRU"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_KID_VALUE
init|=
literal|"2011-04-29"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_CURVE_VALUE
init|=
name|JsonWebKey
operator|.
name|EC_CURVE_P256
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_X_COORDINATE_VALUE
init|=
literal|"MKBCTNIcKUSDii11ySs3526iDZ8AiTo7Tu6KPAqv7D4"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_Y_COORDINATE_VALUE
init|=
literal|"4Etl6SRW2YiLUrN5vfvVHuhp7x8PxltmWWlbbM4IFyM"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_PRIVATE_KEY_VALUE
init|=
literal|"870MB6gfuTJ4HtUnUvYMyJpr5eUZNP4Bk43bVdj3eAE"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_KID_VALUE
init|=
literal|"1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|AES_SECRET_VALUE
init|=
literal|"GawgguFyGrWKav7AX4VKUg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|AES_KID_VALUE
init|=
literal|"AesWrapKey"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HMAC_SECRET_VALUE
init|=
literal|"AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75aKtMN3"
operator|+
literal|"Yj0iPS4hcgUuTwjAzZr1Z9CAow"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HMAC_KID_VALUE
init|=
literal|"HMACKey"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testPublicSetAsList
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"jwkPublicSet.txt"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|jwks
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|ecKey
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
literal|6
argument_list|,
name|ecKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validatePublicEcKey
argument_list|(
name|ecKey
argument_list|)
expr_stmt|;
name|JsonWebKey
name|rsaKey
init|=
name|keys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|rsaKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validatePublicRsaKey
argument_list|(
name|rsaKey
argument_list|)
expr_stmt|;
name|JsonWebKey
name|rsaKeyCert
init|=
name|keys
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|rsaKeyCert
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|rsaKeyCert
operator|.
name|getX509Chain
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certs
init|=
name|JwkUtils
operator|.
name|toX509CertificateChain
argument_list|(
name|rsaKeyCert
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|certs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPublicSetAsMap
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"jwkPublicSet.txt"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|JsonWebKey
argument_list|>
name|keysMap
init|=
name|jwks
operator|.
name|getKeyIdMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|keysMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|rsaKey
init|=
name|keysMap
operator|.
name|get
argument_list|(
name|RSA_KID_VALUE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|rsaKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validatePublicRsaKey
argument_list|(
name|rsaKey
argument_list|)
expr_stmt|;
name|JsonWebKey
name|ecKey
init|=
name|keysMap
operator|.
name|get
argument_list|(
name|EC_KID_VALUE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|ecKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validatePublicEcKey
argument_list|(
name|ecKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrivateSetAsList
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"jwkPrivateSet.txt"
argument_list|)
decl_stmt|;
name|validatePrivateSet
argument_list|(
name|jwks
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePrivateSet
parameter_list|(
name|JsonWebKeys
name|jwks
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|jwks
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|ecKey
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
literal|7
argument_list|,
name|ecKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validatePrivateEcKey
argument_list|(
name|ecKey
argument_list|)
expr_stmt|;
name|JsonWebKey
name|rsaKey
init|=
name|keys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|rsaKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validatePrivateRsaKey
argument_list|(
name|rsaKey
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptDecryptPrivateSet
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|password
init|=
literal|"Thus from my lips, by yours, my sin is purged."
decl_stmt|;
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"jwkPrivateSet.txt"
argument_list|)
decl_stmt|;
name|validatePrivateSet
argument_list|(
name|jwks
argument_list|)
expr_stmt|;
name|String
name|encryptedKeySet
init|=
name|JwkUtils
operator|.
name|encryptJwkSet
argument_list|(
name|jwks
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|JweCompactConsumer
name|c
init|=
operator|new
name|JweCompactConsumer
argument_list|(
name|encryptedKeySet
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jwk-set+json"
argument_list|,
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS256_A128KW
argument_list|,
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ContentAlgorithm
operator|.
name|A128CBC_HS256
argument_list|,
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"p2s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"p2c"
argument_list|)
argument_list|)
expr_stmt|;
name|jwks
operator|=
name|JwkUtils
operator|.
name|decryptJwkSet
argument_list|(
name|encryptedKeySet
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|validatePrivateSet
argument_list|(
name|jwks
argument_list|)
expr_stmt|;
block|}
finally|finally
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptDecryptPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|password
init|=
literal|"Thus from my lips, by yours, my sin is purged."
decl_stmt|;
specifier|final
name|String
name|key
init|=
literal|"{\"kty\":\"oct\","
operator|+
literal|"\"alg\":\"A128KW\","
operator|+
literal|"\"k\":\"GawgguFyGrWKav7AX4VKUg\","
operator|+
literal|"\"kid\":\"AesWrapKey\"}"
decl_stmt|;
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JsonWebKey
name|jwk
init|=
name|readKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|validateSecretAesKey
argument_list|(
name|jwk
argument_list|)
expr_stmt|;
name|String
name|encryptedKey
init|=
name|JwkUtils
operator|.
name|encryptJwkKey
argument_list|(
name|jwk
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|JweCompactConsumer
name|c
init|=
operator|new
name|JweCompactConsumer
argument_list|(
name|encryptedKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jwk+json"
argument_list|,
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KeyAlgorithm
operator|.
name|PBES2_HS256_A128KW
argument_list|,
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getKeyEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ContentAlgorithm
operator|.
name|A128CBC_HS256
argument_list|,
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getContentEncryptionAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"p2s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|c
operator|.
name|getJweHeaders
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"p2c"
argument_list|)
argument_list|)
expr_stmt|;
name|jwk
operator|=
name|JwkUtils
operator|.
name|decryptJwkKey
argument_list|(
name|encryptedKey
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|validateSecretAesKey
argument_list|(
name|jwk
argument_list|)
expr_stmt|;
block|}
finally|finally
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSecretSetAsList
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"jwkSecretSet.txt"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|keys
init|=
name|jwks
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|aesKey
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
literal|4
argument_list|,
name|aesKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validateSecretAesKey
argument_list|(
name|aesKey
argument_list|)
expr_stmt|;
name|JsonWebKey
name|hmacKey
init|=
name|keys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|hmacKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validateSecretHmacKey
argument_list|(
name|hmacKey
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateSecretAesKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|AES_SECRET_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|OCTET_KEY_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AES_KID_VALUE
argument_list|,
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KeyType
operator|.
name|OCTET
argument_list|,
name|key
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AlgorithmUtils
operator|.
name|A128KW_ALGO
argument_list|,
name|key
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateSecretHmacKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|HMAC_SECRET_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|OCTET_KEY_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HMAC_KID_VALUE
argument_list|,
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KeyType
operator|.
name|OCTET
argument_list|,
name|key
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|AlgorithmUtils
operator|.
name|HMAC_SHA_256_ALGO
argument_list|,
name|key
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePublicRsaKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|RSA_MODULUS_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_MODULUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_PUBLIC_EXP_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PUBLIC_EXP
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_KID_VALUE
argument_list|,
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
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
name|AlgorithmUtils
operator|.
name|RS_SHA_256_ALGO
argument_list|,
name|key
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePrivateRsaKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|validatePublicRsaKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_PRIVATE_EXP_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_PRIVATE_EXP
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_FIRST_PRIME_FACTOR_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_FIRST_PRIME_FACTOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_SECOND_PRIME_FACTOR_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_SECOND_PRIME_FACTOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_FIRST_PRIME_CRT_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_FIRST_PRIME_CRT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_SECOND_PRIME_CRT_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_SECOND_PRIME_CRT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RSA_FIRST_CRT_COEFFICIENT_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|RSA_FIRST_CRT_COEFFICIENT
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePublicEcKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|EC_X_COORDINATE_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_X_COORDINATE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EC_Y_COORDINATE_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_Y_COORDINATE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EC_KID_VALUE
argument_list|,
name|key
operator|.
name|getKeyId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|KeyType
operator|.
name|EC
argument_list|,
name|key
operator|.
name|getKeyType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EC_CURVE_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_CURVE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PublicKeyUse
operator|.
name|ENCRYPT
argument_list|,
name|key
operator|.
name|getPublicKeyUse
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validatePrivateEcKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|validatePublicEcKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EC_PRIVATE_KEY_VALUE
argument_list|,
name|key
operator|.
name|getProperty
argument_list|(
name|JsonWebKey
operator|.
name|EC_PRIVATE_KEY
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
name|JsonWebKeyTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|IOUtils
operator|.
name|readStringFromStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
return|return
name|JwkUtils
operator|.
name|readJwkSet
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|public
name|JsonWebKey
name|readKey
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|JwkUtils
operator|.
name|readJwkKey
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
end_class

end_unit

