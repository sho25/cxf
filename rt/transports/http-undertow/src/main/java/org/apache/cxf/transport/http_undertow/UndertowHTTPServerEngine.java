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
name|http_undertow
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
name|net
operator|.
name|URL
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|ConcurrentMap
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
name|Level
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|X509KeyManager
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
name|ServletException
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
name|i18n
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
name|PropertyUtils
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
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
name|interceptor
operator|.
name|Fault
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
name|HttpUriMapper
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
name|https
operator|.
name|AliasedX509ExtendedKeyManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|Sequence
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|SslClientAuthMode
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|Handlers
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|Undertow
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|Undertow
operator|.
name|Builder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|UndertowOptions
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|handlers
operator|.
name|HttpContinueReadHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|handlers
operator|.
name|PathHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|api
operator|.
name|DeploymentInfo
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|api
operator|.
name|DeploymentManager
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|api
operator|.
name|ServletContainer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|api
operator|.
name|ServletInfo
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|core
operator|.
name|ServletContainerImpl
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|servlet
operator|.
name|handlers
operator|.
name|ServletPathMatches
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|CopyOnWriteMap
import|;
end_import

begin_class
specifier|public
class|class
name|UndertowHTTPServerEngine
implements|implements
name|ServerEngine
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DO_NOT_CHECK_URL_PROP
init|=
literal|"org.apache.cxf.transports.http_undertow.DontCheckUrl"
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
name|UndertowHTTPServerEngine
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * This is the network port for which this engine is allocated.      */
specifier|private
name|int
name|port
decl_stmt|;
comment|/**      * This is the network address for which this engine is allocated.      */
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * This field holds the protocol for which this engine is      * enabled, i.e. "http" or "https".      */
specifier|private
name|String
name|protocol
init|=
literal|"http"
decl_stmt|;
specifier|private
name|int
name|servantCount
decl_stmt|;
specifier|private
name|Undertow
name|server
decl_stmt|;
comment|/**      * This field holds the TLS ServerParameters that are programatically      * configured. The tlsServerParamers (due to JAXB) holds the struct      * placed by SpringConfig.      */
specifier|private
name|TLSServerParameters
name|tlsServerParameters
decl_stmt|;
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
comment|/**      * This boolean signfies that SpringConfig is over. finalizeConfig      * has been called.      */
specifier|private
name|boolean
name|configFinalized
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|UndertowHTTPHandler
argument_list|>
name|registedPaths
init|=
operator|new
name|CopyOnWriteMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|continuationsEnabled
init|=
literal|true
decl_stmt|;
specifier|private
name|ServletContext
name|servletContext
decl_stmt|;
specifier|private
name|PathHandler
name|path
decl_stmt|;
specifier|private
name|int
name|maxIdleTime
init|=
literal|200000
decl_stmt|;
specifier|private
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http_undertow
operator|.
name|ThreadingParameters
name|threadingParameters
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CXFUndertowHttpHandler
argument_list|>
name|handlers
decl_stmt|;
specifier|public
name|UndertowHTTPServerEngine
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
specifier|public
name|UndertowHTTPServerEngine
parameter_list|()
block|{      }
annotation|@
name|Override
specifier|public
name|void
name|addServant
parameter_list|(
name|URL
name|url
parameter_list|,
name|UndertowHTTPHandler
name|handler
parameter_list|)
block|{
if|if
condition|(
name|shouldCheckUrl
argument_list|(
name|handler
operator|.
name|getBus
argument_list|()
argument_list|)
condition|)
block|{
name|checkRegistedContext
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// create a new undertow server instance if there is no server there
name|String
name|contextName
init|=
name|HttpUriMapper
operator|.
name|getContextName
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|servletContext
operator|=
name|buildServletContext
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
name|handler
operator|.
name|setServletContext
argument_list|(
name|servletContext
argument_list|)
expr_stmt|;
name|server
operator|=
name|createServer
argument_list|(
name|url
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"START_UP_SERVER_FAILED_MSG"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
operator|.
name|getMessage
argument_list|()
block|,
name|port
block|}
argument_list|)
expr_stmt|;
comment|//problem starting server
try|try
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore - probably wasn't fully started anyway
block|}
name|server
operator|=
literal|null
expr_stmt|;
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"START_UP_SERVER_FAILED_MSG"
argument_list|,
name|LOG
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|port
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|String
name|contextName
init|=
name|HttpUriMapper
operator|.
name|getContextName
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|servletContext
operator|=
name|buildServletContext
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServletException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"START_UP_SERVER_FAILED_MSG"
argument_list|,
name|LOG
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|port
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|handler
operator|.
name|setServletContext
argument_list|(
name|servletContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|isContextMatchExact
argument_list|()
condition|)
block|{
name|path
operator|.
name|addExactPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|addPrefixPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|String
name|smap
init|=
name|HttpUriMapper
operator|.
name|getResourceBase
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|handler
operator|.
name|setName
argument_list|(
name|smap
argument_list|)
expr_stmt|;
name|registedPaths
operator|.
name|put
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|servantCount
operator|=
name|servantCount
operator|+
literal|1
expr_stmt|;
block|}
specifier|private
name|ServletContext
name|buildServletContext
parameter_list|(
name|String
name|contextName
parameter_list|)
throws|throws
name|ServletException
block|{
name|ServletContainer
name|servletContainer
init|=
operator|new
name|ServletContainerImpl
argument_list|()
decl_stmt|;
name|DeploymentInfo
name|deploymentInfo
init|=
operator|new
name|DeploymentInfo
argument_list|()
decl_stmt|;
name|deploymentInfo
operator|.
name|setClassLoader
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|deploymentInfo
operator|.
name|setDeploymentName
argument_list|(
literal|"cxf-undertow"
argument_list|)
expr_stmt|;
name|deploymentInfo
operator|.
name|setContextPath
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
name|ServletInfo
name|asyncServlet
init|=
operator|new
name|ServletInfo
argument_list|(
name|ServletPathMatches
operator|.
name|DEFAULT_SERVLET_NAME
argument_list|,
name|CxfUndertowServlet
operator|.
name|class
argument_list|)
decl_stmt|;
name|deploymentInfo
operator|.
name|addServlet
argument_list|(
name|asyncServlet
argument_list|)
expr_stmt|;
name|servletContainer
operator|.
name|addDeployment
argument_list|(
name|deploymentInfo
argument_list|)
expr_stmt|;
name|DeploymentManager
name|deploymentManager
init|=
name|servletContainer
operator|.
name|getDeployment
argument_list|(
name|deploymentInfo
operator|.
name|getDeploymentName
argument_list|()
argument_list|)
decl_stmt|;
name|deploymentManager
operator|.
name|deploy
argument_list|()
expr_stmt|;
name|deploymentManager
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|deploymentManager
operator|.
name|getDeployment
argument_list|()
operator|.
name|getServletContext
argument_list|()
return|;
block|}
specifier|private
name|Undertow
name|createServer
parameter_list|(
name|URL
name|url
parameter_list|,
name|UndertowHTTPHandler
name|undertowHTTPHandler
parameter_list|)
throws|throws
name|Exception
block|{
name|Undertow
operator|.
name|Builder
name|result
init|=
name|Undertow
operator|.
name|builder
argument_list|()
decl_stmt|;
name|result
operator|.
name|setServerOption
argument_list|(
name|UndertowOptions
operator|.
name|IDLE_TIMEOUT
argument_list|,
name|getMaxIdleTime
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|tlsServerParameters
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|sslContext
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|sslContext
operator|=
name|createSSLContext
argument_list|()
expr_stmt|;
block|}
name|result
operator|=
name|result
operator|.
name|addHttpsListener
argument_list|(
name|getPort
argument_list|()
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|this
operator|.
name|sslContext
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|result
operator|.
name|addHttpListener
argument_list|(
name|getPort
argument_list|()
argument_list|,
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
name|Handlers
operator|.
name|path
argument_list|(
operator|new
name|NotFoundHandler
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|setHandler
argument_list|(
name|Handlers
operator|.
name|trace
argument_list|(
name|undertowHTTPHandler
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|undertowHTTPHandler
operator|.
name|isContextMatchExact
argument_list|()
condition|)
block|{
name|path
operator|.
name|addExactPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
name|undertowHTTPHandler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|addPrefixPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
name|undertowHTTPHandler
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|result
operator|.
name|setHandler
argument_list|(
name|wrapHandler
argument_list|(
operator|new
name|HttpContinueReadHandler
argument_list|(
name|path
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|decorateUndertowSocketConnection
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|disableSSLv3
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|result
operator|=
name|configureThreads
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|Builder
name|configureThreads
parameter_list|(
name|Builder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|threadingParameters
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|threadingParameters
operator|.
name|isWorkerIOThreadsSet
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|setWorkerOption
argument_list|(
name|Options
operator|.
name|WORKER_IO_THREADS
argument_list|,
name|this
operator|.
name|threadingParameters
operator|.
name|getWorkerIOThreads
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|threadingParameters
operator|.
name|isMinThreadsSet
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|setWorkerOption
argument_list|(
name|Options
operator|.
name|WORKER_TASK_CORE_THREADS
argument_list|,
name|this
operator|.
name|threadingParameters
operator|.
name|getMinThreads
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|threadingParameters
operator|.
name|isMaxThreadsSet
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|setWorkerOption
argument_list|(
name|Options
operator|.
name|WORKER_TASK_MAX_THREADS
argument_list|,
name|this
operator|.
name|threadingParameters
operator|.
name|getMaxThreads
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
return|;
block|}
specifier|private
name|HttpHandler
name|wrapHandler
parameter_list|(
name|HttpHandler
name|handler
parameter_list|)
block|{
name|HttpHandler
name|nextHandler
init|=
name|handler
decl_stmt|;
for|for
control|(
name|CXFUndertowHttpHandler
name|h
range|:
name|getHandlers
argument_list|()
control|)
block|{
name|h
operator|.
name|setNext
argument_list|(
name|nextHandler
argument_list|)
expr_stmt|;
name|nextHandler
operator|=
name|h
expr_stmt|;
block|}
return|return
name|nextHandler
return|;
block|}
specifier|private
name|Builder
name|disableSSLv3
parameter_list|(
name|Builder
name|result
parameter_list|)
block|{
comment|//SSLv3 isn't safe, disable it by default unless explicitly use it
if|if
condition|(
name|tlsServerParameters
operator|!=
literal|null
operator|&&
operator|(
literal|"SSLv3"
operator|.
name|equals
argument_list|(
name|tlsServerParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
argument_list|)
operator|||
operator|!
name|tlsServerParameters
operator|.
name|getIncludeProtocols
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|protocols
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"TLSv1"
argument_list|,
literal|"TLSv1.1"
argument_list|,
literal|"TLSv1.2"
argument_list|,
literal|"SSLv3"
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|excludedProtocol
range|:
name|tlsServerParameters
operator|.
name|getExcludeProtocols
argument_list|()
control|)
block|{
if|if
condition|(
name|protocols
operator|.
name|contains
argument_list|(
name|excludedProtocol
argument_list|)
condition|)
block|{
name|protocols
operator|.
name|remove
argument_list|(
name|excludedProtocol
argument_list|)
expr_stmt|;
block|}
block|}
name|Sequence
argument_list|<
name|String
argument_list|>
name|supportProtocols
init|=
name|Sequence
operator|.
name|of
argument_list|(
name|protocols
argument_list|)
decl_stmt|;
return|return
name|result
operator|.
name|setSocketOption
argument_list|(
name|Options
operator|.
name|SSL_ENABLED_PROTOCOLS
argument_list|,
name|supportProtocols
argument_list|)
return|;
block|}
name|Sequence
argument_list|<
name|String
argument_list|>
name|supportProtocols
init|=
name|Sequence
operator|.
name|of
argument_list|(
literal|"TLSv1"
argument_list|,
literal|"TLSv1.1"
argument_list|,
literal|"TLSv1.2"
argument_list|)
decl_stmt|;
return|return
name|result
operator|.
name|setSocketOption
argument_list|(
name|Options
operator|.
name|SSL_ENABLED_PROTOCOLS
argument_list|,
name|supportProtocols
argument_list|)
return|;
block|}
specifier|public
name|Undertow
operator|.
name|Builder
name|decorateUndertowSocketConnection
parameter_list|(
name|Undertow
operator|.
name|Builder
name|builder
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|tlsServerParameters
operator|!=
literal|null
operator|&&
name|this
operator|.
name|tlsServerParameters
operator|.
name|getClientAuthentication
argument_list|()
operator|!=
literal|null
operator|&&
name|this
operator|.
name|tlsServerParameters
operator|.
name|getClientAuthentication
argument_list|()
operator|.
name|isRequired
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|setSocketOption
argument_list|(
name|Options
operator|.
name|SSL_CLIENT_AUTH_MODE
argument_list|,
name|SslClientAuthMode
operator|.
name|REQUIRED
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|tlsServerParameters
operator|!=
literal|null
operator|&&
name|this
operator|.
name|tlsServerParameters
operator|.
name|getClientAuthentication
argument_list|()
operator|!=
literal|null
operator|&&
name|this
operator|.
name|tlsServerParameters
operator|.
name|getClientAuthentication
argument_list|()
operator|.
name|isWant
argument_list|()
operator|&&
operator|!
name|this
operator|.
name|tlsServerParameters
operator|.
name|getClientAuthentication
argument_list|()
operator|.
name|isRequired
argument_list|()
condition|)
block|{
name|builder
operator|=
name|builder
operator|.
name|setSocketOption
argument_list|(
name|Options
operator|.
name|SSL_CLIENT_AUTH_MODE
argument_list|,
name|SslClientAuthMode
operator|.
name|REQUESTED
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
return|;
block|}
specifier|private
name|boolean
name|shouldCheckUrl
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|Object
name|prop
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|prop
operator|=
name|bus
operator|.
name|getProperty
argument_list|(
name|DO_NOT_CHECK_URL_PROP
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prop
operator|==
literal|null
condition|)
block|{
name|prop
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
name|DO_NOT_CHECK_URL_PROP
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|prop
argument_list|)
return|;
block|}
specifier|protected
name|void
name|checkRegistedContext
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|String
name|urlPath
init|=
name|url
operator|.
name|getPath
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|registedPath
range|:
name|registedPaths
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|urlPath
operator|.
name|equals
argument_list|(
name|registedPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ADD_HANDLER_CONTEXT_IS_USED_MSG"
argument_list|,
name|LOG
argument_list|,
name|url
argument_list|,
name|registedPath
argument_list|)
argument_list|)
throw|;
block|}
comment|// There are some context path conflicts which could cause the UndertowHTTPServerEngine
comment|// doesn't route the message to the right UndertowHTTPHandler
if|if
condition|(
name|urlPath
operator|.
name|equals
argument_list|(
name|HttpUriMapper
operator|.
name|getContextName
argument_list|(
name|registedPath
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ADD_HANDLER_CONTEXT_IS_USED_MSG"
argument_list|,
name|LOG
argument_list|,
name|url
argument_list|,
name|registedPath
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|registedPath
operator|.
name|equals
argument_list|(
name|HttpUriMapper
operator|.
name|getContextName
argument_list|(
name|urlPath
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
operator|new
name|Message
argument_list|(
literal|"ADD_HANDLER_CONTEXT_CONFILICT_MSG"
argument_list|,
name|LOG
argument_list|,
name|url
argument_list|,
name|registedPath
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeServant
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|UndertowHTTPHandler
name|handler
init|=
name|registedPaths
operator|.
name|remove
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
return|return;
block|}
operator|--
name|servantCount
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|handler
operator|.
name|isContextMatchExact
argument_list|()
condition|)
block|{
name|path
operator|.
name|removeExactPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|removePrefixPath
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|UndertowHTTPHandler
name|getServant
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
return|return
name|registedPaths
operator|.
name|get
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the protocol "http" or "https" for which this engine      * was configured.      */
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
comment|/**      * Returns the port number for which this server engine was configured.      * @return      */
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * Returns the host for which this server engine was configured.      * @return      */
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|p
parameter_list|)
block|{
name|port
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
specifier|public
name|void
name|finalizeConfig
parameter_list|()
block|{
name|retrieveListenerFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|configFinalized
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * This method is used to programmatically set the TLSServerParameters.      * This method may only be called by the factory.      * @throws IOException      */
specifier|public
name|void
name|setTlsServerParameters
parameter_list|(
name|TLSServerParameters
name|params
parameter_list|)
block|{
name|tlsServerParameters
operator|=
name|params
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|configFinalized
condition|)
block|{
name|this
operator|.
name|retrieveListenerFactory
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|retrieveListenerFactory
parameter_list|()
block|{
if|if
condition|(
name|tlsServerParameters
operator|!=
literal|null
condition|)
block|{
name|protocol
operator|=
literal|"https"
expr_stmt|;
block|}
else|else
block|{
name|protocol
operator|=
literal|"http"
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Configured port "
operator|+
name|port
operator|+
literal|" for \""
operator|+
name|protocol
operator|+
literal|"\"."
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method returns the programmatically set TLSServerParameters, not      * the TLSServerParametersType, which is the JAXB generated type used      * in SpringConfiguration.      * @return      */
specifier|public
name|TLSServerParameters
name|getTlsServerParameters
parameter_list|()
block|{
return|return
name|tlsServerParameters
return|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|server
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This method will shut down the server engine and      * remove it from the factory's cache.      */
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|registedPaths
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|shouldDestroyPort
argument_list|()
condition|)
block|{
if|if
condition|(
name|servantCount
operator|==
literal|0
condition|)
block|{
name|UndertowHTTPServerEngineFactory
operator|.
name|destroyForPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"FAILED_TO_SHUTDOWN_ENGINE_MSG"
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|shouldDestroyPort
parameter_list|()
block|{
comment|//if we shutdown the port, on SOME OS's/JVM's, if a client
comment|//in the same jvm had been talking to it at some point and keep alives
comment|//are on, then the port is held open for about 60 seconds
comment|//afterwards and if we restart, connections will then
comment|//get sent into the old stuff where there are
comment|//no longer any servant registered.   They pretty much just hang.
comment|//this is most often seen in our unit/system tests that
comment|//test things in the same VM.
name|String
name|s
init|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"org.apache.cxf.transports.http_undertow.DontClosePort."
operator|+
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|s
operator|=
name|SystemPropertyAction
operator|.
name|getPropertyOrNull
argument_list|(
literal|"org.apache.cxf.transports.http_undertow.DontClosePort"
argument_list|)
expr_stmt|;
block|}
return|return
operator|!
name|Boolean
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
return|;
block|}
specifier|protected
name|SSLContext
name|createSSLContext
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|proto
init|=
name|tlsServerParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
operator|==
literal|null
condition|?
literal|"TLS"
else|:
name|tlsServerParameters
operator|.
name|getSecureSocketProtocol
argument_list|()
decl_stmt|;
name|SSLContext
name|context
init|=
name|tlsServerParameters
operator|.
name|getJsseProvider
argument_list|()
operator|==
literal|null
condition|?
name|SSLContext
operator|.
name|getInstance
argument_list|(
name|proto
argument_list|)
else|:
name|SSLContext
operator|.
name|getInstance
argument_list|(
name|proto
argument_list|,
name|tlsServerParameters
operator|.
name|getJsseProvider
argument_list|()
argument_list|)
decl_stmt|;
name|KeyManager
index|[]
name|keyManagers
init|=
name|tlsServerParameters
operator|.
name|getKeyManagers
argument_list|()
decl_stmt|;
if|if
condition|(
name|tlsServerParameters
operator|.
name|getCertAlias
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|keyManagers
operator|=
name|getKeyManagersWithCertAlias
argument_list|(
name|keyManagers
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|init
argument_list|(
name|keyManagers
argument_list|,
name|tlsServerParameters
operator|.
name|getTrustManagers
argument_list|()
argument_list|,
name|tlsServerParameters
operator|.
name|getSecureRandom
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|protected
name|KeyManager
index|[]
name|getKeyManagersWithCertAlias
parameter_list|(
name|KeyManager
index|[]
name|keyManagers
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|tlsServerParameters
operator|.
name|getCertAlias
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|keyManagers
operator|.
name|length
condition|;
name|idx
operator|++
control|)
block|{
if|if
condition|(
name|keyManagers
index|[
name|idx
index|]
operator|instanceof
name|X509KeyManager
condition|)
block|{
name|keyManagers
index|[
name|idx
index|]
operator|=
operator|new
name|AliasedX509ExtendedKeyManager
argument_list|(
name|tlsServerParameters
operator|.
name|getCertAlias
argument_list|()
argument_list|,
operator|(
name|X509KeyManager
operator|)
name|keyManagers
index|[
name|idx
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|keyManagers
return|;
block|}
comment|/**      * This method sets the threading parameters for this particular      * server engine.      * This method may only be called by the factory.      */
specifier|public
name|void
name|setThreadingParameters
parameter_list|(
name|ThreadingParameters
name|params
parameter_list|)
block|{
name|threadingParameters
operator|=
name|params
expr_stmt|;
block|}
comment|/**      * This method returns whether the threading parameters are set.      */
specifier|public
name|boolean
name|isSetThreadingParameters
parameter_list|()
block|{
return|return
name|threadingParameters
operator|!=
literal|null
return|;
block|}
comment|/**      * This method returns the threading parameters that have been set.      * This method may return null, if the threading parameters have not      * been set.      */
specifier|public
name|ThreadingParameters
name|getThreadingParameters
parameter_list|()
block|{
return|return
name|threadingParameters
return|;
block|}
specifier|public
name|void
name|setContinuationsEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|continuationsEnabled
operator|=
name|enabled
expr_stmt|;
block|}
specifier|public
name|boolean
name|getContinuationsEnabled
parameter_list|()
block|{
return|return
name|continuationsEnabled
return|;
block|}
specifier|public
name|int
name|getMaxIdleTime
parameter_list|()
block|{
return|return
name|maxIdleTime
return|;
block|}
specifier|public
name|void
name|setMaxIdleTime
parameter_list|(
name|int
name|maxIdleTime
parameter_list|)
block|{
name|this
operator|.
name|maxIdleTime
operator|=
name|maxIdleTime
expr_stmt|;
block|}
comment|/**      * set the Undertow server's handlers      * @param h      */
specifier|public
name|void
name|setHandlers
parameter_list|(
name|List
argument_list|<
name|CXFUndertowHttpHandler
argument_list|>
name|h
parameter_list|)
block|{
name|handlers
operator|=
name|h
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|CXFUndertowHttpHandler
argument_list|>
name|getHandlers
parameter_list|()
block|{
return|return
name|handlers
operator|!=
literal|null
condition|?
name|handlers
else|:
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
block|}
end_class

end_unit

