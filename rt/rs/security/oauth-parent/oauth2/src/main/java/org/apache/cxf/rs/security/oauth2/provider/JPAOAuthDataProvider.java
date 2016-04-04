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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityExistsException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NoResultException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TypedQuery
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
name|JPAOAuthDataProvider
extends|extends
name|AbstractOAuthDataProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_TABLE_NAME
init|=
name|Client
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
specifier|private
name|EntityManager
name|entityManager
decl_stmt|;
specifier|public
name|JPAOAuthDataProvider
parameter_list|()
block|{     }
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
try|try
block|{
return|return
name|getClientQuery
argument_list|(
name|clientId
argument_list|)
operator|.
name|getSingleResult
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoResultException
name|ex
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|persistEntity
argument_list|(
name|client
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
name|persistEntity
argument_list|(
name|client
argument_list|)
expr_stmt|;
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
name|removeEntity
argument_list|(
name|c
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
name|getClientsQuery
argument_list|(
name|resourceOwner
argument_list|)
operator|.
name|getResultList
argument_list|()
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
name|c
parameter_list|,
name|UserSubject
name|sub
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
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
name|c
parameter_list|,
name|UserSubject
name|sub
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
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
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeAccessToken
parameter_list|(
name|ServerAccessToken
name|at
parameter_list|)
block|{     }
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
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeRefreshToken
parameter_list|(
name|RefreshToken
name|rt
parameter_list|)
block|{      }
specifier|protected
name|void
name|saveAccessToken
parameter_list|(
name|ServerAccessToken
name|serverToken
parameter_list|)
block|{
name|persistEntity
argument_list|(
name|serverToken
argument_list|)
expr_stmt|;
block|}
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
block|{     }
specifier|protected
name|void
name|persistEntity
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
try|try
block|{
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|persist
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EntityExistsException
name|ex
parameter_list|)
block|{
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|removeEntity
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|begin
argument_list|()
expr_stmt|;
name|entityManager
operator|.
name|remove
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|entityManager
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|TypedQuery
argument_list|<
name|Client
argument_list|>
name|getClientQuery
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
return|return
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT c FROM "
operator|+
name|CLIENT_TABLE_NAME
operator|+
literal|" c WHERE c.clientId = '"
operator|+
name|clientId
operator|+
literal|"'"
argument_list|,
name|Client
operator|.
name|class
argument_list|)
return|;
block|}
specifier|protected
name|TypedQuery
argument_list|<
name|Client
argument_list|>
name|getClientsQuery
parameter_list|(
name|UserSubject
name|resourceOwnerSubject
parameter_list|)
block|{
if|if
condition|(
name|resourceOwnerSubject
operator|==
literal|null
condition|)
block|{
return|return
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT c FROM "
operator|+
name|CLIENT_TABLE_NAME
operator|+
literal|" c"
argument_list|,
name|Client
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT c FROM "
operator|+
name|CLIENT_TABLE_NAME
operator|+
literal|" c JOIN c.resourceOwnerSubject r WHERE r.login = '"
operator|+
name|resourceOwnerSubject
operator|.
name|getLogin
argument_list|()
operator|+
literal|"'"
argument_list|,
name|Client
operator|.
name|class
argument_list|)
return|;
block|}
block|}
specifier|public
name|void
name|setEntityManager
parameter_list|(
name|EntityManager
name|entityManager
parameter_list|)
block|{
name|this
operator|.
name|entityManager
operator|=
name|entityManager
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|entityManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

