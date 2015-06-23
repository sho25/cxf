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
name|TraceInfo
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|htrace
operator|.
name|Tracer
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
name|impl
operator|.
name|MilliSpan
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractHTraceProvider
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
name|AbstractHTraceProvider
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
name|AbstractHTraceProvider
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
parameter_list|)
block|{
comment|// Try to extract the Trace Id value from the request header
specifier|final
name|long
name|traceId
init|=
name|getFirstValueOrDefault
argument_list|(
name|requestHeaders
argument_list|,
name|getTraceIdHeader
argument_list|()
argument_list|,
name|Tracer
operator|.
name|DONT_TRACE
operator|.
name|traceId
argument_list|)
decl_stmt|;
comment|// Try to extract the Span Id value from the request header
specifier|final
name|long
name|spanId
init|=
name|getFirstValueOrDefault
argument_list|(
name|requestHeaders
argument_list|,
name|getSpanIdHeader
argument_list|()
argument_list|,
name|Tracer
operator|.
name|DONT_TRACE
operator|.
name|spanId
argument_list|)
decl_stmt|;
if|if
condition|(
name|traceId
operator|==
name|Tracer
operator|.
name|DONT_TRACE
operator|.
name|traceId
operator|||
name|spanId
operator|==
name|Tracer
operator|.
name|DONT_TRACE
operator|.
name|spanId
condition|)
block|{
return|return
name|Trace
operator|.
name|startSpan
argument_list|(
name|path
argument_list|,
operator|(
name|Sampler
argument_list|<
name|TraceInfo
argument_list|>
operator|)
name|sampler
argument_list|,
operator|new
name|TraceInfo
argument_list|(
name|traceId
argument_list|,
name|spanId
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Trace
operator|.
name|startSpan
argument_list|(
name|path
argument_list|,
operator|new
name|MilliSpan
operator|.
name|Builder
argument_list|()
operator|.
name|spanId
argument_list|(
name|spanId
argument_list|)
operator|.
name|traceId
argument_list|(
name|traceId
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
return|;
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
name|TraceScope
name|span
parameter_list|)
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
if|if
condition|(
name|requestHeaders
operator|.
name|containsKey
argument_list|(
name|traceIdHeader
argument_list|)
operator|&&
name|requestHeaders
operator|.
name|containsKey
argument_list|(
name|spanIdHeader
argument_list|)
condition|)
block|{
name|responseHeaders
operator|.
name|put
argument_list|(
name|traceIdHeader
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|requestHeaders
operator|.
name|get
argument_list|(
name|traceIdHeader
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|responseHeaders
operator|.
name|put
argument_list|(
name|spanIdHeader
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|requestHeaders
operator|.
name|get
argument_list|(
name|spanIdHeader
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|span
operator|!=
literal|null
condition|)
block|{
name|span
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|Long
name|getFirstValueOrDefault
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
name|headers
parameter_list|,
specifier|final
name|String
name|header
parameter_list|,
specifier|final
name|long
name|defaultValue
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|value
init|=
name|headers
operator|.
name|get
argument_list|(
name|header
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
try|try
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|value
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Unable to parse '%s' header value to long number"
argument_list|,
name|header
argument_list|)
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|defaultValue
return|;
block|}
block|}
end_class

end_unit
