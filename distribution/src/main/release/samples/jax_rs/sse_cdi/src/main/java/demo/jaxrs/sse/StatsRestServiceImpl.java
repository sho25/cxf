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
name|SseEventSink
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Emitter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|Flowable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|schedulers
operator|.
name|Schedulers
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
name|SseBroadcaster
name|broadcaster
decl_stmt|;
specifier|private
name|Builder
name|builder
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setSse
parameter_list|(
name|Sse
name|sse
parameter_list|)
block|{
name|this
operator|.
name|broadcaster
operator|=
name|sse
operator|.
name|newBroadcaster
argument_list|()
expr_stmt|;
name|this
operator|.
name|builder
operator|=
name|sse
operator|.
name|newEventBuilder
argument_list|()
expr_stmt|;
name|Flowable
operator|.
name|interval
argument_list|(
literal|500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|zipWith
argument_list|(
name|Flowable
operator|.
name|generate
argument_list|(
parameter_list|(
name|Emitter
argument_list|<
name|OutboundSseEvent
operator|.
name|Builder
argument_list|>
name|emitter
parameter_list|)
lambda|->
name|emitter
operator|.
name|onNext
argument_list|(
name|builder
operator|.
name|name
argument_list|(
literal|"stats"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
parameter_list|(
name|id
parameter_list|,
name|bldr
parameter_list|)
lambda|->
name|createStatsEvent
argument_list|(
name|bldr
argument_list|,
name|id
argument_list|)
argument_list|)
operator|.
name|subscribeOn
argument_list|(
name|Schedulers
operator|.
name|single
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
name|broadcaster
operator|::
name|broadcast
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"sse"
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
parameter_list|)
block|{
name|broadcaster
operator|.
name|register
argument_list|(
name|sink
argument_list|)
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
name|long
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

