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
name|sts
operator|.
name|request
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|Unmarshaller
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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
name|sts
operator|.
name|common
operator|.
name|PasswordCallbackHandler
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
name|sts
operator|.
name|token
operator|.
name|canceller
operator|.
name|SCTCanceller
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
name|sts
operator|.
name|token
operator|.
name|validator
operator|.
name|SCTValidator
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenType
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
name|crypto
operator|.
name|Crypto
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
name|crypto
operator|.
name|CryptoFactory
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
name|ext
operator|.
name|WSSecurityException
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
name|engine
operator|.
name|WSSecurityEngine
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
name|RequestData
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
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|RequestParserUnitTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SECURITY_HEADER
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><wsse:Security "
operator|+
literal|"xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\""
operator|+
literal|" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\""
operator|+
literal|"><wsse:UsernameToken wsu:Id=\"UsernameToken-5\"><wsse:Username>alice</wsse:Username>"
operator|+
literal|"<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username"
operator|+
literal|"-token-profile-1.0#PasswordText\">clarinet</wsse:Password>"
operator|+
literal|"</wsse:UsernameToken><wsc:SecurityContextToken "
operator|+
literal|"xmlns:wsc=\"http://schemas.xmlsoap.org/ws/2005/02/sc\" "
operator|+
literal|"xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" "
operator|+
literal|"wsu:Id=\"sct\"><wsc:Identifier>check</wsc:Identifier></wsc:SecurityContextToken></wsse:Security>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SECURITY_HEADER_X509
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><wsse:Security "
operator|+
literal|"xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\""
operator|+
literal|" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\""
operator|+
literal|"><wsse:UsernameToken wsu:Id=\"UsernameToken-5\"><wsse:Username>alice</wsse:Username>"
operator|+
literal|"<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username"
operator|+
literal|"-token-profile-1.0#PasswordText\">clarinet</wsse:Password>"
operator|+
literal|"</wsse:UsernameToken><wsse:BinarySecurityToken "
operator|+
literal|"xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" "
operator|+
literal|"EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0"
operator|+
literal|"#Base64Binary\" "
operator|+
literal|"ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0"
operator|+
literal|"#X509v3\" "
operator|+
literal|"wsu:Id=\"x509\">"
operator|+
literal|"MIIEFjCCA3+gAwIBAgIJAJORWX2Xsa8DMA0GCSqGSIb3DQEBBQUAMIG5MQswCQYDVQQGEwJVUzERMA8GA1UECBMITmV3IFlvcm"
operator|+
literal|"sxFjAUBgNVBAcTDU5pYWdhcmEgRmFsbHMxLDAqBgNVBAoTI1NhbXBsZSBDbGllbnQgLS0gTk9UIEZPUiBQUk9EVUNUSU9OMRYw"
operator|+
literal|"FAYDVQQLEw1JVCBEZXBhcnRtZW50MRcwFQYDVQQDEw53d3cuY2xpZW50LmNvbTEgMB4GCSqGSIb3DQEJARYRY2xpZW50QGNsaW"
operator|+
literal|"VudC5jb20wHhcNMTEwMjA5MTgzMDI3WhcNMjEwMjA2MTgzMDI3WjCBuTELMAkGA1UEBhMCVVMxETAPBgNVBAgTCE5ldyBZb3Jr"
operator|+
literal|"MRYwFAYDVQQHEw1OaWFnYXJhIEZhbGxzMSwwKgYDVQQKEyNTYW1wbGUgQ2xpZW50IC0tIE5PVCBGT1IgUFJPRFVDVElPTjEWMB"
operator|+
literal|"QGA1UECxMNSVQgRGVwYXJ0bWVudDEXMBUGA1UEAxMOd3d3LmNsaWVudC5jb20xIDAeBgkqhkiG9w0BCQEWEWNsaWVudEBjbGll"
operator|+
literal|"bnQuY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDauFNVqi4B2+u/PC9ktDkn82bglEQYcL4o5JRUhQVEhTK2iEloz1"
operator|+
literal|"Rvo/qyfDhBPc1lzIUn4ams+DKBSSjZMCgop3XbeCXzIVP784ruC8HF5QrYsXUQfTc7lzqafXZXH8Bk89gSScA1fFme6TpvYzM0"
operator|+
literal|"zjBETSXADtKOs9oKB2VOIwIDAQABo4IBIjCCAR4wHQYDVR0OBBYEFFIz+0BSZlLtXkA/udRjRgphtREuMIHuBgNVHSMEgeYwge"
operator|+
literal|"OAFFIz+0BSZlLtXkA/udRjRgphtREuoYG/pIG8MIG5MQswCQYDVQQGEwJVUzERMA8GA1UECBMITmV3IFlvcmsxFjAUBgNVBAcT"
operator|+
literal|"DU5pYWdhcmEgRmFsbHMxLDAqBgNVBAoTI1NhbXBsZSBDbGllbnQgLS0gTk9UIEZPUiBQUk9EVUNUSU9OMRYwFAYDVQQLEw1JVC"
operator|+
literal|"BEZXBhcnRtZW50MRcwFQYDVQQDEw53d3cuY2xpZW50LmNvbTEgMB4GCSqGSIb3DQEJARYRY2xpZW50QGNsaWVudC5jb22CCQCT"
operator|+
literal|"kVl9l7GvAzAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4GBAEjEr9QfaYsZf7ELnqB++OkWcKxpMt1Yj/VOyL99AekkVT"
operator|+
literal|"M+rRHCU9Bu+tncMNsfy8mIXUC1JqKQ+Cq5RlaDh/ujzt6i17G7uSGd6U1U/DPZBqTm3Dxwl1cMAGU/CoAKTWE+o+fS4Q2xHv7L"
operator|+
literal|"1KiXQQc9EWJ4C34Ik45fB6g3DiTj</wsse:BinarySecurityToken></wsse:Security>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CANCEL_SCT_REFERENCE
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<wst:RequestSecurityToken xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">"
operator|+
literal|"<wst:TokenType>http://schemas.xmlsoap.org/ws/2005/02/sc/sct</wst:TokenType>"
operator|+
literal|"<wst:RequestType>http://docs.oasis-open.org/ws-sx/ws-trust/200512/Cancel</wst:RequestType>"
operator|+
literal|"<wst:CancelTarget>"
operator|+
literal|"<wsse:SecurityTokenReference "
operator|+
literal|"xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
operator|+
literal|"<wsse:Reference URI=\"#sct\"></wsse:Reference></wsse:SecurityTokenReference>"
operator|+
literal|"</wst:CancelTarget>"
operator|+
literal|"</wst:RequestSecurityToken>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VALIDATE_SCT_REFERENCE
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<wst:RequestSecurityToken xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">"
operator|+
literal|"<wst:TokenType>http://schemas.xmlsoap.org/ws/2005/02/sc/sct</wst:TokenType>"
operator|+
literal|"<wst:RequestType>http://docs.oasis-open.org/ws-sx/ws-trust/200512/Validate</wst:RequestType>"
operator|+
literal|"<wst:ValidateTarget>"
operator|+
literal|"<wsse:SecurityTokenReference "
operator|+
literal|"xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
operator|+
literal|"<wsse:Reference URI=\"#sct\"></wsse:Reference></wsse:SecurityTokenReference>"
operator|+
literal|"</wst:ValidateTarget>"
operator|+
literal|"</wst:RequestSecurityToken>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USE_KEY_X509_REFERENCE
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
literal|"<wst:RequestSecurityToken xmlns:wst=\"http://docs.oasis-open.org/ws-sx/ws-trust/200512\">"
operator|+
literal|"<wst:TokenType>http://schemas.xmlsoap.org/ws/2005/02/sc/sct</wst:TokenType>"
operator|+
literal|"<wst:RequestType>http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue</wst:RequestType>"
operator|+
literal|"<wst:UseKey>"
operator|+
literal|"<wsse:SecurityTokenReference "
operator|+
literal|"xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
operator|+
literal|"<wsse:Reference URI=\"#x509\"></wsse:Reference></wsse:SecurityTokenReference>"
operator|+
literal|"</wst:UseKey>"
operator|+
literal|"</wst:RequestSecurityToken>"
decl_stmt|;
comment|/**      * Test for fetching (and cancelling) a referenced SecurityContextToken.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCancelSCT
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|secHeaderElement
init|=
operator|(
name|Element
operator|)
name|parseStringToElement
argument_list|(
name|SECURITY_HEADER
argument_list|)
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|RequestSecurityTokenType
name|request
init|=
name|createJaxbObject
argument_list|(
name|CANCEL_SCT_REFERENCE
argument_list|)
decl_stmt|;
name|RequestParser
name|parser
init|=
operator|new
name|RequestParser
argument_list|()
decl_stmt|;
comment|// Mock up message context
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|WrappedMessageContext
name|msgContext
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// Process the security header and store the results in the message context
name|WSSecurityEngine
name|securityEngine
init|=
operator|new
name|WSSecurityEngine
argument_list|()
decl_stmt|;
name|RequestData
name|reqData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|reqData
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|WSHandlerResult
name|results
init|=
name|securityEngine
operator|.
name|processSecurityHeader
argument_list|(
name|secHeaderElement
argument_list|,
name|reqData
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|resultsList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|resultsList
operator|.
name|add
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|msgContext
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|resultsList
argument_list|)
expr_stmt|;
name|RequestRequirements
name|requestRequirements
init|=
name|parser
operator|.
name|parseRequest
argument_list|(
name|request
argument_list|,
name|msgContext
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SCTCanceller
name|sctCanceller
init|=
operator|new
name|SCTCanceller
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sctCanceller
operator|.
name|canHandleToken
argument_list|(
name|requestRequirements
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getCancelTarget
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for fetching (and validating) a referenced SecurityContextToken.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidateSCT
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|secHeaderElement
init|=
operator|(
name|Element
operator|)
name|parseStringToElement
argument_list|(
name|SECURITY_HEADER
argument_list|)
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|RequestSecurityTokenType
name|request
init|=
name|createJaxbObject
argument_list|(
name|VALIDATE_SCT_REFERENCE
argument_list|)
decl_stmt|;
name|RequestParser
name|parser
init|=
operator|new
name|RequestParser
argument_list|()
decl_stmt|;
comment|// Mock up message context
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|WrappedMessageContext
name|msgContext
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// Process the security header and store the results in the message context
name|WSSecurityEngine
name|securityEngine
init|=
operator|new
name|WSSecurityEngine
argument_list|()
decl_stmt|;
name|RequestData
name|reqData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|reqData
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|WSHandlerResult
name|results
init|=
name|securityEngine
operator|.
name|processSecurityHeader
argument_list|(
name|secHeaderElement
argument_list|,
name|reqData
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|resultsList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|resultsList
operator|.
name|add
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|msgContext
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|resultsList
argument_list|)
expr_stmt|;
name|RequestRequirements
name|requestRequirements
init|=
name|parser
operator|.
name|parseRequest
argument_list|(
name|request
argument_list|,
name|msgContext
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SCTValidator
name|sctValidator
init|=
operator|new
name|SCTValidator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|sctValidator
operator|.
name|canHandleToken
argument_list|(
name|requestRequirements
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getValidateTarget
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test for fetching (and validating) a referenced BinarySecurityToken from a UseKey Element.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testUseKeyX509
parameter_list|()
throws|throws
name|Exception
block|{
name|Element
name|secHeaderElement
init|=
operator|(
name|Element
operator|)
name|parseStringToElement
argument_list|(
name|SECURITY_HEADER_X509
argument_list|)
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
name|RequestSecurityTokenType
name|request
init|=
name|createJaxbObject
argument_list|(
name|USE_KEY_X509_REFERENCE
argument_list|)
decl_stmt|;
name|RequestParser
name|parser
init|=
operator|new
name|RequestParser
argument_list|()
decl_stmt|;
comment|// Mock up message context
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|WrappedMessageContext
name|msgContext
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|msg
argument_list|)
decl_stmt|;
comment|// Process the security header and store the results in the message context
name|WSSecurityEngine
name|securityEngine
init|=
operator|new
name|WSSecurityEngine
argument_list|()
decl_stmt|;
name|RequestData
name|reqData
init|=
operator|new
name|RequestData
argument_list|()
decl_stmt|;
name|reqData
operator|.
name|setSigVerCrypto
argument_list|(
name|getCrypto
argument_list|()
argument_list|)
expr_stmt|;
name|reqData
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|WSHandlerResult
name|results
init|=
name|securityEngine
operator|.
name|processSecurityHeader
argument_list|(
name|secHeaderElement
argument_list|,
name|reqData
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|resultsList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|resultsList
operator|.
name|add
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|msgContext
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|resultsList
argument_list|)
expr_stmt|;
name|RequestRequirements
name|requestRequirements
init|=
name|parser
operator|.
name|parseRequest
argument_list|(
name|request
argument_list|,
name|msgContext
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|requestRequirements
operator|.
name|getKeyRequirements
argument_list|()
operator|.
name|getReceivedCredential
argument_list|()
operator|.
name|getX509Cert
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Document
name|parseStringToElement
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|Exception
block|{
name|DocumentBuilderFactory
name|builderFac
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|builderFac
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|builderFac
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|builderFac
operator|.
name|setIgnoringElementContentWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|docBuilder
init|=
name|builderFac
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|docBuilder
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|str
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|RequestSecurityTokenType
name|createJaxbObject
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|jaxbContext
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.cxf.ws.security.sts.provider.model"
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|jaxbElement
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|str
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|RequestSecurityTokenType
operator|)
name|jaxbElement
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|private
name|Crypto
name|getCrypto
parameter_list|()
throws|throws
name|WSSecurityException
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.wss4j.crypto.provider"
argument_list|,
literal|"org.apache.wss4j.common.crypto.Merlin"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.wss4j.crypto.merlin.keystore.password"
argument_list|,
literal|"stsspass"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.wss4j.crypto.merlin.keystore.file"
argument_list|,
literal|"keys/stsstore.jks"
argument_list|)
expr_stmt|;
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|properties
argument_list|)
return|;
block|}
block|}
end_class

end_unit

