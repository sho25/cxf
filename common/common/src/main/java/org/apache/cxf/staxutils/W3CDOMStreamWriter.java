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
name|Collections
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
name|Stack
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
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|Document
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
name|DocumentFragment
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
name|Element
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
name|helpers
operator|.
name|MapNamespaceContext
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
name|XMLUtils
import|;
end_import

begin_class
specifier|public
class|class
name|W3CDOMStreamWriter
implements|implements
name|XMLStreamWriter
block|{
specifier|static
specifier|final
name|String
name|XML_NS
init|=
literal|"http://www.w3.org/2000/xmlns/"
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|Node
argument_list|>
name|stack
init|=
operator|new
name|Stack
argument_list|<
name|Node
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Document
name|document
decl_stmt|;
specifier|private
name|Node
name|currentNode
decl_stmt|;
specifier|private
name|NamespaceContext
name|context
init|=
operator|new
name|W3CNamespaceContext
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|nsRepairing
decl_stmt|;
specifier|private
name|Map
name|properties
init|=
name|Collections
operator|.
name|EMPTY_MAP
decl_stmt|;
specifier|public
name|W3CDOMStreamWriter
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
name|document
operator|=
name|XMLUtils
operator|.
name|newDocument
argument_list|()
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamWriter
parameter_list|(
name|DocumentBuilder
name|builder
parameter_list|)
block|{
name|document
operator|=
name|builder
operator|.
name|newDocument
argument_list|()
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamWriter
parameter_list|(
name|Document
name|document
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamWriter
parameter_list|(
name|DocumentFragment
name|frag
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|frag
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
name|currentNode
operator|=
name|frag
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamWriter
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|e
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
name|currentNode
operator|=
name|e
expr_stmt|;
operator|(
operator|(
name|W3CNamespaceContext
operator|)
name|context
operator|)
operator|.
name|setElement
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Element
name|getCurrentNode
parameter_list|()
block|{
if|if
condition|(
name|currentNode
operator|instanceof
name|Element
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|currentNode
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|DocumentFragment
name|getCurrentFragment
parameter_list|()
block|{
if|if
condition|(
name|currentNode
operator|instanceof
name|DocumentFragment
condition|)
block|{
return|return
operator|(
name|DocumentFragment
operator|)
name|currentNode
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setNsRepairing
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|nsRepairing
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNsRepairing
parameter_list|()
block|{
return|return
name|nsRepairing
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Map
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|Document
name|getDocument
parameter_list|()
block|{
return|return
name|document
return|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|newChild
argument_list|(
name|document
operator|.
name|createElement
argument_list|(
name|local
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|newChild
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|setChild
argument_list|(
name|element
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setChild
parameter_list|(
name|Element
name|element
parameter_list|,
name|boolean
name|append
parameter_list|)
block|{
if|if
condition|(
name|currentNode
operator|!=
literal|null
condition|)
block|{
name|stack
operator|.
name|push
argument_list|(
name|currentNode
argument_list|)
expr_stmt|;
if|if
condition|(
name|append
condition|)
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|append
condition|)
block|{
name|document
operator|.
name|appendChild
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
operator|(
name|context
operator|instanceof
name|W3CNamespaceContext
operator|)
condition|)
block|{
name|context
operator|=
operator|new
name|W3CNamespaceContext
argument_list|()
expr_stmt|;
block|}
operator|(
operator|(
name|W3CNamespaceContext
operator|)
name|context
operator|)
operator|.
name|setElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|currentNode
operator|=
name|element
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|newChild
argument_list|(
name|document
operator|.
name|createElementNS
argument_list|(
name|namespace
argument_list|,
name|local
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
operator|||
name|prefix
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|writeStartElement
argument_list|(
name|namespace
argument_list|,
name|local
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newChild
argument_list|(
name|document
operator|.
name|createElementNS
argument_list|(
name|namespace
argument_list|,
name|prefix
operator|+
literal|":"
operator|+
name|local
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|nsRepairing
operator|&&
operator|!
name|prefix
operator|.
name|equals
argument_list|(
name|getNamespaceContext
argument_list|()
operator|.
name|getPrefix
argument_list|(
name|namespace
argument_list|)
argument_list|)
condition|)
block|{
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|namespace
argument_list|,
name|local
argument_list|)
expr_stmt|;
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|,
name|local
argument_list|)
expr_stmt|;
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|local
argument_list|)
expr_stmt|;
name|writeEndElement
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
if|if
condition|(
name|stack
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|currentNode
operator|=
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|currentNode
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|instanceof
name|W3CNamespaceContext
operator|&&
name|currentNode
operator|instanceof
name|Element
condition|)
block|{
operator|(
operator|(
name|W3CNamespaceContext
operator|)
name|context
operator|)
operator|.
name|setElement
argument_list|(
operator|(
name|Element
operator|)
name|currentNode
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|context
operator|instanceof
name|MapNamespaceContext
condition|)
block|{
operator|(
operator|(
name|MapNamespaceContext
operator|)
name|context
operator|)
operator|.
name|setTargetNode
argument_list|(
name|currentNode
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeEndDocument
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|local
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Attr
name|a
init|=
name|document
operator|.
name|createAttribute
argument_list|(
name|local
argument_list|)
decl_stmt|;
name|a
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Element
operator|)
name|currentNode
operator|)
operator|.
name|setAttributeNode
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|local
operator|=
name|prefix
operator|+
literal|":"
operator|+
name|local
expr_stmt|;
block|}
name|Attr
name|a
init|=
name|document
operator|.
name|createAttributeNS
argument_list|(
name|namespace
argument_list|,
name|local
argument_list|)
decl_stmt|;
name|a
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Element
operator|)
name|currentNode
operator|)
operator|.
name|setAttributeNodeNS
argument_list|(
name|a
argument_list|)
expr_stmt|;
if|if
condition|(
name|nsRepairing
operator|&&
operator|!
name|prefix
operator|.
name|equals
argument_list|(
name|getNamespaceContext
argument_list|()
operator|.
name|getPrefix
argument_list|(
name|namespace
argument_list|)
argument_list|)
condition|)
block|{
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Attr
name|a
init|=
name|document
operator|.
name|createAttributeNS
argument_list|(
name|namespace
argument_list|,
name|local
argument_list|)
decl_stmt|;
name|a
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Element
operator|)
name|currentNode
operator|)
operator|.
name|setAttributeNodeNS
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeNamespace
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|writeDefaultNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Attr
name|attr
init|=
name|document
operator|.
name|createAttributeNS
argument_list|(
name|XML_NS
argument_list|,
literal|"xmlns:"
operator|+
name|prefix
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Element
operator|)
name|currentNode
operator|)
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeDefaultNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|Attr
name|attr
init|=
name|document
operator|.
name|createAttributeNS
argument_list|(
name|XML_NS
argument_list|,
literal|"xmlns"
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
operator|(
operator|(
name|Element
operator|)
name|currentNode
operator|)
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeComment
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|currentNode
operator|==
literal|null
condition|)
block|{
name|document
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createComment
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createComment
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|String
name|target
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|currentNode
operator|==
literal|null
condition|)
block|{
name|document
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createProcessingInstruction
argument_list|(
name|target
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createProcessingInstruction
argument_list|(
name|target
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|String
name|target
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|currentNode
operator|==
literal|null
condition|)
block|{
name|document
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createProcessingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createProcessingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeCData
parameter_list|(
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createCDATASection
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeDTD
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|writeEntityRef
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createEntityReference
argument_list|(
name|ref
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{
try|try
block|{
name|document
operator|.
name|setXmlVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - likely not DOM level 3
block|}
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|String
name|encoding
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{
try|try
block|{
name|document
operator|.
name|setXmlVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - likely not DOM level 3
block|}
block|}
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentNode
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createTextNode
argument_list|(
name|text
argument_list|)
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
name|writeCharacters
argument_list|(
operator|new
name|String
argument_list|(
name|text
argument_list|,
name|start
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|context
operator|==
literal|null
condition|?
literal|null
else|:
name|context
operator|.
name|getPrefix
argument_list|(
name|uri
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespaceContext
name|ctx
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|this
operator|.
name|context
operator|=
name|ctx
expr_stmt|;
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|prop
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|prop
argument_list|)
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
block|}
end_class

end_unit

