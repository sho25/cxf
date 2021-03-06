begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
operator|.
name|server
package|;
end_package

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

begin_class
specifier|public
class|class
name|HelloWorldImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSayHi
parameter_list|()
block|{
name|HelloWorldImpl
name|hwi
init|=
operator|new
name|HelloWorldImpl
argument_list|()
decl_stmt|;
name|String
name|response
init|=
name|hwi
operator|.
name|sayHi
argument_list|(
literal|"Bob"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SayHi isn't returning expected string"
argument_list|,
literal|"Hello Bob"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSayHiToUser
parameter_list|()
block|{
name|HelloWorldImpl
name|hwi
init|=
operator|new
name|HelloWorldImpl
argument_list|()
decl_stmt|;
name|User
name|sam
init|=
operator|new
name|UserImpl
argument_list|(
literal|"Sam"
argument_list|)
decl_stmt|;
name|String
name|response
init|=
name|hwi
operator|.
name|sayHiToUser
argument_list|(
name|sam
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SayHiToUser isn't returning expected string"
argument_list|,
literal|"Hello Sam"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetUsers
parameter_list|()
block|{
name|HelloWorldImpl
name|hwi
init|=
operator|new
name|HelloWorldImpl
argument_list|()
decl_stmt|;
name|User
name|mike
init|=
operator|new
name|UserImpl
argument_list|(
literal|"Mike"
argument_list|)
decl_stmt|;
name|hwi
operator|.
name|sayHiToUser
argument_list|(
name|mike
argument_list|)
expr_stmt|;
name|User
name|george
init|=
operator|new
name|UserImpl
argument_list|(
literal|"George"
argument_list|)
decl_stmt|;
name|hwi
operator|.
name|sayHiToUser
argument_list|(
name|george
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|userMap
init|=
name|hwi
operator|.
name|getUsers
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getUsers() not returning expected number of users"
argument_list|,
name|userMap
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected user Mike not found"
argument_list|,
literal|"Mike"
argument_list|,
name|userMap
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected user George not found"
argument_list|,
literal|"George"
argument_list|,
name|userMap
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

