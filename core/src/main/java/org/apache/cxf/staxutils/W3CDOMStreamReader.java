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
name|ArrayList
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
name|XMLStreamException
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
name|Comment
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
name|NamedNodeMap
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
name|w3c
operator|.
name|dom
operator|.
name|ProcessingInstruction
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
name|Text
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
name|TypeInfo
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

begin_class
specifier|public
class|class
name|W3CDOMStreamReader
extends|extends
name|AbstractDOMStreamReader
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
block|{
specifier|private
name|Node
name|content
decl_stmt|;
specifier|private
name|Document
name|document
decl_stmt|;
specifier|private
name|W3CNamespaceContext
name|context
decl_stmt|;
specifier|private
name|String
name|sysId
decl_stmt|;
comment|/**      * @param element      */
specifier|public
name|W3CDOMStreamReader
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
argument_list|(
name|element
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|content
operator|=
name|element
expr_stmt|;
name|newFrame
argument_list|(
name|getCurrentFrame
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|element
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamReader
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|systemId
parameter_list|)
block|{
name|this
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|sysId
operator|=
name|systemId
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamReader
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
argument_list|(
name|doc
argument_list|,
literal|false
argument_list|)
block|{
specifier|public
name|boolean
name|isDocument
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|doc
expr_stmt|;
block|}
specifier|public
name|W3CDOMStreamReader
parameter_list|(
name|DocumentFragment
name|docfrag
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
argument_list|(
name|docfrag
argument_list|,
literal|true
argument_list|)
block|{
specifier|public
name|boolean
name|isDocumentFragment
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|docfrag
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get the document associated with this stream.      */
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
name|String
name|getSystemId
parameter_list|()
block|{
try|try
block|{
return|return
name|sysId
operator|==
literal|null
condition|?
name|document
operator|.
name|getDocumentURI
argument_list|()
else|:
name|sysId
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|//ignore, probably not DOM level 3
block|}
return|return
name|sysId
return|;
block|}
comment|/**      * Find name spaces declaration in atrributes and move them to separate      * collection.      */
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|newFrame
parameter_list|(
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
name|frame
parameter_list|)
block|{
name|Node
name|element
init|=
name|getCurrentNode
argument_list|()
decl_stmt|;
name|frame
operator|.
name|uris
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|frame
operator|.
name|prefixes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|frame
operator|.
name|attributes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
operator|new
name|W3CNamespaceContext
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|element
operator|instanceof
name|Element
condition|)
block|{
name|context
operator|.
name|setElement
argument_list|(
operator|(
name|Element
operator|)
name|element
argument_list|)
expr_stmt|;
block|}
name|NamedNodeMap
name|nodes
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodes
operator|!=
literal|null
condition|)
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
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|node
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|localName
init|=
name|node
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|node
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getNodeName
argument_list|()
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
literal|""
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|frame
operator|.
name|uris
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|frame
operator|.
name|prefixes
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|prefix
operator|.
name|isEmpty
argument_list|()
operator|&&
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|frame
operator|.
name|uris
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|frame
operator|.
name|prefixes
operator|.
name|add
argument_list|(
name|localName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
condition|)
block|{
name|frame
operator|.
name|uris
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|frame
operator|.
name|prefixes
operator|.
name|add
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|frame
operator|.
name|attributes
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
specifier|final
name|Node
name|getCurrentNode
parameter_list|()
block|{
return|return
name|getCurrentFrame
argument_list|()
operator|.
name|element
return|;
block|}
specifier|public
specifier|final
name|Element
name|getCurrentElement
parameter_list|()
block|{
return|return
operator|(
name|Element
operator|)
name|getCurrentFrame
argument_list|()
operator|.
name|element
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
name|getChildFrame
parameter_list|()
block|{
return|return
operator|new
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
argument_list|(
name|getCurrentFrame
argument_list|()
operator|.
name|currentChild
argument_list|,
name|getCurrentFrame
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|hasMoreChildren
parameter_list|()
block|{
if|if
condition|(
name|getCurrentFrame
argument_list|()
operator|.
name|currentChild
operator|==
literal|null
condition|)
block|{
return|return
name|getCurrentNode
argument_list|()
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
return|;
block|}
return|return
name|getCurrentFrame
argument_list|()
operator|.
name|currentChild
operator|.
name|getNextSibling
argument_list|()
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|nextChild
parameter_list|()
block|{
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
name|frame
init|=
name|getCurrentFrame
argument_list|()
decl_stmt|;
if|if
condition|(
name|frame
operator|.
name|currentChild
operator|==
literal|null
condition|)
block|{
name|content
operator|=
name|getCurrentNode
argument_list|()
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|content
operator|=
name|frame
operator|.
name|currentChild
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|frame
operator|.
name|currentChild
operator|=
name|content
expr_stmt|;
switch|switch
condition|(
name|content
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
case|case
name|Node
operator|.
name|ELEMENT_NODE
case|:
return|return
name|START_ELEMENT
return|;
case|case
name|Node
operator|.
name|TEXT_NODE
case|:
return|return
name|CHARACTERS
return|;
case|case
name|Node
operator|.
name|COMMENT_NODE
case|:
return|return
name|COMMENT
return|;
case|case
name|Node
operator|.
name|CDATA_SECTION_NODE
case|:
return|return
name|CDATA
return|;
case|case
name|Node
operator|.
name|ENTITY_REFERENCE_NODE
case|:
return|return
name|ENTITY_REFERENCE
return|;
case|case
name|Node
operator|.
name|PROCESSING_INSTRUCTION_NODE
case|:
return|return
name|PROCESSING_INSTRUCTION
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Found type: "
operator|+
name|content
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getElementText
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|String
name|result
init|=
name|DOMUtils
operator|.
name|getRawContent
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
name|frame
init|=
name|getCurrentFrame
argument_list|()
decl_stmt|;
name|frame
operator|.
name|ended
operator|=
literal|true
expr_stmt|;
name|currentEvent
operator|=
name|END_ELEMENT
expr_stmt|;
name|endElement
argument_list|()
expr_stmt|;
comment|// we should not return null according to the StAx API javadoc
return|return
name|result
operator|!=
literal|null
condition|?
name|result
else|:
literal|""
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|ElementFrame
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
name|frame
init|=
name|getCurrentFrame
argument_list|()
decl_stmt|;
while|while
condition|(
literal|null
operator|!=
name|frame
condition|)
block|{
name|int
name|index
init|=
name|frame
operator|.
name|prefixes
operator|.
name|indexOf
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|frame
operator|.
name|uris
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
if|if
condition|(
name|frame
operator|.
name|parent
operator|==
literal|null
operator|&&
name|frame
operator|.
name|getElement
argument_list|()
operator|instanceof
name|Element
condition|)
block|{
return|return
operator|(
operator|(
name|Element
operator|)
name|frame
operator|.
name|getElement
argument_list|()
operator|)
operator|.
name|lookupNamespaceURI
argument_list|(
name|prefix
argument_list|)
return|;
block|}
name|frame
operator|=
name|frame
operator|.
name|parent
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getAttributeValue
parameter_list|(
name|String
name|ns
parameter_list|,
name|String
name|local
parameter_list|)
block|{
name|Attr
name|at
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
name|ns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|at
operator|=
name|getCurrentElement
argument_list|()
operator|.
name|getAttributeNode
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|at
operator|=
name|getCurrentElement
argument_list|()
operator|.
name|getAttributeNodeNS
argument_list|(
name|ns
argument_list|,
name|local
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|at
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|at
operator|.
name|getNodeValue
argument_list|()
return|;
block|}
specifier|public
name|int
name|getAttributeCount
parameter_list|()
block|{
return|return
name|getCurrentFrame
argument_list|()
operator|.
name|attributes
operator|.
name|size
argument_list|()
return|;
block|}
name|Attr
name|getAttribute
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
operator|(
name|Attr
operator|)
name|getCurrentFrame
argument_list|()
operator|.
name|attributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
specifier|private
name|String
name|getLocalName
parameter_list|(
name|Attr
name|attr
parameter_list|)
block|{
name|String
name|name
init|=
name|attr
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|attr
operator|.
name|getNodeName
argument_list|()
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|public
name|QName
name|getAttributeName
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|Attr
name|at
init|=
name|getAttribute
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|prefix
init|=
name|at
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|ln
init|=
name|getLocalName
argument_list|(
name|at
argument_list|)
decl_stmt|;
comment|// at.getNodeName();
name|String
name|ns
init|=
name|at
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|ln
argument_list|)
return|;
block|}
return|return
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|ln
argument_list|,
name|prefix
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributeNamespace
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
specifier|public
name|String
name|getAttributeLocalName
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|Attr
name|attr
init|=
name|getAttribute
argument_list|(
name|i
argument_list|)
decl_stmt|;
return|return
name|getLocalName
argument_list|(
name|attr
argument_list|)
return|;
block|}
specifier|public
name|String
name|getAttributePrefix
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getPrefix
argument_list|()
return|;
block|}
specifier|public
name|String
name|getAttributeType
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|Attr
name|attr
init|=
name|getAttribute
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|attr
operator|.
name|isId
argument_list|()
condition|)
block|{
return|return
literal|"ID"
return|;
block|}
name|TypeInfo
name|schemaType
init|=
literal|null
decl_stmt|;
try|try
block|{
name|schemaType
operator|=
name|attr
operator|.
name|getSchemaTypeInfo
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//DOM level 2?
name|schemaType
operator|=
literal|null
expr_stmt|;
block|}
return|return
operator|(
name|schemaType
operator|==
literal|null
operator|)
condition|?
literal|"CDATA"
else|:
name|schemaType
operator|.
name|getTypeName
argument_list|()
operator|==
literal|null
condition|?
literal|"CDATA"
else|:
name|schemaType
operator|.
name|getTypeName
argument_list|()
return|;
block|}
specifier|public
name|String
name|getAttributeValue
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isAttributeSpecified
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|getAttribute
argument_list|(
name|i
argument_list|)
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
return|;
block|}
specifier|public
name|int
name|getNamespaceCount
parameter_list|()
block|{
return|return
name|getCurrentFrame
argument_list|()
operator|.
name|prefixes
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|String
name|getNamespacePrefix
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|getCurrentFrame
argument_list|()
operator|.
name|prefixes
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|getCurrentFrame
argument_list|()
operator|.
name|uris
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
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
name|String
name|getText
parameter_list|()
block|{
if|if
condition|(
name|content
operator|instanceof
name|Text
condition|)
block|{
return|return
operator|(
operator|(
name|Text
operator|)
name|content
operator|)
operator|.
name|getData
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|content
operator|instanceof
name|Comment
condition|)
block|{
return|return
operator|(
operator|(
name|Comment
operator|)
name|content
operator|)
operator|.
name|getData
argument_list|()
return|;
block|}
return|return
name|DOMUtils
operator|.
name|getRawContent
argument_list|(
name|getCurrentNode
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|char
index|[]
name|getTextCharacters
parameter_list|()
block|{
return|return
name|getText
argument_list|()
operator|.
name|toCharArray
argument_list|()
return|;
block|}
specifier|public
name|int
name|getTextStart
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|int
name|getTextLength
parameter_list|()
block|{
return|return
name|getText
argument_list|()
operator|.
name|length
argument_list|()
return|;
block|}
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
name|Node
name|el
init|=
name|getCurrentNode
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
name|getPrefix
argument_list|()
decl_stmt|;
name|String
name|ln
init|=
name|getLocalName
argument_list|()
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|ln
argument_list|,
name|prefix
argument_list|)
return|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
name|String
name|ln
init|=
name|getCurrentNode
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ln
operator|==
literal|null
condition|)
block|{
name|ln
operator|=
name|getCurrentNode
argument_list|()
operator|.
name|getNodeName
argument_list|()
expr_stmt|;
if|if
condition|(
name|ln
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|ln
operator|=
name|ln
operator|.
name|substring
argument_list|(
name|ln
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ln
return|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
name|String
name|ln
init|=
name|getCurrentNode
argument_list|()
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|ln
operator|==
literal|null
condition|)
block|{
name|ln
operator|=
name|getCurrentNode
argument_list|()
operator|.
name|getNodeName
argument_list|()
expr_stmt|;
if|if
condition|(
name|ln
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
name|ln
operator|=
name|getNamespaceURI
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ln
operator|=
name|getNamespaceURI
argument_list|(
name|ln
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ln
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ln
return|;
block|}
return|return
name|getCurrentNode
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
name|String
name|prefix
init|=
name|getCurrentNode
argument_list|()
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|String
name|nodeName
init|=
name|getCurrentNode
argument_list|()
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodeName
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|prefix
operator|=
name|nodeName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|nodeName
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
block|}
return|return
name|prefix
return|;
block|}
specifier|public
name|String
name|getPITarget
parameter_list|()
block|{
return|return
operator|(
operator|(
name|ProcessingInstruction
operator|)
name|content
operator|)
operator|.
name|getTarget
argument_list|()
return|;
block|}
specifier|public
name|String
name|getPIData
parameter_list|()
block|{
return|return
operator|(
operator|(
name|ProcessingInstruction
operator|)
name|content
operator|)
operator|.
name|getData
argument_list|()
return|;
block|}
specifier|public
name|Location
name|getLocation
parameter_list|()
block|{
try|try
block|{
name|Object
name|o
init|=
name|getCurrentNode
argument_list|()
operator|.
name|getUserData
argument_list|(
literal|"location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Location
condition|)
block|{
return|return
operator|(
name|Location
operator|)
name|o
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|//ignore, probably not DOM level 3
block|}
return|return
name|super
operator|.
name|getLocation
argument_list|()
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
return|return
literal|"<null>"
return|;
block|}
if|if
condition|(
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|"<null document element>"
return|;
block|}
try|try
block|{
return|return
name|StaxUtils
operator|.
name|toString
argument_list|(
name|document
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

