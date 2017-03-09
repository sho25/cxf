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
name|Executor
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
name|Client
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
name|ClientImpl
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
name|service
operator|.
name|model
operator|.
name|InterfaceInfo
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
name|workqueue
operator|.
name|SynchronousExecutor
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
name|RelatesToType
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
name|SourcePolicyType
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
name|CloseSequenceType
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
name|TerminateSequenceType
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|Proxy
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
name|Proxy
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|RMEndpoint
name|reliableEndpoint
decl_stmt|;
comment|// REVISIT assumption there is only a single outstanding offer
specifier|private
name|Identifier
name|offeredIdentifier
decl_stmt|;
comment|//hold the sequence message context
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sequenceContext
decl_stmt|;
specifier|public
name|Proxy
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
name|RMEndpoint
name|getReliableEndpoint
parameter_list|()
block|{
return|return
name|reliableEndpoint
return|;
block|}
name|void
name|acknowledge
parameter_list|(
name|DestinationSequence
name|ds
parameter_list|)
throws|throws
name|RMException
block|{
specifier|final
name|ProtocolVariation
name|protocol
init|=
name|ds
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|ds
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
literal|"STANDALONE_ANON_ACKS_NOT_SUPPORTED"
argument_list|)
expr_stmt|;
return|return;
block|}
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|reliableEndpoint
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
name|constants
operator|.
name|getSequenceAckOperationName
argument_list|()
argument_list|)
decl_stmt|;
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|ds
block|}
argument_list|,
name|this
operator|.
name|sequenceContext
argument_list|)
expr_stmt|;
block|}
name|void
name|terminate
parameter_list|(
name|SourceSequence
name|ss
parameter_list|)
throws|throws
name|RMException
block|{
name|ProtocolVariation
name|protocol
init|=
name|ss
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|reliableEndpoint
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
name|constants
operator|.
name|getTerminateSequenceOperationName
argument_list|()
argument_list|)
decl_stmt|;
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
name|ss
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|ts
operator|.
name|setLastMsgNumber
argument_list|(
name|ss
operator|.
name|getCurrentMessageNr
argument_list|()
argument_list|)
expr_stmt|;
name|EncoderDecoder
name|codec
init|=
name|protocol
operator|.
name|getCodec
argument_list|()
decl_stmt|;
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|codec
operator|.
name|convertToSend
argument_list|(
name|ts
argument_list|)
block|}
argument_list|,
name|this
operator|.
name|sequenceContext
argument_list|)
expr_stmt|;
block|}
name|void
name|terminate
parameter_list|(
name|DestinationSequence
name|ds
parameter_list|)
throws|throws
name|RMException
block|{
name|ProtocolVariation
name|protocol
init|=
name|ds
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|reliableEndpoint
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
name|constants
operator|.
name|getTerminateSequenceOperationName
argument_list|()
argument_list|)
decl_stmt|;
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
name|ds
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|ts
operator|.
name|setLastMsgNumber
argument_list|(
name|ds
operator|.
name|getLastMessageNumber
argument_list|()
argument_list|)
expr_stmt|;
name|EncoderDecoder
name|codec
init|=
name|protocol
operator|.
name|getCodec
argument_list|()
decl_stmt|;
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|codec
operator|.
name|convertToSend
argument_list|(
name|ts
argument_list|)
block|}
argument_list|,
name|this
operator|.
name|sequenceContext
argument_list|)
expr_stmt|;
block|}
name|void
name|createSequenceResponse
parameter_list|(
specifier|final
name|Object
name|createResponse
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|)
throws|throws
name|RMException
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"sending CreateSequenceResponse from client side"
argument_list|)
expr_stmt|;
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
specifier|final
name|OperationInfo
name|oi
init|=
name|reliableEndpoint
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
name|constants
operator|.
name|getCreateSequenceResponseOnewayOperationName
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: need to set relatesTo
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|createResponse
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CreateSequenceResponseType
name|createSequence
parameter_list|(
name|EndpointReferenceType
name|defaultAcksTo
parameter_list|,
name|RelatesToType
name|relatesTo
parameter_list|,
name|boolean
name|isServer
parameter_list|,
specifier|final
name|ProtocolVariation
name|protocol
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
throws|throws
name|RMException
block|{
name|this
operator|.
name|sequenceContext
operator|=
name|context
expr_stmt|;
name|SourcePolicyType
name|sp
init|=
name|reliableEndpoint
operator|.
name|getManager
argument_list|()
operator|.
name|getSourcePolicy
argument_list|()
decl_stmt|;
name|CreateSequenceType
name|create
init|=
operator|new
name|CreateSequenceType
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|sp
operator|.
name|getAcksTo
argument_list|()
decl_stmt|;
name|EndpointReferenceType
name|acksTo
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|address
condition|)
block|{
name|acksTo
operator|=
name|RMUtils
operator|.
name|createReference
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|acksTo
operator|=
name|defaultAcksTo
expr_stmt|;
block|}
name|create
operator|.
name|setAcksTo
argument_list|(
name|acksTo
argument_list|)
expr_stmt|;
name|Duration
name|d
init|=
name|sp
operator|.
name|getSequenceExpiration
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|d
condition|)
block|{
name|Expires
name|expires
init|=
operator|new
name|Expires
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setValue
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|create
operator|.
name|setExpires
argument_list|(
name|expires
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sp
operator|.
name|isIncludeOffer
argument_list|()
condition|)
block|{
name|OfferType
name|offer
init|=
operator|new
name|OfferType
argument_list|()
decl_stmt|;
name|d
operator|=
name|sp
operator|.
name|getOfferedSequenceExpiration
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|d
condition|)
block|{
name|Expires
name|expires
init|=
operator|new
name|Expires
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setValue
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|offer
operator|.
name|setExpires
argument_list|(
name|expires
argument_list|)
expr_stmt|;
block|}
name|offer
operator|.
name|setIdentifier
argument_list|(
name|reliableEndpoint
operator|.
name|getSource
argument_list|()
operator|.
name|generateSequenceIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|offer
operator|.
name|setEndpoint
argument_list|(
name|acksTo
argument_list|)
expr_stmt|;
name|create
operator|.
name|setOffer
argument_list|(
name|offer
argument_list|)
expr_stmt|;
name|setOfferedIdentifier
argument_list|(
name|offer
argument_list|)
expr_stmt|;
block|}
name|InterfaceInfo
name|ii
init|=
name|reliableEndpoint
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
decl_stmt|;
name|EncoderDecoder
name|codec
init|=
name|protocol
operator|.
name|getCodec
argument_list|()
decl_stmt|;
name|RMConstants
name|constants
init|=
name|codec
operator|.
name|getConstants
argument_list|()
decl_stmt|;
specifier|final
name|OperationInfo
name|oi
init|=
name|isServer
condition|?
name|ii
operator|.
name|getOperation
argument_list|(
name|constants
operator|.
name|getCreateSequenceOnewayOperationName
argument_list|()
argument_list|)
else|:
name|ii
operator|.
name|getOperation
argument_list|(
name|constants
operator|.
name|getCreateSequenceOperationName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|send
init|=
name|codec
operator|.
name|convertToSend
argument_list|(
name|create
argument_list|)
decl_stmt|;
comment|// tried using separate thread - did not help either
if|if
condition|(
name|isServer
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"sending CreateSequenceRequest from server side"
argument_list|)
expr_stmt|;
name|Runnable
name|r
init|=
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|send
block|}
argument_list|,
literal|null
argument_list|,
name|exchange
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
decl_stmt|;
name|Executor
name|ex
init|=
name|reliableEndpoint
operator|.
name|getApplicationEndpoint
argument_list|()
operator|.
name|getExecutor
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|==
literal|null
condition|)
block|{
name|ex
operator|=
name|SynchronousExecutor
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
name|ex
operator|.
name|execute
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|Object
name|resp
init|=
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|send
block|}
argument_list|,
name|context
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|codec
operator|.
name|convertReceivedCreateSequenceResponse
argument_list|(
name|resp
argument_list|)
return|;
block|}
name|void
name|lastMessage
parameter_list|(
name|SourceSequence
name|s
parameter_list|)
throws|throws
name|RMException
block|{
specifier|final
name|ProtocolVariation
name|protocol
init|=
name|s
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|EndpointReferenceType
name|target
init|=
name|s
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|AttributedURIType
name|uri
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|target
condition|)
block|{
name|uri
operator|=
name|target
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
name|String
name|addr
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|uri
condition|)
block|{
name|addr
operator|=
name|uri
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|addr
operator|==
literal|null
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
literal|"STANDALONE_CLOSE_SEQUENCE_NO_TARGET_MSG"
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
name|addr
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
literal|"STANDALONE_CLOSE_SEQUENCE_ANON_TARGET_MSG"
argument_list|)
expr_stmt|;
return|return;
block|}
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|reliableEndpoint
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
name|constants
operator|.
name|getCloseSequenceOperationName
argument_list|()
argument_list|)
decl_stmt|;
comment|// pass reference to source sequence in invocation context
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|SourceSequence
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|Object
operator|)
name|s
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|constants
operator|instanceof
name|RM11Constants
condition|)
block|{
name|CloseSequenceType
name|csr
init|=
operator|new
name|CloseSequenceType
argument_list|()
decl_stmt|;
name|csr
operator|.
name|setIdentifier
argument_list|(
name|s
operator|.
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|csr
operator|.
name|setLastMsgNumber
argument_list|(
name|s
operator|.
name|getCurrentMessageNr
argument_list|()
argument_list|)
expr_stmt|;
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{
name|csr
block|}
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|ackRequested
parameter_list|(
name|SourceSequence
name|s
parameter_list|)
throws|throws
name|RMException
block|{
specifier|final
name|ProtocolVariation
name|protocol
init|=
name|s
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|EndpointReferenceType
name|target
init|=
name|s
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|AttributedURIType
name|uri
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|target
condition|)
block|{
name|uri
operator|=
name|target
operator|.
name|getAddress
argument_list|()
expr_stmt|;
block|}
name|String
name|addr
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|uri
condition|)
block|{
name|addr
operator|=
name|uri
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|addr
operator|==
literal|null
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
literal|"STANDALONE_ACK_REQUESTED_NO_TARGET_MSG"
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
name|addr
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
literal|"STANDALONE_ACK_REQUESTED_ANON_TARGET_MSG"
argument_list|)
expr_stmt|;
return|return;
block|}
name|RMConstants
name|constants
init|=
name|protocol
operator|.
name|getConstants
argument_list|()
decl_stmt|;
name|OperationInfo
name|oi
init|=
name|reliableEndpoint
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
name|constants
operator|.
name|getAckRequestedOperationName
argument_list|()
argument_list|)
decl_stmt|;
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
name|Identifier
name|getOfferedIdentifier
parameter_list|()
block|{
return|return
name|offeredIdentifier
return|;
block|}
name|void
name|setOfferedIdentifier
parameter_list|(
name|OfferType
name|offer
parameter_list|)
block|{
if|if
condition|(
name|offer
operator|!=
literal|null
condition|)
block|{
name|offeredIdentifier
operator|=
name|offer
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
block|}
block|}
name|Object
name|invoke
parameter_list|(
name|OperationInfo
name|oi
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|RMException
block|{
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
literal|"Sending out-of-band RM protocol message {0}."
argument_list|,
name|oi
operator|==
literal|null
condition|?
literal|null
else|:
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|RMManager
name|manager
init|=
name|reliableEndpoint
operator|.
name|getManager
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|manager
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|reliableEndpoint
operator|.
name|getEndpoint
argument_list|(
name|protocol
argument_list|)
decl_stmt|;
name|BindingInfo
name|bi
init|=
name|reliableEndpoint
operator|.
name|getBindingInfo
argument_list|(
name|protocol
argument_list|)
decl_stmt|;
name|Conduit
name|c
init|=
name|reliableEndpoint
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|length
operator|>
literal|0
operator|&&
name|params
index|[
literal|0
index|]
operator|instanceof
name|DestinationSequence
condition|)
block|{
name|EndpointReferenceType
name|acksTo
init|=
operator|(
operator|(
name|DestinationSequence
operator|)
name|params
index|[
literal|0
index|]
operator|)
operator|.
name|getAcksTo
argument_list|()
decl_stmt|;
name|String
name|acksAddress
init|=
name|acksTo
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|AttributedURIType
name|attrURIType
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|attrURIType
operator|.
name|setValue
argument_list|(
name|acksAddress
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|acks
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|acks
operator|.
name|setAddress
argument_list|(
name|attrURIType
argument_list|)
expr_stmt|;
name|client
operator|=
name|createClient
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|,
name|protocol
argument_list|,
name|c
argument_list|,
name|acks
argument_list|)
expr_stmt|;
name|params
operator|=
operator|new
name|Object
index|[]
block|{}
expr_stmt|;
block|}
else|else
block|{
name|EndpointReferenceType
name|replyTo
init|=
name|reliableEndpoint
operator|.
name|getReplyTo
argument_list|()
decl_stmt|;
name|client
operator|=
name|createClient
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|,
name|protocol
argument_list|,
name|c
argument_list|,
name|replyTo
argument_list|)
expr_stmt|;
block|}
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
try|try
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
name|Object
index|[]
name|result
init|=
name|client
operator|.
name|invoke
argument_list|(
name|boi
argument_list|,
name|params
argument_list|,
name|context
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
name|result
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|result
index|[
literal|0
index|]
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
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
literal|"SEND_PROTOCOL_MSG_FAILED_EXC"
argument_list|,
name|LOG
argument_list|,
name|oi
operator|==
literal|null
condition|?
literal|null
else|:
name|oi
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RMException
argument_list|(
name|msg
argument_list|,
name|ex
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
name|Object
name|invoke
parameter_list|(
name|OperationInfo
name|oi
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|,
name|Object
index|[]
name|params
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
throws|throws
name|RMException
block|{
return|return
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
name|params
argument_list|,
name|context
argument_list|,
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
return|;
block|}
name|Object
name|invoke
parameter_list|(
name|OperationInfo
name|oi
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
throws|throws
name|RMException
block|{
return|return
name|invoke
argument_list|(
name|oi
argument_list|,
name|protocol
argument_list|,
name|params
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|Client
name|createClient
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|ProtocolVariation
name|protocol
parameter_list|,
name|Conduit
name|conduit
parameter_list|,
specifier|final
name|EndpointReferenceType
name|address
parameter_list|)
block|{
name|ConduitSelector
name|cs
init|=
operator|new
name|DeferredConduitSelector
argument_list|(
name|conduit
argument_list|)
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
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
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
name|RMClient
name|client
init|=
operator|new
name|RMClient
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|,
name|cs
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
init|=
name|client
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_VERSION_PROPERTY
argument_list|,
name|protocol
operator|.
name|getWSRMNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|RMManager
operator|.
name|WSRM_WSA_VERSION_PROPERTY
argument_list|,
name|protocol
operator|.
name|getWSANamespace
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
class|class
name|RMClient
extends|extends
name|ClientImpl
block|{
name|RMClient
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|ConduitSelector
name|cs
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|endpoint
argument_list|,
name|cs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|Proxy
operator|.
name|this
operator|.
name|reliableEndpoint
operator|.
name|getApplicationEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|onMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
comment|// for test
name|void
name|setReliableEndpoint
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
block|}
end_class

end_unit

