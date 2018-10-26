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
name|jaxrs
operator|.
name|impl
operator|.
name|MetadataMap
import|;
end_import

begin_comment
comment|/**  * Fluid builder class for {@link Attachment} objects.  */
end_comment

begin_class
specifier|public
class|class
name|AttachmentBuilder
block|{
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
argument_list|<>
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
name|DataHandler
name|dataHandler
decl_stmt|;
specifier|private
name|ContentDisposition
name|contentDisposition
decl_stmt|;
specifier|public
name|AttachmentBuilder
parameter_list|()
block|{
comment|//
block|}
specifier|public
name|AttachmentBuilder
name|id
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|AttachmentBuilder
name|mediaType
parameter_list|(
name|String
name|mediaType
parameter_list|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
name|mediaType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|AttachmentBuilder
name|object
parameter_list|(
name|Object
name|theObject
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|theObject
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|AttachmentBuilder
name|dataHandler
parameter_list|(
name|DataHandler
name|newDataHandler
parameter_list|)
block|{
name|this
operator|.
name|dataHandler
operator|=
name|newDataHandler
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|AttachmentBuilder
name|header
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set all of the headers. This will overwrite any content ID,      * media type, ContentDisposition, or other header set by previous calls.      * @param allHeaders      * @return      */
specifier|public
name|AttachmentBuilder
name|headers
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|allHeaders
parameter_list|)
block|{
name|headers
operator|=
name|allHeaders
expr_stmt|;
name|contentDisposition
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|AttachmentBuilder
name|contentDisposition
parameter_list|(
name|ContentDisposition
name|newContentDisposition
parameter_list|)
block|{
name|this
operator|.
name|contentDisposition
operator|=
name|newContentDisposition
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Attachment
name|build
parameter_list|()
block|{
if|if
condition|(
name|contentDisposition
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
name|contentDisposition
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Attachment
argument_list|(
name|headers
argument_list|,
name|dataHandler
argument_list|,
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

