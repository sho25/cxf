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
name|TreeMap
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
name|PostConstruct
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

begin_comment
comment|/**  * This Bus Extension handles the configuration of network port  * numbers for use with "http" or "https". This factory   * caches the JettyHTTPServerEngines so that they may be   * retrieved if already previously configured.  */
end_comment

begin_class
specifier|public
class|class
name|JettyHTTPServerEngineFactory
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
name|Map
argument_list|<
name|Integer
argument_list|,
name|JettyHTTPServerEngine
argument_list|>
name|portMap
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|JettyHTTPServerEngine
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|ThreadingParameters
argument_list|>
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
argument_list|<
name|String
argument_list|,
name|TLSServerParameters
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * The bus.      */
specifier|private
name|Bus
name|bus
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
name|bus
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
block|}
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
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
assert|assert
name|this
operator|.
name|bus
operator|==
literal|null
operator|||
name|this
operator|.
name|bus
operator|==
name|bus
assert|;
name|this
operator|.
name|bus
operator|=
name|bus
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
annotation|@
name|PostConstruct
specifier|public
name|void
name|registerWithBus
parameter_list|()
block|{
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
block|}
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
comment|/**      * This call sets TLSParametersMap for a JettyHTTPServerEngine      *       */
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
name|put
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
comment|/**      * This call sets the ThreadingParameters for a JettyHTTPServerEngine      *       */
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
comment|/**      * This call sets TLSServerParameters for a JettyHTTPServerEngine      * that will be subsequently created. It will not alter an engine      * that has already been created for that network port.      * @param port       The network port number to bind to the engine.      * @param tlsParams  The tls server parameters. Cannot be null.      * @throws IOException       * @throws GeneralSecurityException       */
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
name|ref
operator|=
operator|new
name|JettyHTTPServerEngine
argument_list|(
name|this
argument_list|,
name|bus
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setTlsServerParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
name|portMap
operator|.
name|put
argument_list|(
name|port
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|ref
operator|.
name|finalizeConfig
argument_list|()
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
comment|/**      * This call creates a new JettyHTTPServerEngine initialized for "http"      * or "https" on the given port. The determination of "http" or "https"      * will depend on configuration of the engine's bean name.      *       * If an JettyHTTPEngine already exists, or the port      * is already in use, a BindIOException will be thrown. If the       * engine is being Spring configured for TLS a GeneralSecurityException      * may be thrown.      */
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
name|ref
operator|=
operator|new
name|JettyHTTPServerEngine
argument_list|(
name|this
argument_list|,
name|bus
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|portMap
operator|.
name|put
argument_list|(
name|port
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|ref
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * This method removes the Server Engine from the port map and stops it.      */
specifier|public
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
annotation|@
name|PostConstruct
specifier|public
name|void
name|finalizeConfig
parameter_list|()
block|{
name|registerWithBus
argument_list|()
expr_stmt|;
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
comment|//shut down the jetty server in the portMap
comment|// To avoid the CurrentModificationException,
comment|// do not use portMap.vaules directly
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
literal|0
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

