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
name|httpsignature
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|KeyStore
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|httpsignature
operator|.
name|HTTPSignatureConstants
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
name|httpsignature
operator|.
name|PrivateKeyPasswordProvider
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
name|httpsignature
operator|.
name|exception
operator|.
name|SignatureException
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
name|crypto
operator|.
name|CryptoUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|KeyManagementUtils
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
name|KeyManagementUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLASSPATH_PREFIX
init|=
literal|"classpath:"
decl_stmt|;
specifier|private
name|KeyManagementUtils
parameter_list|()
block|{      }
specifier|public
specifier|static
name|Properties
name|loadSignatureOutProperties
parameter_list|()
block|{
name|Message
name|m
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
return|return
name|loadStoreProperties
argument_list|(
name|m
argument_list|,
name|HTTPSignatureConstants
operator|.
name|RSSEC_SIGNATURE_OUT_PROPS
argument_list|,
name|HTTPSignatureConstants
operator|.
name|RSSEC_SIGNATURE_PROPS
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Properties
name|loadStoreProperties
parameter_list|(
name|Message
name|m
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
name|SignatureException
argument_list|(
literal|"Properties resource is not identified"
argument_list|,
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
name|HTTPSignatureConstants
operator|.
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
name|HTTPSignatureConstants
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
name|HTTPSignatureConstants
operator|.
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
literal|"JKS"
expr_stmt|;
block|}
name|props
operator|.
name|setProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|String
name|alias
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|)
decl_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|,
name|alias
argument_list|)
expr_stmt|;
block|}
name|String
name|keystorePassword
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|)
decl_stmt|;
if|if
condition|(
name|keystorePassword
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|,
name|keystorePassword
argument_list|)
expr_stmt|;
block|}
name|String
name|keyPassword
init|=
operator|(
name|String
operator|)
name|m
operator|.
name|getContextualProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_PSWD
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyPassword
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|setProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_PSWD
argument_list|,
name|keyPassword
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|props
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
name|Properties
name|props
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
name|String
name|keyPswd
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_PSWD
argument_list|)
decl_stmt|;
name|String
name|alias
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_ALIAS
argument_list|)
decl_stmt|;
name|char
index|[]
name|keyPswdChars
init|=
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
if|if
condition|(
name|keyPswdChars
operator|==
literal|null
condition|)
block|{
name|PrivateKeyPasswordProvider
name|provider
init|=
name|loadPasswordProvider
argument_list|(
name|m
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|keyPswdChars
operator|=
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
literal|null
expr_stmt|;
block|}
return|return
name|CryptoUtils
operator|.
name|loadPrivateKey
argument_list|(
name|keyStore
argument_list|,
name|keyPswdChars
argument_list|,
name|alias
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|PrivateKeyPasswordProvider
name|loadPasswordProvider
parameter_list|(
name|Message
name|m
parameter_list|,
name|Properties
name|props
parameter_list|)
block|{
name|PrivateKeyPasswordProvider
name|cb
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|props
operator|.
name|containsKey
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_PSWD_PROVIDER
argument_list|)
condition|)
block|{
name|cb
operator|=
operator|(
name|PrivateKeyPasswordProvider
operator|)
name|props
operator|.
name|get
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_PSWD_PROVIDER
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|m
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
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_PSWD_PROVIDER
argument_list|)
expr_stmt|;
block|}
return|return
name|cb
return|;
block|}
specifier|private
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
name|KeyStore
name|keyStore
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|props
operator|.
name|containsKey
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE
argument_list|)
condition|)
block|{
name|keyStore
operator|=
operator|(
name|KeyStore
operator|)
name|props
operator|.
name|get
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyStore
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|props
operator|.
name|containsKey
argument_list|(
name|HTTPSignatureConstants
operator|.
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
name|SignatureException
argument_list|(
literal|"No keystore file has been configured"
argument_list|)
throw|;
block|}
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|Object
name|keyStoreProp
init|=
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
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyStoreProp
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|keyStoreProp
operator|instanceof
name|KeyStore
operator|)
condition|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"Unexpected key store class: "
operator|+
name|keyStoreProp
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|keyStore
operator|=
operator|(
name|KeyStore
operator|)
name|keyStoreProp
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|keyStore
operator|==
literal|null
condition|)
block|{
name|Bus
name|bus
init|=
name|m
operator|!=
literal|null
condition|?
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
else|:
literal|null
decl_stmt|;
name|keyStore
operator|=
name|loadKeyStore
argument_list|(
name|props
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
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
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|)
argument_list|,
name|keyStore
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|keyStore
return|;
block|}
specifier|private
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
name|keyStoreLoc
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_FILE
argument_list|)
decl_stmt|;
name|String
name|keyStoreType
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_TYPE
argument_list|)
decl_stmt|;
name|String
name|keyStorePswd
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|HTTPSignatureConstants
operator|.
name|RSSEC_KEY_STORE_PSWD
argument_list|)
decl_stmt|;
return|return
name|loadKeyStore
argument_list|(
name|keyStoreLoc
argument_list|,
name|keyStoreType
argument_list|,
name|keyStorePswd
argument_list|,
name|bus
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|KeyStore
name|loadKeyStore
parameter_list|(
name|String
name|keyStoreLoc
parameter_list|,
name|String
name|keyStoreType
parameter_list|,
name|String
name|keyStorePswd
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|keyStorePswd
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"No keystore password was defined"
argument_list|)
throw|;
block|}
try|try
block|{
name|InputStream
name|is
init|=
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
name|SignatureException
argument_list|(
literal|"Key store can not be loaded"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|//
comment|//<Start> Copied from JAX-RS RT FRONTEND ResourceUtils
comment|//
specifier|private
specifier|static
name|InputStream
name|getResourceStream
parameter_list|(
name|String
name|loc
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getResourceURL
argument_list|(
name|loc
argument_list|,
name|bus
argument_list|)
decl_stmt|;
return|return
name|url
operator|==
literal|null
condition|?
literal|null
else|:
name|url
operator|.
name|openStream
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|URL
name|getResourceURL
parameter_list|(
name|String
name|loc
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|loc
operator|.
name|startsWith
argument_list|(
name|CLASSPATH_PREFIX
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|loc
operator|.
name|substring
argument_list|(
name|CLASSPATH_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|url
operator|=
name|getClasspathResourceURL
argument_list|(
name|path
argument_list|,
name|KeyManagementUtils
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|loc
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// it can be either a classpath or file resource without a scheme
name|url
operator|=
name|getClasspathResourceURL
argument_list|(
name|loc
argument_list|,
name|KeyManagementUtils
operator|.
name|class
argument_list|,
name|bus
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|loc
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|url
operator|=
name|file
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No resource "
operator|+
name|loc
operator|+
literal|" is available"
argument_list|)
expr_stmt|;
block|}
return|return
name|url
return|;
block|}
specifier|private
specifier|static
name|URL
name|getClasspathResourceURL
parameter_list|(
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|callingClass
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|URL
name|url
init|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|path
argument_list|,
name|callingClass
argument_list|)
decl_stmt|;
return|return
name|url
operator|==
literal|null
condition|?
name|getResource
argument_list|(
name|path
argument_list|,
name|URL
operator|.
name|class
argument_list|,
name|bus
argument_list|)
else|:
name|url
return|;
block|}
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getResource
parameter_list|(
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceClass
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
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
return|return
name|rm
operator|.
name|resolveResource
argument_list|(
name|path
argument_list|,
name|resourceClass
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|String
name|propertiesLocation
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|getResourceStream
argument_list|(
name|propertiesLocation
argument_list|,
name|bus
argument_list|)
init|)
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SignatureException
argument_list|(
literal|"The properties file "
operator|+
name|propertiesLocation
operator|+
literal|" could not be read"
argument_list|)
throw|;
block|}
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
return|return
name|props
return|;
block|}
comment|//
comment|//<End> Copied from JAX-RS RT FRONTEND ResourceUtils
comment|//
block|}
end_class

end_unit

