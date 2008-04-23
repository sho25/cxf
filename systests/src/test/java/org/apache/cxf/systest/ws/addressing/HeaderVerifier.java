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
name|systest
operator|.
name|ws
operator|.
name|addressing
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|JAXBElement
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
name|bind
operator|.
name|Marshaller
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
name|SOAPException
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
name|NodeList
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
name|ws
operator|.
name|addressing
operator|.
name|AddressingProperties
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
name|AttributedURIType
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
name|ContextUtils
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
name|Names
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
name|soap
operator|.
name|VersionTransformer
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
name|v200408
operator|.
name|AttributedURI
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES_INBOUND
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
import|;
end_import

begin_comment
comment|/**  * Verifies presence of expected SOAP headers.  */
end_comment

begin_class
specifier|public
class|class
name|HeaderVerifier
extends|extends
name|AbstractSoapInterceptor
block|{
name|VerificationCache
name|verificationCache
decl_stmt|;
name|String
name|currentNamespaceURI
decl_stmt|;
specifier|public
name|HeaderVerifier
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|POST_PROTOCOL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
return|return
name|Names
operator|.
name|HEADERS
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
name|mediate
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|mediate
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|mediate
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|boolean
name|outgoingPartialResponse
init|=
name|isOutgoingPartialResponse
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|outgoingPartialResponse
condition|)
block|{
name|addPartialResponseHeader
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|verify
argument_list|(
name|message
argument_list|,
name|outgoingPartialResponse
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addPartialResponseHeader
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
try|try
block|{
comment|// add piggybacked wsa:From header to partial response
name|List
argument_list|<
name|Header
argument_list|>
name|header
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|SoapVersion
name|ver
init|=
name|message
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|Element
name|hdr
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|ver
operator|.
name|getHeader
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|ver
operator|.
name|getHeader
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
name|hdr
operator|.
name|setPrefix
argument_list|(
name|ver
operator|.
name|getHeader
argument_list|()
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|marshallFrom
argument_list|(
literal|"urn:piggyback_responder"
argument_list|,
name|hdr
argument_list|,
name|getMarshaller
argument_list|()
argument_list|)
expr_stmt|;
name|NodeList
name|nl
init|=
name|hdr
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
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|obj
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|elem
init|=
operator|(
name|Element
operator|)
name|obj
decl_stmt|;
name|Header
name|holder
init|=
operator|new
name|Header
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
name|elem
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|header
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|verificationCache
operator|.
name|put
argument_list|(
literal|"SOAP header addition failed: "
operator|+
name|e
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|verify
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|boolean
name|outgoingPartialResponse
parameter_list|)
block|{
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|wsaHeaders
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|headers
init|=
name|message
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|recordWSAHeaders
argument_list|(
name|headers
argument_list|,
name|wsaHeaders
argument_list|,
name|Names
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|recordWSAHeaders
argument_list|(
name|headers
argument_list|,
name|wsaHeaders
argument_list|,
name|VersionTransformer
operator|.
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|)
expr_stmt|;
name|recordWSAHeaders
argument_list|(
name|headers
argument_list|,
name|wsaHeaders
argument_list|,
name|MAPTestBase
operator|.
name|CUSTOMER_NAME
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|partialResponse
init|=
name|isIncomingPartialResponse
argument_list|(
name|message
argument_list|)
operator|||
name|outgoingPartialResponse
decl_stmt|;
name|verificationCache
operator|.
name|put
argument_list|(
name|MAPTest
operator|.
name|verifyHeaders
argument_list|(
name|wsaHeaders
argument_list|,
name|partialResponse
argument_list|,
name|isRequestLeg
argument_list|(
name|message
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|se
parameter_list|)
block|{
name|verificationCache
operator|.
name|put
argument_list|(
literal|"SOAP header verification failed: "
operator|+
name|se
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|recordWSAHeaders
parameter_list|(
name|List
argument_list|<
name|Header
argument_list|>
name|headers
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|wsaHeaders
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Header
argument_list|>
name|iter
init|=
name|headers
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|obj
init|=
name|iter
operator|.
name|next
argument_list|()
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|hdr
init|=
operator|(
name|Element
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|hdr
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|endsWith
argument_list|(
literal|"addressing"
argument_list|)
condition|)
block|{
name|currentNamespaceURI
operator|=
name|namespaceURI
expr_stmt|;
name|wsaHeaders
operator|.
name|add
argument_list|(
name|hdr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|MAPTestBase
operator|.
name|CUSTOMER_NAME
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
name|String
name|headerText
init|=
name|hdr
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|MAPTestBase
operator|.
name|CUSTOMER_KEY
operator|.
name|equals
argument_list|(
name|headerText
argument_list|)
condition|)
block|{
name|wsaHeaders
operator|.
name|add
argument_list|(
name|hdr
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|private
name|boolean
name|isRequestLeg
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
return|return
operator|(
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|ContextUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
operator|)
operator|||
operator|(
operator|!
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
operator|!
name|ContextUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
operator|)
return|;
block|}
specifier|private
name|boolean
name|isOutgoingPartialResponse
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|AddressingProperties
name|maps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|get
argument_list|(
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
argument_list|)
decl_stmt|;
return|return
name|ContextUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
operator|&&
operator|!
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|maps
operator|!=
literal|null
operator|&&
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|.
name|equals
argument_list|(
name|maps
operator|.
name|getTo
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isIncomingPartialResponse
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|SOAPException
block|{
name|AddressingProperties
name|maps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|get
argument_list|(
name|CLIENT_ADDRESSING_PROPERTIES_INBOUND
argument_list|)
decl_stmt|;
return|return
operator|!
name|ContextUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
operator|&&
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
operator|&&
name|maps
operator|!=
literal|null
operator|&&
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|.
name|equals
argument_list|(
name|maps
operator|.
name|getTo
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Marshaller
name|getMarshaller
parameter_list|()
throws|throws
name|JAXBException
block|{
name|JAXBContext
name|jaxbContext
init|=
name|VersionTransformer
operator|.
name|getExposedJAXBContext
argument_list|(
name|currentNamespaceURI
argument_list|)
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FRAGMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|marshaller
return|;
block|}
specifier|private
name|void
name|marshallFrom
parameter_list|(
name|String
name|from
parameter_list|,
name|Element
name|header
parameter_list|,
name|Marshaller
name|marshaller
parameter_list|)
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|Names
operator|.
name|WSA_NAMESPACE_NAME
operator|.
name|equals
argument_list|(
name|currentNamespaceURI
argument_list|)
condition|)
block|{
name|String
name|u
init|=
literal|"urn:piggyback_responder"
decl_stmt|;
name|AttributedURIType
name|value
init|=
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
name|ContextUtils
operator|.
name|getAttributedURI
argument_list|(
name|u
argument_list|)
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
operator|new
name|JAXBElement
argument_list|<
name|AttributedURIType
argument_list|>
argument_list|(
name|Names
operator|.
name|WSA_FROM_QNAME
argument_list|,
name|AttributedURIType
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|VersionTransformer
operator|.
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
operator|.
name|equals
argument_list|(
name|currentNamespaceURI
argument_list|)
condition|)
block|{
name|AttributedURI
name|value
init|=
name|VersionTransformer
operator|.
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createAttributedURI
argument_list|()
decl_stmt|;
name|value
operator|.
name|setValue
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
name|VersionTransformer
operator|.
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
argument_list|,
name|Names
operator|.
name|WSA_FROM_NAME
argument_list|)
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
operator|new
name|JAXBElement
argument_list|<
name|AttributedURI
argument_list|>
argument_list|(
name|qname
argument_list|,
name|AttributedURI
operator|.
name|class
argument_list|,
name|value
argument_list|)
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setVerificationCache
parameter_list|(
name|VerificationCache
name|cache
parameter_list|)
block|{
name|verificationCache
operator|=
name|cache
expr_stmt|;
block|}
block|}
end_class

end_unit

