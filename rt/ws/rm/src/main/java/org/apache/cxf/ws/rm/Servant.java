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
name|Collections
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
name|service
operator|.
name|invoker
operator|.
name|Invoker
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
name|manager
operator|.
name|DestinationPolicyType
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
name|AcceptType
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
name|CreateSequenceResponseType
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
name|CreateSequenceType
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
name|OfferType
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
name|TerminateSequenceResponseType
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
name|Servant
implements|implements
name|Invoker
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
name|Servant
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|RMEndpoint
name|reliableEndpoint
decl_stmt|;
comment|// REVISIT assumption there is only a single outstanding unattached Identifier
specifier|private
name|Identifier
name|unattachedIdentifier
decl_stmt|;
name|Servant
parameter_list|(
name|RMEndpoint
name|rme
parameter_list|)
block|{
name|reliableEndpoint
operator|=
name|rme
expr_stmt|;
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Invoking on RM Endpoint"
argument_list|)
expr_stmt|;
specifier|final
name|ProtocolVariation
name|protocol
init|=
name|RMContextUtils
operator|.
name|getProtocolVariation
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|exchange
operator|.
name|get
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|oi
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No operation info."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|RM10Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|RM10Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceOnewayOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceOnewayOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|createSequence
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|RM10Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceResponseOnewayOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getCreateSequenceResponseOnewayOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|EncoderDecoder
name|codec
init|=
name|protocol
operator|.
name|getCodec
argument_list|()
decl_stmt|;
name|CreateSequenceResponseType
name|createResponse
init|=
name|codec
operator|.
name|convertReceivedCreateSequenceResponse
argument_list|(
name|getParameter
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|createSequenceResponse
argument_list|(
name|createResponse
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RM10Constants
operator|.
name|INSTANCE
operator|.
name|getTerminateSequenceOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getTerminateSequenceOperationName
argument_list|()
operator|.
name|equals
argument_list|(
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|Object
name|tsr
init|=
name|terminateSequence
argument_list|(
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tsr
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|tsr
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
name|Object
name|createSequence
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating sequence"
argument_list|)
expr_stmt|;
specifier|final
name|ProtocolVariation
name|protocol
init|=
name|RMContextUtils
operator|.
name|getProtocolVariation
argument_list|(
name|message
argument_list|)
decl_stmt|;
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
literal|false
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|outMessage
condition|)
block|{
name|RMContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|EncoderDecoder
name|codec
init|=
name|protocol
operator|.
name|getCodec
argument_list|()
decl_stmt|;
name|CreateSequenceType
name|create
init|=
name|codec
operator|.
name|convertReceivedCreateSequence
argument_list|(
name|getParameter
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|Destination
name|destination
init|=
name|reliableEndpoint
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|CreateSequenceResponseType
name|createResponse
init|=
operator|new
name|CreateSequenceResponseType
argument_list|()
decl_stmt|;
name|createResponse
operator|.
name|setIdentifier
argument_list|(
name|destination
operator|.
name|generateSequenceIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|DestinationPolicyType
name|dp
init|=
name|reliableEndpoint
operator|.
name|getManager
argument_list|()
operator|.
name|getDestinationPolicy
argument_list|()
decl_stmt|;
name|Duration
name|supportedDuration
init|=
name|dp
operator|.
name|getSequenceExpiration
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|supportedDuration
condition|)
block|{
name|supportedDuration
operator|=
name|DatatypeFactory
operator|.
name|PT0S
expr_stmt|;
block|}
name|Expires
name|ex
init|=
name|create
operator|.
name|getExpires
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|ex
condition|)
block|{
name|Duration
name|effectiveDuration
init|=
name|ex
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|// PT0S represents 0 second and the shortest duration but in ws-rm, considered the longest
if|if
condition|(
name|DatatypeFactory
operator|.
name|PT0S
operator|.
name|equals
argument_list|(
name|effectiveDuration
argument_list|)
operator|||
operator|(
operator|!
name|DatatypeFactory
operator|.
name|PT0S
operator|.
name|equals
argument_list|(
name|supportedDuration
argument_list|)
operator|&&
name|supportedDuration
operator|.
name|isShorterThan
argument_list|(
name|effectiveDuration
argument_list|)
operator|)
condition|)
block|{
name|effectiveDuration
operator|=
name|supportedDuration
expr_stmt|;
block|}
name|ex
operator|=
operator|new
name|Expires
argument_list|()
expr_stmt|;
name|ex
operator|.
name|setValue
argument_list|(
name|effectiveDuration
argument_list|)
expr_stmt|;
name|createResponse
operator|.
name|setExpires
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
name|OfferType
name|offer
init|=
name|create
operator|.
name|getOffer
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|offer
condition|)
block|{
name|AcceptType
name|accept
init|=
operator|new
name|AcceptType
argument_list|()
decl_stmt|;
if|if
condition|(
name|dp
operator|.
name|isAcceptOffers
argument_list|()
condition|)
block|{
name|Source
name|source
init|=
name|reliableEndpoint
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Accepting inbound sequence offer"
argument_list|)
expr_stmt|;
comment|// AddressingProperties maps = RMContextUtils.retrieveMAPs(message, false, false);
name|accept
operator|.
name|setAcksTo
argument_list|(
name|RMUtils
operator|.
name|createReference
argument_list|(
name|maps
operator|.
name|getTo
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|SourceSequence
name|seq
init|=
operator|new
name|SourceSequence
argument_list|(
name|offer
operator|.
name|getIdentifier
argument_list|()
argument_list|,
literal|null
argument_list|,
name|createResponse
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|protocol
argument_list|)
decl_stmt|;
name|seq
operator|.
name|setExpires
argument_list|(
name|offer
operator|.
name|getExpires
argument_list|()
argument_list|)
expr_stmt|;
name|seq
operator|.
name|setTarget
argument_list|(
name|create
operator|.
name|getAcksTo
argument_list|()
argument_list|)
expr_stmt|;
name|source
operator|.
name|addSequence
argument_list|(
name|seq
argument_list|)
expr_stmt|;
name|source
operator|.
name|setCurrent
argument_list|(
name|createResponse
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|seq
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
literal|"Making offered sequence the current sequence for responses to "
operator|+
name|createResponse
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
literal|"Refusing inbound sequence offer"
argument_list|)
expr_stmt|;
block|}
name|accept
operator|.
name|setAcksTo
argument_list|(
name|RMUtils
operator|.
name|createNoneReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|createResponse
operator|.
name|setAccept
argument_list|(
name|accept
argument_list|)
expr_stmt|;
block|}
name|DestinationSequence
name|seq
init|=
operator|new
name|DestinationSequence
argument_list|(
name|createResponse
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|create
operator|.
name|getAcksTo
argument_list|()
argument_list|,
name|destination
argument_list|,
name|protocol
argument_list|)
decl_stmt|;
name|seq
operator|.
name|setCorrelationID
argument_list|(
name|maps
operator|.
name|getMessageID
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|destination
operator|.
name|addSequence
argument_list|(
name|seq
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"returning "
operator|+
name|createResponse
argument_list|)
expr_stmt|;
return|return
name|codec
operator|.
name|convertToSend
argument_list|(
name|createResponse
argument_list|)
return|;
block|}
specifier|public
name|void
name|createSequenceResponse
parameter_list|(
name|CreateSequenceResponseType
name|createResponse
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating sequence response"
argument_list|)
expr_stmt|;
name|SourceSequence
name|seq
init|=
operator|new
name|SourceSequence
argument_list|(
name|createResponse
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|protocol
argument_list|)
decl_stmt|;
name|seq
operator|.
name|setExpires
argument_list|(
name|createResponse
operator|.
name|getExpires
argument_list|()
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
name|reliableEndpoint
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|source
operator|.
name|addSequence
argument_list|(
name|seq
argument_list|)
expr_stmt|;
comment|// the incoming sequence ID is either used as the requestor sequence
comment|// (signalled by null) or associated with a corresponding sequence
comment|// identifier
name|source
operator|.
name|setCurrent
argument_list|(
name|clearUnattachedIdentifier
argument_list|()
argument_list|,
name|seq
argument_list|)
expr_stmt|;
comment|// if a sequence was offered and accepted, then we can add this to
comment|// to the local destination sequence list, otherwise we have to wait for
comment|// and incoming CreateSequence request
name|Identifier
name|offeredId
init|=
name|reliableEndpoint
operator|.
name|getProxy
argument_list|()
operator|.
name|getOfferedIdentifier
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|offeredId
condition|)
block|{
name|AcceptType
name|accept
init|=
name|createResponse
operator|.
name|getAccept
argument_list|()
decl_stmt|;
assert|assert
literal|null
operator|!=
name|accept
assert|;
name|Destination
name|dest
init|=
name|reliableEndpoint
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|accept
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
name|RMUtils
operator|.
name|getAddressingConstants
argument_list|()
operator|.
name|getNoneURI
argument_list|()
operator|.
name|equals
argument_list|(
name|address
argument_list|)
condition|)
block|{
name|DestinationSequence
name|ds
init|=
operator|new
name|DestinationSequence
argument_list|(
name|offeredId
argument_list|,
name|accept
operator|.
name|getAcksTo
argument_list|()
argument_list|,
name|dest
argument_list|,
name|protocol
argument_list|)
decl_stmt|;
name|dest
operator|.
name|addSequence
argument_list|(
name|ds
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Object
name|terminateSequence
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Terminating sequence"
argument_list|)
expr_stmt|;
specifier|final
name|ProtocolVariation
name|protocol
init|=
name|RMContextUtils
operator|.
name|getProtocolVariation
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|EncoderDecoder
name|codec
init|=
name|protocol
operator|.
name|getCodec
argument_list|()
decl_stmt|;
name|TerminateSequenceType
name|terminate
init|=
name|codec
operator|.
name|convertReceivedTerminateSequence
argument_list|(
name|getParameter
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
comment|// check if the terminated sequence was created in response to a a createSequence
comment|// request
name|Destination
name|destination
init|=
name|reliableEndpoint
operator|.
name|getDestination
argument_list|()
decl_stmt|;
name|Identifier
name|sid
init|=
name|terminate
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
name|DestinationSequence
name|terminatedSeq
init|=
name|destination
operator|.
name|getSequence
argument_list|(
name|sid
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|terminatedSeq
condition|)
block|{
comment|//  TODO
name|LOG
operator|.
name|severe
argument_list|(
literal|"No such sequence."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|destination
operator|.
name|removeSequence
argument_list|(
name|terminatedSeq
argument_list|)
expr_stmt|;
comment|// the following may be necessary if the last message for this sequence was a oneway
comment|// request and hence there was no response to which a last message could have been added
comment|// REVISIT: A last message for the correlated sequence should have been sent by the time
comment|// the last message for the underlying sequence was received.
name|Source
name|source
init|=
name|reliableEndpoint
operator|.
name|getSource
argument_list|()
decl_stmt|;
for|for
control|(
name|SourceSequence
name|outboundSeq
range|:
name|source
operator|.
name|getAllSequences
argument_list|()
control|)
block|{
if|if
condition|(
name|outboundSeq
operator|.
name|offeredBy
argument_list|(
name|sid
argument_list|)
operator|&&
operator|!
name|outboundSeq
operator|.
name|isLastMessage
argument_list|()
condition|)
block|{
if|if
condition|(
name|outboundSeq
operator|.
name|getCurrentMessageNr
argument_list|()
operator|==
literal|0
condition|)
block|{
name|source
operator|.
name|removeSequence
argument_list|(
name|outboundSeq
argument_list|)
expr_stmt|;
block|}
comment|// send an out of band message with an empty body and a
comment|// sequence header containing a lastMessage element.
comment|/*                 Proxy proxy = new Proxy(reliableEndpoint);                 try {                     proxy.lastMessage(outboundSeq);                 } catch (RMException ex) {                     LogUtils.log(LOG, Level.SEVERE, "CORRELATED_SEQ_TERMINATION_EXC", ex);                 }                 */
break|break;
block|}
block|}
name|TerminateSequenceResponseType
name|terminateResponse
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|RM11Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|protocol
operator|.
name|getWSRMNamespace
argument_list|()
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
literal|false
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|outMessage
condition|)
block|{
comment|// outMessage may be null e.g. if ReplyTo is not set for TS
name|outMessage
operator|=
name|ContextUtils
operator|.
name|createMessage
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setOutMessage
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|outMessage
condition|)
block|{
name|RMContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|outMessage
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|terminateResponse
operator|=
operator|new
name|TerminateSequenceResponseType
argument_list|()
expr_stmt|;
name|terminateResponse
operator|.
name|setIdentifier
argument_list|(
name|sid
argument_list|)
expr_stmt|;
block|}
return|return
name|terminateResponse
return|;
block|}
name|Object
name|getParameter
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|resList
init|=
literal|null
decl_stmt|;
comment|// assert message == message.getExchange().getInMessage();
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|resList
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resList
operator|!=
literal|null
condition|)
block|{
return|return
name|resList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
name|Identifier
name|clearUnattachedIdentifier
parameter_list|()
block|{
name|Identifier
name|ret
init|=
name|unattachedIdentifier
decl_stmt|;
name|unattachedIdentifier
operator|=
literal|null
expr_stmt|;
return|return
name|ret
return|;
block|}
name|void
name|setUnattachedIdentifier
parameter_list|(
name|Identifier
name|i
parameter_list|)
block|{
name|unattachedIdentifier
operator|=
name|i
expr_stmt|;
block|}
block|}
end_class

end_unit

