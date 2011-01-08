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
name|JAXRSSimpleSecurityTest
extends|extends
name|AbstractSpringSecurityTest
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerSimpleSecurity
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
name|BookServerSimpleSecurity
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookUserAdminWithFaultInterceptor
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
literal|"/security1/bookstorestorage/thosebooks/123"
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
name|testGetBookUserAdminWithFilter
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
literal|"/security2/bookstorestorage/thosebooks/123"
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
name|testGetBookUserAdminWithAnnotationsInterceptor
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
literal|"/security3/bookstorestorage/thebook/123"
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
name|testGetBookUserAdminWithAnnotationsInterface
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
literal|"/security5/bookstorestorage/thosebooks"
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
name|testGetBookUserAdminWithAnnotationsFilter
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
literal|"/security4/bookstorestorage/thebook/123"
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

