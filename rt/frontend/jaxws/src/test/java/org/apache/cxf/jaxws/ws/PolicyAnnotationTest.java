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
name|ws
package|;
end_package

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
name|xml
operator|.
name|WSDLWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
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
name|annotations
operator|.
name|Policies
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
name|Policy
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
name|WSDLConstants
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
name|Server
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
name|XPathUtils
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
name|JaxWsServerFactoryBean
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
name|local
operator|.
name|LocalTransportFactory
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
name|ws
operator|.
name|policy
operator|.
name|EndpointPolicy
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
name|ws
operator|.
name|policy
operator|.
name|PolicyEngine
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
name|neethi
operator|.
name|Constants
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PolicyAnnotationTest
extends|extends
name|Assert
block|{
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|TestImpl
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tp
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
literal|"http://www.w3.org/2003/05/soap/bindings/HTTP/"
argument_list|,
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|,
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
decl_stmt|;
name|LocalTransportFactory
name|f
init|=
operator|new
name|LocalTransportFactory
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|f
operator|.
name|getUriPrefixes
argument_list|()
operator|.
name|add
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
name|f
operator|.
name|setTransportIds
argument_list|(
name|tp
argument_list|)
expr_stmt|;
name|f
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|f
operator|.
name|register
argument_list|()
expr_stmt|;
name|Server
name|s
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
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
name|WSDLWriter
name|wsdlWriter
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
decl_stmt|;
name|def
operator|.
name|setExtensionRegistry
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getExtensionRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|wsdl
init|=
name|wsdlWriter
operator|.
name|getDocument
argument_list|(
name|def
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
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
name|ns
operator|.
name|put
argument_list|(
literal|"wsdl"
argument_list|,
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"wsu"
argument_list|,
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"wsp"
argument_list|,
name|Constants
operator|.
name|URI_POLICY_13_NS
argument_list|)
expr_stmt|;
name|XPathUtils
name|xpu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(wsdl);
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:service/wsdl:port"
argument_list|,
literal|"TestImplPortPortPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/"
argument_list|,
literal|"TestInterfacePortTypePolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/wsdl:operation/"
argument_list|,
literal|"echoIntPortTypeOpPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/wsdl:operation/wsdl:input"
argument_list|,
literal|"echoIntPortTypeOpInputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/wsdl:operation/wsdl:output"
argument_list|,
literal|"echoIntPortTypeOpOutputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/"
argument_list|,
literal|"TestImplServiceSoapBindingBindingPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation/"
argument_list|,
literal|"echoIntBindingOpPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation/wsdl:input"
argument_list|,
literal|"echoIntBindingOpInputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation/wsdl:output"
argument_list|,
literal|"echoIntBindingOpOutputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:service/"
argument_list|,
literal|"TestImplServiceServicePolicy"
argument_list|)
expr_stmt|;
name|EndpointPolicy
name|policy
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
operator|.
name|getServerEndpointPolicy
argument_list|(
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|policy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|policy
operator|.
name|getChosenAlternative
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testAnnotationsInterfaceAsClass
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|factory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|TestImpl
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|TestInterface
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|tp
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
literal|"http://www.w3.org/2003/05/soap/bindings/HTTP/"
argument_list|,
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|,
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
decl_stmt|;
name|LocalTransportFactory
name|f
init|=
operator|new
name|LocalTransportFactory
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|f
operator|.
name|getUriPrefixes
argument_list|()
operator|.
name|add
argument_list|(
literal|"http"
argument_list|)
expr_stmt|;
name|f
operator|.
name|setTransportIds
argument_list|(
name|tp
argument_list|)
expr_stmt|;
name|f
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|f
operator|.
name|register
argument_list|()
expr_stmt|;
name|Server
name|s
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|ServiceWSDLBuilder
name|builder
init|=
operator|new
name|ServiceWSDLBuilder
argument_list|(
name|bus
argument_list|,
name|s
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
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
name|WSDLWriter
name|wsdlWriter
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
decl_stmt|;
name|def
operator|.
name|setExtensionRegistry
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
operator|.
name|getExtensionRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|Element
name|wsdl
init|=
name|wsdlWriter
operator|.
name|getDocument
argument_list|(
name|def
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ns
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
name|ns
operator|.
name|put
argument_list|(
literal|"wsdl"
argument_list|,
name|WSDLConstants
operator|.
name|NS_WSDL11
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"wsu"
argument_list|,
literal|"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
argument_list|)
expr_stmt|;
name|ns
operator|.
name|put
argument_list|(
literal|"wsp"
argument_list|,
name|Constants
operator|.
name|URI_POLICY_13_NS
argument_list|)
expr_stmt|;
name|XPathUtils
name|xpu
init|=
operator|new
name|XPathUtils
argument_list|(
name|ns
argument_list|)
decl_stmt|;
comment|//org.apache.cxf.helpers.XMLUtils.printDOM(wsdl);
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:service/wsdl:port"
argument_list|,
literal|"TestInterfacePortPortPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/"
argument_list|,
literal|"TestInterfacePortTypePolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/wsdl:operation/"
argument_list|,
literal|"echoIntPortTypeOpPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/wsdl:operation/wsdl:input"
argument_list|,
literal|"echoIntPortTypeOpInputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:portType/wsdl:operation/wsdl:output"
argument_list|,
literal|"echoIntPortTypeOpOutputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/"
argument_list|,
literal|"TestInterfaceServiceSoapBindingBindingPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation/"
argument_list|,
literal|"echoIntBindingOpPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation/wsdl:input"
argument_list|,
literal|"echoIntBindingOpInputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation/wsdl:output"
argument_list|,
literal|"echoIntBindingOpOutputPolicy"
argument_list|)
expr_stmt|;
name|check
argument_list|(
name|xpu
argument_list|,
name|wsdl
argument_list|,
literal|"/wsdl:definitions/wsdl:service/"
argument_list|,
literal|"TestInterfaceServiceServicePolicy"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|check
parameter_list|(
name|XPathUtils
name|xpu
parameter_list|,
name|Element
name|wsdl
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|uri
operator|+
literal|" not found"
argument_list|,
name|xpu
operator|.
name|isExist
argument_list|(
literal|"/wsdl:definitions/wsp:Policy[@wsu:Id='"
operator|+
name|uri
operator|+
literal|"']"
argument_list|,
name|wsdl
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|xpu
operator|.
name|getValueList
argument_list|(
literal|"/wsdl:definitions/wsp:Policy[@wsu:Id='"
operator|+
name|uri
operator|+
literal|"']"
argument_list|,
name|wsdl
argument_list|)
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri
operator|+
literal|" reference not found"
argument_list|,
name|xpu
operator|.
name|isExist
argument_list|(
name|path
operator|+
literal|"/wsp:PolicyReference[@URI='#"
operator|+
name|uri
operator|+
literal|"']"
argument_list|,
name|wsdl
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Policies
argument_list|(
block|{
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestInterfacePolicy.xml"
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestImplPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|SERVICE_PORT
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestPortTypePolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|PORT_TYPE
argument_list|)
block|}
argument_list|)
annotation|@
name|WebService
specifier|public
interface|interface
name|TestInterface
block|{
annotation|@
name|Policies
argument_list|(
block|{
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestOperationPolicy.xml"
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestOperationInputPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|BINDING_OPERATION_INPUT
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestOperationOutputPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|BINDING_OPERATION_OUTPUT
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestOperationPTPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestOperationPTInputPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_INPUT
argument_list|)
block|,
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestOperationPTOutputPolicy.xml"
argument_list|,
name|placement
operator|=
name|Policy
operator|.
name|Placement
operator|.
name|PORT_TYPE_OPERATION_OUTPUT
argument_list|)
block|}
argument_list|)
name|int
name|echoInt
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
block|}
annotation|@
name|Policies
argument_list|(
block|{
annotation|@
name|Policy
argument_list|(
name|uri
operator|=
literal|"annotationpolicies/TestImplPolicy.xml"
argument_list|)
block|}
argument_list|)
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.jaxws.ws.PolicyAnnotationTest$TestInterface"
argument_list|)
specifier|public
specifier|static
class|class
name|TestImpl
implements|implements
name|TestInterface
block|{
specifier|public
name|int
name|echoInt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|i
return|;
block|}
block|}
block|}
end_class

end_unit

