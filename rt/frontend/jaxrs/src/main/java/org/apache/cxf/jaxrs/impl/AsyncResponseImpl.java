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
name|impl
package|;
end_package

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|concurrent
operator|.
name|TimeUnit
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
name|ServiceUnavailableException
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
name|container
operator|.
name|CompletionCallback
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
name|ConnectionCallback
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
name|TimeoutHandler
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
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
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
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationCallback
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
name|continuations
operator|.
name|ContinuationProvider
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
name|Fault
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
name|HttpUtils
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
name|AsyncResponseImpl
implements|implements
name|AsyncResponse
implements|,
name|ContinuationCallback
block|{
specifier|private
name|Continuation
name|cont
decl_stmt|;
specifier|private
name|Message
name|inMessage
decl_stmt|;
specifier|private
name|boolean
name|initialSuspend
decl_stmt|;
specifier|private
name|boolean
name|cancelled
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|done
decl_stmt|;
specifier|private
name|boolean
name|resumedByApplication
decl_stmt|;
specifier|private
name|TimeoutHandler
name|timeoutHandler
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CompletionCallback
argument_list|>
name|completionCallbacks
init|=
operator|new
name|LinkedList
argument_list|<
name|CompletionCallback
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ConnectionCallback
argument_list|>
name|connectionCallbacks
init|=
operator|new
name|LinkedList
argument_list|<
name|ConnectionCallback
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Throwable
name|unmappedThrowable
decl_stmt|;
specifier|public
name|AsyncResponseImpl
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
name|inMessage
operator|.
name|put
argument_list|(
name|AsyncResponse
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|ContinuationCallback
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|inMessage
operator|=
name|inMessage
expr_stmt|;
name|initContinuation
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|resume
parameter_list|(
name|Object
name|response
parameter_list|)
block|{
return|return
name|doResume
argument_list|(
name|response
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|resume
parameter_list|(
name|Throwable
name|response
parameter_list|)
block|{
return|return
name|doResume
argument_list|(
name|response
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isCancelledOrNotSuspended
parameter_list|()
block|{
return|return
name|isCancelled
argument_list|()
operator|||
operator|!
name|isSuspended
argument_list|()
return|;
block|}
specifier|private
specifier|synchronized
name|boolean
name|doResume
parameter_list|(
name|Object
name|response
parameter_list|)
block|{
if|if
condition|(
name|isCancelledOrNotSuspended
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|AsyncResponse
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|cont
operator|.
name|setObject
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|resumedByApplication
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|initialSuspend
condition|)
block|{
name|cont
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|initialSuspend
operator|=
literal|false
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|cancel
parameter_list|()
block|{
return|return
name|doCancel
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|cancel
parameter_list|(
name|int
name|retryAfter
parameter_list|)
block|{
return|return
name|doCancel
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|retryAfter
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|cancel
parameter_list|(
name|Date
name|retryAfter
parameter_list|)
block|{
return|return
name|doCancel
argument_list|(
name|HttpUtils
operator|.
name|getHttpDateFormat
argument_list|()
operator|.
name|format
argument_list|(
name|retryAfter
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|synchronized
name|boolean
name|doCancel
parameter_list|(
name|String
name|retryAfterHeader
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSuspended
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ResponseBuilder
name|rb
init|=
name|Response
operator|.
name|status
argument_list|(
literal|503
argument_list|)
decl_stmt|;
if|if
condition|(
name|retryAfterHeader
operator|!=
literal|null
condition|)
block|{
name|rb
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|RETRY_AFTER
argument_list|,
name|retryAfterHeader
argument_list|)
expr_stmt|;
block|}
name|doResume
argument_list|(
name|rb
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|cancelled
operator|=
literal|true
expr_stmt|;
return|return
name|cancelled
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|boolean
name|isSuspended
parameter_list|()
block|{
return|return
name|initialSuspend
operator|||
name|cont
operator|.
name|isPending
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
name|cancelled
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|done
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|boolean
name|setTimeout
parameter_list|(
name|long
name|time
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|IllegalStateException
block|{
if|if
condition|(
name|isCancelledOrNotSuspended
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|AsyncResponse
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|long
name|timeout
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|time
argument_list|,
name|unit
argument_list|)
decl_stmt|;
name|initialSuspend
operator|=
literal|false
expr_stmt|;
name|cont
operator|.
name|suspend
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setTimeoutHandler
parameter_list|(
name|TimeoutHandler
name|handler
parameter_list|)
block|{
name|timeoutHandler
operator|=
name|handler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|callback
parameter_list|)
throws|throws
name|NullPointerException
block|{
return|return
name|register
argument_list|(
name|callback
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{}
block|)
function|.get
parameter_list|(
name|callback
parameter_list|)
function|;
block|}
end_class

begin_function
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|register
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|callback
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|callbacks
parameter_list|)
throws|throws
name|NullPointerException
block|{
try|try
block|{
name|Object
index|[]
name|extraCallbacks
init|=
operator|new
name|Object
index|[
name|callbacks
operator|.
name|length
index|]
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
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|extraCallbacks
index|[
name|i
index|]
operator|=
name|callbacks
index|[
name|i
index|]
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|register
argument_list|(
name|callback
operator|.
name|newInstance
argument_list|()
argument_list|,
name|extraCallbacks
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|register
parameter_list|(
name|Object
name|callback
parameter_list|)
throws|throws
name|NullPointerException
block|{
return|return
name|register
argument_list|(
name|callback
argument_list|,
operator|new
name|Object
index|[]
block|{}
argument_list|)
operator|.
name|get
argument_list|(
name|callback
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|register
parameter_list|(
name|Object
name|callback
parameter_list|,
name|Object
modifier|...
name|callbacks
parameter_list|)
throws|throws
name|NullPointerException
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Object
index|[]
name|allCallbacks
init|=
operator|new
name|Object
index|[
literal|1
operator|+
name|callbacks
operator|.
name|length
index|]
decl_stmt|;
name|allCallbacks
index|[
literal|0
index|]
operator|=
name|callback
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|allCallbacks
argument_list|,
literal|1
argument_list|,
name|callbacks
argument_list|,
literal|0
argument_list|,
name|callbacks
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|allCallbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|allCallbacks
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|callbackCls
init|=
name|allCallbacks
index|[
name|i
index|]
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|knownCallbacks
init|=
name|map
operator|.
name|get
argument_list|(
name|callbackCls
argument_list|)
decl_stmt|;
if|if
condition|(
name|knownCallbacks
operator|==
literal|null
condition|)
block|{
name|knownCallbacks
operator|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|callbackCls
argument_list|,
name|knownCallbacks
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allCallbacks
index|[
name|i
index|]
operator|instanceof
name|CompletionCallback
condition|)
block|{
name|knownCallbacks
operator|.
name|add
argument_list|(
name|CompletionCallback
operator|.
name|class
argument_list|)
expr_stmt|;
name|completionCallbacks
operator|.
name|add
argument_list|(
operator|(
name|CompletionCallback
operator|)
name|allCallbacks
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|allCallbacks
index|[
name|i
index|]
operator|instanceof
name|ConnectionCallback
condition|)
block|{
name|knownCallbacks
operator|.
name|add
argument_list|(
name|ConnectionCallback
operator|.
name|class
argument_list|)
expr_stmt|;
name|connectionCallbacks
operator|.
name|add
argument_list|(
operator|(
name|ConnectionCallback
operator|)
name|allCallbacks
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|done
operator|=
literal|true
expr_stmt|;
name|updateCompletionCallbacks
argument_list|(
name|unmappedThrowable
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|error
parameter_list|)
block|{
name|updateCompletionCallbacks
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|void
name|updateCompletionCallbacks
parameter_list|(
name|Throwable
name|error
parameter_list|)
block|{
name|Throwable
name|actualError
init|=
name|error
operator|instanceof
name|Fault
condition|?
operator|(
operator|(
name|Fault
operator|)
name|error
operator|)
operator|.
name|getCause
argument_list|()
else|:
name|error
decl_stmt|;
for|for
control|(
name|CompletionCallback
name|completionCallback
range|:
name|completionCallbacks
control|)
block|{
name|completionCallback
operator|.
name|onComplete
argument_list|(
name|actualError
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|void
name|onDisconnect
parameter_list|()
block|{
for|for
control|(
name|ConnectionCallback
name|connectionCallback
range|:
name|connectionCallbacks
control|)
block|{
name|connectionCallback
operator|.
name|onDisconnect
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
specifier|public
specifier|synchronized
name|boolean
name|suspendContinuationIfNeeded
parameter_list|()
block|{
if|if
condition|(
operator|!
name|cont
operator|.
name|isPending
argument_list|()
operator|&&
operator|!
name|resumedByApplication
condition|)
block|{
name|initialSuspend
operator|=
literal|false
expr_stmt|;
name|cont
operator|.
name|suspend
argument_list|(
name|AsyncResponse
operator|.
name|NO_TIMEOUT
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
end_function

begin_function
specifier|public
specifier|synchronized
name|Object
name|getResponseObject
parameter_list|()
block|{
name|Object
name|obj
init|=
name|cont
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|Response
operator|)
operator|&&
operator|!
operator|(
name|obj
operator|instanceof
name|Throwable
operator|)
condition|)
block|{
name|obj
operator|=
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|entity
argument_list|(
name|obj
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|obj
return|;
block|}
end_function

begin_function
specifier|public
specifier|synchronized
name|boolean
name|isResumedByApplication
parameter_list|()
block|{
return|return
name|resumedByApplication
return|;
block|}
end_function

begin_function
specifier|public
specifier|synchronized
name|void
name|handleTimeout
parameter_list|()
block|{
if|if
condition|(
operator|!
name|resumedByApplication
condition|)
block|{
if|if
condition|(
name|timeoutHandler
operator|!=
literal|null
condition|)
block|{
name|timeoutHandler
operator|.
name|handleTimeout
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cont
operator|.
name|setObject
argument_list|(
operator|new
name|ServiceUnavailableException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_function

begin_function
specifier|private
name|void
name|initContinuation
parameter_list|()
block|{
name|ContinuationProvider
name|provider
init|=
operator|(
name|ContinuationProvider
operator|)
name|inMessage
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|cont
operator|=
name|provider
operator|.
name|getContinuation
argument_list|()
expr_stmt|;
name|initialSuspend
operator|=
literal|true
expr_stmt|;
block|}
end_function

begin_function
specifier|public
name|void
name|prepareContinuation
parameter_list|()
block|{
name|initContinuation
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
specifier|public
name|void
name|setUnmappedThrowable
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|unmappedThrowable
operator|=
name|t
expr_stmt|;
block|}
end_function

begin_function
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|cont
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

