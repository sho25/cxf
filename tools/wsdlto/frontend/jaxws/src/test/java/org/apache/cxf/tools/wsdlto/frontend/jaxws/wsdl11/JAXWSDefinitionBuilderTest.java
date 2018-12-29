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
name|wsdl11
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
name|util
operator|.
name|Iterator
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
name|Operation
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|PortType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensibilityElement
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
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
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
name|ToolConstants
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
name|ToolContext
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|customization
operator|.
name|JAXWSBinding
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
name|assertEquals
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
name|JAXWSDefinitionBuilderTest
block|{
specifier|private
name|ToolContext
name|env
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|env
operator|=
operator|new
name|ToolContext
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomization
parameter_list|()
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/hello_world.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/binding2.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JAXWSDefinitionBuilder
name|builder
init|=
operator|new
name|JAXWSDefinitionBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|builder
operator|.
name|customize
argument_list|()
expr_stmt|;
name|Definition
name|customizedDef
init|=
name|builder
operator|.
name|getWSDLModel
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|defExtensionList
init|=
name|customizedDef
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite
init|=
name|defExtensionList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ite
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExtensibilityElement
name|extElement
init|=
operator|(
name|ExtensibilityElement
operator|)
name|ite
operator|.
name|next
argument_list|()
decl_stmt|;
name|JAXWSBinding
name|binding
init|=
operator|(
name|JAXWSBinding
operator|)
name|extElement
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Customized package name does not been parsered"
argument_list|,
literal|"com.foo"
argument_list|,
name|binding
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized enableAsync does not parsered"
argument_list|,
literal|true
argument_list|,
name|binding
operator|.
name|isEnableAsyncMapping
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PortType
name|portType
init|=
name|customizedDef
operator|.
name|getPortType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|portTypeList
init|=
name|portType
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|JAXWSBinding
name|binding
init|=
operator|(
name|JAXWSBinding
operator|)
name|portTypeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Customized enable EnableWrapperStyle name does not been parsered"
argument_list|,
literal|true
argument_list|,
name|binding
operator|.
name|isEnableWrapperStyle
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|opList
init|=
name|portType
operator|.
name|getOperations
argument_list|()
decl_stmt|;
name|Operation
name|operation
init|=
operator|(
name|Operation
operator|)
name|opList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|extList
init|=
name|operation
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|binding
operator|=
operator|(
name|JAXWSBinding
operator|)
name|extList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized method name does not parsered"
argument_list|,
literal|"echoMeOneWay"
argument_list|,
name|binding
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized parameter element name does not parsered"
argument_list|,
literal|"number1"
argument_list|,
name|binding
operator|.
name|getJaxwsParas
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElementName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized parameter message name does not parsered"
argument_list|,
literal|"greetMeOneWayRequest"
argument_list|,
name|binding
operator|.
name|getJaxwsParas
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"customized parameter name does not parsered"
argument_list|,
literal|"num1"
argument_list|,
name|binding
operator|.
name|getJaxwsParas
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomizationWithDifferentNS
parameter_list|()
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/hello_world.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/binding3.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JAXWSDefinitionBuilder
name|builder
init|=
operator|new
name|JAXWSDefinitionBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|builder
operator|.
name|customize
argument_list|()
expr_stmt|;
name|Definition
name|customizedDef
init|=
name|builder
operator|.
name|getWSDLModel
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|defExtensionList
init|=
name|customizedDef
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|ite
init|=
name|defExtensionList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ite
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExtensibilityElement
name|extElement
init|=
operator|(
name|ExtensibilityElement
operator|)
name|ite
operator|.
name|next
argument_list|()
decl_stmt|;
name|JAXWSBinding
name|binding
init|=
operator|(
name|JAXWSBinding
operator|)
name|extElement
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Customized package name does not been parsered"
argument_list|,
literal|"com.foo"
argument_list|,
name|binding
operator|.
name|getPackage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized enableAsync does not parsered"
argument_list|,
literal|true
argument_list|,
name|binding
operator|.
name|isEnableAsyncMapping
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PortType
name|portType
init|=
name|customizedDef
operator|.
name|getPortType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|portTypeList
init|=
name|portType
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|JAXWSBinding
name|binding
init|=
operator|(
name|JAXWSBinding
operator|)
name|portTypeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Customized enable EnableWrapperStyle name does not been parsered"
argument_list|,
literal|true
argument_list|,
name|binding
operator|.
name|isEnableWrapperStyle
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|opList
init|=
name|portType
operator|.
name|getOperations
argument_list|()
decl_stmt|;
name|Operation
name|operation
init|=
operator|(
name|Operation
operator|)
name|opList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|extList
init|=
name|operation
operator|.
name|getExtensibilityElements
argument_list|()
decl_stmt|;
name|binding
operator|=
operator|(
name|JAXWSBinding
operator|)
name|extList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized method name does not parsered"
argument_list|,
literal|"echoMeOneWay"
argument_list|,
name|binding
operator|.
name|getMethodName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized parameter element name does not parsered"
argument_list|,
literal|"number1"
argument_list|,
name|binding
operator|.
name|getJaxwsParas
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getElementName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Customized parameter message name does not parsered"
argument_list|,
literal|"greetMeOneWayRequest"
argument_list|,
name|binding
operator|.
name|getJaxwsParas
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessageName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"customized parameter name does not parsered"
argument_list|,
literal|"num1"
argument_list|,
name|binding
operator|.
name|getJaxwsParas
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// tests the error case described in JIRA CXF-556
annotation|@
name|Test
specifier|public
name|void
name|testCustomizationWhereURINotAnExactStringMatch
parameter_list|()
throws|throws
name|Exception
block|{
comment|// set up a URI with ./../wsdl11/hello_world.wsdl instead of
comment|// ./hello_world.wsdl
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
operator|new
name|File
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"JAXWSDefinitionBuilderTest.class"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParent
argument_list|()
operator|+
literal|"/wsdl11/resources/hello_world.wsdl"
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_BINDING
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/cxf556_binding.xml"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JAXWSDefinitionBuilder
name|builder
init|=
operator|new
name|JAXWSDefinitionBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// this call will fail before CXF-556
name|builder
operator|.
name|customize
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoService
parameter_list|()
block|{
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/build.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JAXWSDefinitionBuilder
name|builder
init|=
operator|new
name|JAXWSDefinitionBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setContext
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|getWSDLModel
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|def
operator|.
name|getServices
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

