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
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|AbstractOutDatabindingInterceptor
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
name|AttachmentOutInterceptor
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
name|Fault
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
name|LoggingOutInterceptor
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
name|FaultMode
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
name|MessageContentsList
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|OperationInfo
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
name|v200702
operator|.
name|TerminateSequenceType
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|RMCaptureOutInterceptor
extends|extends
name|AbstractRMInterceptor
argument_list|<
name|Message
argument_list|>
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
name|RMCaptureOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RMCaptureOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|LoggingOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handle
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|SequenceFault
throws|,
name|RMException
block|{
name|AddressingProperties
name|maps
init|=
name|ContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|maps
condition|)
block|{
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
literal|"MAPS_RETRIEVAL_FAILURE_MSG"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|msg
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|RM_RETRANSMISSION
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isRuntimeFault
argument_list|(
name|msg
argument_list|)
condition|)
block|{
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
literal|"RUNTIME_FAULT_MSG"
argument_list|)
expr_stmt|;
comment|// in case of a SequenceFault or other WS-RM related fault, set action appropriately.
comment|// the received inbound maps is available to extract some values in case if needed.
name|Throwable
name|cause
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|SequenceFault
operator|||
name|cause
operator|instanceof
name|RMException
condition|)
block|{
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|setValue
argument_list|(
name|getAddressingNamespace
argument_list|(
name|maps
argument_list|)
operator|+
literal|"/fault"
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|Source
name|source
init|=
name|getManager
argument_list|()
operator|.
name|getSource
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|RMConfiguration
name|config
init|=
name|getManager
argument_list|()
operator|.
name|getEffectiveConfiguration
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|String
name|wsaNamespace
init|=
name|config
operator|.
name|getAddressingNamespace
argument_list|()
decl_stmt|;
name|String
name|rmNamespace
init|=
name|config
operator|.
name|getRMNamespace
argument_list|()
decl_stmt|;
name|ProtocolVariation
name|protocol
init|=
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|rmNamespace
argument_list|,
name|wsaNamespace
argument_list|)
decl_stmt|;
name|RMContextUtils
operator|.
name|setProtocolVariation
argument_list|(
name|msg
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
name|maps
operator|.
name|exposeAs
argument_list|(
name|wsaNamespace
argument_list|)
expr_stmt|;
name|String
name|action
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|maps
operator|.
name|getAction
argument_list|()
condition|)
block|{
name|action
operator|=
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
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
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Action: "
operator|+
name|action
argument_list|)
expr_stmt|;
block|}
name|boolean
name|isApplicationMessage
init|=
operator|!
name|RMContextUtils
operator|.
name|isRMProtocolMessage
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|boolean
name|isPartialResponse
init|=
name|MessageUtils
operator|.
name|isPartialResponse
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|boolean
name|isLastMessage
init|=
name|constants
operator|.
name|getCloseSequenceAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
decl_stmt|;
name|RMProperties
name|rmpsOut
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|msg
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|rmpsOut
condition|)
block|{
name|rmpsOut
operator|=
operator|new
name|RMProperties
argument_list|()
expr_stmt|;
name|rmpsOut
operator|.
name|exposeAs
argument_list|(
name|protocol
operator|.
name|getWSRMNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|RMContextUtils
operator|.
name|storeRMProperties
argument_list|(
name|msg
argument_list|,
name|rmpsOut
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// Activate process response for oneWay
if|if
condition|(
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROCESS_ONEWAY_RESPONSE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|RMProperties
name|rmpsIn
init|=
literal|null
decl_stmt|;
name|Identifier
name|inSeqId
init|=
literal|null
decl_stmt|;
name|long
name|inMessageNumber
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|isApplicationMessage
condition|)
block|{
name|rmpsIn
operator|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|rmpsIn
operator|&&
literal|null
operator|!=
name|rmpsIn
operator|.
name|getSequence
argument_list|()
condition|)
block|{
name|inSeqId
operator|=
name|rmpsIn
operator|.
name|getSequence
argument_list|()
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
name|inMessageNumber
operator|=
name|rmpsIn
operator|.
name|getSequence
argument_list|()
operator|.
name|getMessageNumber
argument_list|()
expr_stmt|;
block|}
name|ContextUtils
operator|.
name|storeDeferUncorrelatedMessageAbort
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|invocationContext
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|msg
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INVOCATION_CONTEXT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|isApplicationMessage
operator|||
operator|(
name|isLastMessage
operator|&&
name|invocationContext
operator|!=
literal|null
operator|)
operator|)
operator|&&
operator|!
name|isPartialResponse
condition|)
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
literal|"inbound sequence: "
operator|+
operator|(
literal|null
operator|==
name|inSeqId
condition|?
literal|"null"
else|:
name|inSeqId
operator|.
name|getValue
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
comment|// get the current sequence, requesting the creation of a new one if necessary
synchronized|synchronized
init|(
name|source
init|)
block|{
name|SourceSequence
name|seq
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isLastMessage
condition|)
block|{
name|seq
operator|=
operator|(
name|SourceSequence
operator|)
name|invocationContext
operator|.
name|get
argument_list|(
name|SourceSequence
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|seq
operator|=
name|getManager
argument_list|()
operator|.
name|getSequence
argument_list|(
name|inSeqId
argument_list|,
name|msg
argument_list|,
name|maps
argument_list|)
expr_stmt|;
block|}
assert|assert
literal|null
operator|!=
name|seq
assert|;
comment|// increase message number and store a sequence type object in
comment|// context
name|seq
operator|.
name|nextMessageNumber
argument_list|(
name|inSeqId
argument_list|,
name|inMessageNumber
argument_list|,
name|isLastMessage
argument_list|)
expr_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|RMManager
operator|.
name|WSRM_LAST_MESSAGE_PROPERTY
argument_list|)
argument_list|)
condition|)
block|{
comment|// mark the message as the last one
name|seq
operator|.
name|setLastMessage
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|rmpsOut
operator|.
name|setSequence
argument_list|(
name|seq
argument_list|)
expr_stmt|;
comment|// if this was the last message in the sequence, reset the
comment|// current sequence so that a new one will be created next
comment|// time the handler is invoked
if|if
condition|(
name|seq
operator|.
name|isLastMessage
argument_list|()
condition|)
block|{
name|source
operator|.
name|setCurrent
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|msg
argument_list|)
operator|&&
name|constants
operator|.
name|getCreateSequenceAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|setValue
argument_list|(
name|constants
operator|.
name|getCreateSequenceResponseAction
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPartialResponse
operator|&&
name|action
operator|==
literal|null
operator|&&
name|isResponseToAction
argument_list|(
name|msg
argument_list|,
name|constants
operator|.
name|getSequenceAckAction
argument_list|()
argument_list|)
condition|)
block|{
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
name|rmpsIn
operator|.
name|getAcks
argument_list|()
decl_stmt|;
if|if
condition|(
name|acks
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|SourceSequence
name|ss
init|=
name|source
operator|.
name|getSequence
argument_list|(
name|acks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ss
operator|!=
literal|null
operator|&&
name|ss
operator|.
name|allAcknowledged
argument_list|()
condition|)
block|{
name|setAction
argument_list|(
name|maps
argument_list|,
name|constants
operator|.
name|getTerminateSequenceAction
argument_list|()
argument_list|)
expr_stmt|;
name|setTerminateSequence
argument_list|(
name|msg
argument_list|,
name|ss
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
name|msg
operator|.
name|remove
argument_list|(
name|Message
operator|.
name|EMPTY_PARTIAL_RESPONSE_MESSAGE
argument_list|)
expr_stmt|;
comment|// removing this sequence now. See the comment in SourceSequence.setAcknowledged()
name|source
operator|.
name|removeSequence
argument_list|(
name|ss
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// capture message if retransmission possible
if|if
condition|(
name|isApplicationMessage
operator|&&
operator|!
name|isPartialResponse
condition|)
block|{
name|getManager
argument_list|()
operator|.
name|initializeInterceptorChain
argument_list|(
name|msg
argument_list|)
expr_stmt|;
comment|//doneCaptureMessage(msg);
name|captureMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|captureMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|put
argument_list|(
name|RMMessageConstants
operator|.
name|MESSAGE_CAPTURE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CaptureStart
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CaptureEnd
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|CaptureStart
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|CaptureStart
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|XMLStreamWriter
name|writer
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RM_ORIGINAL_WRITER"
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|CapturingXMLWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
literal|"RM_CAPTURING_WRITER"
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|CaptureEnd
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|CaptureEnd
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|XMLStreamWriter
name|w
init|=
operator|(
name|XMLStreamWriter
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"RM_ORIGINAL_WRITER"
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|w
argument_list|)
expr_stmt|;
name|CapturingXMLWriter
name|cw
init|=
operator|(
name|CapturingXMLWriter
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"RM_CAPTURING_WRITER"
argument_list|)
decl_stmt|;
try|try
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
name|sequence
init|=
name|rmps
operator|.
name|getSequence
argument_list|()
decl_stmt|;
name|Long
name|number
init|=
name|sequence
operator|.
name|getMessageNumber
argument_list|()
decl_stmt|;
name|Identifier
name|sid
init|=
name|sequence
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
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
literal|"Captured message "
operator|+
name|number
operator|+
literal|" in sequence "
operator|+
name|sid
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// save message for potential retransmission
name|ByteArrayInputStream
name|bis
init|=
name|cw
operator|.
name|getOutputStream
argument_list|()
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|RMMessageConstants
operator|.
name|SAVED_CONTENT
argument_list|,
name|RewindableInputStream
operator|.
name|makeRewindable
argument_list|(
name|bis
argument_list|)
argument_list|)
expr_stmt|;
name|RMManager
name|manager
init|=
name|getManager
argument_list|()
decl_stmt|;
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|start
argument_list|()
expr_stmt|;
name|manager
operator|.
name|getRetransmissionQueue
argument_list|()
operator|.
name|addUnacknowledged
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
comment|// persist message to store
name|Source
name|s
init|=
name|manager
operator|.
name|getSource
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|SourceSequence
name|ss
init|=
name|s
operator|.
name|getSequence
argument_list|(
name|sid
argument_list|)
decl_stmt|;
name|RMMessage
name|msg
init|=
operator|new
name|RMMessage
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setMessageNumber
argument_list|(
name|number
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
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
if|if
condition|(
literal|null
operator|!=
name|maps
operator|&&
literal|null
operator|!=
name|maps
operator|.
name|getTo
argument_list|()
condition|)
block|{
name|msg
operator|.
name|setTo
argument_list|(
name|maps
operator|.
name|getTo
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|msg
operator|.
name|setContent
argument_list|(
name|bis
argument_list|)
expr_stmt|;
name|store
operator|.
name|persistOutgoing
argument_list|(
name|ss
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RMException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
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
literal|"Error persisting message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
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
literal|"Error persisting message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cw
operator|.
name|getThrowable
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Throwable
name|t
init|=
name|cw
operator|.
name|getThrowable
argument_list|()
decl_stmt|;
name|RuntimeException
name|exception
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|t
operator|instanceof
name|RuntimeException
condition|)
block|{
name|exception
operator|=
operator|(
name|RuntimeException
operator|)
name|t
expr_stmt|;
block|}
else|else
block|{
name|exception
operator|=
operator|new
name|Fault
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
throw|throw
name|exception
throw|;
block|}
block|}
block|}
specifier|private
name|String
name|getAddressingNamespace
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|)
block|{
name|String
name|wsaNamespace
init|=
name|maps
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|wsaNamespace
operator|==
literal|null
condition|)
block|{
name|getManager
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAddressingNamespace
argument_list|()
expr_stmt|;
block|}
return|return
name|wsaNamespace
return|;
block|}
name|boolean
name|isRuntimeFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|FaultMode
name|mode
init|=
name|MessageUtils
operator|.
name|getFaultMode
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|mode
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|FaultMode
operator|.
name|CHECKED_APPLICATION_FAULT
operator|!=
name|mode
return|;
block|}
specifier|private
name|boolean
name|isResponseToAction
parameter_list|(
name|Message
name|msg
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AddressingProperties
name|inMaps
init|=
name|RMContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|inAction
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|inMaps
operator|.
name|getAction
argument_list|()
condition|)
block|{
name|inAction
operator|=
name|inMaps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
return|return
name|action
operator|.
name|equals
argument_list|(
name|inAction
argument_list|)
return|;
block|}
specifier|private
name|void
name|setTerminateSequence
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Identifier
name|identifier
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|)
throws|throws
name|RMException
block|{
name|TerminateSequenceType
name|ts
init|=
operator|new
name|TerminateSequenceType
argument_list|()
decl_stmt|;
name|ts
operator|.
name|setIdentifier
argument_list|(
name|identifier
argument_list|)
expr_stmt|;
name|MessageContentsList
name|contents
init|=
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|Object
index|[]
block|{
name|protocol
operator|.
name|getCodec
argument_list|()
operator|.
name|convertToSend
argument_list|(
name|ts
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|contents
argument_list|)
expr_stmt|;
comment|// create a new exchange for this output-only exchange
name|Exchange
name|newex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|Exchange
name|oldex
init|=
name|msg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|oldex
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|oldex
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|oldex
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|oldex
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|newex
operator|.
name|setConduit
argument_list|(
name|oldex
operator|.
name|getConduit
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
name|newex
operator|.
name|setDestination
argument_list|(
name|oldex
operator|.
name|getDestination
argument_list|()
argument_list|)
expr_stmt|;
comment|//Setup the BindingOperationInfo
name|RMEndpoint
name|rmep
init|=
name|getManager
argument_list|()
operator|.
name|getReliableEndpoint
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|rmep
operator|.
name|getEndpoint
argument_list|(
name|protocol
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
name|protocol
operator|.
name|getConstants
argument_list|()
operator|.
name|getTerminateSequenceAnonymousOperationName
argument_list|()
argument_list|)
decl_stmt|;
name|BindingInfo
name|bi
init|=
name|rmep
operator|.
name|getBindingInfo
argument_list|(
name|protocol
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|bi
operator|.
name|getOperation
argument_list|(
name|oi
argument_list|)
decl_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|,
name|bi
argument_list|)
expr_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|newex
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
name|newex
argument_list|)
expr_stmt|;
name|newex
operator|.
name|setOutMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|setAction
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AttributedURIType
name|actionURI
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|actionURI
operator|.
name|setValue
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|maps
operator|.
name|setAction
argument_list|(
name|actionURI
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

