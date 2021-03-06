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
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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

begin_class
specifier|public
class|class
name|PrettyPrintXMLStreamWriter
implements|implements
name|XMLStreamWriter
block|{
specifier|static
specifier|final
name|int
name|DEFAULT_INDENT_LEVEL
init|=
literal|2
decl_stmt|;
name|XMLStreamWriter
name|baseWriter
decl_stmt|;
name|int
name|curIndent
decl_stmt|;
name|int
name|indentAmount
init|=
name|DEFAULT_INDENT_LEVEL
decl_stmt|;
specifier|final
name|Deque
argument_list|<
name|CurrentElement
argument_list|>
name|elems
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|PrettyPrintXMLStreamWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|int
name|indentAmount
parameter_list|)
block|{
name|this
argument_list|(
name|writer
argument_list|,
name|indentAmount
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PrettyPrintXMLStreamWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|int
name|indentAmount
parameter_list|,
name|int
name|initialLevel
parameter_list|)
block|{
name|baseWriter
operator|=
name|writer
expr_stmt|;
name|curIndent
operator|=
name|initialLevel
expr_stmt|;
name|this
operator|.
name|indentAmount
operator|=
name|indentAmount
expr_stmt|;
block|}
specifier|public
name|void
name|writeSpaces
parameter_list|()
throws|throws
name|XMLStreamException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|curIndent
condition|;
name|i
operator|++
control|)
block|{
name|baseWriter
operator|.
name|writeCharacters
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|indentWithSpaces
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|writeSpaces
argument_list|()
expr_stmt|;
name|indent
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|indent
parameter_list|()
block|{
name|curIndent
operator|+=
name|indentAmount
expr_stmt|;
block|}
specifier|public
name|void
name|unindent
parameter_list|()
block|{
name|curIndent
operator|-=
name|indentAmount
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|baseWriter
operator|.
name|getNamespaceContext
argument_list|()
return|;
block|}
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getPrefix
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|baseWriter
operator|.
name|getPrefix
argument_list|(
name|uri
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|name
parameter_list|)
block|{
return|return
name|baseWriter
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|setDefaultNamespace
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespaceContext
name|context
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|setNamespaceContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeAttribute
argument_list|(
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeAttribute
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeAttribute
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeCData
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeCData
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|char
index|[]
name|text
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeCharacters
argument_list|(
name|text
argument_list|,
name|start
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|text
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeComment
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeComment
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeDefaultNamespace
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeDefaultNamespace
argument_list|(
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeDTD
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|dtd
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeDTD
argument_list|(
name|dtd
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeEmptyElement
argument_list|(
name|localName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeEmptyElement
argument_list|(
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeEmptyElement
argument_list|(
name|prefix
argument_list|,
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEndDocument
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEndElement
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|CurrentElement
name|elem
init|=
name|elems
operator|.
name|pop
argument_list|()
decl_stmt|;
name|unindent
argument_list|()
expr_stmt|;
if|if
condition|(
name|elem
operator|.
name|hasChildElements
argument_list|()
condition|)
block|{
name|baseWriter
operator|.
name|writeCharacters
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|writeSpaces
argument_list|()
expr_stmt|;
block|}
name|baseWriter
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|elems
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|baseWriter
operator|.
name|writeCharacters
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeEntityRef
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|name
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeEntityRef
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeNamespace
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|target
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeProcessingInstruction
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|target
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeProcessingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeStartDocument
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|encoding
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|baseWriter
operator|.
name|writeStartDocument
argument_list|(
name|encoding
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
literal|null
argument_list|,
name|localName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
literal|null
argument_list|,
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|QName
name|currElemName
init|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|)
decl_stmt|;
if|if
condition|(
name|elems
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|indentWithSpaces
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|baseWriter
operator|.
name|writeCharacters
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|indentWithSpaces
argument_list|()
expr_stmt|;
name|CurrentElement
name|elem
init|=
name|elems
operator|.
name|peek
argument_list|()
decl_stmt|;
name|elem
operator|.
name|setChildElements
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prefix
operator|==
literal|null
operator|&&
name|namespaceURI
operator|==
literal|null
condition|)
block|{
name|baseWriter
operator|.
name|writeStartElement
argument_list|(
name|localName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|baseWriter
operator|.
name|writeStartElement
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|baseWriter
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
block|}
name|elems
operator|.
name|push
argument_list|(
operator|new
name|CurrentElement
argument_list|(
name|currElemName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|CurrentElement
block|{
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|boolean
name|hasChildElements
decl_stmt|;
name|CurrentElement
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
name|name
operator|=
name|qname
expr_stmt|;
block|}
specifier|public
name|QName
name|getQName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|boolean
name|hasChildElements
parameter_list|()
block|{
return|return
name|hasChildElements
return|;
block|}
specifier|public
name|void
name|setChildElements
parameter_list|(
name|boolean
name|childElements
parameter_list|)
block|{
name|hasChildElements
operator|=
name|childElements
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

