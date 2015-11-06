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
name|List
import|;
end_import

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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|clustering
operator|.
name|circuitbreaker
operator|.
name|CircuitBreaker
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
name|circuitbreaker
operator|.
name|ZestCircuitBreaker
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|Client
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|CircuitBreakerTargetSelector
extends|extends
name|FailoverTargetSelector
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_TIMEOUT
init|=
literal|1000
operator|*
literal|60
comment|/* 1 minute timeout as default */
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_THESHOLD
init|=
literal|1
decl_stmt|;
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
name|CircuitBreakerTargetSelector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|int
name|threshold
decl_stmt|;
specifier|private
specifier|final
name|long
name|timeout
decl_stmt|;
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|CircuitBreaker
argument_list|>
name|circuits
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|CircuitBreakerTargetSelector
parameter_list|(
specifier|final
name|int
name|threshold
parameter_list|,
specifier|final
name|long
name|timeout
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
specifier|public
name|CircuitBreakerTargetSelector
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_THESHOLD
argument_list|,
name|DEFAULT_TIMEOUT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|setStrategy
parameter_list|(
name|FailoverStrategy
name|strategy
parameter_list|)
block|{
name|super
operator|.
name|setStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|alternative
range|:
name|strategy
operator|.
name|getAlternateAddresses
argument_list|(
literal|null
comment|/* no Exchange at this point */
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|alternative
argument_list|)
condition|)
block|{
name|circuits
operator|.
name|putIfAbsent
argument_list|(
name|alternative
argument_list|,
operator|new
name|ZestCircuitBreaker
argument_list|(
name|threshold
argument_list|,
name|timeout
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Endpoint
name|getFailoverTarget
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InvocationContext
name|invocation
parameter_list|)
block|{
if|if
condition|(
name|circuits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"No alternative addresses configured"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|alternateAddresses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|CircuitBreaker
argument_list|>
name|entry
range|:
name|circuits
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|allowRequest
argument_list|()
condition|)
block|{
name|alternateAddresses
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Endpoint
name|failoverTarget
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|alternateAddresses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|String
name|alternateAddress
init|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateAddress
argument_list|(
name|alternateAddresses
argument_list|)
decl_stmt|;
comment|// Reuse current endpoint
if|if
condition|(
name|alternateAddress
operator|!=
literal|null
condition|)
block|{
name|failoverTarget
operator|=
name|getEndpoint
argument_list|()
expr_stmt|;
name|failoverTarget
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|alternateAddress
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|failoverTarget
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|super
operator|.
name|prepare
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onFailure
parameter_list|(
name|InvocationContext
name|context
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|context
argument_list|,
name|ex
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|context
operator|.
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestContext
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|address
init|=
operator|(
name|String
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|CircuitBreaker
name|circuitBreaker
init|=
name|circuits
operator|.
name|get
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|circuitBreaker
operator|!=
literal|null
condition|)
block|{
name|circuitBreaker
operator|.
name|markFailure
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onSuccess
parameter_list|(
name|InvocationContext
name|context
parameter_list|)
block|{
name|super
operator|.
name|onSuccess
argument_list|(
name|context
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestContext
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|context
operator|.
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestContext
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|address
init|=
operator|(
name|String
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
specifier|final
name|CircuitBreaker
name|circuitBreaker
init|=
name|circuits
operator|.
name|get
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|circuitBreaker
operator|!=
literal|null
condition|)
block|{
name|circuitBreaker
operator|.
name|markSuccess
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

