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
name|sts
operator|.
name|request
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceContext
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

begin_comment
comment|/**  * This DelegationHandler implementation extends the Default implementation to allow UsernameTokens  * for OnBehalfOf/ActAs  */
end_comment

begin_class
specifier|public
class|class
name|UsernameTokenDelegationHandler
extends|extends
name|DefaultDelegationHandler
block|{
comment|/**      * Is Delegation allowed for a particular token      */
specifier|protected
name|boolean
name|isDelegationAllowed
parameter_list|(
name|WebServiceContext
name|context
parameter_list|,
name|ReceivedToken
name|receivedToken
parameter_list|,
name|String
name|appliesToAddress
parameter_list|)
block|{
if|if
condition|(
name|receivedToken
operator|.
name|isUsernameToken
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// It must be a SAML Token
if|if
condition|(
operator|!
name|isSAMLToken
argument_list|(
name|receivedToken
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Element
name|validateTargetElement
init|=
operator|(
name|Element
operator|)
name|receivedToken
operator|.
name|getToken
argument_list|()
decl_stmt|;
try|try
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|validateTargetElement
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|confirmationMethod
range|:
name|assertion
operator|.
name|getConfirmationMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|SAML1Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|confirmationMethod
argument_list|)
operator|||
name|SAML2Constants
operator|.
name|CONF_BEARER
operator|.
name|equals
argument_list|(
name|confirmationMethod
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|appliesToAddress
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
name|getAudienceRestrictions
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|addresses
operator|.
name|isEmpty
argument_list|()
operator|||
name|addresses
operator|.
name|contains
argument_list|(
name|appliesToAddress
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
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

