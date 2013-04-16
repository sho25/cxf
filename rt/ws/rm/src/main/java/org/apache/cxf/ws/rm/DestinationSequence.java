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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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
name|continuations
operator|.
name|SuspendedInvocationException
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
name|io
operator|.
name|CachedOutputStream
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
name|MessageUtils
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
name|rm
operator|.
name|manager
operator|.
name|AcksPolicyType
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
name|manager
operator|.
name|DeliveryAssuranceType
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
name|RMMessage
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
name|policy
operator|.
name|RM10PolicyUtils
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
name|SequenceAcknowledgement
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
name|SequenceAcknowledgement
operator|.
name|AcknowledgementRange
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
operator|.
name|AcknowledgementInterval
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
operator|.
name|InactivityTimeout
import|;
end_import

begin_class
specifier|public
class|class
name|DestinationSequence
extends|extends
name|AbstractSequence
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|DestinationSequence
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Destination
name|destination
decl_stmt|;
specifier|private
name|EndpointReferenceType
name|acksTo
decl_stmt|;
specifier|private
name|long
name|lastMessageNumber
decl_stmt|;
specifier|private
name|SequenceMonitor
name|monitor
decl_stmt|;
specifier|private
name|boolean
name|acknowledgeOnNextOccasion
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DeferredAcknowledgment
argument_list|>
name|deferredAcknowledgments
decl_stmt|;
specifier|private
name|SequenceTermination
name|scheduledTermination
decl_stmt|;
specifier|private
name|String
name|correlationID
decl_stmt|;
specifier|private
specifier|volatile
name|long
name|inProcessNumber
decl_stmt|;
specifier|private
specifier|volatile
name|long
name|highNumberCompleted
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Continuation
argument_list|>
name|continuations
init|=
operator|new
name|LinkedList
argument_list|<
name|Continuation
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Long
argument_list|>
name|deliveringMessageNumbers
init|=
operator|new
name|HashSet
argument_list|<
name|Long
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|DestinationSequence
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|EndpointReferenceType
name|a
parameter_list|,
name|Destination
name|d
parameter_list|,
name|ProtocolVariation
name|pv
parameter_list|)
block|{
name|this
argument_list|(
name|i
argument_list|,
name|a
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
name|pv
argument_list|)
expr_stmt|;
name|destination
operator|=
name|d
expr_stmt|;
block|}
specifier|public
name|DestinationSequence
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|EndpointReferenceType
name|a
parameter_list|,
name|long
name|lmn
parameter_list|,
name|SequenceAcknowledgement
name|ac
parameter_list|,
name|ProtocolVariation
name|pv
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|,
name|pv
argument_list|)
expr_stmt|;
name|acksTo
operator|=
name|a
expr_stmt|;
name|lastMessageNumber
operator|=
name|lmn
expr_stmt|;
name|acknowledgement
operator|=
name|ac
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|acknowledgement
condition|)
block|{
name|acknowledgement
operator|=
operator|new
name|SequenceAcknowledgement
argument_list|()
expr_stmt|;
name|acknowledgement
operator|.
name|setIdentifier
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|monitor
operator|=
operator|new
name|SequenceMonitor
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return the acksTo address for the sequence      */
specifier|public
name|EndpointReferenceType
name|getAcksTo
parameter_list|()
block|{
return|return
name|acksTo
return|;
block|}
comment|/**      * @return the message number of the last message or 0 if the last message had not been received.      */
specifier|public
name|long
name|getLastMessageNumber
parameter_list|()
block|{
return|return
name|lastMessageNumber
return|;
block|}
comment|/**      * @return the sequence acknowledgement presenting the sequences thus far received by a destination       */
specifier|public
name|SequenceAcknowledgement
name|getAcknowledgment
parameter_list|()
block|{
return|return
name|acknowledgement
return|;
block|}
comment|/**      * @return the identifier of the rm destination      */
specifier|public
name|String
name|getEndpointIdentifier
parameter_list|()
block|{
return|return
name|destination
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|void
name|acknowledge
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|SequenceFault
block|{
name|RMProperties
name|rmps
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|SequenceType
name|st
init|=
name|rmps
operator|.
name|getSequence
argument_list|()
decl_stmt|;
name|long
name|messageNumber
init|=
name|st
operator|.
name|getMessageNumber
argument_list|()
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Acknowledging message: "
operator|+
name|messageNumber
argument_list|)
expr_stmt|;
if|if
condition|(
literal|0
operator|!=
name|lastMessageNumber
operator|&&
name|messageNumber
operator|>
name|lastMessageNumber
condition|)
block|{
name|RMConstants
name|consts
init|=
name|getProtocol
argument_list|()
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|SequenceFaultFactory
name|sff
init|=
operator|new
name|SequenceFaultFactory
argument_list|(
name|consts
argument_list|)
decl_stmt|;
throw|throw
name|sff
operator|.
name|createSequenceTerminatedFault
argument_list|(
name|st
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|false
argument_list|)
throw|;
block|}
name|monitor
operator|.
name|acknowledgeMessage
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
init|;
name|i
operator|<
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|AcknowledgementRange
name|r
init|=
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|getLower
argument_list|()
operator|.
name|compareTo
argument_list|(
name|messageNumber
argument_list|)
operator|<=
literal|0
operator|&&
name|r
operator|.
name|getUpper
argument_list|()
operator|.
name|compareTo
argument_list|(
name|messageNumber
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|long
name|diff
init|=
name|r
operator|.
name|getLower
argument_list|()
operator|-
name|messageNumber
decl_stmt|;
if|if
condition|(
name|diff
operator|==
literal|1
condition|)
block|{
name|r
operator|.
name|setLower
argument_list|(
name|messageNumber
argument_list|)
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|diff
operator|>
literal|0
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|messageNumber
operator|-
name|r
operator|.
name|getUpper
argument_list|()
operator|.
name|longValue
argument_list|()
operator|==
literal|1
condition|)
block|{
name|r
operator|.
name|setUpper
argument_list|(
name|messageNumber
argument_list|)
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|done
condition|)
block|{
comment|// need new acknowledgement range
name|AcknowledgementRange
name|range
init|=
operator|new
name|AcknowledgementRange
argument_list|()
decl_stmt|;
name|range
operator|.
name|setLower
argument_list|(
name|messageNumber
argument_list|)
expr_stmt|;
name|range
operator|.
name|setUpper
argument_list|(
name|messageNumber
argument_list|)
expr_stmt|;
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|add
argument_list|(
name|i
argument_list|,
name|range
argument_list|)
expr_stmt|;
if|if
condition|(
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// acknowledge out-of-order at first opportunity
name|scheduleImmediateAcknowledgement
argument_list|()
expr_stmt|;
block|}
block|}
name|mergeRanges
argument_list|()
expr_stmt|;
block|}
name|RMStore
name|store
init|=
name|destination
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
name|RMMessage
name|msg
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|)
argument_list|)
condition|)
block|{
name|msg
operator|=
operator|new
name|RMMessage
argument_list|()
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
operator|(
name|CachedOutputStream
operator|)
name|message
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|SAVED_CONTENT
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setMessageNumber
argument_list|(
name|st
operator|.
name|getMessageNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|store
operator|.
name|persistIncoming
argument_list|(
name|this
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
name|RMAssertion
name|rma
init|=
name|RM10PolicyUtils
operator|.
name|getRMAssertion
argument_list|(
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getRMAssertion
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|long
name|acknowledgementInterval
init|=
literal|0
decl_stmt|;
name|AcknowledgementInterval
name|ai
init|=
name|rma
operator|.
name|getAcknowledgementInterval
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ai
condition|)
block|{
name|Long
name|val
init|=
name|ai
operator|.
name|getMilliseconds
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|val
condition|)
block|{
name|acknowledgementInterval
operator|=
name|val
operator|.
name|longValue
argument_list|()
expr_stmt|;
block|}
block|}
name|scheduleAcknowledgement
argument_list|(
name|acknowledgementInterval
argument_list|)
expr_stmt|;
name|long
name|inactivityTimeout
init|=
literal|0
decl_stmt|;
name|InactivityTimeout
name|iat
init|=
name|rma
operator|.
name|getInactivityTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|iat
condition|)
block|{
name|Long
name|val
init|=
name|iat
operator|.
name|getMilliseconds
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|val
condition|)
block|{
name|inactivityTimeout
operator|=
name|val
operator|.
name|longValue
argument_list|()
expr_stmt|;
block|}
block|}
name|scheduleSequenceTermination
argument_list|(
name|inactivityTimeout
argument_list|)
expr_stmt|;
block|}
name|void
name|mergeRanges
parameter_list|()
block|{
name|List
argument_list|<
name|AcknowledgementRange
argument_list|>
name|ranges
init|=
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|ranges
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|AcknowledgementRange
name|current
init|=
name|ranges
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|AcknowledgementRange
name|previous
init|=
name|ranges
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|.
name|getLower
argument_list|()
operator|.
name|longValue
argument_list|()
operator|-
name|previous
operator|.
name|getUpper
argument_list|()
operator|.
name|longValue
argument_list|()
operator|==
literal|1
condition|)
block|{
name|previous
operator|.
name|setUpper
argument_list|(
name|current
operator|.
name|getUpper
argument_list|()
argument_list|)
expr_stmt|;
name|ranges
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|setDestination
parameter_list|(
name|Destination
name|d
parameter_list|)
block|{
name|destination
operator|=
name|d
expr_stmt|;
block|}
name|Destination
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
comment|/**      * Returns the monitor for this sequence.      *       * @return the sequence monitor.      */
name|SequenceMonitor
name|getMonitor
parameter_list|()
block|{
return|return
name|monitor
return|;
block|}
name|void
name|setLastMessageNumber
parameter_list|(
name|long
name|lmn
parameter_list|)
block|{
name|lastMessageNumber
operator|=
name|lmn
expr_stmt|;
block|}
name|boolean
name|canPiggybackAckOnPartialResponse
parameter_list|()
block|{
comment|// TODO: should also check if we allow breaking the WI Profile rule by which no headers
comment|// can be included in a HTTP response
return|return
name|getAcksTo
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|RMUtils
operator|.
name|getAddressingConstants
argument_list|()
operator|.
name|getAnonymousURI
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Ensures that the delivery assurance is honored, e.g. by throwing an       * exception if the message had already been delivered and the delivery      * assurance is AtMostOnce.      * If the delivery assurance includes either AtLeastOnce or ExactlyOnce, combined with InOrder, this      * queues out-of-order messages for processing after the missing messages have been received.      *       * @param mn message number      * @return<code>true</code> if message processing to continue,<code>false</code> if to be dropped      * @throws RMException if message had already been acknowledged      */
name|boolean
name|applyDeliveryAssurance
parameter_list|(
name|long
name|mn
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|RMException
block|{
name|Continuation
name|cont
init|=
name|getContinuation
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|DeliveryAssuranceType
name|da
init|=
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getDeliveryAssurance
argument_list|()
decl_stmt|;
name|boolean
name|canSkip
init|=
operator|!
name|da
operator|.
name|isSetAtLeastOnce
argument_list|()
operator|&&
operator|!
name|da
operator|.
name|isSetExactlyOnce
argument_list|()
decl_stmt|;
name|boolean
name|robust
init|=
literal|false
decl_stmt|;
name|boolean
name|robustDelivering
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|robust
operator|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|robust
condition|)
block|{
name|robustDelivering
operator|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|DELIVERING_ROBUST_ONEWAY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|robust
operator|&&
operator|!
name|robustDelivering
condition|)
block|{
comment|// no check performed if in robust and not in delivering
name|removeDeliveringMessageNumber
argument_list|(
name|mn
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|cont
operator|!=
literal|null
operator|&&
name|da
operator|.
name|isSetInOrder
argument_list|()
operator|&&
operator|!
name|cont
operator|.
name|isNew
argument_list|()
condition|)
block|{
return|return
name|waitInQueue
argument_list|(
name|mn
argument_list|,
name|canSkip
argument_list|,
name|message
argument_list|,
name|cont
argument_list|)
return|;
block|}
if|if
condition|(
operator|(
name|da
operator|.
name|isSetExactlyOnce
argument_list|()
operator|||
name|da
operator|.
name|isSetAtMostOnce
argument_list|()
operator|)
operator|&&
operator|(
name|isAcknowledged
argument_list|(
name|mn
argument_list|)
operator|||
operator|(
name|robustDelivering
operator|&&
name|deliveringMessageNumbers
operator|.
name|contains
argument_list|(
name|mn
argument_list|)
operator|)
operator|)
condition|)
block|{
comment|// acknowledge at first opportunity following duplicate message
name|scheduleImmediateAcknowledgement
argument_list|()
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|msg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"MESSAGE_ALREADY_DELIVERED_EXC"
argument_list|,
name|LOG
argument_list|,
name|mn
argument_list|,
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RMException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
if|if
condition|(
name|robustDelivering
condition|)
block|{
name|deliveringMessageNumbers
operator|.
name|add
argument_list|(
name|mn
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|da
operator|.
name|isSetInOrder
argument_list|()
condition|)
block|{
return|return
name|waitInQueue
argument_list|(
name|mn
argument_list|,
name|canSkip
argument_list|,
name|message
argument_list|,
name|cont
argument_list|)
return|;
block|}
return|return
literal|true
return|;
block|}
name|void
name|removeDeliveringMessageNumber
parameter_list|(
name|long
name|mn
parameter_list|)
block|{
name|deliveringMessageNumbers
operator|.
name|remove
argument_list|(
name|mn
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Continuation
name|getContinuation
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|message
operator|.
name|get
argument_list|(
name|Continuation
operator|.
name|class
argument_list|)
return|;
block|}
specifier|synchronized
name|boolean
name|waitInQueue
parameter_list|(
name|long
name|mn
parameter_list|,
name|boolean
name|canSkip
parameter_list|,
name|Message
name|message
parameter_list|,
name|Continuation
name|continuation
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
comment|// can process now if no other in process and this one is next
if|if
condition|(
name|inProcessNumber
operator|==
literal|0
condition|)
block|{
name|long
name|diff
init|=
name|mn
operator|-
name|highNumberCompleted
decl_stmt|;
if|if
condition|(
name|diff
operator|==
literal|1
operator|||
operator|(
name|canSkip
operator|&&
name|diff
operator|>
literal|0
operator|)
condition|)
block|{
name|inProcessNumber
operator|=
name|mn
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// can abort now if same message in process or already processed
if|if
condition|(
name|mn
operator|==
name|inProcessNumber
operator|||
name|isAcknowledged
argument_list|(
name|mn
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|continuation
operator|==
literal|null
condition|)
block|{
name|ContinuationProvider
name|p
init|=
name|message
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|boolean
name|isOneWay
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
decl_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOneWay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|continuation
operator|=
name|p
operator|.
name|getContinuation
argument_list|()
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOneWay
argument_list|(
name|isOneWay
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Continuation
operator|.
name|class
argument_list|,
name|continuation
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|continuation
operator|!=
literal|null
condition|)
block|{
name|continuation
operator|.
name|setObject
argument_list|(
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|continuation
operator|.
name|suspend
argument_list|(
operator|-
literal|1
argument_list|)
condition|)
block|{
name|continuations
operator|.
name|add
argument_list|(
name|continuation
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SuspendedInvocationException
argument_list|()
throw|;
block|}
block|}
try|try
block|{
comment|//if we get here, there isn't a continuation available
comment|//so we need to block/wait
name|wait
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
block|}
specifier|synchronized
name|void
name|wakeupAll
parameter_list|()
block|{
while|while
condition|(
operator|!
name|continuations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Continuation
name|c
init|=
name|continuations
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|c
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
name|notifyAll
argument_list|()
expr_stmt|;
block|}
specifier|synchronized
name|void
name|processingComplete
parameter_list|(
name|long
name|mn
parameter_list|)
block|{
name|inProcessNumber
operator|=
literal|0
expr_stmt|;
name|highNumberCompleted
operator|=
name|mn
expr_stmt|;
name|wakeupAll
argument_list|()
expr_stmt|;
block|}
name|void
name|purgeAcknowledged
parameter_list|(
name|long
name|messageNr
parameter_list|)
block|{
name|RMStore
name|store
init|=
name|destination
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
operator|==
name|store
condition|)
block|{
return|return;
block|}
name|store
operator|.
name|removeMessages
argument_list|(
name|getIdentifier
argument_list|()
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|messageNr
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called after an acknowledgement header for this sequence has been added to an outgoing message.      */
name|void
name|acknowledgmentSent
parameter_list|()
block|{
name|acknowledgeOnNextOccasion
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|boolean
name|sendAcknowledgement
parameter_list|()
block|{
return|return
name|acknowledgeOnNextOccasion
return|;
block|}
name|List
argument_list|<
name|DeferredAcknowledgment
argument_list|>
name|getDeferredAcknowledgements
parameter_list|()
block|{
return|return
name|deferredAcknowledgments
return|;
block|}
comment|/**      * The correlation of the incoming CreateSequence call used to create this      * sequence is recorded so that in the absence of an offer, the corresponding      * outgoing CreateSeqeunce can be correlated.      */
name|void
name|setCorrelationID
parameter_list|(
name|String
name|cid
parameter_list|)
block|{
name|correlationID
operator|=
name|cid
expr_stmt|;
block|}
name|String
name|getCorrelationID
parameter_list|()
block|{
return|return
name|correlationID
return|;
block|}
name|void
name|scheduleAcknowledgement
parameter_list|(
name|long
name|acknowledgementInterval
parameter_list|)
block|{
name|AcksPolicyType
name|ap
init|=
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getDestinationPolicy
argument_list|()
operator|.
name|getAcksPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|acknowledgementInterval
operator|>
literal|0
operator|&&
name|getMonitor
argument_list|()
operator|.
name|getMPM
argument_list|()
operator|>=
operator|(
name|ap
operator|==
literal|null
condition|?
literal|10
else|:
name|ap
operator|.
name|getIntraMessageThreshold
argument_list|()
operator|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Schedule deferred acknowledgment"
argument_list|)
expr_stmt|;
name|scheduleDeferredAcknowledgement
argument_list|(
name|acknowledgementInterval
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Schedule immediate acknowledgment"
argument_list|)
expr_stmt|;
name|scheduleImmediateAcknowledgement
argument_list|()
expr_stmt|;
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getTimer
argument_list|()
operator|.
name|schedule
argument_list|(
operator|new
name|ImmediateFallbackAcknowledgment
argument_list|()
argument_list|,
name|ap
operator|==
literal|null
condition|?
literal|1000L
else|:
name|ap
operator|.
name|getImmediaAcksTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|scheduleImmediateAcknowledgement
parameter_list|()
block|{
name|acknowledgeOnNextOccasion
operator|=
literal|true
expr_stmt|;
block|}
specifier|synchronized
name|void
name|scheduleSequenceTermination
parameter_list|(
name|long
name|inactivityTimeout
parameter_list|)
block|{
if|if
condition|(
name|inactivityTimeout
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
name|boolean
name|scheduled
init|=
literal|null
operator|!=
name|scheduledTermination
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|scheduledTermination
condition|)
block|{
name|scheduledTermination
operator|=
operator|new
name|SequenceTermination
argument_list|()
expr_stmt|;
block|}
name|scheduledTermination
operator|.
name|updateInactivityTimeout
argument_list|(
name|inactivityTimeout
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|scheduled
condition|)
block|{
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getTimer
argument_list|()
operator|.
name|schedule
argument_list|(
name|scheduledTermination
argument_list|,
name|inactivityTimeout
argument_list|)
expr_stmt|;
block|}
block|}
specifier|synchronized
name|void
name|scheduleDeferredAcknowledgement
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|deferredAcknowledgments
condition|)
block|{
name|deferredAcknowledgments
operator|=
operator|new
name|ArrayList
argument_list|<
name|DeferredAcknowledgment
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|long
name|expectedExecutionTime
init|=
name|now
operator|+
name|delay
decl_stmt|;
for|for
control|(
name|DeferredAcknowledgment
name|da
range|:
name|deferredAcknowledgments
control|)
block|{
if|if
condition|(
name|da
operator|.
name|scheduledExecutionTime
argument_list|()
operator|<=
name|expectedExecutionTime
condition|)
block|{
return|return;
block|}
block|}
name|DeferredAcknowledgment
name|da
init|=
operator|new
name|DeferredAcknowledgment
argument_list|()
decl_stmt|;
name|deferredAcknowledgments
operator|.
name|add
argument_list|(
name|da
argument_list|)
expr_stmt|;
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getTimer
argument_list|()
operator|.
name|schedule
argument_list|(
name|da
argument_list|,
name|delay
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Scheduled acknowledgment to be sent in "
operator|+
name|delay
operator|+
literal|" ms"
argument_list|)
expr_stmt|;
block|}
specifier|synchronized
name|void
name|cancelDeferredAcknowledgments
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|deferredAcknowledgments
condition|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|i
init|=
name|deferredAcknowledgments
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|DeferredAcknowledgment
name|da
init|=
name|deferredAcknowledgments
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|da
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
specifier|synchronized
name|void
name|cancelTermination
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|scheduledTermination
condition|)
block|{
name|scheduledTermination
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
specifier|final
class|class
name|DeferredAcknowledgment
extends|extends
name|TimerTask
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"timer task: send acknowledgment."
argument_list|)
expr_stmt|;
name|DestinationSequence
operator|.
name|this
operator|.
name|scheduleImmediateAcknowledgement
argument_list|()
expr_stmt|;
try|try
block|{
name|RMEndpoint
name|rme
init|=
name|destination
operator|.
name|getReliableEndpoint
argument_list|()
decl_stmt|;
name|Proxy
name|proxy
init|=
name|rme
operator|.
name|getProxy
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|acknowledge
argument_list|(
name|DestinationSequence
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RMException
name|ex
parameter_list|)
block|{
comment|// already logged
block|}
finally|finally
block|{
synchronized|synchronized
init|(
name|DestinationSequence
operator|.
name|this
init|)
block|{
name|DestinationSequence
operator|.
name|this
operator|.
name|deferredAcknowledgments
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|final
class|class
name|ImmediateFallbackAcknowledgment
extends|extends
name|TimerTask
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"timer task: send acknowledgment."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sendAcknowledgement
argument_list|()
condition|)
block|{
comment|//Acknowledgment already get send out
return|return;
block|}
try|try
block|{
name|destination
operator|.
name|getReliableEndpoint
argument_list|()
operator|.
name|getProxy
argument_list|()
operator|.
name|acknowledge
argument_list|(
name|DestinationSequence
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RMException
name|ex
parameter_list|)
block|{
comment|// already logged
block|}
block|}
block|}
specifier|final
class|class
name|SequenceTermination
extends|extends
name|TimerTask
block|{
specifier|private
name|long
name|maxInactivityTimeout
decl_stmt|;
name|void
name|updateInactivityTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|maxInactivityTimeout
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxInactivityTimeout
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|DestinationSequence
operator|.
name|this
init|)
block|{
name|DestinationSequence
operator|.
name|this
operator|.
name|scheduledTermination
operator|=
literal|null
expr_stmt|;
name|RMEndpoint
name|rme
init|=
name|destination
operator|.
name|getReliableEndpoint
argument_list|()
decl_stmt|;
name|long
name|lat
init|=
name|Math
operator|.
name|max
argument_list|(
name|rme
operator|.
name|getLastControlMessage
argument_list|()
argument_list|,
name|rme
operator|.
name|getLastApplicationMessage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|==
name|lat
condition|)
block|{
return|return;
block|}
name|long
name|now
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|now
operator|-
name|lat
operator|>=
name|maxInactivityTimeout
condition|)
block|{
comment|// terminate regardless outstanding acknowledgments - as we assume that the client is
comment|// gone there is no point in sending a SequenceAcknowledgment
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|Level
operator|.
name|WARNING
argument_list|,
literal|"TERMINATING_INACTIVE_SEQ_MSG"
argument_list|,
name|DestinationSequence
operator|.
name|this
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|DestinationSequence
operator|.
name|this
operator|.
name|destination
operator|.
name|removeSequence
argument_list|(
name|DestinationSequence
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// reschedule
name|SequenceTermination
name|st
init|=
operator|new
name|SequenceTermination
argument_list|()
decl_stmt|;
name|st
operator|.
name|updateInactivityTimeout
argument_list|(
name|maxInactivityTimeout
argument_list|)
expr_stmt|;
name|DestinationSequence
operator|.
name|this
operator|.
name|destination
operator|.
name|getManager
argument_list|()
operator|.
name|getTimer
argument_list|()
operator|.
name|schedule
argument_list|(
name|st
argument_list|,
name|maxInactivityTimeout
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

