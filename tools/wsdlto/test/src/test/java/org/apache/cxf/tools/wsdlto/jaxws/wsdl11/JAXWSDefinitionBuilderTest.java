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
name|jaxws
operator|.
name|wsdl11
package|;
end_package

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
name|javax
operator|.
name|wsdl
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingInput
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingOperation
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
name|Port
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Service
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
name|http
operator|.
name|HTTPAddress
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
name|bindings
operator|.
name|xformat
operator|.
name|XMLBindingMessageFormat
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
name|wsdl11
operator|.
name|JAXWSDefinitionBuilder
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
name|transport
operator|.
name|jms
operator|.
name|AddressType
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

begin_class
specifier|public
class|class
name|JAXWSDefinitionBuilderTest
extends|extends
name|Assert
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
name|testBuildDefinitionWithXMLBinding
parameter_list|()
block|{
name|String
name|qname
init|=
literal|"http://apache.org/hello_world_xml_http/bare"
decl_stmt|;
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/hello_world_xml_bare.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
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
name|Definition
name|def
init|=
name|builder
operator|.
name|build
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|Map
name|services
init|=
name|def
operator|.
name|getServices
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|services
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|services
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|qname
argument_list|,
literal|"XMLService"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Map
name|ports
init|=
name|service
operator|.
name|getPorts
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ports
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ports
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Port
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
literal|"XMLPort"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|port
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|port
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|HTTPAddress
argument_list|)
expr_stmt|;
name|Binding
name|binding
init|=
name|port
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
name|qname
argument_list|,
literal|"Greeter_XMLBinding"
argument_list|)
argument_list|,
name|binding
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|BindingOperation
name|operation
init|=
name|binding
operator|.
name|getBindingOperation
argument_list|(
literal|"sayHi"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|BindingInput
name|input
init|=
name|operation
operator|.
name|getBindingInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|input
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|XMLBindingMessageFormat
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildDefinitionWithJMSTransport
parameter_list|()
block|{
name|String
name|qname
init|=
literal|"http://cxf.apache.org/hello_world_jms"
decl_stmt|;
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/jms_test.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
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
name|Definition
name|def
init|=
name|builder
operator|.
name|build
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|Map
name|services
init|=
name|def
operator|.
name|getServices
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|services
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
operator|(
name|Service
operator|)
name|services
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|qname
argument_list|,
literal|"HelloWorldQueueBinMsgService"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Map
name|ports
init|=
name|service
operator|.
name|getPorts
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ports
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ports
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Port
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
literal|"HelloWorldQueueBinMsgPort"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|port
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|port
operator|.
name|getExtensibilityElements
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|AddressType
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

