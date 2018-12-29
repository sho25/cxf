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
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|binding
operator|.
name|soap
operator|.
name|SoapBindingFactory
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
name|SoapTransportFactory
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
name|bus
operator|.
name|CXFBusFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|configuration
operator|.
name|Configurer
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
name|interceptor
operator|.
name|Fault
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
name|jaxws
operator|.
name|support
operator|.
name|JaxWsEndpointImpl
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
name|message
operator|.
name|Message
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|Greeter
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
name|hello_world_soap_http
operator|.
name|SOAPService
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
name|ConfiguredEndpointTest
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
specifier|private
name|BusFactory
name|factory
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|clearProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXFDefaultClientEndpoint
parameter_list|()
block|{
name|factory
operator|=
operator|new
name|CXFBusFactory
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|CXFBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doTestDefaultClientEndpoint
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpringDefaultClientEndpoint
parameter_list|()
block|{
name|factory
operator|=
operator|new
name|SpringBusFactory
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|SpringBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doTestDefaultClientEndpoint
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|doTestDefaultClientEndpoint
parameter_list|()
block|{
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_NAME
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|JaxWsClientProxy
name|eih
init|=
operator|(
name|JaxWsClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|eih
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|JaxWsEndpointImpl
name|endpoint
init|=
operator|(
name|JaxWsEndpointImpl
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name"
argument_list|,
name|PORT_NAME
operator|.
name|toString
argument_list|()
operator|+
literal|".endpoint"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected value for property validating"
argument_list|,
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|get
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// System.out.println("endpoint interceptors");
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|printInterceptors
argument_list|(
literal|"in"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"out"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"inFault"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"outFault"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
comment|// System.out.println("service interceptors");
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
name|svc
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name"
argument_list|,
name|SERVICE_NAME
operator|.
name|toString
argument_list|()
argument_list|,
name|svc
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"in"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"out"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"inFault"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|printInterceptors
argument_list|(
literal|"outFault"
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpringConfiguredClientEndpoint
parameter_list|()
block|{
name|SpringBusFactory
name|sf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|factory
operator|=
name|sf
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|sf
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/jaxws/configured-endpoints.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|SpringBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|doTestConfiguredClientEndpoint
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|doTestConfiguredClientEndpoint
parameter_list|()
block|{
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_NAME
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|JaxWsClientProxy
name|eih
init|=
operator|(
name|JaxWsClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|eih
operator|.
name|getClient
argument_list|()
decl_stmt|;
name|JaxWsEndpointImpl
name|endpoint
init|=
operator|(
name|JaxWsEndpointImpl
operator|)
name|client
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
name|svc
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name."
argument_list|,
name|SERVICE_NAME
operator|.
name|toString
argument_list|()
argument_list|,
name|svc
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|svc
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors: "
operator|+
name|interceptors
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-in"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors: "
operator|+
name|interceptors
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-out"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors: "
operator|+
name|interceptors
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-in-fault"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors: "
operator|+
name|interceptors
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-out-fault"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXFDefaultServerEndpoint
parameter_list|()
block|{
name|factory
operator|=
operator|new
name|CXFBusFactory
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|CXFBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|initializeBus
argument_list|()
expr_stmt|;
name|doTestDefaultServerEndpoint
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpringDefaultServerEndpoint
parameter_list|()
block|{
name|factory
operator|=
operator|new
name|SpringBusFactory
argument_list|()
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|SpringBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|initializeBus
argument_list|()
expr_stmt|;
name|doTestDefaultServerEndpoint
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|doTestDefaultServerEndpoint
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ei
init|=
call|(
name|EndpointImpl
call|)
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
argument_list|)
decl_stmt|;
name|ei
operator|.
name|publish
argument_list|(
literal|"http://localhost/greeter"
argument_list|)
expr_stmt|;
name|JaxWsEndpointImpl
name|endpoint
init|=
operator|(
name|JaxWsEndpointImpl
operator|)
name|ei
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name"
argument_list|,
name|PORT_NAME
operator|.
name|toString
argument_list|()
operator|+
literal|".endpoint"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected value for property validating"
argument_list|,
operator|!
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|get
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
name|svc
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name"
argument_list|,
name|SERVICE_NAME
operator|.
name|toString
argument_list|()
argument_list|,
name|svc
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected test interceptor"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|xtestCXFConfiguredServerEndpoint
parameter_list|()
block|{
name|CXFBusFactory
name|cf
init|=
operator|new
name|CXFBusFactory
argument_list|()
decl_stmt|;
name|factory
operator|=
name|cf
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_NAME
argument_list|,
literal|"org/apache/cxf/jaxws/configured-endpoints.xml"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|cf
operator|.
name|createBus
argument_list|(
literal|null
argument_list|,
name|properties
argument_list|)
argument_list|)
expr_stmt|;
name|initializeBus
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|CXFBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//doTestConfiguredServerEndpoint();
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSpringConfiguredServerEndpoint
parameter_list|()
block|{
name|doTestConfiguredServerEndpoint
argument_list|(
literal|"true"
argument_list|,
literal|"org/apache/cxf/jaxws/configured-endpoints.xml"
argument_list|)
expr_stmt|;
name|doTestConfiguredServerEndpoint
argument_list|(
literal|"BOTH"
argument_list|,
literal|"org/apache/cxf/jaxws/schemavalidationtype-configured-endpoints.xml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestConfiguredServerEndpoint
parameter_list|(
name|Object
name|expectedValidionValue
parameter_list|,
name|String
name|configFile
parameter_list|)
block|{
name|SpringBusFactory
name|sf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|factory
operator|=
name|sf
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|sf
operator|.
name|createBus
argument_list|(
name|configFile
argument_list|)
argument_list|)
expr_stmt|;
name|initializeBus
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|BusFactory
operator|.
name|BUS_FACTORY_PROPERTY_NAME
argument_list|,
name|SpringBusFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|EndpointImpl
name|ei
init|=
call|(
name|EndpointImpl
call|)
argument_list|(
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
argument_list|)
decl_stmt|;
name|ei
operator|.
name|publish
argument_list|(
literal|"http://localhost/greeter"
argument_list|)
expr_stmt|;
name|JaxWsEndpointImpl
name|endpoint
init|=
operator|(
name|JaxWsEndpointImpl
operator|)
name|ei
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name"
argument_list|,
name|PORT_NAME
operator|.
name|toString
argument_list|()
operator|+
literal|".endpoint"
argument_list|,
name|endpoint
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected value for property validating"
argument_list|,
name|expectedValidionValue
argument_list|,
name|ei
operator|.
name|getProperties
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|5
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"endpoint-in"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|5
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"endpoint-out"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|2
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"endpoint-in-fault"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|endpoint
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|2
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"endpoint-out-fault"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
name|svc
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|ServiceImpl
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected bean name."
argument_list|,
name|SERVICE_NAME
operator|.
name|toString
argument_list|()
argument_list|,
name|svc
operator|.
name|getBeanName
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-in"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-out"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-in-fault"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|svc
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors."
argument_list|,
literal|1
argument_list|,
name|interceptors
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor id."
argument_list|,
literal|"service-out-fault"
argument_list|,
name|findTestInterceptor
argument_list|(
name|interceptors
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initializeBus
parameter_list|()
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|SoapBindingFactory
name|bindingFactory
init|=
operator|new
name|SoapBindingFactory
argument_list|()
decl_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
operator|.
name|registerBindingFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|bindingFactory
argument_list|)
expr_stmt|;
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|SoapTransportFactory
name|soapDF
init|=
operator|new
name|SoapTransportFactory
argument_list|()
decl_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
name|soapDF
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
name|soapDF
argument_list|)
expr_stmt|;
name|LocalTransportFactory
name|localTransport
init|=
operator|new
name|LocalTransportFactory
argument_list|()
decl_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/http"
argument_list|,
name|localTransport
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TestInterceptor
name|findTestInterceptor
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|)
block|{
for|for
control|(
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|i
operator|instanceof
name|TestInterceptor
condition|)
block|{
return|return
operator|(
name|TestInterceptor
operator|)
name|i
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|printInterceptors
parameter_list|(
name|String
name|type
parameter_list|,
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
parameter_list|)
block|{
comment|//for (Interceptor i : interceptors) {
comment|//System.out.println("    " + type + ": " + i.getClass().getName());
comment|//}
block|}
specifier|static
specifier|final
class|class
name|TestInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
name|TestInterceptor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{         }
block|}
block|}
end_class

end_unit

