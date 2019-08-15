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
name|rt
operator|.
name|security
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
operator|.
name|AuthorizationCodeParameters
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  * Some tests for public clients.  */
end_comment

begin_class
specifier|public
class|class
name|PublicClientTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JCACHE_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-oauth2-grants-jcache-public"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JCACHE_PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
literal|"jaxrs-oauth2-grants2-jcache-public"
argument_list|)
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
name|BookServerOAuth2GrantsJCache
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
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testAuthorizationCodeGrant
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|PublicClientTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"publicclient.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
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
comment|// Now get the access token - note services2 doesn't require basic auth
name|String
name|address2
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
operator|+
literal|"/services2/"
decl_stmt|;
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address2
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
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testPKCEPlain
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|PublicClientTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"publicclient.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
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
name|AuthorizationCodeParameters
name|parameters
init|=
operator|new
name|AuthorizationCodeParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setConsumerId
argument_list|(
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|String
name|codeVerifier
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|32
argument_list|)
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|setCodeChallenge
argument_list|(
name|codeVerifier
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setCodeChallengeMethod
argument_list|(
literal|"plain"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setResponseType
argument_list|(
literal|"code"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPath
argument_list|(
literal|"authorize/"
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|OAuth2TestUtils
operator|.
name|getLocation
argument_list|(
name|client
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getSubstring
argument_list|(
name|location
argument_list|,
literal|"code"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token - note services2 doesn't require basic auth
name|String
name|address2
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
operator|+
literal|"/services2/"
decl_stmt|;
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address2
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
argument_list|,
literal|"consumer-id"
argument_list|,
literal|null
argument_list|,
name|codeVerifier
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
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testPKCEPlainMissingVerifier
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|PublicClientTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"publicclient.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
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
name|AuthorizationCodeParameters
name|parameters
init|=
operator|new
name|AuthorizationCodeParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setConsumerId
argument_list|(
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|String
name|codeVerifier
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|32
argument_list|)
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|setCodeChallenge
argument_list|(
name|codeVerifier
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setCodeChallengeMethod
argument_list|(
literal|"plain"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setResponseType
argument_list|(
literal|"code"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPath
argument_list|(
literal|"authorize/"
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|OAuth2TestUtils
operator|.
name|getLocation
argument_list|(
name|client
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getSubstring
argument_list|(
name|location
argument_list|,
literal|"code"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token - note services2 doesn't require basic auth
name|String
name|address2
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
operator|+
literal|"/services2/"
decl_stmt|;
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address2
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
try|try
block|{
name|OAuth2TestUtils
operator|.
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|client
argument_list|,
name|code
argument_list|,
literal|"consumer-id"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a missing verifier"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testPKCEPlainDifferentVerifier
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|PublicClientTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"publicclient.xml"
argument_list|)
decl_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
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
name|AuthorizationCodeParameters
name|parameters
init|=
operator|new
name|AuthorizationCodeParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setConsumerId
argument_list|(
literal|"consumer-id"
argument_list|)
expr_stmt|;
name|String
name|codeVerifier
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|32
argument_list|)
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|setCodeChallenge
argument_list|(
name|codeVerifier
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setCodeChallengeMethod
argument_list|(
literal|"plain"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setResponseType
argument_list|(
literal|"code"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPath
argument_list|(
literal|"authorize/"
argument_list|)
expr_stmt|;
name|String
name|location
init|=
name|OAuth2TestUtils
operator|.
name|getLocation
argument_list|(
name|client
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|String
name|code
init|=
name|OAuth2TestUtils
operator|.
name|getSubstring
argument_list|(
name|location
argument_list|,
literal|"code"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
comment|// Now get the access token - note services2 doesn't require basic auth
name|String
name|address2
init|=
literal|"https://localhost:"
operator|+
name|JCACHE_PORT
operator|+
literal|"/services2/"
decl_stmt|;
name|client
operator|=
name|WebClient
operator|.
name|create
argument_list|(
name|address2
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
try|try
block|{
name|codeVerifier
operator|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
literal|32
argument_list|)
argument_list|)
expr_stmt|;
name|OAuth2TestUtils
operator|.
name|getAccessTokenWithAuthorizationCode
argument_list|(
name|client
argument_list|,
name|code
argument_list|,
literal|"consumer-id"
argument_list|,
literal|null
argument_list|,
name|codeVerifier
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a missing verifier"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
comment|//
comment|// Server implementations
comment|//
specifier|public
specifier|static
class|class
name|BookServerOAuth2GrantsJCache
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|private
specifier|static
specifier|final
name|URL
name|SERVER_CONFIG_FILE
init|=
name|BookServerOAuth2GrantsJCache
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"grants-server-public.xml"
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
name|BookServerOAuth2GrantsJCache
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

