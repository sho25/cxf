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
name|List
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
name|message
operator|.
name|MessageImpl
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
name|BindingInfo
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
name|BindingOperationInfo
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
name|EndpointInfo
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
name|OperationInfo
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
name|ColocMessageObserverTest
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
name|ColocMessageObserver
name|observer
decl_stmt|;
specifier|private
name|Message
name|msg
decl_stmt|;
specifier|private
name|Exchange
name|ex
decl_stmt|;
specifier|private
name|Service
name|srv
decl_stmt|;
specifier|private
name|Endpoint
name|ep
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|OperationInfo
name|oi
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
name|ep
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|srv
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Service
operator|.
name|class
argument_list|)
expr_stmt|;
name|oi
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|OperationInfo
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
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|ex
operator|=
operator|new
name|ExchangeImpl
argument_list|()
expr_stmt|;
comment|//msg.setExchange(ex);
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
name|testSetExchangeProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|opName
init|=
operator|new
name|QName
argument_list|(
literal|"A"
argument_list|,
literal|"B"
argument_list|)
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|opName
argument_list|)
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
expr_stmt|;
name|Binding
name|binding
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
name|ep
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|EndpointInfo
name|ei
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ei
argument_list|)
expr_stmt|;
name|BindingInfo
name|bi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ei
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bi
argument_list|)
expr_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bi
operator|.
name|getOperation
argument_list|(
name|opName
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|boi
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|oi
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
name|ClassLoader
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|observer
operator|=
operator|new
name|ColocMessageObserver
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|observer
operator|.
name|setExchangeProperties
argument_list|(
name|ex
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Bus should be set"
argument_list|,
name|ex
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Endpoint should be set"
argument_list|,
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Binding should be set"
argument_list|,
name|ex
operator|.
name|get
argument_list|(
name|Binding
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Service should be set"
argument_list|,
name|ex
operator|.
name|get
argument_list|(
name|Service
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"BindingOperationInfo should be set"
argument_list|,
name|ex
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"OperationInfo should be set"
argument_list|,
name|ex
operator|.
name|get
argument_list|(
name|OperationInfo
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
name|testObserverOnMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Binding
name|binding
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
name|ep
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|Message
name|inMsg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|binding
operator|.
name|createMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|MessageInfo
name|mi
init|=
name|control
operator|.
name|createMock
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|oi
operator|.
name|getInput
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|mi
argument_list|)
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
name|anyTimes
argument_list|()
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
operator|new
name|PhaseManagerImpl
argument_list|()
argument_list|)
operator|.
name|times
argument_list|(
literal|2
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
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|observer
operator|=
operator|new
name|TestColocMessageObserver
argument_list|(
name|ep
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|Exchange
name|inEx
init|=
name|inMsg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should Have a valid Exchange"
argument_list|,
name|inEx
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Message.REQUESTOR_ROLE should be false"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|inMsg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Message.INBOUND_MESSAGE should be true"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|,
name|inMsg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"MessageInfo should be present in the Message instance"
argument_list|,
name|inMsg
operator|.
name|get
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Chain should be set"
argument_list|,
name|inMsg
operator|.
name|getInterceptorChain
argument_list|()
argument_list|)
expr_stmt|;
name|Exchange
name|ex1
init|=
name|msg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Exchange should be set"
argument_list|,
name|ex1
argument_list|)
expr_stmt|;
block|}
class|class
name|TestColocMessageObserver
extends|extends
name|ColocMessageObserver
block|{
specifier|public
name|TestColocMessageObserver
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setExchangeProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|exchange
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
name|exchange
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
name|exchange
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
name|exchange
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|oi
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|addColocInterceptors
parameter_list|()
block|{
return|return
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
return|;
block|}
block|}
block|}
end_class

end_unit

