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
name|saml
operator|.
name|sso
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|ResourceBundle
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
name|DestroyFailedException
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
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
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
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|BundleUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|ext
operator|.
name|WSPasswordCallback
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"metadata"
argument_list|)
specifier|public
class|class
name|MetadataService
extends|extends
name|AbstractSSOSpHandler
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|MetadataService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|ResourceBundle
name|BUNDLE
init|=
name|BundleUtils
operator|.
name|getBundle
argument_list|(
name|MetadataService
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|serviceAddress
decl_stmt|;
specifier|private
name|String
name|assertionConsumerServiceAddress
decl_stmt|;
specifier|private
name|String
name|logoutServiceAddress
decl_stmt|;
specifier|private
name|boolean
name|addEndpointAddressToContext
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
literal|"text/xml"
argument_list|)
specifier|public
name|Document
name|getMetadata
parameter_list|()
block|{
try|try
block|{
name|MetadataWriter
name|metadataWriter
init|=
operator|new
name|MetadataWriter
argument_list|()
decl_stmt|;
name|Crypto
name|crypto
init|=
name|getSignatureCrypto
argument_list|()
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No crypto instance of properties file configured for signature"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|String
name|signatureUser
init|=
name|getSignatureUsername
argument_list|()
decl_stmt|;
if|if
condition|(
name|signatureUser
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No user configured for signature"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|CallbackHandler
name|callbackHandler
init|=
name|getCallbackHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|callbackHandler
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No CallbackHandler configured to supply a password for signature"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
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
name|signatureUser
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
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No issuer certs were found to sign the request using name: "
operator|+
name|signatureUser
argument_list|)
throw|;
block|}
comment|// Get the password
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|signatureUser
argument_list|,
name|WSPasswordCallback
operator|.
name|SIGNATURE
argument_list|)
block|}
decl_stmt|;
name|callbackHandler
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
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
comment|// Get the private key
name|PrivateKey
name|privateKey
init|=
name|crypto
operator|.
name|getPrivateKey
argument_list|(
name|signatureUser
argument_list|,
name|password
argument_list|)
decl_stmt|;
if|if
condition|(
name|addEndpointAddressToContext
condition|)
block|{
name|Message
name|message
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|String
name|rawPath
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
literal|"http.base.path"
argument_list|)
decl_stmt|;
return|return
name|metadataWriter
operator|.
name|getMetaData
argument_list|(
name|rawPath
operator|+
name|serviceAddress
argument_list|,
name|rawPath
operator|+
name|assertionConsumerServiceAddress
argument_list|,
name|rawPath
operator|+
name|logoutServiceAddress
argument_list|,
name|privateKey
argument_list|,
name|issuerCerts
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|)
return|;
block|}
name|Document
name|metadata
init|=
name|metadataWriter
operator|.
name|getMetaData
argument_list|(
name|serviceAddress
argument_list|,
name|assertionConsumerServiceAddress
argument_list|,
name|logoutServiceAddress
argument_list|,
name|privateKey
argument_list|,
name|issuerCerts
index|[
literal|0
index|]
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Clean the private key from memory when we're done
try|try
block|{
name|privateKey
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DestroyFailedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|metadata
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
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|reportError
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
name|errorMsg
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
name|code
argument_list|,
name|BUNDLE
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warning
argument_list|(
name|errorMsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceAddress
parameter_list|()
block|{
return|return
name|serviceAddress
return|;
block|}
specifier|public
name|void
name|setServiceAddress
parameter_list|(
name|String
name|serviceAddress
parameter_list|)
block|{
name|this
operator|.
name|serviceAddress
operator|=
name|serviceAddress
expr_stmt|;
block|}
specifier|public
name|String
name|getLogoutServiceAddress
parameter_list|()
block|{
return|return
name|logoutServiceAddress
return|;
block|}
specifier|public
name|void
name|setLogoutServiceAddress
parameter_list|(
name|String
name|logoutServiceAddress
parameter_list|)
block|{
name|this
operator|.
name|logoutServiceAddress
operator|=
name|logoutServiceAddress
expr_stmt|;
block|}
specifier|public
name|void
name|setAddEndpointAddressToContext
parameter_list|(
name|boolean
name|add
parameter_list|)
block|{
name|addEndpointAddressToContext
operator|=
name|add
expr_stmt|;
block|}
specifier|public
name|String
name|getAssertionConsumerServiceAddress
parameter_list|()
block|{
return|return
name|assertionConsumerServiceAddress
return|;
block|}
specifier|public
name|void
name|setAssertionConsumerServiceAddress
parameter_list|(
name|String
name|assertionConsumerServiceAddress
parameter_list|)
block|{
name|this
operator|.
name|assertionConsumerServiceAddress
operator|=
name|assertionConsumerServiceAddress
expr_stmt|;
block|}
block|}
end_class

end_unit

