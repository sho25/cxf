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
name|Collection
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
block|{
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
block|}
end_class

end_unit

