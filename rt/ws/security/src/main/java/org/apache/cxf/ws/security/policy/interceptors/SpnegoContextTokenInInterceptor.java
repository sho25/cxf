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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapBindingConstants
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
name|SoapActionInInterceptor
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
name|security
operator|.
name|SecurityContext
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
name|AssertionInfoMap
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
name|security
operator|.
name|policy
operator|.
name|interceptors
operator|.
name|HttpsTokenInterceptorProvider
operator|.
name|HttpsTokenInInterceptor
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|WSS4JInInterceptor
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
name|wss4j
operator|.
name|WSS4JStaxInInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|All
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|ExactlyOne
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
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
name|spnego
operator|.
name|SpnegoTokenContext
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
name|WSSConfig
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
name|BinarySecurity
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
name|policy
operator|.
name|SPConstants
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

begin_class
class|class
name|SpnegoContextTokenInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
name|SpnegoContextTokenInInterceptor
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
name|WSS4JStaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|HttpsTokenInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SPNEGO_CONTEXT_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|//client side should be checked on the way out
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|String
name|s
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapBindingConstants
operator|.
name|SOAP_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|SoapActionInInterceptor
operator|.
name|getSoapAction
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|AddressingProperties
name|inProps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|JAXWSAConstants
operator|.
name|ADDRESSING_PROPERTIES_INBOUND
argument_list|)
decl_stmt|;
if|if
condition|(
name|inProps
operator|!=
literal|null
operator|&&
name|s
operator|==
literal|null
condition|)
block|{
comment|//MS/WCF doesn't put a soap action out for this, must check the headers
name|s
operator|=
name|inProps
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
name|s
operator|!=
literal|null
operator|&&
name|s
operator|.
name|contains
argument_list|(
literal|"/RST/Issue"
argument_list|)
operator|&&
operator|(
name|s
operator|.
name|startsWith
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_02
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_12
argument_list|)
operator|)
condition|)
block|{
name|Policy
name|p
init|=
operator|new
name|Policy
argument_list|()
decl_stmt|;
name|ExactlyOne
name|ea
init|=
operator|new
name|ExactlyOne
argument_list|()
decl_stmt|;
name|p
operator|.
name|addPolicyComponent
argument_list|(
name|ea
argument_list|)
expr_stmt|;
name|All
name|all
init|=
operator|new
name|All
argument_list|()
decl_stmt|;
name|Assertion
name|ass
init|=
name|NegotiationUtils
operator|.
name|getAddressingPolicy
argument_list|(
name|aim
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|all
operator|.
name|addPolicyComponent
argument_list|(
name|ass
argument_list|)
expr_stmt|;
name|ea
operator|.
name|addPolicyComponent
argument_list|(
name|all
argument_list|)
expr_stmt|;
comment|//setup endpoint and forward to it.
name|unmapSecurityProps
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|String
name|ns
init|=
name|STSUtils
operator|.
name|WST_NS_05_12
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
name|STSUtils
operator|.
name|WST_NS_05_02
argument_list|)
condition|)
block|{
name|ns
operator|=
name|STSUtils
operator|.
name|WST_NS_05_02
expr_stmt|;
block|}
name|NegotiationUtils
operator|.
name|recalcEffectivePolicy
argument_list|(
name|message
argument_list|,
name|ns
argument_list|,
name|p
argument_list|,
operator|new
name|SpnegoSTSInvoker
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|SpnegoContextTokenFinderInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|unmapSecurityProps
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|SecurityConstants
operator|.
name|ALL_PROPERTIES
control|)
block|{
name|Object
name|v
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|ex
operator|.
name|put
argument_list|(
name|s
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
class|class
name|SpnegoSTSInvoker
extends|extends
name|STSInvoker
block|{
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
block|{
name|SpnegoTokenContext
name|spnegoToken
init|=
name|handleBinaryExchange
argument_list|(
name|binaryExchange
argument_list|,
name|exchange
operator|.
name|getInMessage
argument_list|()
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
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
name|String
name|context
init|=
name|requestEl
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Context"
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|context
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Context"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|// Find TokenType and KeySize
name|int
name|keySize
init|=
literal|256
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
literal|"KeySize"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|keySize
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|)
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
name|el
operator|.
name|getTextContent
argument_list|()
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
comment|// Check received KeySize
if|if
condition|(
name|keySize
argument_list|<
literal|128
operator|||
name|keySize
argument_list|>
literal|512
condition|)
block|{
name|keySize
operator|=
literal|256
expr_stmt|;
block|}
comment|// TokenType
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"TokenType"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
name|tokenType
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
name|prefix
argument_list|,
literal|"RequestedSecurityToken"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
comment|// SecurityContextToken
name|SecurityContextToken
name|sct
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|NegotiationUtils
operator|.
name|getWSCVersion
argument_list|(
name|tokenType
argument_list|)
argument_list|,
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|WSSConfig
name|wssConfig
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
name|sct
operator|.
name|setID
argument_list|(
name|wssConfig
operator|.
name|getIdAllocator
argument_list|()
operator|.
name|createId
argument_list|(
literal|"sctId-"
argument_list|,
name|sct
argument_list|)
argument_list|)
expr_stmt|;
comment|// Lifetime
name|Date
name|created
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|expires
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setTime
argument_list|(
name|created
operator|.
name|getTime
argument_list|()
operator|+
literal|300000L
argument_list|)
expr_stmt|;
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|created
argument_list|,
name|expires
argument_list|)
decl_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|sct
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|SecurityContext
name|sc
init|=
name|exchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setSecurityContext
argument_list|(
name|sc
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// References
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"RequestedAttachedReference"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|token
operator|.
name|setAttachedReference
argument_list|(
name|writeSecurityTokenReference
argument_list|(
name|writer
argument_list|,
literal|"#"
operator|+
name|sct
operator|.
name|getID
argument_list|()
argument_list|,
name|tokenType
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
name|prefix
argument_list|,
literal|"RequestedUnattachedReference"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|token
operator|.
name|setUnattachedReference
argument_list|(
name|writeSecurityTokenReference
argument_list|(
name|writer
argument_list|,
name|sct
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|tokenType
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|writeLifetime
argument_list|(
name|writer
argument_list|,
name|created
argument_list|,
name|expires
argument_list|,
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
comment|// KeySize
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
literal|"KeySize"
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeCharacters
argument_list|(
literal|""
operator|+
name|keySize
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|byte
index|[]
name|secret
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
name|byte
index|[]
name|key
init|=
name|spnegoToken
operator|.
name|wrapKey
argument_list|(
name|secret
argument_list|)
decl_stmt|;
name|writeProofToken
argument_list|(
name|writer
argument_list|,
name|prefix
argument_list|,
name|namespace
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|/*             // Second RequestSecurityTokenResponse containing the Authenticator             // TODO             writer.writeStartElement(prefix, "RequestSecurityTokenResponse", namespace);             if (context != null&& !"".equals(context)) {                 writer.writeAttribute("Context", context);             }             writeAuthenticator(writer, prefix, namespace, secret);             writer.writeEndElement();             */
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|spnegoToken
operator|.
name|clear
argument_list|()
expr_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|secret
argument_list|)
expr_stmt|;
operator|(
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
operator|)
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|void
name|doRenew
parameter_list|(
name|Element
name|requestEl
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SecurityToken
name|securityToken
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
block|{
comment|//Not implemented
block|}
specifier|private
name|SpnegoTokenContext
name|handleBinaryExchange
parameter_list|(
name|Element
name|binaryExchange
parameter_list|,
name|Message
name|message
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|binaryExchange
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No BinaryExchange element received"
argument_list|)
throw|;
block|}
name|String
name|encoding
init|=
name|binaryExchange
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"EncodingType"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|BinarySecurity
operator|.
name|BASE64_ENCODING
operator|.
name|equals
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown encoding type: "
operator|+
name|encoding
argument_list|)
throw|;
block|}
name|String
name|valueType
init|=
name|binaryExchange
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ValueType"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|namespace
operator|+
literal|"/spnego"
operator|)
operator|.
name|equals
argument_list|(
name|valueType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Unknown value type: "
operator|+
name|valueType
argument_list|)
throw|;
block|}
name|String
name|content
init|=
name|DOMUtils
operator|.
name|getContent
argument_list|(
name|binaryExchange
argument_list|)
decl_stmt|;
name|byte
index|[]
name|decodedContent
init|=
name|Base64
operator|.
name|decode
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|String
name|jaasContext
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|KERBEROS_JAAS_CONTEXT_NAME
argument_list|)
decl_stmt|;
name|String
name|kerberosSpn
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|KERBEROS_SPN
argument_list|)
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
name|NegotiationUtils
operator|.
name|getCallbackHandler
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|SpnegoTokenContext
name|spnegoToken
init|=
operator|new
name|SpnegoTokenContext
argument_list|()
decl_stmt|;
name|spnegoToken
operator|.
name|validateServiceTicket
argument_list|(
name|jaasContext
argument_list|,
name|callbackHandler
argument_list|,
name|kerberosSpn
argument_list|,
name|decodedContent
argument_list|)
expr_stmt|;
return|return
name|spnegoToken
return|;
block|}
specifier|private
name|void
name|writeProofToken
parameter_list|(
name|W3CDOMStreamWriter
name|writer
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|,
name|byte
index|[]
name|key
parameter_list|)
throws|throws
name|Exception
block|{
comment|// RequestedProofToken
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
comment|// EncryptedKey
name|writer
operator|.
name|writeStartElement
argument_list|(
name|WSConstants
operator|.
name|ENC_PREFIX
argument_list|,
literal|"EncryptedKey"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|WSConstants
operator|.
name|ENC_PREFIX
argument_list|,
literal|"EncryptionMethod"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Algorithm"
argument_list|,
name|namespace
operator|+
literal|"/spnego#GSS_Wrap"
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
name|WSConstants
operator|.
name|ENC_PREFIX
argument_list|,
literal|"CipherData"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeStartElement
argument_list|(
name|WSConstants
operator|.
name|ENC_PREFIX
argument_list|,
literal|"CipherValue"
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
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
name|key
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
comment|/*         private void writeAuthenticator(             W3CDOMStreamWriter writer,             String prefix,              String namespace,             byte[] secret         ) throws Exception {             // Authenticator             writer.writeStartElement(prefix, "Authenticator", namespace);                          // CombinedHash             writer.writeStartElement(prefix, "CombinedHash", namespace);                          P_SHA1 psha1 = new P_SHA1();             byte[] seed = "AUTH-HASH".getBytes();             byte[] digest = psha1.createKey(secret, seed, 0, 32);             writer.writeCharacters(Base64.encode(digest));                          writer.writeEndElement();                          writer.writeEndElement();         }         */
block|}
specifier|static
specifier|final
class|class
name|SpnegoContextTokenFinderInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|static
specifier|final
name|SpnegoContextTokenFinderInterceptor
name|INSTANCE
init|=
operator|new
name|SpnegoContextTokenFinderInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|SpnegoContextTokenFinderInterceptor
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
name|WSS4JInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|foundSCT
init|=
name|NegotiationUtils
operator|.
name|parseSCTResult
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SPNEGO_CONTEXT_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
for|for
control|(
name|AssertionInfo
name|inf
range|:
name|ais
control|)
block|{
if|if
condition|(
name|foundSCT
condition|)
block|{
name|inf
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|inf
operator|.
name|setNotAsserted
argument_list|(
literal|"No SecurityContextToken token found in message."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

