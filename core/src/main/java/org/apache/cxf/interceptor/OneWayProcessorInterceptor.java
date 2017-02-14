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
name|interceptor
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
name|io
operator|.
name|InputStream
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
name|RejectedExecutionException
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
name|endpoint
operator|.
name|Endpoint
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
name|io
operator|.
name|DelegatingInputStream
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
name|Exchange
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
name|MessageImpl
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
name|MessageUtils
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
name|AbstractPhaseInterceptor
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
name|Phase
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
name|Conduit
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
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|OneWayProcessorInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|USE_ORIGINAL_THREAD
init|=
name|OneWayProcessorInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".USE_ORIGINAL_THREAD"
decl_stmt|;
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
name|OneWayProcessorInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|OneWayProcessorInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_LOGICAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OneWayProcessorInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
operator|&&
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|//in a one way, if an exception is thrown, the stream needs to be closed
name|InputStream
name|in
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
operator|&&
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|message
operator|.
name|get
argument_list|(
name|OneWayProcessorInterceptor
operator|.
name|class
argument_list|)
operator|==
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Executor
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|//one way on server side, fork the rest of this chain onto the
comment|//workqueue, call the Outgoing chain directly.
name|message
operator|.
name|put
argument_list|(
name|OneWayProcessorInterceptor
operator|.
name|class
argument_list|,
name|this
argument_list|)
expr_stmt|;
specifier|final
name|InterceptorChain
name|chain
init|=
name|message
operator|.
name|getInterceptorChain
argument_list|()
decl_stmt|;
name|boolean
name|robust
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ROBUST_ONEWAY
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|useOriginalThread
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|USE_ORIGINAL_THREAD
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|useOriginalThread
operator|&&
operator|!
name|robust
condition|)
block|{
comment|//need to suck in all the data from the input stream as
comment|//the transport might discard any data on the stream when this
comment|//thread unwinds or when the empty response is sent back
name|DelegatingInputStream
name|in
init|=
name|message
operator|.
name|getContent
argument_list|(
name|DelegatingInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|cacheInput
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|robust
condition|)
block|{
comment|// continue to invoke the chain
name|chain
operator|.
name|pause
argument_list|()
expr_stmt|;
name|chain
operator|.
name|resume
argument_list|()
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// CXF-5629 fault has been delivered alread in resume()
return|return;
block|}
block|}
try|try
block|{
name|Message
name|partial
init|=
name|createMessage
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|partial
operator|.
name|remove
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|partial
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|Conduit
name|conduit
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
operator|.
name|getBackChannel
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduit
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|//for a one-way, the back channel could be
comment|//null if it knows it cannot send anything.
name|conduit
operator|.
name|prepare
argument_list|(
name|partial
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|close
argument_list|(
name|partial
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//IGNORE
block|}
if|if
condition|(
operator|!
name|useOriginalThread
operator|&&
operator|!
name|robust
condition|)
block|{
name|chain
operator|.
name|pause
argument_list|()
expr_stmt|;
try|try
block|{
specifier|final
name|Object
name|lock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
operator|.
name|getAutomaticWorkQueue
argument_list|()
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
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|lock
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
name|chain
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|//wait a few milliseconds for the background thread to start processing
comment|//Mostly just to make an attempt at keeping the ordering of the
comment|//messages coming in from a client.  Not guaranteed though.
name|lock
operator|.
name|wait
argument_list|(
literal|20
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Executor queue is full, run the oneway invocation task in caller thread."
operator|+
literal|"  Users can specify a larger executor queue to avoid this."
argument_list|)
expr_stmt|;
comment|// only block the thread if the prop is unset or set to false, otherwise let it go
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
literal|"org.apache.cxf.oneway.rejected_execution_exception"
argument_list|)
argument_list|)
condition|)
block|{
comment|//the executor queue is full, so run the task in the caller thread
name|chain
operator|.
name|unpause
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//ignore - likely a busy work queue so we'll just let the one-way go
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|Message
name|createMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|msg
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
return|;
block|}
block|}
end_class

end_unit

