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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
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
name|Produces
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
name|Context
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
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|OAuthContext
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
name|AbstractOAuthServerJoseJwtProducer
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
name|OAuthContextUtils
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
name|common
operator|.
name|UserInfo
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/userinfo"
argument_list|)
specifier|public
class|class
name|UserInfoService
extends|extends
name|AbstractOAuthServerJoseJwtProducer
block|{
specifier|private
name|UserInfoProvider
name|userInfoProvider
decl_stmt|;
specifier|private
name|OAuthDataProvider
name|oauthDataProvider
decl_stmt|;
annotation|@
name|Context
specifier|private
name|MessageContext
name|mc
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/json"
block|,
literal|"application/jwt"
block|}
argument_list|)
specifier|public
name|Response
name|getUserInfo
parameter_list|()
block|{
name|OAuthContext
name|oauth
init|=
name|OAuthContextUtils
operator|.
name|getContext
argument_list|(
name|mc
argument_list|)
decl_stmt|;
name|UserInfo
name|userInfo
init|=
name|userInfoProvider
operator|.
name|getUserInfo
argument_list|(
name|oauth
operator|.
name|getClientId
argument_list|()
argument_list|,
name|oauth
operator|.
name|getSubject
argument_list|()
argument_list|,
name|oauth
operator|.
name|getPermissions
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|responseEntity
init|=
name|userInfo
decl_stmt|;
if|if
condition|(
name|super
operator|.
name|isJwsRequired
argument_list|()
operator|||
name|super
operator|.
name|isJweRequired
argument_list|()
condition|)
block|{
name|responseEntity
operator|=
name|super
operator|.
name|processJwt
argument_list|(
operator|new
name|JwtToken
argument_list|(
name|userInfo
argument_list|)
argument_list|,
name|oauthDataProvider
operator|.
name|getClient
argument_list|(
name|oauth
operator|.
name|getClientId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|responseEntity
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUserInfoProvider
parameter_list|(
name|UserInfoProvider
name|userInfoProvider
parameter_list|)
block|{
name|this
operator|.
name|userInfoProvider
operator|=
name|userInfoProvider
expr_stmt|;
block|}
specifier|public
name|void
name|setOauthDataProvider
parameter_list|(
name|OAuthDataProvider
name|oauthDataProvider
parameter_list|)
block|{
name|this
operator|.
name|oauthDataProvider
operator|=
name|oauthDataProvider
expr_stmt|;
block|}
block|}
end_class

end_unit

