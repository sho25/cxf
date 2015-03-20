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
name|dom
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
name|message
operator|.
name|token
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
name|SP13Constants
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
name|AbstractSecurityAssertion
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
operator|.
name|PasswordType
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
operator|.
name|UsernameTokenType
import|;
end_import

begin_comment
comment|/**  * Validate a UsernameToken policy.  */
end_comment

begin_class
specifier|public
class|class
name|UsernameTokenPolicyValidator
extends|extends
name|AbstractSecurityPolicyValidator
block|{
comment|/**      * Return true if this SecurityPolicyValidator implementation is capable of validating a       * policy defined by the AssertionInfo parameter      */
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
block|{
if|if
condition|(
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
name|USERNAME_TOKEN
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
name|USERNAME_TOKEN
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
comment|/**      * Validate policies. W      */
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
name|usernameTokenPolicy
init|=
operator|(
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
name|usernameTokenPolicy
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
name|usernameTokenPolicy
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
name|parameters
operator|.
name|getUsernameTokenResults
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
if|if
condition|(
operator|!
name|checkTokens
argument_list|(
name|usernameTokenPolicy
argument_list|,
name|ai
argument_list|,
name|parameters
operator|.
name|getUsernameTokenResults
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
block|}
specifier|private
name|void
name|assertToken
parameter_list|(
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
name|isCreated
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP13Constants
operator|.
name|CREATED
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isNonce
argument_list|()
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP13Constants
operator|.
name|NONCE
argument_list|)
expr_stmt|;
block|}
name|PasswordType
name|passwordType
init|=
name|token
operator|.
name|getPasswordType
argument_list|()
decl_stmt|;
if|if
condition|(
name|passwordType
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
name|namespace
argument_list|,
name|passwordType
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|UsernameTokenType
name|usernameTokenType
init|=
name|token
operator|.
name|getUsernameTokenType
argument_list|()
decl_stmt|;
if|if
condition|(
name|usernameTokenType
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
name|namespace
argument_list|,
name|usernameTokenType
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * All UsernameTokens must conform to the policy      */
specifier|public
name|boolean
name|checkTokens
parameter_list|(
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
name|usernameTokenPolicy
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|utResults
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|utResults
control|)
block|{
name|UsernameToken
name|usernameToken
init|=
operator|(
name|UsernameToken
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_USERNAME_TOKEN
argument_list|)
decl_stmt|;
name|PasswordType
name|passwordType
init|=
name|usernameTokenPolicy
operator|.
name|getPasswordType
argument_list|()
decl_stmt|;
name|boolean
name|isHashPassword
init|=
name|passwordType
operator|==
name|PasswordType
operator|.
name|HashPassword
decl_stmt|;
name|boolean
name|isNoPassword
init|=
name|passwordType
operator|==
name|PasswordType
operator|.
name|NoPassword
decl_stmt|;
if|if
condition|(
name|isHashPassword
operator|!=
name|usernameToken
operator|.
name|isHashed
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Password hashing policy not enforced"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|isNoPassword
operator|&&
operator|(
name|usernameToken
operator|.
name|getPassword
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Username Token NoPassword policy not enforced"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isNoPassword
operator|&&
operator|(
name|usernameToken
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
operator|)
operator|&&
name|isNonEndorsingSupportingToken
argument_list|(
name|usernameTokenPolicy
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Username Token No Password supplied"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|usernameTokenPolicy
operator|.
name|isCreated
argument_list|()
operator|&&
operator|(
name|usernameToken
operator|.
name|getCreated
argument_list|()
operator|==
literal|null
operator|||
name|usernameToken
operator|.
name|isHashed
argument_list|()
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Username Token Created policy not enforced"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|usernameTokenPolicy
operator|.
name|isNonce
argument_list|()
operator|&&
operator|(
name|usernameToken
operator|.
name|getNonce
argument_list|()
operator|==
literal|null
operator|||
name|usernameToken
operator|.
name|isHashed
argument_list|()
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Username Token Nonce policy not enforced"
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
comment|/**      * Return true if this UsernameToken policy is a (non-endorsing)SupportingToken. If this is      * true then the corresponding UsernameToken must have a password element.      */
specifier|private
name|boolean
name|isNonEndorsingSupportingToken
parameter_list|(
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
name|usernameTokenPolicy
parameter_list|)
block|{
name|AbstractSecurityAssertion
name|parentAssertion
init|=
name|usernameTokenPolicy
operator|.
name|getParentAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentAssertion
operator|instanceof
name|SupportingTokens
condition|)
block|{
name|SupportingTokens
name|supportingToken
init|=
operator|(
name|SupportingTokens
operator|)
name|parentAssertion
decl_stmt|;
name|String
name|localname
init|=
name|supportingToken
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|localname
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|SUPPORTING_TOKENS
argument_list|)
operator|||
name|localname
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
operator|||
name|localname
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
operator|||
name|localname
operator|.
name|equals
argument_list|(
name|SPConstants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

