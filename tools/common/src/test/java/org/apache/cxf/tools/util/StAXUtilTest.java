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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|tools
operator|.
name|common
operator|.
name|ProcessorTestBase
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
name|tools
operator|.
name|common
operator|.
name|Tag
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
name|StAXUtilTest
extends|extends
name|ProcessorTestBase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetTags
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/test.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|file
operator|=
name|getResource
argument_list|(
literal|"resources/test2.wsdl"
argument_list|)
expr_stmt|;
name|Tag
name|tag1
init|=
name|StAXUtil
operator|.
name|getTagTree
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tag1
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Tag
name|def1
init|=
name|tag1
operator|.
name|getTags
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|def1
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Tag
name|types1
init|=
name|def1
operator|.
name|getTags
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Tag
name|schema1
init|=
name|types1
operator|.
name|getTags
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|schema1
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|file
operator|=
name|getResource
argument_list|(
literal|"resources/test3.wsdl"
argument_list|)
expr_stmt|;
name|Tag
name|tag2
init|=
name|StAXUtil
operator|.
name|getTagTree
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tag2
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Tag
name|def2
init|=
name|tag2
operator|.
name|getTags
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|def2
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Tag
name|types2
init|=
name|def2
operator|.
name|getTags
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Tag
name|schema2
init|=
name|types2
operator|.
name|getTags
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|schema2
operator|.
name|getTags
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTagEquals
argument_list|(
name|schema1
argument_list|,
name|schema2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

