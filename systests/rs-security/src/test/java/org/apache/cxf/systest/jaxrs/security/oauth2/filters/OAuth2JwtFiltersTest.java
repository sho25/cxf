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
name|oauth2
operator|.
name|filters
package|;
end_package

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
name|jws
operator|.
name|JwsSignatureVerifier
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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|Book
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
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * Some tests for the OAuth 2.0 filters  */
end_comment

begin_class
specifier|public
class|class
name|OAuth2JwtFiltersTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerOAuth2FiltersJwt
operator|.
name|PORT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OAUTH_PORT
init|=
name|BookServerOAuth2ServiceJwt
operator|.
name|PORT
decl_stmt|;
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
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerOAuth2FiltersJwt
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerOAuth2ServiceJwt
operator|.
name|class
argument_list|,
literal|true
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
name|testServiceWithJwtTokenAndScope
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2JwtFiltersTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
comment|// Get Authorization Code
name|String
name|oauthService
init|=
literal|"https://localhost:"
operator|+
name|OAUTH_PORT
operator|+
literal|"/services/"
decl_stmt|;
name|WebClient
name|oauthClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|oauthService
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
name|oauthClient
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
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getAuthorizationCode
argument_list|(
name|oauthClient
argument_list|,
literal|"create_book"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token
name|oauthClient
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|oauthService
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
name|oauthClient
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
name|oauthClient
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
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|JwsSignatureVerifier
name|verifier
init|=
name|JwsUtils
operator|.
name|loadSignatureVerifier
argument_list|(
literal|"org/apache/cxf/systest/jaxrs/security/alice.rs.properties"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jwtConsumer
operator|.
name|verifySignatureWith
argument_list|(
name|verifier
argument_list|)
argument_list|)
expr_stmt|;
name|JwtClaims
name|claims
init|=
name|jwtConsumer
operator|.
name|getJwtClaims
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"consumer-id"
argument_list|,
name|claims
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"alice"
argument_list|,
name|claims
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now invoke on the service with the access token
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/secured/bookstore/books"
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
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
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
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|Book
name|returnedBook
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|returnedBook
operator|.
name|getName
argument_list|()
argument_list|,
literal|"book"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|returnedBook
operator|.
name|getId
argument_list|()
argument_list|,
literal|123L
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

