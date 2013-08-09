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
name|FilterInputStream
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
name|Base64Exception
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
name|Base64Utility
import|;
end_import

begin_comment
comment|/**  * An implementation of a FilterInputStream that decodes the  * stream data in BASE64 encoding format.  This version does the  * decoding "on the fly" rather than decoding a single block of  * data.  Since this version is intended for use by the MimeUtilty class,  * it also handles line breaks in the encoded data.  */
end_comment

begin_class
specifier|public
class|class
name|Base64DecoderStream
extends|extends
name|FilterInputStream
block|{
specifier|static
specifier|final
name|String
name|MAIL_BASE64_IGNOREERRORS
init|=
literal|"mail.mime.base64.ignoreerrors"
decl_stmt|;
comment|// number of decodeable units we'll try to process at one time.  We'll attempt to read that much
comment|// data from the input stream and decode in blocks.
specifier|static
specifier|final
name|int
name|BUFFERED_UNITS
init|=
literal|2000
decl_stmt|;
comment|// can be overridden by a system property.
specifier|protected
name|boolean
name|ignoreErrors
decl_stmt|;
comment|// buffer for reading in chars for decoding (which can support larger bulk reads)
specifier|protected
name|char
index|[]
name|encodedChars
init|=
operator|new
name|char
index|[
name|BUFFERED_UNITS
operator|*
literal|4
index|]
decl_stmt|;
comment|// a buffer for one decoding unit's worth of data (3 bytes).
specifier|protected
name|byte
index|[]
name|decodedChars
decl_stmt|;
comment|// count of characters in the buffer
specifier|protected
name|int
name|decodedCount
decl_stmt|;
comment|// index of the next decoded character
specifier|protected
name|int
name|decodedIndex
decl_stmt|;
specifier|public
name|Base64DecoderStream
parameter_list|(
name|InputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for the existance of decoded characters in our buffer      * of decoded data.      *      * @return True if we currently have buffered characters.      */
specifier|private
name|boolean
name|dataAvailable
parameter_list|()
block|{
return|return
name|decodedCount
operator|!=
literal|0
return|;
block|}
comment|/**      * Decode a requested number of bytes of data into a buffer.      *      * @return true if we were able to obtain more data, false otherwise.      */
specifier|private
name|boolean
name|decodeStreamData
parameter_list|()
throws|throws
name|IOException
block|{
name|decodedIndex
operator|=
literal|0
expr_stmt|;
comment|// fill up a data buffer with input data
name|int
name|readCharacters
init|=
name|fillEncodedBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|readCharacters
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|decodedChars
operator|=
name|Base64Utility
operator|.
name|decodeChunk
argument_list|(
name|encodedChars
argument_list|,
literal|0
argument_list|,
name|readCharacters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|decodedCount
operator|=
name|decodedChars
operator|.
name|length
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Retrieve a single byte from the decoded characters buffer.      *      * @return The decoded character or -1 if there was an EOF condition.      */
specifier|private
name|int
name|getByte
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|dataAvailable
argument_list|()
operator|&&
operator|!
name|decodeStreamData
argument_list|()
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|decodedCount
operator|--
expr_stmt|;
comment|// we need to ensure this doesn't get sign extended
return|return
name|decodedChars
index|[
name|decodedIndex
operator|++
index|]
operator|&
literal|0xff
return|;
block|}
specifier|private
name|int
name|getBytes
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|readCharacters
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|length
operator|>
literal|0
condition|)
block|{
comment|// need data?  Try to get some
if|if
condition|(
operator|!
name|dataAvailable
argument_list|()
operator|&&
operator|!
name|decodeStreamData
argument_list|()
condition|)
block|{
comment|// if we can't get this, return a count of how much we did get (which may be -1).
return|return
name|readCharacters
operator|>
literal|0
condition|?
name|readCharacters
else|:
operator|-
literal|1
return|;
block|}
comment|// now copy some of the data from the decoded buffer to the target buffer
name|int
name|copyCount
init|=
name|Math
operator|.
name|min
argument_list|(
name|decodedCount
argument_list|,
name|length
argument_list|)
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|decodedChars
argument_list|,
name|decodedIndex
argument_list|,
name|data
argument_list|,
name|offset
argument_list|,
name|copyCount
argument_list|)
expr_stmt|;
name|decodedIndex
operator|+=
name|copyCount
expr_stmt|;
name|decodedCount
operator|-=
name|copyCount
expr_stmt|;
name|offset
operator|+=
name|copyCount
expr_stmt|;
name|length
operator|-=
name|copyCount
expr_stmt|;
name|readCharacters
operator|+=
name|copyCount
expr_stmt|;
block|}
return|return
name|readCharacters
return|;
block|}
comment|/**      * Fill our buffer of input characters for decoding from the      * stream.  This will attempt read a full buffer, but will      * terminate on an EOF or read error.  This will filter out      * non-Base64 encoding chars and will only return a valid      * multiple of 4 number of bytes.      *      * @return The count of characters read.      */
specifier|private
name|int
name|fillEncodedBuffer
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|readCharacters
init|=
literal|0
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
comment|// get the next character from the stream
name|int
name|ch
init|=
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
comment|// did we hit an EOF condition?
if|if
condition|(
name|ch
operator|==
operator|-
literal|1
condition|)
block|{
comment|// now check to see if this is normal, or potentially an error
comment|// if we didn't get characters as a multiple of 4, we may need to complain about this.
if|if
condition|(
operator|(
name|readCharacters
operator|%
literal|4
operator|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Base64 encoding error, data truncated"
argument_list|)
throw|;
block|}
comment|// return the count.
return|return
name|readCharacters
return|;
block|}
elseif|else
if|if
condition|(
name|Base64Utility
operator|.
name|isValidBase64
argument_list|(
name|ch
argument_list|)
condition|)
block|{
comment|// if this character is valid in a Base64 stream, copy it to the buffer.
name|encodedChars
index|[
name|readCharacters
operator|++
index|]
operator|=
operator|(
name|char
operator|)
name|ch
expr_stmt|;
comment|// if we've filled up the buffer, time to quit.
if|if
condition|(
name|readCharacters
operator|>=
name|encodedChars
operator|.
name|length
condition|)
block|{
return|return
name|readCharacters
return|;
block|}
block|}
comment|// we're filtering out whitespace and CRLF characters, so just ignore these
block|}
block|}
comment|// in order to function as a filter, these streams need to override the different
comment|// read() signature.
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getByte
argument_list|()
return|;
block|}
specifier|public
name|int
name|read
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getBytes
argument_list|(
name|buffer
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|markSupported
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
operator|(
name|in
operator|.
name|available
argument_list|()
operator|/
literal|4
operator|)
operator|*
literal|3
operator|)
operator|+
name|decodedCount
return|;
block|}
block|}
end_class

end_unit

