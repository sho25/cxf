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
name|jbi
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|DeliveryChannel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|MessagingException
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|jbi
operator|.
name|se
operator|.
name|CXFServiceEngine
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
name|cxf
operator|.
name|transport
operator|.
name|ConduitInitiatorManager
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
name|jbi
operator|.
name|JBITransportFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|jbi
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
name|hello_world
operator|.
name|jbi
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
name|hello_world
operator|.
name|jbi
operator|.
name|HelloWorldService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|jbi
operator|.
name|PingMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|components
operator|.
name|util
operator|.
name|ComponentSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|container
operator|.
name|ActivationSpec
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|servicemix
operator|.
name|jbi
operator|.
name|container
operator|.
name|JBIContainer
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
name|Before
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
name|ClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ClientServerTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/jbi"
argument_list|,
literal|"HelloWorldService"
argument_list|)
decl_stmt|;
specifier|private
name|JBIContainer
name|container
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
comment|//assertTrue("server did not launch correctly", launchServer(Server.class));
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|container
operator|=
operator|new
name|JBIContainer
argument_list|()
expr_stmt|;
name|container
operator|.
name|setEmbedded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|container
operator|.
name|init
argument_list|()
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|container
operator|.
name|shutDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJBI
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
literal|"/wsdl/hello_world_jbi.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|wsdl
argument_list|)
expr_stmt|;
comment|//Bus bus = BusFactory.getDefaultBus();
name|createBus
argument_list|()
expr_stmt|;
assert|assert
name|bus
operator|!=
literal|null
assert|;
name|TestComponent
name|component
init|=
operator|new
name|TestComponent
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world/jbi"
argument_list|,
literal|"HelloWorldService"
argument_list|)
argument_list|,
literal|"endpoint"
argument_list|)
decl_stmt|;
name|container
operator|.
name|activateComponent
argument_list|(
operator|new
name|ActivationSpec
argument_list|(
literal|"component"
argument_list|,
name|component
argument_list|)
argument_list|)
expr_stmt|;
name|DeliveryChannel
name|channel
init|=
name|component
operator|.
name|getChannel
argument_list|()
decl_stmt|;
name|JBITransportFactory
name|jbiTransportFactory
init|=
operator|(
name|JBITransportFactory
operator|)
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
operator|.
name|getConduitInitiator
argument_list|(
name|CXFServiceEngine
operator|.
name|JBI_TRANSPORT_ID
argument_list|)
decl_stmt|;
name|jbiTransportFactory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|jbiTransportFactory
operator|.
name|setDeliveryChannel
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|HelloWorldService
name|ss
init|=
operator|new
name|HelloWorldService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|ss
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://foo/bar/baz"
decl_stmt|;
name|EndpointImpl
name|e
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
decl_stmt|;
name|e
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
name|e
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
name|port
operator|.
name|greetMeOneWay
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|String
name|rep
init|=
name|port
operator|.
name|greetMe
argument_list|(
literal|"ffang"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|rep
argument_list|,
literal|"Hello ffang"
argument_list|)
expr_stmt|;
name|rep
operator|=
name|port
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|rep
argument_list|,
literal|"Bonjour"
argument_list|)
expr_stmt|;
try|try
block|{
name|port
operator|.
name|pingMe
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMajor
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ex
operator|.
name|getFaultInfo
argument_list|()
operator|.
name|getMinor
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|TestComponent
extends|extends
name|ComponentSupport
block|{
specifier|public
name|TestComponent
parameter_list|(
name|QName
name|service
parameter_list|,
name|String
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|service
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DeliveryChannel
name|getChannel
parameter_list|()
throws|throws
name|MessagingException
block|{
return|return
name|getContext
argument_list|()
operator|.
name|getDeliveryChannel
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

