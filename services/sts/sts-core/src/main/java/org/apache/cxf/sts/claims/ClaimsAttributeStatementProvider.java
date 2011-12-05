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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|AttributeStatementProvider
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
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|AttributeBean
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
name|saml
operator|.
name|ext
operator|.
name|bean
operator|.
name|AttributeStatementBean
import|;
end_import

begin_class
specifier|public
class|class
name|ClaimsAttributeStatementProvider
implements|implements
name|AttributeStatementProvider
block|{
specifier|public
name|AttributeStatementBean
name|getStatement
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
name|ClaimCollection
name|retrievedClaims
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|claimsManager
operator|!=
literal|null
condition|)
block|{
name|retrievedClaims
operator|=
name|claimsManager
operator|.
name|retrieveClaimValues
argument_list|(
name|providerParameters
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|providerParameters
operator|.
name|getRequestedClaims
argument_list|()
argument_list|,
name|providerParameters
operator|.
name|getWebServiceContext
argument_list|()
argument_list|,
name|providerParameters
operator|.
name|getRealm
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|attributeList
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeBean
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|tokenType
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Claim
argument_list|>
name|claimIterator
init|=
name|retrievedClaims
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|claimIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
while|while
condition|(
name|claimIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Claim
name|claim
init|=
name|claimIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
name|URI
name|name
init|=
name|claim
operator|.
name|getNamespace
argument_list|()
operator|.
name|relativize
argument_list|(
name|claim
operator|.
name|getClaimType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
name|claim
operator|.
name|getNamespace
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attributeBean
operator|.
name|setSimpleName
argument_list|(
name|name
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
name|claim
operator|.
name|getNamespace
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|attributeBean
operator|.
name|setAttributeValues
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|claim
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|attributeList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
block|}
name|attrBean
operator|.
name|setSamlAttributes
argument_list|(
name|attributeList
argument_list|)
expr_stmt|;
return|return
name|attrBean
return|;
block|}
block|}
end_class

end_unit

