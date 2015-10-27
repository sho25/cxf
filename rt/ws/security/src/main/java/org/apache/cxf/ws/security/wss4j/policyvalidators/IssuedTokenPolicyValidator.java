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
name|cert
operator|.
name|Certificate
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|IssuedToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_comment
comment|/**  * Validate a WSSecurityEngineResult corresponding to the processing of a SAML Assertion  * or Kerberos token against an IssuedToken policy.  */
end_comment

begin_class
specifier|public
class|class
name|IssuedTokenPolicyValidator
extends|extends
name|AbstractSamlPolicyValidator
block|{
specifier|private
name|ClaimsPolicyValidator
name|claimsValidator
init|=
operator|new
name|DefaultClaimsPolicyValidator
argument_list|()
decl_stmt|;
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a       * policy defined by the AssertionInfo parameter      */
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
name|ISSUED_TOKEN
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
name|ISSUED_TOKEN
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
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|samlResults
init|=
name|parameters
operator|.
name|getSamlResults
argument_list|()
decl_stmt|;
if|if
condition|(
name|samlResults
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|samlResult
range|:
name|samlResults
control|)
block|{
name|SamlAssertionWrapper
name|samlAssertion
init|=
operator|(
name|SamlAssertionWrapper
operator|)
name|samlResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|validateSAMLToken
argument_list|(
name|parameters
argument_list|,
name|samlAssertion
argument_list|,
name|ais
argument_list|)
condition|)
block|{
comment|// Store token on the security context
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|samlAssertion
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|getMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|bstResults
init|=
name|parameters
operator|.
name|getResults
argument_list|()
operator|.
name|getActionResults
argument_list|()
operator|.
name|get
argument_list|(
name|WSConstants
operator|.
name|BST
argument_list|)
decl_stmt|;
if|if
condition|(
name|bstResults
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|bstResult
range|:
name|bstResults
control|)
block|{
name|BinarySecurity
name|binarySecurity
init|=
operator|(
name|BinarySecurity
operator|)
name|bstResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_BINARY_SECURITY_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|bstResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_VALIDATED_TOKEN
argument_list|)
argument_list|)
operator|&&
name|validateBinarySecurityToken
argument_list|(
name|parameters
argument_list|,
name|binarySecurity
argument_list|,
name|ais
argument_list|)
condition|)
block|{
comment|// Store token on the security context
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|binarySecurity
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|getMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|validateSAMLToken
parameter_list|(
name|PolicyValidatorParameters
name|parameters
parameter_list|,
name|SamlAssertionWrapper
name|samlAssertion
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|)
block|{
name|boolean
name|asserted
init|=
literal|true
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|IssuedToken
name|issuedToken
init|=
operator|(
name|IssuedToken
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
name|assertToken
argument_list|(
name|issuedToken
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|issuedToken
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|samlAssertion
operator|==
literal|null
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Element
name|template
init|=
name|issuedToken
operator|.
name|getRequestSecurityTokenTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|template
operator|!=
literal|null
operator|&&
operator|!
name|checkIssuedTokenTemplate
argument_list|(
name|template
argument_list|,
name|samlAssertion
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Error in validating the IssuedToken policy"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Element
name|claims
init|=
name|issuedToken
operator|.
name|getClaims
argument_list|()
decl_stmt|;
if|if
condition|(
name|claims
operator|!=
literal|null
condition|)
block|{
name|String
name|dialect
init|=
name|claims
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Dialect"
argument_list|)
decl_stmt|;
if|if
condition|(
name|claimsValidator
operator|.
name|getDialect
argument_list|()
operator|.
name|equals
argument_list|(
name|dialect
argument_list|)
operator|&&
operator|!
name|claimsValidator
operator|.
name|validatePolicy
argument_list|(
name|claims
argument_list|,
name|samlAssertion
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Error in validating the Claims policy"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
name|TLSSessionInfo
name|tlsInfo
init|=
name|parameters
operator|.
name|getMessage
argument_list|()
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|Certificate
index|[]
name|tlsCerts
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tlsInfo
operator|!=
literal|null
condition|)
block|{
name|tlsCerts
operator|=
name|tlsInfo
operator|.
name|getPeerCertificates
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|checkHolderOfKey
argument_list|(
name|samlAssertion
argument_list|,
name|parameters
operator|.
name|getSignedResults
argument_list|()
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Assertion fails holder-of-key requirements"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
return|return
name|asserted
return|;
block|}
specifier|private
name|boolean
name|validateBinarySecurityToken
parameter_list|(
name|PolicyValidatorParameters
name|parameters
parameter_list|,
name|BinarySecurity
name|binarySecurity
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|)
block|{
name|boolean
name|asserted
init|=
literal|true
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|IssuedToken
name|issuedToken
init|=
operator|(
name|IssuedToken
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
name|asserted
operator|=
literal|true
expr_stmt|;
name|assertToken
argument_list|(
name|issuedToken
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|issuedToken
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|binarySecurity
operator|==
literal|null
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Element
name|template
init|=
name|issuedToken
operator|.
name|getRequestSecurityTokenTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|template
operator|!=
literal|null
operator|&&
operator|!
name|checkIssuedTokenTemplate
argument_list|(
name|template
argument_list|,
name|binarySecurity
argument_list|)
condition|)
block|{
name|asserted
operator|=
literal|false
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Error in validating the IssuedToken policy"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
return|return
name|asserted
return|;
block|}
specifier|private
name|void
name|assertToken
parameter_list|(
name|IssuedToken
name|token
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|String
name|namespace
init|=
name|token
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|isRequireExternalReference
argument_list|()
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
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EXTERNAL_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isRequireInternalReference
argument_list|()
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
name|namespace
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_INTERNAL_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Check the issued token template against the received assertion      */
specifier|private
name|boolean
name|checkIssuedTokenTemplate
parameter_list|(
name|Element
name|template
parameter_list|,
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|template
argument_list|)
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"TokenType"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|content
init|=
name|child
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|content
argument_list|)
operator|&&
name|assertionWrapper
operator|.
name|getSamlVersion
argument_list|()
operator|!=
name|SAMLVersion
operator|.
name|VERSION_11
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
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|content
argument_list|)
operator|&&
name|assertionWrapper
operator|.
name|getSamlVersion
argument_list|()
operator|!=
name|SAMLVersion
operator|.
name|VERSION_20
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
literal|"KeyType"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|content
init|=
name|child
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|.
name|endsWith
argument_list|(
literal|"SymmetricKey"
argument_list|)
condition|)
block|{
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectKeyInfo
operator|==
literal|null
operator|||
name|subjectKeyInfo
operator|.
name|getSecret
argument_list|()
operator|==
literal|null
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
name|content
operator|.
name|endsWith
argument_list|(
literal|"PublicKey"
argument_list|)
condition|)
block|{
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectKeyInfo
operator|==
literal|null
operator|||
operator|(
name|subjectKeyInfo
operator|.
name|getPublicKey
argument_list|()
operator|==
literal|null
operator|&&
name|subjectKeyInfo
operator|.
name|getCerts
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"Claims"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|dialect
init|=
name|child
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Dialect"
argument_list|)
decl_stmt|;
if|if
condition|(
name|claimsValidator
operator|.
name|getDialect
argument_list|()
operator|.
name|equals
argument_list|(
name|dialect
argument_list|)
operator|&&
operator|!
name|claimsValidator
operator|.
name|validatePolicy
argument_list|(
name|child
argument_list|,
name|assertionWrapper
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check the issued token template against the received BinarySecurityToken      */
specifier|private
name|boolean
name|checkIssuedTokenTemplate
parameter_list|(
name|Element
name|template
parameter_list|,
name|BinarySecurity
name|binarySecurityToken
parameter_list|)
block|{
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|template
argument_list|)
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"TokenType"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|content
init|=
name|child
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|String
name|valueType
init|=
name|binarySecurityToken
operator|.
name|getValueType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|content
operator|.
name|equals
argument_list|(
name|valueType
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|SecurityToken
name|createSecurityToken
parameter_list|(
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|assertionWrapper
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectKeyInfo
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setSecret
argument_list|(
name|subjectKeyInfo
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
name|X509Certificate
index|[]
name|certs
init|=
name|subjectKeyInfo
operator|.
name|getCerts
argument_list|()
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|token
operator|.
name|setX509Certificate
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|assertionWrapper
operator|.
name|getSaml1
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|assertionWrapper
operator|.
name|getSaml2
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
expr_stmt|;
block|}
name|token
operator|.
name|setToken
argument_list|(
name|assertionWrapper
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
specifier|private
name|SecurityToken
name|createSecurityToken
parameter_list|(
name|BinarySecurity
name|binarySecurityToken
parameter_list|)
block|{
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|binarySecurityToken
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|binarySecurityToken
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|binarySecurityToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|binarySecurityToken
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
block|}
end_class

end_unit

