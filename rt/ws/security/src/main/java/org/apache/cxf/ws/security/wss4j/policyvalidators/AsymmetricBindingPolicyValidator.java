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
name|AbstractToken
operator|.
name|DerivedKeys
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
name|AbstractTokenWrapper
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
name|AsymmetricBinding
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
comment|/**  * Validate an AsymmetricBinding policy.  */
end_comment

begin_class
specifier|public
class|class
name|AsymmetricBindingPolicyValidator
extends|extends
name|AbstractBindingPolicyValidator
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
name|ASYMMETRIC_BINDING
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
name|ASYMMETRIC_BINDING
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
name|boolean
name|hasDerivedKeys
init|=
literal|false
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|parameters
operator|.
name|getResults
argument_list|()
control|)
block|{
name|Integer
name|actInt
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
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|DKT
condition|)
block|{
name|hasDerivedKeys
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|AsymmetricBinding
name|binding
init|=
operator|(
name|AsymmetricBinding
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
comment|// Check the protection order
if|if
condition|(
operator|!
name|checkProtectionOrder
argument_list|(
name|binding
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
name|ai
argument_list|,
name|parameters
operator|.
name|getResults
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// Check various properties of the binding
if|if
condition|(
operator|!
name|checkProperties
argument_list|(
name|binding
argument_list|,
name|ai
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
name|parameters
operator|.
name|getResults
argument_list|()
argument_list|,
name|parameters
operator|.
name|getSignedResults
argument_list|()
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
comment|// Check various tokens of the binding
if|if
condition|(
operator|!
name|checkTokens
argument_list|(
name|binding
argument_list|,
name|ai
argument_list|,
name|parameters
operator|.
name|getAssertionInfoMap
argument_list|()
argument_list|,
name|hasDerivedKeys
argument_list|,
name|parameters
operator|.
name|getSignedResults
argument_list|()
argument_list|,
name|parameters
operator|.
name|getEncryptedResults
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
block|}
comment|/**      * Check various tokens of the binding      */
specifier|private
name|boolean
name|checkTokens
parameter_list|(
name|AsymmetricBinding
name|binding
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|boolean
name|hasDerivedKeys
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|binding
operator|.
name|getInitiatorToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|&=
name|checkInitiatorTokens
argument_list|(
name|binding
operator|.
name|getInitiatorToken
argument_list|()
argument_list|,
name|binding
argument_list|,
name|ai
argument_list|,
name|aim
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|.
name|getInitiatorSignatureToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|&=
name|checkInitiatorTokens
argument_list|(
name|binding
operator|.
name|getInitiatorSignatureToken
argument_list|()
argument_list|,
name|binding
argument_list|,
name|ai
argument_list|,
name|aim
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|.
name|getInitiatorEncryptionToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|&=
name|checkInitiatorTokens
argument_list|(
name|binding
operator|.
name|getInitiatorEncryptionToken
argument_list|()
argument_list|,
name|binding
argument_list|,
name|ai
argument_list|,
name|aim
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|.
name|getRecipientToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|&=
name|checkRecipientTokens
argument_list|(
name|binding
operator|.
name|getRecipientToken
argument_list|()
argument_list|,
name|binding
argument_list|,
name|ai
argument_list|,
name|aim
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|.
name|getRecipientSignatureToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|&=
name|checkRecipientTokens
argument_list|(
name|binding
operator|.
name|getRecipientSignatureToken
argument_list|()
argument_list|,
name|binding
argument_list|,
name|ai
argument_list|,
name|aim
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|.
name|getRecipientEncryptionToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|&=
name|checkRecipientTokens
argument_list|(
name|binding
operator|.
name|getRecipientEncryptionToken
argument_list|()
argument_list|,
name|binding
argument_list|,
name|ai
argument_list|,
name|aim
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|boolean
name|checkInitiatorTokens
parameter_list|(
name|AbstractTokenWrapper
name|wrapper
parameter_list|,
name|AsymmetricBinding
name|binding
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|boolean
name|hasDerivedKeys
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
parameter_list|)
block|{
name|AbstractToken
name|token
init|=
name|wrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|instanceof
name|X509Token
condition|)
block|{
name|boolean
name|foundCert
init|=
literal|false
decl_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|signedResults
control|)
block|{
name|X509Certificate
name|cert
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
name|cert
operator|!=
literal|null
condition|)
block|{
name|foundCert
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|foundCert
operator|&&
operator|!
name|signedResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|error
init|=
literal|"An X.509 certificate was not used for the "
operator|+
name|wrapper
operator|.
name|getName
argument_list|()
decl_stmt|;
name|notAssertPolicy
argument_list|(
name|aim
argument_list|,
name|wrapper
operator|.
name|getName
argument_list|()
argument_list|,
name|error
argument_list|)
expr_stmt|;
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|wrapper
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|checkDerivedKeys
argument_list|(
name|wrapper
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Message fails the DerivedKeys requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|assertToken
argument_list|(
name|wrapper
argument_list|,
name|aim
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|checkRecipientTokens
parameter_list|(
name|AbstractTokenWrapper
name|wrapper
parameter_list|,
name|AsymmetricBinding
name|binding
parameter_list|,
name|AssertionInfo
name|ai
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|boolean
name|hasDerivedKeys
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
parameter_list|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|wrapper
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|checkDerivedKeys
argument_list|(
name|wrapper
argument_list|,
name|hasDerivedKeys
argument_list|,
name|signedResults
argument_list|,
name|encryptedResults
argument_list|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Message fails the DerivedKeys requirement"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|assertToken
argument_list|(
name|wrapper
argument_list|,
name|aim
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|assertToken
parameter_list|(
name|AbstractTokenWrapper
name|tokenWrapper
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|String
name|namespace
init|=
name|tokenWrapper
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|AbstractToken
name|token
init|=
name|tokenWrapper
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|DerivedKeys
name|derivedKeys
init|=
name|token
operator|.
name|getDerivedKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|derivedKeys
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
name|derivedKeys
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

