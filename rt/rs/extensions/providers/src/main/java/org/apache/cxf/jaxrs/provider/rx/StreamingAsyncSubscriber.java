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
name|provider
operator|.
name|rx
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
name|LinkedBlockingQueue
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
name|TimeoutHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|StringUtils
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
name|ext
operator|.
name|StreamingResponse
import|;
end_import

begin_class
specifier|public
class|class
name|StreamingAsyncSubscriber
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractAsyncSubscriber
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|BlockingQueue
argument_list|<
name|T
argument_list|>
name|queue
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|openTag
decl_stmt|;
specifier|private
name|String
name|closeTag
decl_stmt|;
specifier|private
name|String
name|separator
decl_stmt|;
specifier|private
name|long
name|pollTimeout
decl_stmt|;
specifier|private
name|long
name|asyncTimeout
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|completed
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|firstWriteDone
decl_stmt|;
specifier|public
name|StreamingAsyncSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|,
name|String
name|openTag
parameter_list|,
name|String
name|closeTag
parameter_list|,
name|String
name|sep
parameter_list|)
block|{
name|this
argument_list|(
name|ar
argument_list|,
name|openTag
argument_list|,
name|closeTag
argument_list|,
literal|""
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StreamingAsyncSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|,
name|String
name|openTag
parameter_list|,
name|String
name|closeTag
parameter_list|,
name|String
name|sep
parameter_list|,
name|long
name|pollTimeout
parameter_list|)
block|{
name|this
argument_list|(
name|ar
argument_list|,
name|openTag
argument_list|,
name|closeTag
argument_list|,
name|sep
argument_list|,
name|pollTimeout
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|StreamingAsyncSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|,
name|String
name|openTag
parameter_list|,
name|String
name|closeTag
parameter_list|,
name|String
name|sep
parameter_list|,
name|long
name|pollTimeout
parameter_list|,
name|long
name|asyncTimeout
parameter_list|)
block|{
name|super
argument_list|(
name|ar
argument_list|)
expr_stmt|;
name|this
operator|.
name|openTag
operator|=
name|openTag
expr_stmt|;
name|this
operator|.
name|closeTag
operator|=
name|closeTag
expr_stmt|;
name|this
operator|.
name|separator
operator|=
name|sep
expr_stmt|;
name|this
operator|.
name|pollTimeout
operator|=
name|pollTimeout
expr_stmt|;
name|this
operator|.
name|asyncTimeout
operator|=
literal|0
expr_stmt|;
if|if
condition|(
name|asyncTimeout
operator|>
literal|0
condition|)
block|{
name|ar
operator|.
name|setTimeout
argument_list|(
name|asyncTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|ar
operator|.
name|setTimeoutHandler
argument_list|(
operator|new
name|TimeoutHandlerImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onStart
parameter_list|()
block|{
if|if
condition|(
name|asyncTimeout
operator|==
literal|0
condition|)
block|{
name|resumeAsyncResponse
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|resumeAsyncResponse
parameter_list|()
block|{
name|super
operator|.
name|resume
argument_list|(
operator|new
name|StreamingResponseImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onCompleted
parameter_list|()
block|{
name|completed
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|T
name|bean
parameter_list|)
block|{
if|if
condition|(
name|asyncTimeout
operator|>
literal|0
operator|&&
name|getAsyncResponse
argument_list|()
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|resumeAsyncResponse
argument_list|()
expr_stmt|;
block|}
name|queue
operator|.
name|add
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|StreamingResponseImpl
implements|implements
name|StreamingResponse
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|Writer
argument_list|<
name|T
argument_list|>
name|writer
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|openTag
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|getEntityStream
argument_list|()
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|getBytesUtf8
argument_list|(
name|openTag
argument_list|)
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|completed
operator|||
name|queue
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|T
name|bean
init|=
name|queue
operator|.
name|poll
argument_list|(
name|pollTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|bean
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|firstWriteDone
condition|)
block|{
name|writer
operator|.
name|getEntityStream
argument_list|()
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|getBytesUtf8
argument_list|(
name|separator
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|firstWriteDone
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
if|if
condition|(
name|closeTag
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|getEntityStream
argument_list|()
operator|.
name|write
argument_list|(
name|StringUtils
operator|.
name|getBytesUtf8
argument_list|(
name|closeTag
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
class|class
name|TimeoutHandlerImpl
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
if|if
condition|(
name|queue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|asyncResponse
operator|.
name|setTimeout
argument_list|(
name|asyncTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|resumeAsyncResponse
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

