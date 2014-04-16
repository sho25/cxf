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
name|util
operator|.
name|ArrayList
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
name|Iterator
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
name|namespace
operator|.
name|QName
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
name|SoapActionInInterceptor
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
name|model
operator|.
name|SoapBindingInfo
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
name|model
operator|.
name|SoapOperationInfo
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|MessageInfo
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
name|AssertionInfo
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
name|policy
operator|.
name|EffectivePolicy
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
name|SP12Constants
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
name|SPConstants
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
name|model
operator|.
name|AlgorithmSuite
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
name|stax
operator|.
name|OperationPolicy
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
name|stax
operator|.
name|PolicyEnforcer
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
name|stax
operator|.
name|PolicyInputProcessor
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
name|impl
operator|.
name|securityToken
operator|.
name|HttpsSecurityTokenImpl
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
name|HttpsTokenSecurityEvent
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PolicyBasedWSS4JStaxInInterceptor
extends|extends
name|WSS4JStaxInInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|PolicyBasedWSS4JStaxInInterceptor
name|INSTANCE
init|=
operator|new
name|PolicyBasedWSS4JStaxInInterceptor
argument_list|()
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
name|PolicyBasedWSS4JStaxInInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
throws|throws
name|Fault
block|{
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|enableStax
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
name|ENABLE_STREAMING_SECURITY
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|aim
operator|!=
literal|null
operator|&&
name|enableStax
condition|)
block|{
name|super
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|msg
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|PolicyStaxActionInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|WSSSecurityProperties
name|createSecurityProperties
parameter_list|()
block|{
return|return
operator|new
name|WSSSecurityProperties
argument_list|()
return|;
block|}
specifier|private
name|void
name|checkAsymmetricBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|AssertionInfo
name|ais
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Object
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|encrCrypto
init|=
name|getEncryptionCrypto
argument_list|(
name|e
argument_list|,
name|message
argument_list|,
name|securityProperties
argument_list|)
decl_stmt|;
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|signCrypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
else|else
block|{
name|signCrypto
operator|=
name|getSignatureCrypto
argument_list|(
name|s
argument_list|,
name|message
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setDecryptionCrypto
argument_list|(
name|signCrypto
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encrCrypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSignatureVerificationCrypto
argument_list|(
name|encrCrypto
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSignatureVerificationCrypto
argument_list|(
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkTransportBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|XMLSecurityException
block|{
name|boolean
name|transportPolicyInEffect
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|transportPolicyInEffect
operator|&&
operator|!
operator|(
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
operator|==
literal|null
operator|&&
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
operator|==
literal|null
operator|)
condition|)
block|{
return|return;
block|}
comment|// Add a HttpsSecurityEvent so the policy verification code knows TLS is in use
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|HttpsTokenSecurityEvent
name|httpsTokenSecurityEvent
init|=
operator|new
name|HttpsTokenSecurityEvent
argument_list|()
decl_stmt|;
name|httpsTokenSecurityEvent
operator|.
name|setAuthenticationType
argument_list|(
name|HttpsTokenSecurityEvent
operator|.
name|AuthenticationType
operator|.
name|HttpsNoAuthentication
argument_list|)
expr_stmt|;
name|HttpsSecurityTokenImpl
name|httpsSecurityToken
init|=
operator|new
name|HttpsSecurityTokenImpl
argument_list|()
decl_stmt|;
try|try
block|{
name|httpsSecurityToken
operator|.
name|addTokenUsage
argument_list|(
name|WSSecurityTokenConstants
operator|.
name|TokenUsage_MainSignature
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLSecurityException
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
block|}
name|httpsTokenSecurityEvent
operator|.
name|setSecurityToken
argument_list|(
name|httpsSecurityToken
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|securityEvents
init|=
name|getSecurityEventList
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|securityEvents
operator|.
name|add
argument_list|(
name|httpsTokenSecurityEvent
argument_list|)
expr_stmt|;
block|}
name|Object
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|encrCrypto
init|=
name|getEncryptionCrypto
argument_list|(
name|e
argument_list|,
name|message
argument_list|,
name|securityProperties
argument_list|)
decl_stmt|;
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|signCrypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
else|else
block|{
name|signCrypto
operator|=
name|getSignatureCrypto
argument_list|(
name|s
argument_list|,
name|message
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setDecryptionCrypto
argument_list|(
name|signCrypto
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|encrCrypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSignatureVerificationCrypto
argument_list|(
name|encrCrypto
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|signCrypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSignatureVerificationCrypto
argument_list|(
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|SecurityEvent
argument_list|>
name|getSecurityEventList
parameter_list|(
name|Message
name|message
parameter_list|)
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
name|securityEvents
init|=
operator|(
name|List
argument_list|<
name|SecurityEvent
argument_list|>
operator|)
name|message
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
if|if
condition|(
name|securityEvents
operator|==
literal|null
condition|)
block|{
name|securityEvents
operator|=
operator|new
name|ArrayList
argument_list|<
name|SecurityEvent
argument_list|>
argument_list|()
expr_stmt|;
name|message
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
name|securityEvents
argument_list|)
expr_stmt|;
block|}
return|return
name|securityEvents
return|;
block|}
specifier|private
name|void
name|checkSymmetricBinding
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|SoapMessage
name|message
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|AssertionInfo
name|ais
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Object
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Object
name|e
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_CRYPTO
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|e
operator|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|)
expr_stmt|;
block|}
name|Crypto
name|encrCrypto
init|=
name|getEncryptionCrypto
argument_list|(
name|e
argument_list|,
name|message
argument_list|,
name|securityProperties
argument_list|)
decl_stmt|;
name|Crypto
name|signCrypto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|signCrypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
else|else
block|{
name|signCrypto
operator|=
name|getSignatureCrypto
argument_list|(
name|s
argument_list|,
name|message
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|Crypto
name|crypto
init|=
name|encrCrypto
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|signCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSignatureCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
block|}
name|crypto
operator|=
name|signCrypto
expr_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setDecryptionCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Crypto
name|crypto
init|=
name|signCrypto
decl_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|encrCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setSignatureVerificationCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
block|}
name|crypto
operator|=
name|encrCrypto
expr_stmt|;
if|if
condition|(
name|crypto
operator|==
literal|null
condition|)
block|{
name|crypto
operator|=
name|signCrypto
expr_stmt|;
block|}
if|if
condition|(
name|crypto
operator|!=
literal|null
condition|)
block|{
name|securityProperties
operator|.
name|setDecryptionCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
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
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|checkAsymmetricBinding
argument_list|(
name|aim
argument_list|,
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|checkSymmetricBinding
argument_list|(
name|aim
argument_list|,
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
name|checkTransportBinding
argument_list|(
name|aim
argument_list|,
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
comment|// Allow for setting non-standard asymmetric signature algorithms
name|String
name|asymSignatureAlgorithm
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
name|ASYMMETRIC_SIGNATURE_ALGORITHM
argument_list|)
decl_stmt|;
if|if
condition|(
name|asymSignatureAlgorithm
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|algorithmSuites
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ALGORITHM_SUITE
argument_list|)
decl_stmt|;
if|if
condition|(
name|algorithmSuites
operator|!=
literal|null
operator|&&
operator|!
name|algorithmSuites
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|algorithmSuite
range|:
name|algorithmSuites
control|)
block|{
name|AlgorithmSuite
name|algSuite
init|=
operator|(
name|AlgorithmSuite
operator|)
name|algorithmSuite
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|algSuite
operator|.
name|setAsymmetricSignature
argument_list|(
name|asymSignatureAlgorithm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|super
operator|.
name|configureProperties
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Is a Nonce Cache required, i.e. are we expecting a UsernameToken       */
annotation|@
name|Override
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
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|AssertionInfo
name|ais
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is a Timestamp cache required, i.e. are we expecting a Timestamp       */
annotation|@
name|Override
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
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|AssertionInfo
name|ais
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is a SAML Cache required, i.e. are we expecting a SAML Token       */
annotation|@
name|Override
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
name|AssertionInfoMap
name|aim
init|=
name|msg
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|AssertionInfo
name|ais
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SAML_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
name|SecurityEventListener
argument_list|>
name|securityEventListeners
init|=
operator|new
name|ArrayList
argument_list|<
name|SecurityEventListener
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|securityEventListeners
operator|.
name|addAll
argument_list|(
name|super
operator|.
name|configureSecurityEventListeners
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
argument_list|)
expr_stmt|;
name|Endpoint
name|endoint
init|=
name|msg
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
name|PolicyEnforcer
name|policyEnforcer
init|=
name|createPolicyEnforcer
argument_list|(
name|endoint
operator|.
name|getEndpointInfo
argument_list|()
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|securityProperties
operator|.
name|addInputProcessor
argument_list|(
operator|new
name|PolicyInputProcessor
argument_list|(
name|policyEnforcer
argument_list|,
name|securityProperties
argument_list|)
argument_list|)
expr_stmt|;
name|securityEventListeners
operator|.
name|add
argument_list|(
name|policyEnforcer
argument_list|)
expr_stmt|;
return|return
name|securityEventListeners
return|;
block|}
specifier|private
name|PolicyEnforcer
name|createPolicyEnforcer
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|SoapMessage
name|msg
parameter_list|)
throws|throws
name|WSSPolicyException
block|{
name|EffectivePolicy
name|dispatchPolicy
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|OperationPolicy
argument_list|>
name|operationPolicies
init|=
operator|new
name|ArrayList
argument_list|<
name|OperationPolicy
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|BindingOperationInfo
argument_list|>
name|bindingOperationInfos
init|=
name|endpointInfo
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperations
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|BindingOperationInfo
argument_list|>
name|bindingOperationInfoIterator
init|=
name|bindingOperationInfos
operator|.
name|iterator
argument_list|()
init|;
name|bindingOperationInfoIterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|BindingOperationInfo
name|bindingOperationInfo
init|=
name|bindingOperationInfoIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|operationName
init|=
name|bindingOperationInfo
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// todo: I'm not sure what the effectivePolicy exactly contains,
comment|// a) only the operation policy,
comment|// or b) all policies for the service,
comment|// or c) all policies which applies for the current operation.
comment|// c) is that what we need for stax.
name|EffectivePolicy
name|policy
init|=
operator|(
name|EffectivePolicy
operator|)
name|bindingOperationInfo
operator|.
name|getProperty
argument_list|(
literal|"policy-engine-info-serve-request"
argument_list|)
decl_stmt|;
comment|//PolicyEngineImpl.POLICY_INFO_REQUEST_SERVER);
if|if
condition|(
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|msg
argument_list|)
condition|)
block|{
name|policy
operator|=
operator|(
name|EffectivePolicy
operator|)
name|bindingOperationInfo
operator|.
name|getProperty
argument_list|(
literal|"policy-engine-info-client-response"
argument_list|)
expr_stmt|;
comment|// Save the Dispatch Policy as it may be used on another BindingOperationInfo
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
literal|"http://cxf.apache.org/jaxws/dispatch"
operator|.
name|equals
argument_list|(
name|operationName
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|dispatchPolicy
operator|=
name|policy
expr_stmt|;
block|}
if|if
condition|(
name|bindingOperationInfo
operator|.
name|getOutput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|MessageInfo
name|messageInfo
init|=
name|bindingOperationInfo
operator|.
name|getOutput
argument_list|()
operator|.
name|getMessageInfo
argument_list|()
decl_stmt|;
name|operationName
operator|=
name|messageInfo
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|messageInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|messageInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getConcreteName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|operationName
operator|=
name|messageInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getConcreteName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|bindingOperationInfo
operator|.
name|getInput
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|MessageInfo
name|messageInfo
init|=
name|bindingOperationInfo
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageInfo
argument_list|()
decl_stmt|;
name|operationName
operator|=
name|messageInfo
operator|.
name|getName
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|messageInfo
operator|.
name|getMessageParts
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|messageInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getConcreteName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|operationName
operator|=
name|messageInfo
operator|.
name|getMessagePart
argument_list|(
literal|0
argument_list|)
operator|.
name|getConcreteName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|SoapOperationInfo
name|soapOperationInfo
init|=
name|bindingOperationInfo
operator|.
name|getExtensor
argument_list|(
name|SoapOperationInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapOperationInfo
operator|!=
literal|null
operator|&&
name|policy
operator|==
literal|null
operator|&&
name|dispatchPolicy
operator|!=
literal|null
condition|)
block|{
name|policy
operator|=
name|dispatchPolicy
expr_stmt|;
block|}
if|if
condition|(
name|policy
operator|!=
literal|null
operator|&&
name|soapOperationInfo
operator|!=
literal|null
condition|)
block|{
name|String
name|soapNS
decl_stmt|;
name|BindingInfo
name|bindingInfo
init|=
name|bindingOperationInfo
operator|.
name|getBinding
argument_list|()
decl_stmt|;
if|if
condition|(
name|bindingInfo
operator|instanceof
name|SoapBindingInfo
condition|)
block|{
name|soapNS
operator|=
operator|(
operator|(
name|SoapBindingInfo
operator|)
name|bindingInfo
operator|)
operator|.
name|getSoapVersion
argument_list|()
operator|.
name|getNamespace
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|//no idea what todo here...
comment|//most probably throw an exception:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"BindingInfo is not an instance of SoapBindingInfo"
argument_list|)
throw|;
block|}
name|OperationPolicy
name|operationPolicy
init|=
operator|new
name|OperationPolicy
argument_list|(
name|operationName
argument_list|)
decl_stmt|;
name|operationPolicy
operator|.
name|setPolicy
argument_list|(
name|policy
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|operationPolicy
operator|.
name|setOperationAction
argument_list|(
name|soapOperationInfo
operator|.
name|getAction
argument_list|()
argument_list|)
expr_stmt|;
name|operationPolicy
operator|.
name|setSoapMessageVersionNamespace
argument_list|(
name|soapNS
argument_list|)
expr_stmt|;
name|operationPolicies
operator|.
name|add
argument_list|(
name|operationPolicy
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|soapAction
init|=
name|SoapActionInInterceptor
operator|.
name|getSoapAction
argument_list|(
name|msg
argument_list|)
decl_stmt|;
if|if
condition|(
name|soapAction
operator|==
literal|null
condition|)
block|{
name|soapAction
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|actor
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
name|ACTOR
argument_list|)
decl_stmt|;
specifier|final
name|Collection
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Attachment
argument_list|>
name|attachments
init|=
name|msg
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|int
name|attachmentCount
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|attachments
operator|!=
literal|null
operator|&&
operator|!
name|attachments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|attachmentCount
operator|=
name|attachments
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|PolicyEnforcer
argument_list|(
name|operationPolicies
argument_list|,
name|soapAction
argument_list|,
name|isRequestor
argument_list|(
name|msg
argument_list|)
argument_list|,
name|actor
argument_list|,
name|attachmentCount
argument_list|)
return|;
block|}
block|}
end_class

end_unit

