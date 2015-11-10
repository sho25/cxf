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
name|rs
operator|.
name|security
operator|.
name|xml
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

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
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|util
operator|.
name|Base64Exception
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
name|Base64Utility
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|CryptoLoader
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|RSSecurityUtils
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
name|rs
operator|.
name|security
operator|.
name|common
operator|.
name|TrustValidator
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
name|rt
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
name|W3CDOMStreamReader
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
name|common
operator|.
name|util
operator|.
name|KeyUtils
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
name|encryption
operator|.
name|XMLCipher
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
name|encryption
operator|.
name|XMLEncryptionException
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
name|utils
operator|.
name|Constants
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXmlEncInHandler
extends|extends
name|AbstractXmlSecInHandler
block|{
specifier|private
name|EncryptionProperties
name|encProps
decl_stmt|;
specifier|public
name|void
name|decryptContent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Message
name|outMs
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|Message
name|inMsg
init|=
name|outMs
operator|==
literal|null
condition|?
name|message
else|:
name|outMs
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|getDocument
argument_list|(
name|inMsg
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Element
name|root
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|byte
index|[]
name|symmetricKeyBytes
init|=
name|getSymmetricKeyBytes
argument_list|(
name|message
argument_list|,
name|root
argument_list|)
decl_stmt|;
name|String
name|symKeyAlgo
init|=
name|getEncodingMethodAlgorithm
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|encProps
operator|!=
literal|null
operator|&&
name|encProps
operator|.
name|getEncryptionSymmetricKeyAlgo
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|encProps
operator|.
name|getEncryptionSymmetricKeyAlgo
argument_list|()
operator|.
name|equals
argument_list|(
name|symKeyAlgo
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Encryption Symmetric Key Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|byte
index|[]
name|decryptedPayload
init|=
literal|null
decl_stmt|;
try|try
block|{
name|decryptedPayload
operator|=
name|decryptPayload
argument_list|(
name|root
argument_list|,
name|symmetricKeyBytes
argument_list|,
name|symKeyAlgo
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Payload can not be decrypted"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|Document
name|payloadDoc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|payloadDoc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|decryptedPayload
argument_list|)
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Payload document can not be created"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
operator|new
name|W3CDOMStreamReader
argument_list|(
name|payloadDoc
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// Subclasses can overwrite it and return the bytes, assuming they know the actual key
specifier|protected
name|byte
index|[]
name|getSymmetricKeyBytes
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|encDataElement
parameter_list|)
block|{
name|String
name|cryptoKey
init|=
literal|null
decl_stmt|;
name|String
name|propKey
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|RSSecurityUtils
operator|.
name|isSignedAndEncryptedTwoWay
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|cryptoKey
operator|=
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
expr_stmt|;
name|propKey
operator|=
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
expr_stmt|;
block|}
else|else
block|{
name|cryptoKey
operator|=
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
expr_stmt|;
name|propKey
operator|=
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
expr_stmt|;
block|}
name|Crypto
name|crypto
init|=
literal|null
decl_stmt|;
try|try
block|{
name|crypto
operator|=
operator|new
name|CryptoLoader
argument_list|()
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|cryptoKey
argument_list|,
name|propKey
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Crypto can not be loaded"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|Element
name|encKeyElement
init|=
name|getNode
argument_list|(
name|encDataElement
argument_list|,
name|ENC_NS
argument_list|,
literal|"EncryptedKey"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|encKeyElement
operator|==
literal|null
condition|)
block|{
comment|//TODO: support EncryptedData/ds:KeyInfo - the encrypted key is passed out of band
name|throwFault
argument_list|(
literal|"EncryptedKey element is not available"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|X509Certificate
name|cert
init|=
name|loadCertificate
argument_list|(
name|crypto
argument_list|,
name|encKeyElement
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|TrustValidator
argument_list|()
operator|.
name|validateTrust
argument_list|(
name|crypto
argument_list|,
name|cert
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
comment|// now start decrypting
name|String
name|keyEncAlgo
init|=
name|getEncodingMethodAlgorithm
argument_list|(
name|encKeyElement
argument_list|)
decl_stmt|;
name|String
name|digestAlgo
init|=
name|getDigestMethodAlgorithm
argument_list|(
name|encKeyElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|encProps
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|encProps
operator|.
name|getEncryptionKeyTransportAlgo
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|encProps
operator|.
name|getEncryptionKeyTransportAlgo
argument_list|()
operator|.
name|equals
argument_list|(
name|keyEncAlgo
argument_list|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Key Transport Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encProps
operator|.
name|getEncryptionDigestAlgo
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|digestAlgo
operator|==
literal|null
operator|||
operator|!
name|encProps
operator|.
name|getEncryptionDigestAlgo
argument_list|()
operator|.
name|equals
argument_list|(
name|digestAlgo
argument_list|)
operator|)
condition|)
block|{
name|throwFault
argument_list|(
literal|"Digest Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|XMLCipher
operator|.
name|RSA_OAEP
operator|.
name|equals
argument_list|(
name|keyEncAlgo
argument_list|)
condition|)
block|{
comment|// RSA OAEP is the required default Key Transport Algorithm
name|throwFault
argument_list|(
literal|"Key Transport Algorithm is not supported"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|Element
name|cipherValue
init|=
name|getNode
argument_list|(
name|encKeyElement
argument_list|,
name|ENC_NS
argument_list|,
literal|"CipherValue"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|cipherValue
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"CipherValue element is not available"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|decryptSymmetricKey
argument_list|(
name|cipherValue
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
name|cert
argument_list|,
name|crypto
argument_list|,
name|keyEncAlgo
argument_list|,
name|digestAlgo
argument_list|,
name|message
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|X509Certificate
name|loadCertificate
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|Element
name|encKeyElement
parameter_list|)
block|{
comment|/**          * TODO: the following can be easily supported too<X509SKI>31d97bd7</X509SKI><X509SubjectName>Subject of Certificate B</X509SubjectName>          *           */
name|String
name|keyIdentifierType
init|=
name|encProps
operator|!=
literal|null
condition|?
name|encProps
operator|.
name|getEncryptionKeyIdType
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|keyIdentifierType
operator|==
literal|null
operator|||
name|keyIdentifierType
operator|.
name|equals
argument_list|(
name|RSSecurityUtils
operator|.
name|X509_CERT
argument_list|)
condition|)
block|{
name|Element
name|certNode
init|=
name|getNode
argument_list|(
name|encKeyElement
argument_list|,
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"X509Certificate"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|certNode
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|RSSecurityUtils
operator|.
name|loadX509Certificate
argument_list|(
name|crypto
argument_list|,
name|certNode
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"X509Certificate can not be created"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|keyIdentifierType
operator|==
literal|null
operator|||
name|keyIdentifierType
operator|.
name|equals
argument_list|(
name|RSSecurityUtils
operator|.
name|X509_ISSUER_SERIAL
argument_list|)
condition|)
block|{
name|Element
name|certNode
init|=
name|getNode
argument_list|(
name|encKeyElement
argument_list|,
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"X509IssuerSerial"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|certNode
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|RSSecurityUtils
operator|.
name|loadX509IssuerSerial
argument_list|(
name|crypto
argument_list|,
name|certNode
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"X509Certificate can not be created"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|throwFault
argument_list|(
literal|"Certificate is missing"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|getEncodingMethodAlgorithm
parameter_list|(
name|Element
name|parent
parameter_list|)
block|{
name|Element
name|encMethod
init|=
name|getNode
argument_list|(
name|parent
argument_list|,
name|ENC_NS
argument_list|,
literal|"EncryptionMethod"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|encMethod
operator|==
literal|null
condition|)
block|{
name|throwFault
argument_list|(
literal|"EncryptionMethod element is not available"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|encMethod
operator|.
name|getAttribute
argument_list|(
literal|"Algorithm"
argument_list|)
return|;
block|}
specifier|private
name|String
name|getDigestMethodAlgorithm
parameter_list|(
name|Element
name|parent
parameter_list|)
block|{
name|Element
name|encMethod
init|=
name|getNode
argument_list|(
name|parent
argument_list|,
name|ENC_NS
argument_list|,
literal|"EncryptionMethod"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|encMethod
operator|!=
literal|null
condition|)
block|{
name|Element
name|digestMethod
init|=
name|getNode
argument_list|(
name|encMethod
argument_list|,
name|SIG_NS
argument_list|,
literal|"DigestMethod"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|digestMethod
operator|!=
literal|null
condition|)
block|{
return|return
name|digestMethod
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Algorithm"
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|//TODO: Support symmetric keys if requested
specifier|protected
name|byte
index|[]
name|decryptSymmetricKey
parameter_list|(
name|String
name|base64EncodedKey
parameter_list|,
name|X509Certificate
name|cert
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|keyEncAlgo
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
return|return
name|decryptSymmetricKey
argument_list|(
name|base64EncodedKey
argument_list|,
name|cert
argument_list|,
name|crypto
argument_list|,
name|keyEncAlgo
argument_list|,
literal|null
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|//TODO: Support symmetric keys if requested
specifier|protected
name|byte
index|[]
name|decryptSymmetricKey
parameter_list|(
name|String
name|base64EncodedKey
parameter_list|,
name|X509Certificate
name|cert
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|keyEncAlgo
parameter_list|,
name|String
name|digestAlgo
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|CallbackHandler
name|callback
init|=
name|RSSecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|PrivateKey
name|key
init|=
literal|null
decl_stmt|;
try|try
block|{
name|key
operator|=
name|crypto
operator|.
name|getPrivateKey
argument_list|(
name|cert
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Encrypted key can not be decrypted"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
name|Cipher
name|cipher
init|=
name|EncryptionUtils
operator|.
name|initCipherWithKey
argument_list|(
name|keyEncAlgo
argument_list|,
name|digestAlgo
argument_list|,
name|Cipher
operator|.
name|DECRYPT_MODE
argument_list|,
name|key
argument_list|)
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|encryptedBytes
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|base64EncodedKey
argument_list|)
decl_stmt|;
return|return
name|cipher
operator|.
name|doFinal
argument_list|(
name|encryptedBytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Base64 decoding has failed"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|throwFault
argument_list|(
literal|"Encrypted key can not be decrypted"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|byte
index|[]
name|decryptPayload
parameter_list|(
name|Element
name|root
parameter_list|,
name|byte
index|[]
name|secretKeyBytes
parameter_list|,
name|String
name|symEncAlgo
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SecretKey
name|key
init|=
name|KeyUtils
operator|.
name|prepareSecretKey
argument_list|(
name|symEncAlgo
argument_list|,
name|secretKeyBytes
argument_list|)
decl_stmt|;
try|try
block|{
name|XMLCipher
name|xmlCipher
init|=
name|EncryptionUtils
operator|.
name|initXMLCipher
argument_list|(
name|symEncAlgo
argument_list|,
name|XMLCipher
operator|.
name|DECRYPT_MODE
argument_list|,
name|key
argument_list|)
decl_stmt|;
return|return
name|xmlCipher
operator|.
name|decryptToByteArray
argument_list|(
name|root
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XMLEncryptionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|UNSUPPORTED_ALGORITHM
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setEncryptionProperties
parameter_list|(
name|EncryptionProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|encProps
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

