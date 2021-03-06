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
name|concurrent
operator|.
name|CompletableFuture
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
name|CompletionStage
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
name|LongAdder
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
name|WebApplicationException
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
name|SseBroadcaster
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mock
operator|.
name|web
operator|.
name|MockAsyncContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mock
operator|.
name|web
operator|.
name|MockHttpServletRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mock
operator|.
name|web
operator|.
name|MockHttpServletResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
specifier|public
class|class
name|SseBroadcasterImplTest
block|{
specifier|private
name|SseBroadcaster
name|broadcaster
decl_stmt|;
specifier|private
name|MessageBodyWriter
argument_list|<
name|OutboundSseEvent
argument_list|>
name|writer
decl_stmt|;
specifier|private
name|MockHttpServletResponse
name|response
decl_stmt|;
specifier|private
name|MockAsyncContext
name|ctx
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|broadcaster
operator|=
operator|new
name|SseBroadcasterImpl
argument_list|()
expr_stmt|;
name|response
operator|=
operator|new
name|MockHttpServletResponse
argument_list|()
expr_stmt|;
name|writer
operator|=
name|mock
argument_list|(
name|MessageBodyWriter
operator|.
name|class
argument_list|)
expr_stmt|;
name|ctx
operator|=
operator|new
name|MockAsyncContext
argument_list|(
operator|new
name|MockHttpServletRequest
argument_list|()
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnCloseCallbackIsCalled
parameter_list|()
block|{
specifier|final
name|LongAdder
name|adder
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
specifier|final
name|SseEventSinkImpl
name|sink
init|=
operator|new
name|SseEventSinkImpl
argument_list|(
name|writer
argument_list|,
literal|null
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
name|broadcaster
operator|.
name|register
argument_list|(
name|sink
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|onClose
argument_list|(
name|s
lambda|->
block|{
if|if
condition|(
name|s
operator|==
name|sink
condition|)
block|{
name|adder
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|adder
operator|.
name|intValue
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|sink
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|adder
operator|.
name|intValue
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnCloseCallbackIsCalledForBroadcaster
parameter_list|()
block|{
specifier|final
name|LongAdder
name|adder
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
specifier|final
name|SseEventSinkImpl
name|sink
init|=
operator|new
name|SseEventSinkImpl
argument_list|(
name|writer
argument_list|,
literal|null
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
name|broadcaster
operator|.
name|register
argument_list|(
name|sink
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|onClose
argument_list|(
name|s
lambda|->
block|{
if|if
condition|(
name|s
operator|==
name|sink
condition|)
block|{
name|adder
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|adder
operator|.
name|intValue
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|adder
operator|.
name|intValue
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOnErrorCallbackIsCalled
parameter_list|()
throws|throws
name|WebApplicationException
throws|,
name|IOException
block|{
name|when
argument_list|(
name|writer
operator|.
name|isWriteable
argument_list|(
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|,
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|LongAdder
name|adder
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
specifier|final
name|SseEventSinkImpl
name|sink
init|=
operator|new
name|SseEventSinkImpl
argument_list|(
name|writer
argument_list|,
literal|null
argument_list|,
name|ctx
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|CompletionStage
argument_list|<
name|?
argument_list|>
name|send
parameter_list|(
name|OutboundSseEvent
name|event
parameter_list|)
block|{
name|ctx
operator|.
name|start
argument_list|(
parameter_list|()
lambda|->
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to schedule async task"
argument_list|)
throw|;
block|}
argument_list|)
expr_stmt|;
return|return
name|CompletableFuture
operator|.
name|completedFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|broadcaster
operator|.
name|register
argument_list|(
name|sink
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|onError
argument_list|(
parameter_list|(
name|s
parameter_list|,
name|ex
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|s
operator|==
name|sink
condition|)
block|{
name|adder
operator|.
name|increment
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|adder
operator|.
name|intValue
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|broadcast
argument_list|(
operator|new
name|OutboundSseEventImpl
operator|.
name|BuilderImpl
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|broadcaster
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|adder
operator|.
name|intValue
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

