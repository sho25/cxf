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
name|management
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceNotFoundException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|management
operator|.
name|InstrumentationManager
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
name|management
operator|.
name|ManagementConstants
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
name|management
operator|.
name|jmx
operator|.
name|InstrumentationManagerImpl
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|AfterClass
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
name|ManagedClientServerTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMX_PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SoapPort"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|bf
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/management/managed-spring.xml"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|shutdownBus
parameter_list|()
throws|throws
name|Exception
block|{
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
operator|.
name|shutdown
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManagedEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|SpringBusFactory
operator|.
name|getDefaultBus
argument_list|()
decl_stmt|;
name|InstrumentationManager
name|im
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|im
argument_list|)
expr_stmt|;
name|InstrumentationManagerImpl
name|impl
init|=
operator|(
name|InstrumentationManagerImpl
operator|)
name|im
decl_stmt|;
name|assertTrue
argument_list|(
name|impl
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|impl
operator|.
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
name|MBeanServer
name|mbs
init|=
name|im
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
operator|new
name|ObjectName
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":type=Bus.Service.Endpoint,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|?
argument_list|>
name|s
init|=
name|mbs
operator|.
name|queryNames
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|=
operator|(
name|ObjectName
operator|)
name|s
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|Object
name|val
init|=
name|mbs
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"getState"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Service should have been started."
argument_list|,
literal|"STARTED"
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|portName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|updateAddressPort
argument_list|(
name|greeter
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
name|String
name|response
init|=
operator|new
name|String
argument_list|(
literal|"Bonjour"
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|mbs
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"stop"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|val
operator|=
name|mbs
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"State"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Service should have been stopped."
argument_list|,
literal|"STOPPED"
argument_list|,
name|val
argument_list|)
expr_stmt|;
try|try
block|{
name|reply
operator|=
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Endpoint should not be active at this point."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//Expected
block|}
name|mbs
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"start"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|val
operator|=
name|mbs
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"getState"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Service should have been started."
argument_list|,
literal|"STARTED"
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|reply
operator|=
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"no response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|response
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|mbs
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"destroy"
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
try|try
block|{
name|mbs
operator|.
name|getMBeanInfo
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"destroy failed to unregister MBean."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstanceNotFoundException
name|e
parameter_list|)
block|{
comment|// Expected
block|}
block|}
block|}
end_class

end_unit

