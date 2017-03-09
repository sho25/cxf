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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
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
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
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
name|helpers
operator|.
name|CastUtils
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

begin_class
specifier|public
class|class
name|Source
extends|extends
name|AbstractEndpoint
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REQUESTOR_SEQUENCE_ID
init|=
literal|""
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|SourceSequence
argument_list|>
name|map
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|SourceSequence
argument_list|>
name|current
decl_stmt|;
specifier|private
name|Lock
name|sequenceCreationLock
decl_stmt|;
specifier|private
name|Condition
name|sequenceCreationCondition
decl_stmt|;
specifier|private
name|boolean
name|sequenceCreationNotified
decl_stmt|;
name|Source
parameter_list|(
name|RMEndpoint
name|reliableEndpoint
parameter_list|)
block|{
name|super
argument_list|(
name|reliableEndpoint
argument_list|)
expr_stmt|;
name|map
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|SourceSequence
argument_list|>
argument_list|()
expr_stmt|;
name|current
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|sequenceCreationLock
operator|=
operator|new
name|ReentrantLock
argument_list|()
expr_stmt|;
name|sequenceCreationCondition
operator|=
name|sequenceCreationLock
operator|.
name|newCondition
argument_list|()
expr_stmt|;
block|}
specifier|public
name|SourceSequence
name|getSequence
parameter_list|(
name|Identifier
name|id
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|id
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SourceSequence
argument_list|>
name|getAllSequences
parameter_list|()
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|addSequence
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
block|{
name|addSequence
argument_list|(
name|seq
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addSequence
parameter_list|(
name|SourceSequence
name|seq
parameter_list|,
name|boolean
name|persist
parameter_list|)
block|{
name|seq
operator|.
name|setSource
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|seq
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|seq
argument_list|)
expr_stmt|;
if|if
condition|(
name|persist
condition|)
block|{
name|RMStore
name|store
init|=
name|getReliableEndpoint
argument_list|()
operator|.
name|getManager
argument_list|()
operator|.
name|getStore
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|store
condition|)
block|{
name|store
operator|.
name|createSourceSequence
argument_list|(
name|seq
argument_list|)
expr_stmt|;
block|}
block|}
name|processingSequenceCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|removeSequence
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
block|{
name|SourceSequence
name|o
decl_stmt|;
name|o
operator|=
name|map
operator|.
name|remove
argument_list|(
name|seq
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|RMStore
name|store
init|=
name|getReliableEndpoint
argument_list|()
operator|.
name|getManager
argument_list|()
operator|.
name|getStore
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|store
condition|)
block|{
name|store
operator|.
name|removeSourceSequence
argument_list|(
name|seq
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|processingSequenceCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
name|completedSequenceCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns a collection of all sequences for which have not yet been      * completely acknowledged.      *      * @return the collection of unacknowledged sequences.      */
specifier|public
name|Collection
argument_list|<
name|SourceSequence
argument_list|>
name|getAllUnacknowledgedSequences
parameter_list|()
block|{
name|Collection
argument_list|<
name|SourceSequence
argument_list|>
name|seqs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SourceSequence
name|seq
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|seq
operator|.
name|allAcknowledged
argument_list|()
condition|)
block|{
name|seqs
operator|.
name|add
argument_list|(
name|seq
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|seqs
return|;
block|}
comment|/**      * Returns the current sequence used by a client side source.      *      * @return the current sequence.      */
name|SourceSequence
name|getCurrent
parameter_list|()
block|{
return|return
name|getCurrent
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Sets the current sequence used by a client side source.      * @param s the current sequence.      */
specifier|public
name|void
name|setCurrent
parameter_list|(
name|SourceSequence
name|s
parameter_list|)
block|{
name|setCurrent
argument_list|(
literal|null
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the current sequence used by a server side source for responses to a message      * sent as part of the inbound sequence with the specified identifier.      *      * @return the current sequence.      */
name|SourceSequence
name|getCurrent
parameter_list|(
name|Identifier
name|i
parameter_list|)
block|{
name|sequenceCreationLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|getAssociatedSequence
argument_list|(
name|i
argument_list|)
return|;
block|}
finally|finally
block|{
name|sequenceCreationLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the sequence associated with the given identifier.      *      * @param i the corresponding sequence identifier      * @return the associated sequence      * @pre the sequenceCreationLock is already held      */
name|SourceSequence
name|getAssociatedSequence
parameter_list|(
name|Identifier
name|i
parameter_list|)
block|{
return|return
name|current
operator|.
name|get
argument_list|(
name|i
operator|==
literal|null
condition|?
name|REQUESTOR_SEQUENCE_ID
else|:
name|i
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Await the availability of a sequence corresponding to the given identifier.      *      * @param i the sequence identifier      * @return      */
name|SourceSequence
name|awaitCurrent
parameter_list|(
name|Identifier
name|i
parameter_list|)
block|{
name|sequenceCreationLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|SourceSequence
name|seq
init|=
name|getAssociatedSequence
argument_list|(
name|i
argument_list|)
decl_stmt|;
while|while
condition|(
name|seq
operator|==
literal|null
condition|)
block|{
while|while
condition|(
operator|!
name|sequenceCreationNotified
condition|)
block|{
try|try
block|{
name|sequenceCreationCondition
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|seq
operator|=
name|getAssociatedSequence
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|seq
return|;
block|}
finally|finally
block|{
name|sequenceCreationLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Sets the current sequence used by a server side source for responses to a message      * sent as part of the inbound sequence with the specified identifier.      * @param s the current sequence.      */
name|void
name|setCurrent
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|SourceSequence
name|s
parameter_list|)
block|{
name|sequenceCreationLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|current
operator|.
name|put
argument_list|(
name|i
operator|==
literal|null
condition|?
name|REQUESTOR_SEQUENCE_ID
else|:
name|i
operator|.
name|getValue
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|sequenceCreationNotified
operator|=
literal|true
expr_stmt|;
name|sequenceCreationCondition
operator|.
name|signal
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|sequenceCreationLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

