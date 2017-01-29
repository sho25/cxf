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
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|Brave
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|ServerSpan
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|http
operator|.
name|BraveHttpHeaders
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|http
operator|.
name|HttpServerRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|http
operator|.
name|HttpServerRequestAdapter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|http
operator|.
name|HttpServerResponseAdapter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|http
operator|.
name|SpanNameProvider
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
name|tracing
operator|.
name|AbstractTracingProvider
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractBraveProvider
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
name|AbstractBraveProvider
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
literal|"org.apache.cxf.tracing.brave.span"
decl_stmt|;
specifier|protected
specifier|final
name|Brave
name|brave
decl_stmt|;
specifier|protected
specifier|final
name|SpanNameProvider
name|spanNameProvider
decl_stmt|;
specifier|protected
name|AbstractBraveProvider
parameter_list|(
specifier|final
name|Brave
name|brave
parameter_list|)
block|{
name|this
argument_list|(
name|brave
argument_list|,
operator|new
name|ServerSpanNameProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractBraveProvider
parameter_list|(
specifier|final
name|Brave
name|brave
parameter_list|,
specifier|final
name|SpanNameProvider
name|spanNameProvider
parameter_list|)
block|{
name|this
operator|.
name|brave
operator|=
name|brave
expr_stmt|;
name|this
operator|.
name|spanNameProvider
operator|=
name|spanNameProvider
expr_stmt|;
block|}
specifier|protected
name|TraceScopeHolder
argument_list|<
name|ServerSpan
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
name|HttpServerRequest
name|request
init|=
operator|new
name|HttpServerRequest
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHttpMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHttpHeaderValue
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|value
init|=
name|requestHeaders
operator|.
name|get
argument_list|(
name|headerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|value
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|brave
operator|.
name|serverRequestInterceptor
argument_list|()
operator|.
name|handle
argument_list|(
operator|new
name|HttpServerRequestAdapter
argument_list|(
name|request
argument_list|,
name|spanNameProvider
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|ServerSpan
name|serverSpan
init|=
name|brave
operator|.
name|serverSpanThreadBinder
argument_list|()
operator|.
name|getCurrentServerSpan
argument_list|()
decl_stmt|;
comment|// If the service resource is using asynchronous processing mode, the trace
comment|// scope will be closed in another thread and as such should be detached.
name|boolean
name|detached
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|isAsyncResponse
argument_list|()
condition|)
block|{
name|brave
operator|.
name|serverSpanThreadBinder
argument_list|()
operator|.
name|setCurrentSpan
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|propagateContinuationSpan
argument_list|(
name|serverSpan
argument_list|)
expr_stmt|;
name|detached
operator|=
literal|true
expr_stmt|;
block|}
return|return
operator|new
name|TraceScopeHolder
argument_list|<
name|ServerSpan
argument_list|>
argument_list|(
name|serverSpan
argument_list|,
name|detached
argument_list|)
return|;
block|}
specifier|private
name|void
name|transferRequestHeader
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|responseHeaders
parameter_list|,
specifier|final
name|BraveHttpHeaders
name|header
parameter_list|)
block|{
if|if
condition|(
name|requestHeaders
operator|.
name|containsKey
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|responseHeaders
operator|.
name|put
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|requestHeaders
operator|.
name|get
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|stopTraceSpan
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|responseHeaders
parameter_list|,
specifier|final
name|int
name|responseStatus
parameter_list|,
specifier|final
name|TraceScopeHolder
argument_list|<
name|ServerSpan
argument_list|>
name|holder
parameter_list|)
block|{
comment|// Transfer tracing headers into the response headers
name|transferRequestHeader
argument_list|(
name|requestHeaders
argument_list|,
name|responseHeaders
argument_list|,
name|BraveHttpHeaders
operator|.
name|SpanId
argument_list|)
expr_stmt|;
name|transferRequestHeader
argument_list|(
name|requestHeaders
argument_list|,
name|responseHeaders
argument_list|,
name|BraveHttpHeaders
operator|.
name|Sampled
argument_list|)
expr_stmt|;
name|transferRequestHeader
argument_list|(
name|requestHeaders
argument_list|,
name|responseHeaders
argument_list|,
name|BraveHttpHeaders
operator|.
name|ParentSpanId
argument_list|)
expr_stmt|;
name|transferRequestHeader
argument_list|(
name|requestHeaders
argument_list|,
name|responseHeaders
argument_list|,
name|BraveHttpHeaders
operator|.
name|TraceId
argument_list|)
expr_stmt|;
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
name|ServerSpan
name|span
init|=
name|holder
operator|.
name|getScope
argument_list|()
decl_stmt|;
if|if
condition|(
name|span
operator|!=
literal|null
condition|)
block|{
comment|// If the service resource is using asynchronous processing mode, the trace
comment|// scope has been created in another thread and should be re-attached to the current
comment|// one.
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
name|serverSpanThreadBinder
argument_list|()
operator|.
name|setCurrentSpan
argument_list|(
name|span
argument_list|)
expr_stmt|;
block|}
specifier|final
name|HttpResponse
name|response
init|=
parameter_list|()
lambda|->
name|responseStatus
decl_stmt|;
name|brave
operator|.
name|serverResponseInterceptor
argument_list|()
operator|.
name|handle
argument_list|(
operator|new
name|HttpServerResponseAdapter
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|propagateContinuationSpan
parameter_list|(
specifier|final
name|ServerSpan
name|continuationScope
parameter_list|)
block|{
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|put
argument_list|(
name|ServerSpan
operator|.
name|class
argument_list|,
name|continuationScope
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isAsyncResponse
parameter_list|()
block|{
return|return
operator|!
name|PhaseInterceptorChain
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
block|}
end_class

end_unit

