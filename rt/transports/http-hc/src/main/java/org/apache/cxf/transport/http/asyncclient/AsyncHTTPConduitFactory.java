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
name|http
operator|.
name|asyncclient
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
name|InterruptedIOException
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
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
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
name|injection
operator|.
name|NoJSR250Annotations
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
name|SystemPropertyAction
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduitFactory
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
name|HTTPTransportFactory
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
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponseFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|DefaultHttpResponseFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|EntityEnclosingRequestWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|DefaultHttpClientIODispatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|conn
operator|.
name|DefaultClientAsyncConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|conn
operator|.
name|PoolingClientAsyncConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|reactor
operator|.
name|DefaultConnectingIOReactor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|nio
operator|.
name|reactor
operator|.
name|IOReactorConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|ClientAsyncConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|ClientAsyncConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|scheme
operator|.
name|AsyncScheme
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|conn
operator|.
name|scheme
operator|.
name|AsyncSchemeRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|protocol
operator|.
name|HttpAsyncRequestExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|ConnectingIOReactor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|IOEventDispatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|IOReactorException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|IOSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|reactor
operator|.
name|ssl
operator|.
name|SSLIOSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|util
operator|.
name|ByteBufferAllocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|nio
operator|.
name|util
operator|.
name|HeapByteBufferAllocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|BasicHttpParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|HttpParams
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|AsyncHTTPConduitFactory
implements|implements
name|BusLifeCycleListener
implements|,
name|HTTPConduitFactory
block|{
comment|//TCP related properties
specifier|public
specifier|static
specifier|final
name|String
name|TCP_NODELAY
init|=
literal|"org.apache.cxf.transport.http.async.TCP_NODELAY"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SO_KEEPALIVE
init|=
literal|"org.apache.cxf.transport.http.async.SO_KEEPALIVE"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SO_LINGER
init|=
literal|"org.apache.cxf.transport.http.async.SO_LINGER"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SO_TIMEOUT
init|=
literal|"org.apache.cxf.transport.http.async.SO_TIMEOUT"
decl_stmt|;
comment|//ConnectionPool
specifier|public
specifier|static
specifier|final
name|String
name|MAX_CONNECTIONS
init|=
literal|"org.apache.cxf.transport.http.async.MAX_CONNECTIONS"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAX_PER_HOST_CONNECTIONS
init|=
literal|"org.apache.cxf.transport.http.async.MAX_PER_HOST_CONNECTIONS"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_TTL
init|=
literal|"org.apache.cxf.transport.http.async.CONNECTION_TTL"
decl_stmt|;
comment|//AsycClient specific props
specifier|public
specifier|static
specifier|final
name|String
name|THREAD_COUNT
init|=
literal|"org.apache.cxf.transport.http.async.ioThreadCount"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INTEREST_OP_QUEUED
init|=
literal|"org.apache.cxf.transport.http.async.interestOpQueued"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SELECT_INTERVAL
init|=
literal|"org.apache.cxf.transport.http.async.selectInterval"
decl_stmt|;
comment|//CXF specific
specifier|public
specifier|static
specifier|final
name|String
name|USE_POLICY
init|=
literal|"org.apache.cxf.transport.http.async.usePolicy"
decl_stmt|;
specifier|public
specifier|static
enum|enum
name|UseAsyncPolicy
block|{
name|ALWAYS
block|,
name|ASYNC_ONLY
block|,
name|NEVER
block|}
empty_stmt|;
specifier|final
name|IOReactorConfig
name|config
init|=
operator|new
name|IOReactorConfig
argument_list|()
decl_stmt|;
specifier|volatile
name|CXFAsyncRequester
name|requester
decl_stmt|;
specifier|volatile
name|ConnectingIOReactor
name|ioReactor
decl_stmt|;
specifier|volatile
name|PoolingClientAsyncConnectionManager
name|connectionManager
decl_stmt|;
name|boolean
name|isShutdown
decl_stmt|;
name|UseAsyncPolicy
name|policy
decl_stmt|;
name|int
name|maxConnections
init|=
literal|5000
decl_stmt|;
name|int
name|maxPerRoute
init|=
literal|1000
decl_stmt|;
name|int
name|connectionTTL
init|=
literal|60000
decl_stmt|;
specifier|public
name|AsyncHTTPConduitFactory
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|conf
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|config
operator|.
name|setTcpNoDelay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AsyncHTTPConduitFactory
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|addListener
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTcpNoDelay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|b
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UseAsyncPolicy
name|getUseAsyncPolicy
parameter_list|()
block|{
return|return
name|policy
return|;
block|}
specifier|public
name|void
name|update
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
parameter_list|)
block|{
if|if
condition|(
name|setProperties
argument_list|(
name|props
argument_list|)
operator|&&
name|ioReactor
operator|!=
literal|null
condition|)
block|{
name|restartReactor
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|restartReactor
parameter_list|()
block|{
name|ConnectingIOReactor
name|ioReactor2
init|=
name|ioReactor
decl_stmt|;
name|PoolingClientAsyncConnectionManager
name|connectionManager2
init|=
name|connectionManager
decl_stmt|;
name|resetVars
argument_list|()
expr_stmt|;
name|shutdown
argument_list|(
name|ioReactor2
argument_list|,
name|connectionManager2
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|resetVars
parameter_list|()
block|{
name|requester
operator|=
literal|null
expr_stmt|;
name|ioReactor
operator|=
literal|null
expr_stmt|;
name|connectionManager
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|boolean
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|s
parameter_list|)
block|{
comment|//properties that can be updated "live"
name|Object
name|st
init|=
name|s
operator|.
name|get
argument_list|(
name|USE_POLICY
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|==
literal|null
condition|)
block|{
name|st
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|USE_POLICY
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|st
operator|instanceof
name|UseAsyncPolicy
condition|)
block|{
name|policy
operator|=
operator|(
name|UseAsyncPolicy
operator|)
name|st
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|st
operator|instanceof
name|String
condition|)
block|{
name|policy
operator|=
name|UseAsyncPolicy
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|st
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//policy = UseAsyncPolicy.ALWAYS;
name|policy
operator|=
name|UseAsyncPolicy
operator|.
name|ASYNC_ONLY
expr_stmt|;
block|}
name|maxConnections
operator|=
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|MAX_CONNECTIONS
argument_list|)
argument_list|,
name|maxConnections
argument_list|)
expr_stmt|;
name|connectionTTL
operator|=
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|CONNECTION_TTL
argument_list|)
argument_list|,
name|connectionTTL
argument_list|)
expr_stmt|;
name|maxPerRoute
operator|=
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|MAX_PER_HOST_CONNECTIONS
argument_list|)
argument_list|,
name|maxPerRoute
argument_list|)
expr_stmt|;
if|if
condition|(
name|connectionManager
operator|!=
literal|null
condition|)
block|{
name|connectionManager
operator|.
name|setMaxTotal
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
name|connectionManager
operator|.
name|setDefaultMaxPerRoute
argument_list|(
name|maxPerRoute
argument_list|)
expr_stmt|;
block|}
comment|//properties that need a restart of the reactor
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
name|int
name|i
init|=
name|config
operator|.
name|getIoThreadCount
argument_list|()
decl_stmt|;
name|config
operator|.
name|setIoThreadCount
argument_list|(
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|THREAD_COUNT
argument_list|)
argument_list|,
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|i
operator|!=
name|config
operator|.
name|getIoThreadCount
argument_list|()
expr_stmt|;
name|long
name|l
init|=
name|config
operator|.
name|getSelectInterval
argument_list|()
decl_stmt|;
name|config
operator|.
name|setSelectInterval
argument_list|(
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|SELECT_INTERVAL
argument_list|)
argument_list|,
literal|1000
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|l
operator|!=
name|config
operator|.
name|getSelectInterval
argument_list|()
expr_stmt|;
name|i
operator|=
name|config
operator|.
name|getSoLinger
argument_list|()
expr_stmt|;
name|config
operator|.
name|setSoLinger
argument_list|(
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|SO_LINGER
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|i
operator|!=
name|config
operator|.
name|getSoLinger
argument_list|()
expr_stmt|;
name|i
operator|=
name|config
operator|.
name|getSoTimeout
argument_list|()
expr_stmt|;
name|config
operator|.
name|setSoTimeout
argument_list|(
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|SO_TIMEOUT
argument_list|)
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|i
operator|!=
name|config
operator|.
name|getSoTimeout
argument_list|()
expr_stmt|;
name|boolean
name|b
init|=
name|config
operator|.
name|isInterestOpQueued
argument_list|()
decl_stmt|;
name|config
operator|.
name|setInterestOpQueued
argument_list|(
name|getBoolean
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|INTEREST_OP_QUEUED
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|b
operator|!=
name|config
operator|.
name|isInterestOpQueued
argument_list|()
expr_stmt|;
name|b
operator|=
name|config
operator|.
name|isTcpNoDelay
argument_list|()
expr_stmt|;
name|config
operator|.
name|setTcpNoDelay
argument_list|(
name|getBoolean
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|TCP_NODELAY
argument_list|)
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|b
operator|!=
name|config
operator|.
name|isTcpNoDelay
argument_list|()
expr_stmt|;
name|b
operator|=
name|config
operator|.
name|isSoKeepalive
argument_list|()
expr_stmt|;
name|config
operator|.
name|setSoKeepalive
argument_list|(
name|getBoolean
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|SO_KEEPALIVE
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|changed
operator||=
name|b
operator|!=
name|config
operator|.
name|isSoKeepalive
argument_list|()
expr_stmt|;
return|return
name|changed
return|;
block|}
specifier|private
name|int
name|getInt
parameter_list|(
name|Object
name|s
parameter_list|,
name|int
name|defaultv
parameter_list|)
block|{
name|int
name|i
init|=
name|defaultv
decl_stmt|;
if|if
condition|(
name|s
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
name|s
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|instanceof
name|Number
condition|)
block|{
name|i
operator|=
operator|(
operator|(
name|Number
operator|)
name|s
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
operator|-
literal|1
condition|)
block|{
name|i
operator|=
name|defaultv
expr_stmt|;
block|}
return|return
name|i
return|;
block|}
specifier|private
name|boolean
name|getBoolean
parameter_list|(
name|Object
name|s
parameter_list|,
name|boolean
name|defaultv
parameter_list|)
block|{
if|if
condition|(
name|s
operator|instanceof
name|String
condition|)
block|{
return|return
name|Boolean
operator|.
name|parseBoolean
argument_list|(
operator|(
name|String
operator|)
name|s
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|s
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
operator|(
name|Boolean
operator|)
name|s
operator|)
operator|.
name|booleanValue
argument_list|()
return|;
block|}
return|return
name|defaultv
return|;
block|}
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|isShutdown
return|;
block|}
annotation|@
name|Override
specifier|public
name|HTTPConduit
name|createConduit
parameter_list|(
name|HTTPTransportFactory
name|f
parameter_list|,
name|EndpointInfo
name|localInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isShutdown
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|AsyncHTTPConduit
argument_list|(
name|f
operator|.
name|getBus
argument_list|()
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|,
name|this
argument_list|)
return|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|addListener
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{     }
specifier|public
specifier|synchronized
name|void
name|preShutdown
parameter_list|()
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{     }
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|ioReactor
operator|!=
literal|null
condition|)
block|{
name|shutdown
argument_list|(
name|ioReactor
argument_list|,
name|connectionManager
argument_list|)
expr_stmt|;
name|connectionManager
operator|=
literal|null
expr_stmt|;
name|ioReactor
operator|=
literal|null
expr_stmt|;
name|requester
operator|=
literal|null
expr_stmt|;
block|}
name|isShutdown
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|shutdown
parameter_list|(
name|ConnectingIOReactor
name|ioReactor2
parameter_list|,
name|PoolingClientAsyncConnectionManager
name|connectionManager2
parameter_list|)
block|{
try|try
block|{
name|connectionManager2
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e1
parameter_list|)
block|{
name|e1
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|ioReactor2
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|b
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|setupNIOClient
parameter_list|()
throws|throws
name|IOReactorException
block|{
if|if
condition|(
name|requester
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
comment|// Create client-side I/O reactor
specifier|final
name|IOEventDispatch
name|ioEventDispatch
init|=
operator|new
name|DefaultHttpClientIODispatch
argument_list|(
operator|new
name|HttpAsyncRequestExecutor
argument_list|()
argument_list|,
operator|new
name|BasicHttpParams
argument_list|()
argument_list|)
decl_stmt|;
name|ioReactor
operator|=
operator|new
name|DefaultConnectingIOReactor
argument_list|(
name|config
argument_list|)
expr_stmt|;
comment|// Run the I/O reactor in a separate thread
name|Thread
name|t
init|=
operator|new
name|Thread
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
try|try
block|{
comment|// Ready to go!
name|ioReactor
operator|.
name|execute
argument_list|(
name|ioEventDispatch
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedIOException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Interrupted"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"I/O error: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
comment|// Start the client thread
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
name|AsyncSchemeRegistry
name|registry
init|=
operator|new
name|AsyncSchemeRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|AsyncScheme
argument_list|(
literal|"http"
argument_list|,
literal|80
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
operator|new
name|AsyncScheme
argument_list|(
literal|"https"
argument_list|,
literal|443
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|connectionManager
operator|=
operator|new
name|PoolingClientAsyncConnectionManager
argument_list|(
name|ioReactor
argument_list|,
name|registry
argument_list|,
name|connectionTTL
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|ClientAsyncConnectionFactory
name|createClientAsyncConnectionFactory
parameter_list|()
block|{
specifier|final
name|HttpResponseFactory
name|responseFactory
init|=
operator|new
name|DefaultHttpResponseFactory
argument_list|()
decl_stmt|;
specifier|final
name|ByteBufferAllocator
name|allocator
init|=
operator|new
name|HeapByteBufferAllocator
argument_list|()
decl_stmt|;
return|return
operator|new
name|ClientAsyncConnectionFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClientAsyncConnection
name|create
parameter_list|(
name|String
name|id
parameter_list|,
name|IOSession
name|iosession
parameter_list|,
name|HttpParams
name|params
parameter_list|)
block|{
return|return
operator|new
name|DefaultClientAsyncConnection
argument_list|(
name|id
argument_list|,
name|iosession
argument_list|,
name|responseFactory
argument_list|,
name|allocator
argument_list|,
name|params
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|onRequestSubmitted
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
block|{
name|super
operator|.
name|onRequestSubmitted
argument_list|(
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|EntityEnclosingRequestWrapper
condition|)
block|{
name|request
operator|=
operator|(
operator|(
name|EntityEnclosingRequestWrapper
operator|)
name|request
operator|)
operator|.
name|getOriginal
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getIOSession
argument_list|()
operator|instanceof
name|SSLIOSession
condition|)
block|{
name|SSLIOSession
name|sslio
init|=
operator|(
name|SSLIOSession
operator|)
name|getIOSession
argument_list|()
decl_stmt|;
name|getIOSession
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|CXFHttpRequest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|getIOSession
argument_list|()
operator|.
name|getAttribute
argument_list|(
literal|"cxf.handshake.done"
argument_list|)
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|CXFHttpRequest
operator|)
name|request
operator|)
operator|.
name|getOutputStream
argument_list|()
operator|.
name|setSSLSession
argument_list|(
name|sslio
operator|.
name|getSSLSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|;
block|}
block|}
return|;
block|}
block|}
expr_stmt|;
name|connectionManager
operator|.
name|setDefaultMaxPerRoute
argument_list|(
name|maxPerRoute
argument_list|)
expr_stmt|;
name|connectionManager
operator|.
name|setMaxTotal
argument_list|(
name|maxConnections
argument_list|)
expr_stmt|;
name|requester
operator|=
operator|new
name|CXFAsyncRequester
argument_list|(
name|connectionManager
argument_list|)
expr_stmt|;
block|}
specifier|public
name|CXFAsyncRequester
name|getRequester
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|requester
operator|==
literal|null
condition|)
block|{
name|setupNIOClient
argument_list|()
expr_stmt|;
block|}
return|return
name|requester
return|;
block|}
block|}
end_class

end_unit

