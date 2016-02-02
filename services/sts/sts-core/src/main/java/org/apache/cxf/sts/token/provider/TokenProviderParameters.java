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
name|token
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|sts
operator|.
name|STSPropertiesMBean
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
name|sts
operator|.
name|claims
operator|.
name|ClaimsManager
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
name|sts
operator|.
name|request
operator|.
name|KeyRequirements
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
name|sts
operator|.
name|request
operator|.
name|TokenRequirements
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
name|sts
operator|.
name|service
operator|.
name|EncryptionProperties
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
name|tokenstore
operator|.
name|TokenStore
import|;
end_import

begin_comment
comment|/**  * This class encapsulates the parameters that will be passed to a TokenProvider instance to  * create a token. It consists of both parameters that have been extracted from the request,  * as well as configuration specific to the STS itself.  */
end_comment

begin_class
specifier|public
class|class
name|TokenProviderParameters
block|{
specifier|private
name|STSPropertiesMBean
name|stsProperties
decl_stmt|;
specifier|private
name|EncryptionProperties
name|encryptionProperties
decl_stmt|;
specifier|private
name|Principal
name|principal
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageContext
decl_stmt|;
specifier|private
name|ClaimCollection
name|requestedPrimaryClaims
decl_stmt|;
specifier|private
name|ClaimCollection
name|requestedSecondaryClaims
decl_stmt|;
specifier|private
name|KeyRequirements
name|keyRequirements
decl_stmt|;
specifier|private
name|TokenRequirements
name|tokenRequirements
decl_stmt|;
specifier|private
name|String
name|appliesToAddress
decl_stmt|;
specifier|private
name|ClaimsManager
name|claimsManager
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|additionalProperties
decl_stmt|;
specifier|private
name|TokenStore
name|tokenStore
decl_stmt|;
specifier|private
name|String
name|realm
decl_stmt|;
specifier|private
name|boolean
name|encryptToken
decl_stmt|;
specifier|public
name|TokenStore
name|getTokenStore
parameter_list|()
block|{
return|return
name|tokenStore
return|;
block|}
specifier|public
name|void
name|setTokenStore
parameter_list|(
name|TokenStore
name|tokenStore
parameter_list|)
block|{
name|this
operator|.
name|tokenStore
operator|=
name|tokenStore
expr_stmt|;
block|}
specifier|public
name|ClaimsManager
name|getClaimsManager
parameter_list|()
block|{
return|return
name|claimsManager
return|;
block|}
specifier|public
name|void
name|setClaimsManager
parameter_list|(
name|ClaimsManager
name|claimsManager
parameter_list|)
block|{
name|this
operator|.
name|claimsManager
operator|=
name|claimsManager
expr_stmt|;
block|}
specifier|public
name|String
name|getAppliesToAddress
parameter_list|()
block|{
return|return
name|appliesToAddress
return|;
block|}
specifier|public
name|void
name|setAppliesToAddress
parameter_list|(
name|String
name|appliesToAddress
parameter_list|)
block|{
name|this
operator|.
name|appliesToAddress
operator|=
name|appliesToAddress
expr_stmt|;
block|}
specifier|public
name|TokenRequirements
name|getTokenRequirements
parameter_list|()
block|{
return|return
name|tokenRequirements
return|;
block|}
specifier|public
name|void
name|setTokenRequirements
parameter_list|(
name|TokenRequirements
name|tokenRequirements
parameter_list|)
block|{
name|this
operator|.
name|tokenRequirements
operator|=
name|tokenRequirements
expr_stmt|;
block|}
specifier|public
name|KeyRequirements
name|getKeyRequirements
parameter_list|()
block|{
return|return
name|keyRequirements
return|;
block|}
specifier|public
name|void
name|setKeyRequirements
parameter_list|(
name|KeyRequirements
name|keyRequirements
parameter_list|)
block|{
name|this
operator|.
name|keyRequirements
operator|=
name|keyRequirements
expr_stmt|;
block|}
specifier|public
name|STSPropertiesMBean
name|getStsProperties
parameter_list|()
block|{
return|return
name|stsProperties
return|;
block|}
specifier|public
name|void
name|setStsProperties
parameter_list|(
name|STSPropertiesMBean
name|stsProperties
parameter_list|)
block|{
name|this
operator|.
name|stsProperties
operator|=
name|stsProperties
expr_stmt|;
block|}
specifier|public
name|EncryptionProperties
name|getEncryptionProperties
parameter_list|()
block|{
return|return
name|encryptionProperties
return|;
block|}
specifier|public
name|void
name|setEncryptionProperties
parameter_list|(
name|EncryptionProperties
name|encryptionProperties
parameter_list|)
block|{
name|this
operator|.
name|encryptionProperties
operator|=
name|encryptionProperties
expr_stmt|;
block|}
specifier|public
name|void
name|setPrincipal
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
name|this
operator|.
name|principal
operator|=
name|principal
expr_stmt|;
block|}
specifier|public
name|Principal
name|getPrincipal
parameter_list|()
block|{
return|return
name|principal
return|;
block|}
specifier|public
name|void
name|setAdditionalProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|additionalProperties
parameter_list|)
block|{
name|this
operator|.
name|additionalProperties
operator|=
name|additionalProperties
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getAdditionalProperties
parameter_list|()
block|{
return|return
name|additionalProperties
return|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
specifier|public
name|String
name|getRealm
parameter_list|()
block|{
return|return
name|realm
return|;
block|}
specifier|public
name|ClaimCollection
name|getRequestedPrimaryClaims
parameter_list|()
block|{
return|return
name|requestedPrimaryClaims
return|;
block|}
specifier|public
name|void
name|setRequestedPrimaryClaims
parameter_list|(
name|ClaimCollection
name|requestedPrimaryClaims
parameter_list|)
block|{
name|this
operator|.
name|requestedPrimaryClaims
operator|=
name|requestedPrimaryClaims
expr_stmt|;
block|}
specifier|public
name|ClaimCollection
name|getRequestedSecondaryClaims
parameter_list|()
block|{
return|return
name|requestedSecondaryClaims
return|;
block|}
specifier|public
name|void
name|setRequestedSecondaryClaims
parameter_list|(
name|ClaimCollection
name|requestedSecondaryClaims
parameter_list|)
block|{
name|this
operator|.
name|requestedSecondaryClaims
operator|=
name|requestedSecondaryClaims
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEncryptToken
parameter_list|()
block|{
return|return
name|encryptToken
return|;
block|}
specifier|public
name|void
name|setEncryptToken
parameter_list|(
name|boolean
name|encryptToken
parameter_list|)
block|{
name|this
operator|.
name|encryptToken
operator|=
name|encryptToken
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getMessageContext
parameter_list|()
block|{
return|return
name|messageContext
return|;
block|}
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageContext
parameter_list|)
block|{
name|this
operator|.
name|messageContext
operator|=
name|messageContext
expr_stmt|;
block|}
block|}
end_class

end_unit

