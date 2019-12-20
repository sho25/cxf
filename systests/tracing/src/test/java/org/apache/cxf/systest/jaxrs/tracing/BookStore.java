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
name|tracing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|Executors
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
name|ScheduledExecutorService
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
name|PUT
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
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
name|core
operator|.
name|MediaType
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
name|core
operator|.
name|Response
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
name|systest
operator|.
name|Book
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
name|tracing
operator|.
name|Traceable
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
name|tracing
operator|.
name|TracerContext
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore/"
argument_list|)
specifier|public
class|class
name|BookStore
parameter_list|<
name|T
extends|extends
name|Closeable
parameter_list|>
block|{
annotation|@
name|Context
specifier|private
name|TracerContext
name|tracer
decl_stmt|;
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|T
name|span
init|=
name|tracer
operator|.
name|startSpan
argument_list|(
literal|"Get Books"
argument_list|)
init|)
block|{
return|return
name|books
argument_list|()
return|;
block|}
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/async"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|void
name|getBooksAsync
parameter_list|(
annotation|@
name|Suspended
specifier|final
name|AsyncResponse
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|tracer
operator|.
name|continueSpan
argument_list|(
operator|new
name|Traceable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|(
specifier|final
name|TracerContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|executor
operator|.
name|schedule
argument_list|(
name|tracer
operator|.
name|wrap
argument_list|(
literal|"Processing books"
argument_list|,
operator|new
name|Traceable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|(
specifier|final
name|TracerContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|response
operator|.
name|resume
argument_list|(
name|books
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
argument_list|,
literal|200L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
comment|// Simulate some running job
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/async/notrace"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|void
name|getBooksAsyncNoTrace
parameter_list|(
annotation|@
name|Suspended
specifier|final
name|AsyncResponse
name|response
parameter_list|)
block|{
name|executor
operator|.
name|schedule
argument_list|(
parameter_list|()
lambda|->
name|response
operator|.
name|resume
argument_list|(
name|books
argument_list|()
argument_list|)
argument_list|,
literal|200L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
comment|// Simulate some running job
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/pseudo-async"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|getBooksPseudoAsync
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|tracer
operator|.
name|continueSpan
argument_list|(
operator|new
name|Traceable
argument_list|<
name|Collection
argument_list|<
name|Book
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|call
parameter_list|(
specifier|final
name|TracerContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|tracer
operator|.
name|wrap
argument_list|(
literal|"Processing books"
argument_list|,
operator|new
name|Traceable
argument_list|<
name|Collection
argument_list|<
name|Book
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|call
parameter_list|(
specifier|final
name|TracerContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|books
argument_list|()
return|;
block|}
block|}
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/book/{id}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Book
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|final
name|String
name|id
parameter_list|)
block|{
name|tracer
operator|.
name|annotate
argument_list|(
literal|"book-id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
operator|new
name|Book
argument_list|(
literal|"Apache CXF in Action"
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/process"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Response
name|processBooks
parameter_list|()
throws|throws
name|Exception
block|{
name|tracer
operator|.
name|wrap
argument_list|(
literal|"Processing books"
argument_list|,
operator|new
name|Traceable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|(
specifier|final
name|TracerContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|timeline
argument_list|(
literal|"Processing started"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/books/long"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|getBooksLong
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
return|return
name|books
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|Collection
argument_list|<
name|Book
argument_list|>
name|books
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Book
argument_list|(
literal|"Apache CXF in Action"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Book
argument_list|(
literal|"Mastering Apache CXF"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

