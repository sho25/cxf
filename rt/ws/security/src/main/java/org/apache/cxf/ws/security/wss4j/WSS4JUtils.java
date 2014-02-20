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
name|math
operator|.
name|BigInteger
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
name|util
operator|.
name|Date
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
name|TokenStore
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
name|TokenStoreFactory
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
name|Base64
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
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
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
name|hashCode
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
name|cacheKey
operator|+=
literal|"-"
operator|+
name|Base64
operator|.
name|encode
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
operator|(
name|long
operator|)
name|hashCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|URL
name|configFile
init|=
name|getConfigFileURL
argument_list|(
name|message
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
specifier|private
specifier|static
name|URL
name|getConfigFileURL
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CACHE_CONFIG_FILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
name|o
operator|=
literal|"cxf-ehcache.xml"
expr_stmt|;
block|}
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
name|ResourceManager
name|rm
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
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
name|ReplayCacheFactory
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
return|return
name|url
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Do nothing
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
return|return
operator|(
name|URL
operator|)
name|o
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|TokenStore
name|getTokenStore
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|getTokenStore
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|TokenStore
name|getTokenStore
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|EndpointInfo
name|info
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|TokenStore
name|tokenStore
init|=
operator|(
name|TokenStore
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenStore
operator|==
literal|null
condition|)
block|{
name|tokenStore
operator|=
operator|(
name|TokenStore
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|create
operator|&&
name|tokenStore
operator|==
literal|null
condition|)
block|{
name|TokenStoreFactory
name|tokenStoreFactory
init|=
name|TokenStoreFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|String
name|cacheKey
init|=
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
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
name|hashCode
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
name|cacheKey
operator|+=
literal|"-"
operator|+
name|Base64
operator|.
name|encode
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
operator|(
name|long
operator|)
name|hashCode
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|tokenStore
operator|=
name|tokenStoreFactory
operator|.
name|newTokenStore
argument_list|(
name|cacheKey
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|info
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|,
name|tokenStore
argument_list|)
expr_stmt|;
block|}
return|return
name|tokenStore
return|;
block|}
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
name|KerberosToken
condition|)
block|{
name|cachedTok
operator|.
name|setTokenType
argument_list|(
name|WSSConstants
operator|.
name|NS_GSS_Kerberos5_AP_REQ
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
name|Saml11Token
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
name|Saml20Token
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
name|SecureConversationToken
operator|||
name|securityToken
operator|.
name|getTokenType
argument_list|()
operator|==
name|WSSecurityTokenConstants
operator|.
name|SecurityContextToken
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
name|String
name|key
range|:
name|securityToken
operator|.
name|getSecretKey
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Key
name|keyObject
init|=
name|securityToken
operator|.
name|getSecretKey
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyObject
operator|!=
literal|null
condition|)
block|{
name|cachedTok
operator|.
name|setKey
argument_list|(
name|keyObject
argument_list|)
expr_stmt|;
if|if
condition|(
name|keyObject
operator|instanceof
name|SecretKey
condition|)
block|{
name|cachedTok
operator|.
name|setSecret
argument_list|(
name|keyObject
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
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
block|}
end_class

end_unit

