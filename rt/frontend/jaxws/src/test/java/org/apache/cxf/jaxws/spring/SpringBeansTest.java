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
name|spring
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
name|PrintWriter
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
name|List
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
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|anonymous_complex_type
operator|.
name|AnonymousComplexType
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
name|anonymous_complex_type
operator|.
name|SplitNameResponse
operator|.
name|Names
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
name|BindingConfiguration
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
name|Soap12
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
name|SoapBindingConfiguration
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
name|saaj
operator|.
name|SAAJInInterceptor
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
name|saaj
operator|.
name|SAAJOutInterceptor
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
name|source
operator|.
name|SourceDataBinding
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
name|Client
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
name|NullConduitSelector
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
name|ClientProxy
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
name|Interceptor
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
name|EndpointImpl
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
name|JaxWsProxyFactoryBean
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
name|transport
operator|.
name|http
operator|.
name|WSDLQueryHandler
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
name|Greeter
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
specifier|public
class|class
name|SpringBeansTest
extends|extends
name|Assert
block|{
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/jaxws/spring/endpoints.xml"
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|EndpointImpl
name|ep
init|=
operator|(
name|EndpointImpl
operator|)
name|bean
decl_stmt|;
name|assertNotNull
argument_list|(
name|ep
operator|.
name|getImplementor
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
operator|.
name|getServer
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"simpleWithAddress"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|ep
operator|.
name|getImplementor
argument_list|()
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"can't get the right implementor object"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"http://localhost:8080/simpleWithAddress"
argument_list|,
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineImplementor"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|ep
operator|.
name|getImplementor
argument_list|()
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"can't get the right implementor object"
argument_list|)
expr_stmt|;
block|}
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
name|impl
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
operator|)
name|ep
operator|.
name|getImplementor
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The property was not injected rightly"
argument_list|,
name|impl
operator|.
name|getPrefix
argument_list|()
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
operator|.
name|getServer
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineInvoker"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|.
name|getInvoker
argument_list|()
operator|instanceof
name|NullInvoker
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getInvoker
argument_list|()
operator|instanceof
name|NullInvoker
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"simpleWithBindingUri"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|assertEquals
argument_list|(
literal|"get the wrong bindingId"
argument_list|,
name|ep
operator|.
name|getBindingUri
argument_list|()
argument_list|,
literal|"http://cxf.apache.org/bindings/xformat"
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"simpleWithBinding"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|BindingConfiguration
name|bc
init|=
name|ep
operator|.
name|getBindingConfig
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|bc
operator|instanceof
name|SoapBindingConfiguration
argument_list|)
expr_stmt|;
name|SoapBindingConfiguration
name|sbc
init|=
operator|(
name|SoapBindingConfiguration
operator|)
name|bc
decl_stmt|;
name|assertTrue
argument_list|(
name|sbc
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"the soap configure should set isMtomEnabled to be true"
argument_list|,
name|sbc
operator|.
name|isMtomEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"implementorClass"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|assertEquals
argument_list|(
name|Hello
operator|.
name|class
argument_list|,
name|ep
operator|.
name|getImplementorClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|.
name|getImplementor
argument_list|()
operator|.
name|getClass
argument_list|()
operator|==
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"epWithProps"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|ep
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"classImpl"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|assertTrue
argument_list|(
name|ep
operator|.
name|getImplementor
argument_list|()
operator|instanceof
name|Hello
argument_list|)
expr_stmt|;
name|QName
name|name
init|=
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/service"
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HelloServiceCustomized"
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|=
name|ep
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://service.jaxws.cxf.apache.org/endpoint"
argument_list|,
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HelloEndpointCustomized"
argument_list|,
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"wsdlLocation"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"publishedEndpointUrl"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|String
name|expectedEndpointUrl
init|=
literal|"http://cxf.apache.org/Greeter"
decl_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|bean
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedEndpointUrl
argument_list|,
name|ep
operator|.
name|getPublishedEndpointUrl
argument_list|()
argument_list|)
expr_stmt|;
comment|// test for existence of Endpoint without an id element
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|String
index|[]
name|names
init|=
name|ctx
operator|.
name|getBeanNamesForType
argument_list|(
name|EndpointImpl
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|n
range|:
name|names
control|)
block|{
if|if
condition|(
name|n
operator|.
name|startsWith
argument_list|(
name|EndpointImpl
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Could not find server factory with autogenerated id"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|testInterceptors
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testNamespaceMapping
parameter_list|(
name|ApplicationContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|AnonymousComplexType
name|act
init|=
operator|(
name|AnonymousComplexType
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"bookClient"
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|act
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|act
argument_list|)
expr_stmt|;
name|StringWriter
name|logWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|logWriter
argument_list|)
decl_stmt|;
name|LoggingInInterceptor
name|spy
init|=
operator|new
name|LoggingInInterceptor
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|spy
argument_list|)
expr_stmt|;
name|Names
name|n
init|=
name|act
operator|.
name|splitName
argument_list|(
literal|"Hello There"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|n
operator|.
name|getFirst
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|logWriter
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"BeepBeep:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testInterceptors
parameter_list|(
name|ClassPathXmlApplicationContext
name|ctx
parameter_list|)
block|{
name|EndpointImpl
name|ep
decl_stmt|;
name|ep
operator|=
operator|(
name|EndpointImpl
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"epWithInterceptors"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ep
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|inInterceptors
init|=
name|ep
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|boolean
name|saaj
init|=
literal|false
decl_stmt|;
name|boolean
name|logging
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|i
range|:
name|inInterceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|SAAJInInterceptor
condition|)
block|{
name|saaj
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|instanceof
name|LoggingInInterceptor
condition|)
block|{
name|logging
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|saaj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|logging
argument_list|)
expr_stmt|;
name|saaj
operator|=
literal|false
expr_stmt|;
name|logging
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|i
range|:
name|ep
operator|.
name|getOutInterceptors
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|SAAJOutInterceptor
condition|)
block|{
name|saaj
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|instanceof
name|LoggingOutInterceptor
condition|)
block|{
name|logging
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|saaj
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServers
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/jaxws/spring/servers.xml"
block|}
argument_list|)
decl_stmt|;
name|JaxWsServerFactoryBean
name|bean
decl_stmt|;
name|BindingConfiguration
name|bc
decl_stmt|;
name|SoapBindingConfiguration
name|sbc
decl_stmt|;
name|bean
operator|=
operator|(
name|JaxWsServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineSoapBindingRPC"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|bc
operator|=
name|bean
operator|.
name|getBindingConfig
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|bc
operator|instanceof
name|SoapBindingConfiguration
argument_list|)
expr_stmt|;
name|sbc
operator|=
operator|(
name|SoapBindingConfiguration
operator|)
name|bc
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rpc"
argument_list|,
name|sbc
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|WSDLQueryHandler
name|handler
init|=
operator|new
name|WSDLQueryHandler
argument_list|(
operator|(
name|Bus
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bout
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|handler
operator|.
name|writeResponse
argument_list|(
literal|"http://localhost/test?wsdl"
argument_list|,
literal|"/test"
argument_list|,
name|bean
operator|.
name|create
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
name|bout
argument_list|)
expr_stmt|;
name|String
name|wsdl
init|=
name|bout
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|wsdl
operator|.
name|contains
argument_list|(
literal|"name=\"stringArray\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|wsdl
operator|.
name|contains
argument_list|(
literal|"name=\"stringArray\""
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|=
operator|(
name|JaxWsServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"simple"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|bean
operator|=
operator|(
name|JaxWsServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineSoapBinding"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|bc
operator|=
name|bean
operator|.
name|getBindingConfig
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|bc
operator|instanceof
name|SoapBindingConfiguration
argument_list|)
expr_stmt|;
name|sbc
operator|=
operator|(
name|SoapBindingConfiguration
operator|)
name|bc
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Not soap version 1.2: "
operator|+
name|sbc
operator|.
name|getVersion
argument_list|()
argument_list|,
name|sbc
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
argument_list|)
expr_stmt|;
name|bean
operator|=
operator|(
name|JaxWsServerFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineDataBinding"
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|String
index|[]
name|names
init|=
name|ctx
operator|.
name|getBeanNamesForType
argument_list|(
name|JaxWsServerFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|n
range|:
name|names
control|)
block|{
if|if
condition|(
name|n
operator|.
name|startsWith
argument_list|(
name|JaxWsServerFactoryBean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Could not find server factory with autogenerated id"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|testNamespaceMapping
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClients
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/org/apache/cxf/jaxws/spring/clients.xml"
block|}
argument_list|)
decl_stmt|;
name|Object
name|bean
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"client1.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
operator|(
name|Greeter
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"client1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|greeter
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected ConduitSelector"
argument_list|,
name|client
operator|.
name|getConduitSelector
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected ConduitSelector"
argument_list|,
name|client
operator|.
name|getConduitSelector
argument_list|()
operator|instanceof
name|NullConduitSelector
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|inInterceptors
init|=
name|client
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|boolean
name|saaj
init|=
literal|false
decl_stmt|;
name|boolean
name|logging
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|i
range|:
name|inInterceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|SAAJInInterceptor
condition|)
block|{
name|saaj
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|instanceof
name|LoggingInInterceptor
condition|)
block|{
name|logging
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|saaj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|logging
argument_list|)
expr_stmt|;
name|saaj
operator|=
literal|false
expr_stmt|;
name|logging
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|i
range|:
name|client
operator|.
name|getOutInterceptors
argument_list|()
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|SAAJOutInterceptor
condition|)
block|{
name|saaj
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|instanceof
name|LoggingOutInterceptor
condition|)
block|{
name|logging
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
name|saaj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|logging
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|instanceof
name|SourceDataBinding
argument_list|)
expr_stmt|;
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|(
name|JaxWsProxyFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"wsdlLocation.proxyFactory"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|String
name|wsdlLocation
init|=
name|factory
operator|.
name|getWsdlLocation
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the right wsdl location"
argument_list|,
name|wsdlLocation
argument_list|,
literal|"wsdl/hello_world.wsdl"
argument_list|)
expr_stmt|;
name|factory
operator|=
operator|(
name|JaxWsProxyFactoryBean
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"inlineSoapBinding.proxyFactory"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|BindingConfiguration
name|bc
init|=
name|factory
operator|.
name|getBindingConfig
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|bc
operator|instanceof
name|SoapBindingConfiguration
argument_list|)
expr_stmt|;
name|SoapBindingConfiguration
name|sbc
init|=
operator|(
name|SoapBindingConfiguration
operator|)
name|bc
decl_stmt|;
name|assertTrue
argument_list|(
name|sbc
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"the soap configure should set isMtomEnabled to be true"
argument_list|,
name|sbc
operator|.
name|isMtomEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

