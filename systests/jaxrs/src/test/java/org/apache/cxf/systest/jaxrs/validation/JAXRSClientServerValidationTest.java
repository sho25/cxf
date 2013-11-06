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
name|validation
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Form
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
name|MediaType
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
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
name|Interceptor
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
name|JAXRSServerFactoryBean
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
name|jaxrs
operator|.
name|interceptor
operator|.
name|JAXRSOutExceptionMapperInterceptor
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
name|lifecycle
operator|.
name|SingletonResourceProvider
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
name|model
operator|.
name|AbstractResourceInfo
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
name|validation
operator|.
name|JAXRSValidationInInterceptor
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
name|validation
operator|.
name|JAXRSValidationOutInterceptor
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
name|validation
operator|.
name|ValidationExceptionMapper
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
name|apache
operator|.
name|cxf
operator|.
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
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
name|BeforeClass
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

begin_class
specifier|public
class|class
name|JAXRSClientServerValidationTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookStoreWithValidation
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setResourceProvider
argument_list|(
name|BookStoreWithValidation
operator|.
name|class
argument_list|,
operator|new
name|SingletonResourceProvider
argument_list|(
operator|new
name|BookStoreWithValidation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setProvider
argument_list|(
operator|new
name|ValidationExceptionMapper
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setInInterceptors
argument_list|(
name|Arrays
operator|.
expr|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
operator|>
name|asList
argument_list|(
operator|new
name|JAXRSValidationInInterceptor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setOutInterceptors
argument_list|(
name|Arrays
operator|.
expr|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
operator|>
name|asList
argument_list|(
operator|new
name|JAXRSOutExceptionMapperInterceptor
argument_list|()
argument_list|,
operator|new
name|JAXRSValidationOutInterceptor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
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
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
comment|//keep out of process due to stack traces testing failures
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|delete
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatPatternValidationFails
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books/blabla"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNotNullValidationFails
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatSizeValidationFails
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatMinValidationFails
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|query
argument_list|(
literal|"page"
argument_list|,
literal|"0"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNoValidationConstraintsAreViolated
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|query
argument_list|(
literal|"page"
argument_list|,
literal|"2"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatNoValidationConstraintsAreViolatedWithDefaultValue
parameter_list|()
block|{
specifier|final
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatResponseValidationForOneBookFails
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|CREATED
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|=
name|createWebClient
argument_list|(
literal|"/bookstore/books/1234"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatResponseValidationForOneResponseBookFails
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|CREATED
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|=
name|createWebClient
argument_list|(
literal|"/bookstore/booksResponse/1234"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testThatResponseValidationForAllBooksFails
parameter_list|()
block|{
name|Response
name|r
init|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|post
argument_list|(
operator|new
name|Form
argument_list|()
operator|.
name|param
argument_list|(
literal|"id"
argument_list|,
literal|"1234"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|CREATED
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|=
name|createWebClient
argument_list|(
literal|"/bookstore/books"
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|WebClient
name|createWebClient
parameter_list|(
specifier|final
name|String
name|url
parameter_list|)
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
name|url
argument_list|)
operator|.
name|accept
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|wc
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
literal|10000000L
argument_list|)
expr_stmt|;
return|return
name|wc
return|;
block|}
block|}
end_class

end_unit

