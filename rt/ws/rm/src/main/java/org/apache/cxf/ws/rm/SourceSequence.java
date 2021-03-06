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
name|Date
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
name|javax
operator|.
name|xml
operator|.
name|datatype
operator|.
name|Duration
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
name|jaxb
operator|.
name|DatatypeFactory
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
name|ContextUtils
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
name|SequenceTerminationPolicyType
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
name|Expires
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

begin_comment
comment|// TODO: handle lastMessage
end_comment

begin_class
specifier|public
class|class
name|SourceSequence
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
name|SourceSequence
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Date
name|expires
decl_stmt|;
specifier|private
name|Source
name|source
decl_stmt|;
specifier|private
name|long
name|currentMessageNumber
decl_stmt|;
specifier|private
name|boolean
name|lastMessage
decl_stmt|;
specifier|private
name|Identifier
name|offeringId
decl_stmt|;
specifier|private
name|EndpointReferenceType
name|target
decl_stmt|;
specifier|public
name|SourceSequence
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|ProtocolVariation
name|pv
parameter_list|)
block|{
name|this
argument_list|(
name|i
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|pv
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SourceSequence
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|Date
name|e
parameter_list|,
name|Identifier
name|oi
parameter_list|,
name|ProtocolVariation
name|pv
parameter_list|)
block|{
name|this
argument_list|(
name|i
argument_list|,
name|e
argument_list|,
name|oi
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|,
name|pv
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SourceSequence
parameter_list|(
name|Identifier
name|i
parameter_list|,
name|Date
name|e
parameter_list|,
name|Identifier
name|oi
parameter_list|,
name|long
name|cmn
parameter_list|,
name|boolean
name|lm
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
name|expires
operator|=
name|e
expr_stmt|;
name|offeringId
operator|=
name|oi
expr_stmt|;
name|currentMessageNumber
operator|=
name|cmn
expr_stmt|;
name|lastMessage
operator|=
name|lm
expr_stmt|;
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
comment|/**      * @return the message number assigned to the most recent outgoing      *         application message.      */
specifier|public
name|long
name|getCurrentMessageNr
parameter_list|()
block|{
return|return
name|currentMessageNumber
return|;
block|}
comment|/**      * @return true if the last message had been sent for this sequence.      */
specifier|public
name|boolean
name|isLastMessage
parameter_list|()
block|{
return|return
name|lastMessage
return|;
block|}
comment|/**      * @return the identifier of the sequence that was created on behalf of the      *         CreateSequence request that included this sequence as an offer      */
specifier|public
name|Identifier
name|getOfferingSequenceIdentifier
parameter_list|()
block|{
return|return
name|offeringId
return|;
block|}
comment|/**      * @return the identifier of the rm source      */
specifier|public
name|String
name|getEndpointIdentifier
parameter_list|()
block|{
return|return
name|source
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * @return the expiry data of this sequence      */
specifier|public
name|Date
name|getExpires
parameter_list|()
block|{
return|return
name|expires
return|;
block|}
comment|/**      * Returns true if this sequence was constructed from an offer for an      * inbound sequence includes in the CreateSequenceRequest in response to      * which the sequence with the specified identifier was created.      *      * @param sid the sequence identifier      * @return true if the sequence was constructed from an offer.      */
specifier|public
name|boolean
name|offeredBy
parameter_list|(
name|Identifier
name|sid
parameter_list|)
block|{
return|return
literal|null
operator|!=
name|offeringId
operator|&&
name|offeringId
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|sid
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns true if any messages other than the number supplied are waiting for acknowledgment.      *      * @param num message number to check      * @return true if all messages have been acknowledged.      */
specifier|public
name|boolean
name|needAcknowledge
parameter_list|(
name|long
name|num
parameter_list|)
block|{
if|if
condition|(
name|currentMessageNumber
operator|!=
name|num
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
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
literal|0
argument_list|)
decl_stmt|;
return|return
name|r
operator|.
name|getLower
argument_list|()
operator|.
name|longValue
argument_list|()
operator|!=
literal|1
operator|||
name|r
operator|.
name|getUpper
argument_list|()
operator|.
name|longValue
argument_list|()
operator|<
operator|(
name|num
operator|-
literal|1
operator|)
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Returns true if a last message had been sent for this sequence and if all      * messages for this sequence have been acknowledged.      *      * @return true if all messages have been acknowledged.      */
specifier|public
name|boolean
name|allAcknowledged
parameter_list|()
block|{
if|if
condition|(
operator|!
name|lastMessage
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
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
literal|0
argument_list|)
decl_stmt|;
return|return
name|r
operator|.
name|getLower
argument_list|()
operator|.
name|longValue
argument_list|()
operator|==
literal|1
operator|&&
name|r
operator|.
name|getUpper
argument_list|()
operator|.
name|longValue
argument_list|()
operator|==
name|currentMessageNumber
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Used by the RM source to cache received acknowledgements for this      * sequence.      *      * @param ack an acknowledgement for this sequence      */
specifier|public
name|void
name|setAcknowledged
parameter_list|(
name|SequenceAcknowledgement
name|ack
parameter_list|)
throws|throws
name|RMException
block|{
name|acknowledgement
operator|=
name|ack
expr_stmt|;
name|source
operator|.
name|getManager
argument_list|()
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|purgeAcknowledged
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|allAcknowledged
argument_list|()
condition|)
block|{
if|if
condition|(
literal|null
operator|==
name|target
operator|||
name|RMUtils
operator|.
name|getAddressingConstants
argument_list|()
operator|.
name|getAnonymousURI
argument_list|()
operator|.
name|equals
argument_list|(
name|target
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"STANDALONE_ANON_TERMINATE_SEQUENCE_MSG"
argument_list|)
expr_stmt|;
comment|// keep the sequence and let RMOutInterceptor remove it after building the TS message
comment|// if we remove the sequence here, RMOutInterceptor should check for a null sequence
block|}
else|else
block|{
name|RMEndpoint
name|rme
init|=
name|source
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
comment|// REVIST for rm 1.1, provide an option to how the close and terminate messages are sent
if|if
condition|(
name|ProtocolVariation
operator|.
name|RM11WSA200508
operator|==
name|getProtocol
argument_list|()
condition|)
block|{
name|proxy
operator|.
name|lastMessage
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|proxy
operator|.
name|terminate
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|source
operator|.
name|removeSequence
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns the source associated with this source sequence.      *      * @return the source.      */
specifier|public
name|Source
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
name|void
name|setSource
parameter_list|(
name|Source
name|s
parameter_list|)
block|{
name|source
operator|=
name|s
expr_stmt|;
block|}
name|void
name|setLastMessage
parameter_list|(
name|boolean
name|lm
parameter_list|)
block|{
name|lastMessage
operator|=
name|lm
expr_stmt|;
block|}
comment|/**      * Returns true if the sequence is expired.      *      * @return true if the sequence is expired.      */
name|boolean
name|isExpired
parameter_list|()
block|{
return|return
name|expires
operator|!=
literal|null
operator|&&
operator|new
name|Date
argument_list|()
operator|.
name|after
argument_list|(
name|expires
argument_list|)
return|;
block|}
specifier|public
name|void
name|setExpires
parameter_list|(
name|Expires
name|ex
parameter_list|)
block|{
name|Duration
name|d
init|=
literal|null
decl_stmt|;
name|expires
operator|=
literal|null
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ex
condition|)
block|{
name|d
operator|=
name|ex
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|d
operator|&&
operator|!
name|d
operator|.
name|equals
argument_list|(
name|DatatypeFactory
operator|.
name|PT0S
argument_list|)
condition|)
block|{
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|expires
operator|=
operator|new
name|Date
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
operator|+
name|ex
operator|.
name|getValue
argument_list|()
operator|.
name|getTimeInMillis
argument_list|(
name|now
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the next message number and increases the message number.      *      * @return the next message number.      */
name|long
name|nextMessageNumber
parameter_list|()
block|{
return|return
name|nextMessageNumber
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Returns the next message number and increases the message number. The      * parameters, if not null, indicate that this message is being sent as a      * response to the message with the specified message number in the sequence      * specified by the by the identifier, and are used to decide if this      * message should be the last in this sequence.      *      * @return the next message number.      */
specifier|public
name|long
name|nextMessageNumber
parameter_list|(
name|Identifier
name|inSeqId
parameter_list|,
name|long
name|inMsgNumber
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
assert|assert
operator|!
name|lastMessage
assert|;
name|long
name|result
init|=
literal|0
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|currentMessageNumber
operator|++
expr_stmt|;
if|if
condition|(
name|last
condition|)
block|{
name|lastMessage
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|checkLastMessage
argument_list|(
name|inSeqId
argument_list|,
name|inMsgNumber
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|currentMessageNumber
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
name|SequenceAcknowledgement
name|getAcknowledgement
parameter_list|()
block|{
return|return
name|acknowledgement
return|;
block|}
comment|/**      * The target for the sequence is the first non-anonymous address that a      * message is sent to as part of this sequence. It is subsequently used for      * as the target of out-of-band protocol messages related to that sequence      * that originate from the sequnce source (i.e. TerminateSequence and      * LastMessage, but not AckRequested or SequenceAcknowledgement as these are      * orignate from the sequence destination).      *      * @param to      */
specifier|public
specifier|synchronized
name|void
name|setTarget
parameter_list|(
name|EndpointReferenceType
name|to
parameter_list|)
block|{
if|if
condition|(
name|target
operator|==
literal|null
operator|&&
operator|!
name|ContextUtils
operator|.
name|isGenericAddress
argument_list|(
name|to
argument_list|)
condition|)
block|{
name|target
operator|=
name|to
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|EndpointReferenceType
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
comment|/**      * Checks if the current message should be the last message in this sequence      * and if so sets the lastMessageNumber property.      */
specifier|private
name|void
name|checkLastMessage
parameter_list|(
name|Identifier
name|inSeqId
parameter_list|,
name|long
name|inMsgNumber
parameter_list|)
block|{
comment|// check if this is a response to a message that was is the last message
comment|// in the sequence
comment|// that included this sequence as an offer
if|if
condition|(
literal|null
operator|!=
name|inSeqId
operator|&&
literal|0
operator|!=
name|inMsgNumber
condition|)
block|{
name|Destination
name|destination
init|=
name|source
operator|.
name|getReliableEndpoint
argument_list|()
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|DestinationSequence
name|inSeq
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|destination
condition|)
block|{
name|inSeq
operator|=
name|destination
operator|.
name|getSequence
argument_list|(
name|inSeqId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|inSeq
operator|&&
name|offeredBy
argument_list|(
name|inSeqId
argument_list|)
operator|&&
name|inMsgNumber
operator|==
name|inSeq
operator|.
name|getLastMessageNumber
argument_list|()
condition|)
block|{
name|lastMessage
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|lastMessage
condition|)
block|{
name|SequenceTerminationPolicyType
name|stp
init|=
name|source
operator|.
name|getManager
argument_list|()
operator|.
name|getSourcePolicy
argument_list|()
operator|.
name|getSequenceTerminationPolicy
argument_list|()
decl_stmt|;
assert|assert
literal|null
operator|!=
name|stp
assert|;
if|if
condition|(
operator|(
name|stp
operator|.
name|getMaxLength
argument_list|()
operator|!=
literal|0
operator|&&
name|stp
operator|.
name|getMaxLength
argument_list|()
operator|<=
name|currentMessageNumber
operator|)
operator|||
operator|(
name|stp
operator|.
name|getMaxRanges
argument_list|()
operator|>
literal|0
operator|&&
name|acknowledgement
operator|.
name|getAcknowledgementRange
argument_list|()
operator|.
name|size
argument_list|()
operator|>=
name|stp
operator|.
name|getMaxRanges
argument_list|()
operator|)
operator|||
operator|(
name|stp
operator|.
name|getMaxUnacknowledged
argument_list|()
operator|>
literal|0
operator|&&
name|source
operator|.
name|getManager
argument_list|()
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|countUnacknowledged
argument_list|(
name|this
argument_list|)
operator|>=
name|stp
operator|.
name|getMaxUnacknowledged
argument_list|()
operator|)
condition|)
block|{
name|lastMessage
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
operator|&&
name|lastMessage
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|currentMessageNumber
operator|+
literal|" should be the last message in this sequence."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

