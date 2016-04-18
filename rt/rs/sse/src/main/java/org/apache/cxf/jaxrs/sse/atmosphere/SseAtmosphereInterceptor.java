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
operator|.
name|atmosphere
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|atmosphere
operator|.
name|cpr
operator|.
name|Action
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AsyncIOInterceptorAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AsyncIOWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereInterceptorWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResourceEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResourceEventListenerAdapter
operator|.
name|OnPreSuspend
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|interceptor
operator|.
name|AllowInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|interceptor
operator|.
name|SSEAtmosphereInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|util
operator|.
name|Utils
import|;
end_import

begin_import
import|import static
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
name|OutboundSseEventBodyWriter
operator|.
name|SERVER_SENT_EVENTS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|ApplicationConfig
operator|.
name|PROPERTY_USE_STREAM
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|FrameworkConfig
operator|.
name|CALLBACK_JAVASCRIPT_PROTOCOL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|FrameworkConfig
operator|.
name|CONTAINER_RESPONSE
import|;
end_import

begin_comment
comment|/**  * Most of this class implementation is borrowed from SSEAtmosphereInterceptor. The original  * implementation does two things which do not fit well into SSE support:  *  - closes the response stream (overridden by SseAtmosphereInterceptorWriter)  *  - wraps the whatever object is being written to SSE payload (overridden using   *    the complete SSE protocol)   */
end_comment

begin_class
specifier|public
class|class
name|SseAtmosphereInterceptor
extends|extends
name|SSEAtmosphereInterceptor
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|SseAtmosphereInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|PADDING
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PADDING_TEXT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|END
init|=
literal|"\r\n\r\n"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
static|static
block|{
name|StringBuffer
name|whitespace
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2000
condition|;
name|i
operator|++
control|)
block|{
name|whitespace
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|whitespace
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|PADDING_TEXT
operator|=
name|whitespace
operator|.
name|toString
argument_list|()
expr_stmt|;
name|PADDING
operator|=
name|PADDING_TEXT
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|writePadding
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|)
block|{
if|if
condition|(
name|response
operator|.
name|request
argument_list|()
operator|!=
literal|null
operator|&&
name|response
operator|.
name|request
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"paddingWritten"
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|response
operator|.
name|setContentType
argument_list|(
name|SERVER_SENT_EVENTS
argument_list|)
expr_stmt|;
name|response
operator|.
name|setCharacterEncoding
argument_list|(
literal|"utf-8"
argument_list|)
expr_stmt|;
name|boolean
name|isUsingStream
init|=
operator|(
name|Boolean
operator|)
name|response
operator|.
name|request
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|PROPERTY_USE_STREAM
argument_list|)
decl_stmt|;
if|if
condition|(
name|isUsingStream
condition|)
block|{
try|try
block|{
name|OutputStream
name|stream
init|=
name|response
operator|.
name|getResponse
argument_list|()
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|stream
operator|.
name|write
argument_list|(
name|PADDING
argument_list|)
expr_stmt|;
name|stream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"SSE may not work"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|PrintWriter
name|w
init|=
name|response
operator|.
name|getResponse
argument_list|()
operator|.
name|getWriter
argument_list|()
decl_stmt|;
name|w
operator|.
name|println
argument_list|(
name|PADDING_TEXT
argument_list|)
expr_stmt|;
name|w
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|response
operator|.
name|resource
argument_list|()
operator|.
name|getRequest
argument_list|()
operator|.
name|setAttribute
argument_list|(
literal|"paddingWritten"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|Action
name|inspect
parameter_list|(
specifier|final
name|AtmosphereResource
name|r
parameter_list|)
block|{
if|if
condition|(
name|Utils
operator|.
name|webSocketMessage
argument_list|(
name|r
argument_list|)
condition|)
block|{
return|return
name|Action
operator|.
name|CONTINUE
return|;
block|}
specifier|final
name|AtmosphereRequest
name|request
init|=
name|r
operator|.
name|getRequest
argument_list|()
decl_stmt|;
specifier|final
name|String
name|accept
init|=
name|request
operator|.
name|getHeader
argument_list|(
literal|"Accept"
argument_list|)
operator|==
literal|null
condition|?
literal|"text/plain"
else|:
name|request
operator|.
name|getHeader
argument_list|(
literal|"Accept"
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|transport
argument_list|()
operator|.
name|equals
argument_list|(
name|AtmosphereResource
operator|.
name|TRANSPORT
operator|.
name|SSE
argument_list|)
operator|||
name|SERVER_SENT_EVENTS
operator|.
name|equalsIgnoreCase
argument_list|(
name|accept
argument_list|)
condition|)
block|{
specifier|final
name|AtmosphereResponse
name|response
init|=
name|r
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getAsyncIOWriter
argument_list|()
operator|==
literal|null
condition|)
block|{
name|response
operator|.
name|asyncIOWriter
argument_list|(
operator|new
name|SseAtmosphereInterceptorWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|addEventListener
argument_list|(
operator|new
name|P
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
name|AsyncIOWriter
name|writer
init|=
name|response
operator|.
name|getAsyncIOWriter
argument_list|()
decl_stmt|;
if|if
condition|(
name|AtmosphereInterceptorWriter
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|writer
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|AtmosphereInterceptorWriter
operator|.
name|class
operator|.
name|cast
argument_list|(
name|writer
argument_list|)
operator|.
name|interceptor
argument_list|(
operator|new
name|AsyncIOInterceptorAdapter
argument_list|()
block|{
specifier|private
name|boolean
name|padding
parameter_list|()
block|{
if|if
condition|(
operator|!
name|r
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
return|return
name|writePadding
argument_list|(
name|response
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|prePayload
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|padding
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|postPayload
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
comment|// The CALLBACK_JAVASCRIPT_PROTOCOL may be called by a framework running on top of Atmosphere
comment|// In that case, we must pad/protocol indenendently of the state of the AtmosphereResource
if|if
condition|(
name|r
operator|.
name|isSuspended
argument_list|()
operator|||
name|r
operator|.
name|getRequest
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|CALLBACK_JAVASCRIPT_PROTOCOL
argument_list|)
operator|!=
literal|null
operator|||
name|r
operator|.
name|getRequest
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|CONTAINER_RESPONSE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|write
argument_list|(
name|END
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**                          * When used with https://github.com/remy/polyfills/blob/master/EventSource.js , we                          * resume after every message.                          */
name|String
name|ua
init|=
name|r
operator|.
name|getRequest
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"User-Agent"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ua
operator|!=
literal|null
operator|&&
name|ua
operator|.
name|contains
argument_list|(
literal|"MSIE"
argument_list|)
condition|)
block|{
try|try
block|{
name|response
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|,
literal|""
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unable to apply %s. Your AsyncIOWriter must implement %s"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|AtmosphereInterceptorWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Action
operator|.
name|CONTINUE
return|;
block|}
specifier|private
specifier|final
class|class
name|P
extends|extends
name|OnPreSuspend
implements|implements
name|AllowInterceptor
block|{
specifier|private
specifier|final
name|AtmosphereResponse
name|response
decl_stmt|;
specifier|private
name|P
parameter_list|(
name|AtmosphereResponse
name|response
parameter_list|)
block|{
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onPreSuspend
parameter_list|(
name|AtmosphereResourceEvent
name|event
parameter_list|)
block|{
name|writePadding
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

