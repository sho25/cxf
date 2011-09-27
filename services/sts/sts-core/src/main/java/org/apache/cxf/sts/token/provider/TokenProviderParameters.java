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
name|cache
operator|.
name|STSTokenStore
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
name|claims
operator|.
name|RequestClaimCollection
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
name|WebServiceContext
name|webServiceContext
decl_stmt|;
specifier|private
name|RequestClaimCollection
name|requestedClaims
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
name|STSTokenStore
name|tokenStore
decl_stmt|;
specifier|public
name|STSTokenStore
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
name|STSTokenStore
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
name|RequestClaimCollection
name|getRequestedClaims
parameter_list|()
block|{
return|return
name|requestedClaims
return|;
block|}
specifier|public
name|void
name|setRequestedClaims
parameter_list|(
name|RequestClaimCollection
name|requestedClaims
parameter_list|)
block|{
name|this
operator|.
name|requestedClaims
operator|=
name|requestedClaims
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
name|WebServiceContext
name|getWebServiceContext
parameter_list|()
block|{
return|return
name|webServiceContext
return|;
block|}
specifier|public
name|void
name|setWebServiceContext
parameter_list|(
name|WebServiceContext
name|webServiceContext
parameter_list|)
block|{
name|this
operator|.
name|webServiceContext
operator|=
name|webServiceContext
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
block|}
end_class

end_unit

