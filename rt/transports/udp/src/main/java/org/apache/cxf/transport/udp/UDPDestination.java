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
name|udp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MulticastSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|Bus
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|LoadingByteArrayOutputStream
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|AbstractDestination
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
name|Conduit
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
name|workqueue
operator|.
name|AutomaticWorkQueue
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|buffer
operator|.
name|IoBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|service
operator|.
name|IoHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|AttributeKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IdleStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|handler
operator|.
name|stream
operator|.
name|StreamIoHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|DatagramSessionConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioDatagramAcceptor
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|UDPDestination
extends|extends
name|AbstractDestination
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
name|UDPDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AttributeKey
name|KEY_IN
init|=
operator|new
name|AttributeKey
argument_list|(
name|StreamIoHandler
operator|.
name|class
argument_list|,
literal|"in"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AttributeKey
name|KEY_OUT
init|=
operator|new
name|AttributeKey
argument_list|(
name|StreamIoHandler
operator|.
name|class
argument_list|,
literal|"out"
argument_list|)
decl_stmt|;
name|NioDatagramAcceptor
name|acceptor
decl_stmt|;
name|AutomaticWorkQueue
name|queue
decl_stmt|;
specifier|volatile
name|MulticastSocket
name|mcast
decl_stmt|;
specifier|public
name|UDPDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointReferenceType
name|ref
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|super
argument_list|(
name|b
argument_list|,
name|ref
argument_list|,
name|ei
argument_list|)
expr_stmt|;
block|}
class|class
name|MCastListener
implements|implements
name|Runnable
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
name|mcast
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|byte
name|bytes
index|[]
init|=
operator|new
name|byte
index|[
literal|64
operator|*
literal|1024
index|]
decl_stmt|;
specifier|final
name|DatagramPacket
name|p
init|=
operator|new
name|DatagramPacket
argument_list|(
name|bytes
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
decl_stmt|;
name|mcast
operator|.
name|receive
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|LoadingByteArrayOutputStream
name|out
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
block|{
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
specifier|final
name|DatagramPacket
name|p2
init|=
operator|new
name|DatagramPacket
argument_list|(
name|getRawBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
name|this
operator|.
name|size
argument_list|()
argument_list|,
name|p
operator|.
name|getSocketAddress
argument_list|()
argument_list|)
decl_stmt|;
name|mcast
operator|.
name|send
argument_list|(
name|p2
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|UDPConnectionInfo
name|info
init|=
operator|new
name|UDPConnectionInfo
argument_list|(
literal|null
argument_list|,
name|out
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|p
operator|.
name|getLength
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|MessageImpl
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setDestination
argument_list|(
name|UDPDestination
operator|.
name|this
argument_list|)
expr_stmt|;
name|m
operator|.
name|setDestination
argument_list|(
name|UDPDestination
operator|.
name|this
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|info
operator|.
name|in
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|UDPConnectionInfo
operator|.
name|class
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|queue
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|getMessageObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|protected
name|Conduit
name|getInbuiltBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
if|if
condition|(
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|UDPConnectionInfo
name|info
init|=
name|inMessage
operator|.
name|get
argument_list|(
name|UDPConnectionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|AbstractBackChannelConduit
argument_list|()
block|{
specifier|public
name|void
name|prepare
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|info
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
specifier|protected
name|void
name|activate
parameter_list|()
block|{
name|WorkQueueManager
name|queuem
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|queue
operator|=
name|queuem
operator|.
name|getNamedWorkQueue
argument_list|(
literal|"udp-transport"
argument_list|)
expr_stmt|;
if|if
condition|(
name|queue
operator|==
literal|null
condition|)
block|{
name|queue
operator|=
name|queuem
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|this
operator|.
name|getAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|InetSocketAddress
name|isa
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|s
init|=
name|uri
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"//:"
argument_list|)
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|s
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|isa
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|isa
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isa
operator|.
name|getAddress
argument_list|()
operator|.
name|isMulticastAddress
argument_list|()
condition|)
block|{
comment|//ouch...
name|MulticastSocket
name|socket
init|=
operator|new
name|MulticastSocket
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|socket
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|socket
operator|.
name|setReceiveBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|socket
operator|.
name|setSendBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|socket
operator|.
name|setTimeToLive
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|socket
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
name|isa
operator|.
name|getPort
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|socket
operator|.
name|joinGroup
argument_list|(
name|isa
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|mcast
operator|=
name|socket
expr_stmt|;
name|queue
operator|.
name|execute
argument_list|(
operator|new
name|MCastListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|acceptor
operator|=
operator|new
name|NioDatagramAcceptor
argument_list|()
expr_stmt|;
name|acceptor
operator|.
name|setHandler
argument_list|(
operator|new
name|UDPIOHandler
argument_list|()
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|setDefaultLocalAddress
argument_list|(
name|isa
argument_list|)
expr_stmt|;
name|DatagramSessionConfig
name|dcfg
init|=
name|acceptor
operator|.
name|getSessionConfig
argument_list|()
decl_stmt|;
name|dcfg
operator|.
name|setReadBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|dcfg
operator|.
name|setSendBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|dcfg
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|bind
argument_list|()
expr_stmt|;
block|}
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|deactivate
parameter_list|()
block|{
if|if
condition|(
name|acceptor
operator|!=
literal|null
condition|)
block|{
name|acceptor
operator|.
name|unbind
argument_list|()
expr_stmt|;
name|acceptor
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
name|acceptor
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|mcast
operator|!=
literal|null
condition|)
block|{
name|mcast
operator|.
name|close
argument_list|()
expr_stmt|;
name|mcast
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|UDPConnectionInfo
block|{
specifier|final
name|IoSession
name|session
decl_stmt|;
specifier|final
name|OutputStream
name|out
decl_stmt|;
specifier|final
name|InputStream
name|in
decl_stmt|;
specifier|public
name|UDPConnectionInfo
parameter_list|(
name|IoSession
name|io
parameter_list|,
name|OutputStream
name|o
parameter_list|,
name|InputStream
name|i
parameter_list|)
block|{
name|session
operator|=
name|io
expr_stmt|;
name|out
operator|=
name|o
expr_stmt|;
name|in
operator|=
name|i
expr_stmt|;
block|}
block|}
class|class
name|UDPIOHandler
extends|extends
name|StreamIoHandler
implements|implements
name|IoHandler
block|{
annotation|@
name|Override
specifier|public
name|void
name|sessionOpened
parameter_list|(
name|IoSession
name|session
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"open"
argument_list|)
expr_stmt|;
comment|// Set timeouts
name|session
operator|.
name|getConfig
argument_list|()
operator|.
name|setWriteTimeout
argument_list|(
name|getWriteTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|getConfig
argument_list|()
operator|.
name|setIdleTime
argument_list|(
name|IdleStatus
operator|.
name|READER_IDLE
argument_list|,
name|getReadTimeout
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create streams
name|InputStream
name|in
init|=
operator|new
name|IoSessionInputStream
argument_list|()
decl_stmt|;
name|OutputStream
name|out
init|=
operator|new
name|IoSessionOutputStream
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|KEY_IN
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|KEY_OUT
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|processStreamIo
argument_list|(
name|session
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processStreamIo
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
block|{
specifier|final
name|MessageImpl
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setDestination
argument_list|(
name|UDPDestination
operator|.
name|this
argument_list|)
expr_stmt|;
name|m
operator|.
name|setDestination
argument_list|(
name|UDPDestination
operator|.
name|this
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setInMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|out
operator|=
operator|new
name|UDPDestinationOutputStream
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
name|UDPConnectionInfo
operator|.
name|class
argument_list|,
operator|new
name|UDPConnectionInfo
argument_list|(
name|session
argument_list|,
name|out
argument_list|,
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|getMessageObserver
argument_list|()
operator|.
name|onMessage
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|sessionClosed
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"close"
argument_list|)
expr_stmt|;
specifier|final
name|InputStream
name|in
init|=
operator|(
name|InputStream
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|KEY_IN
argument_list|)
decl_stmt|;
specifier|final
name|OutputStream
name|out
init|=
operator|(
name|OutputStream
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|KEY_OUT
argument_list|)
decl_stmt|;
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|buf
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"mr"
argument_list|)
expr_stmt|;
specifier|final
name|IoSessionInputStream
name|in
init|=
operator|(
name|IoSessionInputStream
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|KEY_IN
argument_list|)
decl_stmt|;
name|in
operator|.
name|setBuffer
argument_list|(
operator|(
name|IoBuffer
operator|)
name|buf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ex"
argument_list|)
expr_stmt|;
specifier|final
name|IoSessionInputStream
name|in
init|=
operator|(
name|IoSessionInputStream
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|KEY_IN
argument_list|)
decl_stmt|;
name|IOException
name|e
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|StreamIoException
condition|)
block|{
name|e
operator|=
operator|(
name|IOException
operator|)
name|cause
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cause
operator|instanceof
name|IOException
condition|)
block|{
name|e
operator|=
operator|(
name|IOException
operator|)
name|cause
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|!=
literal|null
operator|&&
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|throwException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|sessionIdle
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|IdleStatus
name|status
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"idle"
argument_list|)
expr_stmt|;
if|if
condition|(
name|status
operator|==
name|IdleStatus
operator|.
name|READER_IDLE
condition|)
block|{
throw|throw
operator|new
name|StreamIoException
argument_list|(
operator|new
name|SocketTimeoutException
argument_list|(
literal|"Read timeout"
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|StreamIoException
extends|extends
name|RuntimeException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3976736960742503222L
decl_stmt|;
specifier|public
name|StreamIoException
parameter_list|(
name|IOException
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
class|class
name|UDPDestinationOutputStream
extends|extends
name|OutputStream
block|{
specifier|final
name|OutputStream
name|out
decl_stmt|;
name|IoBuffer
name|buffer
init|=
name|IoBuffer
operator|.
name|allocate
argument_list|(
literal|64
operator|*
literal|1024
operator|-
literal|42
argument_list|)
decl_stmt|;
comment|//max size
name|boolean
name|closed
decl_stmt|;
specifier|public
name|UDPDestinationOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|buffer
operator|.
name|put
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|b
block|}
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|byte
name|b
index|[]
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
while|while
condition|(
name|len
operator|>
name|buffer
operator|.
name|remaining
argument_list|()
condition|)
block|{
name|int
name|nlen
init|=
name|buffer
operator|.
name|remaining
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|put
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|nlen
argument_list|)
expr_stmt|;
name|len
operator|-=
name|nlen
expr_stmt|;
name|off
operator|+=
name|nlen
expr_stmt|;
name|send
argument_list|()
expr_stmt|;
name|buffer
operator|=
name|IoBuffer
operator|.
name|allocate
argument_list|(
operator|(
literal|64
operator|*
literal|1024
operator|)
operator|-
literal|42
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|put
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|send
parameter_list|()
throws|throws
name|IOException
block|{
name|buffer
operator|.
name|flip
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buffer
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
name|buffer
operator|.
name|limit
argument_list|()
argument_list|)
expr_stmt|;
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
name|closed
condition|)
block|{
return|return;
block|}
name|closed
operator|=
literal|true
expr_stmt|;
name|send
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

