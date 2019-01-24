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
name|httpsignature
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
name|PrivateKey
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
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
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
name|Priorities
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
name|client
operator|.
name|ClientRequestContext
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
name|client
operator|.
name|ClientRequestFilter
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
name|ext
operator|.
name|Provider
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
name|httpsignature
operator|.
name|MessageSigner
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
name|httpsignature
operator|.
name|filters
operator|.
name|CreateSignatureClientFilter
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
comment|/**  * A test for the HTTP Signature functionality in the cxf-rt-rs-security-http-signature module.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSHTTPSignatureTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerHttpSignature
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
name|BookServerHttpSignature
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
name|testHttpSignature
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|CreateSignatureClientFilter
name|signatureFilter
init|=
operator|new
name|CreateSignatureClientFilter
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
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
name|assertNotNull
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKey
argument_list|,
literal|"custom_key_id"
argument_list|)
decl_stmt|;
name|signatureFilter
operator|.
name|setMessageSigner
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/httpsig/bookstore/books"
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureFilter
argument_list|)
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|126L
argument_list|,
name|returnedBook
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpSignatureRsaSha512
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|CreateSignatureClientFilter
name|signatureFilter
init|=
operator|new
name|CreateSignatureClientFilter
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
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
name|assertNotNull
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
literal|"rsa-sha512"
argument_list|,
literal|"SHA-256"
argument_list|,
name|privateKey
argument_list|,
literal|"custom_key_id"
argument_list|)
decl_stmt|;
name|signatureFilter
operator|.
name|setMessageSigner
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/httpsigrsasha512/bookstore/books"
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureFilter
argument_list|)
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|126L
argument_list|,
name|returnedBook
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Negative tests
comment|//
annotation|@
name|Test
specifier|public
name|void
name|testNonMatchingSignatureAlgorithm
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|CreateSignatureClientFilter
name|signatureFilter
init|=
operator|new
name|CreateSignatureClientFilter
argument_list|()
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
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
name|assertNotNull
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
literal|"rsa-sha512"
argument_list|,
literal|"SHA-256"
argument_list|,
name|privateKey
argument_list|,
literal|"custom_key_id"
argument_list|)
decl_stmt|;
name|signatureFilter
operator|.
name|setMessageSigner
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/httpsig/bookstore/books"
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureFilter
argument_list|)
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|400
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoHttpSignature
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
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
name|PORT
operator|+
literal|"/httpsig/bookstore/books"
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
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|400
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWrongHTTPMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|ClientTestFilter
name|signatureFilter
init|=
operator|new
name|ClientTestFilter
argument_list|()
decl_stmt|;
name|signatureFilter
operator|.
name|setHttpMethod
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
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
name|assertNotNull
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKey
argument_list|,
literal|"custom_key_id"
argument_list|)
decl_stmt|;
name|signatureFilter
operator|.
name|setMessageSigner
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/httpsig/bookstore/books"
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureFilter
argument_list|)
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|400
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWrongURI
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|ClientTestFilter
name|signatureFilter
init|=
operator|new
name|ClientTestFilter
argument_list|()
decl_stmt|;
name|signatureFilter
operator|.
name|setUri
argument_list|(
literal|"/httpsig/bookstore/books2"
argument_list|)
expr_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
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
name|assertNotNull
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKey
argument_list|,
literal|"custom_key_id"
argument_list|)
decl_stmt|;
name|signatureFilter
operator|.
name|setMessageSigner
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/httpsig/bookstore/books"
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureFilter
argument_list|)
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|400
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testChangedSignatureMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JAXRSHTTPSignatureTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|ClientTestFilter
name|signatureFilter
init|=
operator|new
name|ClientTestFilter
argument_list|()
decl_stmt|;
name|signatureFilter
operator|.
name|setChangeSignatureAlgorithm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
decl_stmt|;
name|keyStore
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
name|PrivateKey
name|privateKey
init|=
operator|(
name|PrivateKey
operator|)
name|keyStore
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
name|assertNotNull
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKey
argument_list|,
literal|"custom_key_id"
argument_list|)
decl_stmt|;
name|signatureFilter
operator|.
name|setMessageSigner
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/httpsig/bookstore/books"
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
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureFilter
argument_list|)
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
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
literal|"CXF"
argument_list|,
literal|126L
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
literal|400
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provider
annotation|@
name|Priority
argument_list|(
name|Priorities
operator|.
name|AUTHENTICATION
argument_list|)
specifier|private
specifier|final
class|class
name|ClientTestFilter
implements|implements
name|ClientRequestFilter
block|{
specifier|private
name|MessageSigner
name|messageSigner
decl_stmt|;
specifier|private
name|String
name|httpMethod
decl_stmt|;
specifier|private
name|String
name|uri
decl_stmt|;
specifier|private
name|boolean
name|changeSignatureAlgorithm
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestCtx
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestHeaders
init|=
name|requestCtx
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|convertedHeaders
init|=
name|convertHeaders
argument_list|(
name|requestHeaders
argument_list|)
decl_stmt|;
try|try
block|{
name|messageSigner
operator|.
name|sign
argument_list|(
name|convertedHeaders
argument_list|,
name|uri
operator|!=
literal|null
condition|?
name|uri
else|:
name|requestCtx
operator|.
name|getUri
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|httpMethod
operator|!=
literal|null
condition|?
name|httpMethod
else|:
name|requestCtx
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|changeSignatureAlgorithm
condition|)
block|{
name|String
name|signatureValue
init|=
name|convertedHeaders
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|signatureValue
operator|=
name|signatureValue
operator|.
name|replace
argument_list|(
literal|"rsa-sha256"
argument_list|,
literal|"rsa-sha512"
argument_list|)
expr_stmt|;
name|requestHeaders
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|signatureValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestHeaders
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|convertedHeaders
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Convert the headers from List<Object> -> List<String>
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|convertHeaders
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestHeaders
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|convertedHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|requestHeaders
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|entry
range|:
name|requestHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|convertedHeaders
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|o
lambda|->
name|o
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|convertedHeaders
return|;
block|}
specifier|public
name|void
name|setMessageSigner
parameter_list|(
name|MessageSigner
name|messageSigner
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|messageSigner
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageSigner
operator|=
name|messageSigner
expr_stmt|;
block|}
specifier|public
name|void
name|setHttpMethod
parameter_list|(
name|String
name|httpMethod
parameter_list|)
block|{
name|this
operator|.
name|httpMethod
operator|=
name|httpMethod
expr_stmt|;
block|}
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|void
name|setChangeSignatureAlgorithm
parameter_list|(
name|boolean
name|changeSignatureAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|changeSignatureAlgorithm
operator|=
name|changeSignatureAlgorithm
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
