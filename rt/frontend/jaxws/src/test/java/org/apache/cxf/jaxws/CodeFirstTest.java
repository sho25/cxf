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
name|ArrayList
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|interceptor
operator|.
name|LoggingOutInterceptor
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
name|ArrayService
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
name|ArrayServiceImpl
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
name|FooServiceImpl
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
name|Hello
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
name|HelloInterface
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
name|SayHi
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
name|SayHiImpl
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
name|CodeFirstTest
extends|extends
name|AbstractJaxWsTest
block|{
name|String
name|address
init|=
literal|"local://localhost:9000/Hello"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testDocLitModel
parameter_list|()
throws|throws
name|Exception
block|{
name|Definition
name|d
init|=
name|createService
argument_list|(
literal|false
argument_list|)
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
name|d
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"svc"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:service[@name='HelloService']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsdl:port/wsdlsoap:address[@location='"
operator|+
name|address
operator|+
literal|"']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsdl:portType[@name='Hello']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/xsd:schema"
operator|+
literal|"[@targetNamespace='http://service.jaxws.cxf.apache.org/']"
operator|+
literal|"/xsd:import[@namespace='http://jaxb.dev.java.net/array']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:types/xsd:schema"
operator|+
literal|"[@targetNamespace='http://service.jaxws.cxf.apache.org/']"
operator|+
literal|"/xsd:element[@type='ns0:stringArray']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='sayHi']"
operator|+
literal|"/wsdl:part[@element='tns:sayHi'][@name='sayHi']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='getGreetingsResponse']"
operator|+
literal|"/wsdl:part[@element='tns:getGreetingsResponse'][@name='getGreetingsResponse']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:binding/wsdl:operation[@name='getGreetings']"
operator|+
literal|"/wsdlsoap:operation[@soapAction='myaction']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWrappedModel
parameter_list|()
throws|throws
name|Exception
block|{
name|Definition
name|d
init|=
name|createService
argument_list|(
literal|true
argument_list|)
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
name|d
argument_list|)
decl_stmt|;
name|addNamespace
argument_list|(
literal|"svc"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:service[@name='HelloService']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsdl:port/wsdlsoap:address[@location='"
operator|+
name|address
operator|+
literal|"']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//wsdl:portType[@name='Hello']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='sayHi']"
operator|+
literal|"/wsdl:part[@element='tns:sayHi'][@name='parameters']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"/wsdl:definitions/wsdl:message[@name='sayHiResponse']"
operator|+
literal|"/wsdl:part[@element='tns:sayHiResponse'][@name='parameters']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:complexType[@name='sayHi']"
operator|+
literal|"/xsd:sequence/xsd:element[@name='arg0']"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Definition
name|createService
parameter_list|(
name|boolean
name|wrapped
parameter_list|)
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
name|Hello
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setWrapped
argument_list|(
name|wrapped
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
literal|4
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
name|testEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Hello
name|service
init|=
operator|new
name|Hello
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|service
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|ep
operator|.
name|setExecutor
argument_list|(
operator|new
name|Executor
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
operator|new
name|Thread
argument_list|(
name|r
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
name|Node
name|res
init|=
name|invoke
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"sayHi.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"h"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:Body/h:sayHiResponse/return"
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|res
operator|=
name|invoke
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|,
name|LocalTransportFactory
operator|.
name|TRANSPORT_ID
argument_list|,
literal|"getGreetings.xml"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"h"
argument_list|,
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:Body/h:getGreetingsResponse/return[1]"
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//s:Body/h:getGreetingsResponse/return[2]"
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClient
parameter_list|()
throws|throws
name|Exception
block|{
name|Hello
name|serviceImpl
init|=
operator|new
name|Hello
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|serviceImpl
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"HelloService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"HelloPort"
argument_list|)
decl_stmt|;
comment|// need to set the same bus with service , so use the ServiceImpl
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
operator|(
name|URL
operator|)
literal|null
argument_list|,
name|serviceName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
name|HelloInterface
name|proxy
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|HelloInterface
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong result"
argument_list|,
literal|"hello"
argument_list|,
name|proxy
operator|.
name|sayHi
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|strInput
init|=
operator|new
name|String
index|[
literal|2
index|]
decl_stmt|;
name|strInput
index|[
literal|0
index|]
operator|=
literal|"Hello"
expr_stmt|;
name|strInput
index|[
literal|1
index|]
operator|=
literal|"Bonjour"
expr_stmt|;
name|String
index|[]
name|strings
init|=
name|proxy
operator|.
name|getStringArray
argument_list|(
name|strInput
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|strings
operator|.
name|length
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|strings
index|[
literal|0
index|]
argument_list|,
literal|"HelloHello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|strings
index|[
literal|1
index|]
argument_list|,
literal|"BonjourBonjour"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|listInput
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|listInput
operator|.
name|add
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|listInput
operator|.
name|add
argument_list|(
literal|"Bonjour"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|proxy
operator|.
name|getStringList
argument_list|(
name|listInput
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"HelloHello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"BonjourBonjour"
argument_list|)
expr_stmt|;
comment|//now the client side can't unmarshal the complex type without binding types annoutation
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|proxy
operator|.
name|getGreetings
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
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
name|testRpcClient
parameter_list|()
throws|throws
name|Exception
block|{
name|SayHiImpl
name|serviceImpl
init|=
operator|new
name|SayHiImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|serviceImpl
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://mynamespace.com/"
argument_list|,
literal|"SayHiService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://mynamespace.com/"
argument_list|,
literal|"HelloPort"
argument_list|)
decl_stmt|;
comment|// need to set the same bus with service , so use the ServiceImpl
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
operator|(
name|URL
operator|)
literal|null
argument_list|,
name|serviceName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"local://localhost:9090/hello"
argument_list|)
expr_stmt|;
name|SayHi
name|proxy
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|SayHi
operator|.
name|class
argument_list|)
decl_stmt|;
name|long
name|res
init|=
name|proxy
operator|.
name|sayHi
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|res
argument_list|)
expr_stmt|;
name|String
index|[]
name|strInput
init|=
operator|new
name|String
index|[
literal|2
index|]
decl_stmt|;
name|strInput
index|[
literal|0
index|]
operator|=
literal|"Hello"
expr_stmt|;
name|strInput
index|[
literal|1
index|]
operator|=
literal|"Bonjour"
expr_stmt|;
name|String
index|[]
name|strings
init|=
name|proxy
operator|.
name|getStringArray
argument_list|(
name|strInput
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|strings
operator|.
name|length
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|strings
index|[
literal|0
index|]
argument_list|,
literal|"HelloHello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|strings
index|[
literal|1
index|]
argument_list|,
literal|"BonjourBonjour"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArrayAndList
parameter_list|()
throws|throws
name|Exception
block|{
name|ArrayServiceImpl
name|serviceImpl
init|=
operator|new
name|ArrayServiceImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|serviceImpl
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
literal|"local://localhost:9090/array"
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"ArrayService"
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/"
argument_list|,
literal|"ArrayPort"
argument_list|)
decl_stmt|;
comment|// need to set the same bus with service , so use the ServiceImpl
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
operator|(
name|URL
operator|)
literal|null
argument_list|,
name|serviceName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"local://localhost:9090/array"
argument_list|)
expr_stmt|;
name|ArrayService
name|proxy
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|ArrayService
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
index|[]
name|arrayOut
init|=
name|proxy
operator|.
name|arrayOutput
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
operator|.
name|length
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
index|[
literal|0
index|]
argument_list|,
literal|"string1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
index|[
literal|1
index|]
argument_list|,
literal|"string2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
index|[
literal|2
index|]
argument_list|,
literal|"string3"
argument_list|)
expr_stmt|;
name|String
index|[]
name|arrayIn
init|=
operator|new
name|String
index|[
literal|3
index|]
decl_stmt|;
name|arrayIn
index|[
literal|0
index|]
operator|=
literal|"string1"
expr_stmt|;
name|arrayIn
index|[
literal|1
index|]
operator|=
literal|"string2"
expr_stmt|;
name|arrayIn
index|[
literal|2
index|]
operator|=
literal|"string3"
expr_stmt|;
name|assertEquals
argument_list|(
name|proxy
operator|.
name|arrayInput
argument_list|(
name|arrayIn
argument_list|)
argument_list|,
literal|"string1string2string3"
argument_list|)
expr_stmt|;
name|arrayOut
operator|=
name|proxy
operator|.
name|arrayInputAndOutput
argument_list|(
name|arrayIn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
operator|.
name|length
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
index|[
literal|0
index|]
argument_list|,
literal|"string11"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
index|[
literal|1
index|]
argument_list|,
literal|"string22"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|arrayOut
index|[
literal|2
index|]
argument_list|,
literal|"string33"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|listOut
init|=
name|proxy
operator|.
name|listOutput
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|listOut
operator|.
name|size
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|listOut
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"string1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|listOut
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"string2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|listOut
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
literal|"string3"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|listIn
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|listIn
operator|.
name|add
argument_list|(
literal|"list1"
argument_list|)
expr_stmt|;
name|listIn
operator|.
name|add
argument_list|(
literal|"list2"
argument_list|)
expr_stmt|;
name|listIn
operator|.
name|add
argument_list|(
literal|"list3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|proxy
operator|.
name|listInput
argument_list|(
name|listIn
argument_list|)
argument_list|,
literal|"list1list2list3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamespacedWebParamsBare
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://localhost/test"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceClass
argument_list|(
name|FooServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
name|server
argument_list|)
decl_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://namespace3']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://namespace5']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:element[@name='FooEcho2HeaderRequest'][1]"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertInvalid
argument_list|(
literal|"//xsd:element[@name='FooEcho2HeaderRequest'][2]"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamespacedWebParamsWrapped
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsServerFactoryBean
name|sf
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"local://localhost/test"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|FooServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWrapped
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|sf
operator|.
name|create
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|getWSDLDocument
argument_list|(
name|server
argument_list|)
decl_stmt|;
comment|// DOMUtils.writeXml(doc, System.out);
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://namespace3']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|assertValid
argument_list|(
literal|"//xsd:schema[@targetNamespace='http://namespace5']"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

