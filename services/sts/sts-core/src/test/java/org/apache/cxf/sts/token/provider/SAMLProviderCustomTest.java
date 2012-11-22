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
operator|.
name|token
operator|.
name|provider
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|jaxws
operator|.
name|context
operator|.
name|WebServiceContextImpl
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
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
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
name|MessageImpl
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
name|STSConstants
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
name|StaticSTSProperties
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
name|common
operator|.
name|CustomAttributeProvider
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
name|common
operator|.
name|PasswordCallbackHandler
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
name|request
operator|.
name|KeyRequirements
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
name|request
operator|.
name|TokenRequirements
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
name|ws
operator|.
name|security
operator|.
name|CustomTokenPrincipal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|components
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
name|ws
operator|.
name|security
operator|.
name|components
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
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|builder
operator|.
name|SAML1Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|DOM2Writer
import|;
end_import

begin_comment
comment|/**  * Some unit tests for creating custom SAML Tokens.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLProviderCustomTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Create a custom Saml1 Attribute Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml1AttributeAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AttributeStatementProvider
argument_list|>
name|customProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAttributeProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAttributeStatementProviders
argument_list|(
name|customProviderList
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthenticationStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"http://cxf.apache.org/sts/custom"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a custom Saml2 Authentication Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml2AuthenticationAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
name|customProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAuthenticationProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAuthenticationStatementProviders
argument_list|(
name|customProviderList
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthnStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|SAML2Constants
operator|.
name|AUTH_CONTEXT_CLASS_REF_X509
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a custom Saml1 Authentication Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml1AuthenticationAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
name|customProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAuthenticationProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAuthenticationStatementProviders
argument_list|(
name|customProviderList
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthenticationStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|SAML1Constants
operator|.
name|AUTH_METHOD_X509
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a custom Saml2 Authentication and Attribute Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml2CombinedAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
name|customProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AuthenticationStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAuthenticationProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAuthenticationStatementProviders
argument_list|(
name|customProviderList
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AttributeStatementProvider
argument_list|>
name|customAttributeProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customAttributeProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAttributeProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAttributeStatementProviders
argument_list|(
name|customAttributeProviderList
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthnStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a custom Saml1 (Multiple) Attribute Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml1MultipleAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AttributeStatementProvider
argument_list|>
name|customProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AttributeStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAttributeProvider
argument_list|()
argument_list|)
expr_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAttributeProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAttributeStatementProviders
argument_list|(
name|customProviderList
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthenticationStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"http://cxf.apache.org/sts/custom"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a custom Saml2 AuthDecision Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml2AuthDecisionAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AuthDecisionStatementProvider
argument_list|>
name|customProviderList
init|=
operator|new
name|ArrayList
argument_list|<
name|AuthDecisionStatementProvider
argument_list|>
argument_list|()
decl_stmt|;
name|customProviderList
operator|.
name|add
argument_list|(
operator|new
name|CustomAuthDecisionProvider
argument_list|()
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setAuthDecisionStatementProviders
argument_list|(
name|customProviderList
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthnStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthzDecisionStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a Saml1 Attribute Assertion with a custom Subject      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml1SubjectAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setSubjectProvider
argument_list|(
operator|new
name|CustomSubjectProvider
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthenticationStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"http://cxf.apache.org/sts/custom"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a Saml1 Assertion with a custom NameID Format of the Subject      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCustomSaml1SubjectNameIDFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|DefaultSubjectProvider
name|subjectProvider
init|=
operator|new
name|DefaultSubjectProvider
argument_list|()
decl_stmt|;
name|subjectProvider
operator|.
name|setSubjectNameIDFormat
argument_list|(
name|SAML1Constants
operator|.
name|NAMEID_FORMAT_EMAIL_ADDRESS
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setSubjectProvider
argument_list|(
name|subjectProvider
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
operator|&&
name|providerResponse
operator|.
name|getTokenId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|providerResponse
operator|.
name|getTokenId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AuthenticationStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|SAML1Constants
operator|.
name|NAMEID_FORMAT_EMAIL_ADDRESS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TokenProviderParameters
name|createProviderParameters
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|TokenProviderParameters
name|parameters
init|=
operator|new
name|TokenProviderParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
operator|new
name|TokenRequirements
argument_list|()
decl_stmt|;
name|tokenRequirements
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setTokenRequirements
argument_list|(
name|tokenRequirements
argument_list|)
expr_stmt|;
name|KeyRequirements
name|keyRequirements
init|=
operator|new
name|KeyRequirements
argument_list|()
decl_stmt|;
name|keyRequirements
operator|.
name|setKeyType
argument_list|(
name|keyType
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setKeyRequirements
argument_list|(
name|keyRequirements
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setPrincipal
argument_list|(
operator|new
name|CustomTokenPrincipal
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Mock up message context
name|MessageImpl
name|msg
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|WrappedMessageContext
name|msgCtx
init|=
operator|new
name|WrappedMessageContext
argument_list|(
name|msg
argument_list|)
decl_stmt|;
name|WebServiceContextImpl
name|webServiceContext
init|=
operator|new
name|WebServiceContextImpl
argument_list|(
name|msgCtx
argument_list|)
decl_stmt|;
name|parameters
operator|.
name|setWebServiceContext
argument_list|(
name|webServiceContext
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setAppliesToAddress
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
expr_stmt|;
comment|// Add STSProperties object
name|StaticSTSProperties
name|stsProperties
init|=
operator|new
name|StaticSTSProperties
argument_list|()
decl_stmt|;
name|Crypto
name|crypto
init|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|getEncryptionProperties
argument_list|()
argument_list|)
decl_stmt|;
name|stsProperties
operator|.
name|setEncryptionCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setSignatureCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setEncryptionUsername
argument_list|(
literal|"myservicekey"
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setSignatureUsername
argument_list|(
literal|"mystskey"
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setCallbackHandler
argument_list|(
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setIssuer
argument_list|(
literal|"STS"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setStsProperties
argument_list|(
name|stsProperties
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setEncryptionProperties
argument_list|(
operator|new
name|EncryptionProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
specifier|private
name|Properties
name|getEncryptionProperties
parameter_list|()
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.provider"
argument_list|,
literal|"org.apache.ws.security.components.crypto.Merlin"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.merlin.keystore.password"
argument_list|,
literal|"stsspass"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.merlin.keystore.file"
argument_list|,
literal|"stsstore.jks"
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

