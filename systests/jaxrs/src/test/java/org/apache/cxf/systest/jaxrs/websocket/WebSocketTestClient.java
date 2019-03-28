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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|websocket
operator|.
name|WebSocketConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|DefaultAsyncHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocketByteListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocketTextListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocketUpgradeHandler
import|;
end_import

begin_comment
comment|/**  * Test client to do websocket calls.  * @see JAXRSClientServerWebSocketTest  *  * we may put this in test-tools so that other systests can use this code.  * for now keep it here to experiment jaxrs websocket scenarios.  */
end_comment

begin_class
class|class
name|WebSocketTestClient
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WebSocketTestClient
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|received
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|fragments
decl_stmt|;
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
specifier|private
name|AsyncHttpClient
name|client
decl_stmt|;
specifier|private
name|WebSocket
name|websocket
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
name|WebSocketTestClient
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|received
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|fragments
operator|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
operator|new
name|DefaultAsyncHttpClient
argument_list|()
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|IOException
block|{
name|websocket
operator|=
name|client
operator|.
name|prepareGet
argument_list|(
name|url
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|WebSocketUpgradeHandler
operator|.
name|Builder
argument_list|()
operator|.
name|addWebSocketListener
argument_list|(
operator|new
name|WsSocketListener
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|websocket
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"websocket is null"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|sendTextMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|websocket
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|sendMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
name|websocket
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|await
parameter_list|(
name|int
name|secs
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|latch
operator|.
name|await
argument_list|(
name|secs
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
return|;
block|}
specifier|public
name|void
name|reset
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|received
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getReceived
parameter_list|()
block|{
return|return
name|received
return|;
block|}
specifier|public
name|List
argument_list|<
name|Response
argument_list|>
name|getReceivedResponses
parameter_list|()
block|{
name|Object
index|[]
name|objs
init|=
name|received
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Response
argument_list|>
name|responses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|objs
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|objs
control|)
block|{
name|responses
operator|.
name|add
argument_list|(
operator|new
name|Response
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|responses
return|;
block|}
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|websocket
operator|!=
literal|null
condition|)
block|{
name|websocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
class|class
name|WsSocketListener
implements|implements
name|WebSocketTextListener
implements|,
name|WebSocketByteListener
block|{
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|ws
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] opened"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|ws
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] closed"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] error: "
operator|+
name|t
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
name|received
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] received bytes --> "
operator|+
name|makeString
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|onFragment
parameter_list|(
name|byte
index|[]
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] received fragment bytes (last?"
operator|+
name|last
operator|+
literal|") --> "
operator|+
operator|new
name|String
argument_list|(
name|fragment
argument_list|)
argument_list|)
expr_stmt|;
name|processFragments
argument_list|(
name|fragment
argument_list|,
name|last
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|received
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] received --> "
operator|+
name|message
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|onFragment
parameter_list|(
name|String
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"[ws] received fragment (last?"
operator|+
name|last
operator|+
literal|") --> "
operator|+
name|fragment
argument_list|)
expr_stmt|;
name|processFragments
argument_list|(
name|fragment
argument_list|,
name|last
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processFragments
parameter_list|(
name|Object
name|f
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{
synchronized|synchronized
init|(
name|fragments
init|)
block|{
name|fragments
operator|.
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
if|if
condition|(
name|last
condition|)
block|{
if|if
condition|(
name|f
operator|instanceof
name|String
condition|)
block|{
comment|// string
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|fragments
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|o
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|received
operator|.
name|add
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// byte[]
name|ByteArrayOutputStream
name|bao
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|fragments
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|o
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|bao
operator|.
name|write
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|o
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|byte
index|[]
operator|)
name|o
operator|)
operator|.
name|length
argument_list|)
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|received
operator|.
name|add
argument_list|(
name|bao
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
specifier|static
name|String
name|makeString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|data
operator|==
literal|null
condition|?
literal|null
else|:
name|makeString
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|data
operator|.
name|length
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|StringBuilder
name|makeString
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|length
operator|>
literal|256
condition|)
block|{
return|return
name|makeString
argument_list|(
name|data
argument_list|,
name|offset
argument_list|,
literal|256
argument_list|)
operator|.
name|append
argument_list|(
literal|"..."
argument_list|)
return|;
block|}
name|StringBuilder
name|xbuf
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"\nHEX: "
argument_list|)
decl_stmt|;
name|StringBuilder
name|cbuf
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"\nASC: "
argument_list|)
decl_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|data
control|)
block|{
name|writeHex
argument_list|(
name|xbuf
argument_list|,
literal|0xff
operator|&
name|b
argument_list|)
expr_stmt|;
name|writePrintable
argument_list|(
name|cbuf
argument_list|,
literal|0xff
operator|&
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|xbuf
operator|.
name|append
argument_list|(
name|cbuf
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|writeHex
parameter_list|(
name|StringBuilder
name|buf
parameter_list|,
name|int
name|b
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
literal|0x100
operator||
operator|(
literal|0xff
operator|&
name|b
operator|)
argument_list|)
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|writePrintable
parameter_list|(
name|StringBuilder
name|buf
parameter_list|,
name|int
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|==
literal|0x0d
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\\r"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b
operator|==
literal|0x0a
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\\n"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b
operator|==
literal|0x09
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\\t"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
literal|0x80
operator|&
name|b
operator|)
operator|!=
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|b
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
comment|//TODO this is a temporary way to verify the response; we should come up with something better.
specifier|public
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
name|String
name|id
decl_stmt|;
specifier|private
name|Object
name|entity
decl_stmt|;
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
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
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
name|first
operator|&&
name|isStatusCode
argument_list|(
name|line
argument_list|)
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
continue|continue;
block|}
name|first
operator|=
literal|false
expr_stmt|;
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
elseif|else
if|if
condition|(
name|WebSocketConstants
operator|.
name|DEFAULT_RESPONSE_ID_KEY
operator|.
name|equals
argument_list|(
name|h
argument_list|)
condition|)
block|{
name|id
operator|=
name|v
expr_stmt|;
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
name|data
argument_list|,
name|pos
argument_list|,
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
specifier|private
specifier|static
name|boolean
name|isStatusCode
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|char
name|c
init|=
name|line
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
literal|'0'
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|'9'
return|;
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
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|64
argument_list|)
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
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
return|return
operator|(
operator|(
name|String
operator|)
name|o
operator|)
operator|.
name|length
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|char
index|[]
condition|)
block|{
return|return
operator|(
operator|(
name|char
index|[]
operator|)
name|o
operator|)
operator|.
name|length
return|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|byte
index|[]
condition|)
block|{
return|return
operator|(
operator|(
name|byte
index|[]
operator|)
name|o
operator|)
operator|.
name|length
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
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
block|}
end_class

end_unit

