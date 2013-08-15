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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|BlockingQueue
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
name|LinkedBlockingDeque
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
name|ChannelDuplexHandler
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
name|ChannelHandlerContext
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
name|ChannelPromise
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
name|HttpResponse
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpClientHandler
extends|extends
name|ChannelDuplexHandler
block|{
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|NettyHttpClientRequest
argument_list|>
name|sendedQueue
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<
name|NettyHttpClientRequest
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|channelRead
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Object
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|msg
operator|instanceof
name|HttpResponse
condition|)
block|{
comment|// just make sure we can combine the request and response together
name|HttpResponse
name|response
init|=
operator|(
name|HttpResponse
operator|)
name|msg
decl_stmt|;
name|NettyHttpClientRequest
name|request
init|=
name|sendedQueue
operator|.
name|poll
argument_list|()
decl_stmt|;
name|request
operator|.
name|setResponse
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// calling the callback here
name|request
operator|.
name|getCxfResponseCallback
argument_list|()
operator|.
name|responseReceived
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|channelRead
argument_list|(
name|ctx
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Object
name|msg
parameter_list|,
name|ChannelPromise
name|promise
parameter_list|)
throws|throws
name|Exception
block|{
comment|// need to deal with the request
if|if
condition|(
name|msg
operator|instanceof
name|NettyHttpClientRequest
condition|)
block|{
name|NettyHttpClientRequest
name|request
init|=
operator|(
name|NettyHttpClientRequest
operator|)
name|msg
decl_stmt|;
name|sendedQueue
operator|.
name|put
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|writeAndFlush
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|write
argument_list|(
name|ctx
argument_list|,
name|msg
argument_list|,
name|promise
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
comment|//TODO need to handle the exception here
name|cause
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|channelReadComplete
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

