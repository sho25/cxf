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
name|HashMap
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|JAXRSClientFactoryBean
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
name|common
operator|.
name|CryptoLoader
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
name|auth
operator|.
name|saml
operator|.
name|Saml2BearerAuthOutInterceptor
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
name|OAuthClientUtils
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
name|AccessTokenGrant
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
name|grants
operator|.
name|saml
operator|.
name|Saml2BearerGrant
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
name|saml
operator|.
name|Constants
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
name|apache
operator|.
name|cxf
operator|.
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|SAMLUtils
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
name|saml
operator|.
name|SAMLUtils
operator|.
name|SelfSignInfo
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
name|wss4j
operator|.
name|common
operator|.
name|crypto
operator|.
name|Crypto
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSOAuth2Test
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerOAuth2
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CRYPTO_RESOURCE_PROPERTIES
init|=
literal|"org/apache/cxf/systest/jaxrs/security/alice.properties"
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
name|BookServerOAuth2
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSAML2BearerGrant
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/oauth2/token"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|Crypto
name|crypto
init|=
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|loadCrypto
argument_list|(
name|CRYPTO_RESOURCE_PROPERTIES
argument_list|)
decl_stmt|;
name|SelfSignInfo
name|signInfo
init|=
operator|new
name|SelfSignInfo
argument_list|(
name|crypto
argument_list|,
literal|"alice"
argument_list|,
literal|"password"
argument_list|)
decl_stmt|;
name|String
name|assertion
init|=
name|SAMLUtils
operator|.
name|createAssertion
argument_list|(
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|,
name|signInfo
argument_list|)
operator|.
name|assertionToString
argument_list|()
decl_stmt|;
name|Saml2BearerGrant
name|grant
init|=
operator|new
name|Saml2BearerGrant
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
name|ClientAccessToken
name|at
init|=
name|OAuthClientUtils
operator|.
name|getAccessToken
argument_list|(
name|wc
argument_list|,
operator|new
name|OAuthClientUtils
operator|.
name|Consumer
argument_list|(
literal|"alice"
argument_list|,
literal|"alice"
argument_list|)
argument_list|,
name|grant
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSAML2BearerAuthenticationDirect
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/oauth2-auth/token"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|Crypto
name|crypto
init|=
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|loadCrypto
argument_list|(
name|CRYPTO_RESOURCE_PROPERTIES
argument_list|)
decl_stmt|;
name|SelfSignInfo
name|signInfo
init|=
operator|new
name|SelfSignInfo
argument_list|(
name|crypto
argument_list|,
literal|"alice"
argument_list|,
literal|"password"
argument_list|)
decl_stmt|;
name|String
name|assertion
init|=
name|SAMLUtils
operator|.
name|createAssertion
argument_list|(
operator|new
name|SamlCallbackHandler2
argument_list|()
argument_list|,
name|signInfo
argument_list|)
operator|.
name|assertionToString
argument_list|()
decl_stmt|;
name|String
name|encodedAssertion
init|=
name|Base64UrlUtility
operator|.
name|encode
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|extraParams
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_TYPE
argument_list|,
name|Constants
operator|.
name|CLIENT_AUTH_SAML2_BEARER
argument_list|)
expr_stmt|;
name|extraParams
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|CLIENT_AUTH_ASSERTION_PARAM
argument_list|,
name|encodedAssertion
argument_list|)
expr_stmt|;
name|ClientAccessToken
name|at
init|=
name|OAuthClientUtils
operator|.
name|getAccessToken
argument_list|(
name|wc
argument_list|,
operator|new
name|CustomGrant
argument_list|()
argument_list|,
name|extraParams
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoWayTLSAuthentication
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/oauth2/token"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|ClientAccessToken
name|at
init|=
name|OAuthClientUtils
operator|.
name|getAccessToken
argument_list|(
name|wc
argument_list|,
operator|new
name|CustomGrant
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSAML2BearerAuthenticationInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/oauth2-auth/token"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClientWithProps
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|ClientAccessToken
name|at
init|=
name|OAuthClientUtils
operator|.
name|getAccessToken
argument_list|(
name|wc
argument_list|,
operator|new
name|CustomGrant
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|WebClient
name|createWebClient
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|JAXRSOAuth2Test
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|bean
operator|.
name|createWebClient
argument_list|()
decl_stmt|;
name|wc
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
return|return
name|wc
return|;
block|}
specifier|private
name|WebClient
name|createWebClientWithProps
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|JAXRSOAuth2Test
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.jaxrs.security.saml.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.saml-callback-handler"
argument_list|,
literal|"org.apache.cxf.systest.jaxrs.security.oauth2.SamlCallbackHandler2"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.signature.username"
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.signature.properties"
argument_list|,
name|CRYPTO_RESOURCE_PROPERTIES
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.self-sign-saml-assertion"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Saml2BearerAuthOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|bean
operator|.
name|createWebClient
argument_list|()
decl_stmt|;
name|wc
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_FORM_URLENCODED
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
expr_stmt|;
return|return
name|wc
return|;
block|}
specifier|private
specifier|static
class|class
name|CustomGrant
implements|implements
name|AccessTokenGrant
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4007538779198315873L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
literal|"custom_grant"
return|;
block|}
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|()
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
name|OAuthConstants
operator|.
name|GRANT_TYPE
argument_list|,
literal|"custom_grant"
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
block|}
block|}
end_class

end_unit

