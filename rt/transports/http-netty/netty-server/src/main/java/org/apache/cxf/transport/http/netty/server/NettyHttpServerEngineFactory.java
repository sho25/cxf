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
name|TreeMap
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSServerParameters
import|;
end_import

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
name|NettyHttpServerEngineFactory
implements|implements
name|BusLifeCycleListener
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
name|NettyHttpServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|ConcurrentHashMap
argument_list|<
name|Integer
argument_list|,
name|NettyHttpServerEngine
argument_list|>
name|portMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Integer
argument_list|,
name|NettyHttpServerEngine
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|BusLifeCycleManager
name|lifeCycleManager
decl_stmt|;
comment|/**      * This map holds the threading parameters that are to be applied      * to new Engines when bound to the reference id.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
name|threadingParametersMap
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|tlsServerParametersMap
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|NettyHttpServerEngineFactory
parameter_list|()
block|{
comment|// Empty
block|}
specifier|public
name|NettyHttpServerEngineFactory
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NettyHttpServerEngineFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|tls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
name|threads
parameter_list|)
block|{
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|tlsServerParametersMap
operator|=
name|tls
expr_stmt|;
name|threadingParametersMap
operator|=
name|threads
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
comment|/**      * This call is used to set the bus. It should only be called once.      *      * @param bus      */
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"cxf"
argument_list|)
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|NettyHttpServerEngineFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|lifeCycleManager
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|lifeCycleManager
condition|)
block|{
name|lifeCycleManager
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|getTlsServerParametersMap
parameter_list|()
block|{
return|return
name|tlsServerParametersMap
return|;
block|}
specifier|public
name|void
name|setTlsServerParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|tlsParametersMap
parameter_list|)
block|{
name|this
operator|.
name|tlsServerParametersMap
operator|=
name|tlsParametersMap
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
name|getThreadingParametersMap
parameter_list|()
block|{
return|return
name|threadingParametersMap
return|;
block|}
specifier|public
name|void
name|setThreadingParametersMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
name|parameterMap
parameter_list|)
block|{
name|this
operator|.
name|threadingParametersMap
operator|=
name|parameterMap
expr_stmt|;
block|}
specifier|public
name|void
name|setEnginesList
parameter_list|(
name|List
argument_list|<
name|NettyHttpServerEngine
argument_list|>
name|enginesList
parameter_list|)
block|{
for|for
control|(
name|NettyHttpServerEngine
name|engine
range|:
name|enginesList
control|)
block|{
name|portMap
operator|.
name|putIfAbsent
argument_list|(
name|engine
operator|.
name|getPort
argument_list|()
argument_list|,
name|engine
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
comment|// do nothing here
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
comment|// shut down the Netty server in the portMap
comment|// To avoid the CurrentModificationException,
comment|// do not use portMap.values directly
name|NettyHttpServerEngine
index|[]
name|engines
init|=
name|portMap
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|NettyHttpServerEngine
index|[
name|portMap
operator|.
name|values
argument_list|()
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|NettyHttpServerEngine
name|engine
range|:
name|engines
control|)
block|{
name|engine
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
comment|// The engine which is in shutdown status cannot be started anymore
name|portMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|threadingParametersMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|tlsServerParametersMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
comment|// do nothing here
comment|// just let server registry to call the server stop first
block|}
specifier|private
specifier|static
name|NettyHttpServerEngine
name|getOrCreate
parameter_list|(
name|NettyHttpServerEngineFactory
name|factory
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|TLSServerParameters
name|tlsParams
parameter_list|)
throws|throws
name|IOException
block|{
name|NettyHttpServerEngine
name|ref
init|=
name|portMap
operator|.
name|get
argument_list|(
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|ref
operator|=
operator|new
name|NettyHttpServerEngine
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
expr_stmt|;
if|if
condition|(
name|tlsParams
operator|!=
literal|null
condition|)
block|{
name|ref
operator|.
name|setTlsServerParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
block|}
name|ref
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
name|NettyHttpServerEngine
name|tmpRef
init|=
name|portMap
operator|.
name|putIfAbsent
argument_list|(
name|port
argument_list|,
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpRef
operator|!=
literal|null
condition|)
block|{
name|ref
operator|=
name|tmpRef
expr_stmt|;
block|}
block|}
return|return
name|ref
return|;
block|}
specifier|public
specifier|synchronized
name|NettyHttpServerEngine
name|retrieveNettyHttpServerEngine
parameter_list|(
name|int
name|port
parameter_list|)
block|{
return|return
name|portMap
operator|.
name|get
argument_list|(
name|port
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|NettyHttpServerEngine
name|createNettyHttpServerEngine
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|protocol
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"CREATING_NETTY_SERVER_ENGINE"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|TLSServerParameters
name|tlsServerParameters
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"https"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
operator|&&
name|tlsServerParametersMap
operator|!=
literal|null
condition|)
block|{
name|tlsServerParameters
operator|=
name|tlsServerParametersMap
operator|.
name|get
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|NettyHttpServerEngine
name|ref
init|=
name|getOrCreate
argument_list|(
name|this
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|tlsServerParameters
argument_list|)
decl_stmt|;
comment|// checking the protocol
if|if
condition|(
operator|!
name|protocol
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Protocol mismatch for port "
operator|+
name|port
operator|+
literal|": "
operator|+
literal|"engine's protocol is "
operator|+
name|ref
operator|.
name|getProtocol
argument_list|()
operator|+
literal|", the url protocol is "
operator|+
name|protocol
argument_list|)
throw|;
block|}
return|return
name|ref
return|;
block|}
specifier|public
specifier|synchronized
name|NettyHttpServerEngine
name|createNettyHttpServerEngine
parameter_list|(
name|int
name|port
parameter_list|,
name|String
name|protocol
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createNettyHttpServerEngine
argument_list|(
literal|null
argument_list|,
name|port
argument_list|,
name|protocol
argument_list|)
return|;
block|}
comment|/**      * This method removes the Server Engine from the port map and stops it.      */
specifier|public
specifier|static
specifier|synchronized
name|void
name|destroyForPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|NettyHttpServerEngine
name|ref
init|=
name|portMap
operator|.
name|remove
argument_list|(
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"STOPPING_NETTY_SERVER_ENGINE"
argument_list|,
name|port
argument_list|)
expr_stmt|;
try|try
block|{
name|ref
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

