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
name|transport
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|PhaseInterceptorChain
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

begin_class
specifier|public
class|class
name|ChainInitiationObserverTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|TestChain
name|chain
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|ChainInitiationObserver
name|observer
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
name|message
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
expr_stmt|;
name|Phase
name|phase1
init|=
operator|new
name|Phase
argument_list|(
literal|"phase1"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
init|=
operator|new
name|TreeSet
argument_list|<
name|Phase
argument_list|>
argument_list|()
decl_stmt|;
name|phases
operator|.
name|add
argument_list|(
name|phase1
argument_list|)
expr_stmt|;
name|chain
operator|=
operator|new
name|TestChain
argument_list|(
name|phases
argument_list|)
expr_stmt|;
name|observer
operator|=
operator|new
name|ChainInitiationObserver
argument_list|(
literal|null
argument_list|,
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPausedChain
parameter_list|()
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|chain
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|chain
operator|.
name|isInvoked
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|TestChain
extends|extends
name|PhaseInterceptorChain
block|{
specifier|private
name|boolean
name|invoked
decl_stmt|;
specifier|public
name|TestChain
parameter_list|(
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|ps
parameter_list|)
block|{
name|super
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|resume
parameter_list|()
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|State
operator|.
name|PAUSED
return|;
block|}
specifier|public
name|boolean
name|isInvoked
parameter_list|()
block|{
return|return
name|invoked
return|;
block|}
block|}
block|}
end_class

end_unit

