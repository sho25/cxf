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
name|http_jetty
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
name|security
operator|.
name|GeneralSecurityException
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
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
operator|.
name|InstrumentationManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|component
operator|.
name|Container
import|;
end_import

begin_comment
comment|/**  * This Bus Extension handles the configuration of network port  * numbers for use with "http" or "https". This factory  * caches the JettyHTTPServerEngines so that they may be  * retrieved if already previously configured.  */
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
name|JettyHTTPServerEngineFactory
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
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FALLBACK_THREADING_PARAMS_KEY
init|=
literal|0
decl_stmt|;
comment|/**      * This map holds references for allocated ports.      */
comment|// Still use the static map to hold the port information
comment|// in the same JVM
specifier|private
specifier|static
name|ConcurrentHashMap
argument_list|<
name|Integer
argument_list|,
name|JettyHTTPServerEngine
argument_list|>
name|portMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
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
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|ThreadingParameters
name|fallbackThreadingParameters
decl_stmt|;
comment|/**      * This map holds TLS Server Parameters that are to be used to      * configure a subsequently created JettyHTTPServerEngine.      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|tlsParametersMap
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * The bus.      */
specifier|private
name|Bus
name|bus
decl_stmt|;
comment|/**      * The Jetty {@link MBeanContainer} to use when enabling JMX in Jetty.      */
specifier|private
name|Container
operator|.
name|Listener
name|mBeanContainer
decl_stmt|;
specifier|public
name|JettyHTTPServerEngineFactory
parameter_list|()
block|{
comment|// Empty
block|}
specifier|public
name|JettyHTTPServerEngineFactory
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
name|JettyHTTPServerEngineFactory
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
name|threading
parameter_list|)
block|{
name|tlsParametersMap
operator|.
name|putAll
argument_list|(
name|tls
argument_list|)
expr_stmt|;
name|threadingParametersMap
operator|.
name|putAll
argument_list|(
name|threading
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|JettyHTTPServerEngine
name|getOrCreate
parameter_list|(
name|JettyHTTPServerEngineFactory
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
throws|,
name|GeneralSecurityException
block|{
name|JettyHTTPServerEngine
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
name|JettyHTTPServerEngine
argument_list|(
name|factory
operator|.
name|getMBeanContainer
argument_list|()
argument_list|,
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
name|JettyHTTPServerEngine
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
name|ref
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
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
comment|/**      * This call is used to set the bus. It should only be called once.      * @param bus      */
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
name|JettyHTTPServerEngineFactory
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
operator|new
name|JettyBusLifeCycleListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|JettyBusLifeCycleListener
implements|implements
name|BusLifeCycleListener
block|{
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
name|JettyHTTPServerEngineFactory
operator|.
name|this
operator|.
name|initComplete
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{
name|JettyHTTPServerEngineFactory
operator|.
name|this
operator|.
name|preShutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
name|JettyHTTPServerEngineFactory
operator|.
name|this
operator|.
name|postShutdown
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * This call sets TLSParametersMap for a JettyHTTPServerEngine      *      */
specifier|public
name|void
name|setTlsServerParametersMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
name|tlsParamsMap
parameter_list|)
block|{
name|tlsParametersMap
operator|=
name|tlsParamsMap
expr_stmt|;
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
name|tlsParametersMap
return|;
block|}
specifier|public
name|void
name|setEnginesList
parameter_list|(
name|List
argument_list|<
name|JettyHTTPServerEngine
argument_list|>
name|enginesList
parameter_list|)
block|{
for|for
control|(
name|JettyHTTPServerEngine
name|engine
range|:
name|enginesList
control|)
block|{
if|if
condition|(
name|engine
operator|.
name|getPort
argument_list|()
operator|==
name|FALLBACK_THREADING_PARAMS_KEY
condition|)
block|{
name|fallbackThreadingParameters
operator|=
name|engine
operator|.
name|getThreadingParameters
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * This call sets the ThreadingParameters for a JettyHTTPServerEngine      *      */
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
name|threadingParamsMap
parameter_list|)
block|{
name|threadingParametersMap
operator|=
name|threadingParamsMap
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
comment|/**      * This call sets TLSServerParameters for a JettyHTTPServerEngine      * that will be subsequently created. It will not alter an engine      * that has already been created for that network port.      * @param host       if not null, server will listen on this address/host,      *                   otherwise, server will listen on all local addresses.      * @param port       The network port number to bind to the engine.      * @param tlsParams  The tls server parameters. Cannot be null.      * @throws IOException      * @throws GeneralSecurityException      */
specifier|public
name|void
name|setTLSServerParametersForPort
parameter_list|(
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
name|GeneralSecurityException
throws|,
name|IOException
block|{
if|if
condition|(
name|tlsParams
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"tlsParams cannot be null"
argument_list|)
throw|;
block|}
name|JettyHTTPServerEngine
name|ref
init|=
name|retrieveJettyHTTPServerEngine
argument_list|(
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|ref
condition|)
block|{
name|getOrCreate
argument_list|(
name|this
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|tlsParams
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ref
operator|.
name|getConnector
argument_list|()
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getConnector
argument_list|()
operator|.
name|isRunning
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"can't set the TLS params on the opened connector"
argument_list|)
throw|;
block|}
name|ref
operator|.
name|setTlsServerParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * calls thru to {{@link #createJettyHTTPServerEngine(String, int, String)} with 'null' for host value      */
specifier|public
name|void
name|setTLSServerParametersForPort
parameter_list|(
name|int
name|port
parameter_list|,
name|TLSServerParameters
name|tlsParams
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|setTLSServerParametersForPort
argument_list|(
literal|null
argument_list|,
name|port
argument_list|,
name|tlsParams
argument_list|)
expr_stmt|;
block|}
comment|/**      * This call retrieves a previously configured JettyHTTPServerEngine for the      * given port. If none exists, this call returns null.      */
specifier|public
specifier|synchronized
name|JettyHTTPServerEngine
name|retrieveJettyHTTPServerEngine
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
comment|/**      * This call creates a new JettyHTTPServerEngine initialized for "http"      * or "https" on the given port. The determination of "http" or "https"      * will depend on configuration of the engine's bean name.      *      * If an JettyHTTPEngine already exists, or the port      * is already in use, a BindIOException will be thrown. If the      * engine is being Spring configured for TLS a GeneralSecurityException      * may be thrown.      *      * @param host if not null, server will listen on this host/address, otherwise      *        server will listen on all local addresses.      * @param port listen port for server      * @param protocol "http" or "https"      * @param id The key to reference into the tlsParametersMap. Can be null.      * @return      * @throws GeneralSecurityException      * @throws IOException      */
specifier|public
specifier|synchronized
name|JettyHTTPServerEngine
name|createJettyHTTPServerEngine
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|id
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Creating Jetty HTTP Server Engine for port "
operator|+
name|port
operator|+
literal|"."
argument_list|)
expr_stmt|;
name|TLSServerParameters
name|tlsParameters
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
name|tlsParametersMap
operator|!=
literal|null
operator|&&
name|tlsParametersMap
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|tlsParameters
operator|=
name|tlsParametersMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|JettyHTTPServerEngine
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
name|tlsParameters
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
if|if
condition|(
operator|!
operator|(
name|ref
operator|.
name|isSetThreadingParameters
argument_list|()
operator|||
literal|null
operator|==
name|fallbackThreadingParameters
operator|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
condition|)
block|{
specifier|final
name|int
name|min
init|=
name|fallbackThreadingParameters
operator|.
name|getMinThreads
argument_list|()
decl_stmt|;
specifier|final
name|int
name|max
init|=
name|fallbackThreadingParameters
operator|.
name|getMaxThreads
argument_list|()
decl_stmt|;
specifier|final
name|String
name|threadNamePrefix
init|=
name|fallbackThreadingParameters
operator|.
name|getThreadNamePrefix
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"FALLBACK_THREADING_PARAMETERS_MSG"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|port
block|,
name|min
block|,
name|max
block|,
name|threadNamePrefix
block|}
argument_list|)
expr_stmt|;
block|}
name|ref
operator|.
name|setThreadingParameters
argument_list|(
name|fallbackThreadingParameters
argument_list|)
expr_stmt|;
block|}
return|return
name|ref
return|;
block|}
comment|/**      * Calls thru to {{@link #createJettyHTTPServerEngine(String, int, String)} with a 'null' host value      */
specifier|public
specifier|synchronized
name|JettyHTTPServerEngine
name|createJettyHTTPServerEngine
parameter_list|(
name|int
name|port
parameter_list|,
name|String
name|protocol
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
return|return
name|createJettyHTTPServerEngine
argument_list|(
literal|null
argument_list|,
name|port
argument_list|,
name|protocol
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|JettyHTTPServerEngine
name|createJettyHTTPServerEngine
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
name|GeneralSecurityException
throws|,
name|IOException
block|{
return|return
name|createJettyHTTPServerEngine
argument_list|(
name|host
argument_list|,
name|port
argument_list|,
name|protocol
argument_list|,
literal|null
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
name|JettyHTTPServerEngine
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
name|fine
argument_list|(
literal|"Stopping Jetty HTTP Server Engine on port "
operator|+
name|port
operator|+
literal|"."
argument_list|)
expr_stmt|;
try|try
block|{
name|ref
operator|.
name|stop
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
specifier|public
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
operator|&&
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
operator|.
name|getMBeanServer
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|synchronized
name|Container
operator|.
name|Listener
name|getMBeanContainer
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|mBeanContainer
operator|!=
literal|null
condition|)
block|{
return|return
name|mBeanContainer
return|;
block|}
name|MBeanServer
name|mbs
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
if|if
condition|(
name|mbs
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"org.eclipse.jetty.jmx.MBeanContainer"
argument_list|,
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|mBeanContainer
operator|=
operator|(
name|Container
operator|.
name|Listener
operator|)
name|cls
operator|.
name|getConstructor
argument_list|(
name|MBeanServer
operator|.
name|class
argument_list|)
operator|.
name|newInstance
argument_list|(
name|mbs
argument_list|)
expr_stmt|;
try|try
block|{
name|cls
operator|.
name|getMethod
argument_list|(
literal|"start"
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
operator|)
literal|null
argument_list|)
operator|.
name|invoke
argument_list|(
name|mBeanContainer
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|mex
parameter_list|)
block|{
comment|//ignore, Jetty 9.1 removed this methods and it's not needed anymore
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|//ignore - just won't instrument jetty.  Probably don't have the
comment|//jetty-management jar available
name|LOG
operator|.
name|info
argument_list|(
literal|"Could not load or start org.eclipse.management.MBeanContainer.  "
operator|+
literal|"Jetty JMX support will not be enabled: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|mBeanContainer
return|;
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
comment|// shut down the jetty server in the portMap
comment|// To avoid the CurrentModificationException,
comment|// do not use portMap.values directly
name|JettyHTTPServerEngine
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
name|JettyHTTPServerEngine
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
name|JettyHTTPServerEngine
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
comment|// clean up the collections
name|threadingParametersMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|tlsParametersMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mBeanContainer
operator|=
literal|null
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
block|}
end_class

end_unit

