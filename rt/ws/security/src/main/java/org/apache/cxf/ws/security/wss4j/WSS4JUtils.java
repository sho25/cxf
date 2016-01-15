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
name|ws
operator|.
name|security
operator|.
name|wss4j
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
name|security
operator|.
name|Key
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
name|Date
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
name|Map
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
name|binding
operator|.
name|soap
operator|.
name|SoapFault
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|binding
operator|.
name|soap
operator|.
name|SoapVersion
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
name|endpoint
operator|.
name|Endpoint
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|cache
operator|.
name|CXFEHCacheReplayCache
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
name|tokenstore
operator|.
name|SecurityToken
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
name|tokenstore
operator|.
name|TokenStoreUtils
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
name|cache
operator|.
name|ReplayCache
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
name|cache
operator|.
name|ReplayCacheFactory
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
name|crypto
operator|.
name|JasyptPasswordEncryptor
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
name|PasswordEncryptor
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
name|Loader
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
name|WSConstants
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
name|engine
operator|.
name|WSSecurityEngineResult
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
name|handler
operator|.
name|WSHandlerResult
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
name|stax
operator|.
name|ext
operator|.
name|WSSConstants
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
name|stax
operator|.
name|securityToken
operator|.
name|WSSecurityTokenConstants
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
name|exceptions
operator|.
name|XMLSecurityException
import|;
end_import

begin_comment
comment|/**  * Some common functionality that can be shared between the WSS4JInInterceptor and the  * UsernameTokenInterceptor.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WSS4JUtils
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
name|WSS4JUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|WSS4JUtils
parameter_list|()
block|{
comment|// complete
block|}
comment|/**      * Get a ReplayCache instance. It first checks to see whether caching has been explicitly       * enabled or disabled via the booleanKey argument. If it has been set to false then no      * replay caching is done (for this booleanKey). If it has not been specified, then caching      * is enabled only if we are not the initiator of the exchange. If it has been specified, then      * caching is enabled.      *       * It tries to get an instance of ReplayCache via the instanceKey argument from a       * contextual property, and failing that the message exchange. If it can't find any, then it      * defaults to using an EH-Cache instance and stores that on the message exchange.      */
specifier|public
specifier|static
name|ReplayCache
name|getReplayCache
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
name|booleanKey
parameter_list|,
name|String
name|instanceKey
parameter_list|)
block|{
name|boolean
name|specified
init|=
literal|false
decl_stmt|;
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|booleanKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|specified
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|specified
operator|&&
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Endpoint
name|ep
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|ep
operator|!=
literal|null
operator|&&
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|EndpointInfo
name|info
init|=
name|ep
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|ReplayCache
name|replayCache
init|=
operator|(
name|ReplayCache
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|instanceKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|replayCache
operator|==
literal|null
condition|)
block|{
name|replayCache
operator|=
operator|(
name|ReplayCache
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|instanceKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|replayCache
operator|==
literal|null
condition|)
block|{
name|String
name|cacheKey
init|=
name|instanceKey
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|hashcode
init|=
name|info
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|hashcode
operator|<
literal|0
condition|)
block|{
name|cacheKey
operator|+=
name|hashcode
expr_stmt|;
block|}
else|else
block|{
name|cacheKey
operator|+=
literal|"-"
operator|+
name|hashcode
expr_stmt|;
block|}
block|}
name|URL
name|configFile
init|=
name|SecurityUtils
operator|.
name|getConfigFileURL
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|CACHE_CONFIG_FILE
argument_list|,
literal|"cxf-ehcache.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ReplayCacheFactory
operator|.
name|isEhCacheInstalled
argument_list|()
condition|)
block|{
name|Bus
name|bus
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getBus
argument_list|()
decl_stmt|;
name|replayCache
operator|=
operator|new
name|CXFEHCacheReplayCache
argument_list|(
name|cacheKey
argument_list|,
name|bus
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ReplayCacheFactory
name|replayCacheFactory
init|=
name|ReplayCacheFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|replayCache
operator|=
name|replayCacheFactory
operator|.
name|newReplayCache
argument_list|(
name|cacheKey
argument_list|,
name|configFile
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setProperty
argument_list|(
name|instanceKey
argument_list|,
name|replayCache
argument_list|)
expr_stmt|;
block|}
return|return
name|replayCache
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|parseAndStoreStreamingSecurityToken
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|securityToken
operator|.
name|SecurityToken
name|securityToken
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|XMLSecurityException
block|{
if|if
condition|(
name|securityToken
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SecurityToken
name|existingToken
init|=
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|getToken
argument_list|(
name|securityToken
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingToken
operator|==
literal|null
operator|||
name|existingToken
operator|.
name|isExpired
argument_list|()
condition|)
block|{
name|Date
name|created
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|expires
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|expires
operator|.
name|setTime
argument_list|(
name|created
operator|.
name|getTime
argument_list|()
operator|+
literal|300000
argument_list|)
expr_stmt|;
name|SecurityToken
name|cachedTok
init|=
operator|new
name|SecurityToken
argument_list|(
name|securityToken
operator|.
name|getId
argument_list|()
argument_list|,
name|created
argument_list|,
name|expires
argument_list|)
decl_stmt|;
name|cachedTok
operator|.
name|setSHA1
argument_list|(
name|securityToken
operator|.
name|getSha1Identifier
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|EncryptedKeyToken
condition|)
block|{
name|cachedTok
operator|.
name|setTokenType
argument_list|(
name|WSSConstants
operator|.
name|NS_WSS_ENC_KEY_VALUE_TYPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|KERBEROS_TOKEN
condition|)
block|{
name|cachedTok
operator|.
name|setTokenType
argument_list|(
name|WSSConstants
operator|.
name|NS_GSS_KERBEROS5_AP_REQ
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|SAML_11_TOKEN
condition|)
block|{
name|cachedTok
operator|.
name|setTokenType
argument_list|(
name|WSSConstants
operator|.
name|NS_SAML11_TOKEN_PROFILE_TYPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|SAML_20_TOKEN
condition|)
block|{
name|cachedTok
operator|.
name|setTokenType
argument_list|(
name|WSSConstants
operator|.
name|NS_SAML20_TOKEN_PROFILE_TYPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|SECURE_CONVERSATION_TOKEN
operator|||
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|SECURITY_CONTEXT_TOKEN
condition|)
block|{
name|cachedTok
operator|.
name|setTokenType
argument_list|(
name|WSSConstants
operator|.
name|NS_WSC_05_02
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Key
argument_list|>
name|entry
range|:
name|securityToken
operator|.
name|getSecretKey
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cachedTok
operator|.
name|setKey
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|SecretKey
condition|)
block|{
name|cachedTok
operator|.
name|setSecret
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|add
argument_list|(
name|cachedTok
argument_list|)
expr_stmt|;
return|return
name|cachedTok
operator|.
name|getId
argument_list|()
return|;
block|}
return|return
name|existingToken
operator|.
name|getId
argument_list|()
return|;
block|}
comment|/**      * Create a SoapFault from a WSSecurityException, following the SOAP Message Security      * 1.1 specification, chapter 12 "Error Handling".      *       * When the Soap version is 1.1 then set the Fault/Code/Value from the fault code      * specified in the WSSecurityException (if it exists).      *       * Otherwise set the Fault/Code/Value to env:Sender and the Fault/Code/Subcode/Value      * as the fault code from the WSSecurityException.      */
specifier|public
specifier|static
name|SoapFault
name|createSoapFault
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|SoapVersion
name|version
parameter_list|,
name|WSSecurityException
name|e
parameter_list|)
block|{
name|SoapFault
name|fault
decl_stmt|;
name|String
name|errorMessage
init|=
literal|null
decl_stmt|;
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
name|faultCode
init|=
literal|null
decl_stmt|;
name|boolean
name|returnSecurityError
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|RETURN_SECURITY_ERROR
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnSecurityError
operator|||
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|errorMessage
operator|=
name|e
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|faultCode
operator|=
name|e
operator|.
name|getFaultCode
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|errorMessage
operator|=
name|e
operator|.
name|getSafeExceptionMessage
argument_list|()
expr_stmt|;
name|faultCode
operator|=
name|e
operator|.
name|getSafeFaultCode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|==
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|errorMessage
argument_list|,
name|e
argument_list|,
name|faultCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fault
operator|=
operator|new
name|SoapFault
argument_list|(
name|errorMessage
argument_list|,
name|e
argument_list|,
name|version
operator|.
name|getSender
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|.
name|getVersion
argument_list|()
operator|!=
literal|1.1
operator|&&
name|faultCode
operator|!=
literal|null
condition|)
block|{
name|fault
operator|.
name|setSubCode
argument_list|(
name|faultCode
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fault
return|;
block|}
specifier|public
specifier|static
name|Properties
name|getProps
parameter_list|(
name|Object
name|o
parameter_list|,
name|URL
name|propsURL
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
name|propsURL
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
try|try
init|(
name|InputStream
name|ins
init|=
name|propsURL
operator|.
name|openStream
argument_list|()
init|)
block|{
name|properties
operator|.
name|load
argument_list|(
name|ins
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
specifier|public
specifier|static
name|PasswordEncryptor
name|getPasswordEncryptor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PasswordEncryptor
name|passwordEncryptor
init|=
operator|(
name|PasswordEncryptor
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|PASSWORD_ENCRYPTOR_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|passwordEncryptor
operator|!=
literal|null
condition|)
block|{
return|return
name|passwordEncryptor
return|;
block|}
name|Object
name|o
init|=
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
name|message
argument_list|)
decl_stmt|;
try|try
block|{
name|CallbackHandler
name|callbackHandler
init|=
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|callbackHandler
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|JasyptPasswordEncryptor
argument_list|(
name|callbackHandler
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|Crypto
name|loadCryptoFromPropertiesFile
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|propFilename
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|,
name|PasswordEncryptor
name|passwordEncryptor
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
name|URL
name|url
init|=
name|SecurityUtils
operator|.
name|loadResource
argument_list|(
name|message
argument_list|,
name|propFilename
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
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
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
name|props
operator|.
name|load
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|,
name|classLoader
argument_list|,
name|passwordEncryptor
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|propFilename
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Crypto
name|getEncryptionCrypto
parameter_list|(
name|Object
name|e
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|PasswordEncryptor
name|passwordEncryptor
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Crypto
name|encrCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|Crypto
condition|)
block|{
name|encrCrypto
operator|=
operator|(
name|Crypto
operator|)
name|e
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|URL
name|propsURL
init|=
name|SecurityUtils
operator|.
name|loadResource
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
name|WSS4JUtils
operator|.
name|getProps
argument_list|(
name|e
argument_list|,
name|propsURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot find Crypto Encryption properties: "
operator|+
name|e
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|(
literal|"Cannot find Crypto Encryption properties: "
operator|+
name|e
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|encrCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|,
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CryptoFactory
operator|.
name|class
argument_list|)
argument_list|,
name|passwordEncryptor
argument_list|)
expr_stmt|;
name|EndpointInfo
name|info
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|info
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|,
name|encrCrypto
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|encrCrypto
return|;
block|}
specifier|public
specifier|static
name|Crypto
name|getSignatureCrypto
parameter_list|(
name|Object
name|s
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|PasswordEncryptor
name|passwordEncryptor
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|s
operator|instanceof
name|Crypto
condition|)
block|{
name|signCrypto
operator|=
operator|(
name|Crypto
operator|)
name|s
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|URL
name|propsURL
init|=
name|SecurityUtils
operator|.
name|loadResource
argument_list|(
name|message
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
name|WSS4JUtils
operator|.
name|getProps
argument_list|(
name|s
argument_list|,
name|propsURL
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Cannot find Crypto Signature properties: "
operator|+
name|s
argument_list|)
expr_stmt|;
name|Exception
name|ex
init|=
operator|new
name|Exception
argument_list|(
literal|"Cannot find Crypto Signature properties: "
operator|+
name|s
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|signCrypto
operator|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|props
argument_list|,
name|Loader
operator|.
name|getClassLoader
argument_list|(
name|CryptoFactory
operator|.
name|class
argument_list|)
argument_list|,
name|passwordEncryptor
argument_list|)
expr_stmt|;
name|EndpointInfo
name|info
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|info
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|,
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|signCrypto
return|;
block|}
comment|/**      * Get the certificate that was used to sign the request      */
specifier|public
specifier|static
name|X509Certificate
name|getReqSigCert
parameter_list|(
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|results
parameter_list|)
block|{
if|if
condition|(
name|results
operator|==
literal|null
operator|||
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|WSHandlerResult
name|rResult
range|:
name|results
control|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
name|rResult
operator|.
name|getActionResults
argument_list|()
operator|.
name|get
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|)
decl_stmt|;
if|if
condition|(
name|signedResults
operator|!=
literal|null
operator|&&
operator|!
name|signedResults
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|signedResult
range|:
name|signedResults
control|)
block|{
if|if
condition|(
name|signedResult
operator|.
name|containsKey
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATE
argument_list|)
condition|)
block|{
return|return
operator|(
name|X509Certificate
operator|)
name|signedResult
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATE
argument_list|)
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

