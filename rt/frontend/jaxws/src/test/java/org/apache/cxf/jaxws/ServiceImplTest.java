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
name|Field
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|Dispatch
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
name|Service
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
name|handler
operator|.
name|Handler
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
name|handler
operator|.
name|HandlerResolver
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
name|handler
operator|.
name|PortInfo
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
name|BusException
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
name|calculator
operator|.
name|CalculatorPortType
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
name|configuration
operator|.
name|spring
operator|.
name|ConfigurerImpl
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|JAXBDataBinding
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
name|handler
operator|.
name|PortInfoImpl
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
name|SOAPService
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
name|ServiceImplTest
extends|extends
name|AbstractJaxWsTest
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_1
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/calculator"
argument_list|,
literal|"CalculatorService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_1
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/calculator"
argument_list|,
literal|"CalculatorPort"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SOAP_PORT
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
specifier|static
specifier|final
name|QName
name|SOAP_PORT1
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort1"
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|isJAXWSClientFactoryConfigured
decl_stmt|;
specifier|private
name|boolean
name|isClientProxyFactoryBeanConfigured
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testAddPort
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|sName
init|=
operator|new
name|QName
argument_list|(
literal|"service"
argument_list|)
decl_stmt|;
name|QName
name|pName
init|=
operator|new
name|QName
argument_list|(
literal|"port"
argument_list|)
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|sName
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|pName
argument_list|,
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|,
literal|"http://mysite.org/test"
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|QName
argument_list|>
name|ports
init|=
name|service
operator|.
name|getPorts
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|ports
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceImpl
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|Greeter
name|proxy
init|=
name|service
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|proxy
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonSpecificGetPort
parameter_list|()
throws|throws
name|Exception
block|{
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|Greeter
name|proxy
init|=
name|service
operator|.
name|getPort
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|proxy
argument_list|)
decl_stmt|;
name|boolean
name|boolA
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|SOAP_PORT
argument_list|)
decl_stmt|;
name|boolean
name|boolB
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|SOAP_PORT1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|boolA
operator|||
name|boolB
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Override
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
return|return
name|bf
operator|.
name|createBus
argument_list|(
literal|"/org/apache/cxf/jaxws/soapServiceConfig.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadServiceName
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|QName
name|badService
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/calculator"
argument_list|,
literal|"DoesNotExist"
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|badService
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
comment|// that's expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPorts
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|QName
argument_list|>
name|iter
init|=
name|service
operator|.
name|getPorts
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|iter
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PORT_1
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBadPort
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|badPort
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/calculator"
argument_list|,
literal|"PortDoesNotExist"
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|getPort
argument_list|(
name|badPort
argument_list|,
name|CalculatorPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
comment|// that's ok
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBadSEI
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|getPort
argument_list|(
name|ServiceImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw expected exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
comment|// that's ok
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetGoodPort
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|CalculatorPortType
name|cal
init|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_1
argument_list|,
name|CalculatorPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cal
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJAXBCachedOnRepeatGetPort
parameter_list|()
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|CalculatorPortType
name|cal1
init|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_1
argument_list|,
name|CalculatorPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cal1
argument_list|)
expr_stmt|;
name|ClientProxy
name|cp
init|=
operator|(
name|ClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|cal1
argument_list|)
decl_stmt|;
name|JAXBDataBinding
name|db1
init|=
operator|(
name|JAXBDataBinding
operator|)
name|cp
operator|.
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|db1
argument_list|)
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|CalculatorPortType
name|cal2
init|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_1
argument_list|,
name|CalculatorPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cal2
argument_list|)
expr_stmt|;
name|cp
operator|=
operator|(
name|ClientProxy
operator|)
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|cal2
argument_list|)
expr_stmt|;
name|JAXBDataBinding
name|db2
init|=
operator|(
name|JAXBDataBinding
operator|)
name|cp
operator|.
name|getClient
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|db2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"got cached JAXBContext"
argument_list|,
name|db1
operator|.
name|getContext
argument_list|()
argument_list|,
name|db2
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateDispatchGoodPort
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|Source
argument_list|>
name|dispatch
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|PORT_1
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dispatch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateDispatchBadPort
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|badPort
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/cxf/calculator"
argument_list|,
literal|"PortDoesNotExist"
argument_list|)
decl_stmt|;
try|try
block|{
name|service
operator|.
name|createDispatch
argument_list|(
name|badPort
argument_list|,
name|Source
operator|.
name|class
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
comment|// that's ok
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandlerResolver
parameter_list|()
block|{
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl1
argument_list|)
expr_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|TestHandlerResolver
name|resolver
init|=
operator|new
name|TestHandlerResolver
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|resolver
operator|.
name|getPortInfo
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|.
name|setHandlerResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|CalculatorPortType
name|cal
init|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_1
argument_list|,
name|CalculatorPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cal
argument_list|)
expr_stmt|;
name|PortInfo
name|info
init|=
name|resolver
operator|.
name|getPortInfo
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SERVICE_1
argument_list|,
name|info
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PORT_1
argument_list|,
name|info
operator|.
name|getPortName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|,
name|info
operator|.
name|getBindingID
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|TestHandlerResolver
implements|implements
name|HandlerResolver
block|{
specifier|private
name|PortInfo
name|info
decl_stmt|;
specifier|public
name|PortInfo
name|getPortInfo
parameter_list|()
block|{
return|return
name|info
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|List
argument_list|<
name|Handler
argument_list|>
name|getHandlerChain
parameter_list|(
name|PortInfo
name|portInfo
parameter_list|)
block|{
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|this
operator|.
name|info
operator|=
name|portInfo
expr_stmt|;
return|return
name|handlerList
return|;
block|}
block|}
annotation|@
name|Test
comment|//CXF-2723 :Allow configuration of JaxWsClientFactoryBean during port creation
specifier|public
name|void
name|testConfigureBean
parameter_list|()
throws|throws
name|Exception
block|{
name|Configurer
name|oldConfiguer
init|=
name|this
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
name|JAXWSClientFactoryCongfiguer
name|clientConfiguer
init|=
operator|new
name|JAXWSClientFactoryCongfiguer
argument_list|()
decl_stmt|;
name|getBus
argument_list|()
operator|.
name|setExtension
argument_list|(
name|clientConfiguer
argument_list|,
name|Configurer
operator|.
name|class
argument_list|)
expr_stmt|;
name|URL
name|wsdl1
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/calculator.wsdl"
argument_list|)
decl_stmt|;
name|ServiceImpl
name|service
init|=
operator|new
name|ServiceImpl
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|wsdl1
argument_list|,
name|SERVICE_1
argument_list|,
name|ServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
name|service
operator|.
name|createPort
argument_list|(
name|PORT_1
argument_list|,
literal|null
argument_list|,
name|CalculatorPortType
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The JAXWSClientFcatoryBean is not configured by the new configurer"
argument_list|,
name|isJAXWSClientFactoryConfigured
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The ClientProxyFcatoryBean is not configured by the new configurer"
argument_list|,
name|isClientProxyFactoryBeanConfigured
argument_list|)
expr_stmt|;
name|getBus
argument_list|()
operator|.
name|setExtension
argument_list|(
name|oldConfiguer
argument_list|,
name|Configurer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// CXF-2819:<protocl-bindings>##SOAP12_Binding<protocl-bindings> in handler chain is not correctly
comment|// matched
specifier|public
name|void
name|testGetSOAP12BindingIDFromWSDL
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap12_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
name|URL
name|wsdlURL
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/hello_world_soap12.wsdl"
argument_list|)
decl_stmt|;
name|ServiceImpl
name|seviceImpl
init|=
operator|new
name|ServiceImpl
argument_list|(
name|this
operator|.
name|getBus
argument_list|()
argument_list|,
name|wsdlURL
argument_list|,
name|serviceName
argument_list|,
name|org
operator|.
name|apache
operator|.
name|hello_world_soap12_http
operator|.
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|Field
name|f
init|=
name|seviceImpl
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"portInfos"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|PortInfoImpl
argument_list|>
name|portInfoMap
init|=
operator|(
name|Map
argument_list|<
name|QName
argument_list|,
name|PortInfoImpl
argument_list|>
operator|)
name|f
operator|.
name|get
argument_list|(
name|seviceImpl
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|portInfoMap
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getBindingID
argument_list|()
argument_list|,
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
argument_list|)
expr_stmt|;
block|}
class|class
name|JAXWSClientFactoryCongfiguer
extends|extends
name|ConfigurerImpl
block|{
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|configureBean
parameter_list|(
name|String
name|bn
parameter_list|,
name|Object
name|beanInstance
parameter_list|,
name|boolean
name|checkWildcards
parameter_list|)
block|{
if|if
condition|(
name|beanInstance
operator|instanceof
name|JaxWsClientFactoryBean
condition|)
block|{
name|isJAXWSClientFactoryConfigured
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|beanInstance
operator|instanceof
name|ClientProxyFactoryBean
condition|)
block|{
name|isClientProxyFactoryBeanConfigured
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

