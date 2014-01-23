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
name|utils
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
name|List
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
name|jaxrs
operator|.
name|provider
operator|.
name|json
operator|.
name|JSONProvider
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
name|AccessTokenRegistration
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
name|Client
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
name|OAuthPermission
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
name|ServerAccessToken
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
name|UserSubject
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
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
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
name|tokens
operator|.
name|refresh
operator|.
name|RefreshToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
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
name|EncryptionUtilsTest
extends|extends
name|Assert
block|{
specifier|private
name|EncryptingDataProvider
name|p
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|p
operator|=
operator|new
name|EncryptingDataProvider
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|p
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptDecryptToken
parameter_list|()
throws|throws
name|Exception
block|{
name|AccessTokenRegistration
name|atr
init|=
name|prepareTokenRegistration
argument_list|()
decl_stmt|;
comment|// encrypt
name|ServerAccessToken
name|token
init|=
name|p
operator|.
name|createAccessToken
argument_list|(
name|atr
argument_list|)
decl_stmt|;
comment|// decrypt
name|ServerAccessToken
name|token2
init|=
name|p
operator|.
name|getAccessToken
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
comment|// compare tokens
name|compareAccessTokens
argument_list|(
name|token
argument_list|,
name|token2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBearerTokenJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|AccessTokenRegistration
name|atr
init|=
name|prepareTokenRegistration
argument_list|()
decl_stmt|;
name|BearerAccessToken
name|token
init|=
name|p
operator|.
name|createAccessTokenInternal
argument_list|(
name|atr
argument_list|)
decl_stmt|;
name|JSONProvider
argument_list|<
name|BearerAccessToken
argument_list|>
name|jsonp
init|=
operator|new
name|JSONProvider
argument_list|<
name|BearerAccessToken
argument_list|>
argument_list|()
decl_stmt|;
name|jsonp
operator|.
name|setMarshallAsJaxbElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jsonp
operator|.
name|setUnmarshallAsJaxbElement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|jsonp
operator|.
name|writeTo
argument_list|(
name|token
argument_list|,
name|BearerAccessToken
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
name|String
name|encrypted
init|=
name|EncryptionUtils
operator|.
name|encryptSequence
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
argument_list|,
name|p
operator|.
name|tokenKey
argument_list|)
decl_stmt|;
name|String
name|decrypted
init|=
name|EncryptionUtils
operator|.
name|decryptSequence
argument_list|(
name|encrypted
argument_list|,
name|p
operator|.
name|tokenKey
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|token2
init|=
name|jsonp
operator|.
name|readFrom
argument_list|(
name|BearerAccessToken
operator|.
name|class
argument_list|,
name|BearerAccessToken
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
name|decrypted
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// compare tokens
name|compareAccessTokens
argument_list|(
name|token
argument_list|,
name|token2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|compareAccessTokens
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|,
name|ServerAccessToken
name|token2
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|token2
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|token2
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getIssuedAt
argument_list|()
argument_list|,
name|token2
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getExpiresIn
argument_list|()
argument_list|,
name|token2
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
name|Client
name|regClient1
init|=
name|token
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|Client
name|regClient2
init|=
name|token2
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|regClient1
operator|.
name|getClientId
argument_list|()
argument_list|,
name|regClient2
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|UserSubject
name|endUser1
init|=
name|token
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|UserSubject
name|endUser2
init|=
name|token2
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|endUser1
operator|.
name|getLogin
argument_list|()
argument_list|,
name|endUser2
operator|.
name|getLogin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endUser1
operator|.
name|getId
argument_list|()
argument_list|,
name|endUser2
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endUser1
operator|.
name|getRoles
argument_list|()
argument_list|,
name|endUser2
operator|.
name|getRoles
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|token2
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getAudience
argument_list|()
argument_list|,
name|token2
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getGrantType
argument_list|()
argument_list|,
name|token2
operator|.
name|getGrantType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|token
operator|.
name|getParameters
argument_list|()
argument_list|,
name|token2
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
init|=
name|token
operator|.
name|getScopes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions2
init|=
name|token2
operator|.
name|getScopes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|permissions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|permissions2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthPermission
name|perm1
init|=
name|permissions
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|OAuthPermission
name|perm2
init|=
name|permissions2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|perm1
operator|.
name|getPermission
argument_list|()
argument_list|,
name|perm2
operator|.
name|getPermission
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|perm1
operator|.
name|getDescription
argument_list|()
argument_list|,
name|perm2
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|RefreshToken
name|refreshToken
init|=
name|ModelEncryptionSupport
operator|.
name|decryptRefreshToken
argument_list|(
name|p
argument_list|,
name|token2
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|p
operator|.
name|tokenKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1200L
argument_list|,
name|refreshToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|AccessTokenRegistration
name|prepareTokenRegistration
parameter_list|()
block|{
name|AccessTokenRegistration
name|atr
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|Client
name|regClient
init|=
name|p
operator|.
name|getClient
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|atr
operator|.
name|setClient
argument_list|(
name|regClient
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setGrantType
argument_list|(
literal|"code"
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setAudience
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|UserSubject
name|endUser
init|=
operator|new
name|UserSubject
argument_list|(
literal|"Barry"
argument_list|,
literal|"BarryId"
argument_list|)
decl_stmt|;
name|atr
operator|.
name|setSubject
argument_list|(
name|endUser
argument_list|)
expr_stmt|;
name|endUser
operator|.
name|setRoles
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"role1"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|atr
return|;
block|}
block|}
end_class

end_unit

