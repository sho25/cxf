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
name|systest
operator|.
name|jaxrs
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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
name|Calendar
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
name|Date
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|jose
operator|.
name|jaxrs
operator|.
name|JwtAuthenticationClientFilter
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
name|jose
operator|.
name|jwt
operator|.
name|JwtClaims
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
name|jose
operator|.
name|jwt
operator|.
name|JwtConstants
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
name|jose
operator|.
name|jwt
operator|.
name|JwtToken
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
name|systest
operator|.
name|jaxrs
operator|.
name|security
operator|.
name|Book
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_comment
comment|/**  * Some tests for various properties of JWT tokens.  */
end_comment

begin_class
specifier|public
class|class
name|JWTPropertiesTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerJwtProperties
operator|.
name|PORT
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerJwtProperties
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|registerBouncyCastleIfNeeded
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|registerBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Still need it for Oracle Java 7 and Java 8
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|unregisterBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
name|Security
operator|.
name|removeProvider
argument_list|(
name|BouncyCastleProvider
operator|.
name|PROVIDER_NAME
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
name|testExpiredToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the expiry date to be yesterday
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setExpiryTime
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testFutureToken
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the future
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testNearFutureTokenFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the near future
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testNearFutureTokenSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwtnearfuture/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the near future
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testNotBeforeFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the near future
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setNotBefore
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testNotBeforeSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwtnearfuture/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the issued date to be in the near future
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setNotBefore
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|JwtToken
name|token
init|=
operator|new
name|JwtToken
argument_list|(
name|claims
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_TOKEN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testSetClaimsDirectly
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|address
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_CLAIMS
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testBadAudience
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|String
name|badAddress
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/badunsignedjwt/bookstore/books"
decl_stmt|;
name|claims
operator|.
name|setAudiences
argument_list|(
name|toList
argument_list|(
name|badAddress
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_CLAIMS
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testNoAudience
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_CLAIMS
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
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
name|testMultipleAudiences
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|busFile
init|=
name|JWTPropertiesTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JacksonJsonProvider
argument_list|()
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwtAuthenticationClientFilter
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/unsignedjwt/bookstore/books"
decl_stmt|;
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|providers
argument_list|,
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|type
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
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
name|claims
operator|.
name|setIssuedAt
argument_list|(
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
operator|/
literal|1000L
argument_list|)
expr_stmt|;
name|String
name|badAddress
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/badunsignedjwt/bookstore/books"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|audiences
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|audiences
operator|.
name|add
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|audiences
operator|.
name|add
argument_list|(
name|badAddress
argument_list|)
expr_stmt|;
name|claims
operator|.
name|setAudiences
argument_list|(
name|audiences
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"rs.security.signature.algorithm"
argument_list|,
literal|"none"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|JwtConstants
operator|.
name|JWT_CLAIMS
argument_list|,
name|claims
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|client
argument_list|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|post
argument_list|(
operator|new
name|Book
argument_list|(
literal|"book"
argument_list|,
literal|123L
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|response
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|toList
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|address
argument_list|)
return|;
block|}
block|}
end_class

end_unit

