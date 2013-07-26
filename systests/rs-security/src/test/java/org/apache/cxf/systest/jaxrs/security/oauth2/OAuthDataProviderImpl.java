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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|oauth2
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
name|saml
operator|.
name|Constants
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
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
import|;
end_import

begin_class
specifier|public
class|class
name|OAuthDataProviderImpl
implements|implements
name|OAuthDataProvider
block|{
annotation|@
name|Override
specifier|public
name|Client
name|getClient
parameter_list|(
name|String
name|clientId
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
literal|"alice"
argument_list|,
literal|"alice"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
name|Constants
operator|.
name|SAML2_BEARER_GRANT
argument_list|)
expr_stmt|;
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
operator|.
name|add
argument_list|(
literal|"custom_grant"
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|AccessTokenRegistration
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
operator|new
name|BearerAccessToken
argument_list|(
name|accessToken
operator|.
name|getClient
argument_list|()
argument_list|,
literal|3600
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getPreauthorizedToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|,
name|UserSubject
name|subject
parameter_list|,
name|String
name|grantType
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|refreshAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshToken
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeAccessToken
parameter_list|(
name|ServerAccessToken
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|convertScopeToPermissions
parameter_list|(
name|Client
name|client
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedScope
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|revokeToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|token
parameter_list|,
name|String
name|tokenTypeHint
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
comment|// TODO Auto-generated method stub
block|}
block|}
end_class

end_unit

