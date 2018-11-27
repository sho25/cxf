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
name|wss4j
operator|.
name|common
operator|.
name|WSS4JConstants
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

begin_comment
comment|/**  * Some unit tests for creating SAML Tokens via the SAMLTokenProvider in different realms  */
end_comment

begin_class
specifier|public
class|class
name|SAMLProviderRealmTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Test that a SAML 1.1 Bearer Assertion is created in specific realms.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testRealms
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
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
comment|// Create Realms
name|Map
argument_list|<
name|String
argument_list|,
name|RealmProperties
argument_list|>
name|samlRealms
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|RealmProperties
name|samlRealm
init|=
operator|new
name|RealmProperties
argument_list|()
decl_stmt|;
name|samlRealm
operator|.
name|setIssuer
argument_list|(
literal|"A-Issuer"
argument_list|)
expr_stmt|;
name|samlRealms
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
name|samlRealm
argument_list|)
expr_stmt|;
name|samlRealm
operator|=
operator|new
name|RealmProperties
argument_list|()
expr_stmt|;
name|samlRealm
operator|.
name|setIssuer
argument_list|(
literal|"B-Issuer"
argument_list|)
expr_stmt|;
name|samlRealms
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
name|samlRealm
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setRealmMap
argument_list|(
name|samlRealms
argument_list|)
expr_stmt|;
comment|// Realm "A"
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
literal|"A"
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
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"A-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"B-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"STS"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Realm "B"
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|providerResponse
operator|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
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
name|token
operator|=
operator|(
name|Element
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|tokenString
operator|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
expr_stmt|;
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
literal|"A-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"B-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"STS"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Default Realm
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|providerResponse
operator|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
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
name|token
operator|=
operator|(
name|Element
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|tokenString
operator|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
expr_stmt|;
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
literal|"A-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"B-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"STS"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testRealmsUsingOldRealmClass
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
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
argument_list|)
decl_stmt|;
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
comment|// Create Realms
name|Map
argument_list|<
name|String
argument_list|,
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
name|SAMLRealm
argument_list|>
name|samlRealms
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
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
name|SAMLRealm
name|samlRealm
init|=
operator|new
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
name|SAMLRealm
argument_list|()
decl_stmt|;
name|samlRealm
operator|.
name|setIssuer
argument_list|(
literal|"A-Issuer"
argument_list|)
expr_stmt|;
name|samlRealms
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
name|samlRealm
argument_list|)
expr_stmt|;
name|samlRealm
operator|=
operator|new
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
name|SAMLRealm
argument_list|()
expr_stmt|;
name|samlRealm
operator|.
name|setIssuer
argument_list|(
literal|"B-Issuer"
argument_list|)
expr_stmt|;
name|samlRealms
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
name|samlRealm
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SAMLTokenProvider
operator|)
name|samlTokenProvider
operator|)
operator|.
name|setRealmMap
argument_list|(
name|samlRealms
argument_list|)
expr_stmt|;
comment|// Realm "A"
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
literal|"A"
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
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"A-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"B-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"STS"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Realm "B"
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|providerResponse
operator|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
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
name|token
operator|=
operator|(
name|Element
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|tokenString
operator|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
expr_stmt|;
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
literal|"A-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"B-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"STS"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Default Realm
name|providerParameters
operator|.
name|setRealm
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|samlTokenProvider
operator|.
name|canHandleToken
argument_list|(
name|WSS4JConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|providerResponse
operator|=
name|samlTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
expr_stmt|;
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
name|token
operator|=
operator|(
name|Element
operator|)
name|providerResponse
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|tokenString
operator|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|token
argument_list|)
expr_stmt|;
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
literal|"A-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"B-Issuer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"STS"
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

