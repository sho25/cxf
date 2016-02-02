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
name|renewer
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
name|Collections
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
name|Map
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
name|CallbackHandler
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
name|ReceivedKey
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
name|ReceivedToken
operator|.
name|STATE
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
name|Renewing
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
name|cxf
operator|.
name|sts
operator|.
name|token
operator|.
name|validator
operator|.
name|SAMLTokenValidator
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
name|validator
operator|.
name|TokenValidator
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
name|validator
operator|.
name|TokenValidatorParameters
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
name|validator
operator|.
name|TokenValidatorResponse
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
name|CryptoFactory
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
name|CryptoType
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
name|common
operator|.
name|principal
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
operator|.
name|engine
operator|.
name|WSSecurityEngineResult
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
name|dom
operator|.
name|handler
operator|.
name|WSHandlerConstants
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
name|dom
operator|.
name|handler
operator|.
name|WSHandlerResult
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
name|dom
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
comment|/**  * Some unit tests for renewing a SAML token via the SAMLTokenRenewer with proof of possession enabled  * (message level, not TLS).  */
end_comment

begin_class
specifier|public
class|class
name|SAMLTokenRenewerPOPTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|private
specifier|static
name|TokenStore
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
comment|/**      * Renew a valid SAML1 Assertion      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|renewValidSAML1Assertion
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the Assertion
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
literal|50000
argument_list|,
literal|true
argument_list|,
literal|false
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
comment|// Validate the Assertion
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
name|validatorParameters
operator|.
name|setToken
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
name|getToken
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|VALID
argument_list|)
expr_stmt|;
comment|// Renew the Assertion
name|TokenRenewerParameters
name|renewerParameters
init|=
operator|new
name|TokenRenewerParameters
argument_list|()
decl_stmt|;
name|renewerParameters
operator|.
name|setAppliesToAddress
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setStsProperties
argument_list|(
name|validatorParameters
operator|.
name|getStsProperties
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
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
name|renewerParameters
operator|.
name|setMessageContext
argument_list|(
name|validatorParameters
operator|.
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setKeyRequirements
argument_list|(
name|validatorParameters
operator|.
name|getKeyRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setTokenRequirements
argument_list|(
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setTokenStore
argument_list|(
name|validatorParameters
operator|.
name|getTokenStore
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setToken
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|TokenRenewer
name|samlTokenRenewer
init|=
operator|new
name|SAMLTokenRenewer
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|samlTokenRenewer
operator|.
name|canHandleToken
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|samlTokenRenewer
operator|.
name|renewToken
argument_list|(
name|renewerParameters
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on lack of proof of possession"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityEngineResult
name|signedResult
init|=
operator|new
name|WSSecurityEngineResult
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|)
decl_stmt|;
name|CryptoType
name|cryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
argument_list|)
decl_stmt|;
name|cryptoType
operator|.
name|setAlias
argument_list|(
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|signedResult
operator|.
name|put
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATES
argument_list|,
name|crypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
argument_list|)
expr_stmt|;
name|signedResults
operator|.
name|add
argument_list|(
name|signedResult
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|handlerResults
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|WSHandlerResult
name|handlerResult
init|=
operator|new
name|WSHandlerResult
argument_list|(
literal|null
argument_list|,
name|signedResults
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|,
name|signedResults
argument_list|)
argument_list|)
decl_stmt|;
name|handlerResults
operator|.
name|add
argument_list|(
name|handlerResult
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageContext
init|=
name|validatorParameters
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
name|messageContext
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|handlerResults
argument_list|)
expr_stmt|;
comment|// Now successfully renew the token
name|TokenRenewerResponse
name|renewerResponse
init|=
name|samlTokenRenewer
operator|.
name|renewToken
argument_list|(
name|renewerParameters
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|renewerResponse
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|renewerResponse
operator|.
name|getToken
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Renew a valid SAML1 Assertion      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|renewValidSAML1AssertionWrongPOP
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the Assertion
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
literal|50000
argument_list|,
literal|true
argument_list|,
literal|false
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
comment|// Validate the Assertion
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
name|validatorParameters
operator|.
name|setToken
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
name|getToken
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|VALID
argument_list|)
expr_stmt|;
comment|// Renew the Assertion
name|TokenRenewerParameters
name|renewerParameters
init|=
operator|new
name|TokenRenewerParameters
argument_list|()
decl_stmt|;
name|renewerParameters
operator|.
name|setAppliesToAddress
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setStsProperties
argument_list|(
name|validatorParameters
operator|.
name|getStsProperties
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
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
name|renewerParameters
operator|.
name|setMessageContext
argument_list|(
name|validatorParameters
operator|.
name|getMessageContext
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setKeyRequirements
argument_list|(
name|validatorParameters
operator|.
name|getKeyRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setTokenRequirements
argument_list|(
name|validatorParameters
operator|.
name|getTokenRequirements
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setTokenStore
argument_list|(
name|validatorParameters
operator|.
name|getTokenStore
argument_list|()
argument_list|)
expr_stmt|;
name|renewerParameters
operator|.
name|setToken
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|TokenRenewer
name|samlTokenRenewer
init|=
operator|new
name|SAMLTokenRenewer
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|samlTokenRenewer
operator|.
name|canHandleToken
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|samlTokenRenewer
operator|.
name|renewToken
argument_list|(
name|renewerParameters
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on lack of proof of possession"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityEngineResult
name|signedResult
init|=
operator|new
name|WSSecurityEngineResult
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|)
decl_stmt|;
name|CryptoType
name|cryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
argument_list|)
decl_stmt|;
name|cryptoType
operator|.
name|setAlias
argument_list|(
literal|"myservicekey"
argument_list|)
expr_stmt|;
name|signedResult
operator|.
name|put
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_X509_CERTIFICATES
argument_list|,
name|crypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
argument_list|)
expr_stmt|;
name|signedResults
operator|.
name|add
argument_list|(
name|signedResult
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|handlerResults
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|WSHandlerResult
name|handlerResult
init|=
operator|new
name|WSHandlerResult
argument_list|(
literal|null
argument_list|,
name|signedResults
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
name|WSConstants
operator|.
name|SIGN
argument_list|,
name|signedResults
argument_list|)
argument_list|)
decl_stmt|;
name|handlerResults
operator|.
name|add
argument_list|(
name|handlerResult
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|messageContext
init|=
name|validatorParameters
operator|.
name|getMessageContext
argument_list|()
decl_stmt|;
name|messageContext
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|handlerResults
argument_list|)
expr_stmt|;
try|try
block|{
name|samlTokenRenewer
operator|.
name|renewToken
argument_list|(
name|renewerParameters
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected failure on wrong signature key"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// expected
block|}
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
name|parameters
operator|.
name|setMessageContext
argument_list|(
name|msgCtx
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
parameter_list|,
name|long
name|ttlMs
parameter_list|,
name|boolean
name|allowRenewing
parameter_list|,
name|boolean
name|allowRenewingAfterExpiry
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
name|PUBLIC_KEY_KEYTYPE
argument_list|,
name|crypto
argument_list|,
name|signatureUsername
argument_list|,
name|callbackHandler
argument_list|)
decl_stmt|;
name|Renewing
name|renewing
init|=
operator|new
name|Renewing
argument_list|()
decl_stmt|;
name|renewing
operator|.
name|setAllowRenewing
argument_list|(
name|allowRenewing
argument_list|)
expr_stmt|;
name|renewing
operator|.
name|setAllowRenewingAfterExpiry
argument_list|(
name|allowRenewingAfterExpiry
argument_list|)
expr_stmt|;
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|setRenewing
argument_list|(
name|renewing
argument_list|)
expr_stmt|;
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
operator|(
name|Element
operator|)
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
name|ReceivedKey
name|receivedKey
init|=
operator|new
name|ReceivedKey
argument_list|()
decl_stmt|;
name|CryptoType
name|cryptoType
init|=
operator|new
name|CryptoType
argument_list|(
name|CryptoType
operator|.
name|TYPE
operator|.
name|ALIAS
argument_list|)
decl_stmt|;
name|cryptoType
operator|.
name|setAlias
argument_list|(
literal|"myclientkey"
argument_list|)
expr_stmt|;
name|receivedKey
operator|.
name|setX509Cert
argument_list|(
name|crypto
operator|.
name|getX509Certificates
argument_list|(
name|cryptoType
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|keyRequirements
operator|.
name|setReceivedKey
argument_list|(
name|receivedKey
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
name|parameters
operator|.
name|setMessageContext
argument_list|(
name|msgCtx
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
literal|"org.apache.wss4j.crypto.provider"
argument_list|,
literal|"org.apache.wss4j.common.crypto.Merlin"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.wss4j.crypto.merlin.keystore.password"
argument_list|,
literal|"stsspass"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"org.apache.wss4j.crypto.merlin.keystore.file"
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

