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
name|Collection
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|RMOutInterceptor
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
name|RMOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|RMOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|RMCaptureOutInterceptor
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
return|return;
block|}
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
name|Destination
name|destination
init|=
name|getManager
argument_list|()
operator|.
name|getDestination
argument_list|(
name|msg
argument_list|)
decl_stmt|;
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
name|isApplicationMessage
operator|&&
operator|!
name|isPartialResponse
condition|)
block|{
name|addRetransmissionInterceptor
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|Identifier
name|inSeqId
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isApplicationMessage
condition|)
block|{
name|RMProperties
name|rmpsIn
init|=
name|RMContextUtils
operator|.
name|retrieveRMProperties
argument_list|(
name|msg
argument_list|,
literal|false
argument_list|)
decl_stmt|;
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
block|}
block|}
comment|// add Acknowledgements (to application messages or explicitly
comment|// created Acknowledgement messages only)
if|if
condition|(
name|isApplicationMessage
operator|||
name|constants
operator|.
name|getSequenceAckAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|AttributedURIType
name|to
init|=
name|maps
operator|.
name|getTo
argument_list|()
decl_stmt|;
assert|assert
literal|null
operator|!=
name|to
assert|;
name|addAcknowledgements
argument_list|(
name|destination
argument_list|,
name|rmpsOut
argument_list|,
name|inSeqId
argument_list|,
name|to
argument_list|)
expr_stmt|;
if|if
condition|(
name|isPartialResponse
operator|&&
name|rmpsOut
operator|.
name|getAcks
argument_list|()
operator|!=
literal|null
operator|&&
name|rmpsOut
operator|.
name|getAcks
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|setAction
argument_list|(
name|maps
argument_list|,
name|constants
operator|.
name|getSequenceAckAction
argument_list|()
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
block|}
block|}
if|if
condition|(
name|constants
operator|.
name|getSequenceAckAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|||
operator|(
name|constants
operator|.
name|getTerminateSequenceAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|&&
name|RM10Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|rmNamespace
argument_list|)
operator|)
condition|)
block|{
name|maps
operator|.
name|setReplyTo
argument_list|(
name|RMUtils
operator|.
name|createNoneReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertReliability
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
name|void
name|addAcknowledgements
parameter_list|(
name|Destination
name|destination
parameter_list|,
name|RMProperties
name|rmpsOut
parameter_list|,
name|Identifier
name|inSeqId
parameter_list|,
name|AttributedURIType
name|to
parameter_list|)
block|{
for|for
control|(
name|DestinationSequence
name|seq
range|:
name|destination
operator|.
name|getAllSequences
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|seq
operator|.
name|sendAcknowledgement
argument_list|()
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
literal|"no need to add acknowledgements for sequence "
operator|+
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
continue|continue;
block|}
name|String
name|address
init|=
name|seq
operator|.
name|getAcksTo
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|to
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|address
argument_list|)
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
literal|"sequences acksTo address ("
operator|+
name|address
operator|+
literal|") does not match to address ("
operator|+
name|to
operator|.
name|getValue
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
comment|// there may be multiple sources with anonymous acksTo
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
name|address
argument_list|)
operator|&&
operator|!
name|AbstractSequence
operator|.
name|identifierEquals
argument_list|(
name|seq
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|inSeqId
argument_list|)
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
literal|"sequence identifier does not match inbound sequence identifier"
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
name|rmpsOut
operator|.
name|addAck
argument_list|(
name|seq
argument_list|)
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
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
name|rmpsOut
operator|.
name|getAcks
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|acks
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No acknowledgements added."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Added "
operator|+
name|acks
operator|.
name|size
argument_list|()
operator|+
literal|" acknowledgements."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addRetransmissionInterceptor
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|RetransmissionInterceptor
name|ri
init|=
operator|new
name|RetransmissionInterceptor
argument_list|()
decl_stmt|;
name|ri
operator|.
name|setManager
argument_list|(
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO:
comment|// On the server side: If a fault occurs after this interceptor we will switch
comment|// interceptor chains (if this is not already a fault message) and therefore need to
comment|// make sure the retransmission interceptor is added to the fault chain
comment|//
name|msg
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ri
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Added RetransmissionInterceptor to chain."
argument_list|)
expr_stmt|;
name|RetransmissionQueue
name|queue
init|=
name|getManager
argument_list|()
operator|.
name|getRetransmissionQueue
argument_list|()
decl_stmt|;
if|if
condition|(
name|queue
operator|!=
literal|null
condition|)
block|{
name|queue
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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

