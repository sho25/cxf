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
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
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
name|binding
operator|.
name|corba
operator|.
name|utils
operator|.
name|CorbaBindingHelper
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
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|TIMEOUT
import|;
end_import

begin_comment
comment|/**  * This test uses Jacorb implementation, but cleans after itself.  */
end_comment

begin_class
specifier|public
class|class
name|CorbaTimeoutTest
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
literal|"GreeterTimeoutCORBAService"
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
name|cleanDefaultORB
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
name|launchServer
argument_list|(
name|ServerTimeout
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
name|cleanDefaultORB
argument_list|()
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./HelloWorldTimeout.ref"
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
name|testTimeout
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.omg.CORBA.ORBClass"
argument_list|,
literal|"org.jacorb.orb.ORB"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.omg.CORBA.ORBSingletonClass"
argument_list|,
literal|"org.jacorb.orb.ORBSingleton"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"jacorb.connection.client.pending_reply_timeout"
argument_list|,
literal|"1000"
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
literal|"/wsdl_systest/hello_world_corba_timeout.wsdl"
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
name|getPort
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/hello_world_corba"
argument_list|,
literal|"GreeterTimeoutCORBAPort"
argument_list|)
argument_list|,
name|GreeterCORBAService
operator|.
name|GreeterProxy
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|port
operator|.
name|greetMe
argument_list|(
literal|"Betty"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should throw org.omg.CORBA.TIMEOUT exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|TIMEOUT
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
literal|"org.omg.CORBA.ORBClass"
argument_list|)
expr_stmt|;
name|System
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
literal|"org.omg.CORBA.ORBSingletonClass"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|cleanDefaultORB
parameter_list|()
throws|throws
name|NoSuchFieldException
throws|,
name|IllegalAccessException
block|{
name|Field
name|f
init|=
name|CorbaBindingHelper
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"defaultORB"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|f
operator|.
name|set
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

