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
name|EntityManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityTransaction
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
name|helpers
operator|.
name|CastUtils
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
name|tokens
operator|.
name|bearer
operator|.
name|BearerAccessToken
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

begin_comment
comment|/**  * Provides a Jpa BMT implementation for OAuthDataProvider.  *  * If your application runs in a container and if you want to use  * container managed persistence, you'll have to override  * the following methods :  *<ul>  *<li> {@link #getEntityManager()}</li>  *<li> {@link #commitIfNeeded(EntityManager)}</li>  *<li> {@link #closeIfNeeded(EntityManager)}</li>  *</ul>  */
end_comment

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
name|CLIENT_QUERY
init|=
literal|"SELECT client FROM Client client"
operator|+
literal|" INNER JOIN client.resourceOwnerSubject ros"
decl_stmt|;
specifier|private
name|EntityManagerFactory
name|entityManagerFactory
decl_stmt|;
specifier|public
name|void
name|setEntityManagerFactory
parameter_list|(
name|EntityManagerFactory
name|emf
parameter_list|)
block|{
name|this
operator|.
name|entityManagerFactory
operator|=
name|emf
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Client
name|doGetClient
parameter_list|(
specifier|final
name|String
name|clientId
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|execute
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Client
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Client
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|em
operator|.
name|find
argument_list|(
name|Client
operator|.
name|class
argument_list|,
name|clientId
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|EntityManagerOperation
argument_list|<
name|T
argument_list|>
name|operation
parameter_list|)
block|{
name|EntityManager
name|em
init|=
name|getEntityManager
argument_list|()
decl_stmt|;
name|T
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|operation
operator|.
name|execute
argument_list|(
name|em
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|closeIfNeeded
argument_list|(
name|em
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|executeInTransaction
parameter_list|(
name|EntityManagerOperation
argument_list|<
name|T
argument_list|>
name|operation
parameter_list|)
block|{
name|EntityManager
name|em
init|=
name|getEntityManager
argument_list|()
decl_stmt|;
name|EntityTransaction
name|transaction
init|=
literal|null
decl_stmt|;
name|T
name|value
decl_stmt|;
try|try
block|{
name|transaction
operator|=
name|beginIfNeeded
argument_list|(
name|em
argument_list|)
expr_stmt|;
name|value
operator|=
name|operation
operator|.
name|execute
argument_list|(
name|em
argument_list|)
expr_stmt|;
name|flushIfNeeded
argument_list|(
name|em
argument_list|)
expr_stmt|;
name|commitIfNeeded
argument_list|(
name|em
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
if|if
condition|(
name|transaction
operator|!=
literal|null
condition|)
block|{
name|transaction
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|closeIfNeeded
argument_list|(
name|em
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setClient
parameter_list|(
specifier|final
name|Client
name|client
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
if|if
condition|(
name|client
operator|.
name|getResourceOwnerSubject
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|UserSubject
name|sub
init|=
name|em
operator|.
name|find
argument_list|(
name|UserSubject
operator|.
name|class
argument_list|,
name|client
operator|.
name|getResourceOwnerSubject
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sub
operator|==
literal|null
condition|)
block|{
name|em
operator|.
name|persist
argument_list|(
name|client
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|client
operator|.
name|setResourceOwnerSubject
argument_list|(
name|sub
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|clientExists
init|=
name|em
operator|.
name|createQuery
argument_list|(
literal|"SELECT count(client) from Client client "
operator|+
literal|"where client.clientId = :id"
argument_list|,
name|Long
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"id"
argument_list|,
name|client
operator|.
name|getClientId
argument_list|()
argument_list|)
operator|.
name|getSingleResult
argument_list|()
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|clientExists
condition|)
block|{
name|em
operator|.
name|merge
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|em
operator|.
name|persist
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRemoveClient
parameter_list|(
specifier|final
name|Client
name|c
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|Client
name|clientToRemove
init|=
name|em
operator|.
name|getReference
argument_list|(
name|Client
operator|.
name|class
argument_list|,
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
decl_stmt|;
name|em
operator|.
name|remove
argument_list|(
name|clientToRemove
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
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
specifier|final
name|UserSubject
name|resourceOwner
parameter_list|)
block|{
return|return
name|execute
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|List
argument_list|<
name|Client
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Client
argument_list|>
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|getClientsQuery
argument_list|(
name|resourceOwner
argument_list|,
name|em
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
block|}
argument_list|)
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
specifier|final
name|Client
name|c
parameter_list|,
specifier|final
name|UserSubject
name|sub
parameter_list|)
block|{
return|return
name|execute
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|CastUtils
operator|.
name|cast
argument_list|(
name|getTokensQuery
argument_list|(
name|c
argument_list|,
name|sub
argument_list|,
name|em
argument_list|)
operator|.
name|getResultList
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
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
specifier|final
name|Client
name|c
parameter_list|,
specifier|final
name|UserSubject
name|sub
parameter_list|)
block|{
return|return
name|execute
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|List
argument_list|<
name|RefreshToken
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RefreshToken
argument_list|>
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|getRefreshTokensQuery
argument_list|(
name|c
argument_list|,
name|sub
argument_list|,
name|em
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|getAccessToken
parameter_list|(
specifier|final
name|String
name|accessToken
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|execute
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|ServerAccessToken
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ServerAccessToken
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|TypedQuery
argument_list|<
name|BearerAccessToken
argument_list|>
name|query
init|=
name|em
operator|.
name|createQuery
argument_list|(
literal|"SELECT t FROM BearerAccessToken t"
operator|+
literal|" WHERE t.tokenKey = :tokenKey"
argument_list|,
name|BearerAccessToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"tokenKey"
argument_list|,
name|accessToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|.
name|getResultList
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|query
operator|.
name|getSingleResult
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeAccessToken
parameter_list|(
specifier|final
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|ServerAccessToken
name|tokenToRemove
init|=
name|em
operator|.
name|getReference
argument_list|(
name|at
operator|.
name|getClass
argument_list|()
argument_list|,
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|em
operator|.
name|remove
argument_list|(
name|tokenToRemove
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|linkRefreshTokenToAccessToken
parameter_list|(
specifier|final
name|RefreshToken
name|rt
parameter_list|,
specifier|final
name|ServerAccessToken
name|at
parameter_list|)
block|{
name|super
operator|.
name|linkRefreshTokenToAccessToken
argument_list|(
name|rt
argument_list|,
name|at
argument_list|)
expr_stmt|;
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|em
operator|.
name|merge
argument_list|(
name|at
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|RefreshToken
name|getRefreshToken
parameter_list|(
specifier|final
name|String
name|refreshTokenKey
parameter_list|)
block|{
return|return
name|execute
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|RefreshToken
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|RefreshToken
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|em
operator|.
name|find
argument_list|(
name|RefreshToken
operator|.
name|class
argument_list|,
name|refreshTokenKey
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doRevokeRefreshToken
parameter_list|(
specifier|final
name|RefreshToken
name|rt
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|RefreshToken
name|tokentoRemove
init|=
name|em
operator|.
name|getReference
argument_list|(
name|RefreshToken
operator|.
name|class
argument_list|,
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|em
operator|.
name|remove
argument_list|(
name|tokentoRemove
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ServerAccessToken
name|doCreateAccessToken
parameter_list|(
name|AccessTokenRegistration
name|atReg
parameter_list|)
block|{
name|ServerAccessToken
name|at
init|=
name|super
operator|.
name|doCreateAccessToken
argument_list|(
name|atReg
argument_list|)
decl_stmt|;
comment|// we override this in order to get rid of elementCollections directly injected
comment|// from another entity
comment|// this can be the case when using multiple cmt dataProvider operation in a single entityManager
comment|// lifespan
if|if
condition|(
name|at
operator|.
name|getAudiences
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|at
operator|.
name|setAudiences
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|at
operator|.
name|getAudiences
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|at
operator|.
name|getExtraProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|at
operator|.
name|setExtraProperties
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|at
operator|.
name|getExtraProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|at
operator|.
name|getScopes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|at
operator|.
name|setScopes
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|at
operator|.
name|getScopes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|at
operator|.
name|getParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|at
operator|.
name|setParameters
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|at
operator|.
name|getParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|at
return|;
block|}
specifier|protected
name|void
name|saveAccessToken
parameter_list|(
specifier|final
name|ServerAccessToken
name|serverToken
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
init|=
operator|new
name|LinkedList
argument_list|<
name|OAuthPermission
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|OAuthPermission
name|perm
range|:
name|serverToken
operator|.
name|getScopes
argument_list|()
control|)
block|{
name|OAuthPermission
name|permSaved
init|=
name|em
operator|.
name|find
argument_list|(
name|OAuthPermission
operator|.
name|class
argument_list|,
name|perm
operator|.
name|getPermission
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|permSaved
operator|!=
literal|null
condition|)
block|{
name|perms
operator|.
name|add
argument_list|(
name|permSaved
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|em
operator|.
name|persist
argument_list|(
name|perm
argument_list|)
expr_stmt|;
name|perms
operator|.
name|add
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
block|}
name|serverToken
operator|.
name|setScopes
argument_list|(
name|perms
argument_list|)
expr_stmt|;
if|if
condition|(
name|serverToken
operator|.
name|getSubject
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|UserSubject
name|sub
init|=
name|em
operator|.
name|find
argument_list|(
name|UserSubject
operator|.
name|class
argument_list|,
name|serverToken
operator|.
name|getSubject
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sub
operator|==
literal|null
condition|)
block|{
name|em
operator|.
name|persist
argument_list|(
name|serverToken
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sub
operator|=
name|em
operator|.
name|merge
argument_list|(
name|serverToken
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|serverToken
operator|.
name|setSubject
argument_list|(
name|sub
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ensure we have a managed association
comment|// (needed for OpenJPA : InvalidStateException: Encountered unmanaged object)
if|if
condition|(
name|serverToken
operator|.
name|getClient
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|serverToken
operator|.
name|setClient
argument_list|(
name|em
operator|.
name|find
argument_list|(
name|Client
operator|.
name|class
argument_list|,
name|serverToken
operator|.
name|getClient
argument_list|()
operator|.
name|getClientId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|em
operator|.
name|persist
argument_list|(
name|serverToken
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|saveRefreshToken
parameter_list|(
name|RefreshToken
name|refreshToken
parameter_list|)
block|{
name|persistEntity
argument_list|(
name|refreshToken
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|persistEntity
parameter_list|(
specifier|final
name|Object
name|entity
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|em
operator|.
name|persist
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeEntity
parameter_list|(
specifier|final
name|Object
name|entity
parameter_list|)
block|{
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|em
operator|.
name|remove
argument_list|(
name|entity
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
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
parameter_list|,
name|EntityManager
name|entityManager
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
name|CLIENT_QUERY
argument_list|,
name|Client
operator|.
name|class
argument_list|)
return|;
block|}
return|return
name|entityManager
operator|.
name|createQuery
argument_list|(
name|CLIENT_QUERY
operator|+
literal|" WHERE ros.login = :login"
argument_list|,
name|Client
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"login"
argument_list|,
name|resourceOwnerSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|TypedQuery
argument_list|<
name|BearerAccessToken
argument_list|>
name|getTokensQuery
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|resourceOwnerSubject
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
operator|&&
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
literal|"SELECT t FROM BearerAccessToken t"
argument_list|,
name|BearerAccessToken
operator|.
name|class
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT t FROM BearerAccessToken t"
operator|+
literal|" JOIN t.subject s"
operator|+
literal|" WHERE s.login = :login"
argument_list|,
name|BearerAccessToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"login"
argument_list|,
name|resourceOwnerSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
return|;
block|}
elseif|else
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
literal|"SELECT t FROM BearerAccessToken t"
operator|+
literal|" JOIN t.client c"
operator|+
literal|" WHERE c.clientId = :clientId"
argument_list|,
name|BearerAccessToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"clientId"
argument_list|,
name|c
operator|.
name|getClientId
argument_list|()
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
literal|"SELECT t FROM BearerAccessToken t"
operator|+
literal|" JOIN t.subject s"
operator|+
literal|" JOIN t.client c"
operator|+
literal|" WHERE s.login = :login AND c.clientId = :clientId"
argument_list|,
name|BearerAccessToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"login"
argument_list|,
name|resourceOwnerSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"clientId"
argument_list|,
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
return|;
block|}
block|}
specifier|protected
name|TypedQuery
argument_list|<
name|RefreshToken
argument_list|>
name|getRefreshTokensQuery
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|resourceOwnerSubject
parameter_list|,
name|EntityManager
name|entityManager
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
operator|&&
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
literal|"SELECT t FROM RefreshToken t"
argument_list|,
name|RefreshToken
operator|.
name|class
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
name|entityManager
operator|.
name|createQuery
argument_list|(
literal|"SELECT t FROM RefreshToken t"
operator|+
literal|" JOIN t.subject s"
operator|+
literal|" WHERE s.login = :login"
argument_list|,
name|RefreshToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"login"
argument_list|,
name|resourceOwnerSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
return|;
block|}
elseif|else
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
literal|"SELECT t FROM RefreshToken t"
operator|+
literal|" JOIN t.client c"
operator|+
literal|" WHERE c.clientId = :clientId"
argument_list|,
name|RefreshToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"clientId"
argument_list|,
name|c
operator|.
name|getClientId
argument_list|()
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
literal|"SELECT t FROM RefreshToken t"
operator|+
literal|" JOIN t.subject s"
operator|+
literal|" JOIN t.client c"
operator|+
literal|" WHERE s.login = :login AND c.clientId = :clientId"
argument_list|,
name|RefreshToken
operator|.
name|class
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"login"
argument_list|,
name|resourceOwnerSubject
operator|.
name|getLogin
argument_list|()
argument_list|)
operator|.
name|setParameter
argument_list|(
literal|"clientId"
argument_list|,
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns the entityManaged used for the current operation.      */
specifier|protected
name|EntityManager
name|getEntityManager
parameter_list|()
block|{
return|return
name|entityManagerFactory
operator|.
name|createEntityManager
argument_list|()
return|;
block|}
comment|/**      * Begins the current transaction.      *      * This method needs to be overridden in a CMT environment.      */
specifier|protected
name|EntityTransaction
name|beginIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|EntityTransaction
name|tx
init|=
name|em
operator|.
name|getTransaction
argument_list|()
decl_stmt|;
name|tx
operator|.
name|begin
argument_list|()
expr_stmt|;
return|return
name|tx
return|;
block|}
comment|/**      * Flush the current transaction.      */
specifier|protected
name|void
name|flushIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|em
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * Commits the current transaction.      *      * This method needs to be overridden in a CMT environment.      */
specifier|protected
name|void
name|commitIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|em
operator|.
name|getTransaction
argument_list|()
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|/**      * Closes the current em.      *      * This method needs to be overriden in a CMT environment.      */
specifier|protected
name|void
name|closeIfNeeded
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
name|em
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|public
interface|interface
name|EntityManagerOperation
parameter_list|<
name|T
parameter_list|>
block|{
name|T
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

