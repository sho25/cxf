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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|MessageBodyReader
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
name|ext
operator|.
name|Providers
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
name|Transferable
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
name|JAXRSUtils
import|;
end_import

begin_comment
comment|/**  * This class represents an attachment; generally a multipart part.  * Some constructors in here are intended only for  * internal use in CXF, others are suitable or preparing  * attachments to pass to the {@link org.apache.cxf.jaxrs.client.WebClient} API.  * See the {@link AttachmentBuilder} for a convenient  * way to create attachments for use with {@link org.apache.cxf.jaxrs.client.WebClient}.  */
end_comment

begin_class
specifier|public
class|class
name|Attachment
implements|implements
name|Transferable
block|{
specifier|private
name|DataHandler
name|handler
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
specifier|private
name|Object
name|object
decl_stmt|;
specifier|private
name|Providers
name|providers
decl_stmt|;
specifier|public
name|Attachment
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
name|a
parameter_list|,
name|Providers
name|providers
parameter_list|)
block|{
name|handler
operator|=
name|a
operator|.
name|getDataHandler
argument_list|()
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|String
argument_list|>
name|i
init|=
name|a
operator|.
name|getHeaderNames
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|name
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"Content-ID"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|headers
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|a
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-ID"
argument_list|,
name|a
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|providers
operator|=
name|providers
expr_stmt|;
block|}
specifier|public
name|Attachment
parameter_list|(
name|String
name|id
parameter_list|,
name|DataHandler
name|dh
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|handler
operator|=
name|dh
expr_stmt|;
name|this
operator|.
name|headers
operator|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|headers
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-ID"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Attachment
parameter_list|(
name|String
name|id
parameter_list|,
name|DataSource
name|ds
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Attachment
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
block|}
specifier|public
name|Attachment
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|this
argument_list|(
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Content-ID"
argument_list|)
argument_list|,
operator|new
name|DataHandler
argument_list|(
operator|new
name|InputStreamDataSource
argument_list|(
name|is
argument_list|,
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Attachment
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|mediaType
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-ID"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Attachment
parameter_list|(
name|String
name|id
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|ContentDisposition
name|cd
parameter_list|)
block|{
name|handler
operator|=
operator|new
name|DataHandler
argument_list|(
operator|new
name|InputStreamDataSource
argument_list|(
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|cd
operator|!=
literal|null
condition|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Disposition"
argument_list|,
name|cd
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-ID"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
block|}
name|Attachment
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|DataHandler
name|handler
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
block|}
specifier|public
name|ContentDisposition
name|getContentDisposition
parameter_list|()
block|{
name|String
name|header
init|=
name|getHeader
argument_list|(
literal|"Content-Disposition"
argument_list|)
decl_stmt|;
return|return
name|header
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|ContentDisposition
argument_list|(
name|header
argument_list|)
return|;
block|}
specifier|public
name|String
name|getContentId
parameter_list|()
block|{
return|return
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Content-ID"
argument_list|)
return|;
block|}
specifier|public
name|MediaType
name|getContentType
parameter_list|()
block|{
name|String
name|value
init|=
name|handler
operator|!=
literal|null
condition|?
name|handler
operator|.
name|getContentType
argument_list|()
else|:
name|headers
operator|.
name|getFirst
argument_list|(
literal|"Content-Type"
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
name|MediaType
operator|.
name|TEXT_PLAIN_TYPE
else|:
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
name|DataHandler
name|getDataHandler
parameter_list|()
block|{
return|return
name|handler
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getObject
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|providers
operator|!=
literal|null
condition|)
block|{
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
name|mbr
init|=
name|providers
operator|.
name|getMessageBodyReader
argument_list|(
name|cls
argument_list|,
name|cls
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|getContentType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mbr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|mbr
operator|.
name|readFrom
argument_list|(
name|cls
argument_list|,
name|cls
argument_list|,
operator|new
name|Annotation
index|[]
block|{}
argument_list|,
name|getContentType
argument_list|()
argument_list|,
name|headers
argument_list|,
name|getDataHandler
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|header
init|=
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
operator|||
name|header
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
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
name|header
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
name|header
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
name|header
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
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getHeaderAsList
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|headers
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
name|void
name|transferTo
parameter_list|(
name|File
name|destinationFile
parameter_list|)
throws|throws
name|IOException
block|{
name|IOUtils
operator|.
name|transferTo
argument_list|(
name|handler
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|destinationFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|headers
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Attachment
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Attachment
name|other
init|=
operator|(
name|Attachment
operator|)
name|o
decl_stmt|;
return|return
name|headers
operator|.
name|equals
argument_list|(
name|other
operator|.
name|headers
argument_list|)
return|;
block|}
block|}
end_class

end_unit

