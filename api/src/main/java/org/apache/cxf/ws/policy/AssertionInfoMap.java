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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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

begin_class
specifier|public
class|class
name|AssertionInfoMap
extends|extends
name|HashMap
argument_list|<
name|QName
argument_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
argument_list|>
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
name|AssertionInfoMap
operator|.
name|class
argument_list|,
literal|"APIMessages"
argument_list|)
decl_stmt|;
specifier|public
name|AssertionInfoMap
parameter_list|(
name|Policy
name|p
parameter_list|)
block|{
name|this
argument_list|(
name|getAssertions
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AssertionInfoMap
parameter_list|(
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
parameter_list|)
block|{
name|super
argument_list|(
name|assertions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PolicyAssertion
name|a
range|:
name|assertions
control|)
block|{
name|putAssertionInfo
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|putAssertionInfo
parameter_list|(
name|PolicyAssertion
name|a
parameter_list|)
block|{
name|Policy
name|p
init|=
name|a
operator|.
name|getPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PolicyAssertion
name|na
range|:
name|getAssertions
argument_list|(
name|p
argument_list|)
control|)
block|{
name|putAssertionInfo
argument_list|(
name|na
argument_list|)
expr_stmt|;
block|}
block|}
name|AssertionInfo
name|ai
init|=
operator|new
name|AssertionInfo
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ail
init|=
name|get
argument_list|(
name|a
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ail
operator|==
literal|null
condition|)
block|{
name|ail
operator|=
operator|new
name|ArrayList
argument_list|<
name|AssertionInfo
argument_list|>
argument_list|()
expr_stmt|;
name|put
argument_list|(
name|a
operator|.
name|getName
argument_list|()
argument_list|,
name|ail
argument_list|)
expr_stmt|;
block|}
name|ail
operator|.
name|add
argument_list|(
name|ai
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|getAssertionInfo
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ail
init|=
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|ail
operator|!=
literal|null
condition|?
name|ail
else|:
name|CastUtils
operator|.
name|cast
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|AssertionInfo
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|supportsAlternative
parameter_list|(
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|alternative
parameter_list|)
block|{
for|for
control|(
name|PolicyAssertion
name|a
range|:
name|alternative
control|)
block|{
if|if
condition|(
operator|!
name|a
operator|.
name|isAsserted
argument_list|(
name|this
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
specifier|public
name|void
name|checkEffectivePolicy
parameter_list|(
name|Policy
name|policy
parameter_list|)
block|{
name|Iterator
name|alternatives
init|=
name|policy
operator|.
name|getAlternatives
argument_list|()
decl_stmt|;
while|while
condition|(
name|alternatives
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|PolicyAssertion
argument_list|>
name|alternative
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|alternatives
operator|.
name|next
argument_list|()
argument_list|,
name|PolicyAssertion
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|supportsAlternative
argument_list|(
name|alternative
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_ALTERNATIVE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
specifier|public
name|void
name|check
parameter_list|()
block|{
for|for
control|(
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
range|:
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
if|if
condition|(
operator|!
name|ai
operator|.
name|isAsserted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
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
argument_list|(
literal|"NOT_ASSERTED_EXC"
argument_list|,
name|BUNDLE
argument_list|,
name|ai
operator|.
name|getAssertion
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|getAssertions
parameter_list|(
name|PolicyOperator
name|p
parameter_list|)
block|{
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
operator|||
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
return|return
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
return|;
block|}
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
operator|new
name|ArrayList
argument_list|<
name|PolicyAssertion
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PolicyComponent
name|pc
range|:
name|pcs
control|)
block|{
name|assertions
operator|.
name|addAll
argument_list|(
name|getAssertions
argument_list|(
operator|(
name|PolicyOperator
operator|)
name|pc
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|assertions
return|;
block|}
block|}
end_class

end_unit

