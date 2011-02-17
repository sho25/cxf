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
name|policy
operator|.
name|builder
operator|.
name|primitive
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|i18n
operator|.
name|Message
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
name|CastUtils
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
name|AssertionInfo
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
name|AssertionInfoMap
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
name|PolicyException
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
name|Constants
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
name|PolicyComponent
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
name|PolicyOperator
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
comment|/**  * Implementation of an assertion that required exactly one (possibly empty) child element  * of type Policy (as does for examples the wsam:Addressing assertion).  *   */
end_comment

begin_class
specifier|public
class|class
name|NestedPrimitiveAssertion
extends|extends
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|builders
operator|.
name|PolicyContainingPrimitiveAssertion
implements|implements
name|PolicyAssertion
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|NestedPrimitiveAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|assertionRequired
init|=
literal|true
decl_stmt|;
specifier|private
name|PolicyBuilder
name|builder
decl_stmt|;
specifier|public
name|NestedPrimitiveAssertion
parameter_list|(
name|QName
name|name
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|optional
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NestedPrimitiveAssertion
parameter_list|(
name|QName
name|name
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|boolean
name|ignorable
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|optional
argument_list|,
name|ignorable
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NestedPrimitiveAssertion
parameter_list|(
name|QName
name|name
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|Policy
name|p
parameter_list|,
name|boolean
name|assertionRequired
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|optional
argument_list|,
literal|false
argument_list|,
name|p
argument_list|,
name|assertionRequired
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NestedPrimitiveAssertion
parameter_list|(
name|QName
name|name
parameter_list|,
name|boolean
name|optional
parameter_list|,
name|boolean
name|ignorable
parameter_list|,
name|Policy
name|p
parameter_list|,
name|boolean
name|assertionRequired
parameter_list|,
name|PolicyBuilder
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|optional
argument_list|,
name|ignorable
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|this
operator|.
name|assertionRequired
operator|=
name|assertionRequired
expr_stmt|;
name|builder
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|NestedPrimitiveAssertion
parameter_list|(
name|Element
name|elem
parameter_list|,
name|PolicyBuilder
name|builder
parameter_list|)
block|{
name|this
argument_list|(
name|elem
argument_list|,
name|builder
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NestedPrimitiveAssertion
parameter_list|(
name|Element
name|elem
parameter_list|,
name|PolicyBuilder
name|builder
parameter_list|,
name|boolean
name|assertionRequired
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|QName
argument_list|(
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|XMLPrimitiveAssertionBuilder
operator|.
name|isOptional
argument_list|(
name|elem
argument_list|)
argument_list|,
name|XMLPrimitiveAssertionBuilder
operator|.
name|isIgnorable
argument_list|(
name|elem
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|builder
expr_stmt|;
name|this
operator|.
name|assertionRequired
operator|=
name|assertionRequired
expr_stmt|;
comment|// expect exactly one child element of type Policy
name|Element
name|policyElem
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Node
name|nd
init|=
name|elem
operator|.
name|getFirstChild
argument_list|()
init|;
name|nd
operator|!=
literal|null
condition|;
name|nd
operator|=
name|nd
operator|.
name|getNextSibling
argument_list|()
control|)
block|{
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|==
name|nd
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
name|QName
name|qn
init|=
operator|new
name|QName
argument_list|(
name|nd
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|nd
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Constants
operator|.
name|isPolicyElement
argument_list|(
name|qn
argument_list|)
operator|&&
literal|null
operator|==
name|policyElem
condition|)
block|{
name|policyElem
operator|=
operator|(
name|Element
operator|)
name|nd
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"UNEXPECTED_CHILD_ELEMENT_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
literal|null
operator|==
name|policyElem
condition|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"UNEXPECTED_CHILD_ELEMENT_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY
argument_list|)
argument_list|)
throw|;
block|}
name|nested
operator|=
name|builder
operator|.
name|getPolicy
argument_list|(
name|policyElem
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Assertion
name|clone
parameter_list|(
name|boolean
name|opt
parameter_list|,
name|Policy
name|n
parameter_list|)
block|{
return|return
operator|new
name|NestedPrimitiveAssertion
argument_list|(
name|name
argument_list|,
name|opt
argument_list|,
name|ignorable
argument_list|,
name|n
argument_list|,
name|assertionRequired
argument_list|,
name|builder
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|equal
parameter_list|(
name|PolicyComponent
name|policyComponent
parameter_list|)
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|equal
argument_list|(
name|policyComponent
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|NestedPrimitiveAssertion
name|other
init|=
operator|(
name|NestedPrimitiveAssertion
operator|)
name|policyComponent
decl_stmt|;
return|return
name|getPolicy
argument_list|()
operator|.
name|equal
argument_list|(
name|other
operator|.
name|getPolicy
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isAsserted
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
if|if
condition|(
name|assertionRequired
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ail
init|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ail
control|)
block|{
if|if
condition|(
name|ai
operator|.
name|isAsserted
argument_list|()
operator|&&
name|isPolicyAsserted
argument_list|(
name|nested
argument_list|,
name|aim
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
return|return
name|isPolicyAsserted
argument_list|(
name|nested
argument_list|,
name|aim
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isPolicyAsserted
parameter_list|(
name|PolicyOperator
name|p
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
argument_list|<
name|PolicyComponent
argument_list|>
name|pcs
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|p
operator|.
name|getPolicyComponents
argument_list|()
argument_list|,
name|PolicyComponent
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|pcs
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|pcs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|PolicyAssertion
condition|)
block|{
name|List
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pcs
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|PolicyAssertion
name|pa
range|:
name|assertions
control|)
block|{
if|if
condition|(
operator|!
name|pa
operator|.
name|isAsserted
argument_list|(
name|aim
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|PolicyOperator
argument_list|>
name|assertions
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|pcs
argument_list|,
name|PolicyOperator
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|PolicyOperator
name|po
range|:
name|assertions
control|)
block|{
if|if
condition|(
name|isPolicyAsserted
argument_list|(
name|po
argument_list|,
name|aim
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

