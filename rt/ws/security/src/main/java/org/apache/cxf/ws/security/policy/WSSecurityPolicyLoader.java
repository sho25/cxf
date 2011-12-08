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
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|AssertionBuilderLoader
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
name|AssertionBuilderRegistry
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
name|PolicyBuilder
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
name|PolicyInterceptorProviderLoader
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
name|PolicyInterceptorProviderRegistry
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
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertionBuilder
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
name|policy
operator|.
name|builders
operator|.
name|AlgorithmSuiteBuilder
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
name|policy
operator|.
name|builders
operator|.
name|AsymmetricBindingBuilder
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
name|policy
operator|.
name|builders
operator|.
name|ContentEncryptedElementsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|EncryptedElementsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|EncryptedPartsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|HttpsTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|InitiatorSignatureTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|InitiatorTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|IssuedTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|KerberosTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|KeyValueTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|LayoutBuilder
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
name|policy
operator|.
name|builders
operator|.
name|ProtectionTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|RecipientTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|RequiredElementsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|RequiredPartsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SamlTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SecureConversationTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SecurityContextTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SignedElementsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SignedPartsBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SupportingTokens12Builder
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
name|policy
operator|.
name|builders
operator|.
name|SupportingTokensBuilder
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
name|policy
operator|.
name|builders
operator|.
name|SymmetricBindingBuilder
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
name|policy
operator|.
name|builders
operator|.
name|TransportBindingBuilder
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
name|policy
operator|.
name|builders
operator|.
name|TransportTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|Trust10Builder
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
name|policy
operator|.
name|builders
operator|.
name|Trust13Builder
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
name|policy
operator|.
name|builders
operator|.
name|UsernameTokenBuilder
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
name|policy
operator|.
name|builders
operator|.
name|WSS10Builder
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
name|policy
operator|.
name|builders
operator|.
name|WSS11Builder
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
name|policy
operator|.
name|builders
operator|.
name|X509TokenBuilder
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
name|policy
operator|.
name|interceptors
operator|.
name|HttpsTokenInterceptorProvider
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
name|policy
operator|.
name|interceptors
operator|.
name|IssuedTokenInterceptorProvider
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
name|policy
operator|.
name|interceptors
operator|.
name|KerberosTokenInterceptorProvider
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
name|policy
operator|.
name|interceptors
operator|.
name|SecureConversationTokenInterceptorProvider
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
name|policy
operator|.
name|interceptors
operator|.
name|UsernameTokenInterceptorProvider
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
name|policy
operator|.
name|interceptors
operator|.
name|WSSecurityInterceptorProvider
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
name|policy
operator|.
name|interceptors
operator|.
name|WSSecurityPolicyInterceptorProvider
import|;
end_import

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
specifier|final
class|class
name|WSSecurityPolicyLoader
implements|implements
name|PolicyInterceptorProviderLoader
implements|,
name|AssertionBuilderLoader
block|{
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WSSecurityPolicyLoader
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|registerBuilders
argument_list|()
expr_stmt|;
try|try
block|{
name|registerProviders
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//probably wss4j isn't found or something. We'll ignore this
comment|//as the policy framework will then not find the providers
comment|//and error out at that point.  If nothing uses ws-securitypolicy
comment|//no warnings/errors will display
block|}
block|}
specifier|public
name|void
name|registerBuilders
parameter_list|()
block|{
name|AssertionBuilderRegistry
name|reg
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|PolicyBuilder
name|pbuild
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|AlgorithmSuiteBuilder
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|AsymmetricBindingBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|ContentEncryptedElementsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|EncryptedElementsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|EncryptedPartsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|HttpsTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|InitiatorTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|InitiatorSignatureTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|IssuedTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|LayoutBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|ProtectionTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|RecipientTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|RequiredElementsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|RequiredPartsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SamlTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|KerberosTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SecureConversationTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SecurityContextTokenBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SignedElementsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SignedPartsBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SupportingTokens12Builder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SupportingTokensBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|SymmetricBindingBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|TransportBindingBuilder
argument_list|(
name|pbuild
argument_list|,
name|bus
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|TransportTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|Trust10Builder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|Trust13Builder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|UsernameTokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|KeyValueTokenBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|WSS10Builder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|WSS11Builder
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|X509TokenBuilder
argument_list|(
name|pbuild
argument_list|)
argument_list|)
expr_stmt|;
comment|//add generic assertions for these known things to prevent warnings
name|List
argument_list|<
name|QName
argument_list|>
name|others
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|INCLUDE_TIMESTAMP
block|,
name|SP11Constants
operator|.
name|INCLUDE_TIMESTAMP
block|,
name|SP12Constants
operator|.
name|ENCRYPT_SIGNATURE
block|,
name|SP11Constants
operator|.
name|ENCRYPT_SIGNATURE
block|,
name|SP12Constants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
block|,
name|SP11Constants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
block|,
name|SP12Constants
operator|.
name|WSS_X509_V1_TOKEN_10
block|,
name|SP12Constants
operator|.
name|WSS_X509_V1_TOKEN_11
block|,
name|SP12Constants
operator|.
name|WSS_X509_V3_TOKEN_10
block|,
name|SP12Constants
operator|.
name|WSS_X509_V3_TOKEN_11
block|,
name|SP11Constants
operator|.
name|WSS_X509_V1_TOKEN_10
block|,
name|SP11Constants
operator|.
name|WSS_X509_V1_TOKEN_11
block|,
name|SP11Constants
operator|.
name|WSS_X509_V3_TOKEN_10
block|,
name|SP11Constants
operator|.
name|WSS_X509_V3_TOKEN_11
block|,
name|SP12Constants
operator|.
name|WSS_X509_PKCS7_TOKEN_11
block|,
name|SP12Constants
operator|.
name|WSS_X509_PKI_PATH_V1_TOKEN_11
block|,
name|SP11Constants
operator|.
name|WSS_X509_PKCS7_TOKEN_11
block|,
name|SP11Constants
operator|.
name|WSS_X509_PKI_PATH_V1_TOKEN_11
block|,
name|SP12Constants
operator|.
name|REQUIRE_THUMBPRINT_REFERENCE
block|,
name|SP11Constants
operator|.
name|REQUIRE_THUMBPRINT_REFERENCE
block|,
name|SP12Constants
operator|.
name|REQUIRE_DERIVED_KEYS
block|,
name|SP11Constants
operator|.
name|REQUIRE_DERIVED_KEYS
block|,
name|SP12Constants
operator|.
name|REQUIRE_INTERNAL_REFERENCE
block|,
name|SP11Constants
operator|.
name|REQUIRE_INTERNAL_REFERENCE
block|,
name|SP12Constants
operator|.
name|REQUIRE_ISSUER_SERIAL_REFERENCE
block|,
name|SP11Constants
operator|.
name|REQUIRE_ISSUER_SERIAL_REFERENCE
block|,
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPT_BEFORE_SIGNING
argument_list|)
block|,
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SP11Constants
operator|.
name|ENCRYPT_BEFORE_SIGNING
argument_list|)
block|,
operator|new
name|QName
argument_list|(
name|SP12Constants
operator|.
name|SP_NS
argument_list|,
name|SP12Constants
operator|.
name|SIGN_BEFORE_ENCRYPTING
argument_list|)
block|,
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SP11Constants
operator|.
name|SIGN_BEFORE_ENCRYPTING
argument_list|)
block|,
name|SP12Constants
operator|.
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
block|,
name|SP11Constants
operator|.
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
block|,         }
argument_list|)
decl_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|PrimitiveAssertionBuilder
argument_list|(
name|others
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|registerProviders
parameter_list|()
block|{
comment|//interceptor providers for all of the above
name|PolicyInterceptorProviderRegistry
name|reg
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|PolicyInterceptorProviderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|reg
operator|.
name|register
argument_list|(
operator|new
name|WSSecurityPolicyInterceptorProvider
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|WSSecurityInterceptorProvider
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|HttpsTokenInterceptorProvider
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|KerberosTokenInterceptorProvider
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|IssuedTokenInterceptorProvider
argument_list|()
argument_list|)
expr_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|UsernameTokenInterceptorProvider
argument_list|(
name|bus
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|register
argument_list|(
operator|new
name|SecureConversationTokenInterceptorProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

