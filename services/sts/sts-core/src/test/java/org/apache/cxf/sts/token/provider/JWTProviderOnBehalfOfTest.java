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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jws
operator|.
name|JwsJwtCompactConsumer
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JwtConstants
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
name|rs
operator|.
name|security
operator|.
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|QNameConstants
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
name|jwt
operator|.
name|JWTTokenProvider
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|AttributedString
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|secext
operator|.
name|UsernameTokenType
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * Some unit tests for creating JWT Tokens with an OnBehalfOf element.  */
end_comment

begin_class
specifier|public
class|class
name|JWTProviderOnBehalfOfTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Create a JWT Token with OnBehalfOf from a UsernameToken      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTOnBehalfOfUsernameToken
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|UsernameTokenType
name|usernameToken
init|=
operator|new
name|UsernameTokenType
argument_list|()
decl_stmt|;
name|AttributedString
name|username
init|=
operator|new
name|AttributedString
argument_list|()
decl_stmt|;
name|username
operator|.
name|setValue
argument_list|(
literal|"bob"
argument_list|)
expr_stmt|;
name|usernameToken
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|UsernameTokenType
argument_list|>
name|usernameTokenType
init|=
operator|new
name|JAXBElement
argument_list|<
name|UsernameTokenType
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|USERNAME_TOKEN
argument_list|,
name|UsernameTokenType
operator|.
name|class
argument_list|,
name|usernameToken
argument_list|)
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|,
name|usernameTokenType
argument_list|)
decl_stmt|;
comment|//Principal must be set in ReceivedToken/OnBehalfOf
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
operator|.
name|setPrincipal
argument_list|(
operator|new
name|CustomTokenPrincipal
argument_list|(
name|username
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenProvider
operator|.
name|canHandleToken
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|tokenProvider
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
name|String
name|token
init|=
operator|(
name|String
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|token
argument_list|)
expr_stmt|;
comment|// Validate the token
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"bob"
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a JWT Token with OnBehalfOf from a SAML Assertion      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTOnBehalfOfAssertion
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|String
name|user
init|=
literal|"alice"
decl_stmt|;
name|Element
name|saml1Assertion
init|=
name|getSAMLAssertion
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|,
name|saml1Assertion
argument_list|)
decl_stmt|;
comment|//Principal must be set in ReceivedToken/OnBehalfOf
name|providerParameters
operator|.
name|getTokenRequirements
argument_list|()
operator|.
name|getOnBehalfOf
argument_list|()
operator|.
name|setPrincipal
argument_list|(
operator|new
name|CustomTokenPrincipal
argument_list|(
name|user
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenProvider
operator|.
name|canHandleToken
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|TokenProviderResponse
name|providerResponse
init|=
name|tokenProvider
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
name|String
name|token
init|=
operator|(
name|String
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|token
argument_list|)
expr_stmt|;
comment|// Validate the token
name|JwsJwtCompactConsumer
name|jwtConsumer
init|=
operator|new
name|JwsJwtCompactConsumer
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|JwtToken
name|jwt
init|=
name|jwtConsumer
operator|.
name|getJwtToken
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|user
argument_list|,
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_SUBJECT
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Element
name|getSAMLAssertion
parameter_list|(
name|String
name|user
parameter_list|)
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
literal|null
argument_list|)
decl_stmt|;
name|providerParameters
operator|.
name|getKeyRequirements
argument_list|()
operator|.
name|setKeyType
argument_list|(
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
expr_stmt|;
name|providerParameters
operator|.
name|setPrincipal
argument_list|(
operator|new
name|CustomTokenPrincipal
argument_list|(
name|user
argument_list|)
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
name|Object
name|onBehalfOf
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
if|if
condition|(
name|onBehalfOf
operator|!=
literal|null
condition|)
block|{
name|ReceivedToken
name|onBehalfOfToken
init|=
operator|new
name|ReceivedToken
argument_list|(
name|onBehalfOf
argument_list|)
decl_stmt|;
name|onBehalfOfToken
operator|.
name|setState
argument_list|(
name|STATE
operator|.
name|VALID
argument_list|)
expr_stmt|;
name|tokenRequirements
operator|.
name|setOnBehalfOf
argument_list|(
name|onBehalfOfToken
argument_list|)
expr_stmt|;
block|}
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
name|setSignatureCrypto
argument_list|(
name|crypto
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
literal|"keys/stsstore.jks"
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

