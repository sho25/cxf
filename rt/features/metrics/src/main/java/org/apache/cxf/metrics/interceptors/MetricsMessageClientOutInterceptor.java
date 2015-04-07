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
name|metrics
operator|.
name|interceptors
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|metrics
operator|.
name|ExchangeMetrics
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
name|metrics
operator|.
name|MetricsProvider
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
name|MetricsMessageClientOutInterceptor
extends|extends
name|AbstractMetricsInterceptor
block|{
specifier|public
name|MetricsMessageClientOutInterceptor
parameter_list|(
name|MetricsProvider
name|p
index|[]
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|SETUP
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|addBefore
argument_list|(
literal|"*"
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
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|ExchangeMetrics
name|ctx
init|=
name|getExchangeMetrics
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|CountingInputStream
name|newIn
init|=
operator|new
name|CountingInputStream
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|newIn
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|CountingInputStream
operator|.
name|class
argument_list|,
name|newIn
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//we now know the operation, start metrics for it
name|addOperationMetrics
argument_list|(
name|ctx
argument_list|,
name|message
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBindingOperationInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
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
name|ex
operator|!=
literal|null
condition|)
block|{
name|FaultMode
name|fm
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|FaultMode
operator|.
name|RUNTIME_FAULT
argument_list|)
expr_stmt|;
name|stop
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|,
name|fm
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stop
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

