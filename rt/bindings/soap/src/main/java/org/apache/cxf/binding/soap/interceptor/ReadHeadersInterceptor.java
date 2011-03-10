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
name|InputStream
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
name|logging
operator|.
name|Logger
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
name|XMLStreamReader
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
name|Attr
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
name|NamedNodeMap
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

begin_comment
comment|//import org.w3c.dom.NodeList;
end_comment

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
name|SoapVersionFactory
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
name|staxutils
operator|.
name|PartialXMLStreamReader
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

begin_class
specifier|public
class|class
name|ReadHeadersInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
comment|/**      *       */
specifier|public
specifier|static
class|class
name|CheckClosingTagsInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
specifier|public
name|CheckClosingTagsInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_LOGICAL
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
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
if|if
condition|(
name|xmlReader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
while|while
condition|(
name|xmlReader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|xmlReader
operator|.
name|next
argument_list|()
operator|==
name|XMLStreamReader
operator|.
name|END_DOCUMENT
condition|)
block|{
return|return;
block|}
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
name|e
operator|.
name|getMessage
argument_list|()
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
name|ReadHeadersInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|SoapVersion
name|version
decl_stmt|;
specifier|public
name|ReadHeadersInterceptor
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|ReadHeadersInterceptor
parameter_list|(
name|Bus
name|b
parameter_list|,
name|SoapVersion
name|v
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|version
operator|=
name|v
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|ReadHeadersInterceptor
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
specifier|static
name|SoapVersion
name|readVersion
parameter_list|(
name|XMLStreamReader
name|xmlReader
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|String
name|ns
init|=
name|xmlReader
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"NO_NAMESPACE"
argument_list|,
name|LOG
argument_list|,
name|xmlReader
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getVersionMismatch
argument_list|()
argument_list|)
throw|;
block|}
name|SoapVersion
name|soapVersion
init|=
name|SoapVersionFactory
operator|.
name|getInstance
argument_list|()
operator|.
name|getSoapVersion
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapVersion
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_VERSION"
argument_list|,
name|LOG
argument_list|,
name|ns
argument_list|,
name|xmlReader
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getVersionMismatch
argument_list|()
argument_list|)
throw|;
block|}
name|message
operator|.
name|setVersion
argument_list|(
name|soapVersion
argument_list|)
expr_stmt|;
return|return
name|soapVersion
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"ReadHeadersInterceptor skipped in HTTP GET method"
argument_list|)
expr_stmt|;
return|return;
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
if|if
condition|(
name|xmlReader
operator|==
literal|null
condition|)
block|{
name|InputStream
name|in
init|=
operator|(
name|InputStream
operator|)
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Can't find input stream in message"
argument_list|)
throw|;
block|}
name|xmlReader
operator|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|xmlReader
operator|.
name|getEventType
argument_list|()
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
operator|||
name|xmlReader
operator|.
name|nextTag
argument_list|()
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
condition|)
block|{
name|SoapVersion
name|soapVersion
init|=
name|readVersion
argument_list|(
name|xmlReader
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapVersion
operator|==
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|&&
name|version
operator|==
name|Soap11
operator|.
name|getInstance
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"INVALID_11_VERSION"
argument_list|,
name|LOG
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|xmlReader
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getVersionMismatch
argument_list|()
argument_list|)
throw|;
block|}
name|XMLStreamReader
name|filteredReader
init|=
operator|new
name|PartialXMLStreamReader
argument_list|(
name|xmlReader
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|Node
name|nd
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|nd
operator|instanceof
name|Document
condition|)
block|{
name|doc
operator|=
operator|(
name|Document
operator|)
name|nd
expr_stmt|;
name|StaxUtils
operator|.
name|readDocElements
argument_list|(
name|doc
argument_list|,
name|doc
argument_list|,
name|filteredReader
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|filteredReader
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
name|doc
argument_list|)
expr_stmt|;
block|}
comment|// Find header
comment|// TODO - we could stream read the "known" headers and just DOM read the
comment|// unknown ones
name|Element
name|element
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|QName
name|header
init|=
name|soapVersion
operator|.
name|getHeader
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|elemList
init|=
name|DOMUtils
operator|.
name|findAllElementsByTagNameNS
argument_list|(
name|element
argument_list|,
name|header
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|header
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|elem
range|:
name|elemList
control|)
block|{
name|Element
name|hel
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|elem
argument_list|)
decl_stmt|;
while|while
condition|(
name|hel
operator|!=
literal|null
condition|)
block|{
comment|// Need to add any attributes that are present on the parent element
comment|// which otherwise would be lost.
if|if
condition|(
name|elem
operator|.
name|hasAttributes
argument_list|()
condition|)
block|{
name|NamedNodeMap
name|nnp
init|=
name|elem
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|ct
init|=
literal|0
init|;
name|ct
operator|<
name|nnp
operator|.
name|getLength
argument_list|()
condition|;
name|ct
operator|++
control|)
block|{
name|Node
name|attr
init|=
name|nnp
operator|.
name|item
argument_list|(
name|ct
argument_list|)
decl_stmt|;
name|Node
name|headerAttrNode
init|=
name|hel
operator|.
name|hasAttributes
argument_list|()
condition|?
name|hel
operator|.
name|getAttributes
argument_list|()
operator|.
name|getNamedItemNS
argument_list|(
name|attr
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|attr
operator|.
name|getLocalName
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|headerAttrNode
operator|==
literal|null
condition|)
block|{
name|Attr
name|attribute
init|=
name|hel
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createAttributeNS
argument_list|(
name|attr
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|attr
operator|.
name|getNodeName
argument_list|()
argument_list|)
decl_stmt|;
name|attribute
operator|.
name|setNodeValue
argument_list|(
name|attr
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|hel
operator|.
name|setAttributeNodeNS
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|HeaderProcessor
name|p
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
name|hel
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
decl_stmt|;
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
name|hel
expr_stmt|;
block|}
else|else
block|{
name|dataBinding
operator|=
name|p
operator|.
name|getDataBinding
argument_list|()
expr_stmt|;
name|obj
operator|=
name|dataBinding
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
name|hel
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
name|hel
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|hel
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
name|String
name|mu
init|=
name|hel
operator|.
name|getAttributeNS
argument_list|(
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getAttrNameMustUnderstand
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|act
init|=
name|hel
operator|.
name|getAttributeNS
argument_list|(
name|soapVersion
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|soapVersion
operator|.
name|getAttrNameRole
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|act
argument_list|)
condition|)
block|{
name|shead
operator|.
name|setActor
argument_list|(
name|act
argument_list|)
expr_stmt|;
block|}
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
comment|//mark header as inbound header.(for distinguishing between the  direction to
comment|//avoid piggybacking of headers from request->server->response.
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
name|hel
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|hel
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SoapMessage
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|message
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CheckClosingTagsInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"XML_STREAM_EXC"
argument_list|,
name|LOG
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
end_class

end_unit

