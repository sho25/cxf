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
name|provider
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
name|Binding
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
name|soap
operator|.
name|SoapBinding
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
name|soap
operator|.
name|model
operator|.
name|SoapBindingInfo
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
name|xml
operator|.
name|XMLBinding
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
name|endpoint
operator|.
name|ServerImpl
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
name|jaxws
operator|.
name|JAXWSMethodInvoker
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
name|hello_world_soap_http
operator|.
name|HWSoapMessageProvider
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|ProviderServiceFactoryTest
extends|extends
name|AbstractJaxWsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFromWSDL
parameter_list|()
throws|throws
name|Exception
block|{
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
name|JaxWsImplementorInfo
name|implInfo
init|=
operator|new
name|JaxWsImplementorInfo
argument_list|(
name|HWSoapMessageProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|JaxWsServiceFactoryBean
name|bean
init|=
operator|new
name|JaxWsServiceFactoryBean
argument_list|(
name|implInfo
argument_list|)
decl_stmt|;
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
name|HWSoapMessageProvider
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
name|assertTrue
argument_list|(
name|service
operator|.
name|getInvoker
argument_list|()
operator|instanceof
name|JAXWSMethodInvoker
argument_list|)
expr_stmt|;
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
literal|"http://apache.org/hello_world_soap_http"
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
name|assertNotNull
argument_list|(
name|intf
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
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
name|setStart
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ServerImpl
name|server
init|=
operator|(
name|ServerImpl
operator|)
name|svrFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|server
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInvoker
argument_list|()
operator|instanceof
name|JAXWSMethodInvoker
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Binding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|binding
operator|instanceof
name|SoapBinding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testXMLBindingFromCode
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
name|DOMSourcePayloadProvider
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|setInvoker
argument_list|(
operator|new
name|JAXWSMethodInvoker
argument_list|(
operator|new
name|DOMSourcePayloadProvider
argument_list|()
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
name|assertEquals
argument_list|(
literal|"DOMSourcePayloadProviderService"
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
name|assertNotNull
argument_list|(
name|intf
argument_list|)
expr_stmt|;
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9000/test"
decl_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|ServerImpl
name|server
init|=
operator|(
name|ServerImpl
operator|)
name|svrFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
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
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Binding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|binding
operator|instanceof
name|XMLBinding
argument_list|)
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"/org/apache/cxf/jaxws/provider/sayHi.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"j"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/j:sayHi"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSOAPBindingFromCode
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
name|SOAPSourcePayloadProvider
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|setInvoker
argument_list|(
operator|new
name|JAXWSMethodInvoker
argument_list|(
operator|new
name|SOAPSourcePayloadProvider
argument_list|()
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
name|assertEquals
argument_list|(
literal|"SOAPSourcePayloadProviderService"
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
name|assertNotNull
argument_list|(
name|intf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
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
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"local://localhost:9000/test"
decl_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|ServerImpl
name|server
init|=
operator|(
name|ServerImpl
operator|)
name|svrFactory
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// See if our endpoint was created correctly
name|assertEquals
argument_list|(
literal|1
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
operator|.
name|getEndpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Binding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|binding
operator|instanceof
name|SoapBinding
argument_list|)
expr_stmt|;
name|SoapBindingInfo
name|sb
init|=
operator|(
name|SoapBindingInfo
operator|)
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"document"
argument_list|,
name|sb
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|bean
operator|.
name|isWrapped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sb
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"/org/apache/cxf/jaxws/sayHi.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"j"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body/j:sayHi"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSAAJProviderCodeFirst
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
name|SAAJProvider
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|setInvoker
argument_list|(
operator|new
name|JAXWSMethodInvoker
argument_list|(
operator|new
name|SAAJProvider
argument_list|()
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
name|assertEquals
argument_list|(
literal|"SAAJProviderService"
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
name|assertNotNull
argument_list|(
name|intf
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
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
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceFactory
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"local://localhost:9000/test"
decl_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|ServerImpl
name|server
init|=
operator|(
name|ServerImpl
operator|)
name|svrFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|server
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Binding
name|binding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|binding
operator|instanceof
name|SoapBinding
argument_list|)
expr_stmt|;
name|SoapBindingInfo
name|sb
init|=
operator|(
name|SoapBindingInfo
operator|)
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"document"
argument_list|,
name|sb
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|bean
operator|.
name|isWrapped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|sb
operator|.
name|getOperations
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"/org/apache/cxf/jaxws/sayHi.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"j"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body/j:sayHi"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStreamSourceProviderCodeFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setServiceClass
argument_list|(
name|StreamSourcePayloadProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|StreamSourcePayloadProvider
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9000/test"
decl_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setTransportId
argument_list|(
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"/org/apache/cxf/jaxws/sayHi.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"j"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body/j:sayHi"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSourceMessageProviderCodeFirst
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setServiceClass
argument_list|(
name|SourceMessageProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
operator|new
name|SourceMessageProvider
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"local://localhost:9000/test"
decl_stmt|;
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
name|Node
name|res
init|=
name|invoke
argument_list|(
name|address
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"/org/apache/cxf/jaxws/sayHi.xml"
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"j"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/s:Envelope/s:Body/j:sayHi"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

