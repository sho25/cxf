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
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

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
name|Collection
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
name|Application
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlAdapter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|endpoint
operator|.
name|Endpoint
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
name|model
operator|.
name|ParameterType
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
name|provider
operator|.
name|ProviderFactory
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
name|provider
operator|.
name|ServerProviderFactory
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
annotation|@
name|Test
specifier|public
name|void
name|testHandleParameterWithXmlAdapterOnInterface
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Arrange
name|String
name|value
init|=
literal|"1.1"
decl_stmt|;
comment|// Act
name|Object
name|id
init|=
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
name|value
argument_list|,
literal|true
argument_list|,
name|Id
operator|.
name|class
argument_list|,
name|Id
operator|.
name|class
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|ParameterType
operator|.
name|PATH
argument_list|,
name|createMessage
argument_list|()
argument_list|)
decl_stmt|;
comment|// Assert
name|assertTrue
argument_list|(
name|id
operator|instanceof
name|Id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
operator|(
operator|(
name|Id
operator|)
name|id
operator|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|Collection
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
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
name|Collection
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
argument_list|<>
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
argument_list|<>
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
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"set1"
argument_list|)
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|"set2"
argument_list|)
expr_stmt|;
name|bean2
operator|.
name|setS
argument_list|(
name|set
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
literal|7
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"d.s"
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"d.s"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"set1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"d.s"
argument_list|)
operator|.
name|contains
argument_list|(
literal|"set2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstantiateJAXBEnum
parameter_list|()
block|{
name|CarType
name|carType
init|=
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
literal|"AUDI"
argument_list|,
literal|false
argument_list|,
name|CarType
operator|.
name|class
argument_list|,
name|CarType
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|ParameterType
operator|.
name|QUERY
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Type is wrong"
argument_list|,
name|CarType
operator|.
name|AUDI
argument_list|,
name|carType
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstantiateIntegerInQuery
parameter_list|()
block|{
name|Integer
name|integer
init|=
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
literal|""
argument_list|,
literal|false
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|ParameterType
operator|.
name|QUERY
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Integer is not null"
argument_list|,
name|integer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstantiateFloatInQuery
parameter_list|()
block|{
name|Float
name|f
init|=
name|InjectionUtils
operator|.
name|handleParameter
argument_list|(
literal|""
argument_list|,
literal|false
argument_list|,
name|float
operator|.
name|class
argument_list|,
name|float
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|ParameterType
operator|.
name|QUERY
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Float is not 0"
argument_list|,
name|Float
operator|.
name|valueOf
argument_list|(
literal|0F
argument_list|)
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenericInterfaceType
parameter_list|()
throws|throws
name|NoSuchMethodException
block|{
name|Type
name|str
init|=
name|InjectionUtils
operator|.
name|getGenericResponseType
argument_list|(
name|GenericInterface
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"get"
argument_list|)
argument_list|,
name|TestService
operator|.
name|class
argument_list|,
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|,
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|str
argument_list|)
expr_stmt|;
name|ParameterizedType
name|list
init|=
operator|(
name|ParameterizedType
operator|)
name|InjectionUtils
operator|.
name|getGenericResponseType
argument_list|(
name|GenericInterface
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"list"
argument_list|)
argument_list|,
name|TestService
operator|.
name|class
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|,
name|ArrayList
operator|.
name|class
argument_list|,
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|list
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
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
specifier|private
name|String
name|e
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
specifier|public
name|void
name|setE
parameter_list|(
name|String
name|ee
parameter_list|)
block|{
name|this
operator|.
name|e
operator|=
name|ee
expr_stmt|;
block|}
specifier|public
name|String
name|getE
parameter_list|()
block|{
return|return
name|e
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
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|s
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
specifier|public
name|void
name|setS
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|set
parameter_list|)
block|{
name|this
operator|.
name|s
operator|=
name|set
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getS
parameter_list|()
block|{
return|return
name|this
operator|.
name|s
return|;
block|}
block|}
specifier|private
name|Message
name|createMessage
parameter_list|()
block|{
name|ProviderFactory
name|factory
init|=
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.http.case_insensitive_queries"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Exchange
name|e
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|e
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|get
argument_list|(
name|Application
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|size
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|get
argument_list|(
name|ServerProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|factory
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|e
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
specifier|public
specifier|static
class|class
name|Adapter
extends|extends
name|XmlAdapter
argument_list|<
name|String
argument_list|,
name|Id
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|String
name|marshal
parameter_list|(
specifier|final
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|id
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Id
name|unmarshal
parameter_list|(
specifier|final
name|String
name|idStr
parameter_list|)
throws|throws
name|Exception
block|{
name|Id
name|id
init|=
operator|new
name|DelegatingId
argument_list|()
decl_stmt|;
name|id
operator|.
name|setId
argument_list|(
name|idStr
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
block|}
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|Adapter
operator|.
name|class
argument_list|)
specifier|public
interface|interface
name|Id
block|{
name|String
name|getId
parameter_list|()
function_decl|;
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
specifier|public
specifier|static
class|class
name|DelegatingId
implements|implements
name|Id
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
specifier|public
enum|enum
name|CarType
block|{
name|AUDI
argument_list|(
literal|"Audi"
argument_list|)
block|,
name|GOLF
argument_list|(
literal|"Golf"
argument_list|)
block|,
name|BMW
argument_list|(
literal|"BMW"
argument_list|)
block|;
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
name|CarType
parameter_list|(
name|String
name|v
parameter_list|)
block|{
name|value
operator|=
name|v
expr_stmt|;
block|}
specifier|public
name|String
name|value
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
specifier|static
name|CarType
name|fromValue
parameter_list|(
name|String
name|v
parameter_list|)
block|{
for|for
control|(
name|CarType
name|c
range|:
name|CarType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|value
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|c
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|v
argument_list|)
throw|;
block|}
block|}
interface|interface
name|GenericInterface
parameter_list|<
name|A
parameter_list|>
block|{
name|A
name|get
parameter_list|()
function_decl|;
name|List
argument_list|<
name|A
argument_list|>
name|list
parameter_list|()
function_decl|;
block|}
interface|interface
name|ServiceInterface
extends|extends
name|Serializable
extends|,
name|GenericInterface
argument_list|<
name|String
argument_list|>
block|{     }
specifier|public
specifier|static
class|class
name|TestService
implements|implements
name|Serializable
implements|,
name|ServiceInterface
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

