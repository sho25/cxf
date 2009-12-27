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
name|beans
operator|.
name|Introspector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyDescriptor
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
name|Method
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|type
operator|.
name|AegisType
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
name|type
operator|.
name|DefaultTypeMapping
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
name|type
operator|.
name|TypeCreationOptions
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
name|type
operator|.
name|collection
operator|.
name|CollectionType
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
name|type
operator|.
name|collection
operator|.
name|MapType
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
name|type
operator|.
name|java5
operator|.
name|dto
operator|.
name|CollectionDTO
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
name|type
operator|.
name|java5
operator|.
name|dto
operator|.
name|DTOService
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
name|type
operator|.
name|java5
operator|.
name|dto
operator|.
name|ObjectDTO
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
name|common
operator|.
name|util
operator|.
name|SOAPConstants
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

begin_class
specifier|public
class|class
name|CollectionTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|DefaultTypeMapping
name|tm
decl_stmt|;
specifier|private
name|Java5TypeCreator
name|creator
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|tm
operator|=
operator|new
name|DefaultTypeMapping
argument_list|(
name|SOAPConstants
operator|.
name|XSD
argument_list|)
expr_stmt|;
name|creator
operator|=
operator|new
name|Java5TypeCreator
argument_list|()
expr_stmt|;
name|creator
operator|.
name|setConfiguration
argument_list|(
operator|new
name|TypeCreationOptions
argument_list|()
argument_list|)
expr_stmt|;
name|tm
operator|.
name|setTypeCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testType
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|m
init|=
name|CollectionService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getStrings"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|AegisType
name|type
init|=
name|creator
operator|.
name|createType
argument_list|(
name|m
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CollectionType
argument_list|)
expr_stmt|;
name|CollectionType
name|colType
init|=
operator|(
name|CollectionType
operator|)
name|type
decl_stmt|;
name|QName
name|componentName
init|=
name|colType
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ArrayOfString"
argument_list|,
name|componentName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArrayOfString"
argument_list|,
name|componentName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|=
name|colType
operator|.
name|getComponentType
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|.
name|getTypeClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testRecursiveCollections
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|m
init|=
name|CollectionService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getStringCollections"
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|AegisType
name|type
init|=
name|creator
operator|.
name|createType
argument_list|(
name|m
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CollectionType
argument_list|)
expr_stmt|;
name|CollectionType
name|colType
init|=
operator|(
name|CollectionType
operator|)
name|type
decl_stmt|;
name|QName
name|componentName
init|=
name|colType
operator|.
name|getSchemaType
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ArrayOfArrayOfString"
argument_list|,
name|componentName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|=
name|colType
operator|.
name|getComponentType
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CollectionType
argument_list|)
expr_stmt|;
name|CollectionType
name|colType2
init|=
operator|(
name|CollectionType
operator|)
name|type
decl_stmt|;
name|componentName
operator|=
name|colType2
operator|.
name|getSchemaType
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArrayOfString"
argument_list|,
name|componentName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|type
operator|=
name|colType2
operator|.
name|getComponentType
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|.
name|getTypeClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testPDType
parameter_list|()
throws|throws
name|Exception
block|{
name|PropertyDescriptor
name|pd
init|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|CollectionDTO
operator|.
name|class
argument_list|,
name|Object
operator|.
name|class
argument_list|)
operator|.
name|getPropertyDescriptors
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|AegisType
name|type
init|=
name|creator
operator|.
name|createType
argument_list|(
name|pd
argument_list|)
decl_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CollectionType
argument_list|)
expr_stmt|;
name|CollectionType
name|colType
init|=
operator|(
name|CollectionType
operator|)
name|type
decl_stmt|;
name|type
operator|=
name|colType
operator|.
name|getComponentType
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|.
name|getTypeClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|String
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
name|testCollectionDTO
parameter_list|()
block|{
name|tm
operator|=
operator|new
name|DefaultTypeMapping
argument_list|(
name|SOAPConstants
operator|.
name|XSD
argument_list|)
expr_stmt|;
name|creator
operator|=
operator|new
name|Java5TypeCreator
argument_list|()
expr_stmt|;
name|creator
operator|.
name|setConfiguration
argument_list|(
operator|new
name|TypeCreationOptions
argument_list|()
argument_list|)
expr_stmt|;
name|tm
operator|.
name|setTypeCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
name|AegisType
name|dto
init|=
name|creator
operator|.
name|createType
argument_list|(
name|CollectionDTO
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
name|deps
init|=
name|dto
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|AegisType
name|type
init|=
operator|(
name|AegisType
operator|)
name|deps
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CollectionType
argument_list|)
expr_stmt|;
name|CollectionType
name|colType
init|=
operator|(
name|CollectionType
operator|)
name|type
decl_stmt|;
name|deps
operator|=
name|dto
operator|.
name|getDependencies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|deps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AegisType
name|comType
init|=
name|colType
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|comType
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectDTO
parameter_list|()
block|{
name|tm
operator|=
operator|new
name|DefaultTypeMapping
argument_list|(
name|SOAPConstants
operator|.
name|XSD
argument_list|)
expr_stmt|;
name|creator
operator|=
operator|new
name|Java5TypeCreator
argument_list|()
expr_stmt|;
name|creator
operator|.
name|setConfiguration
argument_list|(
operator|new
name|TypeCreationOptions
argument_list|()
argument_list|)
expr_stmt|;
name|tm
operator|.
name|setTypeCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
name|AegisType
name|dto
init|=
name|creator
operator|.
name|createType
argument_list|(
name|ObjectDTO
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
name|deps
init|=
name|dto
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|deps
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|AegisType
name|type
init|=
operator|(
name|AegisType
operator|)
name|deps
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|CollectionType
argument_list|)
expr_stmt|;
name|CollectionType
name|colType
init|=
operator|(
name|CollectionType
operator|)
name|type
decl_stmt|;
name|deps
operator|=
name|dto
operator|.
name|getDependencies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|deps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|AegisType
name|comType
init|=
name|colType
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|comType
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionDTOService
parameter_list|()
throws|throws
name|Exception
block|{
name|createService
argument_list|(
name|DTOService
operator|.
name|class
argument_list|)
expr_stmt|;
name|invoke
argument_list|(
literal|"DTOService"
argument_list|,
literal|"/org/apache/cxf/aegis/type/java5/dto/GetDTO.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCollectionServiceWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|createService
argument_list|(
name|CollectionServiceInterface
operator|.
name|class
argument_list|,
operator|new
name|CollectionService
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Document
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"CollectionServiceInterface"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:element[@name='return'][@type='tns:ArrayOfString']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUnannotatedStrings
parameter_list|()
throws|throws
name|Exception
block|{
name|createService
argument_list|(
name|CollectionServiceInterface
operator|.
name|class
argument_list|,
operator|new
name|CollectionService
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"CollectionServiceInterface"
argument_list|)
decl_stmt|;
comment|// printNode(doc);
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='getUnannotatedStringsResponse']"
operator|+
literal|"/xsd:sequence/xsd:element[@type='tns:ArrayOfString']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDoubleList
parameter_list|()
throws|throws
name|Exception
block|{
name|createService
argument_list|(
name|CollectionServiceInterface
operator|.
name|class
argument_list|,
operator|new
name|CollectionService
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"CollectionServiceInterface"
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='ArrayOfDouble']"
operator|+
literal|"/xsd:sequence/xsd:element[@type='xsd:double']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
comment|/**      * CXF-1833 complained of a bizarre schema when @@WebParaming a parameter of List<String>. This regression      * test captures the fact that we don't, in fact, have this problem with correct use of JAX-WS.      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|webMethodOnListParam
parameter_list|()
throws|throws
name|Exception
block|{
name|createJaxwsService
argument_list|(
name|CollectionService
operator|.
name|class
argument_list|,
operator|new
name|CollectionService
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"CollectionServiceService"
argument_list|)
decl_stmt|;
comment|// what we do not want is<xsd:schema targetNamespace="http://util.java" ... />
name|assertInvalid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://util.java']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNestedMapType
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|m
init|=
name|CollectionService
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"mapOfMapWithStringAndPojo"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Map
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|AegisType
name|type
init|=
name|creator
operator|.
name|createType
argument_list|(
name|m
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|tm
operator|.
name|register
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|type
operator|instanceof
name|MapType
argument_list|)
expr_stmt|;
name|MapType
name|mapType
init|=
operator|(
name|MapType
operator|)
name|type
decl_stmt|;
name|AegisType
name|valueType
init|=
name|mapType
operator|.
name|getValueType
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|valueType
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|.
name|contains
argument_list|(
literal|"any"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

