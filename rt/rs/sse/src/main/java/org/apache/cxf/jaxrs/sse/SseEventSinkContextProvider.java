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
name|jaxrs
operator|.
name|sse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|AsyncResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|sse
operator|.
name|OutboundSseEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|sse
operator|.
name|SseEventSink
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
name|util
operator|.
name|PropertyUtils
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
name|interceptor
operator|.
name|Interceptor
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
name|ext
operator|.
name|ContextProvider
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
name|impl
operator|.
name|AsyncResponseImpl
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
name|provider
operator|.
name|ServerProviderFactory
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
name|sse
operator|.
name|interceptor
operator|.
name|SseInterceptor
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
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
specifier|public
class|class
name|SseEventSinkContextProvider
implements|implements
name|ContextProvider
argument_list|<
name|SseEventSink
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|SseEventSink
name|createContext
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
specifier|final
name|HttpServletRequest
name|request
init|=
operator|(
name|HttpServletRequest
operator|)
name|message
operator|.
name|get
argument_list|(
name|AbstractHTTPDestination
operator|.
name|HTTP_REQUEST
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to retrieve HTTP request from the context"
argument_list|)
throw|;
block|}
specifier|final
name|MessageBodyWriter
argument_list|<
name|OutboundSseEvent
argument_list|>
name|writer
init|=
operator|new
name|OutboundSseEventBodyWriter
argument_list|(
name|ServerProviderFactory
operator|.
name|getInstance
argument_list|(
name|message
argument_list|)
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|AsyncResponse
name|async
init|=
operator|new
name|AsyncResponseImpl
argument_list|(
name|message
argument_list|)
decl_stmt|;
specifier|final
name|Integer
name|bufferSize
init|=
name|PropertyUtils
operator|.
name|getInteger
argument_list|(
name|message
argument_list|,
name|SseEventSinkImpl
operator|.
name|BUFFER_SIZE_PROPERTY
argument_list|)
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|interceptors
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|IN_INTERCEPTORS
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|chain
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|interceptors
operator|!=
literal|null
condition|)
block|{
name|chain
operator|.
name|addAll
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
block|}
name|chain
operator|.
name|add
argument_list|(
operator|new
name|SseInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|IN_INTERCEPTORS
argument_list|,
name|chain
argument_list|)
expr_stmt|;
if|if
condition|(
name|bufferSize
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|SseEventSinkImpl
argument_list|(
name|writer
argument_list|,
name|async
argument_list|,
name|request
operator|.
name|getAsyncContext
argument_list|()
argument_list|,
name|bufferSize
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|SseEventSinkImpl
argument_list|(
name|writer
argument_list|,
name|async
argument_list|,
name|request
operator|.
name|getAsyncContext
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

