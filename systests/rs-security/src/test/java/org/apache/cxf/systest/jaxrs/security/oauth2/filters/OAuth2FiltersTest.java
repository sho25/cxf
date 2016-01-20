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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|UUID
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
name|Form
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthJSONProvider
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
name|OAuth2FiltersTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerOAuth2Filters
operator|.
name|PORT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OAUTH_PORT
init|=
name|BookServerOAuth2Service
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
name|BookServerOAuth2Filters
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
name|BookServerOAuth2Service
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
name|testServiceWithToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
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
name|getAuthorizationCode
argument_list|(
name|oauthClient
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testServiceWithFakeToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
comment|// Now invoke on the service with the faked access token
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
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
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
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testServiceWithNoToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
comment|// Now invoke on the service with the faked access token
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
name|setupProviders
argument_list|()
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
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
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testServiceWithEmptyToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
comment|// Now invoke on the service with the faked access token
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
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testServiceWithTokenAndScope
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testServiceWithTokenAndIncorrectScopeVerb
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
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
name|getAuthorizationCode
argument_list|(
name|oauthClient
argument_list|,
literal|"read_book"
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
comment|// We don't have the scope to post a book here
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
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testServiceWithTokenAndIncorrectScopeURI
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
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
name|getAuthorizationCode
argument_list|(
name|oauthClient
argument_list|,
literal|"create_image"
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
comment|// We don't have the scope to post a book here
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
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testServiceWithTokenAndMultipleScopes
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OAuth2FiltersTest
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
name|getAuthorizationCode
argument_list|(
name|oauthClient
argument_list|,
literal|"read_book create_image create_book"
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
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|setupProviders
parameter_list|()
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|JSONProvider
argument_list|<
name|OAuthAuthorizationData
argument_list|>
name|jsonP
init|=
operator|new
name|JSONProvider
argument_list|<
name|OAuthAuthorizationData
argument_list|>
argument_list|()
decl_stmt|;
name|jsonP
operator|.
name|setNamespaceMap
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"http://org.apache.cxf.rs.security.oauth"
argument_list|,
literal|"ns2"
argument_list|)
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jsonP
argument_list|)
expr_stmt|;
name|OAuthJSONProvider
name|oauthProvider
init|=
operator|new
name|OAuthJSONProvider
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|oauthProvider
argument_list|)
expr_stmt|;
return|return
name|providers
return|;
block|}
specifier|private
name|String
name|getAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|)
block|{
return|return
name|getAuthorizationCode
argument_list|(
name|client
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|String
name|getAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
comment|// Make initial authorization request
name|client
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
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"client_id"
argument_list|,
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"redirect_uri"
argument_list|,
literal|"http://www.blah.apache.org"
argument_list|)
expr_stmt|;
name|client
operator|.
name|query
argument_list|(
literal|"response_type"
argument_list|,
literal|"code"
argument_list|)
expr_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|query
argument_list|(
literal|"scope"
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
name|client
operator|.
name|path
argument_list|(
literal|"authorize/"
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
name|OAuthAuthorizationData
name|authzData
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|OAuthAuthorizationData
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Now call "decision" to get the authorization code grant
name|client
operator|.
name|path
argument_list|(
literal|"decision"
argument_list|)
expr_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
expr_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"session_authenticity_token"
argument_list|,
name|authzData
operator|.
name|getAuthenticityToken
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"client_id"
argument_list|,
name|authzData
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"redirect_uri"
argument_list|,
name|authzData
operator|.
name|getRedirectUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|authzData
operator|.
name|getProposedScope
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|form
operator|.
name|param
argument_list|(
literal|"scope"
argument_list|,
name|authzData
operator|.
name|getProposedScope
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|form
operator|.
name|param
argument_list|(
literal|"oauthDecision"
argument_list|,
literal|"allow"
argument_list|)
expr_stmt|;
name|response
operator|=
name|client
operator|.
name|post
argument_list|(
name|form
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|response
operator|.
name|getHeaderString
argument_list|(
literal|"Location"
argument_list|)
decl_stmt|;
return|return
name|getSubstring
argument_list|(
name|location
argument_list|,
literal|"code"
argument_list|)
return|;
block|}
specifier|private
name|ClientAccessToken
name|getAccessTokenWithAuthorizationCode
parameter_list|(
name|WebClient
name|client
parameter_list|,
name|String
name|code
parameter_list|)
block|{
name|client
operator|.
name|type
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"token"
argument_list|)
expr_stmt|;
name|Form
name|form
init|=
operator|new
name|Form
argument_list|()
decl_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"grant_type"
argument_list|,
literal|"authorization_code"
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"code"
argument_list|,
name|code
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"client_id"
argument_list|,
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
name|form
argument_list|)
decl_stmt|;
return|return
name|response
operator|.
name|readEntity
argument_list|(
name|ClientAccessToken
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|String
name|getSubstring
parameter_list|(
name|String
name|parentString
parameter_list|,
name|String
name|substringName
parameter_list|)
block|{
name|String
name|foundString
init|=
name|parentString
operator|.
name|substring
argument_list|(
name|parentString
operator|.
name|indexOf
argument_list|(
name|substringName
operator|+
literal|"="
argument_list|)
operator|+
operator|(
name|substringName
operator|+
literal|"="
operator|)
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|ampersandIndex
init|=
name|foundString
operator|.
name|indexOf
argument_list|(
literal|'&'
argument_list|)
decl_stmt|;
if|if
condition|(
name|ampersandIndex
operator|<
literal|1
condition|)
block|{
name|ampersandIndex
operator|=
name|foundString
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
return|return
name|foundString
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ampersandIndex
argument_list|)
return|;
block|}
block|}
end_class

end_unit

