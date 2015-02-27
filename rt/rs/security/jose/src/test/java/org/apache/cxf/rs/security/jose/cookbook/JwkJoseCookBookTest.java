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
name|cookbook
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
name|JwkJoseCookBookTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EC_X_COORDINATE_VALUE
init|=
literal|"AHKZLLOsCOzz5cY97ewNUajB957y-C-U88c3v13nmGZx6sYl_oJXu9"
operator|+
literal|"A5RkTKqjqvjyekWF-7ytDyRXYgCF5cj0Kt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_Y_COORDINATE_VALUE
init|=
literal|"AdymlHvOiLxXkEhayXQnNCvDX4h9htZaCJN34kfmC6pV5OhQHiraVy"
operator|+
literal|"SsUdaQkAgDPrwQrJmbnX9cwlGfP-HqHZR1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_KID_VALUE
init|=
literal|"bilbo.baggins@hobbiton.example"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_CURVE_VALUE
init|=
literal|"P-521"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_PRIVATE_KEY_VALUE
init|=
literal|"AAhRON2r9cqXX1hg-RoI6R1tX5p2rUAYdmpHZoC1XNM56KtscrX6zb"
operator|+
literal|"KipQrCW9CGZH3T4ubpnoTKLDYJ_fF3_rJt"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_MODULUS_VALUE
init|=
literal|"n4EPtAOCc9AlkeQHPzHStgAbgs7bTZLwUBZdR8_KuKPEHLd4rHVTeT"
operator|+
literal|"-O-XV2jRojdNhxJWTDvNd7nqQ0VEiZQHz_AJmSCpMaJMRBSFKrKb2wqV"
operator|+
literal|"wGU_NsYOYL-QtiWN2lbzcEe6XC0dApr5ydQLrHqkHHig3RBordaZ6Aj-"
operator|+
literal|"oBHqFEHYpPe7Tpe-OfVfHd1E6cS6M1FZcD1NNLYD5lFHpPI9bTwJlsde"
operator|+
literal|"3uhGqC0ZCuEHg8lhzwOHrtIQbS0FVbb9k3-tVTU4fg_3L_vniUFAKwuC"
operator|+
literal|"LqKnS2BYwdq_mzSnbLY7h_qixoR7jig3__kRhuaxwUkRz5iaiQkqgc5g"
operator|+
literal|"HdrNP5zw"
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
name|RSA_KID_VALUE
init|=
literal|"bilbo.baggins@hobbiton.example"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PRIVATE_EXP_VALUE
init|=
literal|"bWUC9B-EFRIo8kpGfh0ZuyGPvMNKvYWNtB_ikiH9k20eT-O1q_I78e"
operator|+
literal|"iZkpXxXQ0UTEs2LsNRS-8uJbvQ-A1irkwMSMkK1J3XTGgdrhCku9gRld"
operator|+
literal|"Y7sNA_AKZGh-Q661_42rINLRCe8W-nZ34ui_qOfkLnK9QWDDqpaIsA-b"
operator|+
literal|"MwWWSDFu2MUBYwkHTMEzLYGqOe04noqeq1hExBTHBOBdkMXiuFhUq1BU"
operator|+
literal|"6l-DqEiWxqg82sXt2h-LMnT3046AOYJoRioz75tSUQfGCshWTBnP5uDj"
operator|+
literal|"d18kKhyv07lhfSJdrPdM5Plyl21hsFf4L_mHCuoFau7gdsPfHPxxjVOc"
operator|+
literal|"OpBrQzwQ"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_FIRST_PRIME_FACTOR_VALUE
init|=
literal|"3Slxg_DwTXJcb6095RoXygQCAZ5RnAvZlno1yhHtnUex_fp7AZ_9nR"
operator|+
literal|"aO7HX_-SFfGQeutao2TDjDAWU4Vupk8rw9JR0AzZ0N2fvuIAmr_WCsmG"
operator|+
literal|"peNqQnev1T7IyEsnh8UMt-n5CafhkikzhEsrmndH6LxOrvRJlsPp6Zv8"
operator|+
literal|"bUq0k"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_SECOND_PRIME_FACTOR_VALUE
init|=
literal|"uKE2dh-cTf6ERF4k4e_jy78GfPYUIaUyoSSJuBzp3Cubk3OCqs6grT"
operator|+
literal|"8bR_cu0Dm1MZwWmtdqDyI95HrUeq3MP15vMMON8lHTeZu2lmKvwqW7an"
operator|+
literal|"V5UzhM1iZ7z4yMkuUwFWoBvyY898EXvRD-hdqRxHlSqAZ192zB3pVFJ0"
operator|+
literal|"s7pFc"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_FIRST_PRIME_CRT_VALUE
init|=
literal|"B8PVvXkvJrj2L-GYQ7v3y9r6Kw5g9SahXBwsWUzp19TVlgI-YV85q"
operator|+
literal|"1NIb1rxQtD-IsXXR3-TanevuRPRt5OBOdiMGQp8pbt26gljYfKU_E9xn"
operator|+
literal|"-RULHz0-ed9E9gXLKD4VGngpz-PfQ_q29pk5xWHoJp009Qf1HvChixRX"
operator|+
literal|"59ehik"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_SECOND_PRIME_CRT_VALUE
init|=
literal|"CLDmDGduhylc9o7r84rEUVn7pzQ6PF83Y-iBZx5NT-TpnOZKF1pEr"
operator|+
literal|"AMVeKzFEl41DlHHqqBLSM0W1sOFbwTxYWZDm6sI6og5iTbwQGIC3gnJK"
operator|+
literal|"bi_7k_vJgGHwHxgPaX2PnvP-zyEkDERuf-ry4c_Z11Cq9AqC2yeL6kdK"
operator|+
literal|"T1cYF8"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_FIRST_CRT_COEFFICIENT_VALUE
init|=
literal|"3PiqvXQN0zwMeE-sBvZgi289XP9XCQF3VWqPzMKnIgQp7_Tugo6-N"
operator|+
literal|"ZBKCQsMf3HaEGBjTVJs_jcK8-TRXvaKe-7ZMaQj8VfBdYkssbu0NKDDh"
operator|+
literal|"jJ-GtiseaDVWt7dcH0cfwxgFUHpQh7FoCrjFJ6h6ZEpMF6xmujs4qMpP"
operator|+
literal|"z8aaI4"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SIGN_SECRET_VALUE
init|=
literal|"hJtXIZ2uSN5kbQfbtTNWbpdmhkV8FJG-Onbc6mxCcYg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SIGN_KID_VALUE
init|=
literal|"018c0ae5-4d9b-471b-bfd6-eef314bc7037"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_SECRET_VALUE
init|=
literal|"AAPapAv4LbFbiVawEjagUBluYqN5rhna-8nuldDvOx8"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCRYPTION_KID_VALUE
init|=
literal|"1e571774-2e08-40da-8308-e8d68773842d"
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
literal|"cookbookPublicSet.txt"
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
literal|"cookbookPublicSet.txt"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|JsonWebKey
argument_list|>
argument_list|>
name|keysMap
init|=
name|jwks
operator|.
name|getKeyTypeMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keysMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|rsaKeys
init|=
name|keysMap
operator|.
name|get
argument_list|(
literal|"RSA"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rsaKeys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|rsaKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|rsaKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JsonWebKey
argument_list|>
name|ecKeys
init|=
name|keysMap
operator|.
name|get
argument_list|(
literal|"EC"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ecKeys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|ecKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|ecKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
literal|"cookbookPrivateSet.txt"
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
literal|"cookbookSecretSet.txt"
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
name|signKey
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
literal|5
argument_list|,
name|signKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validateSecretSignKey
argument_list|(
name|signKey
argument_list|)
expr_stmt|;
name|JsonWebKey
name|encKey
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
name|encKey
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|validateSecretEncKey
argument_list|(
name|encKey
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateSecretSignKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|SIGN_SECRET_VALUE
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
name|SIGN_KID_VALUE
argument_list|,
name|key
operator|.
name|getKid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
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
name|validateSecretEncKey
parameter_list|(
name|JsonWebKey
name|key
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|ENCRYPTION_SECRET_VALUE
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
name|ENCRYPTION_KID_VALUE
argument_list|,
name|key
operator|.
name|getKid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
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
name|A256GCM_ALGO
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
name|getKid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_RSA
argument_list|,
name|key
operator|.
name|getKeyType
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
name|getKid
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_ELLIPTIC
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
name|JsonWebKey
operator|.
name|PUBLIC_KEY_USE_SIGN
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
name|JwkJoseCookBookTest
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

