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
package|;
end_package

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
name|XmlType
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
name|Endpoint
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
name|WebFault
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
name|annotations
operator|.
name|WSDLDocumentation
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
name|annotations
operator|.
name|WSDLDocumentationCollection
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|service
operator|.
name|Hello2
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
name|service
operator|.
name|Hello3
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
name|service
operator|.
name|HelloExcludeImpl
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
name|support
operator|.
name|JaxWsImplementorInfo
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
name|support
operator|.
name|JaxWsServiceFactoryBean
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
name|WSDLManager
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|CodeFirstWSDLTest
extends|extends
name|AbstractJaxWsTest
block|{
name|String
name|address
init|=
literal|"local://localhost:9000/Hello"
decl_stmt|;
specifier|private
name|Definition
name|createService
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|Exception
block|{
name|JaxWsImplementorInfo
name|info
init|=
operator|new
name|JaxWsImplementorInfo
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|(
name|info
argument_list|)
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
name|Service
name|service
init|=
name|bean
operator|.
name|create
argument_list|()
decl_stmt|;
name|InterfaceInfo
name|i
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
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|i
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ServerFactoryBean
name|svrFactory
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
name|clazz
operator|.
name|newInstance
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|BindingInfo
argument_list|>
name|bindings
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
name|ServiceWSDLBuilder
name|wsdlBuilder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|wsdlBuilder
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWSDL1
parameter_list|()
throws|throws
name|Exception
block|{
name|Definition
name|d
init|=
name|createService
argument_list|(
name|Hello2
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"Hello2Service"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|wsdl
operator|.
name|Service
name|service
init|=
name|d
operator|.
name|getService
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"Hello2Port"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|wsdl
operator|.
name|Port
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|QName
name|portTypeName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"HelloInterface"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|wsdl
operator|.
name|PortType
name|portType
init|=
name|d
operator|.
name|getPortType
argument_list|(
name|portTypeName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|portType
operator|.
name|getOperations
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
name|testWSDL2
parameter_list|()
throws|throws
name|Exception
block|{
name|Definition
name|d
init|=
name|createService
argument_list|(
name|Hello3
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://mynamespace.com/"
argument_list|,
literal|"MyService"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|wsdl
operator|.
name|Service
name|service
init|=
name|d
operator|.
name|getService
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://mynamespace.com/"
argument_list|,
literal|"MyPort"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|wsdl
operator|.
name|Port
name|port
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|QName
name|portTypeName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"HelloInterface"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|wsdl
operator|.
name|PortType
name|portType
init|=
name|d
operator|.
name|getPortType
argument_list|(
name|portTypeName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|portType
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|portType
operator|.
name|getOperations
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
name|testExcludeOnInterface
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|JaxWsImplementorInfo
name|info
init|=
operator|new
name|JaxWsImplementorInfo
argument_list|(
name|HelloExcludeImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|ReflectionServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|(
name|info
argument_list|)
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
name|create
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"WebMethod(exclude=true) is not allowed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JaxWsConfigurationException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"WebMethod"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocumentationOnSEI
parameter_list|()
throws|throws
name|Exception
block|{
comment|//CXF-3093
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"local://foo"
argument_list|,
operator|new
name|CXF3093Impl
argument_list|()
argument_list|)
decl_stmt|;
name|ServiceWSDLBuilder
name|wsdlBuilder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|wsdlBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|Document
name|d
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getWSDLFactory
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
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(d);
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:documentation"
argument_list|,
literal|"My top level documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:portType/wsdl:documentation"
argument_list|,
literal|"My portType documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:binding/wsdl:documentation"
argument_list|,
literal|"My binding doc"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|JaxwsServiceBuilder
name|builder
init|=
operator|new
name|JaxwsServiceBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setServiceClass
argument_list|(
name|CXF3093Impl
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|wsdlBuilder
operator|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
name|def
operator|=
name|wsdlBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|d
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getWSDLFactory
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|getDocument
argument_list|(
name|def
argument_list|)
expr_stmt|;
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(d);
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:documentation"
argument_list|,
literal|"My top level documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:portType/wsdl:documentation"
argument_list|,
literal|"My portType documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:binding/wsdl:documentation"
argument_list|,
literal|"My binding doc"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|)
annotation|@
name|WSDLDocumentationCollection
argument_list|(
block|{
annotation|@
name|WSDLDocumentation
argument_list|(
literal|"My portType documentation"
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"My top level documentation"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|TOP
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"My binding doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING
argument_list|)
block|}
argument_list|)
specifier|public
interface|interface
name|CXF3093PortType
block|{
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
function_decl|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|portName
operator|=
literal|"DoubleItPort"
argument_list|)
specifier|public
specifier|static
class|class
name|CXF3093Impl
implements|implements
name|CXF3093PortType
block|{
specifier|public
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
block|{
return|return
name|numberToDouble
operator|*
literal|2
return|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnlyRootElementOnFaultBean
parameter_list|()
throws|throws
name|Exception
block|{
comment|//CXF-4016
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"local://foo4016"
argument_list|,
operator|new
name|CXF4016Impl
argument_list|()
argument_list|)
decl_stmt|;
name|ServiceWSDLBuilder
name|wsdlBuilder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|wsdlBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|Document
name|d
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getWSDLFactory
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
name|this
operator|.
name|addNamespace
argument_list|(
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
literal|"tns"
argument_list|)
expr_stmt|;
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(d);
name|assertXPathEquals
argument_list|(
literal|"//xsd:element/@ref"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
literal|"CustomMessageBean"
argument_list|)
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|portName
operator|=
literal|"DoubleItPort"
argument_list|)
specifier|public
specifier|static
class|class
name|CXF4016Impl
block|{
specifier|public
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
throws|throws
name|CustomException
block|{
return|return
name|numberToDouble
operator|*
literal|2
return|;
block|}
block|}
annotation|@
name|WebFault
argument_list|(
name|name
operator|=
literal|"CustomException"
argument_list|,
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|)
specifier|public
specifier|static
class|class
name|CustomException
extends|extends
name|Exception
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|CustomMessageBean
name|faultInfo
decl_stmt|;
specifier|public
name|CustomException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CustomException
parameter_list|(
name|String
name|msg
parameter_list|,
name|CustomMessageBean
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|faultInfo
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|CustomMessageBean
name|getCustomMessage
parameter_list|()
block|{
return|return
name|faultInfo
return|;
block|}
specifier|public
name|void
name|setCustomMessage
parameter_list|(
name|CustomMessageBean
name|b
parameter_list|)
block|{
name|faultInfo
operator|=
name|b
expr_stmt|;
block|}
block|}
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"CustomMessageBean"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"myId"
block|,
literal|"msg"
block|}
argument_list|)
specifier|public
specifier|static
class|class
name|CustomMessageBean
block|{
name|String
name|msg
decl_stmt|;
name|int
name|myId
decl_stmt|;
specifier|public
name|CustomMessageBean
parameter_list|()
block|{         }
specifier|public
name|int
name|getMyId
parameter_list|()
block|{
return|return
name|myId
return|;
block|}
specifier|public
name|void
name|setMyId
parameter_list|(
name|int
name|myId
parameter_list|)
block|{
name|this
operator|.
name|myId
operator|=
name|myId
expr_stmt|;
block|}
specifier|public
name|String
name|getMsg
parameter_list|()
block|{
return|return
name|msg
return|;
block|}
specifier|public
name|void
name|setMsg
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|this
operator|.
name|msg
operator|=
name|msg
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDocumentationOnImpl
parameter_list|()
throws|throws
name|Exception
block|{
comment|//CXF-3092
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"local://foo"
argument_list|,
operator|new
name|CXF3092Impl
argument_list|()
argument_list|)
decl_stmt|;
name|ServiceWSDLBuilder
name|wsdlBuilder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Definition
name|def
init|=
name|wsdlBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|Document
name|d
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getWSDLFactory
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
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(d);
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:documentation"
argument_list|,
literal|"My top level documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:service/wsdl:documentation"
argument_list|,
literal|"My Service documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:binding/wsdl:documentation"
argument_list|,
literal|"My binding doc"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|JaxwsServiceBuilder
name|builder
init|=
operator|new
name|JaxwsServiceBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setServiceClass
argument_list|(
name|CXF3092Impl
operator|.
name|class
argument_list|)
expr_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|wsdlBuilder
operator|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
name|def
operator|=
name|wsdlBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|d
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getWSDLFactory
argument_list|()
operator|.
name|newWSDLWriter
argument_list|()
operator|.
name|getDocument
argument_list|(
name|def
argument_list|)
expr_stmt|;
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(d);
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:documentation"
argument_list|,
literal|"My top level documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:service/wsdl:documentation"
argument_list|,
literal|"My Service documentation"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|assertXPathEquals
argument_list|(
literal|"//wsdl:definitions/wsdl:binding/wsdl:documentation"
argument_list|,
literal|"My binding doc"
argument_list|,
name|d
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|)
specifier|public
interface|interface
name|CXF3092PortType
block|{
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
function_decl|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"http://www.example.org/contract/DoubleIt"
argument_list|,
name|serviceName
operator|=
literal|"DoubleItService"
argument_list|,
name|portName
operator|=
literal|"DoubleItPort"
argument_list|)
annotation|@
name|WSDLDocumentationCollection
argument_list|(
block|{
annotation|@
name|WSDLDocumentation
argument_list|(
literal|"My Service documentation"
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"My top level documentation"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|TOP
argument_list|)
block|,
annotation|@
name|WSDLDocumentation
argument_list|(
name|value
operator|=
literal|"My binding doc"
argument_list|,
name|placement
operator|=
name|WSDLDocumentation
operator|.
name|Placement
operator|.
name|BINDING
argument_list|)
block|}
argument_list|)
specifier|public
specifier|static
class|class
name|CXF3092Impl
implements|implements
name|CXF3092PortType
block|{
specifier|public
name|int
name|doubleIt
parameter_list|(
name|int
name|numberToDouble
parameter_list|)
block|{
return|return
name|numberToDouble
operator|*
literal|2
return|;
block|}
block|}
block|}
end_class

end_unit

