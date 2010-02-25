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
name|namespace
operator|.
name|QName
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
name|Binding
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
name|service
operator|.
name|model
operator|.
name|OperationInfo
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
name|ServiceModelUtil
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJAXWSHandlerInterceptor
parameter_list|<
name|T
extends|extends
name|Message
parameter_list|>
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
name|Binding
name|binding
decl_stmt|;
specifier|protected
name|AbstractJAXWSHandlerInterceptor
parameter_list|(
name|Binding
name|b
parameter_list|,
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
name|binding
operator|=
name|b
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isOutbound
parameter_list|(
name|T
name|message
parameter_list|)
block|{
return|return
name|isOutbound
argument_list|(
name|message
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isOutbound
parameter_list|(
name|T
name|message
parameter_list|,
name|Exchange
name|ex
parameter_list|)
block|{
return|return
name|message
operator|==
name|ex
operator|.
name|getOutMessage
argument_list|()
operator|||
name|message
operator|==
name|ex
operator|.
name|getOutFaultMessage
argument_list|()
return|;
block|}
specifier|protected
name|HandlerChainInvoker
name|getInvoker
parameter_list|(
name|T
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
name|HandlerChainInvoker
name|invoker
init|=
name|ex
operator|.
name|get
argument_list|(
name|HandlerChainInvoker
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|invoker
condition|)
block|{
name|invoker
operator|=
operator|new
name|HandlerChainInvoker
argument_list|(
name|binding
operator|.
name|getHandlerChain
argument_list|()
argument_list|,
name|isOutbound
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|HandlerChainInvoker
operator|.
name|class
argument_list|,
name|invoker
argument_list|)
expr_stmt|;
block|}
name|boolean
name|outbound
init|=
name|isOutbound
argument_list|(
name|message
argument_list|,
name|ex
argument_list|)
decl_stmt|;
if|if
condition|(
name|outbound
condition|)
block|{
name|invoker
operator|.
name|setOutbound
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|invoker
operator|.
name|setInbound
argument_list|()
expr_stmt|;
block|}
name|invoker
operator|.
name|setRequestor
argument_list|(
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ex
operator|.
name|isOneWay
argument_list|()
operator|||
operator|(
operator|(
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
operator|!
name|outbound
operator|)
operator|||
operator|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|outbound
operator|)
operator|)
condition|)
block|{
name|invoker
operator|.
name|setResponseExpected
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|invoker
operator|.
name|setResponseExpected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|invoker
return|;
block|}
specifier|protected
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
specifier|public
name|void
name|onCompletion
parameter_list|(
name|T
name|message
parameter_list|)
block|{
name|getInvoker
argument_list|(
name|message
argument_list|)
operator|.
name|mepComplete
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isMEPComlete
parameter_list|(
name|T
name|message
parameter_list|)
block|{
name|HandlerChainInvoker
name|invoker
init|=
name|getInvoker
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|invoker
operator|.
name|isRequestor
argument_list|()
condition|)
block|{
comment|//client inbound and client outbound with no response are end of MEP
if|if
condition|(
name|invoker
operator|.
name|isInbound
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|invoker
operator|.
name|isResponseExpected
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
comment|//server outbound and server inbound with no response are end of MEP
if|if
condition|(
operator|!
name|invoker
operator|.
name|isInbound
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|invoker
operator|.
name|isResponseExpected
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|setupBindingOperationInfo
parameter_list|(
name|Exchange
name|exch
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
if|if
condition|(
name|exch
operator|.
name|get
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|//need to know the operation to determine if oneway
name|QName
name|opName
init|=
name|getOpQName
argument_list|(
name|exch
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|BindingOperationInfo
name|bop
init|=
name|ServiceModelUtil
operator|.
name|getOperationForWrapperElement
argument_list|(
name|exch
argument_list|,
name|opName
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
name|bop
operator|=
name|ServiceModelUtil
operator|.
name|getOperation
argument_list|(
name|exch
argument_list|,
name|opName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bop
operator|!=
literal|null
condition|)
block|{
name|exch
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|bop
argument_list|)
expr_stmt|;
name|exch
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|bop
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|bop
operator|.
name|getOutput
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exch
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|QName
name|getOpQName
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

