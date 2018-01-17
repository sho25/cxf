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
name|brave
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
name|brave
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|Tracer
operator|.
name|SpanInScope
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|http
operator|.
name|HttpClientAdapter
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|http
operator|.
name|HttpClientHandler
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|http
operator|.
name|HttpTracing
import|;
end_import

begin_import
import|import
name|brave
operator|.
name|propagation
operator|.
name|Propagation
operator|.
name|Setter
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
name|brave
operator|.
name|internal
operator|.
name|HttpAdapterFactory
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
name|brave
operator|.
name|internal
operator|.
name|HttpAdapterFactory
operator|.
name|Request
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
name|brave
operator|.
name|internal
operator|.
name|HttpAdapterFactory
operator|.
name|Response
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
name|brave
operator|.
name|internal
operator|.
name|HttpClientAdapterFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractBraveClientProvider
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
name|AbstractBraveClientProvider
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
literal|"org.apache.cxf.tracing.client.brave.span"
decl_stmt|;
specifier|private
specifier|final
name|HttpTracing
name|brave
decl_stmt|;
specifier|public
name|AbstractBraveClientProvider
parameter_list|(
specifier|final
name|HttpTracing
name|brave
parameter_list|)
block|{
name|this
operator|.
name|brave
operator|=
name|brave
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
name|Request
name|request
init|=
name|HttpAdapterFactory
operator|.
name|request
argument_list|(
name|requestHeaders
argument_list|,
name|uri
argument_list|,
name|method
argument_list|)
decl_stmt|;
specifier|final
name|HttpClientAdapter
argument_list|<
name|Request
argument_list|,
name|?
argument_list|>
name|adapter
init|=
name|HttpClientAdapterFactory
operator|.
name|create
argument_list|(
name|request
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|HttpClientHandler
argument_list|<
name|Request
argument_list|,
name|?
argument_list|>
name|handler
init|=
name|HttpClientHandler
operator|.
name|create
argument_list|(
name|brave
argument_list|,
name|adapter
argument_list|)
decl_stmt|;
specifier|final
name|Span
name|span
init|=
name|handler
operator|.
name|handleSend
argument_list|(
name|brave
operator|.
name|tracing
argument_list|()
operator|.
name|propagation
argument_list|()
operator|.
name|injector
argument_list|(
name|inject
argument_list|(
name|requestHeaders
argument_list|)
argument_list|)
argument_list|,
name|request
argument_list|)
decl_stmt|;
comment|// In case of asynchronous client invocation, the span should be detached as JAX-RS
comment|// client request / response filters are going to be executed in different threads.
name|SpanInScope
name|scope
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isAsyncInvocation
argument_list|()
operator|&&
name|span
operator|!=
literal|null
condition|)
block|{
name|scope
operator|=
name|brave
operator|.
name|tracing
argument_list|()
operator|.
name|tracer
argument_list|()
operator|.
name|withSpanInScope
argument_list|(
name|span
argument_list|)
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
name|scope
operator|==
literal|null
comment|/* detached */
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|C
parameter_list|>
name|Setter
argument_list|<
name|C
argument_list|,
name|String
argument_list|>
name|inject
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
parameter_list|)
block|{
return|return
parameter_list|(
name|carrier
parameter_list|,
name|key
parameter_list|,
name|value
parameter_list|)
lambda|->
block|{
if|if
condition|(
operator|!
name|requestHeaders
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|requestHeaders
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|scope
init|=
name|holder
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
name|scope
operator|!=
literal|null
condition|)
block|{
try|try
block|{
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
name|brave
operator|.
name|tracing
argument_list|()
operator|.
name|tracer
argument_list|()
operator|.
name|joinSpan
argument_list|(
name|scope
operator|.
name|getSpan
argument_list|()
operator|.
name|context
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Response
name|response
init|=
name|HttpAdapterFactory
operator|.
name|response
argument_list|(
name|responseStatus
argument_list|)
decl_stmt|;
specifier|final
name|HttpClientAdapter
argument_list|<
name|?
argument_list|,
name|Response
argument_list|>
name|adapter
init|=
name|HttpClientAdapterFactory
operator|.
name|create
argument_list|(
name|response
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|HttpClientHandler
argument_list|<
name|?
argument_list|,
name|Response
argument_list|>
name|handler
init|=
name|HttpClientHandler
operator|.
name|create
argument_list|(
name|brave
argument_list|,
name|adapter
argument_list|)
decl_stmt|;
name|handler
operator|.
name|handleReceive
argument_list|(
name|response
argument_list|,
literal|null
argument_list|,
name|scope
operator|.
name|getSpan
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
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

