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
name|transport
operator|.
name|websocket
operator|.
name|jetty
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|http_jetty
operator|.
name|JettyHTTPDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|Before
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JettyWebSocketManagerTest
extends|extends
name|Assert
block|{
specifier|private
name|IMocksControl
name|control
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|control
operator|=
name|EasyMock
operator|.
name|createNiceControl
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceUsingDestination
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyWebSocketManager
name|jwsm
init|=
operator|new
name|JettyWebSocketManager
argument_list|()
decl_stmt|;
name|JettyWebSocketHandler
name|handler
init|=
name|control
operator|.
name|createMock
argument_list|(
name|JettyWebSocketHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|JettyHTTPDestination
name|dest
init|=
name|control
operator|.
name|createMock
argument_list|(
name|JettyHTTPDestination
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpServletRequest
name|request
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|response
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|dest
operator|.
name|invoke
argument_list|(
name|EasyMock
operator|.
name|isNull
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|isNull
argument_list|(
name|ServletContext
operator|.
name|class
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|request
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|jwsm
operator|.
name|init
argument_list|(
name|handler
argument_list|,
name|dest
argument_list|)
expr_stmt|;
name|jwsm
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testServiceUsingServlet
parameter_list|()
throws|throws
name|Exception
block|{
name|JettyWebSocketManager
name|jwsm
init|=
operator|new
name|JettyWebSocketManager
argument_list|()
decl_stmt|;
name|HttpServletRequest
name|request
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpServletResponse
name|response
init|=
name|control
operator|.
name|createMock
argument_list|(
name|HttpServletResponse
operator|.
name|class
argument_list|)
decl_stmt|;
name|CXFNonSpringJettyWebSocketServlet
name|srvlt
init|=
name|control
operator|.
name|createMock
argument_list|(
name|CXFNonSpringJettyWebSocketServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServletConfig
name|sc
init|=
name|control
operator|.
name|createMock
argument_list|(
name|ServletConfig
operator|.
name|class
argument_list|)
decl_stmt|;
name|srvlt
operator|.
name|serviceInternal
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|request
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|eq
argument_list|(
name|response
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|jwsm
operator|.
name|init
argument_list|(
name|srvlt
argument_list|,
name|sc
argument_list|)
expr_stmt|;
name|jwsm
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

