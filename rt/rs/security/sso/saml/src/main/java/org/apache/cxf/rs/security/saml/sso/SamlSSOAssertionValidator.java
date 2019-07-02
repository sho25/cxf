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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|sso
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|logging
operator|.
name|LogUtils
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
name|OpenSAMLUtil
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
name|saml
operator|.
name|builder
operator|.
name|SAML1Constants
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
name|builder
operator|.
name|SAML2Constants
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
name|validate
operator|.
name|SamlAssertionValidator
import|;
end_import

begin_comment
comment|/**  * An extension of the WSS4J SamlAssertionValidator. We can weaken the subject confirmation method requirements a bit  * for SAML SSO. A Bearer Assertion does not have to be signed by default if the outer Response is signed.  */
end_comment

begin_class
specifier|public
class|class
name|SamlSSOAssertionValidator
extends|extends
name|SamlAssertionValidator
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SamlSSOAssertionValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|signedResponse
decl_stmt|;
specifier|public
name|SamlSSOAssertionValidator
parameter_list|(
name|boolean
name|signedResponse
parameter_list|)
block|{
name|this
operator|.
name|signedResponse
operator|=
name|signedResponse
expr_stmt|;
block|}
comment|/**      * Check the Subject Confirmation method requirements      */
specifier|protected
name|void
name|verifySubjectConfirmationMethod
parameter_list|(
name|SamlAssertionWrapper
name|samlAssertion
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|methods
init|=
name|samlAssertion
operator|.
name|getConfirmationMethods
argument_list|()
decl_stmt|;
if|if
condition|(
name|methods
operator|==
literal|null
operator|||
name|methods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|super
operator|.
name|getRequiredSubjectConfirmationMethod
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"A required subject confirmation method was not present"
argument_list|)
expr_stmt|;
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
elseif|else
if|if
condition|(
name|super
operator|.
name|isRequireStandardSubjectConfirmationMethod
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"A standard subject confirmation method was not present"
argument_list|)
expr_stmt|;
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
name|boolean
name|signed
init|=
name|samlAssertion
operator|.
name|isSigned
argument_list|()
decl_stmt|;
name|boolean
name|requiredMethodFound
init|=
literal|false
decl_stmt|;
name|boolean
name|standardMethodFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|OpenSAMLUtil
operator|.
name|isMethodHolderOfKey
argument_list|(
name|method
argument_list|)
condition|)
block|{
if|if
condition|(
name|samlAssertion
operator|.
name|getSubjectKeyInfo
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"There is no Subject KeyInfo to match the holder-of-key subject conf method"
argument_list|)
expr_stmt|;
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
literal|"noKeyInSAMLToken"
argument_list|)
throw|;
block|}
comment|// The assertion must have been signed for HOK
if|if
condition|(
operator|!
name|signed
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"A holder-of-key assertion must be signed"
argument_list|)
expr_stmt|;
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
name|standardMethodFound
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|method
operator|.
name|equals
argument_list|(
name|super
operator|.
name|getRequiredSubjectConfirmationMethod
argument_list|()
argument_list|)
condition|)
block|{
name|requiredMethodFound
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|SAML2Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|||
name|SAML1Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|standardMethodFound
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|super
operator|.
name|isRequireBearerSignature
argument_list|()
operator|&&
operator|!
name|signed
operator|&&
operator|!
name|signedResponse
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"A Bearer Assertion was not signed"
argument_list|)
expr_stmt|;
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
elseif|else
if|if
condition|(
name|SAML2Constants
operator|.
name|CONF_SENDER_VOUCHES
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|||
name|SAML1Constants
operator|.
name|CONF_SENDER_VOUCHES
operator|.
name|equals
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|standardMethodFound
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|requiredMethodFound
operator|&&
name|super
operator|.
name|getRequiredSubjectConfirmationMethod
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"A required subject confirmation method was not present"
argument_list|)
expr_stmt|;
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
operator|!
name|standardMethodFound
operator|&&
name|super
operator|.
name|isRequireStandardSubjectConfirmationMethod
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"A standard subject confirmation method was not present"
argument_list|)
expr_stmt|;
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
end_class

end_unit

