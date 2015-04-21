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
name|jose
operator|.
name|jaxrs
package|;
end_package

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
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|CertPath
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
name|CertPathBuilder
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
name|CertPathBuilderResult
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
name|CertPathValidator
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
name|CertStore
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
name|Certificate
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
name|CollectionCertStoreParameters
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
name|PKIXBuilderParameters
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
name|X509CertSelector
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
name|security
operator|.
name|interfaces
operator|.
name|RSAPrivateKey
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|PropertyUtils
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
name|crypto
operator|.
name|CryptoUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|JoseException
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
name|jose
operator|.
name|jwk
operator|.
name|JsonWebKey
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_comment
comment|/**  * Encryption helpers  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|KeyManagementUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_TYPE
init|=
literal|"rs.security.keystore.type"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_PSWD
init|=
literal|"rs.security.keystore.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_PSWD
init|=
literal|"rs.security.key.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_ALIAS
init|=
literal|"rs.security.keystore.alias"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_ALIASES
init|=
literal|"rs.security.keystore.aliases"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_STORE_FILE
init|=
literal|"rs.security.keystore.file"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_PRINCIPAL_NAME
init|=
literal|"rs.security.principal.name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.key.password.provider"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_SIG_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.signature.key.password.provider"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_DECRYPT_KEY_PSWD_PROVIDER
init|=
literal|"rs.security.decryption.key.password.provider"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_DEFAULT_ALGORITHMS
init|=
literal|"rs.security.default.algorithms"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RSSEC_REPORT_KEY_PROP
init|=
literal|"rs.security.report.public.key"
decl_stmt|;
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
name|KeyManagementUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|KeyManagementUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|loadAndEncodeX509CertificateOrChain
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
name|X509Certificate
index|[]
name|chain
init|=
name|loadX509CertificateOrChain
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
return|return
name|encodeX509CertificateChain
argument_list|(
name|chain
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|X509Certificate
index|[]
name|loadX509CertificateOrChain
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
name|KeyStore
name|keyStore
init|=
name|KeyManagementUtils
operator|.
name|loadPersistKeyStore
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|String
name|alias
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_ALIAS
argument_list|)
decl_stmt|;
return|return
name|loadX509CertificateOrChain
argument_list|(
name|keyStore
argument_list|,
name|alias
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|X509Certificate
index|[]
name|loadX509CertificateOrChain
parameter_list|(
name|KeyStore
name|keyStore
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
try|try
block|{
name|Certificate
index|[]
name|certs
init|=
name|keyStore
operator|.
name|getCertificateChain
argument_list|(
name|alias
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
name|Arrays
operator|.
name|copyOf
argument_list|(
name|certs
argument_list|,
name|certs
operator|.
name|length
argument_list|,
name|X509Certificate
index|[]
operator|.
expr|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|X509Certificate
index|[]
block|{
operator|(
name|X509Certificate
operator|)
name|CryptoUtils
operator|.
name|loadCertificate
argument_list|(
name|keyStore
argument_list|,
name|alias
argument_list|)
block|}
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"X509 Certificates can not be created"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|PublicKey
name|loadPublicKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
name|KeyStore
name|keyStore
init|=
name|KeyManagementUtils
operator|.
name|loadPersistKeyStore
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|loadPublicKey
argument_list|(
name|keyStore
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_ALIAS
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|PublicKey
name|loadPublicKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|keyStoreLocProp
parameter_list|)
block|{
return|return
name|loadPublicKey
argument_list|(
name|m
argument_list|,
name|keyStoreLocProp
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|PublicKey
name|loadPublicKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|keyStoreLocPropPreferred
parameter_list|,
name|String
name|keyStoreLocPropDefault
parameter_list|)
block|{
name|String
name|keyStoreLoc
init|=
name|getMessageProperty
argument_list|(
name|m
argument_list|,
name|keyStoreLocPropPreferred
argument_list|,
name|keyStoreLocPropDefault
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
try|try
block|{
name|Properties
name|props
init|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|keyStoreLoc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
return|return
name|KeyManagementUtils
operator|.
name|loadPublicKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Public key can not be loaded"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getMessageProperty
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|keyStoreLocPropPreferred
parameter_list|,
name|String
name|keyStoreLocPropDefault
parameter_list|)
block|{
name|String
name|propLoc
init|=
operator|(
name|String
operator|)
name|MessageUtils
operator|.
name|getContextualProperty
argument_list|(
name|m
argument_list|,
name|keyStoreLocPropPreferred
argument_list|,
name|keyStoreLocPropDefault
argument_list|)
decl_stmt|;
if|if
condition|(
name|propLoc
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Properties resource is not identified"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|()
throw|;
block|}
return|return
name|propLoc
return|;
block|}
specifier|private
specifier|static
name|PrivateKey
name|loadPrivateKey
parameter_list|(
name|KeyStore
name|keyStore
parameter_list|,
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|PrivateKeyPasswordProvider
name|provider
parameter_list|,
name|String
name|keyOper
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
name|String
name|keyPswd
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_PSWD
argument_list|)
decl_stmt|;
name|String
name|theAlias
init|=
name|alias
operator|!=
literal|null
condition|?
name|alias
else|:
name|getKeyId
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
name|char
index|[]
name|keyPswdChars
init|=
name|provider
operator|!=
literal|null
condition|?
name|provider
operator|.
name|getPassword
argument_list|(
name|props
argument_list|)
else|:
name|keyPswd
operator|!=
literal|null
condition|?
name|keyPswd
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|loadPrivateKey
argument_list|(
name|keyStore
argument_list|,
name|keyPswdChars
argument_list|,
name|theAlias
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|PrivateKey
name|loadPrivateKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|keyStoreLocProp
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
return|return
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|keyStoreLocProp
argument_list|,
literal|null
argument_list|,
name|keyOper
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|PrivateKey
name|loadPrivateKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|keyStoreLocPropPreferred
parameter_list|,
name|String
name|keyStoreLocPropDefault
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|String
name|keyStoreLoc
init|=
name|getMessageProperty
argument_list|(
name|m
argument_list|,
name|keyStoreLocPropPreferred
argument_list|,
name|keyStoreLocPropDefault
argument_list|)
decl_stmt|;
name|Bus
name|bus
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
try|try
block|{
name|Properties
name|props
init|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|keyStoreLoc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
return|return
name|loadPrivateKey
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getKeyId
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|preferredPropertyName
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|String
name|kid
init|=
literal|null
decl_stmt|;
name|String
name|altPropertyName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|keyOper
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_ENCRYPT
argument_list|)
operator|||
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_DECRYPT
argument_list|)
condition|)
block|{
name|altPropertyName
operator|=
name|preferredPropertyName
operator|+
literal|".jwe"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
operator|||
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_VERIFY
argument_list|)
condition|)
block|{
name|altPropertyName
operator|=
name|preferredPropertyName
operator|+
literal|".jws"
expr_stmt|;
block|}
name|String
name|direction
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|==
name|m
condition|?
literal|".out"
else|:
literal|".in"
decl_stmt|;
name|kid
operator|=
operator|(
name|String
operator|)
name|MessageUtils
operator|.
name|getContextualProperty
argument_list|(
name|m
argument_list|,
name|preferredPropertyName
argument_list|,
name|altPropertyName
operator|+
name|direction
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|kid
operator|==
literal|null
condition|)
block|{
name|kid
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|preferredPropertyName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|kid
operator|==
literal|null
operator|&&
name|altPropertyName
operator|!=
literal|null
condition|)
block|{
name|kid
operator|=
name|props
operator|.
name|getProperty
argument_list|(
name|altPropertyName
argument_list|)
expr_stmt|;
block|}
return|return
name|kid
return|;
block|}
specifier|public
specifier|static
name|PrivateKeyPasswordProvider
name|loadPasswordProvider
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|PrivateKeyPasswordProvider
name|cb
init|=
operator|(
name|PrivateKeyPasswordProvider
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|RSSEC_KEY_PSWD_PROVIDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|cb
operator|==
literal|null
operator|&&
name|keyOper
operator|!=
literal|null
condition|)
block|{
name|String
name|propName
init|=
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_SIGN
argument_list|)
condition|?
name|RSSEC_SIG_KEY_PSWD_PROVIDER
else|:
name|keyOper
operator|.
name|equals
argument_list|(
name|JsonWebKey
operator|.
name|KEY_OPER_DECRYPT
argument_list|)
condition|?
name|RSSEC_DECRYPT_KEY_PSWD_PROVIDER
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|propName
operator|!=
literal|null
condition|)
block|{
name|cb
operator|=
operator|(
name|PrivateKeyPasswordProvider
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|propName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cb
return|;
block|}
specifier|public
specifier|static
name|RSAPrivateKey
name|loadPrivateKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|KeyStore
name|keyStore
init|=
name|loadPersistKeyStore
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
return|return
operator|(
name|RSAPrivateKey
operator|)
name|loadPrivateKey
argument_list|(
name|keyStore
argument_list|,
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|RSAPrivateKey
name|loadPrivateKey
parameter_list|(
name|KeyStore
name|keyStore
parameter_list|,
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|keyOper
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
name|Bus
name|bus
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|PrivateKeyPasswordProvider
name|cb
init|=
name|loadPasswordProvider
argument_list|(
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|)
decl_stmt|;
if|if
condition|(
name|cb
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SecurityContext
name|sc
init|=
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|!=
literal|null
condition|)
block|{
name|Principal
name|p
init|=
name|sc
operator|.
name|getUserPrincipal
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|RSSEC_PRINCIPAL_NAME
argument_list|,
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
operator|(
name|RSAPrivateKey
operator|)
name|loadPrivateKey
argument_list|(
name|keyStore
argument_list|,
name|m
argument_list|,
name|props
argument_list|,
name|bus
argument_list|,
name|cb
argument_list|,
name|keyOper
argument_list|,
name|alias
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|KeyStore
name|loadPersistKeyStore
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
if|if
condition|(
operator|!
name|props
operator|.
name|containsKey
argument_list|(
name|RSSEC_KEY_STORE_FILE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No keystore file has been configured"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
literal|"No keystore file has been configured"
argument_list|)
throw|;
block|}
name|KeyStore
name|keyStore
init|=
operator|(
name|KeyStore
operator|)
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|props
operator|.
name|get
argument_list|(
name|RSSEC_KEY_STORE_FILE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyStore
operator|==
literal|null
condition|)
block|{
name|keyStore
operator|=
name|loadKeyStore
argument_list|(
name|props
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|props
operator|.
name|get
argument_list|(
name|RSSEC_KEY_STORE_FILE
argument_list|)
argument_list|,
name|keyStore
argument_list|)
expr_stmt|;
block|}
return|return
name|keyStore
return|;
block|}
specifier|public
specifier|static
name|KeyStore
name|loadKeyStore
parameter_list|(
name|Properties
name|props
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|String
name|keyStoreType
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_TYPE
argument_list|)
decl_stmt|;
name|String
name|keyStoreLoc
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_FILE
argument_list|)
decl_stmt|;
name|String
name|keyStorePswd
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|RSSEC_KEY_STORE_PSWD
argument_list|)
decl_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|ResourceUtils
operator|.
name|getResourceStream
argument_list|(
name|keyStoreLoc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
return|return
name|CryptoUtils
operator|.
name|loadKeyStore
argument_list|(
name|is
argument_list|,
name|keyStorePswd
operator|.
name|toCharArray
argument_list|()
argument_list|,
name|keyStoreType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Key store can not be loaded"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|encodeX509CertificateChain
parameter_list|(
name|X509Certificate
index|[]
name|chain
parameter_list|)
block|{
return|return
name|encodeX509CertificateChain
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|chain
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|encodeX509CertificateChain
parameter_list|(
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|chain
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|encodedChain
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|chain
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|X509Certificate
name|cert
range|:
name|chain
control|)
block|{
try|try
block|{
name|encodedChain
operator|.
name|add
argument_list|(
name|CryptoUtils
operator|.
name|encodeCertificate
argument_list|(
name|cert
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"X509 Certificate can not be encoded"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|encodedChain
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|toX509CertificateChain
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|base64EncodedChain
parameter_list|)
block|{
if|if
condition|(
name|base64EncodedChain
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|certs
init|=
operator|new
name|ArrayList
argument_list|<
name|X509Certificate
argument_list|>
argument_list|(
name|base64EncodedChain
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|encodedCert
range|:
name|base64EncodedChain
control|)
block|{
try|try
block|{
name|certs
operator|.
name|add
argument_list|(
operator|(
name|X509Certificate
operator|)
name|CryptoUtils
operator|.
name|decodeCertificate
argument_list|(
name|encodedCert
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
name|LOG
operator|.
name|warning
argument_list|(
literal|"X509 Certificate can not be decoded"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|certs
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|validateCertificateChain
parameter_list|(
name|Properties
name|storeProperties
parameter_list|,
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|inCerts
parameter_list|)
block|{
name|KeyStore
name|ks
init|=
name|loadPersistKeyStore
argument_list|(
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
argument_list|,
name|storeProperties
argument_list|)
decl_stmt|;
name|validateCertificateChain
argument_list|(
name|ks
argument_list|,
name|inCerts
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|validateCertificateChain
parameter_list|(
name|KeyStore
name|ks
parameter_list|,
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|inCerts
parameter_list|)
block|{
comment|// Initial chain validation, to be enhanced as needed
try|try
block|{
name|X509CertSelector
name|certSelect
init|=
operator|new
name|X509CertSelector
argument_list|()
decl_stmt|;
name|certSelect
operator|.
name|setCertificate
argument_list|(
operator|(
name|X509Certificate
operator|)
name|inCerts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|PKIXBuilderParameters
name|pbParams
init|=
operator|new
name|PKIXBuilderParameters
argument_list|(
name|ks
argument_list|,
name|certSelect
argument_list|)
decl_stmt|;
name|pbParams
operator|.
name|addCertStore
argument_list|(
name|CertStore
operator|.
name|getInstance
argument_list|(
literal|"Collection"
argument_list|,
operator|new
name|CollectionCertStoreParameters
argument_list|(
name|inCerts
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|pbParams
operator|.
name|setMaxPathLength
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|pbParams
operator|.
name|setRevocationEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|CertPathBuilderResult
name|buildResult
init|=
name|CertPathBuilder
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
operator|.
name|build
argument_list|(
name|pbParams
argument_list|)
decl_stmt|;
name|CertPath
name|certPath
init|=
name|buildResult
operator|.
name|getCertPath
argument_list|()
decl_stmt|;
name|CertPathValidator
operator|.
name|getInstance
argument_list|(
literal|"PKIX"
argument_list|)
operator|.
name|validate
argument_list|(
name|certPath
argument_list|,
name|pbParams
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Certificate path validation error"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|X509Certificate
index|[]
name|toX509CertificateChainArray
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|base64EncodedChain
parameter_list|)
block|{
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|chain
init|=
name|toX509CertificateChain
argument_list|(
name|base64EncodedChain
argument_list|)
decl_stmt|;
return|return
name|chain
operator|==
literal|null
condition|?
literal|null
else|:
name|chain
operator|.
name|toArray
argument_list|(
operator|new
name|X509Certificate
index|[]
block|{}
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getKeyAlgorithm
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|String
name|propName
parameter_list|,
name|String
name|defaultAlg
parameter_list|)
block|{
name|String
name|algo
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|propName
argument_list|)
decl_stmt|;
if|if
condition|(
name|algo
operator|==
literal|null
operator|&&
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|m
operator|.
name|getContextualProperty
argument_list|(
name|RSSEC_DEFAULT_ALGORITHMS
argument_list|)
argument_list|)
condition|)
block|{
name|algo
operator|=
name|defaultAlg
expr_stmt|;
block|}
return|return
name|algo
return|;
block|}
specifier|public
specifier|static
name|Properties
name|loadStoreProperties
parameter_list|(
name|Message
name|m
parameter_list|,
name|boolean
name|required
parameter_list|,
name|String
name|storeProp1
parameter_list|,
name|String
name|storeProp2
parameter_list|)
block|{
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|required
condition|)
block|{
throw|throw
operator|new
name|JoseException
argument_list|()
throw|;
block|}
return|return
literal|null
return|;
block|}
name|Properties
name|props
init|=
literal|null
decl_stmt|;
name|String
name|propLoc
init|=
operator|(
name|String
operator|)
name|MessageUtils
operator|.
name|getContextualProperty
argument_list|(
name|m
argument_list|,
name|storeProp1
argument_list|,
name|storeProp2
argument_list|)
decl_stmt|;
if|if
condition|(
name|propLoc
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|props
operator|=
name|ResourceUtils
operator|.
name|loadProperties
argument_list|(
name|propLoc
argument_list|,
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Properties resource is not identified"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|String
name|keyFile
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|RSSEC_KEY_STORE_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyFile
operator|!=
literal|null
condition|)
block|{
name|props
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
name|KeyManagementUtils
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|,
name|keyFile
argument_list|)
expr_stmt|;
name|String
name|type
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|RSSEC_KEY_STORE_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
literal|"jwk"
expr_stmt|;
block|}
name|props
operator|.
name|setProperty
argument_list|(
name|RSSEC_KEY_STORE_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|props
operator|==
literal|null
operator|&&
name|required
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Properties resource is not identified"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|()
throw|;
block|}
return|return
name|props
return|;
block|}
specifier|public
specifier|static
name|RSAPrivateKey
name|loadPrivateKey
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|,
name|List
argument_list|<
name|X509Certificate
argument_list|>
name|inCerts
parameter_list|,
name|String
name|keyOper
parameter_list|)
block|{
name|KeyStore
name|ks
init|=
name|loadPersistKeyStore
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|alias
init|=
name|ks
operator|.
name|getCertificateAlias
argument_list|(
name|inCerts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|e
init|=
name|ks
operator|.
name|aliases
argument_list|()
init|;
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|currentAlias
init|=
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|X509Certificate
index|[]
name|currentCertArray
init|=
name|loadX509CertificateOrChain
argument_list|(
name|ks
argument_list|,
name|currentAlias
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentCertArray
operator|!=
literal|null
condition|)
block|{
name|alias
operator|=
name|currentAlias
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|loadPrivateKey
argument_list|(
name|ks
argument_list|,
name|m
argument_list|,
name|props
argument_list|,
name|keyOper
argument_list|,
name|alias
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Private key can not be loaded"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JoseException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

