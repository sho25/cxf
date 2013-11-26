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
name|ws
operator|.
name|security
operator|.
name|wss4j
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
name|Collection
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
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|Callback
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|UnsupportedCallbackException
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
name|attachment
operator|.
name|AttachmentDataSource
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|AttachmentRequestCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|AttachmentResultCallback
import|;
end_import

begin_comment
comment|/**  * A outbound CallbackHandler to be used to sign/encrypt SOAP Attachments.  */
end_comment

begin_class
specifier|public
class|class
name|AttachmentOutCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
specifier|final
name|SoapMessage
name|soapMessage
decl_stmt|;
specifier|public
name|AttachmentOutCallbackHandler
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
name|this
operator|.
name|soapMessage
operator|=
name|soapMessage
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Callback
name|callback
init|=
name|callbacks
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|callback
operator|instanceof
name|AttachmentRequestCallback
condition|)
block|{
name|AttachmentRequestCallback
name|attachmentRequestCallback
init|=
operator|(
name|AttachmentRequestCallback
operator|)
name|callback
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|Attachment
argument_list|>
name|attachmentList
init|=
operator|new
name|ArrayList
argument_list|<
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
name|attachmentRequestCallback
operator|.
name|setAttachments
argument_list|(
name|attachmentList
argument_list|)
expr_stmt|;
specifier|final
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|attachments
init|=
name|soapMessage
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|attachments
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Iterator
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|iterator
init|=
name|attachments
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
name|attachment
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|Attachment
name|att
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|Attachment
argument_list|()
decl_stmt|;
name|att
operator|.
name|setMimeType
argument_list|(
name|attachment
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|att
operator|.
name|setId
argument_list|(
name|attachment
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|att
operator|.
name|setSourceStream
argument_list|(
name|attachment
operator|.
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|headerIterator
init|=
name|attachment
operator|.
name|getHeaderNames
argument_list|()
decl_stmt|;
while|while
condition|(
name|headerIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|next
init|=
name|headerIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|att
operator|.
name|addHeader
argument_list|(
name|next
argument_list|,
name|attachment
operator|.
name|getHeader
argument_list|(
name|next
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|attachmentList
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|callback
operator|instanceof
name|AttachmentResultCallback
condition|)
block|{
name|AttachmentResultCallback
name|attachmentResultCallback
init|=
operator|(
name|AttachmentResultCallback
operator|)
name|callback
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|attachments
init|=
name|soapMessage
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|attachment
operator|.
name|AttachmentImpl
name|securedAttachment
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|attachment
operator|.
name|AttachmentImpl
argument_list|(
name|attachmentResultCallback
operator|.
name|getAttachmentId
argument_list|()
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|AttachmentDataSource
argument_list|(
name|attachmentResultCallback
operator|.
name|getAttachment
argument_list|()
operator|.
name|getMimeType
argument_list|()
argument_list|,
name|attachmentResultCallback
operator|.
name|getAttachment
argument_list|()
operator|.
name|getSourceStream
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|attachmentResultCallback
operator|.
name|getAttachment
argument_list|()
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|iterator
init|=
name|headers
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|next
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|securedAttachment
operator|.
name|setHeader
argument_list|(
name|next
operator|.
name|getKey
argument_list|()
argument_list|,
name|next
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|attachments
operator|.
name|add
argument_list|(
name|securedAttachment
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callback
argument_list|,
literal|"Unsupported callback"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

