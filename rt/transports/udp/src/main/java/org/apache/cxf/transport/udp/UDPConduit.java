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
name|DatagramSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
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
name|InterfaceAddress
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
name|NetworkInterface
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Queue
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
name|ArrayBlockingQueue
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
name|ConcurrentHashMap
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
name|transport
operator|.
name|AbstractConduit
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
name|WorkQueue
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
name|future
operator|.
name|ConnectFuture
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
name|IoHandlerAdapter
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
name|NioDatagramConnector
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|UDPConduit
extends|extends
name|AbstractConduit
block|{
comment|/**      * For broadcast/multicast, the specific network interface to use.   This can either be      * a specific  java.net.NetworkInterface or a string for NetworkInterface.getByName(String name)      */
specifier|public
specifier|static
specifier|final
name|String
name|NETWORK_INTERFACE
init|=
name|UDPConduit
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".NETWORK_INTERFACE"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CXF_MESSAGE_ATTR
init|=
literal|"CXFMessage"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MULTI_RESPONSE_TIMEOUT
init|=
literal|"udp.multi.response.timeout"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HOST_PORT
init|=
name|UDPConduit
operator|.
name|class
operator|+
literal|".host:port"
decl_stmt|;
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
name|Bus
name|bus
decl_stmt|;
name|NioDatagramConnector
name|connector
init|=
operator|new
name|NioDatagramConnector
argument_list|()
decl_stmt|;
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Queue
argument_list|<
name|ConnectFuture
argument_list|>
argument_list|>
name|connections
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|UDPConduit
parameter_list|(
name|EndpointReferenceType
name|t
parameter_list|,
specifier|final
name|Bus
name|bus
parameter_list|)
block|{
name|super
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|connector
operator|.
name|getSessionConfig
argument_list|()
operator|.
name|setReadBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|connector
operator|.
name|getSessionConfig
argument_list|()
operator|.
name|setSendBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setHandler
argument_list|(
operator|new
name|IoHandlerAdapter
argument_list|()
block|{
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
name|Message
name|message
init|=
operator|(
name|Message
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|CXF_MESSAGE_ATTR
argument_list|)
decl_stmt|;
name|dataReceived
argument_list|(
name|message
argument_list|,
operator|(
name|IoBuffer
operator|)
name|buf
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|dataReceived
parameter_list|(
name|Message
name|message
parameter_list|,
name|IoBuffer
name|buf
parameter_list|,
name|boolean
name|async
parameter_list|,
name|boolean
name|multi
parameter_list|)
block|{
synchronized|synchronized
init|(
name|message
operator|.
name|getExchange
argument_list|()
init|)
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
specifier|final
name|Message
name|inMessage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|IoSessionInputStream
name|ins
init|=
operator|new
name|IoSessionInputStream
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|ins
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|IoSessionInputStream
operator|.
name|class
argument_list|,
name|ins
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setExchange
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|multi
condition|)
block|{
name|mp
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|async
condition|)
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
name|WorkQueue
name|queue
init|=
name|queuem
operator|.
name|getNamedWorkQueue
argument_list|(
literal|"udp-conduit"
argument_list|)
decl_stmt|;
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
name|queue
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isSynchronous
argument_list|()
operator|||
name|multi
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|setInFaultMessage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|mp
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|s
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s2
range|:
name|s
control|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|s2
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|putAll
argument_list|(
name|mp
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|IoSessionInputStream
name|ins
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|IoSessionInputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|ins
operator|.
name|setBuffer
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|close
parameter_list|(
name|Message
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|(
name|msg
argument_list|)
expr_stmt|;
if|if
condition|(
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
operator|||
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|==
name|msg
operator|||
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|getInFaultMessage
argument_list|()
operator|==
name|msg
condition|)
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|HOST_PORT
argument_list|)
decl_stmt|;
name|ConnectFuture
name|c
init|=
name|msg
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|ConnectFuture
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
operator|&&
name|c
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|getSession
argument_list|()
operator|.
name|removeAttribute
argument_list|(
name|CXF_MESSAGE_ATTR
argument_list|)
expr_stmt|;
name|Queue
argument_list|<
name|ConnectFuture
argument_list|>
name|q
init|=
name|connections
operator|.
name|get
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
name|q
operator|==
literal|null
condition|)
block|{
name|connections
operator|.
name|putIfAbsent
argument_list|(
name|s
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|ConnectFuture
argument_list|>
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|=
name|connections
operator|.
name|get
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|q
operator|.
name|offer
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|c
operator|.
name|getSession
argument_list|()
operator|.
name|closeOnFlush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
for|for
control|(
name|Queue
argument_list|<
name|ConnectFuture
argument_list|>
name|f
range|:
name|connections
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|ConnectFuture
name|cf
range|:
name|f
control|)
block|{
name|cf
operator|.
name|getSession
argument_list|()
operator|.
name|closeOnFlush
argument_list|()
expr_stmt|;
block|}
block|}
name|connections
operator|.
name|clear
argument_list|()
expr_stmt|;
name|connector
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|connector
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|prepare
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|String
name|address
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|address
argument_list|)
condition|)
block|{
name|address
operator|=
name|this
operator|.
name|getTarget
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|address
argument_list|)
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
comment|//NIO doesn't support broadcast, we need to drop down to raw
comment|//java.io for these
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
name|sendViaBroadcast
argument_list|(
name|message
argument_list|,
literal|null
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|InetSocketAddress
name|isa
init|=
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
decl_stmt|;
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
name|sendViaBroadcast
argument_list|(
name|message
argument_list|,
name|isa
argument_list|,
name|isa
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|String
name|hp
init|=
name|uri
operator|.
name|getHost
argument_list|()
operator|+
literal|':'
operator|+
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|Queue
argument_list|<
name|ConnectFuture
argument_list|>
name|q
init|=
name|connections
operator|.
name|get
argument_list|(
name|hp
argument_list|)
decl_stmt|;
name|ConnectFuture
name|connFuture
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|q
operator|!=
literal|null
condition|)
block|{
name|connFuture
operator|=
name|q
operator|.
name|poll
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connFuture
operator|==
literal|null
condition|)
block|{
name|connFuture
operator|=
name|connector
operator|.
name|connect
argument_list|(
name|isa
argument_list|)
expr_stmt|;
name|connFuture
operator|.
name|await
argument_list|()
expr_stmt|;
operator|(
operator|(
name|DatagramSessionConfig
operator|)
name|connFuture
operator|.
name|getSession
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|)
operator|.
name|setSendBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DatagramSessionConfig
operator|)
name|connFuture
operator|.
name|getSession
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|)
operator|.
name|setReceiveBufferSize
argument_list|(
literal|64
operator|*
literal|1024
argument_list|)
expr_stmt|;
block|}
name|connFuture
operator|.
name|getSession
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|CXF_MESSAGE_ATTR
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|UDPConduitOutputStream
argument_list|(
name|connFuture
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|ConnectFuture
operator|.
name|class
argument_list|,
name|connFuture
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|HOST_PORT
argument_list|,
name|hp
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|sendViaBroadcast
parameter_list|(
name|Message
name|message
parameter_list|,
name|InetSocketAddress
name|isa
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
operator|new
name|UDPBroadcastOutputStream
argument_list|(
name|port
argument_list|,
name|isa
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
class|class
name|UDPBroadcastOutputStream
extends|extends
name|LoadingByteArrayOutputStream
block|{
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
specifier|private
specifier|final
name|InetSocketAddress
name|multicast
decl_stmt|;
specifier|private
name|UDPBroadcastOutputStream
parameter_list|(
name|int
name|port
parameter_list|,
name|InetSocketAddress
name|isa
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|multicast
operator|=
name|isa
expr_stmt|;
block|}
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
try|try
init|(
name|DatagramSocket
name|socket
init|=
name|multicast
operator|!=
literal|null
condition|?
operator|new
name|MulticastSocket
argument_list|(
literal|null
argument_list|)
else|:
operator|new
name|DatagramSocket
argument_list|()
init|)
block|{
name|socket
operator|.
name|setSendBufferSize
argument_list|(
name|this
operator|.
name|size
argument_list|()
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
name|setBroadcast
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|socket
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|netIntFromMsg
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|NETWORK_INTERFACE
argument_list|)
decl_stmt|;
name|NetworkInterface
name|netInf
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|netIntFromMsg
operator|instanceof
name|String
condition|)
block|{
name|netInf
operator|=
name|NetworkInterface
operator|.
name|getByName
argument_list|(
operator|(
name|String
operator|)
name|netIntFromMsg
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|netIntFromMsg
operator|instanceof
name|NetworkInterface
condition|)
block|{
name|netInf
operator|=
operator|(
name|NetworkInterface
operator|)
name|netIntFromMsg
expr_stmt|;
block|}
if|if
condition|(
name|multicast
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|MulticastSocket
operator|)
name|socket
operator|)
operator|.
name|setLoopbackMode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|netInf
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|MulticastSocket
operator|)
name|socket
operator|)
operator|.
name|setNetworkInterface
argument_list|(
name|netInf
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|multicast
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|NetworkInterface
argument_list|>
name|interfaces
init|=
name|netInf
operator|==
literal|null
condition|?
name|Collections
operator|.
name|list
argument_list|(
name|NetworkInterface
operator|.
name|getNetworkInterfaces
argument_list|()
argument_list|)
else|:
name|Collections
operator|.
name|singletonList
argument_list|(
name|netInf
argument_list|)
decl_stmt|;
for|for
control|(
name|NetworkInterface
name|networkInterface
range|:
name|interfaces
control|)
block|{
if|if
condition|(
operator|!
name|networkInterface
operator|.
name|isUp
argument_list|()
operator|||
name|networkInterface
operator|.
name|isLoopback
argument_list|()
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|InterfaceAddress
name|interfaceAddress
range|:
name|networkInterface
operator|.
name|getInterfaceAddresses
argument_list|()
control|)
block|{
name|InetAddress
name|broadcast
init|=
name|interfaceAddress
operator|.
name|getBroadcast
argument_list|()
decl_stmt|;
if|if
condition|(
name|broadcast
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|DatagramPacket
name|sendPacket
init|=
operator|new
name|DatagramPacket
argument_list|(
name|this
operator|.
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
name|broadcast
argument_list|,
name|port
argument_list|)
decl_stmt|;
try|try
block|{
name|socket
operator|.
name|send
argument_list|(
name|sendPacket
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
block|}
else|else
block|{
name|DatagramPacket
name|sendPacket
init|=
operator|new
name|DatagramPacket
argument_list|(
name|this
operator|.
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
name|multicast
argument_list|)
decl_stmt|;
try|try
block|{
name|socket
operator|.
name|send
argument_list|(
name|sendPacket
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
if|if
condition|(
operator|!
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|64
operator|*
literal|1024
index|]
decl_stmt|;
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
name|Object
name|to
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|MULTI_RESPONSE_TIMEOUT
argument_list|)
decl_stmt|;
name|Integer
name|i
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|to
operator|instanceof
name|String
condition|)
block|{
name|i
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
operator|(
name|String
operator|)
name|to
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|to
operator|instanceof
name|Integer
condition|)
block|{
name|i
operator|=
operator|(
name|Integer
operator|)
name|to
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
literal|null
operator|||
name|i
operator|<=
literal|0
operator|||
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|socket
operator|.
name|setSoTimeout
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|socket
operator|.
name|receive
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|dataReceived
argument_list|(
name|message
argument_list|,
name|IoBuffer
operator|.
name|wrap
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
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|socket
operator|.
name|setSoTimeout
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|socket
operator|.
name|receive
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|dataReceived
argument_list|(
name|message
argument_list|,
name|IoBuffer
operator|.
name|wrap
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
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|found
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{         }
block|}
specifier|static
class|class
name|UDPConduitOutputStream
extends|extends
name|OutputStream
block|{
specifier|final
name|ConnectFuture
name|future
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
name|UDPConduitOutputStream
parameter_list|(
name|ConnectFuture
name|connFuture
parameter_list|)
block|{
name|this
operator|.
name|future
operator|=
name|connFuture
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
index|[]
name|b
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
try|try
block|{
name|future
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
if|if
condition|(
name|future
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|future
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|future
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|future
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
block|}
name|buffer
operator|.
name|flip
argument_list|()
expr_stmt|;
name|future
operator|.
name|getSession
argument_list|()
operator|.
name|write
argument_list|(
name|buffer
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
block|}
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
block|}
end_class

end_unit

