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
name|realm
package|;
end_package

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
name|rt
operator|.
name|security
operator|.
name|utils
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
name|sts
operator|.
name|SignatureProperties
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
name|sts
operator|.
name|provider
operator|.
name|STSException
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
name|CryptoFactory
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

begin_comment
comment|/**  * This class defines some properties that are associated with a realm for the SAMLTokenProvider and  * SAMLTokenValidator.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLRealm
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
name|SAMLRealm
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|String
name|signatureAlias
decl_stmt|;
specifier|private
name|Crypto
name|signatureCrypto
decl_stmt|;
specifier|private
name|SignatureProperties
name|signatureProperties
decl_stmt|;
specifier|private
name|String
name|signaturePropertiesFile
decl_stmt|;
specifier|private
name|String
name|callbackHandlerClass
decl_stmt|;
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
comment|/**      * Get the issuer of this SAML realm      * @return the issuer of this SAML realm      */
specifier|public
name|String
name|getIssuer
parameter_list|()
block|{
return|return
name|issuer
return|;
block|}
comment|/**      * Set the issuer of this SAML realm      * @param issuer the issuer of this SAML realm      */
specifier|public
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
block|{
name|this
operator|.
name|issuer
operator|=
name|issuer
expr_stmt|;
block|}
comment|/**      * Get the signature alias to use for this SAML realm      * @return the signature alias to use for this SAML realm      */
specifier|public
name|String
name|getSignatureAlias
parameter_list|()
block|{
return|return
name|signatureAlias
return|;
block|}
comment|/**      * Set the signature alias to use for this SAML realm      * @param signatureAlias the signature alias to use for this SAML realm      */
specifier|public
name|void
name|setSignatureAlias
parameter_list|(
name|String
name|signatureAlias
parameter_list|)
block|{
name|this
operator|.
name|signatureAlias
operator|=
name|signatureAlias
expr_stmt|;
block|}
comment|/**      * Set the signature Crypto object      * @param signatureCrypto the signature Crypto object      */
specifier|public
name|void
name|setSignatureCrypto
parameter_list|(
name|Crypto
name|signatureCrypto
parameter_list|)
block|{
name|this
operator|.
name|signatureCrypto
operator|=
name|signatureCrypto
expr_stmt|;
block|}
comment|/**      * Set the String corresponding to the signature Properties class      * @param signaturePropertiesFile the String corresponding to the signature properties file      */
specifier|public
name|void
name|setSignaturePropertiesFile
parameter_list|(
name|String
name|signaturePropertiesFile
parameter_list|)
block|{
name|this
operator|.
name|signaturePropertiesFile
operator|=
name|signaturePropertiesFile
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
literal|"Setting signature properties: "
operator|+
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the SignatureProperties to use.      * @param signatureProperties the SignatureProperties to use.      */
specifier|public
name|void
name|setSignatureProperties
parameter_list|(
name|SignatureProperties
name|signatureProperties
parameter_list|)
block|{
name|this
operator|.
name|signatureProperties
operator|=
name|signatureProperties
expr_stmt|;
block|}
comment|/**      * Get the SignatureProperties to use.      * @return the SignatureProperties to use.      */
specifier|public
name|SignatureProperties
name|getSignatureProperties
parameter_list|()
block|{
return|return
name|signatureProperties
return|;
block|}
comment|/**      * Get the signature Crypto object      * @return the signature Crypto object      */
specifier|public
name|Crypto
name|getSignatureCrypto
parameter_list|()
block|{
if|if
condition|(
name|signatureCrypto
operator|==
literal|null
operator|&&
name|signaturePropertiesFile
operator|!=
literal|null
condition|)
block|{
name|Properties
name|sigProperties
init|=
name|SecurityUtils
operator|.
name|loadProperties
argument_list|(
name|signaturePropertiesFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigProperties
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot load signature properties using: "
operator|+
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Configuration error: cannot load signature properties"
argument_list|)
throw|;
block|}
try|try
block|{
name|signatureCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|sigProperties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Error in loading the signature Crypto object: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|signatureCrypto
return|;
block|}
comment|/**      * Set the CallbackHandler object.       * @param callbackHandler the CallbackHandler object.       */
specifier|public
name|void
name|setCallbackHandler
parameter_list|(
name|CallbackHandler
name|callbackHandler
parameter_list|)
block|{
name|this
operator|.
name|callbackHandler
operator|=
name|callbackHandler
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
literal|"Setting callbackHandler: "
operator|+
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the String corresponding to the CallbackHandler class.       * @param callbackHandlerClass the String corresponding to the CallbackHandler class.       */
specifier|public
name|void
name|setCallbackHandlerClass
parameter_list|(
name|String
name|callbackHandlerClass
parameter_list|)
block|{
name|this
operator|.
name|callbackHandlerClass
operator|=
name|callbackHandlerClass
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
literal|"Setting callbackHandlerClass: "
operator|+
name|callbackHandlerClass
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the CallbackHandler object.      * @return the CallbackHandler object.      */
specifier|public
name|CallbackHandler
name|getCallbackHandler
parameter_list|()
block|{
if|if
condition|(
name|callbackHandler
operator|==
literal|null
operator|&&
name|callbackHandlerClass
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|callbackHandler
operator|=
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|callbackHandlerClass
argument_list|)
expr_stmt|;
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
literal|"Cannot load CallbackHandler using: "
operator|+
name|callbackHandlerClass
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Configuration error: cannot load callback handler"
argument_list|)
throw|;
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
name|fine
argument_list|(
literal|"Error in loading the callback handler object: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|callbackHandler
return|;
block|}
block|}
end_class

end_unit

