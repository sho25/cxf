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
name|saml
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|ProcessingException
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
name|WebApplicationException
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
name|interceptor
operator|.
name|Interceptor
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
name|jaxrs
operator|.
name|provider
operator|.
name|FormEncodingProvider
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
name|message
operator|.
name|Message
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
name|SamlEnvelopedOutInterceptor
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
name|SamlFormOutInterceptor
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
name|SamlHeaderOutInterceptor
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
name|xml
operator|.
name|XmlSigOutInterceptor
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
name|SecurityConstants
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
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|builder
operator|.
name|SAML2Constants
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
name|dom
operator|.
name|WSConstants
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
name|JAXRSSamlTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerSaml
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
name|BookServerSaml
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
name|testGetBookSAMLTokenAsHeader
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
literal|"/samlheader/bookstore/books/123"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|,
operator|new
name|SamlHeaderOutInterceptor
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|Book
name|book
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProcessingException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidSAMLTokenAsHeader
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
literal|"/samlheader/bookstore/books/123"
decl_stmt|;
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
name|JAXRSSamlTest
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
name|header
argument_list|(
literal|"Authorization"
argument_list|,
literal|"SAML invalid_grant"
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSAMLTokenInForm
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
literal|"/samlform/bookstore/books"
decl_stmt|;
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|formProvider
init|=
operator|new
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
argument_list|()
decl_stmt|;
name|formProvider
operator|.
name|setExpectedEncoded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|,
operator|new
name|SamlFormOutInterceptor
argument_list|()
argument_list|,
name|formProvider
argument_list|)
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
name|APPLICATION_XML
argument_list|)
expr_stmt|;
try|try
block|{
name|Book
name|book
init|=
name|wc
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|(
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"CXF"
argument_list|)
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"125"
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|125L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProcessingException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnvelopedSelfSignedSAMLToken
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestEnvelopedSAMLToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBearerSignedDifferentAlgorithms
parameter_list|()
throws|throws
name|Exception
block|{
name|SamlCallbackHandler
name|callbackHandler
init|=
operator|new
name|SamlCallbackHandler
argument_list|()
decl_stmt|;
name|callbackHandler
operator|.
name|setSignatureAlgorithm
argument_list|(
literal|"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setDigestAlgorithm
argument_list|(
name|WSConstants
operator|.
name|SHA256
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setConfirmationMethod
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
expr_stmt|;
name|callbackHandler
operator|.
name|setSignAssertion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doTestEnvelopedSAMLToken
argument_list|(
literal|true
argument_list|,
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEnvelopedUnsignedSAMLToken
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestEnvelopedSAMLToken
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookPreviousSAMLTokenAsHeader
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
literal|"/samlheader/bookstore/books/123"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClientForExistingToken
argument_list|(
name|address
argument_list|,
operator|new
name|SamlHeaderOutInterceptor
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|Book
name|book
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProcessingException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookPreviousSAMLTokenInForm
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
literal|"/samlform/bookstore/books"
decl_stmt|;
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
name|formProvider
init|=
operator|new
name|FormEncodingProvider
argument_list|<
name|Form
argument_list|>
argument_list|()
decl_stmt|;
name|formProvider
operator|.
name|setExpectedEncoded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|WebClient
name|wc
init|=
name|createWebClientForExistingToken
argument_list|(
name|address
argument_list|,
operator|new
name|SamlFormOutInterceptor
argument_list|()
argument_list|,
name|formProvider
argument_list|)
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
name|APPLICATION_XML
argument_list|)
expr_stmt|;
try|try
block|{
name|Book
name|book
init|=
name|wc
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|(
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"CXF"
argument_list|)
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"125"
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|125L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProcessingException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|doTestEnvelopedSAMLToken
parameter_list|(
name|boolean
name|signed
parameter_list|)
throws|throws
name|Exception
block|{
name|doTestEnvelopedSAMLToken
argument_list|(
name|signed
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doTestEnvelopedSAMLToken
parameter_list|(
name|boolean
name|signed
parameter_list|,
name|CallbackHandler
name|samlCallbackHandler
parameter_list|)
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
literal|"/samlxml/bookstore/books"
decl_stmt|;
name|WebClient
name|wc
init|=
name|createWebClient
argument_list|(
name|address
argument_list|,
operator|new
name|SamlEnvelopedOutInterceptor
argument_list|(
operator|!
name|signed
argument_list|)
argument_list|,
literal|null
argument_list|,
name|samlCallbackHandler
argument_list|)
decl_stmt|;
name|XmlSigOutInterceptor
name|xmlSig
init|=
operator|new
name|XmlSigOutInterceptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|signed
condition|)
block|{
name|xmlSig
operator|.
name|setStyle
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|DETACHED_SIG
argument_list|)
expr_stmt|;
block|}
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|xmlSig
argument_list|)
expr_stmt|;
name|wc
operator|.
name|type
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML
argument_list|)
expr_stmt|;
try|try
block|{
name|Book
name|book
init|=
name|wc
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"CXF"
argument_list|,
literal|125L
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|125L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ProcessingException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
operator|&&
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|WebClient
name|createWebClient
parameter_list|(
name|String
name|address
parameter_list|,
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|outInterceptor
parameter_list|,
name|Object
name|provider
parameter_list|)
block|{
return|return
name|createWebClient
argument_list|(
name|address
argument_list|,
name|outInterceptor
argument_list|,
name|provider
argument_list|,
operator|new
name|SamlCallbackHandler
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|WebClient
name|createWebClient
parameter_list|(
name|String
name|address
parameter_list|,
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|outInterceptor
parameter_list|,
name|Object
name|provider
parameter_list|,
name|CallbackHandler
name|samlCallbackHandler
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
name|JAXRSSamlTest
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
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
literal|"org.apache.cxf.systest.jaxrs.security.saml.KeystorePasswordCallback"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SAML_CALLBACK_HANDLER
argument_list|,
name|samlCallbackHandler
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/alice.properties"
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
name|outInterceptor
argument_list|)
expr_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|bean
operator|.
name|setProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
specifier|private
name|WebClient
name|createWebClientForExistingToken
parameter_list|(
name|String
name|address
parameter_list|,
name|Interceptor
argument_list|<
name|Message
argument_list|>
name|outInterceptor
parameter_list|,
name|Object
name|provider
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
name|JAXRSSamlTest
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
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|outInterceptor
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
name|SamlRetrievalInterceptor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|bean
operator|.
name|setProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
return|return
name|bean
operator|.
name|createWebClient
argument_list|()
return|;
block|}
block|}
end_class

end_unit

