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
name|services
package|;
end_package

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
name|ws
operator|.
name|rs
operator|.
name|Path
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
name|MultivaluedMap
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
name|Response
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
name|UriBuilder
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
name|common
operator|.
name|ServerAccessToken
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
name|UserSubject
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
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeDataProvider
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
name|grants
operator|.
name|code
operator|.
name|AuthorizationCodeRegistration
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
name|grants
operator|.
name|code
operator|.
name|ServerAuthorizationCodeGrant
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
name|OAuthServiceException
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

begin_comment
comment|/**  * This resource handles the End User authorising  * or denying the Client to access its resources.  * If End User approves the access this resource will  * redirect End User back to the Client, supplying   * the authorization code.  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/authorize"
argument_list|)
specifier|public
class|class
name|AuthorizationCodeGrantService
extends|extends
name|RedirectionBasedGrantService
block|{
specifier|public
name|AuthorizationCodeGrantService
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|CODE_RESPONSE_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_GRANT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Response
name|createGrant
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|Client
name|client
parameter_list|,
name|String
name|redirectUri
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|approvedScope
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|ServerAccessToken
name|preauthorizedToken
parameter_list|)
block|{
comment|// in this flow the code is still created, the preauthorized token
comment|// will be retrieved by the authorization code grant handler
name|AuthorizationCodeRegistration
name|codeReg
init|=
operator|new
name|AuthorizationCodeRegistration
argument_list|()
decl_stmt|;
name|codeReg
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setRedirectUri
argument_list|(
name|redirectUri
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setRequestedScope
argument_list|(
name|requestedScope
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setApprovedScope
argument_list|(
name|approvedScope
argument_list|)
expr_stmt|;
name|codeReg
operator|.
name|setSubject
argument_list|(
name|userSubject
argument_list|)
expr_stmt|;
name|ServerAuthorizationCodeGrant
name|grant
init|=
literal|null
decl_stmt|;
try|try
block|{
name|grant
operator|=
operator|(
operator|(
name|AuthorizationCodeDataProvider
operator|)
name|getDataProvider
argument_list|()
operator|)
operator|.
name|createCodeGrant
argument_list|(
name|codeReg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OAuthServiceException
name|ex
parameter_list|)
block|{
return|return
name|createErrorResponse
argument_list|(
name|params
argument_list|,
name|redirectUri
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_DENIED
argument_list|)
return|;
block|}
comment|// return the code by appending it as a query parameter to the redirect URI
name|UriBuilder
name|ub
init|=
name|getRedirectUriBuilder
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
argument_list|,
name|redirectUri
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|AUTHORIZATION_CODE_VALUE
argument_list|,
name|grant
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|ub
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|Response
name|createErrorResponse
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|,
name|String
name|redirectUri
parameter_list|,
name|String
name|error
parameter_list|)
block|{
name|UriBuilder
name|ub
init|=
name|getRedirectUriBuilder
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
argument_list|,
name|redirectUri
argument_list|)
decl_stmt|;
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|,
name|error
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|ub
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|UriBuilder
name|getRedirectUriBuilder
parameter_list|(
name|String
name|state
parameter_list|,
name|String
name|redirectUri
parameter_list|)
block|{
name|UriBuilder
name|ub
init|=
name|UriBuilder
operator|.
name|fromUri
argument_list|(
name|redirectUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|ub
operator|.
name|queryParam
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
return|return
name|ub
return|;
block|}
block|}
end_class

end_unit

