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
name|canceller
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
name|message
operator|.
name|token
operator|.
name|SecurityContextToken
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
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Some unit tests for cancelling a SecurityContextToken via the SCTCanceller.  */
end_comment

begin_class
specifier|public
class|class
name|SCTCancellerTest
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
comment|/**      * Get a (valid) SecurityContextToken and successfully cancel it.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCancelToken
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenCanceller
name|sctCanceller
init|=
operator|new
name|SCTCanceller
argument_list|()
decl_stmt|;
name|sctCanceller
operator|.
name|setVerifyProofOfPossession
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|TokenCancellerParameters
name|cancellerParameters
init|=
name|createCancellerParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|cancellerParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a CancelTarget consisting of a SecurityContextToken
name|TokenProviderResponse
name|providerResponse
init|=
name|getSecurityContextToken
argument_list|()
decl_stmt|;
name|ReceivedToken
name|cancelTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|providerResponse
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setCancelTarget
argument_list|(
name|cancelTarget
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setToken
argument_list|(
name|cancelTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sctCanceller
operator|.
name|canHandleToken
argument_list|(
name|cancelTarget
argument_list|)
argument_list|)
expr_stmt|;
name|TokenCancellerResponse
name|cancellerResponse
init|=
name|sctCanceller
operator|.
name|cancelToken
argument_list|(
name|cancellerParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cancellerResponse
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cancellerResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|CANCELLED
argument_list|)
expr_stmt|;
comment|// Try to cancel the token again - this should fail
name|cancellerResponse
operator|=
name|sctCanceller
operator|.
name|cancelToken
argument_list|(
name|cancellerParameters
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cancellerResponse
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cancellerResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|CANCELLED
argument_list|)
expr_stmt|;
block|}
comment|/**      * Try to cancel an invalid SecurityContextToken      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testCancelInvalidToken
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenCanceller
name|sctCanceller
init|=
operator|new
name|SCTCanceller
argument_list|()
decl_stmt|;
name|sctCanceller
operator|.
name|setVerifyProofOfPossession
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|TokenCancellerParameters
name|cancellerParameters
init|=
name|createCancellerParameters
argument_list|()
decl_stmt|;
name|TokenRequirements
name|tokenRequirements
init|=
name|cancellerParameters
operator|.
name|getTokenRequirements
argument_list|()
decl_stmt|;
comment|// Create a CancelTarget consisting of a SecurityContextToken
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|getEmptyDocument
argument_list|()
decl_stmt|;
name|SecurityContextToken
name|sct
init|=
operator|new
name|SecurityContextToken
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|ReceivedToken
name|cancelTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|sct
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
name|tokenRequirements
operator|.
name|setCancelTarget
argument_list|(
name|cancelTarget
argument_list|)
expr_stmt|;
name|cancellerParameters
operator|.
name|setToken
argument_list|(
name|cancelTarget
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|sctCanceller
operator|.
name|canHandleToken
argument_list|(
name|cancelTarget
argument_list|)
argument_list|)
expr_stmt|;
name|TokenCancellerResponse
name|cancellerResponse
init|=
name|sctCanceller
operator|.
name|cancelToken
argument_list|(
name|cancellerParameters
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cancellerResponse
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cancellerResponse
operator|.
name|getToken
argument_list|()
operator|.
name|getState
argument_list|()
operator|==
name|STATE
operator|.
name|CANCELLED
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TokenProviderResponse
name|getSecurityContextToken
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenProvider
name|sctTokenProvider
init|=
operator|new
name|SCTProvider
argument_list|()
decl_stmt|;
name|TokenProviderParameters
name|providerParameters
init|=
name|createProviderParameters
argument_list|(
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_12
argument_list|)
decl_stmt|;
return|return
name|sctTokenProvider
operator|.
name|createToken
argument_list|(
name|providerParameters
argument_list|)
return|;
block|}
specifier|private
name|TokenCancellerParameters
name|createCancellerParameters
parameter_list|()
throws|throws
name|WSSecurityException
block|{
name|TokenCancellerParameters
name|parameters
init|=
operator|new
name|TokenCancellerParameters
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
return|return
name|parameters
return|;
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

