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
name|jose
operator|.
name|jwejws
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
name|security
operator|.
name|Security
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|BadRequestException
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
name|JAXRSClientFactory
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jaxrs
operator|.
name|JwsDetachedSignatureProvider
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
name|jaxrs
operator|.
name|multipart
operator|.
name|JwsMultipartClientRequestFilter
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
name|jaxrs
operator|.
name|multipart
operator|.
name|JwsMultipartClientResponseFilter
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
name|jose
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
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSJwsMultipartTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerJwsMultipart
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
name|BookServerJwsMultipart
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|registerBouncyCastleIfNeeded
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|registerBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Still need it for Oracle Java 7 and Java 8
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|unregisterBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
name|Security
operator|.
name|removeProvider
argument_list|(
name|BouncyCastleProvider
operator|.
name|PROVIDER_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJwsJwkBookHMacMultipart
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
literal|"/jwsjwkhmacSinglePart"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createJwsBookStoreHMac
argument_list|(
name|address
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|echoBookMultipart
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
literal|"book"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testJwsJwkBookRSAMultipart
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
literal|"/jwsjwkrsaSinglePart"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createJwsBookStoreRSA
argument_list|(
name|address
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|echoBookMultipart
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
literal|"book"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testJwsJwkBooksHMacMultipart
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
literal|"/jwsjwkhmacManyParts"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createJwsBookStoreHMac
argument_list|(
name|address
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|LinkedList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book2"
argument_list|,
literal|124L
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|returnBooks
init|=
name|bs
operator|.
name|echoBooksMultipart
argument_list|(
name|books
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"book"
argument_list|,
name|returnBooks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|returnBooks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"book2"
argument_list|,
name|returnBooks
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|returnBooks
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ProcessingException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testJwsJwkBooksHMacMultipartClientRestriction
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
literal|"/jwsjwkhmacManyParts"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createJwsBookStoreHMac
argument_list|(
name|address
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|LinkedList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book2"
argument_list|,
literal|124L
argument_list|)
argument_list|)
expr_stmt|;
name|bs
operator|.
name|echoBooksMultipart
argument_list|(
name|books
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BadRequestException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testJwsJwkBooksHMacMultipartServerRestriction
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
literal|"/jwsjwkhmacSinglePart"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createJwsBookStoreHMac
argument_list|(
name|address
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Book
argument_list|>
name|books
init|=
operator|new
name|LinkedList
argument_list|<
name|Book
argument_list|>
argument_list|()
decl_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|add
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book2"
argument_list|,
literal|124L
argument_list|)
argument_list|)
expr_stmt|;
name|bs
operator|.
name|echoBooksMultipart
argument_list|(
name|books
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BadRequestException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testJwsJwkBooksHMacMultipartUnsigned
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
literal|"/jwsjwkhmacSinglePart"
decl_stmt|;
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|BookStore
operator|.
name|class
argument_list|,
name|JAXRSJwsMultipartTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|bs
operator|.
name|echoBookMultipart
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJwsJwkBookHMacMultipartModified
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
literal|"/jwsjwkhmacSinglePartModified"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createJwsBookStoreHMac
argument_list|(
name|address
argument_list|,
literal|true
argument_list|)
decl_stmt|;
try|try
block|{
name|bs
operator|.
name|echoBookMultipart
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception is expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|BookStore
name|createJwsBookStoreHMac
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|supportSinglePart
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|createJAXRSClientFactoryBean
argument_list|(
name|address
argument_list|,
name|supportSinglePart
argument_list|)
decl_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.signature.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/secret.jwk.properties"
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|BookStore
name|createJwsBookStoreRSA
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|supportSinglePart
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
name|createJAXRSClientFactoryBean
argument_list|(
name|address
argument_list|,
name|supportSinglePart
argument_list|)
decl_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.signature.properties"
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/alice.jwk.properties"
argument_list|)
expr_stmt|;
return|return
name|bean
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
return|;
block|}
specifier|private
name|JAXRSClientFactoryBean
name|createJAXRSClientFactoryBean
parameter_list|(
name|String
name|address
parameter_list|,
name|boolean
name|supportSinglePart
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
name|JAXRSJwsMultipartTest
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
name|setServiceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|JwsMultipartClientRequestFilter
name|outFilter
init|=
operator|new
name|JwsMultipartClientRequestFilter
argument_list|()
decl_stmt|;
name|outFilter
operator|.
name|setSupportSinglePartOnly
argument_list|(
name|supportSinglePart
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|outFilter
argument_list|)
expr_stmt|;
name|JwsMultipartClientResponseFilter
name|inFilter
init|=
operator|new
name|JwsMultipartClientResponseFilter
argument_list|()
decl_stmt|;
name|inFilter
operator|.
name|setSupportSinglePartOnly
argument_list|(
name|supportSinglePart
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|inFilter
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwsDetachedSignatureProvider
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
return|return
name|bean
return|;
block|}
block|}
end_class

end_unit

