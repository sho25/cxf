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
name|HttpResponse
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
name|ProtocolException
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
name|client
operator|.
name|RedirectStrategy
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
name|client
operator|.
name|methods
operator|.
name|HttpUriRequest
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
name|config
operator|.
name|ConnectionConfig
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
name|config
operator|.
name|Registry
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
name|config
operator|.
name|RegistryBuilder
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
name|cookie
operator|.
name|Cookie
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
name|BasicCookieStore
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
name|conn
operator|.
name|DefaultSchemePortResolver
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
name|conn
operator|.
name|SystemDefaultDnsResolver
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
name|client
operator|.
name|CloseableHttpAsyncClient
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
name|client
operator|.
name|HttpAsyncClientBuilder
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
name|client
operator|.
name|HttpAsyncClients
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
name|ManagedNHttpClientConnectionFactory
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
name|PoolingNHttpClientConnectionManager
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
name|ManagedNHttpClientConnection
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
name|NoopIOSessionStrategy
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
name|SchemeIOSessionStrategy
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
name|ssl
operator|.
name|SSLIOSessionStrategy
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
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|AsyncHTTPConduitFactory
implements|implements
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
specifier|public
specifier|static
specifier|final
name|String
name|CONNECTION_MAX_IDLE
init|=
literal|"org.apache.cxf.transport.http.async.CONNECTION_MAX_IDLE"
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
enum|enum
name|UseAsyncPolicy
block|{
name|ALWAYS
block|,
name|ASYNC_ONLY
block|,
name|NEVER
block|;
specifier|public
specifier|static
name|UseAsyncPolicy
name|getPolicy
parameter_list|(
name|Object
name|st
parameter_list|)
block|{
if|if
condition|(
name|st
operator|instanceof
name|UseAsyncPolicy
condition|)
block|{
return|return
operator|(
name|UseAsyncPolicy
operator|)
name|st
return|;
block|}
elseif|else
if|if
condition|(
name|st
operator|instanceof
name|String
condition|)
block|{
name|String
name|s
init|=
operator|(
operator|(
name|String
operator|)
name|st
operator|)
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"ALWAYS"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|ALWAYS
return|;
block|}
elseif|else
if|if
condition|(
literal|"NEVER"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|NEVER
return|;
block|}
elseif|else
if|if
condition|(
literal|"ASYNC_ONLY"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|ASYNC_ONLY
return|;
block|}
else|else
block|{
name|st
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|st
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
operator|(
name|Boolean
operator|)
name|st
operator|)
operator|.
name|booleanValue
argument_list|()
condition|?
name|ALWAYS
else|:
name|NEVER
return|;
block|}
return|return
name|ASYNC_ONLY
return|;
block|}
block|}
empty_stmt|;
specifier|volatile
name|PoolingNHttpClientConnectionManager
name|connectionManager
decl_stmt|;
specifier|volatile
name|CloseableHttpAsyncClient
name|client
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
name|int
name|connectionMaxIdle
init|=
literal|60000
decl_stmt|;
name|int
name|ioThreadCount
init|=
name|IOReactorConfig
operator|.
name|DEFAULT
operator|.
name|getIoThreadCount
argument_list|()
decl_stmt|;
name|long
name|selectInterval
init|=
name|IOReactorConfig
operator|.
name|DEFAULT
operator|.
name|getSelectInterval
argument_list|()
decl_stmt|;
name|boolean
name|interestOpQueued
init|=
name|IOReactorConfig
operator|.
name|DEFAULT
operator|.
name|isInterestOpQueued
argument_list|()
decl_stmt|;
name|int
name|soLinger
init|=
name|IOReactorConfig
operator|.
name|DEFAULT
operator|.
name|getSoLinger
argument_list|()
decl_stmt|;
name|int
name|soTimeout
init|=
name|IOReactorConfig
operator|.
name|DEFAULT
operator|.
name|getSoTimeout
argument_list|()
decl_stmt|;
name|boolean
name|soKeepalive
init|=
name|IOReactorConfig
operator|.
name|DEFAULT
operator|.
name|isSoKeepalive
argument_list|()
decl_stmt|;
name|boolean
name|tcpNoDelay
init|=
literal|true
decl_stmt|;
name|AsyncHTTPConduitFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
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
name|this
argument_list|()
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
name|this
argument_list|()
expr_stmt|;
name|addListener
argument_list|(
name|b
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
name|client
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
name|CloseableHttpAsyncClient
name|client2
init|=
name|client
decl_stmt|;
name|resetVars
argument_list|()
expr_stmt|;
name|shutdown
argument_list|(
name|client2
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|resetVars
parameter_list|()
block|{
name|client
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
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
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
name|policy
operator|=
name|UseAsyncPolicy
operator|.
name|getPolicy
argument_list|(
name|st
argument_list|)
expr_stmt|;
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
name|connectionMaxIdle
operator|=
name|getInt
argument_list|(
name|s
operator|.
name|get
argument_list|(
name|CONNECTION_MAX_IDLE
argument_list|)
argument_list|,
name|connectionMaxIdle
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
name|ioThreadCount
decl_stmt|;
name|ioThreadCount
operator|=
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
expr_stmt|;
name|changed
operator||=
name|i
operator|!=
name|ioThreadCount
expr_stmt|;
name|long
name|l
init|=
name|selectInterval
decl_stmt|;
name|selectInterval
operator|=
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
expr_stmt|;
name|changed
operator||=
name|l
operator|!=
name|selectInterval
expr_stmt|;
name|i
operator|=
name|soLinger
expr_stmt|;
name|soLinger
operator|=
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
expr_stmt|;
name|changed
operator||=
name|i
operator|!=
name|soLinger
expr_stmt|;
name|i
operator|=
name|soTimeout
expr_stmt|;
name|soTimeout
operator|=
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
expr_stmt|;
name|changed
operator||=
name|i
operator|!=
name|soTimeout
expr_stmt|;
name|boolean
name|b
init|=
name|interestOpQueued
decl_stmt|;
name|interestOpQueued
operator|=
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
expr_stmt|;
name|changed
operator||=
name|b
operator|!=
name|interestOpQueued
expr_stmt|;
name|b
operator|=
name|tcpNoDelay
expr_stmt|;
name|tcpNoDelay
operator|=
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
expr_stmt|;
name|changed
operator||=
name|b
operator|!=
name|tcpNoDelay
expr_stmt|;
name|b
operator|=
name|soKeepalive
expr_stmt|;
name|soKeepalive
operator|=
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
expr_stmt|;
name|changed
operator||=
name|b
operator|!=
name|soKeepalive
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
name|Bus
name|bus
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
return|return
name|createConduit
argument_list|(
name|bus
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|)
return|;
block|}
specifier|public
name|HTTPConduit
name|createConduit
parameter_list|(
name|Bus
name|bus
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
name|bus
argument_list|,
name|localInfo
argument_list|,
name|target
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|shutdown
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|connectionManager
operator|=
literal|null
expr_stmt|;
name|client
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
name|CloseableHttpAsyncClient
name|client
parameter_list|)
block|{
try|try
block|{
name|client
operator|.
name|close
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
block|}
specifier|private
name|void
name|addListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|BusLifeCycleManager
name|manager
init|=
name|b
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|manager
operator|.
name|registerLifeCycleListener
argument_list|(
operator|new
name|BusLifeCycleListener
argument_list|()
block|{
specifier|public
name|void
name|initComplete
parameter_list|()
block|{                 }
specifier|public
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
block|{                 }
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|setupNIOClient
parameter_list|(
name|HTTPClientPolicy
name|clientPolicy
parameter_list|)
throws|throws
name|IOReactorException
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|IOReactorConfig
name|config
init|=
name|IOReactorConfig
operator|.
name|custom
argument_list|()
operator|.
name|setIoThreadCount
argument_list|(
name|ioThreadCount
argument_list|)
operator|.
name|setSelectInterval
argument_list|(
name|selectInterval
argument_list|)
operator|.
name|setInterestOpQueued
argument_list|(
name|interestOpQueued
argument_list|)
operator|.
name|setSoLinger
argument_list|(
name|soLinger
argument_list|)
operator|.
name|setSoTimeout
argument_list|(
name|soTimeout
argument_list|)
operator|.
name|setSoKeepAlive
argument_list|(
name|soKeepalive
argument_list|)
operator|.
name|setTcpNoDelay
argument_list|(
name|tcpNoDelay
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Registry
argument_list|<
name|SchemeIOSessionStrategy
argument_list|>
name|ioSessionFactoryRegistry
init|=
name|RegistryBuilder
operator|.
expr|<
name|SchemeIOSessionStrategy
operator|>
name|create
argument_list|()
operator|.
name|register
argument_list|(
literal|"http"
argument_list|,
name|NoopIOSessionStrategy
operator|.
name|INSTANCE
argument_list|)
operator|.
name|register
argument_list|(
literal|"https"
argument_list|,
name|SSLIOSessionStrategy
operator|.
name|getSystemDefaultStrategy
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ManagedNHttpClientConnectionFactory
name|connectionFactory
init|=
operator|new
name|ManagedNHttpClientConnectionFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ManagedNHttpClientConnection
name|create
parameter_list|(
specifier|final
name|IOSession
name|iosession
parameter_list|,
specifier|final
name|ConnectionConfig
name|config
parameter_list|)
block|{
name|ManagedNHttpClientConnection
name|conn
init|=
name|super
operator|.
name|create
argument_list|(
name|iosession
argument_list|,
name|config
argument_list|)
decl_stmt|;
return|return
name|conn
return|;
block|}
block|}
decl_stmt|;
name|DefaultConnectingIOReactor
name|ioreactor
init|=
operator|new
name|DefaultConnectingIOReactor
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|connectionManager
operator|=
operator|new
name|PoolingNHttpClientConnectionManager
argument_list|(
name|ioreactor
argument_list|,
name|connectionFactory
argument_list|,
name|ioSessionFactoryRegistry
argument_list|,
name|DefaultSchemePortResolver
operator|.
name|INSTANCE
argument_list|,
name|SystemDefaultDnsResolver
operator|.
name|INSTANCE
argument_list|,
name|connectionTTL
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
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
name|ConnectionConfig
name|connectionConfig
init|=
name|ConnectionConfig
operator|.
name|custom
argument_list|()
operator|.
name|setBufferSize
argument_list|(
name|clientPolicy
operator|.
name|getChunkLength
argument_list|()
operator|>
literal|0
condition|?
name|clientPolicy
operator|.
name|getChunkLength
argument_list|()
else|:
literal|16332
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|connectionManager
operator|.
name|setDefaultConnectionConfig
argument_list|(
name|connectionConfig
argument_list|)
expr_stmt|;
name|RedirectStrategy
name|redirectStrategy
init|=
operator|new
name|RedirectStrategy
argument_list|()
block|{
specifier|public
name|boolean
name|isRedirected
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|ProtocolException
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|HttpUriRequest
name|getRedirect
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|ProtocolException
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|HttpAsyncClientBuilder
name|httpAsyncClientBuilder
init|=
name|HttpAsyncClients
operator|.
name|custom
argument_list|()
operator|.
name|setConnectionManager
argument_list|(
name|connectionManager
argument_list|)
operator|.
name|setRedirectStrategy
argument_list|(
name|redirectStrategy
argument_list|)
operator|.
name|setDefaultCookieStore
argument_list|(
operator|new
name|BasicCookieStore
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|public
name|void
name|addCookie
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
block|{                 }
block|}
argument_list|)
decl_stmt|;
name|adaptClientBuilder
argument_list|(
name|httpAsyncClientBuilder
argument_list|)
expr_stmt|;
name|client
operator|=
name|httpAsyncClientBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// Start the client thread
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|connectionTTL
operator|==
literal|0
condition|)
block|{
comment|//if the connection does not have an expiry deadline
comment|//use the ConnectionMaxIdle to close the idle connection
operator|new
name|CloseIdleConnectionThread
argument_list|(
name|connectionManager
argument_list|,
name|client
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
comment|//provide a hook to customize the builder
specifier|protected
name|void
name|adaptClientBuilder
parameter_list|(
name|HttpAsyncClientBuilder
name|httpAsyncClientBuilder
parameter_list|)
block|{         }
specifier|public
name|CloseableHttpAsyncClient
name|createClient
parameter_list|(
specifier|final
name|AsyncHTTPConduit
name|c
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|setupNIOClient
argument_list|(
name|c
operator|.
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
specifier|public
class|class
name|CloseIdleConnectionThread
extends|extends
name|Thread
block|{
specifier|private
specifier|final
name|PoolingNHttpClientConnectionManager
name|connMgr
decl_stmt|;
specifier|private
specifier|final
name|CloseableHttpAsyncClient
name|client
decl_stmt|;
specifier|public
name|CloseIdleConnectionThread
parameter_list|(
name|PoolingNHttpClientConnectionManager
name|connMgr
parameter_list|,
name|CloseableHttpAsyncClient
name|client
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|connMgr
operator|=
name|connMgr
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
name|client
operator|.
name|isRunning
argument_list|()
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|sleep
argument_list|(
name|connectionMaxIdle
argument_list|)
expr_stmt|;
comment|// close connections
comment|// that have been idle longer than specified connectionMaxIdle
name|connMgr
operator|.
name|closeIdleConnections
argument_list|(
name|connectionMaxIdle
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// terminate
block|}
block|}
block|}
block|}
end_class

end_unit

