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
name|operation
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
name|claims
operator|.
name|ClaimsMapper
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
name|ClaimsParameters
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
name|ProcessedClaim
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
name|ProcessedClaimCollection
import|;
end_import

begin_comment
comment|/**  * A test implementation of ClaimsMapper.  */
end_comment

begin_class
specifier|public
class|class
name|CustomClaimsMapper
implements|implements
name|ClaimsMapper
block|{
comment|/**      * transforms the claim values to upper-case      */
specifier|public
name|ProcessedClaimCollection
name|mapClaims
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|ProcessedClaimCollection
name|sourceClaims
parameter_list|,
name|String
name|targetRealm
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
name|ProcessedClaimCollection
name|targetClaims
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessedClaim
name|c
range|:
name|sourceClaims
control|)
block|{
name|ProcessedClaim
name|nc
init|=
operator|new
name|ProcessedClaim
argument_list|()
decl_stmt|;
name|nc
operator|.
name|setClaimType
argument_list|(
name|c
operator|.
name|getClaimType
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|setIssuer
argument_list|(
name|c
operator|.
name|getIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|setOriginalIssuer
argument_list|(
name|c
operator|.
name|getOriginalIssuer
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|setPrincipal
argument_list|(
name|c
operator|.
name|getPrincipal
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|s
range|:
name|c
operator|.
name|getValues
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|instanceof
name|String
condition|)
block|{
name|nc
operator|.
name|addValue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|s
operator|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|targetClaims
operator|.
name|add
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
return|return
name|targetClaims
return|;
block|}
block|}
end_class

end_unit

