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
name|wsdl11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Fault
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Input
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Message
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
name|Output
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Part
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
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Types
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
name|wsdl
operator|.
name|extensions
operator|.
name|schema
operator|.
name|Schema
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
name|schema
operator|.
name|SchemaImport
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|CastUtils
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
name|SchemaInfo
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
name|staxutils
operator|.
name|StaxUtils
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
name|DestinationFactory
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
name|wsdl
operator|.
name|WSDLManager
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
name|XmlSchema
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
name|XmlSchemaCollection
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
name|After
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
name|ServiceWSDLBuilderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ServiceWSDLBuilderTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_PATH
init|=
literal|"hello_world.wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_BODY_PARTS_WSDL_PATH
init|=
literal|"no_body_parts.wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_XSD_IMPORT_PATH
init|=
literal|"hello_world_schema_import_test.wsdl"
decl_stmt|;
specifier|private
name|Definition
name|def
decl_stmt|;
specifier|private
name|Definition
name|newDef
decl_stmt|;
specifier|private
name|Service
name|service
decl_stmt|;
specifier|private
name|WSDLServiceBuilder
name|wsdlServiceBuilder
decl_stmt|;
specifier|private
name|ServiceInfo
name|serviceInfo
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|BindingFactoryManager
name|bindingFactoryManager
decl_stmt|;
specifier|private
name|DestinationFactoryManager
name|destinationFactoryManager
decl_stmt|;
specifier|private
name|DestinationFactory
name|destinationFactory
decl_stmt|;
specifier|private
name|void
name|setupWSDL
parameter_list|(
name|String
name|wsdlPath
parameter_list|)
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|wsdlPath
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setupWSDL
parameter_list|(
name|String
name|wsdlPath
parameter_list|,
name|boolean
name|doXsdImports
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|wsdlPath
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"the path of wsdl file is "
operator|+
name|wsdlUrl
argument_list|)
expr_stmt|;
name|WSDLFactory
name|wsdlFactory
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|WSDLReader
name|wsdlReader
init|=
name|wsdlFactory
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
name|def
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|wsdlUrl
argument_list|)
expr_stmt|;
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|bindingFactoryManager
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|destinationFactoryManager
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|destinationFactory
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|wsdlServiceBuilder
operator|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|Service
name|serv
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|def
operator|.
name|getServices
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
name|Service
operator|.
name|class
argument_list|)
control|)
block|{
if|if
condition|(
name|serv
operator|!=
literal|null
condition|)
block|{
name|service
operator|=
name|serv
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
name|WSDLManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|WSDLManagerImpl
argument_list|()
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
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
name|andReturn
argument_list|(
name|destinationFactoryManager
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|destinationFactoryManager
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|destinationFactory
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|serviceInfo
operator|=
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
expr_stmt|;
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|serviceInfo
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setUseSchemaImports
argument_list|(
name|doXsdImports
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBaseFileName
argument_list|(
literal|"HelloWorld"
argument_list|)
expr_stmt|;
name|newDef
operator|=
name|builder
operator|.
name|build
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SchemaInfo
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|newDef
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoBodyParts
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|NO_BODY_PARTS_WSDL_PATH
argument_list|)
expr_stmt|;
name|QName
name|messageName
init|=
operator|new
name|QName
argument_list|(
literal|"urn:org:apache:cxf:no_body_parts/wsdl"
argument_list|,
literal|"operation1Request"
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|newDef
operator|.
name|getMessage
argument_list|(
name|messageName
argument_list|)
decl_stmt|;
name|Part
name|part
init|=
name|message
operator|.
name|getPart
argument_list|(
literal|"mimeAttachment"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|part
operator|.
name|getTypeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http"
argument_list|)
expr_stmt|;
name|Service
name|serv
init|=
name|newDef
operator|.
name|getService
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|serv
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|serv
operator|.
name|getPort
argument_list|(
literal|"SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPortType
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|newDef
operator|.
name|getPortTypes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|PortType
name|portType
init|=
operator|(
name|PortType
operator|)
name|newDef
operator|.
name|getPortTypes
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|portType
operator|.
name|getQName
argument_list|()
operator|.
name|equals
argument_list|(
operator|new
name|QName
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSayHiOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|PortType
name|portType
init|=
name|newDef
operator|.
name|getPortType
argument_list|(
operator|new
name|QName
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Operation
argument_list|>
name|operations
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|portType
operator|.
name|getOperations
argument_list|()
argument_list|,
name|Operation
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|operations
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Operation
name|sayHi
init|=
name|portType
operator|.
name|getOperation
argument_list|(
literal|"sayHi"
argument_list|,
literal|"sayHiRequest"
argument_list|,
literal|"sayHiResponse"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sayHi
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sayHi
operator|.
name|getName
argument_list|()
argument_list|,
literal|"sayHi"
argument_list|)
expr_stmt|;
name|Input
name|input
init|=
name|sayHi
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sayHiRequest"
argument_list|,
name|input
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|input
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sayHiRequest"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"in"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Output
name|output
init|=
name|sayHi
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sayHiResponse"
argument_list|,
name|output
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|output
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sayHiResponse"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"out"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"out"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|sayHi
operator|.
name|getFaults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreetMeOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|PortType
name|portType
init|=
name|newDef
operator|.
name|getPortType
argument_list|(
operator|new
name|QName
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
decl_stmt|;
name|Operation
name|greetMe
init|=
name|portType
operator|.
name|getOperation
argument_list|(
literal|"greetMe"
argument_list|,
literal|"greetMeRequest"
argument_list|,
literal|"greetMeResponse"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|greetMe
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMe"
argument_list|,
name|greetMe
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Input
name|input
init|=
name|greetMe
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeRequest"
argument_list|,
name|input
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|input
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeRequest"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"in"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Output
name|output
init|=
name|greetMe
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeResponse"
argument_list|,
name|output
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|output
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeResponse"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"out"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"out"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|greetMe
operator|.
name|getFaults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGreetMeOneWayOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|PortType
name|portType
init|=
name|newDef
operator|.
name|getPortType
argument_list|(
operator|new
name|QName
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
decl_stmt|;
name|Operation
name|greetMeOneWay
init|=
name|portType
operator|.
name|getOperation
argument_list|(
literal|"greetMeOneWay"
argument_list|,
literal|"greetMeOneWayRequest"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|greetMeOneWay
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeOneWay"
argument_list|,
name|greetMeOneWay
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Input
name|input
init|=
name|greetMeOneWay
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeOneWayRequest"
argument_list|,
name|input
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|input
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeOneWayRequest"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"in"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Output
name|output
init|=
name|greetMeOneWay
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|greetMeOneWay
operator|.
name|getFaults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPingMeOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|PortType
name|portType
init|=
name|newDef
operator|.
name|getPortType
argument_list|(
operator|new
name|QName
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter"
argument_list|)
argument_list|)
decl_stmt|;
name|Operation
name|pingMe
init|=
name|portType
operator|.
name|getOperation
argument_list|(
literal|"pingMe"
argument_list|,
literal|"pingMeRequest"
argument_list|,
literal|"pingMeResponse"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|pingMe
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMe"
argument_list|,
name|pingMe
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Input
name|input
init|=
name|pingMe
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMeRequest"
argument_list|,
name|input
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|input
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMeRequest"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"in"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"in"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Output
name|output
init|=
name|pingMe
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMeResponse"
argument_list|,
name|output
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|output
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMeResponse"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"out"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"out"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|pingMe
operator|.
name|getFaults
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Fault
name|fault
init|=
name|pingMe
operator|.
name|getFault
argument_list|(
literal|"pingMeFault"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|fault
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMeFault"
argument_list|,
name|fault
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|fault
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pingMeFault"
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|message
operator|.
name|getParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"faultDetail"
argument_list|,
name|message
operator|.
name|getPart
argument_list|(
literal|"faultDetail"
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getPart
argument_list|(
literal|"faultDetail"
argument_list|)
operator|.
name|getTypeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getBindings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Binding
name|binding
init|=
name|newDef
operator|.
name|getBinding
argument_list|(
operator|new
name|QName
argument_list|(
name|newDef
operator|.
name|getTargetNamespace
argument_list|()
argument_list|,
literal|"Greeter_SOAPBinding"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|binding
operator|.
name|getBindingOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBindingWithDifferentNamespaceImport
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
literal|"wsdl2/person.wsdl"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newDef
operator|.
name|getBindings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"http://cxf.apache.org/samples/wsdl-first"
operator|.
name|equals
argument_list|(
name|newDef
operator|.
name|getNamespace
argument_list|(
literal|"ns3"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSchemas
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_PATH
argument_list|)
expr_stmt|;
name|Types
name|types
init|=
name|newDef
operator|.
name|getTypes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|types
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ExtensibilityElement
argument_list|>
name|schemas
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|types
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|,
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schemas
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|XmlSchemaCollection
name|schemaCollection
init|=
operator|new
name|XmlSchemaCollection
argument_list|()
decl_stmt|;
name|Element
name|schemaElem
init|=
operator|(
operator|(
name|Schema
operator|)
name|schemas
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|XmlSchema
name|newSchema
init|=
name|schemaCollection
operator|.
name|read
argument_list|(
name|schemaElem
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|,
name|newSchema
operator|.
name|getTargetNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXsdImportMultipleSchemas
parameter_list|()
throws|throws
name|Exception
block|{
name|setupWSDL
argument_list|(
name|WSDL_XSD_IMPORT_PATH
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Types
name|types
init|=
name|newDef
operator|.
name|getTypes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|types
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ExtensibilityElement
argument_list|>
name|schemas
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|types
operator|.
name|getExtensibilityElements
argument_list|()
argument_list|,
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schemas
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Schema
name|schema
init|=
operator|(
name|Schema
operator|)
name|schemas
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|schema
operator|.
name|getImports
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SchemaImport
name|serviceTypesSchemaImport
init|=
name|getImport
argument_list|(
name|schema
operator|.
name|getImports
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http/servicetypes"
argument_list|)
decl_stmt|;
name|Schema
name|serviceTypesSchema
init|=
name|serviceTypesSchemaImport
operator|.
name|getReferencedSchema
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serviceTypesSchema
operator|.
name|getImports
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SchemaImport
name|typesSchemaImport
init|=
name|getImport
argument_list|(
name|serviceTypesSchema
operator|.
name|getImports
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http/types"
argument_list|)
decl_stmt|;
name|Schema
name|typesSchema
init|=
name|typesSchemaImport
operator|.
name|getReferencedSchema
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|typesSchema
operator|.
name|getElement
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|outputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|outputStream
argument_list|,
literal|"utf-8"
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|doc
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// this is a test to make sure any embedded namespaces are properly included
name|String
name|savedSchema
init|=
operator|new
name|String
argument_list|(
name|outputStream
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|savedSchema
operator|.
name|contains
argument_list|(
literal|"http://www.w3.org/2005/05/xmlmime"
argument_list|)
argument_list|)
expr_stmt|;
name|SchemaImport
name|types2SchemaImport
init|=
name|getImport
argument_list|(
name|typesSchema
operator|.
name|getImports
argument_list|()
argument_list|,
literal|"http://apache.org/hello_world_soap_http/types2"
argument_list|)
decl_stmt|;
name|Schema
name|types2Schema
init|=
name|types2SchemaImport
operator|.
name|getReferencedSchema
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|types2Schema
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SchemaImport
name|getImport
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|imps
parameter_list|,
name|String
name|key
parameter_list|)
block|{
name|List
argument_list|<
name|SchemaImport
argument_list|>
name|s1
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|imps
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|s1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

