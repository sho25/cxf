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
name|nio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|InputStream
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
name|concurrent
operator|.
name|atomic
operator|.
name|LongAdder
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
name|annotations
operator|.
name|UseNio
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
name|helpers
operator|.
name|IOUtils
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
name|nio
operator|.
name|NioReadEntity
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
name|nio
operator|.
name|NioWriteEntity
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
name|NioBookStore
block|{
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
specifier|public
name|Response
name|getBookStream
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayInputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/books.txt"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|entity
argument_list|(
operator|new
name|NioWriteEntity
argument_list|(
name|out
lambda|->
block|{
specifier|final
name|int
name|n
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>=
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|closeInputStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// by default the runtime will throw the exception itself
comment|// if the error handler is not provided
comment|//,
comment|//throwable -> {
comment|//    throw throwable;
comment|//}
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/is"
argument_list|)
annotation|@
name|UseNio
specifier|public
name|InputStream
name|getBookStreamFromInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/files/books.txt"
argument_list|)
return|;
block|}
annotation|@
name|POST
annotation|@
name|Consumes
argument_list|(
name|MediaType
operator|.
name|APPLICATION_OCTET_STREAM
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|TEXT_PLAIN
argument_list|)
specifier|public
name|void
name|uploadBookStream
parameter_list|(
annotation|@
name|Suspended
name|AsyncResponse
name|response
parameter_list|)
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
specifier|final
name|LongAdder
name|adder
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
operator|new
name|NioReadEntity
argument_list|(
comment|// read handler
name|in
lambda|->
block|{
specifier|final
name|int
name|n
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|adder
operator|.
name|add
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
comment|// completion handler
parameter_list|()
lambda|->
block|{
name|closeOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|response
operator|.
name|resume
argument_list|(
literal|"Book Store uploaded: "
operator|+
name|adder
operator|.
name|longValue
argument_list|()
operator|+
literal|" bytes"
argument_list|)
expr_stmt|;
block|}
comment|// by default the runtime will resume AsyncResponse with Throwable itself
comment|// if the error handler is not provided
comment|//,
comment|// error handler
comment|//t -> {
comment|//    response.resume(t);
comment|//}
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|closeInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
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
name|ex
parameter_list|)
block|{
comment|/* do nothing */
block|}
block|}
specifier|private
specifier|static
name|void
name|closeOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
try|try
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|/* do nothing */
block|}
block|}
block|}
end_class

end_unit

