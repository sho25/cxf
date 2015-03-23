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
name|common
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|CryptoType
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
name|Merlin
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
name|WSPasswordCallback
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
specifier|final
class|class
name|SecurityUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|X509_CERT
init|=
literal|"X509Certificate"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|X509_ISSUER_SERIAL
init|=
literal|"X509IssuerSerial"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USE_REQUEST_SIGNATURE_CERT
init|=
literal|"useReqSigCert"
decl_stmt|;
specifier|private
name|SecurityUtils
parameter_list|()
block|{              }
specifier|public
specifier|static
name|boolean
name|isSignedAndEncryptedTwoWay
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|Message
name|outMessage
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
decl_stmt|;
name|Message
name|requestMessage
init|=
name|outMessage
operator|!=
literal|null
operator|&&
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|outMessage
argument_list|)
condition|?
name|outMessage
else|:
name|m
decl_stmt|;
return|return
literal|"POST"
operator|.
name|equals
argument_list|(
operator|(
name|String
operator|)
name|requestMessage
operator|.
name|get
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
argument_list|)
operator|&&
name|m
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
specifier|static
name|X509Certificate
name|loadX509Certificate
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|Element
name|certNode
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|base64Value
init|=
name|certNode
operator|.
name|getTextContent
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|byte
index|[]
name|certBytes
init|=
name|Base64Utility
operator|.
name|decode
argument_list|(
name|base64Value
argument_list|)
decl_stmt|;
name|Crypto
name|certCrypto
init|=
name|crypto
decl_stmt|;
if|if
condition|(
name|certCrypto
operator|==
literal|null
condition|)
block|{
name|certCrypto
operator|=
operator|new
name|Merlin
argument_list|()
expr_stmt|;
block|}
return|return
name|certCrypto
operator|.
name|loadCertificate
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|certBytes
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|X509Certificate
name|loadX509IssuerSerial
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|Element
name|certNode
parameter_list|)
throws|throws
name|Exception
block|{
name|Node
name|issuerNameNode
init|=
name|certNode
operator|.
name|getElementsByTagNameNS
argument_list|(
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"X509IssuerName"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Node
name|serialNumberNode
init|=
name|certNode
operator|.
name|getElementsByTagNameNS
argument_list|(
name|Constants
operator|.
name|SignatureSpecNS
argument_list|,
literal|"X509SerialNumber"
argument_list|)
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|CryptoType
name|cryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ISSUER_SERIAL
argument_list|)
decl_stmt|;
name|cryptoType
operator|.
name|setIssuerSerial
argument_list|(
name|issuerNameNode
operator|.
name|getTextContent
argument_list|()
argument_list|,
operator|new
name|BigInteger
argument_list|(
name|serialNumberNode
operator|.
name|getTextContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|crypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
index|[
literal|0
index|]
return|;
block|}
specifier|public
specifier|static
name|X509Certificate
index|[]
name|getCertificates
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
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Crypto instance is null"
argument_list|)
throw|;
block|}
name|CryptoType
name|cryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
argument_list|)
decl_stmt|;
name|cryptoType
operator|.
name|setAlias
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|X509Certificate
index|[]
name|issuerCerts
init|=
name|crypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
decl_stmt|;
if|if
condition|(
name|issuerCerts
operator|==
literal|null
operator|||
name|issuerCerts
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No issuer certs were found using issuer name: "
operator|+
name|user
argument_list|)
throw|;
block|}
return|return
name|issuerCerts
return|;
block|}
specifier|public
specifier|static
name|Crypto
name|getCrypto
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|cryptoKey
parameter_list|,
name|String
name|propKey
parameter_list|)
throws|throws
name|IOException
throws|,
name|WSSecurityException
block|{
return|return
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
return|;
block|}
specifier|public
specifier|static
name|String
name|getUserName
parameter_list|(
name|Message
name|message
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|userNameKey
parameter_list|)
block|{
name|String
name|user
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|userNameKey
argument_list|)
decl_stmt|;
return|return
name|getUserName
argument_list|(
name|crypto
argument_list|,
name|user
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getUserName
parameter_list|(
name|Crypto
name|crypto
parameter_list|,
name|String
name|userName
parameter_list|)
block|{
if|if
condition|(
name|crypto
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|userName
argument_list|)
condition|)
block|{
try|try
block|{
name|userName
operator|=
name|crypto
operator|.
name|getDefaultX509Identifier
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e1
argument_list|)
throw|;
block|}
block|}
return|return
name|userName
return|;
block|}
specifier|public
specifier|static
name|String
name|getPassword
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|userName
parameter_list|,
name|int
name|type
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|CallbackHandler
name|handler
init|=
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|callingClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|userName
argument_list|,
name|type
argument_list|)
block|}
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|//get the password
name|String
name|password
init|=
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
decl_stmt|;
return|return
name|password
operator|==
literal|null
condition|?
literal|""
else|:
name|password
return|;
block|}
specifier|public
specifier|static
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|)
throws|throws
name|WSSecurityException
block|{
return|return
name|getCallbackHandler
argument_list|(
name|message
argument_list|,
name|callingClass
argument_list|,
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Message
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|,
name|String
name|callbackProperty
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|//Then try to get the password from the given callback handler
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|callbackProperty
argument_list|)
decl_stmt|;
return|return
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
name|utils
operator|.
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
end_class

end_unit

