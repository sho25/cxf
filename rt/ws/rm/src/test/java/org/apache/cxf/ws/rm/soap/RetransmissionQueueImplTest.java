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
operator|.
name|soap
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
name|Date
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
name|concurrent
operator|.
name|Executor
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
name|rm
operator|.
name|RMManager
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
name|RMMessageConstants
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
name|RMProperties
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
name|SourceSequence
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
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rmp
operator|.
name|v200502
operator|.
name|RMAssertion
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
comment|/**  * Test resend logic.  */
end_comment

begin_class
specifier|public
class|class
name|RetransmissionQueueImplTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|Long
name|ONE
init|=
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Long
name|TEN
init|=
operator|new
name|Long
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|private
name|IMocksControl
name|control
decl_stmt|;
specifier|private
name|RMManager
name|manager
decl_stmt|;
specifier|private
name|Executor
name|executor
decl_stmt|;
specifier|private
name|RetransmissionQueueImpl
name|queue
decl_stmt|;
specifier|private
name|TestResender
name|resender
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Message
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<
name|Message
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|RMProperties
argument_list|>
name|properties
init|=
operator|new
name|ArrayList
argument_list|<
name|RMProperties
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|SequenceType
argument_list|>
name|sequences
init|=
operator|new
name|ArrayList
argument_list|<
name|SequenceType
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Identifier
argument_list|>
name|identifiers
init|=
operator|new
name|ArrayList
argument_list|<
name|Identifier
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|mocks
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|RMAssertion
name|rma
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
name|manager
operator|=
name|createMock
argument_list|(
name|RMManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|queue
operator|=
operator|new
name|RetransmissionQueueImpl
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|resender
operator|=
operator|new
name|TestResender
argument_list|()
expr_stmt|;
name|queue
operator|.
name|replaceResender
argument_list|(
name|resender
argument_list|)
expr_stmt|;
name|executor
operator|=
name|createMock
argument_list|(
name|Executor
operator|.
name|class
argument_list|)
expr_stmt|;
name|rma
operator|=
name|createMock
argument_list|(
name|RMAssertion
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|executor
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
name|messages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|properties
operator|.
name|clear
argument_list|()
expr_stmt|;
name|sequences
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mocks
operator|.
name|clear
argument_list|()
expr_stmt|;
name|control
operator|.
name|reset
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
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected unacked map"
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"expected empty unacked map"
argument_list|,
literal|0
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|=
operator|new
name|RetransmissionQueueImpl
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|queue
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|setManager
argument_list|(
name|manager
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Unexpected RMManager"
argument_list|,
name|manager
argument_list|,
name|queue
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
name|testResendCandidateCtor
parameter_list|()
block|{
name|Message
name|message
init|=
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
name|candidate
init|=
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|message
argument_list|,
name|candidate
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|candidate
operator|.
name|getRetries
argument_list|()
argument_list|)
expr_stmt|;
name|Date
name|refDate
init|=
operator|new
name|Date
argument_list|(
name|now
operator|+
literal|5000
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|candidate
operator|.
name|getNext
argument_list|()
operator|.
name|before
argument_list|(
name|refDate
argument_list|)
argument_list|)
expr_stmt|;
name|refDate
operator|=
operator|new
name|Date
argument_list|(
name|now
operator|+
literal|7000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|candidate
operator|.
name|getNext
argument_list|()
operator|.
name|after
argument_list|(
name|refDate
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|candidate
operator|.
name|isPending
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResendCandidateAttempted
parameter_list|()
block|{
name|Message
name|message
init|=
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|ready
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
name|candidate
init|=
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|candidate
operator|.
name|attempted
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|candidate
operator|.
name|getRetries
argument_list|()
argument_list|)
expr_stmt|;
name|Date
name|refDate
init|=
operator|new
name|Date
argument_list|(
name|now
operator|+
literal|15000
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|candidate
operator|.
name|getNext
argument_list|()
operator|.
name|before
argument_list|(
name|refDate
argument_list|)
argument_list|)
expr_stmt|;
name|refDate
operator|=
operator|new
name|Date
argument_list|(
name|now
operator|+
literal|17000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|candidate
operator|.
name|getNext
argument_list|()
operator|.
name|after
argument_list|(
name|refDate
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|candidate
operator|.
name|isPending
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCacheUnacknowledged
parameter_list|()
block|{
name|Message
name|message1
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|)
decl_stmt|;
name|Message
name|message2
init|=
name|setUpMessage
argument_list|(
literal|"sequence2"
argument_list|)
decl_stmt|;
name|Message
name|message3
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|setupMessagePolicies
argument_list|(
name|message2
argument_list|)
expr_stmt|;
name|setupMessagePolicies
argument_list|(
name|message3
argument_list|)
expr_stmt|;
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected resend candidate"
argument_list|,
name|queue
operator|.
name|cacheUnacknowledged
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"expected non-empty unacked map"
argument_list|,
literal|1
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
name|sequence1List
init|=
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|get
argument_list|(
literal|"sequence1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null context list"
argument_list|,
name|sequence1List
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"expected context list entry"
argument_list|,
name|message1
argument_list|,
name|sequence1List
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected resend candidate"
argument_list|,
name|queue
operator|.
name|cacheUnacknowledged
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked map size"
argument_list|,
literal|2
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
name|sequence2List
init|=
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|get
argument_list|(
literal|"sequence2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null context list"
argument_list|,
name|sequence2List
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"expected context list entry"
argument_list|,
name|message2
argument_list|,
name|sequence2List
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected resend candidate"
argument_list|,
name|queue
operator|.
name|cacheUnacknowledged
argument_list|(
name|message3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"un expected unacked map size"
argument_list|,
literal|2
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|sequence1List
operator|=
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|get
argument_list|(
literal|"sequence1"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"expected non-null context list"
argument_list|,
name|sequence1List
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"expected context list entry"
argument_list|,
name|message3
argument_list|,
name|sequence1List
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPurgeAcknowledgedSome
parameter_list|()
block|{
name|Long
index|[]
name|messageNumbers
init|=
block|{
name|TEN
block|,
name|ONE
block|}
decl_stmt|;
name|SourceSequence
name|sequence
init|=
name|setUpSequence
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|false
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
name|sequenceList
init|=
operator|new
name|ArrayList
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|put
argument_list|(
literal|"sequence1"
argument_list|,
name|sequenceList
argument_list|)
expr_stmt|;
name|Message
name|message1
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|Message
name|message2
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message2
argument_list|)
expr_stmt|;
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|purgeAcknowledged
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked map size"
argument_list|,
literal|1
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked list size"
argument_list|,
literal|1
argument_list|,
name|sequenceList
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
name|testPurgeAcknowledgedNone
parameter_list|()
block|{
name|Long
index|[]
name|messageNumbers
init|=
block|{
name|TEN
block|,
name|ONE
block|}
decl_stmt|;
name|SourceSequence
name|sequence
init|=
name|setUpSequence
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
name|sequenceList
init|=
operator|new
name|ArrayList
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|put
argument_list|(
literal|"sequence1"
argument_list|,
name|sequenceList
argument_list|)
expr_stmt|;
name|Message
name|message1
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|Message
name|message2
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message2
argument_list|)
expr_stmt|;
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|purgeAcknowledged
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked map size"
argument_list|,
literal|1
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked list size"
argument_list|,
literal|2
argument_list|,
name|sequenceList
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
name|testPurgeAcknowledgedAll
parameter_list|()
block|{
name|Long
index|[]
name|messageNumbers
init|=
block|{
name|TEN
block|,
name|ONE
block|}
decl_stmt|;
name|SourceSequence
name|sequence
init|=
name|setUpSequence
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|true
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
name|sequenceList
init|=
operator|new
name|ArrayList
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|put
argument_list|(
literal|"sequence1"
argument_list|,
name|sequenceList
argument_list|)
expr_stmt|;
name|Message
name|message1
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|Message
name|message2
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message2
argument_list|)
expr_stmt|;
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|purgeAcknowledged
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked map size"
argument_list|,
literal|0
argument_list|,
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked list size"
argument_list|,
literal|0
argument_list|,
name|sequenceList
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
name|testIsEmpty
parameter_list|()
block|{
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"queue is not empty"
argument_list|,
name|queue
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCountUnacknowledged
parameter_list|()
block|{
name|Long
index|[]
name|messageNumbers
init|=
block|{
name|TEN
block|,
name|ONE
block|}
decl_stmt|;
name|SourceSequence
name|sequence
init|=
name|setUpSequence
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
name|sequenceList
init|=
operator|new
name|ArrayList
argument_list|<
name|RetransmissionQueueImpl
operator|.
name|ResendCandidate
argument_list|>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|getUnacknowledged
argument_list|()
operator|.
name|put
argument_list|(
literal|"sequence1"
argument_list|,
name|sequenceList
argument_list|)
expr_stmt|;
name|Message
name|message1
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|0
index|]
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message1
argument_list|)
expr_stmt|;
name|Message
name|message2
init|=
name|setUpMessage
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
index|[
literal|1
index|]
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|setupMessagePolicies
argument_list|(
name|message2
argument_list|)
expr_stmt|;
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message1
argument_list|)
argument_list|)
expr_stmt|;
name|sequenceList
operator|.
name|add
argument_list|(
name|queue
operator|.
name|createResendCandidate
argument_list|(
name|message2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked count"
argument_list|,
literal|2
argument_list|,
name|queue
operator|.
name|countUnacknowledged
argument_list|(
name|sequence
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"queue is empty"
argument_list|,
operator|!
name|queue
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCountUnacknowledgedUnknownSequence
parameter_list|()
block|{
name|Long
index|[]
name|messageNumbers
init|=
block|{
name|TEN
block|,
name|ONE
block|}
decl_stmt|;
name|SourceSequence
name|sequence
init|=
name|setUpSequence
argument_list|(
literal|"sequence1"
argument_list|,
name|messageNumbers
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ready
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected unacked count"
argument_list|,
literal|0
argument_list|,
name|queue
operator|.
name|countUnacknowledged
argument_list|(
name|sequence
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStartStop
parameter_list|()
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|queue
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Message
name|setUpMessage
parameter_list|(
name|String
name|sid
parameter_list|)
block|{
return|return
name|setUpMessage
argument_list|(
name|sid
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|Message
name|setUpMessage
parameter_list|(
name|String
name|sid
parameter_list|,
name|Long
name|messageNumber
parameter_list|)
block|{
return|return
name|setUpMessage
argument_list|(
name|sid
argument_list|,
name|messageNumber
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|Message
name|setUpMessage
parameter_list|(
name|String
name|sid
parameter_list|,
name|Long
name|messageNumber
parameter_list|,
name|boolean
name|storeSequence
parameter_list|)
block|{
name|Message
name|message
init|=
name|createMock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|storeSequence
condition|)
block|{
name|setUpSequenceType
argument_list|(
name|message
argument_list|,
name|sid
argument_list|,
name|messageNumber
argument_list|)
expr_stmt|;
block|}
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
specifier|private
name|void
name|setupMessagePolicies
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
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
name|EasyMock
operator|.
name|expect
argument_list|(
name|manager
operator|.
name|getRMAssertion
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|rma
argument_list|)
expr_stmt|;
name|RMAssertion
operator|.
name|BaseRetransmissionInterval
name|bri
init|=
name|createMock
argument_list|(
name|RMAssertion
operator|.
name|BaseRetransmissionInterval
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|rma
operator|.
name|getBaseRetransmissionInterval
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|bri
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|bri
operator|.
name|getMilliseconds
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|Long
argument_list|(
literal|5000
argument_list|)
argument_list|)
expr_stmt|;
name|RMAssertion
operator|.
name|ExponentialBackoff
name|eb
init|=
name|createMock
argument_list|(
name|RMAssertion
operator|.
name|ExponentialBackoff
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|rma
operator|.
name|getExponentialBackoff
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|eb
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|ready
parameter_list|(
name|boolean
name|doStart
parameter_list|)
block|{
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
if|if
condition|(
name|doStart
condition|)
block|{
name|queue
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|SequenceType
name|setUpSequenceType
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|sid
parameter_list|,
name|Long
name|messageNumber
parameter_list|)
block|{
name|RMProperties
name|rmps
init|=
name|createMock
argument_list|(
name|RMProperties
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_OUTBOUND
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|rmps
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|add
argument_list|(
name|rmps
argument_list|)
expr_stmt|;
name|SequenceType
name|sequence
init|=
name|createMock
argument_list|(
name|SequenceType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|rmps
operator|.
name|getSequence
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageNumber
operator|!=
literal|null
condition|)
block|{
name|sequence
operator|.
name|getMessageNumber
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|messageNumber
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Identifier
name|id
init|=
name|createMock
argument_list|(
name|Identifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|sequence
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|id
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|identifiers
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|sequences
operator|.
name|add
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
return|return
name|sequence
return|;
block|}
specifier|private
name|SourceSequence
name|setUpSequence
parameter_list|(
name|String
name|sid
parameter_list|,
name|Long
index|[]
name|messageNumbers
parameter_list|,
name|boolean
index|[]
name|isAcked
parameter_list|)
block|{
name|SourceSequence
name|sequence
init|=
name|createMock
argument_list|(
name|SourceSequence
operator|.
name|class
argument_list|)
decl_stmt|;
name|Identifier
name|id
init|=
name|createMock
argument_list|(
name|Identifier
operator|.
name|class
argument_list|)
decl_stmt|;
name|sequence
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|id
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|sid
argument_list|)
expr_stmt|;
name|identifiers
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|boolean
name|includesAcked
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|isAcked
operator|!=
literal|null
operator|&&
name|i
operator|<
name|isAcked
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sequence
operator|.
name|isAcknowledged
argument_list|(
name|messageNumbers
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|isAcked
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAcked
index|[
name|i
index|]
condition|)
block|{
name|includesAcked
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|includesAcked
condition|)
block|{
comment|// Will be called once or twice depending on whether any more
comment|// unacknowledged messages are left for this sequence
name|sequence
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|id
argument_list|)
operator|.
name|times
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// Would be called only when there are no more
comment|// unacknowledged messages left for this sequence
name|id
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|sid
argument_list|)
operator|.
name|times
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|RMStore
name|store
init|=
name|createMock
argument_list|(
name|RMStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|manager
operator|.
name|getStore
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|store
argument_list|)
expr_stmt|;
block|}
return|return
name|sequence
return|;
block|}
comment|/**      * Creates a mock object ensuring it remains referenced, so as to      * avoid garbage collection and attendant issues with finalizer      * calls on mocks.      *       * @param toMock the class to mock up      * @return the mock object      */
parameter_list|<
name|T
parameter_list|>
name|T
name|createMock
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|toMock
parameter_list|)
block|{
name|T
name|ret
init|=
name|control
operator|.
name|createMock
argument_list|(
name|toMock
argument_list|)
decl_stmt|;
name|mocks
operator|.
name|add
argument_list|(
name|ret
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|static
class|class
name|TestResender
implements|implements
name|RetransmissionQueueImpl
operator|.
name|Resender
block|{
name|Message
name|message
decl_stmt|;
name|boolean
name|includeAckRequested
decl_stmt|;
specifier|public
name|void
name|resend
parameter_list|(
name|Message
name|ctx
parameter_list|,
name|boolean
name|requestAcknowledge
parameter_list|)
block|{
name|message
operator|=
name|ctx
expr_stmt|;
name|includeAckRequested
operator|=
name|requestAcknowledge
expr_stmt|;
block|}
name|void
name|clear
parameter_list|()
block|{
name|message
operator|=
literal|null
expr_stmt|;
name|includeAckRequested
operator|=
literal|false
expr_stmt|;
block|}
block|}
empty_stmt|;
block|}
end_class

end_unit

