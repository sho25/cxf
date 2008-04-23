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
name|common
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
operator|.
name|Mode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|XmlSeeAlso
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|FaultAction
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
name|JAnnotationTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testList
parameter_list|()
throws|throws
name|Exception
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|XmlSeeAlso
operator|.
name|class
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Class
index|[]
block|{
name|XmlSeeAlso
operator|.
name|class
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@XmlSeeAlso({XmlSeeAlso.class})"
argument_list|,
name|annotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"javax.xml.bind.annotation.XmlSeeAlso"
argument_list|,
name|annotation
operator|.
name|getImports
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSimpleForm
parameter_list|()
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"@WebService"
argument_list|,
name|annotation
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
name|testStringForm
parameter_list|()
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|WebService
operator|.
name|class
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"name"
argument_list|,
literal|"AddNumbersPortType"
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"targetNamespace"
argument_list|,
literal|"http://example.com/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@WebService(name = \"AddNumbersPortType\", targetNamespace = \"http://example.com/\")"
argument_list|,
name|annotation
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
name|testEnum
parameter_list|()
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|SOAPBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"parameterStyle"
argument_list|,
name|SOAPBinding
operator|.
name|ParameterStyle
operator|.
name|BARE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)"
argument_list|,
name|annotation
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
name|testCombination
parameter_list|()
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|Action
operator|.
name|class
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"input"
argument_list|,
literal|"3in"
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"output"
argument_list|,
literal|"3out"
argument_list|)
argument_list|)
expr_stmt|;
name|JAnnotation
name|faultAction
init|=
operator|new
name|JAnnotation
argument_list|(
name|FaultAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|faultAction
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"className"
argument_list|,
name|A
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|faultAction
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"value"
argument_list|,
literal|"3fault"
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"fault"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|JAnnotation
index|[]
block|{
name|faultAction
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"@Action(input = \"3in\", output = \"3out\", "
operator|+
literal|"fault = {@FaultAction(className = A.class, value = \"3fault\")})"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|annotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|annotation
operator|.
name|getImports
argument_list|()
operator|.
name|contains
argument_list|(
literal|"javax.xml.ws.FaultAction"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|annotation
operator|.
name|getImports
argument_list|()
operator|.
name|contains
argument_list|(
literal|"javax.xml.ws.Action"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|annotation
operator|.
name|getImports
argument_list|()
operator|.
name|contains
argument_list|(
literal|"org.apache.cxf.tools.common.model.A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrimitive
parameter_list|()
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|WebParam
operator|.
name|class
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"header"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"mode"
argument_list|,
name|Mode
operator|.
name|INOUT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@WebParam(header = true, mode = WebParam.Mode.INOUT)"
argument_list|,
name|annotation
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
name|testAddSame
parameter_list|()
block|{
name|JAnnotation
name|annotation
init|=
operator|new
name|JAnnotation
argument_list|(
name|WebParam
operator|.
name|class
argument_list|)
decl_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"header"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"header"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"mode"
argument_list|,
name|Mode
operator|.
name|INOUT
argument_list|)
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|addElement
argument_list|(
operator|new
name|JAnnotationElement
argument_list|(
literal|"mode"
argument_list|,
name|Mode
operator|.
name|OUT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"@WebParam(header = false, mode = WebParam.Mode.OUT)"
argument_list|,
name|annotation
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

