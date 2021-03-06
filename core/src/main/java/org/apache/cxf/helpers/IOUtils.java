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
name|helpers
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
name|File
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
name|PushbackInputStream
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
name|UnsupportedEncodingException
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
name|Charset
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|io
operator|.
name|CopyingOutputStream
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
name|io
operator|.
name|Transferable
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|IOUtils
block|{
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF8_CHARSET
init|=
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_BUFFER_SIZE
init|=
literal|1024
operator|*
literal|4
decl_stmt|;
specifier|private
name|IOUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// if available is 0 it does not mean it is empty
if|if
condition|(
name|is
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|is
operator|.
name|markSupported
argument_list|()
condition|)
block|{
name|is
operator|.
name|mark
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|isEof
argument_list|(
name|is
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|is
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
operator|(
name|is
operator|instanceof
name|PushbackInputStream
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// it may be an attachment stream
name|PushbackInputStream
name|pbStream
init|=
operator|(
name|PushbackInputStream
operator|)
name|is
decl_stmt|;
name|boolean
name|isEmpty
init|=
name|isEof
argument_list|(
name|pbStream
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isEmpty
condition|)
block|{
name|pbStream
operator|.
name|unread
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
return|return
name|isEmpty
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isEof
parameter_list|(
name|int
name|result
parameter_list|)
block|{
return|return
name|result
operator|==
operator|-
literal|1
return|;
block|}
comment|/**      * Use this function instead of new String(byte[], String) to avoid surprises from      * non-standard default encodings.      * @param bytes      * @param charsetName      */
specifier|public
specifier|static
name|String
name|newStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|charsetName
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|charsetName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Impossible failure: Charset.forName(\""
operator|+
name|charsetName
operator|+
literal|"\") returns invalid name."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Use this function instead of new String(byte[]) to avoid surprises from non-standard default encodings.      * @param bytes      */
specifier|public
specifier|static
name|String
name|newStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|,
name|UTF8_CHARSET
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Use this function instead of new String(byte[], int, int, String)      * to avoid surprises from non-standard default encodings.      * @param bytes      * @param charsetName      * @param start      * @param length      */
specifier|public
specifier|static
name|String
name|newStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|charsetName
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|start
argument_list|,
name|length
argument_list|,
name|charsetName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Impossible failure: Charset.forName(\""
operator|+
name|charsetName
operator|+
literal|"\") returns invalid name."
argument_list|)
throw|;
block|}
block|}
comment|/**      * Use this function instead of new String(byte[], int, int)      * to avoid surprises from non-standard default encodings.      * @param bytes      * @param start      * @param length      */
specifier|public
specifier|static
name|String
name|newStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|,
name|UTF8_CHARSET
operator|.
name|name
argument_list|()
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|int
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|output
operator|instanceof
name|CopyingOutputStream
condition|)
block|{
return|return
operator|(
operator|(
name|CopyingOutputStream
operator|)
name|output
operator|)
operator|.
name|copyFrom
argument_list|(
name|input
argument_list|)
return|;
block|}
return|return
name|copy
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|int
name|copyAndCloseInput
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|input
init|)
block|{
return|return
name|copy
argument_list|(
name|in
argument_list|,
name|output
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|int
name|copyAndCloseInput
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|input
init|)
block|{
return|return
name|copy
argument_list|(
name|in
argument_list|,
name|output
argument_list|,
name|bufferSize
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copyAndCloseInput
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|,
specifier|final
name|Writer
name|output
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|Reader
name|r
init|=
name|input
init|)
block|{
name|copy
argument_list|(
name|r
argument_list|,
name|output
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|copyAndCloseInput
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|,
specifier|final
name|Writer
name|output
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|Reader
name|r
init|=
name|input
init|)
block|{
name|copy
argument_list|(
name|r
argument_list|,
name|output
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|int
name|copy
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|avail
init|=
name|input
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|avail
operator|>
literal|262144
condition|)
block|{
name|avail
operator|=
literal|262144
expr_stmt|;
block|}
if|if
condition|(
name|avail
operator|>
name|bufferSize
condition|)
block|{
name|bufferSize
operator|=
name|avail
expr_stmt|;
block|}
specifier|final
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"0 bytes read in violation of InputStream.read(byte[])"
argument_list|)
throw|;
block|}
name|output
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
name|total
operator|+=
name|n
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**      * Copy at least the specified number of bytes from the input to the output      * or until the inputstream is finished.      * @param input      * @param output      * @param atLeast      * @throws IOException      */
specifier|public
specifier|static
name|void
name|copyAtLeast
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
specifier|final
name|OutputStream
name|output
parameter_list|,
name|int
name|atLeast
parameter_list|)
throws|throws
name|IOException
block|{
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
name|int
name|n
init|=
name|atLeast
operator|>
name|buffer
operator|.
name|length
condition|?
name|buffer
operator|.
name|length
else|:
name|atLeast
decl_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"0 bytes read in violation of InputStream.read(byte[])"
argument_list|)
throw|;
block|}
name|output
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
name|atLeast
operator|-=
name|n
expr_stmt|;
if|if
condition|(
name|atLeast
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
name|n
operator|=
name|atLeast
operator|>
name|buffer
operator|.
name|length
condition|?
name|buffer
operator|.
name|length
else|:
name|atLeast
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
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
specifier|public
specifier|static
name|void
name|copyAtLeast
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|,
specifier|final
name|Writer
name|output
parameter_list|,
name|int
name|atLeast
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
literal|4096
index|]
decl_stmt|;
name|int
name|n
init|=
name|atLeast
operator|>
name|buffer
operator|.
name|length
condition|?
name|buffer
operator|.
name|length
else|:
name|atLeast
decl_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"0 bytes read in violation of Reader.read(char[])"
argument_list|)
throw|;
block|}
name|output
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
name|atLeast
operator|-=
name|n
expr_stmt|;
if|if
condition|(
name|atLeast
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
name|n
operator|=
name|atLeast
operator|>
name|buffer
operator|.
name|length
condition|?
name|buffer
operator|.
name|length
else|:
name|atLeast
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
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
specifier|public
specifier|static
name|void
name|copy
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|,
specifier|final
name|Writer
name|output
parameter_list|,
specifier|final
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|n
init|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
name|output
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
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|transferTo
parameter_list|(
name|InputStream
name|inputStream
parameter_list|,
name|File
name|destinationFile
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|Transferable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|inputStream
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
operator|(
operator|(
name|Transferable
operator|)
name|inputStream
operator|)
operator|.
name|transferTo
argument_list|(
name|destinationFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
init|(
name|OutputStream
name|out
init|=
name|Files
operator|.
name|newOutputStream
argument_list|(
name|destinationFile
operator|.
name|toPath
argument_list|()
argument_list|)
init|)
block|{
name|copyAndCloseInput
argument_list|(
name|inputStream
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|input
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|input
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|,
name|charset
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
name|int
name|bufferSize
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|input
argument_list|,
name|bufferSize
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
name|int
name|bufferSize
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|avail
init|=
name|input
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|avail
operator|>
name|bufferSize
condition|)
block|{
name|bufferSize
operator|=
name|avail
expr_stmt|;
block|}
name|Reader
name|reader
init|=
name|charset
operator|==
literal|null
condition|?
operator|new
name|InputStreamReader
argument_list|(
name|input
argument_list|,
name|UTF8_CHARSET
argument_list|)
else|:
operator|new
name|InputStreamReader
argument_list|(
name|input
argument_list|,
name|charset
argument_list|)
decl_stmt|;
return|return
name|toString
argument_list|(
name|reader
argument_list|,
name|bufferSize
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|input
argument_list|,
name|DEFAULT_BUFFER_SIZE
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|Reader
name|input
parameter_list|,
name|int
name|bufSize
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|char
index|[]
name|buffer
init|=
operator|new
name|char
index|[
name|bufSize
index|]
decl_stmt|;
try|try
init|(
name|Reader
name|r
init|=
name|input
init|)
block|{
name|int
name|n
init|=
name|r
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"0 bytes read in violation of InputStream.read(byte[])"
argument_list|)
throw|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|n
operator|=
name|r
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|readStringFromStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|in
argument_list|)
return|;
block|}
comment|/**      * Load the InputStream into memory and return a ByteArrayInputStream that      * represents it. Closes the in stream.      *      * @param in      * @throws IOException      */
specifier|public
specifier|static
name|ByteArrayInputStream
name|loadIntoBAIS
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|in
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|<
name|DEFAULT_BUFFER_SIZE
condition|)
block|{
name|i
operator|=
name|DEFAULT_BUFFER_SIZE
expr_stmt|;
block|}
name|LoadingByteArrayOutputStream
name|bout
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|copy
argument_list|(
name|in
argument_list|,
name|bout
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bout
operator|.
name|createInputStream
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|void
name|consume
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|in
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
comment|//if i is 0, then we MAY have already hit the end of the stream
comment|//so try a read and return rather than allocate a buffer and such
name|int
name|i2
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|i2
operator|==
operator|-
literal|1
condition|)
block|{
return|return;
block|}
comment|//reading the byte may have caused a buffer to fill
name|i
operator|=
name|in
operator|.
name|available
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|<
name|DEFAULT_BUFFER_SIZE
condition|)
block|{
name|i
operator|=
name|DEFAULT_BUFFER_SIZE
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|>
literal|65536
condition|)
block|{
name|i
operator|=
literal|65536
expr_stmt|;
block|}
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|i
index|]
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|//nothing - just discarding
block|}
block|}
comment|/**      * Consumes at least the given number of bytes from the input stream      * @param input      * @param atLeast      * @throws IOException      */
specifier|public
specifier|static
name|void
name|consume
parameter_list|(
specifier|final
name|InputStream
name|input
parameter_list|,
name|int
name|atLeast
parameter_list|)
throws|throws
name|IOException
block|{
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
name|int
name|n
init|=
name|atLeast
operator|>
name|buffer
operator|.
name|length
condition|?
name|buffer
operator|.
name|length
else|:
name|atLeast
decl_stmt|;
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
while|while
condition|(
operator|-
literal|1
operator|!=
name|n
condition|)
block|{
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"0 bytes read in violation of InputStream.read(byte[])"
argument_list|)
throw|;
block|}
name|atLeast
operator|-=
name|n
expr_stmt|;
if|if
condition|(
name|atLeast
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
name|n
operator|=
name|atLeast
operator|>
name|buffer
operator|.
name|length
condition|?
name|buffer
operator|.
name|length
else|:
name|atLeast
expr_stmt|;
name|n
operator|=
name|input
operator|.
name|read
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
specifier|public
specifier|static
name|byte
index|[]
name|readBytesFromStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|i
init|=
name|in
operator|.
name|available
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|<
name|DEFAULT_BUFFER_SIZE
condition|)
block|{
name|i
operator|=
name|DEFAULT_BUFFER_SIZE
expr_stmt|;
block|}
try|try
init|(
name|InputStream
name|input
init|=
name|in
init|;
name|ByteArrayOutputStream
name|bos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|i
argument_list|)
init|)
block|{
name|copy
argument_list|(
name|input
argument_list|,
name|bos
argument_list|)
expr_stmt|;
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

