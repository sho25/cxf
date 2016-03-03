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
name|oidc
operator|.
name|idp
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
name|core
operator|.
name|MultivaluedMap
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
name|OAuthPermission
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
name|OAuthRedirectionState
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
name|services
operator|.
name|AuthorizationCodeGrantService
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
name|oidc
operator|.
name|utils
operator|.
name|OidcUtils
import|;
end_import

begin_class
specifier|public
class|class
name|OidcAuthorizationCodeService
extends|extends
name|AuthorizationCodeGrantService
block|{
specifier|private
name|boolean
name|skipAuthorizationWithOidcScope
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|boolean
name|canAuthorizationBeSkipped
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|userSubject
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|,
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|permissions
parameter_list|)
block|{
comment|// No need to challenge the authenticated user with the authorization form
comment|// if all the client application redirecting a user needs is to get this user authenticated
comment|// with OIDC IDP
return|return
name|requestedScope
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|permissions
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|skipAuthorizationWithOidcScope
operator|&&
name|OidcUtils
operator|.
name|OPENID_SCOPE
operator|.
name|equals
argument_list|(
name|requestedScope
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setSkipAuthorizationWithOidcScope
parameter_list|(
name|boolean
name|skipAuthorizationWithOidcScope
parameter_list|)
block|{
name|this
operator|.
name|skipAuthorizationWithOidcScope
operator|=
name|skipAuthorizationWithOidcScope
expr_stmt|;
block|}
specifier|protected
name|AuthorizationCodeRegistration
name|createCodeRegistration
parameter_list|(
name|OAuthRedirectionState
name|state
parameter_list|,
name|Client
name|client
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
name|AuthorizationCodeRegistration
name|codeReg
init|=
name|super
operator|.
name|createCodeRegistration
argument_list|(
name|state
argument_list|,
name|client
argument_list|,
name|requestedScope
argument_list|,
name|approvedScope
argument_list|,
name|userSubject
argument_list|,
name|preauthorizedToken
argument_list|)
decl_stmt|;
name|codeReg
operator|.
name|getExtraProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|state
operator|.
name|getExtraProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|codeReg
return|;
block|}
annotation|@
name|Override
specifier|protected
name|OAuthRedirectionState
name|recreateRedirectionStateFromParams
parameter_list|(
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|OAuthRedirectionState
name|state
init|=
name|super
operator|.
name|recreateRedirectionStateFromParams
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|OidcUtils
operator|.
name|setStateClaimsProperty
argument_list|(
name|state
argument_list|,
name|params
argument_list|)
expr_stmt|;
return|return
name|state
return|;
block|}
block|}
end_class

end_unit

