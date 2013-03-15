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
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|JAXRSJaasSecurityTest
extends|extends
name|AbstractSpringSecurityTest
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|BookServerJaasSecurity
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
name|String
name|jaasConfig
init|=
name|JAXRSJaasSecurityTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/org/apache/cxf/systest/jaxrs/security/jaas.cfg"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerJaasSecurity
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"java.security.auth.login.config"
argument_list|,
name|jaasConfig
argument_list|)
argument_list|,
operator|new
name|String
index|[]
block|{}
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaasInterceptorAuthenticationFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas/bookstorestorage/thosebooks/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar1"
argument_list|,
literal|401
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookUserAdminJaasInterceptor
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas/bookstorestorage/thosebooks/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|403
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
name|testJaasFilterAuthenticationFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas2/bookstorestorage/thosebooks/123"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|AuthorizationPolicy
name|pol
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|pol
operator|.
name|setUserName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|pol
operator|.
name|setPassword
argument_list|(
literal|"bar1"
argument_list|)
expr_stmt|;
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
name|setAuthorization
argument_list|(
name|pol
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
comment|//wc.header(HttpHeaders.AUTHORIZATION,
comment|//          "Basic " + base64Encode("foo" + ":" + "bar1"));
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
literal|401
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|wwwAuthHeader
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
name|wwwAuthHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Basic"
argument_list|,
name|wwwAuthHeader
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaasFilterWebClientAuthorizationPolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas2/bookstorestorage/thosebooks/123"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|AuthorizationPolicy
name|pol
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|pol
operator|.
name|setUserName
argument_list|(
literal|"bob"
argument_list|)
expr_stmt|;
name|pol
operator|.
name|setPassword
argument_list|(
literal|"bobspassword"
argument_list|)
expr_stmt|;
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
name|setAuthorization
argument_list|(
name|pol
argument_list|)
expr_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaasFilterWebClientAuthorizationPolicy2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas2/bookstorestorage/thosebooks/123"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|//WebClient.getConfig(wc).getOutInterceptors().add(new LoggingOutInterceptor());
name|wc
operator|.
name|accept
argument_list|(
literal|"application/xml"
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
name|wc
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaasFilterProxyAuthorizationPolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas2"
decl_stmt|;
name|SecureBookStoreNoAnnotations
name|proxy
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|,
name|SecureBookStoreNoAnnotations
operator|.
name|class
argument_list|,
literal|"bob"
argument_list|,
literal|"bobspassword"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|proxy
operator|.
name|getThatBook
argument_list|(
literal|123L
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaasFilterAuthenticationFailureWithRedirection
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas2/bookstorestorage/thosebooks/123"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|endpointAddress
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/xml,text/html"
argument_list|)
expr_stmt|;
name|wc
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|,
literal|"Basic "
operator|+
name|base64Encode
argument_list|(
literal|"foo"
operator|+
literal|":"
operator|+
literal|"bar1"
argument_list|)
argument_list|)
expr_stmt|;
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
literal|307
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|locationHeader
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
name|LOCATION
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|locationHeader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/login.jsp"
argument_list|,
name|locationHeader
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookUserAdminJaasFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|endpointAddress
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/service/jaas2/bookstorestorage/thosebooks/123"
decl_stmt|;
name|getBook
argument_list|(
name|endpointAddress
argument_list|,
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|403
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
block|}
end_class

end_unit

