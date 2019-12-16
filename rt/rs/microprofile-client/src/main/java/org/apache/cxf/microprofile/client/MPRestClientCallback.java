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
name|microprofile
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
name|CancellationException
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
name|CompletionException
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
name|ExecutorService
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
name|ForkJoinPool
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|client
operator|.
name|InvocationCallback
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
name|client
operator|.
name|JaxrsClientCallback
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

begin_class
specifier|public
class|class
name|MPRestClientCallback
parameter_list|<
name|T
parameter_list|>
extends|extends
name|JaxrsClientCallback
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
specifier|public
name|MPRestClientCallback
parameter_list|(
name|InvocationCallback
argument_list|<
name|T
argument_list|>
name|handler
parameter_list|,
name|Message
name|outMessage
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|responseClass
parameter_list|,
name|Type
name|outGenericType
parameter_list|)
block|{
name|super
argument_list|(
name|handler
argument_list|,
name|responseClass
argument_list|,
name|outGenericType
argument_list|)
expr_stmt|;
name|ExecutorService
name|es
init|=
name|outMessage
operator|.
name|get
argument_list|(
name|ExecutorService
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|es
operator|==
literal|null
condition|)
block|{
name|es
operator|=
name|AccessController
operator|.
name|doPrivileged
argument_list|(
call|(
name|PrivilegedAction
argument_list|<
name|ExecutorService
argument_list|>
call|)
argument_list|()
operator|->
block|{
return|return
name|ForkJoinPool
operator|.
name|commonPool
argument_list|()
return|;
block|}
block|)
empty_stmt|;
block|}
name|executor
operator|=
name|es
expr_stmt|;
block|}
end_class

begin_function
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|Future
argument_list|<
name|T
argument_list|>
name|createFuture
parameter_list|()
block|{
return|return
operator|(
name|Future
argument_list|<
name|T
argument_list|>
operator|)
name|CompletableFuture
operator|.
name|supplyAsync
argument_list|(
parameter_list|()
lambda|->
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
try|try
block|{
name|this
operator|.
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompletionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
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
name|CompletionException
argument_list|(
name|exception
argument_list|)
throw|;
block|}
if|if
condition|(
name|isCancelled
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CancellationException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CompletionStage has been notified, indicating completion, but is not completed."
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|get
argument_list|()
index|[
literal|0
index|]
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CompletionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|,
name|executor
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

