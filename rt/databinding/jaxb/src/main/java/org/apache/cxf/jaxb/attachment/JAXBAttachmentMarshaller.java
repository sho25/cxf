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
name|UUID
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
name|xml
operator|.
name|bind
operator|.
name|attachment
operator|.
name|AttachmentMarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|AttachmentImpl
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
name|AttachmentUtil
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
name|JAXBAttachmentMarshaller
extends|extends
name|AttachmentMarshaller
block|{
specifier|private
name|int
name|threshold
init|=
literal|5
operator|*
literal|1024
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
decl_stmt|;
specifier|private
name|boolean
name|isXop
decl_stmt|;
specifier|private
name|QName
name|lastElementName
decl_stmt|;
specifier|public
name|JAXBAttachmentMarshaller
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|,
name|Integer
name|mtomThreshold
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
if|if
condition|(
name|mtomThreshold
operator|!=
literal|null
condition|)
block|{
name|threshold
operator|=
name|mtomThreshold
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
name|atts
operator|=
name|attachments
expr_stmt|;
name|isXop
operator|=
name|attachments
operator|!=
literal|null
expr_stmt|;
block|}
specifier|public
name|QName
name|getLastMTOMElementName
parameter_list|()
block|{
return|return
name|lastElementName
return|;
block|}
specifier|public
name|String
name|addMtomAttachment
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|,
name|String
name|mimeType
parameter_list|,
name|String
name|elementNS
parameter_list|,
name|String
name|elementLocalName
parameter_list|)
block|{
name|Attachment
name|att
init|=
name|AttachmentUtil
operator|.
name|createMtomAttachment
argument_list|(
name|isXop
argument_list|,
name|mimeType
argument_list|,
name|elementNS
argument_list|,
name|data
argument_list|,
name|offset
argument_list|,
name|length
argument_list|,
name|threshold
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|!=
literal|null
condition|)
block|{
name|atts
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
name|lastElementName
operator|=
operator|new
name|QName
argument_list|(
name|elementNS
argument_list|,
name|elementLocalName
argument_list|)
expr_stmt|;
return|return
literal|"cid:"
operator|+
name|att
operator|.
name|getId
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|String
name|addMtomAttachment
parameter_list|(
name|DataHandler
name|handler
parameter_list|,
name|String
name|elementNS
parameter_list|,
name|String
name|elementLocalName
parameter_list|)
block|{
name|Attachment
name|att
init|=
name|AttachmentUtil
operator|.
name|createMtomAttachmentFromDH
argument_list|(
name|isXop
argument_list|,
name|handler
argument_list|,
name|elementNS
argument_list|,
name|threshold
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|!=
literal|null
condition|)
block|{
name|atts
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
name|lastElementName
operator|=
operator|new
name|QName
argument_list|(
name|elementNS
argument_list|,
name|elementLocalName
argument_list|)
expr_stmt|;
return|return
literal|"cid:"
operator|+
name|att
operator|.
name|getId
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|addSwaRefAttachment
parameter_list|(
name|DataHandler
name|handler
parameter_list|)
block|{
name|String
name|id
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|+
literal|"@apache.org"
decl_stmt|;
name|AttachmentImpl
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|id
argument_list|,
name|handler
argument_list|)
decl_stmt|;
name|att
operator|.
name|setXOP
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|atts
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setXOPPackage
parameter_list|(
name|boolean
name|xop
parameter_list|)
block|{
name|this
operator|.
name|isXop
operator|=
name|xop
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isXOPPackage
parameter_list|()
block|{
return|return
name|isXop
return|;
block|}
block|}
end_class

end_unit

