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
name|PropertyUtils
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
name|AbstractConduitSelector
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
name|endpoint
operator|.
name|Retryable
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
name|BindingOperationInfo
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

begin_comment
comment|/**  * Implements a target selection strategy based on failover to an  * alternate target endpoint when a transport level failure is  * encountered.  * Note that this feature changes the conduit on the fly and thus makes  * the Client not thread safe.  */
end_comment

begin_class
specifier|public
class|class
name|FailoverTargetSelector
extends|extends
name|AbstractConduitSelector
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
name|FailoverTargetSelector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMPLETE_IF_SERVICE_NOT_AVAIL_PROPERTY
init|=
literal|"org.apache.cxf.transport.complete_if_service_not_available"
decl_stmt|;
specifier|protected
name|ConcurrentHashMap
argument_list|<
name|InvocationKey
argument_list|,
name|InvocationContext
argument_list|>
name|inProgress
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|InvocationKey
argument_list|,
name|InvocationContext
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|FailoverStrategy
name|failoverStrategy
decl_stmt|;
specifier|private
name|boolean
name|supportNotAvailableErrorsOnly
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|clientBootstrapAddress
decl_stmt|;
comment|/**      * Normal constructor.      */
specifier|public
name|FailoverTargetSelector
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|FailoverTargetSelector
parameter_list|(
name|String
name|clientBootstrapAddress
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|clientBootstrapAddress
operator|=
name|clientBootstrapAddress
expr_stmt|;
block|}
comment|/**      * Constructor, allowing a specific conduit to override normal selection.      *      * @param c specific conduit      */
specifier|public
name|FailoverTargetSelector
parameter_list|(
name|Conduit
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called prior to the interceptor chain being traversed.      *      * @param message the current Message      */
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|setupExchangeExceptionProperties
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|InvocationKey
name|key
init|=
operator|new
name|InvocationKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|getInvocationContext
argument_list|(
name|key
argument_list|)
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getClientBootstrapAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|getClientBootstrapAddress
argument_list|()
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|addresses
init|=
name|failoverStrategy
operator|.
name|getAlternateAddresses
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|addresses
operator|!=
literal|null
operator|&&
operator|!
name|addresses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|addresses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|addresses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Endpoint
name|endpoint
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bindingOperationInfo
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
name|Object
index|[]
name|params
init|=
name|message
operator|.
name|getContent
argument_list|(
name|List
operator|.
name|class
argument_list|)
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
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
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INVOCATION_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
name|InvocationContext
name|invocation
init|=
operator|new
name|InvocationContext
argument_list|(
name|endpoint
argument_list|,
name|bindingOperationInfo
argument_list|,
name|params
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|inProgress
operator|.
name|putIfAbsent
argument_list|(
name|key
argument_list|,
name|invocation
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setupExchangeExceptionProperties
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportNotAvailableErrorsOnly
argument_list|()
condition|)
block|{
name|ex
operator|.
name|remove
argument_list|(
literal|"org.apache.cxf.transport.no_io_exceptions"
argument_list|)
expr_stmt|;
block|}
name|ex
operator|.
name|put
argument_list|(
name|COMPLETE_IF_SERVICE_NOT_AVAIL_PROPERTY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called when a Conduit is actually required.      *      * @param message      * @return the Conduit to use for mediation of the message      */
specifier|public
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
return|return
name|getSelectedConduit
argument_list|(
name|message
argument_list|)
return|;
block|}
specifier|protected
name|InvocationContext
name|getInvocationContext
parameter_list|(
name|InvocationKey
name|key
parameter_list|)
block|{
return|return
name|inProgress
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * Called on completion of the MEP for which the Conduit was required.      *      * @param exchange represents the completed MEP      */
specifier|public
name|void
name|complete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
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
name|getInvocationContext
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|invocation
operator|==
literal|null
condition|)
block|{
name|super
operator|.
name|complete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|failover
init|=
literal|false
decl_stmt|;
specifier|final
name|Exception
name|ex
init|=
name|getExceptionIfPresent
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|requiresFailover
argument_list|(
name|exchange
argument_list|,
name|ex
argument_list|)
condition|)
block|{
name|onFailure
argument_list|(
name|invocation
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|Conduit
name|old
init|=
operator|(
name|Conduit
operator|)
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|.
name|remove
argument_list|(
name|Conduit
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Endpoint
name|failoverTarget
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
name|failoverTarget
operator|!=
literal|null
condition|)
block|{
name|setEndpoint
argument_list|(
name|failoverTarget
argument_list|)
expr_stmt|;
name|removeConduit
argument_list|(
name|old
argument_list|)
expr_stmt|;
name|failover
operator|=
name|performFailover
argument_list|(
name|exchange
argument_list|,
name|invocation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|remove
argument_list|(
name|COMPLETE_IF_SERVICE_NOT_AVAIL_PROPERTY
argument_list|)
expr_stmt|;
name|setOriginalEndpoint
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|fine
argument_list|(
literal|"FAILOVER_NOT_REQUIRED"
argument_list|)
expr_stmt|;
name|onSuccess
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|failover
condition|)
block|{
name|inProgress
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|doComplete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
operator|.
name|complete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setOriginalEndpoint
parameter_list|(
name|InvocationContext
name|invocation
parameter_list|)
block|{
name|setEndpoint
argument_list|(
name|invocation
operator|.
name|retrieveOriginalEndpoint
argument_list|(
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|performFailover
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InvocationContext
name|invocation
parameter_list|)
block|{
name|Exception
name|prevExchangeFault
init|=
operator|(
name|Exception
operator|)
name|exchange
operator|.
name|remove
argument_list|(
name|Exception
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|Exception
name|prevMessageFault
init|=
name|outMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
name|outMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
literal|null
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
name|Retryable
name|retry
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Retryable
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|clear
argument_list|()
expr_stmt|;
name|boolean
name|failover
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|retry
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|failover
operator|=
literal|true
expr_stmt|;
name|long
name|delay
init|=
name|getDelayBetweenRetries
argument_list|()
decl_stmt|;
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
name|retry
operator|.
name|invoke
argument_list|(
name|invocation
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|,
name|invocation
operator|.
name|getParams
argument_list|()
argument_list|,
name|invocation
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|prevExchangeFault
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|outMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|outMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|prevMessageFault
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|failover
return|;
block|}
specifier|protected
name|void
name|onSuccess
parameter_list|(
name|InvocationContext
name|context
parameter_list|)
block|{     }
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
block|{     }
comment|/**      * @param strategy the FailoverStrategy to use      */
specifier|public
specifier|synchronized
name|void
name|setStrategy
parameter_list|(
name|FailoverStrategy
name|strategy
parameter_list|)
block|{
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"USING_STRATEGY"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|strategy
block|}
argument_list|)
expr_stmt|;
name|failoverStrategy
operator|=
name|strategy
expr_stmt|;
block|}
block|}
comment|/**      * @return strategy the FailoverStrategy to use      */
specifier|public
specifier|synchronized
name|FailoverStrategy
name|getStrategy
parameter_list|()
block|{
if|if
condition|(
name|failoverStrategy
operator|==
literal|null
condition|)
block|{
name|failoverStrategy
operator|=
operator|new
name|SequentialStrategy
argument_list|()
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"USING_STRATEGY"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|failoverStrategy
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|failoverStrategy
return|;
block|}
comment|/**      * @return the logger to use      */
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
comment|/**      * Returns delay (in milliseconds) between retries      * @return delay, 0 means no delay      */
specifier|protected
name|long
name|getDelayBetweenRetries
parameter_list|()
block|{
name|FailoverStrategy
name|strategy
init|=
name|getStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|instanceof
name|AbstractStaticFailoverStrategy
condition|)
block|{
return|return
operator|(
operator|(
name|AbstractStaticFailoverStrategy
operator|)
name|strategy
operator|)
operator|.
name|getDelayBetweenRetries
argument_list|()
return|;
block|}
comment|//perhaps supporting FailoverTargetSelector specific property can make sense too
return|return
literal|0
return|;
block|}
comment|/**      * Check if the exchange is suitable for a failover.      *      * @param exchange the current Exchange      * @return boolean true if a failover should be attempted      */
specifier|protected
name|boolean
name|requiresFailover
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CHECK_LAST_INVOKE_FAILED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|ex
operator|!=
literal|null
block|}
argument_list|)
expr_stmt|;
name|Throwable
name|curr
init|=
name|ex
decl_stmt|;
name|boolean
name|failover
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|curr
operator|!=
literal|null
condition|)
block|{
name|failover
operator|=
name|curr
operator|instanceof
name|java
operator|.
name|io
operator|.
name|IOException
expr_stmt|;
name|curr
operator|=
name|curr
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"CHECK_FAILURE_IN_TRANSPORT"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|ex
block|,
name|failover
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isSupportNotAvailableErrorsOnly
argument_list|()
operator|&&
name|exchange
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|failover
operator|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|exchange
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.transport.service_not_available"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|failover
return|;
block|}
specifier|private
name|Exception
name|getExceptionIfPresent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|outMessage
init|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|Exception
name|ex
init|=
name|outMessage
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|?
name|outMessage
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
else|:
name|exchange
operator|.
name|get
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|ex
return|;
block|}
comment|/**      * Get the failover target endpoint, if a suitable one is available.      *      * @param exchange the current Exchange      * @param invocation the current InvocationContext      * @return a failover endpoint if one is available      */
specifier|protected
name|Endpoint
name|getFailoverTarget
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InvocationContext
name|invocation
parameter_list|)
block|{
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
condition|)
block|{
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
if|if
condition|(
name|alternateAddress
operator|!=
literal|null
condition|)
block|{
comment|// re-use current endpoint
comment|//
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
name|failoverTarget
operator|=
name|getStrategy
argument_list|()
operator|.
name|selectAlternateEndpoint
argument_list|(
name|invocation
operator|.
name|getAlternateEndpoints
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|failoverTarget
return|;
block|}
comment|/**      * Fetches and updates the alternative address or/and alternative endpoints      * (depending on the strategy) for current invocation context.      * @param exchange the current Exchange      * @param invocation the current InvocationContext      * @return alternative addresses      */
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|updateContextAlternatives
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InvocationContext
name|invocation
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|alternateAddresses
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|invocation
operator|.
name|hasAlternates
argument_list|()
condition|)
block|{
comment|// no previous failover attempt on this invocation
comment|//
name|alternateAddresses
operator|=
name|getStrategy
argument_list|()
operator|.
name|getAlternateAddresses
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|alternateAddresses
operator|!=
literal|null
condition|)
block|{
name|invocation
operator|.
name|setAlternateAddresses
argument_list|(
name|alternateAddresses
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invocation
operator|.
name|setAlternateEndpoints
argument_list|(
name|getStrategy
argument_list|()
operator|.
name|getAlternateEndpoints
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|alternateAddresses
operator|=
name|invocation
operator|.
name|getAlternateAddresses
argument_list|()
expr_stmt|;
block|}
return|return
name|alternateAddresses
return|;
block|}
comment|/**      * Override the ENDPOINT_ADDRESS property in the request context      *      * @param context the request context      */
specifier|protected
name|void
name|overrideAddressProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|)
block|{
name|overrideAddressProperty
argument_list|(
name|context
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|overrideAddressProperty
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
parameter_list|,
name|String
name|address
parameter_list|)
block|{
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
name|requestContext
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|requestContext
operator|.
name|put
argument_list|(
literal|"javax.xml.ws.service.endpoint.address"
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Some conduits may replace the endpoint address after it has already been prepared
comment|// but before the invocation has been done (ex, org.apache.cxf.clustering.LoadDistributorTargetSelector)
comment|// which may affect JAX-RS clients where actual endpoint address property may include additional path
comment|// segments.
specifier|protected
name|boolean
name|replaceEndpointAddressPropertyIfNeeded
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|endpointAddress
parameter_list|,
name|Conduit
name|cond
parameter_list|)
block|{
name|String
name|requestURI
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
name|REQUEST_URI
argument_list|)
decl_stmt|;
if|if
condition|(
name|requestURI
operator|!=
literal|null
operator|&&
name|endpointAddress
operator|!=
literal|null
operator|&&
operator|!
name|requestURI
operator|.
name|equals
argument_list|(
name|endpointAddress
argument_list|)
condition|)
block|{
name|String
name|basePath
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
name|BASE_PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePath
operator|!=
literal|null
operator|&&
name|requestURI
operator|.
name|startsWith
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|String
name|pathInfo
init|=
name|requestURI
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|BASE_PATH
argument_list|,
name|endpointAddress
argument_list|)
expr_stmt|;
specifier|final
name|String
name|slash
init|=
literal|"/"
decl_stmt|;
name|boolean
name|startsWithSlash
init|=
name|pathInfo
operator|.
name|startsWith
argument_list|(
name|slash
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpointAddress
operator|.
name|endsWith
argument_list|(
name|slash
argument_list|)
condition|)
block|{
name|endpointAddress
operator|=
name|endpointAddress
operator|+
operator|(
name|startsWithSlash
condition|?
name|pathInfo
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
else|:
name|pathInfo
operator|)
expr_stmt|;
block|}
else|else
block|{
name|endpointAddress
operator|=
name|endpointAddress
operator|+
operator|(
name|startsWithSlash
condition|?
name|pathInfo
else|:
operator|(
name|slash
operator|+
name|pathInfo
operator|)
operator|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|endpointAddress
argument_list|)
expr_stmt|;
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
condition|)
block|{
name|overrideAddressProperty
argument_list|(
name|invocation
operator|.
name|getContext
argument_list|()
argument_list|,
name|cond
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isSupportNotAvailableErrorsOnly
parameter_list|()
block|{
return|return
name|supportNotAvailableErrorsOnly
return|;
block|}
specifier|public
name|void
name|setSupportNotAvailableErrorsOnly
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|this
operator|.
name|supportNotAvailableErrorsOnly
operator|=
name|support
expr_stmt|;
block|}
specifier|public
name|String
name|getClientBootstrapAddress
parameter_list|()
block|{
return|return
name|clientBootstrapAddress
return|;
block|}
specifier|public
name|void
name|setClientBootstrapAddress
parameter_list|(
name|String
name|clientBootstrapAddress
parameter_list|)
block|{
name|this
operator|.
name|clientBootstrapAddress
operator|=
name|clientBootstrapAddress
expr_stmt|;
block|}
specifier|protected
name|InvocationKey
name|getInvocationKey
parameter_list|(
name|Exchange
name|e
parameter_list|)
block|{
return|return
operator|new
name|InvocationKey
argument_list|(
name|e
argument_list|)
return|;
block|}
comment|/**      * Used to wrap an Exchange for usage as a Map key. The raw Exchange      * is not a suitable key type, as the hashCode is computed from its      * current contents, which may obviously change over the lifetime of      * an invocation.      */
specifier|protected
specifier|static
class|class
name|InvocationKey
block|{
specifier|private
name|Exchange
name|exchange
decl_stmt|;
specifier|protected
name|InvocationKey
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
name|exchange
operator|=
name|ex
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|System
operator|.
name|identityHashCode
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|InvocationKey
operator|&&
name|exchange
operator|==
operator|(
operator|(
name|InvocationKey
operator|)
name|o
operator|)
operator|.
name|exchange
return|;
block|}
block|}
comment|/**      * Records the context of an invocation.      */
specifier|protected
class|class
name|InvocationContext
block|{
specifier|private
name|Endpoint
name|originalEndpoint
decl_stmt|;
specifier|private
name|String
name|originalAddress
decl_stmt|;
specifier|private
name|BindingOperationInfo
name|bindingOperationInfo
decl_stmt|;
specifier|private
name|Object
index|[]
name|params
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternateEndpoints
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|alternateAddresses
decl_stmt|;
specifier|protected
name|InvocationContext
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|,
name|Object
index|[]
name|prms
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|)
block|{
name|originalEndpoint
operator|=
name|endpoint
expr_stmt|;
name|originalAddress
operator|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
expr_stmt|;
name|bindingOperationInfo
operator|=
name|boi
expr_stmt|;
name|params
operator|=
name|prms
expr_stmt|;
name|context
operator|=
name|ctx
expr_stmt|;
block|}
specifier|public
name|Endpoint
name|retrieveOriginalEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|!=
name|originalEndpoint
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"REVERT_TO_ORIGINAL_TARGET"
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|equals
argument_list|(
name|originalAddress
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|setAddress
argument_list|(
name|originalAddress
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"REVERT_TO_ORIGINAL_ADDRESS"
argument_list|,
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|originalEndpoint
return|;
block|}
specifier|public
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|()
block|{
return|return
name|bindingOperationInfo
return|;
block|}
specifier|public
name|Object
index|[]
name|getParams
parameter_list|()
block|{
return|return
name|params
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|List
argument_list|<
name|Endpoint
argument_list|>
name|getAlternateEndpoints
parameter_list|()
block|{
return|return
name|alternateEndpoints
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAlternateAddresses
parameter_list|()
block|{
return|return
name|alternateAddresses
return|;
block|}
specifier|protected
name|void
name|setAlternateEndpoints
parameter_list|(
name|List
argument_list|<
name|Endpoint
argument_list|>
name|alternates
parameter_list|)
block|{
name|alternateEndpoints
operator|=
name|alternates
expr_stmt|;
block|}
specifier|protected
name|void
name|setAlternateAddresses
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|alternates
parameter_list|)
block|{
name|alternateAddresses
operator|=
name|alternates
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasAlternates
parameter_list|()
block|{
return|return
operator|!
operator|(
name|alternateEndpoints
operator|==
literal|null
operator|&&
name|alternateAddresses
operator|==
literal|null
operator|)
return|;
block|}
block|}
block|}
end_class

end_unit

