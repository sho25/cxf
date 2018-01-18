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
name|IssuedToken
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
name|KerberosToken
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
name|KeyValueToken
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
name|SecurityContextToken
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
name|SupportingTokens
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
name|UsernameToken
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
comment|/**  * Validate SignedSupportingToken policies.  */
end_comment

begin_class
specifier|public
class|class
name|SignedTokenPolicyValidator
extends|extends
name|AbstractSupportingTokenPolicyValidator
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
name|SIGNED_SUPPORTING_TOKENS
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
name|SIGNED_SUPPORTING_TOKENS
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
name|SupportingTokens
name|binding
init|=
operator|(
name|SupportingTokens
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
name|setSignedParts
argument_list|(
name|binding
operator|.
name|getSignedParts
argument_list|()
argument_list|)
expr_stmt|;
name|setEncryptedParts
argument_list|(
name|binding
operator|.
name|getEncryptedParts
argument_list|()
argument_list|)
expr_stmt|;
name|setSignedElements
argument_list|(
name|binding
operator|.
name|getSignedElements
argument_list|()
argument_list|)
expr_stmt|;
name|setEncryptedElements
argument_list|(
name|binding
operator|.
name|getEncryptedElements
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AbstractToken
argument_list|>
name|tokens
init|=
name|binding
operator|.
name|getTokens
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractToken
name|token
range|:
name|tokens
control|)
block|{
if|if
condition|(
operator|!
name|isTokenRequired
argument_list|(
name|token
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
name|boolean
name|processingFailed
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|token
operator|instanceof
name|UsernameToken
condition|)
block|{
if|if
condition|(
operator|!
name|processUsernameTokens
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|SamlToken
condition|)
block|{
if|if
condition|(
operator|!
name|processSAMLTokens
argument_list|(
name|parameters
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|KerberosToken
condition|)
block|{
if|if
condition|(
operator|!
name|processKerberosTokens
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|X509Token
condition|)
block|{
if|if
condition|(
operator|!
name|processX509Tokens
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|KeyValueToken
condition|)
block|{
if|if
condition|(
operator|!
name|processKeyValueTokens
argument_list|(
name|parameters
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|SecurityContextToken
condition|)
block|{
if|if
condition|(
operator|!
name|processSCTokens
argument_list|(
name|parameters
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|IssuedToken
condition|)
block|{
name|IssuedToken
name|issuedToken
init|=
operator|(
name|IssuedToken
operator|)
name|token
decl_stmt|;
if|if
condition|(
name|isSamlTokenRequiredForIssuedToken
argument_list|(
name|issuedToken
argument_list|)
operator|&&
operator|!
name|processSAMLTokens
argument_list|(
name|parameters
argument_list|)
condition|)
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|processingFailed
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|processingFailed
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"The received token does not match the signed supporting token requirement"
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
block|}
specifier|protected
name|boolean
name|isSigned
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|protected
name|boolean
name|isEncrypted
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|protected
name|boolean
name|isEndorsing
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

