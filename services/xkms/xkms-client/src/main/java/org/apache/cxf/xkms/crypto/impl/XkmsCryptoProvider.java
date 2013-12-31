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
name|xkms
operator|.
name|crypto
operator|.
name|impl
package|;
end_package

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
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
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
name|xkms
operator|.
name|cache
operator|.
name|EHCacheXKMSClientCache
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
name|xkms
operator|.
name|cache
operator|.
name|XKMSCacheToken
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
name|xkms
operator|.
name|cache
operator|.
name|XKMSClientCache
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
name|xkms
operator|.
name|crypto
operator|.
name|CryptoProviderException
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
name|xkms
operator|.
name|handlers
operator|.
name|Applications
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
name|CryptoBase
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
name|CryptoType
operator|.
name|TYPE
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
name|w3
operator|.
name|_2002
operator|.
name|_03
operator|.
name|xkms_wsdl
operator|.
name|XKMSPortType
import|;
end_import

begin_class
specifier|public
class|class
name|XkmsCryptoProvider
extends|extends
name|CryptoBase
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
name|XkmsCryptoProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|XKMSInvoker
name|xkmsInvoker
decl_stmt|;
specifier|private
name|Crypto
name|fallbackCrypto
decl_stmt|;
specifier|private
name|XKMSClientCache
name|xkmsClientCache
decl_stmt|;
specifier|private
name|boolean
name|allowX509FromJKS
init|=
literal|true
decl_stmt|;
specifier|public
name|XkmsCryptoProvider
parameter_list|(
name|XKMSPortType
name|xkmsConsumer
parameter_list|)
block|{
name|this
argument_list|(
name|xkmsConsumer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XkmsCryptoProvider
parameter_list|(
name|XKMSPortType
name|xkmsConsumer
parameter_list|,
name|Crypto
name|fallbackCrypto
parameter_list|)
block|{
name|this
argument_list|(
name|xkmsConsumer
argument_list|,
name|fallbackCrypto
argument_list|,
operator|new
name|EHCacheXKMSClientCache
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XkmsCryptoProvider
parameter_list|(
name|XKMSPortType
name|xkmsConsumer
parameter_list|,
name|Crypto
name|fallbackCrypto
parameter_list|,
name|boolean
name|allowX509FromJKS
parameter_list|)
block|{
name|this
argument_list|(
name|xkmsConsumer
argument_list|,
name|fallbackCrypto
argument_list|,
operator|new
name|EHCacheXKMSClientCache
argument_list|()
argument_list|,
name|allowX509FromJKS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XkmsCryptoProvider
parameter_list|(
name|XKMSPortType
name|xkmsConsumer
parameter_list|,
name|Crypto
name|fallbackCrypto
parameter_list|,
name|XKMSClientCache
name|xkmsClientCache
parameter_list|,
name|boolean
name|allowX509FromJKS
parameter_list|)
block|{
if|if
condition|(
name|xkmsConsumer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"xkmsConsumer may not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|xkmsInvoker
operator|=
operator|new
name|XKMSInvoker
argument_list|(
name|xkmsConsumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|fallbackCrypto
operator|=
name|fallbackCrypto
expr_stmt|;
name|this
operator|.
name|xkmsClientCache
operator|=
name|xkmsClientCache
expr_stmt|;
name|this
operator|.
name|allowX509FromJKS
operator|=
name|allowX509FromJKS
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|X509Certificate
index|[]
name|getX509Certificates
parameter_list|(
name|CryptoType
name|cryptoType
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"XKMS Runtime: getting public certificate for alias: %s; issuer: %s; subjectDN: %s"
argument_list|,
name|cryptoType
operator|.
name|getAlias
argument_list|()
argument_list|,
name|cryptoType
operator|.
name|getIssuer
argument_list|()
argument_list|,
name|cryptoType
operator|.
name|getSubjectDN
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|X509Certificate
index|[]
name|certs
init|=
name|getX509
argument_list|(
name|cryptoType
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot find certificate for alias: %s, issuer: %s; subjectDN: %s"
argument_list|,
name|cryptoType
operator|.
name|getAlias
argument_list|()
argument_list|,
name|cryptoType
operator|.
name|getIssuer
argument_list|()
argument_list|,
name|cryptoType
operator|.
name|getSubjectDN
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|certs
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getX509Identifier
parameter_list|(
name|X509Certificate
name|cert
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|assertDefaultCryptoProvider
argument_list|()
expr_stmt|;
return|return
name|fallbackCrypto
operator|.
name|getX509Identifier
argument_list|(
name|cert
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|assertDefaultCryptoProvider
argument_list|()
expr_stmt|;
return|return
name|fallbackCrypto
operator|.
name|getPrivateKey
argument_list|(
name|certificate
argument_list|,
name|callbackHandler
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|String
name|identifier
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|assertDefaultCryptoProvider
argument_list|()
expr_stmt|;
return|return
name|fallbackCrypto
operator|.
name|getPrivateKey
argument_list|(
name|identifier
argument_list|,
name|password
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|verifyTrust
parameter_list|(
name|X509Certificate
index|[]
name|certs
parameter_list|,
name|boolean
name|enableRevocation
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|certs
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Verifying certificate id: %s"
argument_list|,
name|certs
index|[
literal|0
index|]
operator|.
name|getSubjectDN
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|XKMSCacheToken
name|cachedToken
init|=
literal|null
decl_stmt|;
comment|// Try local cache first
if|if
condition|(
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|length
operator|>
literal|0
operator|&&
name|xkmsClientCache
operator|!=
literal|null
condition|)
block|{
name|String
name|key
init|=
name|certs
index|[
literal|0
index|]
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// Try by Subject DN and IssuerSerial
name|cachedToken
operator|=
name|xkmsClientCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|cachedToken
operator|==
literal|null
condition|)
block|{
name|key
operator|=
name|getKeyForIssuerSerial
argument_list|(
name|certs
index|[
literal|0
index|]
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|certs
index|[
literal|0
index|]
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
expr_stmt|;
name|cachedToken
operator|=
name|xkmsClientCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cachedToken
operator|!=
literal|null
operator|&&
name|cachedToken
operator|.
name|isXkmsValidated
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Certificate has already been validated by the XKMS service"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
if|if
condition|(
name|certs
operator|==
literal|null
operator|||
name|certs
index|[
literal|0
index|]
operator|==
literal|null
operator|||
operator|!
name|xkmsInvoker
operator|.
name|validateCertificate
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"The given certificate is not valid"
argument_list|)
throw|;
block|}
comment|// Validate Cached token
if|if
condition|(
name|cachedToken
operator|!=
literal|null
condition|)
block|{
name|cachedToken
operator|.
name|setXkmsValidated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// Otherwise, Store in the cache as a validated certificate
name|storeCertificateInCache
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|verifyTrust
parameter_list|(
name|PublicKey
name|publicKey
parameter_list|)
throws|throws
name|WSSecurityException
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"PublicKeys cannot be verified"
argument_list|)
throw|;
block|}
specifier|private
name|void
name|assertDefaultCryptoProvider
parameter_list|()
block|{
if|if
condition|(
name|fallbackCrypto
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported by this crypto provider"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|X509Certificate
index|[]
name|getX509
parameter_list|(
name|CryptoType
name|cryptoType
parameter_list|)
block|{
comment|// Try to get X509 certificate from local keystore if it is configured
if|if
condition|(
name|allowX509FromJKS
operator|&&
operator|(
name|fallbackCrypto
operator|!=
literal|null
operator|)
condition|)
block|{
name|X509Certificate
index|[]
name|localCerts
init|=
name|getCertificateLocaly
argument_list|(
name|cryptoType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|localCerts
operator|!=
literal|null
operator|)
operator|&&
name|localCerts
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|localCerts
return|;
block|}
block|}
name|CryptoType
operator|.
name|TYPE
name|type
init|=
name|cryptoType
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|TYPE
operator|.
name|SUBJECT_DN
condition|)
block|{
return|return
name|getX509FromXKMSByID
argument_list|(
name|Applications
operator|.
name|PKIX
argument_list|,
name|cryptoType
operator|.
name|getSubjectDN
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|TYPE
operator|.
name|ENDPOINT
condition|)
block|{
return|return
name|getX509FromXKMSByEndpoint
argument_list|(
name|cryptoType
operator|.
name|getEndpoint
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|TYPE
operator|.
name|ALIAS
condition|)
block|{
name|Applications
name|appId
init|=
literal|null
decl_stmt|;
name|boolean
name|isServiceName
init|=
name|isServiceName
argument_list|(
name|cryptoType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isServiceName
condition|)
block|{
name|appId
operator|=
name|Applications
operator|.
name|PKIX
expr_stmt|;
block|}
else|else
block|{
name|appId
operator|=
name|Applications
operator|.
name|SERVICE_NAME
expr_stmt|;
block|}
return|return
name|getX509FromXKMSByID
argument_list|(
name|appId
argument_list|,
name|cryptoType
operator|.
name|getAlias
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|TYPE
operator|.
name|ISSUER_SERIAL
condition|)
block|{
return|return
name|getX509FromXKMSByIssuerSerial
argument_list|(
name|cryptoType
operator|.
name|getIssuer
argument_list|()
argument_list|,
name|cryptoType
operator|.
name|getSerial
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type "
operator|+
name|type
argument_list|)
throw|;
block|}
specifier|private
name|X509Certificate
index|[]
name|getX509FromXKMSByID
parameter_list|(
name|Applications
name|application
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Getting public certificate from XKMS for application:%s; id: %s"
argument_list|,
name|application
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Id is not specified for certificate request"
argument_list|)
throw|;
block|}
comment|// Try local cache first
name|X509Certificate
index|[]
name|certs
init|=
name|checkX509Cache
argument_list|(
name|id
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
condition|)
block|{
return|return
name|certs
return|;
block|}
comment|// Now ask the XKMS Service
name|X509Certificate
name|cert
init|=
name|xkmsInvoker
operator|.
name|getCertificateForId
argument_list|(
name|application
argument_list|,
name|id
argument_list|)
decl_stmt|;
return|return
name|buildX509GetResult
argument_list|(
name|id
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|cert
argument_list|)
return|;
block|}
specifier|private
name|X509Certificate
index|[]
name|getX509FromXKMSByIssuerSerial
parameter_list|(
name|String
name|issuer
parameter_list|,
name|BigInteger
name|serial
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Getting public certificate from XKMS for issuer:%s; serial: %x"
argument_list|,
name|issuer
argument_list|,
name|serial
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|getKeyForIssuerSerial
argument_list|(
name|issuer
argument_list|,
name|serial
argument_list|)
decl_stmt|;
comment|// Try local cache first
name|X509Certificate
index|[]
name|certs
init|=
name|checkX509Cache
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
condition|)
block|{
return|return
name|certs
return|;
block|}
comment|// Now ask the XKMS Service
name|X509Certificate
name|cert
init|=
name|xkmsInvoker
operator|.
name|getCertificateForIssuerSerial
argument_list|(
name|issuer
argument_list|,
name|serial
argument_list|)
decl_stmt|;
return|return
name|buildX509GetResult
argument_list|(
name|key
argument_list|,
name|cert
argument_list|)
return|;
block|}
specifier|private
name|X509Certificate
index|[]
name|getX509FromXKMSByEndpoint
parameter_list|(
name|String
name|endpoint
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Getting public certificate from XKMS for endpoint:%s"
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
comment|// Try local cache first
name|X509Certificate
index|[]
name|certs
init|=
name|checkX509Cache
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
condition|)
block|{
return|return
name|certs
return|;
block|}
comment|// Now ask the XKMS Service
name|X509Certificate
name|cert
init|=
name|xkmsInvoker
operator|.
name|getCertificateForEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
return|return
name|buildX509GetResult
argument_list|(
name|endpoint
argument_list|,
name|cert
argument_list|)
return|;
block|}
specifier|private
name|X509Certificate
index|[]
name|checkX509Cache
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|xkmsClientCache
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|XKMSCacheToken
name|cachedToken
init|=
name|xkmsClientCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedToken
operator|!=
literal|null
operator|&&
name|cachedToken
operator|.
name|getX509Certificate
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|X509Certificate
index|[]
block|{
name|cachedToken
operator|.
name|getX509Certificate
argument_list|()
block|}
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|X509Certificate
index|[]
name|buildX509GetResult
parameter_list|(
name|String
name|key
parameter_list|,
name|X509Certificate
name|cert
parameter_list|)
block|{
if|if
condition|(
name|cert
operator|!=
literal|null
condition|)
block|{
comment|// Certificate was found: store in the cache
name|storeCertificateInCache
argument_list|(
name|cert
argument_list|,
name|key
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
operator|new
name|X509Certificate
index|[]
block|{
name|cert
block|}
return|;
block|}
else|else
block|{
comment|// Certificate was not found: return empty list
return|return
operator|new
name|X509Certificate
index|[
literal|0
index|]
return|;
block|}
block|}
comment|/**      * Try to get certificate locally. First try using the supplied CryptoType. If this      * does not work, and if the supplied CryptoType is a ALIAS, then try again with SUBJECT_DN      * in case the supplied Alias is actually a Certificate's Subject DN      *       * @param cryptoType      * @return if found certificate otherwise null returned      */
specifier|private
name|X509Certificate
index|[]
name|getCertificateLocaly
parameter_list|(
name|CryptoType
name|cryptoType
parameter_list|)
block|{
comment|// This only applies if we've configured a local Crypto instance...
if|if
condition|(
name|fallbackCrypto
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// First try using the supplied CryptoType instance
name|X509Certificate
index|[]
name|localCerts
init|=
literal|null
decl_stmt|;
try|try
block|{
name|localCerts
operator|=
name|fallbackCrypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Certificate is not found in local keystore using desired CryptoType: "
operator|+
name|cryptoType
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|localCerts
operator|==
literal|null
operator|&&
name|cryptoType
operator|.
name|getType
argument_list|()
operator|==
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
condition|)
block|{
comment|// If none found then try using either the Subject DN. This is because an
comment|// Encryption username in CXF is configured as an Alias in WSS4J, but may in fact
comment|// be a Subject DN
name|CryptoType
name|newCryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|SUBJECT_DN
argument_list|)
decl_stmt|;
name|newCryptoType
operator|.
name|setSubjectDN
argument_list|(
name|cryptoType
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|localCerts
operator|=
name|fallbackCrypto
operator|.
name|getX509Certificates
argument_list|(
name|newCryptoType
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Certificate is not found in local keystore and will be requested from "
operator|+
literal|"XKMS (first trying the cache): "
operator|+
name|cryptoType
operator|.
name|getAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|localCerts
return|;
block|}
comment|/**      * Service Aliases contain namespace      *       * @param cryptoType      * @return      */
specifier|private
name|boolean
name|isServiceName
parameter_list|(
name|CryptoType
name|cryptoType
parameter_list|)
block|{
return|return
name|cryptoType
operator|.
name|getAlias
argument_list|()
operator|.
name|contains
argument_list|(
literal|"{"
argument_list|)
return|;
block|}
specifier|private
name|String
name|getKeyForIssuerSerial
parameter_list|(
name|String
name|issuer
parameter_list|,
name|BigInteger
name|serial
parameter_list|)
block|{
return|return
name|issuer
operator|+
literal|"-"
operator|+
name|serial
operator|.
name|toString
argument_list|(
literal|16
argument_list|)
return|;
block|}
specifier|private
name|void
name|storeCertificateInCache
parameter_list|(
name|X509Certificate
name|certificate
parameter_list|,
name|String
name|key
parameter_list|,
name|boolean
name|validated
parameter_list|)
block|{
comment|// Store in the cache
if|if
condition|(
name|certificate
operator|!=
literal|null
operator|&&
name|xkmsClientCache
operator|!=
literal|null
condition|)
block|{
name|XKMSCacheToken
name|cacheToken
init|=
operator|new
name|XKMSCacheToken
argument_list|(
name|certificate
argument_list|)
decl_stmt|;
name|cacheToken
operator|.
name|setXkmsValidated
argument_list|(
name|validated
argument_list|)
expr_stmt|;
comment|// Store using a custom key (if any)
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|xkmsClientCache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|cacheToken
argument_list|)
expr_stmt|;
block|}
comment|// Store it using IssuerSerial as well
name|String
name|issuerSerialKey
init|=
name|getKeyForIssuerSerial
argument_list|(
name|certificate
operator|.
name|getIssuerX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|certificate
operator|.
name|getSerialNumber
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|issuerSerialKey
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|xkmsClientCache
operator|.
name|put
argument_list|(
name|issuerSerialKey
argument_list|,
name|cacheToken
argument_list|)
expr_stmt|;
block|}
comment|// Store it using the Subject DN as well
name|String
name|subjectDNKey
init|=
name|certificate
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|subjectDNKey
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|xkmsClientCache
operator|.
name|put
argument_list|(
name|subjectDNKey
argument_list|,
name|cacheToken
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

