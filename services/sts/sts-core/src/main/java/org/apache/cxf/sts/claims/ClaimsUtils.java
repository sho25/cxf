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
name|claims
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
import|;
end_import

begin_comment
comment|/**  * Some common utility methods for claims  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ClaimsUtils
block|{
specifier|private
name|ClaimsUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|ProcessedClaimCollection
name|processClaims
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
comment|// Handle Claims
name|ClaimsManager
name|claimsManager
init|=
name|providerParameters
operator|.
name|getClaimsManager
argument_list|()
decl_stmt|;
name|ProcessedClaimCollection
name|retrievedClaims
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|claimsManager
operator|!=
literal|null
condition|)
block|{
name|ClaimsParameters
name|params
init|=
operator|new
name|ClaimsParameters
argument_list|()
decl_stmt|;
name|params
operator|.
name|setAdditionalProperties
argument_list|(
name|providerParameters
operator|.
name|getAdditionalProperties
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setAppliesToAddress
argument_list|(
name|providerParameters
operator|.
name|getAppliesToAddress
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setEncryptionProperties
argument_list|(
name|providerParameters
operator|.
name|getEncryptionProperties
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setKeyRequirements
argument_list|(
name|providerParameters
operator|.
name|getKeyRequirements
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|setPrincipal
argument_list|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setRoles
argument_list|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
operator|.
name|getRoles
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getActAs
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|setPrincipal
argument_list|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getActAs
argument_list|()
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setRoles
argument_list|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getActAs
argument_list|()
operator|.
name|getRoles
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|params
operator|.
name|setPrincipal
argument_list|(
name|providerParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|params
operator|.
name|setRealm
argument_list|(
name|providerParameters
operator|.
name|getRealm
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setStsProperties
argument_list|(
name|providerParameters
operator|.
name|getStsProperties
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setTokenRequirements
argument_list|(
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setTokenStore
argument_list|(
name|providerParameters
operator|.
name|getTokenStore
argument_list|()
argument_list|)
expr_stmt|;
name|params
operator|.
name|setMessageContext
argument_list|(
name|providerParameters
operator|.
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
name|retrievedClaims
operator|=
name|claimsManager
operator|.
name|retrieveClaimValues
argument_list|(
name|providerParameters
operator|.
name|getRequestedPrimaryClaims
argument_list|()
argument_list|,
name|providerParameters
operator|.
name|getRequestedSecondaryClaims
argument_list|()
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
return|return
name|retrievedClaims
return|;
block|}
block|}
end_class

end_unit

