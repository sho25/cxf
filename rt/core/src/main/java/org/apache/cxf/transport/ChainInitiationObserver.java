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
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

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
name|interceptor
operator|.
name|InterceptorProvider
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
name|phase
operator|.
name|PhaseChainCache
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
name|ServiceImpl
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

begin_class
specifier|public
class|class
name|ChainInitiationObserver
implements|implements
name|MessageObserver
block|{
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
specifier|protected
name|Bus
name|bus
decl_stmt|;
specifier|private
name|PhaseChainCache
name|chainCache
init|=
operator|new
name|PhaseChainCache
argument_list|()
decl_stmt|;
specifier|public
name|ChainInitiationObserver
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
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
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
try|try
block|{
name|PhaseInterceptorChain
name|phaseChain
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|getInterceptorChain
argument_list|()
operator|instanceof
name|PhaseInterceptorChain
condition|)
block|{
name|phaseChain
operator|=
operator|(
name|PhaseInterceptorChain
operator|)
name|m
operator|.
name|getInterceptorChain
argument_list|()
expr_stmt|;
comment|// To make sure the phase chain is run by one thread once
synchronized|synchronized
init|(
name|phaseChain
init|)
block|{
if|if
condition|(
name|phaseChain
operator|.
name|getState
argument_list|()
operator|==
name|InterceptorChain
operator|.
name|State
operator|.
name|PAUSED
operator|||
name|phaseChain
operator|.
name|getState
argument_list|()
operator|==
name|InterceptorChain
operator|.
name|State
operator|.
name|SUSPENDED
condition|)
block|{
name|phaseChain
operator|.
name|resume
argument_list|()
expr_stmt|;
return|return;
block|}
block|}
block|}
name|Message
name|message
init|=
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
name|exchange
operator|=
operator|new
name|ExchangeImpl
argument_list|()
expr_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|setExchangeProperties
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|InterceptorProvider
name|dbp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpoint
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
name|dbp
operator|=
operator|(
name|InterceptorProvider
operator|)
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getDataBinding
argument_list|()
expr_stmt|;
block|}
comment|// setup chain
if|if
condition|(
name|dbp
operator|==
literal|null
condition|)
block|{
name|phaseChain
operator|=
name|chainCache
operator|.
name|get
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
argument_list|,
name|bus
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|phaseChain
operator|=
name|chainCache
operator|.
name|get
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
argument_list|,
name|bus
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|getBinding
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|dbp
operator|.
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setInterceptorChain
argument_list|(
name|phaseChain
argument_list|)
expr_stmt|;
name|phaseChain
operator|.
name|setFaultObserver
argument_list|(
name|endpoint
operator|.
name|getOutFaultObserver
argument_list|()
argument_list|)
expr_stmt|;
name|addToChain
argument_list|(
name|phaseChain
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|phaseChain
operator|.
name|doIntercept
argument_list|(
name|message
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
block|}
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
name|getInInterceptors
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
name|IN_INTERCEPTORS
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
name|getInInterceptors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getBinding
argument_list|()
return|;
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
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getDestination
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setDestination
argument_list|(
name|m
operator|.
name|getDestination
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|!=
literal|null
operator|&&
operator|(
name|endpoint
operator|.
name|getService
argument_list|()
operator|instanceof
name|ServiceImpl
operator|)
condition|)
block|{
name|EndpointInfo
name|endpointInfo
init|=
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
name|QName
name|serviceQName
init|=
name|endpointInfo
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_SERVICE
argument_list|,
name|serviceQName
argument_list|)
expr_stmt|;
name|QName
name|interfaceQName
init|=
name|endpointInfo
operator|.
name|getService
argument_list|()
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_INTERFACE
argument_list|,
name|interfaceQName
argument_list|)
expr_stmt|;
name|QName
name|portQName
init|=
name|endpointInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_PORT
argument_list|,
name|portQName
argument_list|)
expr_stmt|;
name|URI
name|wsdlDescription
init|=
name|endpointInfo
operator|.
name|getProperty
argument_list|(
literal|"URI"
argument_list|,
name|URI
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsdlDescription
operator|==
literal|null
condition|)
block|{
name|String
name|address
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
try|try
block|{
name|wsdlDescription
operator|=
operator|new
name|URI
argument_list|(
name|address
operator|+
literal|"?wsdl"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// do nothing
block|}
name|endpointInfo
operator|.
name|setProperty
argument_list|(
literal|"URI"
argument_list|,
name|wsdlDescription
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|put
argument_list|(
name|Message
operator|.
name|WSDL_DESCRIPTION
argument_list|,
name|wsdlDescription
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

