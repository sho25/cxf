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
name|ws
operator|.
name|rm
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
name|Collection
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
name|binding
operator|.
name|Binding
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
name|message
operator|.
name|Exchange
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfo
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfoMap
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
name|ws
operator|.
name|policy
operator|.
name|PolicyAssertion
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|AbstractRMInterceptorTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
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
name|testAccessors
parameter_list|()
block|{
name|RMInterceptor
name|interceptor
init|=
operator|new
name|RMInterceptor
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|,
name|interceptor
operator|.
name|getPhase
argument_list|()
argument_list|)
expr_stmt|;
name|Bus
name|bus
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|RMManager
name|busMgr
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|busMgr
argument_list|)
expr_stmt|;
name|RMManager
name|mgr
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|interceptor
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|bus
argument_list|,
name|interceptor
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|busMgr
argument_list|,
name|interceptor
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|setManager
argument_list|(
name|mgr
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mgr
argument_list|,
name|interceptor
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageSequenceFaultNoBinding
parameter_list|()
block|{
name|RMInterceptor
name|interceptor
init|=
operator|new
name|RMInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|SequenceFault
name|sf
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SequenceFault
operator|.
name|class
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|setSequenceFault
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Endpoint
name|e
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|e
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected Fault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|assertSame
argument_list|(
name|sf
argument_list|,
name|f
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageSequenceFault
parameter_list|()
block|{
name|RMInterceptor
name|interceptor
init|=
operator|new
name|RMInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|SequenceFault
name|sf
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SequenceFault
operator|.
name|class
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|setSequenceFault
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Endpoint
name|e
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|Binding
name|b
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Binding
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|e
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|RMManager
name|mgr
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|setManager
argument_list|(
name|mgr
argument_list|)
expr_stmt|;
name|BindingFaultFactory
name|bff
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingFaultFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|mgr
operator|.
name|getBindingFaultFactory
argument_list|(
name|b
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bff
argument_list|)
expr_stmt|;
name|Fault
name|fault
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Fault
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bff
operator|.
name|createFault
argument_list|(
name|sf
argument_list|,
name|message
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|fault
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bff
operator|.
name|toString
argument_list|(
name|fault
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"f"
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected Fault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|assertSame
argument_list|(
name|f
argument_list|,
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessageRMException
parameter_list|()
block|{
name|RMInterceptor
name|interceptor
init|=
operator|new
name|RMInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|RMException
name|rme
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMException
operator|.
name|class
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|setRMException
argument_list|(
name|rme
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected Fault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|assertSame
argument_list|(
name|rme
argument_list|,
name|f
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAssertReliability
parameter_list|()
block|{
name|RMInterceptor
name|interceptor
init|=
operator|new
name|RMInterceptor
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|AssertionInfoMap
name|aim
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
operator|new
name|ArrayList
argument_list|<
name|AssertionInfo
argument_list|>
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|aim
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|PolicyAssertion
name|a
init|=
name|control
operator|.
name|createMock
argument_list|(
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
name|AssertionInfo
name|ai
init|=
operator|new
name|AssertionInfo
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|assertReliability
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|aim
operator|.
name|put
argument_list|(
name|RM10Constants
operator|.
name|RMASSERTION_QNAME
argument_list|,
name|ais
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|assertReliability
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ai
operator|.
name|isAsserted
argument_list|()
argument_list|)
expr_stmt|;
name|ais
operator|.
name|add
argument_list|(
name|ai
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|assertReliability
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
class|class
name|RMInterceptor
extends|extends
name|AbstractRMInterceptor
block|{
specifier|private
name|SequenceFault
name|sequenceFault
decl_stmt|;
specifier|private
name|RMException
name|rmException
decl_stmt|;
name|void
name|setSequenceFault
parameter_list|(
name|SequenceFault
name|sf
parameter_list|)
block|{
name|sequenceFault
operator|=
name|sf
expr_stmt|;
block|}
name|void
name|setRMException
parameter_list|(
name|RMException
name|rme
parameter_list|)
block|{
name|rmException
operator|=
name|rme
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handle
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|SequenceFault
throws|,
name|RMException
block|{
if|if
condition|(
literal|null
operator|!=
name|sequenceFault
condition|)
block|{
throw|throw
name|sequenceFault
throw|;
block|}
elseif|else
if|if
condition|(
literal|null
operator|!=
name|rmException
condition|)
block|{
throw|throw
name|rmException
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

