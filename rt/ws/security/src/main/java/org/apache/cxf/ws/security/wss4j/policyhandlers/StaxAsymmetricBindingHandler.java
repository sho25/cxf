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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|SoapMessage
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|msg
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
name|configureLayout
argument_list|(
name|aim
argument_list|)
expr_stmt|;
name|abinding
operator|=
operator|(
name|AsymmetricBinding
operator|)
name|getBinding
argument_list|(
name|aim
argument_list|)
expr_stmt|;
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
block|}
else|else
block|{
name|doSignBeforeEncrypt
argument_list|()
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
name|boolean
name|attached
init|=
literal|false
decl_stmt|;
comment|/*             if (initiatorWrapper != null) {                 AbstractToken initiatorToken = initiatorWrapper.getToken();                 if (initiatorToken instanceof IssuedToken) {                     SecurityToken secToken = getSecurityToken();                     if (secToken == null) {                         policyNotAsserted(initiatorToken, "Security token is not found or expired");                         return;                     } else {                         policyAsserted(initiatorToken);                                                  if (includeToken(initiatorToken.getIncludeTokenType())) {                             Element el = secToken.getToken();                             this.addEncryptedKeyElement(cloneElement(el));                             attached = true;                         }                      }                 } else if (initiatorToken instanceof SamlToken) {                     SamlAssertionWrapper assertionWrapper = addSamlToken((SamlToken)initiatorToken);                     if (assertionWrapper != null) {                         if (includeToken(initiatorToken.getIncludeTokenType())) {                             addSupportingElement(assertionWrapper.toDOM(saaj.getSOAPPart()));                             storeAssertionAsSecurityToken(assertionWrapper);                         }                         policyAsserted(initiatorToken);                     }                 }             }             */
comment|// Add timestamp
name|List
argument_list|<
name|SecurePart
argument_list|>
name|sigs
init|=
operator|new
name|ArrayList
argument_list|<
name|SecurePart
argument_list|>
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
name|addSupportingTokens
argument_list|()
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
argument_list|,
name|attached
argument_list|)
expr_stmt|;
comment|//doEndorse();
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
name|doSignature
argument_list|(
name|recipientSignatureToken
argument_list|,
name|sigs
argument_list|,
name|attached
argument_list|)
expr_stmt|;
block|}
block|}
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
name|doEncryption
argument_list|(
name|encToken
argument_list|,
name|enc
argument_list|,
literal|false
argument_list|)
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
name|encryptionToken
operator|=
name|wrapper
operator|.
name|getToken
argument_list|()
expr_stmt|;
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
name|boolean
name|attached
init|=
literal|false
decl_stmt|;
comment|/*             if (initiatorWrapper != null) {                 AbstractToken initiatorToken = initiatorWrapper.getToken();                 if (initiatorToken instanceof IssuedToken) {                     SecurityToken secToken = getSecurityToken();                     if (secToken == null) {                         policyNotAsserted(initiatorToken, "Security token is not found or expired");                         return;                     } else {                         policyAsserted(initiatorToken);                                                  if (includeToken(initiatorToken.getIncludeTokenType())) {                             Element el = secToken.getToken();                             this.addEncryptedKeyElement(cloneElement(el));                             attached = true;                         }                      }                 } else if (initiatorToken instanceof SamlToken) {                     try {                         SamlAssertionWrapper assertionWrapper = addSamlToken((SamlToken)initiatorToken);                         if (assertionWrapper != null) {                             if (includeToken(initiatorToken.getIncludeTokenType())) {                                 addSupportingElement(assertionWrapper.toDOM(saaj.getSOAPPart()));                                 storeAssertionAsSecurityToken(assertionWrapper);                             }                             policyAsserted(initiatorToken);                         }                     } catch (Exception e) {                         String reason = e.getMessage();                         LOG.log(Level.FINE, "Encrypt before sign failed due to : " + reason);                         throw new Fault(e);                     }                 }             }             */
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
name|addSupportingTokens
argument_list|()
expr_stmt|;
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
operator|(
name|sigParts
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
operator|&&
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
argument_list|,
name|attached
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
name|doSignature
argument_list|(
name|recipientSignatureToken
argument_list|,
name|sigParts
argument_list|,
name|attached
argument_list|)
expr_stmt|;
block|}
block|}
comment|//if (isRequestor()) {
comment|//    doEndorse();
comment|//}
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|actionToPerform
init|=
name|ConfigurationConstants
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
name|ConfigurationConstants
operator|.
name|ENCRYPT_DERIVED
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|containsKey
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|)
condition|)
block|{
name|String
name|action
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|)
decl_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|action
operator|+
literal|" "
operator|+
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
name|String
name|parts
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|containsKey
argument_list|(
name|ConfigurationConstants
operator|.
name|ENCRYPTION_PARTS
argument_list|)
condition|)
block|{
name|parts
operator|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|ENCRYPTION_PARTS
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parts
operator|.
name|endsWith
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
name|parts
operator|+=
literal|";"
expr_stmt|;
block|}
block|}
name|encrParts
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|getEncryptedParts
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|SecurePart
name|part
range|:
name|encrParts
control|)
block|{
name|QName
name|name
init|=
name|part
operator|.
name|getName
argument_list|()
decl_stmt|;
name|parts
operator|+=
literal|"{"
operator|+
name|part
operator|.
name|getModifier
argument_list|()
operator|+
literal|"}{"
operator|+
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|+
literal|"}"
operator|+
name|name
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|";"
expr_stmt|;
block|}
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENCRYPTION_PARTS
argument_list|,
name|parts
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENC_KEY_ID
argument_list|,
name|getKeyIdentifierType
argument_list|(
name|recToken
argument_list|,
name|encrToken
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENC_KEY_TRANSPORT
argument_list|,
name|algorithmSuite
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getAsymmetricKeyWrap
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENC_SYM_ALGO
argument_list|,
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
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|encUser
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENCRYPTION_USER
argument_list|,
name|encUser
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
parameter_list|,
name|boolean
name|attached
parameter_list|)
throws|throws
name|WSSecurityException
throws|,
name|SOAPException
block|{
comment|// Action
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getProperties
argument_list|()
decl_stmt|;
name|String
name|actionToPerform
init|=
name|ConfigurationConstants
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
name|ConfigurationConstants
operator|.
name|SIGNATURE_DERIVED
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|containsKey
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|)
condition|)
block|{
name|String
name|action
init|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|action
operator|.
name|contains
argument_list|(
name|ConfigurationConstants
operator|.
name|SAML_TOKEN_SIGNED
argument_list|)
condition|)
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|action
operator|+
literal|" "
operator|+
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|actionToPerform
argument_list|)
expr_stmt|;
block|}
name|String
name|parts
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|containsKey
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_PARTS
argument_list|)
condition|)
block|{
name|parts
operator|=
operator|(
name|String
operator|)
name|config
operator|.
name|get
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_PARTS
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parts
operator|.
name|endsWith
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
name|parts
operator|+=
literal|";"
expr_stmt|;
block|}
block|}
name|sigParts
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|getSignedParts
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|SecurePart
name|part
range|:
name|sigParts
control|)
block|{
name|QName
name|name
init|=
name|part
operator|.
name|getName
argument_list|()
decl_stmt|;
name|parts
operator|+=
literal|"{Element}{"
operator|+
name|name
operator|.
name|getNamespaceURI
argument_list|()
operator|+
literal|"}"
operator|+
name|name
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|";"
expr_stmt|;
block|}
name|AbstractToken
name|sigToken
init|=
name|wrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
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
name|parts
operator|+=
literal|"{Element}{"
operator|+
name|WSSConstants
operator|.
name|NS_WSSE10
operator|+
literal|"}BinarySecurityToken;"
expr_stmt|;
block|}
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_PARTS
argument_list|,
name|parts
argument_list|)
expr_stmt|;
name|configureSignature
argument_list|(
name|wrapper
argument_list|,
name|sigToken
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_ALGO
argument_list|,
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

