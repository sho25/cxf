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
name|utils
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
name|LinkedHashSet
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
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
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
name|AuthorizationUtilsTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testThrowAuthorizationFailureSingleChallenge
parameter_list|()
block|{
try|try
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"Basic"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebApplicationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|Response
name|r
init|=
name|ex
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Basic"
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThrowAuthorizationFailureManyChallenges
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|challenges
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|challenges
operator|.
name|add
argument_list|(
literal|"Basic"
argument_list|)
expr_stmt|;
name|challenges
operator|.
name|add
argument_list|(
literal|"Bearer"
argument_list|)
expr_stmt|;
try|try
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|challenges
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebApplicationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|Response
name|r
init|=
name|ex
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Basic,Bearer"
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThrowAuthorizationFailureNoChallenge
parameter_list|()
block|{
try|try
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebApplicationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|Response
name|r
init|=
name|ex
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThrowAuthorizationFailureWithCause
parameter_list|()
block|{
try|try
block|{
name|AuthorizationUtils
operator|.
name|throwAuthorizationFailure
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|"Basic"
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|RuntimeException
argument_list|(
literal|"expired token"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"WebApplicationException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|ex
parameter_list|)
block|{
name|Response
name|r
init|=
name|ex
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"expired token"
argument_list|,
name|r
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|r
operator|.
name|getMetadata
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Basic"
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

