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
name|common
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
name|sts
operator|.
name|claims
operator|.
name|ClaimsUtils
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
name|cxf
operator|.
name|sts
operator|.
name|request
operator|.
name|ReceivedToken
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|UsernameTokenType
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
name|WSS4JConstants
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
name|principal
operator|.
name|SAMLTokenPrincipal
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
name|principal
operator|.
name|SAMLTokenPrincipalImpl
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|bean
operator|.
name|AttributeStatementBean
import|;
end_import

begin_comment
comment|/**  * A custom AttributeStatementProvider implementation for use in the tests.  */
end_comment

begin_class
specifier|public
class|class
name|CustomAttributeProvider
implements|implements
name|AttributeStatementProvider
block|{
comment|/**      * Get an AttributeStatementBean using the given parameters.      */
specifier|public
name|AttributeStatementBean
name|getStatement
parameter_list|(
name|TokenProviderParameters
name|providerParameters
parameter_list|)
block|{
name|List
argument_list|<
name|AttributeBean
argument_list|>
name|attributeList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|String
name|tokenType
init|=
name|tokenRequirements
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
comment|// Handle Claims
name|ProcessedClaimCollection
name|retrievedClaims
init|=
name|ClaimsUtils
operator|.
name|processClaims
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|AttributeStatementBean
name|attrBean
init|=
operator|new
name|AttributeStatementBean
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ProcessedClaim
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
comment|// If no Claims have been processed then create a default attribute
name|AttributeBean
name|attributeBean
init|=
name|createDefaultAttribute
argument_list|(
name|tokenType
argument_list|)
decl_stmt|;
name|attributeList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|claimIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessedClaim
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
name|createAttributeFromClaim
argument_list|(
name|claim
argument_list|,
name|tokenType
argument_list|)
decl_stmt|;
name|attributeList
operator|.
name|add
argument_list|(
name|attributeBean
argument_list|)
expr_stmt|;
block|}
name|ReceivedToken
name|onBehalfOf
init|=
name|tokenRequirements
operator|.
name|getOnBehalfOf
argument_list|()
decl_stmt|;
name|ReceivedToken
name|actAs
init|=
name|tokenRequirements
operator|.
name|getActAs
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|onBehalfOf
operator|!=
literal|null
condition|)
block|{
name|AttributeBean
name|parameterBean
init|=
name|handleAdditionalParameters
argument_list|(
literal|false
argument_list|,
name|onBehalfOf
operator|.
name|getToken
argument_list|()
argument_list|,
name|tokenType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|parameterBean
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|attributeList
operator|.
name|add
argument_list|(
name|parameterBean
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|actAs
operator|!=
literal|null
condition|)
block|{
name|AttributeBean
name|parameterBean
init|=
name|handleAdditionalParameters
argument_list|(
literal|true
argument_list|,
name|actAs
operator|.
name|getToken
argument_list|()
argument_list|,
name|tokenType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|parameterBean
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|attributeList
operator|.
name|add
argument_list|(
name|parameterBean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|STSException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
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
comment|/**      * Create a default attribute      */
specifier|private
name|AttributeBean
name|createDefaultAttribute
parameter_list|(
name|String
name|tokenType
parameter_list|)
block|{
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|WSS4JConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSS4JConstants
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
literal|"token-requestor"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setNameFormat
argument_list|(
literal|"http://cxf.apache.org/sts/custom"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attributeBean
operator|.
name|setSimpleName
argument_list|(
literal|"token-requestor"
argument_list|)
expr_stmt|;
name|attributeBean
operator|.
name|setQualifiedName
argument_list|(
literal|"http://cxf.apache.org/sts/custom"
argument_list|)
expr_stmt|;
block|}
name|attributeBean
operator|.
name|addAttributeValue
argument_list|(
literal|"authenticated"
argument_list|)
expr_stmt|;
return|return
name|attributeBean
return|;
block|}
comment|/**      * Handle ActAs or OnBehalfOf elements.      */
specifier|private
name|AttributeBean
name|handleAdditionalParameters
parameter_list|(
name|boolean
name|actAs
parameter_list|,
name|Object
name|parameter
parameter_list|,
name|String
name|tokenType
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|AttributeBean
name|parameterBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
name|String
name|claimType
init|=
name|actAs
condition|?
literal|"CustomActAs"
else|:
literal|"CustomOnBehalfOf"
decl_stmt|;
if|if
condition|(
name|WSS4JConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSS4JConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
name|parameterBean
operator|.
name|setQualifiedName
argument_list|(
name|claimType
argument_list|)
expr_stmt|;
name|parameterBean
operator|.
name|setNameFormat
argument_list|(
literal|"http://cxf.apache.org/sts/custom/"
operator|+
name|claimType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parameterBean
operator|.
name|setSimpleName
argument_list|(
name|claimType
argument_list|)
expr_stmt|;
name|parameterBean
operator|.
name|setQualifiedName
argument_list|(
literal|"http://cxf.apache.org/sts/custom/"
operator|+
name|claimType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameter
operator|instanceof
name|UsernameTokenType
condition|)
block|{
name|parameterBean
operator|.
name|addAttributeValue
argument_list|(
operator|(
operator|(
name|UsernameTokenType
operator|)
name|parameter
operator|)
operator|.
name|getUsername
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parameter
operator|instanceof
name|Element
condition|)
block|{
name|SamlAssertionWrapper
name|wrapper
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
operator|(
name|Element
operator|)
name|parameter
argument_list|)
decl_stmt|;
name|SAMLTokenPrincipal
name|principal
init|=
operator|new
name|SAMLTokenPrincipalImpl
argument_list|(
name|wrapper
argument_list|)
decl_stmt|;
name|parameterBean
operator|.
name|addAttributeValue
argument_list|(
name|principal
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|parameterBean
return|;
block|}
comment|/**      * Create an Attribute from a claim.      */
specifier|private
name|AttributeBean
name|createAttributeFromClaim
parameter_list|(
name|ProcessedClaim
name|claim
parameter_list|,
name|String
name|tokenType
parameter_list|)
block|{
name|AttributeBean
name|attributeBean
init|=
operator|new
name|AttributeBean
argument_list|()
decl_stmt|;
if|if
condition|(
name|WSS4JConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
operator|||
name|WSS4JConstants
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
name|claim
operator|.
name|getClaimType
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
name|claim
operator|.
name|getClaimType
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
name|claim
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|attributeBean
return|;
block|}
block|}
end_class

end_unit

