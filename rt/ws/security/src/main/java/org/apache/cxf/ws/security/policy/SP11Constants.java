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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SP11Constants
extends|extends
name|SPConstants
block|{
specifier|public
specifier|static
specifier|final
name|SP11Constants
name|INSTANCE
init|=
operator|new
name|SP11Constants
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SP_NS
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/07/securitypolicy"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SP_PREFIX
init|=
literal|"sp"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INCLUDE_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ATTR_INCLUDE_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INCLUDE_NEVER
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|+
name|SPConstants
operator|.
name|INCLUDE_TOKEN_NEVER_SUFFIX
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INCLUDE_ONCE
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|+
name|SPConstants
operator|.
name|INCLUDE_TOKEN_ONCE_SUFFIX
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INCLUDE_ALWAYS_TO_RECIPIENT
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|+
name|SPConstants
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT_SUFFIX
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INCLUDE_ALWAYS
init|=
name|SP11Constants
operator|.
name|SP_NS
operator|+
name|SPConstants
operator|.
name|INCLUDE_TOKEN_ALWAYS_SUFFIX
decl_stmt|;
comment|// /////////////////////////////////////////////////////////////////////
specifier|public
specifier|static
specifier|final
name|QName
name|ATTR_XPATH_VERSION
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|XPATH_VERSION
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
comment|////////////////////////////////////////////////////////////////////////
specifier|public
specifier|static
specifier|final
name|QName
name|TRANSPORT_BINDING
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_BINDING
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ALGORITHM_SUITE
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ALGO_SUITE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|LAYOUT
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|LAYOUT
argument_list|,
name|SP_PREFIX
argument_list|)
decl_stmt|;
comment|// ////////////////
specifier|public
specifier|static
specifier|final
name|QName
name|INCLUDE_TIMESTAMP
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|INCLUDE_TIMESTAMP
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ONLY_SIGN_ENTIRE_HEADERS_AND_BODY
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|TRANSPORT_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|TRANSPORT_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|HTTPS_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|HTTPS_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SECURITY_CONTEXT_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SECURITY_CONTEXT_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SECURE_CONVERSATION_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SIGNATURE_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SIGNATURE_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SIGNED_PARTS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SIGNED_PARTS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCRYPTED_PARTS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SIGNED_ELEMENTS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SIGNED_ELEMENTS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCRYPTED_ELEMENTS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRED_ELEMENTS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRED_ELEMENTS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|USERNAME_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SAML_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SAML_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|KERBEROS_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|KERBEROS_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SPNEGO_CONTEXT_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SPNEGO_CONTEXT_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_USERNAME_TOKEN10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_USERNAME_TOKEN11
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN11
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCRYPTION_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ENCRYPTION_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|X509_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|X509_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_V1_TOKEN_10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_V1_TOKEN10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_V3_TOKEN_10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_V3_TOKEN10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_PKCS7_TOKEN_10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_PKCS7_TOKEN10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_PKI_PATH_V1_TOKEN_10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_PKI_PATH_V1_TOKEN10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_V1_TOKEN_11
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_V1_TOKEN11
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_V3_TOKEN_11
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_V3_TOKEN11
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_PKCS7_TOKEN_11
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_PKCS7_TOKEN11
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS_X509_PKI_PATH_V1_TOKEN_11
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS_X509_PKI_PATH_V1_TOKEN11
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ISSUED_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ISSUED_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SUPPORTING_TOKENS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SUPPORTING_TOKENS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SIGNED_SUPPORTING_TOKENS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENDORSING_SUPPORTING_TOKENS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ENDORSING_SUPPORTING_TOKENS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|PROTECTION_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|PROTECTION_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ASYMMETRIC_BINDING
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ASYMMETRIC_BINDING
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SYMMETRIC_BINDING
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SYMMETRIC_BINDING
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INITIATOR_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|INITIATOR_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|INITIATOR_SIGNATURE_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|INITIATOR_SIGNATURE_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|RECIPIENT_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|RECIPIENT_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ENCRYPT_SIGNATURE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ENCRYPT_SIGNATURE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|PROTECT_TOKENS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|PROTECT_TOKENS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_KEY_IDENTIFIER_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_ISSUER_SERIAL_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_ISSUER_SERIAL_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_EMBEDDED_TOKEN_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EMBEDDED_TOKEN_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_THUMBPRINT_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_THUMBPRINT_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_REF_KEY_IDENTIFIER
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_REF_KEY_IDENTIFIER
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_REF_ISSUER_SERIAL
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_REF_ISSUER_SERIAL
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_REF_EXTERNAL_URI
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_REF_EXTERNAL_URI
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_REF_EMBEDDED_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_REF_EMBEDDED_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_REF_THUMBPRINT
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_REF_THUMBPRINT
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_REF_ENCRYPTED_KEY
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_REF_ENCRYPTED_KEY
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|WSS11
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|WSS11
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|TRUST_10
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|TRUST_10
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_SIGNATURE_CONFIRMATION
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_SIGNATURE_CONFIRMATION
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_CLIENT_CHALLENGE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_CLIENT_CHALLENGE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_SERVER_CHALLENGE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_SERVER_CHALLENGE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_CLIENT_ENTROPY
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_CLIENT_ENTROPY
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_SERVER_ENTROPY
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_SERVER_ENTROPY
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|MUST_SUPPORT_ISSUED_TOKENS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|MUST_SUPPORT_ISSUED_TOKENS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ISSUER
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|ISSUER
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_DERIVED_KEYS
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_DERIVED_KEYS
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_EXTERNAL_URI_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EXTERNAL_URI_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_EXTERNAL_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EXTERNAL_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUIRE_INTERNAL_REFERENCE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_INTERNAL_REFERENCE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|REQUEST_SECURITY_TOKEN_TEMPLATE
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|REQUEST_SECURITY_TOKEN_TEMPLATE
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|SC10_SECURITY_CONTEXT_TOKEN
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|SC10_SECURITY_CONTEXT_TOKEN
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|BOOTSTRAP_POLICY
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|BOOTSTRAP_POLICY
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|XPATH
init|=
operator|new
name|QName
argument_list|(
name|SP11Constants
operator|.
name|SP_NS
argument_list|,
name|SPConstants
operator|.
name|XPATH_EXPR
argument_list|,
name|SP11Constants
operator|.
name|SP_PREFIX
argument_list|)
decl_stmt|;
specifier|private
name|SP11Constants
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
name|IncludeTokenType
name|getInclusionFromAttributeValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|INCLUDE_ALWAYS
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS
return|;
block|}
elseif|else
if|if
condition|(
name|INCLUDE_ALWAYS_TO_RECIPIENT
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
return|;
block|}
elseif|else
if|if
condition|(
name|INCLUDE_NEVER
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_NEVER
return|;
block|}
elseif|else
if|if
condition|(
name|INCLUDE_ONCE
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ONCE
return|;
block|}
return|return
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|?
name|IncludeTokenType
operator|.
name|INCLUDE_TOKEN_ALWAYS
else|:
literal|null
return|;
block|}
specifier|public
name|String
name|getAttributeValueFromInclusion
parameter_list|(
name|IncludeTokenType
name|value
parameter_list|)
block|{
switch|switch
condition|(
name|value
condition|)
block|{
case|case
name|INCLUDE_TOKEN_ALWAYS
case|:
return|return
name|SP11Constants
operator|.
name|INCLUDE_ALWAYS
return|;
case|case
name|INCLUDE_TOKEN_ALWAYS_TO_RECIPIENT
case|:
return|return
name|SP11Constants
operator|.
name|INCLUDE_ALWAYS_TO_RECIPIENT
return|;
case|case
name|INCLUDE_TOKEN_NEVER
case|:
return|return
name|SP11Constants
operator|.
name|INCLUDE_NEVER
return|;
case|case
name|INCLUDE_TOKEN_ONCE
case|:
return|return
name|SP11Constants
operator|.
name|INCLUDE_ONCE
return|;
default|default :
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|Version
name|getVersion
parameter_list|()
block|{
return|return
name|Version
operator|.
name|SP_V11
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|SP_NS
return|;
block|}
specifier|public
name|QName
name|getWSS10
parameter_list|()
block|{
return|return
name|WSS10
return|;
block|}
specifier|public
name|QName
name|getWSS11
parameter_list|()
block|{
return|return
name|WSS11
return|;
block|}
specifier|public
name|QName
name|getAlgorithmSuite
parameter_list|()
block|{
return|return
name|ALGORITHM_SUITE
return|;
block|}
specifier|public
name|QName
name|getAsymmetricBinding
parameter_list|()
block|{
return|return
name|ASYMMETRIC_BINDING
return|;
block|}
specifier|public
name|QName
name|getEncryptionToken
parameter_list|()
block|{
return|return
name|ENCRYPTION_TOKEN
return|;
block|}
specifier|public
name|QName
name|getHttpsToken
parameter_list|()
block|{
return|return
name|HTTPS_TOKEN
return|;
block|}
specifier|public
name|QName
name|getInitiatorToken
parameter_list|()
block|{
return|return
name|INITIATOR_TOKEN
return|;
block|}
specifier|public
name|QName
name|getInitiatorSignatureToken
parameter_list|()
block|{
return|return
name|INITIATOR_SIGNATURE_TOKEN
return|;
block|}
specifier|public
name|QName
name|getIssuedToken
parameter_list|()
block|{
return|return
name|ISSUED_TOKEN
return|;
block|}
specifier|public
name|QName
name|getLayout
parameter_list|()
block|{
return|return
name|LAYOUT
return|;
block|}
specifier|public
name|QName
name|getProtectionToken
parameter_list|()
block|{
return|return
name|PROTECTION_TOKEN
return|;
block|}
specifier|public
name|QName
name|getRecipientToken
parameter_list|()
block|{
return|return
name|RECIPIENT_TOKEN
return|;
block|}
specifier|public
name|QName
name|getRequiredElements
parameter_list|()
block|{
return|return
name|REQUIRED_ELEMENTS
return|;
block|}
specifier|public
name|QName
name|getSecureConversationToken
parameter_list|()
block|{
return|return
name|SECURE_CONVERSATION_TOKEN
return|;
block|}
specifier|public
name|QName
name|getSecurityContextToken
parameter_list|()
block|{
return|return
name|SECURITY_CONTEXT_TOKEN
return|;
block|}
specifier|public
name|QName
name|getSignatureToken
parameter_list|()
block|{
return|return
name|SIGNATURE_TOKEN
return|;
block|}
specifier|public
name|QName
name|getSignedElements
parameter_list|()
block|{
return|return
name|SIGNED_ELEMENTS
return|;
block|}
specifier|public
name|QName
name|getEncryptedElements
parameter_list|()
block|{
return|return
name|ENCRYPTED_ELEMENTS
return|;
block|}
specifier|public
name|QName
name|getSignedParts
parameter_list|()
block|{
return|return
name|SIGNED_PARTS
return|;
block|}
specifier|public
name|QName
name|getEncryptedParts
parameter_list|()
block|{
return|return
name|ENCRYPTED_PARTS
return|;
block|}
specifier|public
name|QName
name|getSymmetricBinding
parameter_list|()
block|{
return|return
name|SYMMETRIC_BINDING
return|;
block|}
specifier|public
name|QName
name|getTransportBinding
parameter_list|()
block|{
return|return
name|TRANSPORT_BINDING
return|;
block|}
specifier|public
name|QName
name|getTransportToken
parameter_list|()
block|{
return|return
name|TRANSPORT_TOKEN
return|;
block|}
specifier|public
name|QName
name|getUserNameToken
parameter_list|()
block|{
return|return
name|USERNAME_TOKEN
return|;
block|}
specifier|public
name|QName
name|getSamlToken
parameter_list|()
block|{
return|return
name|SAML_TOKEN
return|;
block|}
specifier|public
name|QName
name|getKerberosToken
parameter_list|()
block|{
return|return
name|KERBEROS_TOKEN
return|;
block|}
specifier|public
name|QName
name|getSpnegoContextToken
parameter_list|()
block|{
return|return
name|SPNEGO_CONTEXT_TOKEN
return|;
block|}
specifier|public
name|QName
name|getX509Token
parameter_list|()
block|{
return|return
name|X509_TOKEN
return|;
block|}
specifier|public
name|QName
name|getSupportingTokens
parameter_list|()
block|{
return|return
name|SUPPORTING_TOKENS
return|;
block|}
specifier|public
name|QName
name|getSignedSupportingTokens
parameter_list|()
block|{
return|return
name|SIGNED_SUPPORTING_TOKENS
return|;
block|}
specifier|public
name|QName
name|getEndorsingSupportingTokens
parameter_list|()
block|{
return|return
name|ENDORSING_SUPPORTING_TOKENS
return|;
block|}
specifier|public
name|QName
name|getSignedEndorsingSupportingTokens
parameter_list|()
block|{
return|return
name|SIGNED_ENDORSING_SUPPORTING_TOKENS
return|;
block|}
specifier|public
name|QName
name|getIncludeToken
parameter_list|()
block|{
return|return
name|INCLUDE_TOKEN
return|;
block|}
specifier|public
name|QName
name|getRequiredDerivedKeys
parameter_list|()
block|{
return|return
name|REQUIRE_DERIVED_KEYS
return|;
block|}
specifier|public
name|QName
name|getIncludeTimestamp
parameter_list|()
block|{
return|return
name|INCLUDE_TIMESTAMP
return|;
block|}
block|}
end_class

end_unit

