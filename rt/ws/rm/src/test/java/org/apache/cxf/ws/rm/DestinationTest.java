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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|ws
operator|.
name|rm
operator|.
name|persistence
operator|.
name|RMStore
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
name|rm
operator|.
name|v200702
operator|.
name|Identifier
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
name|rm
operator|.
name|v200702
operator|.
name|SequenceType
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|DestinationTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|RMEndpoint
name|rme
decl_stmt|;
specifier|private
name|Destination
name|destination
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
name|rme
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|RMEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|destination
operator|=
operator|new
name|Destination
argument_list|(
name|rme
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
name|testGetSequence
parameter_list|()
block|{
name|Identifier
name|id
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Identifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|sid
init|=
literal|"s1"
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|id
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|destination
operator|.
name|getSequence
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAllSequences
parameter_list|()
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|destination
operator|.
name|getAllSequences
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
name|testAddRemoveSequence
parameter_list|()
block|{
name|DestinationSequence
name|ds
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationSequence
operator|.
name|class
argument_list|)
decl_stmt|;
name|ds
operator|.
name|setDestination
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|Identifier
name|id
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Identifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ds
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|id
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|String
name|sid
init|=
literal|"s1"
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|id
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sid
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|RMManager
name|manager
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
name|rme
operator|.
name|getManager
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|manager
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|RMStore
name|store
init|=
name|control
operator|.
name|createMock
argument_list|(
name|RMStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|manager
operator|.
name|getStore
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|store
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|store
operator|.
name|createDestinationSequence
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|store
operator|.
name|removeDestinationSequence
argument_list|(
name|id
argument_list|)
expr_stmt|;
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
name|destination
operator|.
name|addSequence
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|destination
operator|.
name|getAllSequences
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|ds
argument_list|,
name|destination
operator|.
name|getSequence
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|destination
operator|.
name|removeSequence
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|destination
operator|.
name|getAllSequences
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
name|testAcknowledgeNoSequence
parameter_list|()
throws|throws
name|SequenceFault
throws|,
name|RMException
block|{
name|Message
name|message
init|=
name|setupMessage
argument_list|()
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
name|message
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|rmps
operator|.
name|getSequence
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
name|destination
operator|.
name|acknowledge
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcknowledgeUnknownSequence
parameter_list|()
throws|throws
name|RMException
block|{
name|Message
name|message
init|=
name|setupMessage
argument_list|()
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
name|message
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
name|SequenceType
name|st
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SequenceType
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|rmps
operator|.
name|getSequence
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|st
argument_list|)
expr_stmt|;
name|Identifier
name|id
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Identifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|st
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|id
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
name|rme
operator|.
name|getEncoderDecoder
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|EncoderDecoder10Impl
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|String
name|sid
init|=
literal|"sid"
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|id
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|destination
operator|.
name|acknowledge
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected SequenceFault not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SequenceFault
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|RM10Constants
operator|.
name|UNKNOWN_SEQUENCE_FAULT_QNAME
argument_list|,
name|ex
operator|.
name|getSequenceFault
argument_list|()
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcknowledgeAlreadyAcknowledgedMessage
parameter_list|()
throws|throws
name|SequenceFault
throws|,
name|RMException
throws|,
name|NoSuchMethodException
block|{
name|Method
name|m1
init|=
name|Destination
operator|.
name|class
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getSequence"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Identifier
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|destination
operator|=
name|control
operator|.
name|createMock
argument_list|(
name|Destination
operator|.
name|class
argument_list|,
operator|new
name|Method
index|[]
block|{
name|m1
block|}
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|setupMessage
argument_list|()
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
name|message
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
name|SequenceType
name|st
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SequenceType
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|rmps
operator|.
name|getSequence
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|st
argument_list|)
expr_stmt|;
name|Identifier
name|id
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Identifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|st
operator|.
name|getIdentifier
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|DestinationSequence
name|ds
init|=
name|control
operator|.
name|createMock
argument_list|(
name|DestinationSequence
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|destination
operator|.
name|getSequence
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|ds
argument_list|)
expr_stmt|;
name|long
name|nr
init|=
literal|10
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|st
operator|.
name|getMessageNumber
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|nr
argument_list|)
expr_stmt|;
name|RMException
name|ex
init|=
operator|new
name|RMException
argument_list|(
operator|new
name|RuntimeException
argument_list|(
literal|"already acknowledged"
argument_list|)
argument_list|)
decl_stmt|;
name|ds
operator|.
name|applyDeliveryAssurance
argument_list|(
name|nr
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andThrow
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
try|try
block|{
name|destination
operator|.
name|acknowledge
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected RMEcception not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RMException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|ex
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*    @Test     public void testAcknowledgeLastMessage() throws Exception {                  Method m1 = Destination.class.getDeclaredMethod("getSequence", new Class[] {Identifier.class});         Method m2 = Destination.class.getMethod("getReliableEndpoint", new Class[] {});                  destination = control.createMock(Destination.class, new Method[] {m1, m2});         Message message = setupMessage();         RMProperties rmps = control.createMock(RMProperties.class);         EasyMock.expect(message.get(RMMessageConstants.RM_PROPERTIES_INBOUND)).andReturn(rmps);         SequenceType st = control.createMock(SequenceType.class);         EasyMock.expect(rmps.getSequence()).andReturn(st);         Identifier id = control.createMock(Identifier.class);         EasyMock.expect(st.getIdentifier()).andReturn(id);          long nr = 10;         EasyMock.expect(st.getMessageNumber()).andReturn(nr).times(3);         DestinationSequence ds = control.createMock(DestinationSequence.class);         EasyMock.expect(destination.getSequence(id)).andReturn(ds);                  ds.applyDeliveryAssurance(nr, message);         EasyMock.expectLastCall().andReturn(Boolean.TRUE);         ds.acknowledge(message);         EasyMock.expectLastCall();         SequenceType.LastMessage lm = control.createMock(SequenceType.LastMessage.class);         EasyMock.expect(st.getLastMessage()).andReturn(lm);         ds.setLastMessageNumber(nr);         EasyMock.expectLastCall();         ds.scheduleImmediateAcknowledgement();         EasyMock.expectLastCall();         AddressingPropertiesImpl maps = control.createMock(AddressingPropertiesImpl.class);         EasyMock.expect(message.get(Message.REQUESTOR_ROLE)).andReturn(null);         EasyMock.expect(message.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND)).andReturn(maps);         EndpointReferenceType replyToEPR = control.createMock(EndpointReferenceType.class);         EasyMock.expect(maps.getReplyTo()).andReturn(replyToEPR).times(2);         AttributedURIType replyToURI = control.createMock(AttributedURIType.class);         EasyMock.expect(replyToEPR.getAddress()).andReturn(replyToURI);         String replyToAddress = "replyTo";                 EasyMock.expect(replyToURI.getValue()).andReturn(replyToAddress);                 org.apache.cxf.ws.addressing.v200408.EndpointReferenceType acksToEPR =             control.createMock(org.apache.cxf.ws.addressing.v200408.EndpointReferenceType.class);         EasyMock.expect(ds.getAcksTo()).andReturn(acksToEPR);         AttributedURI acksToURI = control.createMock(AttributedURI.class);         EasyMock.expect(acksToEPR.getAddress()).andReturn(acksToURI);         String acksToAddress = "acksTo";         EasyMock.expect(acksToURI.getValue()).andReturn(acksToAddress);         EasyMock.expect(ds.canPiggybackAckOnPartialResponse()).andReturn(false);         EasyMock.expect(destination.getReliableEndpoint()).andReturn(rme).times(2);         RMManager manager = control.createMock(RMManager.class);         EasyMock.expect(rme.getManager()).andReturn(manager);         RMStore store = control.createMock(RMStore.class);         EasyMock.expect(manager.getStore()).andReturn(store);         Proxy proxy = control.createMock(Proxy.class);         EasyMock.expect(rme.getProxy()).andReturn(proxy);         proxy.acknowledge(ds);         EasyMock.expectLastCall();                  control.replay();         destination.acknowledge(message);        }   */
specifier|private
name|Message
name|setupMessage
parameter_list|()
block|{
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
name|Exchange
name|exchange
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
name|exchange
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|exchange
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
name|exchange
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
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

