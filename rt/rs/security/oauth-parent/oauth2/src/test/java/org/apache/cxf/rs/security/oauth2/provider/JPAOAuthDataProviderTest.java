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
name|Arrays
import|;
end_import

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
name|EntityManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Persistence
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
name|OAuthConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JPAOAuthDataProviderTest
extends|extends
name|Assert
block|{
specifier|protected
name|EntityManagerFactory
name|emFactory
decl_stmt|;
specifier|private
name|JPAOAuthDataProvider
name|provider
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|emFactory
operator|=
name|Persistence
operator|.
name|createEntityManagerFactory
argument_list|(
name|getPersistenceUnitName
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|=
operator|new
name|JPAOAuthDataProvider
argument_list|()
expr_stmt|;
name|provider
operator|.
name|setEntityManagerFactory
argument_list|(
name|emFactory
argument_list|)
expr_stmt|;
name|initializeProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Exception during JPA EntityManager creation."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getPersistenceUnitName
parameter_list|()
block|{
return|return
literal|"testUnitHibernate"
return|;
block|}
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|JPAOAuthDataProvider
name|dataProvider
parameter_list|)
block|{
name|dataProvider
operator|.
name|setSupportedScopes
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"A Scope"
argument_list|)
argument_list|)
expr_stmt|;
name|dataProvider
operator|.
name|setSupportedScopes
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"refreshToken"
argument_list|,
literal|"RefreshToken"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|JPAOAuthDataProvider
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteClient
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"12345"
argument_list|,
literal|"alice"
argument_list|)
decl_stmt|;
name|Client
name|c2
init|=
name|getProvider
argument_list|()
operator|.
name|getClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
decl_stmt|;
name|compareClients
argument_list|(
name|c
argument_list|,
name|c2
argument_list|)
expr_stmt|;
name|c2
operator|.
name|setClientSecret
argument_list|(
literal|"567"
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|setClient
argument_list|(
name|c2
argument_list|)
expr_stmt|;
name|Client
name|c22
init|=
name|getProvider
argument_list|()
operator|.
name|getClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
decl_stmt|;
name|compareClients
argument_list|(
name|c2
argument_list|,
name|c22
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|removeClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|Client
name|c3
init|=
name|getProvider
argument_list|()
operator|.
name|getClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|c3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteClients
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"12345"
argument_list|,
literal|"alice"
argument_list|)
decl_stmt|;
name|Client
name|c2
init|=
name|addClient
argument_list|(
literal|"56789"
argument_list|,
literal|"alice"
argument_list|)
decl_stmt|;
name|Client
name|c3
init|=
name|addClient
argument_list|(
literal|"09876"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Client
argument_list|>
name|aliceClients
init|=
name|getProvider
argument_list|()
operator|.
name|getClients
argument_list|(
operator|new
name|UserSubject
argument_list|(
literal|"alice"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|aliceClients
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|aliceClients
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|compareClients
argument_list|(
name|c
argument_list|,
name|aliceClients
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClientId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"12345"
argument_list|)
condition|?
name|aliceClients
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
name|aliceClients
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|compareClients
argument_list|(
name|c2
argument_list|,
name|aliceClients
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClientId
argument_list|()
operator|.
name|equals
argument_list|(
literal|"56789"
argument_list|)
condition|?
name|aliceClients
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
name|aliceClients
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Client
argument_list|>
name|bobClients
init|=
name|getProvider
argument_list|()
operator|.
name|getClients
argument_list|(
operator|new
name|UserSubject
argument_list|(
literal|"bob"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bobClients
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bobClients
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Client
name|bobClient
init|=
name|bobClients
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|compareClients
argument_list|(
name|c3
argument_list|,
name|bobClient
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Client
argument_list|>
name|allClients
init|=
name|getProvider
argument_list|()
operator|.
name|getClients
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|allClients
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|allClients
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|removeClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|removeClient
argument_list|(
name|c2
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|removeClient
argument_list|(
name|c3
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|allClients
operator|=
name|getProvider
argument_list|()
operator|.
name|getClients
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|allClients
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|allClients
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteAccessToken
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"101"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
name|AccessTokenRegistration
name|atr
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|atr
operator|.
name|setClient
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setApprovedScope
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setSubject
argument_list|(
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
name|ServerAccessToken
name|at
init|=
name|getProvider
argument_list|()
operator|.
name|createAccessToken
argument_list|(
name|atr
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|at2
init|=
name|getProvider
argument_list|()
operator|.
name|getAccessToken
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|at2
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|scopes
init|=
name|at2
operator|.
name|getScopes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|scopes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|scopes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthPermission
name|perm
init|=
name|scopes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|perm
operator|.
name|getPermission
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
name|tokens
init|=
name|getProvider
argument_list|()
operator|.
name|getAccessTokens
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|tokens
operator|=
name|getProvider
argument_list|()
operator|.
name|getAccessTokens
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|tokens
operator|=
name|getProvider
argument_list|()
operator|.
name|getAccessTokens
argument_list|(
literal|null
argument_list|,
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|tokens
operator|=
name|getProvider
argument_list|()
operator|.
name|getAccessTokens
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|revokeToken
argument_list|(
name|c
argument_list|,
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|OAuthConstants
operator|.
name|ACCESS_TOKEN
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getProvider
argument_list|()
operator|.
name|getAccessToken
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteAccessToken2
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"102"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
name|AccessTokenRegistration
name|atr
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|atr
operator|.
name|setClient
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setApprovedScope
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setSubject
argument_list|(
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|createAccessToken
argument_list|(
name|atr
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServerAccessToken
argument_list|>
name|tokens
init|=
name|getProvider
argument_list|()
operator|.
name|getAccessTokens
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|removeClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|tokens
operator|=
name|getProvider
argument_list|()
operator|.
name|getAccessTokens
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteRefreshToken
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"101"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
name|AccessTokenRegistration
name|atr
init|=
operator|new
name|AccessTokenRegistration
argument_list|()
decl_stmt|;
name|atr
operator|.
name|setClient
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setApprovedScope
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"refreshToken"
argument_list|)
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setSubject
argument_list|(
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
name|ServerAccessToken
name|at
init|=
name|getProvider
argument_list|()
operator|.
name|createAccessToken
argument_list|(
name|atr
argument_list|)
decl_stmt|;
name|ServerAccessToken
name|at2
init|=
name|getProvider
argument_list|()
operator|.
name|getAccessToken
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|at
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|at2
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|scopes
init|=
name|at2
operator|.
name|getScopes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|scopes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|scopes
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthPermission
name|perm
init|=
name|scopes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|perm
operator|.
name|getPermission
argument_list|()
argument_list|)
expr_stmt|;
name|OAuthPermission
name|perm2
init|=
name|scopes
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"refreshToken"
argument_list|,
name|perm2
operator|.
name|getPermission
argument_list|()
argument_list|)
expr_stmt|;
name|RefreshToken
name|rt
init|=
name|getProvider
argument_list|()
operator|.
name|getRefreshToken
argument_list|(
name|at2
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|rt
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|at2
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|rt
operator|.
name|getAccessTokens
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RefreshToken
argument_list|>
name|tokens
init|=
name|getProvider
argument_list|()
operator|.
name|getRefreshTokens
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTokenKey
argument_list|()
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|revokeToken
argument_list|(
name|c
argument_list|,
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|,
name|OAuthConstants
operator|.
name|REFRESH_TOKEN
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getProvider
argument_list|()
operator|.
name|getRefreshToken
argument_list|(
name|rt
operator|.
name|getTokenKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Client
name|addClient
parameter_list|(
name|String
name|clientId
parameter_list|,
name|String
name|userLogin
parameter_list|)
block|{
name|Client
name|c
init|=
operator|new
name|Client
argument_list|()
decl_stmt|;
name|c
operator|.
name|setRedirectUris
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"http://client/redirect"
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|setClientId
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
name|c
operator|.
name|setClientSecret
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
name|c
operator|.
name|setResourceOwnerSubject
argument_list|(
operator|new
name|UserSubject
argument_list|(
name|userLogin
argument_list|)
argument_list|)
expr_stmt|;
name|getProvider
argument_list|()
operator|.
name|setClient
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
specifier|private
name|void
name|compareClients
parameter_list|(
name|Client
name|c
parameter_list|,
name|Client
name|c2
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|c2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|,
name|c2
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|c
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|c2
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://client/redirect"
argument_list|,
name|c
operator|.
name|getRedirectUris
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
operator|.
name|getLogin
argument_list|()
argument_list|,
name|c2
operator|.
name|getResourceOwnerSubject
argument_list|()
operator|.
name|getLogin
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|getProvider
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|emFactory
operator|!=
literal|null
condition|)
block|{
name|emFactory
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
comment|//connection.createStatement().execute("SHUTDOWN");
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

