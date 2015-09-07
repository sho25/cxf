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
name|ArrayList
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
name|namespace
operator|.
name|QName
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
name|SOAPException
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
name|rt
operator|.
name|security
operator|.
name|utils
operator|.
name|SecurityUtils
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
name|TokenStoreUtils
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
name|TokenStoreCallbackHandler
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
name|ConfigurationConstants
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
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
operator|.
name|IncludeTokenType
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
name|model
operator|.
name|AbstractSymmetricAsymmetricBinding
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
name|model
operator|.
name|AbstractToken
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
name|model
operator|.
name|AbstractToken
operator|.
name|DerivedKeys
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
name|model
operator|.
name|AbstractTokenWrapper
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
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AsymmetricBinding
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
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|SamlToken
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
name|wss4j
operator|.
name|policy
operator|.
name|model
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
name|policy
operator|.
name|model
operator|.
name|SpnegoContextToken
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
name|wss4j
operator|.
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
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
name|stax
operator|.
name|ext
operator|.
name|WSSSecurityProperties
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
name|stax
operator|.
name|securityToken
operator|.
name|WSSecurityTokenConstants
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
name|stax
operator|.
name|ext
operator|.
name|OutboundSecurityContext
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
name|stax
operator|.
name|ext
operator|.
name|SecurePart
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
name|stax
operator|.
name|ext
operator|.
name|SecurePart
operator|.
name|Modifier
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|StaxAsymmetricBindingHandler
extends|extends
name|AbstractStaxBindingHandler
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
name|StaxAsymmetricBindingHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AsymmetricBinding
name|abinding
decl_stmt|;
specifier|private
name|SoapMessage
name|message
decl_stmt|;
specifier|public
name|StaxAsymmetricBindingHandler
parameter_list|(
name|WSSSecurityProperties
name|properties
parameter_list|,
name|SoapMessage
name|msg
parameter_list|,
name|AsymmetricBinding
name|abinding
parameter_list|,
name|OutboundSecurityContext
name|outboundSecurityContext
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|,
name|msg
argument_list|,
name|abinding
argument_list|,
name|outboundSecurityContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|msg
expr_stmt|;
name|this
operator|.
name|abinding
operator|=
name|abinding
expr_stmt|;
block|}
specifier|public
name|void
name|handleBinding
parameter_list|()
block|{
name|AssertionInfoMap
name|aim
init|=
name|getMessage
argument_list|()
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|configureTimestamp
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|asymSignatureAlgorithm
init|=
operator|(
name|String
operator|)
name|getMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ASYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|asymSignatureAlgorithm
operator|!=
literal|null
operator|&&
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setAsymmetricSignature
argument_list|(
name|asymSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
name|String
name|symSignatureAlgorithm
init|=
operator|(
name|String
operator|)
name|getMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|symSignatureAlgorithm
operator|!=
literal|null
operator|&&
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setSymmetricSignature
argument_list|(
name|symSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|abinding
operator|.
name|getProtectionOrder
argument_list|()
operator|==
name|AbstractSymmetricAsymmetricBinding
operator|.
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
condition|)
block|{
name|doEncryptBeforeSign
argument_list|()
expr_stmt|;
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_BEFORE_SIGNING
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doSignBeforeEncrypt
argument_list|()
expr_stmt|;
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|SIGN_BEFORE_ENCRYPTING
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|configureLayout
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|assertAlgorithmSuite
argument_list|(
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
argument_list|)
expr_stmt|;
name|assertWSSProperties
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrustProperties
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|abinding
operator|.
name|isProtectTokens
argument_list|()
condition|)
block|{
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|PROTECT_TOKENS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|doSignBeforeEncrypt
parameter_list|()
block|{
try|try
block|{
name|AbstractTokenWrapper
name|initiatorWrapper
init|=
name|abinding
operator|.
name|getInitiatorSignatureToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|initiatorWrapper
operator|==
literal|null
condition|)
block|{
name|initiatorWrapper
operator|=
name|abinding
operator|.
name|getInitiatorToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|initiatorWrapper
operator|!=
literal|null
condition|)
block|{
name|assertTokenWrapper
argument_list|(
name|initiatorWrapper
argument_list|)
expr_stmt|;
name|AbstractToken
name|initiatorToken
init|=
name|initiatorWrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|initiatorToken
operator|instanceof
name|IssuedToken
condition|)
block|{
name|SecurityToken
name|sigTok
init|=
name|getSecurityToken
argument_list|()
decl_stmt|;
name|addIssuedToken
argument_list|(
operator|(
name|IssuedToken
operator|)
name|initiatorToken
argument_list|,
name|sigTok
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|sigTok
operator|!=
literal|null
condition|)
block|{
name|storeSecurityToken
argument_list|(
name|initiatorToken
argument_list|,
name|sigTok
argument_list|)
expr_stmt|;
name|outboundSecurityContext
operator|.
name|remove
argument_list|(
name|WSSConstants
operator|.
name|PROP_USE_THIS_TOKEN_ID_FOR_ENCRYPTION
argument_list|)
expr_stmt|;
block|}
comment|// Set up CallbackHandler which wraps the configured Handler
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|TokenStoreCallbackHandler
name|callbackHandler
init|=
operator|new
name|TokenStoreCallbackHandler
argument_list|(
name|properties
operator|.
name|getCallbackHandler
argument_list|()
argument_list|,
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|initiatorToken
operator|instanceof
name|SamlToken
condition|)
block|{
name|addSamlToken
argument_list|(
operator|(
name|SamlToken
operator|)
name|initiatorToken
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|assertToken
argument_list|(
name|initiatorToken
argument_list|)
expr_stmt|;
block|}
comment|// Add timestamp
name|List
argument_list|<
name|SecurePart
argument_list|>
name|sigs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|timestampAdded
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_WSU10
argument_list|,
literal|"Timestamp"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|sigs
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
name|sigs
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|getSignedParts
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRequestor
argument_list|()
operator|&&
name|initiatorWrapper
operator|!=
literal|null
condition|)
block|{
name|doSignature
argument_list|(
name|initiatorWrapper
argument_list|,
name|sigs
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isRequestor
argument_list|()
condition|)
block|{
comment|//confirm sig
name|addSignatureConfirmation
argument_list|(
name|sigs
argument_list|)
expr_stmt|;
name|AbstractTokenWrapper
name|recipientSignatureToken
init|=
name|abinding
operator|.
name|getRecipientSignatureToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|recipientSignatureToken
operator|==
literal|null
condition|)
block|{
name|recipientSignatureToken
operator|=
name|abinding
operator|.
name|getRecipientToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|recipientSignatureToken
operator|!=
literal|null
condition|)
block|{
name|assertTokenWrapper
argument_list|(
name|recipientSignatureToken
argument_list|)
expr_stmt|;
name|assertToken
argument_list|(
name|recipientSignatureToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recipientSignatureToken
operator|!=
literal|null
operator|&&
name|sigs
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|doSignature
argument_list|(
name|recipientSignatureToken
argument_list|,
name|sigs
argument_list|)
expr_stmt|;
block|}
block|}
name|addSupportingTokens
argument_list|()
expr_stmt|;
name|removeSignatureIfSignedSAML
argument_list|()
expr_stmt|;
name|prependSignatureToSC
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|SecurePart
argument_list|>
name|enc
init|=
name|getEncryptedParts
argument_list|()
decl_stmt|;
comment|//Check for signature protection
if|if
condition|(
name|abinding
operator|.
name|isEncryptSignature
argument_list|()
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_DSIG
argument_list|,
literal|"Signature"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|enc
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
name|signatureConfirmationAdded
condition|)
block|{
name|SecurePart
name|securePart
init|=
operator|new
name|SecurePart
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsse11_SignatureConfirmation
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|enc
operator|.
name|add
argument_list|(
name|securePart
argument_list|)
expr_stmt|;
block|}
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_SIGNATURE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//Do encryption
name|AbstractTokenWrapper
name|encToken
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|()
condition|)
block|{
name|enc
operator|.
name|addAll
argument_list|(
name|encryptedTokensList
argument_list|)
expr_stmt|;
name|encToken
operator|=
name|abinding
operator|.
name|getRecipientEncryptionToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|encToken
operator|==
literal|null
condition|)
block|{
name|encToken
operator|=
name|abinding
operator|.
name|getRecipientToken
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|encToken
operator|=
name|abinding
operator|.
name|getInitiatorEncryptionToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|encToken
operator|==
literal|null
condition|)
block|{
name|encToken
operator|=
name|abinding
operator|.
name|getInitiatorToken
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|encToken
operator|!=
literal|null
condition|)
block|{
name|assertTokenWrapper
argument_list|(
name|encToken
argument_list|)
expr_stmt|;
name|assertToken
argument_list|(
name|encToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|doEncryption
argument_list|(
name|encToken
argument_list|,
name|enc
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|putCustomTokenAfterSignature
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|reason
init|=
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Sign before encryption failed due to : "
operator|+
name|reason
argument_list|)
expr_stmt|;
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
name|void
name|doEncryptBeforeSign
parameter_list|()
block|{
try|try
block|{
name|AbstractTokenWrapper
name|wrapper
decl_stmt|;
name|AbstractToken
name|encryptionToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isRequestor
argument_list|()
condition|)
block|{
name|wrapper
operator|=
name|abinding
operator|.
name|getRecipientEncryptionToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|wrapper
operator|==
literal|null
condition|)
block|{
name|wrapper
operator|=
name|abinding
operator|.
name|getRecipientToken
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|wrapper
operator|=
name|abinding
operator|.
name|getInitiatorEncryptionToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|wrapper
operator|==
literal|null
condition|)
block|{
name|wrapper
operator|=
name|abinding
operator|.
name|getInitiatorToken
argument_list|()
expr_stmt|;
block|}
block|}
name|assertTokenWrapper
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
if|if
condition|(
name|wrapper
operator|!=
literal|null
condition|)
block|{
name|encryptionToken
operator|=
name|wrapper
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|assertToken
argument_list|(
name|encryptionToken
argument_list|)
expr_stmt|;
block|}
name|AbstractTokenWrapper
name|initiatorWrapper
init|=
name|abinding
operator|.
name|getInitiatorSignatureToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|initiatorWrapper
operator|==
literal|null
condition|)
block|{
name|initiatorWrapper
operator|=
name|abinding
operator|.
name|getInitiatorToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|initiatorWrapper
operator|!=
literal|null
condition|)
block|{
name|assertTokenWrapper
argument_list|(
name|initiatorWrapper
argument_list|)
expr_stmt|;
name|AbstractToken
name|initiatorToken
init|=
name|initiatorWrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|initiatorToken
operator|instanceof
name|IssuedToken
condition|)
block|{
name|SecurityToken
name|sigTok
init|=
name|getSecurityToken
argument_list|()
decl_stmt|;
name|addIssuedToken
argument_list|(
operator|(
name|IssuedToken
operator|)
name|initiatorToken
argument_list|,
name|sigTok
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|sigTok
operator|!=
literal|null
condition|)
block|{
name|storeSecurityToken
argument_list|(
name|initiatorToken
argument_list|,
name|sigTok
argument_list|)
expr_stmt|;
name|outboundSecurityContext
operator|.
name|remove
argument_list|(
name|WSSConstants
operator|.
name|PROP_USE_THIS_TOKEN_ID_FOR_ENCRYPTION
argument_list|)
expr_stmt|;
block|}
comment|// Set up CallbackHandler which wraps the configured Handler
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|TokenStoreCallbackHandler
name|callbackHandler
init|=
operator|new
name|TokenStoreCallbackHandler
argument_list|(
name|properties
operator|.
name|getCallbackHandler
argument_list|()
argument_list|,
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|properties
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|initiatorToken
operator|instanceof
name|SamlToken
condition|)
block|{
name|addSamlToken
argument_list|(
operator|(
name|SamlToken
operator|)
name|initiatorToken
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|SecurePart
argument_list|>
name|encrParts
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|SecurePart
argument_list|>
name|sigParts
init|=
literal|null
decl_stmt|;
try|try
block|{
name|encrParts
operator|=
name|getEncryptedParts
argument_list|()
expr_stmt|;
comment|//Signed parts are determined before encryption because encrypted signed headers
comment|//will not be included otherwise
name|sigParts
operator|=
name|getSignedParts
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
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
name|addSupportingTokens
argument_list|()
expr_stmt|;
if|if
condition|(
name|encryptionToken
operator|!=
literal|null
operator|&&
name|encrParts
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|isRequestor
argument_list|()
condition|)
block|{
name|encrParts
operator|.
name|addAll
argument_list|(
name|encryptedTokensList
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addSignatureConfirmation
argument_list|(
name|sigParts
argument_list|)
expr_stmt|;
block|}
comment|//Check for signature protection
if|if
condition|(
name|abinding
operator|.
name|isEncryptSignature
argument_list|()
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_DSIG
argument_list|,
literal|"Signature"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|encrParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
name|signatureConfirmationAdded
condition|)
block|{
name|SecurePart
name|securePart
init|=
operator|new
name|SecurePart
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsse11_SignatureConfirmation
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|encrParts
operator|.
name|add
argument_list|(
name|securePart
argument_list|)
expr_stmt|;
block|}
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|abinding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_SIGNATURE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|doEncryption
argument_list|(
name|wrapper
argument_list|,
name|encrParts
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timestampAdded
condition|)
block|{
name|SecurePart
name|part
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_WSU10
argument_list|,
literal|"Timestamp"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|sigParts
operator|.
name|add
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sigParts
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|initiatorWrapper
operator|!=
literal|null
operator|&&
name|isRequestor
argument_list|()
condition|)
block|{
name|doSignature
argument_list|(
name|initiatorWrapper
argument_list|,
name|sigParts
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isRequestor
argument_list|()
condition|)
block|{
name|AbstractTokenWrapper
name|recipientSignatureToken
init|=
name|abinding
operator|.
name|getRecipientSignatureToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|recipientSignatureToken
operator|==
literal|null
condition|)
block|{
name|recipientSignatureToken
operator|=
name|abinding
operator|.
name|getRecipientToken
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|recipientSignatureToken
operator|!=
literal|null
condition|)
block|{
name|assertTokenWrapper
argument_list|(
name|recipientSignatureToken
argument_list|)
expr_stmt|;
name|assertToken
argument_list|(
name|recipientSignatureToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|doSignature
argument_list|(
name|recipientSignatureToken
argument_list|,
name|sigParts
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|removeSignatureIfSignedSAML
argument_list|()
expr_stmt|;
name|enforceEncryptBeforeSigningWithSignedSAML
argument_list|()
expr_stmt|;
name|prependSignatureToSC
argument_list|()
expr_stmt|;
name|putCustomTokenAfterSignature
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|reason
init|=
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Encrypt before signing failed due to : "
operator|+
name|reason
argument_list|)
expr_stmt|;
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
name|void
name|doEncryption
parameter_list|(
name|AbstractTokenWrapper
name|recToken
parameter_list|,
name|List
argument_list|<
name|SecurePart
argument_list|>
name|encrParts
parameter_list|,
name|boolean
name|externalRef
parameter_list|)
throws|throws
name|SOAPException
block|{
comment|//Do encryption
if|if
condition|(
name|recToken
operator|!=
literal|null
operator|&&
name|recToken
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|encrParts
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|AbstractToken
name|encrToken
init|=
name|recToken
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|AlgorithmSuite
name|algorithmSuite
init|=
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
decl_stmt|;
comment|// Action
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|WSSConstants
operator|.
name|Action
name|actionToPerform
init|=
name|WSSConstants
operator|.
name|ENCRYPT
decl_stmt|;
if|if
condition|(
name|recToken
operator|.
name|getToken
argument_list|()
operator|.
name|getDerivedKeys
argument_list|()
operator|==
name|DerivedKeys
operator|.
name|RequireDerivedKeys
condition|)
block|{
name|actionToPerform
operator|=
name|WSSConstants
operator|.
name|ENCRYPT_WITH_DERIVED_KEY
expr_stmt|;
block|}
name|properties
operator|.
name|addAction
argument_list|(
name|actionToPerform
argument_list|)
expr_stmt|;
name|properties
operator|.
name|getEncryptionSecureParts
argument_list|()
operator|.
name|addAll
argument_list|(
name|encrParts
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setEncryptionKeyIdentifier
argument_list|(
name|getKeyIdentifierType
argument_list|(
name|encrToken
argument_list|)
argument_list|)
expr_stmt|;
comment|// Find out do we also need to include the token as per the Inclusion requirement
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier
name|keyIdentifier
init|=
name|properties
operator|.
name|getEncryptionKeyIdentifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|encrToken
operator|instanceof
name|X509Token
operator|&&
name|isTokenRequired
argument_list|(
name|encrToken
operator|.
name|getIncludeTokenType
argument_list|()
argument_list|)
operator|&&
operator|(
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_IssuerSerial
operator|.
name|equals
argument_list|(
name|keyIdentifier
argument_list|)
operator|||
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_ThumbprintIdentifier
operator|.
name|equals
argument_list|(
name|keyIdentifier
argument_list|)
operator|||
name|WSSecurityTokenConstants
operator|.
name|KeyIdentifier_SecurityTokenDirectReference
operator|.
name|equals
argument_list|(
name|keyIdentifier
argument_list|)
operator|)
condition|)
block|{
name|properties
operator|.
name|setIncludeEncryptionToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|.
name|setIncludeEncryptionToken
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|setEncryptionKeyTransportAlgorithm
argument_list|(
name|algorithmSuite
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getAsymmetricKeyWrap
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setEncryptionSymAlgorithm
argument_list|(
name|algorithmSuite
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getEncryption
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|encUser
init|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|encUser
operator|==
literal|null
condition|)
block|{
name|encUser
operator|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encUser
operator|!=
literal|null
operator|&&
name|properties
operator|.
name|getEncryptionUser
argument_list|()
operator|==
literal|null
condition|)
block|{
name|properties
operator|.
name|setEncryptionUser
argument_list|(
name|encUser
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ConfigurationConstants
operator|.
name|USE_REQ_SIG_CERT
operator|.
name|equals
argument_list|(
name|encUser
argument_list|)
condition|)
block|{
name|properties
operator|.
name|setUseReqSigCertForEncryption
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|//
comment|// Using a stored cert is only suitable for the Issued Token case, where
comment|// we're extracting the cert from a SAML Assertion on the provider side
comment|//
if|if
condition|(
operator|!
name|isRequestor
argument_list|()
operator|&&
name|recToken
operator|.
name|getToken
argument_list|()
operator|instanceof
name|IssuedToken
condition|)
block|{
name|properties
operator|.
name|setUseReqSigCertForEncryption
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|doSignature
parameter_list|(
name|AbstractTokenWrapper
name|wrapper
parameter_list|,
name|List
argument_list|<
name|SecurePart
argument_list|>
name|sigParts
parameter_list|)
throws|throws
name|WSSecurityException
throws|,
name|SOAPException
block|{
comment|// Action
name|WSSSecurityProperties
name|properties
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|WSSConstants
operator|.
name|Action
name|actionToPerform
init|=
name|WSSConstants
operator|.
name|SIGNATURE
decl_stmt|;
if|if
condition|(
name|wrapper
operator|.
name|getToken
argument_list|()
operator|.
name|getDerivedKeys
argument_list|()
operator|==
name|DerivedKeys
operator|.
name|RequireDerivedKeys
condition|)
block|{
name|actionToPerform
operator|=
name|WSSConstants
operator|.
name|SIGNATURE_WITH_DERIVED_KEY
expr_stmt|;
block|}
name|List
argument_list|<
name|WSSConstants
operator|.
name|Action
argument_list|>
name|actionList
init|=
name|properties
operator|.
name|getActions
argument_list|()
decl_stmt|;
comment|// Add a Signature directly before Kerberos, otherwise just append it
name|boolean
name|actionAdded
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|actionList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|WSSConstants
operator|.
name|Action
name|action
init|=
name|actionList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|.
name|equals
argument_list|(
name|WSSConstants
operator|.
name|KERBEROS_TOKEN
argument_list|)
condition|)
block|{
name|actionList
operator|.
name|add
argument_list|(
name|i
argument_list|,
name|actionToPerform
argument_list|)
expr_stmt|;
name|actionAdded
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|actionAdded
condition|)
block|{
name|actionList
operator|.
name|add
argument_list|(
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|getSignatureSecureParts
argument_list|()
operator|.
name|addAll
argument_list|(
name|sigParts
argument_list|)
expr_stmt|;
name|AbstractToken
name|sigToken
init|=
name|wrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|configureSignature
argument_list|(
name|sigToken
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|abinding
operator|.
name|isProtectTokens
argument_list|()
operator|&&
operator|(
name|sigToken
operator|instanceof
name|X509Token
operator|)
operator|&&
name|sigToken
operator|.
name|getIncludeTokenType
argument_list|()
operator|!=
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_NEVER
condition|)
block|{
name|SecurePart
name|securePart
init|=
operator|new
name|SecurePart
argument_list|(
operator|new
name|QName
argument_list|(
name|WSSConstants
operator|.
name|NS_WSSE10
argument_list|,
literal|"BinarySecurityToken"
argument_list|)
argument_list|,
name|Modifier
operator|.
name|Element
argument_list|)
decl_stmt|;
name|properties
operator|.
name|addSignaturePart
argument_list|(
name|securePart
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sigToken
operator|instanceof
name|IssuedToken
operator|||
name|sigToken
operator|instanceof
name|SecurityContextToken
operator|||
name|sigToken
operator|instanceof
name|SecureConversationToken
operator|||
name|sigToken
operator|instanceof
name|SpnegoContextToken
operator|||
name|sigToken
operator|instanceof
name|SamlToken
condition|)
block|{
name|properties
operator|.
name|setIncludeSignatureToken
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sigToken
operator|.
name|getDerivedKeys
argument_list|()
operator|==
name|DerivedKeys
operator|.
name|RequireDerivedKeys
condition|)
block|{
name|properties
operator|.
name|setSignatureAlgorithm
argument_list|(
name|abinding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

