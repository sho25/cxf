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
name|Collection
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
name|Iterator
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
name|ListIterator
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
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|soap
operator|.
name|SoapMessage
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
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|headers
operator|.
name|Header
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
name|Interceptor
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
name|InterceptorChain
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
name|phase
operator|.
name|PhaseInterceptor
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
name|addressing
operator|.
name|soap
operator|.
name|MAPCodec
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
name|AbstractRMInterceptor
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
name|EncoderDecoder
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
name|ProtocolVariation
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
name|RM10Constants
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
name|RM11Constants
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
name|RMConfiguration
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
name|RMConstants
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
name|RMEndpoint
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
name|RMException
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
name|v200702
operator|.
name|AckRequestedType
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
name|wsdl
operator|.
name|interceptors
operator|.
name|BareInInterceptor
import|;
end_import

begin_comment
comment|/**  * Protocol Handler responsible for {en|de}coding the RM  * Properties for {outgo|incom}ing messages.  */
end_comment

begin_class
specifier|public
class|class
name|RMSoapInInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|protected
specifier|static
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|QName
argument_list|>
name|HEADERS
decl_stmt|;
static|static
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|RM10Constants
operator|.
name|HEADERS
argument_list|)
expr_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|RM11Constants
operator|.
name|HEADERS
argument_list|)
expr_stmt|;
name|HEADERS
operator|=
name|set
expr_stmt|;
block|}
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
name|RMSoapInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|RMSoapInInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|MAPCodec
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// AbstractSoapInterceptor interface
comment|/**      * @return the set of SOAP headers understood by this handler      */
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
return|return
name|HEADERS
return|;
block|}
comment|// Interceptor interface
comment|/* (non-Javadoc)      * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)      */
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|decode
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|updateServiceModelInfo
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**      * Decode the RM properties from protocol-specific headers      * and store them in the message.      *      * @param message the SOAP mesage      */
name|void
name|decode
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|RMProperties
name|rmps
init|=
name|unmarshalRMProperties
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|RMContextUtils
operator|.
name|storeRMProperties
argument_list|(
name|message
argument_list|,
name|rmps
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// TODO: decode SequenceFault ?
block|}
comment|/**      * Decode the RM properties from protocol-specific headers.      *      * @param message the SOAP message      * @return the RM properties      */
specifier|public
name|RMProperties
name|unmarshalRMProperties
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|RMProperties
name|rmps
init|=
operator|(
name|RMProperties
operator|)
name|message
operator|.
name|get
argument_list|(
name|RMContextUtils
operator|.
name|getRMPropertiesKey
argument_list|(
literal|false
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rmps
operator|==
literal|null
condition|)
block|{
name|rmps
operator|=
operator|new
name|RMProperties
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|decodeHeaders
argument_list|(
name|message
argument_list|,
name|headers
argument_list|,
name|rmps
argument_list|)
expr_stmt|;
block|}
return|return
name|rmps
return|;
block|}
specifier|public
name|void
name|decodeHeaders
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|List
argument_list|<
name|Header
argument_list|>
name|headers
parameter_list|,
name|RMProperties
name|rmps
parameter_list|)
block|{
try|try
block|{
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|AckRequestedType
argument_list|>
name|requested
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|rmUri
init|=
literal|null
decl_stmt|;
name|EncoderDecoder
name|codec
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|Header
argument_list|>
name|iter
init|=
name|headers
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|node
init|=
name|iter
operator|.
name|next
argument_list|()
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|elem
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|!=
name|elem
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|String
name|ns
init|=
name|elem
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|rmUri
operator|==
literal|null
operator|&&
operator|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
operator|||
name|RM11Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
operator|)
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
literal|"set RM namespace {0}"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|rmUri
operator|=
name|ns
expr_stmt|;
name|rmps
operator|.
name|exposeAs
argument_list|(
name|rmUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rmUri
operator|!=
literal|null
operator|&&
name|rmUri
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
if|if
condition|(
name|codec
operator|==
literal|null
condition|)
block|{
name|String
name|wsauri
init|=
literal|null
decl_stmt|;
name|AddressingProperties
name|maps
init|=
name|ContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
literal|null
condition|)
block|{
name|RMConfiguration
name|config
init|=
name|getManager
argument_list|(
name|message
argument_list|)
operator|.
name|getEffectiveConfiguration
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|wsauri
operator|=
name|config
operator|.
name|getAddressingNamespace
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wsauri
operator|=
name|maps
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
block|}
name|ProtocolVariation
name|protocol
init|=
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|rmUri
argument_list|,
name|wsauri
argument_list|)
decl_stmt|;
if|if
condition|(
name|protocol
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
literal|"NAMESPACE_ERROR_MSG"
argument_list|,
name|wsauri
argument_list|)
expr_stmt|;
break|break;
block|}
name|codec
operator|=
name|protocol
operator|.
name|getCodec
argument_list|()
expr_stmt|;
block|}
name|String
name|localName
init|=
name|elem
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"decoding RM header {0}"
argument_list|,
name|localName
argument_list|)
expr_stmt|;
if|if
condition|(
name|RMConstants
operator|.
name|SEQUENCE_NAME
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|rmps
operator|.
name|setSequence
argument_list|(
name|codec
operator|.
name|decodeSequenceType
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
name|rmps
operator|.
name|setCloseSequence
argument_list|(
name|codec
operator|.
name|decodeSequenceTypeCloseSequence
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RMConstants
operator|.
name|SEQUENCE_ACK_NAME
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|acks
operator|.
name|add
argument_list|(
name|codec
operator|.
name|decodeSequenceAcknowledgement
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RMConstants
operator|.
name|ACK_REQUESTED_NAME
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|requested
operator|.
name|add
argument_list|(
name|codec
operator|.
name|decodeAckRequestedType
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|acks
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rmps
operator|.
name|setAcks
argument_list|(
name|acks
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|requested
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rmps
operator|.
name|setAcksRequested
argument_list|(
name|requested
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
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
literal|"SOAP_HEADER_DECODE_FAILURE_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * When invoked inbound, check if the action indicates that this is one of the      * RM protocol messages (CreateSequence, CreateSequenceResponse, TerminateSequence)      * and if so, replace references to the application service model with references to      * the RM service model.      * The addressing protocol handler must have extracted the action beforehand.      * @see org.apache.cxf.transport.ChainInitiationObserver      *      * @param message the message      */
specifier|private
name|void
name|updateServiceModelInfo
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|AddressingProperties
name|maps
init|=
name|ContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|AttributedURIType
name|actionURI
init|=
literal|null
operator|==
name|maps
condition|?
literal|null
else|:
name|maps
operator|.
name|getAction
argument_list|()
decl_stmt|;
name|String
name|action
init|=
literal|null
operator|==
name|actionURI
condition|?
literal|null
else|:
name|actionURI
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"action: "
operator|+
name|action
argument_list|)
expr_stmt|;
name|RMConstants
name|consts
decl_stmt|;
if|if
condition|(
name|RM10Constants
operator|.
name|ACTIONS
operator|.
name|contains
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|consts
operator|=
name|RM10Constants
operator|.
name|INSTANCE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RM11Constants
operator|.
name|ACTIONS
operator|.
name|contains
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|consts
operator|=
name|RM11Constants
operator|.
name|INSTANCE
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
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
name|rmps
operator|.
name|exposeAs
argument_list|(
name|consts
operator|.
name|getWSRMNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|ProtocolVariation
name|protocol
init|=
name|ProtocolVariation
operator|.
name|findVariant
argument_list|(
name|consts
operator|.
name|getWSRMNamespace
argument_list|()
argument_list|,
name|maps
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Updating service model info in exchange"
argument_list|)
expr_stmt|;
name|RMManager
name|manager
init|=
name|getManager
argument_list|(
name|message
argument_list|)
decl_stmt|;
assert|assert
name|manager
operator|!=
literal|null
assert|;
name|RMEndpoint
name|rme
init|=
literal|null
decl_stmt|;
try|try
block|{
name|rme
operator|=
name|manager
operator|.
name|getReliableEndpoint
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RMException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
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
literal|"CANNOT_PROCESS"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
name|rme
operator|.
name|getEndpoint
argument_list|(
name|protocol
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|ep
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|ep
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|ep
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
comment|// Also set BindingOperationInfo as some operations (SequenceAcknowledgment) have
comment|// neither in nor out messages, and thus the WrappedInInterceptor cannot
comment|// determine the operation name.
name|BindingInfo
name|bi
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
literal|null
decl_stmt|;
name|boolean
name|isOneway
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|consts
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
if|if
condition|(
name|RMContextUtils
operator|.
name|isServerSide
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getCreateSequenceOperationName
argument_list|()
argument_list|)
expr_stmt|;
name|isOneway
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getCreateSequenceOnewayOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|consts
operator|.
name|getCreateSequenceResponseAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
if|if
condition|(
name|RMContextUtils
operator|.
name|isServerSide
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getCreateSequenceResponseOnewayOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getCreateSequenceOperationName
argument_list|()
argument_list|)
expr_stmt|;
name|isOneway
operator|=
literal|false
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|consts
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
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getSequenceAckOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|consts
operator|.
name|getAckRequestedAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getAckRequestedOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|consts
operator|.
name|getTerminateSequenceAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getTerminateSequenceOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getTerminateSequenceResponseAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
comment|//TODO add server-side TSR handling
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getTerminateSequenceOperationName
argument_list|()
argument_list|)
expr_stmt|;
name|isOneway
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|consts
operator|.
name|getCloseSequenceAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|consts
operator|.
name|getCloseSequenceOperationName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getCloseSequenceResponseAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|boi
operator|=
name|bi
operator|.
name|getOperation
argument_list|(
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getCloseSequenceOperationName
argument_list|()
argument_list|)
expr_stmt|;
name|isOneway
operator|=
literal|false
expr_stmt|;
block|}
comment|// make sure the binding information has been set
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No BindingInfo for action "
operator|+
name|action
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
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
name|exchange
operator|.
name|setOneWay
argument_list|(
name|isOneway
argument_list|)
expr_stmt|;
block|}
comment|// Fix requestor role (as the client side message observer always sets it to TRUE)
comment|// to allow unmarshalling the body of a server originated TerminateSequence request.
comment|// In the logical RM interceptor set it back to what it was so that the logical
comment|// addressing interceptor does not try to send a partial response to
comment|// server originated oneway RM protocol messages.
comment|//
if|if
condition|(
operator|!
name|consts
operator|.
name|getCreateSequenceResponseAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|&&
operator|!
name|consts
operator|.
name|getSequenceAckAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|&&
operator|!
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getTerminateSequenceResponseAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
operator|&&
operator|!
name|RM11Constants
operator|.
name|INSTANCE
operator|.
name|getCloseSequenceResponseAction
argument_list|()
operator|.
name|equals
argument_list|(
name|action
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Changing requestor role from "
operator|+
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
operator|+
literal|" to false"
argument_list|)
expr_stmt|;
name|Object
name|originalRequestorRole
init|=
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|originalRequestorRole
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|RMMessageConstants
operator|.
name|ORIGINAL_REQUESTOR_ROLE
argument_list|,
name|originalRequestorRole
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
comment|// replace WrappedInInterceptor with BareInInterceptor if necessary
comment|// as RM protocol messages use parameter style BARE
name|InterceptorChain
name|chain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
name|ListIterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|it
init|=
name|chain
operator|.
name|getIterator
argument_list|()
decl_stmt|;
name|boolean
name|bareIn
init|=
literal|false
decl_stmt|;
name|boolean
name|wrappedIn
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
operator|&&
operator|!
name|wrappedIn
operator|&&
operator|!
name|bareIn
condition|)
block|{
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|pi
init|=
operator|(
name|PhaseInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|BareInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|pi
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|bareIn
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|bareIn
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|new
name|BareInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Added BareInInterceptor to chain."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|RMManager
name|getManager
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|InterceptorChain
name|chain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
name|ListIterator
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|it
init|=
name|chain
operator|.
name|getIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
name|i
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|instanceof
name|AbstractRMInterceptor
condition|)
block|{
return|return
operator|(
operator|(
name|AbstractRMInterceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
operator|)
name|i
operator|)
operator|.
name|getManager
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

