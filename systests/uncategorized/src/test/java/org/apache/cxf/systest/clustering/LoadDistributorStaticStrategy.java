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
name|systest
operator|.
name|clustering
package|;
end_package

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
name|clustering
operator|.
name|SequentialStrategy
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
name|endpoint
operator|.
name|Endpoint
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  *  * @author jtalbut  */
end_comment

begin_class
specifier|public
class|class
name|LoadDistributorStaticStrategy
extends|extends
name|SequentialStrategy
block|{
specifier|private
name|int
name|index
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|>
name|getAlternateEndpoints
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// Get the list of endpoints, including the current one.
comment|// This part is required for most FailoverStrategys that provide alternate
comment|// target endpoints for the LoadDistributorTargetSelector.
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternateEndpoints
init|=
name|getEndpoints
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|alternateEndpoints
return|;
block|}
annotation|@
name|Override
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getNextAlternate
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|alternates
parameter_list|)
block|{
comment|// Iterate through the list of endpoints even though that list is
comment|// regenerated before each call.
comment|// FailoverStrategys that provide alternate target endpoints must either
comment|// maintain state like this or choose a random entry.
return|return
name|alternates
operator|.
name|remove
argument_list|(
name|index
operator|++
operator|%
name|alternates
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

