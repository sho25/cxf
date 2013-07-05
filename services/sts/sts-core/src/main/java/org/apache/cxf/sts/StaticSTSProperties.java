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
package|;
end_package

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
name|sts
operator|.
name|token
operator|.
name|realm
operator|.
name|Relationship
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
name|token
operator|.
name|realm
operator|.
name|RelationshipResolver
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
name|token
operator|.
name|realm
operator|.
name|SAMLRealmCodec
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
name|WSSConfig
import|;
end_import

begin_comment
comment|/**  * A static implementation of the STSPropertiesMBean.  */
end_comment

begin_class
specifier|public
class|class
name|StaticSTSProperties
implements|implements
name|STSPropertiesMBean
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
name|StaticSTSProperties
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
specifier|private
name|String
name|callbackHandlerClass
decl_stmt|;
specifier|private
name|Crypto
name|signatureCrypto
decl_stmt|;
specifier|private
name|Object
name|signatureCryptoProperties
decl_stmt|;
specifier|private
name|String
name|signatureUsername
decl_stmt|;
specifier|private
name|Crypto
name|encryptionCrypto
decl_stmt|;
specifier|private
name|Object
name|encryptionCryptoProperties
decl_stmt|;
specifier|private
name|String
name|encryptionUsername
decl_stmt|;
specifier|private
name|String
name|issuer
decl_stmt|;
specifier|private
name|SignatureProperties
name|signatureProperties
init|=
operator|new
name|SignatureProperties
argument_list|()
decl_stmt|;
specifier|private
name|EncryptionProperties
name|encryptionProperties
init|=
operator|new
name|EncryptionProperties
argument_list|()
decl_stmt|;
specifier|private
name|RealmParser
name|realmParser
decl_stmt|;
specifier|private
name|IdentityMapper
name|identityMapper
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Relationship
argument_list|>
name|relationships
decl_stmt|;
specifier|private
name|RelationshipResolver
name|relationshipResolver
decl_stmt|;
specifier|private
name|SAMLRealmCodec
name|samlRealmCodec
decl_stmt|;
comment|/**      * Load the CallbackHandler, Crypto objects, if necessary.      */
specifier|public
name|void
name|configureProperties
parameter_list|()
throws|throws
name|STSException
block|{
if|if
condition|(
name|signatureCrypto
operator|==
literal|null
operator|&&
name|signatureCryptoProperties
operator|!=
literal|null
condition|)
block|{
name|Properties
name|sigProperties
init|=
name|getProps
argument_list|(
name|signatureCryptoProperties
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
name|signatureCryptoProperties
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
if|if
condition|(
name|encryptionCrypto
operator|==
literal|null
operator|&&
name|encryptionCryptoProperties
operator|!=
literal|null
condition|)
block|{
name|Properties
name|encrProperties
init|=
name|getProps
argument_list|(
name|encryptionCryptoProperties
argument_list|)
decl_stmt|;
if|if
condition|(
name|encrProperties
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot load encryption properties using: "
operator|+
name|encryptionCryptoProperties
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|STSException
argument_list|(
literal|"Configuration error: cannot load encryption properties"
argument_list|)
throw|;
block|}
try|try
block|{
name|encryptionCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|encrProperties
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
literal|"Error in loading the encryption Crypto object: "
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
name|callbackHandler
operator|=
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
name|WSSConfig
operator|.
name|init
argument_list|()
expr_stmt|;
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
comment|/**      * Get the CallbackHandler object.      * @return the CallbackHandler object.      */
specifier|public
name|CallbackHandler
name|getCallbackHandler
parameter_list|()
block|{
return|return
name|callbackHandler
return|;
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
annotation|@
name|Deprecated
specifier|public
name|void
name|setSignaturePropertiesFile
parameter_list|(
name|String
name|signaturePropertiesFile
parameter_list|)
block|{
name|setSignatureCryptoProperties
argument_list|(
name|signaturePropertiesFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the Object corresponding to the signature Properties class. It can be a String      * corresponding to a filename, a Properties object, or a URL.      * @param signatureCryptoProperties the object corresponding to the signature properties      */
specifier|public
name|void
name|setSignatureCryptoProperties
parameter_list|(
name|Object
name|signatureCryptoProperties
parameter_list|)
block|{
name|this
operator|.
name|signatureCryptoProperties
operator|=
name|signatureCryptoProperties
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting signature crypto properties: "
operator|+
name|signatureCryptoProperties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the signature Crypto object      * @return the signature Crypto object      */
specifier|public
name|Crypto
name|getSignatureCrypto
parameter_list|()
block|{
return|return
name|signatureCrypto
return|;
block|}
comment|/**      * Set the username/alias to use to sign any issued tokens      * @param signatureUsername the username/alias to use to sign any issued tokens      */
specifier|public
name|void
name|setSignatureUsername
parameter_list|(
name|String
name|signatureUsername
parameter_list|)
block|{
name|this
operator|.
name|signatureUsername
operator|=
name|signatureUsername
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting signatureUsername: "
operator|+
name|signatureUsername
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the username/alias to use to sign any issued tokens      * @return the username/alias to use to sign any issued tokens      */
specifier|public
name|String
name|getSignatureUsername
parameter_list|()
block|{
return|return
name|signatureUsername
return|;
block|}
comment|/**      * Set the encryption Crypto object      * @param encryptionCrypto the encryption Crypto object      */
specifier|public
name|void
name|setEncryptionCrypto
parameter_list|(
name|Crypto
name|encryptionCrypto
parameter_list|)
block|{
name|this
operator|.
name|encryptionCrypto
operator|=
name|encryptionCrypto
expr_stmt|;
block|}
comment|/**      * Set the String corresponding to the encryption Properties class      * @param signaturePropertiesFile the String corresponding to the encryption properties file      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setEncryptionPropertiesFile
parameter_list|(
name|String
name|encryptionPropertiesFile
parameter_list|)
block|{
name|setEncryptionCryptoProperties
argument_list|(
name|encryptionPropertiesFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the Object corresponding to the encryption Properties class. It can be a String      * corresponding to a filename, a Properties object, or a URL.      * @param encryptionCryptoProperties the object corresponding to the encryption properties      */
specifier|public
name|void
name|setEncryptionCryptoProperties
parameter_list|(
name|Object
name|encryptionCryptoProperties
parameter_list|)
block|{
name|this
operator|.
name|encryptionCryptoProperties
operator|=
name|encryptionCryptoProperties
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting encryptionProperties: "
operator|+
name|encryptionCryptoProperties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the encryption Crypto object      * @return the encryption Crypto object      */
specifier|public
name|Crypto
name|getEncryptionCrypto
parameter_list|()
block|{
return|return
name|encryptionCrypto
return|;
block|}
comment|/**      * Set the username/alias to use to encrypt any issued tokens. This is a default value - it      * can be configured per Service in the ServiceMBean.      * @param encryptionUsername the username/alias to use to encrypt any issued tokens      */
specifier|public
name|void
name|setEncryptionUsername
parameter_list|(
name|String
name|encryptionUsername
parameter_list|)
block|{
name|this
operator|.
name|encryptionUsername
operator|=
name|encryptionUsername
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting encryptionUsername: "
operator|+
name|encryptionUsername
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the username/alias to use to encrypt any issued tokens. This is a default value - it      * can be configured per Service in the ServiceMBean      * @return the username/alias to use to encrypt any issued tokens      */
specifier|public
name|String
name|getEncryptionUsername
parameter_list|()
block|{
return|return
name|encryptionUsername
return|;
block|}
comment|/**      * Set the EncryptionProperties to use.      * @param encryptionProperties the EncryptionProperties to use.      */
specifier|public
name|void
name|setEncryptionProperties
parameter_list|(
name|EncryptionProperties
name|encryptionProperties
parameter_list|)
block|{
name|this
operator|.
name|encryptionProperties
operator|=
name|encryptionProperties
expr_stmt|;
block|}
comment|/**      * Get the EncryptionProperties to use.      * @return the EncryptionProperties to use.      */
specifier|public
name|EncryptionProperties
name|getEncryptionProperties
parameter_list|()
block|{
return|return
name|encryptionProperties
return|;
block|}
comment|/**      * Set the STS issuer name      * @param issuer the STS issuer name      */
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
name|LOG
operator|.
name|fine
argument_list|(
literal|"Setting issuer: "
operator|+
name|issuer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the STS issuer name      * @return the STS issuer name      */
specifier|public
name|String
name|getIssuer
parameter_list|()
block|{
return|return
name|issuer
return|;
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
comment|/**      * Set the RealmParser object to use.      * @param realmParser the RealmParser object to use.      */
specifier|public
name|void
name|setRealmParser
parameter_list|(
name|RealmParser
name|realmParser
parameter_list|)
block|{
name|this
operator|.
name|realmParser
operator|=
name|realmParser
expr_stmt|;
block|}
comment|/**      * Get the RealmParser object to use.      * @return the RealmParser object to use.      */
specifier|public
name|RealmParser
name|getRealmParser
parameter_list|()
block|{
return|return
name|realmParser
return|;
block|}
comment|/**      * Set the IdentityMapper object to use.      * @param identityMapper the IdentityMapper object to use.      */
specifier|public
name|void
name|setIdentityMapper
parameter_list|(
name|IdentityMapper
name|identityMapper
parameter_list|)
block|{
name|this
operator|.
name|identityMapper
operator|=
name|identityMapper
expr_stmt|;
block|}
comment|/**      * Get the IdentityMapper object to use.      * @return the IdentityMapper object to use.      */
specifier|public
name|IdentityMapper
name|getIdentityMapper
parameter_list|()
block|{
return|return
name|identityMapper
return|;
block|}
specifier|private
specifier|static
name|Properties
name|getProps
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|Properties
condition|)
block|{
name|properties
operator|=
operator|(
name|Properties
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
name|Bus
name|bus
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
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
name|url
operator|=
name|rm
operator|.
name|resolveResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|URL
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|StaticSTSProperties
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|InputStream
name|ins
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|URL
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
block|{
name|InputStream
name|ins
init|=
operator|(
operator|(
name|URL
operator|)
name|o
operator|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|properties
return|;
block|}
specifier|private
name|CallbackHandler
name|getCallbackHandler
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|CallbackHandler
name|handler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|CallbackHandler
condition|)
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
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
name|fine
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|handler
return|;
block|}
specifier|public
name|void
name|setRelationships
parameter_list|(
name|List
argument_list|<
name|Relationship
argument_list|>
name|relationships
parameter_list|)
block|{
name|this
operator|.
name|relationships
operator|=
name|relationships
expr_stmt|;
name|this
operator|.
name|relationshipResolver
operator|=
operator|new
name|RelationshipResolver
argument_list|(
name|this
operator|.
name|relationships
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Relationship
argument_list|>
name|getRelationships
parameter_list|()
block|{
return|return
name|relationships
return|;
block|}
specifier|public
name|RelationshipResolver
name|getRelationshipResolver
parameter_list|()
block|{
return|return
name|relationshipResolver
return|;
block|}
specifier|public
name|SAMLRealmCodec
name|getSamlRealmCodec
parameter_list|()
block|{
return|return
name|samlRealmCodec
return|;
block|}
specifier|public
name|void
name|setSamlRealmCodec
parameter_list|(
name|SAMLRealmCodec
name|samlRealmCodec
parameter_list|)
block|{
name|this
operator|.
name|samlRealmCodec
operator|=
name|samlRealmCodec
expr_stmt|;
block|}
block|}
end_class

end_unit

