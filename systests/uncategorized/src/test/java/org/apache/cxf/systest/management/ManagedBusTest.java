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
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
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
name|Set
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
name|helpers
operator|.
name|CastUtils
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
name|TestUtil
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|assertFalse
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
name|ManagedBusTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|JMX_PORT1
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|ManagedBusTest
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JMX_PORT2
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|ManagedBusTest
operator|.
name|class
argument_list|,
literal|3
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|ManagedBusTest
operator|.
name|class
argument_list|,
literal|4
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testTwoSameNamedEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|()
decl_stmt|;
try|try
block|{
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
name|imi
init|=
operator|(
name|InstrumentationManagerImpl
operator|)
name|im
decl_stmt|;
name|imi
operator|.
name|setServer
argument_list|(
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
name|imi
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|imi
operator|.
name|init
argument_list|()
expr_stmt|;
name|Greeter
name|greeter1
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|JaxWsServerFactoryBean
name|svrFactory
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|SERVICE_PORT
operator|+
literal|"/Hello"
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
name|greeter1
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"managed.endpoint.name"
argument_list|,
literal|"greeter1"
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|create
argument_list|()
expr_stmt|;
name|Greeter
name|greeter2
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|svrFactory
operator|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
expr_stmt|;
name|svrFactory
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|SERVICE_PORT
operator|+
literal|"/Hello2"
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|setServiceBean
argument_list|(
name|greeter2
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"managed.endpoint.name"
argument_list|,
literal|"greeter2"
argument_list|)
expr_stmt|;
name|svrFactory
operator|.
name|create
argument_list|()
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
name|queryMBeans
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|s
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
name|Test
specifier|public
name|void
name|testManagedSpringBus
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
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
name|imi
init|=
operator|(
name|InstrumentationManagerImpl
operator|)
name|im
decl_stmt|;
name|assertEquals
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:9913/jmxrmi"
argument_list|,
name|imi
operator|.
name|getJMXServiceURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|imi
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|imi
operator|.
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
comment|//Test that registering without an MBeanServer is a no-op
name|im
operator|.
name|register
argument_list|(
name|imi
argument_list|,
operator|new
name|ObjectName
argument_list|(
literal|"org.apache.cxf:foo=bar"
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManagedBusWithTransientId
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/management/managed-bus.xml"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|doManagedBusTest
argument_list|(
name|bus
argument_list|,
name|bus
operator|.
name|getId
argument_list|()
argument_list|,
literal|"cxf_managed_bus_test"
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|JMX_PORT1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testManagedBusWithPersistentId
parameter_list|()
throws|throws
name|Exception
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/management/persistent-id.xml"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|doManagedBusTest
argument_list|(
name|bus
argument_list|,
literal|"cxf_managed_bus_test"
argument_list|,
name|bus
operator|.
name|getId
argument_list|()
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|JMX_PORT2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doManagedBusTest
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|String
name|expect
parameter_list|,
name|String
name|reject
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
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
name|imi
init|=
operator|(
name|InstrumentationManagerImpl
operator|)
name|im
decl_stmt|;
name|assertEquals
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:"
operator|+
name|port
operator|+
literal|"/jmxrmi"
argument_list|,
name|imi
operator|.
name|getJMXServiceURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|imi
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|imi
operator|.
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
name|WorkQueueManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
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
literal|":type=WorkQueueManager,*"
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
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjectName
name|o
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|s
argument_list|,
name|ObjectName
operator|.
name|class
argument_list|)
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected "
operator|+
name|expect
operator|+
literal|" in object name: "
operator|+
name|o
argument_list|,
name|o
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"bus.id="
operator|+
name|expect
operator|+
literal|","
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected "
operator|+
name|reject
operator|+
literal|" in object name: "
operator|+
name|o
argument_list|,
name|o
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"bus.id="
operator|+
name|reject
operator|+
literal|","
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Size is wrong: "
operator|+
name|b
operator|.
name|toString
argument_list|()
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|manager
operator|.
name|getNamedWorkQueue
argument_list|(
literal|"testQueue"
argument_list|)
argument_list|)
expr_stmt|;
name|manager
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
name|name
operator|=
operator|new
name|ObjectName
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":type=WorkQueues,*"
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbs
operator|.
name|queryNames
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|b
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
for|for
control|(
name|ObjectName
name|o
range|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|s
argument_list|,
name|ObjectName
operator|.
name|class
argument_list|)
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"expected "
operator|+
name|expect
operator|+
literal|" in object name: "
operator|+
name|o
argument_list|,
name|o
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"bus.id="
operator|+
name|expect
operator|+
literal|","
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected "
operator|+
name|reject
operator|+
literal|" in object name: "
operator|+
name|o
argument_list|,
name|o
operator|.
name|toString
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"bus.id="
operator|+
name|reject
operator|+
literal|","
argument_list|)
operator|==
operator|-
literal|1
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Size is wrong: "
operator|+
name|b
operator|.
name|toString
argument_list|()
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|s
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjectName
name|n
init|=
operator|(
name|ObjectName
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Long
name|result
init|=
operator|(
name|Long
operator|)
name|mbs
operator|.
name|invoke
argument_list|(
name|n
argument_list|,
literal|"getWorkQueueMaxSize"
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
name|result
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
literal|256
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|hwm
init|=
operator|(
name|Integer
operator|)
name|mbs
operator|.
name|invoke
argument_list|(
name|n
argument_list|,
literal|"getHighWaterMark"
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
if|if
condition|(
name|n
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"testQueue"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|hwm
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|50
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|hwm
argument_list|,
name|Integer
operator|.
name|valueOf
argument_list|(
literal|25
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|name
operator|=
operator|new
name|ObjectName
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":type=Bus,*"
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbs
operator|.
name|queryNames
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|it
operator|=
name|s
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjectName
name|n
init|=
operator|(
name|ObjectName
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Object
index|[]
name|params
init|=
block|{
name|Boolean
operator|.
name|FALSE
block|}
decl_stmt|;
name|String
index|[]
name|sig
init|=
block|{
literal|"boolean"
block|}
decl_stmt|;
name|mbs
operator|.
name|invoke
argument_list|(
name|n
argument_list|,
literal|"shutdown"
argument_list|,
name|params
argument_list|,
name|sig
argument_list|)
expr_stmt|;
block|}
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

