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
name|helpers
operator|.
name|DOMUtils
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
name|AssertionWrapper
import|;
end_import

begin_comment
comment|/**  * Validate a WS-SecurityPolicy Claims policy for the   * "http://schemas.xmlsoap.org/ws/2005/05/identity" namespace.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultClaimsPolicyValidator
implements|implements
name|ClaimsPolicyValidator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_CLAIMS_NAMESPACE
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
decl_stmt|;
comment|/**      * Validate a particular Claims policy against a received SAML Assertion.       * Return true if the policy is valid.      */
specifier|public
name|boolean
name|validatePolicy
parameter_list|(
name|Element
name|claimsPolicy
parameter_list|,
name|AssertionWrapper
name|assertion
parameter_list|)
block|{
if|if
condition|(
name|claimsPolicy
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|dialect
init|=
name|claimsPolicy
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Dialect"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|DEFAULT_CLAIMS_NAMESPACE
operator|.
name|equals
argument_list|(
name|dialect
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Element
name|claimType
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|claimsPolicy
argument_list|)
decl_stmt|;
while|while
condition|(
name|claimType
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"ClaimType"
operator|.
name|equals
argument_list|(
name|claimType
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|claimTypeUri
init|=
name|claimType
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Uri"
argument_list|)
decl_stmt|;
name|String
name|claimTypeOptional
init|=
name|claimType
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Optional"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
literal|""
operator|.
name|equals
argument_list|(
name|claimTypeOptional
argument_list|)
operator|||
operator|!
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|claimTypeOptional
argument_list|)
operator|)
operator|&&
operator|!
name|findClaimInAssertion
argument_list|(
name|assertion
argument_list|,
name|URI
operator|.
name|create
argument_list|(
name|claimTypeUri
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|claimType
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|claimType
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Return the dialect that this ClaimsPolicyValidator can parse      */
specifier|public
name|String
name|getDialect
parameter_list|()
block|{
return|return
name|DEFAULT_CLAIMS_NAMESPACE
return|;
block|}
specifier|private
name|boolean
name|findClaimInAssertion
parameter_list|(
name|AssertionWrapper
name|assertion
parameter_list|,
name|URI
name|claimURI
parameter_list|)
block|{
if|if
condition|(
name|assertion
operator|.
name|getSaml1
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|findClaimInAssertion
argument_list|(
name|assertion
operator|.
name|getSaml1
argument_list|()
argument_list|,
name|claimURI
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|findClaimInAssertion
argument_list|(
name|assertion
operator|.
name|getSaml2
argument_list|()
argument_list|,
name|claimURI
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|findClaimInAssertion
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Assertion
name|assertion
parameter_list|,
name|URI
name|claimURI
parameter_list|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AttributeStatement
name|statement
range|:
name|attributeStatements
control|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
argument_list|>
name|attributes
init|=
name|statement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Attribute
name|attribute
range|:
name|attributes
control|)
block|{
name|URI
name|attributeNamespace
init|=
name|URI
operator|.
name|create
argument_list|(
name|attribute
operator|.
name|getNameFormat
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|desiredRole
init|=
name|attributeNamespace
operator|.
name|relativize
argument_list|(
name|claimURI
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|desiredRole
argument_list|)
operator|&&
name|attribute
operator|.
name|getAttributeValues
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|attribute
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|findClaimInAssertion
parameter_list|(
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Assertion
name|assertion
parameter_list|,
name|URI
name|claimURI
parameter_list|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|AttributeStatement
name|statement
range|:
name|attributeStatements
control|)
block|{
name|List
argument_list|<
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Attribute
argument_list|>
name|attributes
init|=
name|statement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|opensaml
operator|.
name|saml1
operator|.
name|core
operator|.
name|Attribute
name|attribute
range|:
name|attributes
control|)
block|{
name|URI
name|attributeNamespace
init|=
name|URI
operator|.
name|create
argument_list|(
name|attribute
operator|.
name|getAttributeNamespace
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|desiredRole
init|=
name|attributeNamespace
operator|.
name|relativize
argument_list|(
name|claimURI
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|getAttributeName
argument_list|()
operator|.
name|equals
argument_list|(
name|desiredRole
argument_list|)
operator|&&
name|attribute
operator|.
name|getAttributeValues
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|attribute
operator|.
name|getAttributeValues
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

