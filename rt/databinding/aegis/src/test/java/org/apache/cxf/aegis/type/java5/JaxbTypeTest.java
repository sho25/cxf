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
name|Iterator
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
name|TypeMapping
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
name|XMLTypeCreator
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
name|basic
operator|.
name|BeanType
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
name|basic
operator|.
name|StringType
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
name|Server
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
name|service
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
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
name|JaxbTypeTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|TypeMapping
name|tm
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|AegisDatabinding
name|databinding
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
name|Server
name|s
init|=
name|createService
argument_list|(
name|JaxbService
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|=
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
expr_stmt|;
name|databinding
operator|=
operator|(
name|AegisDatabinding
operator|)
name|service
operator|.
name|getDataBinding
argument_list|()
expr_stmt|;
name|tm
operator|=
name|databinding
operator|.
name|getAegisContext
argument_list|()
operator|.
name|getTypeMapping
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTM
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|tm
operator|.
name|getTypeCreator
argument_list|()
operator|instanceof
name|XMLTypeCreator
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testType
parameter_list|()
block|{
name|AnnotatedTypeInfo
name|info
init|=
operator|new
name|AnnotatedTypeInfo
argument_list|(
name|tm
argument_list|,
name|JaxbBean1
operator|.
name|class
argument_list|,
literal|"urn:foo"
argument_list|,
operator|new
name|TypeCreationOptions
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|QName
argument_list|>
name|elements
init|=
name|info
operator|.
name|getElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|element
init|=
name|elements
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|AegisType
name|custom
init|=
name|info
operator|.
name|getType
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"bogusProperty"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|custom
operator|instanceof
name|StringType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"elementProperty"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|custom
operator|instanceof
name|CustomStringType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Unexpected element name: "
operator|+
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|element
operator|=
name|elements
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|elements
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|custom
operator|=
name|info
operator|.
name|getType
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"bogusProperty"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|custom
operator|instanceof
name|StringType
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"elementProperty"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
name|custom
operator|instanceof
name|CustomStringType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fail
argument_list|(
literal|"Unexpected element name: "
operator|+
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|QName
argument_list|>
name|atts
init|=
name|info
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|atts
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|atts
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|atts
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|info
operator|.
name|isExtensibleElements
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|info
operator|.
name|isExtensibleAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test if attributeProperty is correctly mapped to attProp by      * applying the xml mapping file<className>.aegis.xml      */
annotation|@
name|Test
specifier|public
name|void
name|testAegisType
parameter_list|()
block|{
name|BeanType
name|type
init|=
operator|(
name|BeanType
operator|)
name|tm
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|JaxbBean3
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|QName
argument_list|>
name|itr
init|=
name|type
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|getElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|q
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"attProp"
argument_list|,
name|q
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtensibilityOff
parameter_list|()
block|{
name|BeanType
name|type
init|=
operator|(
name|BeanType
operator|)
name|tm
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|JaxbBean4
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|type
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|isExtensibleElements
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|type
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|isExtensibleAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNillableAndMinOccurs
parameter_list|()
block|{
name|BeanType
name|type
init|=
operator|(
name|BeanType
operator|)
name|tm
operator|.
name|getTypeCreator
argument_list|()
operator|.
name|createType
argument_list|(
name|JaxbBean4
operator|.
name|class
argument_list|)
decl_stmt|;
name|AnnotatedTypeInfo
name|info
init|=
operator|(
name|AnnotatedTypeInfo
operator|)
name|type
operator|.
name|getTypeInfo
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|QName
argument_list|>
name|elements
init|=
name|info
operator|.
name|getElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elements
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// nillable first
name|QName
name|element
init|=
name|elements
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"minOccursProperty"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|info
operator|.
name|getMinOccurs
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
argument_list|(
name|info
operator|.
name|isNillable
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|elements
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// minOccurs = 1 second
name|element
operator|=
name|elements
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"minOccursProperty"
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|info
operator|.
name|getMinOccurs
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
argument_list|(
name|info
operator|.
name|isNillable
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"JaxbService"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"xsd"
argument_list|,
name|Constants
operator|.
name|URI_2001_SCHEMA_XSD
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='JaxbBean1']/xsd:sequence/xsd:"
operator|+
literal|"element[@name='elementProperty']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='JaxbBean1']/xsd:attribute"
operator|+
literal|"[@name='attributeProperty']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='JaxbBean1']/xsd:sequence/xsd:element"
operator|+
literal|"[@name='bogusProperty']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='JaxbBean2']/xsd:sequence/xsd:element"
operator|+
literal|"[@name='element'][@type='xsd:string']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='JaxbBean2']/xsd:attribute"
operator|+
literal|"[@name='attribute'][@type='xsd:string']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSetRequired
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanType
name|type
init|=
operator|new
name|BeanType
argument_list|(
operator|new
name|AnnotatedTypeInfo
argument_list|(
name|tm
argument_list|,
name|BadBean
operator|.
name|class
argument_list|,
literal|"urn:foo"
argument_list|,
operator|new
name|TypeCreationOptions
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|type
operator|.
name|setSchemaType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"urn:foo"
argument_list|,
literal|"BadBean"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|type
operator|.
name|getTypeInfo
argument_list|()
operator|.
name|getElements
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|BadBean
block|{
specifier|public
name|void
name|setString
parameter_list|(
name|String
name|string
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

