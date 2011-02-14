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
name|model
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
name|neethi
operator|.
name|PolicyComponent
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSecurityAssertion
implements|implements
name|PolicyAssertion
block|{
specifier|protected
specifier|final
name|SPConstants
name|constants
decl_stmt|;
specifier|private
name|boolean
name|isOptional
decl_stmt|;
specifier|private
name|boolean
name|ignorable
decl_stmt|;
specifier|private
name|boolean
name|normalized
decl_stmt|;
specifier|public
name|AbstractSecurityAssertion
parameter_list|(
name|SPConstants
name|version
parameter_list|)
block|{
name|constants
operator|=
name|version
expr_stmt|;
block|}
specifier|public
specifier|final
name|SPConstants
name|getSPConstants
parameter_list|()
block|{
return|return
name|constants
return|;
block|}
specifier|public
name|boolean
name|isOptional
parameter_list|()
block|{
return|return
name|isOptional
return|;
block|}
specifier|public
name|void
name|setOptional
parameter_list|(
name|boolean
name|optional
parameter_list|)
block|{
name|this
operator|.
name|isOptional
operator|=
name|optional
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIgnorable
parameter_list|()
block|{
return|return
name|ignorable
return|;
block|}
specifier|public
name|void
name|setIgnorable
parameter_list|(
name|boolean
name|ignorable
parameter_list|)
block|{
name|this
operator|.
name|ignorable
operator|=
name|ignorable
expr_stmt|;
block|}
specifier|public
name|short
name|getType
parameter_list|()
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
operator|.
name|TYPE_ASSERTION
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
return|return
name|policyComponent
operator|==
name|this
return|;
block|}
specifier|public
name|void
name|setNormalized
parameter_list|(
name|boolean
name|normalized
parameter_list|)
block|{
name|this
operator|.
name|normalized
operator|=
name|normalized
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNormalized
parameter_list|()
block|{
return|return
name|normalized
return|;
block|}
specifier|public
name|PolicyComponent
name|normalize
parameter_list|()
block|{
return|return
name|this
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
name|getName
argument_list|()
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
name|ai
operator|.
name|getAssertion
argument_list|()
operator|==
name|this
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
end_class

end_unit

