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
name|net
operator|.
name|URI
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
name|AccessTokenRegistration
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
name|utils
operator|.
name|OAuthConstants
import|;
end_import

begin_comment
comment|/**  * Redirection-based Implicit Grant Service  *   * This resource handles the End User authorising  * or denying the Client embedded in the Web agent.  *   * We can consider having a single authorization service dealing with either  * authorization code or implicit grant.  */
end_comment

begin_class
annotation|@
name|Path
argument_list|(
literal|"/authorize-implicit"
argument_list|)
specifier|public
class|class
name|ImplicitGrantService
extends|extends
name|RedirectionBasedGrantService
block|{
specifier|public
name|ImplicitGrantService
parameter_list|()
block|{
name|super
argument_list|(
name|OAuthConstants
operator|.
name|TOKEN_RESPONSE_TYPE
argument_list|,
name|OAuthConstants
operator|.
name|IMPLICIT_GRANT
argument_list|,
literal|false
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
name|preAuthorizedToken
parameter_list|)
block|{
name|ServerAccessToken
name|token
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|preAuthorizedToken
operator|==
literal|null
condition|)
block|{
name|AccessTokenRegistration
name|reg
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|reg
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setGrantType
argument_list|(
name|OAuthConstants
operator|.
name|IMPLICIT_GRANT
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setSubject
argument_list|(
name|userSubject
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setRequestedScope
argument_list|(
name|requestedScope
argument_list|)
expr_stmt|;
name|reg
operator|.
name|setApprovedScope
argument_list|(
name|approvedScope
argument_list|)
expr_stmt|;
name|token
operator|=
name|getDataProvider
argument_list|()
operator|.
name|createAccessToken
argument_list|(
name|reg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|token
operator|=
name|preAuthorizedToken
expr_stmt|;
block|}
comment|// return the code by appending it as a fragment parameter to the redirect URI
name|StringBuilder
name|sb
init|=
name|getUriWithFragment
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
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|token
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ACCESS_TOKEN_TYPE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
comment|//TODO: token parameters should also be included probably
comment|//      though it's not obvious the embedded client can deal with
comment|//      MAC tokens or other sophisticated tokens
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
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
name|StringBuilder
name|sb
init|=
name|getUriWithFragment
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
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|ERROR_KEY
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|seeOther
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|StringBuilder
name|getUriWithFragment
parameter_list|(
name|String
name|state
parameter_list|,
name|String
name|redirectUri
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|redirectUri
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|OAuthConstants
operator|.
name|STATE
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|state
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
return|;
block|}
block|}
end_class

end_unit

