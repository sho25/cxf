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
name|net
operator|.
name|URL
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
name|jws
operator|.
name|WebService
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
name|factory
operator|.
name|WSDLFactory
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
name|ws
operator|.
name|Binding
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
name|WebServiceException
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
name|WebServiceFeature
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
name|soap
operator|.
name|MTOMFeature
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
name|soap
operator|.
name|SOAPBinding
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
name|NodeList
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
name|endpoint
operator|.
name|Endpoint
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
name|AbstractJaxWsTest
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
name|mtom_xop
operator|.
name|TestMtomImpl
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
name|no_body_parts
operator|.
name|NoBodyPartsImpl
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
name|Service
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
name|invoker
operator|.
name|BeanInvoker
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
name|FaultInfo
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
name|InterfaceInfo
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
name|MessagePartInfo
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
name|wsdl
operator|.
name|service
operator|.
name|factory
operator|.
name|ReflectionServiceFactoryBean
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
name|ServiceWSDLBuilder
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
name|GreeterImpl
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
name|XmlSchemaElement
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

begin_class
specifier|public
class|class
name|JaxWsServiceFactoryBeanTest
extends|extends
name|AbstractJaxWsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testDocLiteralPartWithType
parameter_list|()
throws|throws
name|Exception
block|{
name|ReflectionServiceFactoryBean
name|serviceFactory
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|serviceFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|serviceFactory
operator|.
name|setServiceClass
argument_list|(
name|NoBodyPartsImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|serviceFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
literal|"urn:org:apache:cxf:no_body_parts/wsdl"
argument_list|,
literal|"operation1"
argument_list|)
decl_stmt|;
name|MessageInfo
name|mi
init|=
name|serviceInfo
operator|.
name|getMessage
argument_list|(
name|qname
argument_list|)
decl_stmt|;
name|qname
operator|=
operator|new
name|QName
argument_list|(
literal|"urn:org:apache:cxf:no_body_parts/wsdl"
argument_list|,
literal|"mimeAttachment"
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|mi
operator|.
name|getMessagePart
argument_list|(
name|qname
argument_list|)
decl_stmt|;
name|QName
name|elementQName
init|=
name|mpi
operator|.
name|getElementQName
argument_list|()
decl_stmt|;
name|XmlSchemaElement
name|element
init|=
name|serviceInfo
operator|.
name|getXmlSchemaCollection
argument_list|()
operator|.
name|getElementByQName
argument_list|(
name|elementQName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|URL
name|resource
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setWsdlURL
argument_list|(
name|resource
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|BeanInvoker
name|invoker
init|=
operator|new
name|BeanInvoker
argument_list|(
operator|new
name|GreeterImpl
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
literal|"http://apache.org/hello_world_soap_http"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SOAPService"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|InterfaceInfo
name|intf
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|OperationInfo
name|op
init|=
name|intf
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"sayHi"
argument_list|)
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|wrapper
init|=
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
name|wrapper
operator|=
name|op
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTypeClass
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|invoker
argument_list|,
name|service
operator|.
name|getInvoker
argument_list|()
argument_list|)
expr_stmt|;
name|op
operator|=
name|intf
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"testDocLitFault"
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|FaultInfo
argument_list|>
name|faults
init|=
name|op
operator|.
name|getFaults
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|faults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|FaultInfo
name|f
init|=
name|op
operator|.
name|getFault
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"BadRecordLitFault"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|f
operator|.
name|getProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|f
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
name|f
operator|.
name|getMessagePartByIndex
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mpi
operator|.
name|getTypeClass
argument_list|()
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
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|TestMtomImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|intf
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|OperationInfo
name|op
init|=
name|intf
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/mime"
argument_list|,
literal|"testXop"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|op
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|MessagePartInfo
argument_list|>
name|itr
init|=
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|part
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testXop"
argument_list|,
name|part
operator|.
name|getElementQName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|op
operator|=
name|op
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|op
argument_list|)
expr_stmt|;
comment|// test setup of input parts
name|itr
operator|=
name|op
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|itr
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|part
operator|=
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|part
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|part
operator|.
name|getTypeClass
argument_list|()
argument_list|)
expr_stmt|;
comment|/*          * revisit, try to use other wsdl operation rewrite test in future         assertTrue(itr.hasNext());         part = itr.next();         assertEquals(Boolean.TRUE, part.getProperty(JaxWsServiceFactoryBean.MODE_INOUT));         assertEquals(byte[].class, part.getTypeClass());          assertFalse(itr.hasNext());          // test output setup         itr = op.getOutput().getMessageParts().iterator();          assertTrue(itr.hasNext());         part = itr.next();         assertEquals(Boolean.TRUE, part.getProperty(JaxWsServiceFactoryBean.MODE_INOUT));         */
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWrappedDocLit
parameter_list|()
throws|throws
name|Exception
block|{
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
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
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|InterfaceInfo
name|intf
init|=
name|si
operator|.
name|getInterface
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|intf
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|ns
init|=
name|si
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|OperationInfo
name|greetMeOp
init|=
name|intf
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"greetMe"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|greetMeOp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMe"
argument_list|,
name|greetMeOp
operator|.
name|getInput
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
name|greetMeOp
operator|.
name|getInput
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|messageParts
init|=
name|greetMeOp
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messageParts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|inMessagePart
init|=
name|messageParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
name|inMessagePart
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_doc_lit/types"
argument_list|,
name|inMessagePart
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
comment|// test output
name|messageParts
operator|=
name|greetMeOp
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|messageParts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"greetMeResponse"
argument_list|,
name|greetMeOp
operator|.
name|getOutput
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|outMessagePart
init|=
name|messageParts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|//assertEquals("result", outMessagePart.getName().getLocalPart());
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_doc_lit"
argument_list|,
name|outMessagePart
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://apache.org/hello_world_doc_lit/types"
argument_list|,
name|outMessagePart
operator|.
name|getElementQName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|OperationInfo
name|greetMeOneWayOp
init|=
name|si
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"greetMeOneWay"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|greetMeOneWayOp
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|greetMeOneWayOp
operator|.
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SchemaInfo
argument_list|>
name|schemas
init|=
name|si
operator|.
name|getSchemas
argument_list|()
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBareBug
parameter_list|()
throws|throws
name|Exception
block|{
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|test
operator|.
name|TestInterfacePort
operator|.
name|class
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|si
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|Document
name|wsdl
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|getDocument
argument_list|(
name|def
argument_list|)
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/xsd:schema"
operator|+
literal|"[@targetNamespace='http://cxf.apache.org/"
operator|+
literal|"org.apache.cxf.test.TestInterface/xsd']"
operator|+
literal|"/xsd:element[@name='getMessage']"
argument_list|,
name|wsdl
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='setMessage']"
operator|+
literal|"/wsdl:part[@name = 'parameters'][@element='ns1:setMessage']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='echoCharResponse']"
operator|+
literal|"/wsdl:part[@name = 'y'][@element='ns1:charEl_y']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='echoCharResponse']"
operator|+
literal|"/wsdl:part[@name = 'return'][@element='ns1:charEl_return']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='echoCharResponse']"
operator|+
literal|"/wsdl:part[@name = 'z'][@element='ns1:charEl_z']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='echoChar']"
operator|+
literal|"/wsdl:part[@name = 'x'][@element='ns1:charEl_x']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='echoChar']"
operator|+
literal|"/wsdl:part[@name = 'y'][@element='ns1:charEl_y']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMtomFeature
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
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setWsdlURL
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world.wsdl"
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setWsFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|WebServiceFeature
index|[]
block|{
operator|new
name|MTOMFeature
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|service
operator|.
name|getEndpoints
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
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|JaxWsEndpointImpl
argument_list|)
expr_stmt|;
name|Binding
name|binding
init|=
operator|(
operator|(
name|JaxWsEndpointImpl
operator|)
name|endpoint
operator|)
operator|.
name|getJaxwsBinding
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|binding
operator|instanceof
name|SOAPBinding
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|SOAPBinding
operator|)
name|binding
operator|)
operator|.
name|isMTOMEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebSeviceException
parameter_list|()
throws|throws
name|Exception
block|{
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|getBus
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|WebServiceExceptionTestImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|ServiceInfo
name|si
init|=
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|si
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|Document
name|wsdl
init|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|getDocument
argument_list|(
name|def
argument_list|)
decl_stmt|;
name|assertInvalid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='WebServiceException']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
specifier|public
specifier|static
class|class
name|WebServiceExceptionTestImpl
block|{
specifier|public
name|int
name|echoInt
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|WebServiceException
block|{
return|return
name|i
return|;
block|}
block|}
block|}
end_class

end_unit

