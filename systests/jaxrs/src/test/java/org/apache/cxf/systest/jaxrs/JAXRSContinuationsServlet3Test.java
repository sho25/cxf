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
name|testutil
operator|.
name|common
operator|.
name|ServerLauncher
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
name|JAXRSContinuationsServlet3Test
extends|extends
name|AbstractJAXRSContinuationsTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookContinuationServlet3Server
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookContinuationServlet3Server
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
name|testTimeoutAndCancelAsyncExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestTimeoutAndCancel
argument_list|(
literal|"/asyncexecutor/bookstore"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookUnmappedFromFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getBaseAddress
argument_list|()
operator|+
literal|"/books/unmappedFromFilter"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
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
literal|500
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
name|testClientDisconnect
parameter_list|()
throws|throws
name|Exception
block|{
name|ServerLauncher
name|launcher
init|=
operator|new
name|ServerLauncher
argument_list|(
name|BookContinuationClient
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launcher
operator|.
name|launchServer
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getBaseAddress
parameter_list|()
block|{
return|return
literal|"/async/bookstore"
return|;
block|}
specifier|protected
name|String
name|getPort
parameter_list|()
block|{
return|return
name|PORT
return|;
block|}
block|}
end_class

end_unit

