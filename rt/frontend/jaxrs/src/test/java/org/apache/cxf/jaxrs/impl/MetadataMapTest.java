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
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|MetadataMapTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPutSingle
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value1
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|value1
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"baz"
argument_list|,
literal|"clazz"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value2
init|=
name|m
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only a single value should be in the list"
argument_list|,
literal|1
argument_list|,
name|value2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value is wrong"
argument_list|,
literal|"clazz"
argument_list|,
name|value2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|m
operator|.
name|get
argument_list|(
literal|"baZ"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPutSingleCaseInsensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value1
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|value1
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"baz"
argument_list|,
literal|"clazz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value2
init|=
name|m
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only a single value should be in the list"
argument_list|,
literal|1
argument_list|,
name|value2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value is wrong"
argument_list|,
literal|"clazz"
argument_list|,
name|value2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Baz"
argument_list|,
literal|"clazz2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|value2
operator|=
name|m
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Only a single value should be in the list"
argument_list|,
literal|1
argument_list|,
name|value2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value is wrong"
argument_list|,
literal|"clazz2"
argument_list|,
name|value2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|containsKey
argument_list|(
literal|"Baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|containsKey
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContainsKeyCaseInsensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|containsKey
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContainsKeyCaseSensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|containsKey
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|m
operator|.
name|containsKey
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testKeySetCaseInsensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testKeySetCaseSensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPutAllCaseInsensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value1
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|value1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|m
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m2
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value2
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|value2
operator|.
name|add
argument_list|(
literal|"bar2"
argument_list|)
expr_stmt|;
name|value2
operator|.
name|add
argument_list|(
literal|"foo2"
argument_list|)
expr_stmt|;
name|m2
operator|.
name|put
argument_list|(
literal|"BaZ"
argument_list|,
name|value2
argument_list|)
expr_stmt|;
name|m
operator|.
name|putAll
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|values
operator|=
name|m
operator|.
name|get
argument_list|(
literal|"Baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar2"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo2"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveCaseInsensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value1
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|value1
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|value1
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"baz"
argument_list|,
literal|"clazz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|remove
argument_list|(
literal|"Baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|m
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
name|testAddAndGetFirst
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|value
init|=
name|m
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only a single value should be in the list"
argument_list|,
literal|1
argument_list|,
name|value
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value is wrong"
argument_list|,
literal|"bar"
argument_list|,
name|value
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|value
operator|=
name|m
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Two values should be in the list"
argument_list|,
literal|2
argument_list|,
name|value
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value1 is wrong"
argument_list|,
literal|"bar"
argument_list|,
name|value
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Value2 is wrong"
argument_list|,
literal|"foo"
argument_list|,
name|value
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GetFirst value is wrong"
argument_list|,
literal|"bar"
argument_list|,
name|m
operator|.
name|getFirst
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCopyAndUpdate
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m2
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|m
operator|.
name|remove
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|m2
operator|.
name|getFirst
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|m
operator|.
name|getFirst
argument_list|(
literal|"baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testReadOnly
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m2
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|m
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|m2
operator|.
name|remove
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetCaseInsensitive
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"Baz"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|m2
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|m
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|m2
operator|.
name|getFirst
argument_list|(
literal|"baZ"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|m2
operator|.
name|getFirst
argument_list|(
literal|"Baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m2
operator|.
name|containsKey
argument_list|(
literal|"BaZ"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m2
operator|.
name|containsKey
argument_list|(
literal|"Baz"
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
name|m2
operator|.
name|get
argument_list|(
literal|"baz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

