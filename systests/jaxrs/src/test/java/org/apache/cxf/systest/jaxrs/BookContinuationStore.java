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
name|systest
operator|.
name|jaxrs
package|;
end_package

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
name|ArrayBlockingQueue
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
name|Executor
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
name|ThreadPoolExecutor
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
name|AtomicInteger
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
name|GET
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
name|NotFoundException
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
name|Path
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
name|PathParam
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
name|Produces
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
name|Suspended
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
name|Response
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

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
specifier|public
class|class
name|BookContinuationStore
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|books
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Executor
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
specifier|public
name|BookContinuationStore
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/defaulttimeout"
argument_list|)
specifier|public
name|void
name|getBookDescriptionWithTimeout
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|async
operator|.
name|register
argument_list|(
operator|new
name|CallbackImpl
argument_list|()
argument_list|)
expr_stmt|;
name|async
operator|.
name|setTimeout
argument_list|(
literal|2000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/resume"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|getBookDescriptionImmediateResume
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|async
operator|.
name|resume
argument_list|(
literal|"immediateResume"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/cancel"
argument_list|)
specifier|public
name|void
name|getBookDescriptionWithCancel
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getClass
argument_list|()
expr_stmt|;
name|async
operator|.
name|setTimeout
argument_list|(
literal|2000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|async
operator|.
name|setTimeoutHandler
argument_list|(
operator|new
name|CancelTimeoutHandlerImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/timeouthandler/{id}"
argument_list|)
specifier|public
name|void
name|getBookDescriptionWithHandler
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|async
operator|.
name|setTimeout
argument_list|(
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|async
operator|.
name|setTimeoutHandler
argument_list|(
operator|new
name|TimeoutHandlerImpl
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/timeouthandlerresume/{id}"
argument_list|)
specifier|public
name|void
name|getBookDescriptionWithHandlerResumeOnly
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|async
operator|.
name|setTimeout
argument_list|(
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|async
operator|.
name|setTimeoutHandler
argument_list|(
operator|new
name|TimeoutHandlerImpl
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{id}"
argument_list|)
specifier|public
name|void
name|getBookDescription
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|Suspended
name|AsyncResponse
name|async
parameter_list|)
block|{
name|handleContinuationRequest
argument_list|(
name|id
argument_list|,
name|async
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/books/subresources/"
argument_list|)
specifier|public
name|BookContinuationStore
name|getBookStore
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{id}"
argument_list|)
specifier|public
name|void
name|handleContinuationRequest
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|,
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
block|{
name|resumeSuspended
argument_list|(
name|id
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"books/notfound"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|handleContinuationRequestNotFound
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
block|{
name|response
operator|.
name|register
argument_list|(
operator|new
name|CallbackImpl
argument_list|()
argument_list|)
expr_stmt|;
name|resumeSuspendedNotFound
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"books/notfound/unmapped"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|handleContinuationRequestNotFoundUnmapped
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
block|{
name|response
operator|.
name|register
argument_list|(
operator|new
name|CallbackImpl
argument_list|()
argument_list|)
expr_stmt|;
name|resumeSuspendedNotFoundUnmapped
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"books/unmappedFromFilter"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|handleContinuationRequestUnmappedFromFilter
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
block|{
name|response
operator|.
name|register
argument_list|(
operator|new
name|CallbackImpl
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|resume
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"books/suspend/unmapped"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|handleNotMappedAfterSuspend
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
name|response
operator|.
name|setTimeout
argument_list|(
literal|2000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|response
operator|.
name|setTimeoutHandler
argument_list|(
operator|new
name|CancelTimeoutHandlerImpl
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BookNotFoundFault
argument_list|(
literal|""
argument_list|)
throw|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/disconnect"
argument_list|)
specifier|public
name|void
name|handleClientDisconnects
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
block|{
name|response
operator|.
name|setTimeout
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|response
operator|.
name|register
argument_list|(
operator|new
name|ConnectionCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDisconnect
parameter_list|(
name|AsyncResponse
name|disconnected
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ConnectionCallback: onDisconnect, client disconnects"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|response
operator|.
name|resume
argument_list|(
name|books
operator|.
name|values
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resumeSuspended
parameter_list|(
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|AsyncResponse
name|response
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|response
operator|.
name|resume
argument_list|(
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resumeSuspendedNotFound
parameter_list|(
specifier|final
name|AsyncResponse
name|response
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|response
operator|.
name|resume
argument_list|(
operator|new
name|NotFoundException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|resumeSuspendedNotFoundUnmapped
parameter_list|(
specifier|final
name|AsyncResponse
name|response
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|response
operator|.
name|resume
argument_list|(
operator|new
name|BookNotFoundFault
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|books
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|"CXF in Action1"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"2"
argument_list|,
literal|"CXF in Action2"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"3"
argument_list|,
literal|"CXF in Action3"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"4"
argument_list|,
literal|"CXF in Action4"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"5"
argument_list|,
literal|"CXF in Action5"
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|TimeoutHandlerImpl
implements|implements
name|TimeoutHandler
block|{
specifier|private
name|boolean
name|resumeOnly
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|AtomicInteger
name|timeoutExtendedCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|TimeoutHandlerImpl
parameter_list|(
name|String
name|id
parameter_list|,
name|boolean
name|resumeOnly
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|resumeOnly
operator|=
name|resumeOnly
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleTimeout
parameter_list|(
name|AsyncResponse
name|asyncResponse
parameter_list|)
block|{
if|if
condition|(
operator|!
name|resumeOnly
operator|&&
name|timeoutExtendedCounter
operator|.
name|addAndGet
argument_list|(
literal|1
argument_list|)
operator|<=
literal|2
condition|)
block|{
name|asyncResponse
operator|.
name|setTimeout
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|asyncResponse
operator|.
name|resume
argument_list|(
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|CancelTimeoutHandlerImpl
implements|implements
name|TimeoutHandler
block|{
annotation|@
name|Override
specifier|public
name|void
name|handleTimeout
parameter_list|(
name|AsyncResponse
name|asyncResponse
parameter_list|)
block|{
name|asyncResponse
operator|.
name|cancel
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|CallbackImpl
implements|implements
name|CompletionCallback
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"CompletionCallback: onComplete, throwable: "
operator|+
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

