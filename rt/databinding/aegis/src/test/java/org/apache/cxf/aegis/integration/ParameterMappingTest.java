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
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
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
name|Node
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
name|AddNumbers
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
name|ArrayService
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
comment|/**  * Test inspired by CXF-962 ... do parameters actually work?  */
end_comment

begin_class
specifier|public
class|class
name|ParameterMappingTest
extends|extends
name|AbstractAegisTest
block|{
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
name|setEnableJDOM
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// there are JDOM types in here.
name|createService
argument_list|(
name|AddNumbers
operator|.
name|class
argument_list|,
literal|"AddNumbers"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|createService
argument_list|(
name|ArrayService
operator|.
name|class
argument_list|,
literal|"ArrayService"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParametersWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"AddNumbers"
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|getWSDLDefinition
argument_list|(
literal|"AddNumbers"
argument_list|)
decl_stmt|;
name|StringWriter
name|sink
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|sink
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/"
operator|+
literal|"xsd:schema[@targetNamespace='http://services.aegis.cxf.apache.org']"
operator|+
literal|"/xsd:complexType[@name='add']"
operator|+
literal|"/xsd:sequence"
operator|+
literal|"/xsd:element[@name='value1']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/"
operator|+
literal|"xsd:schema[@targetNamespace='http://services.aegis.cxf.apache.org']"
operator|+
literal|"/xsd:complexType[@name='unmappedAdd']"
operator|+
literal|"/xsd:sequence"
operator|+
literal|"/xsd:element[@name='one']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamedParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"ArrayService"
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|getWSDLDefinition
argument_list|(
literal|"ArrayService"
argument_list|)
decl_stmt|;
name|StringWriter
name|sink
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|sink
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/"
operator|+
literal|"xsd:schema[@targetNamespace= 'http://services.aegis.cxf.apache.org']"
operator|+
literal|"/xsd:complexType[@name='verifyCustomParamName']"
operator|+
literal|"/xsd:sequence"
operator|+
literal|"/xsd:element[@name='custom']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOccursWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|Node
name|doc
init|=
name|getWSDLDocument
argument_list|(
literal|"ArrayService"
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|getWSDLDefinition
argument_list|(
literal|"ArrayService"
argument_list|)
decl_stmt|;
name|StringWriter
name|sink
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|sink
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"/wsdl:definitions/wsdl:types/"
operator|+
literal|"xsd:schema[@targetNamespace= 'http://services.aegis.cxf.apache.org']"
operator|+
literal|"/xsd:complexType[@name='ArrayOfString-2-50']"
operator|+
literal|"/xsd:sequence"
operator|+
literal|"/xsd:element[@name='string']/@minOccurs"
argument_list|,
literal|"2"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

