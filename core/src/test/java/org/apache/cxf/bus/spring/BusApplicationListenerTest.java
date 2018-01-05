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
name|CXFBusLifeCycleManager
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractRefreshableApplicationContext
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
name|BusApplicationListenerTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testParentApplicationEvent
parameter_list|()
block|{
name|AbstractRefreshableApplicationContext
name|parent
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|()
decl_stmt|;
name|parent
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|CXFBusLifeCycleManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|CXFBusLifeCycleManager
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
name|manager
operator|.
name|registerLifeCycleListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
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
operator|.
name|times
argument_list|(
literal|1
argument_list|)
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
operator|.
name|times
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|parent
operator|.
name|close
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

