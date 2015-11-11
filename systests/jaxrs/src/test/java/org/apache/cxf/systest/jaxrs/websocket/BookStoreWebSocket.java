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
name|websocket
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

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
name|Iterator
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
name|Executors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Consumes
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
name|HeaderParam
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
name|POST
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
name|WebApplicationException
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
name|StreamingOutput
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
name|jaxrs
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
name|transport
operator|.
name|websocket
operator|.
name|WebSocketConstants
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/web/bookstore"
argument_list|)
specifier|public
class|class
name|BookStoreWebSocket
block|{
specifier|private
specifier|static
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|OutputStream
argument_list|>
name|eventsStreams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|OutputStream
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/booknames"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|byte
index|[]
name|getBookName
parameter_list|()
block|{
return|return
literal|"CXF in Action"
operator|.
name|getBytes
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/booknames/servletstream"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|void
name|getBookNameStream
parameter_list|(
annotation|@
name|Context
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|Exception
block|{
name|OutputStream
name|os
init|=
name|response
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|response
operator|.
name|setContentType
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|"CXF in Action"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
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
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
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
name|long
name|id
parameter_list|)
block|{
return|return
operator|new
name|Book
argument_list|(
literal|"CXF in Action"
argument_list|,
name|id
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Path
argument_list|(
literal|"/booksplain"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"text/plain"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|Long
name|echoBookId
parameter_list|(
name|long
name|theBookId
parameter_list|)
block|{
return|return
operator|new
name|Long
argument_list|(
name|theBookId
argument_list|)
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/bookbought"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/*"
argument_list|)
specifier|public
name|StreamingOutput
name|getBookBought
parameter_list|()
block|{
return|return
operator|new
name|StreamingOutput
argument_list|()
block|{
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|out
operator|.
name|write
argument_list|(
operator|(
literal|"Today: "
operator|+
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
comment|// just for testing, using a thread
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
for|for
control|(
name|int
name|r
init|=
literal|2
init|,
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|5
condition|;
name|r
operator|*=
literal|2
operator|,
name|i
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|r
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/bookstream"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
specifier|public
name|StreamingResponse
argument_list|<
name|Book
argument_list|>
name|getBookStream
parameter_list|()
block|{
return|return
operator|new
name|StreamingResponse
argument_list|<
name|Book
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|writeTo
parameter_list|(
specifier|final
name|StreamingResponse
operator|.
name|Writer
argument_list|<
name|Book
argument_list|>
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
operator|new
name|Book
argument_list|(
literal|"WebSocket1"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
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
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<=
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
operator|new
name|Book
argument_list|(
literal|"WebSocket"
operator|+
name|i
argument_list|,
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|getEntityStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/hold/{t}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|hold
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"t"
argument_list|)
name|long
name|t
parameter_list|)
block|{
name|Date
name|from
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|"Held from "
operator|+
name|from
operator|+
literal|" for "
operator|+
name|t
operator|+
literal|" ms"
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/events/register"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|StreamingOutput
name|registerEventsStream
parameter_list|(
annotation|@
name|HeaderParam
argument_list|(
name|WebSocketConstants
operator|.
name|DEFAULT_REQUEST_ID_KEY
argument_list|)
name|String
name|reqid
parameter_list|)
block|{
specifier|final
name|String
name|key
init|=
name|reqid
operator|==
literal|null
condition|?
literal|"*"
else|:
name|reqid
decl_stmt|;
return|return
operator|new
name|StreamingOutput
argument_list|()
block|{
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|eventsStreams
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
operator|(
literal|"Registered "
operator|+
name|key
operator|+
literal|" at "
operator|+
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|()
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/events/create/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|createEvent
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|OutputStream
argument_list|>
name|it
init|=
name|eventsStreams
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|OutputStream
name|out
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
operator|(
literal|"News: event "
operator|+
name|name
operator|+
literal|" created"
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|name
operator|+
literal|" created"
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/events/unregister/{key}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|unregisterEventsStream
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|eventsStreams
operator|.
name|remove
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|?
literal|"Unregistered: "
else|:
literal|"Already Unregistered: "
operator|)
operator|+
name|key
return|;
block|}
block|}
end_class

end_unit

