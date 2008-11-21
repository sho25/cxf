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
specifier|private
specifier|static
specifier|final
name|String
name|BODY_ATTACHMENT_ID
init|=
literal|"root.message@cxf.apache.org"
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
name|boolean
name|xop
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
comment|/**      * Serialize the beginning of the attachment which includes the MIME       * beginning and headers for the root message.      */
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
argument_list|(
literal|0
argument_list|)
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
name|bodyCt
operator|=
name|bodyCt
operator|.
name|replaceAll
argument_list|(
literal|"\""
argument_list|,
literal|"\\\""
argument_list|)
expr_stmt|;
comment|// The bodyCt string is used enclosed within "", so if it contains the character ", it
comment|// should be adjusted, like in the following case:
comment|//   application/soap+xml; action="urn:ihe:iti:2007:RetrieveDocumentSet"
comment|// The attribute action is added in SoapActionOutInterceptor, when SOAP 1.2 is used
comment|// The string has to be changed in:
comment|//   application/soap+xml"; action="urn:ihe:iti:2007:RetrieveDocumentSet
comment|// so when it is enclosed within "", the result must be:
comment|//   "application/soap+xml"; action="urn:ihe:iti:2007:RetrieveDocumentSet"
comment|// instead of
comment|//   "application/soap+xml; action="urn:ihe:iti:2007:RetrieveDocumentSet""
comment|// that is wrong because when used it produces:
comment|//   type="application/soap+xml; action="urn:ihe:iti:2007:RetrieveDocumentSet""
if|if
condition|(
operator|(
name|bodyCt
operator|.
name|indexOf
argument_list|(
literal|'"'
argument_list|)
operator|!=
operator|-
literal|1
operator|)
operator|&&
operator|(
name|bodyCt
operator|.
name|indexOf
argument_list|(
literal|';'
argument_list|)
operator|!=
operator|-
literal|1
operator|)
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
name|StringBuffer
name|st
init|=
operator|new
name|StringBuffer
argument_list|(
name|bodyCt
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
argument_list|)
decl_stmt|;
name|st
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyCt
operator|.
name|substring
argument_list|(
name|pos
argument_list|,
name|bodyCt
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|bodyCt
operator|=
name|st
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|enc
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
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|enc
operator|==
literal|null
condition|)
block|{
name|enc
operator|=
literal|"UTF-8"
expr_stmt|;
block|}
comment|// Set transport mime type
name|StringBuilder
name|ct
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ct
operator|.
name|append
argument_list|(
literal|"multipart/related; "
argument_list|)
expr_stmt|;
if|if
condition|(
name|xop
condition|)
block|{
name|ct
operator|.
name|append
argument_list|(
literal|"type=\"application/xop+xml\"; "
argument_list|)
expr_stmt|;
block|}
name|ct
operator|.
name|append
argument_list|(
literal|"boundary=\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyBoundary
argument_list|)
operator|.
name|append
argument_list|(
literal|"\"; "
argument_list|)
operator|.
name|append
argument_list|(
literal|"start=\"<"
argument_list|)
operator|.
name|append
argument_list|(
name|BODY_ATTACHMENT_ID
argument_list|)
operator|.
name|append
argument_list|(
literal|">\"; "
argument_list|)
operator|.
name|append
argument_list|(
literal|"start-info=\""
argument_list|)
operator|.
name|append
argument_list|(
name|bodyCt
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
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
literal|"UTF-8"
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
name|mimeBodyCt
operator|.
name|append
argument_list|(
literal|"application/xop+xml; charset="
argument_list|)
operator|.
name|append
argument_list|(
name|enc
argument_list|)
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
literal|"\";"
argument_list|)
expr_stmt|;
name|writeHeaders
argument_list|(
name|mimeBodyCt
operator|.
name|toString
argument_list|()
argument_list|,
name|BODY_ATTACHMENT_ID
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
name|void
name|writeHeaders
parameter_list|(
name|String
name|contentType
parameter_list|,
name|String
name|attachmentId
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
literal|"\r\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Content-Type: "
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
literal|"\r\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Content-Transfer-Encoding: binary\r\n"
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
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|">\r\n\r\n"
argument_list|)
expr_stmt|;
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
name|writeHeaders
argument_list|(
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|,
name|a
operator|.
name|getId
argument_list|()
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
name|a
operator|.
name|getDataHandler
argument_list|()
operator|.
name|writeTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
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

