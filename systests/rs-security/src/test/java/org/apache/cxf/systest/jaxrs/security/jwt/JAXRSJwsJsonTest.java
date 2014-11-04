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
name|jwt
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jaxrs
operator|.
name|JwsJsonClientResponseFilter
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
name|JwsJsonWriterInterceptor
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

begin_class
specifier|public
class|class
name|JAXRSJwsJsonTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerJwsJson
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
name|BookServerJwsJson
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
name|testJwsJsonPlainTextHmac
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
literal|"/jwsjsonhmac"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createBookStore
argument_list|(
name|address
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/secret.jwk.properties"
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|bs
operator|.
name|echoText
argument_list|(
literal|"book"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"book"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJweJsonBookBeanHmac
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
literal|"/jwsjsonhmac"
decl_stmt|;
name|BookStore
name|bs
init|=
name|createBookStore
argument_list|(
name|address
argument_list|,
literal|"org/apache/cxf/systest/jaxrs/security/secret.jwk.properties"
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|echoBook
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
name|testJwsJsonBookDoubleHmac
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
literal|"/jwsjsonhmac2"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|properties
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|add
argument_list|(
literal|"org/apache/cxf/systest/jaxrs/security/secret.jwk.properties"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|add
argument_list|(
literal|"org/apache/cxf/systest/jaxrs/security/secret.jwk.hmac.properties"
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|createBookStore
argument_list|(
name|address
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|echoBook
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
name|testJwsJsonBookDoubleHmacSinglePropsFile
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
literal|"/jwsjsonhmac2"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|properties
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|add
argument_list|(
literal|"org/apache/cxf/systest/jaxrs/security/secret.jwk.hmac2.properties"
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|createBookStore
argument_list|(
name|address
argument_list|,
name|properties
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|bs
operator|.
name|echoBook
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
specifier|private
name|BookStore
name|createBookStore
parameter_list|(
name|String
name|address
parameter_list|,
name|Object
name|properties
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
name|JAXRSJwsJsonTest
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
name|JwsJsonWriterInterceptor
name|writer
init|=
operator|new
name|JwsJsonWriterInterceptor
argument_list|()
decl_stmt|;
name|writer
operator|.
name|setUseJwsJsonOutputStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwsJsonClientResponseFilter
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
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.signature.list.properties"
argument_list|,
name|properties
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
block|}
end_class

end_unit

