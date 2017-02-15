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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|OutboundSseEvent
operator|.
name|Builder
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
name|Sse
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
name|jaxrs
operator|.
name|sse
operator|.
name|SseImpl
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/stats"
argument_list|)
specifier|public
class|class
name|StatsRestServiceImpl
block|{
specifier|private
specifier|static
specifier|final
name|Random
name|RANDOM
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Sse
name|sse
init|=
name|SseImpl
operator|.
name|create
argument_list|()
decl_stmt|;
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
name|void
name|stats
parameter_list|(
annotation|@
name|Context
name|SseEventSink
name|sink
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|final
name|String
name|id
parameter_list|)
block|{
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
name|Builder
name|builder
init|=
name|sse
operator|.
name|newEventBuilder
argument_list|()
decl_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|sink
operator|.
name|onNext
argument_list|(
name|createStatsEvent
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|,
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|sink
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
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
literal|""
operator|+
name|eventId
argument_list|)
operator|.
name|data
argument_list|(
name|Stats
operator|.
name|class
argument_list|,
operator|new
name|Stats
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|,
name|RANDOM
operator|.
name|nextInt
argument_list|(
literal|100
argument_list|)
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
block|}
end_class

end_unit

