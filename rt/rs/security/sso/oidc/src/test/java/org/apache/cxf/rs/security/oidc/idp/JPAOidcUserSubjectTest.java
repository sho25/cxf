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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|oidc
operator|.
name|common
operator|.
name|IdToken
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
name|JPAOidcUserSubjectTest
extends|extends
name|Assert
block|{
specifier|private
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
name|JPAOAuthDataProvider
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|JPAOAuthDataProvider
name|oauthDataProvider
parameter_list|)
block|{
name|oauthDataProvider
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
name|oauthDataProvider
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
name|String
name|getPersistenceUnitName
parameter_list|()
block|{
return|return
literal|"testUnitHibernate"
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAccessTokenWithOidcUserSubject
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
name|OidcUserSubject
name|oidcSubject
init|=
operator|new
name|OidcUserSubject
argument_list|()
decl_stmt|;
name|oidcSubject
operator|.
name|setLogin
argument_list|(
literal|"bob"
argument_list|)
expr_stmt|;
name|IdToken
name|idToken
init|=
operator|new
name|IdToken
argument_list|()
decl_stmt|;
name|idToken
operator|.
name|setAudience
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|oidcSubject
operator|.
name|setIdToken
argument_list|(
name|idToken
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setSubject
argument_list|(
name|oidcSubject
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
name|OidcUserSubject
name|oidcSubject2
init|=
operator|(
name|OidcUserSubject
operator|)
name|at2
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|,
name|oidcSubject2
operator|.
name|getIdToken
argument_list|()
operator|.
name|getAudience
argument_list|()
argument_list|)
expr_stmt|;
name|OidcUserSubject
name|oidcSubject3
init|=
operator|new
name|OidcUserSubject
argument_list|()
decl_stmt|;
name|oidcSubject3
operator|.
name|setLogin
argument_list|(
literal|"bob"
argument_list|)
expr_stmt|;
name|IdToken
name|idToken2
init|=
operator|new
name|IdToken
argument_list|()
decl_stmt|;
name|idToken2
operator|.
name|setAudience
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|oidcSubject3
operator|.
name|setIdToken
argument_list|(
name|idToken2
argument_list|)
expr_stmt|;
name|atr
operator|.
name|setSubject
argument_list|(
name|oidcSubject3
argument_list|)
expr_stmt|;
name|ServerAccessToken
name|at3
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
name|at4
init|=
name|getProvider
argument_list|()
operator|.
name|getAccessToken
argument_list|(
name|at3
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|OidcUserSubject
name|oidcSubject4
init|=
operator|(
name|OidcUserSubject
operator|)
name|at4
operator|.
name|getSubject
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|,
name|oidcSubject4
operator|.
name|getIdToken
argument_list|()
operator|.
name|getAudience
argument_list|()
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
name|setResourceOwnerSubject
argument_list|(
operator|new
name|OidcUserSubject
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
name|getProvider
argument_list|()
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
if|if
condition|(
name|getProvider
argument_list|()
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

