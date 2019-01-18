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
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|impl
operator|.
name|MetadataMap
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
name|common
operator|.
name|ClientAccessToken
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
name|common
operator|.
name|TokenIntrospection
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
name|OAuthConstants
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|OAuthJSONProviderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReadException
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|response
init|=
literal|"{"
operator|+
literal|"\"error\":\"invalid_client\","
operator|+
literal|"\"error_description\":\"Client authentication failed\""
operator|+
literal|"}"
decl_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseMap
init|=
name|provider
operator|.
name|readJSONResponse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|responseMap
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"invalid_client"
argument_list|,
name|responseMap
operator|.
name|get
argument_list|(
literal|"error"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Client authentication failed"
argument_list|,
name|responseMap
operator|.
name|get
argument_list|(
literal|"error_description"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadExceptionWithCommaInMessage
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|response
init|=
literal|"{"
operator|+
literal|"\"error\":\"invalid_client\","
operator|+
literal|"\"error_description\":\"Client authentication failed, due to xyz\""
operator|+
literal|"}"
decl_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|responseMap
init|=
name|provider
operator|.
name|readJSONResponse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|responseMap
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"invalid_client"
argument_list|,
name|responseMap
operator|.
name|get
argument_list|(
literal|"error"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Client authentication failed, due to xyz"
argument_list|,
name|responseMap
operator|.
name|get
argument_list|(
literal|"error_description"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteBearerClientAccessToken
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientAccessToken
name|token
init|=
operator|new
name|ClientAccessToken
argument_list|(
name|OAuthConstants
operator|.
name|BEARER_TOKEN_TYPE
argument_list|,
literal|"1234"
argument_list|)
decl_stmt|;
name|token
operator|.
name|setExpiresIn
argument_list|(
literal|12345
argument_list|)
expr_stmt|;
name|token
operator|.
name|setRefreshToken
argument_list|(
literal|"5678"
argument_list|)
expr_stmt|;
name|token
operator|.
name|setApprovedScope
argument_list|(
literal|"read"
argument_list|)
expr_stmt|;
name|token
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"my_parameter"
argument_list|,
literal|"http://abc"
argument_list|)
argument_list|)
expr_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
name|token
argument_list|,
name|ClientAccessToken
operator|.
name|class
argument_list|,
name|ClientAccessToken
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|doReadClientAccessToken
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|,
name|OAuthConstants
operator|.
name|BEARER_TOKEN_TYPE
argument_list|,
name|token
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadBearerClientAccessToken
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
literal|"{"
operator|+
literal|"\"access_token\":\"1234\","
operator|+
literal|"\"token_type\":\"bearer\","
operator|+
literal|"\"refresh_token\":\"5678\","
operator|+
literal|"\"expires_in\":12345,"
operator|+
literal|"\"scope\":\"read\","
operator|+
literal|"\"my_parameter\":\"http://abc\""
operator|+
literal|"}"
decl_stmt|;
name|doReadClientAccessToken
argument_list|(
name|response
argument_list|,
name|OAuthConstants
operator|.
name|BEARER_TOKEN_TYPE
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"my_parameter"
argument_list|,
literal|"http://abc"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|public
name|void
name|testReadTokenIntrospection
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
literal|"{\"active\":true,\"client_id\":\"WjcK94pnec7CyA\",\"username\":\"alice\",\"token_type\":\"Bearer\""
operator|+
literal|",\"scope\":\"a\",\"aud\":\"https://localhost:8082/service\","
operator|+
literal|"\"iat\":1453472181,\"exp\":1453475781}"
decl_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|TokenIntrospection
name|t
init|=
operator|(
name|TokenIntrospection
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|TokenIntrospection
operator|.
name|class
argument_list|,
name|TokenIntrospection
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|t
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"WjcK94pnec7CyA"
argument_list|,
name|t
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|t
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|t
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:8082/service"
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1453472181L
argument_list|,
name|t
operator|.
name|getIat
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1453475781L
argument_list|,
name|t
operator|.
name|getExp
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|public
name|void
name|testReadTokenIntrospectionMultipleAuds
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
literal|"{\"active\":true,\"client_id\":\"WjcK94pnec7CyA\",\"username\":\"alice\",\"token_type\":\"Bearer\""
operator|+
literal|",\"scope\":\"a\",\"aud\":[\"https://localhost:8082/service\",\"https://localhost:8083/service\"],"
operator|+
literal|"\"iat\":1453472181,\"exp\":1453475781}"
decl_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|TokenIntrospection
name|t
init|=
operator|(
name|TokenIntrospection
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|TokenIntrospection
operator|.
name|class
argument_list|,
name|TokenIntrospection
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|t
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"WjcK94pnec7CyA"
argument_list|,
name|t
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|t
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|t
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:8082/service"
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:8083/service"
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1453472181L
argument_list|,
name|t
operator|.
name|getIat
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1453475781L
argument_list|,
name|t
operator|.
name|getExp
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|public
name|void
name|testReadTokenIntrospectionSingleAudAsArray
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
literal|"{\"active\":false,\"client_id\":\"WjcK94pnec7CyA\",\"username\":\"alice\",\"token_type\":\"Bearer\""
operator|+
literal|",\"scope\":\"a\",\"aud\":[\"https://localhost:8082/service\"],"
operator|+
literal|"\"iat\":1453472181,\"exp\":1453475781}"
decl_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|TokenIntrospection
name|t
init|=
operator|(
name|TokenIntrospection
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|TokenIntrospection
operator|.
name|class
argument_list|,
name|TokenIntrospection
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|t
operator|.
name|isActive
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"WjcK94pnec7CyA"
argument_list|,
name|t
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|t
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|t
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:8082/service"
argument_list|,
name|t
operator|.
name|getAud
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1453472181L
argument_list|,
name|t
operator|.
name|getIat
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1453475781L
argument_list|,
name|t
operator|.
name|getExp
argument_list|()
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
specifier|public
name|ClientAccessToken
name|doReadClientAccessToken
parameter_list|(
name|String
name|response
parameter_list|,
name|String
name|expectedTokenType
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|expectedParams
parameter_list|)
throws|throws
name|Exception
block|{
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|ClientAccessToken
name|token
init|=
operator|(
name|ClientAccessToken
operator|)
name|provider
operator|.
name|readFrom
argument_list|(
operator|(
name|Class
operator|)
name|ClientAccessToken
operator|.
name|class
argument_list|,
name|ClientAccessToken
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1234"
argument_list|,
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expectedTokenType
operator|.
name|equalsIgnoreCase
argument_list|(
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5678"
argument_list|,
name|token
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12345
argument_list|,
name|token
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"read"
argument_list|,
name|token
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParams
init|=
name|token
operator|.
name|getParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedParams
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|expectedParams
argument_list|,
name|extraParams
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"http://abc"
argument_list|,
name|extraParams
operator|.
name|get
argument_list|(
literal|"my_parameter"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWriteHawkClientAccessToken
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientAccessToken
name|token
init|=
operator|new
name|ClientAccessToken
argument_list|(
literal|"hawk"
argument_list|,
literal|"1234"
argument_list|)
decl_stmt|;
name|token
operator|.
name|setExpiresIn
argument_list|(
literal|12345
argument_list|)
expr_stmt|;
name|token
operator|.
name|setRefreshToken
argument_list|(
literal|"5678"
argument_list|)
expr_stmt|;
name|token
operator|.
name|setApprovedScope
argument_list|(
literal|"read"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_KEY
argument_list|,
literal|"test_mac_secret"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_ALGORITHM
argument_list|,
name|OAuthConstants
operator|.
name|HMAC_ALGO_SHA_1
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"my_parameter"
argument_list|,
literal|"http://abc"
argument_list|)
expr_stmt|;
name|token
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|OAuthJSONProvider
name|provider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|provider
operator|.
name|writeTo
argument_list|(
name|token
argument_list|,
name|ClientAccessToken
operator|.
name|class
argument_list|,
name|ClientAccessToken
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|,
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|doReadClientAccessToken
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|,
name|OAuthConstants
operator|.
name|HAWK_TOKEN_TYPE
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReadHawkClientAccessToken
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|response
init|=
literal|"{"
operator|+
literal|"\"access_token\":\"1234\","
operator|+
literal|"\"token_type\":\"hawk\","
operator|+
literal|"\"refresh_token\":\"5678\","
operator|+
literal|"\"expires_in\":12345,"
operator|+
literal|"\"scope\":\"read\","
operator|+
literal|"\"secret\":\"adijq39jdlaska9asud\","
operator|+
literal|"\"algorithm\":\"hmac-sha-256\","
operator|+
literal|"\"my_parameter\":\"http://abc\""
operator|+
literal|"}"
decl_stmt|;
name|ClientAccessToken
name|macToken
init|=
name|doReadClientAccessToken
argument_list|(
name|response
argument_list|,
literal|"hawk"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"adijq39jdlaska9asud"
argument_list|,
name|macToken
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hmac-sha-256"
argument_list|,
name|macToken
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|OAuthConstants
operator|.
name|HAWK_TOKEN_ALGORITHM
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

