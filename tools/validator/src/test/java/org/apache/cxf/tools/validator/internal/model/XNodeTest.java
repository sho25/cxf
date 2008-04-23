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
name|validator
operator|.
name|internal
operator|.
name|model
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsdl
operator|.
name|WSDLConstants
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
name|XNodeTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testWSDLDefinition
parameter_list|()
block|{
name|XDef
name|def
init|=
operator|new
name|XDef
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/wsdl:definitions"
argument_list|,
name|def
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
name|testGetXPath
parameter_list|()
block|{
name|XNode
name|node
init|=
operator|new
name|XNode
argument_list|()
decl_stmt|;
name|node
operator|.
name|setQName
argument_list|(
name|WSDLConstants
operator|.
name|QNAME_BINDING
argument_list|)
expr_stmt|;
name|node
operator|.
name|setPrefix
argument_list|(
literal|"wsdl"
argument_list|)
expr_stmt|;
name|node
operator|.
name|setAttributeName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|node
operator|.
name|setAttributeValue
argument_list|(
literal|"SOAPBinding"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/wsdl:binding[@name='SOAPBinding']"
argument_list|,
name|node
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[binding:SOAPBinding]"
argument_list|,
name|node
operator|.
name|getPlainText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParentNode
parameter_list|()
block|{
name|XDef
name|definition
init|=
operator|new
name|XDef
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
literal|"{http://apache.org/hello_world/messages}"
decl_stmt|;
name|definition
operator|.
name|setTargetNamespace
argument_list|(
literal|"http://apache.org/hello_world/messages"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|definition
operator|.
name|getPlainText
argument_list|()
argument_list|)
expr_stmt|;
name|XPortType
name|portType
init|=
operator|new
name|XPortType
argument_list|()
decl_stmt|;
name|portType
operator|.
name|setName
argument_list|(
literal|"Greeter"
argument_list|)
expr_stmt|;
name|portType
operator|.
name|setParentNode
argument_list|(
name|definition
argument_list|)
expr_stmt|;
name|String
name|portTypeText
init|=
name|ns
operator|+
literal|"[portType:Greeter]"
decl_stmt|;
name|assertEquals
argument_list|(
name|portTypeText
argument_list|,
name|portType
operator|.
name|getPlainText
argument_list|()
argument_list|)
expr_stmt|;
name|XOperation
name|op
init|=
operator|new
name|XOperation
argument_list|()
decl_stmt|;
name|op
operator|.
name|setName
argument_list|(
literal|"sayHi"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setParentNode
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|portTypeText
operator|+
literal|"[operation:sayHi]"
argument_list|,
name|op
operator|.
name|getPlainText
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"/wsdl:definitions[@targetNamespace='http://apache.org/hello_world/messages']"
decl_stmt|;
name|expected
operator|+=
literal|"/wsdl:portType[@name='Greeter']/wsdl:operation[@name='sayHi']"
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|op
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

