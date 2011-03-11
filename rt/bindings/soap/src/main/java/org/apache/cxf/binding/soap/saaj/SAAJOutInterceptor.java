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
name|binding
operator|.
name|soap
operator|.
name|saaj
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
name|OutputStream
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|AttachmentPart
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
name|MimeHeader
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
name|soap
operator|.
name|SOAPPart
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
name|attachment
operator|.
name|AttachmentImpl
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
name|Soap11
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
name|Soap12
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
name|SoapFault
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|interceptor
operator|.
name|SoapOutInterceptor
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
name|BundleUtils
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|CastUtils
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
name|phase
operator|.
name|Phase
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
name|W3CDOMStreamReader
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

begin_comment
comment|/**  * Sets up the outgoing chain to build a SAAJ tree instead of writing  * directly to the output stream. First it will replace the XMLStreamWriter  * with one which writes to a SOAPMessage. Then it will add an interceptor  * at the end of the chain in the SEND phase which writes the resulting  * SOAPMessage.  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|SAAJOutInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ORIGINAL_XML_WRITER
init|=
name|SAAJOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".original.xml.writer"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|SAAJOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MessageFactory
name|factory11
decl_stmt|;
specifier|private
name|MessageFactory
name|factory12
decl_stmt|;
specifier|public
name|SAAJOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|MessageFactory
name|getFactory
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|SOAPException
block|{
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap11
condition|)
block|{
if|if
condition|(
name|factory11
operator|==
literal|null
condition|)
block|{
name|factory11
operator|=
name|SAAJFactoryResolver
operator|.
name|createMessageFactory
argument_list|(
name|message
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|factory11
return|;
block|}
if|if
condition|(
name|message
operator|.
name|getVersion
argument_list|()
operator|instanceof
name|Soap12
condition|)
block|{
if|if
condition|(
name|factory12
operator|==
literal|null
condition|)
block|{
name|factory12
operator|=
name|SAAJFactoryResolver
operator|.
name|createMessageFactory
argument_list|(
name|message
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|factory12
return|;
block|}
return|return
name|SAAJFactoryResolver
operator|.
name|createMessageFactory
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|SOAPMessage
name|saaj
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
try|try
block|{
if|if
condition|(
name|message
operator|.
name|hasHeaders
argument_list|()
operator|&&
name|saaj
operator|!=
literal|null
operator|&&
name|saaj
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// creating an empty SOAPHeader at this point in the
comment|// pre-existing SOAPMessage avoids the<soap:body> and
comment|//<soap:header> appearing in reverse order when the envolope
comment|// is written to the wire
comment|//
name|saaj
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|addHeader
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SOAPEXCEPTION"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|saaj
operator|==
literal|null
condition|)
block|{
name|SoapVersion
name|version
init|=
name|message
operator|.
name|getVersion
argument_list|()
decl_stmt|;
try|try
block|{
name|MessageFactory
name|factory
init|=
name|getFactory
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|SOAPMessage
name|soapMessage
init|=
name|factory
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|SOAPPart
name|soapPart
init|=
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|origWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|,
name|origWriter
argument_list|)
expr_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|(
name|soapPart
argument_list|)
decl_stmt|;
comment|// Replace stax writer with DomStreamWriter
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|soapMessage
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Node
operator|.
name|class
argument_list|,
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
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
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SOAPEXCEPTION"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|version
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|message
operator|.
name|containsKey
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|)
condition|)
block|{
comment|//as the SOAPMessage already has everything in place, we do not need XMLStreamWriter to write
comment|//anything for us, so we just set XMLStreamWriter's output to a dummy output stream.
name|XMLStreamWriter
name|origWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|,
name|origWriter
argument_list|)
expr_stmt|;
name|XMLStreamWriter
name|dummyWriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
operator|new
name|OutputStream
argument_list|()
block|{
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{                     }
specifier|public
name|void
name|write
parameter_list|(
name|byte
name|b
index|[]
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{                     }
block|}
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|dummyWriter
argument_list|)
expr_stmt|;
block|}
comment|// Add a final interceptor to write the message
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|SAAJOutEndingInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|super
operator|.
name|handleFault
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|//need to clear these so the fault writing will work correctly
name|message
operator|.
name|removeContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
expr_stmt|;
name|message
operator|.
name|remove
argument_list|(
name|SoapOutInterceptor
operator|.
name|WROTE_ENVELOPE_START
argument_list|)
expr_stmt|;
name|XMLStreamWriter
name|writer
init|=
operator|(
name|XMLStreamWriter
operator|)
name|message
operator|.
name|get
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|message
operator|.
name|remove
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|SAAJOutEndingInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|SAAJOutEndingInterceptor
name|INSTANCE
init|=
operator|new
name|SAAJOutEndingInterceptor
argument_list|()
decl_stmt|;
specifier|public
name|SAAJOutEndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|SAAJOutEndingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Phase
operator|.
name|PRE_PROTOCOL_ENDING
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|SOAPMessage
name|soapMessage
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
if|if
condition|(
name|soapMessage
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|soapMessage
operator|.
name|countAttachments
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|message
operator|.
name|getAttachments
argument_list|()
operator|==
literal|null
condition|)
block|{
name|message
operator|.
name|setAttachments
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Attachment
argument_list|>
argument_list|(
name|soapMessage
operator|.
name|countAttachments
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|AttachmentPart
argument_list|>
name|it
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|soapMessage
operator|.
name|getAttachments
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|AttachmentPart
name|part
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|AttachmentImpl
name|att
init|=
operator|new
name|AttachmentImpl
argument_list|(
name|part
operator|.
name|getContentId
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|att
operator|.
name|setDataHandler
argument_list|(
name|part
operator|.
name|getDataHandler
argument_list|()
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
name|Iterator
argument_list|<
name|MimeHeader
argument_list|>
name|it2
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|part
operator|.
name|getAllMimeHeaders
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it2
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|MimeHeader
name|header
init|=
name|it2
operator|.
name|next
argument_list|()
decl_stmt|;
name|att
operator|.
name|setHeader
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getAttachments
argument_list|()
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
block|}
name|XMLStreamWriter
name|writer
init|=
operator|(
name|XMLStreamWriter
operator|)
name|message
operator|.
name|get
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|)
decl_stmt|;
name|message
operator|.
name|remove
argument_list|(
name|ORIGINAL_XML_WRITER
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|W3CDOMStreamReader
argument_list|(
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
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
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"SOAPEXCEPTION"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

