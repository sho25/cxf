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
name|addressing
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|Attr
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|policy
operator|.
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertion
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
name|PolicyContainingPrimitiveAssertion
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
name|xml
operator|.
name|XMLPrimitiveAssertionBuilder
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|AddressingAssertionBuilder
implements|implements
name|AssertionBuilder
argument_list|<
name|Element
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AddressingAssertionBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
index|[]
name|KNOWN_ELEMENTS
init|=
block|{
name|MetadataConstants
operator|.
name|ADDRESSING_ASSERTION_QNAME
block|,
name|MetadataConstants
operator|.
name|ANON_RESPONSES_ASSERTION_QNAME
block|,
name|MetadataConstants
operator|.
name|NON_ANON_RESPONSES_ASSERTION_QNAME
block|,
name|MetadataConstants
operator|.
name|ADDRESSING_ASSERTION_QNAME_0705
block|,
name|MetadataConstants
operator|.
name|ANON_RESPONSES_ASSERTION_QNAME_0705
block|,
name|MetadataConstants
operator|.
name|NON_ANON_RESPONSES_ASSERTION_QNAME_0705
block|}
decl_stmt|;
specifier|public
name|AddressingAssertionBuilder
parameter_list|()
block|{     }
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|elem
parameter_list|,
name|AssertionBuilderFactory
name|factory
parameter_list|)
block|{
name|String
name|localName
init|=
name|elem
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|QName
name|qn
init|=
operator|new
name|QName
argument_list|(
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|localName
argument_list|)
decl_stmt|;
name|boolean
name|optional
init|=
literal|false
decl_stmt|;
name|Attr
name|attribute
init|=
name|PolicyConstants
operator|.
name|findOptionalAttribute
argument_list|(
name|elem
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|optional
operator|=
name|Boolean
operator|.
name|valueOf
argument_list|(
name|attribute
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|MetadataConstants
operator|.
name|ADDRESSING_ASSERTION_QNAME
operator|.
name|equals
argument_list|(
name|qn
argument_list|)
operator|||
name|MetadataConstants
operator|.
name|ADDRESSING_ASSERTION_QNAME_0705
operator|.
name|equals
argument_list|(
name|qn
argument_list|)
condition|)
block|{
name|Assertion
name|nap
init|=
operator|new
name|XMLPrimitiveAssertionBuilder
argument_list|()
block|{
specifier|public
name|Assertion
name|newPrimitiveAssertion
parameter_list|(
name|Element
name|element
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|mp
parameter_list|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|ADDRESSING_ASSERTION_QNAME
argument_list|,
name|isOptional
argument_list|(
name|element
argument_list|)
argument_list|,
name|isIgnorable
argument_list|(
name|element
argument_list|)
argument_list|,
name|mp
argument_list|)
return|;
block|}
specifier|public
name|Assertion
name|newPolicyContainingAssertion
parameter_list|(
name|Element
name|element
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|mp
parameter_list|,
name|Policy
name|policy
parameter_list|)
block|{
return|return
operator|new
name|PolicyContainingPrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|ADDRESSING_ASSERTION_QNAME
argument_list|,
name|isOptional
argument_list|(
name|element
argument_list|)
argument_list|,
name|isIgnorable
argument_list|(
name|element
argument_list|)
argument_list|,
name|mp
argument_list|,
name|policy
argument_list|)
return|;
block|}
block|}
operator|.
name|build
argument_list|(
name|elem
argument_list|,
name|factory
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|nap
operator|instanceof
name|PolicyContainingPrimitiveAssertion
operator|||
name|nap
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|PrimitiveAssertion
operator|)
condition|)
block|{
comment|// this happens when neethi fails to recognize the specified addressing policy element
name|LOG
operator|.
name|warning
argument_list|(
literal|"Unable to recognize the addressing policy"
argument_list|)
expr_stmt|;
block|}
return|return
name|nap
return|;
block|}
elseif|else
if|if
condition|(
name|MetadataConstants
operator|.
name|ANON_RESPONSES_ASSERTION_QNAME
operator|.
name|equals
argument_list|(
name|qn
argument_list|)
operator|||
name|MetadataConstants
operator|.
name|ANON_RESPONSES_ASSERTION_QNAME_0705
operator|.
name|equals
argument_list|(
name|qn
argument_list|)
condition|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|ANON_RESPONSES_ASSERTION_QNAME
argument_list|,
name|optional
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|MetadataConstants
operator|.
name|NON_ANON_RESPONSES_ASSERTION_QNAME
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
operator|||
name|MetadataConstants
operator|.
name|NON_ANON_RESPONSES_ASSERTION_QNAME_0705
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
return|return
operator|new
name|PrimitiveAssertion
argument_list|(
name|MetadataConstants
operator|.
name|NON_ANON_RESPONSES_ASSERTION_QNAME
argument_list|,
name|optional
argument_list|)
return|;
block|}
return|return
literal|null
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

