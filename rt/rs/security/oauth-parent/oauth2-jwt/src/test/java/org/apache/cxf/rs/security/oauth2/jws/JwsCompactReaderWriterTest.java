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
name|oauth2
operator|.
name|jws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|ECPrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|ECPublicKey
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|rs
operator|.
name|security
operator|.
name|oauth2
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
name|oauth2
operator|.
name|jwt
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtConstants
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtHeaders
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtToken
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtTokenReaderWriter
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
name|oauth2
operator|.
name|jwt
operator|.
name|JwtTokenWriter
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
name|oauth2
operator|.
name|utils
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
name|JwsCompactReaderWriterTest
extends|extends
name|Assert
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ENCODED_TOKEN_SIGNED_BY_MAC
init|=
literal|"eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9"
operator|+
literal|".eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ"
operator|+
literal|".dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_MAC_KEY
init|=
literal|"AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75"
operator|+
literal|"aKtMN3Yj0iPS4hcgUuTwjAzZr1Z9CAow"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_TOKEN_WITH_JSON_KEY_SIGNED_BY_MAC
init|=
literal|"eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIU"
operator|+
literal|"zI1NiIsDQogImp3ayI6eyJrdHkiOiJvY3QiLA0KICJrZXlfb3BzIjpbDQogInNpZ24iLA0KICJ2ZXJpZnkiDQogXX19"
operator|+
literal|".eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ"
operator|+
literal|".8cFZqb15gEDYRZqSzUu23nQnKNynru1ADByRPvmmOq8"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_MODULUS_ENCODED
init|=
literal|"ofgWCuLjybRlzo0tZWJjNiuSfb4p4fAkd_wWJcyQoTbji9k0l8W26mPddx"
operator|+
literal|"HmfHQp-Vaw-4qPCJrcS2mJPMEzP1Pt0Bm4d4QlL-yRT-SFd2lZS-pCgNMs"
operator|+
literal|"D1W_YpRPEwOWvG6b32690r2jZ47soMZo9wGzjb_7OMg0LOL-bSf63kpaSH"
operator|+
literal|"SXndS5z5rexMdbBYUsLA9e-KXBdQOS-UTo7WTBEMa2R2CapHg665xsmtdV"
operator|+
literal|"MTBQY4uDZlxvb3qCo5ZwKh9kG4LT6_I5IhlJH7aGhyxXFvUK-DWNmoudF8"
operator|+
literal|"NAco9_h9iaGNj8q2ethFkMLs91kzk2PAcDTW9gb54h4FRWyuXpoQ"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PUBLIC_EXPONENT_ENCODED
init|=
literal|"AQAB"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RSA_PRIVATE_EXPONENT_ENCODED
init|=
literal|"Eq5xpGnNCivDflJsRQBXHx1hdR1k6Ulwe2JZD50LpXyWPEAeP88vLNO97I"
operator|+
literal|"jlA7_GQ5sLKMgvfTeXZx9SE-7YwVol2NXOoAJe46sui395IW_GO-pWJ1O0"
operator|+
literal|"BkTGoVEn2bKVRUCgu-GjBVaYLU6f3l9kJfFNS3E0QbVdxzubSu3Mkqzjkn"
operator|+
literal|"439X0M_V51gfpRLI9JYanrC4D4qAdGcopV_0ZHHzQlBjudU2QvXt4ehNYT"
operator|+
literal|"CBr6XCLQUShb1juUO1ZdiYoFaFQT5Tw8bGUl_x_jTj3ccPDVZFD9pIuhLh"
operator|+
literal|"BOneufuBiB4cS98l2SR_RQyGWSeWjnczT0QU91p1DhOVRuOopznQ"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_TOKEN_SIGNED_BY_PRIVATE_KEY
init|=
literal|"eyJhbGciOiJSUzI1NiJ9"
operator|+
literal|"."
operator|+
literal|"eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFt"
operator|+
literal|"cGxlLmNvbS9pc19yb290Ijp0cnVlfQ"
operator|+
literal|"."
operator|+
literal|"cC4hiUPoj9Eetdgtv3hF80EGrhuB__dzERat0XF9g2VtQgr9PJbu3XOiZj5RZmh7"
operator|+
literal|"AAuHIm4Bh-0Qc_lF5YKt_O8W2Fp5jujGbds9uJdbF9CUAr7t1dnZcAcQjbKBYNX4"
operator|+
literal|"BAynRFdiuB--f_nZLgrnbyTyWzO75vRK5h6xBArLIARNPvkSjtQBMHlb1L07Qe7K"
operator|+
literal|"0GarZRmB_eSN9383LcOLn6_dO--xi12jzDwusC-eOkHWEsqtFZESc6BfI7noOPqv"
operator|+
literal|"hJ1phCnvWh6IeYI2w9QOYEUipUTI8np6LbgGY9Fs98rqVt5AXLIhWkWywlVmtVrB"
operator|+
literal|"p0igcN_IoypGlUPQGe77Rw"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_PRIVATE_KEY_ENCODED
init|=
literal|"jpsQnnGQmL-YBIffH1136cspYG6-0iY7X1fCE9-E9LI"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_X_POINT_ENCODED
init|=
literal|"f83OJ3D2xF1Bg8vub9tLe1gHMzV76e8Tus9uPHvRVEU"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EC_Y_POINT_ENCODED
init|=
literal|"x_FEzRu9m36HLN_tue659LNpXW6pCyStikYjKIWI5a0"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testWriteJwsSignedByMacSpecExample
parameter_list|()
throws|throws
name|Exception
block|{
name|JwtHeaders
name|headers
init|=
operator|new
name|JwtHeaders
argument_list|(
name|Algorithm
operator|.
name|HmacSHA256
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
name|JwsCompactProducer
name|jws
init|=
name|initSpecJwtTokenWriter
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|jws
operator|.
name|signWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ENCODED_TOKEN_SIGNED_BY_MAC
argument_list|,
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteReadJwsUnsigned
parameter_list|()
throws|throws
name|Exception
block|{
name|JwtHeaders
name|headers
init|=
operator|new
name|JwtHeaders
argument_list|(
name|JwtConstants
operator|.
name|PLAIN_TEXT_ALGO
argument_list|)
decl_stmt|;
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"https://jwt-idp.example.com"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"mailto:mike@example.com"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setAudience
argument_list|(
literal|"https://jwt-rp.example.net"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setNotBefore
argument_list|(
literal|1300815780L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
literal|1300819380L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setClaim
argument_list|(
literal|"http://claims.example.com/member"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|JwsCompactProducer
name|writer
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|String
name|signed
init|=
name|writer
operator|.
name|getSignedEncodedJws
argument_list|()
decl_stmt|;
name|JwsJwtCompactConsumer
name|reader
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|signed
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|reader
operator|.
name|getDecodedSignature
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
name|reader
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadJwsSignedByMacSpecExample
parameter_list|()
throws|throws
name|Exception
block|{
name|JwsJwtCompactConsumer
name|jws
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|ENCODED_TOKEN_SIGNED_BY_MAC
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jws
operator|.
name|verifySignatureWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
name|jws
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|JwtHeaders
name|headers
init|=
name|token
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JwtConstants
operator|.
name|TYPE_JWT
argument_list|,
name|headers
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Algorithm
operator|.
name|HmacSHA256
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|headers
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|validateSpecClaim
argument_list|(
name|token
operator|.
name|getClaims
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteJwsWithJwkSignedByMac
parameter_list|()
throws|throws
name|Exception
block|{
name|JsonWebKey
name|key
init|=
operator|new
name|JsonWebKey
argument_list|()
decl_stmt|;
name|key
operator|.
name|setKeyType
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
argument_list|)
expr_stmt|;
name|key
operator|.
name|setKeyOperation
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
block|,
name|JsonWebKey
operator|.
name|KEY_OPER_VERIFY
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|doTestWriteJwsWithJwkSignedByMac
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteJwsWithJwkAsMapSignedByMac
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|JsonWebKey
operator|.
name|KEY_TYPE
argument_list|,
name|JsonWebKey
operator|.
name|KEY_TYPE_OCTET
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPERATIONS
argument_list|,
operator|new
name|String
index|[]
block|{
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
block|,
name|JsonWebKey
operator|.
name|KEY_OPER_VERIFY
block|}
argument_list|)
expr_stmt|;
name|doTestWriteJwsWithJwkSignedByMac
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestWriteJwsWithJwkSignedByMac
parameter_list|(
name|Object
name|jsonWebKey
parameter_list|)
throws|throws
name|Exception
block|{
name|JwtHeaders
name|headers
init|=
operator|new
name|JwtHeaders
argument_list|(
name|Algorithm
operator|.
name|HmacSHA256
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
name|headers
operator|.
name|setHeader
argument_list|(
name|JwtConstants
operator|.
name|HEADER_JSON_WEB_KEY
argument_list|,
name|jsonWebKey
argument_list|)
expr_stmt|;
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"joe"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
literal|1300819380L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setClaim
argument_list|(
literal|"http://example.com/is_root"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|JwsCompactProducer
name|jws
init|=
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|token
argument_list|,
name|getWriter
argument_list|()
argument_list|)
decl_stmt|;
name|jws
operator|.
name|signWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ENCODED_TOKEN_WITH_JSON_KEY_SIGNED_BY_MAC
argument_list|,
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadJwsWithJwkSignedByMac
parameter_list|()
throws|throws
name|Exception
block|{
name|JwsJwtCompactConsumer
name|jws
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|ENCODED_TOKEN_WITH_JSON_KEY_SIGNED_BY_MAC
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jws
operator|.
name|verifySignatureWith
argument_list|(
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
name|jws
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|JwtHeaders
name|headers
init|=
name|token
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|JwtConstants
operator|.
name|TYPE_JWT
argument_list|,
name|headers
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Algorithm
operator|.
name|HmacSHA256
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|headers
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|JsonWebKey
name|key
init|=
name|headers
operator|.
name|getJsonWebKey
argument_list|()
decl_stmt|;
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
name|List
argument_list|<
name|String
argument_list|>
name|keyOps
init|=
name|key
operator|.
name|getKeyOperation
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keyOps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|,
name|keyOps
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_VERIFY
argument_list|,
name|keyOps
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|validateSpecClaim
argument_list|(
name|token
operator|.
name|getClaims
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateSpecClaim
parameter_list|(
name|JwtClaims
name|claims
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"joe"
argument_list|,
name|claims
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1300819380
argument_list|)
argument_list|,
name|claims
operator|.
name|getExpiryTime
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|claims
operator|.
name|getClaim
argument_list|(
literal|"http://example.com/is_root"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteJwsSignedByPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
name|JwtHeaders
name|headers
init|=
operator|new
name|JwtHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|Algorithm
operator|.
name|SHA256withRSA
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
name|JwsCompactProducer
name|jws
init|=
name|initSpecJwtTokenWriter
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|PrivateKey
name|key
init|=
name|CryptoUtils
operator|.
name|getRSAPrivateKey
argument_list|(
name|RSA_MODULUS_ENCODED
argument_list|,
name|RSA_PRIVATE_EXPONENT_ENCODED
argument_list|)
decl_stmt|;
name|jws
operator|.
name|signWith
argument_list|(
operator|new
name|PrivateKeyJwsSignatureProvider
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ENCODED_TOKEN_SIGNED_BY_PRIVATE_KEY
argument_list|,
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteReadJwsSignedByESPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
name|JwtHeaders
name|headers
init|=
operator|new
name|JwtHeaders
argument_list|()
decl_stmt|;
name|headers
operator|.
name|setAlgorithm
argument_list|(
name|Algorithm
operator|.
name|SHA256withECDSA
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
name|JwsCompactProducer
name|jws
init|=
name|initSpecJwtTokenWriter
argument_list|(
name|headers
argument_list|)
decl_stmt|;
name|ECPrivateKey
name|privateKey
init|=
name|CryptoUtils
operator|.
name|getECPrivateKey
argument_list|(
name|JsonWebKey
operator|.
name|EC_CURVE_P256
argument_list|,
name|EC_PRIVATE_KEY_ENCODED
argument_list|)
decl_stmt|;
name|jws
operator|.
name|signWith
argument_list|(
operator|new
name|EcDsaJwsSignatureProvider
argument_list|(
name|privateKey
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|signedJws
init|=
name|jws
operator|.
name|getSignedEncodedJws
argument_list|()
decl_stmt|;
name|ECPublicKey
name|publicKey
init|=
name|CryptoUtils
operator|.
name|getECPublicKey
argument_list|(
name|JsonWebKey
operator|.
name|EC_CURVE_P256
argument_list|,
name|EC_X_POINT_ENCODED
argument_list|,
name|EC_Y_POINT_ENCODED
argument_list|)
decl_stmt|;
name|JwsJwtCompactConsumer
name|jwsConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|signedJws
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jwsConsumer
operator|.
name|verifySignatureWith
argument_list|(
operator|new
name|PublicKeyJwsSignatureVerifier
argument_list|(
name|publicKey
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
name|jwsConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|JwtHeaders
name|headersReceived
init|=
name|token
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Algorithm
operator|.
name|SHA256withECDSA
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|headersReceived
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|validateSpecClaim
argument_list|(
name|token
operator|.
name|getClaims
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadJwsSignedByPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
name|JwsJwtCompactConsumer
name|jws
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|ENCODED_TOKEN_SIGNED_BY_PRIVATE_KEY
argument_list|)
decl_stmt|;
name|RSAPublicKey
name|key
init|=
name|CryptoUtils
operator|.
name|getRSAPublicKey
argument_list|(
name|RSA_MODULUS_ENCODED
argument_list|,
name|RSA_PUBLIC_EXPONENT_ENCODED
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jws
operator|.
name|verifySignatureWith
argument_list|(
operator|new
name|PublicKeyJwsSignatureVerifier
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
name|jws
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|JwtHeaders
name|headers
init|=
name|token
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Algorithm
operator|.
name|SHA256withRSA
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|headers
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|validateSpecClaim
argument_list|(
name|token
operator|.
name|getClaims
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JwsCompactProducer
name|initSpecJwtTokenWriter
parameter_list|(
name|JwtHeaders
name|headers
parameter_list|)
throws|throws
name|Exception
block|{
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"joe"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
literal|1300819380L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setClaim
argument_list|(
literal|"http://example.com/is_root"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|headers
argument_list|,
name|claims
argument_list|)
decl_stmt|;
return|return
operator|new
name|JwsJwtCompactProducer
argument_list|(
name|token
argument_list|,
name|getWriter
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|JwtTokenWriter
name|getWriter
parameter_list|()
block|{
name|JwtTokenReaderWriter
name|jsonWriter
init|=
operator|new
name|JwtTokenReaderWriter
argument_list|()
decl_stmt|;
name|jsonWriter
operator|.
name|setFormat
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|jsonWriter
return|;
block|}
block|}
end_class

end_unit

