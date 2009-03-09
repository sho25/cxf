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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|message
operator|.
name|MessageUtils
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
name|transport
operator|.
name|Conduit
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|ClientPolicyInFaultInterceptor
extends|extends
name|AbstractPolicyInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|ClientPolicyInFaultInterceptor
name|INSTANCE
init|=
operator|new
name|ClientPolicyInFaultInterceptor
argument_list|()
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
name|ClientPolicyInFaultInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ClientPolicyInFaultInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|PolicyConstants
operator|.
name|CLIENT_POLICY_IN_FAULT_INTERCEPTOR_ID
argument_list|,
name|Phase
operator|.
name|RECEIVE
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handle
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|msg
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Not a requestor."
argument_list|)
expr_stmt|;
return|return;
block|}
name|Exchange
name|exchange
init|=
name|msg
operator|.
name|getExchange
argument_list|()
decl_stmt|;
assert|assert
literal|null
operator|!=
name|exchange
assert|;
name|Endpoint
name|e
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
if|if
condition|(
literal|null
operator|==
name|e
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No endpoint."
argument_list|)
expr_stmt|;
return|return;
block|}
name|EndpointInfo
name|ei
init|=
name|e
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|PolicyEngine
name|pe
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyEngine
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|pe
condition|)
block|{
return|return;
block|}
name|Conduit
name|conduit
init|=
name|exchange
operator|.
name|getConduit
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"conduit: "
operator|+
name|conduit
argument_list|)
expr_stmt|;
comment|// We do not know the underlying message type yet - so we pre-emptively add interceptors
comment|// that can deal with all faults returned to this client endpoint.
name|EndpointPolicy
name|ep
init|=
name|pe
operator|.
name|getClientEndpointPolicy
argument_list|(
name|ei
argument_list|,
name|conduit
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"ep: "
operator|+
name|ep
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|>
name|faultInterceptors
init|=
name|ep
operator|.
name|getFaultInterceptors
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"faultInterceptors: "
operator|+
name|faultInterceptors
argument_list|)
expr_stmt|;
for|for
control|(
name|Interceptor
name|i
range|:
name|faultInterceptors
control|)
block|{
name|msg
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Added interceptor of type {0}"
argument_list|,
name|i
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// insert assertions of endpoint's fault vocabulary into message
name|Collection
argument_list|<
name|PolicyAssertion
argument_list|>
name|assertions
init|=
name|ep
operator|.
name|getFaultVocabulary
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|assertions
operator|&&
operator|!
name|assertions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|,
operator|new
name|AssertionInfoMap
argument_list|(
name|assertions
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

