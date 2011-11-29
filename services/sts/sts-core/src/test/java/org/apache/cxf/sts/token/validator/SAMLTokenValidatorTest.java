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
name|validator
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
name|security
operator|.
name|Principal
import|;
end_import

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
name|Properties
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|cache
operator|.
name|DefaultInMemoryTokenStore
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
name|cache
operator|.
name|STSTokenStore
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
name|Lifetime
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
name|ReceivedToken
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
name|cxf
operator|.
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|DefaultConditionsProvider
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
name|token
operator|.
name|provider
operator|.
name|SAMLTokenProvider
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
name|token
operator|.
name|provider
operator|.
name|TokenProvider
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
name|token
operator|.
name|provider
operator|.
name|TokenProviderParameters
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
name|token
operator|.
name|provider
operator|.
name|TokenProviderResponse
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
name|WSPasswordCallback
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
name|util
operator|.
name|XmlSchemaDateFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * Some unit tests for validating a SAML token via the SAMLTokenValidator.  */
end_comment

begin_class
specifier|public
class|class
name|SAMLTokenValidatorTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|private
specifier|static
name|STSTokenStore
name|tokenStore
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
name|tokenStore
operator|=
operator|new
name|DefaultInMemoryTokenStore
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test a valid SAML 1.1 Assertion      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidSAML1Assertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
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
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|PasswordCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"mystskey"
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|Principal
name|principal
init|=
name|validatorResponse
operator|.
name|getPrincipal
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|principal
operator|!=
literal|null
operator|&&
name|principal
operator|.
name|getName
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a valid SAML 2 Assertion      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidSAML2Assertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
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
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|PasswordCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"mystskey"
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|Principal
name|principal
init|=
name|validatorResponse
operator|.
name|getPrincipal
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|principal
operator|!=
literal|null
operator|&&
name|principal
operator|.
name|getName
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a SAML 1.1 Assertion with an invalid signature      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidSignatureSAML1Assertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
name|Crypto
name|crypto
init|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|getEveCryptoProperties
argument_list|()
argument_list|)
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|EveCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"eve"
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set tokenstore to null so that issued token is not found in the cache
name|validatorParameters
operator|.
name|setTokenStore
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a SAML 2 Assertion with an invalid signature      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidSignatureSAML2Assertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
name|Crypto
name|crypto
init|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|getEveCryptoProperties
argument_list|()
argument_list|)
decl_stmt|;
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|EveCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"eve"
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set tokenstore to null so that issued token is not found in the cache
name|validatorParameters
operator|.
name|setTokenStore
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a SAML 1.1 Assertion with an invalid condition      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidConditionSAML1Assertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
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
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|PasswordCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"mystskey"
argument_list|,
name|callbackHandler
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"SAML token is invalid"
argument_list|,
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a SAML 2.0 Assertion with an invalid condition      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidConditionSAML2Assertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
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
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|PasswordCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"mystskey"
argument_list|,
name|callbackHandler
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"SAML token is invalid"
argument_list|,
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a SAML 1.1 Assertion using Certificate Constraints       */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSAML1AssertionCertConstraints
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|samlTokenValidator
init|=
operator|new
name|SAMLTokenValidator
argument_list|()
decl_stmt|;
name|TokenValidatorParameters
name|validatorParameters
init|=
name|createValidatorParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
name|validatorParameters
operator|.
name|setTokenStore
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Create a ValidateTarget consisting of a SAML Assertion
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
name|CallbackHandler
name|callbackHandler
init|=
operator|new
name|PasswordCallbackHandler
argument_list|()
decl_stmt|;
name|Element
name|samlToken
init|=
name|createSAMLAssertion
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|crypto
argument_list|,
literal|"mystskey"
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|samlToken
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|samlToken
operator|=
operator|(
name|Element
operator|)
name|doc
operator|.
name|appendChild
argument_list|(
name|samlToken
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|samlToken
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setValidateTarget
argument_list|(
name|validateTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|certConstraints
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|certConstraints
operator|.
name|add
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
name|certConstraints
operator|.
name|add
argument_list|(
literal|".*CN=www.sts.com.*"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenValidator
operator|)
name|samlTokenValidator
operator|)
operator|.
name|setSubjectConstraints
argument_list|(
name|certConstraints
argument_list|)
expr_stmt|;
name|TokenValidatorResponse
name|validatorResponse
init|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|certConstraints
operator|.
name|clear
argument_list|()
expr_stmt|;
name|certConstraints
operator|.
name|add
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenValidator
operator|)
name|samlTokenValidator
operator|)
operator|.
name|setSubjectConstraints
argument_list|(
name|certConstraints
argument_list|)
expr_stmt|;
name|validatorResponse
operator|=
name|samlTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|validatorResponse
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TokenValidatorParameters
name|createValidatorParameters
parameter_list|()
throws|throws
name|WSSecurityException
block|{
name|TokenValidatorParameters
name|parameters
init|=
operator|new
name|TokenValidatorParameters
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
name|STSConstants
operator|.
name|STATUS
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
name|setTokenStore
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
block|}
specifier|private
name|Element
name|createSAMLAssertion
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|signatureUsername
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|)
throws|throws
name|WSSecurityException
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
name|tokenType
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|,
name|crypto
argument_list|,
name|signatureUsername
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
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
return|return
name|providerResponse
operator|.
name|getToken
argument_list|()
return|;
block|}
specifier|private
name|Element
name|createSAMLAssertion
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|signatureUsername
parameter_list|,
name|CallbackHandler
name|callbackHandler
parameter_list|,
name|long
name|ttlMs
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|SAMLTokenProvider
name|samlTokenProvider
init|=
operator|new
name|SAMLTokenProvider
argument_list|()
decl_stmt|;
name|DefaultConditionsProvider
name|conditionsProvider
init|=
operator|new
name|DefaultConditionsProvider
argument_list|()
decl_stmt|;
name|conditionsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|samlTokenProvider
operator|.
name|setConditionsProvider
argument_list|(
name|conditionsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|tokenType
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|,
name|crypto
argument_list|,
name|signatureUsername
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttlMs
operator|!=
literal|0
condition|)
block|{
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|Date
name|creationTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|Date
name|expirationTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|expirationTime
operator|.
name|setTime
argument_list|(
name|creationTime
operator|.
name|getTime
argument_list|()
operator|+
name|ttlMs
argument_list|)
expr_stmt|;
name|XmlSchemaDateFormat
name|fmt
init|=
operator|new
name|XmlSchemaDateFormat
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|creationTime
argument_list|)
argument_list|)
expr_stmt|;
name|lifetime
operator|.
name|setExpires
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|expirationTime
argument_list|)
argument_list|)
expr_stmt|;
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|setLifetime
argument_list|(
name|lifetime
argument_list|)
expr_stmt|;
block|}
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
return|return
name|providerResponse
operator|.
name|getToken
argument_list|()
return|;
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
parameter_list|,
name|Crypto
name|crypto
parameter_list|,
name|String
name|signatureUsername
parameter_list|,
name|CallbackHandler
name|callbackHandler
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
name|stsProperties
operator|.
name|setSignatureCrypto
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setSignatureUsername
argument_list|(
name|signatureUsername
argument_list|)
expr_stmt|;
name|stsProperties
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
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
name|parameters
operator|.
name|setTokenStore
argument_list|(
name|tokenStore
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
specifier|private
name|Properties
name|getEveCryptoProperties
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
literal|"evespass"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.ws.security.crypto.merlin.keystore.file"
argument_list|,
literal|"eve.jks"
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
specifier|public
class|class
name|EveCallbackHandler
implements|implements
name|CallbackHandler
block|{
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
comment|// CXF
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
if|if
condition|(
literal|"eve"
operator|.
name|equals
argument_list|(
name|pc
operator|.
name|getIdentifier
argument_list|()
argument_list|)
condition|)
block|{
name|pc
operator|.
name|setPassword
argument_list|(
literal|"evekpass"
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

