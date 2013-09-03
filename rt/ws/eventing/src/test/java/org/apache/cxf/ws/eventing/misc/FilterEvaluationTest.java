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
name|eventing
operator|.
name|misc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|CharArrayReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

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
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
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
name|eventing
operator|.
name|FilterType
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
name|eventing
operator|.
name|shared
operator|.
name|utils
operator|.
name|FilteringUtil
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

begin_class
specifier|public
class|class
name|FilterEvaluationTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|simpleFilterEvaluationPositive
parameter_list|()
throws|throws
name|Exception
block|{
name|Reader
name|reader
init|=
operator|new
name|CharArrayReader
argument_list|(
literal|"<tt><in>1</in></tt>"
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|FilterType
name|filter
init|=
operator|new
name|FilterType
argument_list|()
decl_stmt|;
name|filter
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"//tt"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|FilteringUtil
operator|.
name|doesConformToFilter
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|filter
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|simpleFilterEvaluationNegative
parameter_list|()
throws|throws
name|Exception
block|{
name|Reader
name|reader
init|=
operator|new
name|CharArrayReader
argument_list|(
literal|"<tt><in>1</in></tt>"
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|FilterType
name|filter
init|=
operator|new
name|FilterType
argument_list|()
decl_stmt|;
name|filter
operator|.
name|getContent
argument_list|()
operator|.
name|add
argument_list|(
literal|"//ttx"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|FilteringUtil
operator|.
name|doesConformToFilter
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
name|filter
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|validFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|FilteringUtil
operator|.
name|isValidFilter
argument_list|(
literal|"//filter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|invalidFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertFalse
argument_list|(
name|FilteringUtil
operator|.
name|isValidFilter
argument_list|(
literal|"@/$"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

