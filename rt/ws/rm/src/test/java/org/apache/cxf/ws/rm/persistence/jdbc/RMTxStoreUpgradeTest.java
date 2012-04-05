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
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Assert
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
comment|/**  * Tests the automatic table updating of RMTxStore that allows compatible changes   * in the database tables.   */
end_comment

begin_class
specifier|public
class|class
name|RMTxStoreUpgradeTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CREATE_OLD_SRC_SEQ_TABLE_STMT
init|=
literal|"CREATE TABLE CXF_RM_SRC_SEQUENCES "
operator|+
literal|"(SEQ_ID VARCHAR(256) NOT NULL, "
operator|+
literal|"CUR_MSG_NO DECIMAL(19, 0) DEFAULT 1 NOT NULL, "
operator|+
literal|"LAST_MSG CHAR(1), "
operator|+
literal|"EXPIRY DECIMAL(19, 0), "
operator|+
literal|"OFFERING_SEQ_ID VARCHAR(256), "
operator|+
literal|"ENDPOINT_ID VARCHAR(1024), "
operator|+
literal|"PRIMARY KEY (SEQ_ID))"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CREATE_OLD_DEST_SEQ_TABLE_STMT
init|=
literal|"CREATE TABLE CXF_RM_DEST_SEQUENCES "
operator|+
literal|"(SEQ_ID VARCHAR(256) NOT NULL, "
operator|+
literal|"ACKS_TO VARCHAR(1024) NOT NULL, "
operator|+
literal|"LAST_MSG_NO DECIMAL(19, 0), "
operator|+
literal|"ENDPOINT_ID VARCHAR(1024), "
operator|+
literal|"ACKNOWLEDGED BLOB, "
operator|+
literal|"PRIMARY KEY (SEQ_ID))"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CREATE_OLD_MSGS_TABLE_STMT
init|=
literal|"CREATE TABLE {0} "
operator|+
literal|"(SEQ_ID VARCHAR(256) NOT NULL, "
operator|+
literal|"MSG_NO DECIMAL(19, 0) NOT NULL, "
operator|+
literal|"SEND_TO VARCHAR(256), "
operator|+
literal|"CONTENT BLOB, "
operator|+
literal|"PRIMARY KEY (SEQ_ID, MSG_NO))"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INBOUND_MSGS_TABLE_NAME
init|=
literal|"CXF_RM_INBOUND_MESSAGES"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OUTBOUND_MSGS_TABLE_NAME
init|=
literal|"CXF_RM_OUTBOUND_MESSAGES"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DB_NAME
init|=
literal|"rmdb2"
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUpOnce
parameter_list|()
block|{
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
name|TEST_DB_NAME
argument_list|,
literal|true
argument_list|)
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
name|RMTxStore
operator|.
name|deleteDatabaseFiles
argument_list|(
name|TEST_DB_NAME
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUpgradeTables
parameter_list|()
throws|throws
name|Exception
block|{
name|TestRMTxStore
name|store
init|=
operator|new
name|TestRMTxStore
argument_list|()
decl_stmt|;
name|store
operator|.
name|setDriverClassName
argument_list|(
literal|"org.apache.derby.jdbc.EmbeddedDriver"
argument_list|)
expr_stmt|;
comment|// workaround for the db file deletion problem during the tests
name|store
operator|.
name|setUrl
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
literal|"jdbc:derby:{0};create=true"
argument_list|,
name|TEST_DB_NAME
argument_list|)
argument_list|)
expr_stmt|;
comment|// use the old db definitions to create the tables
name|store
operator|.
name|init
argument_list|()
expr_stmt|;
comment|// verify the absence of the new columns in the tables
name|verifyColumns
argument_list|(
name|store
argument_list|,
literal|"CXF_RM_SRC_SEQUENCES"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"PROTOCOL_VERSION"
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyColumns
argument_list|(
name|store
argument_list|,
literal|"CXF_RM_DEST_SEQUENCES"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"PROTOCOL_VERSION"
block|}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyColumns
argument_list|(
name|store
argument_list|,
name|INBOUND_MSGS_TABLE_NAME
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyColumns
argument_list|(
name|store
argument_list|,
name|OUTBOUND_MSGS_TABLE_NAME
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// upgrade the tables and add new columns to the old tables
name|store
operator|.
name|upgrade
argument_list|()
expr_stmt|;
name|store
operator|.
name|init
argument_list|()
expr_stmt|;
comment|// verify the presence of the new columns in the upgraded tables
name|verifyColumns
argument_list|(
name|store
argument_list|,
literal|"CXF_RM_SRC_SEQUENCES"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"PROTOCOL_VERSION"
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|verifyColumns
argument_list|(
name|store
argument_list|,
literal|"CXF_RM_DEST_SEQUENCES"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"PROTOCOL_VERSION"
block|}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|verifyColumns
argument_list|(
name|store
argument_list|,
name|INBOUND_MSGS_TABLE_NAME
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|verifyColumns
argument_list|(
name|store
argument_list|,
name|OUTBOUND_MSGS_TABLE_NAME
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|verifyColumns
parameter_list|(
name|RMTxStore
name|store
parameter_list|,
name|String
name|tableName
parameter_list|,
name|String
index|[]
name|cols
parameter_list|,
name|boolean
name|absent
parameter_list|)
throws|throws
name|Exception
block|{
comment|// verify the presence of the new fields
name|DatabaseMetaData
name|metadata
init|=
name|store
operator|.
name|getConnection
argument_list|()
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|ResultSet
name|rs
init|=
name|metadata
operator|.
name|getColumns
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|tableName
argument_list|,
literal|"%"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|colNames
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|colNames
argument_list|,
name|cols
argument_list|)
expr_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|colNames
operator|.
name|remove
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|absent
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Some new columns are already present"
argument_list|,
name|cols
operator|.
name|length
argument_list|,
name|colNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"Some new columns are still absent"
argument_list|,
literal|0
argument_list|,
name|colNames
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|TestRMTxStore
extends|extends
name|RMTxStore
block|{
specifier|private
name|boolean
name|upgraded
decl_stmt|;
specifier|public
name|void
name|upgrade
parameter_list|()
block|{
name|upgraded
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createTables
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
name|upgraded
condition|)
block|{
name|super
operator|.
name|createTables
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// creating the old tables
name|Statement
name|stmt
init|=
literal|null
decl_stmt|;
name|stmt
operator|=
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
expr_stmt|;
try|try
block|{
name|stmt
operator|.
name|executeUpdate
argument_list|(
name|CREATE_OLD_SRC_SEQ_TABLE_STMT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isTableExistsError
argument_list|(
name|ex
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
name|stmt
operator|=
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
expr_stmt|;
try|try
block|{
name|stmt
operator|.
name|executeUpdate
argument_list|(
name|CREATE_OLD_DEST_SEQ_TABLE_STMT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isTableExistsError
argument_list|(
name|ex
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|tableName
range|:
operator|new
name|String
index|[]
block|{
name|INBOUND_MSGS_TABLE_NAME
block|,
name|OUTBOUND_MSGS_TABLE_NAME
block|}
control|)
block|{
name|stmt
operator|=
name|getConnection
argument_list|()
operator|.
name|createStatement
argument_list|()
expr_stmt|;
try|try
block|{
name|stmt
operator|.
name|executeUpdate
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|CREATE_OLD_MSGS_TABLE_STMT
argument_list|,
name|tableName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isTableExistsError
argument_list|(
name|ex
argument_list|)
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

