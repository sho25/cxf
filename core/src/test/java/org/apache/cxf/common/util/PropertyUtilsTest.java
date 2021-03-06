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
name|common
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|PropertyUtilsTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_KEY
init|=
literal|"my.key"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testIsTrueWithMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsFalseWithMap
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|props
argument_list|,
name|TEST_KEY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTrue
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"true"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"TRUE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"TrUe"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"FALSE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"FaLSE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
literal|"other"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFalse
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"false"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"FALSE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"FaLSE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"true"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"TRUE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"TrUe"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|PropertyUtils
operator|.
name|isFalse
argument_list|(
literal|"other"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

