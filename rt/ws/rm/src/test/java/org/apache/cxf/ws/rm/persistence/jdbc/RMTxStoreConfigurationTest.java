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
name|SQLException
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
name|ws
operator|.
name|rm
operator|.
name|RMManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|assertFalse
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
name|assertNull
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
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|RMTxStoreConfigurationTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTxStoreBean
parameter_list|()
block|{
comment|// connect exception only results in a log message
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/ws/rm/persistence/jdbc/txstore-bean.xml"
argument_list|)
decl_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMTxStore
name|store
init|=
operator|(
name|RMTxStore
operator|)
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|store
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Connection should be null"
argument_list|,
name|store
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.derby.jdbc.EmbeddedDriver"
argument_list|,
name|store
operator|.
name|getDriverClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"scott"
argument_list|,
name|store
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tiger"
argument_list|,
name|store
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jdbc:derby:target/wsrmdb3;create=true"
argument_list|,
name|store
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"schema should be unset"
argument_list|,
name|store
operator|.
name|getSchemaName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetCustomTableExistsState
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/ws/rm/persistence/jdbc/txstore-custom-error-bean.xml"
argument_list|)
decl_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMTxStore
name|store
init|=
operator|(
name|RMTxStore
operator|)
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|isTableExistsError
argument_list|(
operator|new
name|SQLException
argument_list|(
literal|"Table exists"
argument_list|,
literal|"I6000"
argument_list|,
literal|288
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|store
operator|.
name|isTableExistsError
argument_list|(
operator|new
name|SQLException
argument_list|(
literal|"Unknown error"
argument_list|,
literal|"00000"
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetCustomTableExistsState2
parameter_list|()
block|{
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
literal|"target/wsrmdb5"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/ws/rm/persistence/jdbc/txstore-custom-error-bean2.xml"
argument_list|)
decl_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMTxStore
name|store
init|=
operator|(
name|RMTxStore
operator|)
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|store
operator|.
name|isTableExistsError
argument_list|(
operator|new
name|SQLException
argument_list|(
literal|"Table exists"
argument_list|,
literal|"I6000"
argument_list|,
literal|288
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|store
operator|.
name|isTableExistsError
argument_list|(
operator|new
name|SQLException
argument_list|(
literal|"Unknown error"
argument_list|,
literal|"00000"
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTxStoreWithDataSource
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/ws/rm/persistence/jdbc/txstore-ds-bean.xml"
argument_list|)
decl_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMTxStore
name|store
init|=
operator|(
name|RMTxStore
operator|)
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|store
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|store
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTxStoreWithDataSource2
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/ws/rm/persistence/jdbc/txstore-ds-bean2.xml"
argument_list|)
decl_stmt|;
name|RMManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|RMTxStore
name|store
init|=
operator|(
name|RMTxStore
operator|)
name|manager
operator|.
name|getStore
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|store
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|store
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

