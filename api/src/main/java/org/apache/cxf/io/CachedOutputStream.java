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
name|io
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|io
operator|.
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|FileUtils
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
name|helpers
operator|.
name|LoadingByteArrayOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|CachedOutputStream
extends|extends
name|OutputStream
block|{
specifier|private
specifier|static
specifier|final
name|File
name|DEFAULT_TEMP_DIR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_THRESHOLD
decl_stmt|;
static|static
block|{
name|String
name|s
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.io.CachedOutputStream.Threshold"
argument_list|,
literal|"-1"
argument_list|)
decl_stmt|;
name|int
name|i
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|<=
literal|0
condition|)
block|{
name|i
operator|=
literal|64
operator|*
literal|1024
expr_stmt|;
block|}
name|DEFAULT_THRESHOLD
operator|=
name|i
expr_stmt|;
name|s
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.io.CachedOutputStream.OutputDirectory"
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
operator|&&
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|DEFAULT_TEMP_DIR
operator|=
name|f
expr_stmt|;
block|}
else|else
block|{
name|DEFAULT_TEMP_DIR
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
name|DEFAULT_TEMP_DIR
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|outputLocked
decl_stmt|;
specifier|protected
name|OutputStream
name|currentStream
decl_stmt|;
specifier|private
name|long
name|threshold
init|=
name|DEFAULT_THRESHOLD
decl_stmt|;
specifier|private
name|int
name|totalLength
decl_stmt|;
specifier|private
name|boolean
name|inmem
decl_stmt|;
specifier|private
name|boolean
name|tempFileFailed
decl_stmt|;
specifier|private
name|File
name|tempFile
decl_stmt|;
specifier|private
name|File
name|outputDir
init|=
name|DEFAULT_TEMP_DIR
decl_stmt|;
specifier|private
name|boolean
name|allowDeleteOfFile
init|=
literal|true
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CachedOutputStreamCallback
argument_list|>
name|callbacks
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|streamList
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|CachedOutputStream
parameter_list|(
name|PipedInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|currentStream
operator|=
operator|new
name|PipedOutputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|CachedOutputStream
parameter_list|()
block|{
name|currentStream
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|(
literal|2048
argument_list|)
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|CachedOutputStream
parameter_list|(
name|long
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
name|currentStream
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|(
literal|2048
argument_list|)
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|holdTempFile
parameter_list|()
block|{
name|allowDeleteOfFile
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|releaseTempFileHold
parameter_list|()
block|{
name|allowDeleteOfFile
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|registerCallback
parameter_list|(
name|CachedOutputStreamCallback
name|cb
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|callbacks
condition|)
block|{
name|callbacks
operator|=
operator|new
name|ArrayList
argument_list|<
name|CachedOutputStreamCallback
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|callbacks
operator|.
name|add
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|deregisterCallback
parameter_list|(
name|CachedOutputStreamCallback
name|cb
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|callbacks
condition|)
block|{
name|callbacks
operator|.
name|remove
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|CachedOutputStreamCallback
argument_list|>
name|getCallbacks
parameter_list|()
block|{
return|return
name|callbacks
operator|==
literal|null
condition|?
literal|null
else|:
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|callbacks
argument_list|)
return|;
block|}
comment|/**      * Perform any actions required on stream flush (freeze headers, reset      * output stream ... etc.)      */
specifier|protected
name|void
name|doFlush
parameter_list|()
throws|throws
name|IOException
block|{              }
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|callbacks
condition|)
block|{
for|for
control|(
name|CachedOutputStreamCallback
name|cb
range|:
name|callbacks
control|)
block|{
name|cb
operator|.
name|onFlush
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|doFlush
argument_list|()
expr_stmt|;
block|}
comment|/**      * Perform any actions required on stream closure (handle response etc.)      */
specifier|protected
name|void
name|doClose
parameter_list|()
throws|throws
name|IOException
block|{              }
comment|/**      * Perform any actions required after stream closure (close the other related stream etc.)      */
specifier|protected
name|void
name|postClose
parameter_list|()
throws|throws
name|IOException
block|{              }
comment|/**      * Locks the output stream to prevent additional writes, but maintains      * a pointer to it so an InputStream can be obtained      * @throws IOException      */
specifier|public
name|void
name|lockOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|outputLocked
condition|)
block|{
return|return;
block|}
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|outputLocked
operator|=
literal|true
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|callbacks
condition|)
block|{
for|for
control|(
name|CachedOutputStreamCallback
name|cb
range|:
name|callbacks
control|)
block|{
name|cb
operator|.
name|onClose
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|doClose
argument_list|()
expr_stmt|;
name|streamList
operator|.
name|remove
argument_list|(
name|currentStream
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|outputLocked
operator|=
literal|true
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|callbacks
condition|)
block|{
for|for
control|(
name|CachedOutputStreamCallback
name|cb
range|:
name|callbacks
control|)
block|{
name|cb
operator|.
name|onClose
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|doClose
argument_list|()
expr_stmt|;
name|currentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|maybeDeleteTempFile
argument_list|(
name|currentStream
argument_list|)
expr_stmt|;
name|postClose
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|currentStream
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
comment|/**      * Replace the original stream with the new one, optionally copying the content of the old one      * into the new one.      * When with Attachment, needs to replace the xml writer stream with the stream used by      * AttachmentSerializer or copy the cached output stream to the "real"      * output stream, i.e. onto the wire.      *       * @param out the new output stream      * @param copyOldContent flag indicating if the old content should be copied      * @throws IOException      */
specifier|public
name|void
name|resetOut
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|boolean
name|copyOldContent
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|out
operator|==
literal|null
condition|)
block|{
name|out
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|currentStream
operator|instanceof
name|CachedOutputStream
condition|)
block|{
name|CachedOutputStream
name|ac
init|=
operator|(
name|CachedOutputStream
operator|)
name|currentStream
decl_stmt|;
name|InputStream
name|in
init|=
name|ac
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|inmem
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|ByteArrayOutputStream
name|byteOut
init|=
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
decl_stmt|;
if|if
condition|(
name|copyOldContent
operator|&&
name|byteOut
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|byteOut
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|currentStream
operator|instanceof
name|PipedOutputStream
condition|)
block|{
name|PipedOutputStream
name|pipeOut
init|=
operator|(
name|PipedOutputStream
operator|)
name|currentStream
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
operator|new
name|PipedInputStream
argument_list|(
name|pipeOut
argument_list|)
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown format of currentStream"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// read the file
name|currentStream
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|copyOldContent
condition|)
block|{
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|fin
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|streamList
operator|.
name|remove
argument_list|(
name|currentStream
argument_list|)
expr_stmt|;
name|tempFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tempFile
operator|=
literal|null
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|currentStream
operator|=
name|out
expr_stmt|;
name|outputLocked
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|copyStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|totalLength
return|;
block|}
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|inmem
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
return|return
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown format of currentStream"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// read the file
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
decl_stmt|;
return|return
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|fin
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|writeCacheTo
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|inmem
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown format of currentStream"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// read the file
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|fin
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeCacheTo
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|totalLength
operator|<
name|limit
operator|||
name|limit
operator|==
operator|-
literal|1
condition|)
block|{
name|writeCacheTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|count
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|inmem
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|byte
name|bytes
index|[]
init|=
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|limit
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown format of currentStream"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// read the file
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
decl_stmt|;
name|byte
name|bytes
index|[]
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|x
init|=
name|fin
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
while|while
condition|(
name|x
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
operator|(
name|count
operator|+
name|x
operator|)
operator|>
name|limit
condition|)
block|{
name|x
operator|=
name|limit
operator|-
name|count
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|x
argument_list|)
argument_list|)
expr_stmt|;
name|count
operator|+=
name|x
expr_stmt|;
if|if
condition|(
name|count
operator|>=
name|limit
condition|)
block|{
name|x
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
name|x
operator|=
name|fin
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
block|}
name|fin
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeCacheTo
parameter_list|(
name|StringBuilder
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|inmem
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown format of currentStream"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// read the file
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
decl_stmt|;
name|byte
name|bytes
index|[]
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|x
init|=
name|fin
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
while|while
condition|(
name|x
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|x
argument_list|)
argument_list|)
expr_stmt|;
name|x
operator|=
name|fin
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
name|fin
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @return the underlying output stream      */
specifier|public
name|OutputStream
name|getOut
parameter_list|()
block|{
return|return
name|currentStream
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|currentStream
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|CachedOutputStream
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" Content: "
argument_list|)
decl_stmt|;
try|try
block|{
name|writeCacheTo
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|builder
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|protected
name|void
name|onWrite
parameter_list|()
throws|throws
name|IOException
block|{      }
specifier|public
name|void
name|write
parameter_list|(
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
operator|!
name|outputLocked
condition|)
block|{
name|onWrite
argument_list|()
expr_stmt|;
name|this
operator|.
name|totalLength
operator|+=
name|len
expr_stmt|;
if|if
condition|(
name|inmem
operator|&&
name|totalLength
operator|>
name|threshold
operator|&&
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|createFileOutputStream
argument_list|()
expr_stmt|;
block|}
name|currentStream
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|outputLocked
condition|)
block|{
name|onWrite
argument_list|()
expr_stmt|;
name|this
operator|.
name|totalLength
operator|+=
name|b
operator|.
name|length
expr_stmt|;
if|if
condition|(
name|inmem
operator|&&
name|totalLength
operator|>
name|threshold
operator|&&
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|createFileOutputStream
argument_list|()
expr_stmt|;
block|}
name|currentStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|outputLocked
condition|)
block|{
name|onWrite
argument_list|()
expr_stmt|;
name|this
operator|.
name|totalLength
operator|++
expr_stmt|;
if|if
condition|(
name|inmem
operator|&&
name|totalLength
operator|>
name|threshold
operator|&&
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
name|createFileOutputStream
argument_list|()
expr_stmt|;
block|}
name|currentStream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createFileOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|tempFileFailed
condition|)
block|{
return|return;
block|}
name|ByteArrayOutputStream
name|bout
init|=
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
decl_stmt|;
try|try
block|{
if|if
condition|(
name|outputDir
operator|==
literal|null
condition|)
block|{
name|tempFile
operator|=
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"cos"
argument_list|,
literal|"tmp"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tempFile
operator|=
name|FileUtils
operator|.
name|createTempFile
argument_list|(
literal|"cos"
argument_list|,
literal|"tmp"
argument_list|,
name|outputDir
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|currentStream
operator|=
operator|new
name|BufferedOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|tempFile
argument_list|)
argument_list|)
expr_stmt|;
name|bout
operator|.
name|writeTo
argument_list|(
name|currentStream
argument_list|)
expr_stmt|;
name|inmem
operator|=
literal|false
expr_stmt|;
name|streamList
operator|.
name|add
argument_list|(
name|currentStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//Could be IOException or SecurityException or other issues.
comment|//Don't care what, just keep it in memory.
name|tempFileFailed
operator|=
literal|true
expr_stmt|;
name|tempFile
operator|=
literal|null
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
name|currentStream
operator|=
name|bout
expr_stmt|;
block|}
block|}
specifier|public
name|File
name|getTempFile
parameter_list|()
block|{
return|return
name|tempFile
operator|!=
literal|null
operator|&&
name|tempFile
operator|.
name|exists
argument_list|()
condition|?
name|tempFile
else|:
literal|null
return|;
block|}
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|inmem
condition|)
block|{
if|if
condition|(
name|currentStream
operator|instanceof
name|LoadingByteArrayOutputStream
condition|)
block|{
return|return
operator|(
operator|(
name|LoadingByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|createInputStream
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|currentStream
operator|instanceof
name|ByteArrayOutputStream
condition|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|currentStream
operator|)
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|currentStream
operator|instanceof
name|PipedOutputStream
condition|)
block|{
return|return
operator|new
name|PipedInputStream
argument_list|(
operator|(
name|PipedOutputStream
operator|)
name|currentStream
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
try|try
block|{
name|FileInputStream
name|fileInputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
block|{
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|maybeDeleteTempFile
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|streamList
operator|.
name|add
argument_list|(
name|fileInputStream
argument_list|)
expr_stmt|;
return|return
name|fileInputStream
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cached file was deleted, "
operator|+
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|maybeDeleteTempFile
parameter_list|(
name|Object
name|stream
parameter_list|)
block|{
name|streamList
operator|.
name|remove
argument_list|(
name|stream
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|inmem
operator|&&
name|tempFile
operator|!=
literal|null
operator|&&
name|streamList
operator|.
name|isEmpty
argument_list|()
operator|&&
name|allowDeleteOfFile
condition|)
block|{
if|if
condition|(
name|currentStream
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|currentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|postClose
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
name|tempFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|tempFile
operator|=
literal|null
expr_stmt|;
name|currentStream
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setOutputDir
parameter_list|(
name|File
name|outputDir
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|outputDir
operator|=
name|outputDir
expr_stmt|;
block|}
specifier|public
name|void
name|setThreshold
parameter_list|(
name|long
name|threshold
parameter_list|)
block|{
name|this
operator|.
name|threshold
operator|=
name|threshold
expr_stmt|;
block|}
block|}
end_class

end_unit

