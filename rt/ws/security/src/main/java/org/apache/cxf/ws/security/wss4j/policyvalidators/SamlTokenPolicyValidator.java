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
name|saml
operator|.
name|DOMSAMLUtil
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
name|SamlToken
operator|.
name|SamlTokenType
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
comment|/**  * Validate a SamlToken policy.  */
end_comment

begin_class
specifier|public
class|class
name|SamlTokenPolicyValidator
extends|extends
name|AbstractSamlPolicyValidator
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
name|SAML_TOKEN
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
name|SAML_TOKEN
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
name|SamlToken
name|samlToken
init|=
operator|(
name|SamlToken
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
name|samlToken
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
name|samlToken
argument_list|,
name|parameters
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
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
name|samlToken
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|samlToken
operator|.
name|getSamlTokenType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|parameters
operator|.
name|getSamlResults
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the token inclusion requirement"
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// All of the received SAML Assertions must conform to the policy
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|parameters
operator|.
name|getSamlResults
argument_list|()
control|)
block|{
name|SamlAssertionWrapper
name|assertionWrapper
init|=
operator|(
name|SamlAssertionWrapper
operator|)
name|result
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
operator|!
name|checkVersion
argument_list|(
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
name|samlToken
argument_list|,
name|assertionWrapper
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Wrong SAML Version"
argument_list|)
expr_stmt|;
continue|continue;
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
name|assertionWrapper
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
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Assertion fails holder-of-key requirements"
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|DOMSAMLUtil
operator|.
name|checkSenderVouches
argument_list|(
name|assertionWrapper
argument_list|,
name|tlsCerts
argument_list|,
name|parameters
operator|.
name|getSoapBody
argument_list|()
argument_list|,
name|parameters
operator|.
name|getSignedResults
argument_list|()
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Assertion fails sender-vouches requirements"
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|/*                     if (!checkIssuerName(samlToken, assertionWrapper)) {                         ai.setNotAsserted("Wrong IssuerName");                     }                  */
block|}
block|}
block|}
comment|/**      * Check the IssuerName policy against the received assertion     private boolean checkIssuerName(SamlToken samlToken, AssertionWrapper assertionWrapper) {         String issuerName = samlToken.getIssuerName();         if (issuerName != null&& !"".equals(issuerName)) {             String assertionIssuer = assertionWrapper.getIssuerString();             if (!issuerName.equals(assertionIssuer)) {                 return false;             }         }         return true;     }     */
comment|/**      * Check the policy version against the received assertion      */
specifier|private
name|boolean
name|checkVersion
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SamlToken
name|samlToken
parameter_list|,
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
name|SamlTokenType
name|samlTokenType
init|=
name|samlToken
operator|.
name|getSamlTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|samlTokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV11Token10
operator|||
name|samlTokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV11Token11
operator|)
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
name|samlTokenType
operator|==
name|SamlTokenType
operator|.
name|WssSamlV20Token11
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
if|if
condition|(
name|samlTokenType
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
name|samlToken
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|samlTokenType
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|assertToken
parameter_list|(
name|SamlToken
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
name|isRequireKeyIdentifierReference
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
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

