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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|tokens
operator|.
name|hawk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|oauth2
operator|.
name|client
operator|.
name|HttpRequestProperties
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
name|oauth2
operator|.
name|common
operator|.
name|AccessTokenValidation
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
name|oauth2
operator|.
name|common
operator|.
name|Client
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
name|oauth2
operator|.
name|provider
operator|.
name|OAuthDataProvider
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
name|oauth2
operator|.
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
specifier|public
class|class
name|HawkAccessTokenValidatorTest
block|{
specifier|private
name|HawkAccessTokenValidator
name|validator
init|=
operator|new
name|HawkAccessTokenValidator
argument_list|()
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|dataProvider
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|OAuthDataProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|MessageContext
name|messageContext
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|MessageContext
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|validator
operator|.
name|setDataProvider
argument_list|(
name|dataProvider
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testValidateAccessToken
parameter_list|()
throws|throws
name|Exception
block|{
name|HawkAccessToken
name|macAccessToken
init|=
operator|new
name|HawkAccessToken
argument_list|(
operator|new
name|Client
argument_list|(
literal|"testClientId"
argument_list|,
literal|"testClientSecret"
argument_list|,
literal|true
argument_list|)
argument_list|,
name|HmacAlgorithm
operator|.
name|HmacSHA256
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|HttpServletRequest
name|httpRequest
init|=
name|mockHttpRequest
argument_list|()
decl_stmt|;
name|UriInfo
name|uriInfo
init|=
name|mockUriInfo
argument_list|()
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|dataProvider
operator|.
name|getAccessToken
argument_list|(
name|macAccessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|macAccessToken
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|messageContext
operator|.
name|getHttpServletRequest
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|httpRequest
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|messageContext
operator|.
name|getUriInfo
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|uriInfo
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|dataProvider
argument_list|,
name|messageContext
argument_list|,
name|httpRequest
argument_list|,
name|uriInfo
argument_list|)
expr_stmt|;
name|String
name|authData
init|=
name|getClientAuthHeader
argument_list|(
name|macAccessToken
argument_list|)
decl_stmt|;
name|AccessTokenValidation
name|tokenValidation
init|=
name|validator
operator|.
name|validateAccessToken
argument_list|(
name|messageContext
argument_list|,
name|OAuthConstants
operator|.
name|HAWK_AUTHORIZATION_SCHEME
argument_list|,
name|authData
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
index|[
literal|1
index|]
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tokenValidation
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|verify
argument_list|(
name|dataProvider
argument_list|,
name|messageContext
argument_list|,
name|httpRequest
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getClientAuthHeader
parameter_list|(
name|HawkAccessToken
name|macAccessToken
parameter_list|)
block|{
name|String
name|address
init|=
literal|"http://localhost:8080/appContext/oauth2/testResource"
decl_stmt|;
name|HttpRequestProperties
name|props
init|=
operator|new
name|HttpRequestProperties
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|address
argument_list|)
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
return|return
operator|new
name|HawkAuthorizationScheme
argument_list|(
name|props
argument_list|,
name|macAccessToken
argument_list|)
operator|.
name|toAuthorizationHeader
argument_list|(
name|macAccessToken
operator|.
name|getMacAlgorithm
argument_list|()
argument_list|,
name|macAccessToken
operator|.
name|getMacKey
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|HttpServletRequest
name|mockHttpRequest
parameter_list|()
block|{
name|HttpServletRequest
name|httpRequest
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|httpRequest
operator|.
name|getMethod
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
return|return
name|httpRequest
return|;
block|}
specifier|private
specifier|static
name|UriInfo
name|mockUriInfo
parameter_list|()
block|{
name|UriInfo
name|ui
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|UriInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|ui
operator|.
name|getRequestUri
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://localhost:8080/appContext/oauth2/testResource"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ui
return|;
block|}
block|}
end_class

end_unit

