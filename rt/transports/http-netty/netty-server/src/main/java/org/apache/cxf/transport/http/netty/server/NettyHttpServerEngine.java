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
name|URL
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
name|ConcurrentHashMap
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
name|Executors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
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
name|transport
operator|.
name|HttpUriMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|bootstrap
operator|.
name|ServerBootstrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioServerSocketChannelFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|timeout
operator|.
name|IdleStateHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|util
operator|.
name|HashedWheelTimer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpServerEngine
implements|implements
name|ServerEngine
block|{
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
specifier|volatile
name|Channel
name|serverChannel
decl_stmt|;
specifier|private
name|NettyHttpServletPipelineFactory
name|servletPipeline
decl_stmt|;
specifier|private
name|Timer
name|timer
init|=
operator|new
name|HashedWheelTimer
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|NettyHttpContextHandler
argument_list|>
name|handlerMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|NettyHttpContextHandler
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * This field holds the TLS ServerParameters that are programatically      * configured. The tlsServerParamers (due to JAXB) holds the struct      * placed by SpringConfig.      */
specifier|private
name|TLSServerParameters
name|tlsServerParameters
decl_stmt|;
specifier|private
name|int
name|readIdleTime
init|=
literal|60
decl_stmt|;
specifier|private
name|int
name|writeIdleTime
init|=
literal|30
decl_stmt|;
specifier|private
name|boolean
name|sessionSupport
decl_stmt|;
specifier|public
name|NettyHttpServerEngine
parameter_list|()
block|{              }
specifier|public
name|NettyHttpServerEngine
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
name|String
name|getProtocol
parameter_list|()
block|{
return|return
name|protocol
return|;
block|}
specifier|public
name|void
name|setProtocol
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
name|this
operator|.
name|protocol
operator|=
name|protocol
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|finalizeConfig
parameter_list|()
block|{
comment|// need to check if we need to any other thing other than Setting the TLSServerParameter
block|}
comment|/**      * This method is used to programmatically set the TLSServerParameters.      * This method may only be called by the factory.      * @throws IOException       */
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
block|}
comment|/**      * This method returns the programmatically set TLSServerParameters, not      * the TLSServerParametersType, which is the JAXB generated type used       * in SpringConfiguration.      * @return      */
specifier|public
name|TLSServerParameters
name|getTlsServerParameters
parameter_list|()
block|{
return|return
name|tlsServerParameters
return|;
block|}
specifier|protected
name|Channel
name|startServer
parameter_list|()
block|{
comment|// TODO Configure the server.
specifier|final
name|ServerBootstrap
name|bootstrap
init|=
operator|new
name|ServerBootstrap
argument_list|(
operator|new
name|NioServerSocketChannelFactory
argument_list|(
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
argument_list|,
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// Set up the idle handler
name|IdleStateHandler
name|idleStateHandler
init|=
operator|new
name|IdleStateHandler
argument_list|(
name|this
operator|.
name|timer
argument_list|,
name|getReadIdleTime
argument_list|()
argument_list|,
name|getWriteIdleTime
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|// Set up the event pipeline factory.
name|servletPipeline
operator|=
operator|new
name|NettyHttpServletPipelineFactory
argument_list|(
name|tlsServerParameters
argument_list|,
name|sessionSupport
argument_list|,
name|port
argument_list|,
name|handlerMap
argument_list|,
name|idleStateHandler
argument_list|)
expr_stmt|;
comment|// Start the servletPipeline's timer
name|servletPipeline
operator|.
name|start
argument_list|()
expr_stmt|;
name|bootstrap
operator|.
name|setPipelineFactory
argument_list|(
name|servletPipeline
argument_list|)
expr_stmt|;
name|InetSocketAddress
name|address
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
name|address
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
name|address
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
block|}
comment|// Bind and start to accept incoming connections.
return|return
name|bootstrap
operator|.
name|bind
argument_list|(
name|address
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addServant
parameter_list|(
name|URL
name|url
parameter_list|,
name|NettyHttpHandler
name|handler
parameter_list|)
block|{
if|if
condition|(
name|serverChannel
operator|==
literal|null
condition|)
block|{
name|serverChannel
operator|=
name|startServer
argument_list|()
expr_stmt|;
block|}
comment|// need to set the handler name for looking up
name|handler
operator|.
name|setName
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// need to check if the NettyContext is there
name|NettyHttpContextHandler
name|contextHandler
init|=
name|handlerMap
operator|.
name|get
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextHandler
operator|==
literal|null
condition|)
block|{
name|contextHandler
operator|=
operator|new
name|NettyHttpContextHandler
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
name|handlerMap
operator|.
name|put
argument_list|(
name|contextName
argument_list|,
name|contextHandler
argument_list|)
expr_stmt|;
block|}
name|contextHandler
operator|.
name|addNettyHttpHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
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
specifier|final
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
name|NettyHttpContextHandler
name|contextHandler
init|=
name|handlerMap
operator|.
name|get
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextHandler
operator|!=
literal|null
condition|)
block|{
name|contextHandler
operator|.
name|removeNettyHttpHandler
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|contextHandler
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// remove the contextHandler from handlerMap
name|handlerMap
operator|.
name|remove
argument_list|(
name|contextName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|NettyHttpHandler
name|getServant
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
specifier|final
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
name|NettyHttpContextHandler
name|contextHandler
init|=
name|handlerMap
operator|.
name|get
argument_list|(
name|contextName
argument_list|)
decl_stmt|;
if|if
condition|(
name|contextHandler
operator|!=
literal|null
condition|)
block|{
return|return
name|contextHandler
operator|.
name|getNettyHttpHandler
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
comment|// stop the timer
name|timer
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// just unbind the channel
if|if
condition|(
name|serverChannel
operator|!=
literal|null
condition|)
block|{
name|serverChannel
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|servletPipeline
operator|!=
literal|null
condition|)
block|{
name|servletPipeline
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|getReadIdleTime
parameter_list|()
block|{
return|return
name|readIdleTime
return|;
block|}
specifier|public
name|void
name|setReadIdleTime
parameter_list|(
name|int
name|readIdleTime
parameter_list|)
block|{
name|this
operator|.
name|readIdleTime
operator|=
name|readIdleTime
expr_stmt|;
block|}
specifier|public
name|int
name|getWriteIdleTime
parameter_list|()
block|{
return|return
name|writeIdleTime
return|;
block|}
specifier|public
name|void
name|setWriteIdleTime
parameter_list|(
name|int
name|writeIdleTime
parameter_list|)
block|{
name|this
operator|.
name|writeIdleTime
operator|=
name|writeIdleTime
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSessionSupport
parameter_list|()
block|{
return|return
name|sessionSupport
return|;
block|}
specifier|public
name|void
name|setSessionSupport
parameter_list|(
name|boolean
name|session
parameter_list|)
block|{
name|this
operator|.
name|sessionSupport
operator|=
name|session
expr_stmt|;
block|}
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
block|}
end_class

end_unit

