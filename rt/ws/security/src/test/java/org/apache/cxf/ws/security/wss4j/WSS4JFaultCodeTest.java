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
name|DocumentBuilder
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
name|DocumentBuilderFactory
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
name|Document
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
name|helpers
operator|.
name|DOMUtils
operator|.
name|NullResolver
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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
name|WSConstants
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
name|handler
operator|.
name|WSHandlerConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * A number of tests for fault codes that are thrown from WSS4JInInterceptor.  */
end_comment

begin_class
specifier|public
class|class
name|WSS4JFaultCodeTest
extends|extends
name|AbstractSecurityTest
block|{
specifier|public
name|WSS4JFaultCodeTest
parameter_list|()
block|{     }
comment|/**      * Test for WSS4JInInterceptor when it receives a message with no security header.       */
annotation|@
name|Test
specifier|public
name|void
name|testNoSecurity
parameter_list|()
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
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|)
decl_stmt|;
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
name|byte
index|[]
name|docbytes
init|=
name|getMessageBytes
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|docbytes
argument_list|)
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringComments
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|NullResolver
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|db
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inHandler
init|=
operator|new
name|WSS4JInInterceptor
argument_list|()
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
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|ENCRYPT
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|DEC_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
name|TestPwdCallback
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|inmsg
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|RETURN_SECURITY_ERROR
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
try|try
block|{
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an message with no security header"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|fault
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|fault
operator|.
name|getReason
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"An error was discovered processing the<wsse:Security> header"
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|faultCode
init|=
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"InvalidSecurity"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
operator|.
name|equals
argument_list|(
name|faultCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that an invalid Timestamp gets mapped to a proper fault code       */
annotation|@
name|Test
specifier|public
name|void
name|testInvalidTimestamp
parameter_list|()
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
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|TTL_TIMESTAMP
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
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
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|byte
index|[]
name|docbytes
init|=
name|getMessageBytes
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|docbytes
argument_list|)
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringComments
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|NullResolver
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|db
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inHandler
init|=
operator|new
name|WSS4JInInterceptor
argument_list|()
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
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|TTL_TIMESTAMP
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|inmsg
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|RETURN_SECURITY_ERROR
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
try|try
block|{
comment|//
comment|// Sleep for over a second to make the timestamp invalid
comment|//
name|Thread
operator|.
name|sleep
argument_list|(
literal|1250
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an invalid Timestamp"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|fault
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|fault
operator|.
name|getReason
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Invalid timestamp"
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|faultCode
init|=
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"MessageExpired"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
operator|.
name|equals
argument_list|(
name|faultCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test that an action mismatch gets mapped to a proper fault code       */
annotation|@
name|Test
specifier|public
name|void
name|testActionMismatch
parameter_list|()
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
name|msg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
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
name|assertValid
argument_list|(
literal|"//wsse:Security"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|byte
index|[]
name|docbytes
init|=
name|getMessageBytes
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|docbytes
argument_list|)
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringComments
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|NullResolver
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|db
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inHandler
init|=
operator|new
name|WSS4JInInterceptor
argument_list|()
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
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|TIMESTAMP
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
name|TestPwdCallback
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|inmsg
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|RETURN_SECURITY_ERROR
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
try|try
block|{
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on an action mismatch"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|fault
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|fault
operator|.
name|getReason
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"An error was discovered processing the<wsse:Security> header"
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|faultCode
init|=
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"InvalidSecurity"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|fault
operator|.
name|getFaultCode
argument_list|()
operator|.
name|equals
argument_list|(
name|faultCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// See CXF-6900.
annotation|@
name|Test
specifier|public
name|void
name|testSignedEncryptedSOAP12Fault
parameter_list|()
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|readDocument
argument_list|(
literal|"wsse-response-fault.xml"
argument_list|)
decl_stmt|;
name|SoapMessage
name|msg
init|=
name|getSoapMessageForDom
argument_list|(
name|doc
argument_list|,
name|SOAPConstants
operator|.
name|SOAP_1_2_PROTOCOL
argument_list|)
decl_stmt|;
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
name|byte
index|[]
name|docbytes
init|=
name|getMessageBytes
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|docbytes
argument_list|)
argument_list|)
decl_stmt|;
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringComments
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|db
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|db
operator|.
name|setEntityResolver
argument_list|(
operator|new
name|NullResolver
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|db
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|WSS4JInInterceptor
name|inHandler
init|=
operator|new
name|WSS4JInInterceptor
argument_list|()
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
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|ACTION
argument_list|,
name|WSHandlerConstants
operator|.
name|SIGNATURE
operator|+
literal|" "
operator|+
name|WSHandlerConstants
operator|.
name|ENCRYPT
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|DEC_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
literal|"insecurity.properties"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
name|TestPwdCallback
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|WSHandlerConstants
operator|.
name|PW_CALLBACK_CLASS
argument_list|,
literal|"org.apache.cxf.ws.security.wss4j.TestPwdCallback"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
comment|// StaxUtils.print(saajMsg.getSOAPPart());
block|}
block|}
end_class

end_unit

