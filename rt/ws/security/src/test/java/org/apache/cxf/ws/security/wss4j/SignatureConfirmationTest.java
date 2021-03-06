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
name|soap
operator|.
name|SOAPMessage
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
name|wss4j
operator|.
name|common
operator|.
name|ConfigurationConstants
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
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * This a test of the Signature Confirmation functionality that is contained in the  * WS-Security 1.1 specification. The requestor signs an outbound SOAP message and saves  * the signature. The responder processes the inbound SOAP message and saves the received  * signature. Then in the responding message the received signature is attached in the  * form of a wsse11:SignatureConfirmation blob. The requestor processes this blob and  * checks to make sure that the signature value contained therein matches the saved value.  */
end_comment

begin_class
specifier|public
class|class
name|SignatureConfirmationTest
extends|extends
name|AbstractSecurityTest
block|{
specifier|public
name|SignatureConfirmationTest
parameter_list|()
block|{     }
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSignatureConfirmationRequest
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
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENABLE_SIGNATURE_CONFIRMATION
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
literal|"outsecurity.properties"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|USER
argument_list|,
literal|"myalias"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"myAliasPassword"
argument_list|)
expr_stmt|;
comment|//
comment|// This is necessary to convince the WSS4JOutInterceptor that we're
comment|// functioning as a requestor
comment|//
name|msg
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|,
literal|true
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
name|assertValid
argument_list|(
literal|"//wsse:Security/ds:Signature"
argument_list|,
name|doc
argument_list|)
expr_stmt|;
comment|//
comment|// Save the signature for future confirmation
comment|//
name|Set
argument_list|<
name|Integer
argument_list|>
name|sigv
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|msg
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|SEND_SIGV
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sigv
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sigv
operator|.
name|isEmpty
argument_list|()
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
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|SIGNATURE
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|setProperty
argument_list|(
name|ConfigurationConstants
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
name|ConfigurationConstants
operator|.
name|ENABLE_SIGNATURE_CONFIRMATION
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
comment|//
comment|// Check that the inbound signature result was saved
comment|//
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|sigReceived
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|inmsg
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sigReceived
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|sigReceived
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|testSignatureConfirmationResponse
argument_list|(
name|sigv
argument_list|,
name|sigReceived
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|testSignatureConfirmationResponse
parameter_list|(
name|Set
argument_list|<
name|Integer
argument_list|>
name|sigSaved
parameter_list|,
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|sigReceived
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
name|msg
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
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
name|RECV_RESULTS
argument_list|,
name|sigReceived
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
comment|// assertValid("//wsse:Security/wsse11:SignatureConfirmation", doc);
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
name|ConfigurationConstants
operator|.
name|ACTION
argument_list|,
name|ConfigurationConstants
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
name|inmsg
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|SEND_SIGV
argument_list|,
name|sigSaved
argument_list|)
expr_stmt|;
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

