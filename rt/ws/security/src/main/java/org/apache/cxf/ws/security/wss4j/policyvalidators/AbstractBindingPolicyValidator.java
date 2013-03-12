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
name|Layout
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
name|ProtectionToken
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
name|SignatureToken
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
name|SymmetricAsymmetricBindingBase
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
name|X509Token
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
name|WSDataRef
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
name|WSSecurityEngineResult
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
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|WSSecurityUtil
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
name|BindingPolicyValidator
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
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|results
argument_list|,
name|WSConstants
operator|.
name|TS
argument_list|,
name|timestampResults
argument_list|)
expr_stmt|;
comment|// Check whether we received a timestamp and compare it to the policy
if|if
condition|(
name|includeTimestamp
operator|&&
name|timestampResults
operator|.
name|size
argument_list|()
operator|!=
literal|1
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
if|if
condition|(
name|timestampResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
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
operator|==
literal|5
operator|&&
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
continue|continue;
block|}
elseif|else
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
literal|4
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Validate the layout assertion. It just checks the LaxTsFirst and LaxTsLast properties      */
specifier|protected
name|boolean
name|validateLayout
parameter_list|(
name|boolean
name|laxTimestampFirst
parameter_list|,
name|boolean
name|laxTimestampLast
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|)
block|{
if|if
condition|(
name|laxTimestampFirst
condition|)
block|{
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Integer
name|firstAction
init|=
operator|(
name|Integer
operator|)
name|results
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
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstAction
operator|.
name|intValue
argument_list|()
operator|!=
name|WSConstants
operator|.
name|TS
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
name|laxTimestampLast
condition|)
block|{
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Integer
name|lastAction
init|=
operator|(
name|Integer
operator|)
name|results
operator|.
name|get
argument_list|(
name|results
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
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
name|lastAction
operator|.
name|intValue
argument_list|()
operator|!=
name|WSConstants
operator|.
name|TS
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
comment|/**      * Check various properties set in the policy of the binding      */
specifier|protected
name|boolean
name|checkProperties
parameter_list|(
name|SymmetricAsymmetricBindingBase
name|binding
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
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
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
comment|// Check the AlgorithmSuite
name|AlgorithmSuitePolicyValidator
name|algorithmValidator
init|=
operator|new
name|AlgorithmSuitePolicyValidator
argument_list|(
name|results
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|algorithmValidator
operator|.
name|validatePolicy
argument_list|(
name|ai
argument_list|,
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|notAssertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|,
name|error
argument_list|)
expr_stmt|;
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
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
expr_stmt|;
comment|// Check the Layout
name|Layout
name|layout
init|=
name|binding
operator|.
name|getLayout
argument_list|()
decl_stmt|;
name|boolean
name|timestampFirst
init|=
name|layout
operator|.
name|getValue
argument_list|()
operator|==
name|SPConstants
operator|.
name|Layout
operator|.
name|LaxTimestampFirst
decl_stmt|;
name|boolean
name|timestampLast
init|=
name|layout
operator|.
name|getValue
argument_list|()
operator|==
name|SPConstants
operator|.
name|Layout
operator|.
name|LaxTimestampLast
decl_stmt|;
if|if
condition|(
operator|!
name|validateLayout
argument_list|(
name|timestampFirst
argument_list|,
name|timestampLast
argument_list|,
name|results
argument_list|)
condition|)
block|{
name|String
name|error
init|=
literal|"Layout does not match the requirements"
decl_stmt|;
name|notAssertPolicy
argument_list|(
name|aim
argument_list|,
name|layout
argument_list|,
name|error
argument_list|)
expr_stmt|;
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
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|layout
argument_list|)
expr_stmt|;
comment|// Check the EntireHeaderAndBodySignatures property
if|if
condition|(
name|binding
operator|.
name|isEntireHeadersAndBodySignatures
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
comment|// Check whether the signatures were encrypted or not
if|if
condition|(
name|binding
operator|.
name|isSignatureProtection
argument_list|()
operator|&&
operator|!
name|isSignatureEncrypted
argument_list|(
name|results
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
return|return
literal|true
return|;
block|}
comment|/**      * Check the Protection Order of the binding      */
specifier|protected
name|boolean
name|checkProtectionOrder
parameter_list|(
name|SymmetricAsymmetricBindingBase
name|binding
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
if|if
condition|(
name|binding
operator|.
name|getProtectionOrder
argument_list|()
operator|==
name|SPConstants
operator|.
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
name|isSignatureProtection
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
block|}
elseif|else
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
if|if
condition|(
name|signed
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
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
if|if
condition|(
name|encrypted
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
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
name|TokenWrapper
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
name|Token
name|token
init|=
name|tokenWrapper
operator|.
name|getToken
argument_list|()
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
name|token
operator|.
name|isDerivedKeys
argument_list|()
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
name|String
name|sigId
init|=
operator|(
name|String
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigId
operator|==
literal|null
operator|||
operator|!
name|isIdEncrypted
argument_list|(
name|sigId
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
name|String
name|sigId
init|=
operator|(
name|String
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigId
operator|==
literal|null
operator|||
operator|!
name|isIdEncrypted
argument_list|(
name|sigId
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
comment|/**      * Return true if the given id was encrypted      */
specifier|private
name|boolean
name|isIdEncrypted
parameter_list|(
name|String
name|sigId
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
name|protectedElement
operator|!=
literal|null
condition|)
block|{
name|String
name|id
init|=
name|protectedElement
operator|.
name|getAttribute
argument_list|(
literal|"Id"
argument_list|)
decl_stmt|;
name|String
name|wsuId
init|=
name|protectedElement
operator|.
name|getAttributeNS
argument_list|(
name|WSConstants
operator|.
name|WSU_NS
argument_list|,
literal|"Id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigId
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|||
name|sigId
operator|.
name|equals
argument_list|(
name|wsuId
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
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Assertion
name|token
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|token
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|getAssertion
argument_list|()
operator|==
name|token
condition|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|notAssertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|Assertion
name|token
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|token
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|getAssertion
argument_list|()
operator|==
name|token
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|boolean
name|assertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|q
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|notAssertPolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|QName
name|q
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
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
name|setNotAsserted
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

