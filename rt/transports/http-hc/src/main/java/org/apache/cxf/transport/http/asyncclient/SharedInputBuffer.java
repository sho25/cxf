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
name|nio
operator|.
name|ContentDecoder
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
comment|/**  * Content buffer that can be shared by multiple threads, usually the I/O dispatch of  * an I/O reactor and a worker thread.  *<p/>  * The I/O dispatch thread is expect to transfer data from {@link ContentDecoder} to the buffer  *   by calling {@link #consumeContent(ContentDecoder)}.  *<p/>  * The worker thread is expected to read the data from the buffer by calling  *   {@link #read()} or {@link #read(byte[], int, int)} methods.  *<p/>  * In case of an abnormal situation or when no longer needed the buffer must be shut down  * using {@link #shutdown()} method.  */
end_comment

begin_class
specifier|public
class|class
name|SharedInputBuffer
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
specifier|final
name|int
name|requestInputSize
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
name|waitingBuffer
decl_stmt|;
comment|//private volatile int waitCnt;
comment|//private volatile int nowaitCnt;
specifier|public
name|SharedInputBuffer
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
comment|//if the buffer become 3/4 empty, we'll turn on the input
comment|//events again to hopefully get more data before the next
comment|//the buffer fully empties and we have to wait to read
name|this
operator|.
name|requestInputSize
operator|=
name|buffersize
operator|*
literal|3
operator|/
literal|4
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
specifier|public
name|int
name|consumeContent
parameter_list|(
specifier|final
name|ContentDecoder
name|decoder
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
comment|//something bad happened, we need to shutdown the connection
comment|//as we're not going to read the data at all and we
comment|//don't want to keep getting read notices and such
name|ioc
operator|.
name|shutdown
argument_list|()
expr_stmt|;
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
name|setInputMode
argument_list|()
expr_stmt|;
name|int
name|totalRead
init|=
literal|0
decl_stmt|;
name|int
name|bytesRead
decl_stmt|;
if|if
condition|(
name|waitingBuffer
operator|!=
literal|null
operator|&&
name|this
operator|.
name|buffer
operator|.
name|position
argument_list|()
operator|==
literal|0
condition|)
block|{
while|while
condition|(
operator|(
name|bytesRead
operator|=
name|decoder
operator|.
name|read
argument_list|(
name|this
operator|.
name|waitingBuffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|totalRead
operator|+=
name|bytesRead
expr_stmt|;
block|}
block|}
comment|//read more
while|while
condition|(
operator|(
name|bytesRead
operator|=
name|decoder
operator|.
name|read
argument_list|(
name|this
operator|.
name|buffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|totalRead
operator|+=
name|bytesRead
expr_stmt|;
block|}
if|if
condition|(
name|bytesRead
operator|==
operator|-
literal|1
operator|||
name|decoder
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
name|this
operator|.
name|ioctrl
operator|!=
literal|null
operator|&&
operator|!
name|this
operator|.
name|endOfStream
condition|)
block|{
name|this
operator|.
name|ioctrl
operator|.
name|suspendInput
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|condition
operator|.
name|signalAll
argument_list|()
expr_stmt|;
if|if
condition|(
name|totalRead
operator|>
literal|0
condition|)
block|{
return|return
name|totalRead
return|;
block|}
if|if
condition|(
name|this
operator|.
name|endOfStream
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
literal|0
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
specifier|protected
name|void
name|waitForData
parameter_list|(
name|int
name|waitPos
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
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|waitingBuffer
operator|!=
literal|null
operator|&&
name|this
operator|.
name|waitingBuffer
operator|.
name|position
argument_list|()
operator|>
name|waitPos
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|super
operator|.
name|hasData
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|this
operator|.
name|endOfStream
condition|)
block|{
return|return;
block|}
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
literal|"Input operation aborted"
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
name|requestInput
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
literal|"Interrupted while waiting for more data"
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
name|close
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
name|endOfStream
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
specifier|protected
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|this
operator|.
name|shutdown
return|;
block|}
specifier|protected
name|boolean
name|isEndOfStream
parameter_list|()
block|{
return|return
name|this
operator|.
name|shutdown
operator|||
operator|(
operator|!
name|hasData
argument_list|()
operator|&&
name|this
operator|.
name|endOfStream
operator|)
return|;
block|}
specifier|public
name|int
name|read
parameter_list|()
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
if|if
condition|(
operator|!
name|super
operator|.
name|hasData
argument_list|()
condition|)
block|{
name|waitForData
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isEndOfStream
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|setOutputMode
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|buffer
operator|.
name|get
argument_list|()
operator|&
literal|0xff
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
name|read
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
operator|!
name|hasData
argument_list|()
condition|)
block|{
name|this
operator|.
name|waitingBuffer
operator|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|waitForData
argument_list|(
name|off
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|waitingBuffer
operator|.
name|position
argument_list|()
operator|-
name|off
decl_stmt|;
name|waitingBuffer
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
comment|//++waitCnt;
return|return
name|i
return|;
block|}
block|}
if|if
condition|(
name|isEndOfStream
argument_list|()
condition|)
block|{
comment|//System.out.println(waitCnt + " " + nowaitCnt);
return|return
operator|-
literal|1
return|;
block|}
name|setOutputMode
argument_list|()
expr_stmt|;
name|int
name|chunk
init|=
name|len
decl_stmt|;
if|if
condition|(
name|chunk
operator|>
name|this
operator|.
name|buffer
operator|.
name|remaining
argument_list|()
condition|)
block|{
name|chunk
operator|=
name|this
operator|.
name|buffer
operator|.
name|remaining
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|buffer
operator|.
name|get
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|chunk
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|buffer
operator|.
name|position
argument_list|()
operator|>=
name|this
operator|.
name|requestInputSize
operator|&&
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
comment|//we have a significant amount of space empty in the buffer, we'll turn on
comment|//the input so maybe we'll get another chunk by the time the next read happens
comment|//and we can then avoid waiting for input
name|this
operator|.
name|ioctrl
operator|.
name|requestInput
argument_list|()
expr_stmt|;
block|}
comment|//++nowaitCnt;
return|return
name|chunk
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
name|read
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
return|return
name|read
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|b
operator|.
name|length
argument_list|)
return|;
block|}
block|}
end_class

end_unit

