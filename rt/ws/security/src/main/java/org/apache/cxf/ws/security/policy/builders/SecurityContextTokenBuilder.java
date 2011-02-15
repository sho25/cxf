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
name|SecurityContextToken
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

begin_class
specifier|public
class|class
name|SecurityContextTokenBuilder
implements|implements
name|AssertionBuilder
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
name|SECURITY_CONTEXT_TOKEN
block|,
name|SP12Constants
operator|.
name|SECURITY_CONTEXT_TOKEN
block|}
decl_stmt|;
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
name|SecurityContextToken
name|contextToken
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|consts
argument_list|)
decl_stmt|;
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
name|contextToken
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
name|element
operator|=
name|PolicyConstants
operator|.
name|findPolicyElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
name|element
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
name|contextToken
operator|.
name|setDerivedKeys
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
name|element
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
name|contextToken
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
name|element
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
name|contextToken
operator|.
name|setSc10SecurityContextToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|contextToken
return|;
block|}
block|}
end_class

end_unit

