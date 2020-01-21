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
name|utils
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|MBeanAttributeInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|JMXConnector
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
name|JMXConnectorFactory
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
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
name|ManagementConstants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ManagementConsole
block|{
specifier|private
specifier|static
name|MBeanServerConnection
name|mbsc
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_JMXSERVICE_URL
init|=
literal|"service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"
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
name|ManagementConsole
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|jmxServerURL
decl_stmt|;
name|String
name|portName
decl_stmt|;
name|String
name|serviceName
decl_stmt|;
name|String
name|operationName
decl_stmt|;
name|ManagementConsole
parameter_list|()
block|{      }
specifier|public
name|void
name|getManagedObjectAttributes
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|mbsc
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"NO_MBEAN_SERVER"
argument_list|)
expr_stmt|;
return|return;
block|}
name|MBeanInfo
name|info
init|=
name|mbsc
operator|.
name|getMBeanInfo
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|MBeanAttributeInfo
index|[]
name|attrs
init|=
name|info
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attrs
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|attrs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|attrs
index|[
name|i
index|]
operator|.
name|isReadable
argument_list|()
condition|)
block|{
try|try
block|{
name|Object
name|o
init|=
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
name|attrs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\t"
operator|+
name|attrs
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|+
literal|" = "
operator|+
name|o
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
block|}
name|void
name|connectToMBserver
parameter_list|()
throws|throws
name|IOException
block|{
name|jmxServerURL
operator|=
name|jmxServerURL
operator|==
literal|null
condition|?
name|DEFAULT_JMXSERVICE_URL
else|:
name|jmxServerURL
expr_stmt|;
name|JMXServiceURL
name|url
init|=
operator|new
name|JMXServiceURL
argument_list|(
name|jmxServerURL
argument_list|)
decl_stmt|;
name|JMXConnector
name|jmxc
init|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|mbsc
operator|=
name|jmxc
operator|.
name|getMBeanServerConnection
argument_list|()
expr_stmt|;
block|}
name|void
name|listAllManagedEndpoint
parameter_list|()
block|{
try|try
block|{
name|ObjectName
name|queryEndpointName
init|=
operator|new
name|ObjectName
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
operator|+
literal|":type=Bus.Service.Endpoint,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|endpointNames
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
name|queryEndpointName
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The endpoints are : "
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectName
name|oName
range|:
name|endpointNames
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|oName
argument_list|)
expr_stmt|;
name|getManagedObjectAttributes
argument_list|(
name|oName
argument_list|)
expr_stmt|;
block|}
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
literal|"FAIL_TO_LIST_ENDPOINTS"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|ObjectName
name|getEndpointObjectName
parameter_list|()
throws|throws
name|MalformedObjectNameException
throws|,
name|NullPointerException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
argument_list|)
operator|.
name|append
argument_list|(
literal|":type=Bus.Service.Endpoint,"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|SERVICE_NAME_PROP
operator|+
literal|"=\""
operator|+
name|serviceName
operator|+
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|PORT_NAME_PROP
operator|+
literal|"=\""
operator|+
name|portName
operator|+
literal|"\",*"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|invokeEndpoint
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|ObjectName
name|endpointName
init|=
literal|null
decl_stmt|;
name|ObjectName
name|queryEndpointName
decl_stmt|;
try|try
block|{
name|queryEndpointName
operator|=
name|getEndpointObjectName
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|endpointNames
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|mbsc
operator|.
name|queryNames
argument_list|(
name|queryEndpointName
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
comment|// now get the ObjectName with the busId
name|Iterator
argument_list|<
name|ObjectName
argument_list|>
name|it
init|=
name|endpointNames
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// only deal with the first endpoint object which retrun from the list.
name|endpointName
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
name|mbsc
operator|.
name|invoke
argument_list|(
name|endpointName
argument_list|,
name|operation
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"invoke endpoint "
operator|+
name|endpointName
operator|+
literal|" operation "
operator|+
name|operation
operator|+
literal|" succeed!"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|endpointName
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"FAIL_TO_CREATE_ENDPOINT_OBEJCTNAME"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|e
block|}
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
name|SEVERE
argument_list|,
literal|"FAIL_TO_INVOKE_MANAGED_OBJECT_OPERATION"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|endpointName
block|,
name|operation
block|,
name|e
operator|.
name|toString
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|startEndpoint
parameter_list|()
block|{
name|invokeEndpoint
argument_list|(
literal|"start"
argument_list|)
expr_stmt|;
block|}
name|void
name|stopEndpoint
parameter_list|()
block|{
name|invokeEndpoint
argument_list|(
literal|"stop"
argument_list|)
expr_stmt|;
block|}
name|void
name|restartEndpoint
parameter_list|()
block|{
name|invokeEndpoint
argument_list|(
literal|"stop"
argument_list|)
expr_stmt|;
name|invokeEndpoint
argument_list|(
literal|"start"
argument_list|)
expr_stmt|;
block|}
name|boolean
name|parserArguments
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|portName
operator|=
literal|""
expr_stmt|;
name|serviceName
operator|=
literal|""
expr_stmt|;
name|operationName
operator|=
literal|""
expr_stmt|;
name|boolean
name|result
init|=
literal|false
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|arg
init|=
name|args
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
literal|"--port"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
operator|||
literal|"-p"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|portName
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"--service"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
operator|||
literal|"-s"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|serviceName
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"--jmx"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
operator|||
literal|"-j"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|jmxServerURL
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"--operation"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
operator|||
literal|"-o"
operator|.
name|equals
argument_list|(
name|arg
argument_list|)
condition|)
block|{
name|operationName
operator|=
name|args
index|[
operator|++
name|i
index|]
expr_stmt|;
comment|// it is the key option
name|result
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// can't parse the argument rightly
return|return
literal|false
return|;
block|}
return|return
name|result
return|;
block|}
specifier|private
specifier|static
name|void
name|printUsage
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Management Console for CXF Managed Endpoints"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"You can start and stop the endpoints which export as JMX managed objects"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Usage: -o list "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"       -o {start|stop|restart} -p PORTQNAME -s SERVICEQNAME "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Valid options:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" -o [--operation] {list|start|stop|restart}  call the managed endpoint "
operator|+
literal|"operation"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                          list: show all the managed endpoints' objectNames and"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                                attributes"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                          start: start the endpoint with the -p and -s "
operator|+
literal|"arguments"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                          stop: stop the endpoint with the -p and -s arguments"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                          restart: restart the endpoint with the -p and -s "
operator|+
literal|"arguments"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" -p [--port] arg          ARG: the port Qname of the managed endpoint"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" -s [--service] arg       ARG: the service Qname of the managed endpoint"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" -j [--jmx] arg           ARG: the JMXServerURL for connecting to the mbean "
operator|+
literal|"server"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                           if not using this option, the JMXServerURL will be "
operator|+
literal|"set as"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"                           \"service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi"
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doManagement
parameter_list|()
block|{
try|try
block|{
name|connectToMBserver
argument_list|()
expr_stmt|;
if|if
condition|(
literal|"list"
operator|.
name|equalsIgnoreCase
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|listAllManagedEndpoint
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"start"
operator|.
name|equalsIgnoreCase
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|startEndpoint
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"stop"
operator|.
name|equalsIgnoreCase
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|stopEndpoint
argument_list|()
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"restart"
operator|.
name|equalsIgnoreCase
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|restartEndpoint
argument_list|()
expr_stmt|;
return|return;
block|}
name|printUsage
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
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
literal|"FAIL_TO_CONNECT_TO_MBEAN_SERVER"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|jmxServerURL
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param args      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|ManagementConsole
name|mc
init|=
operator|new
name|ManagementConsole
argument_list|()
decl_stmt|;
if|if
condition|(
name|mc
operator|.
name|parserArguments
argument_list|(
name|args
argument_list|)
condition|)
block|{
name|mc
operator|.
name|doManagement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|printUsage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

