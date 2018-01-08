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
name|SortedSet
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|service
operator|.
name|model
operator|.
name|BindingFaultInfo
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
specifier|abstract
class|class
name|AbstractFaultChainInitiatorObserver
implements|implements
name|MessageObserver
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
name|AbstractFaultChainInitiatorObserver
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|ClassLoader
name|loader
decl_stmt|;
specifier|public
name|AbstractFaultChainInitiatorObserver
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
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|loader
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
assert|assert
literal|null
operator|!=
name|message
assert|;
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|Message
name|faultMessage
init|=
literal|null
decl_stmt|;
comment|// now that we have switched over to the fault chain,
comment|// prevent any further operations on the in/out message
if|if
condition|(
name|isOutboundObserver
argument_list|()
condition|)
block|{
name|Exception
name|ex
init|=
name|message
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
operator|!
operator|(
name|ex
operator|instanceof
name|Fault
operator|)
condition|)
block|{
name|ex
operator|=
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
name|FaultMode
name|mode
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
name|faultMessage
operator|=
name|exchange
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|faultMessage
condition|)
block|{
name|faultMessage
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|faultMessage
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|faultMessage
operator|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
block|}
name|faultMessage
operator|.
name|setContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mode
condition|)
block|{
name|faultMessage
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
comment|//CXF-3981
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context.inbound"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|faultMessage
operator|.
name|put
argument_list|(
literal|"javax.xml.ws.addressing.context.inbound"
argument_list|,
name|message
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context.inbound"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setOutMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOutFaultMessage
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|get
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|faultMessage
operator|.
name|put
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|,
name|message
operator|.
name|get
argument_list|(
name|BindingFaultInfo
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|faultMessage
operator|=
name|message
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInFaultMessage
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
block|}
comment|// setup chain
name|PhaseInterceptorChain
name|chain
init|=
operator|new
name|PhaseInterceptorChain
argument_list|(
name|getPhases
argument_list|()
argument_list|)
decl_stmt|;
name|initializeInterceptors
argument_list|(
name|faultMessage
operator|.
name|getExchange
argument_list|()
argument_list|,
name|chain
argument_list|)
expr_stmt|;
name|faultMessage
operator|.
name|setInterceptorChain
argument_list|(
name|chain
argument_list|)
expr_stmt|;
try|try
block|{
name|chain
operator|.
name|doIntercept
argument_list|(
name|faultMessage
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|exc
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"ERROR_DURING_ERROR_PROCESSING"
argument_list|,
name|exc
argument_list|)
expr_stmt|;
throw|throw
name|exc
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exc
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"ERROR_DURING_ERROR_PROCESSING"
argument_list|,
name|exc
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|exc
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|abstract
name|boolean
name|isOutboundObserver
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|SortedSet
argument_list|<
name|Phase
argument_list|>
name|getPhases
parameter_list|()
function_decl|;
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
block|{      }
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
block|}
end_class

end_unit

