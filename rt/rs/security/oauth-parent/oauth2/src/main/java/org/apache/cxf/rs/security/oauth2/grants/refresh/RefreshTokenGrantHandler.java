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
name|grants
operator|.
name|refresh
package|;
end_package

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
name|provider
operator|.
name|AccessTokenGrantHandler
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
name|utils
operator|.
name|OAuthConstants
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
name|OAuthUtils
import|;
end_import

begin_class
specifier|public
class|class
name|RefreshTokenGrantHandler
implements|implements
name|AccessTokenGrantHandler
block|{
specifier|private
name|OAuthDataProvider
name|dataProvider
decl_stmt|;
specifier|public
name|void
name|setDataProvider
parameter_list|(
name|OAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|this
operator|.
name|dataProvider
operator|=
name|dataProvider
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSupportedGrantTypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|OAuthConstants
operator|.
name|REFRESH_TOKEN_GRANT
argument_list|)
return|;
block|}
specifier|public
name|ServerAccessToken
name|createAccessToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
if|if
condition|(
operator|!
name|OAuthUtils
operator|.
name|isGrantSupportedForClient
argument_list|(
name|client
argument_list|,
literal|true
argument_list|,
name|OAuthConstants
operator|.
name|REFRESH_TOKEN_GRANT
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|UNAUTHORIZED_CLIENT
argument_list|)
throw|;
block|}
name|String
name|refreshToken
init|=
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|REFRESH_TOKEN
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|requestedScopes
init|=
name|OAuthUtils
operator|.
name|parseScope
argument_list|(
name|params
operator|.
name|getFirst
argument_list|(
name|OAuthConstants
operator|.
name|SCOPE
argument_list|)
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|token
init|=
name|dataProvider
operator|.
name|refreshAccessToken
argument_list|(
name|client
argument_list|,
name|refreshToken
argument_list|,
name|requestedScopes
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|tokenScopes
init|=
name|OAuthUtils
operator|.
name|convertPermissionsToScopeList
argument_list|(
name|token
operator|.
name|getScopes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tokenScopes
operator|.
name|containsAll
argument_list|(
name|requestedScopes
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
name|OAuthConstants
operator|.
name|INVALID_SCOPE
argument_list|)
throw|;
block|}
return|return
name|token
return|;
block|}
block|}
end_class

end_unit

