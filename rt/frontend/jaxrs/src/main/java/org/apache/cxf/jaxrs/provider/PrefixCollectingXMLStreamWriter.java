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

begin_comment
comment|/**  * Spy on calls to setPrefix, and collect them. This is coded to assume that  * a prefix is not reused for different URIs at different places in the tree.  * If that assumption is not valid, Jettison is hopeless.  */
end_comment

begin_class
specifier|public
class|class
name|PrefixCollectingXMLStreamWriter
implements|implements
name|XMLStreamWriter
block|{
specifier|private
name|XMLStreamWriter
name|target
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
decl_stmt|;
specifier|public
name|PrefixCollectingXMLStreamWriter
parameter_list|(
name|XMLStreamWriter
name|target
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|namespaces
operator|=
name|namespaces
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|target
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
name|target
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
name|target
operator|.
name|getNamespaceContext
argument_list|()
return|;
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
name|target
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
name|String
name|name
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|target
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
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|target
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
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|String
name|already
init|=
name|namespaces
operator|.
name|get
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|already
operator|!=
literal|null
operator|&&
operator|!
name|prefix
operator|.
name|equals
argument_list|(
name|already
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Reuse of prefix "
operator|+
name|prefix
argument_list|)
throw|;
block|}
name|namespaces
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|target
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
name|String
name|prefix
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|writeAttribute
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|localName
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|writeCData
parameter_list|(
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|target
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
name|String
name|text
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|dtd
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|prefix
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|writeEmptyElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeEmptyElement
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeEmptyElement
argument_list|(
name|localName
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
name|target
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
name|target
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEntityRef
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|prefix
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
name|String
name|pitarget
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeProcessingInstruction
argument_list|(
name|pitarget
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|String
name|pitarget
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeProcessingInstruction
argument_list|(
name|pitarget
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
name|target
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
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
name|target
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
name|writeStartDocument
parameter_list|(
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeStartDocument
argument_list|(
name|version
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
name|localName
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
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
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeStartElement
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|target
operator|.
name|writeStartElement
argument_list|(
name|localName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

