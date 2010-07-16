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
name|jaxws
operator|.
name|handler
operator|.
name|logical
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
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|MessageFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|LogicalMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
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
name|Node
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|io
operator|.
name|CachedOutputStream
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
name|message
operator|.
name|XMLMessage
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
name|LogicalMessageImpl
implements|implements
name|LogicalMessage
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
name|LogicalMessageImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|LogicalMessageContextImpl
name|msgContext
decl_stmt|;
specifier|public
name|LogicalMessageImpl
parameter_list|(
name|LogicalMessageContextImpl
name|lmctx
parameter_list|)
block|{
name|msgContext
operator|=
name|lmctx
expr_stmt|;
block|}
specifier|public
name|Source
name|getPayload
parameter_list|()
block|{
name|Source
name|source
init|=
literal|null
decl_stmt|;
name|Service
operator|.
name|Mode
name|mode
init|=
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Service
operator|.
name|Mode
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
comment|//Dispatch/Provider case
name|source
operator|=
name|handleDispatchProviderCase
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
name|message
init|=
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
decl_stmt|;
name|source
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|Source
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
comment|// need to convert
name|SOAPMessage
name|msg
init|=
name|message
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Node
name|node
init|=
name|msg
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|node
operator|instanceof
name|Element
operator|)
condition|)
block|{
name|node
operator|=
name|node
operator|.
name|getNextSibling
argument_list|()
expr_stmt|;
block|}
name|source
operator|=
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|source
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|reader
operator|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
expr_stmt|;
comment|//content must be an element thing, skip over any whitespace
name|StaxUtils
operator|.
name|toNextTag
argument_list|(
name|reader
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
name|source
operator|=
operator|new
name|DOMSource
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
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
name|e
argument_list|)
throw|;
block|}
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|source
operator|instanceof
name|DOMSource
operator|)
condition|)
block|{
name|W3CDOMStreamWriter
name|writer
decl_stmt|;
try|try
block|{
name|writer
operator|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|XMLStreamReader
name|reader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
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
name|e
argument_list|)
throw|;
block|}
name|source
operator|=
operator|new
name|DOMSource
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|reader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|source
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|source
return|;
block|}
specifier|private
name|Source
name|handleDispatchProviderCase
parameter_list|(
name|Service
operator|.
name|Mode
name|mode
parameter_list|)
block|{
name|Source
name|source
init|=
literal|null
decl_stmt|;
name|Message
name|message
init|=
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
decl_stmt|;
name|Source
name|obj
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Source
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|SoapMessage
condition|)
block|{
comment|// StreamSource may only be used once, need to make a copy
if|if
condition|(
name|obj
operator|instanceof
name|StreamSource
condition|)
block|{
try|try
block|{
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|obj
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|obj
operator|=
operator|new
name|StreamSource
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
operator|new
name|StreamSource
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|mode
operator|==
name|Service
operator|.
name|Mode
operator|.
name|PAYLOAD
condition|)
block|{
name|source
operator|=
operator|(
name|Source
operator|)
name|obj
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|obj
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|cos
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|SOAPMessage
name|msg
init|=
name|initSOAPMessage
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|source
operator|=
operator|new
name|DOMSource
argument_list|(
operator|(
operator|(
name|SOAPMessage
operator|)
name|msg
operator|)
operator|.
name|getSOAPBody
argument_list|()
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|XMLMessage
condition|)
block|{
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|source
operator|=
operator|(
name|Source
operator|)
name|obj
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|getContent
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"GETPAYLOAD_OF_DATASOURCE_NOT_VALID_XMLHTTPBINDING"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
block|}
return|return
name|source
return|;
block|}
specifier|public
name|void
name|setPayload
parameter_list|(
name|Source
name|s
parameter_list|)
block|{
name|Message
name|message
init|=
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
decl_stmt|;
name|Service
operator|.
name|Mode
name|mode
init|=
operator|(
name|Service
operator|.
name|Mode
operator|)
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
operator|.
name|getContextualProperty
argument_list|(
name|Service
operator|.
name|Mode
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mode
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|message
operator|instanceof
name|SoapMessage
condition|)
block|{
if|if
condition|(
name|mode
operator|==
name|Service
operator|.
name|Mode
operator|.
name|MESSAGE
condition|)
block|{
try|try
block|{
comment|// REVISIT: should try to use the original SOAPMessage
comment|// instead of creating a new empty one.
name|SOAPMessage
name|msg
init|=
name|initSOAPMessage
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|write
argument_list|(
name|s
argument_list|,
name|msg
operator|.
name|getSOAPBody
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
operator|new
name|DOMSource
argument_list|(
operator|(
operator|(
name|SOAPMessage
operator|)
name|msg
operator|)
operator|.
name|getSOAPPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|XMLMessage
operator|&&
name|message
operator|.
name|getContent
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
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
argument_list|(
literal|"GETPAYLOAD_OF_DATASOURCE_NOT_VALID_XMLHTTPBINDING"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
name|msgContext
operator|.
name|getWrappedMessage
argument_list|()
operator|.
name|setContent
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|getPayload
parameter_list|(
name|JAXBContext
name|arg0
parameter_list|)
block|{
try|try
block|{
return|return
name|arg0
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|getPayload
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setPayload
parameter_list|(
name|Object
name|arg0
parameter_list|,
name|JAXBContext
name|arg1
parameter_list|)
block|{
try|try
block|{
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|arg1
operator|.
name|createMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|arg0
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|Source
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|writer
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|setPayload
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|write
parameter_list|(
name|Source
name|source
parameter_list|,
name|Node
name|n
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|source
operator|instanceof
name|DOMSource
operator|&&
operator|(
operator|(
name|DOMSource
operator|)
name|source
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
name|XMLStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
operator|(
name|Element
operator|)
name|n
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|source
argument_list|,
name|writer
argument_list|)
expr_stmt|;
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
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|SOAPMessage
name|initSOAPMessage
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|SOAPException
throws|,
name|IOException
block|{
name|SOAPMessage
name|msg
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createMessage
argument_list|()
expr_stmt|;
block|}
name|msg
operator|.
name|setProperty
argument_list|(
name|SOAPMessage
operator|.
name|WRITE_XML_DECLARATION
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|addNamespaceDeclaration
argument_list|(
name|WSDLConstants
operator|.
name|NP_SCHEMA_XSD
argument_list|,
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSD
argument_list|)
expr_stmt|;
name|msg
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|addNamespaceDeclaration
argument_list|(
name|WSDLConstants
operator|.
name|NP_SCHEMA_XSI
argument_list|,
name|WSDLConstants
operator|.
name|NS_SCHEMA_XSI
argument_list|)
expr_stmt|;
return|return
name|msg
return|;
block|}
block|}
end_class

end_unit

