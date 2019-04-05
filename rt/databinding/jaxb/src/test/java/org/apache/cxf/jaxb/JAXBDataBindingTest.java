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
name|ByteArrayOutputStream
import|;
end_import

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
name|StringReader
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
name|bind
operator|.
name|JAXBContext
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
name|XmlAttribute
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
name|XmlRootElement
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
name|adapters
operator|.
name|XmlAdapter
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
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|XMLInputFactory
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
name|common
operator|.
name|util
operator|.
name|ASMHelper
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
name|ReflectionUtil
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
name|assertNotNull
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|JAXBDataBindingTest
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
name|JAXBDataBinding
name|createJaxbContext
parameter_list|(
name|boolean
name|internal
parameter_list|)
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
argument_list|<>
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
argument_list|<>
argument_list|()
decl_stmt|;
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
argument_list|<>
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
comment|//have to fastboot to avoid conflicts of generated accessors
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.xml.bind.v2.runtime.JAXBContextImpl.fastBoot"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.fastBoot"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
if|if
condition|(
name|internal
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JAXBContext
operator|.
name|JAXB_CONTEXT_FACTORY
argument_list|,
literal|"com.sun.xml.internal.bind.v2.ContextFactory"
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
name|System
operator|.
name|clearProperty
argument_list|(
name|JAXBContext
operator|.
name|JAXB_CONTEXT_FACTORY
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
name|System
operator|.
name|clearProperty
argument_list|(
literal|"com.sun.xml.bind.v2.runtime.JAXBContextImpl.fastBoot"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.fastBoot"
argument_list|)
expr_stmt|;
return|return
name|db
return|;
block|}
name|void
name|doNamespaceMappingTest
parameter_list|(
name|boolean
name|internal
parameter_list|,
name|boolean
name|asm
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|internal
condition|)
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
literal|"com.sun.xml.internal.bind.v2.ContextFactory"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//on a JVM (likely IBM's) that doesn't rename the ContextFactory package to include "internal"
return|return;
block|}
block|}
try|try
block|{
if|if
condition|(
operator|!
name|asm
condition|)
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|ASMHelper
operator|.
name|class
argument_list|,
literal|"badASM"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|null
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|JAXBDataBinding
name|db
init|=
name|createJaxbContext
argument_list|(
name|internal
argument_list|)
decl_stmt|;
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
literal|"Failed to map namespace "
operator|+
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
finally|finally
block|{
if|if
condition|(
operator|!
name|asm
condition|)
block|{
name|ReflectionUtil
operator|.
name|setAccessible
argument_list|(
name|ReflectionUtil
operator|.
name|getDeclaredField
argument_list|(
name|ASMHelper
operator|.
name|class
argument_list|,
literal|"badASM"
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|null
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeclaredNamespaceMappingRI
parameter_list|()
throws|throws
name|Exception
block|{
name|doNamespaceMappingTest
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeclaredNamespaceMappingInternal
parameter_list|()
throws|throws
name|Exception
block|{
name|doNamespaceMappingTest
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeclaredNamespaceMappingInternalNoAsm
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|doNamespaceMappingTest
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Internal needs ASM"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
name|er
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|er
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Failed to map namespace"
argument_list|)
operator|||
name|er
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Internal needs ASM"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
annotation|@
name|Test
specifier|public
name|void
name|testConfiguredXmlAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|Language
name|dutch
init|=
operator|new
name|Language
argument_list|(
literal|"nl_NL"
argument_list|,
literal|"Dutch"
argument_list|)
decl_stmt|;
name|Language
name|americanEnglish
init|=
operator|new
name|Language
argument_list|(
literal|"en_US"
argument_list|,
literal|"Americanish"
argument_list|)
decl_stmt|;
name|Person
name|person
init|=
operator|new
name|Person
argument_list|(
name|dutch
argument_list|)
decl_stmt|;
name|JAXBDataBinding
name|binding
init|=
operator|new
name|JAXBDataBinding
argument_list|(
name|Person
operator|.
name|class
argument_list|,
name|Language
operator|.
name|class
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setConfiguredXmlAdapters
argument_list|(
name|Arrays
operator|.
expr|<
name|XmlAdapter
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|>
name|asList
argument_list|(
operator|new
name|LanguageAdapter
argument_list|(
name|dutch
argument_list|,
name|americanEnglish
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|DataWriter
argument_list|<
name|OutputStream
argument_list|>
name|writer
init|=
name|binding
operator|.
name|createWriter
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|person
argument_list|,
name|baos
argument_list|)
expr_stmt|;
name|String
name|output
init|=
name|baos
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|xml
init|=
literal|"<person motherTongue=\"nl_NL\"/>"
decl_stmt|;
name|assertEquals
argument_list|(
name|xml
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|DataReader
argument_list|<
name|XMLStreamReader
argument_list|>
name|reader
init|=
name|binding
operator|.
name|createReader
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|Person
name|read
init|=
operator|(
name|Person
operator|)
name|reader
operator|.
name|read
argument_list|(
name|XMLInputFactory
operator|.
name|newFactory
argument_list|()
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xml
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|dutch
argument_list|,
name|read
operator|.
name|getMotherTongue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClassInDefaultPackage
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|sampleClassInDefaultPackage
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"SampleClassInDefaultPackage"
argument_list|)
decl_stmt|;
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
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
name|sampleClassInDefaultPackage
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|classes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|XmlRootElement
specifier|public
specifier|static
class|class
name|Person
block|{
annotation|@
name|XmlAttribute
annotation|@
name|XmlJavaTypeAdapter
argument_list|(
name|LanguageAdapter
operator|.
name|class
argument_list|)
specifier|private
specifier|final
name|Language
name|motherTongue
decl_stmt|;
specifier|public
name|Person
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Person
parameter_list|(
name|Language
name|motherTongue
parameter_list|)
block|{
name|this
operator|.
name|motherTongue
operator|=
name|motherTongue
expr_stmt|;
block|}
specifier|public
name|Language
name|getMotherTongue
parameter_list|()
block|{
return|return
name|motherTongue
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|Language
block|{
specifier|private
specifier|final
name|String
name|code
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|public
name|Language
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Language
parameter_list|(
name|String
name|code
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|code
operator|=
name|code
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getCode
parameter_list|()
block|{
return|return
name|code
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|LanguageAdapter
extends|extends
name|XmlAdapter
argument_list|<
name|String
argument_list|,
name|Language
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Language
argument_list|>
name|knownLanguages
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|LanguageAdapter
parameter_list|(
name|Language
modifier|...
name|languages
parameter_list|)
block|{
for|for
control|(
name|Language
name|language
range|:
name|languages
control|)
block|{
name|knownLanguages
operator|.
name|put
argument_list|(
name|language
operator|.
name|getCode
argument_list|()
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|marshal
parameter_list|(
name|Language
name|language
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|language
operator|.
name|getCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Language
name|unmarshal
parameter_list|(
name|String
name|code
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|knownLanguages
operator|.
name|get
argument_list|(
name|code
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

