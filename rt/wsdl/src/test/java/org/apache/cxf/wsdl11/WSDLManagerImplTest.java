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
name|Part
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
name|XMLStreamReader
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
name|SysPropExpandingStreamReader
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
name|XMLStreamReaderWrapper
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
name|WSDLManagerImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBuildSimpleWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|qname
init|=
literal|"http://apache.org/hello_world_soap_http"
decl_stmt|;
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"hello_world.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLManagerImpl
name|builder
init|=
operator|new
name|WSDLManagerImpl
argument_list|()
decl_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|getDefinition
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
literal|"SoapPort"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuildImportedWSDL
parameter_list|()
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
literal|"hello_world_services.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLManagerImpl
name|builder
init|=
operator|new
name|WSDLManagerImpl
argument_list|()
decl_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|getDefinition
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
name|String
name|serviceQName
init|=
literal|"http://apache.org/hello_world/services"
decl_stmt|;
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
name|serviceQName
argument_list|,
literal|"SOAPService"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
literal|"SoapPort"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|port
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
name|QName
name|bindingQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/bindings"
argument_list|,
literal|"SOAPBinding"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|bindingQName
argument_list|,
name|binding
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|PortType
name|portType
init|=
name|binding
operator|.
name|getPortType
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|QName
name|portTypeQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"Greeter"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|portTypeQName
argument_list|,
name|portType
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|Operation
name|op1
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
name|op1
argument_list|)
expr_stmt|;
name|QName
name|messageQName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/messages"
argument_list|,
literal|"sayHiRequest"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|messageQName
argument_list|,
name|op1
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|Part
name|part
init|=
name|op1
operator|.
name|getInput
argument_list|()
operator|.
name|getMessage
argument_list|()
operator|.
name|getPart
argument_list|(
literal|"in"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|part
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/types"
argument_list|,
literal|"sayHi"
argument_list|)
argument_list|,
name|part
operator|.
name|getElementName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalNamespacedWSDL
parameter_list|()
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
literal|"hello_world_local_nsdecl.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLManagerImpl
name|builder
init|=
operator|new
name|WSDLManagerImpl
argument_list|()
decl_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|getDefinition
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|builder
operator|.
name|getWSDLFactory
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXMLStreamReaderWrapper
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|testProp
init|=
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.test.wsdl11.port"
argument_list|,
literal|"99999"
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|wsdlUrl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"hello_world_wrap.wsdl"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|WSDLManagerImpl
name|builder
init|=
operator|new
name|WSDLManagerImpl
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setXMLStreamReaderWrapper
argument_list|(
operator|new
name|XMLStreamReaderWrapper
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|XMLStreamReader
name|wrap
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
return|return
operator|new
name|SysPropExpandingStreamReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|getDefinition
argument_list|(
name|wsdlUrl
argument_list|)
decl_stmt|;
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|builder
operator|.
name|getWSDLFactory
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|writeWSDL
argument_list|(
name|def
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"http://localhost:99999/SoapContext/SoapPort"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|testProp
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.test.wsdl11.port"
argument_list|,
name|testProp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"org.apache.cxf.test.wsdl11.port"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

