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
name|Collections
import|;
end_import

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
name|LifetimeType
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
name|utility
operator|.
name|AttributedDateTime
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
name|util
operator|.
name|XmlSchemaDateFormat
import|;
end_import

begin_comment
comment|/**  * Some unit tests for the issue operation.  */
end_comment

begin_class
specifier|public
class|class
name|IssueUnitTest
extends|extends
name|org
operator|.
name|junit
operator|.
name|Assert
block|{
comment|/**      * Test to successfully issue a (dummy) token.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testIssueToken
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
name|DummyTokenProvider
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
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
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
name|WebServiceContextImpl
name|webServiceContext
init|=
operator|new
name|WebServiceContextImpl
argument_list|(
name|msgCtx
argument_list|)
decl_stmt|;
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
name|webServiceContext
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
block|}
comment|/**      * Test to issue a token of an unknown or missing TokenType value.      */
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
name|TokenIssueOperation
name|issueOperation
init|=
operator|new
name|TokenIssueOperation
argument_list|()
decl_stmt|;
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
name|DummyTokenProvider
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
name|WebServiceContextImpl
name|webServiceContext
init|=
operator|new
name|WebServiceContextImpl
argument_list|(
name|msgCtx
argument_list|)
decl_stmt|;
comment|// Issue a token - failure expected on an unknown token type
try|try
block|{
name|issueOperation
operator|.
name|issue
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
comment|// Issue a token - failure expected as no token type is sent
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
try|try
block|{
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|webServiceContext
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on no token type"
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
comment|// Issue a token - this time it defaults to a known token type
name|service
operator|.
name|setTokenType
argument_list|(
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
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
name|RequestSecurityTokenResponseCollectionType
name|response
init|=
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|webServiceContext
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
block|}
comment|/**      * Test the endpoint address sent to the STS as part of AppliesTo. If the STS does not      * recognise the endpoint address it does not issue a token.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testIssueEndpointAddress
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
name|DummyTokenProvider
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
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
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
literal|"http://dummy-service.com/dummy-unknown"
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
comment|// Issue a token - failure expected on an unknown address
try|try
block|{
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|webServiceContext
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an unknown address"
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
comment|// Issue a token - This should work as wildcards are used
name|service
operator|.
name|setEndpoints
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://dummy-service.com/dummy.*"
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
name|RequestSecurityTokenResponseCollectionType
name|response
init|=
name|issueOperation
operator|.
name|issue
argument_list|(
name|request
argument_list|,
name|webServiceContext
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
block|}
comment|/**      * Test that a request with no AppliesTo is not rejected      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNoAppliesTo
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
name|DummyTokenProvider
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
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
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
name|webServiceContext
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
block|}
comment|/**      * Test that sends a Context attribute when requesting a token, and checks it gets      * a response with the Context attribute properly set.      */
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
name|TokenIssueOperation
name|issueOperation
init|=
operator|new
name|TokenIssueOperation
argument_list|()
decl_stmt|;
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
name|DummyTokenProvider
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
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
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
name|webServiceContext
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
name|assertTrue
argument_list|(
literal|"AuthenticationContext"
operator|.
name|equals
argument_list|(
name|securityTokenResponse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test to successfully issue a (dummy) token with a supplied lifetime. It only tests that      * the lifetime can be successfully processed by the RequestParser for now.      */
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testLifetime
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
name|DummyTokenProvider
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
name|DummyTokenProvider
operator|.
name|TOKEN_TYPE
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
name|LifetimeType
name|lifetime
init|=
name|createLifetime
argument_list|(
literal|300L
operator|*
literal|5L
argument_list|)
decl_stmt|;
name|JAXBElement
argument_list|<
name|LifetimeType
argument_list|>
name|lifetimeJaxb
init|=
operator|new
name|JAXBElement
argument_list|<
name|LifetimeType
argument_list|>
argument_list|(
name|QNameConstants
operator|.
name|LIFETIME
argument_list|,
name|LifetimeType
operator|.
name|class
argument_list|,
name|lifetime
argument_list|)
decl_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|lifetimeJaxb
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
name|webServiceContext
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
comment|/**      * Create a LifetimeType object given a lifetime in seconds      */
specifier|private
name|LifetimeType
name|createLifetime
parameter_list|(
name|long
name|lifetime
parameter_list|)
block|{
name|AttributedDateTime
name|created
init|=
name|QNameConstants
operator|.
name|UTIL_FACTORY
operator|.
name|createAttributedDateTime
argument_list|()
decl_stmt|;
name|AttributedDateTime
name|expires
init|=
name|QNameConstants
operator|.
name|UTIL_FACTORY
operator|.
name|createAttributedDateTime
argument_list|()
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
if|if
condition|(
name|lifetime
operator|<=
literal|0
condition|)
block|{
name|lifetime
operator|=
literal|300L
expr_stmt|;
block|}
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
name|lifetime
operator|*
literal|1000L
operator|)
argument_list|)
expr_stmt|;
name|XmlSchemaDateFormat
name|fmt
init|=
operator|new
name|XmlSchemaDateFormat
argument_list|()
decl_stmt|;
name|created
operator|.
name|setValue
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|creationTime
argument_list|)
argument_list|)
expr_stmt|;
name|expires
operator|.
name|setValue
argument_list|(
name|fmt
operator|.
name|format
argument_list|(
name|expirationTime
argument_list|)
argument_list|)
expr_stmt|;
name|LifetimeType
name|lifetimeType
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createLifetimeType
argument_list|()
decl_stmt|;
name|lifetimeType
operator|.
name|setCreated
argument_list|(
name|created
argument_list|)
expr_stmt|;
name|lifetimeType
operator|.
name|setExpires
argument_list|(
name|expires
argument_list|)
expr_stmt|;
return|return
name|lifetimeType
return|;
block|}
block|}
end_class

end_unit

