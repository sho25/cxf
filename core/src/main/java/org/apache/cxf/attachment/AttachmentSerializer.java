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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|net
operator|.
name|URLDecoder
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
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
name|AttachmentSerializer
block|{
comment|// http://tools.ietf.org/html/rfc2387
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_MULTIPART_TYPE
init|=
literal|"multipart/related"
decl_stmt|;
specifier|private
name|String
name|contentTransferEncoding
init|=
literal|"binary"
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|String
name|bodyBoundary
decl_stmt|;
specifier|private
name|OutputStream
name|out
decl_stmt|;
specifier|private
name|String
name|encoding
decl_stmt|;
specifier|private
name|String
name|multipartType
decl_stmt|;
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
name|rootHeaders
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|xop
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|writeOptionalTypeParameters
init|=
literal|true
decl_stmt|;
specifier|public
name|AttachmentSerializer
parameter_list|(
name|Message
name|messageParam
parameter_list|)
block|{
name|message
operator|=
name|messageParam
expr_stmt|;
block|}
specifier|public
name|AttachmentSerializer
parameter_list|(
name|Message
name|messageParam
parameter_list|,
name|String
name|multipartType
parameter_list|,
name|boolean
name|writeOptionalTypeParameters
parameter_list|,
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
block|{
name|message
operator|=
name|messageParam
expr_stmt|;
name|this
operator|.
name|multipartType
operator|=
name|multipartType
expr_stmt|;
name|this
operator|.
name|writeOptionalTypeParameters
operator|=
name|writeOptionalTypeParameters
expr_stmt|;
name|this
operator|.
name|rootHeaders
operator|=
name|headers
expr_stmt|;
block|}
comment|/**      * Serialize the beginning of the attachment which includes the MIME      * beginning and headers for the root message.      */
specifier|public
name|void
name|writeProlog
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Create boundary for body
name|bodyBoundary
operator|=
name|AttachmentUtil
operator|.
name|getUniqueBoundaryValue
argument_list|()
expr_stmt|;
name|String
name|bodyCt
init|=
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
decl_stmt|;
name|String
name|bodyCtParams
init|=
literal|null
decl_stmt|;
name|String
name|bodyCtParamsEscaped
init|=
literal|null
decl_stmt|;
comment|// split the bodyCt to its head that is the type and its properties so that we
comment|// can insert the values at the right places based on the soap version and the mtom option
comment|// bodyCt will be of the form
comment|// soap11 -> text/xml
comment|// soap12 -> application/soap+xml; action="urn:ihe:iti:2007:RetrieveDocumentSet"
if|if
condition|(
name|bodyCt
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|pos
init|=
name|bodyCt
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
decl_stmt|;
comment|// get everything from the semi-colon
name|bodyCtParams
operator|=
name|bodyCt
operator|.
name|substring
argument_list|(
name|pos
argument_list|)
expr_stmt|;
name|bodyCtParamsEscaped
operator|=
name|escapeQuotes
argument_list|(
name|bodyCtParams
argument_list|)
expr_stmt|;
comment|// keep the type/subtype part in bodyCt
name|bodyCt
operator|=
name|bodyCt
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
expr_stmt|;
block|}
comment|// Set transport mime type
name|String
name|requestMimeType
init|=
name|multipartType
operator|==
literal|null
condition|?
name|DEFAULT_MULTIPART_TYPE
else|:
name|multipartType
decl_stmt|;
name|StringBuilder
name|ct
init|=
operator|new
name|StringBuilder
argument_list|(
literal|32
argument_list|)
decl_stmt|;
name|ct
operator|.
name|append
argument_list|(
name|requestMimeType
argument_list|)
expr_stmt|;
comment|// having xop set to true implies multipart/related, but just in case...
name|boolean
name|xopOrMultipartRelated
init|=
name|xop
operator|||
name|DEFAULT_MULTIPART_TYPE
operator|.
name|equalsIgnoreCase
argument_list|(
name|requestMimeType
argument_list|)
operator|||
name|DEFAULT_MULTIPART_TYPE
operator|.
name|startsWith
argument_list|(
name|requestMimeType
argument_list|)
decl_stmt|;
comment|// type is a required parameter for multipart/related only
if|if
condition|(
name|xopOrMultipartRelated
operator|&&
name|requestMimeType
operator|.
name|indexOf
argument_list|(
literal|"type="
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|xop
condition|)
block|{
name|ct
operator|.
name|append
argument_list|(
literal|"; type=\"application/xop+xml\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ct
operator|.
name|append
argument_list|(
literal|"; type=\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyCt
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
block|}
comment|// boundary
name|ct
operator|.
name|append
argument_list|(
literal|"; boundary=\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyBoundary
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|String
name|rootContentId
init|=
name|getHeaderValue
argument_list|(
literal|"Content-ID"
argument_list|,
name|AttachmentUtil
operator|.
name|BODY_ATTACHMENT_ID
argument_list|)
decl_stmt|;
comment|// 'start' is a required parameter for XOP/MTOM, clearly defined
comment|// for simpler multipart/related payloads but is not needed for
comment|// multipart/mixed, multipart/form-data
if|if
condition|(
name|xopOrMultipartRelated
condition|)
block|{
name|ct
operator|.
name|append
argument_list|(
literal|"; start=\"<"
argument_list|)
operator|.
name|append
argument_list|(
name|checkAngleBrackets
argument_list|(
name|rootContentId
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|">\""
argument_list|)
expr_stmt|;
block|}
comment|// start-info is a required parameter for XOP/MTOM, may be needed for
comment|// other WS cases but is redundant in simpler multipart/related cases
comment|// the parameters need to be included within the start-info's value in the escaped form
if|if
condition|(
name|writeOptionalTypeParameters
operator|||
name|xop
condition|)
block|{
name|ct
operator|.
name|append
argument_list|(
literal|"; start-info=\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyCt
argument_list|)
expr_stmt|;
if|if
condition|(
name|bodyCtParamsEscaped
operator|!=
literal|null
condition|)
block|{
name|ct
operator|.
name|append
argument_list|(
name|bodyCtParamsEscaped
argument_list|)
expr_stmt|;
block|}
name|ct
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2. write headers
name|out
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|encoding
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
name|ENCODING
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bodyBoundary
argument_list|)
expr_stmt|;
name|StringBuilder
name|mimeBodyCt
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|bodyType
init|=
name|getHeaderValue
argument_list|(
literal|"Content-Type"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|bodyType
operator|==
literal|null
condition|)
block|{
name|mimeBodyCt
operator|.
name|append
argument_list|(
name|xop
condition|?
literal|"application/xop+xml"
else|:
name|bodyCt
argument_list|)
operator|.
name|append
argument_list|(
literal|"; charset="
argument_list|)
operator|.
name|append
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
if|if
condition|(
name|xop
condition|)
block|{
name|mimeBodyCt
operator|.
name|append
argument_list|(
literal|"; type=\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyCt
argument_list|)
expr_stmt|;
if|if
condition|(
name|bodyCtParamsEscaped
operator|!=
literal|null
condition|)
block|{
name|mimeBodyCt
operator|.
name|append
argument_list|(
name|bodyCtParamsEscaped
argument_list|)
expr_stmt|;
block|}
name|mimeBodyCt
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bodyCtParams
operator|!=
literal|null
condition|)
block|{
name|mimeBodyCt
operator|.
name|append
argument_list|(
name|bodyCtParams
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|mimeBodyCt
operator|.
name|append
argument_list|(
name|bodyType
argument_list|)
expr_stmt|;
block|}
name|writeHeaders
argument_list|(
name|mimeBodyCt
operator|.
name|toString
argument_list|()
argument_list|,
name|rootContentId
argument_list|,
name|rootHeaders
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|escapeQuotes
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|.
name|indexOf
argument_list|(
literal|'"'
argument_list|)
operator|!=
literal|0
condition|?
name|s
operator|.
name|replace
argument_list|(
literal|"\""
argument_list|,
literal|"\\\""
argument_list|)
else|:
name|s
return|;
block|}
specifier|public
name|void
name|setContentTransferEncoding
parameter_list|(
name|String
name|cte
parameter_list|)
block|{
name|contentTransferEncoding
operator|=
name|cte
expr_stmt|;
block|}
specifier|private
name|String
name|getHeaderValue
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|value
init|=
name|rootHeaders
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|value
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|value
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|value
operator|.
name|size
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|writeHeaders
parameter_list|(
name|String
name|contentType
parameter_list|,
name|String
name|attachmentId
parameter_list|,
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
parameter_list|,
name|Writer
name|writer
parameter_list|)
throws|throws
name|IOException
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"\r\nContent-Type: "
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"\r\nContent-Transfer-Encoding: "
operator|+
name|contentTransferEncoding
operator|+
literal|"\r\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|attachmentId
operator|!=
literal|null
condition|)
block|{
name|attachmentId
operator|=
name|checkAngleBrackets
argument_list|(
name|attachmentId
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Content-ID:<"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|URLDecoder
operator|.
name|decode
argument_list|(
name|attachmentId
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|">\r\n"
argument_list|)
expr_stmt|;
block|}
comment|// headers like Content-Disposition need to be serialized
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"Content-Type"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
operator|||
literal|"Content-ID"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
operator|||
literal|"Content-Transfer-Encoding"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|writer
operator|.
name|write
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|values
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|values
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|values
operator|.
name|size
argument_list|()
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|checkAngleBrackets
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'<'
operator|&&
name|value
operator|.
name|charAt
argument_list|(
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'>'
condition|)
block|{
return|return
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Write the end of the body boundary and any attachments included.      * @throws IOException      */
specifier|public
name|void
name|writeAttachments
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|message
operator|.
name|getAttachments
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Attachment
name|a
range|:
name|message
operator|.
name|getAttachments
argument_list|()
control|)
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"\r\n--"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bodyBoundary
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
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|a
operator|.
name|getHeaderNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|headers
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|a
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|headers
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
name|DataHandler
name|handler
init|=
name|a
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|setCommandMap
argument_list|(
name|AttachmentUtil
operator|.
name|getCommandMap
argument_list|()
argument_list|)
expr_stmt|;
name|writeHeaders
argument_list|(
name|handler
operator|.
name|getContentType
argument_list|()
argument_list|,
name|a
operator|.
name|getId
argument_list|()
argument_list|,
name|headers
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"base64"
operator|.
name|equals
argument_list|(
name|contentTransferEncoding
argument_list|)
condition|)
block|{
try|try
init|(
name|InputStream
name|inputStream
init|=
name|handler
operator|.
name|getInputStream
argument_list|()
init|)
block|{
name|encodeBase64
argument_list|(
name|inputStream
argument_list|,
name|out
argument_list|,
name|IOUtils
operator|.
name|DEFAULT_BUFFER_SIZE
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|handler
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"\r\n--"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|bodyBoundary
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"--"
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|private
name|int
name|encodeBase64
parameter_list|(
name|InputStream
name|input
parameter_list|,
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
literal|262143
condition|)
block|{
comment|//must be divisible by 3
name|avail
operator|=
literal|262143
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
comment|//make sure n is divisible by 3
name|int
name|left
init|=
name|n
operator|%
literal|3
decl_stmt|;
name|n
operator|-=
name|left
expr_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|Base64Utility
operator|.
name|encodeAndStream
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|n
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|total
operator|+=
name|n
expr_stmt|;
block|}
if|if
condition|(
name|left
operator|!=
literal|0
condition|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|left
condition|;
operator|++
name|x
control|)
block|{
name|buffer
index|[
name|x
index|]
operator|=
name|buffer
index|[
name|n
operator|+
name|x
index|]
expr_stmt|;
block|}
name|n
operator|=
name|input
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
name|left
argument_list|,
name|buffer
operator|.
name|length
operator|-
name|left
argument_list|)
expr_stmt|;
if|if
condition|(
name|n
operator|==
operator|-
literal|1
condition|)
block|{
comment|// we've hit the end, but still have stuff left, write it out
name|Base64Utility
operator|.
name|encodeAndStream
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|left
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|total
operator|+=
name|left
expr_stmt|;
block|}
block|}
else|else
block|{
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
return|return
name|total
return|;
block|}
specifier|public
name|boolean
name|isXop
parameter_list|()
block|{
return|return
name|xop
return|;
block|}
specifier|public
name|void
name|setXop
parameter_list|(
name|boolean
name|xop
parameter_list|)
block|{
name|this
operator|.
name|xop
operator|=
name|xop
expr_stmt|;
block|}
block|}
end_class

end_unit

