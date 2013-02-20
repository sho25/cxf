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
name|policy
operator|.
name|builders
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|addressing
operator|.
name|VersionTransformer
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
name|policy
operator|.
name|PolicyBuilder
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
name|policy
operator|.
name|PolicyConstants
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
name|policy
operator|.
name|SP11Constants
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
name|policy
operator|.
name|SP12Constants
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
name|policy
operator|.
name|SPConstants
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
name|policy
operator|.
name|model
operator|.
name|IssuedToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Assertion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|AssertionBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|AssertionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|IssuedTokenBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|IssuedTokenBuilder
parameter_list|(
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|builder
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
operator|new
name|QName
index|[]
block|{
name|SP11Constants
operator|.
name|ISSUED_TOKEN
block|,
name|SP12Constants
operator|.
name|ISSUED_TOKEN
block|}
return|;
block|}
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|SPConstants
name|consts
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|?
name|SP11Constants
operator|.
name|INSTANCE
else|:
name|SP12Constants
operator|.
name|INSTANCE
decl_stmt|;
name|IssuedToken
name|issuedToken
init|=
operator|new
name|IssuedToken
argument_list|(
name|consts
argument_list|)
decl_stmt|;
name|issuedToken
operator|.
name|setOptional
argument_list|(
name|PolicyConstants
operator|.
name|isOptional
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|issuedToken
operator|.
name|setIgnorable
argument_list|(
name|PolicyConstants
operator|.
name|isIgnorable
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|includeAttr
init|=
name|DOMUtils
operator|.
name|getAttribute
argument_list|(
name|element
argument_list|,
name|consts
operator|.
name|getIncludeToken
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|includeAttr
operator|!=
literal|null
condition|)
block|{
name|issuedToken
operator|.
name|setInclusion
argument_list|(
name|consts
operator|.
name|getInclusionFromAttributeValue
argument_list|(
name|includeAttr
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|boolean
name|foundPolicy
init|=
literal|false
decl_stmt|;
name|boolean
name|foundRST
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|String
name|ln
init|=
name|child
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|SPConstants
operator|.
name|ISSUER
operator|.
name|equals
argument_list|(
name|ln
argument_list|)
condition|)
block|{
try|try
block|{
name|EndpointReferenceType
name|epr
init|=
name|VersionTransformer
operator|.
name|parseEndpointReference
argument_list|(
name|child
argument_list|)
decl_stmt|;
name|issuedToken
operator|.
name|setIssuerEpr
argument_list|(
name|epr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|REQUEST_SECURITY_TOKEN_TEMPLATE
operator|.
name|equals
argument_list|(
name|ln
argument_list|)
condition|)
block|{
name|foundRST
operator|=
literal|true
expr_stmt|;
name|issuedToken
operator|.
name|setRstTemplate
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
operator|.
name|ELEM_POLICY
operator|.
name|equals
argument_list|(
name|ln
argument_list|)
condition|)
block|{
name|foundPolicy
operator|=
literal|true
expr_stmt|;
name|Policy
name|policy
init|=
name|builder
operator|.
name|getPolicy
argument_list|(
name|child
argument_list|)
decl_stmt|;
name|policy
operator|=
name|policy
operator|.
name|normalize
argument_list|(
name|builder
operator|.
name|getPolicyRegistry
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|issuedToken
operator|.
name|setPolicy
argument_list|(
name|child
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|iterator
init|=
name|policy
operator|.
name|getAlternatives
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|processAlternative
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|,
name|issuedToken
argument_list|)
expr_stmt|;
break|break;
comment|// since there should be only one alternative ..
block|}
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ISSUER_NAME
operator|.
name|equals
argument_list|(
name|ln
argument_list|)
condition|)
block|{
name|String
name|issuerName
init|=
name|child
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|issuedToken
operator|.
name|setIssuerName
argument_list|(
name|issuerName
argument_list|)
expr_stmt|;
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|foundPolicy
operator|&&
name|consts
operator|!=
name|SP11Constants
operator|.
name|INSTANCE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"sp:IssuedToken/wsp:Policy must have a value"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|foundRST
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"sp:IssuedToken/sp:RequestSecurityTokenTemplate must have a value"
argument_list|)
throw|;
block|}
return|return
name|issuedToken
return|;
block|}
specifier|private
name|void
name|processAlternative
parameter_list|(
name|List
argument_list|<
name|Assertion
argument_list|>
name|assertions
parameter_list|,
name|IssuedToken
name|parent
parameter_list|)
block|{
name|QName
name|name
decl_stmt|;
for|for
control|(
name|Assertion
name|assertion
range|:
name|assertions
control|)
block|{
name|name
operator|=
name|assertion
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
name|SPConstants
operator|.
name|REQUIRE_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setDerivedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|REQUIRE_EXTERNAL_REFERENCE
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setRequireExternalReference
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|REQUIRE_INTERNAL_REFERENCE
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|parent
operator|.
name|setRequireInternalReference
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

