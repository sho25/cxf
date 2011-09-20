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
name|UsernameTokenValidator
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|StatusType
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
name|ValidateTargetType
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
name|saml
operator|.
name|ext
operator|.
name|builder
operator|.
name|SAML2Constants
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
name|DOM2Writer
import|;
end_import

begin_comment
comment|/**  * In this test, a token (UsernameToken) is validated and transformed into a SAML Assertion.  */
end_comment

begin_class
specifier|public
class|class
name|ValidateTokenTransformationUnitTest
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
specifier|private
specifier|static
specifier|final
name|QName
name|QNAME_WST_STATUS
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createStatus
argument_list|(
literal|null
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Test to successfully validate a UsernameToken and transform it into a SAML Assertion.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testUsernameTokenTransformation
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenValidateOperation
name|validateOperation
init|=
operator|new
name|TokenValidateOperation
argument_list|()
decl_stmt|;
comment|// Add Token Validator
name|List
argument_list|<
name|TokenValidator
argument_list|>
name|validatorList
init|=
operator|new
name|ArrayList
argument_list|<
name|TokenValidator
argument_list|>
argument_list|()
decl_stmt|;
name|validatorList
operator|.
name|add
argument_list|(
operator|new
name|UsernameTokenValidator
argument_list|()
argument_list|)
expr_stmt|;
name|validateOperation
operator|.
name|setTokenValidators
argument_list|(
name|validatorList
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
argument_list|<
name|TokenProvider
argument_list|>
argument_list|()
decl_stmt|;
name|providerList
operator|.
name|add
argument_list|(
operator|new
name|SAMLTokenProvider
argument_list|()
argument_list|)
expr_stmt|;
name|validateOperation
operator|.
name|setTokenProviders
argument_list|(
name|providerList
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
name|validateOperation
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
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
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
comment|// Create a UsernameToken
name|JAXBElement
argument_list|<
name|UsernameTokenType
argument_list|>
name|usernameTokenType
init|=
name|createUsernameToken
argument_list|(
literal|"alice"
argument_list|,
literal|"clarinet"
argument_list|)
decl_stmt|;
name|ValidateTargetType
name|validateTarget
init|=
operator|new
name|ValidateTargetType
argument_list|()
decl_stmt|;
name|validateTarget
operator|.
name|setAny
argument_list|(
name|usernameTokenType
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|ValidateTargetType
argument_list|>
name|validateTargetType
init|=
operator|new
name|JAXBElement
argument_list|<
name|ValidateTargetType
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|VALIDATE_TARGET
argument_list|,
name|ValidateTargetType
operator|.
name|class
argument_list|,
name|validateTarget
argument_list|)
decl_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|validateTargetType
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
operator|new
name|CustomTokenPrincipal
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|WebServiceContextImpl
name|webServiceContext
init|=
operator|new
name|WebServiceContextImpl
argument_list|(
name|msgCtx
argument_list|)
decl_stmt|;
comment|// Validate a token
name|RequestSecurityTokenResponseType
name|response
init|=
name|validateOperation
operator|.
name|validate
argument_list|(
name|request
argument_list|,
name|webServiceContext
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validateResponse
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test the generated token.
name|Element
name|assertion
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Object
name|tokenObject
range|:
name|response
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
name|assertion
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
name|assertion
argument_list|)
expr_stmt|;
name|String
name|tokenString
init|=
name|DOM2Writer
operator|.
name|nodeToString
argument_list|(
name|assertion
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"AttributeStatement"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tokenString
operator|.
name|contains
argument_list|(
name|SAML2Constants
operator|.
name|CONF_BEARER
argument_list|)
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
comment|/**      * Return true if the response has a valid status, false otherwise      */
specifier|private
name|boolean
name|validateResponse
parameter_list|(
name|RequestSecurityTokenResponseType
name|response
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|response
operator|!=
literal|null
operator|&&
name|response
operator|.
name|getAny
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|response
operator|.
name|getAny
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|requestObject
range|:
name|response
operator|.
name|getAny
argument_list|()
control|)
block|{
if|if
condition|(
name|requestObject
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|jaxbElement
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|requestObject
decl_stmt|;
if|if
condition|(
name|QNAME_WST_STATUS
operator|.
name|equals
argument_list|(
name|jaxbElement
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|StatusType
name|status
init|=
operator|(
name|StatusType
operator|)
name|jaxbElement
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|STSConstants
operator|.
name|VALID_CODE
operator|.
name|equals
argument_list|(
name|status
operator|.
name|getCode
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
end_class

begin_function
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
end_function

begin_function
specifier|private
name|JAXBElement
argument_list|<
name|UsernameTokenType
argument_list|>
name|createUsernameToken
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|)
block|{
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
name|name
argument_list|)
expr_stmt|;
name|usernameToken
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
comment|// Add a password
name|PasswordString
name|passwordString
init|=
operator|new
name|PasswordString
argument_list|()
decl_stmt|;
name|passwordString
operator|.
name|setValue
argument_list|(
name|password
argument_list|)
expr_stmt|;
name|passwordString
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
name|passwordString
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
return|return
name|tokenType
return|;
block|}
end_function

unit|}
end_unit

