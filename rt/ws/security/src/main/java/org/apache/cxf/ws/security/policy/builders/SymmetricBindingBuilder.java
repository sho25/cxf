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
name|Layout
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
name|ProtectionToken
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
name|SymmetricBinding
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
name|SymmetricBindingBuilder
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
name|SymmetricBindingBuilder
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
name|SYMMETRIC_BINDING
block|,
name|SP12Constants
operator|.
name|SYMMETRIC_BINDING
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
name|SymmetricBinding
name|symmetricBinding
init|=
operator|new
name|SymmetricBinding
argument_list|(
name|consts
argument_list|,
name|builder
argument_list|)
decl_stmt|;
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
name|processAlternatives
argument_list|(
operator|(
name|List
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|,
name|symmetricBinding
argument_list|,
name|consts
argument_list|)
expr_stmt|;
comment|/*              * since there should be only one alternative ..              */
break|break;
block|}
return|return
name|symmetricBinding
return|;
block|}
specifier|private
name|void
name|processAlternatives
parameter_list|(
name|List
name|assertions
parameter_list|,
name|SymmetricBinding
name|symmetricBinding
parameter_list|,
name|SPConstants
name|consts
parameter_list|)
block|{
name|Assertion
name|assertion
decl_stmt|;
name|QName
name|name
decl_stmt|;
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
name|assertion
operator|=
operator|(
name|Assertion
operator|)
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|name
operator|=
name|assertion
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|consts
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
operator|!
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|SPConstants
operator|.
name|ALGO_SUITE
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
name|symmetricBinding
operator|.
name|setAlgorithmSuite
argument_list|(
operator|(
name|AlgorithmSuite
operator|)
name|assertion
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|LAYOUT
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
name|symmetricBinding
operator|.
name|setLayout
argument_list|(
operator|(
name|Layout
operator|)
name|assertion
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
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
name|symmetricBinding
operator|.
name|setIncludeTimestamp
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
name|PROTECTION_TOKEN
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
name|symmetricBinding
operator|.
name|setProtectionToken
argument_list|(
operator|(
name|ProtectionToken
operator|)
name|assertion
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ENCRYPT_BEFORE_SIGNING
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
name|symmetricBinding
operator|.
name|setProtectionOrder
argument_list|(
name|SPConstants
operator|.
name|ProtectionOrder
operator|.
name|EncryptBeforeSigning
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|SIGN_BEFORE_ENCRYPTING
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
name|symmetricBinding
operator|.
name|setProtectionOrder
argument_list|(
name|SPConstants
operator|.
name|ProtectionOrder
operator|.
name|SignBeforeEncrypting
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SPConstants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
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
name|symmetricBinding
operator|.
name|setEntireHeadersAndBodySignatures
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
name|ENCRYPT_SIGNATURE
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
name|symmetricBinding
operator|.
name|setSignatureProtection
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

