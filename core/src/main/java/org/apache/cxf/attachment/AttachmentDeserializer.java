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
name|HashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|DataSource
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
name|StringUtils
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
name|HttpHeaderHelper
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
name|ATTACHMENT_PART_HEADERS
init|=
name|AttachmentDeserializer
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".headers"
decl_stmt|;
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
name|String
name|ATTACHMENT_MAX_SIZE
init|=
literal|"attachment-max-size"
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
literal|"^--(\\S*)$"
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
name|int
name|createCount
decl_stmt|;
specifier|private
name|int
name|closedCount
decl_stmt|;
specifier|private
name|boolean
name|closed
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|supportedTypes
decl_stmt|;
specifier|public
name|AttachmentDeserializer
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"multipart/related"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AttachmentDeserializer
parameter_list|(
name|Message
name|message
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|supportedTypes
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|supportedTypes
operator|=
name|supportedTypes
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
name|AttachmentUtil
operator|.
name|isTypeSupported
argument_list|(
name|contentType
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|supportedTypes
argument_list|)
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|ih
init|=
name|loadPartHeaders
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|ATTACHMENT_PART_HEADERS
argument_list|,
name|ih
argument_list|)
expr_stmt|;
name|String
name|val
init|=
name|AttachmentUtil
operator|.
name|getHeader
argument_list|(
name|ih
argument_list|,
literal|"Content-Type"
argument_list|,
literal|"; "
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|val
argument_list|)
condition|)
block|{
name|String
name|cs
init|=
name|HttpHeaderHelper
operator|.
name|findCharset
argument_list|(
name|val
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|cs
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
name|HttpHeaderHelper
operator|.
name|mapCharset
argument_list|(
name|cs
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|createCount
operator|++
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
if|if
condition|(
name|closed
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|loadPartHeaders
argument_list|(
name|stream
argument_list|)
decl_stmt|;
return|return
operator|(
name|AttachmentImpl
operator|)
name|createAttachment
argument_list|(
name|headers
argument_list|)
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
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|attachments
operator|.
name|getLoadedAttachments
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|atts
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
name|s
operator|instanceof
name|AttachmentDataSource
condition|)
block|{
name|AttachmentDataSource
name|ads
init|=
operator|(
name|AttachmentDataSource
operator|)
name|s
decl_stmt|;
if|if
condition|(
operator|!
name|ads
operator|.
name|isCached
argument_list|()
condition|)
block|{
name|ads
operator|.
name|cache
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|s
operator|.
name|getInputStream
argument_list|()
operator|instanceof
name|DelegatingInputStream
condition|)
block|{
name|cache
argument_list|(
operator|(
name|DelegatingInputStream
operator|)
name|s
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//assume a normal stream that is already cached
block|}
block|}
block|}
specifier|private
name|void
name|cache
parameter_list|(
name|DelegatingInputStream
name|input
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
name|InputStream
name|origIn
init|=
name|input
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|CachedOutputStream
name|out
init|=
operator|new
name|CachedOutputStream
argument_list|()
init|)
block|{
name|AttachmentUtil
operator|.
name|setStreamedAttachmentProperties
argument_list|(
name|message
argument_list|,
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
comment|/**      * Create an Attachment from the MIME stream. If there is a previous attachment      * that is not read, cache that attachment.      *      * @throws IOException      */
specifier|private
name|Attachment
name|createAttachment
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|partStream
init|=
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
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|createCount
operator|++
expr_stmt|;
return|return
name|AttachmentUtil
operator|.
name|createAttachment
argument_list|(
name|partStream
argument_list|,
name|headers
argument_list|)
return|;
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
specifier|public
name|void
name|markClosed
parameter_list|(
name|DelegatingInputStream
name|delegatingInputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|closedCount
operator|++
expr_stmt|;
if|if
condition|(
name|closedCount
operator|==
name|createCount
operator|&&
operator|!
name|attachments
operator|.
name|hasNext
argument_list|(
literal|false
argument_list|)
condition|)
block|{
name|int
name|x
init|=
name|stream
operator|.
name|read
argument_list|()
decl_stmt|;
while|while
condition|(
name|x
operator|!=
operator|-
literal|1
condition|)
block|{
name|x
operator|=
name|stream
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|stream
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
comment|/**      *  Check for more attachment.      *      * @return whether there is more attachment or not.  It will not deserialize the next attachment.      * @throws IOException      */
specifier|public
name|boolean
name|hasNext
parameter_list|()
throws|throws
name|IOException
block|{
name|cacheStreamedAttachments
argument_list|()
expr_stmt|;
if|if
condition|(
name|closed
condition|)
block|{
return|return
literal|false
return|;
block|}
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
literal|false
return|;
block|}
name|stream
operator|.
name|unread
argument_list|(
name|v
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|loadPartHeaders
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|heads
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
comment|// loop until we hit the end or a null line
while|while
condition|(
name|readLine
argument_list|(
name|in
argument_list|,
name|b
argument_list|)
condition|)
block|{
comment|// lines beginning with white space get special handling
name|char
name|c
init|=
name|b
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|' '
operator|||
name|c
operator|==
literal|'\t'
condition|)
block|{
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
comment|// preserve the line break and append the continuation
name|buffer
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// if we have a line pending in the buffer, flush it
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addHeaderLine
argument_list|(
name|heads
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// add this to the accumulator
name|buffer
operator|.
name|append
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if we have a line pending in the buffer, flush it
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addHeaderLine
argument_list|(
name|heads
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|heads
return|;
block|}
specifier|private
name|boolean
name|readLine
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|StringBuilder
name|buffer
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|buffer
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|int
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
comment|// a linefeed is a terminator, always.
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\r'
condition|)
block|{
comment|//just ignore the CR.  The next character SHOULD be an NL.  If not, we're
comment|//just going to discard this
continue|continue;
block|}
else|else
block|{
comment|// just add to the buffer
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
block|}
comment|// no characters found...this was either an eof or a null line.
return|return
name|buffer
operator|.
name|length
argument_list|()
operator|!=
literal|0
return|;
block|}
specifier|private
name|void
name|addHeaderLine
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|heads
parameter_list|,
name|StringBuilder
name|line
parameter_list|)
block|{
comment|// null lines are a nop
specifier|final
name|int
name|size
init|=
name|line
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|int
name|separator
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
name|String
name|value
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|separator
operator|==
operator|-
literal|1
condition|)
block|{
name|name
operator|=
name|line
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|separator
argument_list|)
expr_stmt|;
comment|// step past the separator.  Now we need to remove any leading white space characters.
name|separator
operator|++
expr_stmt|;
while|while
condition|(
name|separator
operator|<
name|size
condition|)
block|{
name|char
name|ch
init|=
name|line
operator|.
name|charAt
argument_list|(
name|separator
argument_list|)
decl_stmt|;
if|if
condition|(
name|ch
operator|!=
literal|' '
operator|&&
name|ch
operator|!=
literal|'\t'
operator|&&
name|ch
operator|!=
literal|'\r'
operator|&&
name|ch
operator|!=
literal|'\n'
condition|)
block|{
break|break;
block|}
name|separator
operator|++
expr_stmt|;
block|}
name|value
operator|=
name|line
operator|.
name|substring
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|v
init|=
name|heads
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|==
literal|null
condition|)
block|{
name|v
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|heads
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
name|v
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

