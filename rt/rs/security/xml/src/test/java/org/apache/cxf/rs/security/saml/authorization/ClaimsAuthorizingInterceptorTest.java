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
name|saml
operator|.
name|authorization
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
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
name|security
operator|.
name|SecurityContext
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
name|ClaimsAuthorizingInterceptorTest
extends|extends
name|Assert
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
name|claim
init|=
operator|new
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
operator|.
name|Claim
modifier|...
name|claim
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
argument_list|>
name|claims
init|=
operator|new
name|ArrayList
argument_list|<
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|claim
argument_list|)
argument_list|)
decl_stmt|;
name|SecurityContext
name|sc
init|=
operator|new
name|SAMLSecurityContext
argument_list|(
operator|new
name|Subject
argument_list|(
literal|"user"
argument_list|)
argument_list|,
name|claims
argument_list|)
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
operator|.
name|Claim
name|createDefaultClaim
parameter_list|(
name|String
modifier|...
name|values
parameter_list|)
block|{
return|return
name|createClaim
argument_list|(
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
operator|.
name|DEFAULT_ROLE_NAME
argument_list|,
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
operator|.
name|DEFAULT_NAME_FORMAT
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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
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
name|String
modifier|...
name|values
parameter_list|)
block|{
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
name|claim
init|=
operator|new
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
name|saml
operator|.
name|assertion
operator|.
name|Claim
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
block|{                      }
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
block|{                      }
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
block|{                       }
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
block|{                       }
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
block|{                      }
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
block|{                      }
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
block|}
end_class

end_unit

