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
name|CharArrayReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|CharArrayWriter
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
name|InputStreamReader
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|javax
operator|.
name|crypto
operator|.
name|CipherInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|CipherOutputStream
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
name|Bus
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
name|BusFactory
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
name|common
operator|.
name|util
operator|.
name|SystemPropertyAction
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
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_class
specifier|public
class|class
name|CachedWriter
extends|extends
name|Writer
block|{
specifier|private
specifier|static
specifier|final
name|File
name|DEFAULT_TEMP_DIR
decl_stmt|;
specifier|private
specifier|static
name|int
name|defaultThreshold
decl_stmt|;
specifier|private
specifier|static
name|long
name|defaultMaxSize
decl_stmt|;
specifier|private
specifier|static
name|String
name|defaultCipherTransformation
decl_stmt|;
static|static
block|{
name|String
name|s
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|CachedConstants
operator|.
name|OUTPUT_DIRECTORY_SYS_PROP
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
comment|// lookup the deprecated property
name|s
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"org.apache.cxf.io.CachedWriter.OutputDirectory"
argument_list|)
expr_stmt|;
block|}
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
name|setDefaultThreshold
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|setDefaultMaxSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|setDefaultCipherTransformation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|outputLocked
decl_stmt|;
specifier|protected
name|Writer
name|currentStream
decl_stmt|;
specifier|private
name|boolean
name|cosClosed
decl_stmt|;
specifier|private
name|long
name|threshold
init|=
name|defaultThreshold
decl_stmt|;
specifier|private
name|long
name|maxSize
init|=
name|defaultMaxSize
decl_stmt|;
specifier|private
name|File
name|outputDir
init|=
name|DEFAULT_TEMP_DIR
decl_stmt|;
specifier|private
name|String
name|cipherTransformation
init|=
name|defaultCipherTransformation
decl_stmt|;
specifier|private
name|long
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
name|boolean
name|allowDeleteOfFile
init|=
literal|true
decl_stmt|;
specifier|private
name|CipherPair
name|ciphers
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CachedWriterCallback
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|static
class|class
name|LoadingCharArrayWriter
extends|extends
name|CharArrayWriter
block|{
name|LoadingCharArrayWriter
parameter_list|()
block|{
name|super
argument_list|(
literal|1024
argument_list|)
expr_stmt|;
block|}
specifier|public
name|char
index|[]
name|rawCharArray
parameter_list|()
block|{
return|return
name|super
operator|.
name|buf
return|;
block|}
block|}
specifier|public
name|CachedWriter
parameter_list|()
block|{
name|this
argument_list|(
name|defaultThreshold
argument_list|)
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|CachedWriter
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
name|LoadingCharArrayWriter
argument_list|()
expr_stmt|;
name|inmem
operator|=
literal|true
expr_stmt|;
name|readBusProperties
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|readBusProperties
parameter_list|()
block|{
name|Bus
name|b
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|String
name|v
init|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
name|CachedConstants
operator|.
name|THRESHOLD_BUS_PROP
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
operator|&&
name|threshold
operator|==
name|defaultThreshold
condition|)
block|{
name|threshold
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
name|v
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
name|CachedConstants
operator|.
name|MAX_SIZE_BUS_PROP
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|maxSize
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
name|v
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
name|CachedConstants
operator|.
name|CIPHER_TRANSFORMATION_BUS_PROP
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|cipherTransformation
operator|=
name|v
expr_stmt|;
block|}
name|v
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
name|CachedConstants
operator|.
name|OUTPUT_DIRECTORY_BUS_PROP
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
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
name|v
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
name|outputDir
operator|=
name|f
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|String
name|getBusProperty
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|dflt
parameter_list|)
block|{
name|String
name|v
init|=
operator|(
name|String
operator|)
name|b
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|v
operator|!=
literal|null
condition|?
name|v
else|:
name|dflt
return|;
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
name|CachedWriterCallback
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
argument_list|<>
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
name|CachedWriterCallback
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
name|CachedWriterCallback
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
block|{      }
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|cosClosed
condition|)
block|{
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|callbacks
condition|)
block|{
for|for
control|(
name|CachedWriterCallback
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
block|{      }
comment|/**      * Perform any actions required after stream closure (close the other related stream etc.)      */
specifier|protected
name|void
name|postClose
parameter_list|()
throws|throws
name|IOException
block|{      }
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
name|CachedWriterCallback
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
if|if
condition|(
operator|!
name|cosClosed
condition|)
block|{
name|currentStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
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
name|CachedWriterCallback
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
if|if
condition|(
name|ciphers
operator|!=
literal|null
condition|)
block|{
name|ciphers
operator|.
name|clean
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|CachedWriter
condition|)
block|{
return|return
name|currentStream
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|CachedWriter
operator|)
name|obj
operator|)
operator|.
name|currentStream
argument_list|)
return|;
block|}
return|return
name|currentStream
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
comment|/**      * Replace the original stream with the new one, optionally copying the content of the old one      * into the new one.      * When with Attachment, needs to replace the xml writer stream with the stream used by      * AttachmentSerializer or copy the cached output stream to the "real"      * output stream, i.e. onto the wire.      *      * @param out the new output stream      * @param copyOldContent flag indicating if the old content should be copied      * @throws IOException      */
specifier|public
name|void
name|resetOut
parameter_list|(
name|Writer
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
name|LoadingCharArrayWriter
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|currentStream
operator|instanceof
name|CachedWriter
condition|)
block|{
name|CachedWriter
name|ac
init|=
operator|(
name|CachedWriter
operator|)
name|currentStream
decl_stmt|;
name|Reader
name|in
init|=
name|ac
operator|.
name|getReader
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
name|LoadingCharArrayWriter
condition|)
block|{
name|LoadingCharArrayWriter
name|byteOut
init|=
operator|(
name|LoadingCharArrayWriter
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
name|InputStreamReader
name|fin
init|=
name|createInputStreamReader
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
name|deleteTempFile
argument_list|()
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
name|long
name|size
parameter_list|()
block|{
return|return
name|totalLength
return|;
block|}
specifier|public
name|char
index|[]
name|getChars
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
name|LoadingCharArrayWriter
condition|)
block|{
return|return
operator|(
operator|(
name|LoadingCharArrayWriter
operator|)
name|currentStream
operator|)
operator|.
name|toCharArray
argument_list|()
return|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown format of currentStream"
argument_list|)
throw|;
block|}
comment|// read the file
try|try
init|(
name|Reader
name|fin
init|=
name|createInputStreamReader
argument_list|(
name|tempFile
argument_list|)
init|)
block|{
name|CharArrayWriter
name|out
init|=
operator|new
name|CharArrayWriter
argument_list|(
operator|(
name|int
operator|)
name|tempFile
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|char
index|[]
name|bytes
init|=
operator|new
name|char
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
name|write
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|x
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
return|return
name|out
operator|.
name|toCharArray
argument_list|()
return|;
block|}
block|}
specifier|public
name|void
name|writeCacheTo
parameter_list|(
name|Writer
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
name|LoadingCharArrayWriter
condition|)
block|{
operator|(
operator|(
name|LoadingCharArrayWriter
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
try|try
init|(
name|Reader
name|fin
init|=
name|createInputStreamReader
argument_list|(
name|tempFile
argument_list|)
init|)
block|{
name|char
index|[]
name|bytes
init|=
operator|new
name|char
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
name|write
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|x
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
block|}
block|}
block|}
specifier|public
name|void
name|writeCacheTo
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|long
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
name|long
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
name|LoadingCharArrayWriter
condition|)
block|{
name|LoadingCharArrayWriter
name|s
init|=
operator|(
name|LoadingCharArrayWriter
operator|)
name|currentStream
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|s
operator|.
name|rawCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
operator|(
name|int
operator|)
name|limit
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
try|try
init|(
name|Reader
name|fin
init|=
name|createInputStreamReader
argument_list|(
name|tempFile
argument_list|)
init|)
block|{
name|char
index|[]
name|bytes
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
name|long
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
name|bytes
argument_list|,
literal|0
argument_list|,
operator|(
name|int
operator|)
name|x
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
block|}
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
name|LoadingCharArrayWriter
condition|)
block|{
name|LoadingCharArrayWriter
name|lcaw
init|=
operator|(
name|LoadingCharArrayWriter
operator|)
name|currentStream
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|lcaw
operator|.
name|rawCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|lcaw
operator|.
name|size
argument_list|()
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
try|try
init|(
name|Reader
name|r
init|=
name|createInputStreamReader
argument_list|(
name|tempFile
argument_list|)
init|)
block|{
name|char
index|[]
name|chars
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|x
init|=
name|r
operator|.
name|read
argument_list|(
name|chars
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
name|chars
argument_list|,
literal|0
argument_list|,
name|x
argument_list|)
expr_stmt|;
name|x
operator|=
name|r
operator|.
name|read
argument_list|(
name|chars
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * @return the underlying output stream      */
specifier|public
name|Writer
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
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|CachedWriter
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
literal|']'
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
specifier|private
name|void
name|enforceLimits
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|maxSize
operator|>
literal|0
operator|&&
name|totalLength
operator|>
name|maxSize
condition|)
block|{
throw|throw
operator|new
name|CacheSizeExceededException
argument_list|()
throw|;
block|}
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
name|LoadingCharArrayWriter
condition|)
block|{
name|createFileOutputStream
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|write
parameter_list|(
name|char
index|[]
name|cbuf
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
name|enforceLimits
argument_list|()
expr_stmt|;
name|currentStream
operator|.
name|write
argument_list|(
name|cbuf
argument_list|,
name|off
argument_list|,
name|len
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
name|LoadingCharArrayWriter
name|bout
init|=
operator|(
name|LoadingCharArrayWriter
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
name|createOutputStreamWriter
argument_list|(
name|tempFile
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
if|if
condition|(
name|currentStream
operator|!=
name|bout
condition|)
block|{
name|currentStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|deleteTempFile
argument_list|()
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
name|Reader
name|getReader
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
name|LoadingCharArrayWriter
condition|)
block|{
name|LoadingCharArrayWriter
name|lcaw
init|=
operator|(
name|LoadingCharArrayWriter
operator|)
name|currentStream
decl_stmt|;
return|return
operator|new
name|CharArrayReader
argument_list|(
name|lcaw
operator|.
name|rawCharArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|lcaw
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
try|try
block|{
name|InputStream
name|fileInputStream
init|=
operator|new
name|FileInputStream
argument_list|(
name|tempFile
argument_list|)
block|{
name|boolean
name|closed
decl_stmt|;
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
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
name|closed
operator|=
literal|true
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
if|if
condition|(
name|cipherTransformation
operator|!=
literal|null
condition|)
block|{
name|fileInputStream
operator|=
operator|new
name|CipherInputStream
argument_list|(
name|fileInputStream
argument_list|,
name|ciphers
operator|.
name|getDecryptor
argument_list|()
argument_list|)
block|{
name|boolean
name|closed
decl_stmt|;
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
return|return
operator|new
name|InputStreamReader
argument_list|(
name|fileInputStream
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
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
specifier|private
specifier|synchronized
name|void
name|deleteTempFile
parameter_list|()
block|{
if|if
condition|(
name|tempFile
operator|!=
literal|null
condition|)
block|{
name|File
name|file
init|=
name|tempFile
decl_stmt|;
name|tempFile
operator|=
literal|null
expr_stmt|;
name|FileUtils
operator|.
name|delete
argument_list|(
name|file
argument_list|)
expr_stmt|;
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
name|deleteTempFile
argument_list|()
expr_stmt|;
name|currentStream
operator|=
operator|new
name|LoadingCharArrayWriter
argument_list|()
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
specifier|public
name|void
name|setMaxSize
parameter_list|(
name|long
name|maxSize
parameter_list|)
block|{
name|this
operator|.
name|maxSize
operator|=
name|maxSize
expr_stmt|;
block|}
specifier|public
name|void
name|setCipherTransformation
parameter_list|(
name|String
name|cipherTransformation
parameter_list|)
block|{
name|this
operator|.
name|cipherTransformation
operator|=
name|cipherTransformation
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setDefaultMaxSize
parameter_list|(
name|long
name|l
parameter_list|)
block|{
if|if
condition|(
name|l
operator|==
operator|-
literal|1
condition|)
block|{
name|String
name|s
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|CachedConstants
operator|.
name|MAX_SIZE_SYS_PROP
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
comment|// lookup the deprecated property
name|s
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.io.CachedWriter.MaxSize"
argument_list|,
literal|"-1"
argument_list|)
expr_stmt|;
block|}
name|l
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|defaultMaxSize
operator|=
name|l
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setDefaultThreshold
parameter_list|(
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
name|i
operator|=
name|SystemPropertyAction
operator|.
name|getInteger
argument_list|(
name|CachedConstants
operator|.
name|THRESHOLD_SYS_PROP
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
comment|// lookup the deprecated property
name|i
operator|=
name|SystemPropertyAction
operator|.
name|getInteger
argument_list|(
literal|"org.apache.cxf.io.CachedWriter.Threshold"
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
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
block|}
name|defaultThreshold
operator|=
name|i
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|setDefaultCipherTransformation
parameter_list|(
name|String
name|n
parameter_list|)
block|{
if|if
condition|(
name|n
operator|==
literal|null
condition|)
block|{
name|n
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|CachedConstants
operator|.
name|CIPHER_TRANSFORMATION_SYS_PROP
argument_list|)
expr_stmt|;
block|}
name|defaultCipherTransformation
operator|=
name|n
expr_stmt|;
block|}
specifier|private
name|OutputStreamWriter
name|createOutputStreamWriter
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|out
init|=
operator|new
name|BufferedOutputStream
argument_list|(
name|Files
operator|.
name|newOutputStream
argument_list|(
name|file
operator|.
name|toPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cipherTransformation
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|ciphers
operator|==
literal|null
condition|)
block|{
name|ciphers
operator|=
operator|new
name|CipherPair
argument_list|(
name|cipherTransformation
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|out
operator|=
operator|new
name|CipherOutputStream
argument_list|(
name|out
argument_list|,
name|ciphers
operator|.
name|getEncryptor
argument_list|()
argument_list|)
block|{
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|cosClosed
condition|)
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|cosClosed
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
return|return
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
name|UTF_8
argument_list|)
block|{
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|cosClosed
condition|)
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|cosClosed
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
specifier|private
name|InputStreamReader
name|createInputStreamReader
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
name|Files
operator|.
name|newInputStream
argument_list|(
name|file
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cipherTransformation
operator|!=
literal|null
condition|)
block|{
name|in
operator|=
operator|new
name|CipherInputStream
argument_list|(
name|in
argument_list|,
name|ciphers
operator|.
name|getDecryptor
argument_list|()
argument_list|)
block|{
name|boolean
name|closed
decl_stmt|;
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|closed
condition|)
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
return|return
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|UTF_8
argument_list|)
return|;
block|}
block|}
end_class

end_unit

