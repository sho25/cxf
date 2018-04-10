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
name|jose
operator|.
name|jwt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * Some tests for JwtUtils  */
end_comment

begin_class
specifier|public
class|class
name|JwtUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testExpiredToken
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
comment|// Set the expiry date to be yesterday
name|ZonedDateTime
name|dateTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
operator|.
name|minusDays
argument_list|(
literal|1L
argument_list|)
decl_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|dateTime
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtExpiry
argument_list|(
name|claims
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an expired token"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testFutureToken
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the future
name|ZonedDateTime
name|dateTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
operator|.
name|plusDays
argument_list|(
literal|1L
argument_list|)
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|dateTime
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|claims
argument_list|,
literal|300
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a token issued in the future"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNearFutureToken
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the near future
name|ZonedDateTime
name|dateTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
operator|.
name|plusSeconds
argument_list|(
literal|30L
argument_list|)
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|dateTime
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|claims
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on a token issued in the future"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// Now set the clock offset
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|claims
argument_list|,
literal|0
argument_list|,
literal|60
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testNotBefore
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the near future
name|ZonedDateTime
name|dateTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|dateTime
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setNotBefore
argument_list|(
name|dateTime
operator|.
name|plusSeconds
argument_list|(
literal|30L
argument_list|)
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtNotBefore
argument_list|(
name|claims
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on not before"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// Now set the clock offset
name|JwtUtils
operator|.
name|validateJwtNotBefore
argument_list|(
name|claims
argument_list|,
literal|60
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testIssuedAtTTL
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
comment|// Set the issued date to be now
name|ZonedDateTime
name|dateTime
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
decl_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|dateTime
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now test the TTL
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|claims
argument_list|,
literal|60
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Now create the token 70 seconds ago
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|dateTime
operator|.
name|minusSeconds
argument_list|(
literal|70L
argument_list|)
operator|.
name|toEpochSecond
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtIssuedAt
argument_list|(
name|claims
argument_list|,
literal|60
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an expired token"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Test
specifier|public
name|void
name|testExpectedAudience
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create the JWT Token
name|JwtClaims
name|claims
init|=
operator|new
name|JwtClaims
argument_list|()
decl_stmt|;
name|claims
operator|.
name|setSubject
argument_list|(
literal|"alice"
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuer
argument_list|(
literal|"DoubleItSTSIssuer"
argument_list|)
expr_stmt|;
comment|// No aud claim should validate OK
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|JwtUtils
operator|.
name|validateJwtAudienceRestriction
argument_list|(
name|claims
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// It should fail when we have an unknown aud claim
name|claims
operator|.
name|setAudience
argument_list|(
literal|"Receiver"
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtAudienceRestriction
argument_list|(
name|claims
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an invalid audience"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
comment|// Here the aud claim matches what is expected
name|message
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|EXPECTED_CLAIM_AUDIENCE
argument_list|,
literal|"Receiver"
argument_list|)
expr_stmt|;
name|JwtUtils
operator|.
name|validateJwtAudienceRestriction
argument_list|(
name|claims
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// It should fail when the expected aud claim is not present
name|claims
operator|.
name|removeProperty
argument_list|(
name|JwtConstants
operator|.
name|CLAIM_AUDIENCE
argument_list|)
expr_stmt|;
try|try
block|{
name|JwtUtils
operator|.
name|validateJwtAudienceRestriction
argument_list|(
name|claims
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an invalid audience"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JwtException
name|ex
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

