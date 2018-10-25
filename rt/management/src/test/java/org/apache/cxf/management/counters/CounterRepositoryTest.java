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
operator|.
name|counters
package|;
end_package

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
name|List
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
name|message
operator|.
name|Message
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
name|CounterRepositoryTest
extends|extends
name|Assert
block|{
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|CounterRepository
name|cr
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|inlist
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outlist
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|faultlist
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|//private InstrumentationManager im;
specifier|private
name|ObjectName
name|serviceCounter
decl_stmt|;
specifier|private
name|ObjectName
name|operationCounter
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|inlist
operator|.
name|clear
argument_list|()
expr_stmt|;
name|outlist
operator|.
name|clear
argument_list|()
expr_stmt|;
name|serviceCounter
operator|=
operator|new
name|ObjectName
argument_list|(
literal|"tandoori:type=counter,service=help"
argument_list|)
expr_stmt|;
name|operationCounter
operator|=
operator|new
name|ObjectName
argument_list|(
literal|"tandoori:type=counter,service=help,operation=me"
argument_list|)
expr_stmt|;
name|bus
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inlist
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|outlist
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|faultlist
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|cr
operator|=
operator|new
name|CounterRepository
argument_list|()
expr_stmt|;
name|bus
operator|.
name|setExtension
argument_list|(
name|cr
argument_list|,
name|CounterRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|once
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|cr
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncreaseOneWayResponseCounter
parameter_list|()
throws|throws
name|Exception
block|{
comment|//cr.createCounter(operationCounter, true);
name|MessageHandlingTimeRecorder
name|mhtr
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|isOneWay
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|getEndTime
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|(
name|long
operator|)
literal|100000000
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|getHandlingTime
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|(
name|long
operator|)
literal|1000
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|getFaultMode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mhtr
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|serviceCounter
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|operationCounter
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
name|ResponseTimeCounter
name|opCounter
init|=
operator|(
name|ResponseTimeCounter
operator|)
name|cr
operator|.
name|getCounter
argument_list|(
name|operationCounter
argument_list|)
decl_stmt|;
name|ResponseTimeCounter
name|sCounter
init|=
operator|(
name|ResponseTimeCounter
operator|)
name|cr
operator|.
name|getCounter
argument_list|(
name|serviceCounter
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter isn't increased"
argument_list|,
name|opCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Service counter isn't increased"
argument_list|,
name|sCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|verifyBus
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mhtr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncreaseOneWayNoResponseCounter
parameter_list|()
throws|throws
name|Exception
block|{
comment|//cr.createCounter(operationCounter, true);
name|MessageHandlingTimeRecorder
name|mhtr
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|isOneWay
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|getEndTime
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr
operator|.
name|getFaultMode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mhtr
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|serviceCounter
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|operationCounter
argument_list|,
name|mhtr
argument_list|)
expr_stmt|;
name|ResponseTimeCounter
name|opCounter
init|=
operator|(
name|ResponseTimeCounter
operator|)
name|cr
operator|.
name|getCounter
argument_list|(
name|operationCounter
argument_list|)
decl_stmt|;
name|ResponseTimeCounter
name|sCounter
init|=
operator|(
name|ResponseTimeCounter
operator|)
name|cr
operator|.
name|getCounter
argument_list|(
name|serviceCounter
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter isn't increased"
argument_list|,
name|opCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Service counter isn't increased"
argument_list|,
name|sCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|verifyBus
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mhtr
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncreaseResponseCounter
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageHandlingTimeRecorder
name|mhtr1
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr1
operator|.
name|isOneWay
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr1
operator|.
name|getHandlingTime
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|(
name|long
operator|)
literal|1000
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr1
operator|.
name|getFaultMode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mhtr1
argument_list|)
expr_stmt|;
name|cr
operator|.
name|createCounter
argument_list|(
name|operationCounter
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|serviceCounter
argument_list|,
name|mhtr1
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|operationCounter
argument_list|,
name|mhtr1
argument_list|)
expr_stmt|;
name|ResponseTimeCounter
name|opCounter
init|=
operator|(
name|ResponseTimeCounter
operator|)
name|cr
operator|.
name|getCounter
argument_list|(
name|operationCounter
argument_list|)
decl_stmt|;
name|ResponseTimeCounter
name|sCounter
init|=
operator|(
name|ResponseTimeCounter
operator|)
name|cr
operator|.
name|getCounter
argument_list|(
name|serviceCounter
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter isn't increased"
argument_list|,
name|opCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter's AvgResponseTime is wrong "
argument_list|,
name|opCounter
operator|.
name|getAvgResponseTime
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter's MaxResponseTime is wrong "
argument_list|,
name|opCounter
operator|.
name|getMaxResponseTime
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter's MinResponseTime is wrong "
argument_list|,
name|opCounter
operator|.
name|getMinResponseTime
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Service counter isn't increased"
argument_list|,
name|sCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MessageHandlingTimeRecorder
name|mhtr2
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MessageHandlingTimeRecorder
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr2
operator|.
name|isOneWay
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr2
operator|.
name|getHandlingTime
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|(
name|long
operator|)
literal|2000
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mhtr2
operator|.
name|getFaultMode
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mhtr2
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|serviceCounter
argument_list|,
name|mhtr2
argument_list|)
expr_stmt|;
name|cr
operator|.
name|increaseCounter
argument_list|(
name|operationCounter
argument_list|,
name|mhtr2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter isn't increased"
argument_list|,
name|opCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter's AvgResponseTime is wrong "
argument_list|,
name|opCounter
operator|.
name|getAvgResponseTime
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|1500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter's MaxResponseTime is wrong "
argument_list|,
name|opCounter
operator|.
name|getMaxResponseTime
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|2000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The operation counter's MinResponseTime is wrong "
argument_list|,
name|opCounter
operator|.
name|getMinResponseTime
argument_list|()
argument_list|,
operator|(
name|long
operator|)
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Service counter isn't increased"
argument_list|,
name|sCounter
operator|.
name|getNumInvocations
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|opCounter
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getNumCheckedApplicationFaults
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getNumInvocations
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getNumLogicalRuntimeFaults
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getNumRuntimeFaults
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getNumUnCheckedApplicationFaults
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getTotalHandlingTime
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getMinResponseTime
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getMaxResponseTime
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|opCounter
operator|.
name|getAvgResponseTime
argument_list|()
operator|.
name|intValue
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|verifyBus
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mhtr1
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|mhtr2
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyBus
parameter_list|()
block|{
name|EasyMock
operator|.
name|verify
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// the numbers should match the implementation of CounterRepository
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|inlist
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|outlist
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

