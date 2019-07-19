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
name|security
operator|.
name|wss4j
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|saaj
operator|.
name|SAAJStreamWriter
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
name|ExchangeImpl
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
name|MessageImpl
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
name|PhaseInterceptor
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
name|test
operator|.
name|AbstractCXFTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractSecurityTest
extends|extends
name|AbstractCXFTest
block|{
specifier|public
name|AbstractSecurityTest
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsse"
argument_list|,
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsse11"
argument_list|,
name|WSS4JConstants
operator|.
name|WSSE11_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"ds"
argument_list|,
name|WSS4JConstants
operator|.
name|SIG_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"s"
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"xenc"
argument_list|,
name|WSS4JConstants
operator|.
name|ENC_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"wsu"
argument_list|,
name|WSS4JConstants
operator|.
name|WSU_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"saml1"
argument_list|,
name|WSS4JConstants
operator|.
name|SAML_NS
argument_list|)
expr_stmt|;
name|addNamespace
argument_list|(
literal|"saml2"
argument_list|,
name|WSS4JConstants
operator|.
name|SAML2_NS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads a classpath resource into a Document.      * @param name the name of the classpath resource      */
specifier|protected
name|Document
name|readDocument
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
throws|,
name|ParserConfigurationException
block|{
name|InputStream
name|inStream
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|StaxUtils
operator|.
name|read
argument_list|(
name|inStream
argument_list|)
return|;
block|}
comment|/**      * Creates a {@link SoapMessage} from the contents of a document.      * @param doc the document containing the SOAP content.      */
specifier|protected
name|SoapMessage
name|getSoapMessageForDom
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|,
name|SOAPConstants
operator|.
name|SOAP_1_1_PROTOCOL
argument_list|)
return|;
block|}
specifier|protected
name|SoapMessage
name|getSoapMessageForDom
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|protocol
parameter_list|)
throws|throws
name|Exception
block|{
name|SOAPMessage
name|saajMsg
init|=
name|MessageFactory
operator|.
name|newInstance
argument_list|(
name|protocol
argument_list|)
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|SOAPPart
name|part
init|=
name|saajMsg
operator|.
name|getSOAPPart
argument_list|()
decl_stmt|;
name|SAAJStreamWriter
name|writer
init|=
operator|new
name|SAAJStreamWriter
argument_list|(
name|part
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|doc
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|saajMsg
operator|.
name|saveChanges
argument_list|()
expr_stmt|;
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|SoapMessage
name|msg
init|=
operator|new
name|SoapMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|saajMsg
argument_list|)
expr_stmt|;
return|return
name|msg
return|;
block|}
specifier|protected
name|byte
index|[]
name|getMessageBytes
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|outputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|byteArrayWriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|writeDocument
argument_list|(
name|doc
argument_list|,
name|byteArrayWriter
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|byteArrayWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|outputStream
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|protected
name|SoapMessage
name|makeInvocation
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outProperties
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|xpaths
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inProperties
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|)
decl_stmt|;
name|WSS4JOutInterceptor
name|ohandler
init|=
operator|new
name|WSS4JOutInterceptor
argument_list|()
decl_stmt|;
name|PhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
name|handler
init|=
name|ohandler
operator|.
name|createEndingInterceptor
argument_list|()
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|outProperties
operator|.
name|keySet
argument_list|()
control|)
block|{
name|msg
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|outProperties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|handler
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|SOAPMessage
name|saajMsg
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|)
decl_stmt|;
name|doc
operator|=
name|saajMsg
operator|.
name|getSOAPPart
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|xpath
range|:
name|xpaths
control|)
block|{
name|assertValid
argument_list|(
name|xpath
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|docbytes
init|=
name|getMessageBytes
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|docbytes
argument_list|)
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inHandler
init|=
operator|new
name|WSS4JInInterceptor
argument_list|(
name|inProperties
argument_list|)
decl_stmt|;
name|SoapMessage
name|inmsg
init|=
operator|new
name|SoapMessage
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setInMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
name|inmsg
operator|.
name|setContent
argument_list|(
name|SOAPMessage
operator|.
name|class
argument_list|,
name|saajMsg
argument_list|)
expr_stmt|;
name|Element
name|securityHeaderElem
init|=
name|WSSecurityUtil
operator|.
name|getSecurityHeader
argument_list|(
name|doc
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|SoapHeader
name|securityHeader
init|=
operator|new
name|SoapHeader
argument_list|(
operator|new
name|QName
argument_list|(
name|securityHeaderElem
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|securityHeaderElem
operator|.
name|getLocalName
argument_list|()
argument_list|)
argument_list|,
name|securityHeaderElem
argument_list|)
decl_stmt|;
name|inmsg
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|securityHeader
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
return|return
name|inmsg
return|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
block|{
name|String
name|tmpDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpDir
operator|!=
literal|null
condition|)
block|{
name|File
index|[]
name|tmpFiles
init|=
operator|new
name|File
argument_list|(
name|tmpDir
argument_list|)
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmpFiles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|tmpFile
range|:
name|tmpFiles
control|)
block|{
if|if
condition|(
name|tmpFile
operator|.
name|exists
argument_list|()
operator|&&
operator|(
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"ws-security.nonce.cache.instance"
argument_list|)
operator|||
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"wss4j-nonce-cache"
argument_list|)
operator|||
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"ws-security.timestamp.cache.instance"
argument_list|)
operator|||
name|tmpFile
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"wss4j-timestamp-cache"
argument_list|)
operator|)
condition|)
block|{
name|tmpFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

