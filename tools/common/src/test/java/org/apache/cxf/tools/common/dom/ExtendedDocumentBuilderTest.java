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
name|tools
operator|.
name|common
operator|.
name|dom
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|ExtendedDocumentBuilderTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMassMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtendedDocumentBuilder
name|builder
init|=
operator|new
name|ExtendedDocumentBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|String
name|tsSource
init|=
literal|"/org/apache/cxf/tools/common/toolspec/parser/resources/testtool.xml"
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
operator|.
name|parse
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParse
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtendedDocumentBuilder
name|builder
init|=
operator|new
name|ExtendedDocumentBuilder
argument_list|()
decl_stmt|;
name|String
name|tsSource
init|=
literal|"/org/apache/cxf/tools/common/toolspec/parser/resources/testtool1.xml"
decl_stmt|;
name|Document
name|doc
init|=
name|builder
operator|.
name|parse
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|doc
operator|.
name|getXmlVersion
argument_list|()
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

