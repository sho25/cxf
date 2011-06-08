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
name|binding
operator|.
name|coloc
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|BusFactory
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
name|binding
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
name|interceptor
operator|.
name|InterceptorChain
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
name|ExchangeImpl
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
name|MessageImpl
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
name|Service
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
name|BindingInfo
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
name|MessageInfo
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
name|transport
operator|.
name|ChainInitiationObserver
import|;
end_import

begin_class
specifier|public
class|class
name|ColocMessageObserver
extends|extends
name|ChainInitiationObserver
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
name|ColocMessageObserver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COLOCATED
init|=
name|Message
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".COLOCATED"
decl_stmt|;
specifier|public
name|ColocMessageObserver
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|ClassLoader
name|origLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|ClassLoader
name|loader
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"Processing Message at collocated endpoint.  Request message: "
operator|+
name|m
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|setExchangeProperties
argument_list|(
name|ex
argument_list|,
name|m
argument_list|)
expr_stmt|;
name|Message
name|inMsg
init|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|MessageImpl
operator|.
name|copyContent
argument_list|(
name|m
argument_list|,
name|inMsg
argument_list|)
expr_stmt|;
comment|//Copy Request Context to Server inBound Message
comment|//TODO a Context Filter Strategy required.
name|inMsg
operator|.
name|putAll
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|put
argument_list|(
name|COLOCATED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|put
argument_list|(
name|Message
operator|.
name|INBOUND_MESSAGE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|OperationInfo
name|oi
init|=
name|ex
operator|.
name|get
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oi
operator|!=
literal|null
condition|)
block|{
name|inMsg
operator|.
name|put
argument_list|(
name|MessageInfo
operator|.
name|class
argument_list|,
name|oi
operator|.
name|getInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ex
operator|.
name|setInMessage
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"Build inbound interceptor chain."
argument_list|)
expr_stmt|;
block|}
comment|//Add all interceptors between USER_LOGICAL and INVOKE.
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
init|=
operator|new
name|TreeSet
argument_list|<
name|Phase
argument_list|>
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
operator|.
name|getInPhases
argument_list|()
argument_list|)
decl_stmt|;
name|ColocUtil
operator|.
name|setPhases
argument_list|(
name|phases
argument_list|,
name|Phase
operator|.
name|USER_LOGICAL
argument_list|,
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
name|InterceptorChain
name|chain
init|=
name|ColocUtil
operator|.
name|getInInterceptorChain
argument_list|(
name|ex
argument_list|,
name|phases
argument_list|)
decl_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|addColocInterceptors
argument_list|()
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
comment|//Convert the coloc object type if necessary
name|OperationInfo
name|soi
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|soi
operator|!=
literal|null
operator|&&
name|oi
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|soi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|oi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// converting source -> pojo
name|ColocUtil
operator|.
name|convertSourceToObject
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|oi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|soi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// converting pojo -> source
name|ColocUtil
operator|.
name|convertObjectToSource
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
block|}
block|}
name|chain
operator|.
name|doIntercept
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
if|if
condition|(
name|soi
operator|!=
literal|null
operator|&&
name|oi
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|soi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|oi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|&&
name|ex
operator|.
name|getOutMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// converting pojo -> source
name|ColocUtil
operator|.
name|convertObjectToSource
argument_list|(
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|oi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|ColocUtil
operator|.
name|isAssignableOperationInfo
argument_list|(
name|soi
argument_list|,
name|Source
operator|.
name|class
argument_list|)
operator|&&
name|ex
operator|.
name|getOutMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// converting pojo -> source
name|ColocUtil
operator|.
name|convertSourceToObject
argument_list|(
name|ex
operator|.
name|getOutMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//Set Server OutBound Message onto InBound Exchange.
name|setOutBoundMessage
argument_list|(
name|ex
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|origLoader
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setOutBoundMessage
parameter_list|(
name|Exchange
name|from
parameter_list|,
name|Exchange
name|to
parameter_list|)
block|{
if|if
condition|(
name|from
operator|.
name|getOutFaultMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|to
operator|.
name|setInFaultMessage
argument_list|(
name|from
operator|.
name|getOutFaultMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|to
operator|.
name|setInMessage
argument_list|(
name|from
operator|.
name|getOutMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setExchangeProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Service
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getService
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Binding
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
comment|//Setup the BindingOperationInfo
name|QName
name|opName
init|=
operator|(
name|QName
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|)
decl_stmt|;
name|BindingInfo
name|bi
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|bi
operator|.
name|getOperation
argument_list|(
name|opName
argument_list|)
decl_stmt|;
if|if
condition|(
name|boi
operator|!=
literal|null
operator|&&
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|put
argument_list|(
name|BindingInfo
operator|.
name|class
argument_list|,
name|bi
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|OperationInfo
operator|.
name|class
argument_list|,
name|boi
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|addColocInterceptors
parameter_list|()
block|{
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|ColocInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

