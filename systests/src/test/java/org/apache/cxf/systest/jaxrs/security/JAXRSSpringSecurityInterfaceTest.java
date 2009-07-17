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
package|;
end_package

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
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
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
name|systest
operator|.
name|jaxrs
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
name|systest
operator|.
name|jaxrs
operator|.
name|BookNotFoundFault
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSSpringSecurityInterfaceTest
extends|extends
name|AbstractSpringSecurityTest
block|{
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
name|BookServerSecuritySpringInterface
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailedAuthentication
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"ba"
argument_list|,
literal|401
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookUserAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookUser
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks/123/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"baddy"
argument_list|,
literal|"baddyspassword"
argument_list|,
literal|403
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|403
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookSubresource
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/bookstorestorage/subresource"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|403
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebClientAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks"
decl_stmt|;
name|doGetBookWebClient
argument_list|(
name|address
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProxyClientAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstorestorage"
decl_stmt|;
name|doGetBookProxyClient
argument_list|(
name|address
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebClientUserUnauthorized
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks"
decl_stmt|;
name|doGetBookWebClient
argument_list|(
name|address
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|403
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWebClientUserAuthorized
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:9080/bookstorestorage/thosebooks/123/123"
decl_stmt|;
name|doGetBookWebClient
argument_list|(
name|address
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doGetBookWebClient
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|expectedStatus
parameter_list|)
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Response
name|r
init|=
name|wc
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedStatus
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|WebClient
name|wc2
init|=
name|WebClient
operator|.
name|fromClient
argument_list|(
name|wc
argument_list|)
decl_stmt|;
name|r
operator|=
name|wc2
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedStatus
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doGetBookProxyClient
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|expectedStatus
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
name|SecureBookInterface
name|books
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|SecureBookInterface
operator|.
name|class
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Book
name|b
init|=
name|books
operator|.
name|getThatBook
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|b
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|WebClient
operator|.
name|client
argument_list|(
name|books
argument_list|)
operator|.
name|getResponse
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedStatus
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
name|testGetBookSubresourceAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:9080/bookstorestorage/securebook/self"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|200
argument_list|)
expr_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|403
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

