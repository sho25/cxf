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
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
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
name|ResourceBundle
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
name|validation
operator|.
name|Schema
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
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|model
operator|.
name|SoapHeaderInfo
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
name|util
operator|.
name|PropertyUtils
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
name|ServiceUtils
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
name|WriteOnCloseOutputStream
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
name|Exchange
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
name|MessageContentsList
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
name|MessageUtils
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
name|service
operator|.
name|Service
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
name|BindingMessageInfo
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
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceModelUtil
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceUtils
import|;
end_import

begin_class
specifier|public
class|class
name|SoapOutInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WROTE_ENVELOPE_START
init|=
literal|"wrote.envelope.start"
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
name|SoapOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|SoapOutInterceptor
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|WRITE
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|SoapOutInterceptor
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
comment|// Yes this is ugly, but it avoids us from having to implement any kind of caching strategy
name|boolean
name|wroteStart
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|WROTE_ENVELOPE_START
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|wroteStart
condition|)
block|{
name|writeSoapEnvelopeStart
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Unless we're caching the whole message in memory skip the envelope writing
comment|// if there's a fault later.
if|if
condition|(
operator|!
operator|(
name|os
operator|instanceof
name|WriteOnCloseOutputStream
operator|)
operator|&&
operator|!
name|MessageUtils
operator|.
name|isDOMPresent
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|WROTE_ENVELOPE_START
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|cte
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"soap.attachement.content.transfer.encoding"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cte
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TRANSFER_ENCODING
argument_list|,
name|cte
argument_list|)
expr_stmt|;
block|}
comment|// Add a final interceptor to write end elements
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SoapOutEndingInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|writeSoapEnvelopeStart
parameter_list|(
specifier|final
name|SoapMessage
name|message
parameter_list|)
block|{
specifier|final
name|SoapVersion
name|soapVersion
init|=
name|message
operator|.
name|getVersion
argument_list|()
decl_stmt|;
try|try
block|{
name|XMLStreamWriter
name|xtw
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
name|String
name|soapPrefix
init|=
name|xtw
operator|.
name|getPrefix
argument_list|(
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|soapPrefix
argument_list|)
condition|)
block|{
name|soapPrefix
operator|=
literal|"soap"
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|hasAdditionalEnvNs
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|nsMap
init|=
name|message
operator|.
name|getEnvelopeNs
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
name|entry
range|:
name|nsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|soapVersion
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|soapPrefix
operator|=
name|entry
operator|.
name|getKey
argument_list|()
expr_stmt|;
block|}
block|}
name|xtw
operator|.
name|setPrefix
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|xtw
operator|.
name|writeStartElement
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|xtw
operator|.
name|writeNamespace
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
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
name|nsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|soapVersion
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|xtw
operator|.
name|writeNamespace
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|xtw
operator|.
name|setPrefix
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|xtw
operator|.
name|writeStartElement
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s2
init|=
name|xtw
operator|.
name|getPrefix
argument_list|(
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|s2
argument_list|)
operator|||
name|soapPrefix
operator|.
name|equals
argument_list|(
name|s2
argument_list|)
condition|)
block|{
name|xtw
operator|.
name|writeNamespace
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|soapPrefix
operator|=
name|s2
expr_stmt|;
block|}
block|}
name|boolean
name|preexistingHeaders
init|=
name|message
operator|.
name|hasHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|preexistingHeaders
condition|)
block|{
name|xtw
operator|.
name|writeStartElement
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getHeader
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|hdrList
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|Header
name|header
range|:
name|hdrList
control|)
block|{
name|XMLStreamWriter
name|writer
init|=
name|xtw
decl_stmt|;
if|if
condition|(
name|xtw
operator|instanceof
name|W3CDOMStreamWriter
condition|)
block|{
name|Element
name|nd
init|=
operator|(
operator|(
name|W3CDOMStreamWriter
operator|)
name|xtw
operator|)
operator|.
name|getCurrentNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Element
operator|&&
name|nd
operator|.
name|isSameNode
argument_list|(
operator|(
operator|(
name|Element
operator|)
name|header
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getParentNode
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
if|if
condition|(
name|header
operator|instanceof
name|SoapHeader
condition|)
block|{
name|SoapHeader
name|soapHeader
init|=
operator|(
name|SoapHeader
operator|)
name|header
decl_stmt|;
name|writer
operator|=
operator|new
name|SOAPHeaderWriter
argument_list|(
name|xtw
argument_list|,
name|soapHeader
argument_list|,
name|soapVersion
argument_list|,
name|soapPrefix
argument_list|)
expr_stmt|;
block|}
name|DataBinding
name|b
init|=
name|header
operator|.
name|getDataBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
name|HeaderProcessor
name|hp
init|=
name|bus
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
name|header
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|hp
operator|!=
literal|null
condition|)
block|{
name|b
operator|=
name|hp
operator|.
name|getDataBinding
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|MessagePartInfo
name|part
init|=
operator|new
name|MessagePartInfo
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|part
operator|.
name|setConcreteName
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
operator|.
name|write
argument_list|(
name|header
operator|.
name|getObject
argument_list|()
argument_list|,
name|part
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Element
name|node
init|=
operator|(
name|Element
operator|)
name|header
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|node
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|boolean
name|endedHeader
init|=
name|handleHeaderPart
argument_list|(
name|preexistingHeaders
argument_list|,
name|message
argument_list|,
name|soapPrefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|preexistingHeaders
operator|&&
operator|!
name|endedHeader
condition|)
block|{
name|xtw
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
name|xtw
operator|.
name|writeStartElement
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getBody
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
comment|// Interceptors followed such as Wrapped/RPC/Doc Interceptor will write SOAP body
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
literal|"XML_WRITE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|soapVersion
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|handleHeaderPart
parameter_list|(
name|boolean
name|preexistingHeaders
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|String
name|soapPrefix
parameter_list|)
block|{
comment|//add MessagePart to soapHeader if necessary
name|boolean
name|endedHeader
init|=
literal|false
decl_stmt|;
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|bop
init|=
name|exchange
operator|.
name|getBindingOperationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|bop
operator|==
literal|null
condition|)
block|{
return|return
name|endedHeader
return|;
block|}
name|XMLStreamWriter
name|xtw
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
name|boolean
name|startedHeader
init|=
literal|false
decl_stmt|;
name|BindingOperationInfo
name|unwrappedOp
init|=
name|bop
decl_stmt|;
if|if
condition|(
name|bop
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|unwrappedOp
operator|=
name|bop
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
block|}
name|boolean
name|client
init|=
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|bmi
init|=
name|client
condition|?
name|unwrappedOp
operator|.
name|getInput
argument_list|()
else|:
name|unwrappedOp
operator|.
name|getOutput
argument_list|()
decl_stmt|;
name|BindingMessageInfo
name|wrappedBmi
init|=
name|client
condition|?
name|bop
operator|.
name|getInput
argument_list|()
else|:
name|bop
operator|.
name|getOutput
argument_list|()
decl_stmt|;
if|if
condition|(
name|bmi
operator|==
literal|null
condition|)
block|{
return|return
name|endedHeader
return|;
block|}
if|if
condition|(
name|wrappedBmi
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|getMessagePartsNumber
argument_list|()
operator|>
literal|0
condition|)
block|{
name|MessageContentsList
name|objs
init|=
name|MessageContentsList
operator|.
name|getContentsList
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|objs
operator|==
literal|null
condition|)
block|{
return|return
name|endedHeader
return|;
block|}
name|SoapVersion
name|soapVersion
init|=
name|message
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|SoapHeaderInfo
argument_list|>
name|headers
init|=
name|bmi
operator|.
name|getExtensors
argument_list|(
name|SoapHeaderInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return
name|endedHeader
return|;
block|}
for|for
control|(
name|SoapHeaderInfo
name|header
range|:
name|headers
control|)
block|{
name|MessagePartInfo
name|part
init|=
name|header
operator|.
name|getPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|wrappedBmi
operator|!=
name|bmi
condition|)
block|{
name|part
operator|=
name|wrappedBmi
operator|.
name|getMessageInfo
argument_list|()
operator|.
name|addMessagePart
argument_list|(
name|part
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|part
operator|.
name|getIndex
argument_list|()
operator|>=
name|objs
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// The optional out of band header is not a part of parameters of the method
continue|continue;
block|}
name|Object
name|arg
init|=
name|objs
operator|.
name|get
argument_list|(
name|part
argument_list|)
decl_stmt|;
if|if
condition|(
name|arg
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|objs
operator|.
name|remove
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|startedHeader
operator|||
name|preexistingHeaders
operator|)
condition|)
block|{
try|try
block|{
name|xtw
operator|.
name|writeStartElement
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getHeader
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
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
literal|"XML_WRITE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|soapVersion
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
name|startedHeader
operator|=
literal|true
expr_stmt|;
block|}
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dataWriter
init|=
name|getDataWriter
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|dataWriter
operator|.
name|write
argument_list|(
name|arg
argument_list|,
name|header
operator|.
name|getPart
argument_list|()
argument_list|,
name|xtw
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|startedHeader
operator|||
name|preexistingHeaders
condition|)
block|{
try|try
block|{
name|xtw
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|endedHeader
operator|=
literal|true
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
literal|"XML_WRITE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|soapVersion
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|endedHeader
return|;
block|}
specifier|protected
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|getDataWriter
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Service
name|service
init|=
name|ServiceModelUtil
operator|.
name|getService
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
decl_stmt|;
name|DataWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|dataWriter
init|=
name|service
operator|.
name|getDataBinding
argument_list|()
operator|.
name|createWriter
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataWriter
operator|==
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
literal|"NO_DATAWRITER"
argument_list|,
name|BUNDLE
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|dataWriter
operator|.
name|setAttachments
argument_list|(
name|message
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
name|setDataWriterValidation
argument_list|(
name|service
argument_list|,
name|message
argument_list|,
name|dataWriter
argument_list|)
expr_stmt|;
return|return
name|dataWriter
return|;
block|}
specifier|private
name|void
name|setDataWriterValidation
parameter_list|(
name|Service
name|service
parameter_list|,
name|Message
name|message
parameter_list|,
name|DataWriter
argument_list|<
name|?
argument_list|>
name|writer
parameter_list|)
block|{
if|if
condition|(
name|ServiceUtils
operator|.
name|isSchemaValidationEnabled
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|,
name|message
argument_list|)
condition|)
block|{
name|Schema
name|schema
init|=
name|EndpointReferenceUtils
operator|.
name|getSchema
argument_list|(
name|service
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
name|writer
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
class|class
name|SoapOutEndingInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|SoapOutEndingInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|SoapOutEndingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Phase
operator|.
name|WRITE_ENDING
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
name|XMLStreamWriter
name|xtw
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
if|if
condition|(
name|xtw
operator|!=
literal|null
condition|)
block|{
comment|// Write body end
name|xtw
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
comment|// Write Envelope end element
name|xtw
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|xtw
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|xtw
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|EOFException
condition|)
block|{
comment|//Nothing we can do about this, some clients will close the connection early if
comment|//they fully parse everything they need
block|}
else|else
block|{
name|SoapVersion
name|soapVersion
init|=
name|message
operator|.
name|getVersion
argument_list|()
decl_stmt|;
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
literal|"XML_WRITE_EXC"
argument_list|,
name|BUNDLE
argument_list|)
argument_list|,
name|e
argument_list|,
name|soapVersion
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|public
specifier|static
class|class
name|SOAPHeaderWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|final
name|SoapHeader
name|soapHeader
decl_stmt|;
specifier|final
name|SoapVersion
name|soapVersion
decl_stmt|;
specifier|final
name|String
name|soapPrefix
decl_stmt|;
name|boolean
name|firstDone
decl_stmt|;
specifier|public
name|SOAPHeaderWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|SoapHeader
name|header
parameter_list|,
name|SoapVersion
name|version
parameter_list|,
name|String
name|pfx
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|soapHeader
operator|=
name|header
expr_stmt|;
name|soapVersion
operator|=
name|version
expr_stmt|;
name|soapPrefix
operator|=
name|pfx
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
name|soapVersion
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
operator|&&
operator|(
name|local
operator|.
name|equals
argument_list|(
name|soapVersion
operator|.
name|getAttrNameMustUnderstand
argument_list|()
argument_list|)
operator|||
name|local
operator|.
name|equals
argument_list|(
name|soapVersion
operator|.
name|getAttrNameRole
argument_list|()
argument_list|)
operator|)
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
name|soapVersion
operator|.
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
operator|&&
operator|(
name|local
operator|.
name|equals
argument_list|(
name|soapVersion
operator|.
name|getAttrNameMustUnderstand
argument_list|()
argument_list|)
operator|||
name|local
operator|.
name|equals
argument_list|(
name|soapVersion
operator|.
name|getAttrNameRole
argument_list|()
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
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
specifier|private
name|void
name|writeSoapAttributes
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
operator|!
name|firstDone
condition|)
block|{
name|firstDone
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|soapHeader
operator|.
name|getActor
argument_list|()
argument_list|)
condition|)
block|{
name|super
operator|.
name|writeAttribute
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getAttrNameRole
argument_list|()
argument_list|,
name|soapHeader
operator|.
name|getActor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|mu
init|=
name|soapHeader
operator|.
name|isMustUnderstand
argument_list|()
decl_stmt|;
if|if
condition|(
name|mu
condition|)
block|{
name|String
name|mul
init|=
name|soapVersion
operator|.
name|getAttrValueMustUnderstand
argument_list|(
name|mu
argument_list|)
decl_stmt|;
name|super
operator|.
name|writeAttribute
argument_list|(
name|soapPrefix
argument_list|,
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getAttrNameMustUnderstand
argument_list|()
argument_list|,
name|mul
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|arg0
parameter_list|,
name|String
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|super
operator|.
name|writeStartElement
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|,
name|arg2
argument_list|)
expr_stmt|;
name|writeSoapAttributes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
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
name|super
operator|.
name|writeStartElement
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|)
expr_stmt|;
name|writeSoapAttributes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|super
operator|.
name|writeStartElement
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
name|writeSoapAttributes
argument_list|()
expr_stmt|;
block|}
block|}
empty_stmt|;
block|}
end_class

end_unit

