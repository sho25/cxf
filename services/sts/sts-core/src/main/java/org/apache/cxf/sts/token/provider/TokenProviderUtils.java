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
name|token
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Level
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
name|ws
operator|.
name|WebServiceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
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
name|sts
operator|.
name|STSConstants
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
name|STSPropertiesMBean
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
name|request
operator|.
name|KeyRequirements
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
name|service
operator|.
name|EncryptionProperties
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
name|EndpointReferenceType
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
name|wss4j
operator|.
name|WSS4JUtils
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
name|WSEncryptionPart
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
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|message
operator|.
name|WSSecEncrypt
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
name|exceptions
operator|.
name|XMLSecurityException
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

begin_class
specifier|public
specifier|final
class|class
name|TokenProviderUtils
block|{
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
name|TokenProviderUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|TokenProviderUtils
parameter_list|()
block|{
comment|// complete
block|}
comment|/**      * Extract an address from a Participants EPR DOM element      */
specifier|public
specifier|static
name|String
name|extractAddressFromParticipantsEPR
parameter_list|(
name|Object
name|participants
parameter_list|)
block|{
if|if
condition|(
name|participants
operator|instanceof
name|Element
condition|)
block|{
name|String
name|localName
init|=
operator|(
operator|(
name|Element
operator|)
name|participants
operator|)
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
name|String
name|namespace
init|=
operator|(
operator|(
name|Element
operator|)
name|participants
operator|)
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|STSConstants
operator|.
name|WSA_NS_05
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|&&
literal|"EndpointReference"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found EndpointReference element"
argument_list|)
expr_stmt|;
name|Element
name|address
init|=
name|DOMUtils
operator|.
name|getFirstChildWithName
argument_list|(
operator|(
name|Element
operator|)
name|participants
argument_list|,
name|STSConstants
operator|.
name|WSA_NS_05
argument_list|,
literal|"Address"
argument_list|)
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found address element"
argument_list|)
expr_stmt|;
return|return
name|address
operator|.
name|getTextContent
argument_list|()
return|;
block|}
block|}
elseif|else
if|if
condition|(
operator|(
name|STSConstants
operator|.
name|WSP_NS
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|||
name|STSConstants
operator|.
name|WSP_NS_04
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|)
operator|&&
literal|"URI"
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
return|return
operator|(
operator|(
name|Element
operator|)
name|participants
operator|)
operator|.
name|getTextContent
argument_list|()
return|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Participants element does not exist or could not be parsed"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|participants
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
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
name|participants
decl_stmt|;
name|QName
name|participantsName
init|=
name|jaxbElement
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|STSConstants
operator|.
name|WSA_NS_05
operator|.
name|equals
argument_list|(
name|participantsName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
operator|&&
literal|"EndpointReference"
operator|.
name|equals
argument_list|(
name|participantsName
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found EndpointReference element"
argument_list|)
expr_stmt|;
name|EndpointReferenceType
name|endpointReference
init|=
operator|(
name|EndpointReferenceType
operator|)
name|jaxbElement
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpointReference
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Found address element"
argument_list|)
expr_stmt|;
return|return
name|endpointReference
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Participants element does not exist or could not be parsed"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
end_class

begin_comment
comment|/**      * Encrypt a Token element using the given arguments.      */
end_comment

begin_function
specifier|public
specifier|static
name|Element
name|encryptToken
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|id
parameter_list|,
name|STSPropertiesMBean
name|stsProperties
parameter_list|,
name|EncryptionProperties
name|encryptionProperties
parameter_list|,
name|KeyRequirements
name|keyRequirements
parameter_list|,
name|WebServiceContext
name|context
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|String
name|name
init|=
name|encryptionProperties
operator|.
name|getEncryptionName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|stsProperties
operator|.
name|getEncryptionUsername
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No encryption alias is configured"
argument_list|)
expr_stmt|;
return|return
name|element
return|;
block|}
comment|// Get the encryption algorithm to use
name|String
name|encryptionAlgorithm
init|=
name|keyRequirements
operator|.
name|getEncryptionAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|encryptionAlgorithm
operator|==
literal|null
condition|)
block|{
comment|// If none then default to what is configured
name|encryptionAlgorithm
operator|=
name|encryptionProperties
operator|.
name|getEncryptionAlgorithm
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|supportedAlgorithms
init|=
name|encryptionProperties
operator|.
name|getAcceptedEncryptionAlgorithms
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|supportedAlgorithms
operator|.
name|contains
argument_list|(
name|encryptionAlgorithm
argument_list|)
condition|)
block|{
name|encryptionAlgorithm
operator|=
name|encryptionProperties
operator|.
name|getEncryptionAlgorithm
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"EncryptionAlgorithm not supported, defaulting to: "
operator|+
name|encryptionAlgorithm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Get the key-wrap algorithm to use
name|String
name|keyWrapAlgorithm
init|=
name|keyRequirements
operator|.
name|getKeywrapAlgorithm
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyWrapAlgorithm
operator|==
literal|null
condition|)
block|{
comment|// If none then default to what is configured
name|keyWrapAlgorithm
operator|=
name|encryptionProperties
operator|.
name|getKeyWrapAlgorithm
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|supportedAlgorithms
init|=
name|encryptionProperties
operator|.
name|getAcceptedKeyWrapAlgorithms
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|supportedAlgorithms
operator|.
name|contains
argument_list|(
name|keyWrapAlgorithm
argument_list|)
condition|)
block|{
name|keyWrapAlgorithm
operator|=
name|encryptionProperties
operator|.
name|getKeyWrapAlgorithm
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"KeyWrapAlgorithm not supported, defaulting to: "
operator|+
name|keyWrapAlgorithm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|WSSecEncrypt
name|builder
init|=
operator|new
name|WSSecEncrypt
argument_list|()
decl_stmt|;
if|if
condition|(
name|WSHandlerConstants
operator|.
name|USE_REQ_SIG_CERT
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|X509Certificate
name|cert
init|=
name|getReqSigCert
argument_list|(
name|context
operator|.
name|getMessageContext
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setUseThisCert
argument_list|(
name|cert
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|setUserInfo
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|setKeyIdentifierType
argument_list|(
name|encryptionProperties
operator|.
name|getKeyIdentifierType
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSymmetricEncAlgorithm
argument_list|(
name|encryptionAlgorithm
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setKeyEncAlgo
argument_list|(
name|keyWrapAlgorithm
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setEmbedEncryptedKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|WSEncryptionPart
name|encryptionPart
init|=
operator|new
name|WSEncryptionPart
argument_list|(
name|id
argument_list|,
literal|"Element"
argument_list|)
decl_stmt|;
name|encryptionPart
operator|.
name|setElement
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|element
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|doc
operator|.
name|appendChild
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|builder
operator|.
name|prepare
argument_list|(
name|element
operator|.
name|getOwnerDocument
argument_list|()
argument_list|,
name|stsProperties
operator|.
name|getEncryptionCrypto
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|encryptForRef
argument_list|(
literal|null
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|encryptionPart
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|doc
operator|.
name|getDocumentElement
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**      * Get the X509Certificate associated with the signature that was received. This cert is to be used      * for encrypting the issued token.      */
end_comment

begin_function
specifier|public
specifier|static
name|X509Certificate
name|getReqSigCert
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|results
init|=
operator|(
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
operator|)
name|context
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
decl_stmt|;
comment|// DOM
name|X509Certificate
name|cert
init|=
name|WSS4JUtils
operator|.
name|getReqSigCert
argument_list|(
name|results
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|!=
literal|null
condition|)
block|{
return|return
name|cert
return|;
block|}
comment|// Streaming
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
name|incomingEventList
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|context
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
if|if
condition|(
name|incomingEventList
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|SecurityEvent
name|incomingEvent
range|:
name|incomingEventList
control|)
block|{
if|if
condition|(
name|WSSecurityEventConstants
operator|.
name|SignedPart
operator|==
name|incomingEvent
operator|.
name|getSecurityEventType
argument_list|()
operator|||
name|WSSecurityEventConstants
operator|.
name|SignedElement
operator|==
name|incomingEvent
operator|.
name|getSecurityEventType
argument_list|()
condition|)
block|{
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
name|securityToken
operator|.
name|SecurityToken
name|token
init|=
operator|(
operator|(
name|AbstractSecuredElementSecurityEvent
operator|)
name|incomingEvent
operator|)
operator|.
name|getSecurityToken
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
name|token
operator|.
name|getX509Certificates
argument_list|()
operator|!=
literal|null
operator|&&
name|token
operator|.
name|getX509Certificates
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|token
operator|.
name|getX509Certificates
argument_list|()
index|[
literal|0
index|]
return|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
end_function

unit|}
end_unit
