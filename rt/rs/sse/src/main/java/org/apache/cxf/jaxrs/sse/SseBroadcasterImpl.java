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
name|Set
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
name|CopyOnWriteArraySet
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
name|SseEventOutput
import|;
end_import

begin_class
specifier|public
class|class
name|SseBroadcasterImpl
implements|implements
name|SseBroadcaster
block|{
specifier|private
specifier|final
name|Set
argument_list|<
name|SseEventOutput
argument_list|>
name|outputs
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Listener
argument_list|>
name|listeners
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|register
parameter_list|(
name|Listener
name|listener
parameter_list|)
block|{
return|return
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|register
parameter_list|(
name|SseEventOutput
name|output
parameter_list|)
block|{
return|return
name|outputs
operator|.
name|add
argument_list|(
name|output
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|broadcast
parameter_list|(
name|OutboundSseEvent
name|event
parameter_list|)
block|{
for|for
control|(
specifier|final
name|SseEventOutput
name|output
range|:
name|outputs
control|)
block|{
try|try
block|{
name|output
operator|.
name|write
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
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
name|onException
argument_list|(
name|output
argument_list|,
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
specifier|final
name|SseEventOutput
name|output
range|:
name|outputs
control|)
block|{
try|try
block|{
name|output
operator|.
name|close
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
name|onClose
argument_list|(
name|output
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
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
name|onException
argument_list|(
name|output
argument_list|,
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

