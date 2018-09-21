begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|tracing
operator|.
name|server
package|;
end_package

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
name|context
operator|.
name|slf4j
operator|.
name|MDCScopeDecorator
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
name|ThreadLocalCurrentTraceContext
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|AsyncReporter
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|Sender
import|;
end_import

begin_import
import|import
name|zipkin2
operator|.
name|reporter
operator|.
name|okhttp3
operator|.
name|OkHttpSender
import|;
end_import

begin_class
specifier|public
class|class
name|CatalogTracing
block|{
specifier|private
specifier|volatile
name|HttpTracing
name|httpTracing
decl_stmt|;
specifier|private
specifier|final
name|String
name|serviceName
decl_stmt|;
specifier|public
name|CatalogTracing
parameter_list|(
specifier|final
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
specifier|public
name|HttpTracing
name|getHttpTracing
parameter_list|()
block|{
name|HttpTracing
name|result
init|=
name|httpTracing
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|result
operator|=
name|httpTracing
expr_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|httpTracing
operator|=
name|result
operator|=
name|createHttpTracing
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|HttpTracing
name|createHttpTracing
parameter_list|()
block|{
specifier|final
name|Sender
name|sender
init|=
name|OkHttpSender
operator|.
name|create
argument_list|(
literal|"http://localhost:9411/api/v2/spans"
argument_list|)
decl_stmt|;
specifier|final
name|AsyncReporter
argument_list|<
name|Span
argument_list|>
name|reporter
init|=
name|AsyncReporter
operator|.
name|create
argument_list|(
name|sender
argument_list|)
decl_stmt|;
specifier|final
name|Tracing
name|tracing
init|=
name|Tracing
operator|.
name|newBuilder
argument_list|()
operator|.
name|localServiceName
argument_list|(
name|serviceName
argument_list|)
operator|.
name|currentTraceContext
argument_list|(
name|ThreadLocalCurrentTraceContext
operator|.
name|newBuilder
argument_list|()
operator|.
name|addScopeDecorator
argument_list|(
name|MDCScopeDecorator
operator|.
name|create
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|spanReporter
argument_list|(
name|reporter
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
name|HttpTracing
operator|.
name|create
argument_list|(
name|tracing
argument_list|)
return|;
block|}
block|}
end_class

end_unit

