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
name|policy
operator|.
name|model
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
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|SP12Constants
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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|PolicyComponent
import|;
end_import

begin_class
specifier|public
class|class
name|SignedEncryptedParts
extends|extends
name|AbstractSecurityAssertion
block|{
specifier|private
name|boolean
name|body
decl_stmt|;
specifier|private
name|boolean
name|attachments
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
operator|new
name|ArrayList
argument_list|<
name|Header
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|signedParts
decl_stmt|;
specifier|public
name|SignedEncryptedParts
parameter_list|(
name|boolean
name|signedParts
parameter_list|,
name|SPConstants
name|version
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|this
operator|.
name|signedParts
operator|=
name|signedParts
expr_stmt|;
block|}
comment|/**      * @return Returns the body.      */
specifier|public
name|boolean
name|isBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
comment|/**      * @param body The body to set.      */
specifier|public
name|void
name|setBody
parameter_list|(
name|boolean
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
comment|/**      * @return Returns the attachments.      */
specifier|public
name|boolean
name|isAttachments
parameter_list|()
block|{
return|return
name|attachments
return|;
block|}
comment|/**      * @param attachments The attachments to set.      */
specifier|public
name|void
name|setAttachments
parameter_list|(
name|boolean
name|attachments
parameter_list|)
block|{
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
comment|/**      * @return Returns the headers.      */
specifier|public
name|List
argument_list|<
name|Header
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|this
operator|.
name|headers
return|;
block|}
comment|/**      * @param headers The headers to set.      */
specifier|public
name|void
name|addHeader
parameter_list|(
name|Header
name|header
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|.
name|add
argument_list|(
name|header
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the signedParts.      */
specifier|public
name|boolean
name|isSignedParts
parameter_list|()
block|{
return|return
name|signedParts
return|;
block|}
specifier|public
name|QName
name|getRealName
parameter_list|()
block|{
if|if
condition|(
name|signedParts
condition|)
block|{
return|return
name|constants
operator|.
name|getSignedParts
argument_list|()
return|;
block|}
return|return
name|constants
operator|.
name|getEncryptedParts
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
if|if
condition|(
name|signedParts
condition|)
block|{
return|return
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getSignedParts
argument_list|()
return|;
block|}
return|return
name|SP12Constants
operator|.
name|INSTANCE
operator|.
name|getEncryptedParts
argument_list|()
return|;
block|}
specifier|public
name|PolicyComponent
name|normalize
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|public
name|void
name|serialize
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|localName
init|=
name|getRealName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|namespaceURI
init|=
name|getRealName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|writer
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
name|getRealName
argument_list|()
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|writer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
comment|//<sp:SignedParts> |<sp:EncryptedParts>
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
comment|// xmlns:sp=".."
name|writer
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
if|if
condition|(
name|isBody
argument_list|()
condition|)
block|{
comment|//<sp:Body />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|BODY
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
name|Header
name|header
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|headers
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
name|header
operator|=
operator|(
name|Header
operator|)
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
comment|//<sp:Header Name=".." Namespace=".." />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|HEADER
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
comment|// Name attribute is optional
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Name"
argument_list|,
name|header
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|writeAttribute
argument_list|(
literal|"Namespace"
argument_list|,
name|header
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isAttachments
argument_list|()
operator|&&
name|constants
operator|.
name|getVersion
argument_list|()
operator|==
name|SPConstants
operator|.
name|Version
operator|.
name|SP_V12
condition|)
block|{
comment|//<sp:Attachments />
name|writer
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|SPConstants
operator|.
name|ATTACHMENTS
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
comment|//</sp:SignedParts> |</sp:EncryptedParts>
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

