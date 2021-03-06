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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|DTDHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|EntityResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXNotRecognizedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXNotSupportedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ext
operator|.
name|LexicalHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|AttributesImpl
import|;
end_import

begin_class
specifier|public
class|class
name|StaxSource
extends|extends
name|SAXSource
implements|implements
name|XMLReader
block|{
specifier|private
name|XMLStreamReader
name|streamReader
decl_stmt|;
specifier|private
name|ContentHandler
name|contentHandler
decl_stmt|;
specifier|private
name|LexicalHandler
name|lexicalHandler
decl_stmt|;
specifier|public
name|StaxSource
parameter_list|(
name|XMLStreamReader
name|streamReader
parameter_list|)
block|{
name|this
operator|.
name|streamReader
operator|=
name|streamReader
expr_stmt|;
name|setInputSource
argument_list|(
operator|new
name|InputSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XMLReader
name|getXMLReader
parameter_list|()
block|{
return|return
name|this
return|;
block|}
specifier|public
name|XMLStreamReader
name|getXMLStreamReader
parameter_list|()
block|{
return|return
name|streamReader
return|;
block|}
specifier|protected
name|void
name|parse
parameter_list|()
throws|throws
name|SAXException
block|{
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
switch|switch
condition|(
name|streamReader
operator|.
name|getEventType
argument_list|()
condition|)
block|{
comment|// Attributes are handled in START_ELEMENT
case|case
name|XMLStreamConstants
operator|.
name|ATTRIBUTE
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|CDATA
case|:
block|{
if|if
condition|(
name|lexicalHandler
operator|!=
literal|null
condition|)
block|{
name|lexicalHandler
operator|.
name|startCDATA
argument_list|()
expr_stmt|;
block|}
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|contentHandler
operator|.
name|characters
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
if|if
condition|(
name|lexicalHandler
operator|!=
literal|null
condition|)
block|{
name|lexicalHandler
operator|.
name|endCDATA
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|CHARACTERS
case|:
block|{
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|contentHandler
operator|.
name|characters
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|SPACE
case|:
block|{
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|contentHandler
operator|.
name|ignorableWhitespace
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|COMMENT
case|:
if|if
condition|(
name|lexicalHandler
operator|!=
literal|null
condition|)
block|{
name|int
name|length
init|=
name|streamReader
operator|.
name|getTextLength
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|streamReader
operator|.
name|getTextStart
argument_list|()
decl_stmt|;
name|char
index|[]
name|chars
init|=
name|streamReader
operator|.
name|getTextCharacters
argument_list|()
decl_stmt|;
name|lexicalHandler
operator|.
name|comment
argument_list|(
name|chars
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|XMLStreamConstants
operator|.
name|DTD
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_DOCUMENT
case|:
name|contentHandler
operator|.
name|endDocument
argument_list|()
expr_stmt|;
return|return;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
block|{
name|String
name|uri
init|=
name|streamReader
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localName
init|=
name|streamReader
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|streamReader
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|qname
init|=
name|prefix
operator|!=
literal|null
operator|&&
operator|!
name|prefix
operator|.
name|isEmpty
argument_list|()
condition|?
name|prefix
operator|+
literal|':'
operator|+
name|localName
else|:
name|localName
decl_stmt|;
name|contentHandler
operator|.
name|endElement
argument_list|(
name|uri
argument_list|,
name|localName
argument_list|,
name|qname
argument_list|)
expr_stmt|;
comment|// namespaces
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|streamReader
operator|.
name|getNamespaceCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|contentHandler
operator|.
name|endPrefixMapping
argument_list|(
name|streamReader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
name|XMLStreamConstants
operator|.
name|ENTITY_DECLARATION
case|:
case|case
name|XMLStreamConstants
operator|.
name|ENTITY_REFERENCE
case|:
case|case
name|XMLStreamConstants
operator|.
name|NAMESPACE
case|:
case|case
name|XMLStreamConstants
operator|.
name|NOTATION_DECLARATION
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|PROCESSING_INSTRUCTION
case|:
break|break;
case|case
name|XMLStreamConstants
operator|.
name|START_DOCUMENT
case|:
name|contentHandler
operator|.
name|startDocument
argument_list|()
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
block|{
name|String
name|uri
init|=
name|streamReader
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
name|String
name|localName
init|=
name|streamReader
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|streamReader
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|qname
init|=
name|prefix
operator|!=
literal|null
operator|&&
operator|!
name|prefix
operator|.
name|isEmpty
argument_list|()
condition|?
name|prefix
operator|+
literal|':'
operator|+
name|localName
else|:
name|localName
decl_stmt|;
comment|// namespaces
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|streamReader
operator|.
name|getNamespaceCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|nsPrefix
init|=
name|streamReader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|nsUri
init|=
name|streamReader
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|nsUri
operator|==
literal|null
condition|)
block|{
name|nsUri
operator|=
literal|""
expr_stmt|;
block|}
name|contentHandler
operator|.
name|startPrefixMapping
argument_list|(
name|nsPrefix
argument_list|,
name|nsUri
argument_list|)
expr_stmt|;
block|}
name|contentHandler
operator|.
name|startElement
argument_list|(
name|uri
operator|==
literal|null
condition|?
literal|""
else|:
name|uri
argument_list|,
name|localName
argument_list|,
name|qname
argument_list|,
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
default|default:
break|break;
block|}
if|if
condition|(
operator|!
name|streamReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return;
block|}
name|streamReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|SAXParseException
name|spe
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|spe
operator|=
operator|new
name|SAXParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
operator|.
name|getLocation
argument_list|()
operator|.
name|getLineNumber
argument_list|()
argument_list|,
name|e
operator|.
name|getLocation
argument_list|()
operator|.
name|getColumnNumber
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|spe
operator|=
operator|new
name|SAXParseException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|spe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|spe
throw|;
block|}
block|}
specifier|protected
name|String
name|getQualifiedName
parameter_list|()
block|{
name|String
name|prefix
init|=
name|streamReader
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
operator|!
name|prefix
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|prefix
operator|+
literal|':'
operator|+
name|streamReader
operator|.
name|getLocalName
argument_list|()
return|;
block|}
return|return
name|streamReader
operator|.
name|getLocalName
argument_list|()
return|;
block|}
specifier|protected
name|Attributes
name|getAttributes
parameter_list|()
block|{
name|AttributesImpl
name|attrs
init|=
operator|new
name|AttributesImpl
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
name|streamReader
operator|.
name|getAttributeCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|uri
init|=
name|streamReader
operator|.
name|getAttributeNamespace
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|localName
init|=
name|streamReader
operator|.
name|getAttributeLocalName
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|streamReader
operator|.
name|getAttributePrefix
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|qName
decl_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|qName
operator|=
name|prefix
operator|+
literal|':'
operator|+
name|localName
expr_stmt|;
block|}
else|else
block|{
name|qName
operator|=
name|localName
expr_stmt|;
block|}
name|String
name|type
init|=
name|streamReader
operator|.
name|getAttributeType
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|streamReader
operator|.
name|getAttributeValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
literal|""
expr_stmt|;
block|}
name|attrs
operator|.
name|addAttribute
argument_list|(
name|uri
operator|==
literal|null
condition|?
literal|""
else|:
name|uri
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|attrs
return|;
block|}
specifier|public
name|boolean
name|getFeature
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setFeature
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{     }
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|SAXNotRecognizedException
throws|,
name|SAXNotSupportedException
block|{
if|if
condition|(
literal|"http://xml.org/sax/properties/lexical-handler"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|lexicalHandler
operator|=
operator|(
name|LexicalHandler
operator|)
name|value
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|SAXNotRecognizedException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{     }
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setDTDHandler
parameter_list|(
name|DTDHandler
name|handler
parameter_list|)
block|{     }
specifier|public
name|DTDHandler
name|getDTDHandler
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setContentHandler
parameter_list|(
name|ContentHandler
name|handler
parameter_list|)
block|{
name|this
operator|.
name|contentHandler
operator|=
name|handler
expr_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|LexicalHandler
operator|&&
name|lexicalHandler
operator|==
literal|null
condition|)
block|{
name|lexicalHandler
operator|=
operator|(
name|LexicalHandler
operator|)
name|handler
expr_stmt|;
block|}
block|}
specifier|public
name|ContentHandler
name|getContentHandler
parameter_list|()
block|{
return|return
name|this
operator|.
name|contentHandler
return|;
block|}
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|ErrorHandler
name|handler
parameter_list|)
block|{     }
specifier|public
name|ErrorHandler
name|getErrorHandler
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|parse
parameter_list|(
name|InputSource
name|input
parameter_list|)
throws|throws
name|SAXException
block|{
name|StaxSource
operator|.
name|this
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|parse
parameter_list|(
name|String
name|systemId
parameter_list|)
throws|throws
name|SAXException
block|{
name|StaxSource
operator|.
name|this
operator|.
name|parse
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

