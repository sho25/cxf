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
name|bus
operator|.
name|spring
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|bus
operator|.
name|managers
operator|.
name|PhaseManagerImpl
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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|ServerRegistry
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
name|feature
operator|.
name|AbstractFeature
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
name|feature
operator|.
name|Feature
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
name|Phase
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
name|PhaseManager
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
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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

begin_class
specifier|public
class|class
name|SpringBusFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefault
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|BindingFactoryManager
name|bfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No binding factory manager"
argument_list|,
name|bfm
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No configurer"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No resource manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No destination factory manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No conduit initiator manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No phase manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No workqueue manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No lifecycle manager"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No service registry"
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|bfm
operator|.
name|getBindingFactory
argument_list|(
literal|"http://cxf.apache.org/unknown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BusException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|assertEquals
argument_list|(
literal|"Unexpected interceptors"
argument_list|,
literal|0
argument_list|,
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptors"
argument_list|,
literal|0
argument_list|,
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptors"
argument_list|,
literal|0
argument_list|,
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptors"
argument_list|,
literal|0
argument_list|,
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
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
name|testCustomFileName
parameter_list|()
block|{
name|String
name|cfgFile
init|=
literal|"org/apache/cxf/bus/spring/resources/bus-overwrite.xml"
decl_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|cfgFile
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|checkCustomerConfiguration
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomerBusShutdown
parameter_list|()
block|{
name|String
name|cfgFile
init|=
literal|"org/apache/cxf/bus/spring/customerBus.xml"
decl_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|cfgFile
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// We have three bus here, which should be closed rightly
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
name|testCustomFileURLFromSystemProperty
parameter_list|()
block|{
name|URL
name|cfgFileURL
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/bus-overwrite.xml"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_URL
argument_list|,
name|cfgFileURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|checkCustomerConfiguration
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|Configurer
operator|.
name|USER_CFG_FILE_PROPERTY_URL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCustomFileURL
parameter_list|()
block|{
name|URL
name|cfgFileURL
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"resources/bus-overwrite.xml"
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|cfgFileURL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|checkCustomerConfiguration
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkCustomerConfiguration
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|bus
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
name|bus
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors"
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
literal|"Unexpected interceptor"
argument_list|,
literal|"in-a"
argument_list|,
name|interceptors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interceptor"
argument_list|,
literal|"in-b"
argument_list|,
name|interceptors
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|bus
operator|.
name|getInFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors"
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
literal|"Unexpected interceptor"
argument_list|,
literal|"in-fault"
argument_list|,
name|interceptors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors"
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
literal|"Unexpected interceptor"
argument_list|,
literal|"out-fault"
argument_list|,
name|interceptors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|interceptors
operator|=
name|bus
operator|.
name|getOutInterceptors
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected number of interceptors"
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
literal|"Unexpected interceptor"
argument_list|,
literal|"out"
argument_list|,
name|interceptors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testForLifeCycle
parameter_list|()
block|{
name|BusLifeCycleListener
name|bl
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|BusLifeCycleListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|BusLifeCycleManager
name|lifeCycleManager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|lifeCycleManager
operator|.
name|registerLifeCycleListener
argument_list|(
name|bl
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|reset
argument_list|(
name|bl
argument_list|)
expr_stmt|;
name|bl
operator|.
name|preShutdown
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|bl
operator|.
name|postShutdown
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bl
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|bl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPhases
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|PhaseManager
name|cxfPM
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|PhaseManager
name|defaultPM
init|=
operator|new
name|PhaseManagerImpl
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|cxfPhases
init|=
name|cxfPM
operator|.
name|getInPhases
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|defaultPhases
init|=
name|defaultPM
operator|.
name|getInPhases
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|defaultPhases
operator|.
name|size
argument_list|()
argument_list|,
name|cxfPhases
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cxfPhases
operator|.
name|equals
argument_list|(
name|defaultPhases
argument_list|)
argument_list|)
expr_stmt|;
name|cxfPhases
operator|=
name|cxfPM
operator|.
name|getOutPhases
argument_list|()
expr_stmt|;
name|defaultPhases
operator|=
name|defaultPM
operator|.
name|getOutPhases
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|defaultPhases
operator|.
name|size
argument_list|()
argument_list|,
name|cxfPhases
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cxfPhases
operator|.
name|equals
argument_list|(
name|defaultPhases
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJsr250
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/bus/spring/testjsr250.xml"
argument_list|)
decl_stmt|;
name|TestExtension
name|te
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|TestExtension
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"@PostConstruct annotated method has not been called."
argument_list|,
name|te
operator|.
name|postConstructMethodCalled
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"@PreDestroy annoated method has been called already."
argument_list|,
operator|!
name|te
operator|.
name|preDestroyMethodCalled
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"@PreDestroy annotated method has not been called."
argument_list|,
name|te
operator|.
name|preDestroyMethodCalled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitialisation
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/bus/spring/init.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|TestListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bus
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|BusApplicationContext
operator|.
name|class
argument_list|)
operator|.
name|getBean
argument_list|(
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|TestInterceptor
implements|implements
name|Interceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
name|String
name|name
decl_stmt|;
name|TestInterceptor
parameter_list|()
block|{                     }
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|name
operator|=
name|n
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{           }
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{            }
specifier|public
name|void
name|postHandleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{                     }
block|}
specifier|static
class|class
name|TestExtension
block|{
name|boolean
name|postConstructMethodCalled
decl_stmt|;
name|boolean
name|preDestroyMethodCalled
decl_stmt|;
name|TestExtension
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|TestExtension
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
name|void
name|postConstructMethod
parameter_list|()
block|{
name|postConstructMethodCalled
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PreDestroy
name|void
name|preDestroyMethod
parameter_list|()
block|{
name|preDestroyMethodCalled
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|TestFeature
extends|extends
name|AbstractFeature
block|{
name|boolean
name|initialised
decl_stmt|;
name|TestFeature
parameter_list|()
block|{
comment|//nothing
block|}
annotation|@
name|Override
specifier|public
name|void
name|initialize
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|initialised
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|TestListener
implements|implements
name|BusLifeCycleListener
block|{
name|Bus
name|bus
decl_stmt|;
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|register
parameter_list|()
block|{
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
name|assertNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|TestFeature
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Feature
argument_list|>
name|features
init|=
name|bus
operator|.
name|getFeatures
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|features
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|TestFeature
name|tf
init|=
operator|(
name|TestFeature
operator|)
name|features
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|tf
operator|.
name|initialised
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|TestListener
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{         }
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{         }
block|}
block|}
end_class

end_unit

