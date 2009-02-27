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
package|;
end_package

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
name|systest
operator|.
name|jaxrs
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
name|JAXRSHttpsBookTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_FILE
init|=
literal|"org/apache/cxf/systest/jaxrs/security/jaxrs-https.xml"
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
name|BookHttpsServer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123Client
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|CLIENT_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
literal|"https://localhost:9095"
argument_list|,
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// just to verify the interface call goes through CGLIB proxy too
name|assertEquals
argument_list|(
literal|"https://localhost:9095"
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|bs
argument_list|)
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|bs
operator|.
name|getSecureBook
argument_list|(
literal|"123"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|b
operator|.
name|getId
argument_list|()
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBook123WebClient
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|CLIENT_CONFIG_FILE
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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
literal|"https://localhost:9095"
argument_list|)
expr_stmt|;
name|WebClient
name|client
init|=
name|bean
operator|.
name|createWebClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"https://localhost:9095"
argument_list|,
name|client
operator|.
name|getBaseURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|path
argument_list|(
literal|"/bookstore/securebooks/123"
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_XML_TYPE
argument_list|)
expr_stmt|;
name|Book
name|b
init|=
name|client
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
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

