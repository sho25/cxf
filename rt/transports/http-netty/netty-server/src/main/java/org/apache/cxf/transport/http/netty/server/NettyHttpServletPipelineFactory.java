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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
package|;
end_package

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
name|logging
operator|.
name|Level
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
name|net
operator|.
name|ssl
operator|.
name|SSLEngine
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|interceptor
operator|.
name|ChannelInterceptor
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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|interceptor
operator|.
name|HttpSessionInterceptor
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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|session
operator|.
name|DefaultHttpSessionStore
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
name|transport
operator|.
name|http
operator|.
name|netty
operator|.
name|server
operator|.
name|session
operator|.
name|HttpSessionStore
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
name|transport
operator|.
name|https
operator|.
name|SSLUtils
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelInitializer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelPipeline
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|group
operator|.
name|ChannelGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|group
operator|.
name|DefaultChannelGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpContentCompressor
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpObjectAggregator
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequestDecoder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponseEncoder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|ssl
operator|.
name|SslHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|timeout
operator|.
name|IdleStateHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|concurrent
operator|.
name|DefaultEventExecutorGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|concurrent
operator|.
name|EventExecutorGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ImmediateEventExecutor
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpServletPipelineFactory
extends|extends
name|ChannelInitializer
argument_list|<
name|Channel
argument_list|>
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
name|NettyHttpServletPipelineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//Holds the child channel
specifier|private
specifier|final
name|ChannelGroup
name|allChannels
init|=
operator|new
name|DefaultChannelGroup
argument_list|(
name|ImmediateEventExecutor
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
empty_stmt|;
specifier|private
specifier|final
name|HttpSessionWatchdog
name|watchdog
decl_stmt|;
specifier|private
specifier|final
name|TLSServerParameters
name|tlsServerParameters
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|supportSession
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|NettyHttpContextHandler
argument_list|>
name|handlerMap
decl_stmt|;
specifier|private
specifier|final
name|int
name|maxChunkContentSize
decl_stmt|;
specifier|private
specifier|final
name|EventExecutorGroup
name|applicationExecutor
decl_stmt|;
specifier|private
specifier|final
name|NettyHttpServerEngine
name|nettyHttpServerEngine
decl_stmt|;
specifier|public
name|NettyHttpServletPipelineFactory
parameter_list|(
name|TLSServerParameters
name|tlsServerParameters
parameter_list|,
name|boolean
name|supportSession
parameter_list|,
name|int
name|threadPoolSize
parameter_list|,
name|int
name|maxChunkContentSize
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|NettyHttpContextHandler
argument_list|>
name|handlerMap
parameter_list|,
name|NettyHttpServerEngine
name|engine
parameter_list|)
block|{
name|this
operator|.
name|supportSession
operator|=
name|supportSession
expr_stmt|;
name|this
operator|.
name|watchdog
operator|=
operator|new
name|HttpSessionWatchdog
argument_list|()
expr_stmt|;
name|this
operator|.
name|handlerMap
operator|=
name|handlerMap
expr_stmt|;
name|this
operator|.
name|tlsServerParameters
operator|=
name|tlsServerParameters
expr_stmt|;
name|this
operator|.
name|maxChunkContentSize
operator|=
name|maxChunkContentSize
expr_stmt|;
name|this
operator|.
name|nettyHttpServerEngine
operator|=
name|engine
expr_stmt|;
comment|//TODO need to configure the thread size of EventExecutorGroup
name|applicationExecutor
operator|=
operator|new
name|DefaultEventExecutorGroup
argument_list|(
name|threadPoolSize
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|NettyHttpContextHandler
argument_list|>
name|getHttpContextHandlerMap
parameter_list|()
block|{
return|return
name|handlerMap
return|;
block|}
specifier|public
name|ChannelGroup
name|getAllChannels
parameter_list|()
block|{
return|return
name|allChannels
return|;
block|}
specifier|public
name|NettyHttpContextHandler
name|getNettyHttpHandler
parameter_list|(
name|String
name|url
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|NettyHttpContextHandler
argument_list|>
name|entry
range|:
name|handlerMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// Here just check the context path first
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
if|if
condition|(
name|supportSession
condition|)
block|{
operator|new
name|Thread
argument_list|(
name|watchdog
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|allChannels
operator|.
name|close
argument_list|()
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
name|watchdog
operator|.
name|stopWatching
argument_list|()
expr_stmt|;
name|applicationExecutor
operator|.
name|shutdownGracefully
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|HttpSessionStore
name|getHttpSessionStore
parameter_list|()
block|{
return|return
operator|new
name|DefaultHttpSessionStore
argument_list|()
return|;
block|}
specifier|protected
name|NettyHttpServletHandler
name|getServletHandler
parameter_list|()
block|{
name|NettyHttpServletHandler
name|handler
init|=
operator|new
name|NettyHttpServletHandler
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|handler
operator|.
name|addInterceptor
argument_list|(
operator|new
name|ChannelInterceptor
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportSession
condition|)
block|{
name|handler
operator|.
name|addInterceptor
argument_list|(
operator|new
name|HttpSessionInterceptor
argument_list|(
name|getHttpSessionStore
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|handler
return|;
block|}
specifier|protected
name|ChannelPipeline
name|getDefaulHttpChannelPipeline
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Create a default pipeline implementation.
name|ChannelPipeline
name|pipeline
init|=
name|channel
operator|.
name|pipeline
argument_list|()
decl_stmt|;
name|SslHandler
name|sslHandler
init|=
name|configureServerSSLOnDemand
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslHandler
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Server SSL handler configured and added as an interceptor against the ChannelPipeline: {}"
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"ssl"
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
block|}
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"decoder"
argument_list|,
operator|new
name|HttpRequestDecoder
argument_list|()
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"aggregator"
argument_list|,
operator|new
name|HttpObjectAggregator
argument_list|(
name|maxChunkContentSize
argument_list|)
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"encoder"
argument_list|,
operator|new
name|HttpResponseEncoder
argument_list|()
argument_list|)
expr_stmt|;
comment|// Remove the following line if you don't want automatic content
comment|// compression.
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"deflater"
argument_list|,
operator|new
name|HttpContentCompressor
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set up the idle handler
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"idle"
argument_list|,
operator|new
name|IdleStateHandler
argument_list|(
name|nettyHttpServerEngine
operator|.
name|getReadIdleTime
argument_list|()
argument_list|,
name|nettyHttpServerEngine
operator|.
name|getWriteIdleTime
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|pipeline
return|;
block|}
specifier|private
name|SslHandler
name|configureServerSSLOnDemand
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|tlsServerParameters
operator|!=
literal|null
condition|)
block|{
name|SSLEngine
name|sslEngine
init|=
name|SSLUtils
operator|.
name|createServerSSLEngine
argument_list|(
name|tlsServerParameters
argument_list|)
decl_stmt|;
return|return
operator|new
name|SslHandler
argument_list|(
name|sslEngine
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
class|class
name|HttpSessionWatchdog
implements|implements
name|Runnable
block|{
specifier|private
name|boolean
name|shouldStopWatching
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
operator|!
name|shouldStopWatching
condition|)
block|{
try|try
block|{
name|HttpSessionStore
name|store
init|=
name|getHttpSessionStore
argument_list|()
decl_stmt|;
if|if
condition|(
name|store
operator|!=
literal|null
condition|)
block|{
name|store
operator|.
name|destroyInactiveSessions
argument_list|()
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
return|return;
block|}
block|}
block|}
specifier|public
name|void
name|stopWatching
parameter_list|()
block|{
name|this
operator|.
name|shouldStopWatching
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|initChannel
parameter_list|(
name|Channel
name|ch
parameter_list|)
throws|throws
name|Exception
block|{
name|ChannelPipeline
name|pipeline
init|=
name|getDefaulHttpChannelPipeline
argument_list|(
name|ch
argument_list|)
decl_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
name|applicationExecutor
argument_list|,
literal|"handler"
argument_list|,
name|this
operator|.
name|getServletHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

