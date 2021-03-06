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
name|Collection
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
name|CopyOnWriteArrayList
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
name|ScheduledExecutorService
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
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
name|client
operator|.
name|Invocation
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
name|client
operator|.
name|WebTarget
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
name|Configuration
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
name|HttpHeaders
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|sse
operator|.
name|SseEventSource
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
name|WebClient
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
name|ExceptionUtils
import|;
end_import

begin_comment
comment|/**  * SSE Event Source implementation   */
end_comment

begin_class
specifier|public
class|class
name|SseEventSourceImpl
implements|implements
name|SseEventSource
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
name|SseEventSourceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|WebTarget
name|target
decl_stmt|;
specifier|private
specifier|final
name|Collection
argument_list|<
name|InboundSseEventListener
argument_list|>
name|listeners
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|SseSourceState
argument_list|>
name|state
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|SseSourceState
operator|.
name|CLOSED
argument_list|)
decl_stmt|;
comment|// It may happen that open() and close() could be called on separate threads
specifier|private
specifier|volatile
name|ScheduledExecutorService
name|executor
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|managedExecutor
init|=
literal|true
decl_stmt|;
specifier|private
specifier|volatile
name|InboundSseEventProcessor
name|processor
decl_stmt|;
specifier|private
specifier|volatile
name|TimeUnit
name|unit
decl_stmt|;
specifier|private
specifier|volatile
name|long
name|delay
decl_stmt|;
specifier|private
class|class
name|InboundSseEventListenerDelegate
implements|implements
name|InboundSseEventListener
block|{
specifier|private
name|String
name|lastEventId
decl_stmt|;
name|InboundSseEventListenerDelegate
parameter_list|(
name|String
name|lastEventId
parameter_list|)
block|{
name|this
operator|.
name|lastEventId
operator|=
name|lastEventId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|InboundSseEvent
name|event
parameter_list|)
block|{
name|lastEventId
operator|=
name|event
operator|.
name|getId
argument_list|()
expr_stmt|;
name|listeners
operator|.
name|forEach
argument_list|(
name|listener
lambda|->
name|listener
operator|.
name|onNext
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
comment|// Reconnect delay is set in milliseconds
if|if
condition|(
name|event
operator|.
name|isReconnectDelaySet
argument_list|()
condition|)
block|{
name|unit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
name|delay
operator|=
name|event
operator|.
name|getReconnectDelay
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|listeners
operator|.
name|forEach
argument_list|(
name|listener
lambda|->
name|listener
operator|.
name|onError
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|delay
operator|>=
literal|0
operator|&&
name|unit
operator|!=
literal|null
condition|)
block|{
name|scheduleReconnect
argument_list|(
name|delay
argument_list|,
name|unit
argument_list|,
name|lastEventId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|listeners
operator|.
name|forEach
argument_list|(
name|InboundSseEventListener
operator|::
name|onComplete
argument_list|)
expr_stmt|;
if|if
condition|(
name|delay
operator|>=
literal|0
operator|&&
name|unit
operator|!=
literal|null
condition|)
block|{
name|scheduleReconnect
argument_list|(
name|delay
argument_list|,
name|unit
argument_list|,
name|lastEventId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|InboundSseEventListenerImpl
implements|implements
name|InboundSseEventListener
block|{
specifier|private
specifier|final
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|onEvent
decl_stmt|;
specifier|private
specifier|final
name|Consumer
argument_list|<
name|Throwable
argument_list|>
name|onError
decl_stmt|;
specifier|private
specifier|final
name|Runnable
name|onComplete
decl_stmt|;
name|InboundSseEventListenerImpl
parameter_list|(
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|e
parameter_list|)
block|{
name|this
argument_list|(
name|e
argument_list|,
name|ex
lambda|->
block|{ }
argument_list|,
parameter_list|()
lambda|->
block|{ }
argument_list|)
expr_stmt|;
block|}
name|InboundSseEventListenerImpl
parameter_list|(
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|e
parameter_list|,
name|Consumer
argument_list|<
name|Throwable
argument_list|>
name|t
parameter_list|)
block|{
name|this
argument_list|(
name|e
argument_list|,
name|t
argument_list|,
parameter_list|()
lambda|->
block|{ }
argument_list|)
expr_stmt|;
block|}
name|InboundSseEventListenerImpl
parameter_list|(
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|e
parameter_list|,
name|Consumer
argument_list|<
name|Throwable
argument_list|>
name|t
parameter_list|,
name|Runnable
name|c
parameter_list|)
block|{
name|this
operator|.
name|onEvent
operator|=
name|e
expr_stmt|;
name|this
operator|.
name|onError
operator|=
name|t
expr_stmt|;
name|this
operator|.
name|onComplete
operator|=
name|c
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|InboundSseEvent
name|event
parameter_list|)
block|{
name|onEvent
operator|.
name|accept
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|onError
operator|.
name|accept
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|onComplete
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * https://www.w3.org/TR/2012/WD-eventsource-20120426/#dom-eventsource-connecting      */
specifier|private
enum|enum
name|SseSourceState
block|{
name|CONNECTING
block|,
name|OPEN
block|,
name|CLOSED
block|}
name|SseEventSourceImpl
parameter_list|(
name|WebTarget
name|target
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
name|this
operator|.
name|unit
operator|=
name|unit
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|onEvent
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
operator|new
name|InboundSseEventListenerImpl
argument_list|(
name|onEvent
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|onEvent
parameter_list|,
name|Consumer
argument_list|<
name|Throwable
argument_list|>
name|onError
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
operator|new
name|InboundSseEventListenerImpl
argument_list|(
name|onEvent
argument_list|,
name|onError
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|register
parameter_list|(
name|Consumer
argument_list|<
name|InboundSseEvent
argument_list|>
name|onEvent
parameter_list|,
name|Consumer
argument_list|<
name|Throwable
argument_list|>
name|onError
parameter_list|,
name|Runnable
name|onComplete
parameter_list|)
block|{
name|listeners
operator|.
name|add
argument_list|(
operator|new
name|InboundSseEventListenerImpl
argument_list|(
name|onEvent
argument_list|,
name|onError
argument_list|,
name|onComplete
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|open
parameter_list|()
block|{
if|if
condition|(
operator|!
name|state
operator|.
name|compareAndSet
argument_list|(
name|SseSourceState
operator|.
name|CLOSED
argument_list|,
name|SseSourceState
operator|.
name|CONNECTING
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The SseEventSource is already in "
operator|+
name|state
operator|.
name|get
argument_list|()
operator|+
literal|" state"
argument_list|)
throw|;
block|}
comment|// Create the executor for scheduling the reconnect tasks
specifier|final
name|Configuration
name|configuration
init|=
name|target
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
operator|(
name|ScheduledExecutorService
operator|)
name|configuration
operator|.
name|getProperty
argument_list|(
literal|"scheduledExecutorService"
argument_list|)
expr_stmt|;
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
expr_stmt|;
name|managedExecutor
operator|=
literal|false
expr_stmt|;
comment|/* we manage lifecycle */
block|}
block|}
specifier|final
name|Object
name|lastEventId
init|=
name|configuration
operator|.
name|getProperty
argument_list|(
name|HttpHeaders
operator|.
name|LAST_EVENT_ID_HEADER
argument_list|)
decl_stmt|;
name|connect
argument_list|(
name|lastEventId
operator|!=
literal|null
condition|?
name|lastEventId
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|connect
parameter_list|(
name|String
name|lastEventId
parameter_list|)
block|{
specifier|final
name|InboundSseEventListenerDelegate
name|delegate
init|=
operator|new
name|InboundSseEventListenerDelegate
argument_list|(
name|lastEventId
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Invocation
operator|.
name|Builder
name|builder
init|=
name|target
operator|.
name|request
argument_list|(
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastEventId
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|LAST_EVENT_ID_HEADER
argument_list|,
name|lastEventId
argument_list|)
expr_stmt|;
block|}
name|response
operator|=
name|builder
operator|.
name|get
argument_list|()
expr_stmt|;
comment|// A client can be told to stop reconnecting using the HTTP 204 No Content
comment|// response code. In this case, we should give up.
specifier|final
name|int
name|status
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|==
literal|204
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"SSE endpoint "
operator|+
name|target
operator|.
name|getUri
argument_list|()
operator|+
literal|" returns no data, disconnecting"
argument_list|)
expr_stmt|;
name|state
operator|.
name|set
argument_list|(
name|SseSourceState
operator|.
name|CLOSED
argument_list|)
expr_stmt|;
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// Convert unsuccessful responses to instances of WebApplicationException
if|if
condition|(
name|status
operator|!=
literal|304
operator|&&
name|status
operator|>=
literal|300
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"SSE connection to "
operator|+
name|target
operator|.
name|getUri
argument_list|()
operator|+
literal|" returns "
operator|+
name|status
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toWebApplicationException
argument_list|(
name|response
argument_list|)
throw|;
block|}
comment|// Should not happen but if close() was called from another thread, we could
comment|// end up there.
if|if
condition|(
name|state
operator|.
name|get
argument_list|()
operator|==
name|SseSourceState
operator|.
name|CLOSED
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"SSE connection to "
operator|+
name|target
operator|.
name|getUri
argument_list|()
operator|+
literal|" has been closed already"
argument_list|)
expr_stmt|;
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
return|return;
block|}
specifier|final
name|Endpoint
name|endpoint
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|target
argument_list|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
comment|// Create new processor if this is the first time or the old one has been closed
if|if
condition|(
name|processor
operator|==
literal|null
operator|||
name|processor
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating new instance of SSE event processor ..."
argument_list|)
expr_stmt|;
name|processor
operator|=
operator|new
name|InboundSseEventProcessor
argument_list|(
name|endpoint
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
comment|// Start consuming events
name|processor
operator|.
name|run
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"SSE event processor has been started ..."
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|state
operator|.
name|compareAndSet
argument_list|(
name|SseSourceState
operator|.
name|CONNECTING
argument_list|,
name|SseSourceState
operator|.
name|OPEN
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The SseEventSource is already in "
operator|+
name|state
operator|.
name|get
argument_list|()
operator|+
literal|" state"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Successfuly opened SSE connection to "
operator|+
name|target
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|close
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|processor
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// We don't change the state here as the reconnection will be scheduled (if configured)
name|LOG
operator|.
name|fine
argument_list|(
literal|"Failed to open SSE connection to "
operator|+
name|target
operator|.
name|getUri
argument_list|()
operator|+
literal|". "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|onError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|state
operator|.
name|get
argument_list|()
operator|==
name|SseSourceState
operator|.
name|OPEN
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|close
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|tunit
parameter_list|)
block|{
if|if
condition|(
name|state
operator|.
name|get
argument_list|()
operator|==
name|SseSourceState
operator|.
name|CLOSED
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|state
operator|.
name|compareAndSet
argument_list|(
name|SseSourceState
operator|.
name|CONNECTING
argument_list|,
name|SseSourceState
operator|.
name|CLOSED
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"The SseEventSource was not connected, closing anyway"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|state
operator|.
name|compareAndSet
argument_list|(
name|SseSourceState
operator|.
name|OPEN
argument_list|,
name|SseSourceState
operator|.
name|CLOSED
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The SseEventSource is not opened, but in "
operator|+
name|state
operator|.
name|get
argument_list|()
operator|+
literal|" state"
argument_list|)
throw|;
block|}
if|if
condition|(
name|executor
operator|!=
literal|null
operator|&&
operator|!
name|managedExecutor
condition|)
block|{
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
function|;
name|executor
operator|=
literal|null
expr_stmt|;
name|managedExecutor
operator|=
literal|true
expr_stmt|;
block|}
end_class

begin_comment
comment|// Should never happen
end_comment

begin_if
if|if
condition|(
name|processor
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
end_if

begin_return
return|return
name|processor
operator|.
name|close
argument_list|(
name|timeout
argument_list|,
name|tunit
argument_list|)
return|;
end_return

begin_function
unit|}          private
name|void
name|scheduleReconnect
parameter_list|(
name|long
name|tdelay
parameter_list|,
name|TimeUnit
name|tunit
parameter_list|,
name|String
name|lastEventId
parameter_list|)
block|{
comment|// If delay == RECONNECT_NOT_SET, no reconnection attempt should be performed
if|if
condition|(
name|tdelay
operator|<
literal|0
operator|||
name|executor
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// If the event source is already closed, do nothing
if|if
condition|(
name|state
operator|.
name|get
argument_list|()
operator|==
name|SseSourceState
operator|.
name|CLOSED
condition|)
block|{
return|return;
block|}
comment|// If the connection was still on connecting state, just try to reconnect
if|if
condition|(
name|state
operator|.
name|get
argument_list|()
operator|!=
name|SseSourceState
operator|.
name|CONNECTING
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"The SseEventSource is still opened, moving it to connecting state"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|state
operator|.
name|compareAndSet
argument_list|(
name|SseSourceState
operator|.
name|OPEN
argument_list|,
name|SseSourceState
operator|.
name|CONNECTING
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The SseEventSource is not opened, but in "
operator|+
name|state
operator|.
name|get
argument_list|()
operator|+
literal|" state, unable to reconnect"
argument_list|)
throw|;
block|}
block|}
name|executor
operator|.
name|schedule
argument_list|(
parameter_list|()
lambda|->
block|{
comment|// If we are still in connecting state (not closed/open), let's try to reconnect
if|if
condition|(
name|state
operator|.
name|get
argument_list|()
operator|==
name|SseSourceState
operator|.
name|CONNECTING
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Reestablishing SSE connection to "
operator|+
name|target
operator|.
name|getUri
argument_list|()
argument_list|)
expr_stmt|;
name|connect
argument_list|(
name|lastEventId
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|tdelay
argument_list|,
name|tunit
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"The reconnection attempt to "
operator|+
name|target
operator|.
name|getUri
argument_list|()
operator|+
literal|" is scheduled in "
operator|+
name|tunit
operator|.
name|toMillis
argument_list|(
name|tdelay
argument_list|)
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

