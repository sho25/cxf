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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|JAXRSClientServerWebSocketTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerWebSocket
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
literal|"enableWebSocket"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|AbstractResourceInfo
operator|.
name|clearAllMaps
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
operator|new
name|BookServerWebSocket
argument_list|(
name|properties
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
comment|// seems to cause PermGen issue to another systests/jaxrs later in some system. the code under analysis
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
specifier|public
name|void
name|testBookWithWebSocket
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/bookstore"
decl_stmt|;
name|WebSocketTestClient
name|wsclient
init|=
operator|new
name|WebSocketTestClient
argument_list|(
name|address
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
try|try
block|{
comment|// call the GET service
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"GET /bookstore/booknames/123"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"one book must be returned"
argument_list|,
name|wsclient
operator|.
name|await
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceivedBytes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|new
name|String
argument_list|(
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// call another GET service
name|wsclient
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"GET /bookstore/books/123"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"response expected"
argument_list|,
name|wsclient
operator|.
name|await
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|received
operator|=
name|wsclient
operator|.
name|getReceivedBytes
argument_list|()
expr_stmt|;
name|value
operator|=
operator|new
name|String
argument_list|(
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|value
operator|.
name|startsWith
argument_list|(
literal|"<?xml "
argument_list|)
operator|&&
name|value
operator|.
name|endsWith
argument_list|(
literal|"</Book>"
argument_list|)
argument_list|)
expr_stmt|;
comment|// call the GET service using POST
name|wsclient
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"POST /bookstore/booknames\r\n\r\n123"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"wrong method, no response expected"
argument_list|,
name|wsclient
operator|.
name|await
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
comment|// call the POST service
name|wsclient
operator|.
name|reset
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"POST /bookstore/booksplain\r\nContent-Type: text/plain\r\n\r\n123"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"response expected"
argument_list|,
name|wsclient
operator|.
name|await
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|received
operator|=
name|wsclient
operator|.
name|getReceivedBytes
argument_list|()
expr_stmt|;
name|value
operator|=
operator|new
name|String
argument_list|(
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// call the GET service returning a continous stream output
name|wsclient
operator|.
name|reset
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"GET /bookstore/bookbought"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"wrong method, no response expected"
argument_list|,
name|wsclient
operator|.
name|await
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|received
operator|=
name|wsclient
operator|.
name|getReceivedBytes
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|new
name|String
argument_list|(
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|)
operator|.
name|startsWith
argument_list|(
literal|"Today:"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|r
init|=
literal|2
init|,
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|6
condition|;
name|r
operator|*=
literal|2
operator|,
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|r
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
operator|new
name|String
argument_list|(
name|received
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

