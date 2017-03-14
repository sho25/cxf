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
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|common
operator|.
name|util
operator|.
name|DOM2Writer
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
name|util
operator|.
name|DateUtil
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

begin_comment
comment|/**  * Some unit tests for creating SAML Tokens with lifetime  */
end_comment

begin_class
specifier|public
class|class
name|SAMLProviderLifetimeTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Issue SAML 2 token with a valid requested lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ValidLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|requestedLifetime
init|=
literal|60
decl_stmt|;
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|ZonedDateTime
name|expirationTime
init|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|requestedLifetime
argument_list|)
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|long
name|duration
init|=
name|Duration
operator|.
name|between
argument_list|(
name|providerResponse
operator|.
name|getCreated
argument_list|()
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|requestedLifetime
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
operator|(
name|Element
operator|)
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
block|}
comment|/**      * Issue SAML 2 token with a lifetime configured in SAMLTokenProvider      * No specific lifetime requested      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ProviderLifetime
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
name|setLifetime
argument_list|(
name|providerLifetime
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
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
name|long
name|duration
init|=
name|Duration
operator|.
name|between
argument_list|(
name|providerResponse
operator|.
name|getCreated
argument_list|()
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|providerLifetime
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
operator|(
name|Element
operator|)
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
block|}
comment|/**      * Issue SAML 2 token with a with a lifetime      * which exceeds configured maximum lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ExceededConfiguredMaxLifetime
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
name|setMaxLifetime
argument_list|(
name|maxLifetime
argument_list|)
expr_stmt|;
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 35 minutes
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|long
name|requestedLifetime
init|=
literal|35
operator|*
literal|60L
decl_stmt|;
name|ZonedDateTime
name|expirationTime
init|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|requestedLifetime
argument_list|)
decl_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
try|try
block|{
name|samlTokenProvider
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
comment|/**      * Issue SAML 2 token with a with a lifetime      * which exceeds default maximum lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ExceededDefaultMaxLifetime
parameter_list|()
throws|throws
name|Exception
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to Default max lifetime plus 1
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|long
name|requestedLifetime
init|=
name|DefaultConditionsProvider
operator|.
name|DEFAULT_MAX_LIFETIME
operator|+
literal|1
decl_stmt|;
name|ZonedDateTime
name|expirationTime
init|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|requestedLifetime
argument_list|)
decl_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
try|try
block|{
name|samlTokenProvider
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
comment|/**      * Issue SAML 2 token with a with a lifetime      * which exceeds configured maximum lifetime      * Lifetime reduced to maximum lifetime      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2ExceededConfiguredMaxLifetimeButUpdated
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
name|setMaxLifetime
argument_list|(
name|maxLifetime
argument_list|)
expr_stmt|;
name|conditionsProvider
operator|.
name|setFailLifetimeExceedance
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 35 minutes
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|long
name|requestedLifetime
init|=
literal|35
operator|*
literal|60L
decl_stmt|;
name|ZonedDateTime
name|expirationTime
init|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|requestedLifetime
argument_list|)
decl_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|long
name|duration
init|=
name|Duration
operator|.
name|between
argument_list|(
name|providerResponse
operator|.
name|getCreated
argument_list|()
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|maxLifetime
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
operator|(
name|Element
operator|)
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
block|}
comment|/**      * Issue SAML 2 token with a near future Created Lifetime. This should pass as we allow a future      * dated Lifetime up to 60 seconds to avoid clock skew problems.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2NearFutureCreatedLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|requestedLifetime
init|=
literal|60
decl_stmt|;
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|ZonedDateTime
name|expirationTime
init|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|requestedLifetime
argument_list|)
decl_stmt|;
name|creationTime
operator|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
literal|10L
argument_list|)
expr_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|long
name|duration
init|=
name|Duration
operator|.
name|between
argument_list|(
name|providerResponse
operator|.
name|getCreated
argument_list|()
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
operator|(
name|Element
operator|)
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
block|}
comment|/**      * Issue SAML 2 token with a future Created Lifetime. This should fail as we only allow a future      * dated Lifetime up to 60 seconds to avoid clock skew problems.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2FarFutureCreatedLifetime
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|requestedLifetime
init|=
literal|60
decl_stmt|;
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
operator|.
name|plusSeconds
argument_list|(
literal|120L
argument_list|)
decl_stmt|;
name|ZonedDateTime
name|expirationTime
init|=
name|creationTime
operator|.
name|plusSeconds
argument_list|(
name|requestedLifetime
argument_list|)
decl_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
try|try
block|{
name|samlTokenProvider
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
name|conditionsProvider
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
operator|(
name|Element
operator|)
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
block|}
comment|/**      * Issue SAML 2 token with no Expires element. This will be rejected, but will default to the      * configured TTL and so the request will pass.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testSaml2NoExpires
parameter_list|()
throws|throws
name|Exception
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
comment|// Set expected lifetime to 1 minute
name|ZonedDateTime
name|creationTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
operator|.
name|plusSeconds
argument_list|(
literal|120L
argument_list|)
decl_stmt|;
name|Lifetime
name|lifetime
init|=
operator|new
name|Lifetime
argument_list|()
decl_stmt|;
name|lifetime
operator|.
name|setCreated
argument_list|(
name|DateUtil
operator|.
name|getDateTimeFormatter
argument_list|(
literal|true
argument_list|)
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
name|long
name|duration
init|=
name|Duration
operator|.
name|between
argument_list|(
name|providerResponse
operator|.
name|getCreated
argument_list|()
argument_list|,
name|providerResponse
operator|.
name|getExpires
argument_list|()
argument_list|)
operator|.
name|getSeconds
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|conditionsProvider
operator|.
name|getLifetime
argument_list|()
argument_list|,
name|duration
argument_list|)
expr_stmt|;
name|Element
name|token
init|=
operator|(
name|Element
operator|)
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

