begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|sample
operator|.
name|rs
operator|.
name|service
package|;
end_package

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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|embedded
operator|.
name|LocalServerPort
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
operator|.
name|WebEnvironment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
name|SampleRestApplication
operator|.
name|class
argument_list|,
name|webEnvironment
operator|=
name|WebEnvironment
operator|.
name|RANDOM_PORT
argument_list|)
specifier|public
class|class
name|SampleRestApplicationTest
block|{
annotation|@
name|LocalServerPort
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testHelloRequest
parameter_list|()
throws|throws
name|Exception
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
name|port
operator|+
literal|"/services/helloservice"
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
comment|// HelloServiceImpl1
name|wc
operator|.
name|path
argument_list|(
literal|"sayHello"
argument_list|)
operator|.
name|path
argument_list|(
literal|"ApacheCxfUser"
argument_list|)
expr_stmt|;
name|String
name|greeting
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello ApacheCxfUser, Welcome to CXF RS Spring Boot World!!!"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
comment|// Reverse to the starting URI
name|wc
operator|.
name|back
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// HelloServiceImpl2
name|wc
operator|.
name|path
argument_list|(
literal|"sayHello2"
argument_list|)
operator|.
name|path
argument_list|(
literal|"ApacheCxfUser"
argument_list|)
expr_stmt|;
name|greeting
operator|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello2 ApacheCxfUser, Welcome to CXF RS Spring Boot World!!!"
argument_list|,
name|greeting
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

