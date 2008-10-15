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
name|Arrays
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
name|policy
operator|.
name|AssertionBuilder
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
name|PolicyAssertion
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
name|SecureConversationToken
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

begin_class
specifier|public
class|class
name|SecureConversationTokenBuilder
implements|implements
name|AssertionBuilder
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|QName
argument_list|>
name|KNOWN_ELEMENTS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|SP11Constants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|)
decl_stmt|;
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|SecureConversationTokenBuilder
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
name|List
argument_list|<
name|QName
argument_list|>
name|getKnownElements
parameter_list|()
block|{
return|return
name|KNOWN_ELEMENTS
return|;
block|}
specifier|public
name|PolicyAssertion
name|build
parameter_list|(
name|Element
name|element
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
name|SecureConversationToken
name|conversationToken
init|=
operator|new
name|SecureConversationToken
argument_list|(
name|consts
argument_list|)
decl_stmt|;
name|String
name|attribute
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
name|attribute
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SecurityContextToken doesn't contain "
operator|+
literal|"any sp:IncludeToken attribute"
argument_list|)
throw|;
block|}
name|String
name|inclusionValue
init|=
name|attribute
operator|.
name|trim
argument_list|()
decl_stmt|;
name|conversationToken
operator|.
name|setInclusion
argument_list|(
name|consts
operator|.
name|getInclusionFromAttributeValue
argument_list|(
name|inclusionValue
argument_list|)
argument_list|)
expr_stmt|;
name|Element
name|elem
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|element
argument_list|)
decl_stmt|;
while|while
condition|(
name|elem
operator|!=
literal|null
condition|)
block|{
name|QName
name|qn
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|elem
argument_list|)
decl_stmt|;
if|if
condition|(
name|PolicyConstants
operator|.
name|isPolicyElem
argument_list|(
name|qn
argument_list|)
condition|)
block|{
if|if
condition|(
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|elem
argument_list|,
name|consts
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_DERIVED_KEYS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|conversationToken
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
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|elem
argument_list|,
name|SP12Constants
operator|.
name|REQUIRE_IMPLIED_DERIVED_KEYS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|conversationToken
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
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|elem
argument_list|,
name|SP12Constants
operator|.
name|REQUIRE_EXPLICIT_DERIVED_KEYS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|conversationToken
operator|.
name|setExplicitDerivedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|elem
argument_list|,
name|consts
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EXTERNAL_URI_REFERENCE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|conversationToken
operator|.
name|setRequireExternalUriRef
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|elem
argument_list|,
name|consts
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|SC10_SECURITY_CONTEXT_TOKEN
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|conversationToken
operator|.
name|setSc10SecurityContextToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|Element
name|bootstrapPolicyElement
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|elem
argument_list|,
name|consts
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|BOOTSTRAP_POLICY
argument_list|)
decl_stmt|;
if|if
condition|(
name|bootstrapPolicyElement
operator|!=
literal|null
condition|)
block|{
name|Policy
name|policy
init|=
name|builder
operator|.
name|getPolicy
argument_list|(
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|bootstrapPolicyElement
argument_list|)
argument_list|)
decl_stmt|;
name|conversationToken
operator|.
name|setBootstrapPolicy
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|consts
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|SPConstants
operator|.
name|ISSUER
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|conversationToken
operator|.
name|setIssuerEpr
argument_list|(
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|elem
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|elem
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
return|return
name|conversationToken
return|;
block|}
specifier|public
name|PolicyAssertion
name|buildCompatible
parameter_list|(
name|PolicyAssertion
name|a
parameter_list|,
name|PolicyAssertion
name|b
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

