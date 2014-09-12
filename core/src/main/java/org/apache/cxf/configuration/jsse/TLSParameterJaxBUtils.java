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
name|configuration
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|KeyStoreException
import|;
end_import

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
name|SecureRandom
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
name|CertificateException
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
name|CertificateFactory
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
name|Collection
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
name|net
operator|.
name|ssl
operator|.
name|KeyManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManagerFactory
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|PasswordCallback
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
name|BusFactory
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|configuration
operator|.
name|security
operator|.
name|CertStoreType
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
name|configuration
operator|.
name|security
operator|.
name|KeyManagersType
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
name|configuration
operator|.
name|security
operator|.
name|KeyStoreType
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
name|configuration
operator|.
name|security
operator|.
name|SecureRandomParameters
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
name|configuration
operator|.
name|security
operator|.
name|TrustManagersType
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
name|resource
operator|.
name|ResourceManager
import|;
end_import

begin_comment
comment|/**  * This class provides some functionality to convert the JAXB  * generated types in the security.xsd to the items needed  * to programatically configure the HTTPConduit and HTTPDestination  * with TLSClientParameters and TLSServerParameters respectively.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TLSParameterJaxBUtils
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
name|TLSParameterJaxBUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|TLSParameterJaxBUtils
parameter_list|()
block|{
comment|// empty
block|}
comment|/**      * This method converts the JAXB generated type into a SecureRandom.      */
specifier|public
specifier|static
name|SecureRandom
name|getSecureRandom
parameter_list|(
name|SecureRandomParameters
name|secureRandomParams
parameter_list|)
throws|throws
name|GeneralSecurityException
block|{
name|SecureRandom
name|secureRandom
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|secureRandomParams
operator|!=
literal|null
condition|)
block|{
name|String
name|secureRandomAlg
init|=
name|secureRandomParams
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|String
name|randomProvider
init|=
name|secureRandomParams
operator|.
name|getProvider
argument_list|()
decl_stmt|;
if|if
condition|(
name|randomProvider
operator|!=
literal|null
condition|)
block|{
name|secureRandom
operator|=
name|secureRandomAlg
operator|!=
literal|null
condition|?
name|SecureRandom
operator|.
name|getInstance
argument_list|(
name|secureRandomAlg
argument_list|,
name|randomProvider
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
else|else
block|{
name|secureRandom
operator|=
name|secureRandomAlg
operator|!=
literal|null
condition|?
name|SecureRandom
operator|.
name|getInstance
argument_list|(
name|secureRandomAlg
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
block|}
return|return
name|secureRandom
return|;
block|}
comment|/**      * This method converts a JAXB generated KeyStoreType into a KeyStore.      */
specifier|public
specifier|static
name|KeyStore
name|getKeyStore
parameter_list|(
name|KeyStoreType
name|kst
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
if|if
condition|(
name|kst
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|type
init|=
name|SSLUtils
operator|.
name|getKeystoreType
argument_list|(
name|kst
operator|.
name|isSetType
argument_list|()
condition|?
name|kst
operator|.
name|getType
argument_list|()
else|:
literal|null
argument_list|,
name|LOG
argument_list|,
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|char
index|[]
name|password
init|=
name|kst
operator|.
name|isSetPassword
argument_list|()
condition|?
name|deobfuscate
argument_list|(
name|kst
operator|.
name|getPassword
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|String
name|tmp
init|=
name|SSLUtils
operator|.
name|getKeystorePassword
argument_list|(
literal|null
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|!=
literal|null
condition|)
block|{
name|password
operator|=
name|tmp
operator|.
name|toCharArray
argument_list|()
expr_stmt|;
block|}
block|}
name|String
name|provider
init|=
name|SSLUtils
operator|.
name|getKeystoreProvider
argument_list|(
name|kst
operator|.
name|isSetProvider
argument_list|()
condition|?
name|kst
operator|.
name|getProvider
argument_list|()
else|:
literal|null
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|provider
operator|==
literal|null
condition|?
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
else|:
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|type
argument_list|,
name|provider
argument_list|)
decl_stmt|;
if|if
condition|(
name|kst
operator|.
name|isSetFile
argument_list|()
condition|)
block|{
name|FileInputStream
name|kstInputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|kstInputStream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|kst
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|kstInputStream
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|kstInputStream
operator|!=
literal|null
condition|)
block|{
name|kstInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|kst
operator|.
name|isSetResource
argument_list|()
condition|)
block|{
specifier|final
name|java
operator|.
name|io
operator|.
name|InputStream
name|is
init|=
name|getResourceAsStream
argument_list|(
name|kst
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
specifier|final
name|String
name|msg
init|=
literal|"Could not load keystore resource "
operator|+
name|kst
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|java
operator|.
name|io
operator|.
name|IOException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|keyStore
operator|.
name|load
argument_list|(
name|is
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|kst
operator|.
name|isSetUrl
argument_list|()
condition|)
block|{
name|keyStore
operator|.
name|load
argument_list|(
operator|new
name|URL
argument_list|(
name|kst
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|loc
init|=
name|SSLUtils
operator|.
name|getKeystore
argument_list|(
literal|null
argument_list|,
name|LOG
argument_list|)
decl_stmt|;
name|InputStream
name|ins
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loc
operator|!=
literal|null
condition|)
block|{
name|ins
operator|=
operator|new
name|FileInputStream
argument_list|(
name|loc
argument_list|)
expr_stmt|;
block|}
name|keyStore
operator|.
name|load
argument_list|(
name|ins
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ins
operator|!=
literal|null
condition|)
block|{
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|keyStore
return|;
block|}
comment|/**      * This method converts a JAXB generated CertStoreType into a KeyStore.      */
specifier|public
specifier|static
name|KeyStore
name|getKeyStore
parameter_list|(
specifier|final
name|CertStoreType
name|pst
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
throws|,
name|KeyStoreException
throws|,
name|NoSuchAlgorithmException
block|{
if|if
condition|(
name|pst
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|pst
operator|.
name|isSetFile
argument_list|()
condition|)
block|{
return|return
name|createTrustStore
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|pst
operator|.
name|getFile
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|pst
operator|.
name|isSetResource
argument_list|()
condition|)
block|{
specifier|final
name|java
operator|.
name|io
operator|.
name|InputStream
name|is
init|=
name|getResourceAsStream
argument_list|(
name|pst
operator|.
name|getResource
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
specifier|final
name|String
name|msg
init|=
literal|"Could not load truststore resource "
operator|+
name|pst
operator|.
name|getResource
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|java
operator|.
name|io
operator|.
name|IOException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
return|return
name|createTrustStore
argument_list|(
name|is
argument_list|)
return|;
block|}
if|if
condition|(
name|pst
operator|.
name|isSetUrl
argument_list|()
condition|)
block|{
return|return
name|createTrustStore
argument_list|(
operator|new
name|URL
argument_list|(
name|pst
operator|.
name|getUrl
argument_list|()
argument_list|)
operator|.
name|openStream
argument_list|()
argument_list|)
return|;
block|}
comment|// TODO error?
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|,
name|TLSParameterJaxBUtils
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|ResourceManager
name|rm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ResourceManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rm
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|rm
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|is
return|;
block|}
comment|/**      * Create a KeyStore containing the trusted CA certificates contained      * in the supplied input stream.      */
specifier|private
specifier|static
name|KeyStore
name|createTrustStore
parameter_list|(
specifier|final
name|java
operator|.
name|io
operator|.
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
throws|,
name|KeyStoreException
throws|,
name|NoSuchAlgorithmException
block|{
specifier|final
name|Collection
argument_list|<
name|?
extends|extends
name|Certificate
argument_list|>
name|certs
init|=
name|loadCertificates
argument_list|(
name|is
argument_list|)
decl_stmt|;
specifier|final
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|Certificate
name|cert
range|:
name|certs
control|)
block|{
specifier|final
name|X509Certificate
name|xcert
init|=
operator|(
name|X509Certificate
operator|)
name|cert
decl_stmt|;
name|keyStore
operator|.
name|setCertificateEntry
argument_list|(
name|xcert
operator|.
name|getSubjectX500Principal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|cert
argument_list|)
expr_stmt|;
block|}
return|return
name|keyStore
return|;
block|}
comment|/**      * load the certificates as X.509 certificates      */
specifier|private
specifier|static
name|Collection
argument_list|<
name|?
extends|extends
name|Certificate
argument_list|>
name|loadCertificates
parameter_list|(
specifier|final
name|java
operator|.
name|io
operator|.
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
throws|,
name|CertificateException
block|{
specifier|final
name|CertificateFactory
name|factory
init|=
name|CertificateFactory
operator|.
name|getInstance
argument_list|(
literal|"X.509"
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|generateCertificates
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|char
index|[]
name|deobfuscate
parameter_list|(
name|String
name|s
parameter_list|)
block|{
comment|// From the Jetty org.eclipse.jetty.http.security.Password class
if|if
condition|(
operator|!
name|s
operator|.
name|startsWith
argument_list|(
literal|"OBF:"
argument_list|)
condition|)
block|{
return|return
name|s
operator|.
name|toCharArray
argument_list|()
return|;
block|}
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|char
index|[]
name|b
init|=
operator|new
name|char
index|[
name|s
operator|.
name|length
argument_list|()
operator|/
literal|2
index|]
decl_stmt|;
name|int
name|l
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|+=
literal|4
control|)
block|{
name|String
name|x
init|=
name|s
operator|.
name|substring
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|4
argument_list|)
decl_stmt|;
name|int
name|i0
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|x
argument_list|,
literal|36
argument_list|)
decl_stmt|;
name|int
name|i1
init|=
name|i0
operator|/
literal|256
decl_stmt|;
name|int
name|i2
init|=
name|i0
operator|%
literal|256
decl_stmt|;
name|b
index|[
name|l
operator|++
index|]
operator|=
call|(
name|char
call|)
argument_list|(
operator|(
name|i1
operator|+
name|i2
operator|-
literal|254
operator|)
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|String
argument_list|(
name|b
argument_list|,
literal|0
argument_list|,
name|l
argument_list|)
operator|.
name|toCharArray
argument_list|()
return|;
block|}
comment|/**      * This method converts the JAXB KeyManagersType into a list of      * JSSE KeyManagers.      */
specifier|public
specifier|static
name|KeyManager
index|[]
name|getKeyManagers
parameter_list|(
name|KeyManagersType
name|kmc
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|KeyStore
name|keyStore
init|=
name|getKeyStore
argument_list|(
name|kmc
operator|.
name|getKeyStore
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|alg
init|=
name|kmc
operator|.
name|isSetFactoryAlgorithm
argument_list|()
condition|?
name|kmc
operator|.
name|getFactoryAlgorithm
argument_list|()
else|:
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
decl_stmt|;
name|char
index|[]
name|keyPass
init|=
name|getKeyPassword
argument_list|(
name|kmc
argument_list|)
decl_stmt|;
name|KeyManagerFactory
name|fac
init|=
name|kmc
operator|.
name|isSetProvider
argument_list|()
condition|?
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|alg
argument_list|,
name|kmc
operator|.
name|getProvider
argument_list|()
argument_list|)
else|:
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|alg
argument_list|)
decl_stmt|;
name|fac
operator|.
name|init
argument_list|(
name|keyStore
argument_list|,
name|keyPass
argument_list|)
expr_stmt|;
return|return
name|fac
operator|.
name|getKeyManagers
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|char
index|[]
name|getKeyPassword
parameter_list|(
name|KeyManagersType
name|kmc
parameter_list|)
block|{
name|char
index|[]
name|keyPass
init|=
name|kmc
operator|.
name|isSetKeyPassword
argument_list|()
condition|?
name|deobfuscate
argument_list|(
name|kmc
operator|.
name|getKeyPassword
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|keyPass
operator|!=
literal|null
condition|)
block|{
return|return
name|keyPass
return|;
block|}
name|String
name|callbackHandlerClass
init|=
name|kmc
operator|.
name|getKeyPasswordCallbackHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackHandlerClass
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|CallbackHandler
name|ch
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ch
operator|=
operator|(
name|CallbackHandler
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|callbackHandlerClass
argument_list|,
name|TLSParameterJaxBUtils
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
if|if
condition|(
name|ch
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PasswordCallback
name|pwCb
init|=
operator|new
name|PasswordCallback
argument_list|(
name|kmc
operator|.
name|getKeyStore
argument_list|()
operator|.
name|getFile
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|PasswordCallback
index|[]
name|callbacks
init|=
operator|new
name|PasswordCallback
index|[]
block|{
name|pwCb
block|}
decl_stmt|;
name|ch
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
name|keyPass
operator|=
name|callbacks
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
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
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Cannot load key password from callback handler: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|keyPass
return|;
block|}
comment|/**      * This method converts the JAXB KeyManagersType into a list of      * JSSE TrustManagers.      */
specifier|public
specifier|static
name|TrustManager
index|[]
name|getTrustManagers
parameter_list|(
name|TrustManagersType
name|tmc
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
specifier|final
name|KeyStore
name|keyStore
init|=
name|tmc
operator|.
name|isSetKeyStore
argument_list|()
condition|?
name|getKeyStore
argument_list|(
name|tmc
operator|.
name|getKeyStore
argument_list|()
argument_list|)
else|:
operator|(
name|tmc
operator|.
name|isSetCertStore
argument_list|()
condition|?
name|getKeyStore
argument_list|(
name|tmc
operator|.
name|getCertStore
argument_list|()
argument_list|)
else|:
operator|(
name|KeyStore
operator|)
literal|null
operator|)
decl_stmt|;
name|String
name|alg
init|=
name|tmc
operator|.
name|isSetFactoryAlgorithm
argument_list|()
condition|?
name|tmc
operator|.
name|getFactoryAlgorithm
argument_list|()
else|:
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
decl_stmt|;
name|TrustManagerFactory
name|fac
init|=
name|tmc
operator|.
name|isSetProvider
argument_list|()
condition|?
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|alg
argument_list|,
name|tmc
operator|.
name|getProvider
argument_list|()
argument_list|)
else|:
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|alg
argument_list|)
decl_stmt|;
name|fac
operator|.
name|init
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
return|return
name|fac
operator|.
name|getTrustManagers
argument_list|()
return|;
block|}
block|}
end_class

end_unit

