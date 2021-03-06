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
name|HashMap
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
name|helpers
operator|.
name|DOMUtils
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
name|sts
operator|.
name|token
operator|.
name|realm
operator|.
name|JWTRealmCodec
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
name|realm
operator|.
name|RealmProperties
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
name|jwt
operator|.
name|JWTTokenValidator
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Some unit tests for validating JWTTokens in different realms  */
end_comment

begin_class
specifier|public
class|class
name|JWTTokenValidatorRealmTest
block|{
specifier|private
specifier|static
name|TokenStore
name|tokenStore
init|=
operator|new
name|DefaultInMemoryTokenStore
argument_list|()
decl_stmt|;
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testRealmA
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create
name|TokenProvider
name|jwtTokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
operator|(
operator|(
name|JWTTokenProvider
operator|)
name|jwtTokenProvider
operator|)
operator|.
name|setSignToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|JWTTokenProvider
operator|)
name|jwtTokenProvider
operator|)
operator|.
name|setRealmMap
argument_list|(
name|getRealms
argument_list|()
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|()
decl_stmt|;
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jwtTokenProvider
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
name|jwtTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|providerResponse
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
name|assertTrue
argument_list|(
name|token
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
operator|.
name|length
operator|==
literal|3
argument_list|)
expr_stmt|;
comment|// Validate the token - no realm is returned
name|TokenValidator
name|jwtTokenValidator
init|=
operator|new
name|JWTTokenValidator
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
comment|// Create a ValidateTarget consisting of a JWT Token
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|createTokenWrapper
argument_list|(
name|token
argument_list|)
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
name|jwtTokenValidator
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
name|jwtTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
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
name|assertNull
argument_list|(
name|validatorResponse
operator|.
name|getTokenRealm
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now set the JWTRealmCodec implementation on the Validator
operator|(
operator|(
name|JWTTokenValidator
operator|)
name|jwtTokenValidator
operator|)
operator|.
name|setRealmCodec
argument_list|(
operator|new
name|IssuerJWTRealmCodec
argument_list|()
argument_list|)
expr_stmt|;
name|validatorResponse
operator|=
name|jwtTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
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
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|validatorResponse
operator|.
name|getTokenRealm
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
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testRealmB
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create
name|TokenProvider
name|jwtTokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
operator|(
operator|(
name|JWTTokenProvider
operator|)
name|jwtTokenProvider
operator|)
operator|.
name|setSignToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|(
operator|(
name|JWTTokenProvider
operator|)
name|jwtTokenProvider
operator|)
operator|.
name|setRealmMap
argument_list|(
name|getRealms
argument_list|()
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|()
decl_stmt|;
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jwtTokenProvider
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
name|jwtTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|providerResponse
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
name|assertTrue
argument_list|(
name|token
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
operator|.
name|length
operator|==
literal|3
argument_list|)
expr_stmt|;
comment|// Validate the token - no realm is returned
name|TokenValidator
name|jwtTokenValidator
init|=
operator|new
name|JWTTokenValidator
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
comment|// Create a ValidateTarget consisting of a JWT Token
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|createTokenWrapper
argument_list|(
name|token
argument_list|)
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
name|jwtTokenValidator
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
name|jwtTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
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
name|assertNull
argument_list|(
name|validatorResponse
operator|.
name|getTokenRealm
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now set the JWTRealmCodec implementation on the Validator
operator|(
operator|(
name|JWTTokenValidator
operator|)
name|jwtTokenValidator
operator|)
operator|.
name|setRealmCodec
argument_list|(
operator|new
name|IssuerJWTRealmCodec
argument_list|()
argument_list|)
expr_stmt|;
name|validatorResponse
operator|=
name|jwtTokenValidator
operator|.
name|validateToken
argument_list|(
name|validatorParameters
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|validatorResponse
operator|.
name|getToken
argument_list|()
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
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|validatorResponse
operator|.
name|getTokenRealm
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
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RealmProperties
argument_list|>
name|getRealms
parameter_list|()
block|{
comment|// Create Realms
name|Map
argument_list|<
name|String
argument_list|,
name|RealmProperties
argument_list|>
name|realms
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|RealmProperties
name|realm
init|=
operator|new
name|RealmProperties
argument_list|()
decl_stmt|;
name|realm
operator|.
name|setIssuer
argument_list|(
literal|"A-Issuer"
argument_list|)
expr_stmt|;
name|realms
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
name|realm
argument_list|)
expr_stmt|;
name|realm
operator|=
operator|new
name|RealmProperties
argument_list|()
expr_stmt|;
name|realm
operator|.
name|setIssuer
argument_list|(
literal|"B-Issuer"
argument_list|)
expr_stmt|;
name|realms
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
name|realm
argument_list|)
expr_stmt|;
return|return
name|realms
return|;
block|}
specifier|private
name|TokenProviderParameters
name|createProviderParameters
parameter_list|()
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
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
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
name|setTokenStore
argument_list|(
name|tokenStore
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
name|stsProperties
operator|.
name|setEncryptionCrypto
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
name|setCallbackHandler
argument_list|(
operator|new
name|PasswordCallbackHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|parameters
return|;
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
specifier|private
name|Element
name|createTokenWrapper
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|getEmptyDocument
argument_list|()
decl_stmt|;
name|Element
name|tokenWrapper
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
literal|null
argument_list|,
literal|"TokenWrapper"
argument_list|)
decl_stmt|;
name|tokenWrapper
operator|.
name|setTextContent
argument_list|(
name|token
argument_list|)
expr_stmt|;
return|return
name|tokenWrapper
return|;
block|}
comment|/**      * This class returns a realm associated with a JWTToken depending on the issuer.      */
specifier|private
specifier|static
class|class
name|IssuerJWTRealmCodec
implements|implements
name|JWTRealmCodec
block|{
specifier|public
name|String
name|getRealmFromToken
parameter_list|(
name|JwtToken
name|token
parameter_list|)
block|{
if|if
condition|(
literal|"A-Issuer"
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUER
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|"A"
return|;
block|}
elseif|else
if|if
condition|(
literal|"B-Issuer"
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUER
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|"B"
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

