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
name|PushbackInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|InternetHeaders
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
name|io
operator|.
name|CachedOutputStream
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
name|message
operator|.
name|Attachment
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|AttachmentDeserializer
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_DIRECTORY
init|=
literal|"attachment-directory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_MEMORY_THRESHOLD
init|=
literal|"attachment-memory-threshold"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|THRESHOLD
init|=
literal|1024
operator|*
literal|100
decl_stmt|;
comment|//100K (byte unit)
specifier|private
specifier|static
specifier|final
name|Pattern
name|CONTENT_TYPE_BOUNDARY_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"boundary=\"?([^\";]*)"
argument_list|)
decl_stmt|;
comment|// TODO: Is there a better way to detect boundaries in the message content?
comment|// It seems constricting to assume the boundary will start with ----=_Part_
specifier|private
specifier|static
specifier|final
name|Pattern
name|INPUT_STREAM_BOUNDARY_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^--(----=_Part_\\S*)"
argument_list|,
name|Pattern
operator|.
name|MULTILINE
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|lazyLoading
init|=
literal|true
decl_stmt|;
specifier|private
name|int
name|pbAmount
init|=
literal|2048
decl_stmt|;
specifier|private
name|PushbackInputStream
name|stream
decl_stmt|;
specifier|private
name|byte
name|boundary
index|[]
decl_stmt|;
specifier|private
name|String
name|contentType
decl_stmt|;
specifier|private
name|LazyAttachmentCollection
name|attachments
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|InputStream
name|body
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|DelegatingInputStream
argument_list|>
name|loaded
init|=
operator|new
name|HashSet
argument_list|<
name|DelegatingInputStream
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|AttachmentDeserializer
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
specifier|public
name|void
name|initializeAttachments
parameter_list|()
throws|throws
name|IOException
block|{
name|initializeRootMessage
argument_list|()
expr_stmt|;
name|attachments
operator|=
operator|new
name|LazyAttachmentCollection
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|message
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initializeRootMessage
parameter_list|()
throws|throws
name|IOException
block|{
name|contentType
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Content-Type can not be empty!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"An InputStream must be provided!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|contentType
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"multipart/related"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|boundaryString
init|=
name|findBoundaryFromContentType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|boundaryString
condition|)
block|{
name|boundaryString
operator|=
name|findBoundaryFromInputStream
argument_list|()
expr_stmt|;
block|}
comment|// If a boundary still wasn't found, throw an exception
if|if
condition|(
literal|null
operator|==
name|boundaryString
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Couldn't determine the boundary from the message!"
argument_list|)
throw|;
block|}
name|boundary
operator|=
name|boundaryString
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
expr_stmt|;
name|stream
operator|=
operator|new
name|PushbackInputStream
argument_list|(
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
name|pbAmount
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|readTillFirstBoundary
argument_list|(
name|stream
argument_list|,
name|boundary
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Couldn't find MIME boundary: "
operator|+
name|boundaryString
argument_list|)
throw|;
block|}
try|try
block|{
comment|// TODO: Do we need to copy these headers somewhere?
operator|new
name|InternetHeaders
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|body
operator|=
operator|new
name|DelegatingInputStream
argument_list|(
operator|new
name|MimeBodyPartInputStream
argument_list|(
name|stream
argument_list|,
name|boundary
argument_list|,
name|pbAmount
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|findBoundaryFromContentType
parameter_list|(
name|String
name|ct
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Use regex to get the boundary and return null if it's not found
name|Matcher
name|m
init|=
name|CONTENT_TYPE_BOUNDARY_PATTERN
operator|.
name|matcher
argument_list|(
name|ct
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|find
argument_list|()
condition|?
literal|"--"
operator|+
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
specifier|private
name|String
name|findBoundaryFromInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//boundary should definitely be in the first 2K;
name|PushbackInputStream
name|in
init|=
operator|new
name|PushbackInputStream
argument_list|(
name|is
argument_list|,
literal|4096
argument_list|)
decl_stmt|;
name|byte
name|buf
index|[]
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|i
init|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|String
name|msg
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|in
operator|.
name|unread
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
comment|// Reset the input stream since we'll need it again later
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|in
argument_list|)
expr_stmt|;
comment|// Use regex to get the boundary and return null if it's not found
name|Matcher
name|m
init|=
name|INPUT_STREAM_BOUNDARY_PATTERN
operator|.
name|matcher
argument_list|(
name|msg
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|find
argument_list|()
condition|?
literal|"--"
operator|+
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
specifier|private
name|void
name|setStreamedAttachmentProperties
parameter_list|(
name|CachedOutputStream
name|bos
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|directory
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|ATTACHMENT_DIRECTORY
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|directory
operator|instanceof
name|File
condition|)
block|{
name|bos
operator|.
name|setOutputDir
argument_list|(
operator|(
name|File
operator|)
name|directory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bos
operator|.
name|setOutputDir
argument_list|(
operator|new
name|File
argument_list|(
operator|(
name|String
operator|)
name|directory
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Object
name|threshold
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|)
decl_stmt|;
if|if
condition|(
name|threshold
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|threshold
operator|instanceof
name|Long
condition|)
block|{
name|bos
operator|.
name|setThreshold
argument_list|(
operator|(
name|Long
operator|)
name|threshold
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bos
operator|.
name|setThreshold
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|threshold
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|bos
operator|.
name|setThreshold
argument_list|(
name|THRESHOLD
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|AttachmentImpl
name|readNext
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Cache any mime parts that are currently being streamed
name|cacheStreamedAttachments
argument_list|()
expr_stmt|;
name|int
name|v
init|=
name|stream
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
name|stream
operator|.
name|unread
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|InternetHeaders
name|headers
decl_stmt|;
try|try
block|{
name|headers
operator|=
operator|new
name|InternetHeaders
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
comment|// TODO create custom IOException
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|String
name|id
init|=
name|headers
operator|.
name|getHeader
argument_list|(
literal|"Content-ID"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|id
operator|.
name|startsWith
argument_list|(
literal|"<"
argument_list|)
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|id
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//no Content-ID, set cxf default ID
name|id
operator|=
literal|"Content-ID:<root.message@cxf.apache.org"
expr_stmt|;
block|}
name|id
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|id
operator|.
name|startsWith
argument_list|(
literal|"cid:"
argument_list|)
condition|?
name|id
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
else|:
name|id
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|AttachmentImpl
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|setupAttachment
argument_list|(
name|att
argument_list|,
name|headers
argument_list|)
expr_stmt|;
return|return
name|att
return|;
block|}
specifier|private
name|void
name|cacheStreamedAttachments
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|body
operator|instanceof
name|DelegatingInputStream
operator|&&
operator|!
operator|(
operator|(
name|DelegatingInputStream
operator|)
name|body
operator|)
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|cache
argument_list|(
operator|(
name|DelegatingInputStream
operator|)
name|body
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Attachment
name|a
range|:
name|attachments
operator|.
name|getLoadedAttachments
argument_list|()
control|)
block|{
name|DataSource
name|s
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getDataSource
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|AttachmentDataSource
operator|)
condition|)
block|{
comment|//AttachementDataSource objects are already cached
name|cache
argument_list|(
operator|(
name|DelegatingInputStream
operator|)
name|s
operator|.
name|getInputStream
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|cache
parameter_list|(
name|DelegatingInputStream
name|input
parameter_list|,
name|boolean
name|deleteOnClose
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|loaded
operator|.
name|contains
argument_list|(
name|input
argument_list|)
condition|)
block|{
return|return;
block|}
name|loaded
operator|.
name|add
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|CachedOutputStream
name|out
init|=
literal|null
decl_stmt|;
name|InputStream
name|origIn
init|=
name|input
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|out
operator|=
operator|new
name|CachedOutputStream
argument_list|()
expr_stmt|;
name|setStreamedAttachmentProperties
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|input
operator|.
name|setInputStream
argument_list|(
name|out
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|origIn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Move the read pointer to the begining of the first part read till the end      * of first boundary      *      * @param pushbackInStream      * @param boundary      * @throws MessagingException      */
specifier|private
specifier|static
name|boolean
name|readTillFirstBoundary
parameter_list|(
name|PushbackInputStream
name|pbs
parameter_list|,
name|byte
index|[]
name|bp
parameter_list|)
throws|throws
name|IOException
block|{
comment|// work around a bug in PushBackInputStream where the buffer isn't
comment|// initialized
comment|// and available always returns 0.
name|int
name|value
init|=
name|pbs
operator|.
name|read
argument_list|()
decl_stmt|;
name|pbs
operator|.
name|unread
argument_list|(
name|value
argument_list|)
expr_stmt|;
while|while
condition|(
name|value
operator|!=
operator|-
literal|1
condition|)
block|{
name|value
operator|=
name|pbs
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
operator|==
name|bp
index|[
literal|0
index|]
condition|)
block|{
name|int
name|boundaryIndex
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|value
operator|!=
operator|-
literal|1
operator|&&
operator|(
name|boundaryIndex
operator|<
name|bp
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
name|bp
index|[
name|boundaryIndex
index|]
operator|)
condition|)
block|{
name|value
operator|=
name|pbs
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected End while searching for first Mime Boundary"
argument_list|)
throw|;
block|}
name|boundaryIndex
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|boundaryIndex
operator|==
name|bp
operator|.
name|length
condition|)
block|{
comment|// boundary found, read the newline
if|if
condition|(
name|value
operator|==
literal|13
condition|)
block|{
name|pbs
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Create an Attachment from the MIME stream. If there is a previous attachment      * that is not read, cache that attachment.      *      * @return      * @throws IOException      */
specifier|private
name|void
name|setupAttachment
parameter_list|(
name|AttachmentImpl
name|att
parameter_list|,
name|InternetHeaders
name|headers
parameter_list|)
throws|throws
name|IOException
block|{
name|MimeBodyPartInputStream
name|partStream
init|=
operator|new
name|MimeBodyPartInputStream
argument_list|(
name|stream
argument_list|,
name|boundary
argument_list|,
name|pbAmount
argument_list|)
decl_stmt|;
specifier|final
name|String
name|ct
init|=
name|headers
operator|.
name|getHeader
argument_list|(
literal|"Content-Type"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|boolean
name|quotedPrintable
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|?
argument_list|>
name|e
init|=
name|headers
operator|.
name|getAllHeaders
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|Header
name|header
init|=
operator|(
name|Header
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"Content-Transfer-Encoding"
argument_list|)
condition|)
block|{
if|if
condition|(
name|header
operator|.
name|getValue
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"binary"
argument_list|)
condition|)
block|{
name|att
operator|.
name|setXOP
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|header
operator|.
name|getValue
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"quoted-printable"
argument_list|)
condition|)
block|{
name|quotedPrintable
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|att
operator|.
name|setHeader
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DelegatingInputStream
name|is
init|=
operator|new
name|DelegatingInputStream
argument_list|(
name|partStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|quotedPrintable
condition|)
block|{
name|DataSource
name|source
init|=
operator|new
name|AttachmentDataSource
argument_list|(
name|ct
argument_list|,
operator|new
name|QuotedPrintableDecoderStream
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|att
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DataSource
name|source
init|=
operator|new
name|AttachmentDataSource
argument_list|(
name|ct
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|att
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|isLazyLoading
parameter_list|()
block|{
return|return
name|lazyLoading
return|;
block|}
specifier|public
name|void
name|setLazyLoading
parameter_list|(
name|boolean
name|lazyLoading
parameter_list|)
block|{
name|this
operator|.
name|lazyLoading
operator|=
name|lazyLoading
expr_stmt|;
block|}
block|}
end_class

end_unit

