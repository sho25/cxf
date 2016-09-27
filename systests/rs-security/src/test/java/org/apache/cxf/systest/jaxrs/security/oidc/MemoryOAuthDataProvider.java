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
name|oidc
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
name|HashMap
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
name|Map
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
name|provider
operator|.
name|AbstractOAuthDataProvider
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
name|tokens
operator|.
name|refresh
operator|.
name|RefreshToken
import|;
end_import

begin_class
specifier|public
class|class
name|MemoryOAuthDataProvider
extends|extends
name|AbstractOAuthDataProvider
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
name|clients
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Client
argument_list|>
argument_list|()
decl_stmt|;
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
return|return
name|clients
operator|.
name|get
argument_list|(
name|clientId
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
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
name|getAccessTokens
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
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
name|List
argument_list|<
name|RefreshToken
argument_list|>
name|getRefreshTokens
parameter_list|(
name|Client
name|client
parameter_list|,
name|UserSubject
name|subject
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
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|clients
operator|.
name|put
argument_list|(
name|client
operator|.
name|getClientId
argument_list|()
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Client
argument_list|>
name|getClients
parameter_list|(
name|UserSubject
name|resourceOwner
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Client
argument_list|>
argument_list|(
name|clients
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|saveAccessToken
parameter_list|(
name|ServerAccessToken
name|serverToken
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|protected
name|void
name|saveRefreshToken
parameter_list|(
name|RefreshToken
name|refreshToken
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeAccessToken
parameter_list|(
name|ServerAccessToken
name|accessToken
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeRefreshToken
parameter_list|(
name|RefreshToken
name|refreshToken
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
block|}
annotation|@
name|Override
specifier|protected
name|RefreshToken
name|getRefreshToken
parameter_list|(
name|String
name|refreshTokenKey
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRemoveClient
parameter_list|(
name|Client
name|c
parameter_list|)
block|{
name|clients
operator|.
name|remove
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

