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
name|cdi
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|AbstractCdiSingleAppTest
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
name|systests
operator|.
name|cdi
operator|.
name|base
operator|.
name|jetty
operator|.
name|AbstractJettyServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|webbeans
operator|.
name|servlet
operator|.
name|WebBeansConfigurationListener
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JettyEmbeddedTest
extends|extends
name|AbstractCdiSingleAppTest
block|{
specifier|public
specifier|static
class|class
name|EmbeddedJettyServer
extends|extends
name|AbstractJettyServer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|allocatePortAsInt
argument_list|(
name|EmbeddedJettyServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|EmbeddedJettyServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/"
argument_list|,
name|PORT
argument_list|,
operator|new
name|WebBeansConfigurationListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|EmbeddedJettyServer
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
name|testAddOneBookWithValidation
parameter_list|()
block|{
specifier|final
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
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
name|testResponseHasBeenReceivedWhenQueringAllBookAsAtomFeed
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books/feed"
argument_list|,
literal|"application/atom+xml"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/atom+xml"
argument_list|,
name|r
operator|.
name|getMediaType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBookHasBeenValidatedWhenPostedAsAtomFeed
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books/feed"
argument_list|,
literal|"application/atom+xml"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"Book 1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
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
name|testBookHasBeenCreatedWhenPostedAsAtomFeed
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
name|getBasePath
argument_list|()
operator|+
literal|"/books/feed"
argument_list|,
literal|"application/atom+xml"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"1234"
argument_list|)
operator|.
name|param
argument_list|(
literal|"name"
argument_list|,
literal|"Book 1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Response
operator|.
name|Status
operator|.
name|CREATED
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|EmbeddedJettyServer
operator|.
name|PORT
return|;
block|}
block|}
end_class

end_unit

