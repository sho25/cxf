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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|XMLStreamWriter
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
name|interceptor
operator|.
name|AbstractOutDatabindingInterceptor
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
name|AttachmentOutInterceptor
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
name|StaxOutInterceptor
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
name|Exchange
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
name|AbstractPhaseInterceptor
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
name|policy
operator|.
name|AssertionInfoMap
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
name|policy
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
name|OutboundWSSec
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
name|impl
operator|.
name|OutboundSecurityContextImpl
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
name|securityToken
operator|.
name|OutboundSecurityToken
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
name|securityToken
operator|.
name|SecurityTokenProvider
import|;
end_import

begin_class
specifier|public
class|class
name|WSS4JStaxOutInterceptor
extends|extends
name|AbstractWSS4JStaxInterceptor
block|{
comment|/**      * Property name for a map of action IDs ({@link Integer}) to action      * class names. Values can be either {@link Class}) or Objects -    * implementing {@link Action}.      */
specifier|public
specifier|static
specifier|final
name|String
name|WSS4J_ACTION_MAP
init|=
literal|"wss4j.action.map"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_STREAM_HOLDER
init|=
name|WSS4JStaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".outputstream"
decl_stmt|;
specifier|private
name|WSS4JStaxOutInterceptorInternal
name|ending
decl_stmt|;
specifier|private
name|boolean
name|mtomEnabled
decl_stmt|;
specifier|public
name|WSS4JStaxOutInterceptor
parameter_list|(
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|setPhase
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ending
operator|=
name|createEndingInterceptor
argument_list|()
expr_stmt|;
name|setSecurityProperties
argument_list|(
name|securityProperties
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WSS4JStaxOutInterceptor
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
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|StaxOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ending
operator|=
name|createEndingInterceptor
argument_list|()
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAllowMTOM
parameter_list|()
block|{
return|return
name|mtomEnabled
return|;
block|}
comment|/**      * Enable or disable mtom with WS-Security.   By default MTOM is disabled as      * attachments would not get encrypted or be part of the signature.      * @param mtomEnabled      */
specifier|public
name|void
name|setAllowMTOM
parameter_list|(
name|boolean
name|allowMTOM
parameter_list|)
block|{
name|this
operator|.
name|mtomEnabled
operator|=
name|allowMTOM
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getProperty
parameter_list|(
name|Object
name|msgContext
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|super
operator|.
name|getProperty
argument_list|(
name|msgContext
argument_list|,
name|key
argument_list|)
return|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|mc
parameter_list|)
throws|throws
name|Fault
block|{
comment|//must turn off mtom when using WS-Sec so binary is inlined so it can
comment|//be properly signed/encrypted/etc...
if|if
condition|(
operator|!
name|mtomEnabled
condition|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|MTOM_ENABLED
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|OutputStream
name|os
init|=
name|mc
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|encoding
init|=
name|getEncoding
argument_list|(
name|mc
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|newXMLStreamWriter
decl_stmt|;
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
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
name|mc
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
literal|".in"
argument_list|)
decl_stmt|;
name|translateProperties
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|SecurityTokenProvider
argument_list|<
name|OutboundSecurityToken
argument_list|>
argument_list|>
name|outboundTokens
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SecurityTokenProvider
argument_list|<
name|OutboundSecurityToken
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|configureCallbackHandler
argument_list|(
name|mc
argument_list|)
expr_stmt|;
name|configureProperties
argument_list|(
name|mc
argument_list|,
name|outboundTokens
argument_list|)
expr_stmt|;
name|OutboundWSSec
name|outboundWSSec
init|=
literal|null
decl_stmt|;
name|WSSSecurityProperties
name|secProps
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getSecurityProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|secProps
operator|=
name|getSecurityProperties
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|secProps
operator|=
name|ConfigurationConverter
operator|.
name|convert
argument_list|(
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|secProps
operator|.
name|getOutAction
argument_list|()
operator|==
literal|null
operator|||
name|secProps
operator|.
name|getOutAction
argument_list|()
operator|.
name|length
operator|==
literal|0
operator|)
operator|&&
name|mc
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// If no actions configured (with SecurityPolicy) then return
return|return;
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
name|AttachmentOutCallbackHandler
argument_list|(
name|mc
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SecurityEventListener
name|securityEventListener
init|=
name|configureSecurityEventListener
argument_list|(
name|mc
argument_list|,
name|secProps
argument_list|)
decl_stmt|;
name|outboundWSSec
operator|=
name|WSSec
operator|.
name|getOutboundWSSec
argument_list|(
name|secProps
argument_list|)
expr_stmt|;
specifier|final
name|OutboundSecurityContextImpl
name|outboundSecurityContext
init|=
operator|new
name|OutboundSecurityContextImpl
argument_list|()
decl_stmt|;
name|outboundSecurityContext
operator|.
name|putList
argument_list|(
name|SecurityEvent
operator|.
name|class
argument_list|,
name|requestSecurityEvents
argument_list|)
expr_stmt|;
name|outboundSecurityContext
operator|.
name|addSecurityEventListener
argument_list|(
name|securityEventListener
argument_list|)
expr_stmt|;
comment|// Save Tokens on the security context
for|for
control|(
name|String
name|key
range|:
name|outboundTokens
operator|.
name|keySet
argument_list|()
control|)
block|{
name|SecurityTokenProvider
argument_list|<
name|OutboundSecurityToken
argument_list|>
name|provider
init|=
name|outboundTokens
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|outboundSecurityContext
operator|.
name|registerSecurityTokenProvider
argument_list|(
name|provider
operator|.
name|getId
argument_list|()
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|outboundSecurityContext
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|provider
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|newXMLStreamWriter
operator|=
name|outboundWSSec
operator|.
name|processOutMessage
argument_list|(
name|os
argument_list|,
name|encoding
argument_list|,
name|outboundSecurityContext
argument_list|)
expr_stmt|;
name|mc
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|newXMLStreamWriter
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
operator|new
name|Fault
argument_list|(
name|e
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
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|mc
operator|.
name|put
argument_list|(
name|AbstractOutDatabindingInterceptor
operator|.
name|DISABLE_OUTPUTSTREAM_OPTIMIZATION
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|mc
operator|.
name|put
argument_list|(
name|StaxOutInterceptor
operator|.
name|FORCE_START_DOCUMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|mc
argument_list|,
name|StaxOutInterceptor
operator|.
name|FORCE_START_DOCUMENT
argument_list|,
literal|false
argument_list|)
condition|)
block|{
try|try
block|{
name|newXMLStreamWriter
operator|.
name|writeStartDocument
argument_list|(
name|encoding
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|mc
operator|.
name|removeContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|mc
operator|.
name|put
argument_list|(
name|OUTPUT_STREAM_HOLDER
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
comment|// Add a final interceptor to write end elements
name|mc
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
name|ending
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|SecurityEventListener
name|configureSecurityEventListener
parameter_list|(
specifier|final
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
name|outgoingSecurityEventList
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
name|XMLSecurityException
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
name|SamlToken
operator|&&
name|securityEvent
operator|instanceof
name|TokenSecurityEvent
condition|)
block|{
comment|// Store SAML keys in case we need them on the inbound side
name|TokenSecurityEvent
argument_list|<
name|?
argument_list|>
name|tokenSecurityEvent
init|=
operator|(
name|TokenSecurityEvent
argument_list|<
name|?
argument_list|>
operator|)
name|securityEvent
decl_stmt|;
name|WSS4JUtils
operator|.
name|parseAndStoreStreamingSecurityToken
argument_list|(
name|tokenSecurityEvent
operator|.
name|getSecurityToken
argument_list|()
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outgoingSecurityEventList
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
literal|".out"
argument_list|,
name|outgoingSecurityEventList
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
literal|".out"
argument_list|,
name|outgoingSecurityEventList
argument_list|)
expr_stmt|;
return|return
name|securityEventListener
return|;
block|}
specifier|protected
name|void
name|configureProperties
parameter_list|(
name|SoapMessage
name|msg
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|SecurityTokenProvider
argument_list|<
name|OutboundSecurityToken
argument_list|>
argument_list|>
name|outboundTokens
parameter_list|)
throws|throws
name|WSSecurityException
block|{
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
comment|// Crypto loading only applies for Map
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
name|String
name|user
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
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
name|USER
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
name|String
name|sigUser
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigUser
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
name|SIGNATURE_USER
argument_list|,
name|sigUser
argument_list|)
expr_stmt|;
block|}
name|String
name|encUser
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|encUser
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
name|ENCRYPTION_USER
argument_list|,
name|encUser
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|sigCrypto
init|=
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
argument_list|)
decl_stmt|;
if|if
condition|(
name|sigCrypto
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
name|SIG_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|sigCrypto
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
name|sigCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|sigCrypto
argument_list|)
expr_stmt|;
if|if
condition|(
name|sigUser
operator|==
literal|null
operator|&&
name|sigCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Fall back to default identifier
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|SIGNATURE_USER
argument_list|,
name|sigCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Crypto
name|encCrypto
init|=
name|loadCrypto
argument_list|(
name|msg
argument_list|,
name|ConfigurationConstants
operator|.
name|ENC_PROP_FILE
argument_list|,
name|ConfigurationConstants
operator|.
name|ENC_PROP_REF_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|encCrypto
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
name|ENC_PROP_REF_ID
argument_list|,
literal|"RefId-"
operator|+
name|encCrypto
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
name|encCrypto
operator|.
name|hashCode
argument_list|()
argument_list|,
name|encCrypto
argument_list|)
expr_stmt|;
if|if
condition|(
name|encUser
operator|==
literal|null
operator|&&
name|encCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Fall back to default identifier
name|config
operator|.
name|put
argument_list|(
name|ConfigurationConstants
operator|.
name|ENCRYPTION_USER
argument_list|,
name|encCrypto
operator|.
name|getDefaultX509Identifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
specifier|final
name|WSS4JStaxOutInterceptorInternal
name|createEndingInterceptor
parameter_list|()
block|{
return|return
operator|new
name|WSS4JStaxOutInterceptorInternal
argument_list|()
return|;
block|}
specifier|private
name|String
name|getEncoding
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|ex
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|String
name|encoding
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
operator|&&
name|ex
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoding
operator|=
operator|(
name|String
operator|)
name|ex
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
literal|"UTF-8"
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
return|return
name|encoding
return|;
block|}
specifier|final
class|class
name|WSS4JStaxOutInterceptorInternal
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|WSS4JStaxOutInterceptorInternal
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM_ENDING
argument_list|)
expr_stmt|;
name|getBefore
argument_list|()
operator|.
name|add
argument_list|(
name|AttachmentOutInterceptor
operator|.
name|AttachmentOutEndingInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|mc
parameter_list|)
throws|throws
name|Fault
block|{
try|try
block|{
name|XMLStreamWriter
name|xtw
init|=
name|mc
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|xtw
operator|!=
literal|null
condition|)
block|{
name|xtw
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|xtw
operator|.
name|flush
argument_list|()
expr_stmt|;
name|xtw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|OutputStream
name|os
init|=
operator|(
name|OutputStream
operator|)
name|mc
operator|.
name|get
argument_list|(
name|OUTPUT_STREAM_HOLDER
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|mc
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
name|mc
operator|.
name|removeContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

