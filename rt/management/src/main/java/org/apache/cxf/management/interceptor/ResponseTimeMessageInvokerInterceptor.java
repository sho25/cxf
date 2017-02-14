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
name|ServiceInvokerInterceptor
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
name|Phase
import|;
end_import

begin_comment
comment|/* When the message get from the server side  * The exchange.isOneWay() is workable when the message  * handler by the binging interceptor  * */
end_comment

begin_class
specifier|public
class|class
name|ResponseTimeMessageInvokerInterceptor
extends|extends
name|AbstractMessageResponseTimeInterceptor
block|{
specifier|public
name|ResponseTimeMessageInvokerInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
comment|// this interceptor should be add before the serviceInvokerInterceptor
name|addBefore
argument_list|(
name|ServiceInvokerInterceptor
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
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ResponseTimeMessageInvokerEndingInteceptor
argument_list|()
argument_list|)
expr_stmt|;
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
name|isServiceCounterEnabled
argument_list|(
name|ex
argument_list|)
operator|&&
operator|!
name|forceDisabled
condition|)
block|{
name|ex
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|message
operator|.
name|get
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ex
operator|.
name|isOneWay
argument_list|()
operator|&&
operator|!
name|isClient
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|endHandlingMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
class|class
name|ResponseTimeMessageInvokerEndingInteceptor
extends|extends
name|AbstractMessageResponseTimeInterceptor
block|{
name|ResponseTimeMessageInvokerEndingInteceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|INVOKE
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|ServiceInvokerInterceptor
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
if|if
condition|(
name|ex
operator|.
name|isOneWay
argument_list|()
operator|&&
operator|!
name|isClient
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|setOneWayMessage
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

