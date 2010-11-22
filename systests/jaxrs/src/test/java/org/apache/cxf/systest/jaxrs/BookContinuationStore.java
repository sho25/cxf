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
name|systest
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|ArrayBlockingQueue
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
name|ThreadPoolExecutor
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
name|annotation
operator|.
name|Resource
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|continuations
operator|.
name|Continuation
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
name|continuations
operator|.
name|ContinuationProvider
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
name|MessageContext
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore"
argument_list|)
specifier|public
class|class
name|BookContinuationStore
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|books
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Continuation
argument_list|>
name|suspended
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Continuation
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Executor
name|executor
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|5
argument_list|,
literal|5
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Resource
specifier|private
name|MessageContext
name|context
decl_stmt|;
specifier|public
name|BookContinuationStore
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/{id}"
argument_list|)
specifier|public
name|String
name|getBookDescription
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
return|return
name|handleContinuationRequest
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/books/subresources/"
argument_list|)
specifier|public
name|BookContinuationStore
name|getBookStore
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"{id}"
argument_list|)
specifier|public
name|String
name|handleContinuationRequest
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
name|Continuation
name|continuation
init|=
name|getContinuation
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|continuation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Failed to get continuation"
argument_list|)
throw|;
block|}
synchronized|synchronized
init|(
name|continuation
init|)
block|{
if|if
condition|(
name|continuation
operator|.
name|isNew
argument_list|()
condition|)
block|{
name|continuation
operator|.
name|setObject
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|suspendInvocation
argument_list|(
name|id
argument_list|,
name|continuation
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|savedId
init|=
name|continuation
operator|.
name|getObject
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|savedId
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"SavedId is wrong"
argument_list|)
throw|;
block|}
return|return
name|books
operator|.
name|get
argument_list|(
name|savedId
argument_list|)
return|;
block|}
block|}
comment|// unreachable
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|resumeRequest
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
name|Continuation
name|suspendedCont
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|suspended
init|)
block|{
name|suspendedCont
operator|=
name|suspended
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|suspendedCont
operator|!=
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|suspendedCont
init|)
block|{
name|suspendedCont
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|suspendInvocation
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
name|Continuation
name|cont
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Suspending invocation for "
operator|+
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|cont
operator|.
name|suspend
argument_list|(
literal|500000
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
synchronized|synchronized
init|(
name|suspended
init|)
block|{
name|suspended
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|cont
argument_list|)
expr_stmt|;
block|}
name|executor
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
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|resumeRequest
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Continuation
name|getContinuation
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Getting continuation for "
operator|+
name|name
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|suspended
init|)
block|{
name|Continuation
name|suspendedCont
init|=
name|suspended
operator|.
name|remove
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|suspendedCont
operator|!=
literal|null
condition|)
block|{
return|return
name|suspendedCont
return|;
block|}
block|}
name|ContinuationProvider
name|provider
init|=
operator|(
name|ContinuationProvider
operator|)
name|context
operator|.
name|get
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|provider
operator|.
name|getContinuation
argument_list|()
return|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|books
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|"CXF in Action1"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"2"
argument_list|,
literal|"CXF in Action2"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"3"
argument_list|,
literal|"CXF in Action3"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"4"
argument_list|,
literal|"CXF in Action4"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"5"
argument_list|,
literal|"CXF in Action5"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

