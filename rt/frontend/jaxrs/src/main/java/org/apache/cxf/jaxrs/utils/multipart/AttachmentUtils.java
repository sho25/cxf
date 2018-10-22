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
name|utils
operator|.
name|multipart
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
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
name|AttachmentDeserializer
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
name|i18n
operator|.
name|BundleUtils
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
name|logging
operator|.
name|LogUtils
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
name|CastUtils
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
name|ext
operator|.
name|MessageContext
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
name|ext
operator|.
name|multipart
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|ContentDisposition
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
name|ext
operator|.
name|multipart
operator|.
name|Multipart
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
name|ext
operator|.
name|multipart
operator|.
name|MultipartBody
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
name|ext
operator|.
name|multipart
operator|.
name|MultipartInputFilter
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
name|ext
operator|.
name|multipart
operator|.
name|MultipartOutputFilter
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
name|impl
operator|.
name|MetadataMap
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
name|ExceptionUtils
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
name|FormUtils
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|phase
operator|.
name|PhaseInterceptorChain
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|AttachmentUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|OUT_FILTERS
init|=
literal|"multipart.output.filters"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_FILTERS
init|=
literal|"multipart.input.filters"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JAXRSUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|JAXRSUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AttachmentUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|addMultipartOutFilter
parameter_list|(
name|MultipartOutputFilter
name|filter
parameter_list|)
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MultipartOutputFilter
argument_list|>
name|outFilters
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|OUT_FILTERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|outFilters
operator|==
literal|null
condition|)
block|{
name|outFilters
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|OUT_FILTERS
argument_list|,
name|outFilters
argument_list|)
expr_stmt|;
block|}
name|outFilters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|addMultipartInFilter
parameter_list|(
name|MultipartInputFilter
name|filter
parameter_list|)
block|{
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MultipartInputFilter
argument_list|>
name|inFilters
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|IN_FILTERS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|inFilters
operator|==
literal|null
condition|)
block|{
name|inFilters
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|IN_FILTERS
argument_list|,
name|inFilters
argument_list|)
expr_stmt|;
block|}
name|inFilters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|MultipartBody
name|getMultipartBody
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
operator|(
name|MultipartBody
operator|)
name|mc
operator|.
name|get
argument_list|(
name|MultipartBody
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|getChildAttachmentsMap
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|boolean
name|preferContentDisposition
parameter_list|)
block|{
return|return
name|fromListToMap
argument_list|(
name|getChildAttachments
argument_list|(
name|mc
argument_list|)
argument_list|,
name|preferContentDisposition
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|getChildAttachmentsMap
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|fromListToMap
argument_list|(
name|getChildAttachments
argument_list|(
name|mc
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Attachment
argument_list|>
name|getChildAttachments
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|getMultipartBody
argument_list|(
name|mc
argument_list|)
operator|.
name|getChildAttachments
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|getAttachmentsMap
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|boolean
name|preferContentDisposition
parameter_list|)
block|{
return|return
name|fromListToMap
argument_list|(
name|getAttachments
argument_list|(
name|mc
argument_list|)
argument_list|,
name|preferContentDisposition
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|getAttachmentsMap
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|fromListToMap
argument_list|(
name|getAttachments
argument_list|(
name|mc
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Attachment
argument_list|>
name|getAttachments
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|getMultipartBody
argument_list|(
name|mc
argument_list|)
operator|.
name|getAllAttachments
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Attachment
name|getFirstMatchingPart
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|Multipart
name|id
parameter_list|)
block|{
return|return
name|getFirstMatchingPart
argument_list|(
name|mc
argument_list|,
name|id
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Attachment
name|getFirstMatchingPart
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|id
parameter_list|)
block|{
return|return
name|getFirstMatchingPart
argument_list|(
name|mc
argument_list|,
name|id
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Attachment
name|getFirstMatchingPart
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|mediaType
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|all
init|=
name|getAttachments
argument_list|(
name|mc
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Attachment
argument_list|>
name|matching
init|=
name|getMatchingAttachments
argument_list|(
name|id
argument_list|,
name|mediaType
argument_list|,
name|all
argument_list|)
decl_stmt|;
return|return
name|matching
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|matching
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|MultipartBody
name|getMultipartBody
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|attachmentDir
parameter_list|,
name|String
name|attachmentThreshold
parameter_list|,
name|String
name|attachmentMaxSize
parameter_list|)
block|{
if|if
condition|(
name|attachmentDir
operator|!=
literal|null
condition|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_DIRECTORY
argument_list|,
name|attachmentDir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attachmentThreshold
operator|!=
literal|null
condition|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MEMORY_THRESHOLD
argument_list|,
name|attachmentThreshold
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attachmentMaxSize
operator|!=
literal|null
condition|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|AttachmentDeserializer
operator|.
name|ATTACHMENT_MAX_SIZE
argument_list|,
name|attachmentMaxSize
argument_list|)
expr_stmt|;
block|}
name|boolean
name|embeddedAttachment
init|=
name|mc
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.multipart.embedded"
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|String
name|propertyName
init|=
name|embeddedAttachment
condition|?
name|MultipartBody
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
operator|+
literal|".embedded"
else|:
name|MultipartBody
operator|.
name|INBOUND_MESSAGE_ATTACHMENTS
decl_stmt|;
name|MultipartBody
name|body
init|=
operator|(
name|MultipartBody
operator|)
name|mc
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|embeddedAttachment
operator|&&
name|mc
operator|.
name|get
argument_list|(
name|IN_FILTERS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|MultipartInputFilter
argument_list|>
name|filters
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|mc
operator|.
name|get
argument_list|(
name|IN_FILTERS
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|MultipartInputFilter
name|filter
range|:
name|filters
control|)
block|{
name|filter
operator|.
name|filter
argument_list|(
name|body
operator|.
name|getAllAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|body
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Attachment
argument_list|>
name|getAttachments
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|attachmentDir
parameter_list|,
name|String
name|attachmentThreshold
parameter_list|,
name|String
name|attachmentMaxSize
parameter_list|)
block|{
return|return
name|getMultipartBody
argument_list|(
name|mc
argument_list|,
name|attachmentDir
argument_list|,
name|attachmentThreshold
argument_list|,
name|attachmentMaxSize
argument_list|)
operator|.
name|getAllAttachments
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Attachment
name|getMultipart
parameter_list|(
name|Multipart
name|id
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|infos
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Attachment
name|a
range|:
name|infos
control|)
block|{
if|if
condition|(
name|matchAttachmentId
argument_list|(
name|a
argument_list|,
name|id
argument_list|)
condition|)
block|{
name|checkMediaTypes
argument_list|(
name|a
operator|.
name|getContentType
argument_list|()
argument_list|,
name|id
operator|.
name|type
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|a
return|;
block|}
block|}
if|if
condition|(
name|id
operator|.
name|required
argument_list|()
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|errorMsg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"MULTTIPART_ID_NOT_FOUND"
argument_list|,
name|BUNDLE
argument_list|,
name|id
operator|.
name|value
argument_list|()
argument_list|,
name|mt
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
operator|new
name|MultipartReadException
argument_list|(
name|id
operator|.
name|value
argument_list|()
argument_list|,
name|id
operator|.
name|type
argument_list|()
argument_list|,
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
throw|;
block|}
return|return
literal|null
return|;
block|}
return|return
operator|!
name|infos
operator|.
name|isEmpty
argument_list|()
condition|?
name|infos
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
specifier|static
name|List
argument_list|<
name|Attachment
argument_list|>
name|getMatchingAttachments
parameter_list|(
name|Multipart
name|id
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|infos
parameter_list|)
block|{
return|return
name|getMatchingAttachments
argument_list|(
name|id
operator|.
name|value
argument_list|()
argument_list|,
name|id
operator|.
name|type
argument_list|()
argument_list|,
name|infos
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Attachment
argument_list|>
name|getMatchingAttachments
parameter_list|(
name|String
name|id
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|infos
parameter_list|)
block|{
return|return
name|getMatchingAttachments
argument_list|(
name|id
argument_list|,
literal|null
argument_list|,
name|infos
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Attachment
argument_list|>
name|getMatchingAttachments
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|mediaType
parameter_list|,
name|List
argument_list|<
name|Attachment
argument_list|>
name|infos
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|all
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|infos
control|)
block|{
if|if
condition|(
name|matchAttachmentId
argument_list|(
name|a
argument_list|,
name|id
argument_list|)
condition|)
block|{
if|if
condition|(
name|mediaType
operator|!=
literal|null
condition|)
block|{
name|checkMediaTypes
argument_list|(
name|a
operator|.
name|getContentType
argument_list|()
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
block|}
name|all
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|all
return|;
block|}
specifier|public
specifier|static
name|boolean
name|matchAttachmentId
parameter_list|(
name|Attachment
name|at
parameter_list|,
name|Multipart
name|mid
parameter_list|)
block|{
return|return
name|matchAttachmentId
argument_list|(
name|at
argument_list|,
name|mid
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|matchAttachmentId
parameter_list|(
name|Attachment
name|at
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|at
operator|.
name|getContentId
argument_list|()
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|ContentDisposition
name|cd
init|=
name|at
operator|.
name|getContentDisposition
argument_list|()
decl_stmt|;
return|return
name|cd
operator|!=
literal|null
operator|&&
name|value
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
return|;
block|}
specifier|public
specifier|static
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|populateFormMap
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|boolean
name|errorIfMissing
parameter_list|)
block|{
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|FormUtils
operator|.
name|populateMapFromMultipart
argument_list|(
name|data
argument_list|,
name|AttachmentUtils
operator|.
name|getMultipartBody
argument_list|(
name|mc
argument_list|)
argument_list|,
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
specifier|public
specifier|static
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|populateFormMap
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|populateFormMap
argument_list|(
name|mc
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|fromListToMap
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|atts
parameter_list|,
name|boolean
name|preferContentDisposition
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Attachment
name|a
range|:
name|atts
control|)
block|{
name|String
name|contentId
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|preferContentDisposition
condition|)
block|{
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
condition|)
block|{
name|contentId
operator|=
name|cd
operator|.
name|getParameter
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|contentId
operator|==
literal|null
condition|)
block|{
name|contentId
operator|=
name|a
operator|.
name|getContentId
argument_list|()
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|contentId
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
specifier|private
specifier|static
name|void
name|checkMediaTypes
parameter_list|(
name|MediaType
name|mt1
parameter_list|,
name|String
name|mt2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|mt1
operator|.
name|isCompatible
argument_list|(
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|mt2
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotSupportedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

