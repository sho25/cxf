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
name|bus
operator|.
name|osgi
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
name|io
operator|.
name|InputStream
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|Properties
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
name|ConcurrentMap
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
name|CopyOnWriteArrayList
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
name|extension
operator|.
name|Extension
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
name|extension
operator|.
name|ExtensionFragmentParser
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
name|extension
operator|.
name|ExtensionRegistry
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
name|workqueue
operator|.
name|AutomaticWorkQueueImpl
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
name|workqueue
operator|.
name|WorkQueueManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Bundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|SynchronousBundleListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|cm
operator|.
name|ManagedService
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|OSGiExtensionLocator
implements|implements
name|BundleActivator
implements|,
name|SynchronousBundleListener
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
name|OSGiExtensionLocator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|Long
argument_list|,
name|List
argument_list|<
name|Extension
argument_list|>
argument_list|>
name|extensions
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Long
argument_list|,
name|List
argument_list|<
name|Extension
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|private
name|Extension
name|listener
decl_stmt|;
specifier|static
class|class
name|WorkQueueList
implements|implements
name|ManagedService
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|OSGiAutomaticWorkQueue
argument_list|>
name|queues
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|OSGiAutomaticWorkQueue
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|updated
parameter_list|(
name|Dictionary
name|d
parameter_list|)
throws|throws
name|ConfigurationException
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|d
operator|.
name|get
argument_list|(
literal|"org.apache.cxf.workqueue.names"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|String
name|s2
index|[]
init|=
name|s
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|s2
control|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|queues
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|queues
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|updated
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|OSGiAutomaticWorkQueue
name|wq
init|=
operator|new
name|OSGiAutomaticWorkQueue
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|wq
operator|.
name|updated
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|wq
operator|.
name|setShared
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|queues
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|wq
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
empty_stmt|;
specifier|private
name|WorkQueueList
name|workQueues
init|=
operator|new
name|WorkQueueList
argument_list|()
decl_stmt|;
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|bundleChanged
parameter_list|(
name|BundleEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|RESOLVED
operator|&&
name|id
operator|!=
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
condition|)
block|{
try|try
block|{
name|register
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNRESOLVED
operator|||
name|event
operator|.
name|getType
argument_list|()
operator|==
name|BundleEvent
operator|.
name|UNINSTALLED
condition|)
block|{
name|unregister
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|addBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|id
operator|=
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
expr_stmt|;
name|registerBusListener
argument_list|()
expr_stmt|;
for|for
control|(
name|Bundle
name|bundle
range|:
name|context
operator|.
name|getBundles
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|RESOLVED
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|STARTING
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|ACTIVE
operator|||
name|bundle
operator|.
name|getState
argument_list|()
operator|==
name|Bundle
operator|.
name|STOPPING
operator|)
operator|&&
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|!=
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
condition|)
block|{
name|register
argument_list|(
name|bundle
argument_list|)
expr_stmt|;
block|}
block|}
name|ServiceReference
name|configAdminServiceRef
init|=
name|context
operator|.
name|getServiceReference
argument_list|(
name|ConfigurationAdmin
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|configAdminServiceRef
operator|!=
literal|null
condition|)
block|{
name|ConfigurationAdmin
name|configAdmin
init|=
operator|(
name|ConfigurationAdmin
operator|)
name|context
operator|.
name|getService
argument_list|(
name|configAdminServiceRef
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|configAdmin
operator|.
name|getConfiguration
argument_list|(
literal|"org.apache.cxf.workqueues"
argument_list|)
decl_stmt|;
name|Dictionary
name|d
init|=
name|config
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SERVICE_PID
argument_list|,
literal|"org.apache.cxf.workqueues"
argument_list|)
expr_stmt|;
name|context
operator|.
name|registerService
argument_list|(
name|ManagedService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|workQueues
argument_list|,
name|props
argument_list|)
expr_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
name|workQueues
operator|.
name|updated
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
name|Extension
name|ext
init|=
operator|new
name|Extension
argument_list|(
name|WorkQueueList
operator|.
name|class
argument_list|)
block|{
specifier|public
name|Object
name|getLoadedObject
parameter_list|()
block|{
return|return
name|workQueues
return|;
block|}
specifier|public
name|Extension
name|cloneNoObject
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
decl_stmt|;
name|ExtensionRegistry
operator|.
name|addExtensions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|ext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|context
operator|.
name|removeBundleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|unregisterBusListener
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|extensions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|unregister
argument_list|(
name|extensions
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|AutomaticWorkQueueImpl
name|wq
range|:
name|workQueues
operator|.
name|queues
operator|.
name|values
argument_list|()
control|)
block|{
name|wq
operator|.
name|setShared
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|wq
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|workQueues
operator|.
name|queues
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|registerBusListener
parameter_list|()
block|{
name|listener
operator|=
operator|new
name|Extension
argument_list|(
name|OSGIBusListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|ExtensionRegistry
operator|.
name|addExtensions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|unregisterBusListener
parameter_list|()
block|{
name|ExtensionRegistry
operator|.
name|removeExtensions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
name|listener
operator|=
literal|null
expr_stmt|;
block|}
specifier|protected
name|void
name|register
parameter_list|(
specifier|final
name|Bundle
name|bundle
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Extension
argument_list|>
name|list
init|=
name|extensions
operator|.
name|get
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
decl_stmt|;
name|Enumeration
name|e
init|=
name|bundle
operator|.
name|findEntries
argument_list|(
literal|"META-INF/cxf/"
argument_list|,
literal|"bus-extensions.txt"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
specifier|final
name|URL
name|u
init|=
operator|(
name|URL
operator|)
name|e
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|InputStream
name|ins
init|=
name|u
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Extension
argument_list|>
name|orig
init|=
operator|new
name|ExtensionFragmentParser
argument_list|()
operator|.
name|getExtensionsFromText
argument_list|(
name|ins
argument_list|)
decl_stmt|;
name|ins
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Loading the extension from bundle "
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|orig
operator|!=
literal|null
operator|&&
operator|!
name|orig
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|Extension
argument_list|>
argument_list|()
expr_stmt|;
name|extensions
operator|.
name|put
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Extension
name|ext
range|:
name|orig
control|)
block|{
name|list
operator|.
name|add
argument_list|(
operator|new
name|OSGiExtension
argument_list|(
name|ext
argument_list|,
name|bundle
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ExtensionRegistry
operator|.
name|addExtensions
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|unregister
parameter_list|(
specifier|final
name|long
name|bundleId
parameter_list|)
block|{
name|List
argument_list|<
name|Extension
argument_list|>
name|list
init|=
name|extensions
operator|.
name|remove
argument_list|(
name|bundleId
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Removed the extensions for bundle "
operator|+
name|bundleId
argument_list|)
expr_stmt|;
name|ExtensionRegistry
operator|.
name|removeExtensions
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|OSGIBusListener
implements|implements
name|BusLifeCycleListener
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_SYMBOLIC_NAME_PROPERTY
init|=
literal|"cxf.context.symbolicname"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_VERSION_PROPERTY
init|=
literal|"cxf.context.version"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_NAME_PROPERTY
init|=
literal|"cxf.bus.id"
decl_stmt|;
name|Bus
name|bus
decl_stmt|;
name|ServiceRegistration
name|service
decl_stmt|;
specifier|public
name|OSGIBusListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Version
name|getBundleVersion
parameter_list|(
name|Bundle
name|bundle
parameter_list|)
block|{
name|Dictionary
name|headers
init|=
name|bundle
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|String
name|version
init|=
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|BUNDLE_VERSION
argument_list|)
decl_stmt|;
return|return
operator|(
name|version
operator|!=
literal|null
operator|)
condition|?
name|Version
operator|.
name|parseVersion
argument_list|(
name|version
argument_list|)
else|:
name|Version
operator|.
name|emptyVersion
return|;
block|}
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
name|WorkQueueManager
name|m
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
name|WorkQueueList
name|l
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueList
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|!=
literal|null
operator|&&
name|m
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AutomaticWorkQueueImpl
name|wq
range|:
name|l
operator|.
name|queues
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getNamedWorkQueue
argument_list|(
name|wq
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|m
operator|.
name|addNamedWorkQueue
argument_list|(
name|wq
operator|.
name|getName
argument_list|()
argument_list|,
name|wq
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|BundleContext
name|context
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BundleContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_SYMBOLIC_NAME_PROPERTY
argument_list|,
name|context
operator|.
name|getBundle
argument_list|()
operator|.
name|getSymbolicName
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_VERSION_PROPERTY
argument_list|,
name|getBundleVersion
argument_list|(
name|context
operator|.
name|getBundle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CONTEXT_NAME_PROPERTY
argument_list|,
name|bus
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|service
operator|=
name|context
operator|.
name|registerService
argument_list|(
name|Bus
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|bus
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{         }
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|unregister
argument_list|()
expr_stmt|;
name|service
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
specifier|public
class|class
name|OSGiExtension
extends|extends
name|Extension
block|{
specifier|final
name|Bundle
name|bundle
decl_stmt|;
specifier|public
name|OSGiExtension
parameter_list|(
name|Extension
name|e
parameter_list|,
name|Bundle
name|b
parameter_list|)
block|{
name|super
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|bundle
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getClassObject
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
if|if
condition|(
name|clazz
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|clazz
operator|=
name|bundle
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|//ignore, fall to super
block|}
block|}
return|return
name|super
operator|.
name|getClassObject
argument_list|(
name|cl
argument_list|)
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadInterface
parameter_list|(
name|ClassLoader
name|cl
parameter_list|)
block|{
try|try
block|{
return|return
name|bundle
operator|.
name|loadClass
argument_list|(
name|interfaceName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|//ignore, fall to super
block|}
return|return
name|super
operator|.
name|loadInterface
argument_list|(
name|cl
argument_list|)
return|;
block|}
specifier|public
name|Extension
name|cloneNoObject
parameter_list|()
block|{
name|OSGiExtension
name|ext
init|=
operator|new
name|OSGiExtension
argument_list|(
name|this
argument_list|,
name|bundle
argument_list|)
decl_stmt|;
name|ext
operator|.
name|obj
operator|=
literal|null
expr_stmt|;
return|return
name|ext
return|;
block|}
block|}
block|}
end_class

end_unit

