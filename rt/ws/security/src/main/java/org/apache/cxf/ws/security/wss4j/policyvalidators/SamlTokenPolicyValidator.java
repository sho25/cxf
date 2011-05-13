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
name|OpenSAMLUtil
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
comment|/**  * Validate a WSSecurityEngineResult corresponding to the processing of a SAML Assertion  * against the appropriate policy.  */
end_comment

begin_class
specifier|public
class|class
name|SamlTokenPolicyValidator
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
name|Element
name|soapBody
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|public
name|SamlTokenPolicyValidator
parameter_list|(
name|Element
name|soapBody
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
name|this
operator|.
name|soapBody
operator|=
name|soapBody
expr_stmt|;
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
name|AssertionInfoMap
name|aim
parameter_list|,
name|WSSecurityEngineResult
name|wser
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|samlAis
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SAML_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|samlAis
operator|!=
literal|null
operator|&&
operator|!
name|samlAis
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
name|samlAis
control|)
block|{
name|AssertionWrapper
name|assertionWrapper
init|=
operator|(
name|AssertionWrapper
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
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
name|boolean
name|tokenRequired
init|=
name|isTokenRequired
argument_list|(
name|samlToken
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|tokenRequired
operator|&&
name|assertionWrapper
operator|==
literal|null
operator|)
operator|||
operator|(
operator|!
name|tokenRequired
operator|&&
name|assertionWrapper
operator|!=
literal|null
operator|)
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
if|if
condition|(
operator|!
name|tokenRequired
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|checkVersion
argument_list|(
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
if|if
condition|(
operator|!
name|checkSenderVouches
argument_list|(
name|assertionWrapper
argument_list|,
name|tlsCerts
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
return|return
literal|false
return|;
block|}
comment|/*                 if (!checkIssuerName(samlToken, assertionWrapper)) {                     ai.setNotAsserted("Wrong IssuerName");                 }                 */
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Check the IssuerName policy against the received assertion     private boolean checkIssuerName(SamlToken samlToken, AssertionWrapper assertionWrapper) {         String issuerName = samlToken.getIssuerName();         if (issuerName != null&& !"".equals(issuerName)) {             String assertionIssuer = assertionWrapper.getIssuerString();             if (!issuerName.equals(assertionIssuer)) {                 return false;             }         }         return true;     }     */
comment|/**      * Check the policy version against the received assertion      */
specifier|private
name|boolean
name|checkVersion
parameter_list|(
name|SamlToken
name|samlToken
parameter_list|,
name|AssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
if|if
condition|(
operator|(
name|samlToken
operator|.
name|isUseSamlVersion11Profile10
argument_list|()
operator|||
name|samlToken
operator|.
name|isUseSamlVersion11Profile11
argument_list|()
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
name|samlToken
operator|.
name|isUseSamlVersion20Profile11
argument_list|()
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
return|return
literal|true
return|;
block|}
comment|/**      * Check the sender-vouches requirements against the received assertion. The SAML      * Assertion and the SOAP Body must be signed by the same signature.      */
specifier|private
name|boolean
name|checkSenderVouches
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
parameter_list|,
name|Certificate
index|[]
name|tlsCerts
parameter_list|)
block|{
comment|//
comment|// If we have a 2-way TLS connection, then we don't have to check that the
comment|// assertion + SOAP body are signed
comment|//
if|if
condition|(
name|tlsCerts
operator|!=
literal|null
operator|&&
name|tlsCerts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|confirmationMethods
init|=
name|assertionWrapper
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|confirmationMethods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodSenderVouches
argument_list|(
name|confirmationMethod
argument_list|)
condition|)
block|{
if|if
condition|(
name|signedResults
operator|==
literal|null
operator|||
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
if|if
condition|(
operator|!
name|checkAssertionAndBodyAreSigned
argument_list|(
name|assertionWrapper
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
comment|/**      * Return true if there is a signature which references the Assertion and the SOAP Body.      * @param assertionWrapper the AssertionWrapper object      * @return true if there is a signature which references the Assertion and the SOAP Body.      */
specifier|private
name|boolean
name|checkAssertionAndBodyAreSigned
parameter_list|(
name|AssertionWrapper
name|assertionWrapper
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
name|boolean
name|assertionIsSigned
init|=
literal|false
decl_stmt|;
name|boolean
name|bodyIsSigned
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
name|se
init|=
name|dataRef
operator|.
name|getProtectedElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|se
operator|==
name|assertionWrapper
operator|.
name|getElement
argument_list|()
condition|)
block|{
name|assertionIsSigned
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|se
operator|==
name|soapBody
condition|)
block|{
name|bodyIsSigned
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|assertionIsSigned
operator|&&
name|bodyIsSigned
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
block|}
end_class

end_unit

