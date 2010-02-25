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
name|systest
operator|.
name|handlers
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
name|List
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
name|JAXBContext
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
name|JAXBElement
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
name|soap
operator|.
name|SOAPMessage
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
name|BindingProvider
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
name|MessageContext
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
name|soap
operator|.
name|SOAPMessageContext
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
name|resource
operator|.
name|ResourceManager
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|AddNumbers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|AddNumbersService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|types
operator|.
name|AddNumbersResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|types
operator|.
name|ObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|HandlerInvocationUsingAddNumbersTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/handlers"
argument_list|,
literal|"AddNumbersService"
argument_list|)
decl_stmt|;
specifier|static
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/handlers"
argument_list|,
literal|"AddNumbersPort"
argument_list|)
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|HandlerServer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddHandlerProgrammaticallyClientSide
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/addNumbers.wsdl"
argument_list|)
decl_stmt|;
name|AddNumbersService
name|service
init|=
operator|new
name|AddNumbersService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|AddNumbers
name|port
init|=
operator|(
name|AddNumbers
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmallNumberHandler
name|sh
init|=
operator|new
name|SmallNumberHandler
argument_list|()
decl_stmt|;
name|addHandlersProgrammatically
argument_list|(
operator|(
name|BindingProvider
operator|)
name|port
argument_list|,
name|sh
argument_list|)
expr_stmt|;
name|int
name|result
init|=
name|port
operator|.
name|addNumbers
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|int
name|result1
init|=
name|port
operator|.
name|addNumbers
argument_list|(
literal|5
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|result1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddHandlerByAnnotationClientSide
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/addNumbers.wsdl"
argument_list|)
decl_stmt|;
name|AddNumbersServiceWithAnnotation
name|service
init|=
operator|new
name|AddNumbersServiceWithAnnotation
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|AddNumbers
name|port
init|=
operator|(
name|AddNumbers
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|result
init|=
name|port
operator|.
name|addNumbers
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|int
name|result1
init|=
name|port
operator|.
name|addNumbers
argument_list|(
literal|5
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|result1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvokeFromDispatchWithJAXBPayload
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/addNumbers.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
name|AddNumbersService
name|service
init|=
operator|new
name|AddNumbersService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|JAXBContext
name|jc
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.handlers.types"
argument_list|)
decl_stmt|;
name|Dispatch
argument_list|<
name|Object
argument_list|>
name|disp
init|=
name|service
operator|.
name|createDispatch
argument_list|(
name|portName
argument_list|,
name|jc
argument_list|,
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
argument_list|)
decl_stmt|;
name|SmallNumberHandler
name|sh
init|=
operator|new
name|SmallNumberHandler
argument_list|()
decl_stmt|;
name|TestSOAPHandler
name|soapHandler
init|=
operator|new
name|TestSOAPHandler
argument_list|<
name|SOAPMessageContext
argument_list|>
argument_list|(
literal|false
argument_list|)
block|{
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|ctx
parameter_list|)
block|{
name|super
operator|.
name|handleMessage
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|Boolean
name|outbound
init|=
operator|(
name|Boolean
operator|)
name|ctx
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|outbound
condition|)
block|{
try|try
block|{
name|SOAPMessage
name|msg
init|=
name|ctx
operator|.
name|getMessage
argument_list|()
decl_stmt|;
comment|//System.out.println("aaaaaaaaaaaa");
comment|//msg.writeTo(System.out);
name|assertNotNull
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|addHandlersProgrammatically
argument_list|(
name|disp
argument_list|,
name|sh
argument_list|,
name|soapHandler
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|types
operator|.
name|AddNumbers
name|req
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|handlers
operator|.
name|types
operator|.
name|AddNumbers
argument_list|()
decl_stmt|;
name|req
operator|.
name|setArg0
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|req
operator|.
name|setArg1
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|ObjectFactory
name|factory
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
name|JAXBElement
name|e
init|=
name|factory
operator|.
name|createAddNumbers
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|JAXBElement
name|response
init|=
operator|(
name|JAXBElement
operator|)
name|disp
operator|.
name|invoke
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|AddNumbersResponse
name|value
init|=
operator|(
name|AddNumbersResponse
operator|)
name|response
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|value
operator|.
name|getReturn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandlerPostConstruct
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/addNumbers.wsdl"
argument_list|)
decl_stmt|;
name|AddNumbersServiceWithAnnotation
name|service
init|=
operator|new
name|AddNumbersServiceWithAnnotation
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|AddNumbers
name|port
init|=
operator|(
name|AddNumbers
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerChain
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getHandlerChain
argument_list|()
decl_stmt|;
name|SmallNumberHandler
name|h
init|=
operator|(
name|SmallNumberHandler
operator|)
name|handlerChain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|h
operator|.
name|isPostConstructInvoked
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandlerInjectingResource
parameter_list|()
throws|throws
name|Exception
block|{
comment|//When CXF is deployed in a servlet container, ServletContextResourceResolver is used to resolve
comment|//Servlet context resources.
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|ResourceManager
name|resourceManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resourceManager
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|TestResourceResolver
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl/addNumbers.wsdl"
argument_list|)
decl_stmt|;
name|AddNumbersServiceWithAnnotation
name|service
init|=
operator|new
name|AddNumbersServiceWithAnnotation
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|AddNumbers
name|port
init|=
operator|(
name|AddNumbers
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|AddNumbers
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerChain
init|=
operator|(
operator|(
name|BindingProvider
operator|)
name|port
operator|)
operator|.
name|getBinding
argument_list|()
operator|.
name|getHandlerChain
argument_list|()
decl_stmt|;
name|SmallNumberHandler
name|h
init|=
operator|(
name|SmallNumberHandler
operator|)
name|handlerChain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"injectedValue"
argument_list|,
name|h
operator|.
name|getInjectedString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addHandlersProgrammatically
parameter_list|(
name|BindingProvider
name|bp
parameter_list|,
name|Handler
modifier|...
name|handlers
parameter_list|)
block|{
name|List
argument_list|<
name|Handler
argument_list|>
name|handlerChain
init|=
name|bp
operator|.
name|getBinding
argument_list|()
operator|.
name|getHandlerChain
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|handlerChain
argument_list|)
expr_stmt|;
for|for
control|(
name|Handler
name|h
range|:
name|handlers
control|)
block|{
name|handlerChain
operator|.
name|add
argument_list|(
name|h
argument_list|)
expr_stmt|;
block|}
name|bp
operator|.
name|getBinding
argument_list|()
operator|.
name|setHandlerChain
argument_list|(
name|handlerChain
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

