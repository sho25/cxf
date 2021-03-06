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
name|Map
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
name|ClientCallback
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
name|PhaseManager
import|;
end_import

begin_class
specifier|public
class|class
name|ClientOutFaultObserver
extends|extends
name|AbstractFaultChainInitiatorObserver
block|{
specifier|public
name|ClientOutFaultObserver
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
annotation|@
name|Override
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
comment|/**      * override the super class method      */
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|)
operator|.
name|equals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
condition|)
block|{
comment|//it's outbound fault observer so only take care of outbound fault
return|return;
block|}
name|Exception
name|ex
init|=
name|m
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// remove callback so that it won't be invoked twice
name|ClientCallback
name|callback
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|ClientCallback
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|resCtx
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
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|INVOCATION_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
name|resCtx
operator|=
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
name|resCtx
operator|.
name|get
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|handleException
argument_list|(
name|resCtx
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
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

