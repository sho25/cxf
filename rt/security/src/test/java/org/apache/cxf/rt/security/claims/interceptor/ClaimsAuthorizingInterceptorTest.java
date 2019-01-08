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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|common
operator|.
name|security
operator|.
name|SimpleGroup
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
name|common
operator|.
name|security
operator|.
name|SimplePrincipal
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
name|interceptor
operator|.
name|security
operator|.
name|AccessDeniedException
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
name|interceptor
operator|.
name|security
operator|.
name|SecureAnnotationsInterceptor
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimBean
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimsSecurityContext
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|SAMLClaim
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
name|security
operator|.
name|SecurityContext
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
name|security
operator|.
name|claims
operator|.
name|authorization
operator|.
name|Claim
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
name|security
operator|.
name|claims
operator|.
name|authorization
operator|.
name|ClaimMode
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
name|security
operator|.
name|claims
operator|.
name|authorization
operator|.
name|Claims
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|ClaimsAuthorizingInterceptorTest
block|{
specifier|private
name|ClaimsAuthorizingInterceptor
name|interceptor
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|interceptor
operator|=
operator|new
name|ClaimsAuthorizingInterceptor
argument_list|()
expr_stmt|;
name|interceptor
operator|.
name|setNameAliases
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"authentication"
argument_list|,
literal|"http://authentication"
argument_list|)
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|setFormatAliases
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"claims"
argument_list|,
literal|"http://claims"
argument_list|)
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|setSecuredObject
argument_list|(
operator|new
name|TestService
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClaimDefaultNameAndFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"claimWithDefaultNameAndFormat"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|,
literal|"user"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doTestClaims
argument_list|(
literal|"claimWithDefaultNameAndFormat"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"user"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNonSAMLClaimDefaultNameAndFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
name|claim1
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
argument_list|()
decl_stmt|;
name|claim1
operator|.
name|setClaimType
argument_list|(
literal|"role"
argument_list|)
expr_stmt|;
name|claim1
operator|.
name|setValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"admin"
argument_list|,
literal|"user"
argument_list|)
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
name|claim2
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
argument_list|()
decl_stmt|;
name|claim2
operator|.
name|setClaimType
argument_list|(
literal|"http://authentication"
argument_list|)
expr_stmt|;
name|claim2
operator|.
name|setValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
name|prepareMessage
argument_list|(
name|TestService
operator|.
name|class
argument_list|,
literal|"claimWithSpecificName"
argument_list|,
literal|"role"
argument_list|,
name|claim1
argument_list|,
name|claim2
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
try|try
block|{
name|claim1
operator|.
name|setValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"user"
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|=
name|prepareMessage
argument_list|(
name|TestService
operator|.
name|class
argument_list|,
literal|"claimWithSpecificName"
argument_list|,
literal|"role"
argument_list|,
name|claim1
argument_list|,
name|claim2
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClaimMatchAll
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"claimMatchAll"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|,
literal|"manager"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doTestClaims
argument_list|(
literal|"claimMatchAll"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|doTestClaims
argument_list|(
literal|"claimMatchAll"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"manager"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMissingExpectedClaim
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"claimWithDefaultNameAndFormat"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doTestClaims
argument_list|(
literal|"claimWithDefaultNameAndFormat"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtraNonExpectedClaim
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"claimWithDefaultNameAndFormat"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|,
literal|"user"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://extra/claims"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"claim"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClaimSpecificNameAndFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"claimWithSpecificNameAndFormat"
argument_list|,
name|createClaim
argument_list|(
literal|"http://cxf/roles"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"admin"
argument_list|,
literal|"user"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doTestClaims
argument_list|(
literal|"claimWithSpecificNameAndFormat"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|,
literal|"user"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClaimLaxMode
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"claimLaxMode"
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
name|doTestClaims
argument_list|(
literal|"claimLaxMode"
argument_list|)
expr_stmt|;
try|try
block|{
name|doTestClaims
argument_list|(
literal|"claimLaxMode"
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"smartcard"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleClaims
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestClaims
argument_list|(
literal|"multipleClaims"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"smartcard"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://location"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"UK"
argument_list|)
argument_list|)
expr_stmt|;
name|doTestClaims
argument_list|(
literal|"multipleClaims"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"password"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://location"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"USA"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|doTestClaims
argument_list|(
literal|"multipleClaims"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://authentication"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"unsecuretransport"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"http://location"
argument_list|,
literal|"http://claims"
argument_list|,
literal|"UK"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUserInRoleAndClaims
parameter_list|()
throws|throws
name|Exception
block|{
name|SecureAnnotationsInterceptor
name|in
init|=
operator|new
name|SecureAnnotationsInterceptor
argument_list|()
decl_stmt|;
name|in
operator|.
name|setAnnotationClassName
argument_list|(
name|SecureRole
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setSecuredObject
argument_list|(
operator|new
name|TestService2
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
name|prepareMessage
argument_list|(
name|TestService2
operator|.
name|class
argument_list|,
literal|"test"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"admin"
argument_list|)
argument_list|,
name|createClaim
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|in
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|ClaimsAuthorizingInterceptor
name|in2
init|=
operator|new
name|ClaimsAuthorizingInterceptor
argument_list|()
decl_stmt|;
name|SAMLClaim
name|claim
init|=
operator|new
name|SAMLClaim
argument_list|()
decl_stmt|;
name|claim
operator|.
name|setNameFormat
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setName
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|claim
operator|.
name|addValue
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|in2
operator|.
name|setClaims
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"test"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|ClaimBean
argument_list|(
name|claim
argument_list|,
literal|"a"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|in2
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
try|try
block|{
name|in
operator|.
name|handleMessage
argument_list|(
name|prepareMessage
argument_list|(
name|TestService2
operator|.
name|class
argument_list|,
literal|"test"
argument_list|,
name|createDefaultClaim
argument_list|(
literal|"user"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"AccessDeniedException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|private
name|void
name|doTestClaims
parameter_list|(
name|String
name|methodName
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
modifier|...
name|claim
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|m
init|=
name|prepareMessage
argument_list|(
name|TestService
operator|.
name|class
argument_list|,
name|methodName
argument_list|,
name|claim
argument_list|)
decl_stmt|;
name|interceptor
operator|.
name|handleMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|prepareMessage
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|methodName
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
modifier|...
name|claim
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|prepareMessage
argument_list|(
name|cls
argument_list|,
name|methodName
argument_list|,
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
name|claim
argument_list|)
return|;
block|}
specifier|private
name|Message
name|prepareMessage
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|String
name|methodName
parameter_list|,
name|String
name|roleName
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
modifier|...
name|claim
parameter_list|)
throws|throws
name|Exception
block|{
name|ClaimCollection
name|claims
init|=
operator|new
name|ClaimCollection
argument_list|()
decl_stmt|;
name|claims
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|claim
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|roleName
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified"
argument_list|)
decl_stmt|;
name|ClaimsSecurityContext
name|sc
init|=
operator|new
name|ClaimsSecurityContext
argument_list|()
block|{
specifier|private
name|Principal
name|p
init|=
operator|new
name|SimplePrincipal
argument_list|(
literal|"user"
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
return|return
name|p
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|role
parameter_list|)
block|{
if|if
condition|(
name|roles
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Principal
name|principalRole
range|:
name|roles
control|)
block|{
if|if
condition|(
name|principalRole
operator|!=
name|p
operator|&&
name|principalRole
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|role
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Subject
name|getSubject
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Principal
argument_list|>
name|getUserRoles
parameter_list|()
block|{
return|return
name|roles
return|;
block|}
annotation|@
name|Override
specifier|public
name|ClaimCollection
name|getClaims
parameter_list|()
block|{
return|return
name|claims
return|;
block|}
block|}
decl_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
operator|new
name|ExchangeImpl
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"org.apache.cxf.resource.method"
argument_list|,
name|cls
operator|.
name|getMethod
argument_list|(
name|methodName
argument_list|,
operator|new
name|Class
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
specifier|private
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
name|createDefaultClaim
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
block|{
return|return
name|createClaim
argument_list|(
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
argument_list|,
literal|"urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified"
argument_list|,
name|values
argument_list|)
return|;
block|}
specifier|private
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
name|createClaim
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|values
parameter_list|)
block|{
name|SAMLClaim
name|claim
init|=
operator|new
name|SAMLClaim
argument_list|()
decl_stmt|;
name|claim
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setNameFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|claim
operator|.
name|setValues
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|claim
return|;
block|}
annotation|@
name|Claim
argument_list|(
name|name
operator|=
literal|"authentication"
argument_list|,
name|format
operator|=
literal|"claims"
argument_list|,
name|value
operator|=
literal|"password"
argument_list|)
specifier|public
specifier|static
class|class
name|TestService
block|{
comment|// default name and format are used
annotation|@
name|Claim
argument_list|(
block|{
literal|"admin"
block|,
literal|"manager"
block|}
argument_list|)
specifier|public
name|void
name|claimWithDefaultNameAndFormat
parameter_list|()
block|{          }
comment|// explicit name and format
annotation|@
name|Claim
argument_list|(
name|name
operator|=
literal|"http://cxf/roles"
argument_list|,
name|format
operator|=
literal|"http://claims"
argument_list|,
name|value
operator|=
block|{
literal|"admin"
block|,
literal|"manager"
block|}
argument_list|)
specifier|public
name|void
name|claimWithSpecificNameAndFormat
parameter_list|()
block|{          }
comment|// explicit name
annotation|@
name|Claim
argument_list|(
name|name
operator|=
literal|"role"
argument_list|,
name|value
operator|=
block|{
literal|"admin"
block|,
literal|"manager"
block|}
argument_list|)
specifier|public
name|void
name|claimWithSpecificName
parameter_list|()
block|{          }
annotation|@
name|Claim
argument_list|(
name|name
operator|=
literal|"http://authentication"
argument_list|,
name|format
operator|=
literal|"http://claims"
argument_list|,
name|value
operator|=
literal|"password"
argument_list|,
name|mode
operator|=
name|ClaimMode
operator|.
name|LAX
argument_list|)
specifier|public
name|void
name|claimLaxMode
parameter_list|()
block|{          }
annotation|@
name|Claims
argument_list|(
block|{
annotation|@
name|Claim
argument_list|(
name|name
operator|=
literal|"http://location"
argument_list|,
name|format
operator|=
literal|"http://claims"
argument_list|,
name|value
operator|=
block|{
literal|"UK"
block|,
literal|"USA"
block|}
argument_list|)
block|,
annotation|@
name|Claim
argument_list|(
name|value
operator|=
block|{
literal|"admin"
block|,
literal|"manager"
block|}
argument_list|)
block|,
annotation|@
name|Claim
argument_list|(
name|name
operator|=
literal|"authentication"
argument_list|,
name|format
operator|=
literal|"claims"
argument_list|,
name|value
operator|=
block|{
literal|"password"
block|,
literal|"smartcard"
block|}
argument_list|)
block|}
argument_list|)
specifier|public
name|void
name|multipleClaims
parameter_list|()
block|{          }
comment|// user must have both admin and manager roles, default is 'or'
annotation|@
name|Claim
argument_list|(
name|value
operator|=
block|{
literal|"admin"
block|,
literal|"manager"
block|}
argument_list|,
name|matchAll
operator|=
literal|true
argument_list|)
specifier|public
name|void
name|claimMatchAll
parameter_list|()
block|{          }
block|}
specifier|public
specifier|static
class|class
name|TestService2
block|{
annotation|@
name|SecureRole
argument_list|(
literal|"admin"
argument_list|)
specifier|public
name|void
name|test
parameter_list|()
block|{          }
block|}
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|METHOD
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
specifier|public
annotation_defn|@interface
name|SecureRole
block|{
name|String
index|[]
name|value
parameter_list|()
function_decl|;
block|}
specifier|private
specifier|static
name|Set
argument_list|<
name|Principal
argument_list|>
name|parseRolesFromClaims
parameter_list|(
name|ClaimCollection
name|claims
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|nameFormat
parameter_list|)
block|{
name|String
name|roleAttributeName
init|=
name|name
decl_stmt|;
if|if
condition|(
name|roleAttributeName
operator|==
literal|null
condition|)
block|{
name|roleAttributeName
operator|=
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
expr_stmt|;
block|}
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|Claim
name|claim
range|:
name|claims
control|)
block|{
if|if
condition|(
name|claim
operator|instanceof
name|SAMLClaim
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|roleAttributeName
argument_list|)
operator|&&
operator|(
name|nameFormat
operator|==
literal|null
operator|||
name|nameFormat
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|SAMLClaim
operator|)
name|claim
operator|)
operator|.
name|getNameFormat
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|addValues
argument_list|(
name|roles
argument_list|,
name|claim
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// Don't search for other attributes with the same name if> 1 claim value
break|break;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|claim
operator|.
name|getClaimType
argument_list|()
operator|.
name|equals
argument_list|(
name|roleAttributeName
argument_list|)
condition|)
block|{
name|addValues
argument_list|(
name|roles
argument_list|,
name|claim
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|claim
operator|.
name|getValues
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// Don't search for other attributes with the same name if> 1 claim value
break|break;
block|}
block|}
block|}
return|return
name|roles
return|;
block|}
specifier|private
specifier|static
name|void
name|addValues
parameter_list|(
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|)
block|{
for|for
control|(
name|Object
name|claimValue
range|:
name|values
control|)
block|{
if|if
condition|(
name|claimValue
operator|instanceof
name|String
condition|)
block|{
name|roles
operator|.
name|add
argument_list|(
operator|new
name|SimpleGroup
argument_list|(
operator|(
name|String
operator|)
name|claimValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

