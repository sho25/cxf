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
name|wss4j
operator|.
name|policyhandlers
operator|.
name|StaxAsymmetricBindingHandler
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
name|wss4j
operator|.
name|policyhandlers
operator|.
name|StaxSymmetricBindingHandler
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
name|wss4j
operator|.
name|policyhandlers
operator|.
name|StaxTransportBindingHandler
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
name|AsymmetricBinding
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
name|SymmetricBinding
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
name|TransportBinding
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
name|xml
operator|.
name|security
operator|.
name|stax
operator|.
name|ext
operator|.
name|OutboundSecurityContext
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|PolicyBasedWSS4JStaxOutInterceptor
extends|extends
name|WSS4JStaxOutInterceptor
block|{
specifier|public
specifier|static
specifier|final
name|PolicyBasedWSS4JStaxOutInterceptor
name|INSTANCE
init|=
operator|new
name|PolicyBasedWSS4JStaxOutInterceptor
argument_list|()
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
name|SoapMessage
name|message
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
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
name|setSignatureCrypto
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
name|setEncryptionCrypto
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
name|setEncryptionCrypto
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
name|SoapMessage
name|message
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
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
name|setSignatureCrypto
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
name|setEncryptionCrypto
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
name|setEncryptionCrypto
argument_list|(
name|signCrypto
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|checkSymmetricBinding
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
block|{
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
name|setEncryptionCrypto
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
name|setSignatureCrypto
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
name|setEncryptionCrypto
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
name|setSignatureCrypto
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
name|OutboundSecurityContext
name|outboundSecurityContext
parameter_list|,
name|WSSSecurityProperties
name|securityProperties
parameter_list|)
throws|throws
name|WSSecurityException
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
name|AssertionInfo
name|asymAis
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
name|asymAis
operator|!=
literal|null
condition|)
block|{
name|checkAsymmetricBinding
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
name|AssertionInfo
name|symAis
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
name|symAis
operator|!=
literal|null
condition|)
block|{
name|checkSymmetricBinding
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
name|AssertionInfo
name|transAis
init|=
name|getFirstAssertionByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|transAis
operator|!=
literal|null
condition|)
block|{
name|checkTransportBinding
argument_list|(
name|msg
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|configureProperties
argument_list|(
name|msg
argument_list|,
name|outboundSecurityContext
argument_list|,
name|securityProperties
argument_list|)
expr_stmt|;
if|if
condition|(
name|transAis
operator|!=
literal|null
condition|)
block|{
name|TransportBinding
name|binding
init|=
operator|(
name|TransportBinding
operator|)
name|transAis
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
operator|new
name|StaxTransportBindingHandler
argument_list|(
name|securityProperties
argument_list|,
name|msg
argument_list|,
name|binding
argument_list|,
name|outboundSecurityContext
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|asymAis
operator|!=
literal|null
condition|)
block|{
name|AsymmetricBinding
name|binding
init|=
operator|(
name|AsymmetricBinding
operator|)
name|asymAis
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
operator|new
name|StaxAsymmetricBindingHandler
argument_list|(
name|securityProperties
argument_list|,
name|msg
argument_list|,
name|binding
argument_list|,
name|outboundSecurityContext
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|symAis
operator|!=
literal|null
condition|)
block|{
name|SymmetricBinding
name|binding
init|=
operator|(
name|SymmetricBinding
operator|)
name|symAis
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
operator|new
name|StaxSymmetricBindingHandler
argument_list|(
name|securityProperties
argument_list|,
name|msg
argument_list|,
name|binding
argument_list|,
name|outboundSecurityContext
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Fall back to Transport Binding
operator|new
name|StaxTransportBindingHandler
argument_list|(
name|securityProperties
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|,
name|outboundSecurityContext
argument_list|)
operator|.
name|handleBinding
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

