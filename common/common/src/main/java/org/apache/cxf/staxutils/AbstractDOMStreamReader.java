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
name|HashMap
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

begin_comment
comment|/**  * Abstract logic for creating XMLStreamReader from DOM documents. Its works  * using adapters for Element, Node and Attribute (  *   * @see ElementAdapter }  * @author<a href="mailto:tsztelak@gmail.com">Tomasz Sztelak</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDOMStreamReader
parameter_list|<
name|T
parameter_list|,
name|I
parameter_list|>
implements|implements
name|XMLStreamReader
block|{
specifier|protected
name|int
name|currentEvent
init|=
name|XMLStreamConstants
operator|.
name|START_DOCUMENT
decl_stmt|;
specifier|private
name|Map
name|properties
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|FastStack
argument_list|<
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
argument_list|>
name|frames
init|=
operator|new
name|FastStack
argument_list|<
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|frame
decl_stmt|;
comment|/**      *           */
specifier|public
specifier|static
class|class
name|ElementFrame
parameter_list|<
name|T
parameter_list|,
name|I
parameter_list|>
block|{
name|T
name|element
decl_stmt|;
name|I
name|currentChild
decl_stmt|;
name|boolean
name|started
decl_stmt|;
name|boolean
name|ended
decl_stmt|;
name|int
name|currentAttribute
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|currentNamespace
init|=
operator|-
literal|1
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|uris
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|prefixes
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|attributes
decl_stmt|;
specifier|final
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|parent
decl_stmt|;
specifier|public
name|ElementFrame
parameter_list|(
name|T
name|element
parameter_list|,
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|parent
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
specifier|public
name|ElementFrame
parameter_list|(
name|T
name|element
parameter_list|,
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|parent
parameter_list|,
name|I
name|ch
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|element
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|currentChild
operator|=
name|ch
expr_stmt|;
block|}
specifier|public
name|ElementFrame
parameter_list|(
name|T
name|doc
parameter_list|,
name|boolean
name|s
parameter_list|)
block|{
name|this
operator|.
name|element
operator|=
name|doc
expr_stmt|;
name|parent
operator|=
literal|null
expr_stmt|;
name|started
operator|=
name|s
expr_stmt|;
name|attributes
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
name|prefixes
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
name|uris
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ElementFrame
parameter_list|(
name|T
name|doc
parameter_list|)
block|{
name|this
argument_list|(
name|doc
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|T
name|getElement
parameter_list|()
block|{
return|return
name|element
return|;
block|}
specifier|public
name|I
name|getCurrentChild
parameter_list|()
block|{
return|return
name|currentChild
return|;
block|}
specifier|public
name|void
name|setCurrentChild
parameter_list|(
name|I
name|o
parameter_list|)
block|{
name|currentChild
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDocument
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isDocumentFragment
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * @param element      */
specifier|public
name|AbstractDOMStreamReader
parameter_list|(
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|frame
parameter_list|)
block|{
name|this
operator|.
name|frame
operator|=
name|frame
expr_stmt|;
name|frames
operator|.
name|push
argument_list|(
name|this
operator|.
name|frame
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|getCurrentFrame
parameter_list|()
block|{
return|return
name|frame
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#getProperty(java.lang.String)      */
specifier|public
name|Object
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#next()      */
specifier|public
name|int
name|next
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|frame
operator|.
name|ended
condition|)
block|{
name|frames
operator|.
name|pop
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|frames
operator|.
name|empty
argument_list|()
condition|)
block|{
name|frame
operator|=
name|frames
operator|.
name|peek
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|currentEvent
operator|=
name|END_DOCUMENT
expr_stmt|;
return|return
name|currentEvent
return|;
block|}
block|}
if|if
condition|(
operator|!
name|frame
operator|.
name|started
condition|)
block|{
name|frame
operator|.
name|started
operator|=
literal|true
expr_stmt|;
name|currentEvent
operator|=
name|frame
operator|.
name|isDocument
argument_list|()
condition|?
name|START_DOCUMENT
else|:
name|START_ELEMENT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|frame
operator|.
name|currentAttribute
operator|<
name|getAttributeCount
argument_list|()
operator|-
literal|1
condition|)
block|{
name|frame
operator|.
name|currentAttribute
operator|++
expr_stmt|;
name|currentEvent
operator|=
name|ATTRIBUTE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|frame
operator|.
name|currentNamespace
operator|<
name|getNamespaceCount
argument_list|()
operator|-
literal|1
condition|)
block|{
name|frame
operator|.
name|currentNamespace
operator|++
expr_stmt|;
name|currentEvent
operator|=
name|NAMESPACE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hasMoreChildren
argument_list|()
condition|)
block|{
name|currentEvent
operator|=
name|nextChild
argument_list|()
expr_stmt|;
if|if
condition|(
name|currentEvent
operator|==
name|START_ELEMENT
condition|)
block|{
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|newFrame
init|=
name|getChildFrame
argument_list|()
decl_stmt|;
name|newFrame
operator|.
name|started
operator|=
literal|true
expr_stmt|;
name|frame
operator|=
name|newFrame
expr_stmt|;
name|frames
operator|.
name|push
argument_list|(
name|this
operator|.
name|frame
argument_list|)
expr_stmt|;
name|currentEvent
operator|=
name|START_ELEMENT
expr_stmt|;
name|newFrame
argument_list|(
name|newFrame
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|frame
operator|.
name|ended
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|frame
operator|.
name|isDocument
argument_list|()
condition|)
block|{
name|currentEvent
operator|=
name|END_DOCUMENT
expr_stmt|;
block|}
else|else
block|{
name|currentEvent
operator|=
name|END_ELEMENT
expr_stmt|;
name|endElement
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|currentEvent
return|;
block|}
specifier|protected
name|void
name|newFrame
parameter_list|(
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|newFrame
parameter_list|)
block|{     }
specifier|protected
name|void
name|endElement
parameter_list|()
block|{     }
specifier|protected
specifier|abstract
name|boolean
name|hasMoreChildren
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|int
name|nextChild
parameter_list|()
function_decl|;
specifier|protected
specifier|abstract
name|ElementFrame
argument_list|<
name|T
argument_list|,
name|I
argument_list|>
name|getChildFrame
parameter_list|()
function_decl|;
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#require(int, java.lang.String,      *      java.lang.String)      */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#getElementText()      */
specifier|public
specifier|abstract
name|String
name|getElementText
parameter_list|()
throws|throws
name|XMLStreamException
function_decl|;
specifier|public
name|void
name|consumeFrame
parameter_list|()
block|{
name|frame
operator|.
name|started
operator|=
literal|true
expr_stmt|;
name|frame
operator|.
name|ended
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|frame
operator|.
name|isDocument
argument_list|()
condition|)
block|{
name|currentEvent
operator|=
name|END_DOCUMENT
expr_stmt|;
block|}
else|else
block|{
name|currentEvent
operator|=
name|END_ELEMENT
expr_stmt|;
name|endElement
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#nextTag()      */
specifier|public
name|int
name|nextTag
parameter_list|()
throws|throws
name|XMLStreamException
block|{
while|while
condition|(
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|START_ELEMENT
operator|==
name|next
argument_list|()
condition|)
block|{
return|return
name|START_ELEMENT
return|;
block|}
block|}
return|return
name|currentEvent
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#hasNext()      */
specifier|public
name|boolean
name|hasNext
parameter_list|()
throws|throws
name|XMLStreamException
block|{
return|return
operator|!
operator|(
name|frame
operator|.
name|ended
operator|&&
operator|(
name|frames
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
name|frame
operator|.
name|isDocumentFragment
argument_list|()
operator|)
operator|)
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#close()      */
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#getNamespaceURI(java.lang.String)      */
specifier|public
specifier|abstract
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
function_decl|;
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#isStartElement()      */
specifier|public
name|boolean
name|isStartElement
parameter_list|()
block|{
return|return
name|currentEvent
operator|==
name|START_ELEMENT
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#isEndElement()      */
specifier|public
name|boolean
name|isEndElement
parameter_list|()
block|{
return|return
name|currentEvent
operator|==
name|END_ELEMENT
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#isCharacters()      */
specifier|public
name|boolean
name|isCharacters
parameter_list|()
block|{
return|return
name|currentEvent
operator|==
name|CHARACTERS
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see javax.xml.stream.XMLStreamReader#isWhiteSpace()      */
specifier|public
name|boolean
name|isWhiteSpace
parameter_list|()
block|{
if|if
condition|(
name|currentEvent
operator|==
name|CHARACTERS
operator|||
name|currentEvent
operator|==
name|CDATA
condition|)
block|{
name|String
name|text
init|=
name|getText
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|text
operator|.
name|length
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
name|len
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|text
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|>
literal|0x0020
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
return|return
name|currentEvent
operator|==
name|SPACE
return|;
block|}
specifier|public
name|int
name|getEventType
parameter_list|()
block|{
return|return
name|currentEvent
return|;
block|}
specifier|public
name|int
name|getTextCharacters
parameter_list|(
name|int
name|sourceStart
parameter_list|,
name|char
index|[]
name|target
parameter_list|,
name|int
name|targetStart
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|char
index|[]
name|src
init|=
name|getText
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|sourceStart
operator|+
name|length
operator|>=
name|src
operator|.
name|length
condition|)
block|{
name|length
operator|=
name|src
operator|.
name|length
operator|-
name|sourceStart
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|target
index|[
name|targetStart
operator|+
name|i
index|]
operator|=
name|src
index|[
name|i
operator|+
name|sourceStart
index|]
expr_stmt|;
block|}
return|return
name|length
return|;
block|}
specifier|public
name|boolean
name|hasText
parameter_list|()
block|{
return|return
name|currentEvent
operator|==
name|CHARACTERS
operator|||
name|currentEvent
operator|==
name|DTD
operator|||
name|currentEvent
operator|==
name|ENTITY_REFERENCE
operator|||
name|currentEvent
operator|==
name|COMMENT
operator|||
name|currentEvent
operator|==
name|SPACE
return|;
block|}
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Location
name|getLocation
parameter_list|()
block|{
return|return
operator|new
name|Location
argument_list|()
block|{
specifier|public
name|int
name|getCharacterOffset
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|int
name|getColumnNumber
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|int
name|getLineNumber
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
name|AbstractDOMStreamReader
operator|.
name|this
operator|.
name|getPublicId
argument_list|()
return|;
block|}
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
name|AbstractDOMStreamReader
operator|.
name|this
operator|.
name|getSystemId
argument_list|()
return|;
block|}
block|}
return|;
block|}
specifier|public
name|boolean
name|hasName
parameter_list|()
block|{
return|return
name|currentEvent
operator|==
name|START_ELEMENT
operator|||
name|currentEvent
operator|==
name|END_ELEMENT
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isStandalone
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|standaloneSet
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
specifier|public
name|String
name|getCharacterEncodingScheme
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

