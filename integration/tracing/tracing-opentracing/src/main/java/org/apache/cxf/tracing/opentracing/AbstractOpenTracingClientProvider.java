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
name|opentracing
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
name|cxf
operator|.
name|tracing
operator|.
name|opentracing
operator|.
name|internal
operator|.
name|TextMapInjectAdapter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Tracer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|propagation
operator|.
name|Format
operator|.
name|Builtin
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|tag
operator|.
name|Tags
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractOpenTracingClientProvider
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
name|AbstractOpenTracingClientProvider
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
literal|"org.apache.cxf.tracing.client.opentracing.span"
decl_stmt|;
specifier|private
specifier|final
name|Tracer
name|tracer
decl_stmt|;
specifier|public
name|AbstractOpenTracingClientProvider
parameter_list|(
specifier|final
name|Tracer
name|tracer
parameter_list|)
block|{
name|this
operator|.
name|tracer
operator|=
name|tracer
expr_stmt|;
block|}
specifier|protected
name|TraceScopeHolder
argument_list|<
name|TraceScope
argument_list|>
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
name|URI
name|uri
parameter_list|,
name|String
name|method
parameter_list|)
block|{
specifier|final
name|Span
name|parent
init|=
name|tracer
operator|.
name|activeSpan
argument_list|()
decl_stmt|;
name|Scope
name|scope
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|scope
operator|=
name|tracer
operator|.
name|buildSpan
argument_list|(
name|buildSpanDescription
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|,
name|method
argument_list|)
argument_list|)
operator|.
name|startActive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scope
operator|=
name|tracer
operator|.
name|buildSpan
argument_list|(
name|buildSpanDescription
argument_list|(
name|uri
operator|.
name|toString
argument_list|()
argument_list|,
name|method
argument_list|)
argument_list|)
operator|.
name|asChildOf
argument_list|(
name|parent
argument_list|)
operator|.
name|startActive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// Set additional tags
name|scope
operator|.
name|span
argument_list|()
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|HTTP_METHOD
operator|.
name|getKey
argument_list|()
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|scope
operator|.
name|span
argument_list|()
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|HTTP_URL
operator|.
name|getKey
argument_list|()
argument_list|,
name|uri
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|inject
argument_list|(
name|scope
operator|.
name|span
argument_list|()
operator|.
name|context
argument_list|()
argument_list|,
name|Builtin
operator|.
name|HTTP_HEADERS
argument_list|,
operator|new
name|TextMapInjectAdapter
argument_list|(
name|requestHeaders
argument_list|)
argument_list|)
expr_stmt|;
comment|// In case of asynchronous client invocation, the span should be detached as JAX-RS
comment|// client request / response filters are going to be executed in different threads.
name|Span
name|span
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isAsyncInvocation
argument_list|()
condition|)
block|{
name|span
operator|=
name|scope
operator|.
name|span
argument_list|()
expr_stmt|;
name|scope
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|TraceScopeHolder
argument_list|<
name|TraceScope
argument_list|>
argument_list|(
operator|new
name|TraceScope
argument_list|(
name|span
argument_list|,
name|scope
argument_list|)
argument_list|,
name|span
operator|!=
literal|null
comment|/* detached */
argument_list|)
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
name|TraceScopeHolder
argument_list|<
name|TraceScope
argument_list|>
name|holder
parameter_list|,
specifier|final
name|int
name|responseStatus
parameter_list|)
block|{
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
return|return;
block|}
specifier|final
name|TraceScope
name|traceScope
init|=
name|holder
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
name|traceScope
operator|!=
literal|null
condition|)
block|{
name|Span
name|span
init|=
name|traceScope
operator|.
name|getSpan
argument_list|()
decl_stmt|;
name|Scope
name|scope
init|=
name|traceScope
operator|.
name|getScope
argument_list|()
decl_stmt|;
comment|// If the client invocation was asynchronous , the trace span has been created
comment|// in another thread and should be re-attached to the current one.
if|if
condition|(
name|holder
operator|.
name|isDetached
argument_list|()
condition|)
block|{
name|scope
operator|=
name|tracer
operator|.
name|scopeManager
argument_list|()
operator|.
name|activate
argument_list|(
name|span
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|scope
operator|.
name|span
argument_list|()
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|HTTP_STATUS
operator|.
name|getKey
argument_list|()
argument_list|,
name|responseStatus
argument_list|)
expr_stmt|;
name|scope
operator|.
name|span
argument_list|()
operator|.
name|finish
argument_list|()
expr_stmt|;
name|scope
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

