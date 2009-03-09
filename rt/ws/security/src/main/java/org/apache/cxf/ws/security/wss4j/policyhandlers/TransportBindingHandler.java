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
name|wss4j
operator|.
name|policyhandlers
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
name|Vector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|com
operator|.
name|ibm
operator|.
name|wsdl
operator|.
name|util
operator|.
name|xml
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
name|SP12Constants
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
name|SPConstants
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
name|SPConstants
operator|.
name|SupportTokenType
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
name|model
operator|.
name|AlgorithmSuite
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
name|model
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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|model
operator|.
name|IssuedToken
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
name|model
operator|.
name|KeyValueToken
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
name|model
operator|.
name|SecureConversationToken
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
name|model
operator|.
name|SignedEncryptedParts
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
name|model
operator|.
name|SupportingToken
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
name|model
operator|.
name|Token
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
name|model
operator|.
name|TokenWrapper
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
name|model
operator|.
name|TransportBinding
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
name|model
operator|.
name|UsernameToken
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
name|model
operator|.
name|X509Token
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|WSEncryptionPart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|WSPasswordCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|components
operator|.
name|crypto
operator|.
name|Crypto
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|conversation
operator|.
name|ConversationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|WSSecDKSign
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|WSSecEncryptedKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|WSSecHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|WSSecSignature
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|WSSecTimestamp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|WSSecUsernameToken
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|TransportBindingHandler
extends|extends
name|AbstractBindingBuilder
block|{
name|TransportBinding
name|tbinding
decl_stmt|;
specifier|public
name|TransportBindingHandler
parameter_list|(
name|TransportBinding
name|binding
parameter_list|,
name|SOAPMessage
name|saaj
parameter_list|,
name|WSSecHeader
name|secHeader
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|binding
argument_list|,
name|saaj
argument_list|,
name|secHeader
argument_list|,
name|aim
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|tbinding
operator|=
name|binding
expr_stmt|;
block|}
specifier|private
name|void
name|addUsernameTokens
parameter_list|(
name|SupportingToken
name|sgndSuppTokens
parameter_list|)
block|{
for|for
control|(
name|Token
name|token
range|:
name|sgndSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|UsernameToken
condition|)
block|{
name|WSSecUsernameToken
name|utBuilder
init|=
name|addUsernameToken
argument_list|(
operator|(
name|UsernameToken
operator|)
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|utBuilder
operator|!=
literal|null
condition|)
block|{
name|utBuilder
operator|.
name|prepare
argument_list|(
name|saaj
operator|.
name|getSOAPPart
argument_list|()
argument_list|)
expr_stmt|;
name|utBuilder
operator|.
name|appendToHeader
argument_list|(
name|secHeader
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|IssuedToken
condition|)
block|{
name|SecurityToken
name|secTok
init|=
name|getSecurityToken
argument_list|()
decl_stmt|;
name|SPConstants
operator|.
name|IncludeTokenType
name|inclusion
init|=
name|token
operator|.
name|getInclusion
argument_list|()
decl_stmt|;
if|if
condition|(
name|inclusion
operator|==
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS
operator|||
operator|(
operator|(
name|inclusion
operator|==
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
operator|||
name|inclusion
operator|==
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ONCE
operator|)
operator|&&
name|isRequestor
argument_list|()
operator|)
condition|)
block|{
comment|//Add the token
name|addEncyptedKeyElement
argument_list|(
name|cloneElement
argument_list|(
name|secTok
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//REVISIT - not supported for signed.  Exception?
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|addSig
parameter_list|(
name|Vector
argument_list|<
name|byte
index|[]
argument_list|>
name|signatureValues
parameter_list|,
name|byte
index|[]
name|val
parameter_list|)
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|signatureValues
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleBinding
parameter_list|()
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
decl_stmt|;
name|WSSecTimestamp
name|timestamp
init|=
name|createTimestamp
argument_list|()
decl_stmt|;
name|handleLayout
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|isRequestor
argument_list|()
condition|)
block|{
name|Vector
argument_list|<
name|byte
index|[]
argument_list|>
name|signatureValues
init|=
operator|new
name|Vector
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
name|SupportingToken
name|sgndSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|sgndSuppTokens
operator|=
operator|(
name|SupportingToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sgndSuppTokens
operator|!=
literal|null
condition|)
block|{
name|addUsernameTokens
argument_list|(
name|sgndSuppTokens
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
name|SupportingToken
name|sgndSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|sgndSuppTokens
operator|=
operator|(
name|SupportingToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sgndSuppTokens
operator|!=
literal|null
condition|)
block|{
name|SignedEncryptedParts
name|signdParts
init|=
name|sgndSuppTokens
operator|.
name|getSignedParts
argument_list|()
decl_stmt|;
for|for
control|(
name|Token
name|token
range|:
name|sgndSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|IssuedToken
operator|||
name|token
operator|instanceof
name|SecureConversationToken
operator|||
name|token
operator|instanceof
name|KeyValueToken
condition|)
block|{
name|addSig
argument_list|(
name|signatureValues
argument_list|,
name|doIssuedTokenSignature
argument_list|(
name|token
argument_list|,
name|signdParts
argument_list|,
name|sgndSuppTokens
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|X509Token
operator|||
name|token
operator|instanceof
name|KeyValueToken
condition|)
block|{
name|addSig
argument_list|(
name|signatureValues
argument_list|,
name|doX509TokenSignature
argument_list|(
name|token
argument_list|,
name|signdParts
argument_list|,
name|sgndSuppTokens
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
name|SupportingToken
name|sgndSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|sgndSuppTokens
operator|=
operator|(
name|SupportingToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sgndSuppTokens
operator|!=
literal|null
condition|)
block|{
name|addUsernameTokens
argument_list|(
name|sgndSuppTokens
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
name|SupportingToken
name|endSuppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|endSuppTokens
operator|=
operator|(
name|SupportingToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endSuppTokens
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Token
name|token
range|:
name|endSuppTokens
operator|.
name|getTokens
argument_list|()
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|IssuedToken
operator|||
name|token
operator|instanceof
name|SecureConversationToken
condition|)
block|{
name|addSig
argument_list|(
name|signatureValues
argument_list|,
name|doIssuedTokenSignature
argument_list|(
name|token
argument_list|,
name|endSuppTokens
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|endSuppTokens
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|X509Token
operator|||
name|token
operator|instanceof
name|KeyValueToken
condition|)
block|{
name|addSig
argument_list|(
name|signatureValues
argument_list|,
name|doX509TokenSignature
argument_list|(
name|token
argument_list|,
name|endSuppTokens
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|endSuppTokens
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|SecurityToken
name|token
init|=
operator|(
name|SecurityToken
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_CONTEXT_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|SupportingToken
name|endSuppTokens
init|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_ENDORSING
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|SignedEncryptedParts
name|signedParts
init|=
operator|new
name|SignedEncryptedParts
argument_list|(
literal|true
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|signedParts
operator|.
name|setBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|endSuppTokens
operator|.
name|setSignedParts
argument_list|(
name|signedParts
argument_list|)
expr_stmt|;
comment|//need to endorse everything
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstChildElement
argument_list|(
name|saaj
operator|.
name|getSOAPHeader
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|el
operator|!=
name|this
operator|.
name|secHeader
operator|.
name|getSecurityHeader
argument_list|()
condition|)
block|{
name|signedParts
operator|.
name|addHeader
argument_list|(
operator|new
name|Header
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|,
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextSiblingElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getFirstChildElement
argument_list|(
name|secHeader
operator|.
name|getSecurityHeader
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|timestamp
operator|!=
literal|null
operator|&&
name|el
operator|!=
name|timestamp
operator|.
name|getElement
argument_list|()
condition|)
block|{
name|signedParts
operator|.
name|addHeader
argument_list|(
operator|new
name|Header
argument_list|(
name|el
operator|.
name|getLocalName
argument_list|()
argument_list|,
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextSiblingElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
name|addSig
argument_list|(
name|signatureValues
argument_list|,
name|doIssuedTokenSignature
argument_list|(
operator|new
name|IssuedToken
argument_list|(
name|SP12Constants
operator|.
name|INSTANCE
argument_list|)
argument_list|,
name|endSuppTokens
operator|.
name|getSignedParts
argument_list|()
argument_list|,
name|endSuppTokens
argument_list|,
name|token
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
name|SupportingToken
name|suppTokens
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|suppTokens
operator|=
operator|(
name|SupportingToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|suppTokens
operator|!=
literal|null
operator|&&
name|suppTokens
operator|.
name|getTokens
argument_list|()
operator|!=
literal|null
operator|&&
name|suppTokens
operator|.
name|getTokens
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|handleSupportingTokens
argument_list|(
name|suppTokens
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|addSignatureConfirmation
argument_list|(
literal|null
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
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|byte
index|[]
name|doX509TokenSignature
parameter_list|(
name|Token
name|token
parameter_list|,
name|SignedEncryptedParts
name|signdParts
parameter_list|,
name|TokenWrapper
name|wrapper
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|saaj
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|Vector
argument_list|<
name|WSEncryptionPart
argument_list|>
name|sigParts
init|=
operator|new
name|Vector
argument_list|<
name|WSEncryptionPart
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|timestampEl
operator|!=
literal|null
condition|)
block|{
name|sigParts
operator|.
name|add
argument_list|(
operator|new
name|WSEncryptionPart
argument_list|(
name|timestampEl
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signdParts
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|signdParts
operator|.
name|isBody
argument_list|()
condition|)
block|{
name|sigParts
operator|.
name|add
argument_list|(
operator|new
name|WSEncryptionPart
argument_list|(
name|addWsuIdToElement
argument_list|(
name|saaj
operator|.
name|getSOAPBody
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Header
name|header
range|:
name|signdParts
operator|.
name|getHeaders
argument_list|()
control|)
block|{
name|WSEncryptionPart
name|wep
init|=
operator|new
name|WSEncryptionPart
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"Content"
argument_list|)
decl_stmt|;
name|sigParts
operator|.
name|add
argument_list|(
name|wep
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|token
operator|.
name|isDerivedKeys
argument_list|()
condition|)
block|{
name|WSSecEncryptedKey
name|encrKey
init|=
name|getEncryptedKeyBuilder
argument_list|(
name|wrapper
argument_list|,
name|token
argument_list|)
decl_stmt|;
name|Element
name|bstElem
init|=
name|encrKey
operator|.
name|getBinarySecurityTokenElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|bstElem
operator|!=
literal|null
condition|)
block|{
name|addTopDownElement
argument_list|(
name|bstElem
argument_list|)
expr_stmt|;
block|}
name|encrKey
operator|.
name|appendToHeader
argument_list|(
name|secHeader
argument_list|)
expr_stmt|;
name|WSSecDKSign
name|dkSig
init|=
operator|new
name|WSSecDKSign
argument_list|()
decl_stmt|;
name|dkSig
operator|.
name|setSigCanonicalization
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getInclusiveC14n
argument_list|()
argument_list|)
expr_stmt|;
name|dkSig
operator|.
name|setSignatureAlgorithm
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|dkSig
operator|.
name|setDerivedKeyLength
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSignatureDerivedKeyLength
argument_list|()
operator|/
literal|8
argument_list|)
expr_stmt|;
name|dkSig
operator|.
name|setExternalKey
argument_list|(
name|encrKey
operator|.
name|getEphemeralKey
argument_list|()
argument_list|,
name|encrKey
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|dkSig
operator|.
name|prepare
argument_list|(
name|doc
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
comment|/*             if(binding.isTokenProtection()) {                 sigParts.add(new WSEncryptionPart(encrKey.getBSTTokenId()));             }             */
name|dkSig
operator|.
name|setParts
argument_list|(
name|sigParts
argument_list|)
expr_stmt|;
name|dkSig
operator|.
name|addReferencesToSign
argument_list|(
name|sigParts
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
comment|//Do signature
name|dkSig
operator|.
name|computeSignature
argument_list|()
expr_stmt|;
name|dkSig
operator|.
name|appendDKElementToHeader
argument_list|(
name|secHeader
argument_list|)
expr_stmt|;
name|dkSig
operator|.
name|appendSigToHeader
argument_list|(
name|secHeader
argument_list|)
expr_stmt|;
return|return
name|dkSig
operator|.
name|getSignatureValue
argument_list|()
return|;
block|}
else|else
block|{
name|WSSecSignature
name|sig
init|=
name|getSignatureBuider
argument_list|(
name|wrapper
argument_list|,
name|token
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|sig
operator|!=
literal|null
condition|)
block|{
name|sig
operator|.
name|prependBSTElementToHeader
argument_list|(
name|secHeader
argument_list|)
expr_stmt|;
name|sig
operator|.
name|addReferencesToSign
argument_list|(
name|sigParts
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
name|insertBeforeBottomUp
argument_list|(
name|sig
operator|.
name|getSignatureElement
argument_list|()
argument_list|)
expr_stmt|;
name|sig
operator|.
name|computeSignature
argument_list|()
expr_stmt|;
return|return
name|sig
operator|.
name|getSignatureValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|private
name|byte
index|[]
name|doIssuedTokenSignature
parameter_list|(
name|Token
name|token
parameter_list|,
name|SignedEncryptedParts
name|signdParts
parameter_list|,
name|TokenWrapper
name|wrapper
parameter_list|,
name|SecurityToken
name|securityTok
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|saaj
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
comment|//Get the issued token
name|SecurityToken
name|secTok
init|=
name|securityTok
decl_stmt|;
if|if
condition|(
name|secTok
operator|==
literal|null
condition|)
block|{
name|secTok
operator|=
name|getSecurityToken
argument_list|()
expr_stmt|;
block|}
name|SPConstants
operator|.
name|IncludeTokenType
name|inclusion
init|=
name|token
operator|.
name|getInclusion
argument_list|()
decl_stmt|;
name|boolean
name|tokenIncluded
init|=
literal|false
decl_stmt|;
name|Vector
argument_list|<
name|WSEncryptionPart
argument_list|>
name|sigParts
init|=
operator|new
name|Vector
argument_list|<
name|WSEncryptionPart
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|inclusion
operator|==
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS
operator|||
operator|(
operator|(
name|inclusion
operator|==
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
operator|||
name|inclusion
operator|==
name|SPConstants
operator|.
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ONCE
operator|)
operator|&&
name|isRequestor
argument_list|()
operator|)
condition|)
block|{
comment|//Add the token
name|Element
name|el
init|=
name|cloneElement
argument_list|(
name|secTok
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityTok
operator|!=
literal|null
condition|)
block|{
comment|//do we need to sign this as well?
comment|//String id = addWsuIdToElement(el);
comment|//sigParts.add(new WSEncryptionPart(id));
block|}
name|addEncyptedKeyElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|tokenIncluded
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|timestampEl
operator|!=
literal|null
condition|)
block|{
name|sigParts
operator|.
name|add
argument_list|(
operator|new
name|WSEncryptionPart
argument_list|(
name|timestampEl
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signdParts
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|signdParts
operator|.
name|isBody
argument_list|()
condition|)
block|{
name|sigParts
operator|.
name|add
argument_list|(
operator|new
name|WSEncryptionPart
argument_list|(
name|addWsuIdToElement
argument_list|(
name|saaj
operator|.
name|getSOAPBody
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|secTok
operator|.
name|getX509Certificate
argument_list|()
operator|!=
literal|null
operator|||
name|securityTok
operator|!=
literal|null
condition|)
block|{
comment|//the "getX509Certificate" this is to workaround an issue in WCF
comment|//In WCF, for TransportBinding, in most cases, it doesn't wan't any of
comment|//the headers signed even if the policy sais so.   HOWEVER, for KeyValue
comment|//IssuedTokends, it DOES want them signed
for|for
control|(
name|Header
name|header
range|:
name|signdParts
operator|.
name|getHeaders
argument_list|()
control|)
block|{
name|WSEncryptionPart
name|wep
init|=
operator|new
name|WSEncryptionPart
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"Content"
argument_list|)
decl_stmt|;
name|sigParts
operator|.
name|add
argument_list|(
name|wep
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|//check for derived keys
name|AlgorithmSuite
name|algorithmSuite
init|=
name|tbinding
operator|.
name|getAlgorithmSuite
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|isDerivedKeys
argument_list|()
condition|)
block|{
comment|//Do Signature with derived keys
name|WSSecDKSign
name|dkSign
init|=
operator|new
name|WSSecDKSign
argument_list|()
decl_stmt|;
comment|//Setting the AttachedReference or the UnattachedReference according to the flag
name|Element
name|ref
decl_stmt|;
if|if
condition|(
name|tokenIncluded
condition|)
block|{
name|ref
operator|=
name|secTok
operator|.
name|getAttachedReference
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ref
operator|=
name|secTok
operator|.
name|getUnattachedReference
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|dkSign
operator|.
name|setExternalKey
argument_list|(
name|secTok
operator|.
name|getSecret
argument_list|()
argument_list|,
name|cloneElement
argument_list|(
name|ref
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dkSign
operator|.
name|setExternalKey
argument_list|(
name|secTok
operator|.
name|getSecret
argument_list|()
argument_list|,
name|secTok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//    Set the algo info
name|dkSign
operator|.
name|setSignatureAlgorithm
argument_list|(
name|algorithmSuite
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|dkSign
operator|.
name|setDerivedKeyLength
argument_list|(
name|algorithmSuite
operator|.
name|getSignatureDerivedKeyLength
argument_list|()
operator|/
literal|8
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|getSPConstants
argument_list|()
operator|==
name|SP12Constants
operator|.
name|INSTANCE
condition|)
block|{
name|dkSign
operator|.
name|setWscVersion
argument_list|(
name|ConversationConstants
operator|.
name|VERSION_05_12
argument_list|)
expr_stmt|;
block|}
name|dkSign
operator|.
name|prepare
argument_list|(
name|doc
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
name|addDerivedKeyElement
argument_list|(
name|dkSign
operator|.
name|getdktElement
argument_list|()
argument_list|)
expr_stmt|;
name|dkSign
operator|.
name|setParts
argument_list|(
name|sigParts
argument_list|)
expr_stmt|;
name|dkSign
operator|.
name|addReferencesToSign
argument_list|(
name|sigParts
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
comment|//Do signature
name|dkSign
operator|.
name|computeSignature
argument_list|()
expr_stmt|;
name|dkSign
operator|.
name|appendSigToHeader
argument_list|(
name|secHeader
argument_list|)
expr_stmt|;
return|return
name|dkSign
operator|.
name|getSignatureValue
argument_list|()
return|;
block|}
else|else
block|{
name|WSSecSignature
name|sig
init|=
operator|new
name|WSSecSignature
argument_list|()
decl_stmt|;
name|sig
operator|.
name|setCustomTokenId
argument_list|(
name|secTok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|secTok
operator|.
name|getTokenType
argument_list|()
operator|==
literal|null
condition|)
block|{
name|sig
operator|.
name|setCustomTokenValueType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_NS
operator|+
name|WSConstants
operator|.
name|SAML_ASSERTION_ID
argument_list|)
expr_stmt|;
name|sig
operator|.
name|setKeyIdentifierType
argument_list|(
name|WSConstants
operator|.
name|CUSTOM_KEY_IDENTIFIER
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sig
operator|.
name|setCustomTokenValueType
argument_list|(
name|secTok
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|sig
operator|.
name|setKeyIdentifierType
argument_list|(
name|WSConstants
operator|.
name|CUSTOM_SYMM_SIGNING
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|crypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|secTok
operator|.
name|getSecret
argument_list|()
operator|==
literal|null
condition|)
block|{
name|sig
operator|.
name|setX509Certificate
argument_list|(
name|secTok
operator|.
name|getX509Certificate
argument_list|()
argument_list|)
expr_stmt|;
name|crypto
operator|=
name|secTok
operator|.
name|getCrypto
argument_list|()
expr_stmt|;
name|String
name|uname
init|=
name|crypto
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getCertificateAlias
argument_list|(
name|secTok
operator|.
name|getX509Certificate
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|getPassword
argument_list|(
name|uname
argument_list|,
name|token
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|)
decl_stmt|;
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
literal|""
expr_stmt|;
block|}
name|sig
operator|.
name|setUserInfo
argument_list|(
name|uname
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|sig
operator|.
name|setSignatureAlgorithm
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAsymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|crypto
operator|=
name|getSignatureCrypto
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
name|sig
operator|.
name|setSecretKey
argument_list|(
name|secTok
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
name|sig
operator|.
name|setSignatureAlgorithm
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sig
operator|.
name|setSigCanonicalization
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getInclusiveC14n
argument_list|()
argument_list|)
expr_stmt|;
name|sig
operator|.
name|prepare
argument_list|(
name|doc
argument_list|,
name|crypto
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
name|sig
operator|.
name|setParts
argument_list|(
name|sigParts
argument_list|)
expr_stmt|;
name|sig
operator|.
name|addReferencesToSign
argument_list|(
name|sigParts
argument_list|,
name|secHeader
argument_list|)
expr_stmt|;
comment|//Do signature
name|sig
operator|.
name|computeSignature
argument_list|()
expr_stmt|;
comment|//Add elements to header
name|insertBeforeBottomUp
argument_list|(
name|sig
operator|.
name|getSignatureElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sig
operator|.
name|getSignatureValue
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

