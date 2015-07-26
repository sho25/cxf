begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|Json
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonArray
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonArrayBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|Cell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|CellUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|TableName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Delete
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Get
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|ResultScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Scan
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Table
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|trace
operator|.
name|SpanReceiverHost
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|util
operator|.
name|Bytes
import|;
end_import

begin_class
specifier|public
class|class
name|CatalogStore
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
specifier|final
name|SpanReceiverHost
name|spanReceiverHost
decl_stmt|;
specifier|private
specifier|final
name|Connection
name|connection
decl_stmt|;
specifier|private
specifier|final
name|String
name|tableName
decl_stmt|;
specifier|public
name|CatalogStore
parameter_list|(
specifier|final
name|Configuration
name|configuration
parameter_list|,
specifier|final
name|String
name|tableName
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|connection
operator|=
name|ConnectionFactory
operator|.
name|createConnection
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|spanReceiverHost
operator|=
name|SpanReceiverHost
operator|.
name|getInstance
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
specifier|public
name|boolean
name|remove
parameter_list|(
specifier|final
name|String
name|key
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
specifier|final
name|Table
name|table
init|=
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
name|tableName
argument_list|)
argument_list|)
init|)
block|{
if|if
condition|(
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Delete
name|delete
init|=
operator|new
name|Delete
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|table
operator|.
name|delete
argument_list|(
name|delete
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|JsonObject
name|get
parameter_list|(
specifier|final
name|String
name|key
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
specifier|final
name|Table
name|table
init|=
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
name|tableName
argument_list|)
argument_list|)
init|)
block|{
specifier|final
name|Get
name|get
init|=
operator|new
name|Get
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Result
name|result
init|=
name|table
operator|.
name|get
argument_list|(
name|get
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Cell
name|cell
init|=
name|result
operator|.
name|getColumnLatestCell
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
literal|"c"
argument_list|)
argument_list|,
name|Bytes
operator|.
name|toBytes
argument_list|(
literal|"title"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Json
operator|.
name|createObjectBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"id"
argument_list|,
name|Bytes
operator|.
name|toString
argument_list|(
name|CellUtil
operator|.
name|cloneRow
argument_list|(
name|cell
argument_list|)
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"title"
argument_list|,
name|Bytes
operator|.
name|toString
argument_list|(
name|CellUtil
operator|.
name|cloneValue
argument_list|(
name|cell
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|JsonArray
name|scan
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|JsonArrayBuilder
name|builder
init|=
name|Json
operator|.
name|createArrayBuilder
argument_list|()
decl_stmt|;
try|try
init|(
specifier|final
name|Table
name|table
init|=
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
name|tableName
argument_list|)
argument_list|)
init|)
block|{
specifier|final
name|Scan
name|scan
init|=
operator|new
name|Scan
argument_list|()
decl_stmt|;
specifier|final
name|ResultScanner
name|results
init|=
name|table
operator|.
name|getScanner
argument_list|(
name|scan
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Result
name|result
range|:
name|results
control|)
block|{
specifier|final
name|Cell
name|cell
init|=
name|result
operator|.
name|getColumnLatestCell
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
literal|"c"
argument_list|)
argument_list|,
name|Bytes
operator|.
name|toBytes
argument_list|(
literal|"title"
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|Json
operator|.
name|createObjectBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"id"
argument_list|,
name|Bytes
operator|.
name|toString
argument_list|(
name|CellUtil
operator|.
name|cloneRow
argument_list|(
name|cell
argument_list|)
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"title"
argument_list|,
name|Bytes
operator|.
name|toString
argument_list|(
name|CellUtil
operator|.
name|cloneValue
argument_list|(
name|cell
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

