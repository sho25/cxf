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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oidc
package|;
end_package

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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

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
name|cert
operator|.
name|Certificate
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
name|CertificateException
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Response
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|client
operator|.
name|WebClient
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
name|jwe
operator|.
name|JweJwtCompactConsumer
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
name|JwsJwtCompactConsumer
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
name|jose
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
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
name|oidc
operator|.
name|common
operator|.
name|UserInfo
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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|SecurityTestUtil
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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|OAuth2TestUtils
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the UserInfo Service in OpenId Connect. This can be used to return the User's claims given  * an access token. The tests are run multiple times with different OAuthDataProvider implementations:  * a) JCACHE_PORT - JCache  * b) JWT_JCACHE_PORT - JCache with useJwtFormatForAccessTokens enabled  * c) JPA_PORT - JPA provider  * d) JWT_NON_PERSIST_JCACHE_PORT-  JCache with useJwtFormatForAccessTokens + !persistJwtEncoding  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|UserInfoTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|JCACHE_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-userinfo-jcache"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|JCACHE_JWT_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-userinfo-jcache-jwt"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|JPA_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-userinfo-jpa"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|JWT_NON_PERSIST_JCACHE_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-userinfo-jcache-jwt-non-persist"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|port
decl_stmt|;
specifier|public
name|UserInfoTest
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|UserInfoServerJCache
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|UserInfoServerJCacheJWT
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|UserInfoServerJPA
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|UserInfoServerJCacheJWTNonPersist
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
specifier|public
specifier|static
name|Collection
argument_list|<
name|String
argument_list|>
name|data
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|JCACHE_PORT
argument_list|,
name|JCACHE_JWT_PORT
argument_list|,
name|JPA_PORT
argument_list|,
name|JWT_NON_PERSIST_JCACHE_PORT
argument_list|)
return|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testPlainUserInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|UserInfoTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|port
operator|+
literal|"/services/oidc"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"alice"
argument_list|,
literal|"security"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// Get Authorization Code
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
literal|"openid"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"consumer-id"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ClientAccessToken
name|accessToken
init|=
name|OAuth2TestUtils
operator|.
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|client
argument_list|,
name|code
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
operator|.
name|contains
argument_list|(
literal|"openid"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|idToken
init|=
name|accessToken
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"id_token"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
name|validateIdToken
argument_list|(
name|idToken
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Now invoke on the UserInfo service with the access token
name|String
name|userInfoAddress
init|=
literal|"https://localhost:"
operator|+
name|port
operator|+
literal|"/services/plain/userinfo"
decl_stmt|;
name|WebClient
name|userInfoClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|userInfoAddress
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|userInfoClient
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|userInfoClient
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
literal|"Bearer "
operator|+
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|serviceResponse
init|=
name|userInfoClient
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|serviceResponse
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|UserInfo
name|userInfo
init|=
name|serviceResponse
operator|.
name|readEntity
argument_list|(
name|UserInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|userInfo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|userInfo
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"consumer-id"
argument_list|,
name|userInfo
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSignedUserInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|UserInfoTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|port
operator|+
literal|"/services/oidc"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"alice"
argument_list|,
literal|"security"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// Get Authorization Code
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
literal|"openid"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"consumer-id"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ClientAccessToken
name|accessToken
init|=
name|OAuth2TestUtils
operator|.
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|client
argument_list|,
name|code
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
operator|.
name|contains
argument_list|(
literal|"openid"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|idToken
init|=
name|accessToken
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"id_token"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
name|validateIdToken
argument_list|(
name|idToken
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Now invoke on the UserInfo service with the access token
name|String
name|userInfoAddress
init|=
literal|"https://localhost:"
operator|+
name|port
operator|+
literal|"/services/signed/userinfo"
decl_stmt|;
name|WebClient
name|userInfoClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|userInfoAddress
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|userInfoClient
operator|.
name|accept
argument_list|(
literal|"application/jwt"
argument_list|)
expr_stmt|;
name|userInfoClient
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
literal|"Bearer "
operator|+
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|serviceResponse
init|=
name|userInfoClient
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|serviceResponse
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|String
name|token
init|=
name|serviceResponse
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"consumer-id"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|)
argument_list|)
expr_stmt|;
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keystore
operator|.
name|load
argument_list|(
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/alice.jks"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|Certificate
name|cert
init|=
name|keystore
operator|.
name|getCertificate
argument_list|(
literal|"alice"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|cert
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|jwtConsumer
operator|.
name|verifySignatureWith
argument_list|(
operator|(
name|X509Certificate
operator|)
name|cert
argument_list|,
name|SignatureAlgorithm
operator|.
name|RS256
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testEncryptedUserInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|UserInfoTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|port
operator|+
literal|"/services/oidc"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"alice"
argument_list|,
literal|"security"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// Get Authorization Code
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
literal|"openid"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"consumer-id"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MAINTAIN_SESSION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ClientAccessToken
name|accessToken
init|=
name|OAuth2TestUtils
operator|.
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|client
argument_list|,
name|code
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
operator|.
name|contains
argument_list|(
literal|"openid"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|idToken
init|=
name|accessToken
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|"id_token"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
name|validateIdToken
argument_list|(
name|idToken
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Now invoke on the UserInfo service with the access token
name|String
name|userInfoAddress
init|=
literal|"https://localhost:"
operator|+
name|port
operator|+
literal|"/services/encrypted/userinfo"
decl_stmt|;
name|WebClient
name|userInfoClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|userInfoAddress
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|userInfoClient
operator|.
name|accept
argument_list|(
literal|"application/jwt"
argument_list|)
expr_stmt|;
name|userInfoClient
operator|.
name|header
argument_list|(
literal|"Authorization"
argument_list|,
literal|"Bearer "
operator|+
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|serviceResponse
init|=
name|userInfoClient
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|serviceResponse
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|token
init|=
name|serviceResponse
operator|.
name|readEntity
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keystore
operator|.
name|load
argument_list|(
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/alice.jks"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|JweJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JweJwtCompactConsumer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
literal|"alice"
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|decryptWith
argument_list|(
name|privateKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"consumer-id"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateIdToken
parameter_list|(
name|String
name|idToken
parameter_list|,
name|String
name|nonce
parameter_list|)
throws|throws
name|KeyStoreException
throws|,
name|NoSuchAlgorithmException
throws|,
name|CertificateException
throws|,
name|IOException
block|{
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|idToken
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
comment|// Validate claims
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"OIDC IdP"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUER
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"consumer-id"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_EXPIRY
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|nonce
operator|!=
literal|null
condition|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|nonce
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|IdToken
operator|.
name|NONCE_CLAIM
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keystore
operator|.
name|load
argument_list|(
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/alice.jks"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|Certificate
name|cert
init|=
name|keystore
operator|.
name|getCertificate
argument_list|(
literal|"alice"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|cert
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|jwtConsumer
operator|.
name|verifySignatureWith
argument_list|(
operator|(
name|X509Certificate
operator|)
name|cert
argument_list|,
name|SignatureAlgorithm
operator|.
name|RS256
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Server implementations
comment|//
specifier|public
specifier|static
class|class
name|UserInfoServerJCache
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
specifier|static
specifier|final
name|URL
name|SERVER_CONFIG_FILE
init|=
name|UserInfoServerJCache
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"userinfo-server-jcache.xml"
argument_list|)
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|SERVER_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|UserInfoServerJCache
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|UserInfoServerJCacheJWT
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
specifier|static
specifier|final
name|URL
name|SERVER_CONFIG_FILE
init|=
name|UserInfoServerJCacheJWT
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"userinfo-server-jcache-jwt.xml"
argument_list|)
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|SERVER_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|UserInfoServerJCacheJWT
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|UserInfoServerJPA
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
specifier|static
specifier|final
name|URL
name|SERVER_CONFIG_FILE
init|=
name|UserInfoServerJPA
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"userinfo-server-jpa.xml"
argument_list|)
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|SERVER_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|UserInfoServerJPA
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|UserInfoServerJCacheJWTNonPersist
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
specifier|static
specifier|final
name|URL
name|SERVER_CONFIG_FILE
init|=
name|UserInfoServerJCacheJWTNonPersist
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"userinfo-server-jcache-jwt-non-persist.xml"
argument_list|)
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|SERVER_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|UserInfoServerJCacheJWTNonPersist
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

