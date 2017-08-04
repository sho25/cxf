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
name|xml
operator|.
name|stax
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|xml
operator|.
name|AbstractMessageReader
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
name|staxutils
operator|.
name|DepthXMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
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
comment|/**  * Reads literal encoded messages.  */
end_comment

begin_class
specifier|public
class|class
name|ElementReader
extends|extends
name|AbstractMessageReader
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|QNAME_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"([^:]+):([^:]+)"
argument_list|)
decl_stmt|;
specifier|private
name|DepthXMLStreamReader
name|root
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
specifier|private
name|String
name|localName
decl_stmt|;
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|QName
name|xsiType
decl_stmt|;
specifier|private
name|boolean
name|hasCheckedChildren
decl_stmt|;
specifier|private
name|boolean
name|hasChildren
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|int
name|depth
decl_stmt|;
specifier|private
name|int
name|currentAttribute
decl_stmt|;
comment|/**      * @param root      */
specifier|public
name|ElementReader
parameter_list|(
name|DepthXMLStreamReader
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
name|this
operator|.
name|localName
operator|=
name|root
operator|.
name|getLocalName
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|root
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|root
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|extractXsiType
argument_list|()
expr_stmt|;
name|depth
operator|=
name|root
operator|.
name|getDepth
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ElementReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|this
argument_list|(
name|reader
operator|instanceof
name|DepthXMLStreamReader
condition|?
operator|(
name|DepthXMLStreamReader
operator|)
name|reader
else|:
operator|new
name|DepthXMLStreamReader
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param is      * @throws XMLStreamException      */
specifier|public
name|ElementReader
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|// XMLInputFactory factory = XMLInputFactory.newInstance();
comment|// XMLStreamReader xmlReader = factory.createXMLStreamReader(is);
name|XMLStreamReader
name|xmlReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|xmlReader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|this
operator|.
name|root
operator|=
operator|new
name|DepthXMLStreamReader
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
name|this
operator|.
name|localName
operator|=
name|root
operator|.
name|getLocalName
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|root
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|root
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|extractXsiType
argument_list|()
expr_stmt|;
name|depth
operator|=
name|root
operator|.
name|getDepth
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|extractXsiType
parameter_list|()
block|{
comment|/*          * We're making a conscious choice here -- garbage in == garbage out.          */
name|String
name|xsiTypeQname
init|=
name|root
operator|.
name|getAttributeValue
argument_list|(
name|Constants
operator|.
name|URI_2001_SCHEMA_XSI
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
if|if
condition|(
name|xsiTypeQname
operator|!=
literal|null
condition|)
block|{
name|Matcher
name|m
init|=
name|QNAME_PATTERN
operator|.
name|matcher
argument_list|(
name|xsiTypeQname
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|NamespaceContext
name|nc
init|=
name|root
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
name|this
operator|.
name|xsiType
operator|=
operator|new
name|QName
argument_list|(
name|nc
operator|.
name|getNamespaceURI
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|,
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|xsiType
operator|=
operator|new
name|QName
argument_list|(
name|this
operator|.
name|namespace
argument_list|,
name|xsiTypeQname
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|isXsiNil
argument_list|()
condition|)
block|{
name|readToEnd
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
name|value
operator|=
name|root
operator|.
name|getElementText
argument_list|()
expr_stmt|;
name|hasCheckedChildren
operator|=
literal|true
expr_stmt|;
name|hasChildren
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|root
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|root
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
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not read XML stream."
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
block|}
return|return
name|value
return|;
block|}
specifier|public
name|String
name|getValue
parameter_list|(
name|String
name|ns
parameter_list|,
name|String
name|attr
parameter_list|)
block|{
return|return
name|root
operator|.
name|getAttributeValue
argument_list|(
name|ns
argument_list|,
name|attr
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasMoreElementReaders
parameter_list|()
block|{
comment|// Check to see if we checked before,
comment|// so we don't mess up the stream position.
if|if
condition|(
operator|!
name|hasCheckedChildren
condition|)
block|{
name|checkHasMoreChildReaders
argument_list|()
expr_stmt|;
block|}
return|return
name|hasChildren
return|;
block|}
specifier|private
name|boolean
name|checkHasMoreChildReaders
parameter_list|()
block|{
try|try
block|{
name|int
name|event
init|=
name|root
operator|.
name|getEventType
argument_list|()
decl_stmt|;
while|while
condition|(
name|root
operator|.
name|hasNext
argument_list|()
condition|)
block|{
switch|switch
condition|(
name|event
condition|)
block|{
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
if|if
condition|(
name|root
operator|.
name|getDepth
argument_list|()
operator|>
name|depth
condition|)
block|{
name|hasCheckedChildren
operator|=
literal|true
expr_stmt|;
name|hasChildren
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
if|if
condition|(
name|root
operator|.
name|getDepth
argument_list|()
operator|<
name|depth
condition|)
block|{
name|hasCheckedChildren
operator|=
literal|true
expr_stmt|;
name|hasChildren
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|root
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|root
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_DOCUMENT
case|:
comment|// We should never get here...
name|hasCheckedChildren
operator|=
literal|true
expr_stmt|;
name|hasChildren
operator|=
literal|false
expr_stmt|;
return|return
literal|false
return|;
default|default:
break|break;
block|}
if|if
condition|(
name|root
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|event
operator|=
name|root
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
name|hasCheckedChildren
operator|=
literal|true
expr_stmt|;
name|hasChildren
operator|=
literal|false
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Error parsing document."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|MessageReader
name|getNextElementReader
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasCheckedChildren
condition|)
block|{
name|checkHasMoreChildReaders
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|hasChildren
condition|)
block|{
return|return
literal|null
return|;
block|}
name|hasCheckedChildren
operator|=
literal|false
expr_stmt|;
return|return
operator|new
name|ElementReader
argument_list|(
name|root
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
return|return
name|localName
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|QName
name|getXsiType
parameter_list|()
block|{
return|return
name|xsiType
return|;
block|}
specifier|public
name|XMLStreamReader
name|getXMLStreamReader
parameter_list|()
block|{
return|return
name|root
return|;
block|}
specifier|public
name|boolean
name|hasMoreAttributeReaders
parameter_list|()
block|{
if|if
condition|(
operator|!
name|root
operator|.
name|isStartElement
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|currentAttribute
operator|<
name|root
operator|.
name|getAttributeCount
argument_list|()
return|;
block|}
specifier|public
name|MessageReader
name|getAttributeReader
parameter_list|(
name|QName
name|qName
parameter_list|)
block|{
name|String
name|attribute
init|=
name|root
operator|.
name|getAttributeValue
argument_list|(
name|qName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
operator|&&
literal|""
operator|.
name|equals
argument_list|(
name|qName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
comment|// The qName namespaceURI of the attribute seems to be null
comment|// rather than "" when using the ibmjdk.
comment|// The MtomTest systest fails unless we do this.
name|attribute
operator|=
name|root
operator|.
name|getAttributeValue
argument_list|(
literal|null
argument_list|,
name|qName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|AttributeReader
argument_list|(
name|qName
argument_list|,
name|attribute
argument_list|)
return|;
block|}
specifier|public
name|MessageReader
name|getNextAttributeReader
parameter_list|()
block|{
name|MessageReader
name|reader
init|=
operator|new
name|AttributeReader
argument_list|(
name|root
operator|.
name|getAttributeName
argument_list|(
name|currentAttribute
argument_list|)
argument_list|,
name|root
operator|.
name|getAttributeValue
argument_list|(
name|currentAttribute
argument_list|)
argument_list|)
decl_stmt|;
name|currentAttribute
operator|++
expr_stmt|;
return|return
name|reader
return|;
block|}
specifier|public
name|String
name|getNamespaceForPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
return|return
name|root
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

