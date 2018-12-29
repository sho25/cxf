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
name|Ignore
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|JCacheCodeDataProviderTest
block|{
specifier|private
name|JCacheCodeDataProvider
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
name|provider
operator|=
operator|new
name|JCacheCodeDataProvider
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteCodeGrants
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"111"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
name|AuthorizationCodeRegistration
name|atr
init|=
operator|new
name|AuthorizationCodeRegistration
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
name|ServerAuthorizationCodeGrant
name|grant
init|=
name|provider
operator|.
name|createCodeGrant
argument_list|(
name|atr
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|grants
init|=
name|provider
operator|.
name|getCodeGrants
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
name|grants
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|grants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|,
name|grants
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|grants
operator|=
name|provider
operator|.
name|getCodeGrants
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|grants
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|grants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|,
name|grants
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|ServerAuthorizationCodeGrant
name|grant2
init|=
name|provider
operator|.
name|removeCodeGrant
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|grant
operator|.
name|getCode
argument_list|()
argument_list|,
name|grant2
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|grants
operator|=
name|provider
operator|.
name|getCodeGrants
argument_list|(
name|c
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|grants
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|grants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Test
specifier|public
name|void
name|testAddGetDeleteCodeGrants2
parameter_list|()
block|{
name|Client
name|c
init|=
name|addClient
argument_list|(
literal|"111"
argument_list|,
literal|"bob"
argument_list|)
decl_stmt|;
name|AuthorizationCodeRegistration
name|atr
init|=
operator|new
name|AuthorizationCodeRegistration
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
name|provider
operator|.
name|createCodeGrant
argument_list|(
name|atr
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServerAuthorizationCodeGrant
argument_list|>
name|grants
init|=
name|provider
operator|.
name|getCodeGrants
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
name|grants
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|grants
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|provider
operator|.
name|removeClient
argument_list|(
name|c
operator|.
name|getClientId
argument_list|()
argument_list|)
expr_stmt|;
name|grants
operator|=
name|provider
operator|.
name|getCodeGrants
argument_list|(
name|c
argument_list|,
name|c
operator|.
name|getResourceOwnerSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|grants
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|grants
operator|.
name|size
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
name|UserSubject
argument_list|(
name|userLogin
argument_list|)
argument_list|)
expr_stmt|;
name|provider
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
if|if
condition|(
name|provider
operator|!=
literal|null
condition|)
block|{
name|provider
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

