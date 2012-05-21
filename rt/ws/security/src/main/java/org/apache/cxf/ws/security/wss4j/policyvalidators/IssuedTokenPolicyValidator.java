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
name|BinarySecurity
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|AssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
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
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|ClaimsPolicyValidator
name|claimsValidator
init|=
operator|new
name|DefaultClaimsPolicyValidator
argument_list|()
decl_stmt|;
specifier|public
name|IssuedTokenPolicyValidator
parameter_list|(
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
name|this
operator|.
name|signedResults
operator|=
name|signedResults
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|boolean
name|validatePolicy
parameter_list|(
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|,
name|AssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
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
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|issuedToken
argument_list|,
name|message
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|assertionWrapper
operator|==
literal|null
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|Element
name|template
init|=
name|issuedToken
operator|.
name|getRstTemplate
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
name|assertionWrapper
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Error in validating the IssuedToken policy"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
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
name|assertionWrapper
argument_list|,
name|signedResults
argument_list|,
name|tlsCerts
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Assertion fails holder-of-key requirements"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|validatePolicy
parameter_list|(
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|,
name|BinarySecurity
name|binarySecurityToken
parameter_list|)
block|{
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
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
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|issuedToken
argument_list|,
name|message
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|binarySecurityToken
operator|==
literal|null
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|Element
name|template
init|=
name|issuedToken
operator|.
name|getRstTemplate
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
name|binarySecurityToken
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Error in validating the IssuedToken policy"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check the issued token template against the received assertion      */
specifier|private
name|boolean
name|checkIssuedTokenTemplate
parameter_list|(
name|Element
name|template
parameter_list|,
name|AssertionWrapper
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
block|}
end_class

end_unit

