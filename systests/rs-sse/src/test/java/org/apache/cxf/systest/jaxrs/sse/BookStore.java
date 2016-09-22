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
operator|.
name|sse
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
name|util
operator|.
name|Arrays
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
name|CountDownLatch
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DefaultValue
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
name|HeaderParam
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
name|POST
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
name|core
operator|.
name|Context
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
name|SseBroadcaster
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
name|SseContext
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/api/bookstore"
argument_list|)
specifier|public
class|class
name|BookStore
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|SseBroadcaster
argument_list|>
name|broadcaster
init|=
operator|new
name|AtomicReference
argument_list|<
name|SseBroadcaster
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|OutboundSseEvent
name|createStatsEvent
parameter_list|(
specifier|final
name|OutboundSseEvent
operator|.
name|Builder
name|builder
parameter_list|,
specifier|final
name|int
name|eventId
parameter_list|)
block|{
return|return
name|builder
operator|.
name|id
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|eventId
argument_list|)
argument_list|)
operator|.
name|data
argument_list|(
name|Book
operator|.
name|class
argument_list|,
operator|new
name|Book
argument_list|(
literal|"New Book #"
operator|+
name|eventId
argument_list|,
name|eventId
argument_list|)
argument_list|)
operator|.
name|mediaType
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Book
argument_list|(
literal|"New Book #1"
argument_list|,
literal|1
argument_list|)
argument_list|,
operator|new
name|Book
argument_list|(
literal|"New Book #2"
argument_list|,
literal|2
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"sse/{id}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
specifier|public
name|SseEventOutput
name|forBook
parameter_list|(
annotation|@
name|Context
name|SseContext
name|sseContext
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|final
name|String
name|id
parameter_list|,
annotation|@
name|HeaderParam
argument_list|(
name|HttpHeaders
operator|.
name|LAST_EVENT_ID_HEADER
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"0"
argument_list|)
specifier|final
name|String
name|lastEventId
parameter_list|)
block|{
specifier|final
name|SseEventOutput
name|output
init|=
name|sseContext
operator|.
name|newOutput
argument_list|()
decl_stmt|;
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
specifier|final
name|Integer
name|id
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|lastEventId
argument_list|)
decl_stmt|;
name|output
operator|.
name|write
argument_list|(
name|createStatsEvent
argument_list|(
name|sseContext
operator|.
name|newEvent
argument_list|()
operator|.
name|name
argument_list|(
literal|"book"
argument_list|)
argument_list|,
name|id
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|createStatsEvent
argument_list|(
name|sseContext
operator|.
name|newEvent
argument_list|()
operator|.
name|name
argument_list|(
literal|"book"
argument_list|)
argument_list|,
name|id
operator|+
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|createStatsEvent
argument_list|(
name|sseContext
operator|.
name|newEvent
argument_list|()
operator|.
name|name
argument_list|(
literal|"book"
argument_list|)
argument_list|,
name|id
operator|+
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|createStatsEvent
argument_list|(
name|sseContext
operator|.
name|newEvent
argument_list|()
operator|.
name|name
argument_list|(
literal|"book"
argument_list|)
argument_list|,
name|id
operator|+
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
decl||
name|IOException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Communication error"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|output
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"broadcast/sse"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|SERVER_SENT_EVENTS
argument_list|)
specifier|public
name|SseEventOutput
name|broadcast
parameter_list|(
annotation|@
name|Context
name|SseContext
name|sseContext
parameter_list|)
block|{
specifier|final
name|SseEventOutput
name|output
init|=
name|sseContext
operator|.
name|newOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|broadcaster
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|broadcaster
operator|.
name|compareAndSet
argument_list|(
literal|null
argument_list|,
name|sseContext
operator|.
name|newBroadcaster
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|broadcaster
operator|.
name|get
argument_list|()
operator|.
name|register
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|get
argument_list|()
operator|.
name|broadcast
argument_list|(
name|createStatsEvent
argument_list|(
name|sseContext
operator|.
name|newEvent
argument_list|()
operator|.
name|name
argument_list|(
literal|"book"
argument_list|)
argument_list|,
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|get
argument_list|()
operator|.
name|broadcast
argument_list|(
name|createStatsEvent
argument_list|(
name|sseContext
operator|.
name|newEvent
argument_list|()
operator|.
name|name
argument_list|(
literal|"book"
argument_list|)
argument_list|,
literal|2000
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
finally|finally
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"broadcast/close"
argument_list|)
specifier|public
name|void
name|stop
parameter_list|()
block|{
try|try
block|{
comment|// Await a least 2 clients to be broadcasted over
if|if
condition|(
operator|!
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Not enough clients have been connected, closing broadcaster anyway"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Wait has been interrupted"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|broadcaster
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|broadcaster
operator|.
name|get
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
