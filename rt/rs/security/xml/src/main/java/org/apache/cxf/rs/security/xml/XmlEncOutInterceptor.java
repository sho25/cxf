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
name|security
operator|.
name|NoSuchAlgorithmException
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
name|CertificateEncodingException
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
name|crypto
operator|.
name|BadPaddingException
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
name|IllegalBlockSizeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|KeyGenerator
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
name|Node
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
name|w3c
operator|.
name|dom
operator|.
name|Text
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
name|SecurityUtils
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|components
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
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|token
operator|.
name|DOMX509Data
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|message
operator|.
name|token
operator|.
name|DOMX509IssuerSerial
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|UUIDGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|WSSecurityUtil
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
name|algorithms
operator|.
name|JCEMapper
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
name|utils
operator|.
name|EncryptionConstants
import|;
end_import

begin_class
specifier|public
class|class
name|XmlEncOutInterceptor
extends|extends
name|AbstractXmlSecOutInterceptor
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
name|XmlEncOutInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_RETRIEVAL_METHOD_TYPE
init|=
literal|"http://www.w3.org/2001/04/xmlenc#EncryptedKey"
decl_stmt|;
specifier|private
name|boolean
name|encryptSymmetricKey
init|=
literal|true
decl_stmt|;
specifier|private
name|SecretKey
name|symmetricKey
decl_stmt|;
specifier|private
name|String
name|keyEncAlgo
init|=
name|XMLCipher
operator|.
name|RSA_OAEP
decl_stmt|;
specifier|private
name|String
name|symEncAlgo
init|=
name|XMLCipher
operator|.
name|AES_256
decl_stmt|;
specifier|private
name|String
name|keyIdentifierType
init|=
name|SecurityUtils
operator|.
name|X509_KEY
decl_stmt|;
specifier|public
name|XmlEncOutInterceptor
parameter_list|()
block|{
name|addAfter
argument_list|(
name|XmlSigOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setKeyIdentifierType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|keyIdentifierType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|void
name|setSymmetricEncAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
if|if
condition|(
operator|!
name|algo
operator|.
name|startsWith
argument_list|(
name|EncryptionConstants
operator|.
name|EncryptionSpecNS
argument_list|)
condition|)
block|{
name|algo
operator|=
name|EncryptionConstants
operator|.
name|EncryptionSpecNS
operator|+
name|algo
expr_stmt|;
block|}
name|symEncAlgo
operator|=
name|algo
expr_stmt|;
block|}
specifier|public
name|void
name|setKeyEncAlgorithm
parameter_list|(
name|String
name|algo
parameter_list|)
block|{
name|keyEncAlgo
operator|=
name|algo
expr_stmt|;
block|}
specifier|protected
name|Document
name|processDocument
parameter_list|(
name|Message
name|message
parameter_list|,
name|Document
name|payloadDoc
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|encryptDocument
argument_list|(
name|message
argument_list|,
name|payloadDoc
argument_list|)
return|;
block|}
specifier|protected
name|Document
name|encryptDocument
parameter_list|(
name|Message
name|message
parameter_list|,
name|Document
name|payloadDoc
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|secretKey
init|=
name|getSymmetricKey
argument_list|()
decl_stmt|;
name|Document
name|encryptedDataDoc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|encryptedDataElement
init|=
name|createEncryptedDataElement
argument_list|(
name|encryptedDataDoc
argument_list|)
decl_stmt|;
if|if
condition|(
name|encryptSymmetricKey
condition|)
block|{
name|CryptoLoader
name|loader
init|=
operator|new
name|CryptoLoader
argument_list|()
decl_stmt|;
name|Crypto
name|crypto
init|=
name|loader
operator|.
name|getCrypto
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|,
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|SecurityUtils
operator|.
name|getUserName
argument_list|(
name|message
argument_list|,
name|crypto
argument_list|,
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|user
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|X509Certificate
name|cert
init|=
name|getReceiverCertificate
argument_list|(
name|crypto
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|byte
index|[]
name|encryptedSecretKey
init|=
name|encryptSymmetricKey
argument_list|(
name|secretKey
argument_list|,
name|cert
argument_list|,
name|crypto
argument_list|)
decl_stmt|;
name|addEncryptedKeyElement
argument_list|(
name|encryptedDataElement
argument_list|,
name|cert
argument_list|,
name|encryptedSecretKey
argument_list|)
expr_stmt|;
block|}
comment|// encrypt payloadDoc
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
name|ENCRYPT_MODE
argument_list|,
name|symmetricKey
argument_list|)
decl_stmt|;
name|Document
name|result
init|=
name|xmlCipher
operator|.
name|doFinal
argument_list|(
name|payloadDoc
argument_list|,
name|payloadDoc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|NodeList
name|list
init|=
name|result
operator|.
name|getElementsByTagNameNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
literal|"CipherValue"
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|getLength
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
literal|"Payload CipherData is missing"
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|String
name|cipherText
init|=
operator|(
operator|(
name|Element
operator|)
name|list
operator|.
name|item
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|Element
name|cipherValue
init|=
name|createCipherValue
argument_list|(
name|encryptedDataDoc
argument_list|,
name|encryptedDataDoc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|cipherValue
operator|.
name|appendChild
argument_list|(
name|encryptedDataDoc
operator|.
name|createTextNode
argument_list|(
name|cipherText
argument_list|)
argument_list|)
expr_stmt|;
comment|//StaxUtils.copy(new DOMSource(encryptedDataDoc), System.out);
return|return
name|encryptedDataDoc
return|;
block|}
specifier|private
name|byte
index|[]
name|getSymmetricKey
parameter_list|()
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|symmetricKey
operator|==
literal|null
condition|)
block|{
name|KeyGenerator
name|keyGen
init|=
name|getKeyGenerator
argument_list|()
decl_stmt|;
name|symmetricKey
operator|=
name|keyGen
operator|.
name|generateKey
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|symmetricKey
operator|.
name|getEncoded
argument_list|()
return|;
block|}
specifier|private
name|X509Certificate
name|getReceiverCertificate
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|String
name|user
parameter_list|)
throws|throws
name|Exception
block|{
name|X509Certificate
index|[]
name|certs
init|=
name|SecurityUtils
operator|.
name|getCertificates
argument_list|(
name|crypto
argument_list|,
name|user
argument_list|)
decl_stmt|;
return|return
name|certs
index|[
literal|0
index|]
return|;
block|}
specifier|private
name|KeyGenerator
name|getKeyGenerator
parameter_list|()
throws|throws
name|WSSecurityException
block|{
try|try
block|{
comment|//
comment|// Assume AES as default, so initialize it
comment|//
name|String
name|keyAlgorithm
init|=
name|JCEMapper
operator|.
name|getJCEKeyAlgorithmFromURI
argument_list|(
name|symEncAlgo
argument_list|)
decl_stmt|;
name|KeyGenerator
name|keyGen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
name|keyAlgorithm
argument_list|)
decl_stmt|;
if|if
condition|(
name|symEncAlgo
operator|.
name|equalsIgnoreCase
argument_list|(
name|WSConstants
operator|.
name|AES_128
argument_list|)
condition|)
block|{
name|keyGen
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|symEncAlgo
operator|.
name|equalsIgnoreCase
argument_list|(
name|WSConstants
operator|.
name|AES_192
argument_list|)
condition|)
block|{
name|keyGen
operator|.
name|init
argument_list|(
literal|192
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|symEncAlgo
operator|.
name|equalsIgnoreCase
argument_list|(
name|WSConstants
operator|.
name|AES_256
argument_list|)
condition|)
block|{
name|keyGen
operator|.
name|init
argument_list|(
literal|256
argument_list|)
expr_stmt|;
block|}
return|return
name|keyGen
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|UNSUPPORTED_ALGORITHM
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Apache Security XMLCipher does not support
comment|// Certificates for encrypting the keys
specifier|protected
name|byte
index|[]
name|encryptSymmetricKey
parameter_list|(
name|byte
index|[]
name|keyBytes
parameter_list|,
name|X509Certificate
name|remoteCert
parameter_list|,
name|Crypto
name|crypto
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Cipher
name|cipher
init|=
name|EncryptionUtils
operator|.
name|initCipherWithCert
argument_list|(
name|keyEncAlgo
argument_list|,
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|remoteCert
argument_list|)
decl_stmt|;
name|int
name|blockSize
init|=
name|cipher
operator|.
name|getBlockSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|blockSize
operator|>
literal|0
operator|&&
name|blockSize
operator|<
name|keyBytes
operator|.
name|length
condition|)
block|{
name|String
name|message
init|=
literal|"Public key algorithm too weak to encrypt symmetric key"
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILURE
argument_list|,
literal|"unsupportedKeyTransp"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|message
block|}
argument_list|)
throw|;
block|}
name|byte
index|[]
name|encryptedEphemeralKey
init|=
literal|null
decl_stmt|;
try|try
block|{
name|encryptedEphemeralKey
operator|=
name|cipher
operator|.
name|doFinal
argument_list|(
name|keyBytes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalBlockSizeException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|BadPaddingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|FAILED_ENCRYPTION
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
throw|;
block|}
return|return
name|encryptedEphemeralKey
return|;
block|}
specifier|private
name|void
name|addEncryptedKeyElement
parameter_list|(
name|Element
name|encryptedDataElement
parameter_list|,
name|X509Certificate
name|cert
parameter_list|,
name|byte
index|[]
name|encryptedKey
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|doc
init|=
name|encryptedDataElement
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|String
name|encodedKey
init|=
name|Base64Utility
operator|.
name|encode
argument_list|(
name|encryptedKey
argument_list|)
decl_stmt|;
name|Element
name|encryptedKeyElement
init|=
name|createEncryptedKeyElement
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|String
name|encKeyId
init|=
literal|"EK-"
operator|+
name|UUIDGenerator
operator|.
name|getUUID
argument_list|()
decl_stmt|;
name|encryptedKeyElement
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Id"
argument_list|,
name|encKeyId
argument_list|)
expr_stmt|;
name|Element
name|keyInfoElement
init|=
name|createKeyInfoElement
argument_list|(
name|doc
argument_list|,
name|cert
argument_list|)
decl_stmt|;
name|encryptedKeyElement
operator|.
name|appendChild
argument_list|(
name|keyInfoElement
argument_list|)
expr_stmt|;
name|Element
name|xencCipherValue
init|=
name|createCipherValue
argument_list|(
name|doc
argument_list|,
name|encryptedKeyElement
argument_list|)
decl_stmt|;
name|xencCipherValue
operator|.
name|appendChild
argument_list|(
name|doc
operator|.
name|createTextNode
argument_list|(
name|encodedKey
argument_list|)
argument_list|)
expr_stmt|;
name|Element
name|topKeyInfoElement
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":"
operator|+
name|WSConstants
operator|.
name|KEYINFO_LN
argument_list|)
decl_stmt|;
name|Element
name|retrievalMethodElement
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":RetrievalMethod"
argument_list|)
decl_stmt|;
name|retrievalMethodElement
operator|.
name|setAttribute
argument_list|(
literal|"Type"
argument_list|,
name|DEFAULT_RETRIEVAL_METHOD_TYPE
argument_list|)
expr_stmt|;
name|topKeyInfoElement
operator|.
name|appendChild
argument_list|(
name|retrievalMethodElement
argument_list|)
expr_stmt|;
name|topKeyInfoElement
operator|.
name|appendChild
argument_list|(
name|encryptedKeyElement
argument_list|)
expr_stmt|;
name|encryptedDataElement
operator|.
name|appendChild
argument_list|(
name|topKeyInfoElement
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Element
name|createCipherValue
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Element
name|encryptedKey
parameter_list|)
block|{
name|Element
name|cipherData
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
operator|+
literal|":CipherData"
argument_list|)
decl_stmt|;
name|Element
name|cipherValue
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
operator|+
literal|":CipherValue"
argument_list|)
decl_stmt|;
name|cipherData
operator|.
name|appendChild
argument_list|(
name|cipherValue
argument_list|)
expr_stmt|;
name|encryptedKey
operator|.
name|appendChild
argument_list|(
name|cipherData
argument_list|)
expr_stmt|;
return|return
name|cipherValue
return|;
block|}
specifier|private
name|Element
name|createKeyInfoElement
parameter_list|(
name|Document
name|encryptedDataDoc
parameter_list|,
name|X509Certificate
name|remoteCert
parameter_list|)
throws|throws
name|Exception
block|{
name|Element
name|keyInfoElement
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":"
operator|+
name|WSConstants
operator|.
name|KEYINFO_LN
argument_list|)
decl_stmt|;
name|Node
name|keyIdentifierNode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keyIdentifierType
operator|.
name|equals
argument_list|(
name|SecurityUtils
operator|.
name|X509_KEY
argument_list|)
condition|)
block|{
name|byte
name|data
index|[]
init|=
literal|null
decl_stmt|;
try|try
block|{
name|data
operator|=
name|remoteCert
operator|.
name|getEncoded
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CertificateEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|SECURITY_TOKEN_UNAVAILABLE
argument_list|,
literal|"encodeError"
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|Text
name|text
init|=
name|encryptedDataDoc
operator|.
name|createTextNode
argument_list|(
name|Base64
operator|.
name|encode
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
name|Element
name|cert
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":"
operator|+
name|WSConstants
operator|.
name|X509_CERT_LN
argument_list|)
decl_stmt|;
name|cert
operator|.
name|appendChild
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|Element
name|x509Data
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|SIG_NS
argument_list|,
name|WSConstants
operator|.
name|SIG_PREFIX
operator|+
literal|":"
operator|+
name|WSConstants
operator|.
name|X509_DATA_LN
argument_list|)
decl_stmt|;
name|x509Data
operator|.
name|appendChild
argument_list|(
name|cert
argument_list|)
expr_stmt|;
name|keyIdentifierNode
operator|=
name|x509Data
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyIdentifierType
operator|.
name|equals
argument_list|(
name|SecurityUtils
operator|.
name|X509_ISSUER_SERIAL
argument_list|)
condition|)
block|{
name|String
name|issuer
init|=
name|remoteCert
operator|.
name|getIssuerDN
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|java
operator|.
name|math
operator|.
name|BigInteger
name|serialNumber
init|=
name|remoteCert
operator|.
name|getSerialNumber
argument_list|()
decl_stmt|;
name|DOMX509IssuerSerial
name|domIssuerSerial
init|=
operator|new
name|DOMX509IssuerSerial
argument_list|(
name|encryptedDataDoc
argument_list|,
name|issuer
argument_list|,
name|serialNumber
argument_list|)
decl_stmt|;
name|DOMX509Data
name|domX509Data
init|=
operator|new
name|DOMX509Data
argument_list|(
name|encryptedDataDoc
argument_list|,
name|domIssuerSerial
argument_list|)
decl_stmt|;
name|keyIdentifierNode
operator|=
name|domX509Data
operator|.
name|getElement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
literal|"Unsupported key identifier:"
operator|+
name|keyIdentifierType
argument_list|)
throw|;
block|}
name|keyInfoElement
operator|.
name|appendChild
argument_list|(
name|keyIdentifierNode
argument_list|)
expr_stmt|;
return|return
name|keyInfoElement
return|;
block|}
specifier|protected
name|Element
name|createEncryptedKeyElement
parameter_list|(
name|Document
name|encryptedDataDoc
parameter_list|)
block|{
name|Element
name|encryptedKey
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
operator|+
literal|":EncryptedKey"
argument_list|)
decl_stmt|;
name|Element
name|encryptionMethod
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
operator|+
literal|":EncryptionMethod"
argument_list|)
decl_stmt|;
name|encryptionMethod
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Algorithm"
argument_list|,
name|keyEncAlgo
argument_list|)
expr_stmt|;
name|encryptedKey
operator|.
name|appendChild
argument_list|(
name|encryptionMethod
argument_list|)
expr_stmt|;
return|return
name|encryptedKey
return|;
block|}
specifier|protected
name|Element
name|createEncryptedDataElement
parameter_list|(
name|Document
name|encryptedDataDoc
parameter_list|)
block|{
name|Element
name|encryptedData
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
operator|+
literal|":EncryptedData"
argument_list|)
decl_stmt|;
name|WSSecurityUtil
operator|.
name|setNamespace
argument_list|(
name|encryptedData
argument_list|,
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
argument_list|)
expr_stmt|;
name|Element
name|encryptionMethod
init|=
name|encryptedDataDoc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|ENC_NS
argument_list|,
name|WSConstants
operator|.
name|ENC_PREFIX
operator|+
literal|":EncryptionMethod"
argument_list|)
decl_stmt|;
name|encryptionMethod
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Algorithm"
argument_list|,
name|symEncAlgo
argument_list|)
expr_stmt|;
name|encryptedData
operator|.
name|appendChild
argument_list|(
name|encryptionMethod
argument_list|)
expr_stmt|;
name|encryptedDataDoc
operator|.
name|appendChild
argument_list|(
name|encryptedData
argument_list|)
expr_stmt|;
return|return
name|encryptedData
return|;
block|}
block|}
end_class

end_unit

