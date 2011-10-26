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
name|ArrayList
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
name|AsymmetricBinding
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
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
decl_stmt|;
specifier|private
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|encryptedResults
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|boolean
name|hasDerivedKeys
decl_stmt|;
specifier|public
name|AsymmetricBindingPolicyValidator
parameter_list|(
name|Message
name|message
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
parameter_list|,
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|results
operator|=
name|results
expr_stmt|;
name|this
operator|.
name|signedResults
operator|=
name|signedResults
expr_stmt|;
comment|// Store the encryption results and whether we have any derived key results
name|encryptedResults
operator|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|WSSecurityEngineResult
name|result
range|:
name|results
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
block|}
elseif|else
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ENCR
condition|)
block|{
name|encryptedResults
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|boolean
name|validatePolicy
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
decl_stmt|;
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
name|ai
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
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
name|aim
argument_list|,
name|signedResults
argument_list|,
name|message
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
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
name|aim
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
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
parameter_list|)
block|{
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
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|binding
operator|.
name|getInitiatorToken
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|checkDerivedKeys
argument_list|(
name|binding
operator|.
name|getInitiatorToken
argument_list|()
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
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|binding
operator|.
name|getRecipientToken
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|checkDerivedKeys
argument_list|(
name|binding
operator|.
name|getRecipientToken
argument_list|()
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
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

