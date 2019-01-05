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
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|Tracing
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tracing
operator|.
name|Traceable
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
name|TracerContext
import|;
end_import

begin_class
specifier|public
class|class
name|BraveTracerContext
implements|implements
name|TracerContext
block|{
specifier|private
specifier|final
name|HttpTracing
name|brave
decl_stmt|;
specifier|private
specifier|final
name|Tracer
name|tracer
decl_stmt|;
specifier|private
specifier|final
name|Span
name|continuationSpan
decl_stmt|;
specifier|public
name|BraveTracerContext
parameter_list|(
specifier|final
name|HttpTracing
name|brave
parameter_list|)
block|{
name|this
argument_list|(
name|brave
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BraveTracerContext
parameter_list|(
specifier|final
name|HttpTracing
name|brave
parameter_list|,
specifier|final
name|Span
name|continuationSpan
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
name|tracer
operator|=
name|brave
operator|.
name|tracing
argument_list|()
operator|.
name|tracer
argument_list|()
expr_stmt|;
name|this
operator|.
name|continuationSpan
operator|=
name|continuationSpan
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|TraceScope
name|startSpan
parameter_list|(
specifier|final
name|String
name|description
parameter_list|)
block|{
return|return
operator|new
name|TraceScope
argument_list|(
name|brave
argument_list|,
name|tracer
operator|.
name|nextSpan
argument_list|()
operator|.
name|name
argument_list|(
name|description
argument_list|)
operator|.
name|start
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|continueSpan
parameter_list|(
specifier|final
name|Traceable
argument_list|<
name|T
argument_list|>
name|traceable
parameter_list|)
throws|throws
name|Exception
block|{
name|SpanInScope
name|scope
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tracer
operator|.
name|currentSpan
argument_list|()
operator|==
literal|null
operator|&&
name|continuationSpan
operator|!=
literal|null
condition|)
block|{
name|scope
operator|=
name|tracer
operator|.
name|withSpanInScope
argument_list|(
name|continuationSpan
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|traceable
operator|.
name|call
argument_list|(
operator|new
name|BraveTracerContext
argument_list|(
name|brave
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|continuationSpan
operator|!=
literal|null
operator|&&
name|scope
operator|!=
literal|null
condition|)
block|{
name|scope
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|String
name|description
parameter_list|,
specifier|final
name|Traceable
argument_list|<
name|T
argument_list|>
name|traceable
parameter_list|)
block|{
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|traceable
operator|.
name|call
argument_list|(
operator|new
name|BraveTracerContext
argument_list|(
name|brave
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|// Carry over parent from the current thread
specifier|final
name|Span
name|parent
init|=
name|tracer
operator|.
name|currentSpan
argument_list|()
decl_stmt|;
return|return
parameter_list|()
lambda|->
block|{
try|try
init|(
name|TraceScope
name|span
init|=
name|newOrChildSpan
argument_list|(
name|description
argument_list|,
name|parent
argument_list|)
init|)
block|{
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|annotate
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
specifier|final
name|Span
name|current
init|=
name|tracer
operator|.
name|currentSpan
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|current
operator|.
name|tag
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|timeline
parameter_list|(
name|String
name|message
parameter_list|)
block|{
specifier|final
name|Span
name|current
init|=
name|tracer
operator|.
name|currentSpan
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|current
operator|.
name|annotate
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|HttpTracing
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|brave
return|;
block|}
elseif|else
if|if
condition|(
name|Tracing
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|brave
operator|.
name|tracing
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|Tracer
operator|.
name|class
operator|.
name|equals
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|tracer
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The class is '"
operator|+
name|clazz
operator|+
literal|"'not supported and cannot be unwrapped"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|TraceScope
name|newOrChildSpan
parameter_list|(
specifier|final
name|String
name|description
parameter_list|,
specifier|final
name|Span
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|TraceScope
argument_list|(
name|brave
argument_list|,
name|tracer
operator|.
name|newTrace
argument_list|()
operator|.
name|name
argument_list|(
name|description
argument_list|)
operator|.
name|start
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|new
name|TraceScope
argument_list|(
name|brave
argument_list|,
name|tracer
operator|.
name|newChild
argument_list|(
name|parent
operator|.
name|context
argument_list|()
argument_list|)
operator|.
name|name
argument_list|(
name|description
argument_list|)
operator|.
name|start
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

