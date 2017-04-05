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
name|code
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
name|EntityNotFoundException
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
name|JPAOAuthDataProvider
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

begin_class
specifier|public
class|class
name|JPACodeDataProvider
extends|extends
name|JPAOAuthDataProvider
implements|implements
name|AuthorizationCodeDataProvider
block|{
specifier|private
name|long
name|codeLifetime
init|=
literal|10
operator|*
literal|60
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|createCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|doCreateCodeGrant
argument_list|(
name|reg
argument_list|)
decl_stmt|;
name|saveCodeGrant
argument_list|(
name|grant
argument_list|)
expr_stmt|;
return|return
name|grant
return|;
block|}
specifier|protected
name|ServerAuthorizationCodeGrant
name|doCreateCodeGrant
parameter_list|(
name|AuthorizationCodeRegistration
name|reg
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|AbstractCodeDataProvider
operator|.
name|initCodeGrant
argument_list|(
name|reg
argument_list|,
name|codeLifetime
argument_list|)
return|;
block|}
specifier|protected
name|void
name|saveCodeGrant
parameter_list|(
specifier|final
name|ServerAuthorizationCodeGrant
name|grant
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
name|grant
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
name|grant
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
name|grant
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
name|grant
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|grant
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
name|grant
operator|.
name|getClient
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|grant
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
name|grant
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
name|grant
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
name|removeClientCodeGrants
argument_list|(
name|c
argument_list|,
name|em
argument_list|)
expr_stmt|;
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
specifier|protected
name|void
name|removeClientCodeGrants
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
name|removeClientCodeGrants
argument_list|(
name|c
argument_list|,
name|em
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
name|removeClientCodeGrants
parameter_list|(
specifier|final
name|Client
name|c
parameter_list|,
name|EntityManager
name|em
parameter_list|)
block|{
for|for
control|(
name|ServerAuthorizationCodeGrant
name|grant
range|:
name|getCodeGrants
argument_list|(
name|c
argument_list|,
literal|null
argument_list|,
name|em
argument_list|)
control|)
block|{
name|removeCodeGrant
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|,
name|em
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|removeCodeGrant
parameter_list|(
specifier|final
name|String
name|code
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|executeInTransaction
argument_list|(
operator|new
name|EntityManagerOperation
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ServerAuthorizationCodeGrant
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|removeCodeGrant
argument_list|(
name|code
argument_list|,
name|em
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|private
name|ServerAuthorizationCodeGrant
name|removeCodeGrant
parameter_list|(
name|String
name|code
parameter_list|,
name|EntityManager
name|em
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|em
operator|.
name|getReference
argument_list|(
name|ServerAuthorizationCodeGrant
operator|.
name|class
argument_list|,
name|code
argument_list|)
decl_stmt|;
try|try
block|{
name|em
operator|.
name|remove
argument_list|(
name|grant
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EntityNotFoundException
name|e
parameter_list|)
block|{         }
return|return
name|grant
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|getCodeGrants
parameter_list|(
specifier|final
name|Client
name|c
parameter_list|,
specifier|final
name|UserSubject
name|subject
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
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|execute
parameter_list|(
name|EntityManager
name|em
parameter_list|)
block|{
return|return
name|getCodeGrants
argument_list|(
name|c
argument_list|,
name|subject
argument_list|,
name|em
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|getCodeGrants
parameter_list|(
specifier|final
name|Client
name|c
parameter_list|,
specifier|final
name|UserSubject
name|subject
parameter_list|,
name|EntityManager
name|em
parameter_list|)
throws|throws
name|OAuthServiceException
block|{
return|return
name|getCodesQuery
argument_list|(
name|c
argument_list|,
name|subject
argument_list|,
name|em
argument_list|)
operator|.
name|getResultList
argument_list|()
return|;
block|}
specifier|public
name|void
name|setCodeLifetime
parameter_list|(
name|long
name|codeLifetime
parameter_list|)
block|{
name|this
operator|.
name|codeLifetime
operator|=
name|codeLifetime
expr_stmt|;
block|}
specifier|protected
name|TypedQuery
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|getCodesQuery
parameter_list|(
name|Client
name|c
parameter_list|,
name|UserSubject
name|resourceOwnerSubject
parameter_list|,
name|EntityManager
name|em
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
name|em
operator|.
name|createQuery
argument_list|(
literal|"SELECT c FROM ServerAuthorizationCodeGrant c"
argument_list|,
name|ServerAuthorizationCodeGrant
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
name|em
operator|.
name|createQuery
argument_list|(
literal|"SELECT c FROM ServerAuthorizationCodeGrant"
operator|+
literal|" c JOIN c.subject s"
operator|+
literal|" WHERE s.login = :login"
argument_list|,
name|ServerAuthorizationCodeGrant
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
name|em
operator|.
name|createQuery
argument_list|(
literal|"SELECT code FROM ServerAuthorizationCodeGrant code"
operator|+
literal|" JOIN code.client c"
operator|+
literal|" WHERE c.clientId = :clientId"
argument_list|,
name|ServerAuthorizationCodeGrant
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
name|em
operator|.
name|createQuery
argument_list|(
literal|"SELECT code FROM ServerAuthorizationCodeGrant code"
operator|+
literal|" JOIN code.subject s"
operator|+
literal|" JOIN code.client c"
operator|+
literal|" WHERE s.login = :login"
operator|+
literal|" AND c.clientId = :clientId"
argument_list|,
name|ServerAuthorizationCodeGrant
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
block|}
block|}
end_class

end_unit

