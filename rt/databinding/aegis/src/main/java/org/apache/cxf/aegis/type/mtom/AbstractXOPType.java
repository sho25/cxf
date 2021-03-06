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
name|aegis
operator|.
name|type
operator|.
name|mtom
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
name|HashMap
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
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Attr
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|DatabindingException
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|aegis
operator|.
name|type
operator|.
name|basic
operator|.
name|Base64Type
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
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
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
name|DOMUtils
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
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|XmlSchemaElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|constants
operator|.
name|Constants
import|;
end_import

begin_comment
comment|/**  * Base class for MtoM types.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXOPType
extends|extends
name|AegisType
block|{
specifier|public
specifier|static
specifier|final
name|String
name|XOP_NS
init|=
literal|"http://www.w3.org/2004/08/xop/include"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML_MIME_NS
init|=
literal|"http://www.w3.org/2005/05/xmlmime"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XML_MIME_ATTR_LOCAL_NAME
init|=
literal|"expectedContentTypes"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XOP_INCLUDE
init|=
operator|new
name|QName
argument_list|(
name|XOP_NS
argument_list|,
literal|"Include"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XML_MIME_CONTENT_TYPE
init|=
operator|new
name|QName
argument_list|(
name|XML_MIME_NS
argument_list|,
literal|"contentType"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XOP_HREF
init|=
operator|new
name|QName
argument_list|(
literal|"href"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XML_MIME_BASE64
init|=
operator|new
name|QName
argument_list|(
name|XML_MIME_NS
argument_list|,
literal|"base64Binary"
argument_list|,
literal|"xmime"
argument_list|)
decl_stmt|;
specifier|private
name|String
name|expectedContentTypes
decl_stmt|;
comment|// the base64 type knows how to deal with just plain base64 here, which is essentially always
comment|// what we get in the absence of the optimization. So we need something of a coroutine.
specifier|private
name|Base64Type
name|fallbackDelegate
decl_stmt|;
specifier|private
name|boolean
name|useXmimeBinaryType
decl_stmt|;
comment|/**      * Create an XOP type. This type will use xmime to publish and receive the content type      * via xmime:base64Binary if useXmimeBinaryType is true. If expectedContentTypes != null, then      * it will use xmime to advertise expected content types.      * @param useXmimeBinaryType whether to use xmime:base64Binary.      * @param expectedContentTypes whether to public xmime:expectedContentTypes.      */
specifier|public
name|AbstractXOPType
parameter_list|(
name|boolean
name|useXmimeBinaryType
parameter_list|,
name|String
name|expectedContentTypes
parameter_list|)
block|{
name|this
operator|.
name|expectedContentTypes
operator|=
name|expectedContentTypes
expr_stmt|;
name|this
operator|.
name|useXmimeBinaryType
operator|=
name|useXmimeBinaryType
expr_stmt|;
name|fallbackDelegate
operator|=
operator|new
name|Base64Type
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|useXmimeBinaryType
condition|)
block|{
comment|//      we use the XMIME type instead of the XSD type to allow for our content type attribute.
name|setSchemaType
argument_list|(
name|XML_MIME_BASE64
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setSchemaType
argument_list|(
name|Constants
operator|.
name|XSD_BASE64
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This is called from base64Type when it recognizes an XOP attachment.      * @param reader      * @param context      * @return      * @throws DatabindingException      */
specifier|public
name|Object
name|readMtoM
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|Object
name|o
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|hasMoreElementReaders
argument_list|()
condition|)
block|{
name|MessageReader
name|child
init|=
name|reader
operator|.
name|getNextElementReader
argument_list|()
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|XOP_INCLUDE
argument_list|)
condition|)
block|{
name|MessageReader
name|mimeReader
init|=
name|child
operator|.
name|getAttributeReader
argument_list|(
name|XOP_HREF
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|mimeReader
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|o
operator|=
name|readInclude
argument_list|(
name|type
argument_list|,
name|child
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
name|child
operator|.
name|readToEnd
argument_list|()
expr_stmt|;
block|}
return|return
name|o
return|;
block|}
comment|/**      * This defers to the plain base64 type, which calls back into here above for XOP.      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|XMLStreamReader
name|xreader
init|=
name|reader
operator|.
name|getXMLStreamReader
argument_list|()
decl_stmt|;
name|String
name|contentType
init|=
name|xreader
operator|.
name|getAttributeValue
argument_list|(
name|AbstractXOPType
operator|.
name|XML_MIME_NS
argument_list|,
name|AbstractXOPType
operator|.
name|XML_MIME_CONTENT_TYPE
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|thingRead
init|=
name|fallbackDelegate
operator|.
name|readObject
argument_list|(
name|reader
argument_list|,
name|context
argument_list|)
decl_stmt|;
comment|// If there was actually an attachment, the delegate will have called back to us and gotten
comment|// the appropriate data type. If there wasn't an attachment, it just returned the bytes.
comment|// Our subclass have to package them.
if|if
condition|(
name|thingRead
operator|.
name|getClass
argument_list|()
operator|==
operator|(
operator|new
name|byte
index|[
literal|0
index|]
operator|)
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
name|wrapBytes
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|thingRead
argument_list|,
name|contentType
argument_list|)
return|;
block|}
return|return
name|thingRead
return|;
block|}
specifier|private
name|Object
name|readInclude
parameter_list|(
name|String
name|type
parameter_list|,
name|MessageReader
name|reader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|String
name|href
init|=
name|reader
operator|.
name|getAttributeReader
argument_list|(
name|XOP_HREF
argument_list|)
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|Attachment
name|att
init|=
name|AttachmentUtil
operator|.
name|getAttachment
argument_list|(
name|href
argument_list|,
name|context
operator|.
name|getAttachments
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|att
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not find the attachment "
operator|+
name|href
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|readAttachment
argument_list|(
name|att
argument_list|,
name|context
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not read attachment"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
specifier|abstract
name|Object
name|readAttachment
parameter_list|(
name|Attachment
name|att
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|IOException
function_decl|;
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
comment|// add the content type attribute even if we are going to fall back.
name|String
name|contentType
init|=
name|getContentType
argument_list|(
name|object
argument_list|,
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|useXmimeBinaryType
condition|)
block|{
name|MessageWriter
name|ctWriter
init|=
name|writer
operator|.
name|getAttributeWriter
argument_list|(
name|XML_MIME_CONTENT_TYPE
argument_list|)
decl_stmt|;
name|ctWriter
operator|.
name|writeValue
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|context
operator|.
name|isMtomEnabled
argument_list|()
condition|)
block|{
name|fallbackDelegate
operator|.
name|writeObject
argument_list|(
name|getBytes
argument_list|(
name|object
argument_list|)
argument_list|,
name|writer
argument_list|,
name|context
argument_list|)
expr_stmt|;
return|return;
block|}
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
init|=
name|context
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
name|attachments
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|context
operator|.
name|setAttachments
argument_list|(
name|attachments
argument_list|)
expr_stmt|;
block|}
name|String
name|id
init|=
name|AttachmentUtil
operator|.
name|createContentID
argument_list|(
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
name|Attachment
name|att
init|=
name|createAttachment
argument_list|(
name|object
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|attachments
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
name|MessageWriter
name|include
init|=
name|writer
operator|.
name|getElementWriter
argument_list|(
name|XOP_INCLUDE
argument_list|)
decl_stmt|;
name|MessageWriter
name|href
init|=
name|include
operator|.
name|getAttributeWriter
argument_list|(
name|XOP_HREF
argument_list|)
decl_stmt|;
name|href
operator|.
name|writeValue
argument_list|(
literal|"cid:"
operator|+
name|id
argument_list|)
expr_stmt|;
name|include
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|Attachment
name|createAttachment
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|id
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|String
name|getContentType
parameter_list|(
name|Object
name|object
parameter_list|,
name|Context
name|context
parameter_list|)
function_decl|;
comment|/**      * If one of these types arrives unoptimized, we need to convert it to the      * desired return type.      * @param bareBytes the bytes pulled out of the base64.      * @param contentType when we support xmime:contentType, this will be passed along.      * @return      */
specifier|protected
specifier|abstract
name|Object
name|wrapBytes
parameter_list|(
name|byte
index|[]
name|bareBytes
parameter_list|,
name|String
name|contentType
parameter_list|)
function_decl|;
comment|/**      * if MtoM is not enabled, we need bytes to turn into base64.      * @return      */
specifier|protected
specifier|abstract
name|byte
index|[]
name|getBytes
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|void
name|addToSchemaElement
parameter_list|(
name|XmlSchemaElement
name|schemaElement
parameter_list|)
block|{
if|if
condition|(
name|expectedContentTypes
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Node
argument_list|>
name|extAttrMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Attr
name|theAttr
init|=
name|DOMUtils
operator|.
name|getEmptyDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
name|XML_MIME_NS
argument_list|,
literal|"xmime"
argument_list|)
decl_stmt|;
name|theAttr
operator|.
name|setNodeValue
argument_list|(
name|expectedContentTypes
argument_list|)
expr_stmt|;
name|extAttrMap
operator|.
name|put
argument_list|(
literal|"xmime"
argument_list|,
name|theAttr
argument_list|)
expr_stmt|;
name|schemaElement
operator|.
name|addMetaInfo
argument_list|(
name|Constants
operator|.
name|MetaDataConstants
operator|.
name|EXTERNAL_ATTRIBUTES
argument_list|,
name|extAttrMap
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|usesXmime
parameter_list|()
block|{
return|return
name|useXmimeBinaryType
operator|||
name|expectedContentTypes
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

