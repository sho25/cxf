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
operator|.
name|reactive
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Suspended
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
name|reactivestreams
operator|.
name|server
operator|.
name|AbstractSubscriber
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
name|reactivestreams
operator|.
name|server
operator|.
name|JsonStreamingAsyncSubscriber
import|;
end_import

begin_import
import|import
name|io
operator|.
name|reactivex
operator|.
name|BackpressureStrategy
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
name|Single
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
literal|"/flowable"
argument_list|)
specifier|public
class|class
name|RxJava2FlowableService
block|{
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textJson"
argument_list|)
specifier|public
name|Flowable
argument_list|<
name|HelloWorldBean
argument_list|>
name|getJson
parameter_list|()
block|{
return|return
name|Flowable
operator|.
name|just
argument_list|(
operator|new
name|HelloWorldBean
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textJsonImplicitListAsync"
argument_list|)
specifier|public
name|void
name|getJsonImplicitListAsync
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|ar
parameter_list|)
block|{
specifier|final
name|HelloWorldBean
name|bean1
init|=
operator|new
name|HelloWorldBean
argument_list|()
decl_stmt|;
specifier|final
name|HelloWorldBean
name|bean2
init|=
operator|new
name|HelloWorldBean
argument_list|(
literal|"Ciao"
argument_list|)
decl_stmt|;
operator|new
name|Thread
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
literal|1000
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
name|Flowable
operator|.
name|just
argument_list|(
name|bean1
argument_list|,
name|bean2
argument_list|)
operator|.
name|subscribe
argument_list|(
operator|new
name|ListAsyncSubscriber
argument_list|<
name|HelloWorldBean
argument_list|>
argument_list|(
name|ar
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textJsonImplicitListAsyncStream"
argument_list|)
specifier|public
name|void
name|getJsonImplicitListStreamingAsync
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|Flowable
operator|.
name|just
argument_list|(
literal|"Hello"
argument_list|,
literal|"Ciao"
argument_list|)
operator|.
name|map
argument_list|(
name|HelloWorldBean
operator|::
operator|new
argument_list|)
operator|.
name|subscribeOn
argument_list|(
name|Schedulers
operator|.
name|computation
argument_list|()
argument_list|)
operator|.
name|subscribe
argument_list|(
operator|new
name|JsonStreamingAsyncSubscriber
argument_list|<
name|HelloWorldBean
argument_list|>
argument_list|(
name|ar
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textJsonImplicitList"
argument_list|)
specifier|public
name|Flowable
argument_list|<
name|HelloWorldBean
argument_list|>
name|getJsonImplicitList
parameter_list|()
block|{
return|return
name|Flowable
operator|.
name|create
argument_list|(
name|subscriber
lambda|->
block|{
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
parameter_list|()
lambda|->
block|{
name|subscriber
operator|.
name|onNext
argument_list|(
operator|new
name|HelloWorldBean
argument_list|(
literal|"Hello"
argument_list|)
argument_list|)
expr_stmt|;
name|sleep
argument_list|()
expr_stmt|;
name|subscriber
operator|.
name|onNext
argument_list|(
operator|new
name|HelloWorldBean
argument_list|(
literal|"Ciao"
argument_list|)
argument_list|)
expr_stmt|;
name|sleep
argument_list|()
expr_stmt|;
name|subscriber
operator|.
name|onComplete
argument_list|()
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
argument_list|,
name|BackpressureStrategy
operator|.
name|MISSING
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textJsonSingle"
argument_list|)
specifier|public
name|Single
argument_list|<
name|HelloWorldBean
argument_list|>
name|getJsonSingle
parameter_list|()
block|{
name|CompletableFuture
argument_list|<
name|HelloWorldBean
argument_list|>
name|completableFuture
init|=
name|CompletableFuture
operator|.
name|supplyAsync
argument_list|(
parameter_list|()
lambda|->
block|{
name|sleep
argument_list|()
expr_stmt|;
return|return
operator|new
name|HelloWorldBean
argument_list|(
literal|"Hello"
argument_list|)
return|;
block|}
argument_list|)
decl_stmt|;
return|return
name|Single
operator|.
name|fromFuture
argument_list|(
name|completableFuture
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|sleep
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
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
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"textAsync"
argument_list|)
specifier|public
name|void
name|getTextAsync
parameter_list|(
annotation|@
name|Suspended
specifier|final
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|Flowable
operator|.
name|just
argument_list|(
literal|"Hello, "
argument_list|)
operator|.
name|map
argument_list|(
name|s
lambda|->
name|s
operator|+
literal|"world!"
argument_list|)
operator|.
name|subscribe
argument_list|(
operator|new
name|StringAsyncSubscriber
argument_list|(
name|ar
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|StringAsyncSubscriber
extends|extends
name|AbstractSubscriber
argument_list|<
name|String
argument_list|>
block|{
specifier|private
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringAsyncSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|super
argument_list|(
name|ar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|super
operator|.
name|resume
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onNext
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|super
operator|.
name|requestNext
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ListAsyncSubscriber
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractSubscriber
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|T
argument_list|>
name|beans
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|ListAsyncSubscriber
parameter_list|(
name|AsyncResponse
name|ar
parameter_list|)
block|{
name|super
argument_list|(
name|ar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|()
block|{
name|super
operator|.
name|resume
argument_list|(
name|beans
argument_list|)
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
name|beans
operator|.
name|add
argument_list|(
name|bean
argument_list|)
expr_stmt|;
name|super
operator|.
name|requestNext
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

