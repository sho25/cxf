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
name|netty
operator|.
name|server
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
name|MalformedURLException
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
name|BusFactory
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
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
name|AbstractHTTPDestination
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
name|DestinationRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpDestination
extends|extends
name|AbstractHTTPDestination
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
name|NettyHttpDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|NettyHttpServerEngine
name|engine
decl_stmt|;
specifier|protected
name|NettyHttpServerEngineFactory
name|serverEngineFactory
decl_stmt|;
specifier|protected
name|ServletContext
name|servletContext
decl_stmt|;
specifier|protected
name|ClassLoader
name|loader
decl_stmt|;
specifier|protected
name|URL
name|nurl
decl_stmt|;
specifier|private
name|boolean
name|configFinalized
decl_stmt|;
comment|/**      * Constructor      *      * @param b                   the associated Bus      * @param registry            the associated destinationRegistry      * @param ei                  the endpoint info of the destination      * @param serverEngineFactory the serverEngineFactory which could be used to create ServerEngine      * @throws java.io.IOException      */
specifier|public
name|NettyHttpDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|NettyHttpServerEngineFactory
name|serverEngineFactory
parameter_list|)
throws|throws
name|IOException
block|{
comment|//Add the default port if the address is missing it
name|super
argument_list|(
name|b
argument_list|,
name|registry
argument_list|,
name|ei
argument_list|,
name|getAddressValue
argument_list|(
name|ei
argument_list|,
literal|true
argument_list|)
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|loader
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|serverEngineFactory
operator|=
name|serverEngineFactory
expr_stmt|;
name|nurl
operator|=
operator|new
name|URL
argument_list|(
name|getAddress
argument_list|(
name|endpointInfo
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|retrieveEngine
parameter_list|()
throws|throws
name|IOException
block|{
name|engine
operator|=
name|serverEngineFactory
operator|.
name|retrieveNettyHttpServerEngine
argument_list|(
name|nurl
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|engine
operator|==
literal|null
condition|)
block|{
name|engine
operator|=
name|serverEngineFactory
operator|.
name|createNettyHttpServerEngine
argument_list|(
name|nurl
operator|.
name|getHost
argument_list|()
argument_list|,
name|nurl
operator|.
name|getPort
argument_list|()
argument_list|,
name|nurl
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
assert|assert
name|engine
operator|!=
literal|null
assert|;
block|}
specifier|public
name|void
name|finalizeConfig
parameter_list|()
block|{
assert|assert
operator|!
name|configFinalized
assert|;
try|try
block|{
name|retrieveEngine
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|configFinalized
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
name|String
name|getAddress
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|String
name|address
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
literal|"netty://"
argument_list|)
condition|)
block|{
name|address
operator|=
name|address
operator|.
name|substring
argument_list|(
literal|8
argument_list|)
expr_stmt|;
block|}
return|return
name|address
return|;
block|}
specifier|protected
name|String
name|getBasePath
parameter_list|(
name|String
name|contextPath
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
operator|new
name|URL
argument_list|(
name|getAddress
argument_list|(
name|endpointInfo
argument_list|)
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
comment|/**      * Activate receipt of incoming messages.      */
specifier|protected
name|void
name|activate
parameter_list|()
block|{
name|super
operator|.
name|activate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Activating receipt of incoming messages"
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|getAddress
argument_list|(
name|endpointInfo
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// setup the path for it
name|engine
operator|.
name|addServant
argument_list|(
name|url
argument_list|,
operator|new
name|NettyHttpHandler
argument_list|(
name|this
argument_list|,
name|contextMatchOnExact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Deactivate receipt of incoming messages.      */
specifier|protected
name|void
name|deactivate
parameter_list|()
block|{
name|super
operator|.
name|deactivate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Deactivating receipt of incoming messages"
argument_list|)
expr_stmt|;
name|engine
operator|.
name|removeServant
argument_list|(
name|nurl
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doService
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
name|doService
argument_list|(
name|servletContext
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doService
parameter_list|(
name|ServletContext
name|context
parameter_list|,
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|resp
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|servletContext
expr_stmt|;
block|}
if|if
condition|(
name|getServer
argument_list|()
operator|.
name|isSetRedirectURL
argument_list|()
condition|)
block|{
name|resp
operator|.
name|sendRedirect
argument_list|(
name|getServer
argument_list|()
operator|.
name|getRedirectURL
argument_list|()
argument_list|)
expr_stmt|;
name|resp
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// REVISIT: service on executor if associated with endpoint
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
name|Bus
name|origBus
init|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|invoke
argument_list|(
literal|null
argument_list|,
name|context
argument_list|,
name|req
argument_list|,
name|resp
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|invokeComplete
parameter_list|(
specifier|final
name|ServletContext
name|context
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|resp
parameter_list|,
name|Message
name|m
parameter_list|)
throws|throws
name|IOException
block|{
name|resp
operator|.
name|flushBuffer
argument_list|()
expr_stmt|;
name|super
operator|.
name|invokeComplete
argument_list|(
name|context
argument_list|,
name|req
argument_list|,
name|resp
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerEngine
name|getEngine
parameter_list|()
block|{
return|return
name|engine
return|;
block|}
specifier|protected
name|Message
name|retrieveFromContinuation
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
operator|(
name|Message
operator|)
name|req
operator|.
name|getAttribute
argument_list|(
name|CXF_CONTINUATION_MESSAGE
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setupContinuation
parameter_list|(
name|Message
name|inMessage
parameter_list|,
specifier|final
name|HttpServletRequest
name|req
parameter_list|,
specifier|final
name|HttpServletResponse
name|resp
parameter_list|)
block|{
comment|// Here we don't support the Continuation
block|}
specifier|protected
name|String
name|getBasePathForFullAddress
parameter_list|(
name|String
name|addr
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URL
argument_list|(
name|addr
argument_list|)
operator|.
name|getPath
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|servletContext
return|;
block|}
specifier|public
name|void
name|setServletContext
parameter_list|(
name|ServletContext
name|servletContext
parameter_list|)
block|{
name|this
operator|.
name|servletContext
operator|=
name|servletContext
expr_stmt|;
block|}
block|}
end_class

end_unit

