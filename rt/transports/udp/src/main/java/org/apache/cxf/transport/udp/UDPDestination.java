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
name|BufferedOutputStream
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
name|InetSocketAddress
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
name|NioDatagramAcceptor
name|acceptor
decl_stmt|;
name|AutomaticWorkQueue
name|queue
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
name|acceptor
operator|=
literal|null
expr_stmt|;
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
name|BufferedOutputStream
argument_list|(
name|out
argument_list|,
literal|64
operator|*
literal|1024
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
block|}
block|}
end_class

end_unit

