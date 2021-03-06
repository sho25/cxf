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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
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
name|systest
operator|.
name|jaxrs
operator|.
name|jaxws
operator|.
name|HelloWorld
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
name|jaxws
operator|.
name|User
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
name|jaxws
operator|.
name|UserImpl
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
name|AbstractClientServerTestBase
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
name|JAXRSSoapRestBlueprintTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|BlueprintServer
operator|.
name|PORT
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must be 'in-process' to communicate with inner class in single JVM
comment|// and to spawn class SpringServer w/o using main() method
name|launchServer
argument_list|(
name|BlueprintServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|BlueprintServer
extends|extends
name|AbstractSpringServer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|allocatePortAsInt
argument_list|(
name|BlueprintServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|BlueprintServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_soap_blueprint"
argument_list|,
literal|"/bp"
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelloRest
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bp/services/hello-rest"
decl_stmt|;
name|HelloWorld
name|service
init|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|address
argument_list|,
name|HelloWorld
operator|.
name|class
argument_list|)
decl_stmt|;
name|useHelloService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHelloSoap
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://hello.com"
argument_list|,
literal|"HelloWorld"
argument_list|)
decl_stmt|;
specifier|final
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"http://hello.com"
argument_list|,
literal|"HelloWorldPort"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/bp/services/hello-soap"
decl_stmt|;
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|HelloWorld
name|hw
init|=
name|service
operator|.
name|getPort
argument_list|(
name|HelloWorld
operator|.
name|class
argument_list|)
decl_stmt|;
name|useHelloService
argument_list|(
name|hw
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|useHelloService
parameter_list|(
name|HelloWorld
name|service
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Hello Barry"
argument_list|,
name|service
operator|.
name|sayHi
argument_list|(
literal|"Barry"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Fred"
argument_list|,
name|service
operator|.
name|sayHiToUser
argument_list|(
operator|new
name|UserImpl
argument_list|(
literal|"Fred"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|User
argument_list|>
name|users
init|=
name|service
operator|.
name|getUsers
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|users
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|users
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|users
operator|=
name|service
operator|.
name|echoUsers
argument_list|(
name|users
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|users
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Fred"
argument_list|,
name|users
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|service
operator|.
name|clearUsers
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|service
operator|.
name|clearUsers
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

