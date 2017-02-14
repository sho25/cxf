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
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
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
name|HashSet
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
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|InstanceAlreadyExistsException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
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
name|MBeanServerDelegate
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
name|MalformedObjectNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|NotCompliantMBeanException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectInstance
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
name|modelmbean
operator|.
name|InvalidTargetObjectTypeException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|ModelMBeanInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|modelmbean
operator|.
name|RequiredModelMBean
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
name|bus
operator|.
name|ManagedBus
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
name|InstrumentationManager
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
name|ManagedComponent
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
name|jmx
operator|.
name|export
operator|.
name|runtime
operator|.
name|ModelMBeanAssembler
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
name|jmx
operator|.
name|type
operator|.
name|JMXConnectorPolicyType
import|;
end_import

begin_comment
comment|/**  * The manager class for the JMXManagedComponent which hosts the JMXManagedComponents.  */
end_comment

begin_class
specifier|public
class|class
name|InstrumentationManagerImpl
extends|extends
name|JMXConnectorPolicyType
implements|implements
name|InstrumentationManager
implements|,
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
name|InstrumentationManagerImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mbeanServerIDMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|MBServerConnectorFactory
name|mcf
decl_stmt|;
specifier|private
name|MBeanServer
name|mbs
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|busMBeans
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|connectFailed
decl_stmt|;
specifier|private
name|String
name|persistentBusId
decl_stmt|;
comment|/**      * For backward compatibility, {@link #createMBServerConnectorFactory} is<code>true</code> by default.      */
specifier|private
name|boolean
name|createMBServerConnectorFactory
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|mbeanServerName
init|=
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
decl_stmt|;
specifier|private
name|boolean
name|usePlatformMBeanServer
decl_stmt|;
specifier|public
name|InstrumentationManagerImpl
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|InstrumentationManagerImpl
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|readJMXProperties
argument_list|(
name|bus
argument_list|)
expr_stmt|;
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
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|bus
operator|==
literal|null
condition|)
block|{
name|readJMXProperties
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// possibly this bus was reassigned from another im bean
name|InstrumentationManager
name|im
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|InstrumentationManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|!=
name|im
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
try|try
block|{
name|ManagedBus
name|mbus
init|=
operator|new
name|ManagedBus
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|im
operator|.
name|unregister
argument_list|(
name|mbus
argument_list|)
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
literal|"unregistered "
operator|+
name|mbus
operator|.
name|getObjectName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
block|}
specifier|public
name|void
name|setServerName
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|mbeanServerName
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|void
name|setCreateMBServerConnectorFactory
parameter_list|(
name|boolean
name|createMBServerConnectorFactory
parameter_list|)
block|{
name|this
operator|.
name|createMBServerConnectorFactory
operator|=
name|createMBServerConnectorFactory
expr_stmt|;
block|}
specifier|public
name|void
name|setUsePlatformMBeanServer
parameter_list|(
name|Boolean
name|flag
parameter_list|)
block|{
name|usePlatformMBeanServer
operator|=
name|flag
expr_stmt|;
block|}
annotation|@
name|Deprecated
specifier|public
name|void
name|register
parameter_list|()
block|{     }
annotation|@
name|PostConstruct
specifier|public
name|void
name|init
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
name|MBeanServer
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|enabled
operator|=
literal|true
expr_stmt|;
name|createMBServerConnectorFactory
operator|=
literal|false
expr_stmt|;
name|mbs
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|MBeanServer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|mbs
operator|==
literal|null
condition|)
block|{
comment|// return platform mbean server if the option is specified.
if|if
condition|(
name|usePlatformMBeanServer
condition|)
block|{
name|mbs
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|mbeanServerID
init|=
name|mbeanServerIDMap
operator|.
name|get
argument_list|(
name|mbeanServerName
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|servers
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mbeanServerID
operator|!=
literal|null
condition|)
block|{
name|servers
operator|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
name|mbeanServerID
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|servers
operator|==
literal|null
operator|||
name|servers
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|mbs
operator|=
name|MBeanServerFactory
operator|.
name|createMBeanServer
argument_list|(
name|mbeanServerName
argument_list|)
expr_stmt|;
try|try
block|{
name|mbeanServerID
operator|=
operator|(
name|String
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|getDelegateName
argument_list|()
argument_list|,
literal|"MBeanServerId"
argument_list|)
expr_stmt|;
name|mbeanServerIDMap
operator|.
name|put
argument_list|(
name|mbeanServerName
argument_list|,
name|mbeanServerID
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
else|else
block|{
name|mbs
operator|=
name|servers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|createMBServerConnectorFactory
condition|)
block|{
name|mcf
operator|=
name|MBServerConnectorFactory
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|mcf
operator|.
name|setMBeanServer
argument_list|(
name|mbs
argument_list|)
expr_stmt|;
name|mcf
operator|.
name|setThreaded
argument_list|(
name|isThreaded
argument_list|()
argument_list|)
expr_stmt|;
name|mcf
operator|.
name|setDaemon
argument_list|(
name|isDaemon
argument_list|()
argument_list|)
expr_stmt|;
name|mcf
operator|.
name|setServiceUrl
argument_list|(
name|getJMXServiceURL
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|mcf
operator|.
name|createConnector
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|connectFailed
operator|=
literal|true
expr_stmt|;
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
if|if
condition|(
operator|!
name|connectFailed
operator|&&
literal|null
operator|!=
name|bus
condition|)
block|{
try|try
block|{
comment|//Register Bus here since we can guarantee that Instrumentation
comment|//infrastructure has been initialized.
name|ManagedBus
name|mbus
init|=
operator|new
name|ManagedBus
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|register
argument_list|(
name|mbus
argument_list|)
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
literal|"registered "
operator|+
name|mbus
operator|.
name|getObjectName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
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
literal|"REGISTER_FAILURE_MSG"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|bus
block|,
name|jmex
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|InstrumentationManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|BusLifeCycleManager
name|blcm
init|=
name|bus
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
literal|null
operator|!=
name|blcm
condition|)
block|{
name|blcm
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|ObjectName
name|getDelegateName
parameter_list|()
throws|throws
name|JMException
block|{
try|try
block|{
return|return
operator|(
name|ObjectName
operator|)
name|MBeanServerDelegate
operator|.
name|class
operator|.
name|getField
argument_list|(
literal|"DELEGATE_NAME"
argument_list|)
operator|.
name|get
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore, likely on Java5
block|}
try|try
block|{
return|return
operator|new
name|ObjectName
argument_list|(
literal|"JMImplementation:type=MBeanServerDelegate"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|MalformedObjectNameException
name|e
parameter_list|)
block|{
name|JMException
name|jme
init|=
operator|new
name|JMException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
name|jme
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|jme
throw|;
block|}
block|}
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
block|{
name|register
argument_list|(
name|obj
argument_list|,
name|name
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
block|{
if|if
condition|(
operator|!
name|isEnabled
argument_list|()
operator|||
name|connectFailed
condition|)
block|{
return|return;
block|}
comment|//Try to register as a Standard MBean
try|try
block|{
name|registerMBeanWithServer
argument_list|(
name|obj
argument_list|,
name|persist
argument_list|(
name|name
argument_list|)
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NotCompliantMBeanException
name|e
parameter_list|)
block|{
comment|//If this is not a "normal" MBean, then try to deploy it using JMX annotations
name|ModelMBeanAssembler
name|assembler
init|=
operator|new
name|ModelMBeanAssembler
argument_list|()
decl_stmt|;
name|ModelMBeanInfo
name|mbi
init|=
name|assembler
operator|.
name|getModelMbeanInfo
argument_list|(
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|register
argument_list|(
name|obj
argument_list|,
name|name
argument_list|,
name|mbi
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ObjectName
name|register
parameter_list|(
name|ManagedComponent
name|i
parameter_list|)
throws|throws
name|JMException
block|{
name|ObjectName
name|name
init|=
name|register
argument_list|(
name|i
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|name
return|;
block|}
specifier|public
name|ObjectName
name|register
parameter_list|(
name|ManagedComponent
name|i
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
block|{
name|ObjectName
name|name
init|=
name|i
operator|.
name|getObjectName
argument_list|()
decl_stmt|;
name|register
argument_list|(
name|i
argument_list|,
name|name
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
return|return
name|name
return|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|ManagedComponent
name|component
parameter_list|)
throws|throws
name|JMException
block|{
name|ObjectName
name|name
init|=
name|component
operator|.
name|getObjectName
argument_list|()
decl_stmt|;
name|unregister
argument_list|(
name|persist
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
block|{
if|if
condition|(
operator|!
name|isEnabled
argument_list|()
operator|||
name|connectFailed
condition|)
block|{
return|return;
block|}
name|busMBeans
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|mbs
operator|.
name|unregisterMBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|mbs
return|;
block|}
specifier|public
name|void
name|setServer
parameter_list|(
name|MBeanServer
name|server
parameter_list|)
block|{
name|this
operator|.
name|mbs
operator|=
name|server
expr_stmt|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isEnabled
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|mcf
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|mcf
operator|.
name|destroy
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
literal|"STOP_CONNECTOR_FAILURE_MSG"
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
comment|//Using the array to hold the busMBeans to avoid the CurrentModificationException
name|Object
index|[]
name|mBeans
init|=
name|busMBeans
operator|.
name|toArray
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|name
range|:
name|mBeans
control|)
block|{
name|busMBeans
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|unregister
argument_list|(
operator|(
name|ObjectName
operator|)
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMException
name|jmex
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
literal|"UNREGISTER_FAILURE_MSG"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|name
block|,
name|jmex
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{      }
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{      }
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
name|this
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|ModelMBeanInfo
name|mbi
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
block|{
name|RequiredModelMBean
name|rtMBean
init|=
operator|(
name|RequiredModelMBean
operator|)
name|mbs
operator|.
name|instantiate
argument_list|(
literal|"javax.management.modelmbean.RequiredModelMBean"
argument_list|)
decl_stmt|;
name|rtMBean
operator|.
name|setModelMBeanInfo
argument_list|(
name|mbi
argument_list|)
expr_stmt|;
try|try
block|{
name|rtMBean
operator|.
name|setManagedResource
argument_list|(
name|obj
argument_list|,
literal|"ObjectReference"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidTargetObjectTypeException
name|itotex
parameter_list|)
block|{
throw|throw
operator|new
name|JMException
argument_list|(
name|itotex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|registerMBeanWithServer
argument_list|(
name|rtMBean
argument_list|,
name|persist
argument_list|(
name|name
argument_list|)
argument_list|,
name|forceRegistration
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|registerMBeanWithServer
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
block|{
name|ObjectInstance
name|instance
init|=
literal|null
decl_stmt|;
try|try
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
name|LOG
operator|.
name|info
argument_list|(
literal|"registering MBean "
operator|+
name|name
operator|+
literal|": "
operator|+
name|obj
argument_list|)
expr_stmt|;
block|}
name|instance
operator|=
name|mbs
operator|.
name|registerMBean
argument_list|(
name|obj
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstanceAlreadyExistsException
name|e
parameter_list|)
block|{
if|if
condition|(
name|forceRegistration
condition|)
block|{
name|mbs
operator|.
name|unregisterMBean
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|instance
operator|=
name|mbs
operator|.
name|registerMBean
argument_list|(
name|obj
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
name|busMBeans
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|getPersistentBusId
parameter_list|()
block|{
return|return
name|persistentBusId
return|;
block|}
specifier|public
name|void
name|setPersistentBusId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|persistentBusId
operator|=
name|sanitize
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ObjectName
name|persist
parameter_list|(
name|ObjectName
name|original
parameter_list|)
throws|throws
name|JMException
block|{
name|ObjectName
name|persisted
init|=
name|original
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|persistentBusId
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|persistentBusId
argument_list|)
operator|||
name|persistentBusId
operator|.
name|startsWith
argument_list|(
literal|"${"
argument_list|)
operator|)
condition|)
block|{
name|String
name|originalStr
init|=
name|original
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|originalStr
operator|.
name|indexOf
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|persistedStr
init|=
name|originalStr
operator|.
name|replaceFirst
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
operator|+
literal|"="
operator|+
name|bus
operator|.
name|getId
argument_list|()
argument_list|,
name|ManagementConstants
operator|.
name|BUS_ID_PROP
operator|+
literal|"="
operator|+
name|persistentBusId
argument_list|)
decl_stmt|;
name|persisted
operator|=
operator|new
name|ObjectName
argument_list|(
name|persistedStr
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|persisted
return|;
block|}
specifier|private
name|String
name|sanitize
parameter_list|(
name|String
name|in
parameter_list|)
block|{
name|StringBuilder
name|str
init|=
operator|new
name|StringBuilder
argument_list|(
name|in
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|in
operator|.
name|length
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|char
name|ch
init|=
name|in
operator|.
name|charAt
argument_list|(
name|x
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|ch
condition|)
block|{
case|case
literal|':'
case|:
case|case
literal|'/'
case|:
case|case
literal|'\\'
case|:
case|case
literal|'?'
case|:
case|case
literal|'='
case|:
case|case
literal|','
case|:
name|str
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
break|break;
default|default:
name|str
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|str
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|readJMXProperties
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|!=
literal|null
condition|)
block|{
name|persistentBusId
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.persistentBusId"
argument_list|,
name|persistentBusId
argument_list|)
expr_stmt|;
name|mbeanServerName
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.serverName"
argument_list|,
name|mbeanServerName
argument_list|)
expr_stmt|;
name|usePlatformMBeanServer
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.usePlatformMBeanServer"
argument_list|,
name|usePlatformMBeanServer
argument_list|)
expr_stmt|;
name|createMBServerConnectorFactory
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.createMBServerConnectorFactory"
argument_list|,
name|createMBServerConnectorFactory
argument_list|)
expr_stmt|;
name|daemon
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.daemon"
argument_list|,
name|daemon
argument_list|)
expr_stmt|;
name|threaded
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.threaded"
argument_list|,
name|threaded
argument_list|)
expr_stmt|;
name|enabled
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.enabled"
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
name|jmxServiceURL
operator|=
name|getBusProperty
argument_list|(
name|b
argument_list|,
literal|"bus.jmx.JMXServiceURL"
argument_list|,
name|jmxServiceURL
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
name|getBusProperty
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|dflt
parameter_list|)
block|{
name|String
name|v
init|=
operator|(
name|String
operator|)
name|b
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|v
operator|!=
literal|null
condition|?
name|v
else|:
name|dflt
return|;
block|}
specifier|private
specifier|static
name|boolean
name|getBusProperty
parameter_list|(
name|Bus
name|b
parameter_list|,
name|String
name|key
parameter_list|,
name|boolean
name|dflt
parameter_list|)
block|{
name|Object
name|v
init|=
name|b
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
operator|instanceof
name|Boolean
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|v
return|;
block|}
return|return
name|v
operator|!=
literal|null
condition|?
name|Boolean
operator|.
name|valueOf
argument_list|(
name|v
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|dflt
return|;
block|}
block|}
end_class

end_unit

