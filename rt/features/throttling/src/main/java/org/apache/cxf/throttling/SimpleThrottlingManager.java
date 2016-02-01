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
name|throttling
package|;
end_package

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
name|phase
operator|.
name|Phase
import|;
end_import

begin_comment
comment|/**  * Suspends or aborts the requests if the threshold has been reached  */
end_comment

begin_class
specifier|public
class|class
name|SimpleThrottlingManager
extends|extends
name|ThrottleResponse
implements|implements
name|ThrottlingManager
block|{
specifier|private
specifier|static
specifier|final
name|String
name|THROTTLED_KEY
init|=
literal|"THROTTLED"
decl_stmt|;
specifier|private
name|int
name|threshold
decl_stmt|;
specifier|private
name|ThrottlingCounter
name|counter
init|=
operator|new
name|ThrottlingCounter
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getDecisionPhases
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ThrottleResponse
name|getThrottleResponse
parameter_list|(
name|String
name|phase
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|.
name|containsKey
argument_list|(
name|THROTTLED_KEY
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|ThrottlingCounter
operator|.
name|class
argument_list|,
name|counter
argument_list|)
expr_stmt|;
if|if
condition|(
name|counter
operator|.
name|incrementAndGet
argument_list|()
operator|>=
name|threshold
condition|)
block|{
name|m
operator|.
name|put
argument_list|(
name|THROTTLED_KEY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|int
name|getThreshold
parameter_list|()
block|{
return|return
name|threshold
return|;
block|}
specifier|public
name|void
name|setThreshold
parameter_list|(
name|int
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
block|}
end_class

end_unit

