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
name|UsernameToken
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
name|UsernameTokenBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|KNOWN_ELEMENTS
index|[]
init|=
block|{
name|SP11Constants
operator|.
name|USERNAME_TOKEN
block|,
name|SP12Constants
operator|.
name|USERNAME_TOKEN
block|}
decl_stmt|;
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|UsernameTokenBuilder
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
name|UsernameToken
name|usernameToken
init|=
operator|new
name|UsernameToken
argument_list|(
name|consts
argument_list|)
decl_stmt|;
name|usernameToken
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
name|usernameToken
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
name|usernameToken
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
name|polEl
init|=
name|PolicyConstants
operator|.
name|findPolicyElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|polEl
operator|!=
literal|null
condition|)
block|{
name|NodeList
name|children
init|=
name|polEl
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Element
condition|)
block|{
name|child
operator|=
operator|(
name|Element
operator|)
name|child
expr_stmt|;
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|SPConstants
operator|.
name|USERNAME_TOKEN10
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|usernameToken
operator|.
name|setUseUTProfile10
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
name|USERNAME_TOKEN11
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|usernameToken
operator|.
name|setUseUTProfile11
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|NO_PASSWORD
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|usernameToken
operator|.
name|setNoPassword
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|HASH_PASSWORD
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|usernameToken
operator|.
name|setHashPassword
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|REQUIRE_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|usernameToken
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
name|SP12Constants
operator|.
name|REQUIRE_EXPLICIT_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|usernameToken
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
name|SP12Constants
operator|.
name|REQUIRE_IMPLIED_DERIVED_KEYS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|usernameToken
operator|.
name|setImpliedDerivedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|usernameToken
return|;
block|}
specifier|public
name|QName
index|[]
name|getKnownElements
parameter_list|()
block|{
return|return
name|KNOWN_ELEMENTS
return|;
block|}
block|}
end_class

end_unit

