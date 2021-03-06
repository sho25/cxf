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
name|ExtensionException
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
name|bus
operator|.
name|extension
operator|.
name|TextExtensionFragmentParser
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
name|SynchronousBundleListener
import|;
end_import

begin_class
specifier|public
class|class
name|CXFExtensionBundleListener
implements|implements
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
name|CXFActivator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|long
name|id
decl_stmt|;
specifier|private
name|ConcurrentMap
argument_list|<
name|Long
argument_list|,
name|List
argument_list|<
name|OSGiExtension
argument_list|>
argument_list|>
name|extensions
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
literal|16
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
decl_stmt|;
specifier|public
name|CXFExtensionBundleListener
parameter_list|(
name|long
name|bundleId
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|bundleId
expr_stmt|;
block|}
specifier|public
name|void
name|registerExistingBundles
parameter_list|(
name|BundleContext
name|context
parameter_list|)
throws|throws
name|IOException
block|{
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
block|}
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
name|register
argument_list|(
name|event
operator|.
name|getBundle
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|protected
name|void
name|register
parameter_list|(
specifier|final
name|Bundle
name|bundle
parameter_list|)
block|{
name|Enumeration
argument_list|<
name|?
argument_list|>
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
while|while
condition|(
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Extension
argument_list|>
name|orig
init|=
operator|new
name|TextExtensionFragmentParser
argument_list|(
literal|null
argument_list|)
operator|.
name|getExtensions
argument_list|(
operator|(
name|URL
operator|)
name|e
operator|.
name|nextElement
argument_list|()
argument_list|)
decl_stmt|;
name|addExtensions
argument_list|(
name|bundle
argument_list|,
name|orig
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|addExtensions
parameter_list|(
specifier|final
name|Bundle
name|bundle
parameter_list|,
name|List
argument_list|<
name|Extension
argument_list|>
name|orig
parameter_list|)
block|{
if|if
condition|(
name|orig
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|orig
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Extension
name|ext
range|:
name|orig
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|ext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Adding the extensions from bundle "
operator|+
name|bundle
operator|.
name|getSymbolicName
argument_list|()
operator|+
literal|" ("
operator|+
name|bundle
operator|.
name|getBundleId
argument_list|()
operator|+
literal|") "
operator|+
name|names
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OSGiExtension
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
argument_list|<>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|OSGiExtension
argument_list|>
name|preList
init|=
name|extensions
operator|.
name|putIfAbsent
argument_list|(
name|bundle
operator|.
name|getBundleId
argument_list|()
argument_list|,
name|list
argument_list|)
decl_stmt|;
if|if
condition|(
name|preList
operator|!=
literal|null
condition|)
block|{
name|list
operator|=
name|preList
expr_stmt|;
block|}
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
return|return
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
return|;
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
name|OSGiExtension
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
literal|"Removing the extensions for bundle "
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
name|void
name|shutdown
parameter_list|()
block|{
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
name|Object
name|serviceObject
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
name|void
name|setServiceObject
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|serviceObject
operator|=
name|o
expr_stmt|;
name|obj
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|Object
name|load
parameter_list|(
name|ClassLoader
name|cl
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
if|if
condition|(
name|interfaceName
operator|==
literal|null
operator|&&
name|bundle
operator|.
name|getBundleContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ServiceReference
argument_list|<
name|?
argument_list|>
name|ref
init|=
name|bundle
operator|.
name|getBundleContext
argument_list|()
operator|.
name|getServiceReference
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getBundle
argument_list|()
operator|.
name|getBundleId
argument_list|()
operator|==
name|bundle
operator|.
name|getBundleId
argument_list|()
condition|)
block|{
name|Object
name|o
init|=
name|bundle
operator|.
name|getBundleContext
argument_list|()
operator|.
name|getService
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|serviceObject
operator|=
name|o
expr_stmt|;
name|obj
operator|=
name|o
expr_stmt|;
return|return
name|obj
return|;
block|}
block|}
return|return
name|super
operator|.
name|load
argument_list|(
name|cl
argument_list|,
name|b
argument_list|)
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|tryClass
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|cl
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
literal|null
decl_stmt|;
name|Throwable
name|origExc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|c
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
name|Throwable
name|e
parameter_list|)
block|{
name|origExc
operator|=
name|e
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
try|try
block|{
return|return
name|super
operator|.
name|tryClass
argument_list|(
name|name
argument_list|,
name|cl
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExtensionException
name|ee
parameter_list|)
block|{
if|if
condition|(
name|origExc
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"PROBLEM_LOADING_EXTENSION_CLASS"
argument_list|,
name|Extension
operator|.
name|LOG
argument_list|,
name|name
argument_list|)
argument_list|,
name|origExc
argument_list|)
throw|;
block|}
throw|throw
name|ee
throw|;
block|}
block|}
return|return
name|c
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
name|serviceObject
expr_stmt|;
return|return
name|ext
return|;
block|}
block|}
block|}
end_class

end_unit

