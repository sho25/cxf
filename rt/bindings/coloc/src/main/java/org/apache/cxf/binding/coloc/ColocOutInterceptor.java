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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|i18n
operator|.
name|BundleUtils
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
name|ClientImpl
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
name|Server
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
name|ServerRegistry
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
name|MessageObserver
import|;
end_import

begin_class
specifier|public
class|class
name|ColocOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|ColocOutInterceptor
operator|.
name|class
argument_list|)
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
name|ClientImpl
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
specifier|private
name|MessageObserver
name|colocObserver
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|ColocOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ColocOutInterceptor
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
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
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
expr_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"BUS_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
block|}
name|ServerRegistry
name|registry
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"SERVER_REGISTRY_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Endpoint
name|senderEndpoint
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|senderEndpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"ENDPOINT_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
name|BindingOperationInfo
name|boi
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"OPERATIONINFO_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|)
throw|;
block|}
name|Server
name|srv
init|=
name|isColocated
argument_list|(
name|registry
operator|.
name|getServers
argument_list|()
argument_list|,
name|senderEndpoint
argument_list|,
name|boi
argument_list|)
decl_stmt|;
if|if
condition|(
name|srv
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Operation:"
operator|+
name|boi
operator|.
name|getName
argument_list|()
operator|+
literal|" dispatched as colocated call."
argument_list|)
expr_stmt|;
block|}
name|InterceptorChain
name|outChain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
name|outChain
operator|.
name|abort
argument_list|()
expr_stmt|;
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
name|message
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
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_OPERATION
argument_list|,
name|boi
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_INTERFACE
argument_list|,
name|boi
operator|.
name|getBinding
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|invokeColocObserver
argument_list|(
name|message
argument_list|,
name|srv
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|invokeInboundChain
argument_list|(
name|exchange
argument_list|,
name|senderEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Operation:"
operator|+
name|boi
operator|.
name|getName
argument_list|()
operator|+
literal|" dispatched as remote call."
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|COLOCATED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|invokeColocObserver
parameter_list|(
name|Message
name|outMsg
parameter_list|,
name|Endpoint
name|inboundEndpoint
parameter_list|)
block|{
if|if
condition|(
name|colocObserver
operator|==
literal|null
condition|)
block|{
name|colocObserver
operator|=
operator|new
name|ColocMessageObserver
argument_list|(
name|inboundEndpoint
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Invoke on Coloc Observer."
argument_list|)
expr_stmt|;
block|}
name|colocObserver
operator|.
name|onMessage
argument_list|(
name|outMsg
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|invokeInboundChain
parameter_list|(
name|Exchange
name|ex
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
block|{
name|Message
name|m
init|=
name|getInBoundMessage
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|Message
name|inMsg
init|=
name|ep
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
comment|//Copy Response Context to Client inBound Message
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
name|Message
operator|.
name|REQUESTOR_ROLE
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
name|INBOUND_MESSAGE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|inMsg
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Exception
name|exc
init|=
name|inMsg
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|exc
operator|!=
literal|null
condition|)
block|{
name|ex
operator|.
name|setInFaultMessage
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|ColocInFaultObserver
name|observer
init|=
operator|new
name|ColocInFaultObserver
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|observer
operator|.
name|onMessage
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//Handle Response
name|ex
operator|.
name|setInMessage
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
name|PhaseManager
name|pm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PhaseManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|phases
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|pm
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
name|PRE_INVOKE
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
name|inMsg
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
name|chain
operator|.
name|doIntercept
argument_list|(
name|inMsg
argument_list|)
expr_stmt|;
block|}
name|ex
operator|.
name|put
argument_list|(
name|ClientImpl
operator|.
name|FINISHED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Message
name|getInBoundMessage
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
return|return
operator|(
name|ex
operator|.
name|getInFaultMessage
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|ex
operator|.
name|getInFaultMessage
argument_list|()
else|:
name|ex
operator|.
name|getInMessage
argument_list|()
return|;
block|}
specifier|protected
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{
name|colocObserver
operator|=
name|observer
expr_stmt|;
block|}
specifier|protected
name|Server
name|isColocated
parameter_list|(
name|List
argument_list|<
name|Server
argument_list|>
name|servers
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|BindingOperationInfo
name|boi
parameter_list|)
block|{
if|if
condition|(
name|servers
operator|!=
literal|null
condition|)
block|{
name|Service
name|senderService
init|=
name|endpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|EndpointInfo
name|senderEI
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
for|for
control|(
name|Server
name|s
range|:
name|servers
control|)
block|{
name|Endpoint
name|receiverEndpoint
init|=
name|s
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Service
name|receiverService
init|=
name|receiverEndpoint
operator|.
name|getService
argument_list|()
decl_stmt|;
name|EndpointInfo
name|receiverEI
init|=
name|receiverEndpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|receiverService
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|senderService
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|receiverEI
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|senderEI
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|//Check For Operation Match.
name|BindingOperationInfo
name|receiverOI
init|=
name|receiverEI
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|boi
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|receiverOI
operator|!=
literal|null
operator|&&
name|isCompatibleOperationInfo
argument_list|(
name|boi
argument_list|,
name|receiverOI
argument_list|)
condition|)
block|{
return|return
name|s
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|boolean
name|isSameOperationInfo
parameter_list|(
name|BindingOperationInfo
name|sender
parameter_list|,
name|BindingOperationInfo
name|receiver
parameter_list|)
block|{
return|return
name|ColocUtil
operator|.
name|isSameOperationInfo
argument_list|(
name|sender
operator|.
name|getOperationInfo
argument_list|()
argument_list|,
name|receiver
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isCompatibleOperationInfo
parameter_list|(
name|BindingOperationInfo
name|sender
parameter_list|,
name|BindingOperationInfo
name|receiver
parameter_list|)
block|{
return|return
name|ColocUtil
operator|.
name|isCompatibleOperationInfo
argument_list|(
name|sender
operator|.
name|getOperationInfo
argument_list|()
argument_list|,
name|receiver
operator|.
name|getOperationInfo
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setExchangeProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
block|{
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|ep
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
name|ep
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
name|ep
operator|.
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|bus
operator|==
literal|null
condition|?
name|BusFactory
operator|.
name|getDefaultBus
argument_list|(
literal|false
argument_list|)
else|:
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

