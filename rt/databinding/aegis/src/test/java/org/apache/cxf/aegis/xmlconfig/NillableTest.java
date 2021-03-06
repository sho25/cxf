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
name|xmlconfig
package|;
end_package

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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|NodeList
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
name|services
operator|.
name|NillableService
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
name|NillableTest
extends|extends
name|AbstractAegisTest
block|{
specifier|private
name|Document
name|arrayWsdlDoc
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
name|createService
argument_list|(
name|NillableService
operator|.
name|class
argument_list|,
operator|new
name|NillableService
argument_list|()
argument_list|,
literal|"Nillable"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"urn:nillable"
argument_list|,
literal|"Nillable"
argument_list|)
argument_list|)
expr_stmt|;
name|arrayWsdlDoc
operator|=
name|getWSDLDocument
argument_list|(
literal|"Nillable"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXmlConfigurationOfParameterTypeSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|NodeList
name|typeList
init|=
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='urn:nillable']"
operator|+
literal|"/xsd:complexType[@name=\"submitStringArray\"]"
operator|+
literal|"/xsd:sequence/xsd:element"
operator|+
literal|"[@name='array']"
argument_list|,
name|arrayWsdlDoc
argument_list|)
decl_stmt|;
name|Element
name|typeElement
init|=
operator|(
name|Element
operator|)
name|typeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|nillableValue
init|=
name|typeElement
operator|.
name|getAttribute
argument_list|(
literal|"nillable"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|nillableValue
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|nillableValue
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|nillableValue
argument_list|)
argument_list|)
expr_stmt|;
name|typeList
operator|=
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types"
operator|+
literal|"/xsd:schema[@targetNamespace='urn:nillable']"
operator|+
literal|"/xsd:complexType[@name=\"takeNotNillableString\"]"
operator|+
literal|"/xsd:sequence/xsd:element[@name='string']"
argument_list|,
name|arrayWsdlDoc
argument_list|)
expr_stmt|;
name|typeElement
operator|=
operator|(
name|Element
operator|)
name|typeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|nillableValue
operator|=
name|typeElement
operator|.
name|getAttribute
argument_list|(
literal|"nillable"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|nillableValue
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|nillableValue
argument_list|)
operator|||
literal|"false"
operator|.
name|equals
argument_list|(
name|nillableValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

