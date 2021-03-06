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
operator|.
name|transform
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
name|io
operator|.
name|OutputStream
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|XMLStreamReader
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|TransformUtils
block|{
specifier|private
name|TransformUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|XMLStreamReader
name|createNewReaderIfNeeded
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|InputStream
name|is
parameter_list|)
block|{
return|return
name|reader
operator|==
literal|null
condition|?
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
else|:
name|reader
return|;
block|}
specifier|public
specifier|static
name|XMLStreamWriter
name|createNewWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
return|return
name|createNewWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|os
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLStreamWriter
name|createNewWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
return|return
name|writer
return|;
block|}
elseif|else
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
return|return
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|,
name|encoding
argument_list|)
return|;
block|}
return|return
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLStreamWriter
name|createTransformWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElementsMap
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|outDropElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAppendMap
parameter_list|,
name|boolean
name|attributesToElements
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
block|{
if|if
condition|(
name|outElementsMap
operator|!=
literal|null
operator|||
name|outDropElements
operator|!=
literal|null
operator|||
name|outAppendMap
operator|!=
literal|null
operator|||
name|attributesToElements
condition|)
block|{
name|writer
operator|=
operator|new
name|OutTransformWriter
argument_list|(
name|createNewWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|os
argument_list|)
argument_list|,
name|outElementsMap
argument_list|,
name|outAppendMap
argument_list|,
name|outDropElements
argument_list|,
literal|null
argument_list|,
name|attributesToElements
argument_list|,
name|defaultNamespace
argument_list|)
expr_stmt|;
block|}
return|return
name|writer
return|;
block|}
comment|//CHECKSTYLE:OFF ParameterNumber
specifier|public
specifier|static
name|XMLStreamWriter
name|createTransformWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outElementsMap
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|outDropElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAppendMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAttributesMap
parameter_list|,
name|boolean
name|attributesToElements
parameter_list|,
name|String
name|defaultNamespace
parameter_list|,
name|String
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|outElementsMap
operator|!=
literal|null
operator|||
name|outDropElements
operator|!=
literal|null
operator|||
name|outAppendMap
operator|!=
literal|null
operator|||
name|attributesToElements
condition|)
block|{
name|writer
operator|=
operator|new
name|OutTransformWriter
argument_list|(
name|createNewWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|os
argument_list|,
name|encoding
argument_list|)
argument_list|,
name|outElementsMap
argument_list|,
name|outAppendMap
argument_list|,
name|outDropElements
argument_list|,
name|outAttributesMap
argument_list|,
name|attributesToElements
argument_list|,
name|defaultNamespace
argument_list|)
expr_stmt|;
block|}
return|return
name|writer
return|;
block|}
comment|//CHECKSTYLE:ON
specifier|public
specifier|static
name|XMLStreamReader
name|createTransformReaderIfNeeded
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|inDropElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElementsMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inAppendMap
parameter_list|,
name|boolean
name|blockOriginalReader
parameter_list|)
block|{
return|return
name|createTransformReaderIfNeeded
argument_list|(
name|reader
argument_list|,
name|is
argument_list|,
name|inDropElements
argument_list|,
name|inElementsMap
argument_list|,
name|inAppendMap
argument_list|,
literal|null
argument_list|,
name|blockOriginalReader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLStreamReader
name|createTransformReaderIfNeeded
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|inDropElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inElementsMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inAppendMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inAttributesMap
parameter_list|,
name|boolean
name|blockOriginalReader
parameter_list|)
block|{
if|if
condition|(
name|inElementsMap
operator|!=
literal|null
operator|||
name|inAppendMap
operator|!=
literal|null
operator|||
name|inDropElements
operator|!=
literal|null
operator|||
name|inAttributesMap
operator|!=
literal|null
condition|)
block|{
name|reader
operator|=
operator|new
name|InTransformReader
argument_list|(
name|createNewReaderIfNeeded
argument_list|(
name|reader
argument_list|,
name|is
argument_list|)
argument_list|,
name|inElementsMap
argument_list|,
name|inAppendMap
argument_list|,
name|inDropElements
argument_list|,
name|inAttributesMap
argument_list|,
name|blockOriginalReader
argument_list|)
expr_stmt|;
block|}
return|return
name|reader
return|;
block|}
specifier|protected
specifier|static
name|void
name|convertToQNamesMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|QNamesMap
name|elementsMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|)
block|{
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|QName
name|lname
init|=
name|DOMUtils
operator|.
name|convertStringToQName
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|QName
name|rname
init|=
name|DOMUtils
operator|.
name|convertStringToQName
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|elementsMap
operator|.
name|put
argument_list|(
name|lname
argument_list|,
name|rname
argument_list|)
expr_stmt|;
if|if
condition|(
name|nsMap
operator|!=
literal|null
operator|&&
operator|!
name|isEmptyQName
argument_list|(
name|rname
argument_list|)
operator|&&
operator|(
literal|"*"
operator|.
name|equals
argument_list|(
name|lname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
literal|"*"
operator|.
name|equals
argument_list|(
name|rname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|nsMap
operator|.
name|put
argument_list|(
name|lname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|rname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|static
name|void
name|convertToMapOfElementProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|ElementProperty
argument_list|>
name|elementsMap
parameter_list|)
block|{
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|text
init|=
literal|null
decl_stmt|;
name|boolean
name|child
init|=
literal|false
decl_stmt|;
comment|// if the content delimiter is present in the value, extract the content
name|int
name|d
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|'}'
argument_list|)
decl_stmt|;
name|d
operator|=
name|value
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|,
name|d
operator|<
literal|0
condition|?
literal|0
else|:
name|d
argument_list|)
expr_stmt|;
if|if
condition|(
name|d
operator|>
literal|0
condition|)
block|{
name|text
operator|=
name|value
operator|.
name|substring
argument_list|(
name|d
operator|+
literal|1
argument_list|)
expr_stmt|;
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
comment|// if the trailer delimiter is present in the key, remove it
if|if
condition|(
name|key
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|key
operator|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|key
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|child
operator|=
literal|true
expr_stmt|;
block|}
name|QName
name|lname
init|=
name|DOMUtils
operator|.
name|convertStringToQName
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|QName
name|rname
init|=
name|DOMUtils
operator|.
name|convertStringToQName
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|ElementProperty
name|desc
init|=
operator|new
name|ElementProperty
argument_list|(
name|rname
argument_list|,
name|text
argument_list|,
name|child
argument_list|)
decl_stmt|;
name|elementsMap
operator|.
name|put
argument_list|(
name|lname
argument_list|,
name|desc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
specifier|static
name|void
name|convertToSetOfQNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|set
parameter_list|,
name|Set
argument_list|<
name|QName
argument_list|>
name|elementsSet
parameter_list|)
block|{
if|if
condition|(
name|set
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|entry
range|:
name|set
control|)
block|{
name|QName
name|name
init|=
name|DOMUtils
operator|.
name|convertStringToQName
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|elementsSet
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|static
name|boolean
name|isEmptyQName
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
name|XMLConstants
operator|.
name|NULL_NS_URI
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|static
name|ParsingEvent
name|createStartElementEvent
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
operator|new
name|ParsingEvent
argument_list|(
name|XMLStreamConstants
operator|.
name|START_ELEMENT
argument_list|,
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|static
name|ParsingEvent
name|createEndElementEvent
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
return|return
operator|new
name|ParsingEvent
argument_list|(
name|XMLStreamConstants
operator|.
name|END_ELEMENT
argument_list|,
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|static
name|ParsingEvent
name|createCharactersEvent
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|ParsingEvent
argument_list|(
name|XMLStreamConstants
operator|.
name|CHARACTERS
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

