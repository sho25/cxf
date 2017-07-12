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
name|utils
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
name|UserInfo
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|OidcUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testScopeToClaimsMappingNoValue
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|claims
init|=
name|OidcUtils
operator|.
name|getScopeClaims
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|claims
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|claims
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
name|testScopeToClaimsMappingSingleValue
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|claims
init|=
name|OidcUtils
operator|.
name|getScopeClaims
argument_list|(
name|OidcUtils
operator|.
name|PHONE_SCOPE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|claims
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|claims
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|UserInfo
operator|.
name|PHONE_CLAIM
argument_list|,
name|claims
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testScopeToClaimsMappingMultiValue
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|claims
init|=
name|OidcUtils
operator|.
name|getScopeClaims
argument_list|(
name|OidcUtils
operator|.
name|PROFILE_SCOPE
argument_list|,
name|OidcUtils
operator|.
name|PHONE_SCOPE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|claims
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|claims
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

