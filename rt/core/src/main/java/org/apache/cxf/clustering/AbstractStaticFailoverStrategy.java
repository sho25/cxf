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
name|clustering
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
name|List
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
name|Level
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_comment
comment|/**  * Failover strategy based on a static cluster represented by  * multiple endpoints associated with the same service instance.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractStaticFailoverStrategy
implements|implements
name|FailoverStrategy
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
name|AbstractStaticFailoverStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Get the alternate endpoints for this invocation.      *       * @param exchange the current Exchange      * @return a List of alternate endpoints if available      */
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
name|Endpoint
name|endpoint
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|ServiceInfo
argument_list|>
name|services
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
decl_stmt|;
name|QName
name|currentBinding
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|getBindingInfo
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternates
init|=
operator|new
name|ArrayList
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceInfo
name|service
range|:
name|services
control|)
block|{
name|Collection
argument_list|<
name|EndpointInfo
argument_list|>
name|candidates
init|=
name|service
operator|.
name|getEndpoints
argument_list|()
decl_stmt|;
for|for
control|(
name|EndpointInfo
name|candidate
range|:
name|candidates
control|)
block|{
name|QName
name|candidateBinding
init|=
name|candidate
operator|.
name|getBinding
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|candidateBinding
operator|.
name|equals
argument_list|(
name|currentBinding
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|candidate
operator|.
name|getAddress
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
condition|)
block|{
name|Endpoint
name|alternate
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getEndpoints
argument_list|()
operator|.
name|get
argument_list|(
name|candidate
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|alternate
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"FAILOVER_CANDIDATE_ACCEPTED"
argument_list|,
name|candidate
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|alternates
operator|.
name|add
argument_list|(
name|alternate
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"FAILOVER_CANDIDATE_REJECTED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|candidate
operator|.
name|getName
argument_list|()
block|,
name|candidateBinding
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|alternates
return|;
block|}
comment|/**      * Select one of the alternate endpoints for a retried invocation.      *       * @param a List of alternate endpoints if available      * @return the selected endpoint      */
specifier|public
name|Endpoint
name|selectAlternateEndpoint
parameter_list|(
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternates
parameter_list|)
block|{
name|Endpoint
name|selected
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alternates
operator|!=
literal|null
operator|&&
name|alternates
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|selected
operator|=
name|getNextAlternate
argument_list|(
name|alternates
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"FAILING_OVER_TO"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|selected
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
block|,
name|selected
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"NO_ALTERNATE_TARGETS_REMAIN"
argument_list|)
expr_stmt|;
block|}
return|return
name|selected
return|;
block|}
comment|/**      * Get next alternate endpoint.      *       * @param alternates non-empty List of alternate endpoints       * @return      */
specifier|protected
specifier|abstract
name|Endpoint
name|getNextAlternate
parameter_list|(
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternates
parameter_list|)
function_decl|;
block|}
end_class

end_unit

