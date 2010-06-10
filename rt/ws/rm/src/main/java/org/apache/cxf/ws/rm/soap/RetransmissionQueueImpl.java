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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|java
operator|.
name|net
operator|.
name|ConnectException
import|;
end_import

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
name|Arrays
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
name|Date
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
name|List
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
name|TimerTask
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
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
name|endpoint
operator|.
name|ConduitSelector
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
name|DeferredConduitSelector
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
name|helpers
operator|.
name|IOUtils
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
name|io
operator|.
name|CachedOutputStreamCallback
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
name|transport
operator|.
name|Conduit
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
name|transport
operator|.
name|MessageObserver
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
name|builder
operator|.
name|jaxb
operator|.
name|JaxbAssertion
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
name|RMContextUtils
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
name|RMUtils
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
name|RetransmissionCallback
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
name|RetransmissionQueue
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
name|policy
operator|.
name|PolicyUtils
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
name|RMAssertion
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|RetransmissionQueueImpl
implements|implements
name|RetransmissionQueue
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
name|RetransmissionQueueImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ResendCandidate
argument_list|>
argument_list|>
name|candidates
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ResendCandidate
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Resender
name|resender
decl_stmt|;
specifier|private
name|RMManager
name|manager
decl_stmt|;
specifier|public
name|RetransmissionQueueImpl
parameter_list|(
name|RMManager
name|m
parameter_list|)
block|{
name|manager
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|RMManager
name|getManager
parameter_list|()
block|{
return|return
name|manager
return|;
block|}
specifier|public
name|void
name|setManager
parameter_list|(
name|RMManager
name|m
parameter_list|)
block|{
name|manager
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|void
name|addUnacknowledged
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|cacheUnacknowledged
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param seq the sequence under consideration      * @return the number of unacknowledged messages for that sequence      */
specifier|public
specifier|synchronized
name|int
name|countUnacknowledged
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
block|{
name|List
argument_list|<
name|ResendCandidate
argument_list|>
name|sequenceCandidates
init|=
name|getSequenceCandidates
argument_list|(
name|seq
argument_list|)
decl_stmt|;
return|return
name|sequenceCandidates
operator|==
literal|null
condition|?
literal|0
else|:
name|sequenceCandidates
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * @return true if there are no unacknowledged messages in the queue      */
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|0
operator|==
name|getUnacknowledged
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Purge all candidates for the given sequence that have been acknowledged.      *       * @param seq the sequence object.      */
specifier|public
name|void
name|purgeAcknowledged
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
block|{
name|Collection
argument_list|<
name|BigInteger
argument_list|>
name|purged
init|=
operator|new
name|ArrayList
argument_list|<
name|BigInteger
argument_list|>
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Start purging resend candidates."
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ResendCandidate
argument_list|>
name|sequenceCandidates
init|=
name|getSequenceCandidates
argument_list|(
name|seq
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|sequenceCandidates
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
name|sequenceCandidates
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
name|ResendCandidate
name|candidate
init|=
name|sequenceCandidates
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|RMProperties
name|properties
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|candidate
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|SequenceType
name|st
init|=
name|properties
operator|.
name|getSequence
argument_list|()
decl_stmt|;
name|BigInteger
name|m
init|=
name|st
operator|.
name|getMessageNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|seq
operator|.
name|isAcknowledged
argument_list|(
name|m
argument_list|)
condition|)
block|{
name|sequenceCandidates
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|candidate
operator|.
name|resolved
argument_list|()
expr_stmt|;
name|purged
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sequenceCandidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|candidates
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
block|}
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Completed purging resend candidates."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|purged
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|RMStore
name|store
init|=
name|manager
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
name|removeMessages
argument_list|(
name|seq
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|purged
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Initiate resends.      */
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|resender
condition|)
block|{
return|return;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Starting retransmission queue"
argument_list|)
expr_stmt|;
comment|// setup resender
name|resender
operator|=
name|getDefaultResender
argument_list|()
expr_stmt|;
block|}
comment|/**      * Stops resending messages for the specified source sequence.      */
specifier|public
name|void
name|stop
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|List
argument_list|<
name|ResendCandidate
argument_list|>
name|sequenceCandidates
init|=
name|getSequenceCandidates
argument_list|(
name|seq
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|sequenceCandidates
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
name|sequenceCandidates
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
name|ResendCandidate
name|candidate
init|=
name|sequenceCandidates
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|candidate
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Cancelled resends for sequence {0}."
argument_list|,
name|seq
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|stop
parameter_list|()
block|{              }
comment|/**      * @return the exponential backoff      */
specifier|protected
name|int
name|getExponentialBackoff
parameter_list|()
block|{
return|return
name|DEFAULT_EXPONENTIAL_BACKOFF
return|;
block|}
comment|/**      * @param message the message context      * @return a ResendCandidate      */
specifier|protected
name|ResendCandidate
name|createResendCandidate
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|new
name|ResendCandidate
argument_list|(
name|message
argument_list|)
return|;
block|}
comment|/**      * Accepts a new resend candidate.      *       * @param ctx the message context.      * @return ResendCandidate      */
specifier|protected
name|ResendCandidate
name|cacheUnacknowledged
parameter_list|(
name|Message
name|message
parameter_list|)
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
literal|true
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
name|Identifier
name|sid
init|=
name|st
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|sid
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ResendCandidate
name|candidate
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|List
argument_list|<
name|ResendCandidate
argument_list|>
name|sequenceCandidates
init|=
name|getSequenceCandidates
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|sequenceCandidates
condition|)
block|{
name|sequenceCandidates
operator|=
operator|new
name|ArrayList
argument_list|<
name|ResendCandidate
argument_list|>
argument_list|()
expr_stmt|;
name|candidates
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|sequenceCandidates
argument_list|)
expr_stmt|;
block|}
name|candidate
operator|=
operator|new
name|ResendCandidate
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|sequenceCandidates
operator|.
name|add
argument_list|(
name|candidate
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cached unacknowledged message."
argument_list|)
expr_stmt|;
return|return
name|candidate
return|;
block|}
comment|/**      * @return a map relating sequence ID to a lists of un-acknowledged messages      *         for that sequence      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ResendCandidate
argument_list|>
argument_list|>
name|getUnacknowledged
parameter_list|()
block|{
return|return
name|candidates
return|;
block|}
comment|/**      * @param seq the sequence under consideration      * @return the list of resend candidates for that sequence      * @pre called with mutex held      */
specifier|protected
name|List
argument_list|<
name|ResendCandidate
argument_list|>
name|getSequenceCandidates
parameter_list|(
name|SourceSequence
name|seq
parameter_list|)
block|{
return|return
name|getSequenceCandidates
argument_list|(
name|seq
operator|.
name|getIdentifier
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @param key the sequence identifier under consideration      * @return the list of resend candidates for that sequence      * @pre called with mutex held      */
specifier|protected
name|List
argument_list|<
name|ResendCandidate
argument_list|>
name|getSequenceCandidates
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|candidates
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|private
name|void
name|clientResend
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Conduit
name|c
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getConduit
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|resend
argument_list|(
name|c
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|serverResend
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
comment|// get the message's to address
name|AddressingProperties
name|maps
init|=
name|RMContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AttributedURIType
name|to
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|maps
condition|)
block|{
name|to
operator|=
name|maps
operator|.
name|getTo
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|==
name|to
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"NO_ADDRESS_FOR_RESEND_MSG"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
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
name|to
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
name|FINE
argument_list|,
literal|"Cannot resend to anonymous target"
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|String
name|address
init|=
name|to
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Resending to address: "
operator|+
name|address
argument_list|)
expr_stmt|;
specifier|final
name|Endpoint
name|reliableEndpoint
init|=
name|manager
operator|.
name|getReliableEndpoint
argument_list|(
name|message
argument_list|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|ConduitSelector
name|cs
init|=
operator|new
name|DeferredConduitSelector
argument_list|()
block|{
annotation|@
name|Override
specifier|public
specifier|synchronized
name|Conduit
name|selectConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Conduit
name|conduit
init|=
literal|null
decl_stmt|;
name|EndpointInfo
name|endpointInfo
init|=
name|reliableEndpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
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
name|original
init|=
name|endpointInfo
operator|.
name|getTarget
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
literal|null
operator|!=
name|address
condition|)
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
name|conduit
operator|=
name|super
operator|.
name|selectConduit
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
return|return
name|conduit
return|;
block|}
block|}
decl_stmt|;
name|cs
operator|.
name|setEndpoint
argument_list|(
name|reliableEndpoint
argument_list|)
expr_stmt|;
name|Conduit
name|c
init|=
name|cs
operator|.
name|selectConduit
argument_list|(
name|message
argument_list|)
decl_stmt|;
comment|// REVISIT
comment|// use application endpoint message observer instead?
name|c
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Ignoring response to resent message."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|resend
argument_list|(
name|c
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resend
parameter_list|(
name|Conduit
name|c
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
comment|// get registered callbacks, create new output stream and
comment|// re-register
comment|// all callbacks except the retransmission callback
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CachedOutputStreamCallback
argument_list|>
name|callbacks
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|os
operator|instanceof
name|CachedOutputStream
condition|)
block|{
name|callbacks
operator|=
operator|(
operator|(
name|CachedOutputStream
operator|)
name|os
operator|)
operator|.
name|getCallbacks
argument_list|()
expr_stmt|;
block|}
name|message
operator|.
name|removeContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|c
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|os
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|callbacks
operator|&&
name|callbacks
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|os
operator|instanceof
name|CachedOutputStream
operator|)
condition|)
block|{
name|os
operator|=
name|RMUtils
operator|.
name|createCachedStream
argument_list|(
name|message
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|CachedOutputStreamCallback
name|cb
range|:
name|callbacks
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|cb
operator|instanceof
name|RetransmissionCallback
operator|)
condition|)
block|{
operator|(
operator|(
name|CachedOutputStream
operator|)
name|os
operator|)
operator|.
name|registerCallback
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|byte
index|[]
name|content
init|=
operator|(
name|byte
index|[]
operator|)
name|message
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|SAVED_CONTENT
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|content
condition|)
block|{
name|content
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
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
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Using saved byte array: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|content
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
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
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Using saved output stream: "
operator|+
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|content
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|content
argument_list|)
decl_stmt|;
comment|// copy saved output stream to new output stream in chunks of 1024
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|bis
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConnectException
name|ex
parameter_list|)
block|{
comment|//ignore, we'll just resent again later
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"RESEND_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Represents a candidate for resend, i.e. an unacked outgoing message.      */
specifier|protected
class|class
name|ResendCandidate
implements|implements
name|Runnable
block|{
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|OutputStream
name|out
decl_stmt|;
specifier|private
name|Date
name|next
decl_stmt|;
specifier|private
name|TimerTask
name|nextTask
decl_stmt|;
specifier|private
name|int
name|resends
decl_stmt|;
specifier|private
name|long
name|nextInterval
decl_stmt|;
specifier|private
name|long
name|backoff
decl_stmt|;
specifier|private
name|boolean
name|pending
decl_stmt|;
specifier|private
name|boolean
name|includeAckRequested
decl_stmt|;
comment|/**          * @param ctx message context for the unacked message          */
specifier|protected
name|ResendCandidate
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|message
operator|=
name|m
expr_stmt|;
name|resends
operator|=
literal|0
expr_stmt|;
name|out
operator|=
name|m
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|RMAssertion
name|rma
init|=
name|PolicyUtils
operator|.
name|getRMAssertion
argument_list|(
name|manager
operator|.
name|getRMAssertion
argument_list|()
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|long
name|baseRetransmissionInterval
init|=
name|rma
operator|.
name|getBaseRetransmissionInterval
argument_list|()
operator|.
name|getMilliseconds
argument_list|()
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|backoff
operator|=
literal|null
operator|!=
name|rma
operator|.
name|getExponentialBackoff
argument_list|()
condition|?
name|RetransmissionQueue
operator|.
name|DEFAULT_EXPONENTIAL_BACKOFF
else|:
literal|1
expr_stmt|;
name|next
operator|=
operator|new
name|Date
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|baseRetransmissionInterval
argument_list|)
expr_stmt|;
name|nextInterval
operator|=
name|baseRetransmissionInterval
operator|*
name|backoff
expr_stmt|;
name|AddressingProperties
name|maps
init|=
name|RMContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|AttributedURIType
name|to
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|maps
condition|)
block|{
name|to
operator|=
name|maps
operator|.
name|getTo
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|to
operator|!=
literal|null
operator|&&
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
name|to
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
name|INFO
argument_list|,
literal|"Cannot resend to anonymous target.  Not scheduling a resend."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|null
operator|!=
name|manager
operator|.
name|getTimer
argument_list|()
condition|)
block|{
name|schedule
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**          * Initiate resend asynchronsly.          *           * @param requestAcknowledge true if a AckRequest header is to be sent          *            with resend          */
specifier|protected
name|void
name|initiate
parameter_list|(
name|boolean
name|requestAcknowledge
parameter_list|)
block|{
name|includeAckRequested
operator|=
name|requestAcknowledge
expr_stmt|;
name|pending
operator|=
literal|true
expr_stmt|;
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Executor
name|executor
init|=
name|ep
operator|.
name|getExecutor
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|executor
condition|)
block|{
name|executor
operator|=
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getExecutor
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Using service executor {0}"
argument_list|,
name|executor
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Using endpoint executor {0}"
argument_list|,
name|executor
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"RESEND_INITIATION_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|// ensure ACK wasn't received while this task was enqueued
comment|// on executor
if|if
condition|(
name|isPending
argument_list|()
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|resender
operator|.
name|resend
argument_list|(
name|message
argument_list|,
name|includeAckRequested
argument_list|)
expr_stmt|;
name|includeAckRequested
operator|=
literal|false
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|attempted
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**          * @return number of resend attempts          */
specifier|protected
name|int
name|getResends
parameter_list|()
block|{
return|return
name|resends
return|;
block|}
comment|/**          * @return date of next resend          */
specifier|protected
name|Date
name|getNext
parameter_list|()
block|{
return|return
name|next
return|;
block|}
comment|/**          * @return if resend attempt is pending          */
specifier|protected
specifier|synchronized
name|boolean
name|isPending
parameter_list|()
block|{
return|return
name|pending
return|;
block|}
comment|/**          * ACK has been received for this candidate.          */
specifier|protected
specifier|synchronized
name|void
name|resolved
parameter_list|()
block|{
name|pending
operator|=
literal|false
expr_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|nextTask
condition|)
block|{
name|nextTask
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**          * Cancel further resend (although no ACK has been received).          */
specifier|protected
name|void
name|cancel
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|!=
name|nextTask
condition|)
block|{
name|nextTask
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**          * @return associated message context          */
specifier|protected
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
comment|/**          * A resend has been attempted. Schedule the next attempt.          */
specifier|protected
specifier|synchronized
name|void
name|attempted
parameter_list|()
block|{
name|pending
operator|=
literal|false
expr_stmt|;
name|resends
operator|++
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|next
condition|)
block|{
name|next
operator|=
operator|new
name|Date
argument_list|(
name|next
operator|.
name|getTime
argument_list|()
operator|+
name|nextInterval
argument_list|)
expr_stmt|;
name|nextInterval
operator|*=
name|backoff
expr_stmt|;
name|schedule
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
specifier|final
specifier|synchronized
name|void
name|schedule
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|manager
operator|.
name|getTimer
argument_list|()
condition|)
block|{
return|return;
block|}
class|class
name|ResendTask
extends|extends
name|TimerTask
block|{
name|ResendCandidate
name|candidate
decl_stmt|;
name|ResendTask
parameter_list|(
name|ResendCandidate
name|c
parameter_list|)
block|{
name|candidate
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
operator|!
name|candidate
operator|.
name|isPending
argument_list|()
condition|)
block|{
name|candidate
operator|.
name|initiate
argument_list|(
name|includeAckRequested
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|nextTask
operator|=
operator|new
name|ResendTask
argument_list|(
name|this
argument_list|)
expr_stmt|;
try|try
block|{
name|manager
operator|.
name|getTimer
argument_list|()
operator|.
name|schedule
argument_list|(
name|nextTask
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SCHEDULE_RESEND_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Encapsulates actual resend logic (pluggable to facilitate unit testing)      */
specifier|public
interface|interface
name|Resender
block|{
comment|/**          * Resend mechanics.          *           * @param context the cloned message context.          * @param if a AckRequest should be included          */
name|void
name|resend
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|requestAcknowledge
parameter_list|)
function_decl|;
block|}
comment|/**      * Create default Resender logic.      *       * @return default Resender      */
specifier|protected
specifier|final
name|Resender
name|getDefaultResender
parameter_list|()
block|{
return|return
operator|new
name|Resender
argument_list|()
block|{
specifier|public
name|void
name|resend
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|requestAcknowledge
parameter_list|)
block|{
name|RMProperties
name|properties
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|SequenceType
name|st
init|=
name|properties
operator|.
name|getSequence
argument_list|()
decl_stmt|;
if|if
condition|(
name|st
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"RESEND_MSG"
argument_list|,
name|st
operator|.
name|getMessageNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// TODO: remove previously added acknowledgments and update
comment|// message id (to avoid duplicates)
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|clientResend
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serverResend
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"RESEND_FAILED_MSG"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
comment|/**      * Plug in replacement resend logic (facilitates unit testing).      *       * @param replacement resend logic      */
specifier|protected
name|void
name|replaceResender
parameter_list|(
name|Resender
name|replacement
parameter_list|)
block|{
name|resender
operator|=
name|replacement
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|JaxbAssertion
argument_list|<
name|RMAssertion
argument_list|>
name|getAssertion
parameter_list|(
name|AssertionInfo
name|ai
parameter_list|)
block|{
return|return
operator|(
name|JaxbAssertion
argument_list|<
name|RMAssertion
argument_list|>
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
return|;
block|}
block|}
end_class

end_unit

