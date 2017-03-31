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
name|failover
package|;
end_package

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
name|Collections
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
name|clustering
operator|.
name|FailoverFeature
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
name|clustering
operator|.
name|SequentialStrategy
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
name|model
operator|.
name|AbstractResourceInfo
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

begin_comment
comment|/**  * A test for failover using a WebClient object  */
end_comment

begin_class
specifier|public
class|class
name|FailoverWebClientTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT1
init|=
name|allocatePort
argument_list|(
name|FailoverBookServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT2
init|=
name|allocatePort
argument_list|(
name|FailoverBookServer
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT3
init|=
name|allocatePort
argument_list|(
name|FailoverBookServer
operator|.
name|class
argument_list|,
literal|3
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|FailoverBookServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEchoXmlBookQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/bookstore"
decl_stmt|;
name|FailoverFeature
name|failoverFeature
init|=
operator|new
name|FailoverFeature
argument_list|()
decl_stmt|;
name|SequentialStrategy
name|strategy
init|=
operator|new
name|SequentialStrategy
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|addresses
operator|.
name|add
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/bookstore"
argument_list|)
expr_stmt|;
name|addresses
operator|.
name|add
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT3
operator|+
literal|"/bookstore"
argument_list|)
expr_stmt|;
name|strategy
operator|.
name|setAlternateAddresses
argument_list|(
name|addresses
argument_list|)
expr_stmt|;
name|failoverFeature
operator|.
name|setStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|WebClient
name|webClient
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|failoverFeature
argument_list|)
argument_list|,
literal|null
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
decl_stmt|;
comment|// Should hit PORT1
name|Book
name|b
init|=
name|webClient
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
literal|124L
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Should failover to PORT2
name|webClient
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Should failover to PORT3
name|webClient
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|124L
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"root"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

