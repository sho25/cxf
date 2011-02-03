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
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|addressing
operator|.
name|AddressingPropertiesImpl
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
name|AttributedURIType
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
name|EndpointReferenceType
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
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|AttributedURI
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
name|RMConstants
operator|.
name|getUnknownSequenceFaultCode
argument_list|()
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
name|BigInteger
name|nr
init|=
name|BigInteger
operator|.
name|TEN
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
annotation|@
name|Test
specifier|public
name|void
name|testAcknowledgeLastMessage
parameter_list|()
throws|throws
name|Exception
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
name|Method
name|m2
init|=
name|Destination
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"getReliableEndpoint"
argument_list|,
operator|new
name|Class
index|[]
block|{}
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
block|,
name|m2
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
name|BigInteger
name|nr
init|=
name|BigInteger
operator|.
name|TEN
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
operator|.
name|times
argument_list|(
literal|2
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
name|andReturn
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|ds
operator|.
name|acknowledge
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|SequenceType
operator|.
name|LastMessage
name|lm
init|=
name|control
operator|.
name|createMock
argument_list|(
name|SequenceType
operator|.
name|LastMessage
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
name|getLastMessage
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|lm
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setLastMessageNumber
argument_list|(
name|nr
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|ds
operator|.
name|scheduleImmediateAcknowledgement
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|AddressingPropertiesImpl
name|maps
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AddressingPropertiesImpl
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
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
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
name|message
operator|.
name|get
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|maps
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|replyToEPR
init|=
name|control
operator|.
name|createMock
argument_list|(
name|EndpointReferenceType
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|maps
operator|.
name|getReplyTo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|replyToEPR
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|AttributedURIType
name|replyToURI
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AttributedURIType
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|replyToEPR
operator|.
name|getAddress
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|replyToURI
argument_list|)
expr_stmt|;
name|String
name|replyToAddress
init|=
literal|"replyTo"
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|replyToURI
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|replyToAddress
argument_list|)
expr_stmt|;
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
name|v200408
operator|.
name|EndpointReferenceType
name|acksToEPR
init|=
name|control
operator|.
name|createMock
argument_list|(
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
name|v200408
operator|.
name|EndpointReferenceType
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
name|getAcksTo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|acksToEPR
argument_list|)
expr_stmt|;
name|AttributedURI
name|acksToURI
init|=
name|control
operator|.
name|createMock
argument_list|(
name|AttributedURI
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|acksToEPR
operator|.
name|getAddress
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|acksToURI
argument_list|)
expr_stmt|;
name|String
name|acksToAddress
init|=
literal|"acksTo"
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|acksToURI
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|acksToAddress
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ds
operator|.
name|canPiggybackAckOnPartialResponse
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|destination
operator|.
name|getReliableEndpoint
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rme
argument_list|)
expr_stmt|;
name|Proxy
name|proxy
init|=
name|control
operator|.
name|createMock
argument_list|(
name|Proxy
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
name|getProxy
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|acknowledge
argument_list|(
name|ds
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
name|acknowledge
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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

