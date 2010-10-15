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
name|attachment
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
name|PushbackInputStream
import|;
end_import

begin_class
specifier|public
class|class
name|MimeBodyPartInputStream
extends|extends
name|InputStream
block|{
name|PushbackInputStream
name|inStream
decl_stmt|;
name|boolean
name|boundaryFound
decl_stmt|;
name|int
name|pbAmount
decl_stmt|;
name|byte
index|[]
name|boundary
decl_stmt|;
name|byte
index|[]
name|boundaryBuffer
decl_stmt|;
specifier|public
name|MimeBodyPartInputStream
parameter_list|(
name|PushbackInputStream
name|inStreamParam
parameter_list|,
name|byte
index|[]
name|boundaryParam
parameter_list|,
name|int
name|pbsize
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|inStream
operator|=
name|inStreamParam
expr_stmt|;
name|this
operator|.
name|boundary
operator|=
name|boundaryParam
expr_stmt|;
name|this
operator|.
name|pbAmount
operator|=
name|pbsize
expr_stmt|;
block|}
specifier|public
name|int
name|read
parameter_list|(
name|byte
name|buf
index|[]
parameter_list|,
name|int
name|origOff
parameter_list|,
name|int
name|origLen
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
name|b
index|[]
init|=
name|buf
decl_stmt|;
name|int
name|off
init|=
name|origOff
decl_stmt|;
name|int
name|len
init|=
name|origLen
decl_stmt|;
if|if
condition|(
name|boundaryFound
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|if
condition|(
operator|(
name|off
operator|<
literal|0
operator|)
operator|||
operator|(
name|off
operator|>
name|b
operator|.
name|length
operator|)
operator|||
operator|(
name|len
operator|<
literal|0
operator|)
operator|||
operator|(
operator|(
name|off
operator|+
name|len
operator|)
operator|>
name|b
operator|.
name|length
operator|)
operator|||
operator|(
operator|(
name|off
operator|+
name|len
operator|)
operator|<
literal|0
operator|)
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|()
throw|;
block|}
if|if
condition|(
name|len
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
name|boolean
name|bufferCreated
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|len
operator|<
name|boundary
operator|.
name|length
operator|*
literal|2
condition|)
block|{
comment|//buffer is too short to detect boundaries with it.  We'll need to create a larger buffer
name|bufferCreated
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|boundaryBuffer
operator|==
literal|null
condition|)
block|{
name|boundaryBuffer
operator|=
operator|new
name|byte
index|[
name|boundary
operator|.
name|length
operator|*
literal|2
index|]
expr_stmt|;
block|}
name|b
operator|=
name|boundaryBuffer
expr_stmt|;
name|off
operator|=
literal|0
expr_stmt|;
name|len
operator|=
name|boundaryBuffer
operator|.
name|length
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
name|pbAmount
condition|)
block|{
name|len
operator|=
name|pbAmount
expr_stmt|;
comment|//can only pushback that much so make sure we can
block|}
name|int
name|read
init|=
literal|0
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|read
operator|>=
literal|0
operator|&&
name|idx
operator|<
name|len
operator|&&
name|idx
operator|<
operator|(
name|boundary
operator|.
name|length
operator|*
literal|2
operator|)
condition|)
block|{
comment|//make sure we read enough to detect the boundary
name|read
operator|=
name|inStream
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
operator|+
name|idx
argument_list|,
name|len
operator|-
name|idx
argument_list|)
expr_stmt|;
if|if
condition|(
name|read
operator|!=
operator|-
literal|1
condition|)
block|{
name|idx
operator|+=
name|read
expr_stmt|;
block|}
block|}
if|if
condition|(
name|read
operator|==
operator|-
literal|1
operator|&&
name|idx
operator|==
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|len
operator|=
name|idx
expr_stmt|;
name|int
name|i
init|=
name|processBuffer
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
if|if
condition|(
name|bufferCreated
operator|&&
name|i
operator|>
literal|0
condition|)
block|{
comment|// read more than we need, push it back
if|if
condition|(
name|origLen
operator|>=
name|i
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|buf
argument_list|,
name|origOff
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|buf
argument_list|,
name|origOff
argument_list|,
name|origLen
argument_list|)
expr_stmt|;
name|inStream
operator|.
name|unread
argument_list|(
name|b
argument_list|,
name|origLen
argument_list|,
name|i
operator|-
name|origLen
argument_list|)
expr_stmt|;
name|i
operator|=
name|origLen
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
name|boundaryFound
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|i
return|;
block|}
comment|//Has Data after encountering CRLF
specifier|private
name|boolean
name|hasData
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|initialPointer
parameter_list|,
name|int
name|pointer
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
name|pointer
operator|<
operator|(
name|off
operator|+
name|len
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|pointer
operator|>=
literal|1000000000
condition|)
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|b
argument_list|,
name|initialPointer
argument_list|,
operator|(
name|off
operator|+
name|len
operator|)
operator|-
name|initialPointer
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|int
name|x
init|=
name|inStream
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|x
operator|!=
operator|-
literal|1
condition|)
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|inStream
operator|.
name|unread
argument_list|(
name|b
argument_list|,
name|initialPointer
argument_list|,
operator|(
name|off
operator|+
name|len
operator|)
operator|-
name|initialPointer
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
specifier|protected
name|int
name|processBuffer
parameter_list|(
name|byte
index|[]
name|buffer
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
for|for
control|(
name|int
name|i
init|=
name|off
init|;
name|i
operator|<
operator|(
name|off
operator|+
name|len
operator|)
condition|;
name|i
operator|++
control|)
block|{
name|boolean
name|needUnread0d0a
init|=
literal|false
decl_stmt|;
name|int
name|value
init|=
name|buffer
index|[
name|i
index|]
decl_stmt|;
name|int
name|initialI
init|=
name|i
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|13
condition|)
block|{
if|if
condition|(
operator|!
name|hasData
argument_list|(
name|buffer
argument_list|,
name|initialI
argument_list|,
name|initialI
operator|+
literal|1
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
condition|)
block|{
return|return
name|initialI
operator|-
name|off
return|;
block|}
name|value
operator|=
name|buffer
index|[
name|initialI
operator|+
literal|1
index|]
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|10
condition|)
block|{
continue|continue;
block|}
else|else
block|{
comment|//if it comes here then 13, 10 are values and will try to match boundaries
if|if
condition|(
operator|!
name|hasData
argument_list|(
name|buffer
argument_list|,
name|initialI
argument_list|,
name|initialI
operator|+
literal|2
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
condition|)
block|{
return|return
name|initialI
operator|-
name|off
return|;
block|}
name|value
operator|=
name|buffer
index|[
name|initialI
operator|+
literal|2
index|]
expr_stmt|;
if|if
condition|(
operator|(
name|byte
operator|)
name|value
operator|!=
name|boundary
index|[
literal|0
index|]
condition|)
block|{
name|i
operator|++
expr_stmt|;
continue|continue;
block|}
else|else
block|{
comment|//13, 10, boundaries first value matched
name|needUnread0d0a
operator|=
literal|true
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
comment|//i after this points to boundary[0] element
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|value
operator|!=
name|boundary
index|[
literal|0
index|]
condition|)
block|{
continue|continue;
block|}
name|int
name|boundaryIndex
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|boundaryIndex
operator|<
name|boundary
operator|.
name|length
operator|)
operator|&&
operator|(
name|value
operator|==
name|boundary
index|[
name|boundaryIndex
index|]
operator|)
condition|)
block|{
if|if
condition|(
operator|!
name|hasData
argument_list|(
name|buffer
argument_list|,
name|initialI
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
condition|)
block|{
return|return
name|initialI
operator|-
name|off
return|;
block|}
name|value
operator|=
name|buffer
index|[
operator|++
name|i
index|]
expr_stmt|;
name|boundaryIndex
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|boundaryIndex
operator|==
name|boundary
operator|.
name|length
condition|)
block|{
comment|// read the end of line character
if|if
condition|(
name|initialI
operator|!=
name|off
condition|)
block|{
name|i
operator|=
literal|1000000000
expr_stmt|;
block|}
if|if
condition|(
name|initialI
operator|-
name|off
operator|!=
literal|0
operator|&&
operator|!
name|hasData
argument_list|(
name|buffer
argument_list|,
name|initialI
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
condition|)
block|{
return|return
name|initialI
operator|-
name|off
return|;
block|}
name|boundaryFound
operator|=
literal|true
expr_stmt|;
name|int
name|j
init|=
name|i
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|j
operator|<
name|len
operator|&&
name|buffer
index|[
name|j
index|]
operator|==
literal|45
operator|&&
name|value
operator|==
literal|45
condition|)
block|{
comment|// Last mime boundary should have a succeeding "--"
comment|// as we are on it, read the terminating CRLF
name|i
operator|+=
literal|2
expr_stmt|;
comment|//last mime boundary
block|}
comment|//boundary matched (may or may not be last mime boundary)
name|int
name|processed
init|=
name|initialI
operator|-
name|off
decl_stmt|;
if|if
condition|(
operator|(
name|len
operator|-
operator|(
name|i
operator|+
literal|2
operator|)
operator|)
operator|>
literal|0
condition|)
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|buffer
argument_list|,
name|i
operator|+
literal|2
argument_list|,
name|len
operator|-
operator|(
name|i
operator|+
literal|2
operator|)
argument_list|)
expr_stmt|;
block|}
return|return
name|processed
return|;
block|}
comment|// Boundary not found. Restoring bytes skipped.
comment|// write first skipped byte, push back the rest
if|if
condition|(
name|value
operator|!=
operator|-
literal|1
condition|)
block|{
comment|//pushing back first byte of boundary
comment|// Stream might have ended
name|i
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|needUnread0d0a
condition|)
block|{
comment|//Pushing all,  returning 13
name|i
operator|=
name|i
operator|-
name|boundaryIndex
expr_stmt|;
name|i
operator|--
expr_stmt|;
comment|//for 10
name|value
operator|=
literal|13
expr_stmt|;
block|}
else|else
block|{
name|i
operator|=
name|i
operator|-
name|boundaryIndex
expr_stmt|;
name|i
operator|++
expr_stmt|;
name|value
operator|=
name|boundary
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
return|return
name|len
return|;
block|}
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|boolean
name|needUnread0d0a
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|boundaryFound
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
comment|// read the next value from stream
name|int
name|value
init|=
name|inStream
operator|.
name|read
argument_list|()
decl_stmt|;
comment|// A problem occurred because all the mime parts tends to have a /r/n
comment|// at the end. Making it hard to transform them to correct
comment|// DataSources.
comment|// This logic introduced to handle it
if|if
condition|(
name|value
operator|==
literal|13
condition|)
block|{
name|value
operator|=
name|inStream
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|10
condition|)
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
literal|13
return|;
block|}
else|else
block|{
name|value
operator|=
name|inStream
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|byte
operator|)
name|value
operator|!=
name|boundary
index|[
literal|0
index|]
condition|)
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|inStream
operator|.
name|unread
argument_list|(
literal|10
argument_list|)
expr_stmt|;
return|return
literal|13
return|;
block|}
else|else
block|{
name|needUnread0d0a
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
operator|(
name|byte
operator|)
name|value
operator|!=
name|boundary
index|[
literal|0
index|]
condition|)
block|{
return|return
name|value
return|;
block|}
comment|// read value is the first byte of the boundary. Start matching the
comment|// next characters to find a boundary
name|int
name|boundaryIndex
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|boundaryIndex
operator|<
name|boundary
operator|.
name|length
operator|)
operator|&&
operator|(
operator|(
name|byte
operator|)
name|value
operator|==
name|boundary
index|[
name|boundaryIndex
index|]
operator|)
condition|)
block|{
name|value
operator|=
name|inStream
operator|.
name|read
argument_list|()
expr_stmt|;
name|boundaryIndex
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|boundaryIndex
operator|==
name|boundary
operator|.
name|length
condition|)
block|{
comment|// boundary found
name|boundaryFound
operator|=
literal|true
expr_stmt|;
name|int
name|dashNext
init|=
name|inStream
operator|.
name|read
argument_list|()
decl_stmt|;
comment|// read the end of line character
if|if
condition|(
name|dashNext
operator|==
literal|45
operator|&&
name|value
operator|==
literal|45
condition|)
block|{
comment|// Last mime boundary should have a succeeding "--"
comment|// as we are on it, read the terminating CRLF
name|inStream
operator|.
name|read
argument_list|()
expr_stmt|;
name|inStream
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|// Boundary not found. Restoring bytes skipped.
comment|// write first skipped byte, push back the rest
if|if
condition|(
name|value
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// Stream might have ended
name|inStream
operator|.
name|unread
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|needUnread0d0a
condition|)
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|boundary
argument_list|,
literal|0
argument_list|,
name|boundaryIndex
argument_list|)
expr_stmt|;
name|inStream
operator|.
name|unread
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|value
operator|=
literal|13
expr_stmt|;
block|}
else|else
block|{
name|inStream
operator|.
name|unread
argument_list|(
name|boundary
argument_list|,
literal|1
argument_list|,
name|boundaryIndex
operator|-
literal|1
argument_list|)
expr_stmt|;
name|value
operator|=
name|boundary
index|[
literal|0
index|]
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

