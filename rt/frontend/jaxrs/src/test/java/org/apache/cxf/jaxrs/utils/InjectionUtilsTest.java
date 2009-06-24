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
name|utils
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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

begin_class
specifier|public
class|class
name|InjectionUtilsTest
extends|extends
name|Assert
block|{
specifier|public
name|void
name|testCollectionTypeFromArray
parameter_list|()
block|{
name|assertNull
argument_list|(
name|InjectionUtils
operator|.
name|getCollectionType
argument_list|(
name|String
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionType
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ArrayList
operator|.
name|class
argument_list|,
name|InjectionUtils
operator|.
name|getCollectionType
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashSet
operator|.
name|class
argument_list|,
name|InjectionUtils
operator|.
name|getCollectionType
argument_list|(
name|Set
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TreeSet
operator|.
name|class
argument_list|,
name|InjectionUtils
operator|.
name|getCollectionType
argument_list|(
name|SortedSet
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSupportedCollectionType
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|String
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|Set
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|SortedSet
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtractValuesFromBean
parameter_list|()
block|{
name|CustomerBean1
name|bean1
init|=
operator|new
name|CustomerBean1
argument_list|()
decl_stmt|;
name|bean1
operator|.
name|setA
argument_list|(
literal|"aValue"
argument_list|)
expr_stmt|;
name|bean1
operator|.
name|setB
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"lv1"
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"lv2"
argument_list|)
expr_stmt|;
name|bean1
operator|.
name|setC
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|CustomerBean2
name|bean2
init|=
operator|new
name|CustomerBean2
argument_list|()
decl_stmt|;
name|bean2
operator|.
name|setA
argument_list|(
literal|"aaValue"
argument_list|)
expr_stmt|;
name|bean2
operator|.
name|setB
argument_list|(
literal|2L
argument_list|)
expr_stmt|;
name|values
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"lv11"
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"lv22"
argument_list|)
expr_stmt|;
name|bean2
operator|.
name|setC
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|bean1
operator|.
name|setD
argument_list|(
name|bean2
argument_list|)
expr_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
name|InjectionUtils
operator|.
name|extractValuesFromBean
argument_list|(
name|bean1
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Size is wrong"
argument_list|,
literal|6
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
literal|"a"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aValue"
argument_list|,
name|map
operator|.
name|getFirst
argument_list|(
literal|"a"
argument_list|)
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
literal|"b"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|map
operator|.
name|getFirst
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lv1"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lv2"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"c"
argument_list|)
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
literal|"d.a"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aaValue"
argument_list|,
name|map
operator|.
name|getFirst
argument_list|(
literal|"d.a"
argument_list|)
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
literal|"d.b"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|map
operator|.
name|getFirst
argument_list|(
literal|"d.b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"d.c"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lv11"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"d.c"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"lv22"
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"d.c"
argument_list|)
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|CustomerBean1
block|{
specifier|private
name|String
name|a
decl_stmt|;
specifier|private
name|Long
name|b
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|c
decl_stmt|;
specifier|private
name|CustomerBean2
name|d
decl_stmt|;
specifier|public
name|void
name|setA
parameter_list|(
name|String
name|aString
parameter_list|)
block|{
name|this
operator|.
name|a
operator|=
name|aString
expr_stmt|;
block|}
specifier|public
name|void
name|setB
parameter_list|(
name|Long
name|bLong
parameter_list|)
block|{
name|this
operator|.
name|b
operator|=
name|bLong
expr_stmt|;
block|}
specifier|public
name|void
name|setC
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|cStringList
parameter_list|)
block|{
name|this
operator|.
name|c
operator|=
name|cStringList
expr_stmt|;
block|}
specifier|public
name|void
name|setD
parameter_list|(
name|CustomerBean2
name|dCustomerBean
parameter_list|)
block|{
name|this
operator|.
name|d
operator|=
name|dCustomerBean
expr_stmt|;
block|}
specifier|public
name|String
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
specifier|public
name|Long
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getC
parameter_list|()
block|{
return|return
name|c
return|;
block|}
specifier|public
name|CustomerBean2
name|getD
parameter_list|()
block|{
return|return
name|d
return|;
block|}
block|}
specifier|static
class|class
name|CustomerBean2
block|{
specifier|private
name|String
name|a
decl_stmt|;
specifier|private
name|Long
name|b
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|c
decl_stmt|;
specifier|public
name|void
name|setA
parameter_list|(
name|String
name|aString
parameter_list|)
block|{
name|this
operator|.
name|a
operator|=
name|aString
expr_stmt|;
block|}
specifier|public
name|void
name|setB
parameter_list|(
name|Long
name|bLong
parameter_list|)
block|{
name|this
operator|.
name|b
operator|=
name|bLong
expr_stmt|;
block|}
specifier|public
name|void
name|setC
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|cStringList
parameter_list|)
block|{
name|this
operator|.
name|c
operator|=
name|cStringList
expr_stmt|;
block|}
specifier|public
name|String
name|getA
parameter_list|()
block|{
return|return
name|a
return|;
block|}
specifier|public
name|Long
name|getB
parameter_list|()
block|{
return|return
name|b
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getC
parameter_list|()
block|{
return|return
name|c
return|;
block|}
block|}
block|}
end_class

end_unit

