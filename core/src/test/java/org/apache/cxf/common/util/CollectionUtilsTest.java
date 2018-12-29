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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
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

begin_class
specifier|public
class|class
name|CollectionUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDiff
parameter_list|()
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|l1
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|,
literal|"3"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|l2
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"2"
argument_list|,
literal|"4"
argument_list|,
literal|"5"
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|l3
init|=
name|CollectionUtils
operator|.
name|diff
argument_list|(
name|l1
argument_list|,
name|l2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|l3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|l3
operator|.
name|contains
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|l3
operator|.
name|contains
argument_list|(
literal|"3"
argument_list|)
argument_list|)
expr_stmt|;
name|l3
operator|=
name|CollectionUtils
operator|.
name|diff
argument_list|(
name|l1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|l3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|l3
operator|.
name|containsAll
argument_list|(
name|l1
argument_list|)
argument_list|)
expr_stmt|;
name|l3
operator|=
name|CollectionUtils
operator|.
name|diff
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|l3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|l
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|CollectionUtils
operator|.
name|isEmpty
argument_list|(
name|l
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToDictionaryNull
parameter_list|()
throws|throws
name|Exception
block|{
name|Dictionary
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|d
init|=
name|CollectionUtils
operator|.
name|toDictionary
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|d
operator|.
name|elements
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|d
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|d
operator|.
name|keys
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|d
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSingletonDictionary
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|key
init|=
literal|"k"
decl_stmt|;
name|String
name|value
init|=
literal|"v"
decl_stmt|;
name|Dictionary
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|d
init|=
name|CollectionUtils
operator|.
name|singletonDictionary
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|d
operator|.
name|elements
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|d
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|d
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|d
operator|.
name|keys
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|d
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

