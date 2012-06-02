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
name|ws
operator|.
name|rm
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|persistence
operator|.
name|jdbc
operator|.
name|RMTxStore
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

begin_comment
comment|/**  * Tests the acknowledgement delivery back to the non-decoupled port when there is some  * error at the provider side and how its behavior is affected by the robust in-only mode setting.  */
end_comment

begin_class
specifier|public
class|class
name|ServiceInvocationAckPersistenceTest
extends|extends
name|ServiceInvocationAckBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|ServiceInvocationAckPersistenceTest
operator|.
name|class
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
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
literal|"synack"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|startServer
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|PORT
return|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanUpDerby
parameter_list|()
throws|throws
name|Exception
block|{
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
literal|"synack"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupGreeter
parameter_list|()
throws|throws
name|Exception
block|{
name|setupGreeter
argument_list|(
literal|"org/apache/cxf/systest/ws/rm/sync-ack-persistent-server.xml"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

