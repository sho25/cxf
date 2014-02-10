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
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Callback
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
name|UnsupportedCallbackException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|i18n
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
name|interceptor
operator|.
name|StaxInInterceptor
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
name|Phase
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
name|wss4j
operator|.
name|common
operator|.
name|ConfigurationConstants
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
name|WSSPolicyException
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
name|wss4j
operator|.
name|stax
operator|.
name|ConfigurationConverter
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
name|WSSec
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
name|InboundWSSec
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
name|ext
operator|.
name|WSSSecurityProperties
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
name|validate
operator|.
name|Validator
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
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEvent
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
name|stax
operator|.
name|securityEvent
operator|.
name|SecurityEventListener
import|;
end_import

begin_class
specifier|public
class|class
name|WSS4JStaxInInterceptor
extends|extends
name|AbstractWSS4JStaxInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SECURITY_PROCESSED
init|=
name|WSS4JStaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".DONE"
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
name|WSS4JStaxInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|WSS4JStaxInInterceptor
parameter_list|(
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
block|{
name|super
argument_list|(
name|securityProperties
argument_list|)
expr_stmt|;
name|setPhase
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|StaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSS4JStaxInInterceptor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
name|super
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|setPhase
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|StaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSS4JStaxInInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setPhase
argument_list|(
name|Phase
operator|.
name|POST_STREAM
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
argument_list|(
name|StaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|boolean
name|isGET
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|String
name|method
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|SoapMessage
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
decl_stmt|;
return|return
literal|"GET"
operator|.
name|equals
argument_list|(
name|method
argument_list|)
operator|&&
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|soapMessage
operator|.
name|containsKey
argument_list|(
name|SECURITY_PROCESSED
argument_list|)
operator|||
name|isGET
argument_list|(
name|soapMessage
argument_list|)
condition|)
block|{
return|return;
block|}
name|XMLStreamReader
name|originalXmlStreamReader
init|=
name|soapMessage
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|newXmlStreamReader
decl_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxSecurityContextInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|requestSecurityEvents
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".out"
argument_list|)
decl_stmt|;
name|WSSSecurityProperties
name|secProps
init|=
name|createSecurityProperties
argument_list|()
decl_stmt|;
name|translateProperties
argument_list|(
name|soapMessage
argument_list|,
name|secProps
argument_list|)
expr_stmt|;
name|configureCallbackHandler
argument_list|(
name|soapMessage
argument_list|,
name|secProps
argument_list|)
expr_stmt|;
name|configureProperties
argument_list|(
name|soapMessage
argument_list|,
name|secProps
argument_list|)
expr_stmt|;
name|InboundWSSec
name|inboundWSSec
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|secProps
operator|.
name|getActions
argument_list|()
operator|!=
literal|null
operator|&&
name|secProps
operator|.
name|getActions
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxActionInInterceptor
argument_list|(
name|secProps
operator|.
name|getActions
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|secProps
operator|.
name|getAttachmentCallbackHandler
argument_list|()
operator|==
literal|null
condition|)
block|{
name|secProps
operator|.
name|setAttachmentCallbackHandler
argument_list|(
operator|new
name|AttachmentCallbackHandler
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|TokenStoreCallbackHandler
name|callbackHandler
init|=
operator|new
name|TokenStoreCallbackHandler
argument_list|(
name|secProps
operator|.
name|getCallbackHandler
argument_list|()
argument_list|,
name|WSS4JUtils
operator|.
name|getTokenStore
argument_list|(
name|soapMessage
argument_list|)
argument_list|)
decl_stmt|;
name|secProps
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
name|setTokenValidators
argument_list|(
name|secProps
argument_list|,
name|soapMessage
argument_list|)
expr_stmt|;
name|secProps
operator|.
name|setMsgContext
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SecurityEventListener
argument_list|>
name|securityEventListeners
init|=
name|configureSecurityEventListeners
argument_list|(
name|soapMessage
argument_list|,
name|secProps
argument_list|)
decl_stmt|;
name|inboundWSSec
operator|=
name|WSSec
operator|.
name|getInboundWSSec
argument_list|(
name|secProps
argument_list|)
expr_stmt|;
name|newXmlStreamReader
operator|=
name|inboundWSSec
operator|.
name|processInMessage
argument_list|(
name|originalXmlStreamReader
argument_list|,
name|requestSecurityEvents
argument_list|,
name|securityEventListeners
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|newXmlStreamReader
argument_list|)
expr_stmt|;
comment|// Warning: The exceptions which can occur here are not security relevant exceptions
comment|// but configuration-errors. To catch security relevant exceptions you have to catch
comment|// them e.g.in the FaultOutInterceptor. Why? Because we do streaming security. This
comment|// interceptor doesn't handle the ws-security stuff but just setup the relevant stuff
comment|// for it. Exceptions will be thrown as a wrapped XMLStreamException during further
comment|// processing in the WS-Stack.
name|soapMessage
operator|.
name|put
argument_list|(
name|SECURITY_PROCESSED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
throw|throw
name|createSoapFault
argument_list|(
name|soapMessage
operator|.
name|getVersion
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"STAX_EX"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|,
name|soapMessage
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|WSSPolicyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|,
name|soapMessage
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SoapFault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"STAX_EX"
argument_list|,
name|LOG
argument_list|)
argument_list|,
name|e
argument_list|,
name|soapMessage
operator|.
name|getVersion
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|SecurityEventListener
argument_list|>
name|configureSecurityEventListeners
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSPolicyException
block|{
specifier|final
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|incomingSecurityEventList
init|=
operator|new
name|LinkedList
argument_list|<
name|SecurityEvent
argument_list|>
argument_list|()
decl_stmt|;
name|SecurityEventListener
name|securityEventListener
init|=
operator|new
name|SecurityEventListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|registerSecurityEvent
parameter_list|(
name|SecurityEvent
name|securityEvent
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|incomingSecurityEventList
operator|.
name|add
argument_list|(
name|securityEvent
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".in"
argument_list|,
name|incomingSecurityEventList
argument_list|)
expr_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|SecurityEvent
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".in"
argument_list|,
name|incomingSecurityEventList
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|securityEventListener
argument_list|)
return|;
block|}
specifier|protected
name|void
name|configureProperties
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|XMLSecurityException
block|{
comment|// Configure replay caching
name|ReplayCache
name|nonceCache
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isNonceCacheRequired
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
condition|)
block|{
name|nonceCache
operator|=
name|WSS4JUtils
operator|.
name|getReplayCache
argument_list|(
name|msg
argument_list|,
name|SecurityConstants
operator|.
name|ENABLE_NONCE_CACHE
argument_list|,
name|SecurityConstants
operator|.
name|NONCE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nonceCache
operator|==
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setEnableNonceReplayCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|securityProperties
operator|.
name|setNonceReplayCache
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|securityProperties
operator|.
name|setEnableNonceReplayCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|securityProperties
operator|.
name|setNonceReplayCache
argument_list|(
name|nonceCache
argument_list|)
expr_stmt|;
block|}
name|ReplayCache
name|timestampCache
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isTimestampCacheRequired
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
condition|)
block|{
name|timestampCache
operator|=
name|WSS4JUtils
operator|.
name|getReplayCache
argument_list|(
name|msg
argument_list|,
name|SecurityConstants
operator|.
name|ENABLE_TIMESTAMP_CACHE
argument_list|,
name|SecurityConstants
operator|.
name|TIMESTAMP_CACHE_INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timestampCache
operator|==
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setEnableTimestampReplayCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|securityProperties
operator|.
name|setTimestampReplayCache
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|securityProperties
operator|.
name|setEnableTimestampReplayCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|securityProperties
operator|.
name|setTimestampReplayCache
argument_list|(
name|timestampCache
argument_list|)
expr_stmt|;
block|}
name|ReplayCache
name|samlCache
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isSamlCacheRequired
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
condition|)
block|{
name|samlCache
operator|=
name|WSS4JUtils
operator|.
name|getReplayCache
argument_list|(
name|msg
argument_list|,
name|SecurityConstants
operator|.
name|ENABLE_SAML_ONE_TIME_USE_CACHE
argument_list|,
name|SecurityConstants
operator|.
name|SAML_ONE_TIME_USE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|samlCache
operator|==
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setEnableSamlOneTimeUseReplayCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|securityProperties
operator|.
name|setSamlOneTimeUseReplayCache
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|securityProperties
operator|.
name|setEnableSamlOneTimeUseReplayCache
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|securityProperties
operator|.
name|setSamlOneTimeUseReplayCache
argument_list|(
name|samlCache
argument_list|)
expr_stmt|;
block|}
name|boolean
name|enableRevocation
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_REVOCATION
argument_list|)
argument_list|)
decl_stmt|;
name|securityProperties
operator|.
name|setEnableRevocation
argument_list|(
name|enableRevocation
argument_list|)
expr_stmt|;
comment|// Crypto loading only applies for Map
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|config
init|=
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|Crypto
name|sigVerCrypto
init|=
name|loadCrypto
argument_list|(
name|msg
argument_list|,
name|ConfigurationConstants
operator|.
name|SIG_VER_PROP_FILE
argument_list|,
name|ConfigurationConstants
operator|.
name|SIG_VER_PROP_REF_ID
argument_list|,
name|securityProperties
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigVerCrypto
operator|==
literal|null
condition|)
block|{
comment|// Fall back to using the Signature properties for verification
name|sigVerCrypto
operator|=
name|loadCrypto
argument_list|(
name|msg
argument_list|,
name|ConfigurationConstants
operator|.
name|SIG_PROP_FILE
argument_list|,
name|ConfigurationConstants
operator|.
name|SIG_PROP_REF_ID
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sigVerCrypto
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIG_VER_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|sigVerCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|sigVerCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|sigVerCrypto
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|decCrypto
init|=
name|loadCrypto
argument_list|(
name|msg
argument_list|,
name|ConfigurationConstants
operator|.
name|DEC_PROP_FILE
argument_list|,
name|ConfigurationConstants
operator|.
name|DEC_PROP_REF_ID
argument_list|,
name|securityProperties
argument_list|)
decl_stmt|;
if|if
condition|(
name|decCrypto
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|DEC_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|decCrypto
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|put
argument_list|(
literal|"RefId-"
operator|+
name|decCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|decCrypto
argument_list|)
expr_stmt|;
block|}
name|ConfigurationConverter
operator|.
name|parseCrypto
argument_list|(
name|config
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Is a Nonce Cache required, i.e. are we expecting a UsernameToken       */
specifier|protected
name|boolean
name|isNonceCacheRequired
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
block|{
if|if
condition|(
name|securityProperties
operator|!=
literal|null
operator|&&
name|securityProperties
operator|.
name|getActions
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSConstants
operator|.
name|Action
name|action
range|:
name|securityProperties
operator|.
name|getActions
argument_list|()
control|)
block|{
if|if
condition|(
name|action
operator|==
name|WSSConstants
operator|.
name|USERNAMETOKEN
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is a Timestamp cache required, i.e. are we expecting a Timestamp       */
specifier|protected
name|boolean
name|isTimestampCacheRequired
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
block|{
if|if
condition|(
name|securityProperties
operator|!=
literal|null
operator|&&
name|securityProperties
operator|.
name|getActions
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSConstants
operator|.
name|Action
name|action
range|:
name|securityProperties
operator|.
name|getActions
argument_list|()
control|)
block|{
if|if
condition|(
name|action
operator|==
name|WSSConstants
operator|.
name|TIMESTAMP
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is a SAML Cache required, i.e. are we expecting a SAML Token       */
specifier|protected
name|boolean
name|isSamlCacheRequired
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
block|{
if|if
condition|(
name|securityProperties
operator|!=
literal|null
operator|&&
name|securityProperties
operator|.
name|getActions
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSSConstants
operator|.
name|Action
name|action
range|:
name|securityProperties
operator|.
name|getActions
argument_list|()
control|)
block|{
if|if
condition|(
name|action
operator|==
name|WSSConstants
operator|.
name|SAML_TOKEN_UNSIGNED
operator|||
name|action
operator|==
name|WSSConstants
operator|.
name|SAML_TOKEN_SIGNED
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Create a SoapFault from a WSSecurityException, following the SOAP Message Security      * 1.1 specification, chapter 12 "Error Handling".      *       * When the Soap version is 1.1 then set the Fault/Code/Value from the fault code      * specified in the WSSecurityException (if it exists).      *       * Otherwise set the Fault/Code/Value to env:Sender and the Fault/Code/Subcode/Value      * as the fault code from the WSSecurityException.      */
specifier|private
name|SoapFault
name|createSoapFault
parameter_list|(
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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
name|faultCode
init|=
name|e
operator|.
name|getFaultCode
argument_list|()
decl_stmt|;
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
name|e
operator|.
name|getMessage
argument_list|()
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
name|e
operator|.
name|getMessage
argument_list|()
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
specifier|private
name|void
name|setTokenValidators
parameter_list|(
name|WSSSecurityProperties
name|properties
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Validator
name|validator
init|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|SAML1_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_saml_Assertion
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
name|validator
operator|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|SAML2_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_saml2_Assertion
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
name|validator
operator|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsse_UsernameToken
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
name|validator
operator|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_dsig_Signature
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
name|validator
operator|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|TIMESTAMP_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsu_Timestamp
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
name|validator
operator|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|BST_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsse_BinarySecurityToken
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
name|validator
operator|=
name|loadValidator
argument_list|(
name|SecurityConstants
operator|.
name|SCT_TOKEN_VALIDATOR
argument_list|,
name|message
argument_list|)
expr_stmt|;
if|if
condition|(
name|validator
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsc0502_SecurityContextToken
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addValidator
argument_list|(
name|WSSConstants
operator|.
name|TAG_wsc0512_SecurityContextToken
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Validator
name|loadValidator
parameter_list|(
name|String
name|validatorKey
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|validatorKey
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|o
operator|instanceof
name|Validator
condition|)
block|{
return|return
operator|(
name|Validator
operator|)
name|o
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Class
condition|)
block|{
return|return
call|(
name|Validator
call|)
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|o
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
name|Validator
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|,
name|WSS4JStaxInInterceptor
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
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
literal|"Cannot load Validator: "
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|t
parameter_list|)
block|{
throw|throw
name|t
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
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
return|return
literal|null
return|;
block|}
specifier|private
class|class
name|TokenStoreCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|CallbackHandler
name|internal
decl_stmt|;
specifier|private
name|TokenStore
name|store
decl_stmt|;
specifier|public
name|TokenStoreCallbackHandler
parameter_list|(
name|CallbackHandler
name|in
parameter_list|,
name|TokenStore
name|st
parameter_list|)
block|{
name|internal
operator|=
name|in
expr_stmt|;
name|store
operator|=
name|st
expr_stmt|;
block|}
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|WSPasswordCallback
condition|)
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|String
name|id
init|=
name|pc
operator|.
name|getIdentifier
argument_list|()
decl_stmt|;
name|SecurityToken
name|tok
init|=
name|store
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|tok
operator|!=
literal|null
condition|)
block|{
name|pc
operator|.
name|setKey
argument_list|(
name|tok
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
name|pc
operator|.
name|setKey
argument_list|(
name|tok
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|pc
operator|.
name|setCustomToken
argument_list|(
name|tok
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
if|if
condition|(
name|internal
operator|!=
literal|null
condition|)
block|{
name|internal
operator|.
name|handle
argument_list|(
name|callbacks
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

