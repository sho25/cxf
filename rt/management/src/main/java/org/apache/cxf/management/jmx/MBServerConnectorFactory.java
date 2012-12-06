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
name|management
operator|.
name|jmx
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
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
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
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorServerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXServiceURL
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

begin_comment
comment|/**   * Deal with the MBeanServer Connections   *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|MBServerConnectorFactory
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_SERVICE_URL
init|=
literal|"service:jmx:rmi:///jndi/rmi://localhost:9913/jmxrmi"
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
name|MBServerConnectorFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|MBeanServer
name|server
decl_stmt|;
specifier|private
specifier|static
name|String
name|serviceUrl
init|=
name|DEFAULT_SERVICE_URL
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|environment
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|threaded
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|daemon
decl_stmt|;
specifier|private
specifier|static
name|JMXConnectorServer
name|connectorServer
decl_stmt|;
specifier|private
specifier|static
class|class
name|MBServerConnectorFactoryHolder
block|{
specifier|private
specifier|static
specifier|final
name|MBServerConnectorFactory
name|INSTANCE
init|=
operator|new
name|MBServerConnectorFactory
argument_list|()
decl_stmt|;
block|}
specifier|private
specifier|static
class|class
name|MBeanServerHolder
block|{
specifier|private
specifier|static
specifier|final
name|MBeanServer
name|INSTANCE
init|=
name|MBeanServerFactory
operator|.
name|createMBeanServer
argument_list|()
decl_stmt|;
block|}
specifier|private
name|MBServerConnectorFactory
parameter_list|()
block|{              }
specifier|private
name|int
name|getURLLocalHostPort
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|int
name|portStart
init|=
name|url
operator|.
name|indexOf
argument_list|(
literal|"localhost"
argument_list|)
operator|+
literal|10
decl_stmt|;
name|int
name|portEnd
decl_stmt|;
name|int
name|port
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|portStart
operator|>
literal|0
condition|)
block|{
name|portEnd
operator|=
name|indexNotOfNumber
argument_list|(
name|url
argument_list|,
name|portStart
argument_list|)
expr_stmt|;
if|if
condition|(
name|portEnd
operator|>
name|portStart
condition|)
block|{
specifier|final
name|String
name|portString
init|=
name|url
operator|.
name|substring
argument_list|(
name|portStart
argument_list|,
name|portEnd
argument_list|)
decl_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|portString
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|port
return|;
block|}
specifier|private
specifier|static
name|int
name|indexNotOfNumber
parameter_list|(
name|String
name|str
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|i
operator|=
name|index
init|;
name|i
operator|<
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|<
literal|'0'
operator|||
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|>
literal|'9'
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|public
specifier|static
name|MBServerConnectorFactory
name|getInstance
parameter_list|()
block|{
return|return
name|MBServerConnectorFactoryHolder
operator|.
name|INSTANCE
return|;
block|}
specifier|public
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|ms
parameter_list|)
block|{
name|server
operator|=
name|ms
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|serviceUrl
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|env
parameter_list|)
block|{
name|environment
operator|=
name|env
expr_stmt|;
block|}
specifier|public
name|void
name|setThreaded
parameter_list|(
name|boolean
name|fthread
parameter_list|)
block|{
name|threaded
operator|=
name|fthread
expr_stmt|;
block|}
specifier|public
name|void
name|setDaemon
parameter_list|(
name|boolean
name|fdaemon
parameter_list|)
block|{
name|daemon
operator|=
name|fdaemon
expr_stmt|;
block|}
specifier|public
name|void
name|createConnector
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
name|server
operator|=
name|MBeanServerHolder
operator|.
name|INSTANCE
expr_stmt|;
block|}
comment|// Create the JMX service URL.
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
name|serviceUrl
argument_list|)
decl_stmt|;
comment|// if the URL is localhost, start up an Registry
if|if
condition|(
name|serviceUrl
operator|.
name|indexOf
argument_list|(
literal|"localhost"
argument_list|)
operator|>
operator|-
literal|1
operator|&&
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|compareToIgnoreCase
argument_list|(
literal|"rmi"
argument_list|)
operator|==
literal|0
condition|)
block|{
try|try
block|{
name|int
name|port
init|=
name|getURLLocalHostPort
argument_list|(
name|serviceUrl
argument_list|)
decl_stmt|;
try|try
block|{
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// the registry may had been created
name|LocateRegistry
operator|.
name|getRegistry
argument_list|(
name|port
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
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"CREATE_REGISTRY_FAULT_MSG"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|ex
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Create the connector server now.
name|connectorServer
operator|=
name|JMXConnectorServerFactory
operator|.
name|newJMXConnectorServer
argument_list|(
name|url
argument_list|,
name|environment
argument_list|,
name|server
argument_list|)
expr_stmt|;
if|if
condition|(
name|threaded
condition|)
block|{
comment|// Start the connector server asynchronously (in a separate thread).
name|Thread
name|connectorThread
init|=
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|connectorServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
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
literal|"START_CONNECTOR_FAILURE_MSG"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|ex
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|connectorThread
operator|.
name|setName
argument_list|(
literal|"JMX Connector Thread ["
operator|+
name|serviceUrl
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|connectorThread
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
name|connectorThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Start the connector server in the same thread.
name|connectorServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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
name|LOG
operator|.
name|info
argument_list|(
literal|"JMX connector server started: "
operator|+
name|connectorServer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|IOException
block|{
name|connectorServer
operator|.
name|stop
argument_list|()
expr_stmt|;
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
name|LOG
operator|.
name|info
argument_list|(
literal|"JMX connector server stopped: "
operator|+
name|connectorServer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

