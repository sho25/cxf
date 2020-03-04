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
name|URI
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
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
name|oauth2
operator|.
name|client
operator|.
name|AccessTokenClientFilter
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
name|OAuthAuthorizationData
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
name|utils
operator|.
name|OidcUtils
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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oidc
operator|.
name|SpringBusTestServer
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HTTPConduitConfigurer
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

begin_comment
comment|/**  * Some tests for the OIDC filters  */
end_comment

begin_class
specifier|public
class|class
name|OIDCFiltersTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerOIDCFilters
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OIDC_PORT
init|=
name|BookServerOIDCService
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|SpringBusTestServer
name|BOOK_JWK_SERVER
init|=
operator|new
name|SpringBusTestServer
argument_list|(
literal|"filters-jwks-server"
argument_list|)
block|{     }
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
name|createStaticBus
argument_list|()
operator|.
name|setExtension
argument_list|(
name|OAuth2TestUtils
operator|.
name|clientHTTPConduitConfigurer
argument_list|()
argument_list|,
name|HTTPConduitConfigurer
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerOIDCFilters
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerOIDCService
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BOOK_JWK_SERVER
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
name|testClientCodeRequestFilter
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Make an invocation + get back the redirection to the OIDC IdP
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
literal|null
argument_list|)
decl_stmt|;
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
name|Response
name|response
init|=
name|client
operator|.
name|get
argument_list|()
decl_stmt|;
name|URI
name|location
init|=
name|response
operator|.
name|getLocation
argument_list|()
decl_stmt|;
comment|// Now make an invocation on the OIDC IdP using another WebClient instance
name|WebClient
name|idpClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|location
operator|.
name|toString
argument_list|()
argument_list|,
name|OAuth2TestUtils
operator|.
name|setupProviders
argument_list|()
argument_list|,
literal|"bob"
argument_list|,
literal|"security"
argument_list|,
literal|null
argument_list|)
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
decl_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|idpClient
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
comment|// Make initial authorization request
specifier|final
name|OAuthAuthorizationData
name|authzData
init|=
name|idpClient
operator|.
name|get
argument_list|(
name|OAuthAuthorizationData
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Get Authorization Code + State
name|String
name|authzCodeLocation
init|=
name|OAuth2TestUtils
operator|.
name|getLocation
argument_list|(
name|idpClient
argument_list|,
name|authzData
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|state
init|=
name|OAuth2TestUtils
operator|.
name|getSubstring
argument_list|(
name|authzCodeLocation
argument_list|,
literal|"state"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|state
argument_list|)
expr_stmt|;
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getSubstring
argument_list|(
name|authzCodeLocation
argument_list|,
literal|"code"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Add Referer
name|String
name|referer
init|=
literal|"https://localhost:"
operator|+
name|OIDC_PORT
operator|+
literal|"/services/authorize"
decl_stmt|;
name|client
operator|.
name|header
argument_list|(
literal|"Referer"
argument_list|,
name|referer
argument_list|)
expr_stmt|;
comment|// Now invoke back on the service using the authorization code
name|client
operator|.
name|query
argument_list|(
literal|"code"
argument_list|,
name|code
argument_list|)
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"state"
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|Response
name|serviceResponse
init|=
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
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
name|serviceResponse
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
name|serviceResponse
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJwsVerifierRequestFilter
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|request
init|=
literal|"echo"
decl_stmt|;
specifier|final
name|Response
name|response
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"https://localhost:"
operator|+
name|BOOK_JWK_SERVER
operator|.
name|getPort
argument_list|()
operator|+
literal|"/secured/bookstore/books"
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|post
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|UNAUTHORIZED
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|OIDC_PORT
operator|+
literal|"/services/"
decl_stmt|;
name|WebClient
name|idpClient
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
literal|"bob"
argument_list|,
literal|"security"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// Save the Cookie for the second request...
name|WebClient
operator|.
name|getConfig
argument_list|(
name|idpClient
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
name|idpClient
argument_list|,
name|OidcUtils
operator|.
name|getOpenIdScope
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token
name|idpClient
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
literal|"consumer-id"
argument_list|,
literal|"this-is-a-secret"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ClientAccessToken
name|accessToken
init|=
name|OAuth2TestUtils
operator|.
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|idpClient
argument_list|,
name|code
argument_list|)
decl_stmt|;
comment|// Make an invocation
specifier|final
name|AccessTokenClientFilter
name|accessTokenClientFilter
init|=
operator|new
name|AccessTokenClientFilter
argument_list|()
decl_stmt|;
name|accessTokenClientFilter
operator|.
name|setAccessToken
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|echo
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"https://localhost:"
operator|+
name|BOOK_JWK_SERVER
operator|.
name|getPort
argument_list|()
operator|+
literal|"/secured/bookstore/books"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|accessTokenClientFilter
argument_list|)
argument_list|)
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
operator|.
name|post
argument_list|(
name|request
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|request
argument_list|,
name|echo
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Server implementations
comment|//
specifier|public
specifier|static
class|class
name|BookServerOIDCFilters
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-oidc-filters"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|setBus
argument_list|(
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"filters-server.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|BookServerOIDCService
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-filters-oidc-service"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|setBus
argument_list|(
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"oidc-server.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

