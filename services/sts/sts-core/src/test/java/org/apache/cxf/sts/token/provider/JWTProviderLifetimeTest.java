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
name|Date
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
name|DefaultJWTClaimsProvider
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
name|STSException
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
name|util
operator|.
name|XmlSchemaDateFormat
import|;
end_import

begin_comment
comment|/**  * Some unit tests for creating JWT Tokens with lifetime  */
end_comment

begin_class
specifier|public
class|class
name|JWTProviderLifetimeTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Issue JWT token with a valid requested lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTValidLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|requestedLifetime
init|=
literal|60
decl_stmt|;
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
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
operator|(
name|requestedLifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
name|assertEquals
argument_list|(
name|requestedLifetime
operator|*
literal|1000L
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
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
name|assertEquals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|,
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Issue JWT token with a lifetime configured in JWTTokenProvider      * No specific lifetime requested      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTProviderLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|providerLifetime
init|=
literal|10
operator|*
literal|600L
decl_stmt|;
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setLifetime
argument_list|(
name|providerLifetime
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
name|providerLifetime
operator|*
literal|1000L
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
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
name|assertEquals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|,
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Issue JWT token with a with a lifetime      * which exceeds configured maximum lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTExceededConfiguredMaxLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|maxLifetime
init|=
literal|30
operator|*
literal|60L
decl_stmt|;
comment|// 30 minutes
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setMaxLifetime
argument_list|(
name|maxLifetime
argument_list|)
expr_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 35 minutes
name|long
name|requestedLifetime
init|=
literal|35
operator|*
literal|60L
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
operator|(
name|requestedLifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
try|try
block|{
name|tokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected due to exceeded lifetime"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|STSException
name|ex
parameter_list|)
block|{
comment|//expected
block|}
block|}
comment|/**      * Issue JWT token with a with a lifetime      * which exceeds default maximum lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTExceededDefaultMaxLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to Default max lifetime plus 1
name|long
name|requestedLifetime
init|=
name|DefaultConditionsProvider
operator|.
name|DEFAULT_MAX_LIFETIME
operator|+
literal|1
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
operator|(
name|requestedLifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
try|try
block|{
name|tokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected due to exceeded lifetime"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|STSException
name|ex
parameter_list|)
block|{
comment|//expected
block|}
block|}
comment|/**      * Issue JWT token with a with a lifetime      * which exceeds configured maximum lifetime      * Lifetime reduced to maximum lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTExceededConfiguredMaxLifetimeButUpdated
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|maxLifetime
init|=
literal|30
operator|*
literal|60L
decl_stmt|;
comment|// 30 minutes
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setMaxLifetime
argument_list|(
name|maxLifetime
argument_list|)
expr_stmt|;
name|claimsProvider
operator|.
name|setFailLifetimeExceedance
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 35 minutes
name|long
name|requestedLifetime
init|=
literal|35
operator|*
literal|60L
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
operator|(
name|requestedLifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
name|assertEquals
argument_list|(
name|maxLifetime
operator|*
literal|1000L
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
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
name|assertEquals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|,
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Issue JWT token with a near future Created Lifetime. This should pass as we allow a future      * dated Lifetime up to 60 seconds to avoid clock skew problems.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTNearFutureCreatedLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|requestedLifetime
init|=
literal|60
decl_stmt|;
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
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
operator|(
name|requestedLifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|creationTime
operator|.
name|setTime
argument_list|(
name|creationTime
operator|.
name|getTime
argument_list|()
operator|+
operator|(
literal|10
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|50L
operator|*
literal|1000L
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
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
name|assertEquals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|,
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Issue JWT token with a future Created Lifetime. This should fail as we only allow a future      * dated Lifetime up to 60 seconds to avoid clock skew problems.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTFarFutureCreatedLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|requestedLifetime
init|=
literal|60
decl_stmt|;
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
name|Date
name|creationTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|creationTime
operator|.
name|setTime
argument_list|(
name|creationTime
operator|.
name|getTime
argument_list|()
operator|+
operator|(
literal|60L
operator|*
literal|2L
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
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
operator|(
name|requestedLifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
try|try
block|{
name|tokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a Created Element too far in the future"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|STSException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// Now allow this sort of Created Element
name|claimsProvider
operator|.
name|setFutureTimeToLive
argument_list|(
literal|60L
operator|*
literal|60L
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
name|assertEquals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|,
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
block|}
comment|/**      * Issue JWT token with no Expires element. This will be rejected, but will default to the      * configured TTL and so the request will pass.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testJWTNoExpires
parameter_list|()
throws|throws
name|Exception
block|{
name|JWTTokenProvider
name|tokenProvider
init|=
operator|new
name|JWTTokenProvider
argument_list|()
decl_stmt|;
name|DefaultJWTClaimsProvider
name|claimsProvider
init|=
operator|new
name|DefaultJWTClaimsProvider
argument_list|()
decl_stmt|;
name|claimsProvider
operator|.
name|setAcceptClientLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tokenProvider
operator|.
name|setJwtClaimsProvider
argument_list|(
name|claimsProvider
argument_list|)
expr_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
name|Date
name|creationTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|creationTime
operator|.
name|setTime
argument_list|(
name|creationTime
operator|.
name|getTime
argument_list|()
operator|+
operator|(
literal|60L
operator|*
literal|2L
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
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
name|assertEquals
argument_list|(
name|claimsProvider
operator|.
name|getLifetime
argument_list|()
operator|*
literal|1000L
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
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
name|assertEquals
argument_list|(
name|jwt
operator|.
name|getClaim
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_ISSUED_AT
argument_list|)
argument_list|,
name|providerResponse
operator|.
name|getCreated
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TokenProviderParameters
name|createProviderParameters
parameter_list|(
name|String
name|tokenType
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
