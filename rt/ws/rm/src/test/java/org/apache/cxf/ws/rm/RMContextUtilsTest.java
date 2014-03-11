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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|addressing
operator|.
name|JAXWSAConstants
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
name|RMContextUtilsTest
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
name|testCtor
parameter_list|()
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
operator|new
name|RMContextUtils
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenerateUUID
parameter_list|()
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|RMContextUtils
operator|.
name|generateUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsServerSide
parameter_list|()
block|{
name|Message
name|msg
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
name|msg
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getDestination
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
name|assertTrue
argument_list|(
operator|!
name|RMContextUtils
operator|.
name|isServerSide
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsRmPrtocolMessage
parameter_list|()
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|String
name|action
init|=
literal|null
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|RMContextUtils
operator|.
name|isRMProtocolMessage
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
name|action
operator|=
literal|""
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|RMContextUtils
operator|.
name|isRMProtocolMessage
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
name|action
operator|=
literal|"greetMe"
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|RMContextUtils
operator|.
name|isRMProtocolMessage
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
name|action
operator|=
name|RM10Constants
operator|.
name|CREATE_SEQUENCE_ACTION
expr_stmt|;
name|assertTrue
argument_list|(
name|RMContextUtils
operator|.
name|isRMProtocolMessage
argument_list|(
name|action
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRetrieveOutboundRMProperties
parameter_list|()
block|{
name|Message
name|msg
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
name|RMProperties
name|rmps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_OUTBOUND
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rmps
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|rmps
argument_list|,
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|msg
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRetrieveInboundRMPropertiesFromOutboundMessage
parameter_list|()
block|{
name|Message
name|outMsg
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
name|outMsg
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ex
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|outMsg
argument_list|)
expr_stmt|;
name|Message
name|inMsg
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
name|ex
operator|.
name|getInMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getInFaultMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|RMProperties
name|rmps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|inMsg
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_INBOUND
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rmps
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|rmps
argument_list|,
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|outMsg
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRetrieveInboundRMPropertiesFromInboundMessage
parameter_list|()
block|{
name|Message
name|inMsg
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
name|inMsg
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ex
operator|.
name|getOutFaultMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|RMProperties
name|rmps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|inMsg
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_INBOUND
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rmps
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|rmps
argument_list|,
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|inMsg
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreRMProperties
parameter_list|()
block|{
name|Message
name|msg
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
name|RMProperties
name|rmps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|put
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_INBOUND
argument_list|,
name|rmps
argument_list|)
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
name|RMContextUtils
operator|.
name|storeRMProperties
argument_list|(
name|msg
argument_list|,
name|rmps
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRetrieveMAPs
parameter_list|()
block|{
name|Message
name|msg
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
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|AddressingProperties
name|maps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AddressingProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|JAXWSAConstants
operator|.
name|ADDRESSING_PROPERTIES_OUTBOUND
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|maps
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|maps
argument_list|,
name|RMContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreMAPs
parameter_list|()
block|{
name|Message
name|msg
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
name|AddressingProperties
name|maps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AddressingProperties
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|msg
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|ADDRESSING_PROPERTIES_OUTBOUND
argument_list|,
name|maps
argument_list|)
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
name|RMContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|msg
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

