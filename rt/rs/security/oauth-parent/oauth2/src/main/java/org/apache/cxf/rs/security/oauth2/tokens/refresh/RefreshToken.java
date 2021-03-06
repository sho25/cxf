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
name|refresh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|persistence
operator|.
name|ElementCollection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|FetchType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OrderColumn
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

begin_comment
comment|/**  * Simple Refresh Token implementation  */
end_comment

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|RefreshToken
extends|extends
name|ServerAccessToken
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2837120382251693874L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|accessTokens
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|RefreshToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|long
name|lifetime
parameter_list|)
block|{
name|super
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|REFRESH_TOKEN_TYPE
argument_list|,
name|OAuthUtils
operator|.
name|generateRandomTokenKey
argument_list|()
argument_list|,
name|lifetime
argument_list|,
name|OAuthUtils
operator|.
name|getIssuedAt
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RefreshToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|tokenKey
parameter_list|,
name|long
name|lifetime
parameter_list|,
name|long
name|issuedAt
parameter_list|)
block|{
name|super
argument_list|(
name|client
argument_list|,
name|OAuthConstants
operator|.
name|REFRESH_TOKEN_TYPE
argument_list|,
name|tokenKey
argument_list|,
name|lifetime
argument_list|,
name|issuedAt
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RefreshToken
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|,
name|String
name|key
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|accessTokens
parameter_list|)
block|{
name|super
argument_list|(
name|validateTokenType
argument_list|(
name|token
argument_list|,
name|OAuthConstants
operator|.
name|REFRESH_TOKEN_TYPE
argument_list|)
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|this
operator|.
name|accessTokens
operator|=
name|accessTokens
expr_stmt|;
block|}
specifier|public
name|RefreshToken
parameter_list|()
block|{      }
annotation|@
name|ElementCollection
argument_list|(
name|fetch
operator|=
name|FetchType
operator|.
name|EAGER
argument_list|)
annotation|@
name|OrderColumn
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAccessTokens
parameter_list|()
block|{
return|return
name|accessTokens
return|;
block|}
specifier|public
name|void
name|setAccessTokens
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|accessTokens
parameter_list|)
block|{
name|this
operator|.
name|accessTokens
operator|=
name|accessTokens
expr_stmt|;
block|}
specifier|public
name|void
name|addAccessToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|getAccessTokens
argument_list|()
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|removeAccessToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
return|return
name|getAccessTokens
argument_list|()
operator|.
name|remove
argument_list|(
name|token
argument_list|)
return|;
block|}
block|}
end_class

end_unit

