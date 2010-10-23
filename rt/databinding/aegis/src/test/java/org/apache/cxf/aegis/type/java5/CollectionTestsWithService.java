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
name|aegis
operator|.
name|type
operator|.
name|java5
package|;
end_package

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
name|HashMap
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
name|Map
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
name|aegis
operator|.
name|AbstractAegisTest
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
name|aegis
operator|.
name|databinding
operator|.
name|AegisDatabinding
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
name|aegis
operator|.
name|databinding
operator|.
name|XFireCompatibilityServiceConfiguration
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|CollectionTestsWithService
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|CollectionServiceInterface
name|csi
decl_stmt|;
specifier|private
name|CollectionService
name|impl
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|impl
operator|=
operator|new
name|CollectionService
argument_list|()
expr_stmt|;
name|createService
argument_list|(
name|CollectionServiceInterface
operator|.
name|class
argument_list|,
name|impl
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ClientProxyFactoryBean
name|proxyFac
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|proxyFac
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|XFireCompatibilityServiceConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setDataBinding
argument_list|(
operator|new
name|AegisDatabinding
argument_list|()
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setAddress
argument_list|(
literal|"local://CollectionServiceInterface"
argument_list|)
expr_stmt|;
name|proxyFac
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|csi
operator|=
name|proxyFac
operator|.
name|create
argument_list|(
name|CollectionServiceInterface
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * CXF-2017      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testNestedMap
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|BeanWithGregorianDate
argument_list|>
argument_list|>
name|complexMap
decl_stmt|;
name|complexMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|BeanWithGregorianDate
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|BeanWithGregorianDate
argument_list|>
name|innerMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|BeanWithGregorianDate
argument_list|>
argument_list|()
decl_stmt|;
name|BeanWithGregorianDate
name|bean
init|=
operator|new
name|BeanWithGregorianDate
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setName
argument_list|(
literal|"shem bean"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setId
argument_list|(
literal|42
argument_list|)
expr_stmt|;
name|innerMap
operator|.
name|put
argument_list|(
literal|"firstBean"
argument_list|,
name|bean
argument_list|)
expr_stmt|;
name|complexMap
operator|.
name|put
argument_list|(
literal|"firstKey"
argument_list|,
name|innerMap
argument_list|)
expr_stmt|;
name|csi
operator|.
name|mapOfMapWithStringAndPojo
argument_list|(
name|complexMap
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|BeanWithGregorianDate
argument_list|>
argument_list|>
name|gotMap
init|=
name|impl
operator|.
name|getLastComplexMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|gotMap
operator|.
name|containsKey
argument_list|(
literal|"firstKey"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|BeanWithGregorianDate
argument_list|>
name|v
init|=
name|gotMap
operator|.
name|get
argument_list|(
literal|"firstKey"
argument_list|)
decl_stmt|;
name|BeanWithGregorianDate
name|b
init|=
name|v
operator|.
name|get
argument_list|(
literal|"firstBean"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testListTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|SortedSet
argument_list|<
name|String
argument_list|>
name|strings
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|strings
operator|.
name|add
argument_list|(
literal|"Able"
argument_list|)
expr_stmt|;
name|strings
operator|.
name|add
argument_list|(
literal|"Baker"
argument_list|)
expr_stmt|;
name|String
name|first
init|=
name|csi
operator|.
name|takeSortedStrings
argument_list|(
name|strings
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Able"
argument_list|,
name|first
argument_list|)
expr_stmt|;
comment|//CHECKSTYLE:OFF
name|HashSet
argument_list|<
name|String
argument_list|>
name|hashedSet
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|hashedSet
operator|.
name|addAll
argument_list|(
name|strings
argument_list|)
expr_stmt|;
name|String
name|countString
init|=
name|csi
operator|.
name|takeUnsortedSet
argument_list|(
name|hashedSet
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|countString
argument_list|)
expr_stmt|;
comment|//CHECKSTYLE:ON
block|}
annotation|@
name|Test
specifier|public
name|void
name|returnValueIsCollectionOfArrays
parameter_list|()
block|{
name|Collection
argument_list|<
name|double
index|[]
argument_list|>
name|doubleDouble
init|=
name|csi
operator|.
name|returnCollectionOfPrimitiveArrays
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|doubleDouble
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|double
index|[]
index|[]
name|data
init|=
operator|new
name|double
index|[
literal|2
index|]
index|[]
decl_stmt|;
for|for
control|(
name|double
index|[]
name|array
range|:
name|doubleDouble
control|)
block|{
if|if
condition|(
name|array
operator|.
name|length
operator|==
literal|3
condition|)
block|{
name|data
index|[
literal|0
index|]
operator|=
name|array
expr_stmt|;
block|}
else|else
block|{
name|data
index|[
literal|1
index|]
operator|=
name|array
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
name|data
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|data
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3.14
argument_list|,
name|data
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|,
literal|.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|data
index|[
literal|0
index|]
index|[
literal|1
index|]
argument_list|,
literal|.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|666.6
argument_list|,
name|data
index|[
literal|0
index|]
index|[
literal|2
index|]
argument_list|,
literal|.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|666.6
argument_list|,
name|data
index|[
literal|1
index|]
index|[
literal|0
index|]
argument_list|,
literal|.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3.14
argument_list|,
name|data
index|[
literal|1
index|]
index|[
literal|1
index|]
argument_list|,
literal|.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2.0
argument_list|,
name|data
index|[
literal|1
index|]
index|[
literal|2
index|]
argument_list|,
literal|.0001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|returnValueIsCollectionOfArraysOfAny
parameter_list|()
block|{
name|Collection
argument_list|<
name|Document
index|[]
argument_list|>
name|r
init|=
name|csi
operator|.
name|returnCollectionOfDOMFragments
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|r
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

