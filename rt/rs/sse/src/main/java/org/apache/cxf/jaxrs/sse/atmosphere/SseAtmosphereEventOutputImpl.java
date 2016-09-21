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
name|ByteArrayOutputStream
import|;
end_import

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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|SseEventOutput
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
name|AtmosphereResponse
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
name|Broadcaster
import|;
end_import

begin_class
specifier|public
class|class
name|SseAtmosphereEventOutputImpl
implements|implements
name|SseEventOutput
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
name|SseAtmosphereEventOutputImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|AtmosphereResource
name|resource
decl_stmt|;
specifier|private
specifier|final
name|MessageBodyWriter
argument_list|<
name|OutboundSseEvent
argument_list|>
name|writer
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|closed
decl_stmt|;
specifier|public
name|SseAtmosphereEventOutputImpl
parameter_list|(
specifier|final
name|MessageBodyWriter
argument_list|<
name|OutboundSseEvent
argument_list|>
name|writer
parameter_list|,
specifier|final
name|AtmosphereResource
name|resource
parameter_list|)
block|{
name|this
operator|.
name|writer
operator|=
name|writer
expr_stmt|;
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
if|if
condition|(
operator|!
name|resource
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Atmosphere resource is not suspended, suspending"
argument_list|)
expr_stmt|;
name|resource
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
name|closed
operator|=
literal|true
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Closing Atmosphere SSE event output"
argument_list|)
expr_stmt|;
if|if
condition|(
name|resource
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Atmosphere resource is suspended, resuming"
argument_list|)
expr_stmt|;
name|resource
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Broadcaster
name|broadcaster
init|=
name|resource
operator|.
name|getBroadcaster
argument_list|()
decl_stmt|;
name|resource
operator|.
name|removeFromAllBroadcasters
argument_list|()
expr_stmt|;
try|try
block|{
specifier|final
name|AtmosphereResponse
name|response
init|=
name|resource
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|response
operator|.
name|isCommitted
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Response is not committed, flushing buffer"
argument_list|)
expr_stmt|;
name|response
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
block|}
name|response
operator|.
name|closeStreamOrWriter
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|resource
operator|.
name|close
argument_list|()
expr_stmt|;
name|broadcaster
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Atmosphere SSE event output is closed"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|OutboundSseEvent
name|event
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
operator|&&
name|writer
operator|!=
literal|null
condition|)
block|{
try|try
init|(
specifier|final
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|writer
operator|.
name|writeTo
argument_list|(
name|event
argument_list|,
name|event
operator|.
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|event
operator|.
name|getMediaType
argument_list|()
argument_list|,
literal|null
argument_list|,
name|os
argument_list|)
expr_stmt|;
comment|// Atmosphere broadcasts asynchronously which is acceptable in most cases.
comment|// Unfortunately, calling close() may lead to response stream being closed
comment|// while there are still some SSE delivery scheduled.
specifier|final
name|Future
argument_list|<
name|Object
argument_list|>
name|future
init|=
name|resource
operator|.
name|getBroadcaster
argument_list|()
operator|.
name|broadcast
argument_list|(
name|os
operator|.
name|toString
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|future
operator|.
name|isDone
argument_list|()
condition|)
block|{
comment|// Let us wait at least 200 milliseconds before returning to ensure
comment|// that SSE had the opportunity to be delivered.
name|LOG
operator|.
name|info
argument_list|(
literal|"Waiting 200ms to ensure SSE Atmosphere response is delivered"
argument_list|)
expr_stmt|;
name|future
operator|.
name|get
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|ExecutionException
decl||
name|InterruptedException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
specifier|final
name|TimeoutException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"SSE Atmosphere response was not delivered within default timeout"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
block|}
end_class

end_unit

