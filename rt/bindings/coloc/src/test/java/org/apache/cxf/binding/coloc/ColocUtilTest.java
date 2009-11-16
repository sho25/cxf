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
name|binding
operator|.
name|coloc
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
name|Iterator
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|interceptor
operator|.
name|InterceptorChain
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
name|ExchangeImpl
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
name|phase
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|model
operator|.
name|FaultInfo
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
name|service
operator|.
name|model
operator|.
name|MessageInfo
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|coloc
operator|.
name|types
operator|.
name|FaultDetailT
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|coloc
operator|.
name|types
operator|.
name|InHeaderT
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|coloc
operator|.
name|types
operator|.
name|OutHeaderT
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|rpc_lit
operator|.
name|PingMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|classextension
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
name|classextension
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
name|ColocUtilTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
init|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
decl_stmt|;
specifier|private
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
block|{
name|bus
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
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
name|testSetColocInPhases
parameter_list|()
throws|throws
name|Exception
block|{
name|PhaseManagerImpl
name|phaseMgr
init|=
operator|new
name|PhaseManagerImpl
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|list
init|=
name|phaseMgr
operator|.
name|getInPhases
argument_list|()
decl_stmt|;
name|int
name|size1
init|=
name|list
operator|.
name|size
argument_list|()
decl_stmt|;
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|list
argument_list|,
name|Phase
operator|.
name|USER_LOGICAL
argument_list|,
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"The list size should not be same"
argument_list|,
name|size1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expecting Phase.USER_LOGICAL"
argument_list|,
name|list
operator|.
name|first
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Phase
operator|.
name|USER_LOGICAL
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expecting Phase.POST_INVOKE"
argument_list|,
name|list
operator|.
name|last
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetColocOutPhases
parameter_list|()
throws|throws
name|Exception
block|{
name|PhaseManagerImpl
name|phaseMgr
init|=
operator|new
name|PhaseManagerImpl
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|list
init|=
name|phaseMgr
operator|.
name|getOutPhases
argument_list|()
decl_stmt|;
name|int
name|size1
init|=
name|list
operator|.
name|size
argument_list|()
decl_stmt|;
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|list
argument_list|,
name|Phase
operator|.
name|SETUP
argument_list|,
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
literal|"The list size should not be same"
argument_list|,
name|size1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expecting Phase.SETUP"
argument_list|,
name|list
operator|.
name|first
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Phase
operator|.
name|SETUP
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expecting Phase.POST_LOGICAL"
argument_list|,
name|list
operator|.
name|last
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetOutInterceptorChain
parameter_list|()
throws|throws
name|Exception
block|{
name|PhaseManagerImpl
name|phaseMgr
init|=
operator|new
name|PhaseManagerImpl
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|list
init|=
name|phaseMgr
operator|.
name|getInPhases
argument_list|()
decl_stmt|;
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|list
argument_list|,
name|Phase
operator|.
name|SETUP
argument_list|,
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
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
name|Service
name|srv
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|srv
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|getService
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|srv
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|srv
operator|.
name|getOutInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|atLeastOnce
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
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|InterceptorChain
name|chain
init|=
name|ColocUtil
operator|.
name|getOutInterceptorChain
argument_list|(
name|ex
argument_list|,
name|list
argument_list|)
decl_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have chain instance"
argument_list|,
name|chain
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should not have interceptors in chain"
argument_list|,
literal|false
argument_list|,
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetInInterceptorChain
parameter_list|()
throws|throws
name|Exception
block|{
name|PhaseManagerImpl
name|phaseMgr
init|=
operator|new
name|PhaseManagerImpl
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|list
init|=
name|phaseMgr
operator|.
name|getInPhases
argument_list|()
decl_stmt|;
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|list
argument_list|,
name|Phase
operator|.
name|SETUP
argument_list|,
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
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
name|Service
name|srv
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|srv
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
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
operator|.
name|andReturn
argument_list|(
name|phaseMgr
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|getService
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|srv
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|srv
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
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
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
argument_list|)
operator|.
name|atLeastOnce
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|InterceptorChain
name|chain
init|=
name|ColocUtil
operator|.
name|getInInterceptorChain
argument_list|(
name|ex
argument_list|,
name|list
argument_list|)
decl_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have chain instance"
argument_list|,
name|chain
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|iter
init|=
name|chain
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should not have interceptors in chain"
argument_list|,
literal|false
argument_list|,
name|iter
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"OutFaultObserver should be set"
argument_list|,
name|chain
operator|.
name|getFaultObserver
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsSameFaultInfo
parameter_list|()
block|{
name|OperationInfo
name|oi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|match
init|=
name|ColocUtil
operator|.
name|isSameFaultInfo
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should return true"
argument_list|,
literal|true
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|FaultInfo
argument_list|>
name|fil1
init|=
operator|new
name|ArrayList
argument_list|<
name|FaultInfo
argument_list|>
argument_list|()
decl_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameFaultInfo
argument_list|(
name|fil1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not find a match"
argument_list|,
literal|false
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameFaultInfo
argument_list|(
literal|null
argument_list|,
name|fil1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not find a match"
argument_list|,
literal|false
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|FaultInfo
argument_list|>
name|fil2
init|=
operator|new
name|ArrayList
argument_list|<
name|FaultInfo
argument_list|>
argument_list|()
decl_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameFaultInfo
argument_list|(
name|fil1
argument_list|,
name|fil2
argument_list|)
expr_stmt|;
name|QName
name|fn1
init|=
operator|new
name|QName
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
decl_stmt|;
name|QName
name|fn2
init|=
operator|new
name|QName
argument_list|(
literal|"C"
argument_list|,
literal|"D"
argument_list|)
decl_stmt|;
name|FaultInfo
name|fi1
init|=
operator|new
name|FaultInfo
argument_list|(
name|fn1
argument_list|,
literal|null
argument_list|,
name|oi
argument_list|)
decl_stmt|;
name|fi1
operator|.
name|setProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|PingMeFault
operator|.
name|class
argument_list|)
expr_stmt|;
name|fil1
operator|.
name|add
argument_list|(
name|fi1
argument_list|)
expr_stmt|;
name|FaultInfo
name|fi2
init|=
operator|new
name|FaultInfo
argument_list|(
name|fn2
argument_list|,
literal|null
argument_list|,
name|oi
argument_list|)
decl_stmt|;
name|fi2
operator|.
name|setProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FaultDetailT
operator|.
name|class
argument_list|)
expr_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameFaultInfo
argument_list|(
name|fil1
argument_list|,
name|fil2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not find a match"
argument_list|,
literal|false
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|FaultInfo
name|fi3
init|=
operator|new
name|FaultInfo
argument_list|(
name|fn2
argument_list|,
literal|null
argument_list|,
name|oi
argument_list|)
decl_stmt|;
name|fi3
operator|.
name|setProperty
argument_list|(
name|Class
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|PingMeFault
operator|.
name|class
argument_list|)
expr_stmt|;
name|fil2
operator|.
name|add
argument_list|(
name|fi3
argument_list|)
expr_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameFaultInfo
argument_list|(
name|fil1
argument_list|,
name|fil2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should find a match"
argument_list|,
literal|true
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsSameMessageInfo
parameter_list|()
block|{
name|OperationInfo
name|oi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|match
init|=
name|ColocUtil
operator|.
name|isSameMessageInfo
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should return true"
argument_list|,
literal|true
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|QName
name|mn1
init|=
operator|new
name|QName
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
decl_stmt|;
name|QName
name|mn2
init|=
operator|new
name|QName
argument_list|(
literal|"C"
argument_list|,
literal|"D"
argument_list|)
decl_stmt|;
name|MessageInfo
name|mi1
init|=
operator|new
name|MessageInfo
argument_list|(
name|oi
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|,
name|mn1
argument_list|)
decl_stmt|;
name|MessageInfo
name|mi2
init|=
operator|new
name|MessageInfo
argument_list|(
name|oi
argument_list|,
name|MessageInfo
operator|.
name|Type
operator|.
name|INPUT
argument_list|,
name|mn2
argument_list|)
decl_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameMessageInfo
argument_list|(
name|mi1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not find a match"
argument_list|,
literal|false
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameMessageInfo
argument_list|(
literal|null
argument_list|,
name|mi2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not find a match"
argument_list|,
literal|false
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|MessagePartInfo
name|mpi
init|=
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
literal|"B"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mpi
operator|.
name|setTypeClass
argument_list|(
name|InHeaderT
operator|.
name|class
argument_list|)
expr_stmt|;
name|mi1
operator|.
name|addMessagePart
argument_list|(
name|mpi
argument_list|)
expr_stmt|;
name|mpi
operator|=
operator|new
name|MessagePartInfo
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
literal|"D"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mpi
operator|.
name|setTypeClass
argument_list|(
name|OutHeaderT
operator|.
name|class
argument_list|)
expr_stmt|;
name|mi2
operator|.
name|addMessagePart
argument_list|(
name|mpi
argument_list|)
expr_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameMessageInfo
argument_list|(
name|mi1
argument_list|,
name|mi2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should not find a match"
argument_list|,
literal|false
argument_list|,
name|match
argument_list|)
expr_stmt|;
name|mpi
operator|.
name|setTypeClass
argument_list|(
name|InHeaderT
operator|.
name|class
argument_list|)
expr_stmt|;
name|match
operator|=
name|ColocUtil
operator|.
name|isSameMessageInfo
argument_list|(
name|mi1
argument_list|,
name|mi2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should find a match"
argument_list|,
literal|true
argument_list|,
name|match
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

