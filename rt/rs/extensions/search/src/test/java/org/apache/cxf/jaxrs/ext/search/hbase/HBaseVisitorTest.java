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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|hbase
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
name|jaxrs
operator|.
name|ext
operator|.
name|search
operator|.
name|SearchBean
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
name|ext
operator|.
name|search
operator|.
name|SearchCondition
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
name|ext
operator|.
name|search
operator|.
name|fiql
operator|.
name|FiqlParser
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
name|HBaseConfiguration
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
name|filter
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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

begin_class
specifier|public
class|class
name|HBaseVisitorTest
block|{
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|BOOK_FAMILY
init|=
literal|"book"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|NAME_QUALIFIER
init|=
literal|"name"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|Table
name|table
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Configuration
name|hBaseConfig
init|=
name|HBaseConfiguration
operator|.
name|create
argument_list|()
decl_stmt|;
name|Connection
name|connection
init|=
name|ConnectionFactory
operator|.
name|createConnection
argument_list|(
name|hBaseConfig
argument_list|)
decl_stmt|;
name|table
operator|=
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
literal|"books"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"Enable as soon as it is understood how to run HBase tests in process"
argument_list|)
specifier|public
name|void
name|testScanWithFilterVisitor
parameter_list|()
throws|throws
name|Exception
block|{
name|Scan
name|scan
init|=
operator|new
name|Scan
argument_list|()
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|sc
init|=
operator|new
name|FiqlParser
argument_list|<>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
operator|.
name|parse
argument_list|(
literal|"name==CXF"
argument_list|)
decl_stmt|;
name|HBaseQueryVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|HBaseQueryVisitor
argument_list|<>
argument_list|(
literal|"book"
argument_list|)
decl_stmt|;
name|sc
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|Filter
name|filter
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|scan
operator|.
name|setFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
try|try
init|(
name|ResultScanner
name|rs
init|=
name|table
operator|.
name|getScanner
argument_list|(
name|scan
argument_list|)
init|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Result
name|r
init|=
name|rs
operator|.
name|next
argument_list|()
init|;
name|r
operator|!=
literal|null
condition|;
name|r
operator|=
name|rs
operator|.
name|next
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
literal|"row2"
argument_list|,
operator|new
name|String
argument_list|(
name|r
operator|.
name|getRow
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF"
argument_list|,
operator|new
name|String
argument_list|(
name|r
operator|.
name|getValue
argument_list|(
name|BOOK_FAMILY
argument_list|,
name|NAME_QUALIFIER
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

