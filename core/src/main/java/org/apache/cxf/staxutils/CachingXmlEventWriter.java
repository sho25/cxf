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
name|ArrayList
import|;
end_import

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
name|Deque
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
name|Iterator
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
name|XMLEventFactory
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|XMLEvent
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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|CachingXmlEventWriter
implements|implements
name|XMLStreamWriter
block|{
specifier|protected
name|XMLEventFactory
name|factory
decl_stmt|;
name|List
argument_list|<
name|XMLEvent
argument_list|>
name|events
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
specifier|final
name|Deque
argument_list|<
name|NSContext
argument_list|>
name|contexts
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Deque
argument_list|<
name|QName
argument_list|>
name|elNames
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|QName
name|lastStart
init|=
operator|new
name|QName
argument_list|(
literal|""
argument_list|)
decl_stmt|;
comment|// avoid push null to Deque
name|NSContext
name|curContext
init|=
operator|new
name|NSContext
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|public
name|CachingXmlEventWriter
parameter_list|()
block|{
name|factory
operator|=
name|XMLEventFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|addEvent
parameter_list|(
name|XMLEvent
name|event
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|XMLEvent
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{
comment|//nothing
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{
comment|//nothing
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|curContext
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|curContext
operator|.
name|getPrefix
argument_list|(
name|ns
argument_list|)
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
comment|//nothing
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespaceContext
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|curContext
operator|=
operator|new
name|NSContext
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|XMLConstants
operator|.
name|XMLNS_ATTRIBUTE_NS_URI
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|writeDefaultNamespace
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writeNamespace
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createAttribute
argument_list|(
name|pfx
argument_list|,
name|uri
argument_list|,
name|name
argument_list|,
name|value
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
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createCData
argument_list|(
name|arg0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
name|arg0
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
name|arg0
parameter_list|,
name|int
name|arg1
parameter_list|,
name|int
name|arg2
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createCharacters
argument_list|(
operator|new
name|String
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeComment
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createComment
argument_list|(
name|arg0
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
name|addEvent
argument_list|(
name|factory
operator|.
name|createDTD
argument_list|(
name|arg0
argument_list|)
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
name|addEvent
argument_list|(
name|factory
operator|.
name|createEndDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEntityRef
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createEntityReference
argument_list|(
name|arg0
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createProcessingInstruction
argument_list|(
name|arg0
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createProcessingInstruction
argument_list|(
name|arg0
argument_list|,
name|arg1
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
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createStartDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|addEvent
argument_list|(
name|factory
operator|.
name|createStartDocument
argument_list|(
literal|null
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createStartDocument
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|curContext
operator|.
name|addNs
argument_list|(
literal|""
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeNamespace
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|curContext
operator|.
name|addNs
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|pfx
argument_list|)
condition|)
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createNamespace
argument_list|(
name|ns
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createNamespace
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|String
name|pfx
init|=
name|StaxUtils
operator|.
name|getUniquePrefix
argument_list|(
name|this
argument_list|,
name|uri
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|addEvent
argument_list|(
name|factory
operator|.
name|createAttribute
argument_list|(
name|pfx
argument_list|,
name|uri
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|curContext
operator|.
name|addNs
argument_list|(
name|pfx
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEndElement
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|addEvent
argument_list|(
name|factory
operator|.
name|createEndElement
argument_list|(
name|lastStart
argument_list|,
name|Collections
operator|.
expr|<
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|events
operator|.
name|Namespace
operator|>
name|emptyList
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|curContext
operator|=
name|contexts
operator|.
name|pop
argument_list|()
expr_stmt|;
name|lastStart
operator|=
name|elNames
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeDefaultNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeNamespace
argument_list|(
literal|""
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|name
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
name|name
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|name
argument_list|,
name|ns
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
name|pfx
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|pfx
argument_list|,
name|name
argument_list|,
name|ns
argument_list|)
expr_stmt|;
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|elNames
operator|.
name|push
argument_list|(
name|lastStart
argument_list|)
expr_stmt|;
name|contexts
operator|.
name|push
argument_list|(
name|curContext
argument_list|)
expr_stmt|;
name|curContext
operator|=
operator|new
name|NSContext
argument_list|(
name|curContext
argument_list|)
expr_stmt|;
name|lastStart
operator|=
operator|new
name|QName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|addEvent
argument_list|(
name|factory
operator|.
name|createStartElement
argument_list|(
name|lastStart
argument_list|,
name|Collections
operator|.
name|EMPTY_SET
operator|.
name|iterator
argument_list|()
argument_list|,
name|Collections
operator|.
name|EMPTY_SET
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|elNames
operator|.
name|push
argument_list|(
name|lastStart
argument_list|)
expr_stmt|;
name|contexts
operator|.
name|push
argument_list|(
name|curContext
argument_list|)
expr_stmt|;
name|curContext
operator|=
operator|new
name|NSContext
argument_list|(
name|curContext
argument_list|)
expr_stmt|;
name|lastStart
operator|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|addEvent
argument_list|(
name|factory
operator|.
name|createStartElement
argument_list|(
name|lastStart
argument_list|,
name|Collections
operator|.
name|EMPTY_SET
operator|.
name|iterator
argument_list|()
argument_list|,
name|Collections
operator|.
name|EMPTY_SET
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|elNames
operator|.
name|push
argument_list|(
name|lastStart
argument_list|)
expr_stmt|;
name|contexts
operator|.
name|push
argument_list|(
name|curContext
argument_list|)
expr_stmt|;
name|curContext
operator|=
operator|new
name|NSContext
argument_list|(
name|curContext
argument_list|)
expr_stmt|;
name|lastStart
operator|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|name
argument_list|,
name|pfx
argument_list|)
expr_stmt|;
name|addEvent
argument_list|(
name|factory
operator|.
name|createStartElement
argument_list|(
name|lastStart
argument_list|,
name|Collections
operator|.
name|EMPTY_SET
operator|.
name|iterator
argument_list|()
argument_list|,
name|Collections
operator|.
name|EMPTY_SET
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|NSContext
implements|implements
name|NamespaceContext
block|{
name|NamespaceContext
name|parent
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|NSContext
parameter_list|(
name|NamespaceContext
name|p
parameter_list|)
block|{
name|parent
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|void
name|addNs
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
name|map
operator|.
name|put
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|ret
init|=
name|map
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|ret
operator|==
literal|null
operator|&&
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
return|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
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
name|e
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
return|return
name|e
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
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
name|e
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
name|l
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|l
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|pfx
init|=
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|pfx
operator|==
literal|null
condition|)
block|{
name|l
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
return|return
name|l
operator|.
name|iterator
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|pfx
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
return|return
name|l
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

