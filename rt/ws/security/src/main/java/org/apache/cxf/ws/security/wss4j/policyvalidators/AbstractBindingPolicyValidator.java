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
name|policyvalidators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|CastUtils
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SAMLKeyInfo
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
name|saml
operator|.
name|SamlAssertionWrapper
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
name|common
operator|.
name|token
operator|.
name|PKIPathSecurity
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
name|token
operator|.
name|X509Security
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
name|WSDataRef
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
name|engine
operator|.
name|WSSecurityEngineResult
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
name|handler
operator|.
name|WSHandlerResult
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
name|Timestamp
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
name|AbstractSymmetricAsymmetricBinding
operator|.
name|ProtectionOrder
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
name|EncryptionToken
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
name|ProtectionToken
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
name|SignatureToken
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

begin_comment
comment|/**  * Some abstract functionality for validating a security binding.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractBindingPolicyValidator
implements|implements
name|SecurityPolicyValidator
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SIG_QNAME
init|=
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_LN
argument_list|)
decl_stmt|;
comment|/**      * Validate a Timestamp      * @param includeTimestamp whether a Timestamp must be included or not      * @param transportBinding whether the Transport binding is in use or not      * @param signedResults the signed results list      * @param message the Message object      * @return whether the Timestamp policy is valid or not      */
specifier|protected
name|boolean
name|validateTimestamp
parameter_list|(
name|boolean
name|includeTimestamp
parameter_list|,
name|boolean
name|transportBinding
parameter_list|,
name|WSHandlerResult
name|results
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|timestampResults
init|=
name|results
operator|.
name|getActionResults
argument_list|()
operator|.
name|get
argument_list|(
name|WSConstants
operator|.
name|TS
argument_list|)
decl_stmt|;
comment|// Check whether we received a timestamp and compare it to the policy
if|if
condition|(
name|includeTimestamp
operator|&&
operator|(
name|timestampResults
operator|==
literal|null
operator|||
name|timestampResults
operator|.
name|size
argument_list|()
operator|!=
literal|1
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|includeTimestamp
condition|)
block|{
return|return
name|timestampResults
operator|==
literal|null
operator|||
name|timestampResults
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|// At this point we received a (required) Timestamp. Now check that it is integrity protected.
if|if
condition|(
name|transportBinding
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|signedResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Timestamp
name|timestamp
init|=
operator|(
name|Timestamp
operator|)
name|timestampResults
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TIMESTAMP
argument_list|)
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signedResults
control|)
block|{
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|dataRefs
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|WSDataRef
name|dataRef
range|:
name|dataRefs
control|)
block|{
if|if
condition|(
name|timestamp
operator|.
name|getElement
argument_list|()
operator|==
name|dataRef
operator|.
name|getProtectedElement
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Validate the entire header and body signature property.      */
specifier|protected
name|boolean
name|validateEntireHeaderAndBodySignatures
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signedResults
control|)
block|{
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|dataRefs
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataRefs
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSDataRef
name|dataRef
range|:
name|dataRefs
control|)
block|{
name|String
name|xpath
init|=
name|dataRef
operator|.
name|getXpath
argument_list|()
decl_stmt|;
if|if
condition|(
name|xpath
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|nodes
init|=
name|StringUtils
operator|.
name|split
argument_list|(
name|xpath
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
comment|// envelope/Body || envelope/Header/header || envelope/Header/wsse:Security/header
if|if
condition|(
name|nodes
operator|.
name|length
argument_list|<
literal|3
operator|||
name|nodes
operator|.
name|length
argument_list|>
literal|5
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|nodes
index|[
literal|2
index|]
operator|.
name|contains
argument_list|(
literal|"Header"
argument_list|)
operator|||
name|nodes
index|[
literal|2
index|]
operator|.
name|contains
argument_list|(
literal|"Body"
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|nodes
operator|.
name|length
operator|==
literal|5
operator|&&
operator|!
name|nodes
index|[
literal|3
index|]
operator|.
name|contains
argument_list|(
literal|"Security"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|nodes
operator|.
name|length
operator|==
literal|4
operator|&&
name|nodes
index|[
literal|2
index|]
operator|.
name|contains
argument_list|(
literal|"Body"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check various properties set in the policy of the binding      */
specifier|protected
name|boolean
name|checkProperties
parameter_list|(
name|AbstractSymmetricAsymmetricBinding
name|binding
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|WSHandlerResult
name|results
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
comment|// Check the IncludeTimestamp
if|if
condition|(
operator|!
name|validateTimestamp
argument_list|(
name|binding
operator|.
name|isIncludeTimestamp
argument_list|()
argument_list|,
literal|false
argument_list|,
name|results
argument_list|,
name|signedResults
argument_list|,
name|message
argument_list|)
condition|)
block|{
name|String
name|error
init|=
literal|"Received Timestamp does not match the requirements"
decl_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|namespace
init|=
name|binding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check the EntireHeaderAndBodySignatures property
if|if
condition|(
name|binding
operator|.
name|isOnlySignEntireHeadersAndBody
argument_list|()
operator|&&
operator|!
name|validateEntireHeaderAndBodySignatures
argument_list|(
name|signedResults
argument_list|)
condition|)
block|{
name|String
name|error
init|=
literal|"OnlySignEntireHeadersAndBody does not match the requirements"
decl_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check whether the signatures were encrypted or not
if|if
condition|(
name|binding
operator|.
name|isEncryptSignature
argument_list|()
operator|&&
operator|!
name|isSignatureEncrypted
argument_list|(
name|results
operator|.
name|getResults
argument_list|()
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The signature is not protected"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_SIGNATURE
argument_list|)
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|PROTECT_TOKENS
argument_list|)
argument_list|)
expr_stmt|;
comment|/*         // Check ProtectTokens         if (binding.isTokenProtection()&& !isTokenProtected(results, signedResults)) {             ai.setNotAsserted("The token protection property is not valid");             return false;         }         */
return|return
literal|true
return|;
block|}
comment|/**      * Check the Protection Order of the binding      */
specifier|protected
name|boolean
name|checkProtectionOrder
parameter_list|(
name|AbstractSymmetricAsymmetricBinding
name|binding
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
name|ProtectionOrder
name|protectionOrder
init|=
name|binding
operator|.
name|getProtectionOrder
argument_list|()
decl_stmt|;
name|String
name|namespace
init|=
name|binding
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|protectionOrder
operator|==
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
condition|)
block|{
if|if
condition|(
operator|!
name|binding
operator|.
name|isProtectTokens
argument_list|()
operator|&&
name|isSignedBeforeEncrypted
argument_list|(
name|results
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Not encrypted before signed"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_BEFORE_SIGNING
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protectionOrder
operator|==
name|ProtectionOrder
operator|.
name|SignBeforeEncrypting
condition|)
block|{
if|if
condition|(
name|isEncryptedBeforeSigned
argument_list|(
name|results
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Not signed before encrypted"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|SPConstants
operator|.
name|SIGN_BEFORE_ENCRYPTING
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check to see if a signature was applied before encryption.      * Note that results are stored in the reverse order.      */
specifier|private
name|boolean
name|isSignedBeforeEncrypted
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
name|boolean
name|signed
init|=
literal|false
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|results
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|el
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
comment|// Don't count an endorsing signature
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|SIGN
operator|&&
name|el
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|el
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|el
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|SIG_QNAME
argument_list|)
operator|)
condition|)
block|{
name|signed
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ENCR
operator|&&
name|el
operator|!=
literal|null
condition|)
block|{
return|return
name|signed
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Check to see if encryption was applied before signature.      * Note that results are stored in the reverse order.      */
specifier|private
name|boolean
name|isEncryptedBeforeSigned
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
name|boolean
name|encrypted
init|=
literal|false
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|results
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|el
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ENCR
operator|&&
name|el
operator|!=
literal|null
condition|)
block|{
name|encrypted
operator|=
literal|true
expr_stmt|;
block|}
comment|// Don't count an endorsing signature
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|SIGN
operator|&&
name|el
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|el
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|el
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|SIG_QNAME
argument_list|)
operator|)
condition|)
block|{
return|return
name|encrypted
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Check the derived key requirement.      */
specifier|protected
name|boolean
name|checkDerivedKeys
parameter_list|(
name|AbstractTokenWrapper
name|tokenWrapper
parameter_list|,
name|boolean
name|hasDerivedKeys
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
parameter_list|)
block|{
name|AbstractToken
name|token
init|=
name|tokenWrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|boolean
name|isDerivedKeys
init|=
name|token
operator|.
name|getDerivedKeys
argument_list|()
operator|==
name|DerivedKeys
operator|.
name|RequireDerivedKeys
decl_stmt|;
comment|// If derived keys are not required then just return
if|if
condition|(
operator|!
operator|(
name|token
operator|instanceof
name|X509Token
operator|&&
name|isDerivedKeys
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|tokenWrapper
operator|instanceof
name|EncryptionToken
operator|&&
operator|!
name|hasDerivedKeys
operator|&&
operator|!
name|encryptedResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|tokenWrapper
operator|instanceof
name|SignatureToken
operator|&&
operator|!
name|hasDerivedKeys
operator|&&
operator|!
name|signedResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|tokenWrapper
operator|instanceof
name|ProtectionToken
operator|&&
operator|!
name|hasDerivedKeys
operator|&&
operator|!
operator|(
name|signedResults
operator|.
name|isEmpty
argument_list|()
operator|||
name|encryptedResults
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check whether the token protection policy is followed. In other words, check that the      * signature token was itself signed.      */
specifier|protected
name|boolean
name|isTokenProtected
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|signedResults
control|)
block|{
comment|// Get the Token result that was used for the signature
name|WSSecurityEngineResult
name|tokenResult
init|=
name|findCorrespondingToken
argument_list|(
name|result
argument_list|,
name|results
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenResult
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Now go through what was signed and see if the token itself was signed
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|sl
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|sl
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSDataRef
name|dataRef
range|:
name|sl
control|)
block|{
name|Element
name|referenceElement
init|=
name|dataRef
operator|.
name|getProtectedElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|referenceElement
operator|!=
literal|null
operator|&&
name|referenceElement
operator|.
name|equals
argument_list|(
name|tokenResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TOKEN_ELEMENT
argument_list|)
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Find the token corresponding to either the X509Certificate or PublicKey used to sign      * the "signatureResult" argument.      */
specifier|private
name|WSSecurityEngineResult
name|findCorrespondingToken
parameter_list|(
name|WSSecurityEngineResult
name|signatureResult
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
comment|// See what was used to sign this result
name|X509Certificate
name|cert
init|=
operator|(
name|X509Certificate
operator|)
name|signatureResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATE
argument_list|)
decl_stmt|;
name|PublicKey
name|publicKey
init|=
operator|(
name|PublicKey
operator|)
name|signatureResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PUBLIC_KEY
argument_list|)
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|token
range|:
name|results
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|token
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|actInt
operator|==
name|WSConstants
operator|.
name|SIGN
condition|)
block|{
continue|continue;
block|}
name|BinarySecurity
name|binarySecurity
init|=
operator|(
name|BinarySecurity
operator|)
name|token
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_BINARY_SECURITY_TOKEN
argument_list|)
decl_stmt|;
name|PublicKey
name|foundPublicKey
init|=
operator|(
name|PublicKey
operator|)
name|token
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PUBLIC_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|binarySecurity
operator|instanceof
name|X509Security
operator|||
name|binarySecurity
operator|instanceof
name|PKIPathSecurity
condition|)
block|{
name|X509Certificate
name|foundCert
init|=
operator|(
name|X509Certificate
operator|)
name|token
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|foundCert
operator|.
name|equals
argument_list|(
name|cert
argument_list|)
condition|)
block|{
return|return
name|token
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ST_SIGNED
operator|||
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ST_UNSIGNED
condition|)
block|{
name|SamlAssertionWrapper
name|assertionWrapper
init|=
operator|(
name|SamlAssertionWrapper
operator|)
name|token
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
name|SAMLKeyInfo
name|samlKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|samlKeyInfo
operator|!=
literal|null
condition|)
block|{
name|X509Certificate
index|[]
name|subjectCerts
init|=
name|samlKeyInfo
operator|.
name|getCerts
argument_list|()
decl_stmt|;
name|PublicKey
name|subjectPublicKey
init|=
name|samlKeyInfo
operator|.
name|getPublicKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|cert
operator|!=
literal|null
operator|&&
name|subjectCerts
operator|!=
literal|null
operator|&&
name|cert
operator|.
name|equals
argument_list|(
name|subjectCerts
index|[
literal|0
index|]
argument_list|)
operator|)
operator|||
operator|(
name|subjectPublicKey
operator|!=
literal|null
operator|&&
name|subjectPublicKey
operator|.
name|equals
argument_list|(
name|publicKey
argument_list|)
operator|)
condition|)
block|{
return|return
name|token
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|publicKey
operator|!=
literal|null
operator|&&
name|publicKey
operator|.
name|equals
argument_list|(
name|foundPublicKey
argument_list|)
condition|)
block|{
return|return
name|token
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Check whether the primary Signature (and all SignatureConfirmation) elements were encrypted      */
specifier|protected
name|boolean
name|isSignatureEncrypted
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
name|boolean
name|foundPrimarySignature
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|results
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|WSSecurityEngineResult
name|result
init|=
name|results
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|SIGN
operator|&&
operator|!
name|foundPrimarySignature
condition|)
block|{
name|foundPrimarySignature
operator|=
literal|true
expr_stmt|;
name|Element
name|sigElement
init|=
operator|(
name|Element
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TOKEN_ELEMENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigElement
operator|==
literal|null
operator|||
operator|!
name|isElementEncrypted
argument_list|(
name|sigElement
argument_list|,
name|results
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|SC
condition|)
block|{
name|Element
name|sigElement
init|=
operator|(
name|Element
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TOKEN_ELEMENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigElement
operator|==
literal|null
operator|||
operator|!
name|isElementEncrypted
argument_list|(
name|sigElement
argument_list|,
name|results
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Return true if the given Element was encrypted      */
specifier|private
name|boolean
name|isElementEncrypted
parameter_list|(
name|Element
name|element
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|wser
range|:
name|results
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ENCR
condition|)
block|{
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|el
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_DATA_REF_URIS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSDataRef
name|r
range|:
name|el
control|)
block|{
name|Element
name|protectedElement
init|=
name|r
operator|.
name|getProtectedElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|equals
argument_list|(
name|protectedElement
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|assertDerivedKeys
parameter_list|(
name|AbstractToken
name|token
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|DerivedKeys
name|derivedKeys
init|=
name|token
operator|.
name|getDerivedKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|derivedKeys
operator|!=
literal|null
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
operator|new
name|QName
argument_list|(
name|token
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|derivedKeys
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

