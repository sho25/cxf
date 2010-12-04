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
name|endpoint
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
name|concurrent
operator|.
name|ExecutionException
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
name|Future
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
name|TimeoutException
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

begin_comment
comment|/**  * Asynchronous callback object for calls to {@link Client#invoke(ClientCallback, String, Object...)}  * and related functions.  *  * The default behavior of this expects the following pattern:  *<ol>  *<li>ClientCallback cb = new ClientCallback();</li>  *<li>client.invoke(cb, "someMethod", ....);</li>  *<li>cb.wait();</li>  *<li>// CXF calls notify on the callback object when the operation is complete.</li>  *</ol>  */
end_comment

begin_class
specifier|public
class|class
name|ClientCallback
implements|implements
name|Future
argument_list|<
name|Object
index|[]
argument_list|>
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|context
decl_stmt|;
specifier|protected
name|Object
index|[]
name|result
decl_stmt|;
specifier|protected
name|Throwable
name|exception
decl_stmt|;
specifier|protected
specifier|volatile
name|boolean
name|done
decl_stmt|;
specifier|protected
name|boolean
name|cancelled
decl_stmt|;
specifier|protected
name|boolean
name|started
decl_stmt|;
specifier|public
name|ClientCallback
parameter_list|()
block|{     }
comment|/**      * Called when a message is first received prior to any actions      * being applied to the message.   The InterceptorChain is setup so      * modifications to that can be done.      */
specifier|public
name|void
name|start
parameter_list|(
name|Message
name|msg
parameter_list|)
block|{
name|started
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * If the processing of the incoming message proceeds normally, this      * method is called with the response context values and the resulting objects.      *      * The default behavior just stores the objects and calls notifyAll to wake      * up threads waiting for the response.      *      * @param ctx      * @param res      */
specifier|public
name|void
name|handleResponse
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|,
name|Object
index|[]
name|res
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
name|result
operator|=
name|res
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * If processing of the incoming message results in an exception, this      * method is called with the resulting exception.      *      * The default behavior just stores the objects and calls notifyAll to wake      * up threads waiting for the response.      *      * @param ctx      * @param ex      */
specifier|public
name|void
name|handleException
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
parameter_list|,
name|Throwable
name|ex
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
name|exception
operator|=
name|ex
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
if|if
condition|(
operator|!
name|started
condition|)
block|{
name|cancelled
operator|=
literal|true
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * return the map of items returned from an operation.      * @return      * @throws InterruptedException if the operation was cancelled.      * @throws ExecutionException if the operation resulted in a fault.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getResponseContext
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cancelled
condition|)
block|{
throw|throw
operator|new
name|InterruptedException
argument_list|(
literal|"Operation Cancelled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
return|return
name|context
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Object
index|[]
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cancelled
condition|)
block|{
throw|throw
operator|new
name|InterruptedException
argument_list|(
literal|"Operation Cancelled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Object
index|[]
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|unit
operator|.
name|timedWait
argument_list|(
name|this
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cancelled
condition|)
block|{
throw|throw
operator|new
name|InterruptedException
argument_list|(
literal|"Operation Cancelled"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|done
condition|)
block|{
throw|throw
operator|new
name|TimeoutException
argument_list|(
literal|"Timeout Exceeded"
argument_list|)
throw|;
block|}
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
name|cancelled
return|;
block|}
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|done
return|;
block|}
comment|/*      * If the operation completes with a fault, the resulting exception object ends up here.      */
specifier|public
name|Throwable
name|getException
parameter_list|()
block|{
return|return
name|exception
return|;
block|}
block|}
end_class

end_unit

