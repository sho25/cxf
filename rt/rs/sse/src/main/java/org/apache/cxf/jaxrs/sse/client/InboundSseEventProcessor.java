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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|Callable
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
name|ExecutorService
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
name|Executors
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
name|core
operator|.
name|MediaType
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
name|core
operator|.
name|Response
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
name|InboundSseEvent
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|endpoint
operator|.
name|Endpoint
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
name|client
operator|.
name|ClientProviderFactory
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
name|ResponseImpl
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
name|client
operator|.
name|InboundSseEventImpl
operator|.
name|Builder
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

begin_class
specifier|public
class|class
name|InboundSseEventProcessor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_SENT_EVENTS
init|=
literal|"text/event-stream"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|MediaType
name|SERVER_SENT_EVENTS_TYPE
init|=
name|MediaType
operator|.
name|valueOf
argument_list|(
name|SERVER_SENT_EVENTS
argument_list|)
decl_stmt|;
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
name|InboundSseEventProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COMMENT
init|=
literal|": "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"event: "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ID
init|=
literal|"id: "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RETRY
init|=
literal|"retry: "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DATA
init|=
literal|"data: "
decl_stmt|;
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
specifier|private
specifier|final
name|InboundSseEventListener
name|listener
decl_stmt|;
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|closed
decl_stmt|;
specifier|protected
name|InboundSseEventProcessor
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|InboundSseEventListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|listener
operator|=
name|listener
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
expr_stmt|;
block|}
name|void
name|run
parameter_list|(
specifier|final
name|Response
name|response
parameter_list|)
block|{
if|if
condition|(
name|closed
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The SSE Event Processor is already closed"
argument_list|)
throw|;
block|}
specifier|final
name|InputStream
name|is
init|=
name|response
operator|.
name|readEntity
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ClientProviderFactory
name|factory
init|=
name|ClientProviderFactory
operator|.
name|getInstance
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|response
operator|instanceof
name|ResponseImpl
condition|)
block|{
name|message
operator|=
operator|(
operator|(
name|ResponseImpl
operator|)
name|response
operator|)
operator|.
name|getOutMessage
argument_list|()
expr_stmt|;
block|}
name|executor
operator|.
name|submit
argument_list|(
name|process
argument_list|(
name|response
argument_list|,
name|is
argument_list|,
name|factory
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Callable
argument_list|<
name|?
argument_list|>
name|process
parameter_list|(
name|Response
name|response
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|ClientProviderFactory
name|factory
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
return|return
parameter_list|()
lambda|->
block|{
try|try
init|(
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|InboundSseEventImpl
operator|.
name|Builder
name|builder
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
operator|&&
operator|!
name|Thread
operator|.
name|interrupted
argument_list|()
operator|&&
operator|!
name|closed
condition|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|line
argument_list|)
operator|&&
name|builder
operator|!=
literal|null
condition|)
block|{
comment|/* empty new line */
specifier|final
name|InboundSseEvent
name|event
init|=
name|builder
operator|.
name|build
argument_list|(
name|factory
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|builder
operator|=
literal|null
expr_stmt|;
comment|/* reset the builder for next event */
name|listener
operator|.
name|onNext
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|EVENT
argument_list|)
condition|)
block|{
name|builder
operator|=
name|getOrCreate
argument_list|(
name|builder
argument_list|)
operator|.
name|name
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|EVENT
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|ID
argument_list|)
condition|)
block|{
name|builder
operator|=
name|getOrCreate
argument_list|(
name|builder
argument_list|)
operator|.
name|id
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|ID
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|COMMENT
argument_list|)
condition|)
block|{
name|builder
operator|=
name|getOrCreate
argument_list|(
name|builder
argument_list|)
operator|.
name|comment
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|COMMENT
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|RETRY
argument_list|)
condition|)
block|{
name|builder
operator|=
name|getOrCreate
argument_list|(
name|builder
argument_list|)
operator|.
name|reconnectDelay
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|RETRY
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
name|DATA
argument_list|)
condition|)
block|{
name|builder
operator|=
name|getOrCreate
argument_list|(
name|builder
argument_list|)
operator|.
name|appendData
argument_list|(
name|line
operator|.
name|substring
argument_list|(
name|DATA
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|builder
operator|!=
literal|null
condition|)
block|{
name|listener
operator|.
name|onNext
argument_list|(
name|builder
operator|.
name|build
argument_list|(
name|factory
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// complete the stream
name|listener
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
name|listener
operator|.
name|onError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Closing the response"
argument_list|)
expr_stmt|;
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
return|;
block|}
name|boolean
name|isClosed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
name|boolean
name|close
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
try|try
block|{
name|closed
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|executor
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|Void
argument_list|>
call|)
argument_list|()
operator|->
block|{
name|executor
operator|.
name|shutdown
argument_list|()
block|;
return|return
literal|null
return|;
block|}
block|)
empty_stmt|;
return|return
name|executor
operator|.
name|awaitTermination
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
name|ex
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

begin_comment
comment|/**      * Create builder on-demand, without explicit event demarcation      */
end_comment

begin_function
specifier|private
specifier|static
name|Builder
name|getOrCreate
parameter_list|(
specifier|final
name|Builder
name|builder
parameter_list|)
block|{
return|return
operator|(
name|builder
operator|==
literal|null
operator|)
condition|?
operator|new
name|InboundSseEventImpl
operator|.
name|Builder
argument_list|()
else|:
name|builder
return|;
block|}
end_function

unit|}
end_unit

