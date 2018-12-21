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
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerOIDCFilters
operator|.
name|PORT
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OIDC_PORT
init|=
name|BookServerOIDCService
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
name|BookServerOIDCFilters
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
name|BookServerOIDCService
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
name|testClientCodeRequestFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|OIDCFiltersTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
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
name|busFile
operator|.
name|toString
argument_list|()
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
comment|// Now make an invocation on the OIDC IdP using another WebClient instance
name|WebClient
name|idpClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|location
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
comment|// Get Authorization Code + State
name|String
name|authzCodeLocation
init|=
name|makeAuthorizationCodeInvocation
argument_list|(
name|idpClient
argument_list|)
decl_stmt|;
name|String
name|state
init|=
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
specifier|private
name|String
name|makeAuthorizationCodeInvocation
parameter_list|(
name|WebClient
name|client
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
literal|"state"
argument_list|,
name|authzData
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
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
return|return
name|response
operator|.
name|getHeaderString
argument_list|(
literal|"Location"
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

