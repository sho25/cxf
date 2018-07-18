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
name|grants
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
comment|/**  * Some unit tests for the token introspection service in CXF. The tests are run multiple times with different  * OAuthDataProvider implementations:  * a) PORT - EhCache  * b) JWT_PORT - EhCache with useJwtFormatForAccessTokens enabled  */
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
name|IntrospectionServiceTest
extends|extends
name|AbstractBusClientServerTestBase
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
literal|"jaxrs-oauth2-introspection"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-oauth2-introspection2"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWT_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-oauth2-introspection-jwt"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JWT_PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-oauth2-introspection2-jwt"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|port
decl_stmt|;
specifier|public
name|IntrospectionServiceTest
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
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerOAuth2Introspection
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
name|BookServerOAuth2IntrospectionJWT
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
name|PORT
argument_list|,
name|JWT_PORT
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
name|testTokenIntrospection
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|IntrospectionServiceTest
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
literal|"/services/"
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
comment|// Now query the token introspection service
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
name|client
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
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
literal|"token"
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"introspect/"
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
name|TokenIntrospection
name|tokenIntrospection
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|TokenIntrospection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|isActive
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getUsername
argument_list|()
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getClientId
argument_list|()
argument_list|,
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getScope
argument_list|()
argument_list|,
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|validity
init|=
name|tokenIntrospection
operator|.
name|getExp
argument_list|()
operator|-
name|tokenIntrospection
operator|.
name|getIat
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|validity
operator|==
name|accessToken
operator|.
name|getExpiresIn
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
name|testTokenIntrospectionWithAudience
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|AuthorizationGrantTest
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
literal|"/services/"
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
literal|null
argument_list|,
literal|"consumer-id-aud"
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
literal|"consumer-id-aud"
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
name|String
name|audPort
init|=
name|PORT2
decl_stmt|;
if|if
condition|(
name|JWT_PORT
operator|.
name|equals
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|audPort
operator|=
name|JWT_PORT2
expr_stmt|;
block|}
name|String
name|audience
init|=
literal|"https://localhost:"
operator|+
name|audPort
operator|+
literal|"/secured/bookstore/books"
decl_stmt|;
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
argument_list|,
literal|"consumer-id-aud"
argument_list|,
name|audience
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
comment|// Now query the token introspection service
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
name|client
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
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
literal|"token"
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"introspect/"
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
name|TokenIntrospection
name|tokenIntrospection
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|TokenIntrospection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|isActive
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getUsername
argument_list|()
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getClientId
argument_list|()
argument_list|,
literal|"consumer-id-aud"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getScope
argument_list|()
argument_list|,
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|validity
init|=
name|tokenIntrospection
operator|.
name|getExp
argument_list|()
operator|-
name|tokenIntrospection
operator|.
name|getIat
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|validity
operator|==
name|accessToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getAud
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|audience
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
name|testInvalidToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|IntrospectionServiceTest
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
literal|"/services/"
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
comment|// Now query the token introspection service
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
name|client
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
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
literal|"token"
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
operator|+
literal|"-xyz"
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"introspect/"
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
name|TokenIntrospection
name|tokenIntrospection
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|TokenIntrospection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|isActive
argument_list|()
argument_list|,
literal|false
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
name|testRefreshedToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|AuthorizationGrantTest
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
literal|"/services/"
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
name|assertNotNull
argument_list|(
name|accessToken
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|originalAccessToken
init|=
name|accessToken
operator|.
name|getTokenKey
argument_list|()
decl_stmt|;
comment|// Refresh the access token
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
literal|"refresh_token"
argument_list|)
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"refresh_token"
argument_list|,
name|accessToken
operator|.
name|getRefreshToken
argument_list|()
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
name|accessToken
operator|=
name|response
operator|.
name|readEntity
argument_list|(
name|ClientAccessToken
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|accessToken
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now query the token introspection service
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
name|client
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|type
argument_list|(
literal|"application/x-www-form-urlencoded"
argument_list|)
expr_stmt|;
comment|// Refreshed token should be ok
name|form
operator|=
operator|new
name|Form
argument_list|()
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"token"
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"introspect/"
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
name|TokenIntrospection
name|tokenIntrospection
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|TokenIntrospection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|isActive
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Original token should not be ok
name|form
operator|=
operator|new
name|Form
argument_list|()
expr_stmt|;
name|form
operator|.
name|param
argument_list|(
literal|"token"
argument_list|,
name|originalAccessToken
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
name|tokenIntrospection
operator|=
name|response
operator|.
name|readEntity
argument_list|(
name|TokenIntrospection
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|isActive
argument_list|()
argument_list|,
literal|false
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
name|testTokenIntrospectionWithScope
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|IntrospectionServiceTest
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
literal|"/services/"
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
literal|"read_balance"
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
literal|"read_balance"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Now query the token introspection service
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
name|client
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
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
literal|"token"
argument_list|,
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"introspect/"
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
name|TokenIntrospection
name|tokenIntrospection
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|TokenIntrospection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|isActive
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getUsername
argument_list|()
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getClientId
argument_list|()
argument_list|,
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tokenIntrospection
operator|.
name|getScope
argument_list|()
argument_list|,
name|accessToken
operator|.
name|getApprovedScope
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|validity
init|=
name|tokenIntrospection
operator|.
name|getExp
argument_list|()
operator|-
name|tokenIntrospection
operator|.
name|getIat
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|validity
operator|==
name|accessToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

