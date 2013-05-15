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
name|xmlbeans
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
name|Node
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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|XMLUtils
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
comment|/**  * Tests that we can handle multiple schemas within the same namespace.  */
end_comment

begin_class
specifier|public
class|class
name|MultipleSchemaInNSTest
extends|extends
name|AbstractXmlBeansTest
block|{
name|String
name|ns
init|=
literal|"urn:xfire:xmlbeans:nstest"
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
name|MultipleSchemaService
operator|.
name|class
argument_list|,
operator|new
name|MultipleSchemaService
argument_list|()
argument_list|,
literal|"MultipleSchemaService"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://xmlbeans.cxf.apache.org/"
argument_list|,
literal|"MultipleSchemaService"
argument_list|)
argument_list|)
expr_stmt|;
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
name|Node
name|wsdl
init|=
name|getWSDLDocument
argument_list|(
literal|"MultipleSchemaService"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"xsd"
argument_list|,
name|SOAPConstants
operator|.
name|XSD
argument_list|)
expr_stmt|;
name|NodeList
name|list
init|=
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='"
operator|+
name|ns
operator|+
literal|"']"
argument_list|,
name|wsdl
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|XMLUtils
operator|.
name|toString
argument_list|(
name|wsdl
argument_list|)
argument_list|,
literal|3
argument_list|,
name|list
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:import[@namespace='"
operator|+
name|ns
operator|+
literal|"']"
argument_list|,
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:import[@namespace='"
operator|+
name|ns
operator|+
literal|"']"
argument_list|,
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:import[@namespace='"
operator|+
name|ns
operator|+
literal|"']"
argument_list|,
name|list
operator|.
name|item
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:import[@namespace='"
operator|+
name|ns
operator|+
literal|"']"
argument_list|,
name|list
operator|.
name|item
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"//xsd:import[@namespace='"
operator|+
name|ns
operator|+
literal|"']/@schemaLocation"
argument_list|,
name|list
operator|.
name|item
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"//xsd:import[@namespace='"
operator|+
name|ns
operator|+
literal|"']/@schemaLocation"
argument_list|,
name|list
operator|.
name|item
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|/*         endpoint.setProperty(AbstractWSDL.REMOVE_ALL_IMPORTS, "True");          wsdl = getWSDLDocument("MultipleSchemaService");          assertValid("//xsd:schema[@targetNamespace='" + ns + "'][1]", wsdl);         assertInvalid("//xsd:schema[@targetNamespace='" + ns + "'][1]" + "/xsd:import[@namespace='" + ns                       + "']", wsdl);         assertValid("//xsd:schema[@targetNamespace='" + ns + "'][3]", wsdl);         assertInvalid("//xsd:schema[@targetNamespace='" + ns + "'][3]" + "/xsd:import[@namespace='" + ns                       + "']", wsdl);                       */
block|}
block|}
end_class

end_unit

