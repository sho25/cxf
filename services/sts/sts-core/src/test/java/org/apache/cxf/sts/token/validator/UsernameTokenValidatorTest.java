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
name|EncodedString
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
name|PasswordString
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
name|UsernameToken
import|;
end_import

begin_comment
comment|/**  * Some unit tests for validating a UsernameToken via the UsernameTokenValidator.  */
end_comment

begin_class
specifier|public
class|class
name|UsernameTokenValidatorTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Test a valid UsernameToken with password text      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidUsernameTokenText
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|usernameTokenValidator
init|=
operator|new
name|UsernameTokenValidator
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
comment|// Create a ValidateTarget consisting of a UsernameToken
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
literal|"alice"
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
name|tokenType
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
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|tokenType
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
name|usernameTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
comment|// This will fail as there is no password
name|TokenValidatorResponse
name|validatorResponse
init|=
name|usernameTokenValidator
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
name|INVALID
argument_list|)
expr_stmt|;
comment|// Add a password
name|PasswordString
name|password
init|=
operator|new
name|PasswordString
argument_list|()
decl_stmt|;
name|password
operator|.
name|setValue
argument_list|(
literal|"clarinet"
argument_list|)
expr_stmt|;
name|password
operator|.
name|setType
argument_list|(
name|WSConstants
operator|.
name|PASSWORD_TEXT
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|PasswordString
argument_list|>
name|passwordType
init|=
operator|new
name|JAXBElement
argument_list|<
name|PasswordString
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|PASSWORD
argument_list|,
name|PasswordString
operator|.
name|class
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|usernameToken
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|passwordType
argument_list|)
expr_stmt|;
name|validatorResponse
operator|=
name|usernameTokenValidator
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
comment|/**      * Test an invalid UsernameToken with password text      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testInvalidUsernameTokenText
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|usernameTokenValidator
init|=
operator|new
name|UsernameTokenValidator
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
comment|// Create a ValidateTarget consisting of a UsernameToken
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
literal|"eve"
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
name|tokenType
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
comment|// Add a password
name|PasswordString
name|password
init|=
operator|new
name|PasswordString
argument_list|()
decl_stmt|;
name|password
operator|.
name|setValue
argument_list|(
literal|"clarinet"
argument_list|)
expr_stmt|;
name|password
operator|.
name|setType
argument_list|(
name|WSConstants
operator|.
name|PASSWORD_TEXT
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|PasswordString
argument_list|>
name|passwordType
init|=
operator|new
name|JAXBElement
argument_list|<
name|PasswordString
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|PASSWORD
argument_list|,
name|PasswordString
operator|.
name|class
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|usernameToken
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|passwordType
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|tokenType
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
name|usernameTokenValidator
operator|.
name|canHandleToken
argument_list|(
name|validateTarget
argument_list|)
argument_list|)
expr_stmt|;
comment|// This will fail as the username is bad
name|TokenValidatorResponse
name|validatorResponse
init|=
name|usernameTokenValidator
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
name|INVALID
argument_list|)
expr_stmt|;
comment|// This will fail as the password is bad
name|username
operator|.
name|setValue
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|password
operator|.
name|setValue
argument_list|(
literal|"badpassword"
argument_list|)
expr_stmt|;
name|validatorResponse
operator|=
name|usernameTokenValidator
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
name|INVALID
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test a valid UsernameToken with password digest      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidUsernameTokenDigest
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidator
name|usernameTokenValidator
init|=
operator|new
name|UsernameTokenValidator
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
comment|// Create a ValidateTarget consisting of a UsernameToken
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
literal|"alice"
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
name|tokenType
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
comment|// Create a WSS4J UsernameToken
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|UsernameToken
name|ut
init|=
operator|new
name|UsernameToken
argument_list|(
literal|true
argument_list|,
name|doc
argument_list|,
name|WSConstants
operator|.
name|PASSWORD_DIGEST
argument_list|)
decl_stmt|;
name|ut
operator|.
name|setName
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|ut
operator|.
name|setPassword
argument_list|(
literal|"clarinet"
argument_list|)
expr_stmt|;
name|ut
operator|.
name|addNonce
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|ut
operator|.
name|addCreated
argument_list|(
literal|true
argument_list|,
name|doc
argument_list|)
expr_stmt|;
comment|// Add a password
name|PasswordString
name|password
init|=
operator|new
name|PasswordString
argument_list|()
decl_stmt|;
name|password
operator|.
name|setValue
argument_list|(
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|password
operator|.
name|setType
argument_list|(
name|WSConstants
operator|.
name|PASSWORD_DIGEST
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|PasswordString
argument_list|>
name|passwordType
init|=
operator|new
name|JAXBElement
argument_list|<
name|PasswordString
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|PASSWORD
argument_list|,
name|PasswordString
operator|.
name|class
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|usernameToken
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|passwordType
argument_list|)
expr_stmt|;
comment|// Add a nonce
name|EncodedString
name|nonce
init|=
operator|new
name|EncodedString
argument_list|()
decl_stmt|;
name|nonce
operator|.
name|setValue
argument_list|(
name|ut
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
name|nonce
operator|.
name|setEncodingType
argument_list|(
name|WSConstants
operator|.
name|SOAPMESSAGE_NS
operator|+
literal|"#Base64Binary"
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|EncodedString
argument_list|>
name|nonceType
init|=
operator|new
name|JAXBElement
argument_list|<
name|EncodedString
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|NONCE
argument_list|,
name|EncodedString
operator|.
name|class
argument_list|,
name|nonce
argument_list|)
decl_stmt|;
name|usernameToken
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|nonceType
argument_list|)
expr_stmt|;
comment|// Add Created value
name|String
name|created
init|=
name|ut
operator|.
name|getCreated
argument_list|()
decl_stmt|;
name|Element
name|createdElement
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
operator|.
name|WSU_NS
argument_list|,
literal|"Created"
argument_list|)
decl_stmt|;
name|createdElement
operator|.
name|setAttributeNS
argument_list|(
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns"
argument_list|,
name|WSConstants
operator|.
name|WSU_NS
argument_list|)
expr_stmt|;
name|createdElement
operator|.
name|setTextContent
argument_list|(
name|created
argument_list|)
expr_stmt|;
name|usernameToken
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|createdElement
argument_list|)
expr_stmt|;
name|ReceivedToken
name|validateTarget
init|=
operator|new
name|ReceivedToken
argument_list|(
name|tokenType
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
name|usernameTokenValidator
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
name|usernameTokenValidator
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
comment|// Expected failure on a bad password
name|password
operator|.
name|setValue
argument_list|(
literal|"badpassword"
argument_list|)
expr_stmt|;
name|validatorResponse
operator|=
name|usernameTokenValidator
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
name|INVALID
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

