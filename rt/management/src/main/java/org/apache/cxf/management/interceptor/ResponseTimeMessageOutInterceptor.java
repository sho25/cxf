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
name|management
operator|.
name|interceptor
package|;
end_package

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
name|interceptor
operator|.
name|MessageSenderInterceptor
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
name|FaultMode
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
name|AbstractPhaseInterceptor
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

begin_class
specifier|public
class|class
name|ResponseTimeMessageOutInterceptor
extends|extends
name|AbstractMessageResponseTimeInterceptor
block|{
specifier|private
name|EndingInterceptor
name|ending
init|=
operator|new
name|EndingInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|ResponseTimeMessageOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND_ENDING
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
name|MessageSenderInterceptor
operator|.
name|MessageSenderEndingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Boolean
name|forceDisabled
init|=
name|Boolean
operator|.
name|FALSE
operator|.
name|equals
argument_list|(
operator|(
name|Boolean
operator|)
name|ex
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.management.counter.enabled"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|forceDisabled
operator|&&
name|isServiceCounterEnabled
argument_list|(
name|ex
argument_list|)
condition|)
block|{
if|if
condition|(
name|ex
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
name|endHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PARTIAL_RESPONSE_MESSAGE
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isClient
argument_list|(
name|message
argument_list|)
condition|)
block|{
if|if
condition|(
name|ex
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
block|}
name|beginHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// the message is handled by server
name|endHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.management.counter.enabled"
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ex
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
comment|// do nothing, done by the ResponseTimeInvokerInterceptor
block|}
else|else
block|{
name|FaultMode
name|faultMode
init|=
name|message
operator|.
name|get
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|faultMode
operator|==
literal|null
condition|)
block|{
comment|// client side exceptions don't have FaultMode set un the message properties (as of 2.1.4)
name|faultMode
operator|=
name|FaultMode
operator|.
name|RUNTIME_FAULT
expr_stmt|;
block|}
name|ex
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|faultMode
argument_list|)
expr_stmt|;
name|endHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|EndingInterceptor
name|getEndingInterceptor
parameter_list|()
block|{
return|return
name|ending
return|;
block|}
specifier|public
class|class
name|EndingInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|EndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND_ENDING
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|endHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|endHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

