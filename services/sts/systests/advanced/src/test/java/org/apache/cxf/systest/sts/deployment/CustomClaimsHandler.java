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
name|deployment
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
name|saml
operator|.
name|OpenSAMLUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|XMLObjectBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|XMLObjectBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|config
operator|.
name|XMLObjectProviderRegistrySupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|schema
operator|.
name|XSInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeValue
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
name|String
name|ROLE
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GIVEN_NAME
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LANGUAGE
init|=
literal|"http://schemas.mycompany.com/claims/language"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NUMBER
init|=
literal|"http://schemas.mycompany.com/claims/number"
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
operator|!
name|claims
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ProcessedClaimCollection
name|claimCollection
init|=
operator|new
name|ProcessedClaimCollection
argument_list|()
decl_stmt|;
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
name|NUMBER
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
comment|// Create and add a custom Attribute (Integer)
name|OpenSAMLUtil
operator|.
name|initSamlEngine
argument_list|()
expr_stmt|;
name|XMLObjectBuilderFactory
name|builderFactory
init|=
name|XMLObjectProviderRegistrySupport
operator|.
name|getBuilderFactory
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|XMLObjectBuilder
argument_list|<
name|XSInteger
argument_list|>
name|xsIntegerBuilder
init|=
operator|(
name|XMLObjectBuilder
argument_list|<
name|XSInteger
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|XSInteger
operator|.
name|TYPE_NAME
argument_list|)
decl_stmt|;
name|XSInteger
name|attributeValue
init|=
name|xsIntegerBuilder
operator|.
name|buildObject
argument_list|(
name|AttributeValue
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|,
name|XSInteger
operator|.
name|TYPE_NAME
argument_list|)
decl_stmt|;
name|attributeValue
operator|.
name|setValue
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|claim
operator|.
name|addValue
argument_list|(
name|attributeValue
argument_list|)
expr_stmt|;
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
name|String
argument_list|>
name|getSupportedClaimTypes
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|list
operator|.
name|add
argument_list|(
name|NUMBER
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

