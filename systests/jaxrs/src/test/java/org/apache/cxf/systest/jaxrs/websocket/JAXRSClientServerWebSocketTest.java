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
name|List
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

begin_class
specifier|public
class|class
name|JAXRSClientServerWebSocketTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|private
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
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|createStaticBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
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
name|getPort
argument_list|()
operator|+
literal|"/websocket/web/bookstore"
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
literal|"GET /websocket/web/bookstore/booknames"
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
name|Object
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
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
name|Response
name|resp
init|=
operator|new
name|Response
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
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|resp
operator|.
name|getTextEntity
argument_list|()
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
literal|"GET /websocket/web/bookstore/books/123"
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
name|getReceived
argument_list|()
expr_stmt|;
name|resp
operator|=
operator|new
name|Response
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
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/xml"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|resp
operator|.
name|getTextEntity
argument_list|()
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
literal|"POST /websocket/web/bookstore/booksplain\r\nContent-Type: text/plain\r\n\r\n123"
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
name|getReceived
argument_list|()
expr_stmt|;
name|resp
operator|=
operator|new
name|Response
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
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|resp
operator|.
name|getTextEntity
argument_list|()
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
literal|"GET /websocket/web/bookstore/bookbought"
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
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|received
operator|=
name|wsclient
operator|.
name|getReceived
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
name|resp
operator|=
operator|new
name|Response
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
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"application/octet-stream"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|resp
operator|.
name|getTextEntity
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|value
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
comment|// subsequent data should not carry the headers nor the status.
name|resp
operator|=
operator|new
name|Response
argument_list|(
name|received
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|r
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|resp
operator|.
name|getTextEntity
argument_list|()
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
annotation|@
name|Test
specifier|public
name|void
name|testBookWithWebSocketServletStream
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"ws://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/websocket/web/bookstore"
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
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"GET /websocket/web/bookstore/booknames/servletstream"
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
name|Object
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
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
name|Response
name|resp
init|=
operator|new
name|Response
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
literal|200
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"text/plain"
argument_list|,
name|resp
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|resp
operator|.
name|getTextEntity
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testWrongMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"ws://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/websocket/web/bookstore"
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
comment|// call the GET service using POST
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"POST /websocket/web/bookstore/booknames"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"error response expected"
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
name|Object
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
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
name|Response
name|resp
init|=
operator|new
name|Response
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
literal|405
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Test
specifier|public
name|void
name|testPathRestriction
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"ws://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/websocket/web/bookstore"
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
comment|// call the GET service over the different path
name|wsclient
operator|.
name|sendMessage
argument_list|(
literal|"GET /websocket/bookstore2"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"error response expected"
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
name|Object
argument_list|>
name|received
init|=
name|wsclient
operator|.
name|getReceived
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
name|Response
name|resp
init|=
operator|new
name|Response
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
literal|400
argument_list|,
name|resp
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
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
comment|//TODO this is a temporary way to verify the response; we should come up with something better.
specifier|private
specifier|static
class|class
name|Response
block|{
specifier|private
name|Object
name|data
decl_stmt|;
specifier|private
name|int
name|pos
decl_stmt|;
specifier|private
name|int
name|statusCode
decl_stmt|;
specifier|private
name|String
name|contentType
decl_stmt|;
specifier|private
name|Object
name|entity
decl_stmt|;
specifier|public
name|Response
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|String
name|line
init|=
name|readLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|statusCode
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|line
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|del
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|String
name|h
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|del
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|v
init|=
name|line
operator|.
name|substring
argument_list|(
name|del
operator|+
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"Content-Type"
operator|.
name|equalsIgnoreCase
argument_list|(
name|h
argument_list|)
condition|)
block|{
name|contentType
operator|=
name|v
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|data
operator|instanceof
name|String
condition|)
block|{
name|entity
operator|=
operator|(
operator|(
name|String
operator|)
name|data
operator|)
operator|.
name|substring
argument_list|(
name|pos
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|data
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|entity
operator|=
operator|new
name|byte
index|[
operator|(
operator|(
name|byte
index|[]
operator|)
name|data
operator|)
operator|.
name|length
operator|-
name|pos
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|data
argument_list|,
name|pos
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|entity
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|byte
index|[]
operator|)
name|entity
operator|)
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
name|contentType
return|;
block|}
specifier|public
name|Object
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|String
name|getTextEntity
parameter_list|()
block|{
return|return
name|gettext
argument_list|(
name|entity
argument_list|)
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Status: "
argument_list|)
operator|.
name|append
argument_list|(
name|statusCode
argument_list|)
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Type: "
argument_list|)
operator|.
name|append
argument_list|(
name|contentType
argument_list|)
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Entity: "
argument_list|)
operator|.
name|append
argument_list|(
name|gettext
argument_list|(
name|entity
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\r\n"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|String
name|readLine
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|pos
operator|<
name|length
argument_list|(
name|data
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|getchar
argument_list|(
name|data
argument_list|,
name|pos
operator|++
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\n'
condition|)
block|{
break|break;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\r'
condition|)
block|{
continue|continue;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|int
name|length
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|char
index|[]
condition|?
operator|(
operator|(
name|String
operator|)
name|o
operator|)
operator|.
name|length
argument_list|()
else|:
operator|(
name|o
operator|instanceof
name|byte
index|[]
condition|?
operator|(
operator|(
name|byte
index|[]
operator|)
name|o
operator|)
operator|.
name|length
else|:
literal|0
operator|)
return|;
block|}
specifier|private
name|int
name|getchar
parameter_list|(
name|Object
name|o
parameter_list|,
name|int
name|p
parameter_list|)
block|{
return|return
literal|0xff
operator|&
operator|(
name|o
operator|instanceof
name|String
condition|?
operator|(
operator|(
name|String
operator|)
name|o
operator|)
operator|.
name|charAt
argument_list|(
name|p
argument_list|)
else|:
operator|(
name|o
operator|instanceof
name|byte
index|[]
condition|?
operator|(
operator|(
name|byte
index|[]
operator|)
name|o
operator|)
index|[
name|p
index|]
else|:
operator|-
literal|1
operator|)
operator|)
return|;
block|}
specifier|private
name|String
name|gettext
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|String
condition|?
operator|(
name|String
operator|)
name|o
else|:
operator|(
name|o
operator|instanceof
name|byte
index|[]
condition|?
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|o
argument_list|)
else|:
literal|null
operator|)
return|;
block|}
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
block|}
end_class

end_unit

