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
name|Arrays
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|assertSame
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
name|MetadataMapTest
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
argument_list|<>
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
argument_list|<>
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
name|testPutSingleNullKey
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
argument_list|<>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|null
argument_list|,
literal|"null"
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|null
argument_list|,
literal|"null2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"null2"
argument_list|,
name|m
operator|.
name|getFirst
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPutSingleNullKeyCaseSensitive
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
argument_list|<>
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
literal|null
argument_list|,
literal|"null"
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|null
argument_list|,
literal|"null2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"null2"
argument_list|,
name|m
operator|.
name|getFirst
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPutSingleNullKeyCaseSensitive2
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Object
name|obj1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|obj2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|"key"
argument_list|,
name|obj1
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|"key"
argument_list|,
name|obj2
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|null
argument_list|,
name|obj2
argument_list|)
expr_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|null
argument_list|,
name|obj1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
operator|.
name|getFirst
argument_list|(
literal|"key"
argument_list|)
argument_list|,
name|obj2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
operator|.
name|getFirst
argument_list|(
literal|null
argument_list|)
argument_list|,
name|obj1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddFirst
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
argument_list|<>
argument_list|()
decl_stmt|;
name|m
operator|.
name|addFirst
argument_list|(
literal|"baz"
argument_list|,
literal|"foo"
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
literal|"foo"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|addFirst
argument_list|(
literal|"baz"
argument_list|,
literal|"clazz"
argument_list|)
expr_stmt|;
name|values
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
literal|"clazz"
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddFirstUnmodifiableListFirst
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
argument_list|<>
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|Arrays
operator|.
expr|<
name|Object
operator|>
name|asList
argument_list|(
literal|"foo"
argument_list|)
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
literal|"foo"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|addFirst
argument_list|(
literal|"baz"
argument_list|,
literal|"clazz"
argument_list|)
expr_stmt|;
name|values
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
literal|"clazz"
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddAll
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
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|m
operator|.
name|addAll
argument_list|(
literal|"baz"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|values
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
literal|"foo"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|addAll
argument_list|(
literal|"baz"
argument_list|,
name|Collections
operator|.
expr|<
name|Object
operator|>
name|singletonList
argument_list|(
literal|"foo2"
argument_list|)
argument_list|)
expr_stmt|;
name|values
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
literal|"foo"
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
name|testReadOnlyRemove
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
argument_list|<>
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
argument_list|<>
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
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testReadOnlyAdd
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
argument_list|<>
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
argument_list|<>
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
name|add
argument_list|(
literal|"bar"
argument_list|,
literal|"foo"
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
name|testReadOnlyAddFirst
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
argument_list|<>
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
argument_list|<>
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
name|addFirst
argument_list|(
literal|"baz"
argument_list|,
literal|"bar2"
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
name|testReadOnlyAdd2
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|values
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|(
name|values
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|map
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"baz"
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
name|testReadOnlyAddFirst2
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|values
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|list
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|(
name|values
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|map
operator|.
name|addFirst
argument_list|(
literal|"baz"
argument_list|,
literal|"bar2"
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
name|testReadOnlyPutSingle
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|(
name|values
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|map
operator|.
name|putSingle
argument_list|(
literal|"baz"
argument_list|,
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
argument_list|<>
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
argument_list|<>
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
annotation|@
name|Test
specifier|public
name|void
name|testGetFirstEmptyMap
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
argument_list|<>
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|m
operator|.
name|getFirst
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"key"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|m
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|m
operator|.
name|getFirst
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompareIgnoreValueOrder
parameter_list|()
block|{
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar1"
argument_list|)
expr_stmt|;
name|m
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar2"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
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
literal|"bar1"
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
literal|"bar2"
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
name|String
argument_list|>
name|m2
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|m2
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar2"
argument_list|)
expr_stmt|;
name|m2
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar1"
argument_list|)
expr_stmt|;
name|values
operator|=
name|m2
operator|.
name|get
argument_list|(
literal|"baz"
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
literal|"bar1"
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|equalsIgnoreValueOrder
argument_list|(
name|m2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m
operator|.
name|equalsIgnoreValueOrder
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|m2
operator|.
name|equalsIgnoreValueOrder
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m3
init|=
operator|new
name|MetadataMap
argument_list|<>
argument_list|()
decl_stmt|;
name|m3
operator|.
name|add
argument_list|(
literal|"baz"
argument_list|,
literal|"bar1"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|m
operator|.
name|equalsIgnoreValueOrder
argument_list|(
name|m3
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|m2
operator|.
name|equalsIgnoreValueOrder
argument_list|(
name|m3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

