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
name|jaxrs
operator|.
name|provider
operator|.
name|json
operator|.
name|JsonMapObjectReaderWriter
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
name|JoseHeaders
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
name|EcDsaJwsSignatureProvider
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
name|JwsCompactConsumer
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
name|JwsCompactProducer
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
name|JwsJsonConsumer
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
name|JwsJsonProducer
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
name|JwsJsonProtectedHeader
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
name|JwsUtils
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JwsJoseCookBookTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PAYLOAD
init|=
literal|"It’s a dangerous business, Frodo, going out your door. "
operator|+
literal|"You step onto the road, and if you don't keep your feet, "
operator|+
literal|"there’s no knowing where you might be swept off to."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_PAYLOAD
init|=
literal|"SXTigJlzIGEgZGFuZ2Vyb3VzIGJ1c2luZXNzLCBGcm9kbywgZ29pbmcgb3V0IH"
operator|+
literal|"lvdXIgZG9vci4gWW91IHN0ZXAgb250byB0aGUgcm9hZCwgYW5kIGlmIHlvdSBk"
operator|+
literal|"b24ndCBrZWVwIHlvdXIgZmVldCwgdGhlcmXigJlzIG5vIGtub3dpbmcgd2hlcm"
operator|+
literal|"UgeW91IG1pZ2h0IGJlIHN3ZXB0IG9mZiB0by4"
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
name|RSA_V1_5_SIGNATURE_PROTECTED_HEADER
init|=
literal|"eyJhbGciOiJSUzI1NiIsImtpZCI6ImJpbGJvLmJhZ2dpbnNAaG9iYml0b24uZX"
operator|+
literal|"hhbXBsZSJ9"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_V1_5_SIGNATURE_PROTECTED_HEADER_JSON
init|=
operator|(
literal|"{"
operator|+
literal|"\"alg\": \"RS256\","
operator|+
literal|"\"kid\": \"bilbo.baggins@hobbiton.example\""
operator|+
literal|"}"
operator|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_V1_5_SIGNATURE_VALUE
init|=
literal|"MRjdkly7_-oTPTS3AXP41iQIGKa80A0ZmTuV5MEaHoxnW2e5CZ5NlKtainoFmK"
operator|+
literal|"ZopdHM1O2U4mwzJdQx996ivp83xuglII7PNDi84wnB-BDkoBwA78185hX-Es4J"
operator|+
literal|"IwmDLJK3lfWRa-XtL0RnltuYv746iYTh_qHRD68BNt1uSNCrUCTJDt5aAE6x8w"
operator|+
literal|"W1Kt9eRo4QPocSadnHXFxnt8Is9UzpERV0ePPQdLuW3IS_de3xyIrDaLGdjluP"
operator|+
literal|"xUAhb6L2aXic1U12podGU0KLUQSE_oI-ZnmKJ3F4uOZDnd6QZWJushZ41Axf_f"
operator|+
literal|"cIe8u9ipH84ogoree7vjbU5y18kDquDg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_V1_5_JSON_GENERAL_SERIALIZATION
init|=
operator|(
literal|"{"
operator|+
literal|"\"payload\": \"SXTigJlzIGEgZGFuZ2Vyb3VzIGJ1c2luZXNzLCBGcm9kbywg"
operator|+
literal|"Z29pbmcgb3V0IHlvdXIgZG9vci4gWW91IHN0ZXAgb250byB0aGUgcm9h"
operator|+
literal|"ZCwgYW5kIGlmIHlvdSBkb24ndCBrZWVwIHlvdXIgZmVldCwgdGhlcmXi"
operator|+
literal|"gJlzIG5vIGtub3dpbmcgd2hlcmUgeW91IG1pZ2h0IGJlIHN3ZXB0IG9m"
operator|+
literal|"ZiB0by4\","
operator|+
literal|"\"signatures\": ["
operator|+
literal|"{"
operator|+
literal|"\"protected\": \"eyJhbGciOiJSUzI1NiIsImtpZCI6ImJpbGJvLmJhZ2"
operator|+
literal|"dpbnNAaG9iYml0b24uZXhhbXBsZSJ9\","
operator|+
literal|"\"signature\": \"MRjdkly7_-oTPTS3AXP41iQIGKa80A0ZmTuV5MEaHo"
operator|+
literal|"xnW2e5CZ5NlKtainoFmKZopdHM1O2U4mwzJdQx996ivp83xuglII"
operator|+
literal|"7PNDi84wnB-BDkoBwA78185hX-Es4JIwmDLJK3lfWRa-XtL0Rnlt"
operator|+
literal|"uYv746iYTh_qHRD68BNt1uSNCrUCTJDt5aAE6x8wW1Kt9eRo4QPo"
operator|+
literal|"cSadnHXFxnt8Is9UzpERV0ePPQdLuW3IS_de3xyIrDaLGdjluPxU"
operator|+
literal|"Ahb6L2aXic1U12podGU0KLUQSE_oI-ZnmKJ3F4uOZDnd6QZWJush"
operator|+
literal|"Z41Axf_fcIe8u9ipH84ogoree7vjbU5y18kDquDg\""
operator|+
literal|"}"
operator|+
literal|"]"
operator|+
literal|"}"
operator|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_V1_5_JSON_FLATTENED_SERIALIZATION
init|=
operator|(
literal|"{"
operator|+
literal|"\"payload\": \"SXTigJlzIGEgZGFuZ2Vyb3VzIGJ1c2luZXNzLCBGcm9kbywg"
operator|+
literal|"Z29pbmcgb3V0IHlvdXIgZG9vci4gWW91IHN0ZXAgb250byB0aGUgcm9h"
operator|+
literal|"ZCwgYW5kIGlmIHlvdSBkb24ndCBrZWVwIHlvdXIgZmVldCwgdGhlcmXi"
operator|+
literal|"gJlzIG5vIGtub3dpbmcgd2hlcmUgeW91IG1pZ2h0IGJlIHN3ZXB0IG9m"
operator|+
literal|"ZiB0by4\","
operator|+
literal|"\"protected\": \"eyJhbGciOiJSUzI1NiIsImtpZCI6ImJpbGJvLmJhZ2dpbn"
operator|+
literal|"NAaG9iYml0b24uZXhhbXBsZSJ9\","
operator|+
literal|"\"signature\": \"MRjdkly7_-oTPTS3AXP41iQIGKa80A0ZmTuV5MEaHoxnW2"
operator|+
literal|"e5CZ5NlKtainoFmKZopdHM1O2U4mwzJdQx996ivp83xuglII7PNDi84w"
operator|+
literal|"nB-BDkoBwA78185hX-Es4JIwmDLJK3lfWRa-XtL0RnltuYv746iYTh_q"
operator|+
literal|"HRD68BNt1uSNCrUCTJDt5aAE6x8wW1Kt9eRo4QPocSadnHXFxnt8Is9U"
operator|+
literal|"zpERV0ePPQdLuW3IS_de3xyIrDaLGdjluPxUAhb6L2aXic1U12podGU0"
operator|+
literal|"KLUQSE_oI-ZnmKJ3F4uOZDnd6QZWJushZ41Axf_fcIe8u9ipH84ogore"
operator|+
literal|"e7vjbU5y18kDquDg\""
operator|+
literal|"}"
operator|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PSS_SIGNATURE_PROTECTED_HEADER_JSON
init|=
operator|(
literal|"{"
operator|+
literal|"\"alg\": \"PS384\","
operator|+
literal|"\"kid\": \"bilbo.baggins@hobbiton.example\""
operator|+
literal|"}"
operator|)
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PSS_SIGNATURE_PROTECTED_HEADER
init|=
literal|"eyJhbGciOiJQUzM4NCIsImtpZCI6ImJpbGJvLmJhZ2dpbnNAaG9iYml0b24uZX"
operator|+
literal|"hhbXBsZSJ9"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PSS_SIGNATURE_VALUE
init|=
literal|"cu22eBqkYDKgIlTpzDXGvaFfz6WGoz7fUDcfT0kkOy42miAh2qyBzk1xEsnk2I"
operator|+
literal|"pN6-tPid6VrklHkqsGqDqHCdP6O8TTB5dDDItllVo6_1OLPpcbUrhiUSMxbbXU"
operator|+
literal|"vdvWXzg-UD8biiReQFlfz28zGWVsdiNAUf8ZnyPEgVFn442ZdNqiVJRmBqrYRX"
operator|+
literal|"e8P_ijQ7p8Vdz0TTrxUeT3lm8d9shnr2lfJT8ImUjvAA2Xez2Mlp8cBE5awDzT"
operator|+
literal|"0qI0n6uiP1aCN_2_jLAeQTlqRHtfa64QQSUmFAAjVKPbByi7xho0uTOcbH510a"
operator|+
literal|"6GYmJUAfmWjwZ6oD4ifKo8DYM-X72Eaw"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PSS_JSON_GENERAL_SERIALIZATION
init|=
operator|(
literal|"{"
operator|+
literal|"\"payload\": \"SXTigJlzIGEgZGFuZ2Vyb3VzIGJ1c2luZXNzLCBGcm9kbywg"
operator|+
literal|"Z29pbmcgb3V0IHlvdXIgZG9vci4gWW91IHN0ZXAgb250byB0aGUgcm9h"
operator|+
literal|"ZCwgYW5kIGlmIHlvdSBkb24ndCBrZWVwIHlvdXIgZmVldCwgdGhlcmXi"
operator|+
literal|"gJlzIG5vIGtub3dpbmcgd2hlcmUgeW91IG1pZ2h0IGJlIHN3ZXB0IG9m"
operator|+
literal|"ZiB0by4\","
operator|+
literal|"\"signatures\": ["
operator|+
literal|"{"
operator|+
literal|"\"protected\": \"eyJhbGciOiJQUzM4NCIsImtpZCI6ImJpbGJvLmJhZ2"
operator|+
literal|"dpbnNAaG9iYml0b24uZXhhbXBsZSJ9\","
operator|+
literal|"\"signature\": \"cu22eBqkYDKgIlTpzDXGvaFfz6WGoz7fUDcfT0kkOy"
operator|+
literal|"42miAh2qyBzk1xEsnk2IpN6-tPid6VrklHkqsGqDqHCdP6O8TTB5"
operator|+
literal|"dDDItllVo6_1OLPpcbUrhiUSMxbbXUvdvWXzg-UD8biiReQFlfz2"
operator|+
literal|"8zGWVsdiNAUf8ZnyPEgVFn442ZdNqiVJRmBqrYRXe8P_ijQ7p8Vd"
operator|+
literal|"z0TTrxUeT3lm8d9shnr2lfJT8ImUjvAA2Xez2Mlp8cBE5awDzT0q"
operator|+
literal|"I0n6uiP1aCN_2_jLAeQTlqRHtfa64QQSUmFAAjVKPbByi7xho0uT"
operator|+
literal|"OcbH510a6GYmJUAfmWjwZ6oD4ifKo8DYM-X72Eaw\""
operator|+
literal|"}"
operator|+
literal|"]"
operator|+
literal|"}"
operator|)
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PSS_JSON_FLATTENED_SERIALIZATION
init|=
operator|(
literal|"{"
operator|+
literal|"\"payload\": \"SXTigJlzIGEgZGFuZ2Vyb3VzIGJ1c2luZXNzLCBGcm9kbywg"
operator|+
literal|"Z29pbmcgb3V0IHlvdXIgZG9vci4gWW91IHN0ZXAgb250byB0aGUgcm9h"
operator|+
literal|"ZCwgYW5kIGlmIHlvdSBkb24ndCBrZWVwIHlvdXIgZmVldCwgdGhlcmXi"
operator|+
literal|"gJlzIG5vIGtub3dpbmcgd2hlcmUgeW91IG1pZ2h0IGJlIHN3ZXB0IG9m"
operator|+
literal|"ZiB0by4\","
operator|+
literal|"\"protected\": \"eyJhbGciOiJQUzM4NCIsImtpZCI6ImJpbGJvLmJhZ2dpbn"
operator|+
literal|"NAaG9iYml0b24uZXhhbXBsZSJ9\","
operator|+
literal|"\"signature\": \"cu22eBqkYDKgIlTpzDXGvaFfz6WGoz7fUDcfT0kkOy42mi"
operator|+
literal|"Ah2qyBzk1xEsnk2IpN6-tPid6VrklHkqsGqDqHCdP6O8TTB5dDDItllV"
operator|+
literal|"o6_1OLPpcbUrhiUSMxbbXUvdvWXzg-UD8biiReQFlfz28zGWVsdiNAUf"
operator|+
literal|"8ZnyPEgVFn442ZdNqiVJRmBqrYRXe8P_ijQ7p8Vdz0TTrxUeT3lm8d9s"
operator|+
literal|"hnr2lfJT8ImUjvAA2Xez2Mlp8cBE5awDzT0qI0n6uiP1aCN_2_jLAeQT"
operator|+
literal|"lqRHtfa64QQSUmFAAjVKPbByi7xho0uTOcbH510a6GYmJUAfmWjwZ6oD"
operator|+
literal|"4ifKo8DYM-X72Eaw\""
operator|+
literal|"}"
operator|)
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ECDSA_KID_VALUE
init|=
literal|"bilbo.baggins@hobbiton.example"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ECDSA_SIGNATURE_PROTECTED_HEADER_JSON
init|=
operator|(
literal|"{"
operator|+
literal|"\"alg\": \"ES512\","
operator|+
literal|"\"kid\": \"bilbo.baggins@hobbiton.example\""
operator|+
literal|"}"
operator|)
operator|.
name|replace
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ECSDA_SIGNATURE_PROTECTED_HEADER
init|=
literal|"eyJhbGciOiJFUzUxMiIsImtpZCI6ImJpbGJvLmJhZ2dpbnNAaG9iYml0b24uZX"
operator|+
literal|"hhbXBsZSJ9"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testEncodedPayload
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|PAYLOAD
argument_list|)
argument_list|,
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRSAv15Signature
parameter_list|()
throws|throws
name|Exception
block|{
name|JwsCompactProducer
name|compactProducer
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|PAYLOAD
argument_list|)
decl_stmt|;
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
expr_stmt|;
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|setKeyId
argument_list|(
name|RSA_KID_VALUE
argument_list|)
expr_stmt|;
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|reader
operator|.
name|toJson
argument_list|(
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
argument_list|,
name|RSA_V1_5_SIGNATURE_PROTECTED_HEADER_JSON
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|compactProducer
operator|.
name|getUnsignedEncodedJws
argument_list|()
argument_list|,
name|RSA_V1_5_SIGNATURE_PROTECTED_HEADER
operator|+
literal|"."
operator|+
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"cookbookPrivateSet.txt"
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
name|compactProducer
operator|.
name|signWith
argument_list|(
name|rsaKey
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|compactProducer
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|,
name|RSA_V1_5_SIGNATURE_PROTECTED_HEADER
operator|+
literal|"."
operator|+
name|ENCODED_PAYLOAD
operator|+
literal|"."
operator|+
name|RSA_V1_5_SIGNATURE_VALUE
argument_list|)
expr_stmt|;
name|JwsCompactConsumer
name|compactConsumer
init|=
operator|new
name|JwsCompactConsumer
argument_list|(
name|compactProducer
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|publicJwks
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
name|publicKeys
init|=
name|publicJwks
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|JsonWebKey
name|rsaPublicKey
init|=
name|publicKeys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|compactConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|rsaPublicKey
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
argument_list|)
expr_stmt|;
name|JwsJsonProducer
name|jsonProducer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|PAYLOAD
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getPlainPayload
argument_list|()
argument_list|,
name|PAYLOAD
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getUnsignedEncodedPayload
argument_list|()
argument_list|,
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
name|JoseHeaders
name|joseHeaders
init|=
operator|new
name|JoseHeaders
argument_list|()
decl_stmt|;
name|joseHeaders
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
expr_stmt|;
name|joseHeaders
operator|.
name|setKeyId
argument_list|(
name|RSA_KID_VALUE
argument_list|)
expr_stmt|;
name|JwsJsonProtectedHeader
name|protectedHeader
init|=
operator|new
name|JwsJsonProtectedHeader
argument_list|(
name|joseHeaders
argument_list|)
decl_stmt|;
name|jsonProducer
operator|.
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|rsaKey
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
argument_list|,
name|protectedHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|,
name|RSA_V1_5_JSON_GENERAL_SERIALIZATION
argument_list|)
expr_stmt|;
name|JwsJsonConsumer
name|jsonConsumer
init|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jsonConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|rsaPublicKey
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
argument_list|)
expr_stmt|;
name|jsonProducer
operator|=
operator|new
name|JwsJsonProducer
argument_list|(
name|PAYLOAD
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|jsonProducer
operator|.
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|rsaKey
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
argument_list|,
name|protectedHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|,
name|RSA_V1_5_JSON_FLATTENED_SERIALIZATION
argument_list|)
expr_stmt|;
name|jsonConsumer
operator|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jsonConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|rsaPublicKey
argument_list|,
name|JoseConstants
operator|.
name|RS_SHA_256_ALGO
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRSAPSSSignature
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
name|Algorithm
operator|.
name|PS_SHA_384_JAVA
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
name|JwsCompactProducer
name|compactProducer
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|PAYLOAD
argument_list|)
decl_stmt|;
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
expr_stmt|;
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|setKeyId
argument_list|(
name|RSA_KID_VALUE
argument_list|)
expr_stmt|;
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|reader
operator|.
name|toJson
argument_list|(
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
argument_list|,
name|RSA_PSS_SIGNATURE_PROTECTED_HEADER_JSON
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|compactProducer
operator|.
name|getUnsignedEncodedJws
argument_list|()
argument_list|,
name|RSA_PSS_SIGNATURE_PROTECTED_HEADER
operator|+
literal|"."
operator|+
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"cookbookPrivateSet.txt"
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
name|compactProducer
operator|.
name|signWith
argument_list|(
name|rsaKey
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|compactProducer
operator|.
name|getSignedEncodedJws
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
operator|(
name|RSA_PSS_SIGNATURE_PROTECTED_HEADER
operator|+
literal|"."
operator|+
name|ENCODED_PAYLOAD
operator|+
literal|"."
operator|+
name|RSA_PSS_SIGNATURE_VALUE
operator|)
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|JwsCompactConsumer
name|compactConsumer
init|=
operator|new
name|JwsCompactConsumer
argument_list|(
name|compactProducer
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|publicJwks
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
name|publicKeys
init|=
name|publicJwks
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|JsonWebKey
name|rsaPublicKey
init|=
name|publicKeys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|compactConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|rsaPublicKey
argument_list|,
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
argument_list|)
expr_stmt|;
name|JwsJsonProducer
name|jsonProducer
init|=
operator|new
name|JwsJsonProducer
argument_list|(
name|PAYLOAD
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getPlainPayload
argument_list|()
argument_list|,
name|PAYLOAD
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getUnsignedEncodedPayload
argument_list|()
argument_list|,
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
name|JoseHeaders
name|joseHeaders
init|=
operator|new
name|JoseHeaders
argument_list|()
decl_stmt|;
name|joseHeaders
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
expr_stmt|;
name|joseHeaders
operator|.
name|setKeyId
argument_list|(
name|RSA_KID_VALUE
argument_list|)
expr_stmt|;
name|JwsJsonProtectedHeader
name|protectedHeader
init|=
operator|new
name|JwsJsonProtectedHeader
argument_list|(
name|joseHeaders
argument_list|)
decl_stmt|;
name|jsonProducer
operator|.
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|rsaKey
argument_list|,
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
argument_list|,
name|protectedHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
name|RSA_PSS_JSON_GENERAL_SERIALIZATION
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|JwsJsonConsumer
name|jsonConsumer
init|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jsonConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|rsaPublicKey
argument_list|,
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
argument_list|)
expr_stmt|;
name|jsonProducer
operator|=
operator|new
name|JwsJsonProducer
argument_list|(
name|PAYLOAD
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|jsonProducer
operator|.
name|signWith
argument_list|(
name|JwsUtils
operator|.
name|getSignatureProvider
argument_list|(
name|rsaKey
argument_list|,
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
argument_list|,
name|protectedHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
name|RSA_PSS_JSON_FLATTENED_SERIALIZATION
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|jsonConsumer
operator|=
operator|new
name|JwsJsonConsumer
argument_list|(
name|jsonProducer
operator|.
name|getJwsJsonSignedDocument
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jsonConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|rsaPublicKey
argument_list|,
name|JoseConstants
operator|.
name|PS_SHA_384_ALGO
argument_list|)
argument_list|)
expr_stmt|;
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
name|testECDSASignature
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
name|Algorithm
operator|.
name|ES_SHA_512_JAVA
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
try|try
block|{
name|JwsCompactProducer
name|compactProducer
init|=
operator|new
name|JwsCompactProducer
argument_list|(
name|PAYLOAD
argument_list|)
decl_stmt|;
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|setAlgorithm
argument_list|(
name|JoseConstants
operator|.
name|ES_SHA_512_ALGO
argument_list|)
expr_stmt|;
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|setKeyId
argument_list|(
name|ECDSA_KID_VALUE
argument_list|)
expr_stmt|;
name|JsonMapObjectReaderWriter
name|reader
init|=
operator|new
name|JsonMapObjectReaderWriter
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|reader
operator|.
name|toJson
argument_list|(
name|compactProducer
operator|.
name|getJoseHeaders
argument_list|()
operator|.
name|asMap
argument_list|()
argument_list|)
argument_list|,
name|ECDSA_SIGNATURE_PROTECTED_HEADER_JSON
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|compactProducer
operator|.
name|getUnsignedEncodedJws
argument_list|()
argument_list|,
name|ECSDA_SIGNATURE_PROTECTED_HEADER
operator|+
literal|"."
operator|+
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
name|JsonWebKeys
name|jwks
init|=
name|readKeySet
argument_list|(
literal|"cookbookPrivateSet.txt"
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
name|compactProducer
operator|.
name|signWith
argument_list|(
operator|new
name|EcDsaJwsSignatureProvider
argument_list|(
name|JwkUtils
operator|.
name|toECPrivateKey
argument_list|(
name|ecKey
argument_list|)
argument_list|,
name|JoseConstants
operator|.
name|ES_SHA_512_ALGO
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|compactProducer
operator|.
name|getUnsignedEncodedJws
argument_list|()
argument_list|,
name|ECSDA_SIGNATURE_PROTECTED_HEADER
operator|+
literal|"."
operator|+
name|ENCODED_PAYLOAD
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|132
argument_list|,
name|Base64UrlUtility
operator|.
name|decode
argument_list|(
name|compactProducer
operator|.
name|getEncodedSignature
argument_list|()
argument_list|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|JwsCompactConsumer
name|compactConsumer
init|=
operator|new
name|JwsCompactConsumer
argument_list|(
name|compactProducer
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|)
decl_stmt|;
name|JsonWebKeys
name|publicJwks
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
name|publicKeys
init|=
name|publicJwks
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|JsonWebKey
name|ecPublicKey
init|=
name|publicKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|compactConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|ecPublicKey
argument_list|,
name|JoseConstants
operator|.
name|ES_SHA_512_ALGO
argument_list|)
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
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|JwsJoseCookBookTest
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
block|}
end_class

end_unit

