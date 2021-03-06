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
name|Principal
import|;
end_import

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
name|security
operator|.
name|interfaces
operator|.
name|DSAPublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|interfaces
operator|.
name|RSAPublicKey
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
name|WSS4JConstants
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
name|principal
operator|.
name|WSDerivedKeyTokenPrincipal
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
name|transform
operator|.
name|STRTransform
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
name|SP11Constants
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
name|SP12Constants
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
name|AlgorithmSuite
operator|.
name|AlgorithmSuiteType
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
name|transforms
operator|.
name|Transforms
import|;
end_import

begin_comment
comment|/**  * Validate results corresponding to the processing of a Signature, EncryptedKey or  * EncryptedData structure against an AlgorithmSuite policy.  */
end_comment

begin_class
specifier|public
class|class
name|AlgorithmSuitePolicyValidator
extends|extends
name|AbstractSecurityPolicyValidator
block|{
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a      * policy defined by the AssertionInfo parameter      */
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
block|{
return|return
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|SP12Constants
operator|.
name|ALGORITHM_SUITE
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|SP11Constants
operator|.
name|ALGORITHM_SUITE
operator|.
name|equals
argument_list|(
name|assertionInfo
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
return|;
block|}
comment|/**      * Validate policies.      */
specifier|public
name|void
name|validatePolicies
parameter_list|(
name|PolicyValidatorParameters
name|parameters
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|AlgorithmSuite
name|algorithmSuite
init|=
operator|(
name|AlgorithmSuite
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|boolean
name|valid
init|=
name|validatePolicy
argument_list|(
name|ai
argument_list|,
name|algorithmSuite
argument_list|,
name|parameters
operator|.
name|getResults
argument_list|()
operator|.
name|getResults
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|String
name|namespace
init|=
name|algorithmSuite
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getNamespace
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|algorithmSuite
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|algSuiteAis
init|=
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|algSuiteAis
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|algSuiteAi
range|:
name|algSuiteAis
control|)
block|{
name|algSuiteAi
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
operator|new
name|QName
argument_list|(
name|algorithmSuite
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|algorithmSuite
operator|.
name|getC14n
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ai
operator|.
name|isAsserted
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Error in validating AlgorithmSuite policy"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|validatePolicy
parameter_list|(
name|AssertionInfo
name|ai
parameter_list|,
name|AlgorithmSuite
name|algorithmPolicy
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
name|result
range|:
name|results
control|)
block|{
name|Integer
name|action
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
name|WSConstants
operator|.
name|SIGN
operator|==
name|action
operator|&&
operator|!
name|checkSignatureAlgorithms
argument_list|(
name|result
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|WSConstants
operator|.
name|ENCR
operator|==
name|action
operator|&&
operator|!
name|checkEncryptionAlgorithms
argument_list|(
name|result
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|)
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
comment|/**      * Check the Signature Algorithms      */
specifier|private
name|boolean
name|checkSignatureAlgorithms
parameter_list|(
name|WSSecurityEngineResult
name|result
parameter_list|,
name|AlgorithmSuite
name|algorithmPolicy
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|)
block|{
name|String
name|signatureMethod
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
name|TAG_SIGNATURE_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|algorithmPolicy
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getAsymmetricSignature
argument_list|()
operator|.
name|equals
argument_list|(
name|signatureMethod
argument_list|)
operator|&&
operator|!
name|algorithmPolicy
operator|.
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|getSymmetricSignature
argument_list|()
operator|.
name|equals
argument_list|(
name|signatureMethod
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The signature method does not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|c14nMethod
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
name|TAG_CANONICALIZATION_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|algorithmPolicy
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|c14nMethod
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The c14n method does not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
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
operator|!
name|checkDataRefs
argument_list|(
name|dataRefs
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|checkKeyLengths
argument_list|(
name|result
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Check the individual signature references      */
specifier|private
name|boolean
name|checkDataRefs
parameter_list|(
name|List
argument_list|<
name|WSDataRef
argument_list|>
name|dataRefs
parameter_list|,
name|AlgorithmSuite
name|algorithmPolicy
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|)
block|{
name|AlgorithmSuiteType
name|algorithmSuiteType
init|=
name|algorithmPolicy
operator|.
name|getAlgorithmSuiteType
argument_list|()
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
name|digestMethod
init|=
name|dataRef
operator|.
name|getDigestAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|algorithmSuiteType
operator|.
name|getDigest
argument_list|()
operator|.
name|equals
argument_list|(
name|digestMethod
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The digest method does not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|transformAlgorithms
init|=
name|dataRef
operator|.
name|getTransformAlgorithms
argument_list|()
decl_stmt|;
comment|// Only a max of 2 transforms per reference is allowed
if|if
condition|(
name|transformAlgorithms
operator|==
literal|null
operator|||
name|transformAlgorithms
operator|.
name|size
argument_list|()
operator|>
literal|2
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The transform algorithms do not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
for|for
control|(
name|String
name|transformAlgorithm
range|:
name|transformAlgorithms
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|algorithmPolicy
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|transformAlgorithm
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|C14N_EXCL_OMIT_COMMENTS
operator|.
name|equals
argument_list|(
name|transformAlgorithm
argument_list|)
operator|||
name|STRTransform
operator|.
name|TRANSFORM_URI
operator|.
name|equals
argument_list|(
name|transformAlgorithm
argument_list|)
operator|||
name|Transforms
operator|.
name|TRANSFORM_ENVELOPED_SIGNATURE
operator|.
name|equals
argument_list|(
name|transformAlgorithm
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SWA_ATTACHMENT_CONTENT_SIG_TRANS
operator|.
name|equals
argument_list|(
name|transformAlgorithm
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SWA_ATTACHMENT_COMPLETE_SIG_TRANS
operator|.
name|equals
argument_list|(
name|transformAlgorithm
argument_list|)
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The transform algorithms do not match the requirement"
argument_list|)
expr_stmt|;
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
comment|/**      * Check the Encryption Algorithms      */
specifier|private
name|boolean
name|checkEncryptionAlgorithms
parameter_list|(
name|WSSecurityEngineResult
name|result
parameter_list|,
name|AlgorithmSuite
name|algorithmPolicy
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|)
block|{
name|AlgorithmSuiteType
name|algorithmSuiteType
init|=
name|algorithmPolicy
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
name|String
name|transportMethod
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
name|TAG_ENCRYPTED_KEY_TRANSPORT_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|transportMethod
operator|!=
literal|null
operator|&&
operator|!
name|algorithmSuiteType
operator|.
name|getSymmetricKeyWrap
argument_list|()
operator|.
name|equals
argument_list|(
name|transportMethod
argument_list|)
operator|&&
operator|!
name|algorithmSuiteType
operator|.
name|getAsymmetricKeyWrap
argument_list|()
operator|.
name|equals
argument_list|(
name|transportMethod
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The Key transport method does not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
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
name|encryptionAlgorithm
init|=
name|dataRef
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|algorithmSuiteType
operator|.
name|getEncryption
argument_list|()
operator|.
name|equals
argument_list|(
name|encryptionAlgorithm
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The encryption algorithm does not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
name|checkKeyLengths
argument_list|(
name|result
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Check the key lengths of the secret and public keys.      */
specifier|private
name|boolean
name|checkKeyLengths
parameter_list|(
name|WSSecurityEngineResult
name|result
parameter_list|,
name|AlgorithmSuite
name|algorithmPolicy
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|boolean
name|signature
parameter_list|)
block|{
name|PublicKey
name|publicKey
init|=
operator|(
name|PublicKey
operator|)
name|result
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
name|publicKey
operator|!=
literal|null
operator|&&
operator|!
name|checkPublicKeyLength
argument_list|(
name|publicKey
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|X509Certificate
name|x509Cert
init|=
operator|(
name|X509Certificate
operator|)
name|result
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
name|x509Cert
operator|!=
literal|null
operator|&&
operator|!
name|checkPublicKeyLength
argument_list|(
name|x509Cert
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|algorithmPolicy
argument_list|,
name|ai
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AlgorithmSuiteType
name|algorithmSuiteType
init|=
name|algorithmPolicy
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
name|byte
index|[]
name|secret
init|=
operator|(
name|byte
index|[]
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SECRET
argument_list|)
decl_stmt|;
if|if
condition|(
name|signature
condition|)
block|{
name|Principal
name|principal
init|=
operator|(
name|Principal
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PRINCIPAL
argument_list|)
decl_stmt|;
if|if
condition|(
name|principal
operator|instanceof
name|WSDerivedKeyTokenPrincipal
condition|)
block|{
name|int
name|requiredLength
init|=
name|algorithmSuiteType
operator|.
name|getSignatureDerivedKeyLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|secret
operator|==
literal|null
operator|||
name|secret
operator|.
name|length
operator|!=
operator|(
name|requiredLength
operator|/
literal|8
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The signature derived key length does not match the requirement"
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
name|secret
operator|!=
literal|null
operator|&&
operator|(
name|secret
operator|.
name|length
argument_list|<
operator|(
name|algorithmSuiteType
operator|.
name|getMinimumSymmetricKeyLength
operator|(
operator|)
operator|/
literal|8
operator|)
operator|||
name|secret
operator|.
name|length
argument_list|>
argument_list|(
name|algorithmSuiteType
operator|.
name|getMaximumSymmetricKeyLength
argument_list|()
operator|/
literal|8
argument_list|)
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The symmetric key length does not match the requirement"
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
name|secret
operator|!=
literal|null
operator|&&
operator|(
name|secret
operator|.
name|length
argument_list|<
operator|(
name|algorithmSuiteType
operator|.
name|getMinimumSymmetricKeyLength
operator|(
operator|)
operator|/
literal|8
operator|)
operator|||
name|secret
operator|.
name|length
argument_list|>
argument_list|(
name|algorithmSuiteType
operator|.
name|getMaximumSymmetricKeyLength
argument_list|()
operator|/
literal|8
argument_list|)
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The symmetric key length does not match the requirement"
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
comment|/**      * Check the public key lengths      */
specifier|private
name|boolean
name|checkPublicKeyLength
parameter_list|(
name|PublicKey
name|publicKey
parameter_list|,
name|AlgorithmSuite
name|algorithmPolicy
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|)
block|{
name|AlgorithmSuiteType
name|algorithmSuiteType
init|=
name|algorithmPolicy
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
if|if
condition|(
name|publicKey
operator|instanceof
name|RSAPublicKey
condition|)
block|{
name|int
name|modulus
init|=
operator|(
operator|(
name|RSAPublicKey
operator|)
name|publicKey
operator|)
operator|.
name|getModulus
argument_list|()
operator|.
name|bitLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|modulus
argument_list|<
name|algorithmSuiteType
operator|.
name|getMinimumAsymmetricKeyLength
operator|(
operator|)
operator|||
name|modulus
argument_list|>
name|algorithmSuiteType
operator|.
name|getMaximumAsymmetricKeyLength
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The asymmetric key length does not match the requirement"
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
name|publicKey
operator|instanceof
name|DSAPublicKey
condition|)
block|{
name|int
name|length
init|=
operator|(
operator|(
name|DSAPublicKey
operator|)
name|publicKey
operator|)
operator|.
name|getParams
argument_list|()
operator|.
name|getP
argument_list|()
operator|.
name|bitLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|length
argument_list|<
name|algorithmSuiteType
operator|.
name|getMinimumAsymmetricKeyLength
operator|(
operator|)
operator|||
name|length
argument_list|>
name|algorithmSuiteType
operator|.
name|getMaximumAsymmetricKeyLength
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The asymmetric key length does not match the requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"An unknown public key was provided"
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
block|}
end_class

end_unit

