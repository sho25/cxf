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
name|staxutils
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
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
name|Location
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
name|XMLStreamConstants
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
name|XMLStreamReader
import|;
end_import

begin_class
specifier|public
class|class
name|DepthXMLStreamReader
implements|implements
name|XMLStreamReader
block|{
specifier|protected
name|XMLStreamReader
name|reader
decl_stmt|;
specifier|private
name|int
name|depth
decl_stmt|;
specifier|public
name|DepthXMLStreamReader
parameter_list|(
name|XMLStreamReader
name|r
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|r
expr_stmt|;
block|}
specifier|public
name|XMLStreamReader
name|getReader
parameter_list|()
block|{
return|return
name|this
operator|.
name|reader
return|;
block|}
specifier|public
name|int
name|getDepth
parameter_list|()
block|{
return|return
name|depth
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|int
name|getAttributeCount
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getAttributeCount
argument_list|()
return|;
block|}
specifier|public
name|String
name|getAttributeLocalName
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributeLocalName
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getAttributeName
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributeName
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributeNamespace
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributeNamespace
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributePrefix
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributePrefix
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributeType
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributeType
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributeValue
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributeValue
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributeValue
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|localName
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getAttributeValue
argument_list|(
name|namespace
argument_list|,
name|localName
argument_list|)
return|;
block|}
specifier|public
name|String
name|getCharacterEncodingScheme
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getCharacterEncodingScheme
argument_list|()
return|;
block|}
specifier|public
name|String
name|getElementText
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|String
name|ret
init|=
name|reader
operator|.
name|getElementText
argument_list|()
decl_stmt|;
comment|//workaround bugs in some readers that aren't properly advancing to
comment|//the END_ELEMENT (*cough*jettison*cough*)
while|while
condition|(
name|reader
operator|.
name|getEventType
argument_list|()
operator|!=
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
name|reader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|depth
operator|--
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getEncoding
argument_list|()
return|;
block|}
specifier|public
name|int
name|getEventType
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getEventType
argument_list|()
return|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getLocalName
argument_list|()
return|;
block|}
specifier|public
name|Location
name|getLocation
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getLocation
argument_list|()
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getNamespaceContext
argument_list|()
return|;
block|}
specifier|public
name|int
name|getNamespaceCount
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getNamespaceCount
argument_list|()
return|;
block|}
specifier|public
name|String
name|getNamespacePrefix
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getNamespacePrefix
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPIData
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getPIData
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPITarget
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getPITarget
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getPrefix
argument_list|()
return|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|reader
operator|.
name|getProperty
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getText
argument_list|()
return|;
block|}
specifier|public
name|char
index|[]
name|getTextCharacters
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getTextCharacters
argument_list|()
return|;
block|}
specifier|public
name|int
name|getTextCharacters
parameter_list|(
name|int
name|arg0
parameter_list|,
name|char
index|[]
name|arg1
parameter_list|,
name|int
name|arg2
parameter_list|,
name|int
name|arg3
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|reader
operator|.
name|getTextCharacters
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|,
name|arg3
argument_list|)
return|;
block|}
specifier|public
name|int
name|getTextLength
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getTextLength
argument_list|()
return|;
block|}
specifier|public
name|int
name|getTextStart
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getTextStart
argument_list|()
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|reader
operator|.
name|getVersion
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasName
parameter_list|()
block|{
return|return
name|reader
operator|.
name|hasName
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
throws|throws
name|XMLStreamException
block|{
return|return
name|reader
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|hasText
parameter_list|()
block|{
return|return
name|reader
operator|.
name|hasText
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isAttributeSpecified
parameter_list|(
name|int
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|isAttributeSpecified
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isCharacters
parameter_list|()
block|{
return|return
name|reader
operator|.
name|isCharacters
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isEndElement
parameter_list|()
block|{
return|return
name|reader
operator|.
name|isEndElement
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isStandalone
parameter_list|()
block|{
return|return
name|reader
operator|.
name|isStandalone
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isStartElement
parameter_list|()
block|{
return|return
name|reader
operator|.
name|isStartElement
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isWhiteSpace
parameter_list|()
block|{
return|return
name|reader
operator|.
name|isWhiteSpace
argument_list|()
return|;
block|}
specifier|public
name|int
name|next
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|int
name|next
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|==
name|START_ELEMENT
condition|)
block|{
name|depth
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|==
name|END_ELEMENT
condition|)
block|{
name|depth
operator|--
expr_stmt|;
block|}
return|return
name|next
return|;
block|}
specifier|public
name|int
name|nextTag
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|int
name|eventType
init|=
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|eventType
operator|==
name|XMLStreamConstants
operator|.
name|CHARACTERS
operator|&&
name|isWhiteSpace
argument_list|()
operator|)
operator|||
operator|(
name|eventType
operator|==
name|XMLStreamConstants
operator|.
name|CDATA
operator|&&
name|isWhiteSpace
argument_list|()
operator|)
comment|// skip whitespace
operator|||
name|eventType
operator|==
name|XMLStreamConstants
operator|.
name|SPACE
operator|||
name|eventType
operator|==
name|XMLStreamConstants
operator|.
name|PROCESSING_INSTRUCTION
operator|||
name|eventType
operator|==
name|XMLStreamConstants
operator|.
name|COMMENT
condition|)
block|{
name|eventType
operator|=
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|eventType
operator|!=
name|XMLStreamConstants
operator|.
name|START_ELEMENT
operator|&&
name|eventType
operator|!=
name|XMLStreamConstants
operator|.
name|END_ELEMENT
condition|)
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"expected start or end tag"
argument_list|,
name|getLocation
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|eventType
return|;
block|}
specifier|public
name|void
name|require
parameter_list|(
name|int
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|reader
operator|.
name|require
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|standaloneSet
parameter_list|()
block|{
return|return
name|reader
operator|.
name|standaloneSet
argument_list|()
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|reader
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|arg0
parameter_list|)
block|{
return|return
name|reader
operator|.
name|equals
argument_list|(
name|arg0
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|reader
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

