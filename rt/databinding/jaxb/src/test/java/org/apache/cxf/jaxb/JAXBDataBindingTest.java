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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|HashSet
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
name|Set
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
name|stream
operator|.
name|XMLEventReader
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
name|XMLEventWriter
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
name|XMLOutputFactory
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
name|XMLStreamReader
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
name|databinding
operator|.
name|DataReader
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
name|databinding
operator|.
name|DataWriter
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
name|jaxb
operator|.
name|fortest
operator|.
name|QualifiedBean
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
name|jaxb
operator|.
name|fortest
operator|.
name|unqualified
operator|.
name|UnqualifiedBean
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
name|jaxb
operator|.
name|io
operator|.
name|DataReaderImpl
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
name|jaxb
operator|.
name|io
operator|.
name|DataWriterImpl
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
name|WSDLServiceBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMe
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|GreetMeOneWay
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
name|JAXBDataBindingTest
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
name|JAXBDataBindingTest
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
literal|"/wsdl/jaxb/hello_world.wsdl"
decl_stmt|;
specifier|private
name|Definition
name|def
decl_stmt|;
specifier|private
name|Service
name|service
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
name|JAXBDataBinding
name|jaxbDataBinding
decl_stmt|;
specifier|private
name|DestinationFactoryManager
name|destinationFactoryManager
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
name|jaxbDataBinding
operator|=
operator|new
name|JAXBDataBinding
argument_list|()
expr_stmt|;
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|WSDL_PATH
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
name|andStubReturn
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
name|destinationFactoryManager
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|WSDLServiceBuilder
name|wsdlServiceBuilder
init|=
operator|new
name|WSDLServiceBuilder
argument_list|(
name|bus
argument_list|)
decl_stmt|;
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
name|wsdlServiceBuilder
operator|.
name|buildServices
argument_list|(
name|def
argument_list|,
name|service
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
block|{      }
annotation|@
name|Test
specifier|public
name|void
name|testCreateReader
parameter_list|()
block|{
name|DataReader
argument_list|<
name|?
argument_list|>
name|reader
init|=
name|jaxbDataBinding
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|reader
operator|instanceof
name|DataReaderImpl
argument_list|)
expr_stmt|;
name|reader
operator|=
name|jaxbDataBinding
operator|.
name|createReader
argument_list|(
name|XMLEventReader
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reader
operator|instanceof
name|DataReaderImpl
argument_list|)
expr_stmt|;
name|reader
operator|=
name|jaxbDataBinding
operator|.
name|createReader
argument_list|(
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reader
operator|instanceof
name|DataReaderImpl
argument_list|)
expr_stmt|;
name|reader
operator|=
name|jaxbDataBinding
operator|.
name|createReader
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSupportedFormats
parameter_list|()
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|cls
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|jaxbDataBinding
operator|.
name|getSupportedWriterFormats
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|cls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|XMLEventWriter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|Node
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|cls
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|jaxbDataBinding
operator|.
name|getSupportedReaderFormats
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|cls
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|XMLEventReader
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cls
operator|.
name|contains
argument_list|(
name|Node
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateWriter
parameter_list|()
block|{
name|DataWriter
argument_list|<
name|?
argument_list|>
name|writer
init|=
name|jaxbDataBinding
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|writer
operator|instanceof
name|DataWriterImpl
argument_list|)
expr_stmt|;
name|writer
operator|=
name|jaxbDataBinding
operator|.
name|createWriter
argument_list|(
name|XMLEventWriter
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|writer
operator|instanceof
name|DataWriterImpl
argument_list|)
expr_stmt|;
name|writer
operator|=
name|jaxbDataBinding
operator|.
name|createWriter
argument_list|(
name|Node
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|writer
operator|instanceof
name|DataWriterImpl
argument_list|)
expr_stmt|;
name|writer
operator|=
name|jaxbDataBinding
operator|.
name|createWriter
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraClass
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|extraClass
init|=
operator|new
name|Class
index|[]
block|{
name|GreetMe
operator|.
name|class
block|,
name|GreetMeOneWay
operator|.
name|class
block|}
decl_stmt|;
name|jaxbDataBinding
operator|.
name|setExtraClass
argument_list|(
name|extraClass
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jaxbDataBinding
operator|.
name|getExtraClass
argument_list|()
operator|.
name|length
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jaxbDataBinding
operator|.
name|getExtraClass
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|GreetMe
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|jaxbDataBinding
operator|.
name|getExtraClass
argument_list|()
index|[
literal|1
index|]
argument_list|,
name|GreetMeOneWay
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContextProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|nsMap
operator|.
name|put
argument_list|(
literal|"uri:ultima:thule"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|db
operator|.
name|setNamespaceMap
argument_list|(
name|nsMap
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextProperties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|contextProperties
operator|.
name|put
argument_list|(
literal|"com.sun.xml.bind.defaultNamespaceRemap"
argument_list|,
literal|"uri:ultima:thule"
argument_list|)
expr_stmt|;
name|db
operator|.
name|setContextProperties
argument_list|(
name|contextProperties
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|UnqualifiedBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|db
operator|.
name|setContext
argument_list|(
name|db
operator|.
name|createJAXBContext
argument_list|(
name|classes
argument_list|)
argument_list|)
expr_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|writer
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLOutputFactory
name|writerFactory
init|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|StringWriter
name|stringWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|writerFactory
operator|.
name|createXMLStreamWriter
argument_list|(
name|stringWriter
argument_list|)
decl_stmt|;
name|UnqualifiedBean
name|bean
init|=
operator|new
name|UnqualifiedBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAriadne
argument_list|(
literal|"spider"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|xmlWriter
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|xml
init|=
name|stringWriter
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
argument_list|,
name|xml
operator|.
name|contains
argument_list|(
literal|"uri:ultima:thule"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeclaredNamespaceMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBDataBinding
name|db
init|=
operator|new
name|JAXBDataBinding
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|nsMap
operator|.
name|put
argument_list|(
literal|"uri:ultima:thule"
argument_list|,
literal|"greenland"
argument_list|)
expr_stmt|;
name|db
operator|.
name|setNamespaceMap
argument_list|(
name|nsMap
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|contextProperties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|//contextProperties.put("com.sun.xml.bind.defaultNamespaceRemap", "uri:ultima:thule");
name|db
operator|.
name|setContextProperties
argument_list|(
name|contextProperties
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|QualifiedBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|db
operator|.
name|setContext
argument_list|(
name|db
operator|.
name|createJAXBContext
argument_list|(
name|classes
argument_list|)
argument_list|)
expr_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|writer
init|=
name|db
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLOutputFactory
name|writerFactory
init|=
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|StringWriter
name|stringWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xmlWriter
init|=
name|writerFactory
operator|.
name|createXMLStreamWriter
argument_list|(
name|stringWriter
argument_list|)
decl_stmt|;
name|QualifiedBean
name|bean
init|=
operator|new
name|QualifiedBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setAriadne
argument_list|(
literal|"spider"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|,
name|xmlWriter
argument_list|)
expr_stmt|;
name|xmlWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|xml
init|=
name|stringWriter
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|xml
argument_list|,
name|xml
operator|.
name|contains
argument_list|(
literal|"greenland=\"uri:ultima:thule"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResursiveType
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|typeReferences
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|JAXBContextInitializer
name|init
init|=
operator|new
name|JAXBContextInitializer
argument_list|(
literal|null
argument_list|,
name|classes
argument_list|,
name|typeReferences
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|init
operator|.
name|addClass
argument_list|(
name|Type2
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|classes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|abstract
specifier|static
class|class
name|Type2
extends|extends
name|AddressEntity
argument_list|<
name|Type2
argument_list|>
block|{     }
specifier|public
specifier|abstract
specifier|static
class|class
name|AddressEntity
parameter_list|<
name|T
extends|extends
name|AddressEntity
parameter_list|<
name|T
parameter_list|>
parameter_list|>
block|{
specifier|public
specifier|abstract
name|Addressable
argument_list|<
name|T
argument_list|>
name|getEntity
parameter_list|()
function_decl|;
block|}
specifier|public
interface|interface
name|Addressable
parameter_list|<
name|T
extends|extends
name|AddressEntity
parameter_list|<
name|T
parameter_list|>
parameter_list|>
block|{     }
block|}
end_class

end_unit

