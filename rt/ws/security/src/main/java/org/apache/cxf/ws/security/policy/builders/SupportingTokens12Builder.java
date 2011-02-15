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
operator|.
name|SupportTokenType
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
name|AlgorithmSuite
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
name|SignedEncryptedElements
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
name|SignedEncryptedParts
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
name|SupportingToken
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
name|Token
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

begin_class
specifier|public
class|class
name|SupportingTokens12Builder
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
name|SP12Constants
operator|.
name|SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|ENCRYPTED_SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
block|,
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
block|}
decl_stmt|;
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|SupportingTokens12Builder
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
block|{
name|QName
name|name
init|=
name|DOMUtils
operator|.
name|getElementQName
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|SupportingToken
name|supportingToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|SP12Constants
operator|.
name|SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_SUPPORTING
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_SIGNED
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_ENDORSING
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_SIGNED_ENDORSING
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|ENCRYPTED_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_ENCRYPTED
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|SIGNED_ENCRYPTED_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_SIGNED_ENCRYPTED
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_ENDORSING_ENCRYPTED
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|SIGNED_ENDORSING_ENCRYPTED_SUPPORTING_TOKENS
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|supportingToken
operator|=
operator|new
name|SupportingToken
argument_list|(
name|SupportTokenType
operator|.
name|SUPPORTING_TOKEN_SIGNED_ENDORSING_ENCRYPTED
argument_list|,
name|SP12Constants
operator|.
name|INSTANCE
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
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
name|element
argument_list|)
argument_list|)
decl_stmt|;
name|policy
operator|=
operator|(
name|Policy
operator|)
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
for|for
control|(
name|Iterator
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
operator|(
name|List
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|,
name|supportingToken
argument_list|)
expr_stmt|;
comment|/*              * for the moment we will say there should be only one alternative               */
break|break;
block|}
return|return
name|supportingToken
return|;
block|}
specifier|private
name|void
name|processAlternative
parameter_list|(
name|List
name|assertions
parameter_list|,
name|SupportingToken
name|supportingToken
parameter_list|)
block|{
for|for
control|(
name|Iterator
name|iterator
init|=
name|assertions
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Assertion
name|primitive
init|=
operator|(
name|Assertion
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|qname
init|=
name|primitive
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|SP12Constants
operator|.
name|ALGORITHM_SUITE
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|supportingToken
operator|.
name|setAlgorithmSuite
argument_list|(
operator|(
name|AlgorithmSuite
operator|)
name|primitive
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|supportingToken
operator|.
name|setSignedParts
argument_list|(
operator|(
name|SignedEncryptedParts
operator|)
name|primitive
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|supportingToken
operator|.
name|setSignedElements
argument_list|(
operator|(
name|SignedEncryptedElements
operator|)
name|primitive
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|supportingToken
operator|.
name|setEncryptedParts
argument_list|(
operator|(
name|SignedEncryptedParts
operator|)
name|primitive
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
operator|.
name|equals
argument_list|(
name|qname
argument_list|)
condition|)
block|{
name|supportingToken
operator|.
name|setEncryptedElements
argument_list|(
operator|(
name|SignedEncryptedElements
operator|)
name|primitive
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|primitive
operator|instanceof
name|Token
condition|)
block|{
name|supportingToken
operator|.
name|addToken
argument_list|(
operator|(
name|Token
operator|)
name|primitive
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

