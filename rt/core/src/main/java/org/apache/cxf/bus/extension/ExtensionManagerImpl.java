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
name|extension
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Collection
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|common
operator|.
name|injection
operator|.
name|ResourceInjector
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
name|Configurer
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
name|resource
operator|.
name|ObjectTypeResolver
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
name|resource
operator|.
name|ResourceManager
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
name|resource
operator|.
name|ResourceResolver
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
name|resource
operator|.
name|SinglePropertyResolver
import|;
end_import

begin_class
specifier|public
class|class
name|ExtensionManagerImpl
implements|implements
name|ExtensionManager
block|{
specifier|public
specifier|static
specifier|final
name|String
name|EXTENSIONMANAGER_PROPERTY_NAME
init|=
literal|"extensionManager"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTIVATION_NAMESPACES_PROPERTY_NAME
init|=
literal|"activationNamespaces"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTIVATION_NAMESPACES_SETTER_METHOD_NAME
init|=
literal|"setActivationNamespaces"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BUS_EXTENSION_RESOURCE_COMPAT
init|=
literal|"META-INF/bus-extensions.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BUS_EXTENSION_RESOURCE
init|=
literal|"META-INF/cxf/bus-extensions.xml"
decl_stmt|;
specifier|private
specifier|final
name|ClassLoader
name|loader
decl_stmt|;
specifier|private
name|ResourceManager
name|resourceManager
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Extension
argument_list|>
argument_list|>
name|deferred
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|activated
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
argument_list|>
name|namespaced
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Bus
name|bus
decl_stmt|;
specifier|public
name|ExtensionManagerImpl
parameter_list|(
name|ClassLoader
name|cl
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|initialExtensions
parameter_list|,
name|ResourceManager
name|rm
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|String
index|[]
block|{
name|BUS_EXTENSION_RESOURCE
block|,
name|BUS_EXTENSION_RESOURCE_COMPAT
block|}
argument_list|,
name|cl
argument_list|,
name|initialExtensions
argument_list|,
name|rm
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ExtensionManagerImpl
parameter_list|(
name|String
name|resource
parameter_list|,
name|ClassLoader
name|cl
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|initialExtensions
parameter_list|,
name|ResourceManager
name|rm
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|String
index|[]
block|{
name|resource
block|}
argument_list|,
name|cl
argument_list|,
name|initialExtensions
argument_list|,
name|rm
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ExtensionManagerImpl
parameter_list|(
name|String
name|resources
index|[]
parameter_list|,
name|ClassLoader
name|cl
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|,
name|Object
argument_list|>
name|initialExtensions
parameter_list|,
name|ResourceManager
name|rm
parameter_list|,
name|Bus
name|b
parameter_list|)
block|{
name|loader
operator|=
name|cl
expr_stmt|;
name|bus
operator|=
name|b
expr_stmt|;
name|activated
operator|=
name|initialExtensions
expr_stmt|;
name|resourceManager
operator|=
name|rm
expr_stmt|;
name|ResourceResolver
name|extensionManagerResolver
init|=
operator|new
name|SinglePropertyResolver
argument_list|(
name|EXTENSIONMANAGER_PROPERTY_NAME
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
name|extensionManagerResolver
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
operator|new
name|ObjectTypeResolver
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|deferred
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Extension
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|load
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|final
name|void
name|load
parameter_list|(
name|String
name|resources
index|[]
parameter_list|)
block|{
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
for|for
control|(
name|String
name|resource
range|:
name|resources
control|)
block|{
name|load
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExtensionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|activateViaNS
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
name|Collection
argument_list|<
name|Extension
argument_list|>
name|extensions
init|=
name|deferred
operator|.
name|get
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|extensions
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Extension
name|e
range|:
name|extensions
control|)
block|{
name|loadAndRegister
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|extensions
operator|.
name|clear
argument_list|()
expr_stmt|;
name|deferred
operator|.
name|remove
argument_list|(
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|activateAll
parameter_list|()
block|{
while|while
condition|(
operator|!
name|deferred
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|activateViaNS
argument_list|(
name|deferred
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
specifier|final
name|void
name|load
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResources
argument_list|(
name|resource
argument_list|)
decl_stmt|;
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|loadFragment
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|void
name|loadFragment
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|List
argument_list|<
name|Extension
argument_list|>
name|extensions
init|=
operator|new
name|ExtensionFragmentParser
argument_list|()
operator|.
name|getExtensions
argument_list|(
name|is
argument_list|)
decl_stmt|;
for|for
control|(
name|Extension
name|e
range|:
name|extensions
control|)
block|{
name|processExtension
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|void
name|processExtension
parameter_list|(
name|Extension
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|isDeferred
argument_list|()
condition|)
block|{
name|loadAndRegister
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|namespaces
init|=
name|e
operator|.
name|getNamespaces
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|ns
range|:
name|namespaces
control|)
block|{
name|Collection
argument_list|<
name|Extension
argument_list|>
name|extensions
init|=
name|deferred
operator|.
name|get
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|extensions
condition|)
block|{
name|extensions
operator|=
operator|new
name|ArrayList
argument_list|<
name|Extension
argument_list|>
argument_list|()
expr_stmt|;
name|deferred
operator|.
name|put
argument_list|(
name|ns
argument_list|,
name|extensions
argument_list|)
expr_stmt|;
block|}
name|extensions
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|void
name|loadAndRegister
parameter_list|(
name|Extension
name|e
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|e
operator|.
name|getInterfaceName
argument_list|()
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getInterfaceName
argument_list|()
argument_list|)
condition|)
block|{
name|cls
operator|=
name|e
operator|.
name|loadInterface
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|activated
operator|&&
literal|null
operator|!=
name|cls
operator|&&
literal|null
operator|!=
name|activated
operator|.
name|get
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return;
block|}
name|Object
name|obj
init|=
name|e
operator|.
name|load
argument_list|(
name|loader
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|Configurer
name|configurer
init|=
call|(
name|Configurer
call|)
argument_list|(
name|activated
operator|.
name|get
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|configurer
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
comment|// let the object know for which namespaces it has been activated
name|ResourceResolver
name|namespacesResolver
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|e
operator|.
name|getNamespaces
argument_list|()
condition|)
block|{
name|namespacesResolver
operator|=
operator|new
name|SinglePropertyResolver
argument_list|(
name|ACTIVATION_NAMESPACES_PROPERTY_NAME
argument_list|,
name|e
operator|.
name|getNamespaces
argument_list|()
argument_list|)
expr_stmt|;
name|resourceManager
operator|.
name|addResourceResolver
argument_list|(
name|namespacesResolver
argument_list|)
expr_stmt|;
block|}
comment|// Since we need to support spring2.5 by removing @Resource("activationNamespaces")
comment|// Now we call the setActivationNamespaces method directly here
name|invokeSetterActivationNSMethod
argument_list|(
name|obj
argument_list|,
name|e
operator|.
name|getNamespaces
argument_list|()
argument_list|)
expr_stmt|;
name|ResourceInjector
name|injector
init|=
operator|new
name|ResourceInjector
argument_list|(
name|resourceManager
argument_list|)
decl_stmt|;
try|try
block|{
name|injector
operator|.
name|inject
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|injector
operator|.
name|construct
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
literal|null
operator|!=
name|namespacesResolver
condition|)
block|{
name|resourceManager
operator|.
name|removeResourceResolver
argument_list|(
name|namespacesResolver
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|null
operator|!=
name|activated
condition|)
block|{
if|if
condition|(
name|cls
operator|==
literal|null
condition|)
block|{
name|cls
operator|=
name|obj
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
name|activated
operator|.
name|put
argument_list|(
name|cls
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|ns
range|:
name|e
operator|.
name|getNamespaces
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|intf2Obj
init|=
name|namespaced
operator|.
name|get
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|intf2Obj
operator|==
literal|null
condition|)
block|{
name|intf2Obj
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|namespaced
operator|.
name|containsKey
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|namespaced
operator|.
name|put
argument_list|(
name|ns
argument_list|,
name|intf2Obj
argument_list|)
expr_stmt|;
block|}
block|}
name|intf2Obj
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getExtension
parameter_list|(
name|String
name|ns
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Collection
argument_list|<
name|Object
argument_list|>
name|nsExts
init|=
name|namespaced
operator|.
name|get
argument_list|(
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
name|nsExts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|o
range|:
name|nsExts
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|invokeSetterActivationNSMethod
parameter_list|(
name|Object
name|target
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Class
name|clazz
init|=
name|target
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|String
name|methodName
init|=
name|ACTIVATION_NAMESPACES_SETTER_METHOD_NAME
decl_stmt|;
while|while
condition|(
name|clazz
operator|!=
name|Object
operator|.
name|class
condition|)
block|{
name|Method
index|[]
name|methods
init|=
name|clazz
operator|.
name|getMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|methods
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Method
name|method
init|=
name|methods
index|[
name|i
index|]
decl_stmt|;
name|Class
name|params
index|[]
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
name|params
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|Class
name|paramType
init|=
name|params
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|paramType
operator|.
name|isInstance
argument_list|(
name|value
argument_list|)
condition|)
block|{
try|try
block|{
name|method
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
operator|new
name|Object
index|[]
block|{
name|value
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// do nothing here
block|}
return|return;
block|}
block|}
block|}
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

