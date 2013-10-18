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
name|security
operator|.
name|policy
operator|.
name|interceptors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Document
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
name|helpers
operator|.
name|DOMUtils
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
name|staxutils
operator|.
name|W3CDOMStreamWriter
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
name|JAXWSAConstants
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
name|security
operator|.
name|SecurityConstants
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
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
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
name|security
operator|.
name|tokenstore
operator|.
name|TokenStore
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
name|security
operator|.
name|trust
operator|.
name|STSUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|derivedKey
operator|.
name|ConversationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|derivedKey
operator|.
name|P_SHA1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|bsp
operator|.
name|BSPEnforcer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|message
operator|.
name|token
operator|.
name|Reference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|message
operator|.
name|token
operator|.
name|SecurityContextToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|message
operator|.
name|token
operator|.
name|SecurityTokenReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|util
operator|.
name|XmlSchemaDateFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|utils
operator|.
name|Base64
import|;
end_import

begin_comment
comment|/**  * An abstract Invoker used by the Spnego and SecureConversationInInterceptors.  */
end_comment

begin_class
specifier|abstract
class|class
name|STSInvoker
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
name|STSInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|AddressingProperties
name|inProps
init|=
operator|(
name|AddressingProperties
operator|)
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
argument_list|)
decl_stmt|;
if|if
condition|(
name|inProps
operator|!=
literal|null
condition|)
block|{
name|AddressingProperties
name|props
init|=
name|inProps
operator|.
name|createCompatibleResponseProperties
argument_list|()
decl_stmt|;
name|AttributedURIType
name|action
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|action
operator|.
name|setValue
argument_list|(
name|inProps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|replace
argument_list|(
literal|"/RST/"
argument_list|,
literal|"/RSTR/"
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|setAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|put
argument_list|(
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
name|MessageContentsList
name|lst
init|=
operator|(
name|MessageContentsList
operator|)
name|o
decl_stmt|;
name|DOMSource
name|src
init|=
operator|(
name|DOMSource
operator|)
name|lst
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Node
name|nd
init|=
name|src
operator|.
name|getNode
argument_list|()
decl_stmt|;
name|Element
name|requestEl
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|nd
operator|instanceof
name|Document
condition|)
block|{
name|requestEl
operator|=
operator|(
operator|(
name|Document
operator|)
name|nd
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|requestEl
operator|=
operator|(
name|Element
operator|)
name|nd
expr_stmt|;
block|}
name|String
name|namespace
init|=
name|requestEl
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|requestEl
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|SecurityToken
name|cancelToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"RequestSecurityToken"
operator|.
name|equals
argument_list|(
name|requestEl
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|String
name|requestType
init|=
literal|null
decl_stmt|;
name|Element
name|binaryExchange
init|=
literal|null
decl_stmt|;
name|String
name|tokenType
init|=
literal|null
decl_stmt|;
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|requestEl
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
name|String
name|localName
init|=
name|el
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"RequestType"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|requestType
operator|=
name|el
operator|.
name|getTextContent
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"CancelTarget"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|cancelToken
operator|=
name|findCancelToken
argument_list|(
name|exchange
argument_list|,
name|el
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"BinaryExchange"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|binaryExchange
operator|=
name|el
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"TokenType"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|tokenType
operator|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|requestType
operator|==
literal|null
condition|)
block|{
name|requestType
operator|=
literal|"/Issue"
expr_stmt|;
block|}
if|if
condition|(
name|requestType
operator|.
name|endsWith
argument_list|(
literal|"/Issue"
argument_list|)
operator|&&
operator|!
name|STSUtils
operator|.
name|getTokenTypeSCT
argument_list|(
name|namespace
argument_list|)
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown token type: "
operator|+
name|tokenType
argument_list|)
throw|;
block|}
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|setNsRepairing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|requestType
operator|.
name|endsWith
argument_list|(
literal|"/Issue"
argument_list|)
condition|)
block|{
name|doIssue
argument_list|(
name|requestEl
argument_list|,
name|exchange
argument_list|,
name|binaryExchange
argument_list|,
name|writer
argument_list|,
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|requestType
operator|.
name|endsWith
argument_list|(
literal|"/Cancel"
argument_list|)
condition|)
block|{
name|doCancel
argument_list|(
name|exchange
argument_list|,
name|cancelToken
argument_list|,
name|writer
argument_list|,
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
comment|//else if (requestType.endsWith("/Renew")) {
comment|//REVISIT - implement
comment|//}
return|return
operator|new
name|MessageContentsList
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
throw|throw
name|ex
throw|;
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
else|else
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"Unknown SecureConversation element: "
operator|+
name|requestEl
operator|.
name|getLocalName
argument_list|()
argument_list|,
name|LOG
argument_list|)
throw|;
block|}
block|}
specifier|abstract
name|void
name|doIssue
parameter_list|(
name|Element
name|requestEl
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Element
name|binaryExchange
parameter_list|,
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
specifier|private
name|void
name|doCancel
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SecurityToken
name|cancelToken
parameter_list|,
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|STSUtils
operator|.
name|WST_NS_05_12
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestSecurityTokenResponseCollection"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestSecurityTokenResponse"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|TokenStore
name|store
init|=
operator|(
name|TokenStore
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|store
operator|.
name|remove
argument_list|(
name|cancelToken
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Put the token on the out message so that we can sign the response
name|exchange
operator|.
name|getEndpoint
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|cancelToken
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEmptyElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestedTokenCancelled"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|STSUtils
operator|.
name|WST_NS_05_12
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|SecurityToken
name|findCancelToken
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Element
name|el
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Element
name|childElement
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
literal|""
decl_stmt|;
if|if
condition|(
literal|"SecurityContextToken"
operator|.
name|equals
argument_list|(
name|childElement
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|SecurityContextToken
name|sct
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|childElement
argument_list|)
decl_stmt|;
name|uri
operator|=
name|sct
operator|.
name|getIdentifier
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|SecurityTokenReference
name|ref
init|=
operator|new
name|SecurityTokenReference
argument_list|(
name|childElement
argument_list|,
operator|new
name|BSPEnforcer
argument_list|()
argument_list|)
decl_stmt|;
name|uri
operator|=
name|ref
operator|.
name|getReference
argument_list|()
operator|.
name|getURI
argument_list|()
expr_stmt|;
block|}
name|TokenStore
name|store
init|=
operator|(
name|TokenStore
operator|)
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|store
operator|.
name|getToken
argument_list|(
name|uri
argument_list|)
return|;
block|}
name|byte
index|[]
name|writeProofToken
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|,
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|byte
index|[]
name|clientEntropy
parameter_list|,
name|int
name|keySize
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
throws|,
name|WSSecurityException
throws|,
name|ConversationException
throws|,
name|XMLStreamException
block|{
name|byte
name|secret
index|[]
init|=
literal|null
decl_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestedProofToken"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
if|if
condition|(
name|clientEntropy
operator|==
literal|null
condition|)
block|{
name|secret
operator|=
name|WSSecurityUtil
operator|.
name|generateNonce
argument_list|(
name|keySize
operator|/
literal|8
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"BinarySecret"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Type"
argument_list|,
name|namespace
operator|+
literal|"/Nonce"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|Base64
operator|.
name|encode
argument_list|(
name|secret
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|byte
name|entropy
index|[]
init|=
name|WSSecurityUtil
operator|.
name|generateNonce
argument_list|(
name|keySize
operator|/
literal|8
argument_list|)
decl_stmt|;
name|P_SHA1
name|psha1
init|=
operator|new
name|P_SHA1
argument_list|()
decl_stmt|;
name|secret
operator|=
name|psha1
operator|.
name|createKey
argument_list|(
name|clientEntropy
argument_list|,
name|entropy
argument_list|,
literal|0
argument_list|,
name|keySize
operator|/
literal|8
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"ComputedKey"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|namespace
operator|+
literal|"/CK/PSHA1"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"Entropy"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"BinarySecret"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Type"
argument_list|,
name|namespace
operator|+
literal|"/Nonce"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|Base64
operator|.
name|encode
argument_list|(
name|entropy
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
return|return
name|secret
return|;
block|}
name|Element
name|writeSecurityTokenReference
parameter_list|(
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|refValueType
parameter_list|)
block|{
name|Reference
name|ref
init|=
operator|new
name|Reference
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|ref
operator|.
name|setURI
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|refValueType
operator|!=
literal|null
condition|)
block|{
name|ref
operator|.
name|setValueType
argument_list|(
name|refValueType
argument_list|)
expr_stmt|;
block|}
name|SecurityTokenReference
name|str
init|=
operator|new
name|SecurityTokenReference
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|str
operator|.
name|addWSSENamespace
argument_list|()
expr_stmt|;
name|str
operator|.
name|setReference
argument_list|(
name|ref
argument_list|)
expr_stmt|;
name|writer
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|str
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|str
operator|.
name|getElement
argument_list|()
return|;
block|}
name|void
name|writeLifetime
parameter_list|(
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|Date
name|created
parameter_list|,
name|Date
name|expires
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|XmlSchemaDateFormat
name|fmt
init|=
operator|new
name|XmlSchemaDateFormat
argument_list|()
decl_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"Lifetime"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeNamespace
argument_list|(
literal|"wsu"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"wsu"
argument_list|,
literal|"Created"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|created
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
literal|"wsu"
argument_list|,
literal|"Expires"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|expires
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

