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
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|NodeList
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
name|SamlToken
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
name|builders
operator|.
name|AssertionBuilder
import|;
end_import

begin_class
specifier|public
class|class
name|SamlTokenBuilder
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
name|SamlTokenBuilder
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
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
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
name|SamlToken
name|samlToken
init|=
operator|new
name|SamlToken
argument_list|(
name|consts
argument_list|)
decl_stmt|;
name|samlToken
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
name|samlToken
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
name|attribute
init|=
name|element
operator|.
name|getAttributeNS
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|ATTR_INCLUDE_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|samlToken
operator|.
name|setInclusion
argument_list|(
name|consts
operator|.
name|getInclusionFromAttributeValue
argument_list|(
name|attribute
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
name|samlToken
operator|.
name|setPolicy
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|NodeList
name|policyChildren
init|=
name|child
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|policyChildren
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|policyChildren
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|policyChild
init|=
name|policyChildren
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|policyChild
operator|instanceof
name|Element
condition|)
block|{
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
name|policyChild
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|policyChild
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|localname
init|=
name|qname
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|SPConstants
operator|.
name|SAML_11_TOKEN_10
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
operator|.
name|setUseSamlVersion11Profile10
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
name|SAML_11_TOKEN_11
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
operator|.
name|setUseSamlVersion11Profile11
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
name|SAML_20_TOKEN_11
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
operator|.
name|setUseSamlVersion20Profile11
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
name|REQUIRE_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
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
name|REQUIRE_EXPLICIT_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
operator|.
name|setExplicitDerivedKeys
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
name|REQUIRE_IMPLIED_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
operator|.
name|setImpliedDerivedKeys
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
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
operator|.
name|equals
argument_list|(
name|localname
argument_list|)
condition|)
block|{
name|samlToken
operator|.
name|setRequireKeyIdentifierReference
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
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
literal|"sp:SpnegoContextToken/wsp:Policy must have a value"
argument_list|)
throw|;
block|}
return|return
name|samlToken
return|;
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
name|SAML_TOKEN
block|,
name|SP12Constants
operator|.
name|SAML_TOKEN
block|}
return|;
block|}
block|}
end_class

end_unit

