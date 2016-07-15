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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|interceptor
operator|.
name|Fault
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
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
name|String
name|IS_SELECTED
init|=
literal|"org.apache.cxf.clustering.CircuitBreakerTargetSelector.IS_SELECTED"
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
comment|/**      * Static instance of empty (or noop) circuit breaker to handle use cases      * when alternative addresses or alternative endpoint addresses are nullable       * (or non-valid).      */
specifier|private
specifier|static
specifier|final
name|CircuitBreaker
name|NOOP_CIRCUIT_BREAKER
init|=
operator|new
name|CircuitBreaker
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|allowRequest
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|markFailure
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{         }
annotation|@
name|Override
specifier|public
name|void
name|markSuccess
parameter_list|()
block|{         }
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|CircuitBreaker
argument_list|>
name|circuits
init|=
operator|new
name|LinkedHashMap
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
parameter_list|(
specifier|final
name|int
name|threshold
parameter_list|,
specifier|final
name|long
name|timeout
parameter_list|,
specifier|final
name|String
name|clientBootstrapAddress
parameter_list|)
block|{
name|super
argument_list|(
name|clientBootstrapAddress
argument_list|)
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
comment|// Registering the original endpoint in the list of circuit breakers
if|if
condition|(
name|getEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|address
init|=
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
condition|)
block|{
name|circuits
operator|.
name|putIfAbsent
argument_list|(
name|address
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
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|alternatives
init|=
name|strategy
operator|.
name|getAlternateAddresses
argument_list|(
literal|null
comment|/* no Exchange at this point */
argument_list|)
decl_stmt|;
if|if
condition|(
name|alternatives
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|alternative
range|:
name|alternatives
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
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|Conduit
name|selectConduit
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Conduit
name|c
init|=
name|message
operator|.
name|get
argument_list|(
name|Conduit
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
return|;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|InvocationKey
name|key
init|=
operator|new
name|InvocationKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|InvocationContext
name|invocation
init|=
name|inProgress
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|invocation
operator|!=
literal|null
operator|&&
operator|!
name|invocation
operator|.
name|getContext
argument_list|()
operator|.
name|containsKey
argument_list|(
name|IS_SELECTED
argument_list|)
condition|)
block|{
specifier|final
name|String
name|address
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
if|if
condition|(
name|isFailoverRequired
argument_list|(
name|address
argument_list|)
condition|)
block|{
name|Endpoint
name|target
init|=
name|getFailoverTarget
argument_list|(
name|exchange
argument_list|,
name|invocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|FailoverFailedException
argument_list|(
literal|"None of alternative addresses are available at the moment"
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|isEndpointChanged
argument_list|(
name|address
argument_list|,
name|target
argument_list|)
condition|)
block|{
name|setEndpoint
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|target
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|overrideAddressProperty
argument_list|(
name|invocation
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|invocation
operator|.
name|getContext
argument_list|()
operator|.
name|put
argument_list|(
name|IS_SELECTED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|getSelectedConduit
argument_list|(
name|message
argument_list|)
return|;
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
name|updateContextAlternatives
argument_list|(
name|exchange
argument_list|,
name|invocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|alternateAddresses
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Iterator
argument_list|<
name|String
argument_list|>
name|alternateAddressIterator
init|=
name|alternateAddresses
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|alternateAddressIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|String
name|alternateAddress
init|=
name|alternateAddressIterator
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|CircuitBreaker
name|circuitBreaker
init|=
name|getCircuitBreaker
argument_list|(
name|alternateAddress
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|circuitBreaker
operator|.
name|allowRequest
argument_list|()
condition|)
block|{
name|alternateAddressIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|Endpoint
name|failoverTarget
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alternateAddresses
operator|!=
literal|null
operator|&&
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
else|else
block|{
specifier|final
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternateEndpoints
init|=
name|invocation
operator|.
name|getAlternateEndpoints
argument_list|()
decl_stmt|;
if|if
condition|(
name|alternateEndpoints
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Iterator
argument_list|<
name|Endpoint
argument_list|>
name|alternateEndpointIterator
init|=
name|alternateEndpoints
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|alternateEndpointIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|Endpoint
name|endpoint
init|=
name|alternateEndpointIterator
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|CircuitBreaker
name|circuitBreaker
init|=
name|getCircuitBreaker
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|circuitBreaker
operator|.
name|allowRequest
argument_list|()
condition|)
block|{
name|alternateEndpointIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|failoverTarget
operator|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateEndpoint
argument_list|(
name|alternateEndpoints
argument_list|)
expr_stmt|;
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
name|getCircuitBreaker
argument_list|(
name|address
argument_list|)
operator|.
name|markFailure
argument_list|(
name|ex
argument_list|)
expr_stmt|;
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
name|getCircuitBreaker
argument_list|(
name|address
argument_list|)
operator|.
name|markSuccess
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|CircuitBreaker
name|getCircuitBreaker
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|getCircuitBreaker
argument_list|(
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|synchronized
name|CircuitBreaker
name|getCircuitBreaker
parameter_list|(
specifier|final
name|String
name|alternateAddress
parameter_list|)
block|{
name|CircuitBreaker
name|circuitBreaker
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|alternateAddress
argument_list|)
condition|)
block|{
for|for
control|(
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
name|alternateAddress
operator|.
name|startsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|circuitBreaker
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|circuitBreaker
operator|==
literal|null
condition|)
block|{
name|circuitBreaker
operator|=
operator|new
name|ZestCircuitBreaker
argument_list|(
name|threshold
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|circuits
operator|.
name|put
argument_list|(
name|alternateAddress
argument_list|,
name|circuitBreaker
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|circuitBreaker
operator|==
literal|null
condition|)
block|{
name|circuitBreaker
operator|=
name|NOOP_CIRCUIT_BREAKER
expr_stmt|;
block|}
return|return
name|circuitBreaker
return|;
block|}
specifier|private
name|boolean
name|isEndpointChanged
parameter_list|(
specifier|final
name|String
name|address
parameter_list|,
specifier|final
name|Endpoint
name|target
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
condition|)
block|{
return|return
operator|!
name|address
operator|.
name|startsWith
argument_list|(
name|target
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|equals
argument_list|(
name|target
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|startsWith
argument_list|(
name|target
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isFailoverRequired
parameter_list|(
specifier|final
name|String
name|address
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
condition|)
block|{
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
name|address
operator|.
name|startsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|!
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|allowRequest
argument_list|()
return|;
block|}
block|}
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"No circuit breaker present for address: "
operator|+
name|address
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

