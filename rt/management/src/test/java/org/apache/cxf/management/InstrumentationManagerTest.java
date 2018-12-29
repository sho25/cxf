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
name|management
package|;
end_package

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
name|managers
operator|.
name|WorkQueueManagerImpl
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
name|counters
operator|.
name|CounterRepository
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|Before
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
name|InstrumentationManagerTest
block|{
name|InstrumentationManager
name|im
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{      }
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|//test case had done the bus.shutdown
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
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
name|testInstrumentationNotEnabled
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|()
expr_stmt|;
name|im
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Instrumentation Manager should not be null"
argument_list|,
name|im
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
name|assertNull
argument_list|(
literal|"MBeanServer should not be available."
argument_list|,
name|mbs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstrumentationEnabledSetBeforeBusSet
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"managed-spring3.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|im
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Instrumentation Manager should not be null"
argument_list|,
name|im
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
name|assertNotNull
argument_list|(
literal|"MBeanServer should be available."
argument_list|,
name|mbs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|// try to get WorkQueue information
specifier|public
name|void
name|testWorkQueueInstrumentation
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
name|bus
operator|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"managed-spring.xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|im
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Instrumentation Manager should not be null"
argument_list|,
name|im
argument_list|)
expr_stmt|;
name|WorkQueueManagerImpl
name|wqm
init|=
operator|new
name|WorkQueueManagerImpl
argument_list|()
decl_stmt|;
name|wqm
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|wqm
operator|.
name|getAutomaticWorkQueue
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
name|assertNotNull
argument_list|(
literal|"MBeanServer should be available."
argument_list|,
name|mbs
argument_list|)
expr_stmt|;
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
literal|":type=WorkQueues,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
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
name|ObjectName
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
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"test-wq"
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|hwm
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|hwm
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|testInstrumentTwoBuses
parameter_list|()
block|{
name|ClassPathXmlApplicationContext
name|context
init|=
literal|null
decl_stmt|;
name|Bus
name|cxf1
init|=
literal|null
decl_stmt|;
name|Bus
name|cxf2
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"managed-spring-twobuses.xml"
argument_list|)
expr_stmt|;
name|cxf1
operator|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"cxf1"
argument_list|)
expr_stmt|;
name|InstrumentationManager
name|im1
init|=
name|cxf1
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
literal|"Instrumentation Manager of cxf1 should not be null"
argument_list|,
name|im1
argument_list|)
expr_stmt|;
name|CounterRepository
name|cr1
init|=
name|cxf1
operator|.
name|getExtension
argument_list|(
name|CounterRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"CounterRepository of cxf1 should not be null"
argument_list|,
name|cr1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CounterRepository of cxf1 has the wrong bus"
argument_list|,
name|cxf1
argument_list|,
name|cr1
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|cxf2
operator|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"cxf2"
argument_list|)
expr_stmt|;
name|InstrumentationManager
name|im2
init|=
name|cxf2
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
literal|"Instrumentation Manager of cxf2 should not be null"
argument_list|,
name|im2
argument_list|)
expr_stmt|;
name|CounterRepository
name|cr2
init|=
name|cxf2
operator|.
name|getExtension
argument_list|(
name|CounterRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"CounterRepository of cxf2 should not be null"
argument_list|,
name|cr2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CounterRepository of cxf2 has the wrong bus"
argument_list|,
name|cxf2
argument_list|,
name|cr2
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|cxf1
operator|!=
literal|null
condition|)
block|{
name|cxf1
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cxf2
operator|!=
literal|null
condition|)
block|{
name|cxf2
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInstrumentBusWithBusProperties
parameter_list|()
block|{
name|ClassPathXmlApplicationContext
name|context
init|=
literal|null
decl_stmt|;
name|Bus
name|cxf1
init|=
literal|null
decl_stmt|;
name|Bus
name|cxf2
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"managed-spring-twobuses2.xml"
argument_list|)
expr_stmt|;
name|cxf1
operator|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"cxf1"
argument_list|)
expr_stmt|;
name|InstrumentationManagerImpl
name|im1
init|=
operator|(
name|InstrumentationManagerImpl
operator|)
name|cxf1
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
literal|"Instrumentation Manager of cxf1 should not be null"
argument_list|,
name|im1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|im1
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:9914/jmxrmi"
argument_list|,
name|im1
operator|.
name|getJMXServiceURL
argument_list|()
argument_list|)
expr_stmt|;
name|cxf2
operator|=
operator|(
name|Bus
operator|)
name|context
operator|.
name|getBean
argument_list|(
literal|"cxf2"
argument_list|)
expr_stmt|;
name|InstrumentationManagerImpl
name|im2
init|=
operator|(
name|InstrumentationManagerImpl
operator|)
name|cxf2
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
literal|"Instrumentation Manager of cxf2 should not be null"
argument_list|,
name|im2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|im2
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:9913/jmxrmi"
argument_list|,
name|im2
operator|.
name|getJMXServiceURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|cxf1
operator|!=
literal|null
condition|)
block|{
name|cxf1
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cxf2
operator|!=
literal|null
condition|)
block|{
name|cxf2
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

