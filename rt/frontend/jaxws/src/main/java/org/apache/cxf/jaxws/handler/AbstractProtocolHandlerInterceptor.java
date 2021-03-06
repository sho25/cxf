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
name|jaxws
operator|.
name|handler
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Binding
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
operator|.
name|Scope
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractProtocolHandlerInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
extends|extends
name|AbstractJAXWSHandlerInterceptor
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
name|AbstractProtocolHandlerInterceptor
parameter_list|(
name|Binding
name|binding
parameter_list|)
block|{
name|super
argument_list|(
name|binding
argument_list|,
name|Phase
operator|.
name|USER_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractProtocolHandlerInterceptor
parameter_list|(
name|Binding
name|binding
parameter_list|,
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|binding
argument_list|,
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|T
name|message
parameter_list|)
block|{
if|if
condition|(
name|binding
operator|.
name|getHandlerChain
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|MessageContext
name|context
init|=
name|createProtocolMessageContext
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|HandlerChainInvoker
name|invoker
init|=
name|getInvoker
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|invoker
operator|.
name|setProtocolMessageContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|invoker
operator|.
name|invokeProtocolHandlers
argument_list|(
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|onCompletion
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|MessageContext
name|createProtocolMessageContext
parameter_list|(
name|T
name|message
parameter_list|)
block|{
return|return
operator|new
name|WrappedMessageContext
argument_list|(
name|message
argument_list|,
name|Scope
operator|.
name|HANDLER
argument_list|)
return|;
block|}
block|}
end_class

end_unit

