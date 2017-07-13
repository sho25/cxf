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
name|sql
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
name|SearchParseException
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

begin_comment
comment|//userName eq "admin@amarkevich.talend.com" and entitlements sw "TDP_"
end_comment

begin_class
specifier|public
class|class
name|SQLHierarchicalQueryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSimpleHierarchicalQuery
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
name|parser
init|=
operator|new
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"cartridges.colour==blue"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"printers"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
operator|.
name|visitor
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM printers left join cartridges"
operator|+
literal|" on printers.id = cartridges.printer_id"
operator|+
literal|" WHERE cartridges.colour = 'blue'"
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAndHierarchicalQuery
parameter_list|()
throws|throws
name|SearchParseException
block|{
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
name|parser
init|=
operator|new
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"name==Epson;cartridges.colour==blue"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"printers"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
operator|.
name|visitor
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|visitor
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SELECT * FROM printers left join cartridges"
operator|+
literal|" on printers.id = cartridges.printer_id"
operator|+
literal|" WHERE (name = 'Epson') AND (cartridges.colour = 'blue')"
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|SearchParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testLongHierarchicalQuery
parameter_list|()
block|{
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
name|parser
init|=
operator|new
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"cartridges.producer.location==Japan"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"printers"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
operator|.
name|visitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|SearchParseException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testTooManyJoins
parameter_list|()
block|{
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
name|parser
init|=
operator|new
name|FiqlParser
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
name|SearchBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|SearchCondition
argument_list|<
name|SearchBean
argument_list|>
name|filter
init|=
name|parser
operator|.
name|parse
argument_list|(
literal|"cartridges.colour==blue;cartridges.location==Japan"
argument_list|)
decl_stmt|;
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
name|visitor
init|=
operator|new
name|SQLPrinterVisitor
argument_list|<
name|SearchBean
argument_list|>
argument_list|(
literal|"printers"
argument_list|)
decl_stmt|;
name|filter
operator|.
name|accept
argument_list|(
name|visitor
operator|.
name|visitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
