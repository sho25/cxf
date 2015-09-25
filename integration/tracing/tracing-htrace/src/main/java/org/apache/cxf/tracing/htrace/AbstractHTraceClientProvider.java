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
name|tracing
operator|.
name|htrace
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|tracing
operator|.
name|AbstractTracingProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|Sampler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|Trace
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|TraceScope
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractHTraceClientProvider
extends|extends
name|AbstractTracingProvider
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractHTraceClientProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|TRACE_SPAN
init|=
literal|"org.apache.cxf.tracing.htrace.span"
decl_stmt|;
specifier|private
specifier|final
name|Sampler
argument_list|<
name|?
argument_list|>
name|sampler
decl_stmt|;
specifier|public
name|AbstractHTraceClientProvider
parameter_list|(
specifier|final
name|Sampler
argument_list|<
name|?
argument_list|>
name|sampler
parameter_list|)
block|{
name|this
operator|.
name|sampler
operator|=
name|sampler
expr_stmt|;
block|}
specifier|protected
name|TraceScope
name|startTraceSpan
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|requestHeaders
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|method
parameter_list|)
block|{
name|Span
name|span
init|=
name|Trace
operator|.
name|currentSpan
argument_list|()
decl_stmt|;
name|TraceScope
name|scope
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|span
operator|==
literal|null
condition|)
block|{
name|scope
operator|=
name|Trace
operator|.
name|startSpan
argument_list|(
name|buildSpanDescription
argument_list|(
name|path
argument_list|,
name|method
argument_list|)
argument_list|,
name|sampler
argument_list|)
expr_stmt|;
name|span
operator|=
name|scope
operator|.
name|getSpan
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|span
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|traceIdHeader
init|=
name|getTraceIdHeader
argument_list|()
decl_stmt|;
specifier|final
name|String
name|spanIdHeader
init|=
name|getSpanIdHeader
argument_list|()
decl_stmt|;
comment|// Transfer tracing headers into the response headers
name|requestHeaders
operator|.
name|put
argument_list|(
name|traceIdHeader
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|span
operator|.
name|getTraceId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|requestHeaders
operator|.
name|put
argument_list|(
name|spanIdHeader
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|span
operator|.
name|getSpanId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// In case of asynchronous client invocation, the span should be detached as JAX-RS
comment|// client request / response filters are going to be executed in different threads.
if|if
condition|(
name|isAsyncInvocation
argument_list|()
operator|&&
name|scope
operator|!=
literal|null
condition|)
block|{
name|scope
operator|.
name|detach
argument_list|()
expr_stmt|;
block|}
return|return
name|scope
return|;
block|}
specifier|private
name|boolean
name|isAsyncInvocation
parameter_list|()
block|{
return|return
operator|!
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|isSynchronous
argument_list|()
return|;
block|}
specifier|protected
name|void
name|stopTraceSpan
parameter_list|(
specifier|final
name|TraceScope
name|scope
parameter_list|)
block|{
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
comment|// If the client invocation was asynchronous , the trace scope has been created
comment|// in another thread and should be re-attached to the current one.
if|if
condition|(
name|scope
operator|.
name|isDetached
argument_list|()
condition|)
block|{
specifier|final
name|TraceScope
name|continueSpan
init|=
name|Trace
operator|.
name|continueSpan
argument_list|(
name|scope
operator|.
name|getSpan
argument_list|()
argument_list|)
decl_stmt|;
name|continueSpan
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|scope
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

