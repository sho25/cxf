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
name|jaxrs
operator|.
name|provider
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|Iterator
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|XMLInputFactory
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
name|XMLOutputFactory
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
name|common
operator|.
name|WSDLConstants
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
name|codehaus
operator|.
name|jettison
operator|.
name|AbstractXMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|badgerfish
operator|.
name|BadgerFishXMLInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|badgerfish
operator|.
name|BadgerFishXMLOutputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedNamespaceConvention
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLStreamWriter
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JSONUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|XSI_PREFIX
init|=
literal|"xsi"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XSI_URI
init|=
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSI
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Charset
name|UTF8
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
specifier|private
name|JSONUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|XMLStreamWriter
name|createBadgerFishWriter
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLOutputFactory
name|factory
init|=
operator|new
name|BadgerFishXMLOutputFactory
argument_list|()
decl_stmt|;
return|return
name|factory
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLStreamReader
name|createBadgerFishReader
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLInputFactory
name|factory
init|=
operator|new
name|BadgerFishXMLInputFactory
argument_list|()
decl_stmt|;
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLStreamWriter
name|createStreamWriter
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|QName
name|qname
parameter_list|,
name|boolean
name|writeXsiType
parameter_list|,
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|,
name|boolean
name|serializeAsArray
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|arrayKeys
parameter_list|,
name|boolean
name|dropRootElement
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|writeXsiType
condition|)
block|{
name|namespaceMap
operator|.
name|putIfAbsent
argument_list|(
name|XSI_URI
argument_list|,
name|XSI_PREFIX
argument_list|)
expr_stmt|;
block|}
name|Configuration
name|c
init|=
operator|new
name|Configuration
argument_list|(
name|namespaceMap
argument_list|)
decl_stmt|;
name|MappedNamespaceConvention
name|convention
init|=
operator|new
name|PrefixRespectingMappedNamespaceConvention
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|AbstractXMLStreamWriter
name|xsw
init|=
operator|new
name|MappedXMLStreamWriter
argument_list|(
name|convention
argument_list|,
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|,
name|UTF8
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|serializeAsArray
condition|)
block|{
if|if
condition|(
name|arrayKeys
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|arrayKeys
control|)
block|{
name|xsw
operator|.
name|seriliazeAsArray
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|key
init|=
name|getKey
argument_list|(
name|convention
argument_list|,
name|qname
argument_list|)
decl_stmt|;
name|xsw
operator|.
name|seriliazeAsArray
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
name|XMLStreamWriter
name|writer
init|=
operator|!
name|writeXsiType
operator|||
name|dropRootElement
condition|?
operator|new
name|IgnoreContentJettisonWriter
argument_list|(
name|xsw
argument_list|,
name|writeXsiType
argument_list|,
name|dropRootElement
condition|?
name|qname
else|:
literal|null
argument_list|)
else|:
name|xsw
decl_stmt|;
return|return
name|writer
return|;
block|}
specifier|public
specifier|static
name|XMLStreamWriter
name|createIgnoreMixedContentWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|boolean
name|ignoreMixedContent
parameter_list|)
block|{
return|return
name|ignoreMixedContent
condition|?
operator|new
name|IgnoreMixedContentWriter
argument_list|(
name|writer
argument_list|)
else|:
name|writer
return|;
block|}
specifier|public
specifier|static
name|XMLStreamWriter
name|createIgnoreNsWriterIfNeeded
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|boolean
name|ignoreNamespaces
parameter_list|)
block|{
return|return
name|ignoreNamespaces
condition|?
operator|new
name|IgnoreNsWriter
argument_list|(
name|writer
argument_list|)
else|:
name|writer
return|;
block|}
specifier|private
specifier|static
name|String
name|getKey
parameter_list|(
name|MappedNamespaceConvention
name|convention
parameter_list|,
name|QName
name|qname
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|convention
operator|.
name|createKey
argument_list|(
name|qname
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMLStreamReader
name|createStreamReader
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|boolean
name|readXsiType
parameter_list|,
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|readXsiType
condition|)
block|{
name|namespaceMap
operator|.
name|putIfAbsent
argument_list|(
name|XSI_URI
argument_list|,
name|XSI_PREFIX
argument_list|)
expr_stmt|;
block|}
name|MappedXMLInputFactory
name|factory
init|=
operator|new
name|MappedXMLInputFactory
argument_list|(
name|namespaceMap
argument_list|)
decl_stmt|;
return|return
operator|new
name|JettisonReader
argument_list|(
name|namespaceMap
argument_list|,
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
class|class
name|JettisonReader
extends|extends
name|DepthXMLStreamReader
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
decl_stmt|;
specifier|public
name|JettisonReader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespaceMap
operator|=
name|nsMap
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAttributePrefix
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|QName
name|name
init|=
name|getAttributeName
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|XSI_URI
operator|.
name|equals
argument_list|(
name|name
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|XSI_PREFIX
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getAttributePrefix
argument_list|(
name|n
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
operator|new
name|NamespaceContext
argument_list|()
block|{
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
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
name|entry
range|:
name|namespaceMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
return|return
name|namespaceMap
operator|.
name|get
argument_list|(
name|ns
argument_list|)
return|;
block|}
specifier|public
name|Iterator
name|getPrefixes
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|String
name|prefix
init|=
name|getPrefix
argument_list|(
name|ns
argument_list|)
decl_stmt|;
return|return
name|prefix
operator|==
literal|null
condition|?
literal|null
else|:
name|Collections
operator|.
name|singletonList
argument_list|(
name|prefix
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
specifier|private
specifier|static
class|class
name|IgnoreContentJettisonWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|private
name|boolean
name|writeXsiType
decl_stmt|;
specifier|private
name|QName
name|ignoredQName
decl_stmt|;
specifier|private
name|boolean
name|rootDropped
decl_stmt|;
specifier|private
name|int
name|index
decl_stmt|;
specifier|public
name|IgnoreContentJettisonWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|boolean
name|writeXsiType
parameter_list|,
name|QName
name|qname
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|this
operator|.
name|writeXsiType
operator|=
name|writeXsiType
expr_stmt|;
name|ignoredQName
operator|=
name|qname
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
if|if
condition|(
operator|!
name|writeXsiType
operator|&&
literal|"type"
operator|.
name|equals
argument_list|(
name|local
argument_list|)
operator|&&
literal|"xsi"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return;
block|}
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
name|index
operator|++
expr_stmt|;
if|if
condition|(
name|ignoredQName
operator|!=
literal|null
operator|&&
name|ignoredQName
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|local
argument_list|)
operator|&&
name|ignoredQName
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|rootDropped
operator|=
literal|true
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|local
argument_list|,
name|uri
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
name|index
operator|--
expr_stmt|;
if|if
condition|(
name|rootDropped
operator|&&
name|index
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|IgnoreMixedContentWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
name|String
name|lastText
decl_stmt|;
name|boolean
name|isMixed
decl_stmt|;
name|List
argument_list|<
name|Boolean
argument_list|>
name|mixed
init|=
operator|new
name|LinkedList
argument_list|<
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|IgnoreMixedContentWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|text
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|lastText
operator|=
name|text
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|lastText
operator|+=
name|text
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isMixed
condition|)
block|{
name|super
operator|.
name|writeCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lastText
operator|=
name|text
expr_stmt|;
block|}
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
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|isMixed
operator|=
literal|true
expr_stmt|;
block|}
name|mixed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|isMixed
argument_list|)
expr_stmt|;
name|lastText
operator|=
literal|null
expr_stmt|;
name|isMixed
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|local
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|isMixed
operator|=
literal|true
expr_stmt|;
block|}
name|mixed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|isMixed
argument_list|)
expr_stmt|;
name|lastText
operator|=
literal|null
expr_stmt|;
name|isMixed
operator|=
literal|false
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
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|isMixed
operator|=
literal|true
expr_stmt|;
block|}
name|mixed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|isMixed
argument_list|)
expr_stmt|;
name|lastText
operator|=
literal|null
expr_stmt|;
name|isMixed
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|writeStartElement
argument_list|(
name|local
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
if|if
condition|(
name|lastText
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|isMixed
operator|||
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|lastText
operator|.
name|trim
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|super
operator|.
name|writeCharacters
argument_list|(
name|lastText
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|isMixed
operator|=
name|mixed
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mixed
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|IgnoreNsWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|public
name|IgnoreNsWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|writer
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
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|super
operator|.
name|writeStartElement
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
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
name|super
operator|.
name|writeStartElement
argument_list|(
name|local
argument_list|)
expr_stmt|;
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
comment|// completed
block|}
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|// completed
block|}
block|}
block|}
end_class

end_unit

