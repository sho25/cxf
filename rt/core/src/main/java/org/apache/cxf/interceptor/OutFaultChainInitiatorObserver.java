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
name|interceptor
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
name|SortedSet
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|PhaseManager
import|;
end_import

begin_class
specifier|public
class|class
name|OutFaultChainInitiatorObserver
extends|extends
name|AbstractFaultChainInitiatorObserver
block|{
specifier|public
name|OutFaultChainInitiatorObserver
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initializeInterceptors
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|PhaseInterceptorChain
name|chain
parameter_list|)
block|{
name|Endpoint
name|e
init|=
name|ex
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Client
name|c
init|=
name|ex
operator|.
name|get
argument_list|(
name|Client
operator|.
name|class
argument_list|)
decl_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|getBus
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|c
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getService
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|e
operator|.
name|getBinding
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|instanceof
name|InterceptorProvider
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|(
operator|(
name|InterceptorProvider
operator|)
name|e
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
operator|)
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addToChain
argument_list|(
name|chain
argument_list|,
name|ex
operator|.
name|getInMessage
argument_list|()
argument_list|)
expr_stmt|;
name|addToChain
argument_list|(
name|chain
argument_list|,
name|ex
operator|.
name|getOutFaultMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addToChain
parameter_list|(
name|PhaseInterceptorChain
name|chain
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|Collection
argument_list|<
name|InterceptorProvider
argument_list|>
name|providers
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INTERCEPTOR_PROVIDERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|InterceptorProvider
name|p
range|:
name|providers
control|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|p
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|is
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|FAULT_OUT_INTERCEPTORS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|m
operator|.
name|getDestination
argument_list|()
operator|instanceof
name|InterceptorProvider
condition|)
block|{
name|chain
operator|.
name|add
argument_list|(
operator|(
operator|(
name|InterceptorProvider
operator|)
name|m
operator|.
name|getDestination
argument_list|()
operator|)
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|getPhases
parameter_list|()
block|{
return|return
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
operator|.
name|getOutPhases
argument_list|()
return|;
block|}
specifier|protected
name|boolean
name|isOutboundObserver
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

