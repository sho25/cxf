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
name|operation
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
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|security
operator|.
name|SecurityContext
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
name|STSPropertiesMBean
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
name|common
operator|.
name|TestUtils
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
name|service
operator|.
name|ServiceMBean
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
name|StaticService
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
name|SCTProvider
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
name|RequestSecurityTokenResponseCollectionType
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
name|RequestSecurityTokenResponseType
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
name|RequestSecurityTokenType
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
name|RequestedSecurityTokenType
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|trust
operator|.
name|STSUtils
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
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the issue operation to issue SecurityContextTokens.  */
end_comment

begin_class
specifier|public
class|class
name|IssueSCTUnitTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
specifier|public
specifier|static
specifier|final
name|QName
name|REQUESTED_SECURITY_TOKEN
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedSecurityToken
argument_list|(
literal|null
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|ATTACHED_REFERENCE
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedAttachedReference
argument_list|(
literal|null
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|QName
name|UNATTACHED_REFERENCE
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestedUnattachedReference
argument_list|(
literal|null
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|TokenStore
name|tokenStore
init|=
operator|new
name|DefaultInMemoryTokenStore
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|unrestrictedPoliciesInstalled
decl_stmt|;
static|static
block|{
name|unrestrictedPoliciesInstalled
operator|=
name|TestUtils
operator|.
name|checkUnrestrictedPoliciesInstalled
argument_list|()
expr_stmt|;
block|}
empty_stmt|;
comment|/**      * Test to successfully issue a SecurityContextToken      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testIssueSCT
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenIssueOperation
name|issueOperation
init|=
operator|new
name|TokenIssueOperation
argument_list|()
decl_stmt|;
name|issueOperation
operator|.
name|setTokenStore
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
comment|// Add Token Provider
name|List
argument_list|<
name|TokenProvider
argument_list|>
name|providerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providerList
operator|.
name|add
argument_list|(
operator|new
name|SCTProvider
argument_list|()
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setTokenProviders
argument_list|(
name|providerList
argument_list|)
expr_stmt|;
comment|// Add Service
name|ServiceMBean
name|service
init|=
operator|new
name|StaticService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setEndpoints
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setServices
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add STSProperties object
name|STSPropertiesMBean
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
name|issueOperation
operator|.
name|setStsProperties
argument_list|(
name|stsProperties
argument_list|)
expr_stmt|;
comment|// Mock up a request
name|RequestSecurityTokenType
name|request
init|=
operator|new
name|RequestSecurityTokenType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|tokenType
init|=
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|TOKEN_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_12
argument_list|)
decl_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|createAppliesToElement
argument_list|(
literal|"http://dummy-service.com/dummy"
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
name|Principal
name|principal
init|=
operator|new
name|CustomTokenPrincipal
argument_list|(
literal|"alice"
argument_list|)
decl_stmt|;
name|msgCtx
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|createSecurityContext
argument_list|(
name|principal
argument_list|)
argument_list|)
expr_stmt|;
comment|// Issue a token
name|RequestSecurityTokenResponseCollectionType
name|response
init|=
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|msgCtx
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RequestSecurityTokenResponseType
argument_list|>
name|securityTokenResponse
init|=
name|response
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|securityTokenResponse
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test the generated token.
name|Element
name|securityContextToken
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|tokenObject
range|:
name|securityTokenResponse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|tokenObject
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|&&
name|REQUESTED_SECURITY_TOKEN
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|tokenObject
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|RequestedSecurityTokenType
name|rstType
init|=
call|(
name|RequestedSecurityTokenType
call|)
argument_list|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|tokenObject
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|securityContextToken
operator|=
operator|(
name|Element
operator|)
name|rstType
operator|.
name|getAny
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|securityContextToken
argument_list|)
expr_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|securityContextToken
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"SecurityContextToken"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"Identifier"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test to successfully issue an encrypted SecurityContextToken      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testIssueEncryptedSCT
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenIssueOperation
name|issueOperation
init|=
operator|new
name|TokenIssueOperation
argument_list|()
decl_stmt|;
name|issueOperation
operator|.
name|setTokenStore
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setEncryptIssuedToken
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Add Token Provider
name|List
argument_list|<
name|TokenProvider
argument_list|>
name|providerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providerList
operator|.
name|add
argument_list|(
operator|new
name|SCTProvider
argument_list|()
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setTokenProviders
argument_list|(
name|providerList
argument_list|)
expr_stmt|;
comment|// Add Service
name|ServiceMBean
name|service
init|=
operator|new
name|StaticService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setEndpoints
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|EncryptionProperties
name|encryptionProperties
init|=
operator|new
name|EncryptionProperties
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|unrestrictedPoliciesInstalled
condition|)
block|{
name|encryptionProperties
operator|.
name|setEncryptionAlgorithm
argument_list|(
name|WSConstants
operator|.
name|AES_128
argument_list|)
expr_stmt|;
block|}
name|service
operator|.
name|setEncryptionProperties
argument_list|(
name|encryptionProperties
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setServices
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add STSProperties object
name|STSPropertiesMBean
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
name|issueOperation
operator|.
name|setStsProperties
argument_list|(
name|stsProperties
argument_list|)
expr_stmt|;
comment|// Mock up a request
name|RequestSecurityTokenType
name|request
init|=
operator|new
name|RequestSecurityTokenType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|tokenType
init|=
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|TOKEN_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_12
argument_list|)
decl_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|createAppliesToElement
argument_list|(
literal|"http://dummy-service.com/dummy"
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
name|Principal
name|principal
init|=
operator|new
name|CustomTokenPrincipal
argument_list|(
literal|"alice"
argument_list|)
decl_stmt|;
name|msgCtx
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|createSecurityContext
argument_list|(
name|principal
argument_list|)
argument_list|)
expr_stmt|;
comment|// Issue a token
name|RequestSecurityTokenResponseCollectionType
name|response
init|=
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|msgCtx
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RequestSecurityTokenResponseType
argument_list|>
name|securityTokenResponse
init|=
name|response
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|securityTokenResponse
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test the generated token.
name|Element
name|securityContextToken
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|tokenObject
range|:
name|securityTokenResponse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|tokenObject
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|&&
name|REQUESTED_SECURITY_TOKEN
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|tokenObject
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|RequestedSecurityTokenType
name|rstType
init|=
call|(
name|RequestedSecurityTokenType
call|)
argument_list|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|tokenObject
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|securityContextToken
operator|=
operator|(
name|Element
operator|)
name|rstType
operator|.
name|getAny
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|securityContextToken
argument_list|)
expr_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|securityContextToken
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"SecurityContextToken"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"Identifier"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"EncryptedData"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test to successfully issue a SecurityContextToken with no references      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testIssueSCTNoReferences
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenIssueOperation
name|issueOperation
init|=
operator|new
name|TokenIssueOperation
argument_list|()
decl_stmt|;
name|issueOperation
operator|.
name|setTokenStore
argument_list|(
name|tokenStore
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setReturnReferences
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Add Token Provider
name|List
argument_list|<
name|TokenProvider
argument_list|>
name|providerList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providerList
operator|.
name|add
argument_list|(
operator|new
name|SCTProvider
argument_list|()
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setTokenProviders
argument_list|(
name|providerList
argument_list|)
expr_stmt|;
comment|// Add Service
name|ServiceMBean
name|service
init|=
operator|new
name|StaticService
argument_list|()
decl_stmt|;
name|service
operator|.
name|setEndpoints
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://dummy-service.com/dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|issueOperation
operator|.
name|setServices
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add STSProperties object
name|STSPropertiesMBean
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
name|issueOperation
operator|.
name|setStsProperties
argument_list|(
name|stsProperties
argument_list|)
expr_stmt|;
comment|// Mock up a request
name|RequestSecurityTokenType
name|request
init|=
operator|new
name|RequestSecurityTokenType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|String
argument_list|>
name|tokenType
init|=
operator|new
name|JAXBElement
argument_list|<
name|String
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|TOKEN_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_12
argument_list|)
decl_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|createAppliesToElement
argument_list|(
literal|"http://dummy-service.com/dummy"
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
name|Principal
name|principal
init|=
operator|new
name|CustomTokenPrincipal
argument_list|(
literal|"alice"
argument_list|)
decl_stmt|;
name|msgCtx
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|createSecurityContext
argument_list|(
name|principal
argument_list|)
argument_list|)
expr_stmt|;
comment|// Issue a token
name|RequestSecurityTokenResponseCollectionType
name|response
init|=
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|principal
argument_list|,
name|msgCtx
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RequestSecurityTokenResponseType
argument_list|>
name|securityTokenResponse
init|=
name|response
operator|.
name|getRequestSecurityTokenResponse
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|securityTokenResponse
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that no references were returned
name|boolean
name|foundReference
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|tokenObject
range|:
name|securityTokenResponse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|tokenObject
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|&&
operator|(
name|ATTACHED_REFERENCE
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|tokenObject
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|UNATTACHED_REFERENCE
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|tokenObject
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|foundReference
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertFalse
argument_list|(
name|foundReference
argument_list|)
expr_stmt|;
block|}
comment|/*      * Create a security context object      */
specifier|private
name|SecurityContext
name|createSecurityContext
parameter_list|(
specifier|final
name|Principal
name|p
parameter_list|)
block|{
return|return
operator|new
name|SecurityContext
argument_list|()
block|{
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|p
return|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
comment|/*      * Mock up an AppliesTo element using the supplied address      */
specifier|private
name|Element
name|createAppliesToElement
parameter_list|(
name|String
name|addressUrl
parameter_list|)
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|appliesTo
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|STSConstants
operator|.
name|WSP_NS
argument_list|,
literal|"wsp:AppliesTo"
argument_list|)
decl_stmt|;
name|appliesTo
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:wsp"
argument_list|,
name|STSConstants
operator|.
name|WSP_NS
argument_list|)
expr_stmt|;
name|Element
name|endpointRef
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|STSConstants
operator|.
name|WSA_NS_05
argument_list|,
literal|"wsa:EndpointReference"
argument_list|)
decl_stmt|;
name|endpointRef
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:wsa"
argument_list|,
name|STSConstants
operator|.
name|WSA_NS_05
argument_list|)
expr_stmt|;
name|Element
name|address
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|STSConstants
operator|.
name|WSA_NS_05
argument_list|,
literal|"wsa:Address"
argument_list|)
decl_stmt|;
name|address
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:wsa"
argument_list|,
name|STSConstants
operator|.
name|WSA_NS_05
argument_list|)
expr_stmt|;
name|address
operator|.
name|setTextContent
argument_list|(
name|addressUrl
argument_list|)
expr_stmt|;
name|endpointRef
operator|.
name|appendChild
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|appliesTo
operator|.
name|appendChild
argument_list|(
name|endpointRef
argument_list|)
expr_stmt|;
return|return
name|appliesTo
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

