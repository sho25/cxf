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
name|jaxws
package|;
end_package

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
name|BindingProvider
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
name|Endpoint
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
name|endpoint
operator|.
name|Client
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
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|ext
operator|.
name|logging
operator|.
name|LoggingOutInterceptor
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
name|frontend
operator|.
name|ClientProxy
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
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|ProxyServerType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|HttpProxyServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|littleshoot
operator|.
name|proxy
operator|.
name|impl
operator|.
name|DefaultHttpProxyServer
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
name|assertEquals
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
name|assertNotNull
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

begin_class
specifier|public
class|class
name|CXF6655Test
extends|extends
name|AbstractClientServerTestBase
block|{
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
specifier|static
specifier|final
name|int
name|PROXY_PORT
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|allocatePort
argument_list|(
name|CXF6655Test
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
specifier|static
name|HttpProxyServer
name|proxy
decl_stmt|;
specifier|public
specifier|static
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|HelloImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/hello"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
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
name|AfterClass
specifier|public
specifier|static
name|void
name|stopProxy
parameter_list|()
block|{
name|proxy
operator|.
name|stop
argument_list|()
expr_stmt|;
name|proxy
operator|=
literal|null
expr_stmt|;
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
name|proxy
operator|=
name|DefaultHttpProxyServer
operator|.
name|bootstrap
argument_list|()
operator|.
name|withPort
argument_list|(
name|PROXY_PORT
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/jaxws/"
argument_list|,
literal|"HelloService"
argument_list|)
decl_stmt|;
name|HelloService
name|service
init|=
operator|new
name|HelloService
argument_list|(
literal|null
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Hello
name|hello
init|=
name|service
operator|.
name|getHelloPort
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|hello
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/hello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getSayHi"
argument_list|,
name|hello
operator|.
name|sayHi
argument_list|(
literal|"SayHi"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConnectionWithProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/systest/jaxws/"
argument_list|,
literal|"HelloService"
argument_list|)
decl_stmt|;
name|HelloService
name|service
init|=
operator|new
name|HelloService
argument_list|(
literal|null
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|Hello
name|hello
init|=
name|service
operator|.
name|getHelloPort
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|hello
argument_list|)
decl_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|HTTPClientPolicy
name|httpClientPolicy
init|=
operator|new
name|HTTPClientPolicy
argument_list|()
decl_stmt|;
name|httpClientPolicy
operator|.
name|setAllowChunking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|httpClientPolicy
operator|.
name|setReceiveTimeout
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|httpClientPolicy
operator|.
name|setProxyServerType
argument_list|(
name|ProxyServerType
operator|.
name|HTTP
argument_list|)
expr_stmt|;
name|httpClientPolicy
operator|.
name|setProxyServer
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|httpClientPolicy
operator|.
name|setProxyServerPort
argument_list|(
name|PROXY_PORT
argument_list|)
expr_stmt|;
name|http
operator|.
name|setClient
argument_list|(
name|httpClientPolicy
argument_list|)
expr_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|hello
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/hello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getSayHi"
argument_list|,
name|hello
operator|.
name|sayHi
argument_list|(
literal|"SayHi"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

