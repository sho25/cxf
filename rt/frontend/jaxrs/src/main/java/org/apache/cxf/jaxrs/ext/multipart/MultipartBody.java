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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
import|;
end_import

begin_class
specifier|public
class|class
name|MultipartBody
block|{
specifier|public
specifier|static
specifier|final
name|String
name|INBOUND_MESSAGE_ATTACHMENTS
init|=
literal|"org.apache.cxf.jaxrs.attachments.inbound"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUTBOUND_MESSAGE_ATTACHMENTS
init|=
literal|"org.apache.cxf.jaxrs.attachments.outbound"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|MediaType
name|MULTIPART_RELATED_TYPE
init|=
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
literal|"multipart/related"
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
decl_stmt|;
specifier|private
name|MediaType
name|mt
decl_stmt|;
specifier|public
name|MultipartBody
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|boolean
name|outbound
parameter_list|)
block|{
name|this
operator|.
name|atts
operator|=
name|atts
expr_stmt|;
name|this
operator|.
name|mt
operator|=
name|mt
operator|==
literal|null
condition|?
name|MULTIPART_RELATED_TYPE
else|:
name|mt
expr_stmt|;
block|}
specifier|public
name|MultipartBody
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
parameter_list|,
name|boolean
name|outbound
parameter_list|)
block|{
name|this
argument_list|(
name|atts
argument_list|,
name|MULTIPART_RELATED_TYPE
argument_list|,
name|outbound
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultipartBody
parameter_list|(
name|Attachment
name|att
parameter_list|)
block|{
name|atts
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
name|this
operator|.
name|mt
operator|=
name|MULTIPART_RELATED_TYPE
expr_stmt|;
block|}
specifier|public
name|MultipartBody
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
parameter_list|)
block|{
name|this
argument_list|(
name|atts
argument_list|,
name|MULTIPART_RELATED_TYPE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MultipartBody
parameter_list|(
name|boolean
name|outbound
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|,
name|MULTIPART_RELATED_TYPE
argument_list|,
name|outbound
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MediaType
name|getType
parameter_list|()
block|{
return|return
name|mt
return|;
block|}
specifier|public
name|List
argument_list|<
name|Attachment
argument_list|>
name|getAllAttachments
parameter_list|()
block|{
return|return
name|atts
return|;
block|}
specifier|public
name|List
argument_list|<
name|Attachment
argument_list|>
name|getChildAttachments
parameter_list|()
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|childAtts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|atts
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|childAtts
operator|.
name|add
argument_list|(
name|atts
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|childAtts
return|;
block|}
specifier|public
name|Attachment
name|getRootAttachment
parameter_list|()
block|{
return|return
operator|!
name|atts
operator|.
name|isEmpty
argument_list|()
condition|?
name|atts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
specifier|public
name|Attachment
name|getAttachment
parameter_list|(
name|String
name|contentId
parameter_list|)
block|{
for|for
control|(
name|Attachment
name|a
range|:
name|atts
control|)
block|{
if|if
condition|(
name|contentId
operator|.
name|equalsIgnoreCase
argument_list|(
name|a
operator|.
name|getContentId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|a
return|;
block|}
name|ContentDisposition
name|cd
init|=
name|a
operator|.
name|getContentDisposition
argument_list|()
decl_stmt|;
if|if
condition|(
name|cd
operator|!=
literal|null
operator|&&
name|contentId
operator|.
name|equals
argument_list|(
name|cd
operator|.
name|getParameter
argument_list|(
literal|"name"
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|a
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getAttachmentObject
parameter_list|(
name|String
name|contentId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
name|Attachment
name|att
init|=
name|getAttachment
argument_list|(
name|contentId
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|!=
literal|null
condition|)
block|{
return|return
name|att
operator|.
name|getObject
argument_list|(
name|cls
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

