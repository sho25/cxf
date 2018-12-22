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
name|corba
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|hello_world_corba
operator|.
name|Greeter
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
name|hello_world_corba
operator|.
name|GreeterCORBAService
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
name|hello_world_corba
operator|.
name|PingMeFault
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
name|assertTrue
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
name|fail
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|CorbaTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|Server
operator|.
name|PERSIST_PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_corba"
argument_list|,
literal|"GreeterCORBAService"
argument_list|)
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
literal|"Server failed to launch"
argument_list|,
name|launchServer
argument_list|(
name|Server
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanupFile
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./HelloWorld.ref"
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientServer
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
literal|"com.sun.CORBA.POA.ORBServerId"
argument_list|)
expr_stmt|;
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
literal|"com.sun.CORBA.POA.ORBPersistentServerPort"
argument_list|)
expr_stmt|;
name|URL
name|wsdlUrl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest/hello_world_corba.wsdl"
argument_list|)
decl_stmt|;
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/corba/hello_world_client.xml"
argument_list|)
expr_stmt|;
name|GreeterCORBAService
name|gcs
init|=
operator|new
name|GreeterCORBAService
argument_list|(
name|wsdlUrl
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|gcs
operator|.
name|getGreeterCORBAPort
argument_list|()
decl_stmt|;
name|String
name|output
init|=
name|port
operator|.
name|greetMe
argument_list|(
literal|"Betty"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected returned string: "
operator|+
name|output
argument_list|,
literal|"Hello Betty"
operator|.
name|equals
argument_list|(
name|output
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testException
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
literal|"com.sun.CORBA.POA.ORBServerId"
argument_list|)
expr_stmt|;
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
literal|"com.sun.CORBA.POA.ORBPersistentServerPort"
argument_list|)
expr_stmt|;
name|URL
name|wsdlUrl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest/hello_world_corba.wsdl"
argument_list|)
decl_stmt|;
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/corba/hello_world_client.xml"
argument_list|)
expr_stmt|;
name|GreeterCORBAService
name|gcs
init|=
operator|new
name|GreeterCORBAService
argument_list|(
name|wsdlUrl
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|gcs
operator|.
name|getGreeterCORBAPort
argument_list|()
decl_stmt|;
try|try
block|{
name|port
operator|.
name|pingMe
argument_list|(
literal|"USER"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|pe
parameter_list|)
block|{
return|return;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return;
block|}
name|fail
argument_list|(
literal|"Didn't catch an exception"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

