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
name|ws
operator|.
name|rm
operator|.
name|persistence
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|derby
operator|.
name|jdbc
operator|.
name|EmbeddedConnectionPoolDataSource
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|RMTxStorePooledConnectionTest
extends|extends
name|RMTxStoreTestBase
block|{
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpOnce
parameter_list|()
block|{
name|RMTxStoreTestBase
operator|.
name|setUpOnce
argument_list|()
expr_stmt|;
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
literal|"rmdbpc"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|EmbeddedConnectionPoolDataSource
name|ds
init|=
operator|new
name|EmbeddedConnectionPoolDataSource
argument_list|()
decl_stmt|;
name|ds
operator|.
name|setDatabaseName
argument_list|(
literal|"rmdbpc"
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setCreateDatabase
argument_list|(
literal|"create"
argument_list|)
expr_stmt|;
name|store
operator|=
operator|new
name|RMTxStore
argument_list|()
expr_stmt|;
name|store
operator|.
name|setDataSource
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|store
operator|.
name|setKeepConnection
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|store
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|tearDownOnce
parameter_list|()
block|{
comment|/*         try {             store.getConnection().close();         } catch (SQLException ex) {             ex.printStackTrace();         }         */
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
literal|"rmdbpc"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Connection
name|getConnection
parameter_list|()
block|{
return|return
name|store
operator|.
name|verifyConnection
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|releaseConnection
parameter_list|(
name|Connection
name|con
parameter_list|)
block|{
if|if
condition|(
name|con
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
block|}
end_class

end_unit

