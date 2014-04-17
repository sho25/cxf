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
name|systest
operator|.
name|ws
operator|.
name|saml
package|;
end_package

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
name|stax
operator|.
name|validate
operator|.
name|SamlTokenValidatorImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Audience
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AudienceRestriction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Conditions
import|;
end_import

begin_comment
comment|/**  * This class checks that the Audiences received as part of AudienceRestrictions match a set   * list of endpoints.  */
end_comment

begin_class
specifier|public
class|class
name|StaxSaml2AudienceRestrictionValidator
extends|extends
name|SamlTokenValidatorImpl
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|endpointAddresses
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|checkConditions
parameter_list|(
name|SamlAssertionWrapper
name|samlAssertion
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|super
operator|.
name|checkConditions
argument_list|(
name|samlAssertion
argument_list|)
expr_stmt|;
name|Assertion
name|saml2Assertion
init|=
name|samlAssertion
operator|.
name|getSaml2
argument_list|()
decl_stmt|;
if|if
condition|(
name|saml2Assertion
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpointAddresses
operator|==
literal|null
operator|||
name|endpointAddresses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Conditions
name|conditions
init|=
name|samlAssertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
decl_stmt|;
if|if
condition|(
name|conditions
operator|!=
literal|null
operator|&&
name|conditions
operator|.
name|getAudienceRestrictions
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|boolean
name|foundAddress
init|=
literal|false
decl_stmt|;
for|for
control|(
name|AudienceRestriction
name|audienceRestriction
range|:
name|conditions
operator|.
name|getAudienceRestrictions
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Audience
argument_list|>
name|audiences
init|=
name|audienceRestriction
operator|.
name|getAudiences
argument_list|()
decl_stmt|;
if|if
condition|(
name|audiences
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Audience
name|audience
range|:
name|audiences
control|)
block|{
name|String
name|audienceURI
init|=
name|audience
operator|.
name|getAudienceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointAddresses
operator|.
name|contains
argument_list|(
name|audienceURI
argument_list|)
condition|)
block|{
name|foundAddress
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|foundAddress
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"invalidSAMLsecurity"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getEndpointAddresses
parameter_list|()
block|{
return|return
name|endpointAddresses
return|;
block|}
specifier|public
name|void
name|setEndpointAddresses
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|endpointAddresses
parameter_list|)
block|{
name|this
operator|.
name|endpointAddresses
operator|=
name|endpointAddresses
expr_stmt|;
block|}
block|}
end_class

end_unit

