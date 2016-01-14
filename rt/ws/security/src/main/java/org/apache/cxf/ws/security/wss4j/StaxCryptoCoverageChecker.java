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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|Names
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
name|stax
operator|.
name|securityEvent
operator|.
name|WSSecurityEventConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|AbstractSecuredElementSecurityEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEventConstants
operator|.
name|Event
import|;
end_import

begin_comment
comment|/**  * This interceptor handles parsing the StaX WS-Security results (events) + checks that the  * specified crypto coverage events actually occurred. The default functionality is to enforce   * that the SOAP Body, Timestamp, and WS-Addressing ReplyTo and FaultTo headers must be signed,  * and the UsernameToken must be encrypted (if they exist in the message payload).  *   * Note that this interceptor must be explicitly added to the InInterceptor chain.  */
end_comment

begin_class
specifier|public
class|class
name|StaxCryptoCoverageChecker
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_NS
init|=
name|WSConstants
operator|.
name|URI_SOAP11_ENV
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP12_NS
init|=
name|WSConstants
operator|.
name|URI_SOAP12_ENV
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSU_NS
init|=
name|WSConstants
operator|.
name|WSU_NS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSSE_NS
init|=
name|WSConstants
operator|.
name|WSSE_NS
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NS
init|=
name|Names
operator|.
name|WSA_NAMESPACE_NAME
decl_stmt|;
specifier|private
name|boolean
name|signBody
decl_stmt|;
specifier|private
name|boolean
name|signTimestamp
decl_stmt|;
specifier|private
name|boolean
name|encryptBody
decl_stmt|;
specifier|private
name|boolean
name|signAddressingHeaders
decl_stmt|;
specifier|private
name|boolean
name|signUsernameToken
decl_stmt|;
specifier|private
name|boolean
name|encryptUsernameToken
decl_stmt|;
specifier|public
name|StaxCryptoCoverageChecker
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
comment|// Sign SOAP Body
name|setSignBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Sign Timestamp
name|setSignTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Sign Addressing Headers
name|setSignAddressingHeaders
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Encrypt UsernameToken
name|setEncryptUsernameToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
throws|throws
name|Fault
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|incomingSecurityEventList
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|soapMessage
operator|.
name|get
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".in"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|incomingSecurityEventList
operator|!=
literal|null
condition|)
block|{
comment|// Get all Signed/Encrypted Results
name|results
operator|.
name|addAll
argument_list|(
name|getEventFromResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|SIGNED_PART
argument_list|,
name|incomingSecurityEventList
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|addAll
argument_list|(
name|getEventFromResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|SignedElement
argument_list|,
name|incomingSecurityEventList
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|encryptBody
operator|||
name|encryptUsernameToken
condition|)
block|{
name|results
operator|.
name|addAll
argument_list|(
name|getEventFromResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|ENCRYPTED_PART
argument_list|,
name|incomingSecurityEventList
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|addAll
argument_list|(
name|getEventFromResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|EncryptedElement
argument_list|,
name|incomingSecurityEventList
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|checkSignedBody
argument_list|(
name|results
argument_list|)
expr_stmt|;
name|checkEncryptedBody
argument_list|(
name|results
argument_list|)
expr_stmt|;
if|if
condition|(
name|signTimestamp
condition|)
block|{
comment|// We only insist on the Timestamp being signed if it is actually present in the message
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|timestampResults
init|=
name|getEventFromResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|TIMESTAMP
argument_list|,
name|incomingSecurityEventList
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|timestampResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|checkSignedTimestamp
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|signAddressingHeaders
condition|)
block|{
name|AddressingProperties
name|addressingProperties
init|=
operator|(
name|AddressingProperties
operator|)
name|soapMessage
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context.inbound"
argument_list|)
decl_stmt|;
name|checkSignedAddressing
argument_list|(
name|results
argument_list|,
name|addressingProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signUsernameToken
operator|||
name|encryptUsernameToken
condition|)
block|{
comment|// We only insist on the UsernameToken being signed/encrypted if it is actually
comment|// present in the message
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|usernameTokenResults
init|=
name|getEventFromResults
argument_list|(
name|WSSecurityEventConstants
operator|.
name|USERNAME_TOKEN
argument_list|,
name|incomingSecurityEventList
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|usernameTokenResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|signUsernameToken
condition|)
block|{
name|checkSignedUsernameToken
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encryptUsernameToken
condition|)
block|{
name|checkEncryptedUsernameToken
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
throw|throw
name|createSoapFault
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|getEventFromResults
parameter_list|(
name|Event
name|event
parameter_list|,
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|incomingSecurityEventList
parameter_list|)
block|{
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|incomingEvent
range|:
name|incomingSecurityEventList
control|)
block|{
if|if
condition|(
name|event
operator|==
name|incomingEvent
operator|.
name|getSecurityEventType
argument_list|()
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|incomingEvent
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
specifier|private
name|void
name|checkSignedBody
parameter_list|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|signBody
condition|)
block|{
return|return;
block|}
name|boolean
name|isBodySigned
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|signedEvent
range|:
name|results
control|)
block|{
name|AbstractSecuredElementSecurityEvent
name|securedEvent
init|=
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|signedEvent
decl_stmt|;
if|if
condition|(
operator|!
name|securedEvent
operator|.
name|isSigned
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|QName
argument_list|>
name|signedPath
init|=
name|securedEvent
operator|.
name|getElementPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|isBody
argument_list|(
name|signedPath
argument_list|)
condition|)
block|{
name|isBodySigned
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isBodySigned
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The SOAP Body is not signed"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|checkEncryptedBody
parameter_list|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|encryptBody
condition|)
block|{
return|return;
block|}
name|boolean
name|isBodyEncrypted
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|signedEvent
range|:
name|results
control|)
block|{
name|AbstractSecuredElementSecurityEvent
name|securedEvent
init|=
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|signedEvent
decl_stmt|;
if|if
condition|(
operator|!
name|securedEvent
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|QName
argument_list|>
name|encryptedPath
init|=
name|securedEvent
operator|.
name|getElementPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|isBody
argument_list|(
name|encryptedPath
argument_list|)
condition|)
block|{
name|isBodyEncrypted
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isBodyEncrypted
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The SOAP Body is not encrypted"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|checkSignedTimestamp
parameter_list|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|signTimestamp
condition|)
block|{
return|return;
block|}
name|boolean
name|isTimestampSigned
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|signedEvent
range|:
name|results
control|)
block|{
name|AbstractSecuredElementSecurityEvent
name|securedEvent
init|=
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|signedEvent
decl_stmt|;
if|if
condition|(
operator|!
name|securedEvent
operator|.
name|isSigned
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|QName
argument_list|>
name|signedPath
init|=
name|securedEvent
operator|.
name|getElementPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|isTimestamp
argument_list|(
name|signedPath
argument_list|)
condition|)
block|{
name|isTimestampSigned
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isTimestampSigned
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The Timestamp is not signed"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|checkSignedAddressing
parameter_list|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
parameter_list|,
name|AddressingProperties
name|addressingProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|signAddressingHeaders
operator|||
name|addressingProperties
operator|==
literal|null
operator|||
operator|(
name|addressingProperties
operator|.
name|getReplyTo
argument_list|()
operator|==
literal|null
operator|&&
name|addressingProperties
operator|.
name|getFaultTo
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
return|return;
block|}
name|boolean
name|isReplyToSigned
init|=
literal|false
decl_stmt|;
name|boolean
name|isFaultToSigned
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|signedEvent
range|:
name|results
control|)
block|{
name|AbstractSecuredElementSecurityEvent
name|securedEvent
init|=
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|signedEvent
decl_stmt|;
if|if
condition|(
operator|!
name|securedEvent
operator|.
name|isSigned
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|QName
argument_list|>
name|signedPath
init|=
name|securedEvent
operator|.
name|getElementPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|isReplyTo
argument_list|(
name|signedPath
argument_list|)
condition|)
block|{
name|isReplyToSigned
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|isFaultTo
argument_list|(
name|signedPath
argument_list|)
condition|)
block|{
name|isFaultToSigned
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|isReplyToSigned
operator|&&
name|isFaultToSigned
condition|)
block|{
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isReplyToSigned
operator|&&
operator|(
name|addressingProperties
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The Addressing headers are not signed"
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isFaultToSigned
operator|&&
operator|(
name|addressingProperties
operator|.
name|getFaultTo
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The Addressing headers are not signed"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|checkSignedUsernameToken
parameter_list|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|signUsernameToken
condition|)
block|{
return|return;
block|}
name|boolean
name|isUsernameTokenSigned
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|signedEvent
range|:
name|results
control|)
block|{
name|AbstractSecuredElementSecurityEvent
name|securedEvent
init|=
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|signedEvent
decl_stmt|;
if|if
condition|(
operator|!
name|securedEvent
operator|.
name|isSigned
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|QName
argument_list|>
name|signedPath
init|=
name|securedEvent
operator|.
name|getElementPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|isUsernameToken
argument_list|(
name|signedPath
argument_list|)
condition|)
block|{
name|isUsernameTokenSigned
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isUsernameTokenSigned
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The UsernameToken is not signed"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|checkEncryptedUsernameToken
parameter_list|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|results
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|encryptUsernameToken
condition|)
block|{
return|return;
block|}
name|boolean
name|isUsernameTokenEncrypted
init|=
literal|false
decl_stmt|;
for|for
control|(
name|SecurityEvent
name|encryptedEvent
range|:
name|results
control|)
block|{
name|AbstractSecuredElementSecurityEvent
name|securedEvent
init|=
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|encryptedEvent
decl_stmt|;
if|if
condition|(
operator|!
name|securedEvent
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|List
argument_list|<
name|QName
argument_list|>
name|encryptedPath
init|=
name|securedEvent
operator|.
name|getElementPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|isUsernameToken
argument_list|(
name|encryptedPath
argument_list|)
condition|)
block|{
name|isUsernameTokenEncrypted
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|isUsernameTokenEncrypted
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
operator|new
name|Exception
argument_list|(
literal|"The UsernameToken is not encrypted"
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|private
name|boolean
name|isEnvelope
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
literal|"Envelope"
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
operator|(
name|SOAP_NS
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|SOAP12_NS
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|private
name|boolean
name|isSoapHeader
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
literal|"Header"
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
operator|(
name|SOAP_NS
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|SOAP12_NS
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|private
name|boolean
name|isSecurityHeader
parameter_list|(
name|QName
name|qname
parameter_list|)
block|{
return|return
literal|"Security"
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|WSSE_NS
operator|.
name|equals
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isTimestamp
parameter_list|(
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
parameter_list|)
block|{
return|return
name|qnames
operator|!=
literal|null
operator|&&
name|qnames
operator|.
name|size
argument_list|()
operator|==
literal|4
operator|&&
name|isEnvelope
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|&&
name|isSoapHeader
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|&&
name|isSecurityHeader
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|&&
literal|"Timestamp"
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|WSU_NS
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isReplyTo
parameter_list|(
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
parameter_list|)
block|{
return|return
name|qnames
operator|!=
literal|null
operator|&&
name|qnames
operator|.
name|size
argument_list|()
operator|==
literal|3
operator|&&
name|isEnvelope
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|&&
name|isSoapHeader
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|&&
literal|"ReplyTo"
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|WSA_NS
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isFaultTo
parameter_list|(
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
parameter_list|)
block|{
return|return
name|qnames
operator|!=
literal|null
operator|&&
name|qnames
operator|.
name|size
argument_list|()
operator|==
literal|3
operator|&&
name|isEnvelope
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|&&
name|isSoapHeader
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|&&
literal|"FaultTo"
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|WSA_NS
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isBody
parameter_list|(
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
parameter_list|)
block|{
return|return
name|qnames
operator|!=
literal|null
operator|&&
name|qnames
operator|.
name|size
argument_list|()
operator|==
literal|2
operator|&&
name|isEnvelope
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|&&
literal|"Body"
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
operator|(
name|SOAP_NS
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|||
name|SOAP12_NS
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|private
name|boolean
name|isUsernameToken
parameter_list|(
name|List
argument_list|<
name|QName
argument_list|>
name|qnames
parameter_list|)
block|{
return|return
name|qnames
operator|!=
literal|null
operator|&&
name|qnames
operator|.
name|size
argument_list|()
operator|==
literal|4
operator|&&
name|isEnvelope
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|&&
name|isSoapHeader
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|&&
name|isSecurityHeader
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|&&
literal|"UsernameToken"
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
name|WSSE_NS
operator|.
name|equals
argument_list|(
name|qnames
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isSignBody
parameter_list|()
block|{
return|return
name|signBody
return|;
block|}
specifier|public
specifier|final
name|void
name|setSignBody
parameter_list|(
name|boolean
name|signBody
parameter_list|)
block|{
name|this
operator|.
name|signBody
operator|=
name|signBody
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSignTimestamp
parameter_list|()
block|{
return|return
name|signTimestamp
return|;
block|}
specifier|public
specifier|final
name|void
name|setSignTimestamp
parameter_list|(
name|boolean
name|signTimestamp
parameter_list|)
block|{
name|this
operator|.
name|signTimestamp
operator|=
name|signTimestamp
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEncryptBody
parameter_list|()
block|{
return|return
name|encryptBody
return|;
block|}
specifier|public
specifier|final
name|void
name|setEncryptBody
parameter_list|(
name|boolean
name|encryptBody
parameter_list|)
block|{
name|this
operator|.
name|encryptBody
operator|=
name|encryptBody
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSignAddressingHeaders
parameter_list|()
block|{
return|return
name|signAddressingHeaders
return|;
block|}
specifier|public
specifier|final
name|void
name|setSignAddressingHeaders
parameter_list|(
name|boolean
name|signAddressingHeaders
parameter_list|)
block|{
name|this
operator|.
name|signAddressingHeaders
operator|=
name|signAddressingHeaders
expr_stmt|;
block|}
comment|/**      * Create a SoapFault from a WSSecurityException, following the SOAP Message Security      * 1.1 specification, chapter 12 "Error Handling".      *       * When the Soap version is 1.1 then set the Fault/Code/Value from the fault code      * specified in the WSSecurityException (if it exists).      *       * Otherwise set the Fault/Code/Value to env:Sender and the Fault/Code/Subcode/Value      * as the fault code from the WSSecurityException.      */
specifier|private
name|SoapFault
name|createSoapFault
parameter_list|(
name|SoapVersion
name|version
parameter_list|,
name|WSSecurityException
name|e
parameter_list|)
block|{
name|SoapFault
name|fault
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
name|faultCode
init|=
name|e
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|==
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|=
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
name|faultCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fault
operator|=
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
name|version
operator|.
name|getSender
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|!=
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setSubCode
argument_list|(
name|faultCode
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fault
return|;
block|}
specifier|public
name|boolean
name|isSignUsernameToken
parameter_list|()
block|{
return|return
name|signUsernameToken
return|;
block|}
specifier|public
name|void
name|setSignUsernameToken
parameter_list|(
name|boolean
name|signUsernameToken
parameter_list|)
block|{
name|this
operator|.
name|signUsernameToken
operator|=
name|signUsernameToken
expr_stmt|;
block|}
specifier|public
name|boolean
name|isEncryptUsernameToken
parameter_list|()
block|{
return|return
name|encryptUsernameToken
return|;
block|}
specifier|public
name|void
name|setEncryptUsernameToken
parameter_list|(
name|boolean
name|encryptUsernameToken
parameter_list|)
block|{
name|this
operator|.
name|encryptUsernameToken
operator|=
name|encryptUsernameToken
expr_stmt|;
block|}
block|}
end_class

end_unit

