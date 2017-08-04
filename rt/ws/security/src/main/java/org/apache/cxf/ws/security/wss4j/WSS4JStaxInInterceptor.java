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
name|security
operator|.
name|Provider
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
name|xml
operator|.
name|stream
operator|.
name|XMLStreamConstants
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|util
operator|.
name|StreamReaderDelegate
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
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|interceptor
operator|.
name|StartBodyInterceptor
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
name|rt
operator|.
name|security
operator|.
name|saml
operator|.
name|utils
operator|.
name|SAMLUtils
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
name|crypto
operator|.
name|ThreadLocalSecurityProvider
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
name|securityEvent
operator|.
name|WSSecurityEventConstants
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
name|setup
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
name|setup
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
name|setup
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
name|AbstractSecuredElementSecurityEvent
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
name|TokenSecurityEvent
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
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|StaxStartBodyInterceptor
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|final
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
name|TokenStoreUtils
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
specifier|final
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
name|boolean
name|returnSecurityError
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|soapMessage
argument_list|,
name|SecurityConstants
operator|.
name|RETURN_SECURITY_ERROR
argument_list|,
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|InboundWSSec
name|inboundWSSec
init|=
name|WSSec
operator|.
name|getInboundWSSec
argument_list|(
name|secProps
argument_list|,
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|soapMessage
argument_list|)
argument_list|,
name|returnSecurityError
argument_list|)
decl_stmt|;
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
specifier|final
name|Object
name|provider
init|=
name|soapMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|!=
literal|null
operator|&&
name|ThreadLocalSecurityProvider
operator|.
name|isInstalled
argument_list|()
condition|)
block|{
name|newXmlStreamReader
operator|=
operator|new
name|StreamReaderDelegate
argument_list|(
name|newXmlStreamReader
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|next
parameter_list|()
throws|throws
name|XMLStreamException
block|{
try|try
block|{
name|ThreadLocalSecurityProvider
operator|.
name|setProvider
argument_list|(
operator|(
name|Provider
operator|)
name|provider
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|next
argument_list|()
return|;
block|}
finally|finally
block|{
name|ThreadLocalSecurityProvider
operator|.
name|unsetProvider
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
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
name|WSS4JUtils
operator|.
name|createSoapFault
argument_list|(
name|soapMessage
argument_list|,
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
argument_list|<>
argument_list|()
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
specifier|final
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
if|if
condition|(
name|securityEvent
operator|.
name|getSecurityEventType
argument_list|()
operator|==
name|WSSecurityEventConstants
operator|.
name|TIMESTAMP
operator|||
name|securityEvent
operator|.
name|getSecurityEventType
argument_list|()
operator|==
name|WSSecurityEventConstants
operator|.
name|SignatureValue
operator|||
name|securityEvent
operator|instanceof
name|TokenSecurityEvent
operator|||
name|securityEvent
operator|instanceof
name|AbstractSecuredElementSecurityEvent
condition|)
block|{
comment|// Store events required for the security context setup, or the crypto coverage checker
name|incomingSecurityEventList
operator|.
name|add
argument_list|(
name|securityEvent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
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
name|securityProperties
operator|.
name|setNonceReplayCache
argument_list|(
name|nonceCache
argument_list|)
expr_stmt|;
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
name|securityProperties
operator|.
name|setTimestampReplayCache
argument_list|(
name|timestampCache
argument_list|)
expr_stmt|;
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
name|securityProperties
operator|.
name|setSamlOneTimeUseReplayCache
argument_list|(
name|samlCache
argument_list|)
expr_stmt|;
name|boolean
name|enableRevocation
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_REVOCATION
argument_list|,
name|msg
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
operator|&&
operator|!
name|config
operator|.
name|isEmpty
argument_list|()
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
comment|// Add Audience Restrictions for SAML
name|securityProperties
operator|.
name|setAudienceRestrictions
argument_list|(
name|SAMLUtils
operator|.
name|getAudienceRestrictions
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Is a Nonce Cache required, i.e. are we expecting a UsernameToken      */
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
comment|/**      * Is a Timestamp cache required, i.e. are we expecting a Timestamp      */
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
comment|/**      * Is a SAML Cache required, i.e. are we expecting a SAML Token      */
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
name|TAG_SAML_ASSERTION
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
name|TAG_SAML2_ASSERTION
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
name|TAG_WSSE_USERNAME_TOKEN
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
name|TAG_WSU_TIMESTAMP
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
name|TAG_WSSE_BINARY_SECURITY_TOKEN
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
name|TAG_WSC0502_SCT
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
name|TAG_WSC0512_SCT
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
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
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
else|else
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
block|}
comment|/**      * This interceptor runs after the StartBodyInterceptor. It skips any white space after the SOAP:Body start tag,      * to make sure that the WSS4J OperationInputProcessor is triggered by the first SOAP Body child (if it is not,      * then WS-Security processing does not happen correctly).      */
specifier|private
class|class
name|StaxStartBodyInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
name|StaxStartBodyInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|READ
argument_list|)
expr_stmt|;
name|super
operator|.
name|addAfter
argument_list|(
name|StartBodyInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|StaxStartBodyInterceptor
parameter_list|(
name|String
name|phase
parameter_list|)
block|{
name|super
argument_list|(
name|phase
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
if|if
condition|(
name|isGET
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"StartBodyInterceptor skipped in HTTP GET method"
argument_list|)
expr_stmt|;
return|return;
block|}
name|XMLStreamReader
name|xmlReader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|i
init|=
name|xmlReader
operator|.
name|getEventType
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|==
name|XMLStreamConstants
operator|.
name|NAMESPACE
operator|||
name|i
operator|==
name|XMLStreamConstants
operator|.
name|ATTRIBUTE
operator|||
name|i
operator|==
name|XMLStreamConstants
operator|.
name|CHARACTERS
condition|)
block|{
name|i
operator|=
name|xmlReader
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
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
literal|"XML_STREAM_EXC"
argument_list|,
name|LOG
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|,
name|message
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
block|}
block|}
end_class

end_unit

