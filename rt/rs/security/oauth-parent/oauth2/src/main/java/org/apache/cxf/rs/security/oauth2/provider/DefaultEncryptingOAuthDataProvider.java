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
name|provider
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
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
name|tokens
operator|.
name|refresh
operator|.
name|RefreshToken
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
name|crypto
operator|.
name|CryptoUtils
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
name|crypto
operator|.
name|KeyProperties
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
name|crypto
operator|.
name|ModelEncryptionSupport
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultEncryptingOAuthDataProvider
extends|extends
name|AbstractOAuthDataProvider
block|{
specifier|protected
name|SecretKey
name|key
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|tokens
init|=
name|Collections
operator|.
name|synchronizedSet
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|refreshTokens
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|DefaultEncryptingOAuthDataProvider
parameter_list|(
name|String
name|algo
parameter_list|,
name|int
name|keySize
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|KeyProperties
argument_list|(
name|algo
argument_list|,
name|keySize
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultEncryptingOAuthDataProvider
parameter_list|(
name|KeyProperties
name|props
parameter_list|)
block|{
name|this
argument_list|(
name|CryptoUtils
operator|.
name|getSecretKey
argument_list|(
name|props
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DefaultEncryptingOAuthDataProvider
parameter_list|(
name|SecretKey
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
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
literal|null
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
return|return
name|ModelEncryptionSupport
operator|.
name|decryptAccessToken
argument_list|(
name|this
argument_list|,
name|accessToken
argument_list|,
name|key
argument_list|)
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
name|revokeAccessToken
argument_list|(
name|accessToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
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
name|encryptAccessToken
argument_list|(
name|serverToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|revokeAccessToken
parameter_list|(
name|String
name|accessTokenKey
parameter_list|)
block|{
return|return
name|tokens
operator|.
name|remove
argument_list|(
name|accessTokenKey
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|saveRefreshToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|,
name|RefreshToken
name|refreshToken
parameter_list|)
block|{
name|String
name|encryptedRefreshToken
init|=
name|ModelEncryptionSupport
operator|.
name|encryptRefreshToken
argument_list|(
name|refreshToken
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|at
operator|.
name|setRefreshToken
argument_list|(
name|encryptedRefreshToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|RefreshToken
name|revokeRefreshToken
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|refreshTokenKey
parameter_list|)
block|{
name|refreshTokens
operator|.
name|remove
argument_list|(
name|refreshTokenKey
argument_list|)
expr_stmt|;
return|return
name|ModelEncryptionSupport
operator|.
name|decryptRefreshToken
argument_list|(
name|this
argument_list|,
name|refreshTokenKey
argument_list|,
name|key
argument_list|)
return|;
block|}
specifier|private
name|void
name|encryptAccessToken
parameter_list|(
name|ServerAccessToken
name|token
parameter_list|)
block|{
name|String
name|encryptedToken
init|=
name|ModelEncryptionSupport
operator|.
name|encryptAccessToken
argument_list|(
name|token
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|add
argument_list|(
name|encryptedToken
argument_list|)
expr_stmt|;
name|refreshTokens
operator|.
name|put
argument_list|(
name|token
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|encryptedToken
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenKey
argument_list|(
name|encryptedToken
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

