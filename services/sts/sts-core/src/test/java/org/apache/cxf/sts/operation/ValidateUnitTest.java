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
name|BinarySecurityTokenType
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the validate operation.  */
end_comment

begin_class
specifier|public
class|class
name|ValidateUnitTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
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
comment|/**      * Test to successfully validate a (dummy) token.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testValidateToken
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
name|DummyTokenValidator
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
comment|// Add STSProperties object
name|STSPropertiesMBean
name|stsProperties
init|=
operator|new
name|StaticSTSProperties
argument_list|()
decl_stmt|;
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
name|STSConstants
operator|.
name|STATUS
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
name|ValidateTargetType
name|validateTarget
init|=
operator|new
name|ValidateTargetType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|BinarySecurityTokenType
argument_list|>
name|token
init|=
name|createToken
argument_list|()
decl_stmt|;
name|validateTarget
operator|.
name|setAny
argument_list|(
name|token
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
block|}
comment|/**      * Test that calls Validate without a ValidateTarget      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNoToken
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
name|DummyTokenValidator
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
comment|// Add STSProperties object
name|STSPropertiesMBean
name|stsProperties
init|=
operator|new
name|StaticSTSProperties
argument_list|()
decl_stmt|;
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
name|STSConstants
operator|.
name|STATUS
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
comment|// Validate a token
try|try
block|{
name|validateOperation
operator|.
name|validate
argument_list|(
name|request
argument_list|,
name|webServiceContext
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected when no element is presented for validation"
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
block|}
comment|/**      * Test to validate a token of an unknown or missing TokenType value.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testTokenType
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
name|DummyTokenValidator
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
comment|// Add STSProperties object
name|STSPropertiesMBean
name|stsProperties
init|=
operator|new
name|StaticSTSProperties
argument_list|()
decl_stmt|;
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
literal|"UnknownTokenType"
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
name|ValidateTargetType
name|validateTarget
init|=
operator|new
name|ValidateTargetType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|BinarySecurityTokenType
argument_list|>
name|token
init|=
name|createToken
argument_list|()
decl_stmt|;
name|validateTarget
operator|.
name|setAny
argument_list|(
name|token
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
name|WebServiceContextImpl
name|webServiceContext
init|=
operator|new
name|WebServiceContextImpl
argument_list|(
name|msgCtx
argument_list|)
decl_stmt|;
comment|// Validate a token - failure expected on an unknown token type
try|try
block|{
name|validateOperation
operator|.
name|validate
argument_list|(
name|request
argument_list|,
name|webServiceContext
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an unknown token type"
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
comment|// Validate a token - no token type is sent, so it defaults to status
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
block|}
comment|/**      * Test that sends a Context attribute when validating a token, and checks it gets      * a response with the Context attribute properly set.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testContext
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
name|DummyTokenValidator
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
comment|// Add STSProperties object
name|STSPropertiesMBean
name|stsProperties
init|=
operator|new
name|StaticSTSProperties
argument_list|()
decl_stmt|;
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
name|STSConstants
operator|.
name|STATUS
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
name|ValidateTargetType
name|validateTarget
init|=
operator|new
name|ValidateTargetType
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|BinarySecurityTokenType
argument_list|>
name|token
init|=
name|createToken
argument_list|()
decl_stmt|;
name|validateTarget
operator|.
name|setAny
argument_list|(
name|token
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
name|request
operator|.
name|setContext
argument_list|(
literal|"AuthenticationContext"
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
name|assertTrue
argument_list|(
literal|"AuthenticationContext"
operator|.
name|equals
argument_list|(
name|response
operator|.
name|getContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mock up a (JAXB) BinarySecurityTokenType.      */
specifier|private
name|JAXBElement
argument_list|<
name|BinarySecurityTokenType
argument_list|>
name|createToken
parameter_list|()
block|{
name|BinarySecurityTokenType
name|binarySecurityToken
init|=
operator|new
name|BinarySecurityTokenType
argument_list|()
decl_stmt|;
name|binarySecurityToken
operator|.
name|setId
argument_list|(
literal|"BST-1234"
argument_list|)
expr_stmt|;
name|binarySecurityToken
operator|.
name|setValue
argument_list|(
literal|"12345678"
argument_list|)
expr_stmt|;
name|binarySecurityToken
operator|.
name|setValueType
argument_list|(
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
argument_list|)
expr_stmt|;
name|binarySecurityToken
operator|.
name|setEncodingType
argument_list|(
name|DummyTokenProvider
operator|.
name|BASE64_NS
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|BinarySecurityTokenType
argument_list|>
name|tokenType
init|=
operator|new
name|JAXBElement
argument_list|<
name|BinarySecurityTokenType
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|BINARY_SECURITY_TOKEN
argument_list|,
name|BinarySecurityTokenType
operator|.
name|class
argument_list|,
name|binarySecurityToken
argument_list|)
decl_stmt|;
return|return
name|tokenType
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

unit|}
end_unit

