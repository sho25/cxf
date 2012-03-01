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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|XMLUtils
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
name|DelegatingXMLStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|OutTransformWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|private
name|String
name|defaultNamespace
decl_stmt|;
specifier|private
name|QNamesMap
name|elementsMap
decl_stmt|;
specifier|private
name|QNamesMap
name|attributesMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|QName
argument_list|,
name|ElementProperty
argument_list|>
name|appendMap
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|ElementProperty
argument_list|>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|writtenUris
init|=
operator|new
name|LinkedList
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|QName
argument_list|>
name|dropElements
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|List
argument_list|<
name|ParsingEvent
argument_list|>
argument_list|>
name|pushedAheadEvents
init|=
operator|new
name|Stack
argument_list|<
name|List
argument_list|<
name|ParsingEvent
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|QName
argument_list|>
name|elementsStack
init|=
operator|new
name|Stack
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|String
name|replaceText
decl_stmt|;
specifier|private
name|int
name|currentDepth
decl_stmt|;
specifier|private
name|int
name|dropDepth
decl_stmt|;
specifier|private
name|boolean
name|attributesToElements
decl_stmt|;
specifier|private
name|DelegatingNamespaceContext
name|namespaceContext
decl_stmt|;
specifier|public
name|OutTransformWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|append
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|dropEls
parameter_list|,
name|boolean
name|attributesToElements
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
block|{
name|this
argument_list|(
name|writer
argument_list|,
name|outMap
argument_list|,
name|append
argument_list|,
name|dropEls
argument_list|,
literal|null
argument_list|,
name|attributesToElements
argument_list|,
name|defaultNamespace
argument_list|)
expr_stmt|;
block|}
specifier|public
name|OutTransformWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outEMap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|append
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|dropEls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outAMap
parameter_list|,
name|boolean
name|attributesToElements
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|elementsMap
operator|=
operator|new
name|QNamesMap
argument_list|(
name|outEMap
operator|==
literal|null
condition|?
literal|0
else|:
name|outEMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|attributesMap
operator|=
operator|new
name|QNamesMap
argument_list|(
name|outAMap
operator|==
literal|null
condition|?
literal|0
else|:
name|outAMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|TransformUtils
operator|.
name|convertToQNamesMap
argument_list|(
name|outEMap
argument_list|,
name|elementsMap
argument_list|,
name|nsMap
argument_list|)
expr_stmt|;
name|TransformUtils
operator|.
name|convertToQNamesMap
argument_list|(
name|outAMap
argument_list|,
name|attributesMap
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|TransformUtils
operator|.
name|convertToMapOfElementProperties
argument_list|(
name|append
argument_list|,
name|appendMap
argument_list|)
expr_stmt|;
name|dropElements
operator|=
name|XMLUtils
operator|.
name|convertStringsToQNames
argument_list|(
name|dropEls
argument_list|)
expr_stmt|;
name|this
operator|.
name|attributesToElements
operator|=
name|attributesToElements
expr_stmt|;
name|namespaceContext
operator|=
operator|new
name|DelegatingNamespaceContext
argument_list|(
name|writer
operator|.
name|getNamespaceContext
argument_list|()
argument_list|,
name|nsMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultNamespace
operator|=
name|defaultNamespace
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeNamespace
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|matchesDropped
argument_list|(
literal|true
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|value
init|=
name|nsMap
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|uri
operator|=
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|uri
expr_stmt|;
if|if
condition|(
name|writtenUris
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
operator|&&
operator|(
name|prefix
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|||
name|prefix
operator|.
name|equals
argument_list|(
name|getPrefix
argument_list|(
name|uri
argument_list|)
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|defaultNamespace
operator|!=
literal|null
operator|&&
name|defaultNamespace
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|super
operator|.
name|writeDefaultNamespace
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
else|else
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
name|prefix
operator|=
name|namespaceContext
operator|.
name|findUniquePrefix
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
name|writtenUris
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentDepth
operator|++
expr_stmt|;
if|if
condition|(
name|matchesDropped
argument_list|(
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|s
decl_stmt|;
if|if
condition|(
name|writtenUris
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|s
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|s
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|writtenUris
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|writtenUris
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
expr_stmt|;
specifier|final
name|QName
name|theName
init|=
operator|new
name|QName
argument_list|(
name|uri
argument_list|,
name|local
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|final
name|ElementProperty
name|appendProp
init|=
name|appendMap
operator|.
name|remove
argument_list|(
name|theName
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|replaceContent
init|=
name|appendProp
operator|!=
literal|null
operator|&&
name|theName
operator|.
name|equals
argument_list|(
name|appendProp
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|boolean
name|dropped
init|=
name|dropElements
operator|.
name|contains
argument_list|(
name|theName
argument_list|)
decl_stmt|;
name|QName
name|expected
init|=
name|elementsMap
operator|.
name|get
argument_list|(
name|theName
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
name|expected
operator|=
name|theName
expr_stmt|;
block|}
else|else
block|{
name|expected
operator|=
operator|new
name|QName
argument_list|(
name|expected
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|expected
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ParsingEvent
argument_list|>
name|pe
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|appendProp
operator|!=
literal|null
operator|&&
operator|!
name|replaceContent
condition|)
block|{
if|if
condition|(
operator|!
name|appendProp
operator|.
name|isChild
argument_list|()
condition|)
block|{
comment|// ap-pre-*
name|QName
name|appendQName
init|=
name|appendProp
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|theprefix
init|=
name|namespaceContext
operator|.
name|getPrefix
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|nsadded
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|theprefix
operator|==
literal|null
condition|)
block|{
name|nsadded
operator|=
literal|true
expr_stmt|;
name|theprefix
operator|=
name|getPrefix
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|theprefix
operator|==
literal|null
operator|&&
operator|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|expected
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
name|expected
operator|.
name|getPrefix
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|)
condition|)
block|{
name|theprefix
operator|=
name|expected
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|theprefix
operator|==
literal|null
condition|)
block|{
name|theprefix
operator|=
name|namespaceContext
operator|.
name|findUniquePrefix
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|theprefix
operator|==
literal|null
condition|)
block|{
name|theprefix
operator|=
literal|""
expr_stmt|;
block|}
block|}
name|write
argument_list|(
operator|new
name|QName
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|appendQName
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|theprefix
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|nsadded
operator|&&
name|theprefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|writeNamespace
argument_list|(
name|theprefix
argument_list|,
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|appendProp
operator|.
name|getText
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// ap-pre-wrap
name|currentDepth
operator|++
expr_stmt|;
name|pe
operator|=
operator|new
name|ArrayList
argument_list|<
name|ParsingEvent
argument_list|>
argument_list|()
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createEndElementEvent
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createEndElementEvent
argument_list|(
name|appendProp
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|pushedAheadEvents
operator|.
name|push
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|elementsStack
operator|.
name|push
argument_list|(
name|appendQName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ap-pre-incl
name|super
operator|.
name|writeCharacters
argument_list|(
name|appendProp
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|replaceContent
condition|)
block|{
comment|//
name|replaceText
operator|=
name|appendProp
operator|.
name|getText
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dropped
condition|)
block|{
comment|// unwrap the current element (shallow drop)
name|elementsStack
operator|.
name|push
argument_list|(
name|theName
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|TransformUtils
operator|.
name|isEmptyQName
argument_list|(
name|expected
argument_list|)
condition|)
block|{
comment|// skip the current element (deep drop));
name|dropDepth
operator|=
name|currentDepth
operator|-
literal|1
expr_stmt|;
return|return;
block|}
name|write
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|pushedAheadEvents
operator|.
name|push
argument_list|(
name|pe
argument_list|)
expr_stmt|;
name|elementsStack
operator|.
name|push
argument_list|(
name|expected
argument_list|)
expr_stmt|;
if|if
condition|(
name|appendProp
operator|!=
literal|null
operator|&&
operator|!
name|replaceContent
operator|&&
name|appendProp
operator|.
name|isChild
argument_list|()
condition|)
block|{
comment|// ap-post-*
name|QName
name|appendQName
init|=
name|appendProp
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|theprefix
init|=
name|getPrefix
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|appendProp
operator|.
name|getText
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// ap-post-wrap
name|write
argument_list|(
operator|new
name|QName
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|appendQName
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|theprefix
operator|==
literal|null
condition|?
literal|""
else|:
name|theprefix
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|getNamespaceContext
argument_list|()
operator|.
name|getPrefix
argument_list|(
name|appendQName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|writeNamespace
argument_list|(
name|theprefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
name|currentDepth
operator|++
expr_stmt|;
name|pe
operator|=
operator|new
name|ArrayList
argument_list|<
name|ParsingEvent
argument_list|>
argument_list|()
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createEndElementEvent
argument_list|(
name|appendProp
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createEndElementEvent
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
name|pushedAheadEvents
operator|.
name|push
argument_list|(
name|pe
argument_list|)
expr_stmt|;
name|elementsStack
operator|.
name|push
argument_list|(
name|appendQName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ap-post-incl
name|pushedAheadEvents
operator|.
name|pop
argument_list|()
expr_stmt|;
name|pe
operator|=
operator|new
name|ArrayList
argument_list|<
name|ParsingEvent
argument_list|>
argument_list|()
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createStartElementEvent
argument_list|(
name|appendProp
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createCharactersEvent
argument_list|(
name|appendProp
operator|.
name|getText
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createEndElementEvent
argument_list|(
name|appendProp
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|pe
operator|.
name|add
argument_list|(
name|TransformUtils
operator|.
name|createEndElementEvent
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
name|pushedAheadEvents
operator|.
name|push
argument_list|(
name|pe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|pushedAheadEvents
operator|.
name|push
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|elementsStack
operator|.
name|push
argument_list|(
operator|new
name|QName
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|writeStartElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|writeStartElement
argument_list|(
literal|""
argument_list|,
name|local
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeEndElement
parameter_list|()
throws|throws
name|XMLStreamException
block|{
operator|--
name|currentDepth
expr_stmt|;
if|if
condition|(
name|matchesDropped
argument_list|(
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
elseif|else
if|if
condition|(
name|dropDepth
operator|>
literal|0
condition|)
block|{
name|dropDepth
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|writtenUris
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|writtenUris
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|QName
name|theName
init|=
name|elementsStack
operator|.
name|pop
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|dropped
init|=
name|dropElements
operator|.
name|contains
argument_list|(
name|theName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|dropped
condition|)
block|{
name|List
argument_list|<
name|ParsingEvent
argument_list|>
name|pes
init|=
name|pushedAheadEvents
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|pes
condition|)
block|{
for|for
control|(
name|ParsingEvent
name|pe
range|:
name|pes
control|)
block|{
switch|switch
condition|(
name|pe
operator|.
name|getEvent
argument_list|()
condition|)
block|{
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
name|write
argument_list|(
name|pe
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
name|super
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|CHARACTERS
case|:
name|super
operator|.
name|writeCharacters
argument_list|(
name|pe
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
block|}
block|}
block|}
else|else
block|{
name|super
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
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
if|if
condition|(
name|matchesDropped
argument_list|(
literal|false
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|replaceText
operator|!=
literal|null
condition|)
block|{
name|text
operator|=
name|replaceText
expr_stmt|;
name|replaceText
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|writeCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|write
parameter_list|(
name|QName
name|qname
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|boolean
name|writeNs
init|=
literal|false
decl_stmt|;
name|String
name|prefix
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|qname
operator|.
name|getPrefix
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|prefix
operator|=
name|getPrefix
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
name|namespaceContext
operator|.
name|findUniquePrefix
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
name|writeNs
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
name|prefix
operator|=
name|qname
operator|.
name|getPrefix
argument_list|()
expr_stmt|;
name|namespaceContext
operator|.
name|addPrefix
argument_list|(
name|prefix
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|defaultNamespace
operator|!=
literal|null
operator|&&
name|defaultNamespace
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
name|super
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|writeNs
condition|)
block|{
name|this
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|matchesDropped
parameter_list|(
name|boolean
name|shallow
parameter_list|)
block|{
if|if
condition|(
operator|(
name|dropDepth
operator|>
literal|0
operator|&&
name|dropDepth
operator|<=
name|currentDepth
operator|)
operator|||
operator|(
name|shallow
operator|&&
operator|(
name|elementsStack
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|dropElements
operator|.
name|contains
argument_list|(
name|elementsStack
operator|.
name|peek
argument_list|()
argument_list|)
operator|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|namespaceContext
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
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
name|QName
name|expected
init|=
name|attributesMap
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
name|uri
argument_list|,
name|local
argument_list|,
name|prefix
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|TransformUtils
operator|.
name|isEmptyQName
argument_list|(
name|expected
argument_list|)
condition|)
block|{
return|return;
block|}
name|uri
operator|=
name|expected
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|local
operator|=
name|expected
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|attributesToElements
condition|)
block|{
name|super
operator|.
name|writeAttribute
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|,
name|local
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeAttributeAsElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
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
name|String
name|uri
init|=
name|XMLConstants
operator|.
name|NULL_NS_URI
decl_stmt|;
name|QName
name|expected
init|=
name|attributesMap
operator|.
name|get
argument_list|(
operator|new
name|QName
argument_list|(
literal|""
argument_list|,
name|local
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|TransformUtils
operator|.
name|isEmptyQName
argument_list|(
name|expected
argument_list|)
condition|)
block|{
return|return;
block|}
name|uri
operator|=
name|expected
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|local
operator|=
name|expected
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|attributesToElements
condition|)
block|{
if|if
condition|(
name|uri
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|super
operator|.
name|writeAttribute
argument_list|(
name|uri
argument_list|,
name|local
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|writeAttribute
argument_list|(
name|local
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|writeAttributeAsElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeAttributeAsElement
parameter_list|(
name|String
name|uri
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
name|this
operator|.
name|writeStartElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
expr_stmt|;
name|this
operator|.
name|writeCharacters
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

