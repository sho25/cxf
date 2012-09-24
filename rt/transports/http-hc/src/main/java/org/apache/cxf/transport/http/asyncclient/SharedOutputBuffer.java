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
name|asyncclient
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
name|io
operator|.
name|InterruptedIOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
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
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|ReentrantLock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|annotation
operator|.
name|ThreadSafe
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|ContentEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|IOControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|util
operator|.
name|ByteBufferAllocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|util
operator|.
name|ExpandableBuffer
import|;
end_import

begin_comment
comment|/**  * Content buffer that can be shared by multiple threads, usually the I/O dispatch of   * an I/O reactor and a worker thread.  *<p/>  * The I/O dispatch thread is expected to transfer data from the buffer to  *   {@link ContentEncoder} by calling {@link #produceContent(ContentEncoder)}.  *<p/>  * The worker thread is expected to write data to the buffer by calling  * {@link #write(int)}, {@link #write(byte[], int, int)} or {@link #writeCompleted()}  *<p/>  * In case of an abnormal situation or when no longer needed the buffer must be  * shut down using {@link #shutdown()} method.  */
end_comment

begin_class
annotation|@
name|ThreadSafe
specifier|public
class|class
name|SharedOutputBuffer
extends|extends
name|ExpandableBuffer
block|{
specifier|private
specifier|final
name|ReentrantLock
name|lock
decl_stmt|;
specifier|private
specifier|final
name|Condition
name|condition
decl_stmt|;
specifier|private
specifier|volatile
name|IOControl
name|ioctrl
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|shutdown
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|endOfStream
decl_stmt|;
specifier|private
specifier|volatile
name|ByteBuffer
name|largeWrapper
decl_stmt|;
specifier|public
name|SharedOutputBuffer
parameter_list|(
name|int
name|buffersize
parameter_list|,
specifier|final
name|ByteBufferAllocator
name|allocator
parameter_list|)
block|{
name|super
argument_list|(
name|buffersize
argument_list|,
name|allocator
argument_list|)
expr_stmt|;
name|this
operator|.
name|lock
operator|=
operator|new
name|ReentrantLock
argument_list|()
expr_stmt|;
name|this
operator|.
name|condition
operator|=
name|this
operator|.
name|lock
operator|.
name|newCondition
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|endOfStream
operator|=
literal|false
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasData
parameter_list|()
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|hasData
argument_list|()
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|available
parameter_list|()
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|available
argument_list|()
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|capacity
parameter_list|()
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|capacity
argument_list|()
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|length
parameter_list|()
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|super
operator|.
name|length
argument_list|()
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|produceContent
parameter_list|(
specifier|final
name|ContentEncoder
name|encoder
parameter_list|,
specifier|final
name|IOControl
name|ioc
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|this
operator|.
name|ioctrl
operator|=
name|ioc
expr_stmt|;
name|setOutputMode
argument_list|()
expr_stmt|;
name|int
name|bytesWritten
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|largeWrapper
operator|!=
literal|null
operator|||
name|super
operator|.
name|hasData
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|buffer
operator|.
name|hasRemaining
argument_list|()
operator|&&
name|largeWrapper
operator|!=
literal|null
condition|)
block|{
name|bytesWritten
operator|=
name|encoder
operator|.
name|write
argument_list|(
name|largeWrapper
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bytesWritten
operator|=
name|encoder
operator|.
name|write
argument_list|(
name|this
operator|.
name|buffer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encoder
operator|.
name|isCompleted
argument_list|()
condition|)
block|{
name|this
operator|.
name|endOfStream
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|(
name|largeWrapper
operator|==
literal|null
operator|||
operator|!
name|largeWrapper
operator|.
name|hasRemaining
argument_list|()
operator|)
operator|&&
operator|!
name|super
operator|.
name|hasData
argument_list|()
condition|)
block|{
comment|// No more buffered content
comment|// If at the end of the stream, terminate
if|if
condition|(
name|this
operator|.
name|endOfStream
operator|&&
operator|!
name|encoder
operator|.
name|isCompleted
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|complete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|this
operator|.
name|endOfStream
operator|&&
name|this
operator|.
name|ioctrl
operator|!=
literal|null
condition|)
block|{
comment|// suspend output events
name|this
operator|.
name|ioctrl
operator|.
name|suspendOutput
argument_list|()
expr_stmt|;
block|}
block|}
comment|// no need to signal if the large wrapper is present and has data remaining
if|if
condition|(
name|largeWrapper
operator|==
literal|null
operator|||
operator|!
name|largeWrapper
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|this
operator|.
name|condition
operator|.
name|signalAll
argument_list|()
expr_stmt|;
block|}
return|return
name|bytesWritten
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|shutdown
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|this
operator|.
name|condition
operator|.
name|signalAll
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|copy
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
operator|||
name|this
operator|.
name|endOfStream
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Buffer already closed for writing"
argument_list|)
throw|;
block|}
name|setInputMode
argument_list|()
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|boolean
name|yielded
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|flushContent
argument_list|()
expr_stmt|;
name|setInputMode
argument_list|()
expr_stmt|;
block|}
name|i
operator|=
name|in
operator|.
name|available
argument_list|()
expr_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
operator|!
name|yielded
condition|)
block|{
comment|//nothing avail right now, we'll attempt an
comment|//output, but not really force a flush.
if|if
condition|(
name|buffer
operator|.
name|position
argument_list|()
operator|!=
literal|0
operator|&&
name|this
operator|.
name|ioctrl
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|ioctrl
operator|.
name|requestOutput
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|condition
operator|.
name|awaitNanos
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
name|setInputMode
argument_list|()
expr_stmt|;
name|yielded
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|int
name|p
init|=
name|this
operator|.
name|buffer
operator|.
name|position
argument_list|()
decl_stmt|;
name|i
operator|=
name|in
operator|.
name|read
argument_list|(
name|this
operator|.
name|buffer
operator|.
name|array
argument_list|()
argument_list|,
name|this
operator|.
name|buffer
operator|.
name|position
argument_list|()
argument_list|,
name|this
operator|.
name|buffer
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
name|yielded
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
name|total
operator|+=
name|i
expr_stmt|;
name|buffer
operator|.
name|position
argument_list|(
name|p
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|/*                     System.out.println("p: " + p + "  " + i + " " + this.buffer.position()                                         + " " + this.buffer.hasRemaining());                                        */
block|}
block|}
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
operator|||
name|this
operator|.
name|endOfStream
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Buffer already closed for writing"
argument_list|)
throw|;
block|}
name|setInputMode
argument_list|()
expr_stmt|;
name|int
name|remaining
init|=
name|len
decl_stmt|;
while|while
condition|(
name|remaining
operator|>
literal|0
condition|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|flushContent
argument_list|()
expr_stmt|;
name|setInputMode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|buffer
operator|.
name|position
argument_list|()
operator|==
literal|0
operator|&&
operator|(
name|this
operator|.
name|buffer
operator|.
name|remaining
argument_list|()
operator|*
literal|2
operator|)
operator|<
name|remaining
condition|)
block|{
name|largeWrapper
operator|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
while|while
condition|(
name|largeWrapper
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|flushContent
argument_list|()
expr_stmt|;
block|}
name|largeWrapper
operator|=
literal|null
expr_stmt|;
name|remaining
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
name|int
name|chunk
init|=
name|Math
operator|.
name|min
argument_list|(
name|remaining
argument_list|,
name|this
operator|.
name|buffer
operator|.
name|remaining
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|buffer
operator|.
name|put
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
name|remaining
operator|-=
name|chunk
expr_stmt|;
name|off
operator|+=
name|chunk
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|write
parameter_list|(
name|ByteBuffer
name|b
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
operator|||
name|this
operator|.
name|endOfStream
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Buffer already closed for writing"
argument_list|)
throw|;
block|}
name|setInputMode
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|flushContent
argument_list|()
expr_stmt|;
name|setInputMode
argument_list|()
expr_stmt|;
block|}
name|int
name|c
init|=
name|b
operator|.
name|limit
argument_list|()
operator|-
name|b
operator|.
name|position
argument_list|()
decl_stmt|;
name|largeWrapper
operator|=
name|b
expr_stmt|;
while|while
condition|(
name|largeWrapper
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|flushContent
argument_list|()
expr_stmt|;
block|}
name|largeWrapper
operator|=
literal|null
expr_stmt|;
return|return
name|c
return|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|write
parameter_list|(
specifier|final
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|write
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
operator|||
name|this
operator|.
name|endOfStream
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Buffer already closed for writing"
argument_list|)
throw|;
block|}
name|setInputMode
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|this
operator|.
name|buffer
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|flushContent
argument_list|()
expr_stmt|;
name|setInputMode
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|buffer
operator|.
name|put
argument_list|(
operator|(
name|byte
operator|)
name|b
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{     }
specifier|private
name|void
name|flushContent
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
try|try
block|{
while|while
condition|(
operator|(
name|largeWrapper
operator|!=
literal|null
operator|&&
name|largeWrapper
operator|.
name|hasRemaining
argument_list|()
operator|)
operator|||
name|super
operator|.
name|hasData
argument_list|()
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|shutdown
condition|)
block|{
throw|throw
operator|new
name|InterruptedIOException
argument_list|(
literal|"Output operation aborted"
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|ioctrl
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|ioctrl
operator|.
name|requestOutput
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|condition
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Interrupted while flushing the content buffer"
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeCompleted
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|endOfStream
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|endOfStream
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|ioctrl
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|ioctrl
operator|.
name|requestOutput
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|this
operator|.
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

