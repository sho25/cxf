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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|jaxws
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|helpers
operator|.
name|IOUtils
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
name|JAXBContextCache
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
name|jaxws
operator|.
name|JaxwsServiceBuilder
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
name|BindingInfo
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
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|WSDL11Generator
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
name|util
operator|.
name|AnnotationUtil
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
name|hello_world_rpclit
operator|.
name|javato
operator|.
name|GreeterRPCLit
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
name|Ignore
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
name|JaxwsServiceBuilderTest
extends|extends
name|ProcessorTestBase
block|{
name|JaxwsServiceBuilder
name|builder
decl_stmt|;
name|WSDL11Generator
name|generator
init|=
operator|new
name|WSDL11Generator
argument_list|()
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
name|JAXBContextCache
operator|.
name|clearCaches
argument_list|()
expr_stmt|;
name|builder
operator|=
operator|new
name|JaxwsServiceBuilder
argument_list|()
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
name|generator
operator|.
name|setBus
argument_list|(
name|builder
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|Bus
name|b
init|=
name|builder
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|b
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|getDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocLitWrappedWithWrapperClass
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|StockWrapped
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"doc_lit_wrapped_with_wrapperclass.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_doc_lit_wrapped_with_wrapperclass.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocLitWrappedWithoutWrapperClass
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|HelloWrapped
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"doc_lit_wrapped_no_wrapperclass.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_doc_lit_wrapped_no_wrapperclass.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
comment|// REVISIT two fault elements in schema
annotation|@
name|Test
specifier|public
name|void
name|testDocLitWrapped
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_lit
operator|.
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"hello_doc_lit.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_hello_world_doc_lit.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
comment|//assertFileEquals(expectedFile, output.getAbsolutePath());
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocWrappedWithLocalName
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|Stock
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"doc_lit_wrapped_localName.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_doc_lit_wrapped_localName.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocWrappedNoWebParam
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|HelloWithNoWebParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"doc_lit_wrapped_no_webparam.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_doc_lit_wrapped_no_webparam.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHolder
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|holder
operator|.
name|HolderService
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"holder.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_holder.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|hello_world_async_soap_http
operator|.
name|GreeterAsync
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"hello_async.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_hello_world_async.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRPCLit
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|GreeterRPCLit
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAddress
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|getOutputFile
argument_list|(
literal|"rpc_lit.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_rpc_lit.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|// TODO assertFileEquals
annotation|@
name|Test
specifier|public
name|void
name|testDocWrapparBare
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|hello_world_doc_wrapped_bare
operator|.
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAddress
argument_list|(
literal|"http://localhost"
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|getOutputFile
argument_list|(
literal|"doc_wrapped_bare.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO assertFileEquals
annotation|@
name|Test
specifier|public
name|void
name|testRPCWithoutParentBindingAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|rpc
operator|.
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|getOutputFile
argument_list|(
literal|"rpc_lit_service_no_anno.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: SOAPBinding can not on method with RPC style
annotation|@
name|Test
annotation|@
name|Ignore
argument_list|(
literal|"RuntimeException: org.apache.cxf.interceptor.Fault: Method [sayHi] pro"
argument_list|)
specifier|public
name|void
name|testSOAPBindingRPCOnMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|rpc
operator|.
name|HelloWrongAnnotation
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|getOutputFile
argument_list|(
literal|"rpc_on_method.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSoapHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|samples
operator|.
name|headers
operator|.
name|HeaderTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|getOutputFile
argument_list|(
literal|"soap_header.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/soap_header.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|// TODO: assertFileEquals
annotation|@
name|Test
specifier|public
name|void
name|testCXF188
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|clz
init|=
name|AnnotationUtil
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.tools.fortest.cxf188.Demo"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setServiceClass
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|file
init|=
name|getOutputFile
argument_list|(
literal|"cxf188.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRpcLitNoSEI
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|rpc
operator|.
name|EchoImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/echotest"
argument_list|,
literal|"EchoService"
argument_list|)
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/echotest"
argument_list|,
literal|"Echo"
argument_list|)
argument_list|,
name|service
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|output
init|=
name|getOutputFile
argument_list|(
literal|"rpclist_no_sei.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|output
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|output
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"EchoPort"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|URI
name|expectedFile
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"expected/expected_rpclist_no_sei.wsdl"
argument_list|)
operator|.
name|toURI
argument_list|()
decl_stmt|;
name|assertWsdlEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF669
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|oldSetting
init|=
name|generator
operator|.
name|allowImports
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setAllowImports
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|cxf669
operator|.
name|HelloImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com/HelloWorldService"
argument_list|,
literal|"HelloService"
argument_list|)
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com/HelloWorld"
argument_list|,
literal|"HelloWorld"
argument_list|)
argument_list|,
name|service
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|getSchemas
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://foo.com/HelloWorld"
argument_list|,
name|service
operator|.
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|BindingInfo
argument_list|>
name|bindings
init|=
name|service
operator|.
name|getBindings
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bindings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.com/HelloWorldService"
argument_list|,
literal|"HelloServiceSoapBinding"
argument_list|)
argument_list|,
name|bindings
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|wsdl
init|=
name|getOutputFile
argument_list|(
literal|"HelloService.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsdl
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|logical
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"HelloWorld.wsdl"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|logical
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|schema
init|=
operator|new
name|File
argument_list|(
name|output
argument_list|,
literal|"HelloService_schema1.xsd"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|schema
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|wsdl
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"<wsdl:import namespace=\"http://foo.com/HelloWorld\" "
operator|+
literal|"location=\"HelloWorld.wsdl\">"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"targetNamespace=\"http://foo.com/HelloWorldService\""
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|s
operator|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|logical
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"<import namespace=\"http://foo.com/HelloWorld\" "
operator|+
literal|"schemaLocation=\"HelloService_schema1.xsd\"/>"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"targetNamespace=\"http://foo.com/HelloWorld\""
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|s
operator|=
name|IOUtils
operator|.
name|toString
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|schema
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"targetNamespace=\"http://foo.com/HelloWorld\""
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setAllowImports
argument_list|(
name|oldSetting
argument_list|)
expr_stmt|;
block|}
specifier|private
name|File
name|getOutputFile
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|output
argument_list|,
name|fileName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

