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
name|selector
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|ws
operator|.
name|policy
operator|.
name|Assertor
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
name|PolicyEngine
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
name|Policy
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|FirstAlternativeSelector
extends|extends
name|BaseAlternativeSelector
block|{
specifier|public
name|Collection
argument_list|<
name|Assertion
argument_list|>
name|selectAlternative
parameter_list|(
name|Policy
name|policy
parameter_list|,
name|PolicyEngine
name|engine
parameter_list|,
name|Assertor
name|assertor
parameter_list|,
name|List
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
name|request
parameter_list|,
name|Message
name|msg
parameter_list|)
block|{
name|Iterator
argument_list|<
name|List
argument_list|<
name|Assertion
argument_list|>
argument_list|>
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
name|Assertion
argument_list|>
name|alternative
init|=
name|alternatives
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|alternative
argument_list|,
name|assertor
argument_list|,
name|msg
argument_list|)
operator|&&
name|this
operator|.
name|isCompatibleWithRequest
argument_list|(
name|alternative
argument_list|,
name|request
argument_list|)
condition|)
block|{
return|return
name|alternative
return|;
block|}
block|}
name|alternatives
operator|=
name|policy
operator|.
name|getAlternatives
argument_list|()
expr_stmt|;
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
name|Assertion
argument_list|>
name|alternative
init|=
name|alternatives
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|engine
operator|.
name|supportsAlternative
argument_list|(
name|alternative
argument_list|,
name|assertor
argument_list|,
name|msg
argument_list|)
condition|)
block|{
return|return
name|alternative
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

