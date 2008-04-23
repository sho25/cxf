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
name|tools
operator|.
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|helpers
operator|.
name|XMLUtils
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
name|tools
operator|.
name|common
operator|.
name|ProcessorTestBase
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
name|CustomizationParserTest
extends|extends
name|ProcessorTestBase
block|{
name|CustomizationParser
name|parser
init|=
operator|new
name|CustomizationParser
argument_list|()
decl_stmt|;
name|CustomNodeSelector
name|selector
init|=
operator|new
name|CustomNodeSelector
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testHasJaxbBindingDeclaration
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|doc
init|=
name|getDocumentElement
argument_list|(
literal|"resources/embeded_jaxb.xjb"
argument_list|)
decl_stmt|;
name|Node
name|jaxwsBindingNode
init|=
name|selector
operator|.
name|queryNode
argument_list|(
name|doc
argument_list|,
literal|"//jaxws:bindings[@node]"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|jaxwsBindingNode
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|parser
operator|.
name|hasJaxbBindingDeclaration
argument_list|(
name|jaxwsBindingNode
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|=
name|getDocumentElement
argument_list|(
literal|"resources/external_jaxws.xml"
argument_list|)
expr_stmt|;
name|jaxwsBindingNode
operator|=
name|selector
operator|.
name|queryNode
argument_list|(
name|doc
argument_list|,
literal|"//jaxws:bindings[@node]"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|jaxwsBindingNode
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|parser
operator|.
name|hasJaxbBindingDeclaration
argument_list|(
name|jaxwsBindingNode
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCopyAllJaxbDeclarations
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|schema
init|=
name|getDocumentElement
argument_list|(
literal|"resources/test.xsd"
argument_list|)
decl_stmt|;
name|Element
name|binding
init|=
name|getDocumentElement
argument_list|(
literal|"resources/embeded_jaxb.xjb"
argument_list|)
decl_stmt|;
name|String
name|checkingPoint
init|=
literal|"//xsd:annotation/xsd:appinfo/jaxb:schemaBindings/jaxb:package[@name]"
decl_stmt|;
name|assertNull
argument_list|(
name|selector
operator|.
name|queryNode
argument_list|(
name|schema
argument_list|,
name|checkingPoint
argument_list|)
argument_list|)
expr_stmt|;
name|Node
name|jaxwsBindingNode
init|=
name|selector
operator|.
name|queryNode
argument_list|(
name|binding
argument_list|,
literal|"//jaxws:bindings[@node]"
argument_list|)
decl_stmt|;
name|Node
name|schemaNode
init|=
name|selector
operator|.
name|queryNode
argument_list|(
name|schema
argument_list|,
literal|"//xsd:schema"
argument_list|)
decl_stmt|;
name|parser
operator|.
name|copyAllJaxbDeclarations
argument_list|(
name|schemaNode
argument_list|,
operator|(
name|Element
operator|)
name|jaxwsBindingNode
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"custom_test.xsd"
argument_list|)
decl_stmt|;
name|XMLUtils
operator|.
name|writeTo
argument_list|(
name|schemaNode
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|testNode
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Node
name|result
init|=
name|selector
operator|.
name|queryNode
argument_list|(
name|testNode
argument_list|,
name|checkingPoint
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInternalizeBinding1
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|wsdlDoc
init|=
name|getDocumentElement
argument_list|(
literal|"resources/test.wsdl"
argument_list|)
decl_stmt|;
name|Element
name|jaxwsBinding
init|=
name|getDocumentElement
argument_list|(
literal|"resources/external_jaxws.xml"
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setWSDLNode
argument_list|(
name|wsdlDoc
argument_list|)
expr_stmt|;
name|parser
operator|.
name|internalizeBinding
argument_list|(
name|jaxwsBinding
argument_list|,
name|wsdlDoc
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"custom_test.wsdl"
argument_list|)
decl_stmt|;
name|XMLUtils
operator|.
name|writeTo
argument_list|(
name|wsdlDoc
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|testNode
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|String
index|[]
name|checkingPoints
init|=
operator|new
name|String
index|[]
block|{
literal|"wsdl:definitions/wsdl:portType/jaxws:bindings/jaxws:class"
block|,
literal|"wsdl:definitions/jaxws:bindings/jaxws:package"
block|}
decl_stmt|;
name|checking
argument_list|(
name|testNode
argument_list|,
name|checkingPoints
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInternalizeBinding2
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|wsdlDoc
init|=
name|getDocumentElement
argument_list|(
literal|"resources/test.wsdl"
argument_list|)
decl_stmt|;
name|Element
name|jaxwsBinding
init|=
name|getDocumentElement
argument_list|(
literal|"resources/external_jaxws_embed_jaxb.xml"
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setWSDLNode
argument_list|(
name|wsdlDoc
argument_list|)
expr_stmt|;
name|parser
operator|.
name|internalizeBinding
argument_list|(
name|jaxwsBinding
argument_list|,
name|wsdlDoc
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|String
name|base
init|=
literal|"wsdl:definitions/wsdl:types/xsd:schema/xsd:annotation/xsd:appinfo/"
decl_stmt|;
name|String
index|[]
name|checkingPoints
init|=
operator|new
name|String
index|[]
block|{
name|base
operator|+
literal|"jaxb:schemaBindings/jaxb:package"
block|}
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"custom_test.wsdl"
argument_list|)
decl_stmt|;
name|XMLUtils
operator|.
name|writeTo
argument_list|(
name|wsdlDoc
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|testNode
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|checking
argument_list|(
name|testNode
argument_list|,
name|checkingPoints
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInternalizeBinding3
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|wsdlDoc
init|=
name|getDocumentElement
argument_list|(
literal|"resources/test.wsdl"
argument_list|)
decl_stmt|;
name|Element
name|jaxwsBinding
init|=
name|getDocumentElement
argument_list|(
literal|"resources/external_jaxws_embed_jaxb_date.xml"
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setWSDLNode
argument_list|(
name|wsdlDoc
argument_list|)
expr_stmt|;
name|parser
operator|.
name|internalizeBinding
argument_list|(
name|jaxwsBinding
argument_list|,
name|wsdlDoc
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|String
name|base
init|=
literal|"wsdl:definitions/wsdl:types/xsd:schema/xsd:annotation/xsd:appinfo/"
decl_stmt|;
name|String
index|[]
name|checkingPoints
init|=
operator|new
name|String
index|[]
block|{
name|base
operator|+
literal|"jaxb:globalBindings/jaxb:javaType"
block|}
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"custom_test.wsdl"
argument_list|)
decl_stmt|;
name|XMLUtils
operator|.
name|writeTo
argument_list|(
name|wsdlDoc
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|testNode
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|checking
argument_list|(
name|testNode
argument_list|,
name|checkingPoints
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInternalizeBinding4
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|wsdlDoc
init|=
name|getDocumentElement
argument_list|(
literal|"resources/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|Element
name|jaxwsBinding
init|=
name|getDocumentElement
argument_list|(
literal|"resources/binding2.xml"
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setWSDLNode
argument_list|(
name|wsdlDoc
argument_list|)
expr_stmt|;
name|parser
operator|.
name|internalizeBinding
argument_list|(
name|jaxwsBinding
argument_list|,
name|wsdlDoc
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|String
name|checkingPoint
init|=
literal|"wsdl:definitions/wsdl:types/xsd:schema"
decl_stmt|;
name|checkingPoint
operator|+=
literal|"/xsd:element[@name='CreateProcess']/xsd:complexType/xsd:sequence"
expr_stmt|;
name|checkingPoint
operator|+=
literal|"/xsd:element[@name='MyProcess']/xsd:simpleType/xsd:annotation/xsd:appinfo"
expr_stmt|;
name|checkingPoint
operator|+=
literal|"/jaxb:typesafeEnumClass/jaxb:typesafeEnumMember[@name='BLUE']"
expr_stmt|;
name|String
index|[]
name|checkingPoints
init|=
operator|new
name|String
index|[]
block|{
name|checkingPoint
block|}
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"custom_test4.wsdl"
argument_list|)
decl_stmt|;
name|XMLUtils
operator|.
name|writeTo
argument_list|(
name|wsdlDoc
argument_list|,
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|Document
name|testNode
init|=
name|XMLUtils
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|checking
argument_list|(
name|testNode
argument_list|,
name|checkingPoints
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Element
name|getDocumentElement
parameter_list|(
specifier|final
name|String
name|resource
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|XMLUtils
operator|.
name|parse
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
specifier|private
name|void
name|checking
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
index|[]
name|checkingPoints
parameter_list|)
block|{
for|for
control|(
name|String
name|checkingPoint
range|:
name|checkingPoints
control|)
block|{
name|assertNotNull
argument_list|(
name|selector
operator|.
name|queryNode
argument_list|(
name|node
argument_list|,
name|checkingPoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

