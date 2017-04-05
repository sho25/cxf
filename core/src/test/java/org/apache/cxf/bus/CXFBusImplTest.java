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
package|;
end_package

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
name|Map
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
name|extension
operator|.
name|ExtensionManagerBus
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
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|CXFBusImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testThreadBus
parameter_list|()
throws|throws
name|BusException
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|Bus
name|b2
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|bus
argument_list|,
name|b2
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
name|testConstructionWithoutExtensions
parameter_list|()
throws|throws
name|BusException
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
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
name|testConstructionWithExtensions
parameter_list|()
throws|throws
name|BusException
block|{
name|IMocksControl
name|control
decl_stmt|;
name|BindingFactoryManager
name|bindingFactoryManager
decl_stmt|;
name|InstrumentationManager
name|instrumentationManager
decl_stmt|;
name|PhaseManager
name|phaseManager
decl_stmt|;
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|extensions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|bindingFactoryManager
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|instrumentationManager
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|phaseManager
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|,
name|bindingFactoryManager
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|,
name|instrumentationManager
argument_list|)
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|,
name|phaseManager
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|(
name|extensions
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|bindingFactoryManager
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|BindingFactoryManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|instrumentationManager
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|phaseManager
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtensions
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|String
name|extension
init|=
literal|"CXF"
decl_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|extension
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|extension
argument_list|,
name|bus
operator|.
name|getExtension
argument_list|(
name|String
operator|.
name|class
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
name|testBusID
parameter_list|()
block|{
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|bus
operator|.
name|getId
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The bus id should be cxf"
argument_list|,
name|id
argument_list|,
name|Bus
operator|.
name|DEFAULT_BUS_ID
operator|+
name|Math
operator|.
name|abs
argument_list|(
name|bus
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|bus
operator|.
name|setId
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The bus id should be changed"
argument_list|,
literal|"test"
argument_list|,
name|bus
operator|.
name|getId
argument_list|()
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
name|testShutdownWithBusLifecycle
parameter_list|()
block|{
specifier|final
name|Bus
name|bus
init|=
operator|new
name|ExtensionManagerBus
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
name|BusLifeCycleListener
name|listener
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
name|EasyMock
operator|.
name|reset
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|preShutdown
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|listener
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
name|listener
argument_list|)
expr_stmt|;
name|lifeCycleManager
operator|.
name|registerLifeCycleListener
argument_list|(
name|listener
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
name|listener
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
block|}
end_class

end_unit

