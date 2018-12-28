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
name|jaxws
operator|.
name|support
package|;
end_package

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
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
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
name|Service
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
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
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
name|Bus
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
name|binding
operator|.
name|BindingFactoryManager
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
name|model
operator|.
name|MessageInfo
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
name|model
operator|.
name|OperationInfo
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
name|model
operator|.
name|ServiceInfo
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
name|DestinationFactoryManager
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
name|wsdl11
operator|.
name|CatalogWSDLLocator
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
name|wsdl11
operator|.
name|WSDLServiceBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|assertFalse
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
name|assertNull
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
name|JaxWsServiceConfigurationTest
extends|extends
name|Assert
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{      }
annotation|@
name|Test
specifier|public
name|void
name|testGetInPartName
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|opName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"sayHello"
argument_list|)
decl_stmt|;
name|Method
name|sayHelloMethod
init|=
name|Hello
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"sayHello"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|String
operator|.
name|class
block|,
name|String
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|getMockedServiceModel
argument_list|(
literal|"/wsdl/default_partname_test.wsdl"
argument_list|)
decl_stmt|;
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServiceConfiguration
name|jwsc
init|=
operator|(
name|JaxWsServiceConfiguration
operator|)
name|bean
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|jwsc
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|OperationInfo
name|op
init|=
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
name|opName
argument_list|)
decl_stmt|;
name|op
operator|.
name|setInput
argument_list|(
literal|"input"
argument_list|,
operator|new
name|MessageInfo
argument_list|(
name|op
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"input"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|op
operator|.
name|setOutput
argument_list|(
literal|"output"
argument_list|,
operator|new
name|MessageInfo
argument_list|(
name|op
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"output"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|partName
init|=
name|jwsc
operator|.
name|getInPartName
argument_list|(
name|op
argument_list|,
name|sayHelloMethod
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"get wrong in partName for first param"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"arg0"
argument_list|)
argument_list|,
name|partName
argument_list|)
expr_stmt|;
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|addMessagePart
argument_list|(
operator|new
name|QName
argument_list|(
literal|"arg0"
argument_list|)
argument_list|)
expr_stmt|;
name|partName
operator|=
name|jwsc
operator|.
name|getInPartName
argument_list|(
name|op
argument_list|,
name|sayHelloMethod
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get wrong in partName for first param"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"arg1"
argument_list|)
argument_list|,
name|partName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultStyle
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|HelloDefault
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServiceConfiguration
name|jwsc
init|=
operator|(
name|JaxWsServiceConfiguration
operator|)
name|bean
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|jwsc
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jwsc
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"document"
argument_list|,
name|bean
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|jwsc
operator|.
name|isWrapped
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRPCStyle
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|HelloRPC
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServiceConfiguration
name|jwsc
init|=
operator|(
name|JaxWsServiceConfiguration
operator|)
name|bean
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|jwsc
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rpc"
argument_list|,
name|jwsc
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|jwsc
operator|.
name|isWrapped
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocumentWrappedStyle
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|HelloWrapped
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServiceConfiguration
name|jwsc
init|=
operator|(
name|JaxWsServiceConfiguration
operator|)
name|bean
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|jwsc
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"document"
argument_list|,
name|jwsc
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jwsc
operator|.
name|isWrapped
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocumentBareStyle
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|HelloBare
operator|.
name|class
argument_list|)
expr_stmt|;
name|JaxWsServiceConfiguration
name|jwsc
init|=
operator|(
name|JaxWsServiceConfiguration
operator|)
name|bean
operator|.
name|getServiceConfigurations
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|jwsc
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"document"
argument_list|,
name|jwsc
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|jwsc
operator|.
name|isWrapped
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetOutPartName
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|opName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"sayHi"
argument_list|)
decl_stmt|;
name|Method
name|sayHiMethod
init|=
name|Hello
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"sayHi"
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|getMockedServiceModel
argument_list|(
literal|"/wsdl/default_partname_test.wsdl"
argument_list|)
decl_stmt|;
name|JaxWsServiceConfiguration
name|jwsc
init|=
operator|new
name|JaxWsServiceConfiguration
argument_list|()
decl_stmt|;
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|jwsc
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
comment|// clear the output
name|OperationInfo
name|op
init|=
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
name|opName
argument_list|)
decl_stmt|;
name|op
operator|.
name|setOutput
argument_list|(
literal|"output"
argument_list|,
operator|new
name|MessageInfo
argument_list|(
name|op
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|OUTPUT
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"output"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|partName
init|=
name|jwsc
operator|.
name|getOutPartName
argument_list|(
name|op
argument_list|,
name|sayHiMethod
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"get wrong return partName"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://cxf.com/"
argument_list|,
literal|"return"
argument_list|)
argument_list|,
name|partName
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ServiceInfo
name|getMockedServiceModel
parameter_list|(
name|String
name|wsdlUrl
parameter_list|)
throws|throws
name|Exception
block|{
name|WSDLReader
name|wsdlReader
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Definition
name|def
init|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
operator|new
name|CatalogWSDLLocator
argument_list|(
name|wsdlUrl
argument_list|)
argument_list|)
decl_stmt|;
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|BindingFactoryManager
name|bindingFactoryManager
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|WSDLServiceBuilder
name|wsdlServiceBuilder
init|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|Service
name|service
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|def
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|obj
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Service
condition|)
block|{
name|service
operator|=
operator|(
name|Service
operator|)
name|obj
expr_stmt|;
break|break;
block|}
block|}
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bindingFactoryManager
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|dfm
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|wsdlServiceBuilder
operator|.
name|buildServices
argument_list|(
name|def
argument_list|,
name|service
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|serviceInfo
operator|.
name|setProperty
argument_list|(
name|WSDLServiceBuilder
operator|.
name|WSDL_DEFINITION
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|serviceInfo
operator|.
name|setProperty
argument_list|(
name|WSDLServiceBuilder
operator|.
name|WSDL_SERVICE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|serviceInfo
return|;
block|}
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"Hello"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.com/"
argument_list|)
annotation|@
name|SOAPBinding
argument_list|(
name|parameterStyle
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|ParameterStyle
operator|.
name|BARE
argument_list|,
name|style
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|Style
operator|.
name|RPC
argument_list|,
name|use
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|Use
operator|.
name|LITERAL
argument_list|)
specifier|public
interface|interface
name|Hello
block|{
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"sayHi"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|String
name|sayHi
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"sayHello"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|String
name|sayHello
parameter_list|(
name|String
name|asdf1
parameter_list|,
name|String
name|asdf2
parameter_list|)
function_decl|;
block|}
annotation|@
name|WebService
argument_list|(
name|name
operator|=
literal|"Hello"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.com/"
argument_list|)
specifier|public
interface|interface
name|HelloDefault
block|{
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"sayHi"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|String
name|sayHi
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"sayHello"
argument_list|,
name|exclude
operator|=
literal|false
argument_list|)
name|String
name|sayHello
parameter_list|(
name|String
name|asdf1
parameter_list|,
name|String
name|asdf2
parameter_list|)
function_decl|;
block|}
annotation|@
name|SOAPBinding
argument_list|(
name|style
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|Style
operator|.
name|RPC
argument_list|)
specifier|public
interface|interface
name|HelloRPC
block|{
name|String
name|sayHi
parameter_list|()
function_decl|;
block|}
annotation|@
name|SOAPBinding
argument_list|(
name|style
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|Style
operator|.
name|DOCUMENT
argument_list|)
specifier|public
interface|interface
name|HelloWrapped
block|{
name|String
name|sayHi
parameter_list|()
function_decl|;
block|}
annotation|@
name|SOAPBinding
argument_list|(
name|parameterStyle
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|ParameterStyle
operator|.
name|BARE
argument_list|,
name|style
operator|=
name|javax
operator|.
name|jws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|Style
operator|.
name|DOCUMENT
argument_list|)
specifier|public
interface|interface
name|HelloBare
block|{
name|String
name|sayHi
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

