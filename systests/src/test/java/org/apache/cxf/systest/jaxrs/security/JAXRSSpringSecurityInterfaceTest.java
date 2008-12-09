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
block|}
end_class

end_unit

