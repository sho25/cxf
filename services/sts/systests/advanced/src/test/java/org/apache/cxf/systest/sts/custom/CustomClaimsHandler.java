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
name|systest
operator|.
name|sts
operator|.
name|custom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|List
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
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
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
name|claims
operator|.
name|ClaimsHandler
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
name|util
operator|.
name|XMLUtils
import|;
end_import

begin_comment
comment|/**  * A custom ClaimsHandler implementation for use in the tests.  */
end_comment

begin_class
specifier|public
class|class
name|CustomClaimsHandler
implements|implements
name|ClaimsHandler
block|{
specifier|public
specifier|static
specifier|final
name|URI
name|ROLE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|URI
name|GIVEN_NAME
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|URI
name|LANGUAGE
init|=
name|URI
operator|.
name|create
argument_list|(
literal|"http://schemas.mycompany.com/claims/language"
argument_list|)
decl_stmt|;
specifier|public
name|ProcessedClaimCollection
name|retrieveClaimValues
parameter_list|(
name|ClaimCollection
name|claims
parameter_list|,
name|ClaimsParameters
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|claims
operator|!=
literal|null
operator|&&
name|claims
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ProcessedClaimCollection
name|claimCollection
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|customContent
init|=
name|parameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getCustomContent
argument_list|()
decl_stmt|;
name|boolean
name|foundContent
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|customContent
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Element
name|customContentElement
range|:
name|customContent
control|)
block|{
name|Element
name|realm
init|=
name|XMLUtils
operator|.
name|findElement
argument_list|(
name|customContentElement
argument_list|,
literal|"realm"
argument_list|,
literal|"http://cxf.apache.org/custom"
argument_list|)
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|String
name|realmStr
init|=
name|realm
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"custom-realm"
operator|.
name|equals
argument_list|(
name|realmStr
argument_list|)
condition|)
block|{
name|foundContent
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
for|for
control|(
name|Claim
name|requestClaim
range|:
name|claims
control|)
block|{
name|ProcessedClaim
name|claim
init|=
operator|new
name|ProcessedClaim
argument_list|()
decl_stmt|;
name|claim
operator|.
name|setClaimType
argument_list|(
name|requestClaim
operator|.
name|getClaimType
argument_list|()
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setIssuer
argument_list|(
literal|"Test Issuer"
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setOriginalIssuer
argument_list|(
literal|"Original Issuer"
argument_list|)
expr_stmt|;
if|if
condition|(
name|foundContent
condition|)
block|{
if|if
condition|(
name|ROLE
operator|.
name|equals
argument_list|(
name|requestClaim
operator|.
name|getClaimType
argument_list|()
argument_list|)
condition|)
block|{
name|claim
operator|.
name|addValue
argument_list|(
literal|"admin-user"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|GIVEN_NAME
operator|.
name|equals
argument_list|(
name|requestClaim
operator|.
name|getClaimType
argument_list|()
argument_list|)
condition|)
block|{
name|claim
operator|.
name|addValue
argument_list|(
name|parameters
operator|.
name|getPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LANGUAGE
operator|.
name|equals
argument_list|(
name|requestClaim
operator|.
name|getClaimType
argument_list|()
argument_list|)
condition|)
block|{
name|claim
operator|.
name|addValue
argument_list|(
name|parameters
operator|.
name|getPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|claimCollection
operator|.
name|add
argument_list|(
name|claim
argument_list|)
expr_stmt|;
block|}
return|return
name|claimCollection
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|getSupportedClaimTypes
parameter_list|()
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|ROLE
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|GIVEN_NAME
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|LANGUAGE
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
block|}
end_class

end_unit
