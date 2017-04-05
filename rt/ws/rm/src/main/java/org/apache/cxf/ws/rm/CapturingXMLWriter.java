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
name|ws
operator|.
name|rm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|HashMap
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
name|LoadingByteArrayOutputStream
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
name|cxf
operator|.
name|staxutils
operator|.
name|transform
operator|.
name|OutTransformWriter
import|;
end_import

begin_class
specifier|public
class|class
name|CapturingXMLWriter
implements|implements
name|XMLStreamWriter
block|{
name|XMLStreamWriter
name|delegate
decl_stmt|;
name|XMLStreamWriter
name|capture
decl_stmt|;
name|LoadingByteArrayOutputStream
name|bos
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Throwable
name|throwable
decl_stmt|;
specifier|public
name|CapturingXMLWriter
parameter_list|(
name|XMLStreamWriter
name|del
parameter_list|)
block|{
name|delegate
operator|=
name|del
expr_stmt|;
name|capture
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|bos
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
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
name|map
operator|.
name|put
argument_list|(
literal|"{http://schemas.xmlsoap.org/ws/2005/02/rm}Sequence"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"{http://schemas.xmlsoap.org/ws/2005/02/rm}SequenceAcknowledgement"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"{http://docs.oasis-open.org/ws-rx/wsrm/200702}Sequence"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"{http://docs.oasis-open.org/ws-rx/wsrm/200702}SequenceAcknowledgement"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|capture
operator|=
operator|new
name|OutTransformWriter
argument_list|(
name|capture
argument_list|,
name|map
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|setDefaultNamespace
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|ctx
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|setNamespaceContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|setNamespaceContext
argument_list|(
name|ctx
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|setPrefix
argument_list|(
name|pfx
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|setPrefix
argument_list|(
name|pfx
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
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
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
specifier|public
name|void
name|writeAttribute
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
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
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeAttribute
argument_list|(
name|local
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeAttribute
argument_list|(
name|local
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
name|cdata
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeCData
argument_list|(
name|cdata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeCData
argument_list|(
name|cdata
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeCharacters
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeCharacters
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|,
name|arg2
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|text
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeComment
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeComment
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeDefaultNamespace
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeDefaultNamespace
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeDefaultNamespace
argument_list|(
name|uri
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeDTD
argument_list|(
name|dtd
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeEmptyElement
argument_list|(
name|prefix
argument_list|,
name|local
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeEmptyElement
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
name|writeEmptyElement
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeEmptyElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeEmptyElement
argument_list|(
name|uri
argument_list|,
name|local
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeEmptyElement
argument_list|(
name|localName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|ent
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeEntityRef
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeEntityRef
argument_list|(
name|ent
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
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeNamespace
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeProcessingInstruction
argument_list|(
name|target
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeProcessingInstruction
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeProcessingInstruction
argument_list|(
name|target
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|ver
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeStartDocument
argument_list|(
name|encoding
argument_list|,
name|ver
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeStartDocument
argument_list|(
name|encoding
argument_list|,
name|ver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|String
name|ver
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeStartDocument
argument_list|(
name|ver
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeStartDocument
argument_list|(
name|ver
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
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
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeStartElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
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
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|writeStartElement
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
name|capture
operator|.
name|writeStartElement
argument_list|(
name|local
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|delegate
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|stopToDelegate
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|getPrefix
argument_list|(
name|uri
argument_list|)
return|;
block|}
return|return
name|capture
operator|.
name|getPrefix
argument_list|(
name|uri
argument_list|)
return|;
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|getNamespaceContext
argument_list|()
return|;
block|}
return|return
name|capture
operator|.
name|getNamespaceContext
argument_list|()
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
return|return
name|delegate
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
return|return
name|capture
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|LoadingByteArrayOutputStream
name|getOutputStream
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|capture
operator|.
name|flush
argument_list|()
expr_stmt|;
name|capture
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
return|;
block|}
specifier|public
name|Throwable
name|getThrowable
parameter_list|()
block|{
return|return
name|throwable
return|;
block|}
comment|//if there is some problem writing to the original output, we need to stop writing
comment|// to the output, but keep capturing the message so we can try and resend later
specifier|private
name|void
name|stopToDelegate
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|throwable
operator|=
name|t
expr_stmt|;
name|delegate
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

