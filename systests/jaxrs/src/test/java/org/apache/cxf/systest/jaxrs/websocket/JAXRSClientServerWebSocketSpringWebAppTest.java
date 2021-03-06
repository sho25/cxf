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
name|websocket
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|jaxrs
operator|.
name|model
operator|.
name|AbstractResourceInfo
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
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Handler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|DefaultHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|handler
operator|.
name|HandlerCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|webapp
operator|.
name|WebAppContext
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

begin_comment
comment|/**  * JAXRSClientServerWebSocket test with jaxrs:server using the jetty webapp server.  */
end_comment

begin_class
specifier|public
class|class
name|JAXRSClientServerWebSocketSpringWebAppTest
extends|extends
name|AbstractJAXRSClientServerWebSocketTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerWebSocket
operator|.
name|PORT_WAR
decl_stmt|;
specifier|private
specifier|static
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
name|server
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
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|startServers
argument_list|(
name|PORT
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|startServers
parameter_list|(
name|String
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|Server
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
name|WebAppContext
name|webappcontext
init|=
operator|new
name|WebAppContext
argument_list|()
decl_stmt|;
name|String
name|contextPath
init|=
literal|null
decl_stmt|;
try|try
block|{
name|contextPath
operator|=
name|JAXRSClientServerWebSocketSpringWebAppTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/jaxrs_websocket"
argument_list|)
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|webappcontext
operator|.
name|setContextPath
argument_list|(
literal|"/webapp"
argument_list|)
expr_stmt|;
name|webappcontext
operator|.
name|setWar
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|HandlerCollection
name|handlers
init|=
operator|new
name|HandlerCollection
argument_list|()
decl_stmt|;
name|handlers
operator|.
name|setHandlers
argument_list|(
operator|new
name|Handler
index|[]
block|{
name|webappcontext
block|,
operator|new
name|DefaultHandler
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|server
operator|.
name|setHandler
argument_list|(
name|handlers
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopServers
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookHTTP
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
name|getContext
argument_list|()
operator|+
literal|"/http/web/bookstore/books/1"
decl_stmt|;
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
decl_stmt|;
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
literal|1L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getPort
parameter_list|()
block|{
return|return
name|PORT
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|getContext
parameter_list|()
block|{
return|return
literal|"/webapp"
return|;
block|}
block|}
end_class

end_unit

