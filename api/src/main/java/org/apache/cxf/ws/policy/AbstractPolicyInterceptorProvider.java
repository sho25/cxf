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
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|AbstractAttributedInterceptorProvider
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
name|interceptor
operator|.
name|Interceptor
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractPolicyInterceptorProvider
extends|extends
name|AbstractAttributedInterceptorProvider
implements|implements
name|PolicyInterceptorProvider
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7076292509741199877L
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|QName
argument_list|>
name|assertionTypes
decl_stmt|;
specifier|public
name|AbstractPolicyInterceptorProvider
parameter_list|(
name|QName
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractPolicyInterceptorProvider
parameter_list|(
name|Collection
argument_list|<
name|QName
argument_list|>
name|at
parameter_list|)
block|{
name|assertionTypes
operator|=
name|at
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|QName
argument_list|>
name|getAssertionTypes
parameter_list|()
block|{
return|return
name|assertionTypes
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|provideInFaultInterceptors
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|getInFaultInterceptors
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|provideInInterceptors
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|getInInterceptors
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|provideOutFaultInterceptors
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|getOutFaultInterceptors
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|provideOutInterceptors
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|getOutInterceptors
argument_list|()
return|;
block|}
block|}
end_class

end_unit

