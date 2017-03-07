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
name|databinding
operator|.
name|source
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Validator
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
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
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
name|i18n
operator|.
name|Message
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
name|logging
operator|.
name|LogUtils
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
name|databinding
operator|.
name|DataWriter
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
name|interceptor
operator|.
name|Fault
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
name|message
operator|.
name|Attachment
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|W3CDOMStreamWriter
import|;
end_import

begin_class
specifier|public
class|class
name|XMLStreamDataWriter
implements|implements
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|XMLStreamDataWriter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Schema
name|schema
decl_stmt|;
specifier|public
name|XMLStreamDataWriter
parameter_list|()
block|{              }
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|MessagePartInfo
name|part
parameter_list|,
name|XMLStreamWriter
name|output
parameter_list|)
block|{
name|write
argument_list|(
name|obj
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|Object
name|obj
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
try|try
block|{
name|XMLStreamReader
name|reader
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|DataSource
condition|)
block|{
name|DataSource
name|ds
init|=
operator|(
name|DataSource
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|DOMSource
name|domSource
init|=
operator|new
name|DOMSource
argument_list|(
name|StaxUtils
operator|.
name|read
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Validator
name|schemaValidator
init|=
name|schema
operator|.
name|newValidator
argument_list|()
decl_stmt|;
name|schemaValidator
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|MtomValidationErrorHandler
argument_list|(
name|schemaValidator
operator|.
name|getErrorHandler
argument_list|()
argument_list|,
name|domSource
operator|.
name|getNode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|schemaValidator
operator|.
name|validate
argument_list|(
name|domSource
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|domSource
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Node
condition|)
block|{
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|Validator
name|schemaValidator
init|=
name|schema
operator|.
name|newValidator
argument_list|()
decl_stmt|;
name|schemaValidator
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|MtomValidationErrorHandler
argument_list|(
name|schemaValidator
operator|.
name|getErrorHandler
argument_list|()
argument_list|,
operator|(
name|Node
operator|)
name|obj
argument_list|)
argument_list|)
expr_stmt|;
name|schemaValidator
operator|.
name|validate
argument_list|(
operator|new
name|DOMSource
argument_list|(
operator|(
name|Node
operator|)
name|obj
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Node
name|nd
init|=
operator|(
name|Node
operator|)
name|obj
decl_stmt|;
name|writeNode
argument_list|(
name|nd
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Source
name|s
init|=
operator|(
name|Source
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|DOMSource
operator|)
condition|)
block|{
comment|//make the source re-readable.
name|s
operator|=
operator|new
name|DOMSource
argument_list|(
name|StaxUtils
operator|.
name|read
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Validator
name|schemaValidator
init|=
name|schema
operator|.
name|newValidator
argument_list|()
decl_stmt|;
name|schemaValidator
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|MtomValidationErrorHandler
argument_list|(
name|schemaValidator
operator|.
name|getErrorHandler
argument_list|()
argument_list|,
operator|(
operator|(
name|DOMSource
operator|)
name|s
operator|)
operator|.
name|getNode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|schemaValidator
operator|.
name|validate
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|s
operator|instanceof
name|DOMSource
operator|&&
operator|(
operator|(
name|DOMSource
operator|)
name|s
operator|)
operator|.
name|getNode
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|StaxUtils
operator|.
name|copy
argument_list|(
name|s
argument_list|,
name|writer
argument_list|)
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
name|Fault
argument_list|(
literal|"COULD_NOT_WRITE_XML_STREAM_CAUSED_BY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"COULD_NOT_WRITE_XML_STREAM"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
literal|"COULD_NOT_WRITE_XML_STREAM_CAUSED_BY"
argument_list|,
name|LOG
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|writeNode
parameter_list|(
name|Node
name|nd
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|writer
operator|instanceof
name|W3CDOMStreamWriter
condition|)
block|{
name|W3CDOMStreamWriter
name|dw
init|=
operator|(
name|W3CDOMStreamWriter
operator|)
name|writer
decl_stmt|;
if|if
condition|(
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nd
operator|instanceof
name|DocumentFragment
operator|&&
name|nd
operator|.
name|getOwnerDocument
argument_list|()
operator|==
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
condition|)
block|{
name|Node
name|ch
init|=
name|nd
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|ch
operator|!=
literal|null
condition|)
block|{
name|nd
operator|.
name|removeChild
argument_list|(
name|ch
argument_list|)
expr_stmt|;
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|ch
argument_list|)
expr_stmt|;
name|ch
operator|=
name|nd
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|nd
operator|.
name|getOwnerDocument
argument_list|()
operator|==
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
condition|)
block|{
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|nd
operator|instanceof
name|DocumentFragment
condition|)
block|{
name|nd
operator|=
name|dw
operator|.
name|getDocument
argument_list|()
operator|.
name|importNode
argument_list|(
name|nd
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|dw
operator|.
name|getCurrentNode
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
elseif|else
if|if
condition|(
name|dw
operator|.
name|getCurrentFragment
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|nd
operator|.
name|getOwnerDocument
argument_list|()
operator|==
name|dw
operator|.
name|getCurrentFragment
argument_list|()
operator|.
name|getOwnerDocument
argument_list|()
condition|)
block|{
name|dw
operator|.
name|getCurrentFragment
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|nd
operator|instanceof
name|DocumentFragment
condition|)
block|{
name|nd
operator|=
name|dw
operator|.
name|getDocument
argument_list|()
operator|.
name|importNode
argument_list|(
name|nd
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|dw
operator|.
name|getCurrentFragment
argument_list|()
operator|.
name|appendChild
argument_list|(
name|nd
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
if|if
condition|(
name|nd
operator|instanceof
name|Document
condition|)
block|{
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
operator|(
name|Document
operator|)
name|nd
argument_list|,
name|writer
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|StaxUtils
operator|.
name|writeNode
argument_list|(
name|nd
argument_list|,
name|writer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|Schema
name|s
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setAttachments
parameter_list|(
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{      }
specifier|public
name|void
name|setProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{     }
specifier|private
specifier|static
class|class
name|MtomValidationErrorHandler
implements|implements
name|ErrorHandler
block|{
specifier|private
name|ErrorHandler
name|origErrorHandler
decl_stmt|;
specifier|private
name|Node
name|node
decl_stmt|;
name|MtomValidationErrorHandler
parameter_list|(
name|ErrorHandler
name|origErrorHandler
parameter_list|,
name|Node
name|node
parameter_list|)
block|{
name|this
operator|.
name|origErrorHandler
operator|=
name|origErrorHandler
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|warning
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|this
operator|.
name|origErrorHandler
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|origErrorHandler
operator|.
name|warning
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|this
operator|.
name|isCVC312Exception
argument_list|(
name|exception
argument_list|)
condition|)
block|{
name|String
name|elementName
init|=
name|this
operator|.
name|getAttachmentElementName
argument_list|(
name|exception
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
operator|&&
name|this
operator|.
name|findIncludeNode
argument_list|(
name|node
argument_list|,
name|elementName
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
if|if
condition|(
name|this
operator|.
name|origErrorHandler
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|origErrorHandler
operator|.
name|error
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|exception
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|fatalError
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|this
operator|.
name|origErrorHandler
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|origErrorHandler
operator|.
name|fatalError
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|exception
throw|;
block|}
block|}
specifier|private
name|boolean
name|isCVC312Exception
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
block|{
name|String
name|msg
init|=
name|exception
operator|.
name|getMessage
argument_list|()
decl_stmt|;
return|return
name|msg
operator|.
name|startsWith
argument_list|(
literal|"cvc-type.3.1.2: "
argument_list|)
operator|&&
name|msg
operator|.
name|endsWith
argument_list|(
literal|"is a simple type, so it must have no element information item [children]."
argument_list|)
return|;
block|}
specifier|private
name|String
name|getAttachmentElementName
parameter_list|(
name|SAXParseException
name|exception
parameter_list|)
block|{
name|String
name|msg
init|=
name|exception
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|String
name|str
index|[]
init|=
name|msg
operator|.
name|split
argument_list|(
literal|"'"
argument_list|)
decl_stmt|;
return|return
name|str
index|[
literal|1
index|]
return|;
block|}
specifier|private
name|boolean
name|findIncludeNode
parameter_list|(
name|Node
name|checkNode
parameter_list|,
name|String
name|mtomElement
parameter_list|)
block|{
name|boolean
name|ret
init|=
literal|false
decl_stmt|;
name|NodeList
name|nList
init|=
name|checkNode
operator|.
name|getChildNodes
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
name|nList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|nNode
init|=
name|nList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|nNode
operator|.
name|getLocalName
argument_list|()
operator|!=
literal|null
operator|&&
name|nNode
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|mtomElement
argument_list|)
condition|)
block|{
name|NodeList
name|subNodeList
init|=
name|nNode
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|subNodeList
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|subNode
init|=
name|subNodeList
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
if|if
condition|(
name|subNode
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
literal|"http://www.w3.org/2004/08/xop/include"
argument_list|)
operator|&&
name|subNode
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Include"
argument_list|)
condition|)
block|{
comment|// This is the Mtom element which break the SchemaValidation so ignore this
return|return
literal|true
return|;
block|}
block|}
block|}
else|else
block|{
name|ret
operator|=
name|findIncludeNode
argument_list|(
name|nNode
argument_list|,
name|mtomElement
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
block|}
block|}
end_class

end_unit

