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
name|jaxb
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
name|ByteArrayOutputStream
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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Collection
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
name|activation
operator|.
name|URLDataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|attachment
operator|.
name|AttachmentUnmarshaller
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
name|LazyDataSource
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
name|interceptor
operator|.
name|Fault
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

begin_class
specifier|public
class|class
name|JAXBAttachmentUnmarshaller
extends|extends
name|AttachmentUnmarshaller
block|{
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
name|JAXBAttachmentUnmarshaller
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
decl_stmt|;
specifier|public
name|JAXBAttachmentUnmarshaller
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DataHandler
name|getAttachmentAsDataHandler
parameter_list|(
name|String
name|contentId
parameter_list|)
block|{
return|return
operator|new
name|DataHandler
argument_list|(
name|getAttachmentDataSource
argument_list|(
name|contentId
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getAttachmentAsByteArray
parameter_list|(
name|String
name|contentId
parameter_list|)
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|getAttachmentDataSource
argument_list|(
name|contentId
argument_list|)
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
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
literal|"ATTACHMENT_READ_ERROR"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isXOPPackage
parameter_list|()
block|{
return|return
name|attachments
operator|!=
literal|null
return|;
block|}
specifier|private
name|DataSource
name|getAttachmentDataSource
parameter_list|(
name|String
name|contentId
parameter_list|)
block|{
comment|// Is this right? - DD
if|if
condition|(
name|contentId
operator|.
name|startsWith
argument_list|(
literal|"cid:"
argument_list|)
condition|)
block|{
try|try
block|{
name|contentId
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|contentId
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ue
parameter_list|)
block|{
name|contentId
operator|=
name|contentId
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|LazyDataSource
argument_list|(
name|contentId
argument_list|,
name|attachments
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|contentId
operator|.
name|indexOf
argument_list|(
literal|"://"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
return|return
operator|new
name|LazyDataSource
argument_list|(
name|contentId
argument_list|,
name|attachments
argument_list|)
return|;
block|}
else|else
block|{
try|try
block|{
return|return
operator|new
name|URLDataSource
argument_list|(
operator|new
name|URL
argument_list|(
name|contentId
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

