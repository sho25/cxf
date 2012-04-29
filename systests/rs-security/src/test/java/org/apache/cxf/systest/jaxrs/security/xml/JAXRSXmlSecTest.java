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
name|xml
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
name|ClientWebApplicationException
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
name|ServerWebApplicationException
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
name|common
operator|.
name|SecurityUtils
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
name|XmlEncInInterceptor
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
name|XmlEncOutInterceptor
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
name|XmlSigInInterceptor
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
name|BookStore
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
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLCipher
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
name|JAXRSXmlSecTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerXmlSec
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
name|BookServerXmlSec
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
name|testPostBookWithEnvelopedSigAndProxy
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
literal|"/xmlsig"
decl_stmt|;
name|doTestSignatureProxy
argument_list|(
name|address
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestSignatureProxy
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|enveloping
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
name|JAXRSXmlSecTest
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
name|XmlSigOutInterceptor
name|sigInterceptor
init|=
operator|new
name|XmlSigOutInterceptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|enveloping
condition|)
block|{
name|sigInterceptor
operator|.
name|setStyle
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|ENVELOPING_SIG
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|sigInterceptor
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|BookStore
name|store
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|Book
name|book
init|=
name|store
operator|.
name|addBook
argument_list|(
operator|new
name|Book
argument_list|(
literal|"CXF"
argument_list|,
literal|126L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|126L
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
name|ServerWebApplicationException
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
name|ClientWebApplicationException
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
name|testPostBookWithEnvelopedSig
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
literal|"/xmlsig/bookstore/books"
decl_stmt|;
name|doTestSignature
argument_list|(
name|address
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostBookWithEnvelopingSig
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
literal|"/xmlsig/bookstore/books"
decl_stmt|;
name|doTestSignature
argument_list|(
name|address
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestSignature
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|enveloping
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
name|JAXRSXmlSecTest
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
name|XmlSigOutInterceptor
name|sigInterceptor
init|=
operator|new
name|XmlSigOutInterceptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|enveloping
condition|)
block|{
name|sigInterceptor
operator|.
name|setStyle
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|ENVELOPING_SIG
argument_list|)
expr_stmt|;
block|}
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|sigInterceptor
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XmlSigInInterceptor
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
literal|126L
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|126L
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
name|ServerWebApplicationException
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
name|ClientWebApplicationException
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
name|testPostEncryptedBook
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
literal|"/xmlenc/bookstore/books"
decl_stmt|;
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
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/bob.properties"
argument_list|)
expr_stmt|;
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
literal|false
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostEncryptedBookGCM
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
literal|"/xmlenc/bookstore/books"
decl_stmt|;
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
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/bob.properties"
argument_list|)
expr_stmt|;
name|String
name|aes128GCM
init|=
literal|"http://www.w3.org/2009/xmlenc11#aes128-gcm"
decl_stmt|;
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
literal|false
argument_list|,
name|properties
argument_list|,
name|SecurityUtils
operator|.
name|X509_KEY
argument_list|,
name|aes128GCM
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostEncryptedBookSHA256
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
literal|"/xmlenc/bookstore/books"
decl_stmt|;
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
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/bob.properties"
argument_list|)
expr_stmt|;
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
literal|false
argument_list|,
name|properties
argument_list|,
name|SecurityUtils
operator|.
name|X509_KEY
argument_list|,
name|XMLCipher
operator|.
name|AES_128
argument_list|,
name|XMLCipher
operator|.
name|SHA256
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostEncryptedBookIssuerSerial
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
literal|"/xmlenc/bookstore/books"
decl_stmt|;
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
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/bob.properties"
argument_list|)
expr_stmt|;
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
literal|false
argument_list|,
name|properties
argument_list|,
name|SecurityUtils
operator|.
name|X509_ISSUER_SERIAL
argument_list|,
name|XMLCipher
operator|.
name|AES_128
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostEncryptedSignedBook
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
literal|"/xmlsec/bookstore/books"
decl_stmt|;
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
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/bob.properties"
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
literal|"org/apache/cxf/systest/jaxrs/security/alice.properties"
argument_list|)
expr_stmt|;
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
literal|true
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPostEncryptedSignedBookUseReqSigCert
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
literal|"/xmlsec-useReqSigCert/bookstore/books"
decl_stmt|;
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
literal|"ws-security.encryption.username"
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"ws-security.encryption.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/bob.properties"
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
literal|"org/apache/cxf/systest/jaxrs/security/alice.properties"
argument_list|)
expr_stmt|;
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
literal|true
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doTestPostEncryptedBook
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|sign
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
throws|throws
name|Exception
block|{
name|doTestPostEncryptedBook
argument_list|(
name|address
argument_list|,
name|sign
argument_list|,
name|properties
argument_list|,
name|SecurityUtils
operator|.
name|X509_KEY
argument_list|,
name|XMLCipher
operator|.
name|AES_128
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doTestPostEncryptedBook
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|sign
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|keyIdentifierType
parameter_list|,
name|String
name|symmetricAlgorithm
parameter_list|,
name|String
name|digestAlgorithm
parameter_list|)
throws|throws
name|Exception
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
name|JAXRSXmlSecTest
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
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
if|if
condition|(
name|sign
condition|)
block|{
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XmlSigOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|XmlEncOutInterceptor
name|encInterceptor
init|=
operator|new
name|XmlEncOutInterceptor
argument_list|()
decl_stmt|;
name|encInterceptor
operator|.
name|setKeyIdentifierType
argument_list|(
name|keyIdentifierType
argument_list|)
expr_stmt|;
name|encInterceptor
operator|.
name|setSymmetricEncAlgorithm
argument_list|(
name|symmetricAlgorithm
argument_list|)
expr_stmt|;
name|encInterceptor
operator|.
name|setDigestAlgorithm
argument_list|(
name|digestAlgorithm
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|encInterceptor
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XmlEncInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|sign
condition|)
block|{
name|bean
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|XmlSigInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|WebClient
name|wc
init|=
name|bean
operator|.
name|createWebClient
argument_list|()
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|10000000L
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
literal|126L
argument_list|)
argument_list|,
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|126L
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
name|ServerWebApplicationException
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
name|ClientWebApplicationException
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
block|}
end_class

end_unit

