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
name|ResourceBundle
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
name|SOAPConstants
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
name|SOAPHeader
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
name|XMLStreamReader
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
name|Bus
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
name|SoapHeader
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
name|databinding
operator|.
name|DataBinding
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
name|headers
operator|.
name|Header
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
name|headers
operator|.
name|HeaderManager
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
name|headers
operator|.
name|HeaderProcessor
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

begin_comment
comment|/**  * Builds a SAAJ tree from the Document fragment inside the message which contains  * the SOAP headers and from the XMLStreamReader.  */
end_comment

begin_class
specifier|public
class|class
name|SAAJInInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
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
name|SAAJInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|SAAJInInterceptor
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
name|SAAJInInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
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
try|try
block|{
name|MessageFactory
name|factory
init|=
literal|null
decl_stmt|;
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
name|factory
operator|=
name|MessageFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
name|MessageFactory
operator|.
name|newInstance
argument_list|(
name|SOAPConstants
operator|.
name|SOAP_1_2_PROTOCOL
argument_list|)
expr_stmt|;
block|}
name|SOAPMessage
name|soapMessage
init|=
name|factory
operator|.
name|createMessage
argument_list|()
decl_stmt|;
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
name|SOAPPart
name|part
init|=
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|Document
name|node
init|=
operator|(
name|Document
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|part
operator|.
name|setContent
argument_list|(
name|source
argument_list|)
expr_stmt|;
comment|// TODO: setup mime headers
name|Collection
argument_list|<
name|Attachment
argument_list|>
name|atts
init|=
name|message
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
if|if
condition|(
name|atts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Attachment
name|a
range|:
name|atts
control|)
block|{
name|AttachmentPart
name|ap
init|=
name|soapMessage
operator|.
name|createAttachmentPart
argument_list|(
name|a
operator|.
name|getDataHandler
argument_list|()
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|addAttachmentPart
argument_list|(
name|ap
argument_list|)
expr_stmt|;
block|}
block|}
comment|//replace header element if necessary
if|if
condition|(
name|message
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
name|replaceHeaders
argument_list|(
name|soapMessage
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|soapMessage
operator|.
name|getSOAPHeader
argument_list|()
operator|==
literal|null
condition|)
block|{
name|soapMessage
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
name|XMLStreamReader
name|xmlReader
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
name|StaxUtils
operator|.
name|readDocElements
argument_list|(
name|soapMessage
operator|.
name|getSOAPBody
argument_list|()
argument_list|,
name|xmlReader
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|DOMSource
name|bodySource
init|=
operator|new
name|DOMSource
argument_list|(
name|soapMessage
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|xmlReader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bodySource
argument_list|)
expr_stmt|;
name|xmlReader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
name|xmlReader
operator|.
name|nextTag
argument_list|()
expr_stmt|;
comment|// move past body tag
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|xmlReader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|soape
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
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
literal|"SOAPHANDLERINTERCEPTOR_EXCEPTION"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|soape
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
literal|"SOAPHANDLERINTERCEPTOR_EXCEPTION"
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
specifier|public
specifier|static
name|void
name|replaceHeaders
parameter_list|(
name|SOAPMessage
name|soapMessage
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|SOAPException
block|{
name|SOAPHeader
name|header
init|=
name|soapMessage
operator|.
name|getSOAPHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Element
name|elem
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|header
argument_list|)
decl_stmt|;
while|while
condition|(
name|elem
operator|!=
literal|null
condition|)
block|{
name|Bus
name|b
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
name|HeaderProcessor
name|p
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|b
operator|!=
literal|null
operator|&&
name|b
operator|.
name|getExtension
argument_list|(
name|HeaderManager
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|p
operator|=
name|b
operator|.
name|getExtension
argument_list|(
name|HeaderManager
operator|.
name|class
argument_list|)
operator|.
name|getHeaderProcessor
argument_list|(
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Object
name|obj
decl_stmt|;
name|DataBinding
name|dataBinding
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
operator|||
name|p
operator|.
name|getDataBinding
argument_list|()
operator|==
literal|null
condition|)
block|{
name|obj
operator|=
name|elem
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|p
operator|.
name|getDataBinding
argument_list|()
operator|.
name|createReader
argument_list|(
name|Node
operator|.
name|class
argument_list|)
operator|.
name|read
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
comment|//TODO - add the interceptors
name|SoapHeader
name|shead
init|=
operator|new
name|SoapHeader
argument_list|(
operator|new
name|QName
argument_list|(
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|obj
argument_list|,
name|dataBinding
argument_list|)
decl_stmt|;
name|shead
operator|.
name|setDirection
argument_list|(
name|SoapHeader
operator|.
name|Direction
operator|.
name|DIRECTION_IN
argument_list|)
expr_stmt|;
name|String
name|mu
init|=
name|elem
operator|.
name|getAttributeNS
argument_list|(
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getAttrNameMustUnderstand
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|act
init|=
name|elem
operator|.
name|getAttributeNS
argument_list|(
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getAttrNameRole
argument_list|()
argument_list|)
decl_stmt|;
name|shead
operator|.
name|setActor
argument_list|(
name|act
argument_list|)
expr_stmt|;
name|shead
operator|.
name|setMustUnderstand
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|mu
argument_list|)
operator|||
literal|"1"
operator|.
name|equals
argument_list|(
name|mu
argument_list|)
argument_list|)
expr_stmt|;
name|Header
name|oldHdr
init|=
name|message
operator|.
name|getHeader
argument_list|(
operator|new
name|QName
argument_list|(
name|elem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|elem
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldHdr
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|remove
argument_list|(
name|oldHdr
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|shead
argument_list|)
expr_stmt|;
name|elem
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

